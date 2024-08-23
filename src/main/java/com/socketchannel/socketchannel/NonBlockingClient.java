package com.socketchannel.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NonBlockingClient {
    public static void main(String[] args) throws IOException {
        InetSocketAddress serverAddress = new
                InetSocketAddress("localhost", 8080);
        SocketChannel clientChannel =
                SocketChannel.open();
        clientChannel.configureBlocking(false);
        clientChannel.connect(serverAddress);

        while (!clientChannel.finishConnect()) {
            // Wait until connection is finished
        }
        System.out.println("Connected to the server...");

        ByteBuffer buffer = ByteBuffer.allocate(256);

        buffer.put("Hello fromclient".getBytes());
        buffer.flip();
        clientChannel.write(buffer);
        buffer.clear();
        int bytesRead = clientChannel.read(buffer);
        if
        (bytesRead > 0) {
            buffer.flip();
            String response = new String(buffer.array()).trim();
            System.out.println("Received response from server:" + response);
        }
        clientChannel.close();

        }
    }
