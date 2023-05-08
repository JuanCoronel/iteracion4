package uniandes.isis2304.alohandes.persistencia;

import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Alojdeshabilitados;

class SQLAlojdeshabilitados {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLAlojdeshabilitados (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

	public long adicionarAlojamientoDeshabilitado (PersistenceManager pm, long id_aloj, Timestamp fecha, String evento) 
	{
		Query isol = pm.newQuery(SQL, "SET TRANSACTION ISOLATION LEVEL READ COMMITTED");
		isol.execute();
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaAlojDeshabilitado() + "(id_alojamiento, fecha, evento) values (?, ?, ?)");
        q.setParameters(id_aloj, fecha, evento);
        return (long) q.executeUnique();
	}

	public boolean checkearDispAlojamiento(PersistenceManager pm, long id_aloj)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaAlojDeshabilitado() + " WHERE id_alojamiento = ?");
        q.setParameters(id_aloj);
        List<Alojdeshabilitados> alojs = (List<Alojdeshabilitados>) q.executeList();
		if (alojs.isEmpty())
		{
			//True = el alojamiento si esta disponible puesto no existe en la lista de deshabilitados
			return true;
		}
		else
		{
			return false;
		}
	}

	//@RF10
	public long rehabilitarAlojamiento(PersistenceManager pm, long id_aloj) 
	{
		Query isol = pm.newQuery(SQL, "SET TRANSACTION ISOLATION LEVEL READ COMMITTED");
		isol.execute();
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaAlojDeshabilitado() + " WHERE id_alojamiento = ?");
        q.setParameters(id_aloj);
        return (long) q.executeUnique();
	}
}