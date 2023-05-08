package uniandes.isis2304.alohandes.negocio;

/**
 * Interfaz para los métodos get de RESIDENCIAU.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public interface VOResidenciaU
{
    public long getIdResidencia();

    public String getNombreResidencia();

    public String getRegistroLegal();
    
    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la residencia
	 */
	public String toString();
    
}