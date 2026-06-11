package logica;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Prestamo {
	private int id;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private boolean terminado;
	private List<Item> items;
	private Usuario usuario;
	private Alerta alerta;
	
	public Prestamo(int id, Usuario usuario) {
		this.id = id;
		this.fechaInicio = LocalDateTime.now();
		this.terminado = false;
		this.items = new ArrayList<>();
		this.usuario = usuario;
		this.alerta = null;
	}

	public int getId() {
		return id;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}
	
	public LocalDateTime getFechaFin() {
		return fechaFin;
	}
	
	public boolean isTerminado() {
		return terminado;
	}

	public void setTerminado(boolean terminado) {
		this.terminado = terminado;
		if (terminado)
			fechaFin = LocalDateTime.now();
	}

	public Alerta getAlerta() {
		return alerta;
	}

	public void setAlerta(Alerta alerta) {
		this.alerta = alerta;
	}

	public Usuario getUsuario() {
		return usuario;
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

    public boolean tieneAlerta() {
        return alerta != null;
    }

    public int cantidadItems() {
        return items.size();
    }
	
	
}
