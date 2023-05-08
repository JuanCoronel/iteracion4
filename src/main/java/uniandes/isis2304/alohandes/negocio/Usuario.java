package uniandes.isis2304.alohandes.negocio;

/**
 * Clase para modelar el concepto USUARIO del negocio de Alohandes
 * 
 * @author Juan Esteban Coronel
 * @author Juan Pablo Martinez
 */

public class Usuario implements VOUsuario
{
    /* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private String nombre;
	private long cedula;
    private String relacion_universidad;

    /* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Usuario() 
    {
    	this.nombre = "";
        this.cedula = 0;;
		this.relacion_universidad = "";
	}

	/**
	 * Constructor con valores
	 */
    public Usuario(String nombre, long cedula, String relacion_universidad)
    {
    	this.nombre = nombre;
        this.cedula = cedula;
		this.relacion_universidad = relacion_universidad;
	}


	public String getNombre() 
	{
		return nombre;
	}

	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}

    public long getCedula() 
	{
		return cedula;
	}

	public void setCedula(long cedula) 
	{
		this.cedula = cedula;
	}

    public String getRelacionUniversidad()
	{
		return relacion_universidad;
	}

	public void setRelacion_universidad(String relacion_universidad) 
	{
		this.relacion_universidad = relacion_universidad;
	}

    @Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del usuario
	 */
	public String toString() 
	{
		return "Usuario [nombre=" + nombre +
        ", cedula=" + cedula +
        ", relacion_universidad=" + relacion_universidad + "]";
	}
}
