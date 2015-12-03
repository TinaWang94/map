/*
 * Classname : Handler
 *
 * Version information : 1.0
 *
 * Date : 11/19/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 * 
 * Assignment : program7
 */
package myn.addatude.app;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.logging.Logger;

import myn.notifi.app.NoTiFiServer;

/**
 * call back function to handle unblocking stream
 * 
 * @author Tong Wang
 * */
public class Handler implements CompletionHandler<AsynchronousSocketChannel,Logger>{
    private AsynchronousServerSocketChannel localServer;
    private static NoTiFiServer notifiServer;
    private final int CAP = 65535;
    
    /**
     * constructor for handler class
     * @param localServer - an AsynchronousServerSocketChannel
     * @param notifiServer - notifiServer to handle notifi message
     */
    @SuppressWarnings("static-access")
    public Handler(AsynchronousServerSocketChannel localServer,NoTiFiServer notifiServer){
        this.localServer = localServer;
        this.notifiServer = notifiServer;
    }
    @Override
    public void completed(AsynchronousSocketChannel channel,Logger logger){
        localServer.accept(null,this);
        ByteBuffer dst = ByteBuffer.allocate(CAP);
        channel.read(dst, null, new BufferHandler(channel,dst,notifiServer));
    }
    @Override
    public void failed(Throwable throwable, Logger logger) {
        logger.log(Level.WARNING, "read failed", throwable);
    }
}
