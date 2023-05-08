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

package uniandes.isis2304.alohandes.interfazDemo;

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
import uniandes.isis2304.alohandes.interfazApp.PanelDatos;
import uniandes.isis2304.alohandes.negocio.Alohandes;
import uniandes.isis2304.alohandes.negocio.Alojamiento;
import uniandes.isis2304.alohandes.negocio.Reserva;
import uniandes.isis2304.alohandes.negocio.Servicio;
import uniandes.isis2304.alohandes.negocio.VOReserva;


/**
 * Clase principal de la interfaz
 * 
 * @author Germán Bravo
 */
@SuppressWarnings("serial")

public class InterfazAlohandesDemo extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazAlohandesDemo.class.getName());
	
	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigDemo.json"; 
	
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
    public InterfazAlohandesDemo( )
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
	 * 			Métodos para la configuración de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
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

	/* ****************************************************************
	 * 			Métodos Exitosos!
	 *****************************************************************/
	/**

	/* ****************************************************************
	 * 			Demos del RF7
	 *****************************************************************/
    /**
     * Demostración del requisito funcional 7
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos tiene los valores minimos
     * Post: La base de datos tiene los valores creados
     */

	 public void demoExitoRF7()
	 {
		//=======================Estado inicial de la base de datos =======================
		List<Reserva> reservas = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientos = alohandes.devolverTablaAlojamiento();
		String resultado = "\n\n******** ESTADO INICIAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Iniciales ==========\n\n";
		for (Reserva res: reservas)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Iniciales ==========\n\n";
		for (Alojamiento aloj: alojamientos)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		//=======================Ejecucion exitosa del requisito funcional 7=======================
		long costoTotal = 0;
		int n = 0;
    	try 
    	{
			
			String cedulaString = "123";
			int cedula = Integer.parseInt(cedulaString);
			String idMasivoString = "1";
			String fecha_llegada = "2023-05-01 12:30:45";
			String fecha_salida = "2023-05-01 12:30:46";
			String tipo_alojamiento = "hotel";
    		String serviciosString = "cocineta";
			String cantidadString = "50";
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


    		}
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultadoE = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultadoE);
			
		}


		//======================= Estado final de la base de datos =======================
		List<Reserva> reservasF = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientosF = alohandes.devolverTablaAlojamiento();
		resultado += "\n\n******** ESTADO FINAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Finales ==========\n\n";
		for (Reserva res: reservasF)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Finales ==========\n\n";
		for (Alojamiento aloj: alojamientosF)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		panelDatos.actualizarInterfaz(resultado);
	 }

	 /* ****************************************************************
	 * 			Demos del RF8
	 *****************************************************************/
    /**
     * Demostración del requisito funcional 8
     * Muestra la traza de la ejecución en el panelDatos
	 * 
	 * Pre: La base de datos tiene los valores minimos
     * Post: La base de datos tiene los valores creados
     * 
     */

	 public void demoExitoRF8()
	 {
		//=======================Estado inicial de la base de datos =======================
		List<Reserva> reservasI = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientos = alohandes.devolverTablaAlojamiento();
		String resultado = "\n\n******** ESTADO INICIAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Iniciales ==========\n\n";
		for (Reserva res: reservasI)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Iniciales ==========\n\n";
		for (Alojamiento aloj: alojamientos)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		//=======================Ejecucion exitosa del requisito funcional 8=======================
		try 
    	{
    		String idTipoStr = "1";

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
				

				resultado+="En eliminar reserva colectivas: \n\n";
				resultado+="\n Total de reservas eliminadas: "+n;
				resultado+="\n Multa total: "+multaTotal+"000 COP";


    		}

		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultadoe = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultadoe);
		}
		


		//======================= Estado final de la base de datos =======================
		List<Reserva> reservasF = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientosF = alohandes.devolverTablaAlojamiento();
		resultado += "\n\n******** ESTADO FINAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Finales ==========\n\n";
		for (Reserva res: reservasF)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Finales ==========\n\n";
		for (Alojamiento aloj: alojamientosF)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		panelDatos.actualizarInterfaz(resultado);
	 }

	  /* ****************************************************************
	 * 			Demos del RF9
	 *****************************************************************/
    /**
     * Demostración del requisito funcional 9
     * Muestra la traza de la ejecución en el panelDatos
	 * 
	 * Pre: La base de datos tiene los valores minimos
     * Post: La base de datos tiene los valores creados
     * 
     */

	 public void demoExitoRF9()
	 {
		//=======================Estado inicial de la base de datos =======================
		List<Reserva> reservasI = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientosI = alohandes.devolverTablaAlojamiento();
		String resultado = "\n\n******** ESTADO INICIAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Iniciales ==========\n\n";
		for (Reserva res: reservasI)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Iniciales ==========\n\n";
		for (Alojamiento aloj: alojamientosI)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		//=======================Ejecucion exitosa del requisito funcional 9=======================
		try 
    	{
    		String idAlojString = "1";
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

				String evento = "EVENTO DEMO";

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

					
					
				}

		  }
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultadoE = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultadoE);
		}

		//======================= Estado final de la base de datos =======================
		List<Reserva> reservasF = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientosF = alohandes.devolverTablaAlojamiento();
		resultado += "\n\n******** ESTADO FINAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Finales ==========\n\n";
		for (Reserva res: reservasF)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Finales ==========\n\n";
		for (Alojamiento aloj: alojamientosF)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		panelDatos.actualizarInterfaz(resultado);
	 }

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
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
		return 0;
    }

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

	/* ****************************************************************
	 * 			Demos del RF10
	 *****************************************************************/
    /**
     * Demostración del requisito funcional 10
     * Muestra la traza de la ejecución en el panelDatos
	 * 
	 * Pre: La base de datos tiene los valores minimos
     * Post: La base de datos tiene los valores creados
     * 
     */

	 public void demoExitoRF10()
	 {
		//=======================Estado inicial de la base de datos =======================
		List<Reserva> reservasI = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientos = alohandes.devolverTablaAlojamiento();
		String resultado = "\n\n******** ESTADO INICIAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Iniciales ==========\n\n";
		for (Reserva res: reservasI)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Iniciales ==========\n\n";
		for (Alojamiento aloj: alojamientos)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		//=======================Ejecucion exitosa del requisito funcional 10=======================

    	try 
    	{
    		String idTipoStr = "1";
			int id_aloj = Integer.parseInt(idTipoStr);


			if (idTipoStr!=null)
			{
				//Paso 1: verificar que el alojamiento este deshabilitado
				if (alohandes.checkearDispAlojamiento(id_aloj))
				{
					resultado += "\nError: El Alojamiento con id " + idTipoStr + " actualmente esta habilitado normalmente. No hace falta rehabilitarlo.\n";
					resultado += "\n\nOperacion Finalizada. No se rehabilito ningun alojamiento";
				}

				//Paso 2: eliminar la tupla de los alojamientos deshabilitados
				else
				{
					alohandes.rehabilitarAlojamiento(id_aloj);
					resultado += "\n\nOperacion Finalizada con exito! Se rehabilito el siguiente alojamiento:\n"+alohandes.darAlojamientoPorId(id_aloj);
				}
			}

		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultadoE = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultadoE);
		}



		//======================= Estado final de la base de datos =======================
		List<Reserva> reservasF = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientosF = alohandes.devolverTablaAlojamiento();
		resultado += "\n\n******** ESTADO FINAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Finales ==========\n\n";
		for (Reserva res: reservasF)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Finales ==========\n\n";
		for (Alojamiento aloj: alojamientosF)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		panelDatos.actualizarInterfaz(resultado);
	 }

	 /* ****************************************************************
	 * 			Métodos no exitosos
	 *****************************************************************/
	/**
	 * 
	 /* ****************************************************************
	 * 			Demos fracaso del RF7
	 *****************************************************************/
    /**
     * Demostración del fracaso requisito funcional 7
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos tiene los valores minimos
     * Post: La base de datos tiene los valores creados
     */

	 public void demoFracasoRF7()
	 {
		//=======================Estado inicial de la base de datos =======================
		List<Reserva> reservas = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientos = alohandes.devolverTablaAlojamiento();
		String resultado = "\n\n******** ESTADO INICIAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Iniciales ==========\n\n";
		for (Reserva res: reservas)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Iniciales ==========\n\n";
		for (Alojamiento aloj: alojamientos)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		//=======================Ejecucion exitosa del requisito funcional 7=======================
		long costoTotal = 0;
		int n = 0;
    	try 
    	{
			
			String cedulaString = "000000";
			int cedula = Integer.parseInt(cedulaString);
			String idMasivoString = "1";
			String fecha_llegada = "2023-05-01 12:30:45";
			String fecha_salida = "2023-05-01 12:30:46";
			String tipo_alojamiento = "hotel";
    		String serviciosString = "cocineta";
			String cantidadString = "50";
    		int cantidad = Integer.parseInt(cantidadString);
			

			if (cantidadString != null && cedulaString == "123")
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


    		}
			else
			{
				resultado+="\n\nError: el usuario con la id '"+cedula+"' no existe en la base de datos de AlohAndes!\n";
				panelDatos.actualizarInterfaz(resultado);
			}
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultadoE = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultadoE);
			
		}
		panelDatos.actualizarInterfaz(resultado);
	 }

	  /* ****************************************************************
	 * 			Demos del fracaso RF8
	 *****************************************************************/
    /**
     * Demostración del fracaso requisito funcional 8
     * Muestra la traza de la ejecución en el panelDatos
	 * 
	 * Pre: La base de datos tiene los valores minimos
     * Post: La base de datos tiene los valores creados
     * 
     */

	 public void demoFracasoRF8()
	 {
		//=======================Estado inicial de la base de datos =======================
		List<Reserva> reservasI = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientos = alohandes.devolverTablaAlojamiento();
		String resultado = "\n\n******** ESTADO INICIAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Iniciales ==========\n\n";
		for (Reserva res: reservasI)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Iniciales ==========\n\n";
		for (Alojamiento aloj: alojamientos)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		//=======================Ejecucion exitosa del requisito funcional 8=======================
		try 
    	{
    		String idTipoStr = "0000";

			if (idTipoStr != null && idTipoStr=="1")
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
				

				resultado+="En eliminar reserva colectivas: \n\n";
				resultado+="\n Total de reservas eliminadas: "+n;
				resultado+="\n Multa total: "+multaTotal+"000 COP";


    		}
			else
			{
				resultado+="\n\n Error: la reserva colectiva con el id: '"+idTipoStr+"' no existe!\n\n";
			}

		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultadoe = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultadoe);
		}
		


		//======================= Estado final de la base de datos =======================
		List<Reserva> reservasF = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientosF = alohandes.devolverTablaAlojamiento();
		resultado += "\n\n******** ESTADO FINAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Finales ==========\n\n";
		for (Reserva res: reservasF)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Finales ==========\n\n";
		for (Alojamiento aloj: alojamientosF)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		panelDatos.actualizarInterfaz(resultado);
	 }

	  /* ****************************************************************
	 * 			Demos del fracaso RF9
	 *****************************************************************/
    /**
     * Demostración del fracaso requisito funcional 9
     * Muestra la traza de la ejecución en el panelDatos
	 * 
	 * Pre: La base de datos tiene los valores minimos
     * Post: La base de datos tiene los valores creados
     * 
     */

	 public void demoFracasoRF9()
	 {
		//=======================Estado inicial de la base de datos =======================
		List<Reserva> reservasI = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientosI = alohandes.devolverTablaAlojamiento();
		String resultado = "\n\n******** ESTADO INICIAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Iniciales ==========\n\n";
		for (Reserva res: reservasI)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Iniciales ==========\n\n";
		for (Alojamiento aloj: alojamientosI)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		//=======================Ejecucion exitosa del requisito funcional 9=======================
		try 
    	{
    		String idAlojString = "1";
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

				String evento = "EVENTO DEMO";

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

					
					
				}

		  }
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultadoE = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultadoE);
		}

		//======================= Estado final de la base de datos =======================
		List<Reserva> reservasF = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientosF = alohandes.devolverTablaAlojamiento();
		resultado += "\n\n******** ESTADO FINAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Finales ==========\n\n";
		for (Reserva res: reservasF)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Finales ==========\n\n";
		for (Alojamiento aloj: alojamientosF)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		panelDatos.actualizarInterfaz(resultado);
	 }

	 /* ****************************************************************
	 * 			Demos del fracaso RF10
	 *****************************************************************/
    /**
     * Demostración del fracaso requisito funcional 10
     * Muestra la traza de la ejecución en el panelDatos
	 * 
	 * Pre: La base de datos tiene los valores minimos
     * Post: La base de datos tiene los valores creados
     * 
     */

	 public void demoFracasoRF10()
	 {
		//=======================Estado inicial de la base de datos =======================
		List<Reserva> reservasI = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientos = alohandes.devolverTablaAlojamiento();
		String resultado = "\n\n******** ESTADO INICIAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Iniciales ==========\n\n";
		for (Reserva res: reservasI)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Iniciales ==========\n\n";
		for (Alojamiento aloj: alojamientos)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		//=======================Ejecucion exitosa del requisito funcional 10=======================

    	try 
    	{
    		String idTipoStr = "2";
			int id_aloj = Integer.parseInt(idTipoStr);


			if (idTipoStr!=null)
			{
				//Paso 1: verificar que el alojamiento este deshabilitado
				if (alohandes.checkearDispAlojamiento(id_aloj))
				{
					resultado += "\nError: El Alojamiento con id " + idTipoStr + " actualmente esta habilitado normalmente. No hace falta rehabilitarlo.\n";
					resultado += "\n\nOperacion Finalizada. No se rehabilito ningun alojamiento";
				}

				//Paso 2: eliminar la tupla de los alojamientos deshabilitados
				else
				{
					alohandes.rehabilitarAlojamiento(id_aloj);
					resultado += "\n\nOperacion Finalizada con exito! Se rehabilito el siguiente alojamiento:\n"+alohandes.darAlojamientoPorId(id_aloj);
				}
			}

		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultadoE = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultadoE);
		}



		//======================= Estado final de la base de datos =======================
		List<Reserva> reservasF = alohandes.devolverTablaReserva(); 
		List<Alojamiento> alojamientosF = alohandes.devolverTablaAlojamiento();
		resultado += "\n\n******** ESTADO FINAL DE LA BASE DE DATOS ********\n\n";
		resultado += "\n\n========== Reservas Finales ==========\n\n";
		for (Reserva res: reservasF)
		{
			resultado+="\n ---> Reserva con id: "+res.getIdReserva();
		}
		resultado += "\n\n========== Alojamientos Finales ==========\n\n";
		for (Alojamiento aloj: alojamientosF)
		{
			resultado+="\n ---> Alojamiento con nombre: "+aloj.getNombreAlojamiento();
		}

		panelDatos.actualizarInterfaz(resultado);
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
	 * Limpia todas las tuplas de todas las tablas de la base de datos de Alohandes
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
			Method req = InterfazAlohandesDemo.class.getMethod ( evento );			
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
            InterfazAlohandesDemo interfaz = new InterfazAlohandesDemo( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
