import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ProgressApp extends JFrame {

    private JButton startButton;
    private JProgressBar progressBar;

    public ProgressApp() {
        // Konfigurasi JFrame
        setTitle("Simple Multi-Thread Progress App");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inisialisasi komponen
        startButton = new JButton("Start");
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(startButton, gbc);

        gbc.gridy = 1;
        add(progressBar, gbc);

        // Event listener untuk tombol Start
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false); // Nonaktifkan tombol saat proses berjalan
                progressBar.setValue(0); // Reset progress bar
                new BackgroundTask().execute(); // Jalankan tugas latar belakang
            }
        });
    }

    // Kelas SwingWorker untuk menangani tugas latar belakang
    private class BackgroundTask extends SwingWorker<Void, Integer> {
        @Override
        protected Void doInBackground() throws Exception {
            // Simulasi tugas yang memakan waktu
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(50); // Penundaan untuk simulasi
                publish(i); // Kirim nilai kemajuan ke process
            }
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            // Update progress bar di EDT
            int progress = chunks.get(chunks.size() - 1);
            progressBar.setValue(progress);
        }

        @Override
        protected void done() {
            // Selesai, aktifkan kembali tombol
            startButton.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        // Pastikan GUI dibuat di Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ProgressApp().setVisible(true);
            }
        });
    }
}