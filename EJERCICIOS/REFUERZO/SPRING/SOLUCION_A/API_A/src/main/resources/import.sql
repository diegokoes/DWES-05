INSERT INTO Organizadores (codigo, nombre, descripcion, pais_origen) VALUES ('ORG001', 'LiveNation', 'Empresa líder en la organización de eventos musicales', 'Estados Unidos');
INSERT INTO Organizadores (codigo, nombre, descripcion, pais_origen) VALUES('ORG002', 'Eventos España', 'Especialistas en conferencias y seminarios', 'España');
INSERT INTO Organizadores (codigo, nombre, descripcion, pais_origen) VALUES ('ORG003', 'Expo Global', 'Organiza ferias y exposiciones internacionales', 'Reino Unido');

INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV001', 'Concierto de Rock', 'Madrid', '2025-06-10 20:30:00', 120.00, 'ORG001');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV002', 'Congreso de Tecnología', 'Barcelona', '2025-07-15 09:00:00', 200.50, 'ORG002');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV003', 'Feria del Automóvil', 'Londres', '2025-09-20 14:00:00', 50.00, 'ORG003');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV004', 'Exposición de Arte', 'París', '2025-08-05 10:00:00', 80.00, 'ORG001');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV005', 'Festival de Jazz', 'Nueva York', '2025-10-12 18:30:00', 150.00, 'ORG002');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV006', 'Maratón Internacional', 'Berlín', '2025-11-22 07:00:00', 40.00, 'ORG003');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV007', 'Simposio de Medicina', 'Roma', '2025-12-03 09:30:00', 300.00, 'ORG003');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV008', 'Convención de Anime', 'Tokio', '2026-01-15 11:00:00', 75.00, 'ORG002');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV009', 'Torneo de Ajedrez', 'Moscú', '2026-02-20 15:00:00', 25.00, 'ORG002');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV010', 'Cumbre de Negocios', 'Pekín', '2026-03-10 08:00:00', 500.00, 'ORG001');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV011', 'Competencia de Drones', 'Sídney', '2026-04-05 13:45:00', 90.00, 'ORG001');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV012', 'Festival Gastronómico', 'México DF', '2026-05-18 17:30:00', 60.00, 'ORG002');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV013', 'Encuentro de Startups', 'San Francisco', '2026-06-25 10:15:00', 250.00, 'ORG003');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV014', 'Torneo de eSports', 'Seúl', '2026-07-30 19:00:00', 120.00, 'ORG002');
INSERT INTO Eventos (codigo, nombre, lugar, fecha, precio, organizador_codigo) VALUES ('EV015', 'Foro de Educación', 'Madrid', '2026-08-12 09:00:00', 70.00, 'ORG001');


INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users (username, password) VALUES ('admin', '$2a$10$Ey/H5tNIopwhtVYXQ76Ms.oeiol3A4NiG3/HJekFyKkmgLVbR1n1C');
INSERT INTO users (username, password) VALUES ('user', '$2a$10$XVgKh.17he10CTo6Av57xOlSpnQWYxVJyshfkxjPKFLGTfth7FQZy');

INSERT INTO users_roles (role_id,user_id) VALUES(2,1);
INSERT INTO users_roles (role_id,user_id) VALUES(1,2);
