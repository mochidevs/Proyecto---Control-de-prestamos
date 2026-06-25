package logica;

import java.util.ArrayList;
import java.util.List;

public class TipoItem {
	private int id;
    private String nombre;
    private String descripcion;
    private boolean generico;
    private List<Item> items;
    
    public TipoItem(int id, String nombre, String descripcion, boolean generico) {
    	this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.generico = generico;
        this.items = new ArrayList<>();
    }

    public int getId() {
    	return id;
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

	public boolean isGenerico() {
		return generico;
	}

	public List<Item> getItems() {
		return items;
	}
    
	public void agregarItem(Item item) {
    	items.add(item);
    }
    
    public void eliminarItem(Item item) {
    	items.remove(item);
    }
    
    public String toString() {
    	return nombre;
    }
}
