package myn.notifi.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class NoTiFiLocationDeletion extends NoTiFiMessage{
    private final String errorMsg = "Invalid locationRecord.";
    private final int code=ConstVal.del;
    
    private LocationRecord locationRecord;
    
    private void checkLR(LocationRecord lr) throws IllegalArgumentException {
        if(locationRecord == null)
            throw new IllegalArgumentException(errorMsg);
    }
    
    public NoTiFiLocationDeletion(DataInputStream in) throws IllegalArgumentException, IOException{
        
    }
    public NoTiFiLocationDeletion(int msgId, LocationRecord locationRecord) throws IllegalArgumentException {
        super(msgId);

        checkLR(locationRecord);
        this.locationRecord=locationRecord;
    }
    public NoTiFiLocationDeletion(DataInputStream in,int msgId, int version) throws IOException, IllegalArgumentException {
        super(msgId);
        this.version=version;
        locationRecord = new LocationRecord(in);
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
        this.locationRecord=locationRecord;
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
        // TODO Auto-generated method stub
        return null;
    }
}
