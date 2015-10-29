package myn.notifi.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class NoTiFiDeregister extends NoTiFiMessage{
    private final int code=ConstVal.deReg;
    private final String errorMsg = "Invalid address";
    
    private int port;
    private Inet4Address address;
    
    private void checkAddress (Inet4Address addr) throws IllegalArgumentException {
        if(addr == null) {
            throw new IllegalArgumentException (errorMsg);
        }
    }
    public NoTiFiDeregister(DataInputStream in) throws IllegalArgumentException, IOException{
        
    }
    public NoTiFiDeregister(int msgId, Inet4Address address, int port) throws IllegalArgumentException {
        super(msgId);
        checkId(port);
        checkAddress(address);
        this.address = address;
        this.port=port;
    }
    
    public NoTiFiDeregister(DataInputStream in,int msgId, int version) 
            throws UnknownHostException, IOException, IllegalArgumentException {
        super(msgId);
        this.version=version;
       //address is 4 bytes      
        address=(Inet4Address)InetAddress.getByAddress(Parser.readAddr(in));
        //port is 2 bytes
        int temp;
        temp=Parser.readInt(in, 2);
        checkId(temp);
        port = temp;
    }
    
    public Inet4Address getAddress() {
        return address;
    }
    @Override
    public int getCode() {
        return code;
    }
    public int getPort() {
        return port;
    }
    public InetSocketAddress getSocketAddress() {
        return new InetSocketAddress(address,port);
    } 
    public void setAddress(Inet4Address address) throws IllegalArgumentException {
        checkAddress(address);
        this.address = address;
    }
    public void setPort(int port) throws IllegalArgumentException {
        checkId(port);
        this.port = port;
    }
    @Override
    public String  toString() {
        return null;
    }
    @Override
    public byte encodeHeader() {
        return Parser.appendBit(ConstVal.version, code);
    }
    
    @Override
    public byte[] encodeHelper() throws IOException {
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write( Parser.changeOrder(address.getAddress()) );
        outputStream.write( Parser.intToByte(port, 2) );

        return outputStream.toByteArray( );
    }
}
