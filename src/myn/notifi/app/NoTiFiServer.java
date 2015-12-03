/*
 * Classname : NoTiFiServer
 *
 * Version information : 1.0
 *
 * Date : 11/10/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 * 
 * Assignment : program6
 */
package myn.notifi.app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import myn.addatude.app.AddATudeServer;
import myn.notifi.protocol.ConstVal;
import myn.notifi.protocol.NoTiFiACK;
import myn.notifi.protocol.NoTiFiDeregister;
import myn.notifi.protocol.NoTiFiError;
import myn.notifi.protocol.NoTiFiLocationAddition;
import myn.notifi.protocol.NoTiFiLocationDeletion;
import myn.notifi.protocol.NoTiFiMessage;
import myn.notifi.protocol.NoTiFiRegister;

public class NoTiFiServer extends Thread{
    /*set local constant values*/
    /*set timeout for server*/
    private final static int TIMEOUT = 5000000;
    /*65507 is maxpayload for UDP*/
    private static final int MAXPAYLOAD = 65507;
    private static final String UNEXPECTEDCODE = "Unexpected code";
    
    /*set constant value to handle different error messages*/
    private static final int MULTICAST = 1;
    private static final int REGISTERED = 2;
    private static final int INCORRECT = 3;
    private static final int UNKNOWNCLIENT = 4;
    private static final int UNEXPECTED = 5;
    private static final int UNKNOWNCODE=6;
    private static final int UNABLE=7;
    /*initial local variable*/
    /*store registered address*/
    private static Set<InetSocketAddress> socketAddress = new HashSet<>();
    private static Set<InetSocketAddress> regAddr = new HashSet<>();
  
    private DatagramSocket socket;
    private int msgId;
    private Inet4Address localIP;
    private int port;
    
    /**
     * Constructor for NoTiFiServer
     * 
     * @param socket - datagramsocket to set 
     * @throws SocketException - timeout exception
     */
    public NoTiFiServer(DatagramSocket socket) throws SocketException {
        this.socket=socket;
        socket.setSoTimeout(TIMEOUT);
    }
    
    
    /**
     * Override run function from Thread
     * */
    @Override
    public void run(){
        while(true) {
            byte [] receiveBuf = new byte[MAXPAYLOAD];
            DatagramPacket receivePkg = new DatagramPacket(receiveBuf,MAXPAYLOAD);
            try {
                socket.receive(receivePkg);
            } catch (IOException e) {
                AddATudeServer.writeToLogger(e.getMessage());
            }
            operation(receivePkg);
            
        }
    }
    
   
    /**
     * handle different operations decoded from receiving datagramPacket
     * 
     * @param receivePkg - receiving datagramPacket
     */
    public void operation(DatagramPacket receivePkg) {
        try {

            localIP = (Inet4Address) receivePkg.getAddress();
            port = receivePkg.getPort();
            byte [] realMsg = Arrays.copyOfRange(receivePkg.getData(),0,receivePkg.getLength());
            NoTiFiMessage msg = NoTiFiMessage.decode(realMsg);
            msgId = msg.getMsgId();
          
            int code = msg.getCode();
            if(code == ConstVal.add || code == ConstVal.del || code == ConstVal.error){
                sendError(UNEXPECTED,code);
                return;
            }
            // for register
            else if(code == ConstVal.reg) {
                //multicast::::
                NoTiFiRegister register = (NoTiFiRegister) msg;
                
                
                if(register.getAddress().isMulticastAddress()) {
                    sendError(MULTICAST,-1);
                    return;
                }
            
                //Already registered
                
                if(regAddr.contains(new InetSocketAddress(register.getAddress(),register.getPort()))) {
                    sendError(REGISTERED,code);
                }                        
                else if ( register.getPort() != port){
                    sendError (INCORRECT,code);
                }
                else {

                    socketAddress.add(new InetSocketAddress(receivePkg.getAddress(),register.getPort()));                   
                    regAddr.add(new InetSocketAddress(register.getAddress(),register.getPort()));
                    sendACK();    
                }
               
            }
            
            else if(code == ConstVal.deReg){
                NoTiFiDeregister deregister = (NoTiFiDeregister) msg;
                if(!regAddr.contains(new InetSocketAddress(deregister.getAddress(),deregister.getPort()))) {
                    sendError(UNKNOWNCLIENT,code);
                }
                else {

                    socketAddress.remove(new InetSocketAddress(receivePkg.getAddress(),deregister.getPort()));
                    regAddr.remove(new InetSocketAddress(deregister.getAddress(),deregister.getPort()));

                    sendACK();
                }
            }

            
        } catch (IllegalArgumentException | IOException e) {
            if(UNEXPECTEDCODE.equals(e.getMessage())) {
                try {
                    int code = NoTiFiMessage.decodeCode(receivePkg.getData());
                    sendError(UNKNOWNCODE,code);
                } catch (IOException e1) {
                    sendError(UNABLE,-1);
                }

            }
            sendError(UNABLE,-1);
        }
        
    }
    
    /**
     * sent error message
     * 
     * @param type - different types of error
     * @param code - code to be encoded to a package
     */
    public void sendError(int type,int code){
        String errorMsg=null;
        if(type == UNKNOWNCODE) {
            errorMsg = "Unknown code: "+code;
            msgId=0;
        }
        else if(type== UNABLE){
            errorMsg = "Unable to parse message";
            msgId=0;
        }
        else if(type == MULTICAST) {
            errorMsg="Bad address";
        }
        else if(type==UNEXPECTED) {
            errorMsg = "Unexpected code: "+code;
        }
        else if(type == REGISTERED) {
            errorMsg = "Already registered";
        }
        else if(type == INCORRECT) {
            errorMsg = "Incorrect port";
        }
        else if(type == UNKNOWNCLIENT) {
            errorMsg ="Unknown client";
        }
        NoTiFiError err = new NoTiFiError(msgId,errorMsg);
        try {
            sendPkg(err.encode());
        } catch (IOException e) {
        }
    }
    
    /**
     * 
     */
    public void sendACK() {
        NoTiFiACK ack = new NoTiFiACK(msgId);
        try {
            sendPkg(ack.encode());
        } catch (IOException e) {
        }
    }
    
    //this locationRecord is from addatude package
    /**
     * Specificlly for sending location to all registered clients
     *  will be called when AddATude cliend ask for adding a location
     *   
     * @param code - add or del a location 
     * @param lr - myn.addatude.protocol.LocationRecord lr be added/deleted
     */
    public void sendLoc(int code,myn.addatude.protocol.LocationRecord lr) {
        myn.notifi.protocol.LocationRecord notifiLR = 
                new myn.notifi.protocol.LocationRecord(lr.getUserId(),lr.getLongitude()
                        ,lr.getLatitude(),lr.getLocationName(),lr.getLocationDescription());
        NoTiFiMessage msg = null;
        if(code == ConstVal.add) {
            msg = new NoTiFiLocationAddition(msgId,notifiLR);
        }
        else if(code == ConstVal.del) {
            msg = new NoTiFiLocationDeletion(msgId,notifiLR);
        }
        byte[] b = null;
        try {
            b = msg.encode();
        } catch (IOException e1) {
            // can't do encoding.
            e1.printStackTrace();
        }
        Iterator<InetSocketAddress> it = socketAddress.iterator();
        while(it.hasNext()) {
            InetSocketAddress socAddress = it.next();
            Inet4Address add = (Inet4Address)socAddress.getAddress();
            int po = socAddress.getPort();
            DatagramPacket pkg = new DatagramPacket(b,b.length,add,po);
            try {
                socket.send(pkg);
            } catch (IOException e) {
                AddATudeServer.writeToLogger(e.getMessage());
            }
        }
    }
    
    /**
     * send package taking byte array
     * @param b - byte array to do encoding
     */
    public void sendPkg(byte [] b) {
        DatagramPacket s = new DatagramPacket(b,b.length,localIP,port);
        try {
            
            socket.send(s);
        } catch (IOException e) {
            AddATudeServer.writeToLogger(e.getMessage());
        }
    }
    
}
