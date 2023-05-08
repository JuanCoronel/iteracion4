package uniandes.isis2304.alohandes.negocio;

/**
 * Interfaz para los métodos get de ALOJAMIENTO.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public interface VOAlojamiento
{
    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    public long getIdAlojamiento();

    public long getOperador();

    public long getCapacidad();

    public long getPrecio();

    public String getRelacionUniversidad();

    public String getHorariosRecepcion();

    public long getPrecioAdministracion();

    public long getPrecioSeguro();

    public String getNombreAlojamiento();

    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del alojamiento
	 */
	public String toString();
    
}
