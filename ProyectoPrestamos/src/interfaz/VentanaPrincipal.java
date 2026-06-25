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
		frame.setTitle("Sistema de Préstamos");
		frame.setBounds(100, 100, 551, 429);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
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
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, Object.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tableItems.getColumnModel().getColumn(0).setResizable(false);
		tableItems.getColumnModel().getColumn(2).setResizable(false);
		tableItems.getColumnModel().getColumn(3).setResizable(false);
		tableItems.getColumnModel().getColumn(4).setResizable(false);
		scrollPane_1.setViewportView(tableItems);
		
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
					JOptionPane.showMessageDialog(frame, ex.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
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
					"¿Seguro de borrar este tipo? Los ítems pasarán al tipo Genérico.",
					"Confirmar", JOptionPane.YES_NO_OPTION);
				if (confirmar == JOptionPane.YES_OPTION) {
					try {
						control.borrarTipo(id);
						cargarTipos();
						cargarItems();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnBorrarTipo.setBounds(251, 11, 89, 23);
		panelTipos.add(btnBorrarTipo);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(37, 58, 457, 236);
		panelTipos.add(scrollPane_2);
		
		tableTipos = new JTable();
		tableTipos.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"#", "Nombre", "Descripci\u00F3n", "\u00CDtems"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableTipos.getColumnModel().getColumn(0).setResizable(false);
		tableTipos.getColumnModel().getColumn(1).setResizable(false);
		tableTipos.getColumnModel().getColumn(1).setPreferredWidth(115);
		tableTipos.getColumnModel().getColumn(2).setResizable(false);
		tableTipos.getColumnModel().getColumn(2).setPreferredWidth(170);
		tableTipos.getColumnModel().getColumn(3).setResizable(false);
		scrollPane_2.setViewportView(tableTipos);
		
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
		
		
		// Prestamos
		
		JPanel panelPresta = new JPanel();
		sistema.addTab("Préstamos", null, panelPresta, null);
		panelPresta.setLayout(null);

		JLabel lblSeleccione = new JLabel("Seleccione un préstamo para finalizarlo o ver sus ítems para retornar");
		lblSeleccione.setBounds(19, 0, 362, 23);
		panelPresta.add(lblSeleccione);		
		
		JButton btnNuevoPrestamo = new JButton("Nuevo préstamo");
		btnNuevoPrestamo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CrearPrestamo crea = new CrearPrestamo();
					crea.setVisible(true);
					cargarPrestamos();
					cargarItems();
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
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnFinalizar.setBounds(new Rectangle(159, 25, 140, 23));
		btnFinalizar.setHorizontalTextPosition(SwingConstants.CENTER);
		panelPresta.add(btnFinalizar);

		JButton btnVerItems = new JButton("Ver ítems");
		btnVerItems.setActionCommand("");
		btnVerItems.setBounds(309, 25, 89, 23);
		panelPresta.add(btnVerItems);
		
		
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
		
	}
	
	// cargar
	
	private void cargarPrestamos() {
		DefaultTableModel modelo = (DefaultTableModel) tablePrestamos.getModel();
		modelo.setRowCount(0);
		for (Prestamo p : control.obtenerListadoPrestamos()) {
			modelo.addRow(new Object[] {
					p.getId(), p.getUsuario().getNombre(), p.getFechaInicio(), p.cantidadItems(), p.tieneAlerta() ? "Sí" : "No"
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
				u.getNumero(), u.getNombre(), u.getTelefono(), u.getEmail(),
				u.tienePrestamos() ? "Sí" : "No"
			});
		}
	}
	
	private void cargarTipos() {
		DefaultTableModel modelo = (DefaultTableModel) tableTipos.getModel();
		modelo.setRowCount(0);
		for (TipoItem tipo : control.obtenerListadoTipos()) {
			modelo.addRow(new Object[] {
				tipo.getId(), tipo.getNombre(), tipo.getDescripcion(),
				tipo.getItems().size()
			});
		}
	}
}
