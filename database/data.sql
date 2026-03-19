-- LOGITRACK S.A - DATA.SQL

-- Las contraseñas están encriptadas con BCrypt
-- Contraseña real de todos los usuarios: admin123

USE logitrack;

-- PERSONAS
INSERT INTO persona (nombre, apellido, documento, email, password, rol) VALUES
('Admin',    'Principal',  '123456789',  'admin@logitrack.com',    '$2a$10$vvMIcnwci64e3FObomK4b.dvfahMLFlmetyV3kh8MVs6zvyGIDZRm', 'ADMIN'),
('Carlos',   'Ramírez',    '987654321',  'carlos@logitrack.com',   '$2a$10$vvMIcnwci64e3FObomK4b.dvfahMLFlmetyV3kh8MVs6zvyGIDZRm', 'EMPLEADO'),
('Sofía',    'Torres',     '456789123',  'sofia@logitrack.com',    '$2a$10$vvMIcnwci64e3FObomK4b.dvfahMLFlmetyV3kh8MVs6zvyGIDZRm', 'EMPLEADO'),
('Miguel',   'Hernández',  '321654987',  'miguel@logitrack.com',   '$2a$10$vvMIcnwci64e3FObomK4b.dvfahMLFlmetyV3kh8MVs6zvyGIDZRm', 'ADMIN');

-- BODEGAS
INSERT INTO bodegas (nombre, ubicacion, capacidad, encargado_id) VALUES
('Bodega Central',    'Piso 1 - Zona A',      1000,   1),
('Bodega Norte',      'Piso 2 - Zona B',      500,    2),
('Bodega Sur',        'Planta Baja - Zona C', 750,    3),
('Bodega Temporal',   'Exterior - Zona D',    300,    4);

-- PRODUCTOS
INSERT INTO productos (nombre, categoria, stock, precio, bodega_id) VALUES
('Caja de cartón grande',   'Embalaje',     150,    2500.00,    1),
('Caja de cartón pequeña',  'Embalaje',     200,    1200.00,    1),
('Pallet de madera',        'Logística',    80,     15000.00,   1),
('Cinta adhesiva',          'Embalaje',     5,      800.00,     2),
('Stretch film',            'Embalaje',     40,     3500.00,    2),
('Etiquetas adhesivas',     'Papelería',    3,      500.00,     3),
('Marcador industrial',     'Papelería',    25,     1500.00,    3),
('Guantes de seguridad',    'Seguridad',    60,     4000.00,    4),
('Casco de seguridad',      'Seguridad',    8,      25000.00,   4),
('Chaleco reflectivo',      'Seguridad',    2,      18000.00,   4);

-- MOVIMIENTOS
INSERT INTO movimientos (fecha, tipo_movimiento, descripcion, usuario_id, bodega_origen_id, bodega_destino_id) VALUES
('2026-03-01 08:30:00', 'ENTRADA', 'Reposición inicial de cajas',      1, NULL, 1),
('2026-03-02 09:15:00', 'ENTRADA', 'Ingreso de materiales de seguridad', 2, NULL, 4),
('2026-03-05 10:00:00', 'SALIDA',  'Despacho pedido #001',              2, 1,    NULL),
('2026-03-08 11:30:00', 'ENTRADA', 'Reposición pallets',                1, NULL, 1),
('2026-03-10 14:00:00', 'SALIDA',  'Despacho pedido #002',              3, 2,    NULL),
('2026-03-12 08:00:00', 'ENTRADA', 'Ingreso papelería',                 1, 1, 3),
('2026-03-15 16:00:00', 'SALIDA',  'Despacho pedido #003',              2, 4,    NULL);

-- MOVIMIENTO DETALLE
INSERT INTO movimiento_detalles (movimiento_id, producto_id, cantidad) VALUES
(1, 1, 50),
(1, 2, 80),
(2, 8, 20),
(2, 9, 5),
(3, 1, 10),
(3, 3, 5),
(4, 3, 20),
(5, 4, 2),
(5, 5, 8),
(6, 6, 10),
(6, 7, 5),
(7, 9, 2),
(7, 10, 3);

-- AUDITORIAS
INSERT INTO auditorias (entidad, operacion, fecha, usuario_id, valor_anterior, valor_nuevo) VALUES
('Producto', 'CREAR',        '2026-03-01 08:30:00', 1, NULL, 'Se creó el producto: Caja de cartón grande'),
('Producto', 'CREAR',        '2026-03-01 08:31:00', 1, NULL,                           'Se creó el producto: Caja de cartón pequeña'),
('Producto', 'CREAR',        '2026-03-01 08:32:00', 1, NULL,                           'Se creó el producto: Pallet de madera'),
('Bodega', 'CREAR',        '2026-03-01 08:00:00', 1, NULL,                           'Se creó la bodega: Bodega Central'),
('Bodega', 'CREAR',        '2026-03-01 08:05:00', 1, NULL,                           'Se creó la bodega: Bodega Norte'),
('Movimiento', 'CREAR',        '2026-03-01 08:30:00', 1, NULL,                           'Movimiento ENTRADA — Productos: Caja de cartón grande (50), Caja de cartón pequeña (80)'),
('Movimiento', 'CREAR',        '2026-03-05 10:00:00', 2, NULL,                           'Movimiento SALIDA — Productos: Caja de cartón grande (10), Pallet de madera (5)'),
('Producto', 'ACTUALIZAR',   '2026-03-08 11:30:00', 1, 'stock: 60',                    'Se actualizó el producto: Pallet de madera'),
('Bodega', 'ELIMINAR',     '2026-03-10 09:00:00', 1, 'Bodega Vieja - stock mínimo',  NULL);