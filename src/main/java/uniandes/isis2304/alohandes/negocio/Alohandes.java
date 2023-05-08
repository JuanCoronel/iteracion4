
package uniandes.isis2304.alohandes.negocio;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import com.google.gson.JsonObject;

import uniandes.isis2304.alohandes.persistencia.PersistenciaAlohandes;

/**
 * Clase principal del negocio
 * Sarisface todos los requerimientos funcionales del negocio
 *
 * @author Juan Coronel
 * @author Pablo Martinez
 */
public class Alohandes 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(Alohandes.class.getName());
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaAlohandes pp;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public Alohandes ()
	{
		pp = PersistenciaAlohandes.getInstance ();
	}
	
	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public Alohandes (JsonObject tableConfig)
	{
		pp = PersistenciaAlohandes.getInstance (tableConfig);
	}
	
	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia ()
	{
		pp.cerrarUnidadPersistencia ();
	}

	/* ****************************************************************
	 * 			Métodos para manejar las Propuestas
	 *****************************************************************/

	public Propuesta adicionarPropuesta(int id, String nombre, String info)
	 {
		log.info("Adicionar propuesta: "+ nombre);
		Propuesta op = pp.adicionarPropuesta(id, nombre, info);
		log.info("Adicionando propuesta: "+ op);
		return op;
	 }

	 /* ****************************************************************
	 * 			Métodos para manejar los Servicios
	 *****************************************************************/

	public List<Servicio> darServiciosDeUnAlojamiento(long id_alojamiento)
	{
	   log.info("Encontrar los servicios del alojamiento con id: "+ id_alojamiento);
	   List<Servicio> services = pp.darServiciosDeUnAlojamiento(id_alojamiento);
	   log.info("Encontrando los servicios del alojamiento con id: "+ id_alojamiento);
	   return services;
	}

	public long darUltimoIdReservas()
	{
	   log.info("Encontrar el id del ultimo servicio registrado");
	   long lastId = pp.darUltimoIdReservas();
	   log.info("Encontrando el id del ultimo servicio registrado");
	   return lastId;
	}

	 /* ****************************************************************
	 * 			Métodos para manejar las Reservas
	 *****************************************************************/

	public Reserva adicionarReserva(int id, String tipo_contrato, String fecha_llegada, String fecha_salida, int costo, int usuario, int alojamiento) throws ParseException
	{
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date fecha = formatoFecha.parse(fecha_llegada);
		long tiempo = fecha.getTime();
		Timestamp timestamp_llegada = new Timestamp(tiempo);

		Date fecha2 = formatoFecha.parse(fecha_salida);
		long tiempo2 = fecha2.getTime();
		Timestamp timestamp_salida = new Timestamp(tiempo2);

	   log.info("Adicionar reserva:"+ id);
	   Reserva op = pp.adicionarReserva(id, tipo_contrato,timestamp_llegada,timestamp_salida, costo, usuario, alojamiento);
	   log.info("Adicionando reserva: "+ id);
	   return op;
	}

	public Reserva adicionarReservaLong(int id, String tipo_contrato, String fecha_llegada, String fecha_salida, int costo, int usuario, long alojamiento) throws ParseException
	{
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date fecha = formatoFecha.parse(fecha_llegada);
		long tiempo = fecha.getTime();
		Timestamp timestamp_llegada = new Timestamp(tiempo);

		Date fecha2 = formatoFecha.parse(fecha_salida);
		long tiempo2 = fecha2.getTime();
		Timestamp timestamp_salida = new Timestamp(tiempo2);

	   log.info("Adicionar reserva:"+ id);
	   Reserva op = pp.adicionarReserva(id, tipo_contrato,timestamp_llegada,timestamp_salida, costo, usuario, alojamiento);
	   log.info("Adicionando reserva: "+ id);
	   return op;
	}

	/**
	 * Encuentra todas las reservas en Alohandes
	 * */
	public List<Reserva> darReservas ()
	{
		log.info ("Consultando reservas");
        List<Reserva> reservas = pp.darReservas();	
        log.info ("Consultando reseravs: " + reservas.size() + " existentes");
        return reservas;
	}

	/**
	 * Encuentra la reserva en Alohandes con el nombre ingresado por parametro
	 * */
	public Reserva darReservaPorId (long id)
	{
		log.info ("Consultando reserva");
        Reserva reserva = pp.darReservaPorId(id);
        return reserva;
	}

	/**
	 * Elimina de manera persistente una Reserva por su id
	 * */
	public long eliminarReservaPorId (int id)
	{
	   log.info("Eliminando reserva por id: " + id);
	   long resp = pp.eliminarReservaPorId(id);
	   log.info("Eliminando reserva por id: "+resp);
	   return resp;

	}

	/* ****************************************************************
	 * 			Métodos para manejar los OPERADORES
	 *****************************************************************/
	
	/**
	 * Adiciona de manera persistente un Operador
	 * */

	 public Operador adicionarOperador(int id, String nombre)
	 {
		log.info("Adicionar operador:"+ nombre);
		Operador op = pp.adicionarOperador(id, nombre);
		log.info("Adicionando Operador: "+ op);
		return op;
	 }

	 /**
	 * Elimina de manera persistente un Operador por su nombre
	 * */
	 public long eliminarOperadorPorNombre (String nombre)
	 {
		log.info("Eliminando operador por nombre: " + nombre);
		long resp = pp.eliminarOperadorPorNombre(nombre);
		log.info("Eliminando operador por nombre: "+resp);
		return resp;

	 }

	 /**
	 * Elimina de manera persistente un Operador por su id
	 * */
	public long eliminarOperadorPorId (int id)
	{
	   log.info("Eliminando operador por id: " + id);
	   long resp = pp.eliminarOperadorPorId(id);
	   log.info("Eliminando operador por id: "+resp);
	   return resp;

	}

	/**
	 * Encuentra todos los operadores en Alohandes
	 * */
	public List<Operador> darOperadores ()
	{
		log.info ("Consultando Operadores");
        List<Operador> operadores = pp.darOperadors();	
        log.info ("Consultando Operadores: " + operadores.size() + " existentes");
        return operadores;
	}

	/**
	 * Encuentra todos los operadores en alohandes y los devuelve como una lista de VOTipoAlojamiento
	 */
	public List<VOOperador> darVOOperadores ()
	{
		log.info ("Generando los VO de operadores");        
        List<VOOperador> voTipos = new LinkedList<VOOperador> ();
        for (Operador tb : pp.darOperadors())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de operadores: " + voTipos.size() + " existentes");
        return voTipos;
	}

	/**
	 * Encuentra el operador en Alohandes con el nombre solicitado
	 */
	public List<Operador> darOperadorPorNombre (String nombre)
	{
		log.info ("Buscando operador por nombre: " + nombre);
		List<Operador> tb = pp.darOperadorsPorNombre(nombre);
		return tb;
	}

	/* ****************************************************************
	 * 			Métodos para manejar los Alojamientos
	 *****************************************************************/
	
	 /**
	 * Adiciona de manera persistente un alojamiento
	 * Adiciona entradas al log de la aplicación
	 */
	public Alojamiento adicionarAlojamiento (int id_aloj, int operador, int capacidad, int precio, String relacion_universidad, String horarios_recepcion, int precio_administracion, int precio_seguro, String nombre)
	{
		log.info ("Adicionar alojamiento " + nombre);
		Alojamiento alojamiento = pp.adicionarAlojamiento(id_aloj, operador, capacidad, precio, relacion_universidad, horarios_recepcion, precio_administracion, precio_seguro, nombre);	
        log.info ("Adicionando alojamiento: " + alojamiento);
        return alojamiento;
	}

	/**
	 * Adiciona de manera persistente un alojamiento a la tabla de alojamientos deshabilitados
	 * Adiciona entradas al log de la aplicación
	 */
	public Alojdeshabilitados adicionarAlojamientoDeshabilitado (long id, Timestamp fecha, String evento)
	{

		log.info ("Adicionar alojamiento a la lista de deshabilitados con el id: " + id);
		Alojdeshabilitados alojamiento = pp.adicionarAlojamientoDeshabilitado(id, fecha, evento);
        log.info ("Adicionando alojamiento a la lista de deshabilitados con el id: " + id);
        return alojamiento;
	}

	/**
	 * Adiciona de manera persistente un alojamiento a la tabla de alojamientos deshabilitados
	 * Adiciona entradas al log de la aplicación
	 */
	public long rehabilitarAlojamiento (long id)
	{

		log.info ("Rehabilitar alojamiento de la lista de deshabilitados con el id: " + id);
		long alojamiento = pp.rehabilitarAlojamiento(id);
        log.info ("Rehabilitando alojamiento de la lista de deshabilitados con el id: " + id);
        return alojamiento;
	}

	/**
	 * Checkea si un alojamiento esta disponible, osea no esta deshabilitado
	 */
	public boolean checkearDispAlojamiento(long id)
	{

		log.info ("Checkear la disponibilidad del alojamiento con id: " + id);
		boolean alojamiento = pp.checkearDispAlojamiento(id);
        log.info ("Checkeando la disponibilidad del alojamiento con id: " + id);
        return alojamiento;
	}

	/**
	 * @RF6: Eliminar la propuesta de alojamiento siempre y cuando 
	 * se cumplan los requisitos
	 */
	public long eliminarAlojamiento (String nombre)
	{
        log.info ("Eliminando alojamiento por nombre: " + nombre);
        long resp = pp.eliminarAlojamientoPorNombre(nombre);
        log.info ("Eliminando Alojamiento por nombre: " + resp + " tuplas eliminadas");
        return resp;
	}

	/**
	 * Encuentra el alojamiento en Alohandes con el nombre solicitado
	 */
	public List<Alojamiento> darAlojamientoPorNombre (String nombre)
	{
		log.info ("Buscando alojamiento por nombre: " + nombre);
		List<Alojamiento> tb = pp.darAlojamientosPorNombre(nombre);
		return tb;
	}

	/**
	 * Encuentra el alojamiento en Alohandes con la id solicitada
	 */
	public Alojamiento darAlojamientoPorId (long id)
	{
		log.info ("Buscando alojamiento por id: " + id);
		Alojamiento tb = pp.darAlojamientoPorId(id);
		return tb;
	}

	/**
	 * Actualiza el alojamiento en Alohandes con la capacidad ingresada
	 */
	public long actualizarCapacidadAlojamiento (long id_aloj, long capacidad)
	{
		log.info ("Actualizando la capacidad del alojamiento con id " + id_aloj);
		long tb = pp.actualizarCapacidadAlojamiento(id_aloj, capacidad);
		return tb;
	}

	/**
	 * Encuentra todas las Alojamiento en Alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Alojamiento con todos las Alojamientos que conoce la aplicación, llenos con su información básica
	 */
	public List<Alojamiento> darAlojamientos ()
	{
        log.info ("Consultando Alojamientos");
        List<Alojamiento> Alojamientos = pp.darAlojamientos ();	
        log.info ("Consultando Alojamientos: " + Alojamientos.size() + " Alojamientos existentes");
        return Alojamientos;
	}

	/**
	 * Encuentra todos los tipos de Alojamiento en Alohandes y los devuelve como una lista de VOTipoAlojamiento
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOAlojamiento con todos las Alojamientos que conoce la aplicación, llenos con su información básica
	 */
	public List<VOAlojamiento> darVOAlojamientos ()
	{
		log.info ("Generando los VO de las Alojamientos");       
        List<VOAlojamiento> voAlojamientos = new LinkedList<VOAlojamiento> ();
        for (Alojamiento beb : pp.darAlojamientos ())
        {
        	voAlojamientos.add (beb);
        }
        log.info ("Generando los VO de las Alojamientos: " + voAlojamientos.size() + " existentes");
		return voAlojamientos;
	}

	/* ****************************************************************
	 * 			Métodos para manejar los usuarios
	 *****************************************************************/

	/**
	 * Adiciona de manera persistente un Usuario 
	 * Adiciona entradas al log de la aplicación
	 * @RF3
	 * @return El objeto Usuario adicionado. null si ocurre alguna Excepción
	 */
	public Usuario adicionarUsuario (String nombre, long cedula, String relacionu)
	{
        log.info ("Adicionando Usuario: " + nombre);
        Usuario Usuario = pp.adicionarUsuario(nombre, cedula, relacionu);
        log.info ("Adicionando Usuario: " + Usuario);
        return Usuario;
	}

	/**
	 * Elimina un Usuario por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del Usuario a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarUsuarioPorNombre (String nombre)
	{
        log.info ("Eliminando Usuario por nombre: " + nombre);
        long resp = pp.eliminarUsuarioPorNombre (nombre);
        log.info ("Eliminando Usuario por nombre: " + resp + " tuplas eliminadas");
        return resp;
	}

	/**
	 * Elimina un Usuario por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idUsuario - El identificador del Usuario a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarUsuarioPorId (long idUsuario)
	{
        log.info ("Eliminando Usuario por id: " + idUsuario);
        long resp = pp.eliminarUsuarioPorId (idUsuario);
        log.info ("Eliminando Usuario por Id: " + resp + " tuplas eliminadas");
        return resp;
	}

	/**
	 * Encuentra un Usuario y su información básica, según su identificador
	 * @param idUsuario - El identificador del Usuario buscado
	 * @return Un objeto Usuario que corresponde con el identificador buscado y lleno con su información básica
	 * 			null, si un Usuario con dicho identificador no existe
	 */
	public Usuario darUsuarioPorId (long idUsuario)
	{
        log.info ("Dar información de un Usuario por id: " + idUsuario);
        Usuario Usuario = pp.darUsuarioPorId (idUsuario);
        log.info ("Buscando Usuario por Id: " + Usuario != null ? Usuario : "NO EXISTE");
        return Usuario;
	}

	/**
	 * Encuentra la información básica de los usuarios, según su nombre
	 * @param nombre - El nombre de Usuario a buscar
	 * @return Una lista de usuarios con su información básica, donde todos tienen el nombre buscado.
	 * 	La lista vacía indica que no existen usuarios con ese nombre
	 */
	public List<Usuario> darusuariosPorNombre (String nombre)
	{
        log.info ("Dar información de usuarios por nombre: " + nombre);
        List<Usuario> usuarios = pp.darUsuariosPorNombre (nombre);
        log.info ("Dar información de usuarios por nombre: " + usuarios.size() + " usuarios con ese nombre existentes");
        return usuarios;
 	}

	/**
	 * Encuentra la información básica de los usuarios, según su nombre y los devuelve como VO
	 * @param nombre - El nombre de Usuario a buscar
	 * @return Una lista de usuarios con su información básica, donde todos tienen el nombre buscado.
	 * 	La lista vacía indica que no existen usuarios con ese nombre
	 */
	public List<VOUsuario> darVOusuariosPorNombre (String nombre)
	{
        log.info ("Generando VO de usuarios por nombre: " + nombre);
        List<VOUsuario> vousuarios = new LinkedList<VOUsuario> ();
       for (Usuario bdor : pp.darUsuariosPorNombre (nombre))
       {
          	vousuarios.add (bdor);
       }
       log.info ("Generando los VO de usuarios: " + vousuarios.size() + " usuarios existentes");
      return vousuarios;
 	}


	/**
	 * Encuentra todos los usuarios en alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Usuario con todos las usuarios que conoce la aplicación, llenos con su información básica
	 */
	public List<Usuario> darUsuarios ()
	{
        log.info ("Listando usuarios");
        List<Usuario> usuarios = pp.darUsuarios ();	
        log.info ("Listando usuarios: " + usuarios.size() + " usuarios existentes");
        return usuarios;
	}
	
	/**
	 * Encuentra todos los usuarios en alohandes y los devuelve como VOUsuario
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOUsuario con todos las usuarios que conoce la aplicación, llenos con su información básica
	 */
	public List<VOUsuario> darVOusuarios ()
	{
        log.info ("Generando los VO de usuarios");
         List<VOUsuario> vousuarios = new LinkedList<VOUsuario> ();
        for (Usuario bdor : pp.darUsuarios ())
        {
        	vousuarios.add (bdor);
        }
        log.info ("Generando los VO de usuarios: " + vousuarios.size() + " usuarios existentes");
       return vousuarios;
	}
	
	
	public long[] limpiarAlohandes ()
	{
        log.info ("Limpiando la BD de Alohandes");
        long [] borrrados = pp.limpiarAlohandes();	
        log.info ("Limpiando la BD de Alohandes: Listo!");
        return borrrados;
	}
	
	public List<Reserva> devolverTablaReserva()
	{
		log.info ("Devolver la tabla de reservas");
        List<Reserva> resp = pp.devolverTablaReservas();
        log.info ("Devolviendo la tabla de reservas: listo!");
        return resp;

	}

	public List<Alojamiento> devolverTablaAlojamiento()
	{
		log.info ("Devolver la tabla de alojamientos");
        List<Alojamiento> resp = pp.devolverTablaAlojamiento();
        log.info ("Devolviendo la tabla de alojamientos: listo!");
        return resp;

	}
}
