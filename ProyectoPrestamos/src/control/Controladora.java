package control;

import logica.*;
import java.util.ArrayList;
import java.util.Comparator;
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
    
    // Verificaciones ===============================================
    
    private void verificarUsuarioExistente(int id) throws Exception {
        if (!usuarios.containsKey(id))
            throw new Exception("Usuario no encontrado.");
    }
    
    private void verificarUsuarioSinPrestamos(int id) throws Exception {
        if (usuarios.get(id).tienePrestamos())
            throw new Exception("No se puede eliminar porque el usuario tiene préstamos activos.");
    }
    
    private void verificarItemExistente(int codigo) throws Exception {
        if (!items.containsKey(codigo))
            throw new Exception("Ítem no encontrado.");
    }
    
    private void verificarItemDisponible(int codigo) throws Exception {
        if (items.get(codigo).isPrestado())
            throw new Exception("No se puede eliminar porque el ítem está en un préstamo activo.");
    }

    private void verificarPrestamoExistente(int id) throws Exception {
        if (!prestamos.containsKey(id))
            throw new Exception("Préstamo no encontrado.");
    }
    
    private void verificarTipoExistente(int id) throws Exception {
        if (!tipos.containsKey(id))
            throw new Exception("Tipo no encontrado.");
    }

    private void verificarTipoNoBorrable(int id) throws Exception {
        if (tipos.get(id).isGenerico())
            throw new Exception("No se puede eliminar el tipo genérico.");
    }
    
    private void verificarCategoriaExistente(int id) throws Exception {
        if (!categorias.containsKey(id))
            throw new Exception("Categoría no encontrada.");
    }

    private void verificarExisteUnTipo() throws Exception {
        if (tipos.size() <= 1)
            throw new Exception("Debe existir al menos un tipo antes de crear un ítem.");
    }
    
    private void verificarExisteUsuarioItemDisponible() throws Exception {
        if (usuarios.isEmpty())
            throw new Exception("Debe existir al menos un usuario para hacer un préstamo.");
        if (obtenerListadoItemsDisponibles().isEmpty())
            throw new Exception("Debe existir al menos un ítem disponible para hacer un préstamo.");
    }
    
    // Usuarios =============================================================
    
    public void crearUsuario(String nombre, String telefono, String email) {
    	Usuario u = new Usuario(consecutivoUsuario, nombre, telefono, email);
    	usuarios.put(consecutivoUsuario++, u);
    }
    
    public void actualizarUsuario(int id, String nombre, String telefono, String email) throws Exception {
    	verificarUsuarioExistente(id);
    	
    	Usuario u = usuarios.get(id);
    	if (u != null) {
    		u.setNombre(nombre);
    		u.setTelefono(telefono);
    		u.setEmail(email);
    	}
    }
    
    public void borrarUsuario(int id) throws Exception {
    	verificarUsuarioExistente(id);
        verificarUsuarioSinPrestamos(id);
        
    	Usuario u = usuarios.get(id);
    	if (u != null && !u.tienePrestamos())
    		usuarios.remove(id);
    }
    
    public Usuario obtenerUsuario(int id) throws Exception {
    	verificarUsuarioExistente(id);
    	
    	return usuarios.get(id);
    }
    
    public List<Usuario> obtenerListadoUsuarios() {
        ArrayList<Usuario> users = new ArrayList<>(usuarios.values());
    	return users;
    }
    
    public boolean usuarioTienePrestamos(int id) throws Exception {
    	verificarUsuarioExistente(id);
    	
    	Usuario u = usuarios.get(id);
    	return u.tienePrestamos();
    }
    
    public List<Usuario> obtenerUsuariosConPrestamos() {
    	List<Usuario> resultado = new ArrayList<>();
    	for (Usuario u : usuarios.values()) {
            if (u.tienePrestamos()) 
            	resultado.add(u);
        }
    	resultado.sort(Comparator.comparing(Usuario::getNombre));
        return resultado;
    }
    
    // Items ===============================================================
    
    public void crearItem(String nombre, String descripcion, int idTipo) throws Exception {
    	verificarExisteUnTipo();
        verificarTipoExistente(idTipo);
        
    	TipoItem tipo = tipos.get(idTipo);
		Item item = new Item( nombre, consecutivoItem++, descripcion, tipo);
		tipo.agregarItem(item);
		items.put(item.getCodigo(), item);
    }
    
    public void actualizarItem(int codigo, String nombre, String descripcion, int idTipo) throws Exception {
    	verificarItemExistente(codigo);
        verificarTipoExistente(idTipo);
    	
    	Item item = items.get(codigo);
    	TipoItem tipoViejo = item.getTipo();
        TipoItem tipoNuevo = tipos.get(idTipo);
        tipoViejo.eliminarItem(item);
        tipoNuevo.agregarItem(item);
        
        item.setNombre(nombre);
        item.setDescripcion(descripcion);
        item.setTipo(tipoNuevo);
    }
    
    public void borrarItem(int codigo) throws Exception {
    	verificarItemExistente(codigo);
        verificarItemDisponible(codigo);
    	
    	Item item = items.get(codigo);
    	TipoItem tipo = item.getTipo();
    	tipo.eliminarItem(item);
    	
    	for (Categoria c : item.getCategorias()) {
    		c.eliminarItem(item);
    	}
        items.remove(codigo);
                
    }
    
    public Item obtenerItem(int codigo) throws Exception {
    	verificarItemExistente(codigo);
    	
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
    
    public boolean itemEstaPrestado(int codigo) throws Exception {
    	verificarItemExistente(codigo);
    	
    	Item item = items.get(codigo);
        return item.isPrestado();
    }
    
    public void agregarCategoriaAItem(int codigoItem, int idCategoria) throws Exception {
    	verificarItemExistente(codigoItem);
        verificarCategoriaExistente(idCategoria);
    	
    	Item item = items.get(codigoItem);
        Categoria categoria = categorias.get(idCategoria);
        item.agregarCategoria(categoria);		//agrega categoria al item
        categoria.agregarItem(item);           //agrega el item a esa categoria(lista)
    }
    
    public void eliminarCategoriaDeItem(int codigoItem, int idCategoria) throws Exception {
    	verificarItemExistente(codigoItem);
        verificarCategoriaExistente(idCategoria);
    	
    	Item item = items.get(codigoItem);
        Categoria categoria = categorias.get(idCategoria);
        item.eliminarCategoria(categoria);
        categoria.eliminarItem(item);           
    }
    
    public List<Item> obtenerItemsPorCategoria(int idCategoria) throws Exception {
    	verificarCategoriaExistente(idCategoria);
    	
        Categoria categoria = categorias.get(idCategoria);
        List<Item> resultado = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.perteneceACategoria(categoria)) 
            	resultado.add(item);
        }
        
        return resultado;
    }

    public List<Item> obtenerItemsPorTipo(int idTipo) throws Exception {
    	verificarTipoExistente(idTipo);
    	
        List<Item> resultado = new ArrayList<>();
        for (Item item : items.values()) {
        	TipoItem tipo = item.getTipo();
            if (tipo.getId() == idTipo) 
            	resultado.add(item);
        }
        return resultado;
    }
    
    // Tipos =========================================================================
    
    public void crearTipo(String nombre, String descripcion) {
        TipoItem tipo = new TipoItem(consecutivoTipo++, nombre, descripcion, false);
        tipos.put(tipo.getId(), tipo);
    }
    
    public void actualizarTipo(int id, String nombre, String descripcion) throws Exception {
    	verificarTipoExistente(id);
    	
    	TipoItem tipo = tipos.get(id);
        tipo.setNombre(nombre);
        tipo.setDescripcion(descripcion);
        
    }
    
    public void borrarTipo(int id) throws Exception {
    	verificarTipoExistente(id);
        verificarTipoNoBorrable(id);
    	
        TipoItem generico = obtenerTipoGenerico();
        TipoItem tipo = tipos.get(id);
        
        for (Item item : tipo.getItems()) {
            item.setTipo(generico);
            generico.agregarItem(item);
        }
        tipos.remove(id);
    }
    
    public TipoItem obtenerTipo(int id) throws Exception {
    	verificarTipoExistente(id);
    	
        return tipos.get(id);
    }
    
    public List<TipoItem> obtenerListadoTipos() {
        return new ArrayList<>(tipos.values());
    }
    
    public TipoItem obtenerTipoGenerico() {
        for (TipoItem tipo : tipos.values()) {
            if (tipo.isGenerico()) 
            	return tipo;
        }
        return null;
    }
    
    // Categorias =========================================================================
    
    public void crearCategoria(String nombre, String descripcion) {
        Categoria categoria = new Categoria(consecutivoCategoria++, nombre, descripcion);
        categorias.put(categoria.getId(), categoria);
    }

    public void actualizarCategoria(int id, String nombre, String descripcion) throws Exception {
    	verificarCategoriaExistente(id);
    	
    	Categoria categoria = categorias.get(id);
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        
    }

    public void borrarCategoria(int id) throws Exception {
    	verificarCategoriaExistente(id);
    	
        Categoria categoria = categorias.get(id);
        for (Item item : categoria.getItems()) {
            item.eliminarCategoria(categoria);
        }
        categorias.remove(id);
        
    }

    public Categoria obtenerCategoria(int id) throws Exception {
    	verificarCategoriaExistente(id);
    	
        return categorias.get(id);
    }

    public List<Categoria> obtenerListadoCategorias() {
        return new ArrayList<>(categorias.values());
    }
    
    // Prestamos ======================================================================0
    
    public Prestamo crearPrestamo(int idUsuario) throws Exception {
    	verificarUsuarioExistente(idUsuario);
    	verificarExisteUsuarioItemDisponible();
    	
        Usuario usuario = usuarios.get(idUsuario);
        Prestamo prestamo = new Prestamo(consecutivoPrestamo++, usuario);
        prestamos.put(prestamo.getId(), prestamo);
        usuario.agregarPrestamo(prestamo);
        return prestamo;
    }

    public void agregarItemAPrestamo(int idPrestamo, int codigoItem) throws Exception {
    	verificarPrestamoExistente(idPrestamo);
        verificarItemExistente(codigoItem);
        verificarItemDisponible(codigoItem);
    	
        Prestamo prestamo = prestamos.get(idPrestamo);
        Item item = items.get(codigoItem);
        prestamo.agregarItem(item);
        item.setPrestado(true);
        
    }

    public void eliminarItemDePrestamo(int idPrestamo, int codigoItem) throws Exception {
    	verificarPrestamoExistente(idPrestamo);
        verificarItemExistente(codigoItem);
    	
        Prestamo prestamo = prestamos.get(idPrestamo);
        Item item = items.get(codigoItem);
        prestamo.eliminarItem(item);
        item.setPrestado(false);
        
    }
    
    public void retornarItemDePrestamo(int idPrestamo, int codigoItem) throws Exception {
    	verificarPrestamoExistente(idPrestamo);
        verificarItemExistente(codigoItem);
        eliminarItemDePrestamo(idPrestamo, codigoItem);
    }

    public void agregarAlertaAPrestamo(int idPrestamo, String mensaje,
                                        boolean esRecurrente, int intervalo) throws Exception {
    	verificarPrestamoExistente(idPrestamo);
    	
        Prestamo prestamo = prestamos.get(idPrestamo);
        Alerta alerta = new Alerta(mensaje, esRecurrente, intervalo);
        prestamo.setAlerta(alerta);
        
    }

    public void devolverItemDePrestamo(int idPrestamo, int codigoItem) throws Exception {
    	verificarPrestamoExistente(idPrestamo);
        verificarItemExistente(codigoItem);
    	eliminarItemDePrestamo(idPrestamo, codigoItem);
    }

    public void finalizarPrestamo(int idPrestamo) throws Exception {
    	verificarPrestamoExistente(idPrestamo);
    	
        Prestamo prestamo = prestamos.get(idPrestamo);
        
        for (Item item : prestamo.getItems()) {
            item.setPrestado(false);
        }
        if (prestamo.tieneAlerta()) {
            prestamo.getAlerta().setActiva(false);
        }
        prestamo.setTerminado(true);
        prestamo.getUsuario().eliminarPrestamo(prestamo);
        prestamos.remove(idPrestamo);
        
    }
    
    public void cancelarPrestamoEnCreacion(int idPrestamo) throws Exception {
        verificarPrestamoExistente(idPrestamo);
        Prestamo prestamo = prestamos.get(idPrestamo);
        for (Item item : prestamo.getItems()) {
            item.setPrestado(false);
        }
        if (prestamo.tieneAlerta()) {
            prestamo.getAlerta().setActiva(false);
        }
        prestamo.getUsuario().eliminarPrestamo(prestamo);
        prestamos.remove(idPrestamo);
    }

    public Prestamo obtenerPrestamo(int idPrestamo) throws Exception {
    	verificarPrestamoExistente(idPrestamo);
    	
        return prestamos.get(idPrestamo);
    }

    public List<Prestamo> obtenerPrestamosPorUsuario(int idUsuario) throws Exception {
    	verificarUsuarioExistente(idUsuario);
    	
        Usuario u = usuarios.get(idUsuario);
        return u.getPrestamos();
    }

    public Prestamo obtenerPrestamoDeItem(int codigoItem) throws Exception {
    	verificarItemExistente(codigoItem);
    	
        for (Prestamo prestamo : prestamos.values()) {
            for (Item item : prestamo.getItems()) {
                if (item.getCodigo() == codigoItem) return prestamo;
            }
        }
        return null;
    }

    public List<Alerta> verificarAlertasPendientes() {
        List<Alerta> pendientes = new ArrayList<>();
        for (Prestamo prestamo : prestamos.values()) {
            if (prestamo.tieneAlerta()) {
                Alerta alerta = prestamo.getAlerta();
                if (alerta.debeActivarse()) {
                    pendientes.add(alerta);
                    alerta.registrarMostrada();
                }
            }
        }
        return pendientes;
    }
    
    public List<Prestamo> obtenerListadoPrestamos() {
        return new ArrayList<>(prestamos.values());
    }

    // Reportes ============================================================
    
    public String reportePorUsuario() {
    	StringBuilder sb = new StringBuilder();
        sb.append("REPORTE POR USUARIO\n");
        sb.append("___________________\n\n");
    	List<Usuario> lista = new ArrayList<>(usuarios.values());
    	lista.sort(Comparator.comparing(Usuario::getNombre));
    	
    	for (Usuario u : lista) {
    		sb.append("Nombre: ").append(u.getNombre()).append("\n");
            sb.append("Teléfono: ").append(u.getTelefono()).append("\n");
            sb.append("Email: ").append(u.getEmail()).append("\n");
            
            if (u.tienePrestamos()) {
            	sb.append("Préstamos activos:\n");
                for (Prestamo p : u.getPrestamos()) {
                    sb.append("  - Préstamo #").append(p.getId()).append(" (").append(p.getFechaInicio()).append(")\n");
                    for (Item item : p.getItems()) {
                        sb.append("      * ").append(item.getNombre()).append("\n");
                    }
                }
            } else {
            	sb.append("Sin préstamos activos.\n");
            }
            sb.append("-------------------\n");
    	}
    	
        return sb.toString();
    }
    
    public String reportePorItem() throws Exception {
    	StringBuilder sb = new StringBuilder();
        sb.append("REPORTE POR ÍTEM\n");
        sb.append("___________________\n\n");
    	List<Item> lista = new ArrayList<>(items.values());
        lista.sort(Comparator.comparing(Item::getNombre));
        
        for (Item item : lista) {
            sb.append("Nombre: ").append(item.getNombre()).append("\n");
            sb.append("Código: ").append(item.getCodigo()).append("\n");
            sb.append("Descripción: ").append(item.getDescripcion()).append("\n");
            sb.append("Tipo: ").append(item.getTipo().getNombre()).append("\n");
            if (item.isPrestado()) {
                Prestamo p = obtenerPrestamoDeItem(item.getCodigo());
                if (p != null) {
                    sb.append("Estado: Prestado a ").append(p.getUsuario().getNombre()).append("\n");
                }
            } else {
                sb.append("Estado: Disponible\n");
            }
            sb.append("-------------------\n");
        }
        
        
        return sb.toString();
    }

    public String reportePorTipo() {
    	StringBuilder sb = new StringBuilder();
        sb.append("REPORTE POR TIPO\n");
        sb.append("___________________\n\n");
    	List<TipoItem> lista = new ArrayList<>(tipos.values());
        lista.sort(Comparator.comparing(TipoItem::getNombre));
        
        for (TipoItem tipo : lista) {
            sb.append("Tipo: ").append(tipo.getNombre()).append("\n");
            sb.append("Descripción: ").append(tipo.getDescripcion()).append("\n");
            List<Item> itemsTipo = new ArrayList<>(tipo.getItems());
            itemsTipo.sort(Comparator.comparing(Item::getNombre));
            
            if (itemsTipo.isEmpty()) {
                sb.append("Sin ítems.\n");
            } else {
                for (Item item : itemsTipo) {
                    sb.append("  - ").append(item.getNombre())
                      .append(item.isPrestado() ? " (prestado)" : " (disponible)")
                      .append("\n");
                }
            }
            sb.append("-------------------\n");
        }
        
        return sb.toString();
    }
    
    
}
