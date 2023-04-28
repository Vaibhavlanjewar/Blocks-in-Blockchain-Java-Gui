import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class EncryptionGUI extends JFrame {
    private JPanel panel;
    private JLabel messageLabel;
    private JTextField messageField;
    private JLabel keyLabel;
    private JTextField keyField;
    private JButton encryptButton;
    private JButton decryptButton;

    public EncryptionGUI() {
        this.setTitle("AES Encryption/Decryption");
        this.setSize(400, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        messageLabel = new JLabel("Enter Message:");
        messageField = new JTextField();
        keyLabel = new JLabel("Enter Key:");
        keyField = new JTextField();
        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");

        panel.add(messageLabel);
        panel.add(messageField);
        panel.add(keyLabel);
        panel.add(keyField);
        panel.add(encryptButton);
        panel.add(decryptButton);

        this.add(panel);

        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String message = messageField.getText();
                    String key = keyField.getText();

                    byte[] encryptedMessage = encrypt(message, key);

                    JFileChooser fileChooser = new JFileChooser();
                    int saveDialogResult = fileChooser.showSaveDialog(panel);

                    if (saveDialogResult == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();

                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(encryptedMessage);
                        fos.close();

                        JOptionPane.showMessageDialog(panel, "Message successfully encrypted and saved to file.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error encrypting message: " + ex.getMessage());
                }
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    int openDialogResult = fileChooser.showOpenDialog(panel);

                    if (openDialogResult == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();

                        FileInputStream fis = new FileInputStream(file);
                        byte[] encryptedMessage = new byte[(int) file.length()];
                        fis.read(encryptedMessage);
                        fis.close();

                        String key = keyField.getText();
                        String decryptedMessage = decrypt(encryptedMessage, key);

                        JOptionPane.showMessageDialog(panel, "Message successfully decrypted: " + decryptedMessage);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error decrypting message: " + ex.getMessage());
                }
            }
        });
    }

    public static byte[] encrypt(String message, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        return cipher.doFinal(message.getBytes());
    }

    public static String decrypt(byte[] encryptedMessage, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedMessage);

        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        EncryptionGUI gui = new EncryptionGUI();
        gui.setVisible(true);
    }
}
