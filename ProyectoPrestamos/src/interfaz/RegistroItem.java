package interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.Categoria;
import logica.Item;
import logica.TipoItem;
import control.Controladora;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistroItem extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private Controladora control = Controladora.getInstance();
	private JTextArea txtDescrip;
	private JTextField txtNombre;
	private JComboBox<TipoItem> selectTipo;
	private Item itemEdit;
	private DefaultListModel<Categoria> modeloCategorias;
	private JList<Categoria> listCategorias;

	public RegistroItem() {
		setTitle("Nuevo ítem");
		setBounds(100, 100, 250, 407);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNombreIt = new JLabel("Nombre");
			lblNombreIt.setBounds(10, 11, 46, 14);
			contentPanel.add(lblNombreIt);
		}
		{
			txtNombre = new JTextField();
			txtNombre.setBounds(10, 28, 209, 20);
			contentPanel.add(txtNombre);
			txtNombre.setColumns(10);
		}
		{
			JLabel lblDescrip = new JLabel("Descripción");
			lblDescrip.setBounds(10, 55, 73, 14);
			contentPanel.add(lblDescrip);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 69, 209, 66);
		contentPanel.add(scrollPane);
		
		JTextArea txtDescrip = new JTextArea();
		txtDescrip.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtDescrip.setLineWrap(true);
		txtDescrip.setTabSize(4);
		scrollPane.setViewportView(txtDescrip);
		
		JLabel lblTipo = new JLabel("Tipo");
		lblTipo.setBounds(10, 141, 34, 14);
		contentPanel.add(lblTipo);
		
		JComboBox<TipoItem> selectTipo = new JComboBox<>();
		selectTipo.setBounds(10, 156, 209, 22);
		contentPanel.add(selectTipo);
		
		for (TipoItem tipo: control.obtenerListadoTipos()) {
			selectTipo.addItem(tipo);
		}		
		
		JLabel lblCateg = new JLabel("Categorías (selección múltiple)");
		lblCateg.setBounds(10, 185, 159, 14);
		contentPanel.add(lblCateg);
		
		modeloCategorias = new DefaultListModel<>();
		for (Categoria categoria : control.obtenerListadoCategorias()) {
			modeloCategorias.addElement(categoria);
		}
		
		
		listCategorias = new JList<>(modeloCategorias);
		listCategorias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listCategorias.setBounds(10, 207, 209, 117);
		contentPanel.add(listCategorias);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			JButton btnCancelIt = new JButton("Cancelar");
			btnCancelIt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			buttonPane.add(btnCancelIt);
		}
		
		JButton btnGuardarIt = new JButton("Guardar");
		btnGuardarIt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		});
		buttonPane.add(btnGuardarIt);
		getRootPane().setDefaultButton(btnGuardarIt);
		
	}
	
	public void cargarDatosItem(Item item) {
		this.itemEdit = item;
		setTitle("Modificar ítem");
		txtNombre.setText(item.getNombre());
		txtDescrip.setText(item.getDescripcion());
		selectTipo.setSelectedItem(item.getTipo());

		for (int i = 0; i < modeloCategorias.size(); i++) {
			if (item.getCategorias().contains(modeloCategorias.get(i))) {
				listCategorias.addSelectionInterval(i, i);
			}
		}
	}
	
	public void guardar() {
		String nombre = txtNombre.getText().trim();
		String descripcion = txtDescrip.getText().trim();
		TipoItem tipoSeleccionado = (TipoItem) selectTipo.getSelectedItem();

		if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (tipoSeleccionado == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			Item item;
			if (itemEdit == null) {
				control.crearItem(nombre, descripcion, tipoSeleccionado.getId());
				item = control.obtenerListadoItems().get(control.obtenerListadoItems().size() - 1);
			} else {
				control.actualizarItem(itemEdit.getCodigo(), nombre, descripcion, tipoSeleccionado.getId());
				item = itemEdit;
			
				for (Categoria c : new java.util.ArrayList<>(item.getCategorias())) {
					control.eliminarCategoriaDeItem(item.getCodigo(), c.getId());
				}
			}
			for (Categoria c : listCategorias.getSelectedValuesList()) {
				control.agregarCategoriaAItem(item.getCodigo(), c.getId());
			}

			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
}
