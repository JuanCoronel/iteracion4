CREATE SEQUENCE ALHOANDES_SEQUENCE;

CREATE TABLE A_Operadores (
	id_operador INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(50) NOT NULL
);


CREATE TABLE A_Alojamientos (
	id_alojamiento INT PRIMARY KEY NOT NULL,
	operador INT NOT NULL,
    capacidad INT NOT NULL,
    precio INT  NOT NULL,
    relacion_universidad VARCHAR(30) NOT NULL,
    horarios_recepcion VARCHAR(60),
    precio_administracion INT NOT NULL,
    precio_seguro INT NOT NULL,
    nombre_alojamiento VARCHAR(30) NOT NULL,
    CONSTRAINT fk_operador_alojamiento FOREIGN KEY (operador) REFERENCES A_Operadores (id_operador)
);

CREATE TABLE A_Usuarios (
	nombre VARCHAR(50),
    cedula INT PRIMARY KEY NOT NULL,
    relacion_universidad VARCHAR(60) NOT NULL
);

CREATE TABLE A_Reservas (
	id_reserva INT PRIMARY KEY NOT NULL,
    tipo_contrato VARCHAR(20),
    fecha_llegada DATE NOT NULL,
    fecha_salida DATE NOT NULL,
    costo INT NOT NULL,
    usuario INT NOT NULL,
    alojamiento_reservado INT NOT NULL,
    CONSTRAINT fk_usuario_reserva FOREIGN KEY (usuario) REFERENCES A_Usuarios (cedula),
    CONSTRAINT fk_alojamiento_reserva FOREIGN KEY (alojamiento_reservado) REFERENCES A_Alojamientos (id_alojamiento)
);



CREATE TABLE A_Propuestas (
 	id_propuesta INT PRIMARY KEY NOT NULL,
    nombre_alojamiento VARCHAR(100) NOT NULL,
    info_alojamiento VARCHAR(200)  
);

CREATE TABLE A_Servicios (
	id_servicio INT PRIMARY KEY NOT NULL,
    nombre_servicio VARCHAR(60) NOT NULL,
    alojamiento INT NOT NULL,
    CONSTRAINT fk_alojamiento_servicio FOREIGN KEY (alojamiento) REFERENCES A_Alojamientos (id_alojamiento)
);

CREATE TABLE A_Contratos (
	id_contrato INT PRIMARY KEY NOT NULL,
    contratista VARCHAR(30),
    alojamiento INT NOT NULL,
    registro_legal VARCHAR(100),
    CONSTRAINT fk_alojamiento_contrato FOREIGN KEY (alojamiento) REFERENCES A_Alojamientos (id_alojamiento)
);

CREATE TABLE A_Particulares (
	id_particular INT PRIMARY KEY NOT NULL,
    nombre_particular VARCHAR(30) NOT NULL,
    CONSTRAINT fk_alojamiento_particular FOREIGN KEY (id_particular) REFERENCES A_Alojamientos (id_alojamiento)  
);

CREATE TABLE A_ResidenciasU(
	id_residencia INT PRIMARY KEY NOT NULL,
    nombre_residencia VARCHAR(30) NOT NULL,
    registro_legal VARCHAR(200) NOT NULL,
    CONSTRAINT fk_alojamiento_residencia FOREIGN KEY (id_residencia) REFERENCES A_Alojamientos (id_alojamiento)  
);

CREATE TABLE A_Hoteles(
	id_hotel INT PRIMARY KEY NOT NULL,
    nombre_hotel VARCHAR(30) NOT NULL,
    registro_legal VARCHAR(200) NOT NULL,
    CONSTRAINT fk_alojamiento_hotel FOREIGN KEY (id_hotel) REFERENCES A_Alojamientos (id_alojamiento)  
);

CREATE TABLE A_Hostales(
	id_hostal INT PRIMARY KEY NOT NULL,
    nombre_hostal VARCHAR(30) NOT NULL,
    registro_legal VARCHAR(200) NOT NULL,
    CONSTRAINT fk_alojamiento_hostal FOREIGN KEY (id_hostal) REFERENCES A_Alojamientos (id_alojamiento)  
);

commit;
