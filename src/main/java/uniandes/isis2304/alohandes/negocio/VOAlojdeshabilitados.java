package uniandes.isis2304.alohandes.negocio;

import java.sql.Timestamp;
/**
 * Interfaz para los métodos get de AlojamientosDeshabilitados.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public interface VOAlojdeshabilitados
{
    public long getId_alojamiento();

    public Timestamp getFecha(); 

    public  String getEvento();

    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la propuesta
	 */
	public String toString();
    
}
