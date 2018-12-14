package space.borisgk.javasimplechat.client;

import lombok.SneakyThrows;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
    protected Socket socket;
    private static final String HOST = "localhost";
    private static final int PORT = 8855;
    PrintWriter pw;
    Scanner scanner;

    @SneakyThrows
    public Client() {
    }

    @SneakyThrows
    public void start() {
        socket = new Socket(HOST, PORT);
        scanner = new Scanner(System.in);
        pw = new PrintWriter(socket.getOutputStream());
//        (new Thread(new Accepter(socket))).start();
        while (true) {
            send(scanner.next());
            Thread.sleep(100);
        }
    }

    public void run() {
        this.start();
    }

    public void send(String s) {
        pw.println(s);
        pw.flush();
    }

    public static void main(String[] args) {
        (new Thread(new Client())).start();
    }
}
