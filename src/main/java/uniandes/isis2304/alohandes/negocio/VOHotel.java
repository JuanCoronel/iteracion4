package uniandes.isis2304.alohandes.negocio;

/**
 * Interfaz para los métodos get de HOTEL.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public interface VOHotel
{
    public long getIdHotel();

    public String getNombreHotel();

    public String getRegistroLegal();
    
    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del hotel
	 */
	public String toString();
    
}
