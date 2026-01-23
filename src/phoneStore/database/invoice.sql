CREATE OR REPLACE PROCEDURE sp_invoice_create(
    IN  p_customer_id BIGINT,
    IN  p_product_ids BIGINT[],
    IN  p_quantities  INT[],
    OUT o_invoice_id  BIGINT,
    OUT o_total       NUMERIC
)
LANGUAGE plpgsql
AS $$
DECLARE
    i INT;
    v_pid BIGINT;
    v_qty INT;
    v_price NUMERIC(12,2);
    v_stock INT;
BEGIN
    -- Validate customer
    IF NOT EXISTS (SELECT 1 FROM customer WHERE id = p_customer_id) THEN
        RAISE EXCEPTION 'CUSTOMER_NOT_FOUND: %', p_customer_id USING ERRCODE = 'P0001';
    END IF;

    -- Validate arrays
    IF p_product_ids IS NULL OR array_length(p_product_ids, 1) IS NULL OR array_length(p_product_ids, 1) = 0 THEN
        RAISE EXCEPTION 'EMPTY_PRODUCT_LIST' USING ERRCODE = 'P0001';
    END IF;

    IF array_length(p_product_ids, 1) <> array_length(p_quantities, 1) THEN
        RAISE EXCEPTION 'ARRAY_LENGTH_MISMATCH' USING ERRCODE = 'P0001';
    END IF;

    -- Create invoice first
    INSERT INTO invoice(customer_id, total_amount)
    VALUES (p_customer_id, 0)
    RETURNING id INTO o_invoice_id;

    o_total := 0;

    -- For each item
    FOR i IN 1..array_length(p_product_ids, 1) LOOP
        v_pid := p_product_ids[i];
        v_qty := p_quantities[i];

        IF v_qty IS NULL OR v_qty <= 0 THEN
            RAISE EXCEPTION 'INVALID_QUANTITY: product_id=% qty=%', v_pid, v_qty USING ERRCODE = 'P0001';
        END IF;

        -- Lock product row
        SELECT price, stock
        INTO v_price, v_stock
        FROM product
        WHERE id = v_pid
        FOR UPDATE;

        IF NOT FOUND THEN
            RAISE EXCEPTION 'PRODUCT_NOT_FOUND: %', v_pid USING ERRCODE = 'P0001';
        END IF;

        IF v_stock < v_qty THEN
            RAISE EXCEPTION 'INSUFFICIENT_STOCK: product_id=% stock=% qty=%', v_pid, v_stock, v_qty USING ERRCODE = 'P0001';
        END IF;

        -- Insert details (unit_price at time of purchase)
        INSERT INTO invoice_details(invoice_id, product_id, quantity, unit_price)
        VALUES (o_invoice_id, v_pid, v_qty, v_price);

        -- Update stock
        UPDATE product
        SET stock = stock - v_qty
        WHERE id = v_pid;

        o_total := o_total + (v_qty * v_price);
    END LOOP;

    -- Update invoice total
    UPDATE invoice
    SET total_amount = o_total
    WHERE id = o_invoice_id;
END;
$$;

-- function

/* Invoice summary list */
CREATE OR REPLACE FUNCTION fn_invoice_list_summary()
RETURNS TABLE (
    invoice_id BIGINT,
    customer_id BIGINT,
    customer_name VARCHAR,
    created_at TIMESTAMP,
    total_amount NUMERIC(12,2)
)
LANGUAGE sql
AS $$
    SELECT i.id, c.id, c.name, i.created_at, i.total_amount
    FROM invoice i
    JOIN customer c ON c.id = i.customer_id
    ORDER BY i.created_at DESC, i.id DESC;
$$;

/* Invoice details for 1 invoice */
CREATE OR REPLACE FUNCTION fn_invoice_get_details(p_invoice_id BIGINT)
RETURNS TABLE (
    detail_id BIGINT,
    product_id BIGINT,
    product_name VARCHAR,
    quantity INT,
    unit_price NUMERIC(12,2),
    line_total NUMERIC(12,2)
)
LANGUAGE sql
AS $$
    SELECT d.id,
           p.id,
           p.name,
           d.quantity,
           d.unit_price,
           (d.quantity * d.unit_price) AS line_total
    FROM invoice_details d
    JOIN product p ON p.id = d.product_id
    WHERE d.invoice_id = p_invoice_id
    ORDER BY d.id;
$$;

/* Search invoices by customer name + day */
CREATE OR REPLACE FUNCTION fn_invoice_search_by_customer_and_day(
    p_customer_keyword TEXT,
    p_day DATE
)
RETURNS TABLE (
    invoice_id BIGINT,
    customer_name VARCHAR,
    created_at TIMESTAMP,
    total_amount NUMERIC(12,2)
)
LANGUAGE sql
AS $$
    SELECT i.id, c.name, i.created_at, i.total_amount
    FROM invoice i
    JOIN customer c ON c.id = i.customer_id
    WHERE c.name ILIKE ('%' || p_customer_keyword || '%')
      AND i.created_at::date = p_day
    ORDER BY i.created_at DESC, i.id DESC;
$$;

/* Search invoices by customer name + month */
CREATE OR REPLACE FUNCTION fn_invoice_search_by_customer_and_month(
    p_customer_keyword TEXT,
    p_year INT,
    p_month INT
)
RETURNS TABLE (
    invoice_id BIGINT,
    customer_name VARCHAR,
    created_at TIMESTAMP,
    total_amount NUMERIC(12,2)
)
LANGUAGE sql
AS $$
    SELECT i.id, c.name, i.created_at, i.total_amount
    FROM invoice i
    JOIN customer c ON c.id = i.customer_id
    WHERE c.name ILIKE ('%' || p_customer_keyword || '%')
      AND EXTRACT(YEAR FROM i.created_at) = p_year
      AND EXTRACT(MONTH FROM i.created_at) = p_month
    ORDER BY i.created_at DESC, i.id DESC;
$$;

/* Search invoices by customer name + year */
CREATE OR REPLACE FUNCTION fn_invoice_search_by_customer_and_year(
    p_customer_keyword TEXT,
    p_year INT
)
RETURNS TABLE (
    invoice_id BIGINT,
    customer_name VARCHAR,
    created_at TIMESTAMP,
    total_amount NUMERIC(12,2)
)
LANGUAGE sql
AS $$
    SELECT i.id, c.name, i.created_at, i.total_amount
    FROM invoice i
    JOIN customer c ON c.id = i.customer_id
    WHERE c.name ILIKE ('%' || p_customer_keyword || '%')
      AND EXTRACT(YEAR FROM i.created_at) = p_year
    ORDER BY i.created_at DESC, i.id DESC;
$$;


-- revenue

CREATE OR REPLACE FUNCTION fn_revenue_by_day(p_day DATE)
RETURNS NUMERIC(12,2)
LANGUAGE sql
AS $$
    SELECT COALESCE(SUM(total_amount), 0)
    FROM invoice
    WHERE created_at::date = p_day;
$$;

CREATE OR REPLACE FUNCTION fn_revenue_by_month(p_year INT, p_month INT)
RETURNS NUMERIC(12,2)
LANGUAGE sql
AS $$
    SELECT COALESCE(SUM(total_amount), 0)
    FROM invoice
    WHERE EXTRACT(YEAR FROM created_at) = p_year
      AND EXTRACT(MONTH FROM created_at) = p_month;
$$;

CREATE OR REPLACE FUNCTION fn_revenue_by_year(p_year INT)
RETURNS NUMERIC(12,2)
LANGUAGE sql
AS $$
    SELECT COALESCE(SUM(total_amount), 0)
    FROM invoice
    WHERE EXTRACT(YEAR FROM created_at) = p_year;
$$;