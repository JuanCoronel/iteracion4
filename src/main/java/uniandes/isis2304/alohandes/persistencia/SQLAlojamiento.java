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
