package logica;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
	private String nombre;
	private String telefono;
	private String email;
	private List<Prestamo> prestamos;
	
	public Usuario(String nombre, String telefono, String email) {
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.prestamos = new ArrayList<>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<Prestamo> getPrestamos() {
		return prestamos;
	}

	public void agregarPrestamo(Prestamo prestamo) {
		prestamos.add(prestamo);
	}
	
	public void eliminarPrestamo(Prestamo prestamo) {
		prestamos.remove(prestamo);
	}
	
	public boolean tienePrestamos() {
		return !prestamos.isEmpty();
	}
}
