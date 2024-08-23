package com.socketchannel.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NonBlockingServer {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 8080));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("server started in web...");
        while (true) {
            if (selector.select() == 0) {
                continue;
            }
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                if
                (key.isAcceptable()) {
                    ServerSocketChannel server =
                            (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel =
                            server.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("Accepted connection from: " +
                            clientChannel.getRemoteAddress());
                }
                if (key.isReadable()) {
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    int bytesRead =
                            clientChannel.read(buffer);
                    if (bytesRead == -1) {
                        clientChannel.close();
                        System.out.println("Connection closed by client.");
                    }
                                        else {
                                            buffer.flip();
                                            String message = new String(buffer.array()).trim();
                                            System.out.println("Received message" + message);
                                      // Send response back to client
                        buffer.clear();
                                buffer.put("Message received".getBytes());
                                buffer.flip();
                        clientChannel.write(buffer);
                    }
                }
            }
        }
    }
}
