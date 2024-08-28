package base.newui;

/**
 * Created by ayu on 04/04/2017.
 */
public class Driver {
    private String Driverid = "";
    private String DriveNo = "";
    private String nearest = "";
    // Boolean nearest;
    private  double  longitude;
    private  double  latitude;

    public String getDriverid() {
        return Driverid;
    }

    public void setDriverid(String driverid) {
        Driverid = driverid;
    }

    public String getDriveNo() {
        return DriveNo;
    }

    public void setDriveNo(String driveNo) {
        DriveNo = driveNo;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getNearest() {
        return nearest;
    }

    public void setNearest(String nearest) {
        this.nearest = nearest;
    }
}
