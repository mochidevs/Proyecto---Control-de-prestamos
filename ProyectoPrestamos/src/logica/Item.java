package logica;

import java.util.ArrayList;
import java.util.List;

public class Item {
	private String nombre;
    private int codigo;
    private String descripcion;
    private boolean prestado;
    private TipoItem tipo;
    private List<Categoria> categorias;
    
    public Item(String nombre, int codigo, String descripcion, TipoItem tipo) {
    	this.nombre = nombre;
    	this.codigo = codigo;
    	this.descripcion = descripcion;
    	this.tipo = tipo;
    	this.prestado = false;
    	this.categorias = new ArrayList<>();
    }

	public String getNombre() {
		return nombre;
	}
	

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isPrestado() {
		return prestado;
	}

	public void setPrestado(boolean prestado) {
		this.prestado = prestado;
	}

	public TipoItem getTipo() {
		return tipo;
	}

	public void setTipo(TipoItem tipo) {
		this.tipo = tipo;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}
    
	public void agregarCategoria(Categoria categoria) {
        categorias.add(categoria);
    }

    public void eliminarCategoria(Categoria categoria) {
        categorias.remove(categoria);
    }

    public boolean perteneceACategoria(Categoria categoria) {
        return categorias.contains(categoria);
    }
    
    public String toString() {
    	return nombre;
    }
}
