package uniandes.isis2304.alohandes.negocio;

/**
 * Interfaz para los métodos get de OPERADOR.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public interface VOOperador
{

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
     /**
	 * @return El id del operador
	 */
	public long getIdOperador();
	
	/**
	 * @return el nombre del operador
	 */
	public String getNombre();

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del operador
	 */
	public String toString();

}
