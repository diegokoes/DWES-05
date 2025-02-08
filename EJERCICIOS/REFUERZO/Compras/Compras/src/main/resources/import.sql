INSERT INTO productos (nombre, precio, categoria) VALUES ('Laptop', 1200.00, 'Electrónica');
INSERT INTO productos (nombre, precio, categoria) VALUES ('Smartphone', 800.00, 'Electrónica');
INSERT INTO productos (nombre, precio, categoria) VALUES ('Teclado Mecánico', 150.00, 'Accesorios');
INSERT INTO productos (nombre, precio, categoria) VALUES ('Monitor 24"', 220.00, 'Periféricos');
INSERT INTO productos (nombre, precio, categoria) VALUES ('Auriculares Inalámbricos', 95.00, 'Audio');

INSERT INTO compras(cliente, fecha, total) VALUES ('Carlos Pérez', '2024-02-10T14:30:00', 1350.00);
INSERT INTO compras(cliente, fecha, total) VALUES ('Ana Gómez', '2024-02-01T14:40:00', 1020.00);
INSERT INTO compras(cliente, fecha, total) VALUES ('Luis Ramírez', '2024-02-10T14:50:00', 890.00);

-- Inserciones en la tabla compra_producto usando SELECT
-- Insertar manualmente las relaciones
INSERT INTO compra_producto (compra_id, producto_id) VALUES (1, 1);  -- Compra 1 -> Laptop
INSERT INTO compra_producto (compra_id, producto_id) VALUES (1, 3);  -- Compra 1 -> Teclado Mecánico
INSERT INTO compra_producto (compra_id, producto_id) VALUES (2, 2);  -- Compra 2 -> Smartphone
INSERT INTO compra_producto (compra_id, producto_id) VALUES (2, 4);  -- Compra 2 -> Monitor 24"
INSERT INTO compra_producto (compra_id, producto_id) VALUES (3, 5);  -- Compra 3 -> Auriculares Inalámbricos
INSERT INTO compra_producto (compra_id, producto_id) VALUES (3, 1);  -- Compra 3 -> Laptop