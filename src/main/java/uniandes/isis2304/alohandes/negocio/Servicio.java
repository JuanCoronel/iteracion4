package uniandes.isis2304.alohandes.negocio;

/**
 * Clase para modelar el concepto SERVICIO del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class Servicio implements VOServicio
{
    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private long id_servicio;
	private String nombre_servicio;
    private long alojamiento;

    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Servicio() 
    {
    	this.id_servicio = 0;
        this.nombre_servicio = "";
        this.alojamiento = 0;
	}

	/**
	 * Constructor con valores
	 */
    public Servicio(long id_servicio, String nombre_servicio, long alojamiento)
    {
    	this.id_servicio = id_servicio;
        this.nombre_servicio = nombre_servicio;
        this.alojamiento = alojamiento;
	}

    public long getIdServicio() 
	{
		return id_servicio;
	}

	public void setId_servicio(long id_servicio) 
	{
		this.id_servicio = id_servicio;
    }

    public String getNombreServicio() 
	{
		return nombre_servicio;
	}

	public void setNombre_servicio(String nombre_servicio) 
	{
		this.nombre_servicio = nombre_servicio;
	}

    public long getAlojamiento() 
	{
		return alojamiento;
	}

	public void setAlojamiento(long alojamiento) 
	{
		this.alojamiento = alojamiento;
	}
    
    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del servicio
	 */
	public String toString() 
	{
		return "Servicio [id_servicio=" + id_servicio +
        ", nombre_servicio=" + nombre_servicio +
        ", alojamiento=" + alojamiento + "]";
	}
    
}
