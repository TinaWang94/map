/*
 * Classname : AddATudeServerAIO
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;













import mapservice.GoogleMapMaker;
import mapservice.MapManager;
import mapservice.MemoryMapManager;
import myn.addatude.protocol.ConstantVariable;
import myn.notifi.app.NoTiFiServer;

public class AddATudeServerAIO {
    private final static int dividor = 256;
    private final static String del = ":";
    public static final int MAPID=345;
    
    private static AsynchronousServerSocketChannel server;
    public static Map<Integer,String> password = new HashMap<>();
    private static final String LOCATIONFILE = "markers.js";
    private static String LOGFILE = "connections.log";
    public static final Logger LOGGER = Logger.getLogger(AddATudeServer.class.getName());
    private static FileHandler fh;
    public static MapManager mgr = new MemoryMapManager();
    
    
    
    
    private static NoTiFiServer notifiServer;
    
    
    

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
     
    
    public static void main(String[] args) throws InterruptedException {
        if((args.length <2) || (args.length >=3)) {
            System.out.println("Parameters for server: <Port> <userFile>");
            System.exit(0);
        }
        try {
            mgr.register(new GoogleMapMaker(LOCATIONFILE, mgr));
            LOGGER.setLevel(Level.ALL);
            LOGGER.addHandler(fh);
            readPassword(args[1]);
            InetSocketAddress addr = new InetSocketAddress("localhost",Integer.valueOf(args[0]));
            server = AsynchronousServerSocketChannel.open().bind(addr); 
            Handler hd = new Handler(server,notifiServer);
            
            server.accept(null, hd);
            
        } catch (Exception e) {
            ///////////////////////////////////////////////
            e.printStackTrace();
        }
        while (true) {
            Thread.sleep(1000);
        }
    }

}


