CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE rol (
  id_role VARCHAR(50) PRIMARY KEY DEFAULT gen_random_uuid(),
  nombre VARCHAR(30) NOT NULL,
  descripcion TEXT
);

CREATE TABLE Usuario (
    id_usuario VARCHAR(50) PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    correo_electronico VARCHAR(80) NOT NULL,
    contrasena VARCHAR(150) NOT NULL,
    id_role VARCHAR(50) NOT NULL,
    documento_identidad VARCHAR(30) NOT NULL,
    salario_base DECIMAL(10, 2) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    fecha_nacimiento DATE,
    CONSTRAINT documento_identidad_unique_constraint UNIQUE (documento_identidad),
    CONSTRAINT correo_electronico_unique_constraint UNIQUE (correo_electronico),
    FOREIGN KEY (id_role) REFERENCES rol(id_role)
);