CREATE TABLE IF NOT EXISTS admin (
    id           BIGSERIAL PRIMARY KEY,
    username     VARCHAR(50)  NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL
);

-- 2.2 Product
CREATE TABLE IF NOT EXISTS product (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    brand        VARCHAR(50)  NOT NULL,
    price        NUMERIC(12,2) NOT NULL CHECK (price > 0),
    stock        INT NOT NULL CHECK (stock >= 0)
);

-- 2.3 Customer
CREATE TABLE IF NOT EXISTS customer (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    phone        VARCHAR(20),
    email        VARCHAR(100),
    address      VARCHAR(255),
    CONSTRAINT uq_customer_email UNIQUE (email)
);

-- 2.4 Invoice
CREATE TABLE IF NOT EXISTS invoice (
    id           BIGSERIAL PRIMARY KEY,
    customer_id  BIGINT NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount NUMERIC(12,2) NOT NULL CHECK (total_amount >= 0),
    CONSTRAINT fk_invoice_customer
        FOREIGN KEY (customer_id) REFERENCES customer(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT
);

-- 2.5 Invoice Details
CREATE TABLE IF NOT EXISTS invoice_details (
    id           BIGSERIAL PRIMARY KEY,
    invoice_id   BIGINT NOT NULL,
    product_id   BIGINT NOT NULL,
    quantity     INT NOT NULL CHECK (quantity > 0),
    unit_price   NUMERIC(12,2) NOT NULL CHECK (unit_price > 0),
    CONSTRAINT fk_invdet_invoice
        FOREIGN KEY (invoice_id) REFERENCES invoice(id)
        ON UPDATE RESTRICT ON DELETE CASCADE,
    CONSTRAINT fk_invdet_product
        FOREIGN KEY (product_id) REFERENCES product(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT uq_invdet_invoice_product UNIQUE (invoice_id, product_id)
);
select *
from public.customer c;
delete
from public.customer
where id = 4;

-- product
INSERT INTO product (name, brand, price, stock) VALUES
('iPhone 15 Pro', 'Apple', 999.99, 25),
('Galaxy S24 Ultra', 'Samsung', 1199.99, 15),
('MacBook Pro M3', 'Apple', 1999.99, 10),
('Dell XPS 13', 'Dell', 1299.99, 12),
('PlayStation 5', 'Sony', 499.99, 30);


-- customer

INSERT INTO customer (name, phone, email, address) VALUES
('Nguyen Van An', '+84912345678', 'nguyenvanan@email.com', '123 Nguyen Trai, District 1, Ho Chi Minh City'),
('Tran Thi Binh', '+84908765432', 'tranthibinh@email.com', '456 Le Loi, Ba Dinh, Hanoi'),
('Le Quang Cuong', '+84934567890', 'lequangcuong@email.com', '789 Tran Phu, Hai Chau, Da Nang'),
('Pham Thi Dung', '+84976543210', 'phamthidung@email.com', '321 Bach Dang, Hai An, Hai Phong'),
('Hoang Minh Duc', '+84965432109', 'hoangminhduc@email.com', '654 Hung Vuong, Vinh, Nghe An');

-- Insert 5 sample records into invoice table
INSERT INTO invoice (customer_id, total_amount) VALUES
(1, 1999.98),
(2, 1199.99),
(3, 999.99),
(4, 1799.98),
(5, 499.99);


INSERT INTO invoice_details (invoice_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 999.99),  -- Invoice 1: iPhone 15 Pro for customer 1
(2, 2, 1, 1199.99), -- Invoice 2: Galaxy S24 Ultra for customer 2
(3, 3, 1, 1999.99), -- Invoice 3: MacBook Pro for customer 3
(4, 4, 1, 1299.99), -- Invoice 4: Dell XPS 13 for customer 4
(5, 5, 1, 499.99);  -- Invoice 5: PlayStation 5 for customer 5



SELECT setval('customer_id_seq', 1, false);
SELECT setval('product_id_seq', 1, false);
SELECT setval('invoice_id_seq', 1, false);
SELECT setval('invoice_details_id_seq', 1, false);