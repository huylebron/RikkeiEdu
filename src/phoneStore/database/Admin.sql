
   --ADMIN - INSERT

CREATE OR REPLACE PROCEDURE sp_admin_insert(
    IN  p_username VARCHAR,
    IN  p_password VARCHAR,
    OUT o_id       BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO admin(username, password)
    VALUES (p_username, p_password)
    RETURNING id INTO o_id;
END;
$$;

call sp_admin_insert('admin', '123456', null);


   --ADMIN - UPDATE PASSWORD

CREATE OR REPLACE PROCEDURE sp_admin_update_password(
    IN p_id BIGINT,
    IN p_password VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM admin WHERE id = p_id) THEN
        RAISE EXCEPTION 'ADMIN_NOT_FOUND: %', p_id USING ERRCODE = 'P0001';
    END IF;

    UPDATE admin SET password = p_password WHERE id = p_id;
END;
$$;

-- fuction login

CREATE OR REPLACE FUNCTION fn_admin_find_by_username(p_username TEXT)
RETURNS TABLE (
    id BIGINT,
    username VARCHAR,
    password VARCHAR
)
LANGUAGE sql
AS $$
    SELECT a.id, a.username, a.password
    FROM admin a
    WHERE a.username = p_username
    LIMIT 1;
$$;

select *
from fn_admin_find_by_username('admin');


SELECT username, password
FROM admin
WHERE username = 'admin';