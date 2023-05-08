package uniandes.isis2304.alohandes.negocio;

/**
 * Clase para modelar el concepto HOSTAL del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class Hostal implements VOHostal
{
    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private long id_hostal;
	private String nombre_hostal;
    private String registro_legal;

    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Hostal() 
    {
    	this.id_hostal = 0;
        this.nombre_hostal = "";
        this.registro_legal = "";
	}

	/**
	 * Constructor con valores
	 */
    public Hostal(long id_hostal, String nombre_hostal, String registro_legal)
    {
    	this.id_hostal = id_hostal;
        this.nombre_hostal = nombre_hostal;
        this.registro_legal = registro_legal;
	}

    public long getIdHostal() 
	{
		return id_hostal;
	}

	public void setIdHostal(long id_hostal) 
	{
		this.id_hostal = id_hostal;
    }

    public String getNombreHostal() 
	{
		return nombre_hostal;
	}

	public void setNombreHostal(String nombre_hostal)
	{
		this.nombre_hostal = nombre_hostal;
	}

    public String getRegistroLegal() 
	{
		return registro_legal;
	}

	public void setRegistroLegal(String registro_legal)
	{
		this.registro_legal = registro_legal;
	}
    
    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del hostal
	 */
	public String toString() 
	{
		return "Hostal [id_hostal=" + id_hostal +
        ", nombre_hostal=" + nombre_hostal +
        ", registro_legal=" + registro_legal + "]";
	}
    
}
