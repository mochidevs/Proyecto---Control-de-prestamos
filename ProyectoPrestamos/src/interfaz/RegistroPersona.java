package interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import control.Controladora;
import logica.Usuario;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistroPersona extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JTextField txtTelefono;
	private JTextField txtEmail;

	private Controladora control = Controladora.getInstance();
	private Usuario personaEdit;

	public RegistroPersona() {
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Nueva persona");
		setBounds(100, 100, 283, 236);
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
		
		JLabel lblTelefono = new JLabel("Teléfono");
		lblTelefono.setBounds(10, 59, 46, 14);
		contentPanel.add(lblTelefono);
		
		txtTelefono = new JTextField();
		txtTelefono.setColumns(10);
		txtTelefono.setBounds(10, 75, 245, 20);
		contentPanel.add(txtTelefono);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(10, 106, 46, 14);
		contentPanel.add(lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(10, 123, 245, 20);
		contentPanel.add(txtEmail);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnCancel = new JButton("Cancelar");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(btnCancel);
			}
			{
				JButton btnGuardar = new JButton("Guardar");
				btnGuardar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						guardar();
					}
				});
				buttonPane.add(btnGuardar);
				getRootPane().setDefaultButton(btnGuardar);
			}
		}
		
	}
	public void cargarDatos(Usuario usuario) {
		this.personaEdit = usuario;
		setTitle("Modificar usuario");
		txtNombre.setText(usuario.getNombre());
		txtTelefono.setText(usuario.getTelefono());
		txtEmail.setText(usuario.getEmail());
	}
	
	private void guardar() {
		String nombre = txtNombre.getText().trim();
		String telefono = txtTelefono.getText().trim();
		String email = txtEmail.getText().trim();

		if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			if (personaEdit == null) {
				control.crearUsuario(nombre, telefono, email);
			} else {
				control.actualizarUsuario(personaEdit.getNumero(), nombre, telefono, email);
			}
			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
		
}
