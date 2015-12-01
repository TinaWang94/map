package myn.addatude.app;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncServer {

    public static void main(String[] args) {
        int port = 8060;
        try {
            final AsynchronousServerSocketChannel server = 
                    AsynchronousServerSocketChannel.open().bind(
                            new InetSocketAddress(port));

            System.out.println("Server listening on " + port);

            server.accept("Client connection", 
                    new CompletionHandler<AsynchronousSocketChannel, Object>() {
                public void completed(AsynchronousSocketChannel ch, Object att) {
                    System.out.println("Accepted a connection");

                    // accept the next connection
                    server.accept("Client connection", this);

                    // handle this connection
                    //TODO handle(ch);
                }

                public void failed(Throwable exc, Object att) {
                    System.out.println("Failed to accept connection");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}