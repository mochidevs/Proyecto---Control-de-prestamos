package logica;

public class TipoItem {
	private int id;
    private String nombre;
    private String descripcion;
    private boolean generico;
    
    public TipoItem(int id, String nombre, String descripcion, boolean generico) {
    	this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.generico = generico;
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getId() {
		return id;
	}

	public boolean isGenerico() {
		return generico;
	}
    
    
    
}
