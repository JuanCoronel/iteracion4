package uniandes.isis2304.alohandes.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Hostal;

class SQLHostal {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLHostal (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

    public long adicionarHostal (PersistenceManager pm, long id_hostal, String nombre_hostal, String registro_legal) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaHostal () + "(id, nombre, ciudad, presupuesto, cantsedes) values (?, ?, ?, ?, ?)");
        q.setParameters(id_hostal, nombre_hostal, registro_legal);
        return (long) q.executeUnique();
	}

    public long eliminarHostalesPorNombre (PersistenceManager pm, String nombreHostal)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaHostal () + " WHERE nombre = ?");
        q.setParameters(nombreHostal);
        return (long) q.executeUnique();
	}

    public long eliminarHostalPorId (PersistenceManager pm, long idHostal)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaHostal () + " WHERE id = ?");
        q.setParameters(idHostal);
        return (long) q.executeUnique();
	}

    public Hostal darHostalPorId (PersistenceManager pm, long idHostal) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHostal () + " WHERE id = ?");
		q.setResultClass(Hostal.class);
		q.setParameters(idHostal);
		return (Hostal) q.executeUnique();
	}

    public List<Hostal> darHostalesPorNombre (PersistenceManager pm, String nombreHostal) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHostal () + " WHERE nombre = ?");
		q.setResultClass(Hostal.class);
		q.setParameters(nombreHostal);
		return (List<Hostal>) q.executeList();
	}

    public List<Hostal> darHostales (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHostal ());
		q.setResultClass(Hostal.class);
		return (List<Hostal>) q.executeList();
	}


}


