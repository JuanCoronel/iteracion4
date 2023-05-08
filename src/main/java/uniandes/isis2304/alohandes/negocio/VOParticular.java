package uniandes.isis2304.alohandes.negocio;

/**
 * Interfaz para los métodos get de PARTICULAR.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public interface VOParticular
{
    public long getIdParticular();

    public String getNombreParticular();
    
    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la propuesta
	 */
	public String toString();
    
}
