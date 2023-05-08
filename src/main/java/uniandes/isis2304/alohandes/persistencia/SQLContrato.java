package uniandes.isis2304.alohandes.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Contrato;

class SQLContrato {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLContrato (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

    public long adicionarContrato (PersistenceManager pm, long id_contrato, String contratista, long Alojamiento,
    String registro_legal) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaContrato () + "(id, nombre, ciudad, presupuesto, cantsedes) values (?, ?, ?, ?, ?)");
        q.setParameters(id_contrato, contratista, Alojamiento, registro_legal);
        return (long) q.executeUnique();
	}

    public long eliminarContratosPorNombre (PersistenceManager pm, String nombreContrato)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaContrato () + " WHERE nombre = ?");
        q.setParameters(nombreContrato);
        return (long) q.executeUnique();
	}

    public long eliminarContratoPorId (PersistenceManager pm, long idContrato)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaContrato () + " WHERE id = ?");
        q.setParameters(idContrato);
        return (long) q.executeUnique();
	}

    public Contrato darContratoPorId (PersistenceManager pm, long idContrato) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaContrato () + " WHERE id = ?");
		q.setResultClass(Contrato.class);
		q.setParameters(idContrato);
		return (Contrato) q.executeUnique();
	}

    public List<Contrato> darContratosPorNombre (PersistenceManager pm, String nombreContrato) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaContrato () + " WHERE nombre = ?");
		q.setResultClass(Contrato.class);
		q.setParameters(nombreContrato);
		return (List<Contrato>) q.executeList();
	}

    public List<Contrato> darContratos (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaContrato ());
		q.setResultClass(Contrato.class);
		return (List<Contrato>) q.executeList();
	}


}

