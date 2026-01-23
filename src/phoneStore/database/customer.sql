-- customer - insert
CREATE OR REPLACE PROCEDURE sp_customer_insert(
    IN  p_name    VARCHAR,
    IN  p_phone   VARCHAR,
    IN  p_email   VARCHAR,
    IN  p_address VARCHAR,
    OUT o_id      BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO customer(name, phone, email, address)
    VALUES (p_name, p_phone, p_email, p_address)
    RETURNING id INTO o_id;
END;
$$;



  -- CUSTOMER - UPDATE (trừ ID)

CREATE OR REPLACE PROCEDURE sp_customer_update(
    IN p_id      BIGINT,
    IN p_name    VARCHAR,
    IN p_phone   VARCHAR,
    IN p_email   VARCHAR,
    IN p_address VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM customer WHERE id = p_id) THEN
        RAISE EXCEPTION 'CUSTOMER_NOT_FOUND: %', p_id USING ERRCODE = 'P0001';
    END IF;

    UPDATE customer
    SET name    = p_name,
        phone   = p_phone,
        email   = p_email,
        address = p_address
    WHERE id = p_id;
END;
$$;

-- Test the procedure with a sample call
CALL sp_customer_update(1, 'Updated Name', '0987654321', 'updated@example.com', 'Updated Address 123');




  -- CUSTOMER - DELETE
  -- Rule : nếu customer đã có invoice thì không cho phép xóa

CREATE OR REPLACE PROCEDURE sp_customer_delete(IN p_id BIGINT)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM customer WHERE id = p_id) THEN
        RAISE EXCEPTION 'CUSTOMER_NOT_FOUND: %', p_id USING ERRCODE = 'P0001';
    END IF;

    IF EXISTS (SELECT 1 FROM invoice WHERE customer_id = p_id) THEN
        RAISE EXCEPTION 'CUSTOMER_HAS_INVOICE_CANNOT_DELETE: %', p_id USING ERRCODE = 'P0001';
    END IF;

    DELETE FROM customer WHERE id = p_id;
END;
$$;

call sp_customer_delete(7);





-- function

CREATE OR REPLACE FUNCTION fn_customer_find_all()
RETURNS TABLE (
    id BIGINT, name VARCHAR, phone VARCHAR, email VARCHAR, address VARCHAR
)
LANGUAGE sql
AS $$
    SELECT c.id, c.name, c.phone, c.email, c.address
    FROM customer c
    ORDER BY c.id;
$$;

select *
from fn_customer_find_all();