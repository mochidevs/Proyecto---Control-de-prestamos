package control;

import logica.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Controladora {
	private static Controladora instancia;

    private int consecutivoUsuario;
    private int consecutivoPrestamo;
    private int consecutivoItem;
    private int consecutivoTipo;
    private int consecutivoCategoria;
    
    private Map<Integer, Usuario> usuarios;
    private Map<Integer, Item> items;
    private Map<Integer, Prestamo> prestamos;
    private Map<Integer, TipoItem> tipos;
    private Map<Integer, Categoria> categorias;
    
    private Controladora() {
    	consecutivoUsuario = 1;
        consecutivoPrestamo = 1;
        consecutivoItem = 1;
        consecutivoTipo = 1;
        consecutivoCategoria = 1;
        
        usuarios = new TreeMap<>();
        items = new TreeMap<>();
        prestamos = new TreeMap<>();
        tipos = new TreeMap<>();
        categorias = new TreeMap<>();
        
        TipoItem generico = new TipoItem(consecutivoTipo++, "Genérico", "Tipo default", true);
        tipos.put(generico.getId(), generico);
    }
    
    public static Controladora getInstance() {
        if (instancia == null) 
        	instancia = new Controladora();
        return instancia;
    }
    
    // Usuarios
    
    public void crearUsuario(String nombre, String telefono, String email) {
    	Usuario u = new Usuario(consecutivoUsuario, nombre, telefono, email);
    	usuarios.put(consecutivoUsuario++, u);
    }
    
    public void actualizarUsuario(int id, String nombre, String telefono, String email) {
    	Usuario u = usuarios.get(id);
    	if (u != null) {
    		u.setNombre(nombre);
    		u.setTelefono(telefono);
    		u.setEmail(email);
    	}
    }
    
    public void borrarUsuario(int id) {
    	Usuario u = usuarios.get(id);
    	if (u != null && !u.tienePrestamos())
    		usuarios.remove(id);
    }
    
    public Usuario obtenerrUsuario(int id) {
    	return usuarios.get(id);
    }
    
    public List<Usuario> obtenerListadoUsuarios() {
        ArrayList<Usuario> users = new ArrayList<>(usuarios.values());
    	return users;
    }
    
    public boolean usuarioTienePrestamos(int id) {
    	Usuario u = usuarios.get(id);
    	return u != null && u.tienePrestamos();
    }
    
    public List<Usuario> obtenerUsuariosConPrestamos() {
    	List<Usuario> resultado = new ArrayList<>();
    	for (Usuario u : usuarios.values()) {
            if (u.tienePrestamos()) 
            	resultado.add(u);
        }
        return resultado;
    }
    
    // Items
    
    
    
}
