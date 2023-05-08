package uniandes.isis2304.alohandes.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Hotel;

class SQLHotel {
    
    private final static String SQL = PersistenciaAlohandes.SQL;

	private PersistenciaAlohandes pp;


	public SQLHotel (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}

    public long adicionarHotel (PersistenceManager pm, long id_Hotel, String nombre_hotel, String registro_legal ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaHotel () + "(id, nombre, ciudad, presupuesto, cantsedes) values (?, ?, ?, ?, ?)");
        q.setParameters(id_Hotel, nombre_hotel, registro_legal);
        return (long) q.executeUnique();
	}

    public long eliminarHotelsPorNombre (PersistenceManager pm, String nombreHotel)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaHotel () + " WHERE nombre = ?");
        q.setParameters(nombreHotel);
        return (long) q.executeUnique();
	}

    public long eliminarHotelPorId (PersistenceManager pm, long idHotel)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaHotel () + " WHERE id = ?");
        q.setParameters(idHotel);
        return (long) q.executeUnique();
	}

    public Hotel darHotelPorId (PersistenceManager pm, long idHotel) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHotel () + " WHERE id = ?");
		q.setResultClass(Hotel.class);
		q.setParameters(idHotel);
		return (Hotel) q.executeUnique();
	}

    public List<Hotel> darHotelsPorNombre (PersistenceManager pm, String nombreHotel) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHotel () + " WHERE nombre = ?");
		q.setResultClass(Hotel.class);
		q.setParameters(nombreHotel);
		return (List<Hotel>) q.executeList();
	}

    public List<Hotel> darHotels (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHotel ());
		q.setResultClass(Hotel.class);
		return (List<Hotel>) q.executeList();
	}


}


