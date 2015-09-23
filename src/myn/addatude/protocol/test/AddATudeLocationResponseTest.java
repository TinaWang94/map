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
import java.io.UnsupportedEncodingException;
import java.util.List;

import myn.addatude.protocol.AddATudeException;
import myn.addatude.protocol.AddATudeLocationResponse;
import myn.addatude.protocol.AddATudeMessage;
import myn.addatude.protocol.LocationRecord;
import myn.addatude.protocol.MessageInput;
import myn.addatude.protocol.MessageOutput;

import org.junit.Test;
/**
 * Purpose:
 * Test AddATude location response message
 * and provides serialization/deserialization
 * 
 * @author tong wang
 * */
public class AddATudeLocationResponseTest {

    private AddATudeLocationResponse a;
    
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
         assertEquals("mapId=345, mapName=BU, number of location record=1,"
                 + " LocationRecord0: userId=1, longitude=1.2, latitude=3.4,"
                 + " location=BU, location name=Baylor.", b.toString());

     }
     /**
      * invalid end of line format, should throw an exception
      * @throws AddATudeException - if deserialization or validation failure
      * @throws EOFException - if premature end of stream
      * @throws UnsupportedEncodingException - unsupported encoding exception
      * 
      * */
     @Test (expected = AddATudeException.class)
     public void testEofL() throws UnsupportedEncodingException, EOFException, AddATudeException {
         MessageInput in = new MessageInput(new ByteArrayInputStream
                 ("ADDATUDEv1 345 RESPONSE 2 BU1 1 1.2 3.4 2 BU6 Baylor \r\n".getBytes("ASCII")));
         
         @SuppressWarnings({ "static-access", "unused" })
         AddATudeMessage b=a.decode(in);
     }
     
     /**
      * test addLocationRecord function and toString function
      * @throws AddATudeException - if deserialization or validation failure
      * @throws EOFException - if premature end of stream
      * @throws UnsupportedEncodingException - unsupported encoding exception
      * */
     @Test 
     public void testAddLocationRecord() throws UnsupportedEncodingException, EOFException, AddATudeException {
         MessageInput in = new MessageInput(new ByteArrayInputStream
                 ("ADDATUDEv1 345 RESPONSE 2 BU1 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
         
         @SuppressWarnings({ "static-access" })
         AddATudeMessage b=a.decode(in);
         LocationRecord lr;
         MessageInput in2 = new MessageInput(new ByteArrayInputStream("1 1.2 3.4 2 BU6 Baylor".getBytes("ASCII")));
         lr = new LocationRecord(in2);
         ((AddATudeLocationResponse)b).addLocationRecord(lr);
         assertEquals("mapId=345, mapName=BU, number of location record=2,"
                 + " LocationRecord0: userId=1, longitude=1.2, latitude=3.4, location=BU, location name=Baylor."
                 + "LocationRecord1: userId=1, longitude=1.2, latitude=3.4, location=BU, location name=Baylor.",
                 b.toString());
         
     }
     /**
      * test setMapName function and getmapName function
      * @throws AddATudeException - if deserialization or validation failure
      * @throws EOFException - if premature end of stream
      * @throws UnsupportedEncodingException - unsupported encoding exception
      * */
     @Test 
     public void testSetMapName() throws UnsupportedEncodingException, EOFException, AddATudeException {
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
      * @throws EOFException - if premature end of stream
      * @throws UnsupportedEncodingException - unsupported encoding exception
      * */
     @Test (expected = AddATudeException.class)
     public void testSetMapNameNull() throws UnsupportedEncodingException, EOFException, AddATudeException {
         MessageInput in = new MessageInput(new ByteArrayInputStream
                 ("ADDATUDEv1 345 RESPONSE 2 BU1 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
         
         @SuppressWarnings({ "static-access" })
         AddATudeMessage b=a.decode(in);
         ((AddATudeLocationResponse)b).setMapName(null);
         
         
     }
     /**
      * test getLocationRecordList function
      * @throws AddATudeException - if deserialization or validation failure
      * @throws EOFException - if premature end of stream
      * @throws UnsupportedEncodingException - unsupported encoding exception
      * */
     @Test 
     public void testGetLocationRecordList() throws UnsupportedEncodingException, EOFException, AddATudeException {
         MessageInput in = new MessageInput(new ByteArrayInputStream
                 ("ADDATUDEv1 345 RESPONSE 2 BU2 1 1.2 3.4 2 BU6 Baylor1 1.3 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
         
         @SuppressWarnings({ "static-access" })
         AddATudeMessage b=a.decode(in);
         List<LocationRecord> lr;
         lr=((AddATudeLocationResponse)b).getLocationRecordList();
         assertEquals("userId=1, longitude=1.2, latitude=3.4, location=BU, location name=Baylor.",lr.get(0).toString());
         assertEquals("userId=1, longitude=1.3, latitude=3.4, location=BU, location name=Baylor.",lr.get(1).toString());
         
     }
     /**
      * test Encode function
      * @throws AddATudeException - if deserialization or validation failure
      * @throws EOFException - if premature end of stream
      */
      @Test
      public void testEncode() throws AddATudeException, IOException {
          MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 RESPONSE 2 BU1 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
          MessageOutput out = null;
          @SuppressWarnings("static-access")
          AddATudeMessage b=a.decode(in);
          b.encode(out);

      }
     
     

}
