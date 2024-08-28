package base.models;

public class SettingsModel {

    public static final String KEY_SETTINGS_MODEL = "SettingsModelKey";

    public static String UName = "";
    private String Name = "";
    private String lName = "";
    private String Email = "";
    private String Address = "";
    private String Miles = "";
    private String Mobile = "";
    private String VehicleType = "";
    private String PaymentType = "Cash";
    private String LoginID = "", Password = "";
    private String AccountNo = "";
    private boolean Verified = false;
    private String AccountWebID = "";
    private String PromoCode = "";
    private String userServerID = "";
    private String defaultclientId = "";
    private String DeviceInfo = "";
    private String uniqueId = "";
    private String uniqueValue = "";
    private String PhoneNo = "";
    private String SubCompanyId = "";

    public static String getUName() {
        return UName;
    }

    public static void setUName(String UName) {
        SettingsModel.UName = UName;
    }

    public String getMiles() {
        return Miles;
    }

    public void setMiles(String miles) {
        Miles = miles;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getDefaultclientId() {
        return defaultclientId;
    }

    public void setDefaultclientId(String defaultclientId) {
        this.defaultclientId = defaultclientId;
    }


    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getSubCompanyId() {
        return SubCompanyId;
    }

    public void setSubCompanyId(String subCompanyId) {
        SubCompanyId = subCompanyId;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getUniqueValue() {
        return uniqueValue;
    }

    public void setUniqueValue(String uniqueValue) {
        this.uniqueValue = uniqueValue;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        DeviceInfo = deviceInfo;
    }

    public void _setDefaultclientId(String defaultclientId) {
        this.defaultclientId = defaultclientId;
    }

    public String _getDefaultclientId() {
        return defaultclientId;
    }

    public SettingsModel() {
    }

    public String getAccountWebID() {
        return AccountWebID;
    }

    public void setAccountWebID(String AccountWebID) {
        this.AccountWebID = AccountWebID;
    }

    public boolean isVerified() {
        return Verified;
    }

    public void setVerified(boolean arg) {
        this.Verified = arg;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String arg) {
        this.Address = arg;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String arg) {
        this.PaymentType = arg;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String arg) {
        this.VehicleType = arg;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String arg) {
        this.AccountNo = arg;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String arg) {
        this.LoginID = arg;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String arg) {
        this.Password = arg;
    }

    public String getPromoCode() {
        return PromoCode;
    }

    public void setPromoCode(String arg) {
        this.PromoCode = arg;
    }

    public String getUserServerID() {
        return userServerID;
    }

    public void setUserServerID(String arg) {
        this.userServerID = arg;
    }
}
