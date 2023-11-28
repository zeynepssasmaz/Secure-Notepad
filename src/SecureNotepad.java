import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class SecureNotepad {

    private JTextArea textArea;
    private JFileChooser fileChooser;
    private Key desKey;
    private Key aesKey;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new SecureNotepad().createAndShowGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Secure Notepad");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });

        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        menuBar.add(fileMenu);

        JMenu encryptionMenu = new JMenu("Encryption");

        JMenuItem encryptDesItem = new JMenuItem("Encrypt (DES)");
        encryptDesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encryptText("DES");
            }
        });

        JMenuItem decryptDesItem = new JMenuItem("Decrypt (DES)");
        decryptDesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decryptText("DES");
            }
        });

        JMenuItem encryptAesItem = new JMenuItem("Encrypt (AES)");
        encryptAesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encryptText("AES");
            }
        });

        JMenuItem decryptAesItem = new JMenuItem("Decrypt (AES)");
        decryptAesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decryptText("AES");
            }
        });

        encryptionMenu.add(encryptDesItem);
        encryptionMenu.add(decryptDesItem);
        encryptionMenu.addSeparator();
        encryptionMenu.add(encryptAesItem);
        encryptionMenu.add(decryptAesItem);

        menuBar.add(encryptionMenu);

        frame.setJMenuBar(menuBar);

        fileChooser = new JFileChooser();

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openFile() {
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                textArea.read(br, null);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile() {
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                textArea.write(bw);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void encryptText(String algorithm) {
        try {
            // Generate keys
            if (algorithm.equals("DES")) {
                desKey = generateKey("DES");
            } else if (algorithm.equals("AES")) {
                aesKey = generateKey("AES");
            }

            // Encrypt text
            String originalText = textArea.getText();
            byte[] encryptedText = encryptData(originalText, (algorithm.equals("DES") ? desKey : aesKey), algorithm);
            textArea.setText(bytesToHex(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void decryptText(String algorithm) {
        try {
            // Decrypt text
            String encryptedHexText = textArea.getText();
            byte[] encryptedText = hexToBytes(encryptedHexText);
            String decryptedText = decryptData(encryptedText, (algorithm.equals("DES") ? desKey : aesKey), algorithm);
            textArea.setText(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private SecretKey generateKey(String algorithm) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        return keyGen.generateKey();
    }

    private byte[] encryptData(String data, Key key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    private String decryptData(byte[] encryptedData, Key key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
