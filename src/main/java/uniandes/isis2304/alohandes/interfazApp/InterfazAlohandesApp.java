/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 *
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Alohandes Uniandes
 * @version 1.0
 * @author Juan Coronel
 * @author Pablo Martinez
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.alohandes.interfazApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.alohandes.negocio.*;



/**
 * Clase principal de la interfaz
 * @author Germán Bravo
 */
@SuppressWarnings("serial")

public class InterfazAlohandesApp extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazAlohandesApp.class.getName());
	
	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json"; 
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
    /**
     * Asociación a la clase principal del negocio.
     */

	private Alohandes alohandes;
    
	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
    /**
     * Objeto JSON con la configuración de interfaz de la app.
     */
    private JsonObject guiConfig;
    
    /**
     * Panel de despliegue de interacción para los requerimientos
     */
    private PanelDatos panelDatos;
    
    /**
     * Menú de la aplicación
     */
    private JMenuBar menuBar;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
    /**
     * Construye la ventana principal de la aplicación. <br>
     * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
     */
    public InterfazAlohandesApp( )
    {
        // Carga la configuración de la interfaz desde un archivo JSON
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        
        // Configura la apariencia del frame que contiene la interfaz gráfica
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        alohandes = new Alohandes (tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicació, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
    
    /**
     * Método para configurar el frame principal de la aplicación
     */
    private void configurarFrame(  )
    {
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	
    	if ( guiConfig == null )
    	{
    		log.info ( "Se aplica configuración por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
    	
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );

        setTitle( titulo );
		setSize ( ancho, alto);        
    }

    /**
     * Método para crear el menú de la aplicación con base em el objeto JSON leído
     * Genera una barra de menú y los menús con sus respectivas opciones
     * @param jsonMenu - Arreglo Json con los menùs deseados
     */
    private void crearMenu(  JsonArray jsonMenu )
    {    	
    	// Creación de la barra de menús
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu)
        {
        	// Creación de cada uno de los menús
        	JsonObject jom = men.getAsJsonObject(); 

        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	
        	JMenu menu = new JMenu( menuTitle);
        	
        	for (JsonElement op : opciones)
        	{       	
        		// Creación de cada una de las opciones del menú
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }
    
	/* ****************************************************************
	 * 			CRUD de Operador
	 *****************************************************************/
    /**
	 * @RF1
     * Adiciona un Operador con la información dada por el usuario
     * Se crea una nueva tupla de Operador en la base de datos, si un Operador con ese nombre no existía
     */
    public void adicionarOperador( )
    {
    	try 
    	{
			String idString = JOptionPane.showInputDialog (this, "id del operador?", "Adicionar Operador", JOptionPane.QUESTION_MESSAGE);
			int id = Integer.parseInt(idString);
    		String nombreTipo = JOptionPane.showInputDialog (this, "Nombre del operador?", "Adicionar Operador", JOptionPane.QUESTION_MESSAGE);
    		if (nombreTipo != null)
    		{
        		VOOperador tb = alohandes.adicionarOperador(id, nombreTipo);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un Operador con nombre: " + nombreTipo);
        		}
        		String resultado = "En adicionarOperador\n\n";
        		resultado += "Operador adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarOperador( )
    {
    	try 
    	{
			List <VOOperador> lista = alohandes.darVOOperadores();

			String resultado = "En listarOperador";
			resultado +=  "\n" + listarTiposBebida (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el Operador con el identificador dado po el usuario
     * Cuando dicho Operador no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarOperadorPorId( )
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id del operador?", "Borrar Operador por Id", JOptionPane.QUESTION_MESSAGE);
    		int id  = Integer.parseInt(idTipoStr);
			if (idTipoStr != null)
    		{
    			
    			long tbEliminados = alohandes.eliminarOperadorPorId (id);

    			String resultado = "En eliminar Operador\n\n";
    			resultado += tbEliminados + " Operadores eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Busca el Operador con el nombre indicado por el usuario y lo muestra en el panel de datos
     */
    public void buscarOperadorPorNombre( )
    {
    	try 
    	{
    		String nombreTb = JOptionPane.showInputDialog (this, "Nombre del Operador?", "Buscar Operador por nombre", JOptionPane.QUESTION_MESSAGE);
    		if (nombreTb != null)
    		{
    			List<Operador> Operadores = alohandes.darOperadorPorNombre (nombreTb);
    			String resultado = "En buscar Operador por nombre\n\n";
    			if (Operadores != null)
    			{
        			resultado += "El Operador es: " + Operadores.get(0);
    			}
    			else
    			{
        			resultado += "Un Operador con nombre: " + nombreTb + " NO EXISTE\n";    				
    			}
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	/* ****************************************************************
	 * 			CRUD de Propuesta
	 *****************************************************************/
    /**
	 * @RF2
     * Adiciona una Propuesta con la información dada por el usuario
     * Se crea una nueva tupla Propuesta en la base de datos, si una Propuesta con ese nombre no existía
     */
    public void adicionarPropuesta( )
    {
    	try 
    	{
			String idString = JOptionPane.showInputDialog (this, "id de la propuesta?", "Adicionar Propuesta", JOptionPane.QUESTION_MESSAGE);
			int id = Integer.parseInt(idString);
    		String nombreTipo = JOptionPane.showInputDialog (this, "Nombre del alojamiento a proponer?", "Adicionar Propuesta", JOptionPane.QUESTION_MESSAGE);
			String info = JOptionPane.showInputDialog (this, "Informacion del alojamiento?", "Adicionar Propuesta", JOptionPane.QUESTION_MESSAGE);
    		if (nombreTipo != null)
    		{
        		VOPropuesta tb = alohandes.adicionarPropuesta(id, nombreTipo, info);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear una Propuesta con nombre: " + nombreTipo);
        		}
        		String resultado = "En adicionarPropuesta\n\n";
        		resultado += "Propuesta adicionada exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	

	/* ****************************************************************
	 * 			CRUD de Usuarios
	 *****************************************************************/
    /**
	 * @RF3
     * Adiciona una Usuario con la información dada por el usuario
     * Se crea una nueva tupla del Usuario en la base de datos
     */
    public void adicionarUsuario( )
    {
    	try 
    	{
			String nombre = JOptionPane.showInputDialog (this, "nombre?", "Adicionar Nombre", JOptionPane.QUESTION_MESSAGE);
			
    		String idString = JOptionPane.showInputDialog (this, "Cedula?", "Adicionar Nombre", JOptionPane.QUESTION_MESSAGE);
			int id = Integer.parseInt(idString);
			String relacionU = JOptionPane.showInputDialog (this, "Relacion con la universidad?", "Adicionar Nombre", JOptionPane.QUESTION_MESSAGE);
    		if (nombre != null)
    		{
        		VOUsuario tb = alohandes.adicionarUsuario(nombre, id, relacionU);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un usuario con nombre: " + nombre);
        		}
        		String resultado = "En adicionarUsuario\n\n";
        		resultado += "Usuario adicionada exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	/* ****************************************************************
	 * 			CRUD de Reservas
	 *****************************************************************/
    /**
	 * @RF4
     * Adiciona una Usuario con la información dada por el usuario
     * Se crea una nueva tupla del Usuario en la base de datos
     */
    public void adicionarReserva()
    {
    	try 
    	{
			String resultado = "En adicionarReserva\n\n";

    		String idString = JOptionPane.showInputDialog (this, "id de la reserva?", "Adicionar Reserva", JOptionPane.QUESTION_MESSAGE);
			int id = Integer.parseInt(idString);

			String tipo_contrato = JOptionPane.showInputDialog (this, "tipo de contrato?", "Adicionar Reserva", JOptionPane.QUESTION_MESSAGE);
			String fecha_llegada = JOptionPane.showInputDialog (this, "fecha de llegada 'ej: 2023-05-01 12:30:45'?", "Adicionar Reserva", JOptionPane.QUESTION_MESSAGE);	
			String fecha_salida = JOptionPane.showInputDialog (this, "fecha de salida 'ej: 2023-05-01 12:30:45'?", "Adicionar Reserva", JOptionPane.QUESTION_MESSAGE);
    		

			String usuario = JOptionPane.showInputDialog (this, "cedula del usuario?", "Adicionar Reserva", JOptionPane.QUESTION_MESSAGE);
			int cedula_int = Integer.parseInt(usuario);
			
			String alojamiento_reservado = JOptionPane.showInputDialog (this, "id del alojamiento reservado?", "Adicionar Reserva", JOptionPane.QUESTION_MESSAGE);
			int id_aloj = Integer.parseInt(alojamiento_reservado);

			//[Verificacion regla de negocio]: Ver si el alojamiento esta habilitado o no
			if (!alohandes.checkearDispAlojamiento(id_aloj))
			{
				resultado += "Error de integridad: el alojamiento con id '"+id_aloj+"' no esta habilitado actualmente!";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{

			//[Verificacion regla de negocio]: el usuario no puede reservar mas de 1 vez en un dia
			Usuario user = alohandes.darUsuarioPorId(cedula_int);
			boolean cent = false;

			List<Reserva> reservas = alohandes.darReservas();
			int i = 0;
			while (i<reservas.size() && cent==false)
			{
				if (reservas.get(i).getUsuario()==cedula_int)
				{
					cent = true;
					resultado += ("\nError: El usuario '"+ user.getNombre()+"' ya ha hecho una reserva en el dia actual.");
					resultado += "\n Operación terminada\n";
					panelDatos.actualizarInterfaz(resultado);
				}
				i++;
			}

			//[Verificacion regla de negocio]: la reserva no puede superar la capacidad del alojamiento
			Alojamiento aloj = alohandes.darAlojamientoPorId(id_aloj);
			if ((aloj.getCapacidad()-1)<0)
			{
				cent = true;
				resultado += ("\nError: La reserva actual supera el aforo maximo del alojamiento.");
				resultado += "\n Operación terminada\n";
				panelDatos.actualizarInterfaz(resultado);
			}


			if (idString != null && cent==false)
    		{
				long costoTotal = aloj.getPrecio() + aloj.getPrecioAdministracion() + aloj.getPrecioSeguro();
        		VOReserva tb = alohandes.adicionarReserva(id, tipo_contrato, fecha_llegada, fecha_salida, (int)costoTotal, cedula_int, id_aloj);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear una reserva con id: " + idString);
        		}
        		
				//Disminuir el numero de la capacidad del alojamiento (-1)
				long capacidad = aloj.getCapacidad() - 1;
				alohandes.actualizarCapacidadAlojamiento(id_aloj, capacidad);

        		resultado += "\nReserva adicionada exitosamente: " + tb;
				resultado += "\nCosto total de la reserva: " + costoTotal+"000 COP";
    			resultado += "\nOperación terminada\n";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz(resultado);
    		}
		}
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	//@RF4 parametrizada para ser invocada por otros methodos
	public void adicionarReservaConParametros(int id, String tipoContrato, String fechallegada, String fechaSalida, int costo, int cedula, long id_aloj)
    {
    	try 
    	{	
			String resultado = "En adicionarReserva\n\n";
			boolean cent = false;

			//[Verificacion regla de negocio]: Ver si el alojamiento esta habilitado o no
			if (!alohandes.checkearDispAlojamiento(id_aloj))
			{
				resultado += "Error de integridad: el alojamiento con id '"+id_aloj+"' no esta habilitado actualmente!";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{


			//[Verficacion regla de negocio]: la reserva no puede superar la capacidad del alojamiento
			Alojamiento aloj = alohandes.darAlojamientoPorId(id_aloj);
			if ((aloj.getCapacidad()-1)<0)
			{
				cent = true;
				resultado += ("\nError: La reserva actual supera el aforo maximo del alojamiento.");
				resultado += "\n Operación terminada\n";
				panelDatos.actualizarInterfaz(resultado);
			}


			if (tipoContrato != null && cent==false)
    		{
        		VOReserva tb = alohandes.adicionarReservaLong(id, tipoContrato, fechallegada, fechaSalida, costo, cedula, id_aloj);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear una reserva con id: " + id);
        		}
        		
				//Disminuir el numero de la capacidad del alojamiento (-1)
				long capacidad = aloj.getCapacidad() - 1;
				alohandes.actualizarCapacidadAlojamiento(id_aloj, capacidad);

        		resultado += "\nReservas adicionadas exitosamente! La ultima reserva fue: " + tb;
    			resultado += "\n Operación terminada\n";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz(resultado);
    		}
		  }
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	/**
	 * @RF5
     * Borra de la base de datos la Reserva con el identificador dado por el usuario
     * la cancelacion tiene una multa segun el tiempo de cancelacion
     */
    public void eliminarReservaPorId( )
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id de la reserva?", "Borrar Reserva por Id", JOptionPane.QUESTION_MESSAGE);
    		int id  = Integer.parseInt(idTipoStr);

			if (idTipoStr != null)
    		{
				String resultado = "En eliminar Reserva\n\n";
				long tbEliminados = 0;

    			//Reserva a eliminar
				Reserva res = alohandes.darReservaPorId(id);

				//HORA DE CREACION DE LA RESERVA
				Timestamp fecha_creacion_timestamp = res.getfechaCreacion();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// Convertir el objeto Timestamp a una cadena de texto con el formato especificado
				String fechaHoraCreacionString = dateFormat.format(fecha_creacion_timestamp);
				resultado += ("\nHora y fecha de la creacion de la reserva: "+fechaHoraCreacionString);
				// Crear objeto LocalDateTime desde la cadena de texto
				DateTimeFormatter formatter0 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime fechaHora0 = LocalDateTime.parse(fechaHoraCreacionString, formatter0);
				// Crear objeto Instant a partir del LocalDateTime
				Instant instant0 = fechaHora0.atZone(ZoneId.systemDefault()).toInstant();
				// Obtener el timestamp en milisegundos
				long timestampMilisegundos0 = instant0.toEpochMilli();




				//HORA LLEGADA
				// Crear objeto SimpleDateFormat con el formato deseado
				// Convertir el objeto Timestamp a una cadena de texto con el formato especificado
				String fechaHoraString = dateFormat.format(res.getFechaLlegada());
				resultado += ("\nHora y fecha de la llegada a la reserva: "+fechaHoraString);
				// Crear objeto LocalDateTime desde la cadena de texto
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraString, formatter);
				// Crear objeto Instant a partir del LocalDateTime
				Instant instant = fechaHora.atZone(ZoneId.systemDefault()).toInstant();
				// Obtener el timestamp en milisegundos
				long timestampMilisegundos = instant.toEpochMilli();



				//HORA ACTUAL
				LocalDateTime fechaActual = LocalDateTime.now();
				DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String fechaHoraStringActual = fechaActual.format(formato);
				resultado += ("\nHora y fecha actual: "+fechaHoraStringActual);
				LocalDateTime fechaHora1 = LocalDateTime.parse(fechaHoraStringActual, formatter);
				// Crear objeto Instant a partir del LocalDateTime
				Instant instant1 = fechaHora1.atZone(ZoneId.systemDefault()).toInstant();
				// Obtener el timestamp en milisegundos
				long timestampMilisegundosActual = instant1.toEpochMilli();


				//TIEMPO QUE HA PASADO DESDE LA CREACION DE LA RESERVA
				long dias_creacion = timestampMilisegundosActual - timestampMilisegundos0;

				//VERIFICACION DEL TIEMPO Y TOMA DE DECISION
				// Calcular el tiempo total en milisegundos para 3 días
				long tresDiasEnMilisegundos = 3 * 24 * 60 * 60 * 1000L;
				// Calcular el tiempo total en milisegundos para 1 día
				long unDiaEnMilisegundos = 24 * 60 * 60 * 1000L;
				long fechaLlegadaMenos1dia = timestampMilisegundos-unDiaEnMilisegundos;
				Alojamiento aloj = alohandes.darAlojamientoPorId(res.getAlojamientoReservado());

				if (dias_creacion<tresDiasEnMilisegundos)
					{
						double multa = 0.1*(aloj.getPrecio() + aloj.getPrecioAdministracion() + aloj.getPrecioSeguro());
						resultado += ("\nHan pasado menos de 3 dias desde la creacion de la reserva. La multa es el 10% del costo = "+multa+"000 COP\nSe procede a eliminar la reserva con id: "+id+"\n\n");
						tbEliminados += alohandes.eliminarReservaPorId(id);
						
						//Liberamos un cupo en la capacidad del alojamiento (+1)
						long capacidad = aloj.getCapacidad() + 1;
						alohandes.actualizarCapacidadAlojamiento(aloj.getIdAlojamiento(), capacidad);
					}
				else if (tresDiasEnMilisegundos<timestampMilisegundosActual && timestampMilisegundosActual<fechaLlegadaMenos1dia)
			
					{
						double multa = 0.3*(aloj.getPrecio() + aloj.getPrecioAdministracion() + aloj.getPrecioSeguro());
						resultado += ("\nLa cancelacion esta entre la fecha limite y 1 dia antes de la fecha de llegada, la multa es del 30% = "+multa+"000 COP\nSe procede a eliminar la reserva con id: "+id+"\n\n");
						tbEliminados += alohandes.eliminarReservaPorId(id);
						//Liberamos un cupo en la capacidad del alojamiento (+1)
						long capacidad = aloj.getCapacidad() + 1;
						alohandes.actualizarCapacidadAlojamiento(aloj.getIdAlojamiento(), capacidad);
					}
				else
					{
						double multa = 0.5*(aloj.getPrecio() + aloj.getPrecioAdministracion() + aloj.getPrecioSeguro());
						resultado += ("\nHa pasado algun tiempo desde la fecha de llegada, por ende, la multa es del 50% = "+multa+"000 COP\nSe procede a eliminar la reserva con id: "+id+"\n\n");
						tbEliminados += alohandes.eliminarReservaPorId(id);
						//Liberamos un cupo en la capacidad del alojamiento (+1)
						long capacidad = aloj.getCapacidad() + 1;
						alohandes.actualizarCapacidadAlojamiento(aloj.getIdAlojamiento(), capacidad);
					}
    			
    			resultado += "\n"+tbEliminados + " Reservas eliminadas\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	/**
	 * @RF5 parametrizado
     * Borra de la base de datos la Reserva con el identificador dado por el usuario
     * la cancelacion tiene una multa segun el tiempo de cancelacion
     */
    public int eliminarReservaPorIdConParametros(int id)
    {
    	try 
    	{

			if (id >= 0)
    		{
				String resultado = "En eliminar Reserva Colectiva\n\n";


    			//Reserva a eliminar
				Reserva res = alohandes.darReservaPorId(id);

				//HORA DE CREACION DE LA RESERVA
				Timestamp fecha_creacion_timestamp = res.getfechaCreacion();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// Convertir el objeto Timestamp a una cadena de texto con el formato especificado
				String fechaHoraCreacionString = dateFormat.format(fecha_creacion_timestamp);
				resultado += ("\nHora y fecha de la creacion de la reserva: "+fechaHoraCreacionString);
				// Crear objeto LocalDateTime desde la cadena de texto
				DateTimeFormatter formatter0 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime fechaHora0 = LocalDateTime.parse(fechaHoraCreacionString, formatter0);
				// Crear objeto Instant a partir del LocalDateTime
				Instant instant0 = fechaHora0.atZone(ZoneId.systemDefault()).toInstant();
				// Obtener el timestamp en milisegundos
				long timestampMilisegundos0 = instant0.toEpochMilli();




				//HORA LLEGADA
				// Crear objeto SimpleDateFormat con el formato deseado
				// Convertir el objeto Timestamp a una cadena de texto con el formato especificado
				String fechaHoraString = dateFormat.format(res.getFechaLlegada());
				resultado += ("\nHora y fecha de la llegada a la reserva: "+fechaHoraString);
				// Crear objeto LocalDateTime desde la cadena de texto
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraString, formatter);
				// Crear objeto Instant a partir del LocalDateTime
				Instant instant = fechaHora.atZone(ZoneId.systemDefault()).toInstant();
				// Obtener el timestamp en milisegundos
				long timestampMilisegundos = instant.toEpochMilli();



				//HORA ACTUAL
				LocalDateTime fechaActual = LocalDateTime.now();
				DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String fechaHoraStringActual = fechaActual.format(formato);
				resultado += ("\nHora y fecha actual: "+fechaHoraStringActual);
				LocalDateTime fechaHora1 = LocalDateTime.parse(fechaHoraStringActual, formatter);
				// Crear objeto Instant a partir del LocalDateTime
				Instant instant1 = fechaHora1.atZone(ZoneId.systemDefault()).toInstant();
				// Obtener el timestamp en milisegundos
				long timestampMilisegundosActual = instant1.toEpochMilli();


				//TIEMPO QUE HA PASADO DESDE LA CREACION DE LA RESERVA
				long dias_creacion = timestampMilisegundosActual - timestampMilisegundos0;

				//VERIFICACION DEL TIEMPO Y TOMA DE DECISION
				// Calcular el tiempo total en milisegundos para 3 días
				long tresDiasEnMilisegundos = 3 * 24 * 60 * 60 * 1000L;
				// Calcular el tiempo total en milisegundos para 1 día
				long unDiaEnMilisegundos = 24 * 60 * 60 * 1000L;
				long fechaLlegadaMenos1dia = timestampMilisegundos-unDiaEnMilisegundos;
				Alojamiento aloj = alohandes.darAlojamientoPorId(res.getAlojamientoReservado());

				if (dias_creacion<tresDiasEnMilisegundos)
					{
						double multa = 0.1*(aloj.getPrecio() + aloj.getPrecioAdministracion() + aloj.getPrecioSeguro());
						resultado += ("\nHan pasado menos de 3 dias desde la creacion de la reserva. La multa es del 10%.\n Se procede a eliminar la reserva con id: "+id+"\n\n");
						alohandes.eliminarReservaPorId(id);
						
						
						//Liberamos un cupo en la capacidad del alojamiento (+1)
						long capacidad = aloj.getCapacidad() + 1;
						alohandes.actualizarCapacidadAlojamiento(aloj.getIdAlojamiento(), capacidad);
						panelDatos.actualizarInterfaz(resultado);
						return (int)multa;
					}
				else if (tresDiasEnMilisegundos<timestampMilisegundosActual && timestampMilisegundosActual<fechaLlegadaMenos1dia)
			
					{
						double multa = 0.3*(aloj.getPrecio() + aloj.getPrecioAdministracion() + aloj.getPrecioSeguro());
						resultado += ("\nLa cancelacion esta entre la fecha limite y 1 dia antes de la fecha de llegada, la multa es del 30%.\n Se procede a eliminar la reserva con id: "+id+"\n\n");
						alohandes.eliminarReservaPorId(id);
						//Liberamos un cupo en la capacidad del alojamiento (+1)
						long capacidad = aloj.getCapacidad() + 1;
						alohandes.actualizarCapacidadAlojamiento(aloj.getIdAlojamiento(), capacidad);
						panelDatos.actualizarInterfaz(resultado);
						return (int)multa;
					}
				else
					{
						double multa = 0.5*(aloj.getPrecio() + aloj.getPrecioAdministracion() + aloj.getPrecioSeguro());
						resultado += ("\nla multa es del 50%.\n Se procede a eliminar la reserva con id: "+id+"\n\n");
						alohandes.eliminarReservaPorId(id);
						//Liberamos un cupo en la capacidad del alojamiento (+1)
						long capacidad = aloj.getCapacidad() + 1;
						alohandes.actualizarCapacidadAlojamiento(aloj.getIdAlojamiento(), capacidad);
						panelDatos.actualizarInterfaz(resultado);
						return (int)multa;
					}
					
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
		return 0;
    }

	/**
	 * @RF5 parametrizado con la traza para @RF9
     * Migra de la base de datos la Reserva con el identificador dado por el usuario
     * No hay multa. Devuelve la traza de las reservas migradas. 
     */
    public Reserva eliminarReservaPorIdConParametrosYTraza(String resultado, int id)
    {
		//Reserva a eliminar
		Reserva res = alohandes.darReservaPorId(id);
    	try 
    	{

			if (id >= 0)
    		{

    			

				//HORA DE CREACION DE LA RESERVA
				Timestamp fecha_creacion_timestamp = res.getfechaCreacion();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// Convertir el objeto Timestamp a una cadena de texto con el formato especificado
				String fechaHoraCreacionString = dateFormat.format(fecha_creacion_timestamp);
				resultado += ("\nHora y fecha de la creacion de la reserva: "+fechaHoraCreacionString);
				// Crear objeto LocalDateTime desde la cadena de texto
				DateTimeFormatter formatter0 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime fechaHora0 = LocalDateTime.parse(fechaHoraCreacionString, formatter0);
				// Crear objeto Instant a partir del LocalDateTime
				Instant instant0 = fechaHora0.atZone(ZoneId.systemDefault()).toInstant();
				// Obtener el timestamp en milisegundos
				long timestampMilisegundos0 = instant0.toEpochMilli();




				//HORA LLEGADA
				// Crear objeto SimpleDateFormat con el formato deseado
				// Convertir el objeto Timestamp a una cadena de texto con el formato especificado
				String fechaHoraString = dateFormat.format(res.getFechaLlegada());
				resultado += ("\nHora y fecha de la llegada a la reserva: "+fechaHoraString);
				// Crear objeto LocalDateTime desde la cadena de texto
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraString, formatter);
				// Crear objeto Instant a partir del LocalDateTime
				Instant instant = fechaHora.atZone(ZoneId.systemDefault()).toInstant();
				// Obtener el timestamp en milisegundos
				long timestampMilisegundos = instant.toEpochMilli();



				//HORA ACTUAL
				LocalDateTime fechaActual = LocalDateTime.now();
				DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String fechaHoraStringActual = fechaActual.format(formato);
				resultado += ("\nHora y fecha actual: "+fechaHoraStringActual);
				LocalDateTime fechaHora1 = LocalDateTime.parse(fechaHoraStringActual, formatter);
				// Crear objeto Instant a partir del LocalDateTime
				Instant instant1 = fechaHora1.atZone(ZoneId.systemDefault()).toInstant();
				// Obtener el timestamp en milisegundos
				long timestampMilisegundosActual = instant1.toEpochMilli();


				//TIEMPO QUE HA PASADO DESDE LA CREACION DE LA RESERVA
				long dias_creacion = timestampMilisegundosActual - timestampMilisegundos0;

				//VERIFICACION DEL TIEMPO Y TOMA DE DECISION
				// Calcular el tiempo total en milisegundos para 3 días
				long tresDiasEnMilisegundos = 3 * 24 * 60 * 60 * 1000L;
				// Calcular el tiempo total en milisegundos para 1 día
				long unDiaEnMilisegundos = 24 * 60 * 60 * 1000L;
				long fechaLlegadaMenos1dia = timestampMilisegundos-unDiaEnMilisegundos;
				Alojamiento aloj = alohandes.darAlojamientoPorId(res.getAlojamientoReservado());

				if (dias_creacion<tresDiasEnMilisegundos)
					{
						resultado += ("\nHan pasado menos de 3 dias desde la creacion de la reserva. La multa es del 0% (migracion de alojamiento deshabilitado).\n Se procede a eliminar la reserva con id: "+id+"\n\n");
						alohandes.eliminarReservaPorId(id);
						
						
						//Liberamos un cupo en la capacidad del alojamiento (+1)
						long capacidad = aloj.getCapacidad() + 1;
						alohandes.actualizarCapacidadAlojamiento(aloj.getIdAlojamiento(), capacidad);
						panelDatos.actualizarInterfaz(resultado);
						return res;
					}
				else if (tresDiasEnMilisegundos<timestampMilisegundosActual && timestampMilisegundosActual<fechaLlegadaMenos1dia)
			
					{
						resultado += ("\nLa cancelacion esta entre la fecha limite y 1 dia antes de la fecha de llegada, la multa es del 0% (migracion de alojamiento deshabilitado).\n Se procede a eliminar la reserva con id: "+id+"\n\n");
						alohandes.eliminarReservaPorId(id);
						//Liberamos un cupo en la capacidad del alojamiento (+1)
						long capacidad = aloj.getCapacidad() + 1;
						alohandes.actualizarCapacidadAlojamiento(aloj.getIdAlojamiento(), capacidad);
						panelDatos.actualizarInterfaz(resultado);
						return res;
					}
				else
					{
						
						resultado += ("\nla multa es del 0% (migracion de alojamiento deshabilitado).\n Se procede a eliminar la reserva con id: "+id+"\n\n");
						alohandes.eliminarReservaPorId(id);
						//Liberamos un cupo en la capacidad del alojamiento (+1)
						long capacidad = aloj.getCapacidad() + 1;
						alohandes.actualizarCapacidadAlojamiento(aloj.getIdAlojamiento(), capacidad);
						panelDatos.actualizarInterfaz(resultado);
						return res;
					}
					
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultadoE = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultadoE);
		}
		return res;
    }

	/**
	 * @RF7
     * Adiciona una reserva colectiva con la información dada por el usuario
     * Se genera todas las reservas con la subtransaccion de insertar reservas, @RF4.
     */
	public void registrarReservaColectiva( )
    {
		long costoTotal = 0;
		int n = 0;
    	try 
    	{
			String resultado = "En Generar Reserva Colectiva\n\n";

			//OJO: FALTA VERIFICAR QUE LAS COSAS EXISTAN O NO SEAN NULAS. LANZAR MENSAJES SI NO HAY ALOJAMIENTOS O ASI.
			String cedulaString = JOptionPane.showInputDialog (this, "Indique su id de usuario (Esta operacion solo la pueden realizar usuarios del sistema): ", "Registrar Reserva Colectiva", JOptionPane.QUESTION_MESSAGE);
			int cedula = Integer.parseInt(cedulaString);
			String idMasivoString = JOptionPane.showInputDialog (this, "Asigne un id a la reserva masiva (esto servira como identificador para cuando se quiera eliminar la reserva colectiva): ", "Registrar Reserva Colectiva", JOptionPane.QUESTION_MESSAGE);
			String fecha_llegada = JOptionPane.showInputDialog (this, "fecha de llegada 'ej: 2023-05-01 12:30:45'?", "Adicionar Reserva", JOptionPane.QUESTION_MESSAGE);	
			String fecha_salida = JOptionPane.showInputDialog (this, "fecha de salida 'ej: 2023-05-01 12:30:45'?", "Adicionar Reserva", JOptionPane.QUESTION_MESSAGE);
			String tipo_alojamiento = JOptionPane.showInputDialog (this, "Indique el tipo de alojamiento deseado (Ej: habitacion sencilla, habitacion multiple, vivienda universitaria, hotel, etc...): ", "Registrar Reserva Colectiva", JOptionPane.QUESTION_MESSAGE);
    		String serviciosString = JOptionPane.showInputDialog (this, "Indique que servicios le gustaria tener, separados por coma (Ej: cocineta,TV,piscina, etc.)", "Registrar Reserva Colectiva", JOptionPane.QUESTION_MESSAGE);
			String cantidadString = JOptionPane.showInputDialog (this, "Indique la cantidad de reservas deseada:", "Registrar Reserva Colectiva", JOptionPane.QUESTION_MESSAGE);
    		int cantidad = Integer.parseInt(cantidadString);
			

			if (cantidadString != null)
    		{
				//Paso 1: Filtrar los alojamientos segun el tipo de alojamiento
				List<Alojamiento> allAlojamientos = alohandes.darAlojamientos();
				List<Alojamiento> alojamientosFiltro1 = new ArrayList<Alojamiento>();

				for (Alojamiento aloj: allAlojamientos)
				{
					if (aloj.getNombreAlojamiento().equals(tipo_alojamiento))
					{
						alojamientosFiltro1.add(aloj);

					}
					
				}
				

				//Paso 2: Filtrar los alojamientos encontrados segun los servicios requeridos
				List<String> serviciosLista = Arrays.asList(serviciosString.split(","));
				List<Alojamiento> alojamientosFiltro2 = new ArrayList<Alojamiento>();

				for (Alojamiento aloj:alojamientosFiltro1)
				{
					//Extraemos los servicios del alojamiento actual
					List<Servicio> servicios = alohandes.darServiciosDeUnAlojamiento(aloj.getIdAlojamiento());
					List<String> serviciosTemp = new ArrayList<String>();
					for (Servicio servicioTemp:servicios)
					{
						serviciosTemp.add(servicioTemp.getNombreServicio());	
					}

					//Si los servicios del alojamiento contiene todos los servicios requeridos, el alojamiento nos sirve
					if (serviciosTemp.containsAll(serviciosLista))
					{
						alojamientosFiltro2.add(aloj);
					}
				}
				

				//Paso3: repartir la cantidad de reservas entre los alojamientos que tengan la capacidad o parte de la capacidad necesaria
				long cantidadTemp = cantidad;
				boolean end = false;
				int a = 0;
				
				while (a<alojamientosFiltro2.size() && end==false && cantidadTemp != 0)
				{
					Alojamiento aloj = alojamientosFiltro2.get(a);

					//Si en el Alojamiento me caben todas las reservas, uso solamente este
					if (aloj.getCapacidad()>=cantidadTemp)
					{
						//use
						int c = 0;
						while (c<cantidadTemp)
						{
							long ultimoIdReserva = alohandes.darUltimoIdReservas();
							ultimoIdReserva+=1;
							long costoTemp = aloj.getPrecio() + aloj.getPrecioAdministracion() + aloj.getPrecioSeguro(); 
							costoTotal+=costoTemp;
							n++;

							//Creamos y adicionamos cada reserva como si fuera una reserva aislada de reserva colectiva
							adicionarReservaConParametros((int) ultimoIdReserva, "reserva_masiva_"+idMasivoString, fecha_llegada, fecha_salida, (int)costoTemp, cedula, aloj.getIdAlojamiento());
							c++;
						}
						end = true;
					}

					//De lo contrario, meto todas las reservas que me alcancen
					else
					{
						//creamos una cantidad de reservas igual a = 'cantidadTemp'.
						int o = 0;
						while (o<aloj.getCapacidad())
						{
							long ultimoIdReserva = alohandes.darUltimoIdReservas();
							ultimoIdReserva+=1;
							long costoTemp = aloj.getPrecio() + aloj.getPrecioAdministracion() + aloj.getPrecioSeguro(); 
							costoTotal+=costoTemp;
							n++;
							adicionarReservaConParametros((int) ultimoIdReserva, "reserva_masiva_"+idMasivoString, fecha_llegada, fecha_salida, (int)costoTemp, cedula, aloj.getIdAlojamiento());
							o++;
						}

						//Restamos las reservas que ya anotamos
						cantidadTemp -= aloj.getCapacidad();

					}
					a++;
				}

				resultado+="\n\nOperacion Realizada con exito!\n";
				resultado+="\nEl costo total de la reserva colectiva es: "+costoTotal+"000 COP\n";
				resultado+="\nEl numero total de reservas creadas fue: "+n+"\n";
				panelDatos.actualizarInterfaz(resultado);

    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
			
		}
    }

	/**
	 * @RF8
     * Borra de la base de datos todas las reservas que pertenecen
	 * a la reserva colectiva con el identificador dado por el usuario
     * la cancelacion tiene una multa segun el tiempo de cancelacion
	 * Usa: @RF5
     */
    public void eliminarReservaColectiva( )
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id de la reserva colectiva?", "Borrar Reserva por Id", JOptionPane.QUESTION_MESSAGE);

			if (idTipoStr != null)
    		{

				//Paso 1: Traemos todas las reservas de la reserva colectiva correspondiente
				List<Reserva> reservas = alohandes.darReservas();
				List<Reserva> reservasFiltro1 = new ArrayList<Reserva>();
				int n=0;
				int multaTotal=0;
				for (Reserva res:reservas)
				{
					if (res.getTipoContrato().equals("reserva_masiva_"+idTipoStr))
					{
						reservasFiltro1.add(res);
					}
				}

				//Paso 2: usamos @RF5 parametrizado para eliminar todas las reservas encontradas
				for (Reserva res:reservasFiltro1)
				{
					n++;
					int multa = eliminarReservaPorIdConParametros((int)res.getIdReserva());
					multaTotal+=multa;
				}
				
				String resultado = "";
				resultado+="En eliminar reserva colectivas: \n\n";
				resultado+="\n Total de reservas eliminadas: "+n;
				resultado+="\n Multa total: "+multaTotal+"000 COP";
				panelDatos.actualizarInterfaz(resultado);


    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	

	/* ****************************************************************
	 * 			CRUD de Alojamientos
	 *****************************************************************/
    /**
	 * 
     * Adiciona un Alojamiento con la información dada por el usuario
     * Se crea una nueva tupla tupla para el Alojamiento en la base de datos
     */
    public void adicionarAlojamiento( )
    {
    	try 
    	{
			
    		String idString = JOptionPane.showInputDialog (this, "id del alojamiento?", "Adicionar Reserva", JOptionPane.QUESTION_MESSAGE);
			int id = Integer.parseInt(idString);

			String opString = JOptionPane.showInputDialog (this, "id del operador?", "Adicionar Alojamiento", JOptionPane.QUESTION_MESSAGE);
			int operadorInt = Integer.parseInt(opString);

			String capacidad = JOptionPane.showInputDialog (this, "capacidad?", "Adicionar Alojamiento", JOptionPane.QUESTION_MESSAGE);
			int capacidadInt = Integer.parseInt(capacidad);

			String precio = JOptionPane.showInputDialog (this, "precio?", "Adicionar Alojamiento", JOptionPane.QUESTION_MESSAGE);
			int precioInt = Integer.parseInt(precio);

			String relacionU= JOptionPane.showInputDialog (this, "relacion con la universidad?", "Adicionar Alojamiento", JOptionPane.QUESTION_MESSAGE);

			String horariosRec = JOptionPane.showInputDialog (this, "horarios recepcion?", "Adicionar Alojamiento", JOptionPane.QUESTION_MESSAGE);
			
			String precioAdmin = JOptionPane.showInputDialog (this, "precio administracion?", "Adicionar Alojamiento", JOptionPane.QUESTION_MESSAGE);
			int precioAdminInt = Integer.parseInt(precioAdmin);

			String precioSeguro = JOptionPane.showInputDialog (this, "precio seguro?", "Adicionar Alojamiento", JOptionPane.QUESTION_MESSAGE);
			int precioSeguroInt = Integer.parseInt(precioSeguro);

			String nombreAlojamiento = JOptionPane.showInputDialog (this, "tipo de alojamiento?", "Adicionar Alojamiento", JOptionPane.QUESTION_MESSAGE);


			if (idString != null)
    		{
        		VOAlojamiento tb = alohandes.adicionarAlojamiento(id, operadorInt, capacidadInt, precioInt, relacionU, horariosRec, precioAdminInt, precioSeguroInt, nombreAlojamiento);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un alojamiento con id: " + idString);
        		}
        		String resultado = "En adicionarAlojamiento\n\n";
        		resultado += "Alojamiento adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	/**
	 * @RF6
     * Borra de la base de datos el Alojamiento con el identificador dado po el usuario
     * Se tiene que cumplir todas las condiciones de las reglas de negocio para ser eliminado
     */
    public void eliminarAlojamientoPorNombre( )
    {
    	try 
    	{
    		String nombre = JOptionPane.showInputDialog (this, "Nombre del alojamiento?", "Borrar Alojamiento por nombre", JOptionPane.QUESTION_MESSAGE);
			if (nombre != null)
    		{
				List<Alojamiento> aloj = alohandes.darAlojamientoPorNombre(nombre);

				//verificar que el id exista
				long id = aloj.get(0).getIdAlojamiento();

				List<Reserva> reservs = alohandes.darReservas();
				boolean cent = false;


				for (Reserva rev:reservs){
					if (rev.getAlojamientoReservado()==id){
						panelDatos.actualizarInterfaz("Error: Hay reservas activas para el alojamiento!");
						cent = true;
					}
				}

				if (cent == false){
					
					long tbEliminados = alohandes.eliminarAlojamiento(nombre);

					String resultado = "En eliminar Alojamiento\n\n";
					resultado += tbEliminados + " Alojamientos eliminados\n";
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);

				}
    		}

    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }


	//@RF9: Deshabilitar temporalmente una oferta de alojamiento
	// y reubicar a las reservas actuales, dandole prioridad a las vigentes
	public void deshabilitarOfertaAlojamiento()
	{
		String resultado = "En deshabilitar Alojamiento\n\n";
		try 
    	{
    		String idAlojString = JOptionPane.showInputDialog (this, "id del alojamiento a deshabilitar?", "Deshabilitar un alojamiento", JOptionPane.QUESTION_MESSAGE);
			int id_aloj = Integer.parseInt(idAlojString);

			//Assert: ver si el alojamiento ingresado ya esta deshabilitado
			if (!alohandes.checkearDispAlojamiento(id_aloj))
			{
				resultado += "Error de integridad: El alojamiento ingresado ya esta deshabilitado!";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				Date date = new Date();
				Timestamp fecha = new Timestamp(date.getTime()); 

				String evento = JOptionPane.showInputDialog (this, "Razon/evento externo por el cual se va a deshabilitar?", "Deshabilitar un alojamiento", JOptionPane.QUESTION_MESSAGE);

				if (idAlojString != null)
				{
					//Paso 1: Insertar la oferta de Alojamiento en la tabla de alojamientos deshabilitados
					alohandes.adicionarAlojamientoDeshabilitado(id_aloj, fecha, evento);

					//Paso 2: extraer las reservas del alojamiento
					List<Reserva> reservas = alohandes.darReservas();
					List<Reserva> reservasAloj = new ArrayList<Reserva>();
					for (Reserva res:reservas)
					{
						if (res.getAlojamientoReservado()==id_aloj)
						{
							reservasAloj.add(res);
						}
					}
					//Si no tiene reservas por reubicar, finalizamos
					if (reservasAloj.isEmpty())
					{
						resultado += "\nSe ha deshabilitado el alojamiento y no hace falta reubicar sus reservas porque no tiene ninguna.\n";
						
					}
					//De lo contrario, continuamos
					else
					{
						List<Reserva> reservasParaMigrarPrimero = new ArrayList<Reserva>();
						List<Reserva> reservasParaMigrarSegundo = new ArrayList<Reserva>();

						//Paso 3: eliminar las reservas del alojamiento deshabilitado para migrarlas usando @RF5
						for (Reserva res: reservasAloj)
						{

							//@RF5
							resultado += "\n\nEliminando la siguiente reserva del alojamiento para que sea migrada:\n";
							resultado += eliminarReservaPorIdConParametrosYTraza(resultado, (int)res.getIdReserva());

							Timestamp fechaLlegada = res.getFechaLlegada();
							Timestamp fechaSalida = res.getFechaSalida();
							Date dateActual = new Date();
							Timestamp fechaActual = new Timestamp(dateActual.getTime());

							/** 
							* Seleccionamos las reservas vigentes (Que estan en funcionamiento con la fecha actual)
							* Verificar si el timestamp intermedio está entre los otros dos
							*/
							if (fechaActual.compareTo(fechaLlegada) > 0 && fechaActual.compareTo(fechaSalida) < 0) 
							{
								reservasParaMigrarPrimero.add(res);
							} 
							else 
							{
								reservasParaMigrarSegundo.add(res);
							}
							
						}

						List<Alojamiento> allAlojamientos = alohandes.darAlojamientos();
						List<Alojamiento> alojamientosFiltro2 = new ArrayList<Alojamiento>();
						//Filtramos los alojamientos que no esten deshabilitados
						for (Alojamiento aloj: allAlojamientos)
						{
							if (alohandes.checkearDispAlojamiento(aloj.getIdAlojamiento()))
							{
								alojamientosFiltro2.add(aloj);
							}
						}

						//Paso 4: adicionar las reservas a migrar usando @RF4
						//Primero migramos las vigentes
						for (Reserva res: reservasParaMigrarPrimero)
						{
								//Paso 4.1: repartir la cantidad de reservas entre los alojamientos que tengan la capacidad o parte de la capacidad necesaria
								long cantidadTemp = reservasParaMigrarPrimero.size();
								boolean end = false;
								int a = 0;
								
								while (a<alojamientosFiltro2.size() && end==false && cantidadTemp != 0)
								{
									Alojamiento aloj = alojamientosFiltro2.get(a);

									//Si en el Alojamiento me caben todas las reservas, uso solamente este
									if (aloj.getCapacidad()>=cantidadTemp)
									{
										//use
										int c = 0;
										while (c<cantidadTemp)
										{
											long ultimoIdReserva = alohandes.darUltimoIdReservas();
											ultimoIdReserva+=1;
											//@RF4
											resultado += "\n\nMigrando las reservas (Vigentes: prioridad alta!) del alojamiento deshabilitado a nuevos alojamientos:\n\n";
											adicionarReservaConParametros((int)ultimoIdReserva, "migrado_por_alojamiento"+id_aloj, res.getFechaLlegada().toString(), res.getFechaSalida().toString(), (int)res.getCosto(),(int)res.getUsuario(), aloj.getIdAlojamiento());
											c++;
										}
										end = true;
									}

									//De lo contrario, meto todas las reservas que me alcancen
									else
									{
										//creamos una cantidad de reservas igual a = 'cantidadTemp'.
										int o = 0;
										while (o<aloj.getCapacidad())
										{
											long ultimoIdReserva = alohandes.darUltimoIdReservas();
											ultimoIdReserva+=1;
											resultado += "\n\nMigrando las reservas (Vigentes: prioridad alta!) del alojamiento deshabilitado a nuevos alojamientos:\n\n";
											adicionarReservaConParametros((int)ultimoIdReserva, "migrado", res.getFechaLlegada().toString(), res.getFechaSalida().toString(), (int)res.getCosto(),(int)res.getUsuario(), aloj.getIdAlojamiento());
											o++;
										}

										//Restamos las reservas que ya anotamos
										cantidadTemp -= aloj.getCapacidad();

									}
									a++;
									//Restamos las reservas que ya anotamos
									cantidadTemp -= aloj.getCapacidad();
								}
						}
						//Despues migramos las no vigentes
						for (Reserva res: reservasParaMigrarSegundo)
						{
							//Paso 4.2: repartir la cantidad de reservas entre los alojamientos que tengan la capacidad o parte de la capacidad necesaria
							long cantidadTemp = reservasParaMigrarSegundo.size();
							boolean end = false;
							int a = 0;
							
							while (a<alojamientosFiltro2.size() && end==false && cantidadTemp != 0)
							{
								Alojamiento aloj = alojamientosFiltro2.get(a);

								//Si en el Alojamiento me caben todas las reservas, uso solamente este
								if (aloj.getCapacidad()>=cantidadTemp)
								{
									//use
									int c = 0;
									while (c<cantidadTemp)
									{
										long ultimoIdReserva = alohandes.darUltimoIdReservas();
										ultimoIdReserva+=1;
										//@RF4
										resultado += "\n\nMigrando las reservas (No-vigentes: prioridad baja...) del alojamiento deshabilitado a nuevos alojamientos:\n\n";
										adicionarReservaConParametros((int)ultimoIdReserva, "migrado", res.getFechaLlegada().toString(), res.getFechaSalida().toString(), (int)res.getCosto(),(int)res.getUsuario(), aloj.getIdAlojamiento());
										c++;
									}
									end = true;
								}

								//De lo contrario, meto todas las reservas que me alcancen
								else
								{
									//creamos una cantidad de reservas igual a = 'cantidadTemp'.
									int o = 0;
									while (o<aloj.getCapacidad())
									{
										long ultimoIdReserva = alohandes.darUltimoIdReservas();
										ultimoIdReserva+=1;
										//@RF4
										resultado += "\n\nMigrando las reservas (No-vigentes: prioridad baja...) del alojamiento deshabilitado a nuevos alojamientos:\n\n";
										adicionarReservaConParametros((int)ultimoIdReserva, "migrado", res.getFechaLlegada().toString(), res.getFechaSalida().toString(), (int)res.getCosto(),(int)res.getUsuario(), aloj.getIdAlojamiento());
										o++;
									}

									//Restamos las reservas que ya anotamos
									cantidadTemp -= aloj.getCapacidad();

								}
								a++;
								//Restamos las reservas que ya anotamos
								cantidadTemp -= aloj.getCapacidad();
							}
						}

					}

					
					resultado +=  "\n\nAlojamiento deshabilitado: \n"+alohandes.darAlojamientoPorId(id_aloj)+"\n\n";
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);

					
					
				}

				else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}
		  }
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultadoE = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultadoE);
		}
	}

	/**
	 * @RF10
     * Rehabilita un alojamiento deshabilitado
     */
    public void rehabilitarAlojamiento( )
    {
		String resultado = "En Rehabilitar Alojamiento\n\n";
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id del alojamiento que sera habilitado?", "Rehabilitar alojamiento por Id", JOptionPane.QUESTION_MESSAGE);
			int id_aloj = Integer.parseInt(idTipoStr);


			if (idTipoStr!=null)
			{
				//Paso 1: verificar que el alojamiento este deshabilitado
				if (alohandes.checkearDispAlojamiento(id_aloj))
				{
					resultado += "\nError: El Alojamiento con id " + idTipoStr + " actualmente esta habilitado normalmente. No hace falta rehabilitarlo.\n";
					resultado += "\n\nOperacion Finalizada. No se rehabilito ningun alojamiento";
					panelDatos.actualizarInterfaz(resultado);
				}

				//Paso 2: eliminar la tupla de los alojamientos deshabilitados
				else
				{
					alohandes.rehabilitarAlojamiento(id_aloj);
					resultado += "\n\nOperacion Finalizada con exito! Se rehabilito el siguiente alojamiento:\n"+alohandes.darAlojamientoPorId(id_aloj);
					panelDatos.actualizarInterfaz(resultado);
				}
			}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultadoE = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultadoE);
		}
    }


	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Parranderos
	 */
	public void mostrarLogParranderos ()
	{
		mostrarArchivo ("parranderos.log");
	}
	
	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}
	
	/**
	 * Limpia el contenido del log de parranderos
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogParranderos ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("parranderos.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de parranderos ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia todas las tuplas de todas las tablas de la base de datos de alohandes
	 * Muestra en el panel de datos el número de tuplas eliminadas de cada tabla
	 */
	public void limpiarBD ()
	{
		try 
		{
    		// Ejecución de la demo y recolección de los resultados
			long eliminados [] = alohandes.limpiarAlohandes();
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " Gustan eliminados\n";
			resultado += eliminados [1] + " Sirven eliminados\n";
			resultado += eliminados [2] + " Visitan eliminados\n";
			resultado += eliminados [3] + " Bebidas eliminadas\n";
			resultado += eliminados [4] + " Tipos de bebida eliminados\n";
			resultado += eliminados [5] + " Bebedores eliminados\n";
			resultado += eliminados [6] + " Bares eliminados\n";
			resultado += eliminados [7] + " Bares eliminados\n";
			resultado += eliminados [8] + " Bares eliminados\n";
			resultado += eliminados [9] + " Bares eliminados\n";
			resultado += eliminados [10] + " Bares eliminados\n";
			resultado += eliminados [11] + " Bares eliminados\n";
			resultado += "\nLimpieza terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	/**
	 * Muestra la presentación general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}
	
	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}
	
	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}
	
	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}
	
	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	/**
	 * Muestra la documentación Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}
	
	/**
     * Muestra la información acerca del desarrollo de esta apicación
     */
    public void acercaDe ()
    {
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Parranderos Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Germán Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia Jiménez, Christian Ariza\n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
    }
    

	/* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una línea por cada Operador
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una líea para cada Operador recibido
     */
    private String listarTiposBebida(List<VOOperador> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOOperador tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
     * @param e - La excepción recibida
     * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
     */
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

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
    /**
     * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
     * Invoca al método correspondiente según el evento recibido
     * @param pEvento - El evento del usuario
     */
    @Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazAlohandesApp.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		} 
	}
    
	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
    /**
     * Este método ejecuta la aplicación, creando una nueva interfaz
     * @param args Arreglo de argumentos que se recibe por línea de comandos
     */
    public static void main( String[] args )
    {
        try
        {
        	
            // Unifica la interfaz para Mac y para Windows.
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazAlohandesApp interfaz = new InterfazAlohandesApp( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
