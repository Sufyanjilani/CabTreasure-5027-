package base.models;

/**
 * Created by ayu on 05/09/2017.
 */

public class ConfirmedBooking {
    public String miles;

    public ConfirmedBooking() {
        // Via = new ViaAddresses();
    }
    public String Version="";
    public String IsQuoted = "0";
    public String fromLatLng;
    public String toLatLng;
    public double ExtraCharges = 0;
    public double AgentFees = 0;
    public double AgentCharge = 0;
    public double Congestion = 0;
    public double Parking = 0;
    public double Waiting = 0;
    public double BookingFee = 0;
    public double AgentCommission = 0;
    public String CardDetails = "";
    public String PostedFrom = "Android";

    public double CompanyPrice = 0;
    public String ASAPBooking = "";


    //    ShareTracking obj = new ShareTracking();
//    obj.defaultClientId =(int)CommonVariables.clientid;
//    obj.uniqueValue = CommonVariables.clientid + HASHKEY_VALUE;
//    obj.UniqueId = deviceid;
//    obj.DeviceInfo = "Android";
//    obj.CustomerId = settingsModel.getUserServerID();
//    obj.Email=settingsModel.getEmail();
//    obj.PromotionCode=params[0];
//    obj.FromAddress=params[1];
//    obj.FromType=params[2];
//    obj.ToAddress=params[3];
//    obj.ToType=params[4];
    public int defaultClientId;
    public String Email;
    public String uniqueValue;
    public String PromotionCode;
    public String UniqueId;
    public String CustomerId;
    public String DeviceInfo;
    public String pickupDate;
    public String pickupTime;
    public String returnDate;
    public String returnTime;
    public String fromAddress;
    public String promocode = "";
    public String toAddress;
    public String customerName;
    public String customerPhoneNo;
    public String discountedFare;
    public String customerMobileNo;
    public String customerEmail;
    public String vehicleName;
    public String PromotionDetails;
    public String PaymentType;
    public String specialInstruction;
    public int journeyType;
    public String fareRate;
    public String returnFareRate;
    public String fromDoorNo;
    public String fromComing;
    public String toDoorNo;
    public String viaAddress;
    public String accountType;
    public String accountId;
    public String fromLocType;
    public String toLocType;
    public String transactionId;
    public String StripeCustomerId;
    public String key;
    public String PromotionId;
    //    public String PromotionCode;
    public String CreditCardSurcharge;
    public String PromotionTypeID;
    //    public String CustomerId;
    public String SubcompanyId;
    public String AttributesList;
    public String IsKonnectPay;
    public String HoldCardPayment;





}

