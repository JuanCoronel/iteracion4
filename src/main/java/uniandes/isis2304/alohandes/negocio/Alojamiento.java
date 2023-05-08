package uniandes.isis2304.alohandes.negocio;

/**
 * Clase para modelar el concepto ALOJAMIENTO del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class Alojamiento implements VOAlojamiento
{
    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private long id_alojamiento;
	private long operador;
    private long capacidad;
    private long precio;
    private String relacion_universidad;
    private String horarios_recepcion;
    private long precio_administracion;
    private long precio_seguro;
    private String nombre_alojamiento;

    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Alojamiento() 
    {
    	this.id_alojamiento = 0;
        this.operador = 0;
        this.capacidad = 0;
        this.precio = 0;
		this.relacion_universidad = "";
        this.horarios_recepcion = "";
        this.precio_administracion = 0;
        this.precio_seguro = 0;
		this.nombre_alojamiento = "";
	}

	/**
	 * Constructor con valores
	 */
    public Alojamiento(long id_alojamiento, long operador, long capacidad, long precio, String relacion_universidad, String horarios_recepcion, long precio_administracion, long precio_seguro, String nombre_alojamiento)
    {
    	this.id_alojamiento = id_alojamiento;
        this.operador = operador;
        this.capacidad = capacidad;
        this.precio = precio;
		this.relacion_universidad = relacion_universidad;
        this.horarios_recepcion = horarios_recepcion;
        this.precio_administracion = precio_administracion;
        this.precio_seguro = precio_seguro;
		this.nombre_alojamiento = nombre_alojamiento;
	}

	public Alojamiento(long id_alojamiento, long operador, int capacidad, int precio, String relacion_universidad, String horarios_recepcion, int precio_administracion, int precio_seguro, String nombre_alojamiento)
    {
    	this.id_alojamiento = id_alojamiento;
        this.operador = operador;
        this.capacidad = capacidad;
        this.precio = precio;
		this.relacion_universidad = relacion_universidad;
        this.horarios_recepcion = horarios_recepcion;
        this.precio_administracion = precio_administracion;
        this.precio_seguro = precio_seguro;
		this.nombre_alojamiento = nombre_alojamiento;
	}

	public Alojamiento(long id_alojamiento)
	{
		this.id_alojamiento = id_alojamiento;
	}


	public long getIdAlojamiento() 
	{
		return this.id_alojamiento;
	}

	public void setId_alojamiento(long id_alojamiento) 
	{
		this.id_alojamiento = id_alojamiento;
	}

    public long getOperador() 
	{
		return operador;
	}

	public void setOperador(long operador) 
	{
		this.operador = operador;
	}

    public long getCapacidad()
	{
		return capacidad;
	}

	public void setCapacidad(long capacidad) 
	{
		this.capacidad = capacidad;
	}

    public long getPrecio() 
	{
		return precio;
	}

	public void setPrecio(long precio) 
	{
		this.precio = precio;
	}

    public String getRelacionUniversidad() 
	{
		return relacion_universidad;
	}

	public void setRelacion_universidad(String relacion_universidad) 
	{
		this.relacion_universidad = relacion_universidad;
	}

    public String getHorariosRecepcion() 
	{
		return horarios_recepcion;
	}

	public void setHorarios_recepcion(String horarios_recepcion) 
	{
		this.horarios_recepcion = horarios_recepcion;
	}

    public long getPrecioAdministracion() 
	{
		return precio_administracion;
	}

	public void setPrecio_administracion(long precio_administracion) 
	{
		this.precio_administracion = precio_administracion;
	}

    public long getPrecioSeguro() 
	{
		return precio_seguro;
	}

	public void setPrecio_seguro(long precio_seguro) 
	{
		this.precio_seguro = precio_seguro;
	}

    public String getNombreAlojamiento() 
	{
		return nombre_alojamiento;
	}

	public void setNombre_alojamiento(String nombre_alojamiento) 
	{
		this.nombre_alojamiento = nombre_alojamiento;
	}

    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del alojamiento
	 */
	public String toString() 
	{
		return "Alojamiento [id_alojamiento=" + id_alojamiento +
        ", operador=" + operador +
        ", operador=" + capacidad +
        ", operador=" + precio +
        ", relacion_universidad=" + relacion_universidad +
        ", horarios_recepcion=" + horarios_recepcion +
        ", precio_administracion=" + precio_administracion +
        ", precio_seguro=" + precio_seguro +
        ", nombre_alojamiento=" + nombre_alojamiento + "]";
	}

}
