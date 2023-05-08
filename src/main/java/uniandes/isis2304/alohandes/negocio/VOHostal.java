package uniandes.isis2304.alohandes.negocio;

/**
 * Interfaz para los métodos get de HOSTAL.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public interface VOHostal
{
    public long getIdHostal();

    public String getNombreHostal();

    public String getRegistroLegal();
    
    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del hostal
	 */
	public String toString();
    
}
