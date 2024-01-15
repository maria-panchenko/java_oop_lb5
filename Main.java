import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    private List<PrintWriter> clients = new ArrayList<>();

    public Main() {
        // создаем серверный сокет
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            while (true) {
                // ждем подключения нового клиента
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // создаем отдельный поток для чтения сообщений от клиента
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                clients.add(out);
                new Thread(() -> {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        while (true) {
                            String message = in.readLine();
                            if (message == null) break;
                            System.out.println("Received message: " + message);
                            for (PrintWriter client : clients) {
                                client.println(message);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        clients.remove(out);
                        System.out.println("Client disconnected");
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}