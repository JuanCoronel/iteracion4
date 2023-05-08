package uniandes.isis2304.alohandes.persistencia;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uniandes.isis2304.alohandes.negocio.Alojamiento;
import uniandes.isis2304.alohandes.negocio.Alojdeshabilitados;
import uniandes.isis2304.alohandes.negocio.Contrato;
import uniandes.isis2304.alohandes.negocio.Hostal;
import uniandes.isis2304.alohandes.negocio.Hotel;
import uniandes.isis2304.alohandes.negocio.Operador;
import uniandes.isis2304.alohandes.negocio.Particular;
import uniandes.isis2304.alohandes.negocio.Propuesta;
import uniandes.isis2304.alohandes.negocio.Reserva;
import uniandes.isis2304.alohandes.negocio.ResidenciaU;
import uniandes.isis2304.alohandes.negocio.Servicio;
import uniandes.isis2304.alohandes.negocio.Usuario;



public class PersistenciaAlohandes 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PersistenciaAlohandes.class.getName());
	
	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el único objeto de la clase - Patrón SINGLETON
	 */
	private static PersistenciaAlohandes instance;
	
	/**
	 * Fábrica de Manejadores de persistencia, para el manejo correcto de las transacciones
	 */
	private PersistenceManagerFactory pmf;
	
	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden:
	 * Secuenciador, tipoBebida, bebida, bar, bebedor, gustan, sirven y visitan
	 */
	private List <String> tablas;
	
	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaAlohandes
	 */
	private SQLUtil sqlUtil;

	private SQLAlojamiento sqlAlojamiento;
	private SQLContrato sqlContrato;
	private SQLHostal sqlHostal;
	private SQLHotel sqlHotel;
	private SQLOperador sqlOperador;
	private SQLParticular sqlParticular;
	private SQLPropuesta sqlPropuesta;
	private SQLReserva sqlReserva;
	private SQLResidenciaU sqlResidenciaU;
	private SQLServicio sqlServicio;
	private SQLUsuario sqlUsuario;
    private SQLAlojdeshabilitados sqlAlojDeshabilitado;
	
	
	/* ****************************************************************
	 * 			Métodos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */
	private PersistenciaAlohandes ()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("Alohandes");		
		crearClasesSQL ();
		
		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("ALOHANDES_SEQUENCE");
		tablas.add ("ALOJAMIENTO");
		tablas.add ("CONTRATO");
		tablas.add ("HOSTAL");
		tablas.add ("HOTEL");
		tablas.add ("OPERADOR");
		tablas.add ("PARTICULAR");
		tablas.add ("PROPUESTA");
		tablas.add ("RESERVA");
		tablas.add ("RESIDENCIAU");
		tablas.add ("SERVICIO");
		tablas.add ("USUARIO");
        tablas.add("ALOJDESHABILITADOS");

}

	/**
	 * Constructor privado, que recibe los nombres de las tablas en un objeto Json - Patrón SINGLETON
	 * @param tableConfig - Objeto Json que contiene los nombres de las tablas y de la unidad de persistencia a manejar
	 */
	private PersistenciaAlohandes (JsonObject tableConfig)
	{
		crearClasesSQL ();
		tablas = leerNombresTablas (tableConfig);
		
		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);
	}

	/**
	 * @return Retorna el único objeto PersistenciaAlohandes existente - Patrón SINGLETON
	 */
	public static PersistenciaAlohandes getInstance ()
	{
		if (instance == null)
		{
			instance = new PersistenciaAlohandes ();
		}
		return instance;
	}
	
	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del objeto tableConfig
	 * @param tableConfig - El objeto JSON con los nombres de las tablas
	 * @return Retorna el único objeto PersistenciaAlohandes existente - Patrón SINGLETON
	 */
	public static PersistenciaAlohandes getInstance (JsonObject tableConfig)
	{
		if (instance == null)
		{
			instance = new PersistenciaAlohandes (tableConfig);
		}
		return instance;
	}

	/**
	 * Cierra la conexión con la base de datos
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}
	
	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList <String> ();
		for (JsonElement nom : nombres)
		{
			resp.add (nom.getAsString ());
		}
		
		return resp;
	}
	
	/**
	 * Crea los atributos de clases de apoyo SQL Parranderos
	 */
	private void crearClasesSQL ()
	{
		sqlAlojamiento = new SQLAlojamiento(this);
		sqlContrato= new SQLContrato(this);
		sqlHostal= new SQLHostal(this);
		sqlHotel = new SQLHotel(this);
		sqlOperador = new SQLOperador(this);
		sqlParticular = new SQLParticular (this);
		sqlPropuesta = new SQLPropuesta(this);
		sqlReserva = new SQLReserva(this);
		sqlResidenciaU = new SQLResidenciaU(this);
		sqlServicio = new SQLServicio(this);		
		sqlUsuario = new SQLUsuario(this);
        sqlAlojDeshabilitado = new SQLAlojdeshabilitados(this);
		sqlUtil = new SQLUtil(this);
	}


	public String darSeqAlohandes ()
	{
		return tablas.get (0);
	}

	public String darTablaAlojamiento ()
	{
		return tablas.get (1);
	}


	public String darTablaContrato ()
	{
		return tablas.get (2);
	}


	public String darTablaHostal ()
	{
		return tablas.get (3);
	}


	public String darTablaHotel ()
	{
		return tablas.get (4);
	}


	public String darTablaOperador ()
	{
		return tablas.get (5);
	}


	public String darTablaParticular ()
	{
		return tablas.get (6);
	}


	public String darTablaPropuesta ()
	{
		return tablas.get (7);
	}


	public String darTablaReserva ()
	{
		return tablas.get (8);
	}


	public String darTablaResidenciaU ()
	{
		return tablas.get (9);
	}


	public String darTablaServicio ()
	{
		return tablas.get (10);
	}


	public String darTablaUsuario ()
	{
		return tablas.get (11);
	}

    public String darTablaAlojDeshabilitado ()
	{
		return tablas.get (12);
	}
	

	private long nextval ()
	{
        long resp = sqlUtil.nextval (pmf.getPersistenceManager());
        log.trace ("Generando secuencia: " + resp);
        return resp;
    }

	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/* ****************************************************************
	 * 			Métodos para manejar ALOJAMIENTOS
	 *****************************************************************/

	
	public Alojamiento adicionarAlojamiento(int id_alojamiento, int operador, int capacidad, int precio, String relacion_universidad,
    String horarios_recepcion, int precio_administracion, int precio_seguro, String nombre_alojamiento)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlAlojamiento.adicionarAlojamiento(pm, id_alojamiento, operador, capacidad, precio, relacion_universidad, horarios_recepcion, precio_administracion, precio_seguro, nombre_alojamiento);
            tx.commit();
            
            log.trace ("Inserción de alojamiento: " + nombre_alojamiento + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Alojamiento (id_alojamiento, operador, capacidad, precio, relacion_universidad, horarios_recepcion, precio_administracion, precio_seguro, nombre_alojamiento);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

    public boolean checkearDispAlojamiento(long id_aloj)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            boolean disponible = sqlAlojDeshabilitado.checkearDispAlojamiento(pm, id_aloj);
            tx.commit();
            
            log.trace ("Checkeando...");
            
            return disponible;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return false;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

    //@RF10
    public long rehabilitarAlojamiento(long id_aloj)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long alojamiento = sqlAlojDeshabilitado.rehabilitarAlojamiento(pm, id_aloj);
            tx.commit();
            
            log.trace ("Checkeando...");
            
            return alojamiento;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


    //RF9
    public Alojdeshabilitados adicionarAlojamientoDeshabilitado(long id_aloj, Timestamp fecha, String evento)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlAlojDeshabilitado.adicionarAlojamientoDeshabilitado(pm, id_aloj, fecha, evento);
            tx.commit();
            
            log.trace ("Inserción de alojamiento deshabilitado: " + id_aloj + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Alojdeshabilitados(id_aloj, fecha, evento);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

    //RF6
	public long eliminarAlojamientoPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlAlojamiento.eliminarAlojamientosPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}




	public int eliminarAlojamientoPorId (int id_alojamiento) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            int resp = sqlAlojamiento.eliminarAlojamientoPorId(pm, id_alojamiento);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public List<Alojamiento> darAlojamientos ()
	{
		return sqlAlojamiento.darAlojamientos(pmf.getPersistenceManager());
	}
 

	public List<Alojamiento> darAlojamientosPorNombre (String nombre)
	{
		return sqlAlojamiento.darAlojamientosPorNombre(pmf.getPersistenceManager(), nombre);
	}
 

	public Alojamiento darAlojamientoPorId (long idAlojamiento)
	{
		return sqlAlojamiento.darAlojamientoPorId (pmf.getPersistenceManager(), idAlojamiento);
	}

    public long actualizarCapacidadAlojamiento (long idAlojamiento, long capacidad)
	{
		return sqlAlojamiento.actualizarCapacidadAlojamiento(pmf.getPersistenceManager(), idAlojamiento, capacidad);
	}
 
	/* ****************************************************************
	 * 			Métodos para manejar CONTRATOS
	 *****************************************************************/
	public Contrato adicionarContrato(long id_contrato, String contratista, long Alojamiento,
    String registro_legal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlContrato.adicionarContrato(pm, id_contrato, contratista, Alojamiento, registro_legal);
            tx.commit();
            
            log.trace ("Inserción de Contrato: " + id_contrato + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Contrato (id_contrato, contratista, Alojamiento, registro_legal);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarContratoPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlContrato.eliminarContratosPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarContratoPorId (long id_Contrato) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlContrato.eliminarContratoPorId(pm, id_Contrato);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public List<Contrato> darContratos ()
	{
		return sqlContrato.darContratos(pmf.getPersistenceManager());
	}
 

	public List<Contrato> darContratosPorNombre (String nombre)
	{
		return sqlContrato.darContratosPorNombre(pmf.getPersistenceManager(), nombre);
	}
 

	public Contrato darContratoPorId (long idContrato)
	{
		return sqlContrato.darContratoPorId (pmf.getPersistenceManager(), idContrato);
	}

	/* ****************************************************************
	 * 			Métodos para manejar HOSTALES
	 *****************************************************************/
	public Hostal adicionarHostal(long id_hostal, String nombre_hostal, String registro_legal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlHostal.adicionarHostal(pm, id_hostal, nombre_hostal, registro_legal);
            tx.commit();
            
            log.trace ("Inserción de Hostal: " + nombre_hostal + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Hostal (id_hostal, nombre_hostal, registro_legal);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarHostalPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlHostal.eliminarHostalesPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarHostalPorId (long id_Hostal) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlHostal.eliminarHostalPorId(pm, id_Hostal);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public List<Hostal> darHostals ()
	{
		return sqlHostal.darHostales(pmf.getPersistenceManager());
	}
 

	public List<Hostal> darHostalsPorNombre (String nombre)
	{
		return sqlHostal.darHostalesPorNombre(pmf.getPersistenceManager(), nombre);
	}
 

	public Hostal darHostalPorId (long idHostal)
	{
		return sqlHostal.darHostalPorId (pmf.getPersistenceManager(), idHostal);
	}




	 /* ****************************************************************
	 * 			Métodos para manejar HOTELES
	 *****************************************************************/
	public Hotel adicionarHotel(long id_Hotel, String nombre_hotel, String registro_legal )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlHotel.adicionarHotel(pm, id_Hotel, nombre_hotel, registro_legal);
            tx.commit();
            
            log.trace ("Inserción de Hotel: " + nombre_hotel + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Hotel (id_Hotel, nombre_hotel, registro_legal);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarHotelPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlHotel.eliminarHotelsPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarHotelPorId (long id_Hotel) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlHotel.eliminarHotelPorId(pm, id_Hotel);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public List<Hotel> darHotels ()
	{
		return sqlHotel.darHotels(pmf.getPersistenceManager());
	}
 

	public List<Hotel> darHotelsPorNombre (String nombre)
	{
		return sqlHotel.darHotelsPorNombre(pmf.getPersistenceManager(), nombre);
	}
 

	public Hotel darHotelPorId (long idHotel)
	{
		return sqlHotel.darHotelPorId (pmf.getPersistenceManager(), idHotel);
	}



	 /* ****************************************************************
	 * 			Métodos para manejar OPERADORES
	 *****************************************************************/

	 public Operador adicionarOperador(int id_Operador, String nombre_Operador)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlOperador.adicionarOperador(pm, id_Operador, nombre_Operador);
            tx.commit();
            
            log.trace ("Inserción de Operador: " + nombre_Operador + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Operador (id_Operador, nombre_Operador);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarOperadorPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlOperador.eliminarOperadorsPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarOperadorPorId (int id_Operador) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlOperador.eliminarOperadorPorId(pm, id_Operador);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public List<Operador> darOperadors ()
	{
        
		return sqlOperador.darOperadors(pmf.getPersistenceManager());
	}
 

	public List<Operador> darOperadorsPorNombre (String nombre)
	{
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            List<Operador> resp = sqlOperador.darOperadorsPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
 

	public Operador darOperadorPorId (long idOperador)
	{
		return sqlOperador.darOperadorPorId (pmf.getPersistenceManager(), idOperador);
	}


	 /* ****************************************************************
	 * 			Métodos para manejar PARTICULARES
	 *****************************************************************/
public Particular adicionarParticular(long id_Particular, String nombre_Particular)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlParticular.adicionarParticular(pm, id_Particular, nombre_Particular);
            tx.commit();
            
            log.trace ("Inserción de Particular: " + nombre_Particular + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Particular (id_Particular, nombre_Particular);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarParticularPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlParticular.eliminarParticularsPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarParticularPorId (long id_Particular) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlParticular.eliminarParticularPorId(pm, id_Particular);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public List<Particular> darParticulars ()
	{
		return sqlParticular.darParticulars(pmf.getPersistenceManager());
	}
 

	public List<Particular> darParticularsPorNombre (String nombre)
	{
		return sqlParticular.darParticularsPorNombre(pmf.getPersistenceManager(), nombre);
	}
 

	public Particular darParticularPorId (long idParticular)
	{
		return sqlParticular.darParticularPorId (pmf.getPersistenceManager(), idParticular);
	}

	 /* ****************************************************************
	 * 			Métodos para manejar PROPUESTAS
	 *****************************************************************/

	public Propuesta adicionarPropuesta(long id_Propuesta, String nombre_alojamiento, String info_alojamiento)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlPropuesta.adicionarPropuesta(pm, id_Propuesta, nombre_alojamiento, info_alojamiento);
            tx.commit();
            
            log.trace ("Inserción de Propuesta en alojamiento de nombre: " + nombre_alojamiento + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Propuesta (id_Propuesta, nombre_alojamiento, info_alojamiento);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarPropuestaPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlPropuesta.eliminarPropuestasPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarPropuestaPorId (long id_Propuesta) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlPropuesta.eliminarPropuestaPorId(pm, id_Propuesta);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public List<Propuesta> darPropuestas ()
	{
		return sqlPropuesta.darPropuestas(pmf.getPersistenceManager());
	}
 

	public List<Propuesta> darPropuestasPorNombre (String nombre)
	{
		return sqlPropuesta.darPropuestasPorNombre(pmf.getPersistenceManager(), nombre);
	}
 

	public Propuesta darPropuestaPorId (long idPropuesta)
	{
		return sqlPropuesta.darPropuestaPorId (pmf.getPersistenceManager(), idPropuesta);
	}

	 /* ****************************************************************
	 * 			Métodos para manejar RESERVAS
	 *****************************************************************/

	public Reserva adicionarReserva(int id_Reserva, String tipo_contrato, Timestamp fecha_llegada, Timestamp fecha_salida, long costo, long usuario, long alojamiento_reservado)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlReserva.adicionarReserva(pm, id_Reserva,  tipo_contrato,  fecha_llegada, fecha_salida, costo, usuario, alojamiento_reservado);
            tx.commit();
            
            log.trace ("Inserción de Reserva: " + id_Reserva + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Reserva (id_Reserva,  tipo_contrato,  fecha_llegada, fecha_salida, costo, usuario, alojamiento_reservado);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarReservaPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlReserva.eliminarReservasPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarReservaPorId (long id_Reserva) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlReserva.eliminarReservaPorId(pm, id_Reserva);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public List<Reserva> darReservas ()
	{
		return sqlReserva.darReservas(pmf.getPersistenceManager());
	}
 

	public List<Reserva> darReservasPorNombre (String nombre)
	{
		return sqlReserva.darReservasPorNombre(pmf.getPersistenceManager(), nombre);
	}
 

	public Reserva darReservaPorId (long idReserva)
	{
		return sqlReserva.darReservaPorId (pmf.getPersistenceManager(), idReserva);
	}

	 /* ****************************************************************
	 * 			Métodos para manejar RESIDENCIAS U
	 *****************************************************************/
	public ResidenciaU adicionarResidenciaU(long id_ResidenciaU, String nombreResidenciaU, String registroLegal  )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlResidenciaU.adicionarResidenciaU(pm, id_ResidenciaU, nombreResidenciaU, registroLegal);
            tx.commit();
            
            log.trace ("Inserción de ResidenciaU: " + nombreResidenciaU + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new ResidenciaU (id_ResidenciaU, nombreResidenciaU, registroLegal);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarResidenciaUPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlResidenciaU.eliminarResidenciaUsPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarResidenciaUPorId (long id_ResidenciaU) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlResidenciaU.eliminarResidenciaUPorId(pm, id_ResidenciaU);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public List<ResidenciaU> darResidenciaUs ()
	{
		return sqlResidenciaU.darResidenciaUs(pmf.getPersistenceManager());
	}
 

	public List<ResidenciaU> darResidenciaUsPorNombre (String nombre)
	{
		return sqlResidenciaU.darResidenciaUsPorNombre(pmf.getPersistenceManager(), nombre);
	}
 

	public ResidenciaU darResidenciaUPorId (long idResidenciaU)
	{
		return sqlResidenciaU.darResidenciaUPorId (pmf.getPersistenceManager(), idResidenciaU);
	}

	/* ****************************************************************
	 * 			Métodos para manejar SERVICIOS
	 *****************************************************************/
	public Servicio adicionarServicio(long id_Servicio, String nombreServicio, long alojamiento)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlServicio.adicionarServicio(pm, id_Servicio, nombreServicio, alojamiento);
            tx.commit();
            
            log.trace ("Inserción de Servicio: " + nombreServicio + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Servicio (id_Servicio, nombreServicio, alojamiento);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarServicioPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlServicio.eliminarServiciosPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarServicioPorId (long id_Servicio) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlServicio.eliminarServicioPorId(pm, id_Servicio);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public List<Servicio> darServiciosDeUnAlojamiento (long id_aloj)
	{
		return sqlServicio.darServiciosDeUnAlojamiento(pmf.getPersistenceManager(), id_aloj);
	}

    public long darUltimoIdReservas()
    {
        return sqlServicio.darUltimoIdReservas(pmf.getPersistenceManager());
    }

    public List<Servicio> darServicios ()
	{
		return sqlServicio.darServicios(pmf.getPersistenceManager());
	}
 

	public List<Servicio> darServiciosPorNombre (String nombre)
	{
		return sqlServicio.darServiciosPorNombre(pmf.getPersistenceManager(), nombre);
	}
 

	public Servicio darServicioPorId (long idServicio)
	{
		return sqlServicio.darServicioPorId (pmf.getPersistenceManager(), idServicio);
	}

	/* ****************************************************************
	 * 			Métodos para manejar USUARIOS
	 *****************************************************************/
	public Usuario adicionarUsuario(String nombre, long cedula, String relacion_universidad)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlUsuario.adicionarUsuario(pm, nombre, cedula, relacion_universidad);
            tx.commit();
            
            log.trace ("Inserción de Usuario: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Usuario (nombre, cedula, relacion_universidad);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarUsuarioPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlUsuario.eliminarUsuariosPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public long eliminarUsuarioPorId (long id_Usuario) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlUsuario.eliminarUsuarioPorId(pm, id_Usuario);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	public List<Usuario> darUsuarios ()
	{
		return sqlUsuario.darUsuarios(pmf.getPersistenceManager());
	}
 

	public List<Usuario> darUsuariosPorNombre (String nombre)
	{
		return sqlUsuario.darUsuariosPorNombre(pmf.getPersistenceManager(), nombre);
	}
 

	public Usuario darUsuarioPorId (long idUsuario)
	{
		return sqlUsuario.darUsuarioPorId (pmf.getPersistenceManager(), idUsuario);
	}

    public long [] limpiarAlohandes ()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long [] resp = sqlUtil.limpiarAlohandes (pm);
            tx.commit ();
            log.info ("Borrada la base de datos");
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new long[] {-1, -1, -1, -1, -1, -1, -1};
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
		
	}

    public List<Reserva> devolverTablaReservas ()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            List<Reserva> resp = sqlUtil.devolverTablaReserva(pm);
            tx.commit ();
            log.info ("Retornar la tabla de reservas");
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
		
	}

    public List<Alojamiento> devolverTablaAlojamiento()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            List<Alojamiento> resp = sqlUtil.devolverTablaAlojamiento(pm);
            tx.commit ();
            log.info ("Retornar la tabla de reservas");
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
		
	}

	
 }
