package myn.notifi.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class NoTiFiError extends NoTiFiMessage {
    private final String errorMsg = "Invalid error message";
    private final int code=ConstVal.error;
    private String errorMessage;
    
    private void checkMsg (String msg) throws IllegalArgumentException {
        if(msg == null) {
            throw new IllegalArgumentException (errorMsg);
        }
    }
    
    public NoTiFiError(DataInputStream in) throws IllegalArgumentException, IOException{
        
    }
    public NoTiFiError(int msgId,String errorMessage) throws IllegalArgumentException {
        super(msgId);
        checkMsg(errorMessage);
        this.errorMessage=errorMessage;
    }
    public NoTiFiError(DataInputStream in,int msgId,int version)
            throws IOException, IllegalArgumentException {
        super(msgId);
        this.version=version;
        errorMessage=Parser.readError(in);
    }
    @Override
    public int getCode() {
        return code;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) throws IllegalArgumentException {
        checkMsg(errorMessage);
        this.errorMessage=errorMessage;
    }
    @Override
    public String toString() {
        return null;
    }
    
    @Override
    public byte encodeHeader() {
        return Parser.appendBit(ConstVal.version, code);
    }
    @Override
    public byte[] encodeHelper() {
        return errorMessage.getBytes();
    } 
}

