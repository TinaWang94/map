/*
 * Classname : AddATudeServer
 *
 * Version information : 1.0
 *
 * Date : 9/23/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 * 
 * Assignment : program3
 */
package myn.addatude.app;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import mapservice.GoogleMapMaker;
import mapservice.Location;
import mapservice.MapManager;
import mapservice.MemoryMapManager;
import myn.addatude.protocol.AddATudeException;
import myn.addatude.protocol.AddATudeLocationResponse;
import myn.addatude.protocol.AddATudeMessage;
import myn.addatude.protocol.AddATudeNewLocation;
import myn.addatude.protocol.ConstantVariable;
import myn.addatude.protocol.LocationRecord;
import myn.addatude.protocol.MessageInput;
import myn.addatude.protocol.MessageOutput;

public class AddATudeServer extends Thread {

    /*Set useful constant value*/
    private final static int dividor = 255;
    private final static String del = ":";
    private final static int TIMEOUT = 500000;
    
    /*name of the location file for google map*/
    
    // path may have to change to appropriate one
    private static final String LOCATIONFILE = "markers.js";
    /*name/version of the message*/
    public static final String InvalidHeader = "Invalid header.";
    /*name of the logger file*/
    private static String LOGFILE = "connections.log";
    /*map name*/
    public static final String MAPNAME="Class Map";
    /*map id*/
    public static final int MAPID=345;
    /*initialize a logger*/
    private static final Logger LOGGER = Logger.getLogger(AddATudeServer.class.getName());
    /*file header for logger*/
    private static FileHandler fh;
    /*initialize variables: socket, MessageInput, MessageOtput, locationRecord map and password map*/
    private Socket socket;
    private MessageInput in;
    private MessageOutput out;
    
    private static Map<Integer,String> password = new HashMap<>();
    //store location record
    private static Map<Integer,LocationRecord> locationList  = new HashMap<>();
    /*initialize MapManager for google map*/
    private MapManager mgr = new MemoryMapManager();
    
    
    /**
     * write out to the outputStream
     * 
     * @param msg -  the response/error message
     * @param Id - mapId
     * @param count - count for total message characters
     */
    private void output (String msg, int Id,int count) {
        try {
            out.write(ConstantVariable.HEADER+" "+Id+" "+
                    ConstantVariable.ERROR+" "+count+" "+msg+ConstantVariable.EOLN);
        } catch (IOException e) {

        }
    }
    
    /**
     * Write message out to logger file
     * 
     * @param msg - the message to write out
     */
    private void writeToLogger(String msg){
        LOGGER.log(Level.ALL, socket.getInetAddress().getHostAddress()+" "+
                socket.getPort()+" "+msg+ConstantVariable.EOLN);
    }
    /**
     * static function: Initialize the logger with handler,formatter
     * 
     * */
    static {
       
        try {
            fh=new FileHandler(LOGFILE,true);
            fh.setEncoding(ConstantVariable.UNI);
            fh.setFormatter(new Formatter() {
                
                @Override
                public String format(LogRecord record) {
                    return record.getMessage();
                }
                
            });
            
                    
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

    }
    
    
    /**
     * read userId, username and password from the given file and
     * store those information to password map. UserId is the key and 
     * name+password is the value
     *  
     * @param filename - the file to read from
     * 
     */
    private static void readPassword(String filename) {
        File file = new File(filename);
        if(file.length() == 0) {
            System.out.println("Empty password file");
        }
        try(BufferedReader br = new BufferedReader(new InputStreamReader
                (new FileInputStream(filename),ConstantVariable.UNI))) {
            String line = br.readLine();
            while(line != null) {
                int pos = line.indexOf(del);
                int key = Integer.valueOf(line.substring(0, pos));
                key=key % dividor;
                //value is username + password
                String value = line.substring(pos+1, line.length());
                password.put(key,value);
                line = br.readLine();
            }
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        
    }
    
    
    /**
     * Constructor. Initialize socket, google map, logger
     * and message input/output
     * 
     * @param socket - socket to connect
     * @throws SocketException - timeout
     */
    public AddATudeServer(Socket socket) throws SocketException {
        this.socket=socket;
        socket.setSoTimeout(TIMEOUT);
        mgr.register(new GoogleMapMaker(LOCATIONFILE, mgr));
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(fh);
        try {
            
            out=new MessageOutput(socket.getOutputStream());
            in=new MessageInput(socket.getInputStream());
        } catch (IOException e) {
            writeToLogger(e.getMessage());
        }
        
    }
    
    /**
     * reactionLoop for this class. Override from thread class.
     * hander different operations which from the result of decode
     * the messageInput 
     * 
     * */
    @Override
    public void run() {
        System.out.println("Handling client "+socket.getInetAddress().getHostAddress()+
                "-"+socket.getPort()+" with thread id "+Thread.currentThread().getId());
       
        while(true) {
                
            AddATudeMessage message = null;
            try {
                message = AddATudeMessage.decode(in);
                
            } catch (EOFException | AddATudeException e1) {
                if(e1.getMessage().equals(InvalidHeader)) {
                    /*receive unexpected version*/
                    String msg="Unexpected version: "+message.getVersion();
                    output(msg,message.getMapId(),msg.length());
                    writeToLogger(msg);
                    return;
                }
                /*Problem parsing message*/
               
                String msg=e1.getMessage();
                output(msg,0,msg.length());
                writeToLogger(msg);
                return;
            } catch (SocketException e) {
                /*server must terminate the connection*/
                writeToLogger(e.getMessage()+"***client terminated");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            String operation = message.getOperation();
            
            if(message.getMapId() != MAPID) {
                String msg="No such map: "+message.getMapId();
                int count=msg.length();
                output(msg,message.getMapId(),count);
                writeToLogger(msg);
                continue;
            }
            
            switch(operation) {
                /*handle NEW operation*/
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
                    int count;
                    count=msg.length();
                    output(msg,message.getMapId(),count);
                    writeToLogger(msg);
                    break;
                /*receive unknown message*/
                default:
                    msg="Unknown operation: "+operation;
                    count=msg.length();
                    output(msg,message.getMapId(),count);
                    writeToLogger(msg);
                    break;
            
            }
        } 
        
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
        if(!password.containsKey(lr.getUserId()%dividor)) {
            String msg="No such user: "+lr.getUserId();
            output(msg,message.getMapId(),msg.length());
            return; 
        }
        /*get user name from password map and re-name the username*/
        String userName = password.get(lr.getUserId()%dividor);
        int pos = userName.indexOf(del);
        userName=userName.substring(0,pos);
        String newLocationName = userName + ": "+lr.getLocationName();
        
        try {
            LocationRecord lr2 = new LocationRecord(lr.getUserId(),lr.getLongitude(),lr.getLatitude()
                    ,newLocationName,lr.getLocationDescription());
            
            locationList.put(lr.getUserId()%dividor, lr2);
            /*make a new response message*/
            AddATudeLocationResponse response=new AddATudeLocationResponse(message.getMapId(),mapName);
            for(LocationRecord i : locationList.values()) {
                response.addLocationRecord(i);
            }
            //write out yo outputStream
            response.encode(out);
            //add new location to google map
            mgr.addLocation(new Location(lr.getLocationName(), lr.getLongitude(), lr.getLatitude(), 
                    lr.getLocationDescription(), Location.Color.BLUE));
            //write out to logger
            String temp = lr.getLocationName()+"-"+lr.getLocationDescription()+
                    "at"+lr.getLongitude()+","+lr.getLatitude();
            writeToLogger(temp);
            
        } catch (AddATudeException e) {
            String msg=e.getLocalizedMessage();
            output(msg,0,msg.length());
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
            response.encode(out);
            
        } catch (AddATudeException e) {
            
            String msg=e.getMessage();
            output(msg,0,msg.length());
            return;
        }
    }
    
    /**
     * main function
     * 
     * @param args - arguments take from the command line
     * first: port number; second: threads number; third: password file name
     * 
     * @throws NumberFormatException - number format exception
     */
    @SuppressWarnings("resource")
    public static void main(String[] args) throws NumberFormatException {
        if((args.length <3) || (args.length >=4)) {
            System.out.println("Parameters for server: <Port> <Thread num> <userFile>");
            System.exit(0);
        }     
        ServerSocket server = null;
        ExecutorService threadPool = null;
        try {
            server = new ServerSocket(Integer.valueOf(args[0]));
            threadPool = Executors.newFixedThreadPool(Integer.valueOf(args[1])); 
            readPassword(args[2]);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        
        while(true) {
            Socket socket=null;
            try {
                socket = server.accept();
                AddATudeServer aServer = new AddATudeServer(socket);
                
                threadPool.submit(aServer);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }          
            

        }
    }
 }
