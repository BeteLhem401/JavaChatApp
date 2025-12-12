package chatclient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ChatClient {

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JTextField nameField;
    private JTextField hostField;
    private JTextField portField;
    private JButton connectBtn;
    private JButton sendBtn;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread readerThread;

    public ChatClient() {
        buildUI();
    }

    private void buildUI() {
        frame = new JFrame("Modern Java Chat Client");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ====== TOP PANEL (Header Style) ======
        JPanel top = new JPanel(new GridLayout(3, 2, 10, 10));
        top.setBorder(new EmptyBorder(15, 15, 15, 15));
        top.setBackground(new Color(235, 235, 245));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel nameLabel = new JLabel("Your Name:");
        nameLabel.setFont(labelFont);
        nameField = new JTextField("User");

        JLabel hostLabel = new JLabel("Server Host:");
        hostLabel.setFont(labelFont);
        hostField = new JTextField("localhost");

        JLabel portLabel = new JLabel("Server Port:");
        portLabel.setFont(labelFont);
        portField = new JTextField("5000");

        top.add(nameLabel);
        top.add(nameField);
        top.add(hostLabel);
        top.add(hostField);
        top.add(portLabel);
        top.add(portField);

        frame.add(top, BorderLayout.NORTH);

        // ====== CHAT AREA ======
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        chatArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatArea.setBackground(new Color(250, 250, 250));

        JScrollPane scroll = new JScrollPane(chatArea);
        scroll.setBorder(null);
        frame.add(scroll, BorderLayout.CENTER);

        // ====== BOTTOM PANEL ======
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottom.setBackground(new Color(245, 245, 245));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        sendBtn = new JButton("Send");
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendBtn.setBackground(new Color(66, 133, 244));
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFocusPainted(false);
        sendBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(sendBtn, BorderLayout.EAST);

        // ====== CONNECT BUTTON ON LEFT ======
        connectBtn = new JButton("Connect");
        connectBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        connectBtn.setBackground(new Color(52, 168, 83));
        connectBtn.setForeground(Color.WHITE);
        connectBtn.setFocusPainted(false);
        connectBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        frame.add(connectBtn, BorderLayout.WEST);
        frame.add(bottom, BorderLayout.SOUTH);

        // ===== EVENT LISTENERS =====
        connectBtn.addActionListener(e -> connect());
        sendBtn.addActionListener(e -> send());
        inputField.addActionListener(e -> send());

        frame.setVisible(true);
    }

    private void connect() {
        try {
            String host = hostField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());
            String name = nameField.getText().trim();

            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(name);
            chatArea.append("[Connected to server]\n");

            readerThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        chatArea.append(msg + "\n");
                    }
                } catch (IOException ignored) {}
            });

            readerThread.start();

        } catch (Exception e) {
            chatArea.append("[ERROR] Could not connect to server.\n");
        }
    }

    private void send() {
        String msg = inputField.getText().trim();
        if (msg.isEmpty()) return;

        out.println(msg);
        inputField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClient::new);
    }
}
