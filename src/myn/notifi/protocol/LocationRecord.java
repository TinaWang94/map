package myn.notifi.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;




public class LocationRecord {
    private int userId;
    private double longitude;
    private double latitude;
    private String locationName;
    private String locationDescription;
    private final String errorMsg = "Invalid id";
    private final String errorMsg3 = "Invalid String value";
    
    private void checkId(int id) throws IllegalArgumentException  {
        if(id < 0) {
            throw new IllegalArgumentException(errorMsg);
        }
    }
    
    private void checkString(String str) throws IllegalArgumentException {
        if(str == null) {
            throw new IllegalArgumentException(errorMsg3);
        }

    }
   
    public LocationRecord(DataInput in) throws IOException, IllegalArgumentException {
        
        short temp = in.readShort();
        checkId(temp);
        userId = temp;
        longitude =  Parser.readDouble(in, ConstVal.numDou);
        latitude=Parser.readDouble(in, ConstVal.numDou);
        int length = Parser.readInt(in,1);
        locationName=Parser.readString(in, length);
        length=Parser.readInt(in, 1);
        locationDescription = Parser.readString(in, length);
    }
    
    public LocationRecord(int userId, double longitude, double latitude,
            String locationName, String locationDescription) throws IllegalArgumentException {
        checkId(userId);
        checkString(locationName);
        checkString(locationDescription);
        
        this.userId=userId;
        this.longitude=longitude;
        this.latitude=latitude;
        this.locationName=locationName;
        this.locationDescription=locationDescription;
        
    }
    public void encode(DataOutput out) throws IOException {
        out.writeShort((short)userId);
        Parser.writeDouble(out, longitude, ConstVal.numDou);
        Parser.writeDouble(out, latitude, ConstVal.numDou);
        byte temp = (byte)locationName.length();
        Parser.writeInt(out, temp, 1);
        Parser.writeString(out, locationName);
        temp=(byte)locationDescription.length();
        Parser.writeInt(out,temp,1);
        Parser.writeString(out, locationDescription);
        
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LocationRecord)) {
            return false;
        }
        if(obj ==this) {
            return true;
        }
        return ((LocationRecord)obj).userId == userId 
                &&  ((LocationRecord)obj).latitude==latitude
                && ((LocationRecord)obj).longitude==longitude
                && ((LocationRecord)obj).locationName.compareTo(locationName) == 0
                && ((LocationRecord)obj).locationDescription.compareTo(locationDescription) == 0;
    }
    public double getLatitude() {
        return latitude;
    }
    public String getLocationDescription() {
        return locationDescription;
    }
    public String getLocationName() {
        return locationName;
    }
    public double getLongitude() {
        return longitude;
    }
    public int getUserId() {
        return userId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId,latitude,longitude,
                locationName,locationDescription);
    }
    public void setLatitude(double latitude) {
        this.latitude=latitude;
    }
    public void setLocationDescription(String locationDescription) throws IllegalArgumentException {
        checkString(locationDescription);
        this.locationDescription = locationDescription;
    }
    public void setLocationName(String locationName) throws IllegalArgumentException {
        checkString(locationName);
        this.locationName = locationName;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setUserId(int userId) throws IllegalArgumentException {
        checkId(userId);
        this.userId = userId;
    }
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        
        result.append("User "+userId+":");
        result.append(locationName+" - ");
        result.append(locationDescription);
        result.append(" at (");
        result.append(longitude+", ");
        result.append(latitude+")\r\n");     
        return result.toString();
    } 
} 
