/*
 * Classname : NoTiFiMulticastClient
 *
 * Version information : 1.0
 *
 * Date : 11/10/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 * 
 * Assignment : program8
 */
package myn.notifi.app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

import myn.notifi.protocol.ConstVal;
import myn.notifi.protocol.NoTiFiMessage;

/**
 * NoTiFiClient by using multicast socket
 * Solve the register and deregister problem
 * @author Tong Wang
 * 
 * */
public class NoTiFiMulticastClient {
    private static final String QUIT = "quit";
    /*maxium payload for UDP protocol, for receiving message*/
    private static final int MAXPAYLOAD = 65507;
    private static final String UNEXPECTEDCODE = "Unexpected code";
    private static final int TIMEOUT = 5000;
    private MulticastSocket socket;
    private boolean share=false;
    /**
     * nested classes  Listener: 
     *     listen for quit from the keyboard
     * @author tong wang
     * 
     * */
    private class Listener extends Thread {
        
        @Override
        public void run(){
            @SuppressWarnings("resource")
            Scanner keyboard = new Scanner(System.in);
            keyboard.useDelimiter("\r\n");
            while(true) {
                if(keyboard.next().equals(QUIT)) {
                    share = true;
                    return;
                }
            }
        }
        
    }
    /**
     * constructer of NoTiFiMulticastClient class
     * 
     * @param host : host address
     * @param port : host port
     */
    public NoTiFiMulticastClient(InetAddress host, int port){
        try {
            socket = new MulticastSocket(port);
            socket.joinGroup(host);
            socket.setSoTimeout(TIMEOUT);
            
            Listener thread = new Listener();
            thread.start();
            while(!share){
                try{
                    receivePackage();
                 }catch(SocketTimeoutException e) {
                     //do noting, then go to the loop again
                     e.printStackTrace();
                 }  
            }
            socket.leaveGroup(host);
            
        } catch (IOException e) {
            System.out.println("Unable to communicate: "+e.getMessage());
            System.exit(0);
        }

    }
    
    /**
     * Handle receiving package - 
     * typically for error and location addition/deletion message
     * will call "dealcode" function based on different type of message
     * 
     * @throws SocketTimeoutException - timeout
     */
    public void receivePackage() throws SocketTimeoutException {
        
        byte [] receiveBuf = new byte[MAXPAYLOAD];
        DatagramPacket receivePkg = new DatagramPacket(receiveBuf,receiveBuf.length);
        
        try {
            socket.receive(receivePkg);
            System.out.println("received string:" + new String(receiveBuf, 0, receiveBuf.length));
        } catch (IOException e1) {
            if(e1 instanceof SocketTimeoutException) {
                throw new SocketTimeoutException();
            }else{
                e1.printStackTrace();
            }
        }
        System.out.println("here");
        try {
            byte [] realMsg = Arrays.copyOfRange(receivePkg.getData(), 0, receivePkg.getLength());
            NoTiFiMessage msg = NoTiFiMessage.decode(realMsg);
            int code = msg.getCode();
            dealCode(msg,code);
            
        } catch (IllegalArgumentException | IOException e) {
            if(e.getMessage().equals(UNEXPECTEDCODE)) {
                System.out.println(e.getMessage());
            }
            System.out.println("Unable to parse message");
        }
        
    }
    /**
     * Printout human readable result from receicing message
     * 
     * @param msg - notifi message after decoding
     * @param code - type of notifi message 
     */
    public void dealCode(NoTiFiMessage msg, int code){
        if(code == ConstVal.error){
            System.out.println(msg.toString()); 
            return;
        }        
        else if(code == ConstVal.add || code == ConstVal.del){
            System.out.println(msg.toString());      
        }       
        else{
            System.out.println("Unexpected message type");
        }         
    }
    
    public static void main(String[] args) throws UnknownHostException {

        if((args.length <2) || (args.length >=3)) {
            System.out.println("parameters: <MulticastAddress> <Port> ");
            System.exit(0);
        } 
        InetAddress destAddress=InetAddress.getByName(args[0]);
        if(!destAddress.isMulticastAddress()) {
            throw new IllegalArgumentException("Not a multicast address");
        }
                 
        @SuppressWarnings("unused")
        NoTiFiMulticastClient client = new NoTiFiMulticastClient(destAddress,Integer.valueOf(args[1]));
    } 
}
