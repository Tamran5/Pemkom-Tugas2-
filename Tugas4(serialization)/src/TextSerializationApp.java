import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TextSerializationApp extends JFrame {
    private JTextArea textArea;
    private JButton saveButton, loadButton;
    private JFileChooser fileChooser;

    public TextSerializationApp() {
        // Inisialisasi komponen GUI
        setTitle("Text Serialization App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout());

        // Text Area
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Panel untuk tombol
        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("Save (Serialize)");
        loadButton = new JButton("Load (Deserialize)");
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // File Chooser
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        // Event Listener untuk tombol Save
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveText();
            }
        });

        // Event Listener untuk tombol Load
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadText();
            }
        });
    }

    // Kelas untuk menyimpan data teks (harus Serializable)
    static class TextData implements Serializable {
        private String text;

        public TextData(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    // Fungsi untuk menyimpan teks (Serialization)
    private void saveText() {
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                TextData data = new TextData(textArea.getText());
                oos.writeObject(data);
                JOptionPane.showMessageDialog(this, "Text saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving text: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Fungsi untuk memuat teks (Deserialization)
    private void loadText() {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                TextData data = (TextData) ois.readObject();
                textArea.setText(data.getText());
                JOptionPane.showMessageDialog(this, "Text loaded successfully!");
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error loading text: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TextSerializationApp().setVisible(true);
        });
    }
}