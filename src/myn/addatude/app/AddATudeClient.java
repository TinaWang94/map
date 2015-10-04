/*
 * Classname : AddATudeClient
 *
 * Version information : 1.0
 *
 * Date : 9/23/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 * 
 * Assignment : program2
 */
package myn.addatude.app;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import myn.addatude.protocol.AddATudeException;
import myn.addatude.protocol.AddATudeLocationRequest;
import myn.addatude.protocol.AddATudeMessage;
import myn.addatude.protocol.AddATudeNewLocation;
import myn.addatude.protocol.LocationRecord;
import myn.addatude.protocol.MessageInput;
import myn.addatude.protocol.MessageOutput;

/**
 * class name: AddATudeClient
 * This class gives basic functions for implement a 
 * TCP client that operates on the class map.
 * @author tong wang
 * 
 * */

public class AddATudeClient {
    /*initialize some local strings for checking valiation purpose*/
    private final String ALL="ALL";
    private final String NEW="NEW";
    private final String RESPONSE="RESPONSE";
    private final String ERROR="ERROR";
    public static final String CHECK2 = "0|[1-9][0-9]*";
    private final String EOLN="\r\n";
    
    /**
     * check if a integer is unsigned or not
     * @param aInt - a interger waiting for check
     * @throws AddATudeException - is thrown if the id is negative
     */
    private void checkInt(int aInt) throws AddATudeException  {
        if(aInt <0) {
            throw new AddATudeException("Invalid id number.");
        }
        
    }
    /**
     * check if the input string is a valid integer
     * @param a - the input string waiting for check
     * @throws AddATudeException - is thrown if the id is invalid
     */
    private void checkInt2(String a) throws AddATudeException {
        if(!a.matches(CHECK2)) {
            throw new AddATudeException("mapId doesn't match the given format.");
        }
    }
    
    /**
     * 
     * print out proper message to the screen when get a invalid message input
     * @param e - exception for invalid message
     */
    private void invalidInput(AddATudeException e) {
        System.out.println("Invalid user input: "+e.getMessage());
    }
    
    /*initial all needed variables*/
    private int mapId;
    private Socket socket;
    private int userId;
    private MessageInput in;
    private MessageOutput out;
    
    
    /**
     * return mapId
     * @return mapId
     */
    public int getMapId(){
        return mapId;
    }
    /**
     * return userId
     * @return userId
     */
    public int getUserId(){
        return userId;
    }
    /**
     * set mapId
     * @param mapId - mapId to be set
     * @throws AddATudeException - valiation failed
     */
    public void setMapId(int mapId) throws AddATudeException {
        checkInt(mapId);
        this.mapId=mapId;
    }
    /**
     * set userId
     * @param userId - userId to be set
     * @throws AddATudeException - valiation failed
     */
    public void setUserId(int userId) throws AddATudeException {
        checkInt(userId);
        this.userId=userId;
    }
    
    /**
     * initialize the socket and get connected to server
     * @param host - host name
     * @param port - port number
     */
    public AddATudeClient(String host,int port) {    
        try {
            socket = new Socket(host,port);
            if(!socket.isConnected()) {
                System.out.println("Unable to communicate: "
                        + "Cannot connect to the server.");
                System.exit(0);
            }
            out = new MessageOutput(socket.getOutputStream());
            in=new MessageInput(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Unable to communicate: "+e.getMessage());
            System.exit(0);
        }
    }

    /**
     * main loop for get user input and deal with them by different input
     * @throws AddATudeException - valiation failed
     * @throws IOException - io error occur
     */
    public void interactLoop() throws AddATudeException, IOException {
        Scanner keyboard = new Scanner(System.in);
        keyboard.useDelimiter(EOLN);
        while(true) {
            System.out.print("Operation>");
            String operation = keyboard.next();
            switch(operation) {
                case ALL:
                    operationAll(keyboard);
                    break;
                case NEW:
                    operationNew(keyboard);
                    break;
                default:
                    System.out.println("Invalid user input: bad operation.");
                    break;
                    
            }
            System.out.print("Continue (y/n)>");
            String judge = keyboard.next();
            if("n".equals(judge) || "N".equals(judge)) {
                break;
            }
            else   {
                while (!("y".equals(judge) || "Y".equals(judge))) {
                    System.out.println("Invalid message: only 'y' or 'n' is accepted");
                    System.out.print("Continue (y/n)>");
                    judge = keyboard.next();
                }
            }
        }
        
    }

    /**
     * user request all locations
     * deal with encode and decode
     * @param keyboard
     * @throws AddATudeException - valiation failed
     * @throws IOException - io error occur
     */
    private void operationAll (Scanner keyboard) throws AddATudeException, IOException     {
        //check valiation for mapId
        System.out.print("Map Id>"); 
        String a = keyboard.next();
        try {
            checkInt2(a);
        } catch (AddATudeException e1) {
            invalidInput(e1);
            return;
        }

        mapId=Integer.valueOf(a);
        
        AddATudeMessage request = null;
        try {
            request = new AddATudeLocationRequest(mapId);
        } catch (AddATudeException e) {
            invalidInput(e);
            return;
        }
        
        request.encode(out);
        
        AddATudeMessage decode=operationDecode();
        if(!(RESPONSE.equals(decode.getOperation()) || ERROR.equals(decode.getOperation()))) {
            System.out.println("Unexpected message: "+decode.getOperation());
            return;
        }
        System.out.println(decode.toString());
    }
    
    /**
     * Decode message after receiveing MessageInput
     * Deal with the invalid message when decoding
     * @return decode - aN AddATude Message variable
     * may be used in all/new function for decoding
     * @throws IOException - io error occur
     * 
     */
    private AddATudeMessage operationDecode () throws IOException {
        AddATudeMessage decode=null;  
        
        try {
            decode = AddATudeMessage.decode(in);
        } catch (EOFException | AddATudeException e) {
            System.out.println("Invalid message: "+e.getMessage());
            
            System.exit(0);
        }
        return decode; 
    }

    /**
     * user request to create a new location
     * deal with encode and decode
     * 
     * @param keyboard - A Scanner, scan input from the screen
     * @throws IOException - io error occur
     * @throws AddATudeException - valiation fails
     */
    private void operationNew (Scanner keyboard) throws  IOException, AddATudeException {
        //check the valiation for mapId
        System.out.print("Map Id>"); 
        String a = keyboard.next();
        try {
            checkInt2(a);
            checkInt(Integer.valueOf(a));
        } catch (AddATudeException e1) {
            invalidInput(e1);
            return;
        }
        mapId=Integer.valueOf(a);
        AddATudeMessage newLocation=null;

            try {
                newLocation = new AddATudeNewLocation(mapId,
                        new LocationRecord(keyboard,System.out));
            } catch (AddATudeException e) {
                invalidInput(e);
                return;
                }

        newLocation.encode(out);
        AddATudeMessage decode=operationDecode();
        if(!(RESPONSE.equals(decode.getOperation()) || ERROR.equals(decode.getOperation()))) {
            System.out.println("Unexpected message: "+decode.getOperation());
            return;
        }
        System.out.println(decode.toString());
        
    }
    
    /**
     * main function
     * impliment a TCP clients
     * @param args - command line arguments
     * @throws IOException - io error occurs
     * @throws AddATudeException - valiation fails
     */
    public static void main(String[] args) throws IOException, AddATudeException {
        if((args.length <2) || (args.length >3)) {
            System.out.println("parameters: <Server> <Word> [<Port>]");
            System.exit(0);
        }     
        
        try{        
            AddATudeClient client = new AddATudeClient(args[0],Integer.valueOf(args[1]));
            client.interactLoop();
        } catch(IOException e) {
            System.out.println("Unable to communicate: "+e.getMessage());
            System.exit(0);
        }
        
    }
}
