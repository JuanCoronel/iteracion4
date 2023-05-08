package uniandes.isis2304.alohandes.negocio;

/**
 * Clase para modelar el concepto CONTRATO del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class Contrato implements VOContrato
{
    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private long id_contrato;
	private String contratista;
    private long alojamiento;
    private String registro_legal;

    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Contrato() 
    {
    	this.id_contrato = 0;
        this.contratista = "";
        this.alojamiento = 0;
        this.registro_legal = "";
	}

	/**
	 * Constructor con valores
	 */
    public Contrato(long id_contrato, String contratista, long alojamiento, String registro_legal)
    {
    	this.id_contrato = id_contrato;
        this.contratista = contratista;
        this.alojamiento = alojamiento;
        this.registro_legal = registro_legal;
	}

    public long getIdContrato() 
	{
		return id_contrato;
	}

	public void setIdContrato(long id_contrato) 
	{
		this.id_contrato = id_contrato;
    }

    public String getContratista() 
	{
		return contratista;
	}

	public void setContratista(String contratista) 
	{
		this.contratista = contratista;
	}

    public long getAlojamiento() 
	{
		return alojamiento;
	}

	public void setAlojamiento(long alojamiento) 
	{
		this.alojamiento = alojamiento;
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
	 * @return Una cadena de caracteres con todos los atributos del contrato
	 */
	public String toString() 
	{
		return "Contrato [id_contrato=" + id_contrato +
        ", contratista=" + contratista +
        ", alojamiento=" + alojamiento + 
        ", registro_legal=" + registro_legal + "]";
	}
    
}
