package uniandes.isis2304.alohandes.negocio;

/**
 * Interfaz para los métodos get de RESERVA.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

import java.sql.Timestamp;

public interface VOReserva
{
    public long getIdReserva();

    public String getTipoContrato();

    public Timestamp getFechaLlegada();

    public Timestamp getFechaSalida();

    public long getCosto();

    public long getUsuario();

    public long getAlojamientoReservado();

    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la reserva
	 */
	public String toString();
}
