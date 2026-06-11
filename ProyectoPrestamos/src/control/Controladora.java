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
    
    public void crearItem(String nombre, String descripcion, int idTipo) {
    	TipoItem tipo = tipos.get(idTipo);
    	if (tipo != null) {
    		Item item = new Item( nombre, consecutivoItem++, descripcion, tipo);
    		items.put(item.getCodigo(), item);
    	}
    }
    
    public void actualizarItem(int codigo, String nombre, String descripcion, int idTipo) {
        Item item = items.get(codigo);
        TipoItem tipo = tipos.get(idTipo);
        if (item != null && tipo != null) {
            item.setNombre(nombre);
            item.setDescripcion(descripcion);
            item.setTipo(tipo);
        }
    }
    
    public void borrarItem(int codigo) {
        Item item = items.get(codigo);
        if (item != null && !item.isPrestado()) {
            items.remove(codigo);
        }
    }
    
    public Item obtenerItem(int codigo) {
        return items.get(codigo);
    }
    
    public List<Item> obtenerListadoItems() {
        return new ArrayList<>(items.values());
    }

    public List<Item> obtenerListadoItemsDisponibles() {
        List<Item> disponibles = new ArrayList<>();
        for (Item item : items.values()) {
            if (!item.isPrestado()) 
            	disponibles.add(item);
        }
        return disponibles;
    }
    
    public boolean itemEstaPrestado(int codigo) {
        Item item = items.get(codigo);
        return item != null && item.isPrestado();
    }
    
    public void agregarCategoriaAItem(int codigoItem, int idCategoria) {
        Item item = items.get(codigoItem);
        Categoria categoria = categorias.get(idCategoria);
        if (item != null && categoria != null) {
            item.agregarCategoria(categoria);
        }
    }
    
    public void eliminarCategoriaDeItem(int codigoItem, int idCategoria) {
        Item item = items.get(codigoItem);
        Categoria categoria = categorias.get(idCategoria);
        if (item != null && categoria != null) {
            item.eliminarCategoria(categoria);
        }
    }
    
    public List<Item> obtenerItemsPorCategoria(int idCategoria) {
        Categoria categoria = categorias.get(idCategoria);
        List<Item> resultado = new ArrayList<>();
        if (categoria != null) {
            for (Item item : items.values()) {
                if (item.perteneceACategoria(categoria)) 
                	resultado.add(item);
            }
        }
        return resultado;
    }

    public List<Item> obtenerItemsPorTipo(int idTipo) {
        List<Item> resultado = new ArrayList<>();
        for (Item item : items.values()) {
        	TipoItem tipo = item.getTipo();
            if (tipo.getId() == idTipo) 
            	resultado.add(item);
        }
        return resultado;
    }
    
}
