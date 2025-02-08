INSERT INTO platos (nombre, precio, categoria) VALUES ('Pizza Margarita', 8.50, 'Italiana');
INSERT INTO platos (nombre, precio, categoria) VALUES ('Hamburguesa', 7.00, 'Americana');
INSERT INTO platos (nombre, precio, categoria) VALUES ('Sushi Roll', 12.50, 'Japonesa');
INSERT INTO platos (nombre, precio, categoria) VALUES ('Ensalada César', 6.75, 'Saludable');
INSERT INTO platos (nombre, precio, categoria) VALUES ( 'Pasta Carbonara', 9.20, 'Italiana');

INSERT INTO pedidos(cliente, fecha, total) VALUES ('Carlos Pérez', '2024-02-10T14:30:00', 15.50);
INSERT INTO pedidos (cliente, fecha, total) VALUES ('Ana Gómez', '2024-02-01T14:40:00', 20.00);
INSERT INTO pedidos (cliente, fecha, total) VALUES ('Luis Ramírez', '2024-02-10T14:50:00', 18.75);

-- Inserciones en la tabla pedido_plato usando SELECT
-- Insertar manualmente las relaciones
INSERT INTO pedidos_platos (pedido_id, plato_id) VALUES (1, 1);  -- Pedido 1 -> Pizza Margarita
INSERT INTO pedidos_platos (pedido_id, plato_id) VALUES (1, 2);  -- Pedido 1 -> Hamburguesa
INSERT INTO pedidos_platos (pedido_id, plato_id) VALUES (2, 3);  -- Pedido 2 -> Sushi Roll
INSERT INTO pedidos_platos (pedido_id, plato_id) VALUES (2, 4);  -- Pedido 2 -> Ensalada César
INSERT INTO pedidos_platos (pedido_id, plato_id) VALUES (3, 5);  -- Pedido 3 -> Pasta Carbonara
INSERT INTO pedidos_platos (pedido_id, plato_id) VALUES (3, 1);  -- Pedido 3 -> Pizza Margarita

