CREATE TABLE IF NOT EXISTS users
(
    id bigserial NOT NULL PRIMARY KEY,
    name text NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS products
(
    id bigserial NOT NULL PRIMARY KEY,

    name text NOT NULL UNIQUE,
    price  double precision NOT NULL
);
DELETE  FROM users;
DELETE  FROM products;

CREATE TABLE IF NOT EXISTS users_products
(
    user_id bigserial NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    product_id bigserial NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    price  double precision
);

CREATE INDEX products_name_idx ON products (name);
CREATE INDEX users_name_idx ON users (name);

SELECT * FROM pg_indexes WHERE tablename = 'products';




INSERT INTO users (name)
VALUES
    ('Person_1'),
    ('Person_2'),
    ('Person_3'),
    ('Person_4'),
    ('Person_5'),
    ('Person_6'),
    ('Person_7'),
    ('Person_8'),
    ('Person_9'),
    ('Person_10'),
    ('Person_11'),
    ('Person_12');

INSERT INTO products (name, price)
VALUES
    ('Product_1', 228.00),
    ('Product_2', 288.00),
    ('Product_3', 128.00),
    ('Product_4', 255.00),
    ('Product_5', 428.00),
    ('Product_6', 528.00),
    ('Product_7', 1928.00),
    ('Product_8', 128.00),
    ('Product_9', 33.00),
    ('Product_10', 200.00),
    ('Product_11', 800.00),
    ('Product_12', 2589.00),
    ('Product_13', 555.00),
    ('Product_14', 27700),
    ('Product_15', 333.00),
    ('Product_16', 628.00);