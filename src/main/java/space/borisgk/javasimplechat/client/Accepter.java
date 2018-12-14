package space.borisgk.javasimplechat.client;

import lombok.SneakyThrows;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Accepter implements Runnable {
    protected Scanner scanner;
    protected PrintWriter pw;

    @SneakyThrows
    public Accepter(Socket socket) {
        scanner = new Scanner(socket.getInputStream());
        pw = new PrintWriter(System.out);
    }

    @SneakyThrows
    public void run() {
        while (true) {
            pw.print(scanner.next());
            pw.flush();
            Thread.sleep(100);
        }
    }
}
