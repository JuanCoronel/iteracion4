package uniandes.isis2304.alohandes.negocio;

import java.sql.Timestamp;

/**
 * Clase para modelar el concepto ALOJAMIENTO DESHABILITADO del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class Alojdeshabilitados implements VOAlojdeshabilitados
{
    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private long id_alojamiento;
	private Timestamp fecha;
    private String evento;


    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Alojdeshabilitados() 
    {
    	this.id_alojamiento = 0;
        this.fecha = new Timestamp(0);
        this.evento = "";

	}

	/**
	 * Constructor con valores
	 */
    public Alojdeshabilitados(long id_alojamiento, Timestamp fecha, String evento)
    {
    	this.id_alojamiento = id_alojamiento;
        this.fecha = fecha;
        this.evento = evento;
	}

    public long getId_alojamiento() 
	{
		return this.id_alojamiento;
	}

	public void setId_alojamiento(long id_alojamiento) 
	{
		this.id_alojamiento = id_alojamiento;
    }

    public Timestamp getFecha() 
	{
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) 
	{
		this.fecha = fecha;
	}

    public String getEvento() 
	{
		return this.evento;
	}

	public void setEvento(String evento) 
	{
		this.evento = evento;
	}

    
    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del contrato
	 */
	public String toString() 
	{
		return "Contrato [id_alojamiento=" + id_alojamiento +
        ", fecha=" + fecha +
        ", evento=" + evento + "]";
	}
    
}
