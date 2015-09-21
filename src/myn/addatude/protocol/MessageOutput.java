/*
 * Classname : MessageOutput
 *
 * Version information : 1.0
 *
 * Date : 9/7/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 */
package myn.addatude.protocol;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * Purpose : Serialization output source for AddATude messages
 * @author Tong Wang
 * @version 1.0
 * */

public class MessageOutput {
    /*treat MessageOutput as a mediator of a outputstream*/
    private OutputStream out;
    
    /**
     * Constructs a new output source from an OutputStream
     * @param out- output source
     * */
    
    public MessageOutput(OutputStream out) {
        this.out = out;
    }
    
    /**
     * Writes b.length bytes from the specified byte array
     * to this output stream.
     * @param  b - the data.
     * @throws IOException - if an I/O error occurs.
     * */
    
    public void write(byte[] b) throws IOException {
        out.write(b);
    }
    
    /**
     * Writes len bytes from the specified byte array starting at
     * offset off to this output stream.
     * @param  b - the data.
     * @param  off - the start offset in the data.
     * @param len - the number of bytes to write.
     * @throws IOException - if an I/O error occurs. In particular,
     *        an IOException is thrown if the output stream is closed.
     * */
    
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b,off,len);
    }
}