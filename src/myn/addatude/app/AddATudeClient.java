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

public class AddATudeClient {
    private final String ALL="ALL";
    private final String NEW="NEW";
    private final String EOLN="\r\n";
    
    private void checkInt(int aInt) throws AddATudeException  {
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
    
    public void interactLoop( ) throws AddATudeException, EOFException {
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
    
    private void operationAll (Scanner keyboard) throws AddATudeException, EOFException  {
        System.out.print("Map Id>"); 
        int id=keyboard.nextInt();
        mapId=id ;
        
        AddATudeMessage request = null;
        request = new AddATudeLocationRequest(mapId);
        
        request.encode(out);
        operationDecode();
    }
    private void operationDecode () throws EOFException, AddATudeException {
        AddATudeMessage decode = AddATudeMessage.decode(in);      
        System.out.println(decode.toString());
    }
    private void operationNew (Scanner keyboard) throws AddATudeException, EOFException {
        System.out.print("Map Id>"); 
        int id=keyboard.nextInt();
        checkInt(id);
        mapId=id ;
        
        AddATudeMessage newLocation = new AddATudeNewLocation(mapId,
                new LocationRecord(keyboard,System.out));
        newLocation.encode(out);
        operationDecode();
        
    }
    public static void main(String[] args) throws IOException, AddATudeException {
        // TODO Auto-generated method stub
        AddATudeClient client = new AddATudeClient(args[0],Integer.valueOf(args[1]));
        client.interactLoop();
    }

   
}
