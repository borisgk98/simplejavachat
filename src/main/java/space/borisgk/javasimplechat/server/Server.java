package space.borisgk.javasimplechat.server;


import lombok.SneakyThrows;
import space.borisgk.javasimplechat.client.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Server implements Runnable{
    private static final Integer PORT = 8855;

    public Server() {
    }

    @SneakyThrows
    public void start() {
        // Сначала создаем ServerSocketChannel (!)
        ServerSocketChannel ssc = ServerSocketChannel.open();

        // Делаем его неблокирующим
        ssc.configureBlocking(false);
        // Получаем ServerSocket по каналу
        ServerSocket ss = ssc.socket();
        // Связываем сокет с адресом
        InetSocketAddress isa = new InetSocketAddress(Server.PORT);
        ss.bind(isa);
        // Create a new Selector for selecting
        Selector selector = Selector.open();
        // Регистрируем серверный канал на событие «прием подключения»
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // проверяем произошедшие события
            int num = selector.select();
            if (num == 0)
                continue;
            // Берем все сработавшие ключи
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if ((key.readyOps() & SelectionKey.OP_ACCEPT) != 0) {
                    // Пришло подключение. Делаем как обычно:
//                   Socket s = ss.accept();
                    // Для работы делаем channel
                    SocketChannel sc = null;
                    try {
                        sc = ss.accept().getChannel();
                    }
                    catch (IllegalBlockingModeException e) {
                        continue;
                    }
                    sc.configureBlocking(false);
                    // Регистрируем на чтение
                    sc.register(selector, SelectionKey.OP_READ);
                } else if ((key.readyOps() & SelectionKey.OP_READ) != 0) {
                    // Пришли данные. Узнаем, от кого и дальше как обычно:
                    SocketChannel sc = (SocketChannel) key.channel();
                    (new Thread(new ClientHandler(sc))).start();
                }
            }

        }
    }

    @Override
    public void run() {
        this.start();
    }

    public static void main(String[] args) {
        (new Thread(new Server())).start();
//        (new Thread(new Client())).start();
    }
}
