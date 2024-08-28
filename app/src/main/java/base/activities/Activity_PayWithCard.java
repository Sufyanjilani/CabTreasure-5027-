package base.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.DataLoadListner;
import com.craftman.cardform.OnPayBtnClickListner;*/
import com.eurosoft.customerapp.R;
import com.google.gson.Gson;

import com.tfb.fbtoast.FBToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

import base.models.CardInformation;
import base.models.Model_BookingDetailsModel;
import base.models.Model_CardDetails;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

//import static base.newui.ReviewBooking.KEY_BOOKING_MODEL;


public class Activity_PayWithCard extends AppCompatActivity {
//    CardForm cardForm;
//    boolean isPayment = false;
//    SharedPrefrenceHelper sharedPrefrenceHelper;
//    boolean startup = false;
//    Model_BookingDetailsModel mBookingModel;
//    Card paymentCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_card);
//        sharedPrefrenceHelper = new SharedPrefrenceHelper(Activity_PayWithCard.this);
//        Model_CardDetails modelCardDetails = sharedPrefrenceHelper.getCardModel();
//        if (getIntent() != null && getIntent().hasExtra("payment")) {
//            isPayment = true;
//        }
//        cardForm = (CardForm) findViewById(R.id.card_form);
//        if (getIntent() != null) {
//            mBookingModel = (Model_BookingDetailsModel) getIntent().getSerializableExtra(CommonVariables.KEY_BOOKING_MODEL);
//
//            cardForm.setServerUseId(sharedPrefrenceHelper.getSettingModel().getUserServerID());
//            cardForm.setAmount(mBookingModel.getOneWayFare());
//
//        }
//
//
////        CardForm cardForm
//        if (modelCardDetails != null && !modelCardDetails.getCardNumber().equals("")) {
//            Card card = new Card(new Card.Builder(
//                    modelCardDetails.getCardNumber(),
//                    Integer.parseInt(modelCardDetails.getExpiryMonth()),
//                    Integer.parseInt(modelCardDetails.getExpiryYear()),
//                    modelCardDetails.getCVV()
//
//            ));
//        }
//
//
//        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
//            @Override
//            public void onClick(Card card) {
//                if (card != null) {
//                    paymentCard = card;
//                    String cardNo = card.getNumber();
//                    String cardName = card.getName();
//                    String ExpYear = String.valueOf(card.getExpYear());
//                    int ExtMonth = card.getExpMonth() == null ? 0 : card.getExpMonth();
//                    String Cvc = card.getCVC();
//
//                    if (!cardNo.equals("") && card.getExpYear() > 0 && ExtMonth > 0 && !Cvc.equals("")) {
//
//                        new SubmitPayment().execute(cardNo, Cvc, "" + ExtMonth, ExpYear.substring(ExpYear.length() - 2, ExpYear.length()), mBookingModel.getOneWayFare());
////                        new SubmitPayment().execute();
//
//                    } else {
//                        FBToast.errorToast(Activity_PayWithCard.this, "Please enter complete card details", Toast.LENGTH_SHORT);
//                    }
//                } else {
//
//                    FBToast.infoToast(Activity_PayWithCard.this, "Please enter card details", FBToast.LENGTH_SHORT);
////                    Animatoo.animateSlideLeft(Activity_PayWithCard.this);
////                    finish();
//                }
//            }
//        });
    }

//    DataLoadListner dataLoadListner;

//    public void setDataLoadListner(DataLoadListner onPayBtnClickListner) {
//        this.dataLoadListner = onPayBtnClickListner;
//    }

    public void goBack(View v) {

//            startActivity(new Intent(this, Mai.class));

      //  Animatoo.animateSlideRight(this);

        finish();
    }

/*    public class SubmitPayment extends AsyncTask<String, Void, String> {

        private String METHOD_NAME_ALL = "MakePaymentByGateway";

        private static final String KEY_DEFAULT_CLIENT_ID = "defaultClientId";
        private static final String KEY_VEHICLE_NAME = "vehicleName";
        private static final String KEY_FROM_ADDRESS = "fromAddresss";
        private static final String KEY_TO_ADDRESS = "toAddresss";
        private static final String KEY_FROM_TYPE = "fromType";
        private static final String KEY_TO_TYPE = "toType";
        private static final String KEY_VIA_POINTS = "viaPoints";
        private static final String KEY_MILES = "miles";
        private static final String KEY_HASHKEY = "dataVal";
        private static final String Fare_TYPE = "fareType";
        private static final String Booking_information = "jsonString";
        private static final String HASHKEY_VALUE = "4321orue";

        private SweetAlertDialog mDialog;


        public SubmitPayment() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mDialog = new SweetAlertDialog(Activity_PayWithCard.this, SweetAlertDialog.PROGRESS_TYPE);
                mDialog.setTitleText("Processing");
                mDialog.setContentText("Please Wait...");
                mDialog.setCancelable(false);
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null && !result.isEmpty()) {
                try {

                    Calendar calendar = Calendar.getInstance();
                    Log.e("Payment", result);

                    //  mBookingModel.setPickUpDate(CommonVariables.getFormattedDate(calendar, CommonVariables.FORMAT_DATE));
                    JSONObject json = new JSONObject(result);


                    String status = json.getString("success");
                    String transId = json.getString("authcode");
                    String message = json.getString("message");
                    if (status.equalsIgnoreCase("true")) {


//                        JSONObject parentData=json.getJSONObject("ResponseData");
//                        String transId=parentData.getString("Transaction_Id");
                        Intent intent = new Intent();
                        intent.putExtra("TransId", transId);
//                        intent.putS
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        FBToast.errorToast(Activity_PayWithCard.this, message, FBToast.LENGTH_SHORT);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    if (e instanceof JSONException) {

                        FBToast.errorToast(Activity_PayWithCard.this, "Unable to retrieve data from server.\nError: " + result, FBToast.LENGTH_SHORT);
                        Log.d("Server Error", result);
                    } else {

                        FBToast.errorToast(Activity_PayWithCard.this, "Something went wrong,  Please try again later", FBToast.LENGTH_LONG);
                    }
                }
            } else
                FBToast.errorToast(Activity_PayWithCard.this, "Problems in getting data. Please check your internet connection", FBToast.LENGTH_SHORT);

            if (mDialog != null)

                mDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                CardInformation obj = new CardInformation();
                obj.cardNumber = String.valueOf(params[0]);
                obj.cv2 = String.valueOf(params[1]);
                obj.expiryMonth = String.valueOf(params[2]);
                obj.expiryYear = String.valueOf(params[3]);
                obj.amount = Double.parseDouble(params[4]);
                obj.paymentgateway = Config.Stripe;

                final String json_String = new Gson().toJson(obj);
                SoapHelper.Builder builder = new SoapHelper.Builder(CommonVariables.SERVICE, getApplicationContext());
                builder
                        .setMethodName("MakePaymentByGateway", true)
                        .addProperty(KEY_DEFAULT_CLIENT_ID, CommonVariables.clientid, PropertyInfo.LONG_CLASS)
//                        .addProperty(KEY_DEFAULT_CLIENT_ID, 166, PropertyInfo.LONG_CLASS)
//                        .addProperty(KEY_VEHICLE_NAME, params[0][0], PropertyInfo.STRING_CLASS)
//                        .addProperty(KEY_FROM_ADDRESS, params[0][1], PropertyInfo.STRING_CLASS)
//                        .addProperty(KEY_TO_ADDRESS, params[0][2], PropertyInfo.STRING_CLASS)
//                        .addProperty(KEY_FROM_TYPE, params[0][3], PropertyInfo.STRING_CLASS).addProperty(KEY_TO_TYPE, params[0][4], PropertyInfo.STRING_CLASS)
//                        .addProperty(KEY_VIA_POINTS, params[0][5], PropertyInfo.STRING_CLASS)
                        //    .addProperty(KEY_MILES, params[0][6], PropertyInfo.STRING_CLASS)
                        .addProperty(Booking_information, json_String, PropertyInfo.STRING_CLASS)
                        .addProperty(KEY_HASHKEY, CommonVariables.clientid + HASHKEY_VALUE, PropertyInfo.STRING_CLASS);


                return builder.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }*/

    public static String getRandomNumber() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        int randomUID = calendar.get(Calendar.SECOND);
        String formatted = String.format("%05d", num) + "" + randomUID;
//        long totalNum=Integer.parseInt(formatted)+date.getTime();
//        return String.valueOf(totalNum);
        return formatted;
    }
}
