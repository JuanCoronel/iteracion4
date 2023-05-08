package uniandes.isis2304.alohandes.negocio;

/**
 * Clase para modelar el concepto RESIDENCIAU del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class ResidenciaU implements VOResidenciaU
{
    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private long id_residencia;
	private String nombre_residencia;
    private String registro_legal;

    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public ResidenciaU() 
    {
    	this.id_residencia = 0;
        this.nombre_residencia = "";
        this.registro_legal = "";
	}

	/**
	 * Constructor con valores
	 */
    public ResidenciaU(long id_residencia, String nombre_residencia, String registro_legal)
    {
    	this.id_residencia = id_residencia;
        this.nombre_residencia = nombre_residencia;
        this.registro_legal = registro_legal;
	}

    public long getIdResidencia() 
	{
		return id_residencia;
	}

	public void setIdResidencia(long id_residencia) 
	{
		this.id_residencia = id_residencia;
    }

    public String getNombreResidencia() 
	{
		return nombre_residencia;
	}

	public void setNombreResidencia(String nombre_residencia) 
	{
		this.nombre_residencia = nombre_residencia;
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
	 * @return Una cadena de caracteres con todos los atributos de la residencia
	 */
	public String toString() 
	{
		return "ResidenciaU [id_residencia=" + id_residencia +
        ", nombre_residencia=" + nombre_residencia +
        ", registro_legal=" + registro_legal + "]";
	}
}