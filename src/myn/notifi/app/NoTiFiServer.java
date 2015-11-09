package myn.notifi.app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import myn.notifi.protocol.ConstVal;
import myn.notifi.protocol.NoTiFiACK;
import myn.notifi.protocol.NoTiFiDeregister;
import myn.notifi.protocol.NoTiFiError;
import myn.notifi.protocol.NoTiFiMessage;
import myn.notifi.protocol.NoTiFiRegister;

public class NoTiFiServer extends Thread{
    private final static int TIMEOUT = 500000;
    private static final int MAXPAYLOAD = 65507;
    private static final String UNEXPECTEDCODE = "Unexpected code";
    
    private static boolean SHARE = false;
    
    private static final int MULTICAST = 1;
    private static final int REGISTERED = 2;
    private static final int INCORRECT = 3;
    private static final int UNKNOWNCLIENT = 4;
    private static final int UNEXPECTED = 5;
    private static final int UNKNOWNCODE=6;
    private static final int UNABLE=7;
    
    private static Map< Integer,Inet4Address> registeredAddr =  new HashMap<>();
    private static Map< Integer,Integer> registeredPort =  new HashMap<>();
    private DatagramSocket socket;
    private int msgId;
    private Inet4Address localIP;
    private int port;

    
    public NoTiFiServer(DatagramSocket socket) throws SocketException {
        this.socket=socket;
        socket.setSoTimeout(TIMEOUT);
               
        
    }
    
    
    @Override
    public void run(){
        while(true) {
            byte [] receiveBuf = new byte[8];
            DatagramPacket receivePkg = new DatagramPacket(receiveBuf,8);
            operation(receivePkg);
            
        }
    }
    
    
    
    public void operation(DatagramPacket receivePkg) {
        try {
            NoTiFiMessage msg = NoTiFiMessage.decode(receivePkg.getData());
            msgId = msg.getMsgId();
            
            localIP = (Inet4Address) receivePkg.getAddress();
            port = receivePkg.getPort();
            
            int code = msg.getCode();
            if(code == ConstVal.add || code == ConstVal.del || code == ConstVal.error){
                sendError(UNEXPECTED,code);
            }
            // for register
            else if(code == ConstVal.reg) {
                //multicast::::
                NoTiFiRegister register = (NoTiFiRegister) msg;
                
                //Already registered
                if(registeredAddr.get(msgId).equals(register.getAddress())
                        &&registeredPort.get(msgId)==register.getPort()) {
                    sendError(REGISTERED,code);
                }
                else if (!register.getAddress().equals((Inet4Address)receivePkg.getAddress()) 
                        || register.getPort() != receivePkg.getPort()){
                    sendError (INCORRECT,code);
                }
                else {
                    registeredAddr.put(msgId,register.getAddress());
                    registeredPort.put(msgId,register.getPort());
                    sendACK();    
                }
               
            }
            
            else if(code == ConstVal.deReg){
                NoTiFiDeregister deregister = (NoTiFiDeregister) msg;
                if(!registeredAddr.get(msgId).equals(deregister.getAddress()) 
                        ||registeredPort.get(msgId) != deregister.getPort() ) {
                    sendError(UNKNOWNCLIENT,code);
                }
                else {
                    registeredAddr.remove(msgId);
                    registeredPort.remove(msgId);
                    sendACK();
                }
            }

            
        } catch (IllegalArgumentException | IOException e) {
            if(e.getMessage().equals(UNEXPECTEDCODE)) {
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
        else if(type==UNEXPECTED) {
            errorMsg = "Unexpected code: "+code;
        }
        else if(type == REGISTERED) {
            errorMsg = "Already registered";
        }
        else if(type == INCORRECT) {
            errorMsg = "Incorrect address/port";
        }
        else if(type == UNKNOWNCLIENT) {
            errorMsg ="Unknown client";
        }
        NoTiFiError err = new NoTiFiError(msgId,errorMsg);
        try {
            sendPkg(err.encode());
        } catch (IOException e) {
            ////////////////////////////
        }
    }
    
    public void sendACK() {
        NoTiFiACK ack = new NoTiFiACK(msgId);
        try {
            sendPkg(ack.encode());
        } catch (IOException e) {
            //////////////////////////////
        }
    }
    
    public void sendLoc(int code) {
        if(code == ConstVal.add) {
            
        }
        else if(code == ConstVal.del) {
            
        }
    }
    
    public void sendPkg(byte [] b) {
        DatagramPacket s = new DatagramPacket(b,b.length,localIP,port);
        try {
            socket.send(s);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
