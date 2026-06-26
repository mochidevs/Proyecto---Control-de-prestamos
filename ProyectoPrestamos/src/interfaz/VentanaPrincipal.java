package interfaz;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.Rectangle;

import control.Controladora;
import logica.Prestamo;
import logica.TipoItem;
import logica.Usuario;
import logica.Categoria;
import logica.Item;


public class VentanaPrincipal {

	private JFrame frame;
	private JTable tablePersonas;
	private JTable tableItems;
	private JTable tableTipos;
	private JTable tablePrestamos;
	private Controladora control = Controladora.getInstance();
	private JTable tableCategs;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal window = new VentanaPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VentanaPrincipal() {
		initialize();
		cargarPrestamos();
		cargarPersonas();
		cargarItems();
		cargarTipos();
	}


	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Sistema de Préstamos");
		frame.setBounds(100, 100, 551, 429);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane sistema = new JTabbedPane(JTabbedPane.TOP);
		sistema.setBounds(0, 0, 535, 390);
		frame.getContentPane().add(sistema);
		
		// admin con pestañas
		
		JPanel panelAdmin = new JPanel();
		sistema.addTab("Administración", null, panelAdmin, null);
		panelAdmin.setLayout(null);
		
		JTabbedPane tabsAdmin = new JTabbedPane(JTabbedPane.TOP);
		tabsAdmin.setBounds(0, 0, 530, 362);
		panelAdmin.add(tabsAdmin);
		
		// pestaña personas
		
		JPanel panelPersonas = new JPanel();
		tabsAdmin.addTab("Personas", null, panelPersonas, null);
		panelPersonas.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 56, 459, 247);
		panelPersonas.add(scrollPane);
		
		tablePersonas = new JTable();
		scrollPane.setViewportView(tablePersonas);
		tablePersonas.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"#", "Nombre", "Tel\u00E9fono", "Email", "Pr\u00E9stamos"
			}
		) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		tablePersonas.getColumnModel().getColumn(0).setResizable(false);
		tablePersonas.getColumnModel().getColumn(2).setResizable(false);
		tablePersonas.getColumnModel().getColumn(3).setResizable(false);
		tablePersonas.getColumnModel().getColumn(4).setResizable(false);
		
		JButton btnNuevaP = new JButton("Nueva persona");
		btnNuevaP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistroPersona dialog = new RegistroPersona();
				dialog.setVisible(true);
				cargarPersonas();
			}
		});
		btnNuevaP.setBounds(33, 11, 105, 23);
		panelPersonas.add(btnNuevaP);
		
		JButton btnCambiar = new JButton("Cambiar");
		btnCambiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tablePersonas.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(frame, "Seleccione una persona primero.");
					return;
				}
				int numero = (int) tablePersonas.getValueAt(fila, 0);
				try {
					Usuario u = control.obtenerUsuario(numero);
					RegistroPersona dialog = new RegistroPersona();
					dialog.cargarDatos(u);
					dialog.setVisible(true);
					cargarPersonas();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnCambiar.setBounds(148, 11, 89, 23);
		panelPersonas.add(btnCambiar);
		
		JButton btnBorrarPersona = new JButton("Borrar");
		btnBorrarPersona.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tablePersonas.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(frame, "Seleccione una persona primero.");
					return;
				}
				int numero = (int) tablePersonas.getValueAt(fila, 0);
				int confirmar = JOptionPane.showConfirmDialog(frame, "¿Está seguro de borrar esta persona?", "Confirmar", JOptionPane.YES_NO_OPTION);
				if (confirmar == JOptionPane.YES_OPTION) {
					try {
						control.borrarUsuario(numero);
						cargarPersonas();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnBorrarPersona.setBounds(247, 11, 89, 23);
		panelPersonas.add(btnBorrarPersona);
		
		// pestaña tipos
		
		JPanel panelTipos = new JPanel();
		tabsAdmin.addTab("Tipos ", null, panelTipos, null);
		panelTipos.setLayout(null);
		
		JButton btnNuevoTipo = new JButton("Nuevo tipo");
		btnNuevoTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistroTipo dialog = new RegistroTipo();
				dialog.setVisible(true);
				cargarTipos();
			}
		});
		btnNuevoTipo.setBounds(37, 11, 105, 23);
		panelTipos.add(btnNuevoTipo);
		
		JButton btnCambiarTipo = new JButton("Cambiar");
		btnCambiarTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tableTipos.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(frame, "Seleccione un tipo primero.");
					return;
				}
				int id = (int) tableTipos.getValueAt(fila, 0);
				try {
					TipoItem tipo = control.obtenerTipo(id);
					RegistroTipo dialog = new RegistroTipo();
					dialog.cargarDatosTipo(tipo);
					dialog.setVisible(true);
					cargarTipos();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnCambiarTipo.setBounds(152, 11, 89, 23);
		panelTipos.add(btnCambiarTipo);
		
		JButton btnBorrarTipo = new JButton("Borrar");
		btnBorrarTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tableTipos.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(frame, "Seleccione un tipo primero.");
					return;
				}
				int id = (int) tableTipos.getValueAt(fila, 0);
				int confirmar = JOptionPane.showConfirmDialog(frame,
					"¿Seguro de borrar este tipo? Los ítems pasarán al tipo Genérico.", "Confirmar", JOptionPane.YES_NO_OPTION);
				if (confirmar == JOptionPane.YES_OPTION) {
					try {
						control.borrarTipo(id);
						cargarTipos();
						cargarItems();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnBorrarTipo.setBounds(251, 11, 89, 23);
		panelTipos.add(btnBorrarTipo);
		
		JScrollPane scrollPaneTipo = new JScrollPane();
		scrollPaneTipo.setBounds(37, 58, 457, 236);
		panelTipos.add(scrollPaneTipo);
		
		tableTipos = new JTable();
		tableTipos.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"#", "Nombre", "Descripci\u00F3n", "\u00CDtems"
			}
		) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		tableTipos.getColumnModel().getColumn(0).setResizable(false);
		tableTipos.getColumnModel().getColumn(1).setResizable(false);
		tableTipos.getColumnModel().getColumn(1).setPreferredWidth(115);
		tableTipos.getColumnModel().getColumn(2).setResizable(false);
		tableTipos.getColumnModel().getColumn(2).setPreferredWidth(170);
		tableTipos.getColumnModel().getColumn(3).setResizable(false);
		scrollPaneTipo.setViewportView(tableTipos);
		
		// pestaña categorias
		
		JPanel panelCategs = new JPanel();
		tabsAdmin.addTab("Categorías", null, panelCategs, null);
		panelCategs.setLayout(null);
		
		JButton btnNuevaCateg = new JButton("Nueva categoría");
		btnNuevaCateg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistroCategoria dialog = new RegistroCategoria();
				dialog.setVisible(true);
				cargarCategorias();
			}
		});
		btnNuevaCateg.setBounds(35, 11, 111, 23);
		panelCategs.add(btnNuevaCateg);
		
		JButton btnCambiarCateg = new JButton("Cambiar");
		btnCambiarCateg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tableCategs.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(frame, "Seleccione una categoría primero.");
					return;
				}
				int id = (int) tableCategs.getValueAt(fila, 0);
				try {
					Categoria categ = control.obtenerCategoria(id);
					RegistroCategoria dialog = new RegistroCategoria();
					dialog.cargarDatosCategs(categ);
					dialog.setVisible(true);
					cargarCategorias();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnCambiarCateg.setBounds(156, 11, 89, 23);
		panelCategs.add(btnCambiarCateg);
		
		JButton btnBorrarCateg = new JButton("Borrar");
		btnBorrarCateg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tableCategs.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(frame, "Seleccione una categoría primero.");
					return;
				}
				int id = (int) tableCategs.getValueAt(fila, 0);
				int confirmar = JOptionPane.showConfirmDialog(frame,
					"¿Seguro de borrar esta categría? Los ítems ya no van a pertenecer a esta categoría.", "Confirmar", JOptionPane.YES_NO_OPTION);
				if (confirmar == JOptionPane.YES_OPTION) {
					try {
						control.borrarCategoria(id);
						cargarCategorias();
						cargarItems();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnBorrarCateg.setBounds(255, 11, 89, 23);
		panelCategs.add(btnBorrarCateg);
		
		JScrollPane scrollPaneCateg = new JScrollPane();
		scrollPaneCateg.setBounds(35, 54, 462, 246);
		panelCategs.add(scrollPaneCateg);
		
		tableCategs = new JTable();
		tableCategs.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"#", "Nombre", "Descripci\u00F3n", "\u00CDtems"
			}
		) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		tableCategs.getColumnModel().getColumn(0).setResizable(false);
		tableCategs.getColumnModel().getColumn(1).setResizable(false);
		tableCategs.getColumnModel().getColumn(1).setPreferredWidth(140);
		tableCategs.getColumnModel().getColumn(2).setResizable(false);
		tableCategs.getColumnModel().getColumn(2).setPreferredWidth(144);
		tableCategs.getColumnModel().getColumn(3).setResizable(false);
		scrollPaneCateg.setViewportView(tableCategs);
		
		// pestaña items
		
		JPanel panelItems = new JPanel();
		tabsAdmin.addTab("Ítems", null, panelItems, null);
		panelItems.setLayout(null);
		
		JButton btnNuevoItem = new JButton("Nuevo Ítem");
		btnNuevoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					RegistroItem dialog = new RegistroItem();
					dialog.setVisible(true);
					cargarItems();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNuevoItem.setBounds(34, 11, 105, 23);
		panelItems.add(btnNuevoItem);
		
		JButton btnCambiarItem = new JButton("Cambiar");
		btnCambiarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tableItems.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(frame, "Seleccione un ítem primero.");
					return;
				}
				int codigo = (int) tableItems.getValueAt(fila, 0);
				try {
					Item item = control.obtenerItem(codigo);
					RegistroItem dialog = new RegistroItem();
					dialog.cargarDatosItem(item);
					dialog.setVisible(true);
					cargarItems();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnCambiarItem.setBounds(149, 11, 89, 23);
		panelItems.add(btnCambiarItem);
		
		JButton btnBorrarItem = new JButton("Borrar");
		btnBorrarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tableItems.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(frame, "Seleccione un ítem primero.");
					return;
				}
				int codigo = (int) tableItems.getValueAt(fila, 0);
				int confirmar = JOptionPane.showConfirmDialog(frame,
					"¿Está seguro de borrar este ítem?", "Confirmar",
					JOptionPane.YES_NO_OPTION);
				if (confirmar == JOptionPane.YES_OPTION) {
					try {
						control.borrarItem(codigo);
						cargarItems();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnBorrarItem.setBounds(248, 11, 89, 23);
		panelItems.add(btnBorrarItem);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(34, 57, 460, 244);
		panelItems.add(scrollPane_1);
		
		tableItems = new JTable();
		tableItems.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"#", "Nombre", "Tipo", "Categorias", "Estado"
			}
		) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		tableItems.getColumnModel().getColumn(0).setResizable(false);
		tableItems.getColumnModel().getColumn(2).setResizable(false);
		tableItems.getColumnModel().getColumn(3).setResizable(false);
		tableItems.getColumnModel().getColumn(4).setResizable(false);
		scrollPane_1.setViewportView(tableItems);
		
		
		// Prestamos
		
		JPanel panelPresta = new JPanel();
		sistema.addTab("Préstamos", null, panelPresta, null);
		panelPresta.setLayout(null);
		
		JLabel lblSeleccione = new JLabel("Seleccione un préstamo para finalizarlo o ver sus ítems para retornar");
		lblSeleccione.setBounds(19, 0, 469, 23);
		panelPresta.add(lblSeleccione);		
		
		JButton btnNuevoPrestamo = new JButton("Nuevo préstamo");
		btnNuevoPrestamo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CrearPrestamo crea = new CrearPrestamo();
					crea.setVisible(true);
					cargarPrestamos();
					cargarItems();
					cargarPersonas();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNuevoPrestamo.setBounds(new Rectangle(19, 25, 130, 23));
		panelPresta.add(btnNuevoPrestamo);
		
		JButton btnFinalizar = new JButton("Finalizar préstamo");
		btnFinalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tablePrestamos.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(frame, "Seleccione un préstamo primero.");
					return;
				}
				int idPrestamo = (int) tablePrestamos.getValueAt(fila, 0);
				int confirmar = JOptionPane.showConfirmDialog(frame, "¿Finalizar el préstamo? Los ítems serán liberados", "Confirmar", JOptionPane.YES_NO_OPTION);
				if (confirmar == JOptionPane.YES_OPTION) {
					try {
						control.finalizarPrestamo(idPrestamo);
						cargarPrestamos();
						cargarItems();
						cargarPersonas();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnFinalizar.setBounds(new Rectangle(279, 25, 140, 23));
		btnFinalizar.setHorizontalTextPosition(SwingConstants.CENTER);
		panelPresta.add(btnFinalizar);
		
		JButton btnRetornar = new JButton("Retornar ítem");
		btnRetornar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tablePrestamos.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(frame, "Seleccione un préstamo primero.");
					return;
				}
				int idPrestamo = (int) tablePrestamos.getValueAt(fila, 0);
				try {
					Prestamo prestamo = control.obtenerPrestamo(idPrestamo);
					List<Item> itemsPrestamo = prestamo.getItems();
					
					if (itemsPrestamo.isEmpty()) {
						JOptionPane.showMessageDialog(frame, "Este préstamo no tiene ítems.");
						return;
					}
					
					Item select = (Item) JOptionPane.showInputDialog(frame, "Seleccione el ítem a retornar:", "Retornar ítem", JOptionPane.PLAIN_MESSAGE, null, itemsPrestamo.toArray(), itemsPrestamo.get(0));
					if (select == null)
						return;
					
					int confirmar = JOptionPane.showConfirmDialog(frame, "¿Seguro que desea retornar el ítem?", "Confirmar", JOptionPane.YES_NO_OPTION);
					if (confirmar == JOptionPane.YES_OPTION) {
						control.retornarItemDePrestamo(idPrestamo, select.getCodigo());
						cargarPrestamos();
						cargarItems();
						cargarPersonas();
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnRetornar.setBounds(159, 25, 110, 23);
		panelPresta.add(btnRetornar);
		
		
		JScrollPane scrollPrestamos = new JScrollPane();
		scrollPrestamos.setBounds(19, 59, 485, 281);
		panelPresta.add(scrollPrestamos);
		
		tablePrestamos = new JTable();
		
		tablePrestamos.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"#", "Usuario", "Fecha inicio", "\u00CDtems", "Alerta"}
		) {
			
			public boolean isCellEditable(int row, int column) {
				return false;}
		});
		tablePrestamos.getColumnModel().getColumn(0).setPreferredWidth(30);
		tablePrestamos.getColumnModel().getColumn(1).setPreferredWidth(120);
		tablePrestamos.getColumnModel().getColumn(2).setPreferredWidth(91);
		tablePrestamos.getColumnModel().getColumn(3).setResizable(false);
		tablePrestamos.getColumnModel().getColumn(4).setResizable(false);
		scrollPrestamos.setViewportView(tablePrestamos);
		
		// Reportes
		JPanel panelReportes = new JPanel();
		sistema.addTab("Reportes", null, panelReportes, null);
		panelReportes.setLayout(null);
		
		JButton btnRepUsuario = new JButton("Por usuario");
		btnRepUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaReporte repo = new VentanaReporte("Reporte por usuario", control.reportePorUsuario());
					repo.setVisible(true);
			}
		});
		btnRepUsuario.setBounds(185, 31, 141, 51);
		panelReportes.add(btnRepUsuario);
		
		JButton btnRepItem = new JButton("Por ítem");
		btnRepItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaReporte repo;
				try {
					repo = new VentanaReporte("Reporte por ítem", control.reportePorItem());
					repo.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRepItem.setBounds(185, 93, 141, 51);
		panelReportes.add(btnRepItem);
		
		JButton btnRepTipo = new JButton("Por tipo");
		btnRepTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaReporte repo = new VentanaReporte("Reporte por tipo", control.reportePorTipo());
				repo.setVisible(true);
			}
		});
		btnRepTipo.setBounds(185, 155, 141, 49);
		panelReportes.add(btnRepTipo);
		
		JButton btnRepCateg = new JButton("Por categoría");
		btnRepCateg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaReporte repo = new VentanaReporte("Reporte por categoría", control.reportePorCategoria());
				repo.setVisible(true);
			}
		});
		btnRepCateg.setBounds(185, 215, 141, 49);
		panelReportes.add(btnRepCateg);
		
	}
	
	// cargar
	
	private void cargarPrestamos() {
		DefaultTableModel modelo = (DefaultTableModel) tablePrestamos.getModel();
		modelo.setRowCount(0);
		for (Prestamo p : control.obtenerListadoPrestamos()) {
			modelo.addRow(new Object[] {
					p.getId(), p.getUsuario().getNombre(), p.getFechaInicio().format(formatoFecha), p.cantidadItems(), p.tieneAlerta() ? "Sí" : "No"
			});
		}
	}
	
	private void cargarItems() {
		DefaultTableModel modelo = (DefaultTableModel) tableItems.getModel();
		modelo.setRowCount(0);
		for (Item item : control.obtenerListadoItems()) {
			StringBuilder categorias = new StringBuilder();
			for (Categoria c : item.getCategorias()) {
				if (categorias.length() >0)
					categorias.append(", ");
				categorias.append(c.getNombre());
			}
			modelo.addRow(new Object[] {
					item.getCodigo(), item.getNombre(), item.getTipo().getNombre(), categorias.toString(), item.isPrestado() ? "Prestado" : "Disponible" 
			});
		}
	}
	
	private void cargarPersonas() {
		DefaultTableModel modelo = (DefaultTableModel) tablePersonas.getModel();
		modelo.setRowCount(0);
		for (Usuario u : control.obtenerListadoUsuarios()) {
			modelo.addRow(new Object[] {
				u.getNumero(), u.getNombre(), u.getTelefono(), u.getEmail(), u.tienePrestamos() ? "Sí" : "No"
			});
		}
	}
	
	private void cargarTipos() {
		DefaultTableModel modelo = (DefaultTableModel) tableTipos.getModel();
		modelo.setRowCount(0);
		for (TipoItem tipo : control.obtenerListadoTipos()) {
			modelo.addRow(new Object[] {
				tipo.getId(), tipo.getNombre(), tipo.getDescripcion(), tipo.getItems().size()
			});
		}
	}
	
	private void cargarCategorias() {
		DefaultTableModel modelo = (DefaultTableModel) tableCategs.getModel();
		modelo.setRowCount(0);
		for (Categoria categoria : control.obtenerListadoCategorias()) {
			modelo.addRow(new Object[] {
					categoria.getId(), categoria.getNombre(), categoria.getDescripcion(), categoria.getItems().size()
			});
		}
	}
	
	private static final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
}
