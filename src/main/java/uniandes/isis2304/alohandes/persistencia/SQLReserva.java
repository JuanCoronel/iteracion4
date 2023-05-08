package uniandes.isis2304.alohandes.persistencia;
import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Reserva;

class SQLReserva {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLReserva (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

    public long adicionarReserva (PersistenceManager pm, int id_Reserva, String tipo_contrato, Timestamp fecha_llegada, Timestamp fecha_salida, long costo, long usuario, long alojamiento_reservado) 
	{
		Query isol = pm.newQuery(SQL, "SET TRANSACTION ISOLATION LEVEL READ COMMITTED");
		isol.execute();
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaReserva () + "(id_reserva, tipo_contrato, fecha_llegada, fecha_salida, costo, usuario, alojamiento_reservado) values (?, ?, ?, ?, ?, ?, ?)");
        q.setParameters(id_Reserva,  tipo_contrato,  fecha_llegada, fecha_salida, costo, usuario, alojamiento_reservado);
        return (long) q.executeUnique();
	}

    public long eliminarReservasPorNombre (PersistenceManager pm, String nombreReserva)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReserva () + " WHERE nombre = ?");
        q.setParameters(nombreReserva);
        return (long) q.executeUnique();
	}

    public long eliminarReservaPorId (PersistenceManager pm, long idReserva)
	{
		Query isol = pm.newQuery(SQL, "SET TRANSACTION ISOLATION LEVEL READ COMMITTED");
		isol.execute();
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReserva () + " WHERE id_reserva = ?");
        q.setParameters(idReserva);
        return (long) q.executeUnique();
	}

    public Reserva darReservaPorId (PersistenceManager pm, long idReserva) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva () + " WHERE id_reserva = ?");
		q.setResultClass(Reserva.class);
		q.setParameters(idReserva);
		List<Reserva> resvas = (List<Reserva>) q.executeList();
		Reserva res = resvas.get(0);
		return res;
	}

    public List<Reserva> darReservasPorNombre (PersistenceManager pm, String nombreReserva) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva () + " WHERE nombre = ?");
		q.setResultClass(Reserva.class);
		q.setParameters(nombreReserva);
		return (List<Reserva>) q.executeList();
	}

    public List<Reserva> darReservas (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva ());
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.executeList();
	}


}



