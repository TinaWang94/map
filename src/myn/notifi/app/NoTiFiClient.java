package myn.notifi.app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

import myn.notifi.protocol.ConstVal;
import myn.notifi.protocol.NoTiFiACK;
import myn.notifi.protocol.NoTiFiMessage;
import myn.notifi.protocol.NoTiFiRegister;
import myn.notifi.protocol.NoTiFiDeregister;


public class NoTiFiClient {
    private static final int TIMEOUT = 10000;
    private static final int MAXPAYLOAD = 65507;
    private static final String UNEXPECTEDCODE = "Unexpected code";
    ////////////////////////////////////////////////
    //testing port
    private static int localPort = 1682;
    
    private DatagramSocket socket;  
    private short msgId;
    private Inet4Address localIP;
    private int port;
    private InetAddress host;
    private boolean share=false;
    
    private class Listener extends Thread {
        
        @Override
        public void run() {
            @SuppressWarnings("resource")
            Scanner keyboard = new Scanner(System.in);
            keyboard.useDelimiter("\r\n");
            while(keyboard.next().equals("quit")) {
                share = true;
                sendDeregPackge();
                return;
            }
        }
        public void sendDeregPackge() {
            byte [] sendBuf = operationDereg();        
            DatagramPacket sendPkg = new DatagramPacket(sendBuf,sendBuf.length,host,port);
            sendPackage(sendPkg,ConstVal.deReg);
            System.exit(0);
        }
    }
    
    
    
    public NoTiFiClient(InetAddress host, int port, Inet4Address local)  {
        try {
            socket = new DatagramSocket();
                
                ////////////////////////////////////////////
                //check
                localIP = local;
                this.host=host;
                this.port=port;
                
                byte [] sendBuf = operationReg();        
                DatagramPacket sendPkg = new DatagramPacket(sendBuf,sendBuf.length,host,port);
                
                sendPackage(sendPkg,ConstVal.reg);                

                Listener thread = new Listener();
                thread.start();
                while(!share) {
                    receivePackage();
                }
                
        
            
        } catch (IOException e) {
            System.out.println("Unable to communicate: "+e.getMessage());
            System.exit(0);
        }

    }
    public void setLocalAddress(InetAddress IP) {
        ///////////////////////////////////////check
        localIP = (Inet4Address) IP;
    }
    public void setmsgID() {
        Random randomGenerator = new Random();
        msgId = (short) randomGenerator.nextInt(255);
    }
    
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
    
    public byte [] operationDereg(){
        NoTiFiMessage reg = new NoTiFiDeregister(msgId,localIP,localPort);
        byte [] result = null;
        try {
            result = reg.encode();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    //////////////////////////////////////////////////
    //exception
    public void sendPackage(DatagramPacket sendPkg,int code)  {
        ////////////////////////////////////////////////
       
        try {
            socket.setSoTimeout(TIMEOUT);
            
            try {
                socket.send(sendPkg);
                byte [] buf = new byte[MAXPAYLOAD];
                DatagramPacket rec = new DatagramPacket(buf,MAXPAYLOAD);
                socket.receive(rec);
             
                System.out.println(new String(rec.getData(), StandardCharsets.UTF_8));
                System.exit(0);
                
                while(!ifACK()) {
                    
                }
                
                return;               
            } catch (IOException e) {
                ////////////////////////////////////
                //
            }
        } catch (SocketException e) {
            
            ////////////////////////////
            //time out resent the package
            try {
                socket.setSoTimeout(TIMEOUT);
                try {
                    socket.send(sendPkg);
                    
                    while(!ifACK()) {
                        
                    }
                    
                    return;
                } catch (IOException e1) {

                }
            } catch (SocketException e1) {
                if(code == ConstVal.reg)
                    System.out.println("Unable to register");
                if(code == ConstVal.deReg)
                    System.out.println("Unable to deregister");
                System.exit(0);
            }
        }
    }
    
    
    
    public boolean ifACK() {
        
        byte [] buf = new byte[MAXPAYLOAD];
        DatagramPacket rec = new DatagramPacket(buf,MAXPAYLOAD);
        try {
            socket.receive(rec);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            
            NoTiFiMessage  msg = new NoTiFiACK(msgId);
            return msg.equals(NoTiFiMessage.decode(rec.getData()));
        } catch (IOException e) {
            return false;
        }
        
    }
    
    public void receivePackage() {
        byte [] receiveBuf = new byte[MAXPAYLOAD];
        DatagramPacket receivePkg = new DatagramPacket(receiveBuf,receiveBuf.length);
        
        
        //System.out.println(new String(receivePkg.getData(), StandardCharsets.UTF_8));
        
        try {
            NoTiFiMessage msg = NoTiFiMessage.decode(receivePkg.getData());
            int code = msg.getCode();
            
            dealCode(msg,code);
            
        } catch (IllegalArgumentException | IOException e) {
            if(e.getMessage().equals(UNEXPECTEDCODE)) {
                System.out.println(e.getMessage());
            }
            System.out.println("Unable to parse message");
        }
        
    }
    public void dealCode(NoTiFiMessage msg, int code){
        if(code == ConstVal.error){
            System.out.println(msg.toString()); 
        }
        System.out.print("location");
        if(code == ConstVal.add){
            System.out.print("Addition: ");
        }
        else if(code == ConstVal.del){
            System.out.print("Deletion: ");
        }
        else{
            System.out.println("Unexpected message type");
        }
        System.out.println(msg.toString());
            
    }
    public static void main(String[] args) throws UnknownHostException {
        if((args.length <3) || (args.length >4)) {
            System.out.println("parameters: <Server> [<Port>] <Local IP>");
            System.exit(0);
        } 
        System.out.println( Integer.valueOf(args[1]));
        NoTiFiClient client = new NoTiFiClient(InetAddress.getByName(args[0]),Integer.valueOf(args[1]),(Inet4Address) InetAddress.getByName(args[2])) ;   
        
    } 
}
