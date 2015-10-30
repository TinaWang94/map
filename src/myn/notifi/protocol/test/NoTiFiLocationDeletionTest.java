/*
 * Classname : NoTiFiLocationDeletionTest
 *
 * Version information : 1.0
 *
 * Date : 10/29/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 */
package myn.notifi.protocol.test;

import static org.junit.Assert.*;

import java.io.IOException;

import myn.notifi.protocol.IllegalArgumentException;
import myn.notifi.protocol.LocationRecord;
import myn.notifi.protocol.NoTiFiLocationDeletion;


import myn.notifi.protocol.NoTiFiMessage;

import org.junit.Test;
/**
 * Generate Junit test case
 * 
 * */
public class NoTiFiLocationDeletionTest {
    /**
     * test Encode Delete location record 
     * 
     * @throws IllegalArgumentException - if data fails validation
     * @throws IOException - if I/O problems
     */
    @Test
    public void testEncodeDel() throws IllegalArgumentException, IOException {
        byte[] b = new byte [] {0x32, 0x00, 0x00, 0x4D, (byte) 0xB7, 0x7A, 0x4E, 0x7A, (byte) 0xDF, 
                (byte) 0x8C, 0x3F, 0x40, 0x71, (byte) 0xE6, 0x57, 0x73, (byte) 0x80, 0x47, 0x58, 
                (byte) 0xC0, 0x02, 0x42, 0x55, 0x06, 0x42, 0x41, 0x59, 0x4C, 0x4F, 0x52 };
        LocationRecord lr = new LocationRecord(77,31.550285,-97.117215,"BU","BAYLOR");
        NoTiFiLocationDeletion m = new NoTiFiLocationDeletion (0,lr);
        assertEquals(0, m.getMsgId());
        assertEquals(2, m.getCode());
        LocationRecord lr2 = m.getLocationRecord();
        assertEquals(77,lr2.getUserId());
        assertEquals(-97.117215,lr2.getLatitude(),0.0);
        assertEquals(31.550285,lr2.getLongitude(),0.0);
        assertEquals("BU",lr2.getLocationName());
        assertEquals("BAYLOR",lr2.getLocationDescription());
        byte[] encode = m.encode();
        assertEquals(b.length,encode.length);
        for(int i = 0; i < b.length; i++){
            assertEquals(b[i],encode[i]);
        }
    }
    /**
     * Test decode of deletion
     * 
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test
    public void testDecodeDel() throws IOException, IllegalArgumentException {
        NoTiFiLocationDeletion m = (NoTiFiLocationDeletion) NoTiFiMessage.decode(new byte [] 
                {0x32, 0x00, 0x00, 0x4D, (byte) 0xB7, 0x7A, 0x4E, 0x7A, (byte) 0xDF, 
                (byte) 0x8C, 0x3F, 0x40, 0x71, (byte) 0xE6, 0x57, 0x73, (byte) 0x80, 
                0x47, 0x58, (byte) 0xC0, 0x02, 0x42, 0x55, 0x06, 0x42, 0x41, 0x59, 0x4C, 0x4F, 0x52 });
        assertEquals(0, m.getMsgId());
        assertEquals(2, m.getCode());
        LocationRecord lr = m.getLocationRecord();
        assertEquals(77,lr.getUserId());
        assertEquals(-97.117215,lr.getLatitude(),0.0);
        assertEquals(31.550285,lr.getLongitude(),0.0);
        assertEquals("BU",lr.getLocationName());
        assertEquals("BAYLOR",lr.getLocationDescription());
    } 

}
