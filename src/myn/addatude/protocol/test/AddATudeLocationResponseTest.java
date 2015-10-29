/*
 * Classname : AddATudeLocationResponse
 *
 * Version information : 1.0
 *
 * Date : 9/13/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 * 
 * Assignment : program1
 */
package myn.addatude.protocol.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;

import myn.addatude.protocol.AddATudeException;
import myn.addatude.protocol.AddATudeLocationResponse;
import myn.addatude.protocol.AddATudeMessage;
import myn.addatude.protocol.LocationRecord;
import myn.addatude.protocol.MessageInput;

import org.junit.Test;
/**
 * Purpose:
 * Test AddATude location response message
 * and provides serialization/deserialization
 * 
 * @author tong wang
 * */
public class AddATudeLocationResponseTest {

    private AddATudeLocationResponse a=null;
    
    /**
     * test response message
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     */
     @Test
     public void testResponse() throws AddATudeException, IOException {
         MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 RESPONSE 2 BU1 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
         
         @SuppressWarnings("static-access")
         AddATudeMessage b=a.decode(in);
         assertEquals("RESPONSE", b.getOperation());
         assertEquals("mapId=345 - BU\r\nUser 1:BU - Baylor at (1.2, 3.4)\r\n", b.toString());

     }
     /**
      * invalid end of line format, should throw an exception
      * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException - IOException
      * 
      * */
     @Test (expected = AddATudeException.class)
     public void testEofL() throws AddATudeException, IOException {
         MessageInput in = new MessageInput(new ByteArrayInputStream
                 ("ADDATUDEv1 345 RESPONSE 2 BU1 1 1.2 3.4 2 BU6 Baylor \r\n".getBytes("ASCII")));
         
         @SuppressWarnings({ "static-access", "unused" })
         AddATudeMessage b=a.decode(in);
     }
     
     /**
      * test addLocationRecord function and toString function
      * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException - IOException
      * */
     @Test 
     public void testAddLocationRecord() throws AddATudeException, IOException {
         MessageInput in = new MessageInput(new ByteArrayInputStream
                 ("ADDATUDEv1 345 RESPONSE 2 BU1 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
         
         @SuppressWarnings({ "static-access" })
         AddATudeMessage b=a.decode(in);
         LocationRecord lr;
         MessageInput in2 = new MessageInput(new ByteArrayInputStream("1 1.2 3.4 2 BU6 Baylor".getBytes("ASCII")));
         lr = new LocationRecord(in2);
         ((AddATudeLocationResponse)b).addLocationRecord(lr);
         assertEquals("mapId=345 - BU\r\nUser 1:BU - Baylor at (1.2, 3.4)\r\nUser 1:BU - Baylor at (1.2, 3.4)\r\n",
                 b.toString());
         
     }
     /**
      * test setMapName function and getmapName function
      * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException - IOException
      * */
     @Test 
     public void testSetMapName() throws AddATudeException, IOException {
         MessageInput in = new MessageInput(new ByteArrayInputStream
                 ("ADDATUDEv1 345 RESPONSE 2 BU1 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
         
         @SuppressWarnings({ "static-access" })
         AddATudeMessage b=a.decode(in);
         
         ((AddATudeLocationResponse)b).setMapName("Bu");
         assertEquals("Bu",((AddATudeLocationResponse)b).getMapName());
         
     }
     /**
      * test setMapName function to null
      * an AddATudeexception should be thrown since mapName shouldn't be null
      * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException - IOException
      * */
     @Test (expected = AddATudeException.class)
     public void testSetMapNameNull() throws AddATudeException, IOException {
         MessageInput in = new MessageInput(new ByteArrayInputStream
                 ("ADDATUDEv1 345 RESPONSE 2 BU1 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
         
         @SuppressWarnings({ "static-access" })
         AddATudeMessage b=a.decode(in);
         ((AddATudeLocationResponse)b).setMapName(null);
         
         
     }
     /**
      * test getLocationRecordList function
      * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException - IOException
      * */
     @Test 
     public void testGetLocationRecordList() throws AddATudeException, IOException {
         MessageInput in = new MessageInput(new ByteArrayInputStream
                 ("ADDATUDEv1 345 RESPONSE 2 BU2 1 1.2 3.4 2 BU6 Baylor1 1.3 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
         
         @SuppressWarnings({ "static-access" })
         AddATudeMessage b=a.decode(in);
         List<LocationRecord> lr;
         lr=((AddATudeLocationResponse)b).getLocationRecordList();
         assertEquals("User 1:BU - Baylor at (1.2, 3.4)\r\n",lr.get(0).toString());
         assertEquals("User 1:BU - Baylor at (1.3, 3.4)\r\n",lr.get(1).toString());
         
     }

}
