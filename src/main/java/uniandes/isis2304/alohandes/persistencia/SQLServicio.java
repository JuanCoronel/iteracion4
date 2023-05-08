package uniandes.isis2304.alohandes.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Reserva;
import uniandes.isis2304.alohandes.negocio.Servicio;

class SQLServicio {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLServicio (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

    public long adicionarServicio (PersistenceManager pm, long id_Servicio, String nombreServicio, long alojamiento) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaServicio () + "(id, nombre, ciudad, presupuesto, cantsedes) values (?, ?, ?, ?, ?)");
        q.setParameters(id_Servicio, nombreServicio, alojamiento);
        return (long) q.executeUnique();
	}

    public long eliminarServiciosPorNombre (PersistenceManager pm, String nombreServicio)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaServicio () + " WHERE nombre = ?");
        q.setParameters(nombreServicio);
        return (long) q.executeUnique();
	}

    public long eliminarServicioPorId (PersistenceManager pm, long idServicio)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaServicio () + " WHERE id = ?");
        q.setParameters(idServicio);
        return (long) q.executeUnique();
	}

	public List<Servicio> darServiciosDeUnAlojamiento(PersistenceManager pm, long idAloj)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaServicio () + " WHERE alojamiento = ?");
		q.setResultClass(Servicio.class);
		q.setParameters(idAloj);
		return (List<Servicio>) q.executeList();
	}

	public long darUltimoIdReservas(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM "+pp.darTablaReserva()+ " WHERE id_reserva = (SELECT MAX(id_reserva) FROM "+pp.darTablaReserva()+ " )");
		q.setResultClass(Reserva.class);
		List<Reserva> reservas = (List<Reserva>) q.executeList();
		System.out.println(reservas);
		if (reservas.isEmpty())
		{
			return 0;
		}
		else
		{
			return reservas.get(0).getIdReserva();
		}
		
	}

    public Servicio darServicioPorId (PersistenceManager pm, long idServicio) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaServicio () + " WHERE id = ?");
		q.setResultClass(Servicio.class);
		q.setParameters(idServicio);
		return (Servicio) q.executeUnique();
	}

    public List<Servicio> darServiciosPorNombre (PersistenceManager pm, String nombreServicio) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaServicio () + " WHERE nombre = ?");
		q.setResultClass(Servicio.class);
		q.setParameters(nombreServicio);
		return (List<Servicio>) q.executeList();
	}

    public List<Servicio> darServicios (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaServicio ());
		q.setResultClass(Servicio.class);
		return (List<Servicio>) q.executeList();
	}


}



