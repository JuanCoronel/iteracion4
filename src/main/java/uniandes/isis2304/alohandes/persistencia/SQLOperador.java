package uniandes.isis2304.alohandes.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Operador;

class SQLOperador {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLOperador (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

    public long adicionarOperador (PersistenceManager pm, long id_Operador, String nombre_Operador) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaOperador () + "(id_operador, nombre) values (?, ?)");
        q.setParameters(id_Operador, nombre_Operador);
        return (long) q.executeUnique();
	}

    public long eliminarOperadorsPorNombre (PersistenceManager pm, String nombreOperador)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaOperador () + " WHERE nombre = ?");
        q.setParameters(nombreOperador);
        return (long) q.executeUnique();
	}

    public long eliminarOperadorPorId (PersistenceManager pm, int idOperador)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaOperador () + " WHERE id_operador = ?");
        q.setParameters(idOperador);
        return (long) q.executeUnique();
	}

    public Operador darOperadorPorId (PersistenceManager pm, long idOperador) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOperador () + " WHERE id_operador = ?");
		q.setResultClass(Operador.class);
		q.setParameters(idOperador);
		return (Operador) q.executeUnique();
	}

    public List<Operador> darOperadorsPorNombre (PersistenceManager pm, String nombreOperador) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOperador () + " WHERE nombre = ?");
		q.setResultClass(Operador.class);
		q.setParameters(nombreOperador);
		return (List<Operador>) q.executeList();
	}

    public List<Operador> darOperadors (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOperador ());
		q.setResultClass(Operador.class);
		return (List<Operador>) q.executeList();
	}


}


