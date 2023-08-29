-- Populate Tables

-- TABLE: USERS
-- Inserta usuarios en la base de datos
--
INSERT INTO users (id, username, password, enabled) VALUES (1, 'admin', '$2a$10$PdNeOP5NuvYVbKjeijKrL.DOWA5lqM8ExWl4aV4B62tr/A9AsqeKu', 1);
INSERT INTO users (id, username, password, enabled) VALUES (2, 'user', '$2a$10$yYBW41Km1tyLSMtWOkE7MurVT.PnF2p/U.0mtUF6hciKSf303A6d2', 1);

--  TABLE: AUTHORITIES
--    Inserta Roles de usuarios en la base de datos

INSERT INTO authorities (authority, user_id) VALUES ('ROLE_ADMIN', 1);
INSERT INTO authorities (authority, user_id) VALUES ('ROLE_USER', 1);
INSERT INTO authorities (authority, user_id) VALUES ('ROLE_USER', 2);

--  TABLE: Clients
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (1, 'Luis', 'Beroiza', 'luis.beroiza@email.com', '2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (2, 'Cata', 'Salazar', 'pequitas@email.com','2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (3, 'Monica', 'Godoy', 'ojos-verdes@email.com','2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (4, 'Lucia', 'Mora', 'rubia@email.com','2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (5, 'Lili', 'Fernandez', 'lili.fer@email.com', '2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (6, 'Elisabeth', 'Smith', 'es-gbr@email.com', '2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (7, 'Marta', 'Sanchez', 'ma-sa@email.com', '2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (8, 'Linus', 'Torvals', 'ltorvals@email.com', '2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (9, 'Carlo', 'Magno', 'cm@email.com', '1000-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (10, 'Jackie', 'Chan', 'jack-chan@email.com', '2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (11, 'David', 'Copperfield', 'magician@email.com', '2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (12, 'Samantha', 'Johansson', 'sam-jo@email.com', '2023-05-26', '2023-05-26', '');

-- Genera el ID autoincremental a partir del id maximo de la tabla
-- ALTER TABLE clients ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM clients);


--  TABLE: Products
INSERT INTO products (id, price, created_at, updated_at, description, name) VALUES  (1, 750, NOW(), NOW(), '35 inch Television wide screen', 'Panasonic LCD Screen');
INSERT INTO products (id, price, created_at, updated_at, description, name) VALUES  (2, 350, NOW(), NOW(), 'Digital Camera Sony DSC-W320B', 'Sony Camera');
INSERT INTO products (id, price, created_at, updated_at, description, name) VALUES  (3, 300, NOW(), NOW(), 'Apple Ipod Shuffle', 'Ipod');
INSERT INTO products (id, price, created_at, updated_at, description, name) VALUES  (4, 1500, NOW(), NOW(), 'Notebook Sony Vaio 15 inch', 'Sony Vaio');
INSERT INTO products (id, price, created_at, updated_at, description, name) VALUES  (5, 279.89, NOW(), NOW(), 'Hewlett Packard Multiprinter', 'Printer HP');
INSERT INTO products (id, price, created_at, updated_at, description, name) VALUES  (6, 1359.89, NOW(), NOW(), 'Bicycle Bianchi 17 inch wheels', 'Bianchi Bicycle');
INSERT INTO products (id, price, created_at, updated_at, description, name) VALUES  (7, 435, NOW(), NOW(), 'Desk for office', 'Mica Desk');

--  TABLE: Invoice
INSERT INTO invoices (id, client_id, created_at, updated_at, description, obs) VALUES (1, 1, NOW(), NOW(), 'Office Invoice', 'No obs');
INSERT INTO invoices (id, client_id, created_at, updated_at, description, obs) VALUES (2, 1, NOW(), NOW(), 'Bicycle Invoice', 'No obs needed');

--  TABLE: Invoice Items
INSERT INTO invoice_items (invoice_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO invoice_items (invoice_id, product_id, quantity) VALUES (1, 4, 3);
INSERT INTO invoice_items (invoice_id, product_id, quantity) VALUES (1, 5, 1);
INSERT INTO invoice_items (invoice_id, product_id, quantity) VALUES (1, 7, 6);
INSERT INTO invoice_items (invoice_id, product_id, quantity) VALUES (2, 6, 5);