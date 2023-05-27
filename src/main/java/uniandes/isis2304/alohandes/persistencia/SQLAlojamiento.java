package uniandes.isis2304.alohandes.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;


import uniandes.isis2304.alohandes.negocio.Alojamiento;
class SQLAlojamiento {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLAlojamiento (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

    public long adicionarAlojamiento (PersistenceManager pm, long id_alojamiento, long operador, int capacidad, int precio, String relacion_universidad,
    String horarios_recepcion, int precio_administracion, int precio_seguro,
    String nombre_alojamiento) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaAlojamiento () + "(id_alojamiento, operador, capacidad, precio, relacion_universidad, horarios_recepcion, precio_administracion, precio_seguro, nombre_alojamiento) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        q.setParameters(id_alojamiento, operador, capacidad, precio, relacion_universidad, horarios_recepcion, precio_administracion, precio_seguro, nombre_alojamiento);
        return (long) q.executeUnique();
	}

	public String rfc1(PersistenceManager pm) {
		
		Query q = pm.newQuery(SQL, "SELECT op.nombre as proveedor, sum(r.costo) as ingresos FROM A_alojamientos a INNER JOIN A_Reservas r on a.id_alojamiento = r.alojamiento_reservado INNER JOIN A_operadores op on a.operador = op.id_operador GROUP BY op.nombre ORDER BY sum(r.costo) desc" );

		// Ejecutar la consulta y obtener los resultados
		List<Object[]> results = (List<Object[]>) q.execute();
		
		// Construir la cadena de texto con los resultados
		StringBuilder sb = new StringBuilder();
		for (Object[] row : results) {
			String proveedor = (String) row[0];
			Double ingresos = (Double) row[1];
			
			sb.append("Proveedor: ").append(proveedor).append(", Ingresos: ").append(ingresos).append("\n");
		}
		
		// Convertir la cadena de texto a String
		String resultString = sb.toString();
		
		return resultString;
	}

	public String rfc10(PersistenceManager pm, String group, String idAlojString, String rango_fecha_inferior, String rango_fecha_superior) {
		
		String resultString = "";

		if (group.equals("1")) {
			Query q1 = pm.newQuery(SQL, "SELECT us.*, COUNT(aloj.id_alojamiento) as cantidad_reservas_usuario FROM A_USUARIOS us INNER JOIN A_RESERVAS res ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on res.alojamiento_reservado = aloj.id_alojamiento WHERE res.alojamiento_reservado = "+idAlojString+" AND res.fecha_llegada BETWEEN TO_DATE('"+rango_fecha_inferior+"', 'YYYY-MM-DD') AND TO_DATE('"+rango_fecha_superior+"', 'YYYY-MM-DD') GROUP BY us.nombre, us.cedula, us.relacion_universidad ORDER BY us.nombre, us.cedula, us.relacion_universidad");

			// Execute the query and retrieve the results
			List<?> results = (List<?>) q1.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			}
		} else if (group.equals("2")) {
			Query q2 = pm.newQuery(SQL, "SELECT aloj.id_alojamiento as alojamiento, COUNT(distinct us.nombre) as cantidad_reservas_usuario FROM A_USUARIOS us INNER JOIN A_RESERVAS res ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on res.alojamiento_reservado = aloj.id_alojamiento WHERE res.alojamiento_reservado = "+idAlojString+" AND res.fecha_llegada BETWEEN TO_DATE('"+rango_fecha_inferior+"', 'YYYY-MM-DD') AND TO_DATE('"+rango_fecha_superior+"', 'YYYY-MM-DD') GROUP BY aloj.id_alojamiento ORDER BY aloj.id_alojamiento");
			
			// Execute the query and retrieve the results
			List<?> results = (List<?>) q2.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			}
		} else if (group.equals("3")) {
			Query q3 = pm.newQuery(SQL, "SELECT aloj.nombre_alojamiento as tipo_alojamiento, COUNT(distinct us.nombre) as cantidad_reservas_usuario FROM A_USUARIOS us INNER JOIN A_RESERVAS res ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on res.alojamiento_reservado = aloj.id_alojamiento WHERE res.alojamiento_reservado = "+idAlojString+" AND res.fecha_llegada BETWEEN TO_DATE('"+rango_fecha_inferior+"', 'YYYY-MM-DD') AND TO_DATE('"+rango_fecha_superior+"', 'YYYY-MM-DD') GROUP BY aloj.nombre_alojamiento ORDER BY aloj.nombre_alojamiento");
			
			// Execute the query and retrieve the results
			List<?> results = (List<?>) q3.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			}
		}

	return resultString;

	}

	public String rfc11(PersistenceManager pm, String group, String idAlojString, String rango_fecha_inferior, String rango_fecha_superior) {
		
		String resultString = "";

		if (group.equals("1")) {
			Query q1 = pm.newQuery(SQL, "SELECT us.*, COUNT(aloj.id_alojamiento) as cantidad_reservas_usuario FROM A_USUARIOS us INNER JOIN A_RESERVAS res ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on res.alojamiento_reservado = aloj.id_alojamiento WHERE res.alojamiento_reservado = "+idAlojString+" AND res.fecha_llegada NOT BETWEEN TO_DATE('"+rango_fecha_inferior+"', 'YYYY-MM-DD') AND TO_DATE('"+rango_fecha_superior+"', 'YYYY-MM-DD') GROUP BY us.nombre, us.cedula, us.relacion_universidad ORDER BY us.nombre, us.cedula, us.relacion_universidad");

			// Execute the query and retrieve the results
			List<?> results = (List<?>) q1.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			}
		} else if (group.equals("2")) {
			Query q2 = pm.newQuery(SQL, "SELECT aloj.id_alojamiento as alojamiento, COUNT(distinct us.nombre) as cantidad_reservas_usuario FROM A_USUARIOS us INNER JOIN A_RESERVAS res ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on res.alojamiento_reservado = aloj.id_alojamiento WHERE res.alojamiento_reservado = "+idAlojString+" AND res.fecha_llegada NOT BETWEEN TO_DATE('"+rango_fecha_inferior+"', 'YYYY-MM-DD') AND TO_DATE('"+rango_fecha_superior+"', 'YYYY-MM-DD') GROUP BY aloj.id_alojamiento ORDER BY aloj.id_alojamiento");
			
			// Execute the query and retrieve the results
			List<?> results = (List<?>) q2.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			}
		} else if (group.equals("3")) {
			Query q3 = pm.newQuery(SQL, "SELECT aloj.nombre_alojamiento as tipo_alojamiento, COUNT(distinct us.nombre) as cantidad_reservas_usuario FROM A_USUARIOS us INNER JOIN A_RESERVAS res ON us.cedula = res.usuario INNER JOIN A_ALOJAMIENTOS aloj on res.alojamiento_reservado = aloj.id_alojamiento WHERE res.alojamiento_reservado = "+idAlojString+" AND res.fecha_llegada NOT BETWEEN TO_DATE('"+rango_fecha_inferior+"', 'YYYY-MM-DD') AND TO_DATE('"+rango_fecha_superior+"', 'YYYY-MM-DD') GROUP BY aloj.nombre_alojamiento ORDER BY aloj.nombre_alojamiento");
			
			// Execute the query and retrieve the results
			List<?> results = (List<?>) q3.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			}
		}

	return resultString;

	}

	public String rfc12(PersistenceManager pm, String group) {
		
		String resultString = "";

		if (group.equals("1")) {
			Query q1 = pm.newQuery(SQL, "SELECT aloj.nombre_alojamiento AS Alojamiento, MIN(total_reservas) AS Minimo_Reservas FROM (SELECT res.alojamiento_reservado, COUNT(*) AS total_reservas FROM A_ALOJAMIENTOS aloj INNER JOIN A_RESERVAS res ON aloj.id_alojamiento = res.alojamiento_reservado GROUP BY res.alojamiento_reservado) subquery INNER JOIN A_ALOJAMIENTOS aloj ON subquery.alojamiento_reservado = aloj.id_alojamiento GROUP BY aloj.nombre_alojamiento");

			// Execute the query and retrieve the results
			List<?> results = (List<?>) q1.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			
    }
		} else if (group.equals("2")) {
			Query q2 = pm.newQuery(SQL, "SELECT op.nombre AS Operador, COUNT(*) AS TotalReservas FROM A_OPERADORES op INNER JOIN A_ALOJAMIENTOS aloj ON op.id_operador = aloj.operador INNER JOIN A_RESERVAS res ON aloj.id_alojamiento = res.alojamiento_reservado GROUP BY op.nombre ORDER BY COUNT(*) DESC");
			
			// Execute the query and retrieve the results
			List<?> results = (List<?>) q2.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			}
		} else if (group.equals("3")) {
			Query q3 = pm.newQuery(SQL, "SELECT op.nombre AS Operador, COUNT(*) AS TotalReservas FROM A_OPERADORES op INNER JOIN A_ALOJAMIENTOS aloj ON op.id_operador = aloj.operador INNER JOIN A_RESERVAS res ON aloj.id_alojamiento = res.alojamiento_reservado GROUP BY op.nombre ORDER BY COUNT(*) ASC");
			
			// Execute the query and retrieve the results
			List<?> results = (List<?>) q3.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			}
		}

	return resultString;

	}

	public String rfc13(PersistenceManager pm, String group, String rango_fecha_inferior, String rango_fecha_superior) {
		
		String resultString = "";

		if (group.equals("1")) {
			Query q1 = pm.newQuery(SQL, "SELECT us.*, COUNT(DISTINCT res.fecha_llegada) as justificacion_buen_cliente_numero_reservas FROM A_USUARIOS us INNER JOIN A_RESERVAS res ON us.cedula = res.usuario WHERE res.fecha_llegada >= TO_DATE('2023-01-01', 'YYYY-MM-DD') AND res.fecha_llegada <= TO_DATE('2023-12-31', 'YYYY-MM-DD') GROUP BY us.cedula, us.nombre, us.relacion_universidad HAVING COUNT(DISTINCT res.fecha_llegada) >= 1");

			// Execute the query and retrieve the results
			List<?> results = (List<?>) q1.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			
    }
		} else if (group.equals("2")) {
			Query q2 = pm.newQuery(SQL, "SELECT us.*, (SELECT COUNT(*) FROM A_RESERVAS res INNER JOIN A_ALOJAMIENTOS aloj ON res.alojamiento_reservado = aloj.id_alojamiento WHERE us.cedula = res.usuario AND aloj.precio > 150) AS justificacion_buen_cliente_cantidad_reservas_mayor_150 FROM A_USUARIOS us WHERE NOT EXISTS (SELECT 1 FROM A_RESERVAS res INNER JOIN A_ALOJAMIENTOS aloj ON res.alojamiento_reservado = aloj.id_alojamiento WHERE us.cedula = res.usuario AND aloj.precio <= 150)");
			
			// Execute the query and retrieve the results
			List<?> results = (List<?>) q2.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			}
		} else if (group.equals("3")) {
			Query q3 = pm.newQuery(SQL, "SELECT us.*, (SELECT COUNT(*) FROM A_RESERVAS res INNER JOIN A_ALOJAMIENTOS aloj ON res.alojamiento_reservado = aloj.id_alojamiento WHERE us.cedula = res.usuario AND aloj.nombre_alojamiento = 'suite') AS justificacion_buen_cliente_cantidad_reservas_suite FROM A_USUARIOS us WHERE NOT EXISTS (SELECT 1 FROM A_RESERVAS res INNER JOIN A_ALOJAMIENTOS aloj ON res.alojamiento_reservado = aloj.id_alojamiento WHERE us.cedula = res.usuario AND aloj.nombre_alojamiento <> 'suite')");
			
			// Execute the query and retrieve the results
			List<?> results = (List<?>) q3.execute();
			
			// Process the results and convert them to a string
			for (Object obj : results) {
				// Process each row of the query result
				// Append the data to the resultString
				resultString += obj.toString();
			}
		}

	return resultString;
	}

	//RF6
    public long eliminarAlojamientosPorNombre (PersistenceManager pm, String nombreAlojamiento)
	{
			Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaAlojamiento () + " WHERE nombre_alojamiento = ?");
			q.setParameters(nombreAlojamiento);
			return (long) q.executeUnique();

	}

	
    public int eliminarAlojamientoPorId (PersistenceManager pm, long idAlojamiento)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaAlojamiento () + " WHERE id = ?");
        q.setParameters(idAlojamiento);
        return (int) q.executeUnique();
	}

    public Alojamiento darAlojamientoPorId (PersistenceManager pm, long idAlojamiento) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaAlojamiento () + " WHERE id_alojamiento = ?");
		q.setResultClass(Alojamiento.class);
		q.setParameters(idAlojamiento);
		List<Alojamiento> alojs = (List<Alojamiento>) q.executeList();
		return alojs.get(0);
	}

	public Long actualizarCapacidadAlojamiento (PersistenceManager pm, long idAlojamiento, long capacidad) 
	{
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaAlojamiento () + " SET capacidad = ? WHERE id_alojamiento = ?");
		q.setResultClass(Alojamiento.class);
		q.setParameters(capacidad,idAlojamiento);
		return (long) q.executeUnique();
	}

    public List<Alojamiento> darAlojamientosPorNombre (PersistenceManager pm, String nombreAlojamiento) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaAlojamiento () + " a WHERE a.nombre_alojamiento = ?");
		q.setResultClass(Alojamiento.class);
		q.setParameters(nombreAlojamiento);
		return (List<Alojamiento>) q.executeList();
	}

    public List<Alojamiento> darAlojamientos (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaAlojamiento ());
		q.setResultClass(Alojamiento.class);
		return (List<Alojamiento>) q.executeList();
	}


}
