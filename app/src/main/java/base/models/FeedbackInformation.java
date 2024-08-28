package base.models;

/**
 * Created by ayu on 05/09/2017.
 */

public class FeedbackInformation {
    public FeedbackInformation() {
        // Via = new ViaAddresses();
    }


    public boolean IsFeedback;
    public int Rating;
    public String hashKey;
    public String defaultclientId;
    public Stripe_Model CardDetail;
    public String ClientName;
    public int BookingNumber = 0;
    public String BookingReference;
    public String Email;
    public String Location;
    public String Title;
    public double Tip;
    public String Message;
}

