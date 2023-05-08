package uniandes.isis2304.alohandes.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Particular;

class SQLParticular {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLParticular (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

    public long adicionarParticular (PersistenceManager pm, long id_Particular, String nombre_Particular) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaParticular () + "(id, nombre, ciudad, presupuesto, cantsedes) values (?, ?, ?, ?, ?)");
        q.setParameters(id_Particular, nombre_Particular);
        return (long) q.executeUnique();
	}

    public long eliminarParticularsPorNombre (PersistenceManager pm, String nombreParticular)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaParticular () + " WHERE nombre = ?");
        q.setParameters(nombreParticular);
        return (long) q.executeUnique();
	}

    public long eliminarParticularPorId (PersistenceManager pm, long idParticular)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaParticular () + " WHERE id = ?");
        q.setParameters(idParticular);
        return (long) q.executeUnique();
	}

    public Particular darParticularPorId (PersistenceManager pm, long idParticular) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaParticular () + " WHERE id = ?");
		q.setResultClass(Particular.class);
		q.setParameters(idParticular);
		return (Particular) q.executeUnique();
	}

    public List<Particular> darParticularsPorNombre (PersistenceManager pm, String nombreParticular) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaParticular () + " WHERE nombre = ?");
		q.setResultClass(Particular.class);
		q.setParameters(nombreParticular);
		return (List<Particular>) q.executeList();
	}

    public List<Particular> darParticulars (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaParticular ());
		q.setResultClass(Particular.class);
		return (List<Particular>) q.executeList();
	}


}



