package logica;

import java.util.ArrayList;
import java.util.List;

public class Categoria {
	private int id;
    private String nombre;
    private String descripcion;
    private List<Item> items;

    public Categoria(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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
