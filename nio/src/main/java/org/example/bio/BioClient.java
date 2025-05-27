package org.example.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class BioClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread tom = new Thread(() -> {
            try {
                sendHello();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "tom");
        Thread jerry = new Thread(() -> {
            try {
                sendHello();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "jerry");
//        tom.run(); // 这样只有一个线程，普通的使用一个线程。
        tom.start();
        jerry.start();
        tom.join();
        jerry.join();
    }

    private static void sendHello() throws IOException {
        Socket socket = new Socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8080);
        socket.connect(inetSocketAddress);
        OutputStream outputStream = socket.getOutputStream();
        for (int i = 0; i < 10; i++) {
            outputStream.write(("hello" + i).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
        socket.close();
    }

    public static class User {
        public String name;
        public Integer age;
        public String friend;
    }
}
