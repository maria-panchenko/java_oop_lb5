import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client extends JFrame {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JTextField textField;
    private JTextArea textArea;

    public Client() {
        // создаем окно
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // добавляем текстовое поле для ввода сообщений и текстовую область для вывода сообщений
        textField = new JTextField();
        add(textField, "South");
        textArea = new JTextArea();
        add(new JScrollPane(textArea), "Center");

        // устанавливаем соединение с сервером
        try {
            socket = new Socket("localhost", 1234);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // запускаем поток для чтения сообщений от сервера
        new Thread(() -> {
            try {
                while (true) {
                    String message = in.readLine();
                    textArea.append(message + "\\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // добавляем обработчик события для отправки сообщения на сервер при нажатии Enter
        textField.addActionListener(e -> {
            out.println(textField.getText());
            textField.setText("");
        });
    }

    public static void main(String[] args) {
        new Client();
    }
}