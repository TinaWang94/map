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
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static final String LOCATIONFILE = "makers.js";
    
    private Socket socket;
    private MessageInput in;
    private MessageOutput out;
    
    private static Map<Integer,String> password = new HashMap<>();
    private MapManager mgr = new MemoryMapManager();
    private final static int dividor = 255;
    private final static String del = ":";
    
    private static void readPassword(String filename) {
       
        
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){String line = br.readLine();
            while(line != null) {
                int pos = line.indexOf(del);
                ///////////////////////////////////////////////
                //check num
                int key = Integer.valueOf(line.substring(0, pos));
                key=key % dividor;
                String value = line.substring(pos+1, line.length());
                password.put(key,value);
                line = br.readLine();
            }
            
        } catch (IOException e) {
            //cannot fine the file
            e.printStackTrace();
        }
        
    }
    

    
    public AddATudeServer(Socket socket) {
        this.socket=socket;
        mgr.register(new GoogleMapMaker(LOCATIONFILE, mgr));
        try {
            out=new MessageOutput(socket.getOutputStream());
            in=new MessageInput(socket.getInputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void run() {
        while(true) {
            try {
                AddATudeMessage message = AddATudeMessage.decode(in);
                String operation = message.getOperation();
                switch(operation) {
                    case ConstantVariable.NEW:
                        break;
                    case ConstantVariable.RESPONSE:
                        break;
                    default:
                        break;
                
                }
            } catch (EOFException | AddATudeException e) {
                
                e.printStackTrace();
            }
            
        }
    }
    /*
    private LocationRecord modify(Location lc) {
        
    }*/
    
    private void operationNew(AddATudeNewLocation message) {
        /////////////////////////////////////////////
        //mapname??
        String mapName="Class Map";
        LocationRecord lr = message.getLocationRecord();
        String userName = password.get(lr.getUserId()%dividor);
        int pos = userName.indexOf(del);
        userName=userName.substring(0,pos);
        String newLocationName = userName + ": "+lr.getLocationName();
        try {
            LocationRecord lr2 = new LocationRecord(lr.getUserId(),lr.getLongitude(),lr.getLatitude()
                    ,newLocationName,lr.getLocationDescription());
            AddATudeLocationResponse response=new AddATudeLocationResponse(message.getMapId(),mapName);
            
        } catch (AddATudeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws NumberFormatException, IOException {
        ServerSocket server = new ServerSocket(Integer.valueOf(args[0]));
        ExecutorService threadPool = Executors.newFixedThreadPool(Integer.valueOf(args[1])); 
        readPassword(args[2]);
        while(true) {
            Socket socket = server.accept();
            AddATudeServer aServer = new AddATudeServer(socket);
            threadPool.submit(aServer);
        }
    }
 }
