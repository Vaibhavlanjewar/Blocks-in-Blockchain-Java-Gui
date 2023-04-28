import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BlockchainGUI implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JLabel dataLabel;
    private JTextField dataField;
    private JButton addButton;
    private JTextArea blockchainArea;

    private ArrayList<Block> blockchain;

    public BlockchainGUI() {
        // Create the frame and panel
        frame = new JFrame("Blockchain");
        panel = new JPanel(new GridLayout(0, 1));
        frame.setSize(1280,720);
         frame.setContentPane(panel);

        // Create the components
        dataLabel = new JLabel("Data:");
        dataField = new JTextField();
        addButton = new JButton("Add Block");
        blockchainArea = new JTextArea();
        blockchainArea.setEditable(false);
        blockchainArea.setLineWrap(true);
        blockchainArea.setWrapStyleWord(true);

        // Add the components to the panel
        panel.add(dataLabel);
        panel.add(dataField);
        panel.add(addButton);
        panel.add(blockchainArea);

        // Add action listener to the "Add Block" button
        addButton.addActionListener(this);

        // Initialize the blockchain with the genesis block
        blockchain = new ArrayList<>();
        String data = "Genesis block";
        String previousHash = "0";
        String hash = calculateHash(data + previousHash);
        Block genesisBlock = new Block(data, previousHash, hash);
        blockchain.add(genesisBlock);

        // Set the frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String data = dataField.getText();

            // Create a new block
            String previousHash = blockchain.get(blockchain.size() - 1).getHash();
            String hash = calculateHash(data + previousHash);
            Block block = new Block(data, previousHash, hash);
            blockchain.add(block);

            // Update the blockchain display
            blockchainArea.append(block.toString() + "\n");
        }
    }

    private static String calculateHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        new BlockchainGUI();
    }
}

class Block {
    private String data;
    private String previousHash;
    private String hash;

    public Block(String data, String previousHash, String hash) {
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
    }

    public String getData() {
        return data;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "Block{   " +
                "data='" + data + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
