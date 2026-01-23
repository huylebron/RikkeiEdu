CREATE OR REPLACE PROCEDURE sp_product_insert(
    IN  p_name  VARCHAR,
    IN  p_brand VARCHAR,
    IN  p_price NUMERIC,
    IN  p_stock INT,
    OUT o_id    BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO product(name, brand, price, stock)
    VALUES (p_name, p_brand, p_price, p_stock)
    RETURNING id INTO o_id;
END;
$$;

call sp_product_insert('iPhone 13', 'Apple', 999.99, 10, null);

select *
from public.product p;


  --  PRODUCT - UPDATE

CREATE OR REPLACE PROCEDURE sp_product_update(
    IN p_id    BIGINT,
    IN p_name  VARCHAR,
    IN p_brand VARCHAR,
    IN p_price NUMERIC,
    IN p_stock INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM product WHERE id = p_id) THEN
        RAISE EXCEPTION 'PRODUCT_NOT_FOUND: %', p_id USING ERRCODE = 'P0001';
    END IF;

    UPDATE product
    SET name  = p_name,
        brand = p_brand,
        price = p_price,
        stock = p_stock
    WHERE id = p_id;
END;
$$;

-- product - delete
CREATE OR REPLACE PROCEDURE sp_product_delete(IN p_id BIGINT)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM product WHERE id = p_id) THEN
        RAISE EXCEPTION 'PRODUCT_NOT_FOUND: %', p_id USING ERRCODE = 'P0001';
    END IF;

    IF EXISTS (SELECT 1 FROM invoice_details WHERE product_id = p_id) THEN
        RAISE EXCEPTION 'PRODUCT_IN_INVOICE_DETAILS_CANNOT_DELETE: %', p_id USING ERRCODE = 'P0001';
    END IF;

    DELETE FROM product WHERE id = p_id;
END;
$$;

-- function


/* Product by id */
CREATE OR REPLACE FUNCTION fn_product_find_by_id(p_id BIGINT)
RETURNS TABLE (
    id BIGINT, name VARCHAR, brand VARCHAR, price NUMERIC(12,2), stock INT
)
LANGUAGE sql
AS $$
    SELECT p.id, p.name, p.brand, p.price, p.stock
    FROM product p
    WHERE p.id = p_id;
$$;

/* Product by brand (near match) */
CREATE OR REPLACE FUNCTION fn_product_search_by_brand(p_keyword TEXT)
RETURNS TABLE (
    id BIGINT, name VARCHAR, brand VARCHAR, price NUMERIC(12,2), stock INT
)
LANGUAGE sql
AS $$
    SELECT p.id, p.name, p.brand, p.price, p.stock
    FROM product p
    WHERE p.brand ILIKE ('%' || p_keyword || '%')
    ORDER BY p.id;
$$;

/* Product by price range */
CREATE OR REPLACE FUNCTION fn_product_search_by_price_range(p_min NUMERIC, p_max NUMERIC)
RETURNS TABLE (
    id BIGINT, name VARCHAR, brand VARCHAR, price NUMERIC(12,2), stock INT
)
LANGUAGE sql
AS $$
    SELECT p.id, p.name, p.brand, p.price, p.stock
    FROM product p
    WHERE p.price BETWEEN p_min AND p_max
    ORDER BY p.price, p.id;
$$;

/* Product by name + stock (near match name) */
CREATE OR REPLACE FUNCTION fn_product_search_by_name_with_stock(p_keyword TEXT)
RETURNS TABLE (
    id BIGINT, name VARCHAR, brand VARCHAR, price NUMERIC(12,2), stock INT
)
LANGUAGE sql
AS $$
    SELECT p.id, p.name, p.brand, p.price, p.stock
    FROM product p
    WHERE p.name ILIKE ('%' || p_keyword || '%')
    ORDER BY p.stock DESC, p.id;
$$;

create or replace function fn_product_find_all()
returns table (
    id BIGINT, name VARCHAR, brand VARCHAR, price NUMERIC(12,2), stock INT
)
language sql
as $$
    SELECT p.id, p.name, p.brand, p.price, p.stock
    FROM product p
    ORDER BY p.id;
$$;

SELECT * FROM fn_product_find_all()