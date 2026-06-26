package interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;


import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import control.Controladora;
import logica.Item;
import logica.Prestamo;
import logica.Usuario;

import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class CrearPrestamo extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	private JComboBox<Usuario> selectUsuario;
	private JTextField txtMensajeAlerta;
	private JCheckBox chkRecurrente;
	private JSpinner spinnerIntervalo;
	
	private Controladora control = Controladora.getInstance();
	private Prestamo prestamoEnCreacion;
	private JTextArea areaItemsPrestamo;

	public CrearPrestamo() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setBounds(100, 100, 268, 394);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setBounds(18, 11, 36, 14);
		contentPanel.add(lblUsuario);
		
		selectUsuario = new JComboBox<>();
		selectUsuario.setBounds(18, 29, 205, 22);
		contentPanel.add(selectUsuario);
		for (Usuario u : control.obtenerListadoUsuarios()) {
			selectUsuario.addItem(u);
		}
		
		JLabel lblItems = new JLabel("Ítems seleccionados (Agregue para ver)");
		lblItems.setBounds(18, 62, 205, 14);
		contentPanel.add(lblItems);
		
		
		
		JButton btnAgregarItem = new JButton("Agregar ítem");
		btnAgregarItem.setBounds(18, 161, 95, 23);
		btnAgregarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregarItem();
			}
		});
		contentPanel.add(btnAgregarItem);
		
		JButton btnQuitarItem = new JButton("Quitar ítem");
		btnQuitarItem.setBounds(123, 161, 85, 23);
		btnQuitarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quitarItem();
			}
		});
		contentPanel.add(btnQuitarItem);
		
		JLabel lblAlerta = new JLabel("Mensaje de alerta (opcional)");
		lblAlerta.setBounds(18, 191, 205, 14);
		contentPanel.add(lblAlerta);
		
		txtMensajeAlerta = new JTextField();
		txtMensajeAlerta.setBounds(10, 216, 218, 20);
		contentPanel.add(txtMensajeAlerta);
		txtMensajeAlerta.setColumns(10);
		
		chkRecurrente = new JCheckBox("¿Recurrente?");
		chkRecurrente.setBounds(18, 244, 89, 23);
		contentPanel.add(chkRecurrente);
		
		JLabel lblIntervalo = new JLabel("Intervalo (segundos)");
		lblIntervalo.setBounds(104, 277, 101, 14);
		contentPanel.add(lblIntervalo);
		
		spinnerIntervalo = new JSpinner(new SpinnerNumberModel(60, 1, 1800, 1));
		spinnerIntervalo.setBounds(23, 274, 63, 20);
		contentPanel.add(spinnerIntervalo);
		
		JScrollPane scrollItems = new JScrollPane();
		scrollItems.setBounds(18, 79, 205, 71);
		contentPanel.add(scrollItems);
		
		areaItemsPrestamo = new JTextArea();
		areaItemsPrestamo.setEditable(false);
		scrollItems.setViewportView(areaItemsPrestamo);
		
		JPanel panelBtn = new JPanel();
		panelBtn.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(panelBtn, BorderLayout.SOUTH);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelar();
			}
		});
		panelBtn.add(btnCancelar);
		
		JButton btnCrear = new JButton("Crear préstamo");
		btnCrear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				crear();
			}
		});
		panelBtn.add(btnCrear);
	}
	
	private void agregarItem() {
		Usuario usuario = (Usuario) selectUsuario.getSelectedItem();
		if (usuario == null) {
			JOptionPane.showMessageDialog(this, "Seleccione primero una persona.");
			return;
		}
		
		try {
			if (prestamoEnCreacion == null) {
				prestamoEnCreacion = control.crearPrestamo(usuario.getNumero());
			}

			List<Item> disponibles = control.obtenerListadoItemsDisponibles();
			if (disponibles.isEmpty()) {
				JOptionPane.showMessageDialog(this, "No hay ítems disponibles.");
				return;
			}

			Item seleccionado = (Item) JOptionPane.showInputDialog(this, "Seleccione un ítem:", "Agregar ítem", JOptionPane.PLAIN_MESSAGE, null, disponibles.toArray(), disponibles.get(0));
			
			if (seleccionado != null) {
				control.agregarItemAPrestamo(prestamoEnCreacion.getId(), seleccionado.getCodigo());
				cargarTablaItems();
			}	
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void quitarItem() {
		if (prestamoEnCreacion == null || prestamoEnCreacion.getItems().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "No hay ítems en el préstamo.");
	        return;
	    }

	    List<Item> items = prestamoEnCreacion.getItems();
	    Item seleccionado = (Item) JOptionPane.showInputDialog(this, "Seleccione el ítem a quitar:", "Quitar ítem", JOptionPane.PLAIN_MESSAGE, null, items.toArray(), items.get(0));

	    if (seleccionado != null) {
	        try {
	            control.eliminarItemDePrestamo(prestamoEnCreacion.getId(), seleccionado.getCodigo());
	            cargarTablaItems();
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
	
	private void cargarTablaItems() {
		StringBuilder sb = new StringBuilder();
	    for (Item item : prestamoEnCreacion.getItems()) {
	        sb.append("- ").append(item.getNombre()).append("\n");
	    }
	    areaItemsPrestamo.setText(sb.toString());
	}
	
	private void crear() {
		if (prestamoEnCreacion == null) {
			JOptionPane.showMessageDialog(this, "Debe agregar al menos un ítem.");
			return;
		}

		String mensaje = txtMensajeAlerta.getText().trim();
		try {
			if (!mensaje.isEmpty()) {
				boolean recurrente = chkRecurrente.isSelected();
				int intervalo = (int) spinnerIntervalo.getValue();
				control.agregarAlertaAPrestamo(prestamoEnCreacion.getId(), mensaje, recurrente, intervalo);
			}
			dispose();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void cancelar() {
		if (prestamoEnCreacion != null) {
			try {
				control.cancelarPrestamoEnCreacion(prestamoEnCreacion.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dispose();
	}
}
