package base.models;

public class User {
    private String defaultClientId = "";
    private String hashKey = "";
    private String uniqueValue = "";
    private String UniqueId = "";
    private String PhoneNo = "";
    private String UserName = "";
    private String Passwrd = "";
    private String NewPassword = "";
    private String Email = "";
    private String PickDetails = "";
    private String DeviceInfo = "";
    private String SubCompanyId = "";
    private String SendSMS = "";
    private String Code = "";
    private String CustomerId = "";
    private String Address = "";
    private String Telephone = "";

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getCode() {
        return Code;
    }

    public String getSendSMS() {
        return SendSMS;
    }

    public void setSendSMS(String sendSMS) {
        SendSMS = sendSMS;
    }

    public String getSubCompanyId() {
        return SubCompanyId;
    }

    public void setSubCompanyId(String subCompanyId) {
        SubCompanyId = subCompanyId;
    }

    public String getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        DeviceInfo = deviceInfo;
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
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

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPasswrd() {
        return Passwrd;
    }

    public void setPasswrd(String passwrd) {
        Passwrd = passwrd;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPickDetails() {
        return PickDetails;
    }

    public void setPickDetails(String pickDetails) {
        PickDetails = pickDetails;
    }
}
