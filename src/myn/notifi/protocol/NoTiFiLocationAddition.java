package myn.notifi.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

public class NoTiFiLocationAddition extends NoTiFiMessage{
    private final String errorMsg = "Invalid locationRecord.";
    private final int code=ConstVal.add;
    
    private LocationRecord locationRecord;
    
    
    private void checkLR(LocationRecord lr) throws IllegalArgumentException {
        if(locationRecord == null)
            throw new IllegalArgumentException(errorMsg);
    }
    
    public NoTiFiLocationAddition(DataInputStream in) throws IllegalArgumentException, IOException{
        
    }
    public NoTiFiLocationAddition(int msgId, LocationRecord locationRecord) 
            throws IllegalArgumentException{
        super(msgId);
        checkLR(locationRecord);
        this.locationRecord=locationRecord;
    }
    
    public NoTiFiLocationAddition(DataInputStream in,int msgId, int version)
            throws IOException, IllegalArgumentException{
        super(msgId);
        this.version=version;
        locationRecord=new LocationRecord(in);
    }
    
    @Override
    public int getCode() {
        return code;
    }
    public LocationRecord getLocationRecord() {
        return locationRecord;
    }
    public void setLocation(LocationRecord locationRecord) throws IllegalArgumentException {
        checkLR(locationRecord);
        this.locationRecord = locationRecord;
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
    public byte[] encodeHelper() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DataOutput out = new DataOutputStream(outStream);
        locationRecord.encode(out);
        return outStream.toByteArray();
    } 
}
