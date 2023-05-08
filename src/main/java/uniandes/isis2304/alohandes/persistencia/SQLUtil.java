package uniandes.isis2304.alohandes.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Alojamiento;
import uniandes.isis2304.alohandes.negocio.Reserva;

class SQLUtil
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaAlohandes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaAlohandes pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLUtil (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para obtener un nuevo número de secuencia
	 * @param pm - El manejador de persistencia
	 * @return El número de secuencia generado
	 */
	public long nextval (PersistenceManager pm)
	{
        Query q = pm.newQuery(SQL, "SELECT "+ pp.darSeqAlohandes () + ".nextval FROM DUAL");
        q.setResultClass(Long.class);
        long resp = (long) q.executeUnique();
        return resp;
	}

	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @param pm - El manejador de persistencia
	 * @return arreglo
	 */
	public long [] limpiarAlohandes (PersistenceManager pm)
	{
        Query qAlojamiento = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaAlojamiento());          
        Query qContrato = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaContrato());
        Query qHostal = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaHostal());
        Query qHotel = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaHotel());
        Query qOperador = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaOperador());
        Query qParticular = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaParticular());
        Query qPropuesta = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPropuesta());
		Query qReserva = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReserva());
		Query qResidenciaU = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaResidenciaU());
		Query qServicio = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaServicio());
		Query qUsuario = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaUsuario());
		Query qAlojsdesh = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaAlojDeshabilitado());

        long alojamientosEliminados = (long) qAlojamiento.executeUnique ();
        long contratosEliminados = (long) qContrato.executeUnique ();
        long hostalesEliminadas = (long) qHostal.executeUnique ();
        long hotelesEliminadas = (long) qHotel.executeUnique ();
        long operadoresEliminados = (long) qOperador.executeUnique ();
        long particularesEliminados = (long) qParticular.executeUnique ();
        long propuestasEliminados = (long) qPropuesta.executeUnique ();
		long reservasEliminados = (long) qReserva.executeUnique ();
		long residenciasUEliminados = (long) qResidenciaU.executeUnique ();
		long serviciosEliminados = (long) qServicio.executeUnique ();
		long usuariosEliminados = (long) qUsuario.executeUnique ();
		long alojdeshabilitados = (long) qAlojsdesh.executeUnique ();
        return new long[] {alojamientosEliminados, contratosEliminados, hostalesEliminadas, hotelesEliminadas,
		operadoresEliminados, particularesEliminados, propuestasEliminados, reservasEliminados, residenciasUEliminados,
	serviciosEliminados, usuariosEliminados, alojdeshabilitados};
	}

	public List<Reserva> devolverTablaReserva(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva());
		q.setResultClass(Reserva.class);
		return (List<Reserva>)q.executeList();

	}

	public List<Alojamiento> devolverTablaAlojamiento(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaAlojamiento());
		q.setResultClass(Alojamiento.class);
		return (List<Alojamiento>)q.executeList();

	}

}
