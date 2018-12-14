package space.borisgk.javasimplechat.server;

import lombok.SneakyThrows;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientHandler implements Runnable {

    protected SocketChannel sc;
    protected ByteBuffer buf;

    public ClientHandler(SocketChannel sc) {
        this.sc = sc;
        buf = ByteBuffer.allocate(1024 * 1024);
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            while (sc.read(buf) > 0) {
                buf.flip();
                while(buf.hasRemaining()){
                    System.out.print((char) buf.get());
                }
                buf.clear();
            }
        }
    }
}
