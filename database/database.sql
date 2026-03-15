DROP DATABASE IF EXISTS LogicTrackSA;
CREATE DATABASE IF NOT EXISTS LogicTrackSA;
USE LogicTrackSA;

-- TABLA PERSONA --
CREATE TABLE persona (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    documento VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'EMPLEADO') NOT NULL DEFAULT 'EMPLEADO'
);

-- TABLA BODEGAS --
CREATE TABLE bodegas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    capacidad INT NOT NULL,
    encargado_id INT NOT NULL,
    CONSTRAINT fk_bodega_encargado
        FOREIGN KEY (encargado_id)
        REFERENCES persona(id)
);

-- TABLA PRODUCTOS --
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    bodega_id INT NOT NULL,
    CONSTRAINT fk_producto_bodega
        FOREIGN KEY (bodega_id)
        REFERENCES bodegas(id)
);

-- TABLA MOVIMIENTOS --
CREATE TABLE movimientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    tipo_movimiento ENUM('ENTRADA', 'SALIDA', 'TRANSFERENCIA') NOT NULL,
    descripcion VARCHAR(255),
    usuario_id INT NOT NULL,
    bodega_origen_id INT,
    bodega_destino_id INT,
    CONSTRAINT fk_movimiento_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES persona(id),
    CONSTRAINT fk_movimiento_bodega_origen
        FOREIGN KEY (bodega_origen_id)
        REFERENCES bodegas(id),
    CONSTRAINT fk_movimiento_bodega_destino
        FOREIGN KEY (bodega_destino_id)
        REFERENCES bodegas(id)
);

-- TABLA MOVIMIENTO_DETALLES -- 
CREATE TABLE movimiento_detalles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movimiento_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    CONSTRAINT fk_detalle_movimiento
        FOREIGN KEY (movimiento_id)
        REFERENCES movimientos(id),
    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (producto_id)
        REFERENCES productos(id),
    CONSTRAINT unique_movimiento_producto
        UNIQUE (movimiento_id, producto_id)
);

-- TABLA AUDITORIAS --
CREATE TABLE auditorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    entidad VARCHAR(100) NOT NULL,
    operacion ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    fecha DATETIME NOT NULL,
    usuario_id INT NOT NULL,
    valor_anterior TEXT,
    valor_nuevo TEXT,
    CONSTRAINT fk_auditoria_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES persona(id)
);