/*
 * Classname : NoTiFiRegister
 *
 * Version information : 1.0
 *
 * Date : 10/29/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 */
package myn.notifi.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Purpose of this class
 * 
 * Represents a register message
 * @author tong wang
 * */

public class NoTiFiRegister extends NoTiFiMessage{
    /*initial constant for code and error message*/
    private final int code=ConstVal.reg;
    private final String errorMsg = "Invalid address";
    private final String errorMsg2 = "Invalid port";
    private final int range = 65535;
    /*member variables*/
    //address is 4 bytes
    private Inet4Address address;
    private int port;
    
    /**
     * Check validation of ip address 
     * 
     * @param addr - address to be checked
     * @throws IllegalArgumentException - if any value fails validation
     */
    private void checkAddress (Inet4Address addr) throws IllegalArgumentException {
        if(addr == null) {
            throw new IllegalArgumentException (errorMsg);
        }
    }
    /**
     * Check validation of port
     * @param p - port to be checked
     * @throws IllegalArgumentException - if port is out of range (0...65535)
     */
    private void checkPort(int p) throws IllegalArgumentException {
        if(p<0 || p>range)
            throw new IllegalArgumentException (errorMsg2);
    }
    /**
     * Constructs deregister message from input stream
     *
     * @param in - deserialization input source
     * @throws IllegalArgumentException - if any value fails validation
     * @throws IOException - if I/O problem
     */
    public NoTiFiRegister(DataInputStream in) throws IllegalArgumentException, IOException{
        
    }
    /**
     * Constructs register message using set values(from dataInput)
     * 
     * @param in - dataInput
     * @param msgId - MSG ID
     * @param version -version info
     * @throws UnknownHostException - unknown host exception
     * @throws IOException - if I/O problem
     * @throws IllegalArgumentException - if any value fails validation
     */
    public NoTiFiRegister(DataInputStream in,int msgId,int version) 
            throws UnknownHostException, IOException, IllegalArgumentException {
        super(msgId);
        this.version = version;
        //address is 4 bytes      
        address=(Inet4Address)InetAddress.getByAddress(Parser.readAddr(in));
        //port is 2 bytes
        int temp;
        temp=Parser.readInt(in, 2);
        checkPort(temp);
        port = temp;
        
    }
    /**
     * Constructs register message using set values
     * 
     * @param msgId - message ID
     * @param address - address to deregister
     * @param port - port to deregister
     * @throws IllegalArgumentException - if any value fails validation
     */
    public NoTiFiRegister(int msgId, Inet4Address address, int port) throws IllegalArgumentException {
        super(msgId);
        checkPort(port);
        checkAddress(address);
        this.address=address;
        this.port=port;
    }
    /**
     * Get (de)register address
     * 
     * @return (de)register address
     */
    public Inet4Address getAddress() {
        return address;
    }
    
    /**
     * get SocketAddress by using ip address and port
     * @return SocketAddress
     */
    public InetSocketAddress getSocketAddress() { 
        return new InetSocketAddress(address,port);
    } 
    
    /**
     * Get (de)register port
     * 
     * @return (de)register port
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Set (de)register address
     * 
     * @param address (de)register address
     * @throws IllegalArgumentException - if any value fails validation
     */
    public void setAddress(Inet4Address address) throws IllegalArgumentException {
        checkAddress(address);
        this.address=address;
    }
    /**
     * Set (de)register port
     * 
     * @param port - (de)register port
     * @throws IllegalArgumentException if port is out of range (0...65535)
     */
    public void setPort(int port) throws IllegalArgumentException {
        checkPort(port);
        this.port = port;
    }
    
    /**
     * Get operation code
     * 
     * @return - operation code
     */
    @Override
    public int getCode() {
        return code;
    }
    /**
     * toString in class NoTiFiMessage
     * 
     * @return human readable result string
     * */
    @Override
    public String toString () {
        return "IP Address: "+address+" Port: "+port;
    }
    /**
     * impliment abstract function from parent class,
     * encode version and code info
     * 
     * @return Serializes message for header
     */
    @Override
    public byte[] encodeHeader() {
        byte [] b= new byte[1];
        b[0]=Parser.appendBit(ConstVal.version, code);
        return b;
    }
    
    /**
     * impliment abstract function from parent class, encode data.
     * Type of data depends on code
     * 
     * @return - Serializes message for data
     * @throws IOException - if I/O problem
     */
    @Override
    public byte[] encodeHelper() throws IOException  {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write( Parser.changeOrder(address.getAddress()) );
        outputStream.write( Parser.intToByte(port, 2) );

        return outputStream.toByteArray( );
    }
    
    @Override
    public boolean equals(Object obj){
        if( !(obj instanceof NoTiFiRegister) ) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if (! super.equals(obj)) return false;
        return ((NoTiFiRegister)obj).port == port 
                && ((NoTiFiRegister)obj).code == code
                &&((NoTiFiRegister)obj).address.equals(address);
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(code, port,address);
    }
}
