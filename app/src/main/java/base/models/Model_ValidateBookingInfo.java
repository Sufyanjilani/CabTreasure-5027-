package base.models;

public class Model_ValidateBookingInfo {

    public Model_ValidateBookingInfo(String bookingTypeId, String subCompanyId, String pickupDate, String pickupTime, String fromAddress, String toAddress, String fromLatLng, String toLatLng, String uniqueValue, String defaultClientId) {
        this.bookingTypeId = bookingTypeId;
        SubCompanyId = subCompanyId;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.fromLatLng = fromLatLng;
        this.toLatLng = toLatLng;
        this.uniqueValue = uniqueValue;
        this.defaultClientId = defaultClientId;
    }

    public String getUniqueValue() {
        return uniqueValue;
    }

    public void setUniqueValue(String uniqueValue) {
        this.uniqueValue = uniqueValue;
    }

    public String getDefaultClientId() {
        return defaultClientId;
    }

    public void setDefaultClientId(String defaultClientId) {
        this.defaultClientId = defaultClientId;
    }

    private String bookingTypeId;
    private String SubCompanyId;
    private String pickupDate;
    private String pickupTime;
    private String fromAddress;
    private String toAddress;
    public String fromLatLng;
    public String toLatLng;
    public String uniqueValue;
    public String defaultClientId;

//    public Model_ValidateBookingInfo(String bookingTypeId, String subCompanyId, String pickupDate, String pickupTime, String fromAddress, String toAddress, String fromLatLng, String toLatLng) {
//        this.bookingTypeId = bookingTypeId;
//        SubCompanyId = subCompanyId;
//        this.pickupDate = pickupDate;
//        this.pickupTime = pickupTime;
//        this.fromAddress = fromAddress;
//        this.toAddress = toAddress;
//        this.fromLatLng = fromLatLng;
//        this.toLatLng = toLatLng;
//    }


    public String getFromLatLng() {
        return fromLatLng;
    }

    public String getToLatLng() {
        return toLatLng;
    }

    public void setToLatLng(String toLatLng) {
        this.toLatLng = toLatLng;
    }

    public void setFromLatLng(String fromLatLng) {
        this.fromLatLng = fromLatLng;
    }
    public String getBookingTypeId() {
        return bookingTypeId;
    }

    public void setBookingTypeId(String bookingTypeId) {
        this.bookingTypeId = bookingTypeId;
    }

    public String getSubCompanyId() {
        return SubCompanyId;
    }

    public void setSubCompanyId(String subCompanyId) {
        SubCompanyId = subCompanyId;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }
}
