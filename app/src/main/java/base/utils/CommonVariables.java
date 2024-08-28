package base.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;


import com.eurosoft.customerapp.BuildConfig;
import com.eurosoft.customerapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import base.fragments.Fragment_Main;

public class CommonVariables {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final int JUDO_REGISTER_REQUEST = 1131;
    public static final int KONNECT_PAY_REQUEST = 113332;
    public static Fragment_Main AppMainActivity = null;

    public static final int REDIRECTED_SERVERPORT = 1101;
  public static final String BASE_URL = "https://www.treasureonlineapi.co.uk/CabTreasureWebApi/Home/";
 // public static final String BASE_URL = "https://cabtreasureappapi.co.uk/CabTreasureWebApi/Home/";

   // public static final String WEB_DISPATCH_BASE_URL = CLIENT_URL + "/api/Supplier/";


    public static final String USerLogin = "Userlogin";
    public static final String ISAuthorized = "ISAuthorized";
    public static final String ISUSERLOGIN = "IsUserlogin";
    public static String TOKEN = "";

    public static final String Restrict_Vehicle = "restrictVehicle";
    public static final String Restrict_Message = "restrictMessage";

    public static String Clientip = "";

    public static String SUB_COMPANY = "1";
    public static final String MEMBERACCNAME = "MEMBERACCNAME";
    public static final String MEMBERACCEMAIL = "MEMBERACCEMAIL";
    public static String GOOGLE_API_KEY = "";
    public static String COUNTRY = "GB";
    public static String Currency = "GB";
    public static final long clientid = BuildConfig.clientid; // 89
    public static final long localid = BuildConfig.clientid; // 209 for Testing local system
    public static final String paymentType = "selectedPaymentType";


    public static final String distanceUnit = "distanceUnit";
    public static final String enableOutsideUK = "enableOutsideUK";
    public static final String IS_WEB_DISPATCH = "isWebDispatch";
    public static final String ENABLE_DELETE_Account = "enable_deleteaccount";
    public static final String EnableGoogleForSuggestion = "enableGoogle";

    public static final String EnablePayAfterBookingClearOnStrip = "EnablePayAfterBookingClearOnStrip";

    public static final String EnableMeetAndGreet = "EnableMeetAndGreet";
    public static final String CurrencySymbol = "CurrencySymbol";

    //23-June-2016


/*    public static final String Stripe_PK = "Stripe_PK";
    public static final String Stripe_SK = "Stripe_SK";*/

    public static final String enableSignup = "enableSignup";
    public static final String EnableReceipt = "EnableReceipt";
    public static final String enableVia = "isVia";

    public static final String DEVICE_TYPE = "Android";

    public static final String Enable_ForeGround_Service = "Enable_ForeGround_Service";
    public static final String refresh_booking_action = "refresh_booking";

    //24-June-2016
    public static final String ISMEMBERUSERLOGIN = "IsMemberUserlogin";
//    public static final String PP_SANDBOX_Client_ID = BuildConfig.pp_sandbox_client_id;
//    public static final String PP_LIVE_Client_ID = BuildConfig.pp_live_client_id;

    public static final int SERVICE = 2;

    public static final String ANY_VEHICLE_TYPE = "Any Vehicle Type";

    public static final int JOURNY_ONEWAY = 1;
    public static final int JOURNY_RETURN = 2;
    public static final int JOURNY_TYPE = 1;

    public static int Timestatus = 0;
    // user settings key
    // active
    public static final String STATUS_WAITING = "Waiting";
    public static final String STATUS_CONFIRMED = "Confirmed";
    public static final String STATUS_STC = "STC";
    public static final String STATUS_STC2 = "SoonToClear";
    public static final String STATUS_POB = "POB";
    public static final String STATUS_POB2 = "passengeronboard";
    public static final String STATUS_ONROUTE = "OnRoute";
    public static final String STATUS_ARRIVED = "Arrived";

    public static final String STATUS_CANCLED = "Cancelled";
    public static final String STATUS_COMPLETED = "Completed";
    // future

    public static final String KEY_BOOKING_MODEL = "keyReviewBundle";
    public static final String KEY_REVIEW_ONLY = "keyReviewOnly";
    public static final String KEY_NEW_BOOKING = "false";


    public static final String EnableTip = "EnableTip";
    public static final String EnableLostItemInquiry = "EnableLostItemInquiry";
    public static final String EnableComplain = "EnableComplain";

    public static final int FORMAT_DATE_TIME = 0x00000003;
    public static final int FORMAT_DATE = 0x00000002;
    public static final int FORMAT_TIME = 0x00000001;

    public static final String getDateFormat(int flag) {
        switch (flag) {

            case FORMAT_DATE_TIME:
                return "dd-MMM-yyyy HH:mm";
            case FORMAT_DATE:
                return "dd-MMM-yyyy";
            case FORMAT_TIME:
                return "HH:mm";
            default:
                return "dd-MMM-yyyy HH:mm";
        }

    }

    public static final String getFormattedDate(Calendar cal, int flag) {
        SimpleDateFormat format = new SimpleDateFormat(CommonVariables.getDateFormat(flag), Locale.getDefault());
        return format.format(cal.getTime());
    }

    public static final String getFormattedDateStr(Calendar cal, String flag) {
        SimpleDateFormat format = new SimpleDateFormat(flag, Locale.getDefault());
        return format.format(cal.getTime());
    }

    public static final void setFont(Context context, View v, FontType type) {

        if (v != null && v instanceof TextView) {

            TextView tv = (TextView) v;
            Typeface tf;
            switch (type) {
                case Bold:
                    tf = Typeface.createFromAsset(context.getAssets(), "font/PT_Sans-Web-Bold.ttf");
                    break;
                case BoldItalic:
                    tf = Typeface.createFromAsset(context.getAssets(), "font/PT_Sans-Web-BoldItalic.ttf");
                    break;
                case Italic:
                    tf = Typeface.createFromAsset(context.getAssets(), "font/PT_Sans-Web-Italic.ttf");
                    break;
                case Regular:
                    tf = Typeface.createFromAsset(context.getAssets(), "font/PT_Sans-Web-Regular.ttf");
                    break;
                default:
                    tf = Typeface.createFromAsset(context.getAssets(), "font/PT_Sans-Web-Regular.ttf");
                    break;
            }
            tv.setTypeface(tf);
        }
    }

    public enum FontType {
        Bold, BoldItalic, Italic, Regular
    }

}
