package uniandes.isis2304.alohandes.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Propuesta;

class SQLPropuesta {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLPropuesta (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

    public long adicionarPropuesta (PersistenceManager pm, long id_Propuesta, String nombre_alojamiento, String info_alojamiento) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPropuesta () + "(id_propuesta, nombre_alojamiento, info_alojamiento) values (?, ?, ?)");
        q.setParameters(id_Propuesta, nombre_alojamiento, info_alojamiento);
        return (long) q.executeUnique();
	}

    public long eliminarPropuestasPorNombre (PersistenceManager pm, String nombrePropuesta)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPropuesta () + " WHERE nombre = ?");
        q.setParameters(nombrePropuesta);
        return (long) q.executeUnique();
	}

    public long eliminarPropuestaPorId (PersistenceManager pm, long idPropuesta)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPropuesta () + " WHERE id = ?");
        q.setParameters(idPropuesta);
        return (long) q.executeUnique();
	}

    public Propuesta darPropuestaPorId (PersistenceManager pm, long idPropuesta) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPropuesta () + " WHERE id = ?");
		q.setResultClass(Propuesta.class);
		q.setParameters(idPropuesta);
		return (Propuesta) q.executeUnique();
	}

    public List<Propuesta> darPropuestasPorNombre (PersistenceManager pm, String nombrePropuesta) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPropuesta () + " WHERE nombre = ?");
		q.setResultClass(Propuesta.class);
		q.setParameters(nombrePropuesta);
		return (List<Propuesta>) q.executeList();
	}

    public List<Propuesta> darPropuestas (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPropuesta ());
		q.setResultClass(Propuesta.class);
		return (List<Propuesta>) q.executeList();
	}


}



