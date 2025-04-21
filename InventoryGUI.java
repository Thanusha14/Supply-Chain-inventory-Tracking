package project;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.List;

//... all your imports

public class InventoryGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private InventoryManager manager;
	private JTable table;
 private DefaultTableModel model;
 private JTextField tfId, tfName, tfCategory, tfPrice, tfQuantity, tfSupplier, tfThreshold;

	public InventoryGUI() {
		manager = new InventoryManager();
     setTitle("Inventory Management System");
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setSize(800, 600);
     setLayout(null);
     
     String[] columns = {"ID", "Name", "Category", "Price", "Quantity", "Supplier"};
     model = new DefaultTableModel(columns, 0);
     table = new JTable(model);
     JScrollPane scrollPane = new JScrollPane(table);
     scrollPane.setBounds(20, 20, 740, 200);
     add(scrollPane);
     
     JLabel lblId = new JLabel("Product ID:");
     lblId.setBounds(20, 240, 100, 25);
     add(lblId);
     tfId = new JTextField();
     tfId.setBounds(120, 240, 150, 25);
     add(tfId);
     
     JLabel lblName = new JLabel("Name:");
     lblName.setBounds(300, 240, 100, 25);
     add(lblName);
     tfName = new JTextField();
     tfName.setBounds(370, 240, 150, 25);
     add(tfName);

     JLabel lblCategory = new JLabel("Category:");
     lblCategory.setBounds(20, 280, 100, 25);
     add(lblCategory);
     tfCategory = new JTextField();
     tfCategory.setBounds(120, 280, 150, 25);
     add(tfCategory);
     
     JLabel lblPrice = new JLabel("Price:");
     lblPrice.setBounds(300, 280, 100, 25);
     add(lblPrice);
     tfPrice = new JTextField();
     tfPrice.setBounds(370, 280, 150, 25);
     add(tfPrice);

     JLabel lblQuantity = new JLabel("Quantity:");
     lblQuantity.setBounds(20, 320, 100, 25);
     add(lblQuantity);
     tfQuantity = new JTextField();
     tfQuantity.setBounds(120, 320, 150, 25);
     add(tfQuantity);
     
     JLabel lblSupplier = new JLabel("Supplier:");
     lblSupplier.setBounds(300, 320, 100, 25);
     add(lblSupplier);
     tfSupplier = new JTextField();
     tfSupplier.setBounds(370, 320, 150, 25);
     add(tfSupplier);

     JButton btnAdd = new JButton("Add Product");
     btnAdd.setBounds(550, 240, 150, 25);
     add(btnAdd);

     JButton btnShowAlerts = new JButton("Low Stock Alerts");
     btnShowAlerts.setBounds(550, 280, 150, 25);
     add(btnShowAlerts);

     JLabel lblThreshold = new JLabel("Threshold:");
     lblThreshold.setBounds(550, 310, 70, 25);
     add(lblThreshold);

     tfThreshold = new JTextField("20");
     tfThreshold.setBounds(620, 310, 80, 25);
     add(tfThreshold);
     
     // Button Listeners
     btnAdd.addActionListener(e -> {
         try {
             int id = Integer.parseInt(tfId.getText());
             String name = tfName.getText();
             String category = tfCategory.getText();
             double price = Double.parseDouble(tfPrice.getText());
             int quantity = Integer.parseInt(tfQuantity.getText());
             String supplier = tfSupplier.getText();

             manager.addProduct(id, name, category, quantity, price, supplier);
             refreshTable();
             clearFields();
         } catch (Exception ex) {
             JOptionPane.showMessageDialog(this, "Invalid input!");
         }
     });

     btnShowAlerts.addActionListener(e -> {
         try {
             int threshold = Integer.parseInt(tfThreshold.getText());
             manager.generateLowStockAlerts(threshold);
             List<Product> alerts = manager.getLowStockAlerts();
             refreshTable();
             highlightLowStock(alerts);

             JOptionPane.showMessageDialog(this,
                     "Check table for low stock products below threshold: " + threshold);
         } catch (Exception ex) {
             JOptionPane.showMessageDialog(this, "Invalid threshold!");
         }
     });

     setVisible(true);
	}

 private void refreshTable() {
     model.setRowCount(0);
     for (Product p : manager.getInventory()) {
         Object[] row = {p.productId, p.name, p.category, p.price, p.quantity, p.supplier};
         model.addRow(row);
     }
 }

 private void clearFields() {
     tfId.setText("");
     tfName.setText("");
     tfCategory.setText("");
     tfPrice.setText("");
     tfQuantity.setText("");
     tfSupplier.setText("");
 }

 private void highlightLowStock(List<Product> alerts) {
	    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
	        @Override
	        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
	                                                                boolean isSelected, boolean hasFocus,
	                                                                int row, int column) {
	            java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	            int modelRow = table.convertRowIndexToModel(row);
	            int productId = Integer.parseInt(model.getValueAt(modelRow, 0).toString()); // FIXED

	            boolean isLowStock = alerts.stream().anyMatch(p -> p.productId == productId);

	            if (isLowStock) {
	                c.setBackground(new java.awt.Color(255, 204, 204)); // Light red
	            } else {
	                c.setBackground(java.awt.Color.WHITE);
	            }

	            return c;
	        }
	    });

	    table.repaint();
	}

 public static void main(String[] args) {
     SwingUtilities.invokeLater(() -> new InventoryGUI());
 }
}
