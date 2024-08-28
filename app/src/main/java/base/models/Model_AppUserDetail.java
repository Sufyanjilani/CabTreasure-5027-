package base.models;

public class Model_AppUserDetail {

    private String TokenDetails;
    private boolean IsDefault;
    private long RecordId;

    private long DefaultRecordId;
    private String DefaultTokenDetails;

    private String PickDetails;
    private String Code;
    private String PhoneNo;

    private String CustomerName;

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    private String UserName;
    private String Email;
    private String SendSMS;
    private String Address;
    private String Telephone;


    private String Passwrd;
    private String NewPassword;

    private long defaultClientId;
    private String uniqueValue;

    private String UniqueId;
    private String DeviceInfo;
    private String JobId;
    private String DriverId;

    private String LostTitle;
    private String LostDescription;

    private String NotifyToken;
    private String CustomerId;
    private long PromotionId;
    private String PromotionCode;
    private int PromotionTypeId;
    private String FromAddress;
    private String FromType;
    private String ToAddress;
    private String ToType;


    public String getTokenDetails() {
        return TokenDetails;
    }

    public void setTokenDetails(String tokenDetails) {
        TokenDetails = tokenDetails;
    }

    public boolean isDefault() {
        return IsDefault;
    }

    public void setDefault(boolean aDefault) {
        IsDefault = aDefault;
    }

    public long getRecordId() {
        return RecordId;
    }

    public void setRecordId(long recordId) {
        RecordId = recordId;
    }

    public long getDefaultRecordId() {
        return DefaultRecordId;
    }

    public void setDefaultRecordId(long defaultRecordId) {
        DefaultRecordId = defaultRecordId;
    }

    public String getDefaultTokenDetails() {
        return DefaultTokenDetails;
    }

    public void setDefaultTokenDetails(String defaultTokenDetails) {
        DefaultTokenDetails = defaultTokenDetails;
    }

    public String getPickDetails() {
        return PickDetails;
    }

    public void setPickDetails(String pickDetails) {
        PickDetails = pickDetails;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSendSMS() {
        return SendSMS;
    }

    public void setSendSMS(String sendSMS) {
        SendSMS = sendSMS;
    }

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

    public String getPasswrd() {
        return Passwrd;
    }

    public void setPasswrd(String passwrd) {
        Passwrd = passwrd;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public long getDefaultClientId() {
        return defaultClientId;
    }

    public void setDefaultClientId(long defaultClientId) {
        this.defaultClientId = defaultClientId;
    }

    public String getUniqueValue() {
        return uniqueValue;
    }

    public void setUniqueValue(String uniqueValue) {
        this.uniqueValue = uniqueValue;
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public String getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        DeviceInfo = deviceInfo;
    }

    public String getJobId() {
        return JobId;
    }

    public void setJobId(String jobId) {
        JobId = jobId;
    }

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public String getLostTitle() {
        return LostTitle;
    }

    public void setLostTitle(String lostTitle) {
        LostTitle = lostTitle;
    }

    public String getLostDescription() {
        return LostDescription;
    }

    public void setLostDescription(String lostDescription) {
        LostDescription = lostDescription;
    }

    public String getNotifyToken() {
        return NotifyToken;
    }

    public void setNotifyToken(String notifyToken) {
        NotifyToken = notifyToken;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public long getPromotionId() {
        return PromotionId;
    }

    public void setPromotionId(long promotionId) {
        PromotionId = promotionId;
    }

    public String getPromotionCode() {
        return PromotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        PromotionCode = promotionCode;
    }

    public int getPromotionTypeId() {
        return PromotionTypeId;
    }

    public void setPromotionTypeId(int promotionTypeId) {
        PromotionTypeId = promotionTypeId;
    }

    public String getFromAddress() {
        return FromAddress;
    }

    public void setFromAddress(String fromAddress) {
        FromAddress = fromAddress;
    }

    public String getFromType() {
        return FromType;
    }

    public void setFromType(String fromType) {
        FromType = fromType;
    }

    public String getToAddress() {
        return ToAddress;
    }

    public void setToAddress(String toAddress) {
        ToAddress = toAddress;
    }

    public String getToType() {
        return ToType;
    }

    public void setToType(String toType) {
        ToType = toType;
    }
}
