package interfaz;

import java.awt.BorderLayout;


import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import control.Controladora;
import logica.Item;
import logica.Prestamo;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ListadoItems extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	private Controladora control = Controladora.getInstance();
	private Prestamo prestamo;
	private JTable tableItems;


	public ListadoItems(Prestamo prestamo) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.prestamo = prestamo;
		
		setModal(true);
		setTitle("Ítems del préstamo #" + prestamo.getId());
		setBounds(100, 100, 400, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 360, 250);
		contentPanel.add(scrollPane);
		
		tableItems = new JTable();
		tableItems.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"#", "Nombre", "Tipo"}
		) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		tableItems.getColumnModel().getColumn(0).setResizable(false);
		tableItems.getColumnModel().getColumn(1).setPreferredWidth(145);
		tableItems.getColumnModel().getColumn(2).setPreferredWidth(106);
		scrollPane.setViewportView(tableItems);
		
		JButton btnRetornar = new JButton("Retornar ítem seleccionado");
		btnRetornar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				retornarItem();
			}
		});
		btnRetornar.setBounds(10, 270, 220, 23);
		contentPanel.add(btnRetornar);

		cargarTabla();
		
	}
	
	private void cargarTabla() {
		DefaultTableModel modelo = (DefaultTableModel) tableItems.getModel();
		modelo.setRowCount(0);
		for (Item item : prestamo.getItems()) {
			modelo.addRow(new Object[] {
					item.getCodigo(), item.getNombre(), item.getTipo().getNombre()
			});
		}
	}
	
	private void retornarItem() {
		int fila = tableItems.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione un ítem primero.");
			return;
		}
		int codigoItem = (int) tableItems.getValueAt(fila, 0);
		try {
			control.retornarItemDePrestamo(prestamo.getId(), codigoItem);
			cargarTabla();
			if (prestamo.getItems().isEmpty()) {
				JOptionPane.showMessageDialog(this, "El préstamo ya no tiene ítems.");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
