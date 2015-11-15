/*
 * Classname : NoTiFiClient
 *
 * Version information : 1.0
 *
 * Date : 11/10/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 * 
 * Assignment : program5
 */
package myn.notifi.app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
//import java.nio.charset.StandardCharsets;

import myn.notifi.protocol.ConstVal;
import myn.notifi.protocol.NoTiFiMessage;
import myn.notifi.protocol.NoTiFiRegister;
import myn.notifi.protocol.NoTiFiDeregister;


public class NoTiFiClient {
    /*initialize local constant values*/
    /*Time out for ACK, 3 seconds*/
    private static final int TIMEOUT = 3000;   
    /*maxium payload for UDP protocol, for receiving message*/
    private static final int MAXPAYLOAD = 65507;
    private static final String UNEXPECTEDCODE = "Unexpected code";
    private static final String QUIT = "quit";
    //the client can change the port
    private static int localPort = 1682;
    /*declair local variables*/
    private DatagramSocket socket;  
    private int msgId;
    private Inet4Address localIP;
    private int port;
    private InetAddress host;
    private boolean share=false;
    /**
     * nested classes  Listener: 
     *     listen for quit from the keyboard
     * 
     * 
     * */
    private class Listener extends Thread {
        
        @Override
        public void run() {
            @SuppressWarnings("resource")
            Scanner keyboard = new Scanner(System.in);
            keyboard.useDelimiter("\r\n");
            while(true) {
                if(keyboard.next().equals(QUIT)){
                    share = true;
                    return;
                }
            }
        }
        
    }
    
    
    /**
     * constructer of NoTiFiClient class
     * 
     * @param host : host address
     * @param port : host port
     * @param local : local address
     */
    public NoTiFiClient(InetAddress host, int port, Inet4Address local)  {
        try {
            socket = new DatagramSocket(localPort, localIP);
                
            localIP = local;
            this.host=host;
            this.port=port;
                
            byte [] sendBuf = operationReg();        
            DatagramPacket sendPkg = new DatagramPacket(sendBuf,sendBuf.length,host,port);
                
            sendPackage(sendPkg,ConstVal.reg);  

            Listener thread = new Listener();
            thread.start();
              
            while(!share) {
                try{
                       receivePackage();
                }catch(SocketTimeoutException e) {
                    //do noting, then go to the loop again
                }     
            }
            //after we get "quit" from the keyboard, send deregPackage    
            sendDeregPackage();
         
        } catch (IOException e) {
            System.out.println("Unable to communicate: "+e.getMessage());
            System.exit(0);
        }
    }

    /**
     * set a random integer value to local port
     */
    public void setmsgID() {
        Random randomGenerator = new Random();
        msgId = randomGenerator.nextInt(255);
    }
    
    /**
     * Get an byte array when the constructor call Register
     *  
     * @return byte [] for further encoding need
     */
    public byte [] operationReg(){
        setmsgID();
        NoTiFiMessage reg = new NoTiFiRegister(msgId,localIP,localPort);
        byte [] result = null;
        try {
            result = reg.encode();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Get an byte array when the constructor call Register
     * Only differece from the register function is don't need
     * to set msgId. 
     *  
     * @return byte [] for further encoding need
     */
    public byte [] operationDereg(){
        NoTiFiMessage reg = new NoTiFiDeregister(msgId,localIP,localPort);
        byte [] result = null;
        try {
            result = reg.encode();
            
        } catch (IOException e) {
            System.out.println("Unable to parse");
        }
        return result;
    }
    

    /**
     * Send package to server
     *  basicly used by register ot deregister
     *  need to handle ACK
     *  
     * @param sendPkg - a DatagramPacket need to be sent
     * @param code - register or deRegister
     */
    public void sendPackage(DatagramPacket sendPkg,int code)  {
        ////////////////////////////////////////////////
        try {
            socket.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            
        }
        try {
            socket.send(sendPkg);
            
        } catch (IOException e) {
        }
        try {
            while(!ifACK()) {
                
            }
        } catch (IOException e) {
            if(e instanceof SocketTimeoutException){
                try {
                    socket.setSoTimeout(TIMEOUT);
                    socket.send(sendPkg);
                    while(!ifACK()) {
                        
                    }
                    return;
                } catch (IOException e1) {
                    if(e1 instanceof SocketTimeoutException) {
                        if(code == ConstVal.reg)
                            System.out.println("Unable to register");
                        else if(code == ConstVal.deReg)
                            System.out.println("Unable to deregister");
                        System.exit(0);
                    }
                }
            }
        }
    
    }
    
    /**
     * Check if the receiving package is an ACK or not
     * Basicly used by register or deregister
     * 
     * @return - true if receiving package is an ACK
     * @throws IOException - I/O error
     */
    public boolean ifACK() throws IOException {
        
        byte [] buf = new byte[MAXPAYLOAD];
        DatagramPacket rec = new DatagramPacket(buf,MAXPAYLOAD);
        
        socket.receive(rec);
        //System.out.println(new String(rec.getData(), StandardCharsets.UTF_8));

        //get real length of the receiving message
        byte [] realMsg = Arrays.copyOfRange(rec.getData(), 0, rec.getLength());
        return NoTiFiMessage.decode(realMsg).getMsgId()==msgId 
                &&NoTiFiMessage.decode(realMsg).getCode()==ConstVal.ACK ;
        
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
        } catch (IOException e1) {
            if(e1 instanceof SocketTimeoutException) {
                throw new SocketTimeoutException();
            }
        }
        
        //System.out.println(new String(receivePkg.getData(), StandardCharsets.UTF_8));
        
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
    /**
     * Send a deRegister message to server
     * will call "sendPackage" function by passing the datagrampacket
     * with specific host and port
     */
    public void sendDeregPackage() {
        byte [] sendBuf = operationDereg();        
        DatagramPacket sendPkg = new DatagramPacket(sendBuf,sendBuf.length,host,port);
        sendPackage(sendPkg,ConstVal.deReg);

    }
    public static void main(String[] args) throws UnknownHostException {
        if((args.length <3) || (args.length >=4)) {
            System.out.println("parameters: <Server> <Port> <Local IP>");
            System.exit(0);
        } 

        @SuppressWarnings("unused")
        NoTiFiClient client = new NoTiFiClient(InetAddress.getByName(args[0]),Integer.valueOf(args[1]),(Inet4Address) InetAddress.getByName(args[2])) ;   
        
    } 
}
