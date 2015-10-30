/*
 * Classname : LocationRecordTest
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
import myn.notifi.protocol.NoTiFiLocationAddition;

import org.junit.Test;
/**
 * Generate Junit test case
 * 
 * */
public class LocationRecordTest {

    /**
     * Test encode of location Reocrd 
     * 
     * @throws IllegalArgumentException - fails to encode
     * @throws IOException i/o problem 
     */
    @Test
    public void testEncodeLr() throws IllegalArgumentException, IOException {

        LocationRecord lr = new LocationRecord(77,31.550285,-97.117215,"BU","BAYLOR");
        NoTiFiLocationAddition m = new NoTiFiLocationAddition (0,lr);
        assertEquals(0, m.getMsgId());
        assertEquals(1, m.getCode());
        LocationRecord lr2 = m.getLocationRecord();
        assertEquals(77,lr2.getUserId());
        assertEquals(-97.117215,lr2.getLatitude(),0.0);
        assertEquals(31.550285,lr2.getLongitude(),0.0);
        assertEquals("BU",lr2.getLocationName());
        assertEquals("BAYLOR",lr2.getLocationDescription());

    }

}
