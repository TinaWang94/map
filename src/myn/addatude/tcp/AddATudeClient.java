package myn.addatude.tcp;

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

public class AddATudeClient {
    private final String ALL="ALL";
    private final String NEW="NEW";
    private void checkInt(int aInt) throws AddATudeException {
        if(aInt <0) {
            throw new AddATudeException("Invalid id number.");
        }
        
    }
    private int mapId;
    private Socket socket;
    private int userId;
    private MessageInput in;
    private MessageOutput out;
    
    public int getMapId(){
        return mapId;
    }
    public int getUserId(){
        return userId;
    }
    public void setMapId(int mapId) throws AddATudeException {
        checkInt(mapId);
        this.mapId=mapId;
    }
    public void setUserId(int userId) throws AddATudeException {
        checkInt(userId);
        this.userId=userId;
    }
    
    public AddATudeClient(String host,int port) throws IOException{
        socket = new Socket(host,port);
        out = new MessageOutput(socket.getOutputStream());
        in=new MessageInput(socket.getInputStream());
    }
    
    public void interactLoop( ) throws AddATudeException, EOFException {
        Scanner keyboard = new Scanner(System.in);
        while(true) {
            System.out.println("Operation>");
            String operation = keyboard.next();
            switch(operation) {
                case ALL:
                    operationAll(keyboard);
                    break;
                case NEW:
                    operationNew(keyboard);
                    break;
                default:
                    break;
                    
            }
            System.out.print("Continue (y/n)>");
            String judge = keyboard.next();
            if("n".equals(judge)) {
                break;
            }
            else if (!("y".equals(judge))) {
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                // re type y/n
            }
        }
        
    }
    
    private void operationAll (Scanner keyboard) throws AddATudeException, EOFException {
        System.out.print("Map Id>"); 
        mapId= keyboard.nextInt();
        AddATudeMessage request = new AddATudeLocationRequest(mapId);
        request.encode(out);
        operationDecode();
    }
    private void operationDecode () throws EOFException, AddATudeException {
        AddATudeMessage response = AddATudeMessage.decode(in);      
        System.out.println(response.toString());
    }
    private void operationNew (Scanner keyboard) throws AddATudeException, EOFException {
        System.out.print("Map Id>"); 
        mapId= keyboard.nextInt();
        System.out.print("User Id>"); 
        userId=keyboard.nextInt();
        System.out.print("Longitude>"); 
        String longitude = keyboard.next();
        System.out.print("Latitude>"); 
        String latitude = keyboard.next();
        System.out.print("Location Name>"); 
        String locationName = keyboard.next();
        System.out.print("Location Description>");
        String locationDescription = keyboard.next();
       
        AddATudeMessage newLocation = new AddATudeNewLocation(mapId,
                new LocationRecord(userId,longitude,latitude,locationName,locationDescription));
        newLocation.encode(out);
        operationDecode();
    }
   
}
