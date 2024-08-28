package base.models;

/**
 * Created by ayu on 05/09/2017.
 */

public class CardInformation {
    public CardInformation() {
        // Via = new ViaAddresses();
    }

    public double amount;
    public String cardNumber;
    public String expiryDate;
    public String expiryMonth;
    public String expiryYear;
    public String cv2;
    public String currency;
    public String mobileNumber;
    public String emailAddress;
    public boolean TestMode = true;
    public String paymentgateway;
    public String merchantid;
    public String merchantpassword;
    public String signature;
}

