--RF10: En caso de que se quiera agrupar por informacion del usuario--
-- parametros: id alojamiento, fecha.
--
SELECT us.*, COUNT(aloj.id_alojamiento) as cantidad_reservas_usuario
FROM A_USUARIOS us INNER JOIN A_RESERVAS res
ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on
res.alojamiento_reservado = aloj.id_alojamiento 
WHERE res.alojamiento_reservado = 0
AND res.fecha_llegada BETWEEN TO_DATE('2023-01-01', 'YYYY-MM-DD') AND TO_DATE('2023-12-31', 'YYYY-MM-DD')

GROUP BY us.nombre, us.cedula, us.relacion_universidad 
ORDER BY us.nombre, us.cedula, us.relacion_universidad
;

--RF10: En caso de que se quiera agrupar por oferta de alojamiento--
-- parametros: id alojamiento, fecha.
--
SELECT aloj.id_alojamiento as alojamiento, COUNT(distinct us.nombre) as cantidad_reservas_usuario
FROM A_USUARIOS us INNER JOIN A_RESERVAS res
ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on
res.alojamiento_reservado = aloj.id_alojamiento 
WHERE res.alojamiento_reservado = 0
AND res.fecha_llegada BETWEEN TO_DATE('2023-01-01', 'YYYY-MM-DD') AND TO_DATE('2023-12-31', 'YYYY-MM-DD')

GROUP BY aloj.id_alojamiento
ORDER BY aloj.id_alojamiento
;

--RF10: En caso de que se quiera agrupar por tipo de alojamiento--
-- parametros: id alojamiento, fecha.
--
SELECT aloj.nombre_alojamiento as tipo_alojamiento, COUNT(distinct us.nombre) as cantidad_reservas_usuario
FROM A_USUARIOS us INNER JOIN A_RESERVAS res
ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on
res.alojamiento_reservado = aloj.id_alojamiento 
WHERE res.alojamiento_reservado = 0
AND res.fecha_llegada BETWEEN TO_DATE('2023-01-01', 'YYYY-MM-DD') AND TO_DATE('2023-12-31', 'YYYY-MM-DD')

GROUP BY aloj.nombre_alojamiento
ORDER BY aloj.nombre_alojamiento
;

--RF11------------------------------------------>

--RF11: En caso de que se quiera agrupar por informacion del usuario--
-- parametros: id alojamiento, fecha.
--
SELECT us.*, COUNT(aloj.id_alojamiento) as cantidad_reservas_usuario
FROM A_USUARIOS us INNER JOIN A_RESERVAS res
ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on
res.alojamiento_reservado = aloj.id_alojamiento 
WHERE res.alojamiento_reservado = 0
AND res.fecha_llegada NOT BETWEEN TO_DATE('2023-01-01', 'YYYY-MM-DD') AND TO_DATE('2023-12-31', 'YYYY-MM-DD')

GROUP BY us.nombre, us.cedula, us.relacion_universidad 
ORDER BY us.nombre, us.cedula, us.relacion_universidad
;

--RF11: En caso de que se quiera agrupar por oferta de alojamiento--
-- parametros: id alojamiento, fecha.
--
SELECT aloj.id_alojamiento as alojamiento, COUNT(distinct us.nombre) as cantidad_reservas_usuario
FROM A_USUARIOS us INNER JOIN A_RESERVAS res
ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on
res.alojamiento_reservado = aloj.id_alojamiento 
WHERE res.alojamiento_reservado = 0
AND res.fecha_llegada NOT BETWEEN TO_DATE('2023-01-01', 'YYYY-MM-DD') AND TO_DATE('2023-12-31', 'YYYY-MM-DD')

GROUP BY aloj.id_alojamiento
ORDER BY aloj.id_alojamiento
;

--RF11: En caso de que se quiera agrupar por tipo de alojamiento--
-- parametros: id alojamiento, fecha.
--
SELECT aloj.nombre_alojamiento as tipo_alojamiento, COUNT(distinct us.nombre) as cantidad_reservas_usuario
FROM A_USUARIOS us INNER JOIN A_RESERVAS res
ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on
res.alojamiento_reservado = aloj.id_alojamiento 
WHERE res.alojamiento_reservado = 0
AND res.fecha_llegada NOT BETWEEN TO_DATE('2023-01-01', 'YYYY-MM-DD') AND TO_DATE('2023-12-31', 'YYYY-MM-DD')

GROUP BY aloj.nombre_alojamiento
ORDER BY aloj.nombre_alojamiento;

--RF12---------------------------->

--Recibe como parametro la semana a evaluar

--RF12:ALOJAMIENTO DE MINIMA OCUPACION:
SELECT aloj.nombre_alojamiento AS Alojamiento, MIN(total_reservas) AS Minimo_Reservas
FROM (
    SELECT res.alojamiento_reservado, COUNT(*) AS total_reservas
    FROM A_ALOJAMIENTOS aloj
    INNER JOIN A_RESERVAS res ON aloj.id_alojamiento = res.alojamiento_reservado
    GROUP BY res.alojamiento_reservado
) subquery
INNER JOIN A_ALOJAMIENTOS aloj ON subquery.alojamiento_reservado = aloj.id_alojamiento
GROUP BY aloj.nombre_alojamiento;

--RF12:OPERADORES MAS SOLICITADOS
SELECT op.nombre AS Operador, COUNT(*) AS TotalReservas
FROM A_OPERADORES op
INNER JOIN A_ALOJAMIENTOS aloj ON op.id_operador = aloj.operador
INNER JOIN A_RESERVAS res ON aloj.id_alojamiento = res.alojamiento_reservado
GROUP BY op.nombre
ORDER BY COUNT(*) DESC;


--RF12: OPERADORES MENOS SOLICITADOS
SELECT op.nombre AS Operador, COUNT(*) AS TotalReservas
FROM A_OPERADORES op
INNER JOIN A_ALOJAMIENTOS aloj ON op.id_operador = aloj.operador
INNER JOIN A_RESERVAS res ON aloj.id_alojamiento = res.alojamiento_reservado
GROUP BY op.nombre
ORDER BY COUNT(*) ASC;


--RF13---------------------------------------->


--RF13: cliente bueno 1: Hacen al menos 1 reserva en el mes
-- Se recibe como parametro
-- la fecha del mes
SELECT us.*, COUNT(DISTINCT res.fecha_llegada) as justificacion_buen_cliente_numero_reservas
FROM A_USUARIOS us
INNER JOIN A_RESERVAS res ON us.cedula = res.usuario
WHERE res.fecha_llegada >= TO_DATE('2023-01-01', 'YYYY-MM-DD')
  AND res.fecha_llegada <= TO_DATE('2023-12-31', 'YYYY-MM-DD')
GROUP BY us.cedula, us.nombre, us.relacion_universidad
HAVING COUNT(DISTINCT res.fecha_llegada) >= 1;

--RF13: cliente bueno 2: Reservan siempre alojamientos costosos
SELECT us.*, (SELECT COUNT(*) 
              FROM A_RESERVAS res
              INNER JOIN A_ALOJAMIENTOS aloj ON res.alojamiento_reservado = aloj.id_alojamiento
              WHERE us.cedula = res.usuario AND aloj.precio > 150) AS justificacion_buen_cliente_cantidad_reservas_mayor_150
FROM A_USUARIOS us
WHERE NOT EXISTS (
    SELECT 1
    FROM A_RESERVAS res
    INNER JOIN A_ALOJAMIENTOS aloj ON res.alojamiento_reservado = aloj.id_alojamiento
    WHERE us.cedula = res.usuario AND aloj.precio <= 150
);


--RF13: cliente bueno 3: Reservan siempre alojamientos tipo suite
SELECT us.*, (SELECT COUNT(*) 
              FROM A_RESERVAS res
              INNER JOIN A_ALOJAMIENTOS aloj ON res.alojamiento_reservado = aloj.id_alojamiento
              WHERE us.cedula = res.usuario AND aloj.nombre_alojamiento = 'suite') AS justificacion_buen_cliente_cantidad_reservas_suite
FROM A_USUARIOS us
WHERE NOT EXISTS (
    SELECT 1
    FROM A_RESERVAS res
    INNER JOIN A_ALOJAMIENTOS aloj ON res.alojamiento_reservado = aloj.id_alojamiento
    WHERE us.cedula = res.usuario AND aloj.nombre_alojamiento <> 'suite'
);


