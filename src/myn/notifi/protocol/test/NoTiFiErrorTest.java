/*
 * Classname : NoTiFiErrorTest
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

import myn.notifi.protocol.NoTiFiError;
import myn.notifi.protocol.NoTiFiMessage;

import org.junit.Test;
/**
 * Generate Junit test case
 * 
 * */
public class NoTiFiErrorTest {
    /**
     * Test Encode Error message
     * 
     * @throws IllegalArgumentException - if data fails validation
     * @throws IOException - if I/O problems
     */
    @Test 
    public void testEncodeErr() throws IllegalArgumentException, IOException{
        byte[] b = new byte [] {0x34, 0x7b, 0x42, 0x61, 0x64,
                0x20, 0x65, 0x72, 0x72, 0x6f, 0x72 };
        NoTiFiMessage m = new NoTiFiError(123,"Bad error");
        assertEquals(123, m.getMsgId());
        assertEquals(4, m.getCode());
        byte[] encode = m.encode();
        assertEquals(b.length,encode.length);
        for(int i = 0; i < b.length; i++){
            assertEquals(b[i],encode[i]);
        }
    }
    /**
     * Test decode of Error message
     * 
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test
    public void testDecodeErr() throws IOException, IllegalArgumentException {
        NoTiFiError m = (NoTiFiError) NoTiFiMessage.decode(new byte [] {0x34, 0x7b, 0x42, 0x61, 0x64, 0x20, 0x65, 0x72, 0x72, 0x6f, 0x72 });
        assertEquals(123, m.getMsgId());
        assertEquals(4, m.getCode());
        assertEquals("Bad error", m.getErrorMessage());
    }
}
