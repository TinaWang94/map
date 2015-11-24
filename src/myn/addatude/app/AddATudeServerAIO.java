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

import java.io.IOException;
import java.nio.ByteBuffer;

import myn.addatude.protocol.AddATudeException;
import myn.addatude.protocol.AddATudeMessage;
import myn.addatude.protocol.MessageInput;

public class AddATudeServerAIO {
    
    
    private static final byte DELIMITER = 13;
    private static final byte DELIMITER2 = 10;

    
    public AddATudeMessage deframer(ByteBuffer readBuffer) throws AddATudeException, IOException{
        
        readBuffer.mark();
        StringBuffer aBuf = new StringBuffer();
        byte b;
        while(readBuffer.hasRemaining()){
            b=readBuffer.get();
            aBuf.append(b);
            if(b==DELIMITER){
                b=readBuffer.get();
                aBuf.append(b);
                if(b==DELIMITER2){
                    return AddATudeMessage.decode(new MessageInput(aBuf.toString().getBytes()));
                }
            }
        }
        readBuffer.reset();
        return null;
    }
    
    
    
    public static void main(String[] args) {


    }

}
