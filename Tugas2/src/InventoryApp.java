import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Kelas generic untuk item inventaris
class Item<T> {
    private String name;
    private T quantity;  // Bisa Integer, Double, dll
    private String category;

    public Item(String name, T quantity, String category) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Item: " + name + ", Quantity: " + quantity + ", Category: " + category;
    }

    // Getter
    public String getName() { return name; }
    public T getQuantity() { return quantity; }
    public String getCategory() { return category; }
}

// Kelas untuk mengelola inventaris
class InventoryManager {
    private List<Item<?>> inventory;  // Wildcard untuk menerima berbagai tipe quantity

    public InventoryManager() {
        inventory = new ArrayList<>();
    }

    // Generic method untuk menambahkan item
    public <T> void addItem(String name, T quantity, String category) {
        Item<T> item = new Item<>(name, quantity, category);
        inventory.add(item);
    }

    // Method dengan wildcard untuk menampilkan item berdasarkan kategori
    public List<Item<?>> getItemsByCategory(String category) {
        List<Item<?>> result = new ArrayList<>();
        for (Item<?> item : inventory) {
            if (item.getCategory().equals(category)) {
                result.add(item);
            }
        }
        return result;
    }

    // Generic method untuk mencari item berdasarkan nama
    public <T extends Item<?>> T findItem(String name, List<T> items) {
        for (T item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public List<Item<?>> getInventory() {
        return inventory;
    }
}

// Kelas utama dengan GUI
public class InventoryApp extends JFrame {
    private InventoryManager manager;
    private JTextArea displayArea;
    private JTextField nameField, quantityField, categoryField;

    public InventoryApp() {
        manager = new InventoryManager();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Inventory Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel input
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        JButton addButton = new JButton("Add Item");
        addButton.addActionListener(e -> addItem());
        inputPanel.add(addButton);

        JButton showButton = new JButton("Show Category Items");
        showButton.addActionListener(e -> showCategoryItems());
        inputPanel.add(showButton);

        // Display area
        displayArea = new JTextArea(15, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Tambahkan ke frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addItem() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String quantityStr = quantityField.getText();

        try {
            // Coba parse sebagai Integer atau Double
            if (quantityStr.contains(".")) {
                Double quantity = Double.parseDouble(quantityStr);
                manager.addItem(name, quantity, category);
            } else {
                Integer quantity = Integer.parseInt(quantityStr);
                manager.addItem(name, quantity, category);
            }
            displayArea.append("Added: " + name + "\n");
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity format!");
        }
    }

    private void showCategoryItems() {
        String category = categoryField.getText();
        List<Item<?>> items = manager.getItemsByCategory(category);
        displayArea.setText("Items in " + category + ":\n");
        for (Item<?> item : items) {
            displayArea.append(item.toString() + "\n");
        }
    }

    private void clearFields() {
        nameField.setText("");
        quantityField.setText("");
        categoryField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InventoryApp app = new InventoryApp();
            app.setVisible(true);
        });
    }
}