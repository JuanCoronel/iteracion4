package uniandes.isis2304.alohandes.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.ResidenciaU;

class SQLResidenciaU {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLResidenciaU (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

    public long adicionarResidenciaU (PersistenceManager pm, long id_ResidenciaU, String nombreResidenciaU, String registroLegal  ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaResidenciaU () + "(id, nombre, ciudad, presupuesto, cantsedes) values (?, ?, ?, ?, ?)");
        q.setParameters(id_ResidenciaU, nombreResidenciaU, registroLegal);
        return (long) q.executeUnique();
	}

    public long eliminarResidenciaUsPorNombre (PersistenceManager pm, String nombreResidenciaU)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaResidenciaU () + " WHERE nombre = ?");
        q.setParameters(nombreResidenciaU);
        return (long) q.executeUnique();
	}

    public long eliminarResidenciaUPorId (PersistenceManager pm, long idResidenciaU)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaResidenciaU () + " WHERE id = ?");
        q.setParameters(idResidenciaU);
        return (long) q.executeUnique();
	}

    public ResidenciaU darResidenciaUPorId (PersistenceManager pm, long idResidenciaU) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaResidenciaU () + " WHERE id = ?");
		q.setResultClass(ResidenciaU.class);
		q.setParameters(idResidenciaU);
		return (ResidenciaU) q.executeUnique();
	}

    public List<ResidenciaU> darResidenciaUsPorNombre (PersistenceManager pm, String nombreResidenciaU) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaResidenciaU () + " WHERE nombre = ?");
		q.setResultClass(ResidenciaU.class);
		q.setParameters(nombreResidenciaU);
		return (List<ResidenciaU>) q.executeList();
	}

    public List<ResidenciaU> darResidenciaUs (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaResidenciaU ());
		q.setResultClass(ResidenciaU.class);
		return (List<ResidenciaU>) q.executeList();
	}


}



