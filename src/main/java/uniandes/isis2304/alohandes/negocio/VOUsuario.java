package uniandes.isis2304.alohandes.negocio;

/**
 * Interfaz para los métodos get de USUARIO.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public interface VOUsuario
{
    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    public String getNombre();

    public long getCedula();

    public String getRelacionUniversidad();

    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del usuario
	 */
	public String toString();
    
}
