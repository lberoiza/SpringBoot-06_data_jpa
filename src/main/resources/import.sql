/* Populate Tables */

/* TABLE: Clients */
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (1, 'Luis', 'Beroiza', 'luis.beroiza@email.com', '2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (2, 'Cata', 'Salazar', 'pequitas@email.com','2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (3, 'Monica', 'Godoy', 'ojos-verdes@email.com','2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (4, 'Lucia', 'Mora', 'rubia@email.com','2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (5, 'Lili', 'Fernandez', 'lili.fer@email.com', '2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (6, 'Elisabeth', 'Smith', 'es-gbr@email.com', '2023-05-26', '2023-05-26', '');

/* Genera el ID autoincremental a partir del id maximo de la tabla */
/* ALTER TABLE clients ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM clients); */


/* TABLE: Products */
INSERT INTO products (price, created_at, updated_at, description, name) VALUES  (750, NOW(), NOW(), '35 inch Television wide screen', 'Panasonic LCD Screen');
INSERT INTO products (price, created_at, updated_at, description, name) VALUES  (350, NOW(), NOW(), 'Digital Camera Sony DSC-W320B', 'Sony Camera');
INSERT INTO products (price, created_at, updated_at, description, name) VALUES  (300, NOW(), NOW(), 'Apple Ipod Shuffle', 'Ipod');
INSERT INTO products (price, created_at, updated_at, description, name) VALUES  (1500, NOW(), NOW(), 'Notebook Sony Vaio 15 inch', 'Sony Vaio');
INSERT INTO products (price, created_at, updated_at, description, name) VALUES  (279.89, NOW(), NOW(), 'Hewlett Packard Multiprinter', ' Printer HP');
INSERT INTO products (price, created_at, updated_at, description, name) VALUES  (1359.89, NOW(), NOW(), 'Bicycle Bianchi 17 inch wheels', 'Bianchi Bicycle');
INSERT INTO products (price, created_at, updated_at, description, name) VALUES  (435, NOW(), NOW(), 'Desk for office', 'Mica Desk');