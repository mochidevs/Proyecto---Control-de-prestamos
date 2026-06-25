package interfaz;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import control.Controladora;
import logica.TipoItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistroTipo extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextField txtDescripcion;
	
	private Controladora controladora = Controladora.getInstance();
	private TipoItem tipoEdit;
	
	public RegistroTipo() {
		setModal(true);
		setTitle("Nuevo tipo");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(10, 11, 46, 14);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(10, 28, 245, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);
		
		JLabel lblDescripcion = new JLabel("Descripción");
		lblDescripcion.setBounds(10, 59, 73, 14);
		contentPanel.add(lblDescripcion);

		txtDescripcion = new JTextField();
		txtDescripcion.setColumns(10);
		txtDescripcion.setBounds(10, 75, 245, 20);
		contentPanel.add(txtDescripcion);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			dispose();
			}
		});
		buttonPane.add(btnCancel);

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		});
		buttonPane.add(btnGuardar);
		getRootPane().setDefaultButton(btnGuardar);
	}
	
	public void cargarDatosTipo(TipoItem tipo) {
		this.tipoEdit = tipo;
		setTitle("Modificar tipo");
		txtNombre.setText(tipo.getNombre());
		txtDescripcion.setText(tipo.getDescripcion());
	}
	
	private void guardar() {
		String nombre = txtNombre.getText().trim();
		String descripcion = txtDescripcion.getText().trim();

		if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			if (tipoEdit == null) {
				controladora.crearTipo(nombre, descripcion);
			} else {
				controladora.actualizarTipo(tipoEdit.getId(), nombre, descripcion);
			}
			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
