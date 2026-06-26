package interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.Controladora;
import logica.Categoria;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistroCategoria extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextField txtDescrip;

	private Controladora control = Controladora.getInstance();
	private Categoria catEdit;
	
	public RegistroCategoria() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setBounds(100, 100, 279, 196);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(10, 11, 46, 14);
		contentPanel.add(lblNombre);
	
	
		txtNombre = new JTextField();
		txtNombre.setColumns(10);
		txtNombre.setBounds(10, 29, 245, 20);
		contentPanel.add(txtNombre);
	
	
		JLabel lblDescripcion = new JLabel("Descripción");
		lblDescripcion.setBounds(10, 60, 73, 14);
		contentPanel.add(lblDescripcion);
	
	
		txtDescrip = new JTextField();
		txtDescrip.setColumns(10);
		txtDescrip.setBounds(10, 78, 245, 20);
		contentPanel.add(txtDescrip);
		
		
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

	public void cargarDatosCategs(Categoria categoria) {
		this.catEdit = categoria;
		setTitle("Modificar categoría");
		txtNombre.setText(categoria.getNombre());
		txtDescrip.setText(categoria.getDescripcion());
	}
	
	private void guardar() {
		String nombre = txtNombre.getText().trim();
		String descripcion = txtDescrip.getText().trim();
		
		if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			if (catEdit == null) {
				control.crearCategoria(nombre, descripcion);
			} else {
				control.actualizarCategoria(catEdit.getId(), nombre, descripcion);
			}
			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
