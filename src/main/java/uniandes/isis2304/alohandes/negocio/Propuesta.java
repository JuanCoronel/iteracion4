package uniandes.isis2304.alohandes.negocio;

/**
 * Clase para modelar el concepto PROPUESTA del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class Propuesta implements VOPropuesta
{
    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private long id_propuesta;
	private String nombre_alojamiento;
    private String info_alojamiento;

    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Propuesta() 
    {
    	this.id_propuesta = 0;
        this.nombre_alojamiento = "";
        this.info_alojamiento ="";
	}

	/**
	 * Constructor con valores
	 */
    public Propuesta(long id_propuesta, String nombre_alojamiento, String info_alojamiento)
    {
    	this.id_propuesta = id_propuesta;
        this.nombre_alojamiento = nombre_alojamiento;
        this.info_alojamiento = info_alojamiento;
	}

    public long getIdPropuesta() 
	{
		return id_propuesta;
	}

	public void setIdPropuesta(long id_propuesta) 
	{
		this.id_propuesta = id_propuesta;
    }

    public String getNombreAlojamiento() 
	{
		return nombre_alojamiento;
	}

	public void setNombreAlojamiento(String nombre_alojamiento) 
	{
		this.nombre_alojamiento = nombre_alojamiento;
	}

    public String getInfoAlojamiento() 
	{
		return info_alojamiento;
	}

	public void setInfoAlojamiento(String info_alojamiento) 
	{
		this.info_alojamiento = info_alojamiento;
	}
    
    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la propuesta
	 */
	public String toString() 
	{
		return "Propuesta [id_propuesta=" + id_propuesta +
        ", nombre_alojamiento=" + nombre_alojamiento +
        ", info_alojamiento=" + info_alojamiento + "]";
	}
}
