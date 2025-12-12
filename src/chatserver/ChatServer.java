package chatserver;

import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private final int port;
    private final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat Server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, this);
                clients.add(handler);

                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    public void broadcast(String msg, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.send(msg);
            }
        }
    }

    public void remove(ClientHandler handler) {
        clients.remove(handler);
    }

    public static void main(String[] args) {
        new ChatServer(5000).start();
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final ChatServer server;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket s, ChatServer server) {
        this.socket = s;
        this.server = server;

        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) {
            close();
        }
    }

    @Override
    public void run() {
        try {
            String name = in.readLine();
            server.broadcast("🔵 " + name + " joined the chat", this);

            String msg;
            while ((msg = in.readLine()) != null) {
                server.broadcast(name + ": " + msg, this);
            }

        } catch (IOException e) {
            //
        } finally {
            close();
        }
    }

    public void send(String msg) {
        out.println(msg);
    }

    public void close() {
        try { socket.close(); } catch (IOException ignored) {}
        server.remove(this);
    }
}
