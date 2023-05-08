package uniandes.isis2304.alohandes.negocio;

import java.sql.Timestamp;

/**
 * Clase para modelar el concepto RESERVA del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class Reserva implements VOReserva
{
    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private long id_reserva;
	private String tipo_contrato;
    private Timestamp fecha_llegada;
    private Timestamp fecha_salida;
    private long costo;
    private long usuario;
    private long alojamiento_reservado;
	private Timestamp fecha_creacion_reserva;

    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Reserva() 
    {
    	this.id_reserva = 0;
        this.tipo_contrato = "";
        this.fecha_llegada = new Timestamp(0);
        this.fecha_salida = new Timestamp(0);
		this.costo = 0;
        this.usuario = 0;
        this.alojamiento_reservado = 0;
		this.fecha_creacion_reserva = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Constructor con valores
	 */
    public Reserva(long id_reserva, String tipo_contrato, Timestamp fecha_llegada, Timestamp fecha_salida, long costo, long usuario, long alojamiento_reservado)
    {
    	this.id_reserva = id_reserva;
        this.tipo_contrato = tipo_contrato;
        this.fecha_llegada = fecha_llegada;
        this.fecha_salida = fecha_salida;
		this.costo = costo;
        this.usuario = usuario;
        this.alojamiento_reservado = alojamiento_reservado;
		this.fecha_creacion_reserva = new Timestamp(System.currentTimeMillis());
	}

    public long getIdReserva() 
	{
		return id_reserva;
	}

	public void setId_reserva(long id_reserva) 
	{
		this.id_reserva = id_reserva;
    }
	public void setFecha_creacion_reserva(Timestamp fecha) 
	{
		this.fecha_creacion_reserva = fecha;
    }

    public String getTipoContrato() 
	{
		return tipo_contrato;
	}

	public void setTipo_contrato(String tipo_contrato) 
	{
		this.tipo_contrato = tipo_contrato;
	}

    public Timestamp getFechaLlegada()
	{
		return fecha_llegada;
	}

	public void setFecha_llegada(Timestamp fecha_llegada) 
	{
		this.fecha_llegada = fecha_llegada;
	}

    public Timestamp getFechaSalida()
	{
		return fecha_salida;
	}

	public void setFecha_salida(Timestamp fecha_salida) 
	{
		this.fecha_salida = fecha_salida;
	}

    public long getCosto() 
	{
		return costo;
	}

	public void setCosto(long costo) 
	{
		this.costo = costo;
	}

    public long getUsuario() 
	{
		return usuario;
	}

	public void setUsuario(long usuario)
	{
		this.usuario = usuario;
	}

    public long getAlojamientoReservado() 
	{
		return alojamiento_reservado;
	}

	public Timestamp getfechaCreacion() 
	{
		return this.fecha_creacion_reserva;
	}

	public void setAlojamiento_reservado(long alojamiento_reservado) 
	{
		this.alojamiento_reservado = alojamiento_reservado;
	}

    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la reserva
	 */
	public String toString() 
	{
		return "Reserva [id_reserva=" + id_reserva +
        ", tipo_contrato=" + tipo_contrato +
        ", fecha_llegada=" + fecha_llegada +
        ", fecha_salida=" + fecha_salida +
        ", costo=" + costo +
        ", usuario=" + usuario +
        ", alojamiento_reservado=" + alojamiento_reservado + "]";
	}
}
