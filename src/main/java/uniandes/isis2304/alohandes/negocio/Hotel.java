package uniandes.isis2304.alohandes.negocio;

/**
 * Clase para modelar el concepto HOTEL del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class Hotel implements VOHotel
{
    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private long id_hotel;
	private String nombre_hotel;
    private String registro_legal;

    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Hotel() 
    {
    	this.id_hotel = 0;
        this.nombre_hotel = "";
        this.registro_legal = "";
	}

	/**
	 * Constructor con valores
	 */
    public Hotel(long id_hotel, String nombre_hotel, String registro_legal)
    {
    	this.id_hotel = id_hotel;
        this.nombre_hotel = nombre_hotel;
        this.registro_legal = registro_legal;
	}

    public long getIdHotel() 
	{
		return id_hotel;
	}

	public void setIdHotel(long id_hotel) 
	{
		this.id_hotel = id_hotel;
    }

    public String getNombreHotel() 
	{
		return nombre_hotel;
	}

	public void setNombreHotel(String nombre_hotel)
	{
		this.nombre_hotel = nombre_hotel;
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
	 * @return Una cadena de caracteres con todos los atributos del hotel
	 */
	public String toString() 
	{
		return "Hotel [id_hotel=" + id_hotel +
        ", nombre_hotel=" + nombre_hotel +
        ", registro_legal=" + registro_legal + "]";
	}
    
}
