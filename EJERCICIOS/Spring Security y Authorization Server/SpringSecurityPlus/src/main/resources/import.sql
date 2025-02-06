-- Crear roles por defecto
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- Crear usuarios por defecto con sus roles
-- Asegúrate de que la tabla 'users' y 'user_roles' existen
--INSERT INTO users (username, password) VALUES ('admin', '{bcrypt}password');
--INSERT INTO users (username, password) VALUES ('user', '{bcrypt}password');

-- Asignar roles a usuarios
--INSERT INTO user_roles (user_id, role_id) VALUES (1,2);
--INSERT INTO user_roles (user_id, role_id) VALUES (2,1);


INSERT INTO products (name, price) VALUES ('Laptop HP', 850.50);
INSERT INTO products (name, price) VALUES ('Monitor Samsung 24"', 200.00);
INSERT INTO products (name, price) VALUES ('Teclado Mecánico Logitech', 120.99);
INSERT INTO products (name, price) VALUES ('Mouse Inalámbrico Logitech', 45.75);
INSERT INTO products (name, price) VALUES ('Silla Gamer', 320.00);
INSERT INTO products (name, price) VALUES ('Auriculares HyperX', 75.00);
INSERT INTO products (name, price) VALUES ('Smartphone Samsung Galaxy', 950.99);
INSERT INTO products (name, price) VALUES ('Tablet iPad', 780.00);
INSERT INTO products (name, price) VALUES ('Impresora Multifunción Epson', 150.49);
INSERT INTO products (name, price) VALUES ('Disco Duro SSD 1TB', 180.00);
