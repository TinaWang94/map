package myn.addatude.app;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mapservice.Location;
import myn.addatude.protocol.AddATudeError;
import myn.addatude.protocol.AddATudeException;
import myn.addatude.protocol.AddATudeLocationResponse;
import myn.addatude.protocol.AddATudeMessage;
import myn.addatude.protocol.AddATudeNewLocation;
import myn.addatude.protocol.ConstantVariable;
import myn.addatude.protocol.LocationRecord;
import myn.addatude.protocol.MessageInput;
import myn.addatude.protocol.MessageOutput;
import myn.notifi.app.NoTiFiServer;
import myn.notifi.protocol.ConstVal;

public class BufferHandler implements CompletionHandler<Integer,Logger>{

    private static final byte DELIMITER = 13;
    private static final byte DELIMITER2 = 10;
    public static final int MAPID=345;
    private final int CAP = 65535;
    private final static int dividor = 256;
    private final static String del = ":";
    ///////////////////////////////////////
    //private final static int TIMEOUT = 500000;
    public static final String MAPNAME="Class Map";
    public static final String InvalidHeader = "Invalid header.";
    private static NoTiFiServer notifiServer;

    private static Map<Integer,LocationRecord> locationList  = new HashMap<>();
    
    
    private static AsynchronousSocketChannel server;
    private ByteBuffer byteBuffer;
    
    public AddATudeMessage deframer(ByteBuffer readBuffer) throws AddATudeException, IOException{
        readBuffer.mark();
        StringBuffer aBuf = new StringBuffer();
        byte b;
        while(readBuffer.hasRemaining()){
            b=readBuffer.get();
            aBuf.append(b);
            if(b==DELIMITER){
                b=readBuffer.get();
                aBuf.append(b);
                if(b==DELIMITER2){
                    return AddATudeMessage.decode(new MessageInput(aBuf.toString().getBytes()));
                }
            }
        }
        readBuffer.reset();
        //readBuffer.compact();
        return null;
    }
    /**
     * Write message out to logger file
     * 
     * @param msg - the message to write out
     * @throws IOException 
     */
    public static void writeToLogger(String msg) {
        InetSocketAddress add = null;
        try {
            add = (InetSocketAddress)(server.getRemoteAddress());
        } catch (IOException e) {
        }
        AddATudeServerAIO.LOGGER.log(Level.ALL, add.getAddress()+" "+
                add.getPort()+" "+msg+ConstantVariable.EOLN);
    }
    

    public BufferHandler(AsynchronousSocketChannel server,ByteBuffer byteBuffer, NoTiFiServer notifiServer) {
        this.server = server;
        this.byteBuffer=byteBuffer;
        this.notifiServer=notifiServer;
    }
    
    
    
    /**
     * Handle New operation: rename the username; add new locationRecord to locationList map;
     * write location record list to outputStream. Add new location to Google map.
     * 
     * @param message - AddATudeLocation message received from client
     */
    private void operationNew(AddATudeNewLocation message) {
        /*set mapname*/
        String mapName=MAPNAME;
        /*get new locationRecord*/
        LocationRecord lr = message.getLocationRecord();
        /*receive unknown user ID*/
        if(!AddATudeServerAIO.password.containsKey(lr.getUserId()%dividor)) {
            String msg="No such user: "+lr.getUserId();
            try {
                writeToByteBuffer(new AddATudeError(message.getMapId(),msg));
            } catch (AddATudeException e) {
            }
            return; 
        }
        /*get user name from password map and re-name the username*/
        String userName = AddATudeServerAIO.password.get(lr.getUserId()%dividor);
        int pos = userName.indexOf(del);
        userName=userName.substring(0,pos);
        String newLocationName = userName + ": "+lr.getLocationName();
        
        try {
            LocationRecord lr2 = new LocationRecord(lr.getUserId(),lr.getLongitude(),lr.getLatitude()
                    ,newLocationName,lr.getLocationDescription());
            
            if(locationList.containsKey(lr.getUserId()%dividor)){
                //location deletion
                notifiServer.sendLoc(ConstVal.del, lr2);
            }
            
            locationList.put(lr.getUserId()%dividor, lr2);
            //location addition
            notifiServer.sendLoc(ConstVal.add, lr2);
            
            /*make a new response message*/
            AddATudeLocationResponse response=new AddATudeLocationResponse(message.getMapId(),mapName);
            for(LocationRecord i : locationList.values()) {
                response.addLocationRecord(i);
            }
            //write out yo outputStream
            writeToByteBuffer(response);
            //add new location to google map
            AddATudeServerAIO.mgr.addLocation(new Location(lr.getLocationName(), lr.getLongitude(), lr.getLatitude(), 
                    lr.getLocationDescription(), Location.Color.BLUE));
            //write out to logger
            String temp = lr.getLocationName()+"-"+lr.getLocationDescription()+
                    "at"+lr.getLongitude()+","+lr.getLatitude();
            /////////////////////
            writeToLogger(temp);
            
        } catch (AddATudeException e) {
            String msg=e.getLocalizedMessage();
            try {
                writeToByteBuffer(new AddATudeError(0,msg));
            } catch (AddATudeException e1) {
            }
            return;
        }
    }
    
    /**
     * Handle ALL operation: write a latest locationRecord list out to outputStream 
     * 
     * @param message - AddATude message received from user
     */
    private void operationAll(AddATudeMessage message) {
        /*set map name*/
        String mapName=MAPNAME;
        try {
            /*make a new response message*/
            AddATudeLocationResponse response=new AddATudeLocationResponse(message.getMapId(),mapName);
            for(LocationRecord i : locationList.values()) {
                response.addLocationRecord(i);
            }
            //write out to output Stream 
            writeToByteBuffer(response);
            
        } catch (AddATudeException e) {
            
            String msg=e.getMessage();
            try {
                writeToByteBuffer(new AddATudeError(0,msg));
            } catch (AddATudeException e1) {
            }
            return;
        }
    }
    
  
    
    public void writeToByteBuffer(AddATudeMessage msg){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            msg.encode(new MessageOutput(out));
            server.write(ByteBuffer.wrap(out.toByteArray()));
        } catch (AddATudeException e) {
            try {
                AddATudeError err = new AddATudeError(0,e.getMessage());
                err.encode(new MessageOutput(out));
                server.write(ByteBuffer.wrap(out.toByteArray()));
            } catch (AddATudeException e1) {
                e1.printStackTrace();
            }
        }
        
    }
    
    @Override
    public void failed(Throwable exc, Logger logger) {

        
    }

    @Override
    public void completed(Integer readByte, Logger logger) {
        if(readByte == -1) {
            ////////////////////////////
        }
        else{
            AddATudeMessage message = null;
            try {
                message = deframer(byteBuffer);
                if(message == null){
                    
                    server.read(byteBuffer, null, new BufferHandler(server,byteBuffer,notifiServer));
                    return;
                }
                System.out.println(message.toString());
            } catch (AddATudeException | IOException e) {
                if(e.getMessage().equals(InvalidHeader)) {
                    /*receive unexpected version*/
                    @SuppressWarnings("null")
                    String msg="Unexpected version: "+message.getVersion();
                    try {
                        writeToByteBuffer(new AddATudeError(message.getMapId(),msg));
                    } catch (AddATudeException e1) {
                    }
                    ///////////////////////////////////////////
                    writeToLogger(msg);
                    //read again or return??????????????????????????
                    ByteBuffer dst = ByteBuffer.allocate(CAP);
                    server.read(dst, null, new BufferHandler(server,dst,notifiServer));
                    return;
                }
                else {
                    try {
                        writeToByteBuffer(new AddATudeError(0,e.getMessage()));
                        /////////////////////////////////////
                        //write to logger
                        writeToLogger(e.getMessage());
                        //read again or return??????????????
                    } catch (AddATudeException e1) {
                    }
                }
                
            }
            String operation = message.getOperation();
            if(message.getMapId() != MAPID) {
                String msg="No such map: "+message.getMapId();
                try {
                    writeToByteBuffer(new AddATudeError(message.getMapId(),msg));
                    /////////////////////////////////////
                    //write to logger
                    writeToLogger(msg);
                    //read again or return??????????????
                } catch (AddATudeException e) {
                }
            }
            switch (operation){
            case ConstantVariable.NEW:
                operationNew((AddATudeNewLocation)message);
                break;
            /*handle ALL operation*/
            case ConstantVariable.ALL:
                operationAll(message);
                break;
            /*receive other AddATudeMessage*/
            case ConstantVariable.RESPONSE:
            case ConstantVariable.ERROR:
                String msg="Unexpected message type: "+operation;                       
                try {
                    writeToByteBuffer(new AddATudeError(message.getMapId(),msg));
                } catch (AddATudeException e) {
                }
                ////////////////////////////////////
                writeToLogger(msg);
                break;
            /*receive unknown message*/
            default:
                msg="Unknown operation: "+operation;
                try {
                    writeToByteBuffer(new AddATudeError(message.getMapId(),msg));
                } catch (AddATudeException e) {
                }
                ////////////////////////////////////////////
                writeToLogger(msg);
                break;
            }
        }
        
    }
    
}
