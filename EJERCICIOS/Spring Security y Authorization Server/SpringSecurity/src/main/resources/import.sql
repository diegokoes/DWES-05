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
