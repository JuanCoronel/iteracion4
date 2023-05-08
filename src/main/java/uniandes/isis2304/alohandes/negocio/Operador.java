package uniandes.isis2304.alohandes.negocio;

/**
 * Clase para modelar el concepto OPERADOR del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class Operador implements VOOperador
{

    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private int id_operador;
	private String nombre;

    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Operador() 
    {
    	this.id_operador = 0;
		this.nombre = "";
	}

	/**
	 * Constructor con valores
	 * @param id_operador - El id del operador
	 * @param nombre - El nombre del operador
	 */
    public Operador(int id_operador, String nombre) 
    {
    	this.id_operador = id_operador;
		this.nombre = nombre;
	}


    /**
	 * @return El id del operador
	 */
	public long getIdOperador() 
	{
		return id_operador;
	}
	
	/**
	 * @param id_operador - El nuevo id del operador
	 */
	public void setId_operador(int id_operador) 
	{
		this.id_operador = id_operador;
	}
	
	/**
	 * @return El nombre del operador
	 */
	public String getNombre() 
	{
		return nombre;
	}
	
	/**
	 * @param nombre El nuevo nombre del operador
	 */
	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}

    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del operador
	 */
	public String toString() 
	{
		return "Operador [id_operador=" + id_operador + ", nombre=" + nombre + "]";
	}
}
