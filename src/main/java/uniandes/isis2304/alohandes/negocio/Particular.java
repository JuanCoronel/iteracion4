package uniandes.isis2304.alohandes.negocio;

/**
 * Clase para modelar el concepto PARTICULAR del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class Particular implements VOParticular
{
    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private long id_particular;
	private String nombre_particular;

    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Particular() 
    {
    	this.id_particular = 0;
        this.nombre_particular = "";
	}

	/**
	 * Constructor con valores
	 */
    public Particular(long id_particular, String nombre_particular)
    {
    	this.id_particular = id_particular;
        this.nombre_particular = nombre_particular;
	}

    public long getIdParticular() 
	{
		return id_particular;
	}

	public void setIdParticular(long id_particular) 
	{
		this.id_particular = id_particular;
    }

    public String getNombreParticular() 
	{
		return nombre_particular;
	}

	public void setNombreParticular(String nombre_particular) 
	{
		this.nombre_particular = nombre_particular;
	}
    
    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la propuesta
	 */
	public String toString() 
	{
		return "Particular [id_particular=" + id_particular +
        ", nombre_particular=" + nombre_particular + "]";
	}
    
}
