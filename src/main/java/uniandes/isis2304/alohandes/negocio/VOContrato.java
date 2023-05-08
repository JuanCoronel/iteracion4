package uniandes.isis2304.alohandes.negocio;

/**
 * Interfaz para los métodos get de CONTRATO.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public interface VOContrato
{
    public long getIdContrato();

    public String getContratista();

    public long getAlojamiento();

    public String getRegistroLegal();
    
    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la propuesta
	 */
	public String toString();
    
}
