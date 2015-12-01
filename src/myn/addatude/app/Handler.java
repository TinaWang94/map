package myn.addatude.app;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.logging.Logger;

import myn.notifi.app.NoTiFiServer;

public class Handler implements CompletionHandler<AsynchronousSocketChannel,Logger>{
    private AsynchronousServerSocketChannel localServer;
    private static NoTiFiServer notifiServer;
    private final int CAP = 65535;
    public Handler(AsynchronousServerSocketChannel localServer,NoTiFiServer notifiServer){
        this.localServer = localServer;
        this.notifiServer = notifiServer;
    }
    
    public void completed(AsynchronousSocketChannel channel,Logger logger){
        localServer.accept(null,this);
        ByteBuffer dst = ByteBuffer.allocate(CAP);
        channel.read(dst, null, new BufferHandler(channel,dst,notifiServer));
    }

    public void failed(Throwable throwable, Logger logger) {
        
    }
}
