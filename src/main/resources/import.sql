/* Populate Tables */

INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (1, 'Luis', 'Beroiza', 'luis.beroiza@email.com', '2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (2, 'Cata', 'Salazar', 'pequitas@email.com','2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (3, 'Monica', 'Godoy', 'ojos-verdes@email.com','2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (4, 'Lucia', 'Mora', 'rubia@email.com','2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (5, 'Lili', 'Fernandez', 'lili.fer@email.com', '2023-05-26', '2023-05-26', '');
INSERT INTO clients (id, name, surname, email, created_at, updated_at, image) VALUES (6, 'Elisabeth', 'Smith', 'es-gbr@email.com', '2023-05-26', '2023-05-26', '');

/* Genera el ID autoincremental a partir del id maximo de la tabla */
/* ALTER TABLE clients ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM clients); */


