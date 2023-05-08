package uniandes.isis2304.alohandes.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Usuario;

class SQLUsuario {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLUsuario (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

    public long adicionarUsuario (PersistenceManager pm, String nombre, long cedula, String relacion_universidad) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaUsuario () + "(nombre, cedula, relacion_universidad) values (?, ?, ?)");
        q.setParameters(nombre, cedula, relacion_universidad);
        return (long) q.executeUnique();
	}

    public long eliminarUsuariosPorNombre (PersistenceManager pm, String nombreUsuario)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaUsuario () + " WHERE nombre = ?");
        q.setParameters(nombreUsuario);
        return (long) q.executeUnique();
	}

    public long eliminarUsuarioPorId (PersistenceManager pm, long idUsuario)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaUsuario () + " WHERE id = ?");
        q.setParameters(idUsuario);
        return (long) q.executeUnique();
	}

    public Usuario darUsuarioPorId (PersistenceManager pm, long idUsuario) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaUsuario () + " WHERE cedula = ?");
		q.setResultClass(Usuario.class);
		q.setParameters(idUsuario);
		List<Usuario> users =  (List<Usuario>) q.executeList();
		return users.get(0);
	}

    public List<Usuario> darUsuariosPorNombre (PersistenceManager pm, String nombreUsuario) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaUsuario () + " WHERE nombre = ?");
		q.setResultClass(Usuario.class);
		q.setParameters(nombreUsuario);
		return (List<Usuario>) q.executeList();
	}

    public List<Usuario> darUsuarios (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaUsuario ());
		q.setResultClass(Usuario.class);
		return (List<Usuario>) q.executeList();
	}


}



