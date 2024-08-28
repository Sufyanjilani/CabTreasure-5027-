//package base.activities;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.blogspot.atifsoftwares.animatoolib.Animatoo;
//import com.craftman.cardform.Card;
//import com.craftman.cardform.CardForm;
//import com.craftman.cardform.DataLoadListner;
//import com.craftman.cardform.OnPayBtnClickListner;
//import com.eurosofttech.kingscars.R;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.judopay.model.Currency;
//import com.stripe.android.ApiResultCallback;
//import com.stripe.android.PaymentConfiguration;
//import com.stripe.android.PaymentIntentResult;
//import com.stripe.android.Stripe;
//import com.stripe.android.model.ConfirmPaymentIntentParams;
//import com.stripe.android.model.PaymentIntent;
//import com.stripe.android.model.PaymentMethodCreateParams;
//import com.stripe.android.view.CardInputWidget;
//import com.tfb.fbtoast.FBToast;
//
//import org.json.JSONObject;
//
//import java.lang.ref.WeakReference;
//import java.util.Objects;
//import java.util.concurrent.TimeUnit;
//
//
//import base.models.Model_CardDetails;
//import base.utils.CommonVariables;
//import base.fragments.Fragment_Main;
//import base.models.ShareTracking;
//import base.utils.Config;
//import base.models.SettingsModel;
//import base.utils.SharedPrefrenceHelper;
//import cn.pedant.SweetAlert.SweetAlertDialog;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//import static base.utils.CommonVariables.CurrencySymbol;
//
//
//public class Activity_PayWithStripe extends AppCompatActivity {
//    CardForm cardForm;
//    boolean isPayment = false;
//    SharedPrefrenceHelper sharedPrefrenceHelper;
//    boolean startup = false;
//    private String paymentIntentClientSecret;
//    private Stripe stripe;
//    CardInputWidget cardInputWidget;
//    private SharedPreferences sp;
//    SettingsModel settingsModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_pay_with_stripe);
//
//        sharedPrefrenceHelper = new SharedPrefrenceHelper(Activity_PayWithStripe.this);
//        settingsModel = sharedPrefrenceHelper.getSettingModel();
//
//        sp = PreferenceManager.getDefaultSharedPreferences(this);
//
//        PaymentConfiguration.init(
//                getApplicationContext(),
//                Config.Stripe_PublishKey
//        );
//        if (getIntent() != null && getIntent().hasExtra("Fares")) {
//            Fares = getIntent().getStringExtra("Fares");
//        } else {
//
//            finish();
//        }
//        Model_CardDetails modelCardDetails = sharedPrefrenceHelper.getCardModel();
//        if (getIntent() != null && getIntent().hasExtra("payment")) {
//            isPayment = true;
//        }
//
//        cardInputWidget = findViewById(R.id.cardInputWidget);
//        cardForm = findViewById(R.id.card_form);
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
//        startCheckout();
//        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
//            @Override
//            public void onClick(Card card) {
//                if (card != null) {
//                    String cardNo = card.getNumber();
//                    String cardName = card.getName();
////                   String cardCountry= card.getCountry();
//
//                    int ExpYear = card.getExpYear();
//                    int ExtMonth = card.getExpMonth() == null ? 0 : card.getExpMonth();
//                    String Cvc = card.getCVC();
//                    if (!cardNo.equals("") && ExpYear > 0 && ExtMonth > 0 && !Cvc.equals("")) {
//                        sharedPrefrenceHelper = new SharedPrefrenceHelper(Activity_PayWithStripe.this);
//
//                        Model_CardDetails modelCardDetails = new Model_CardDetails();
//                        modelCardDetails.setCardNumber(cardNo);
//                        modelCardDetails.setCVV(Cvc);
//                        modelCardDetails.setExpiryMonth(String.valueOf(ExtMonth));
//                        modelCardDetails.setExpiryYear(String.valueOf(ExpYear));
////                modelCardDetails.setAddress(customerAddress.getText().toString());
//                        modelCardDetails.setCardName(cardName);
////                modelCardDetails.setPostCode(customerPostCode.getText().toString());
//                        sharedPrefrenceHelper.putCardModel(modelCardDetails);
////                        Toast.makeText(Activity_AddCard.this, "Add Card Successfully", Toast.LENGTH_SHORT).show();
//                        FBToast.infoToast(Activity_PayWithStripe.this, "Add Card Successfully", FBToast.LENGTH_SHORT);
//                        if (!isPayment) {
//                            startActivity(new Intent(Activity_PayWithStripe.this, Fragment_Main.class));
//                        }
////                        else if(startup){
////                            startActivity(new Intent(Activity_AddCard.this,Activity_Default_Payment.class));
////                        }
//                        Animatoo.animateSlideLeft(Activity_PayWithStripe.this);
//                        finish();
//                    } else {
//                        Toast.makeText(Activity_PayWithStripe.this, "Please enter complete card details", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//
//                    FBToast.infoToast(Activity_PayWithStripe.this, "Card details removed successfully", FBToast.LENGTH_SHORT);
//                    Animatoo.animateSlideLeft(Activity_PayWithStripe.this);
//                    finish();
//                }
//            }
//        });
//    }
//
//    DataLoadListner dataLoadListner;
//
//    public void setDataLoadListner(DataLoadListner onPayBtnClickListner) {
//        this.dataLoadListner = onPayBtnClickListner;
//    }
//
//    public void goBack(View v) {
////        if(isPayment){
////
////        }else {
////            startActivity(new Intent(this, Activity_Default_Payment.class));
////        }
//        Animatoo.animateSlideRight(this);
//
//        finish();
//    }
//
//    String Fares = "1.1";
//    SweetAlertDialog dialog;
//
//    private void startCheckout() {
//        // ...
//
////        Stripe.apiKey = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";
////
////        PaymentIntentCreateParams params =
////                PaymentIntentCreateParams.builder()
////                        .setAmount(1099L)
////                        .setCurrency("usd")
////                        .build();
////
////        PaymentIntent intent = null;
//////        try {
//////            intent = new PaymentIntent.create(params);
////            String clientSecret = intent.getClientSecret();
////        } catch (StripeException e) {
////            e.printStackTrace();
////        }
//
//
//        // Hook up the pay button to the card widget and stripe instance
//        Button payButton = findViewById(R.id.payButton);
//        payButton.setText("Pay " + sp.getString(CurrencySymbol, "\u00A3") + Fares);
//        payButton.setOnClickListener((View view) -> {
//            dialog = showProgressMessage("Please wait...", "Processing your payment");
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    com.stripe.android.model.Card
////                    String cardCountry=cardInputWidget.getCard().getCountry();
////                    Log.e("TAG","COUNTRY---"+cardCountry+"-----"+cardInputWidget.getCard().getAddressCountry());
////                    int finalAmount=(int)(100*Float.parseFloat(Fares));
//                    try {
//
//                        ShareTracking stripeModel = new ShareTracking();
//                        stripeModel.defaultClientId = CommonVariables.clientid;
//                        stripeModel.ReceiptEmail = settingsModel.getEmail();
//                        stripeModel.Amount = Fares;
//                        stripeModel.Currency = Currency.GBP;
//                        //    stripeModel.Stripe_PK = Config.Stripe_PK;
//                        stripeModel.MerchantSecondaryAPIKey = sp.getString(Config.Stripe_SecretKey, "");
//                        Gson gson = new Gson();
//                        String jsonString = gson.toJson(stripeModel);
//                        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
//                        clientBuilder.connectTimeout(8, TimeUnit.SECONDS);
//                        clientBuilder.readTimeout(8, TimeUnit.SECONDS);
//                        OkHttpClient client = clientBuilder.build();
//
//                        String url = "http://eurosofttech-api.co.uk/paymentgateway/api/gateway/createstripepaymentintent";
//                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
//
//                        Request request = new Request.Builder()
//                                .url(url)
//                                .post(requestBody)
//                                .build();
//
//                        Response rawResponse = client.newCall(request).execute();
//                        String response = rawResponse.body().string();
//                        if (response != null && !response.startsWith("false") && !response.equals("")) {
//                            try {
//                                paymentIntentClientSecret = response.replace("\"", "");
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
//                                        if (params != null) {
//                                            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
//                                            final Context context = getApplicationContext();
//
//
//                                            stripe = new Stripe(context, sp.getString(Config.Stripe_PublishKey, ""));
//                                            stripe.confirmPayment(Activity_PayWithStripe.this, confirmParams);
//                                        }
//                                    }
//                                });
//                            } catch (Exception e) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        dialog.dismiss();
//                                        showErrorMessage("Payment Failed", "Unable to process payment, Please try again later");
//                                    }
//                                });
//                            }
//                        } else {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    dialog.dismiss();
//                                    showErrorMessage("Payment Failed", "Unable to process payment, Please try again later");
//                                }
//                            });
//                        }
//
//                    } catch (Exception e) {
//
//                    }
//                }
//            }).start();
//
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Handle the result of stripe.confirmPayment
//        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
//    }
//
//    private static final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
//        private final WeakReference<Activity_PayWithStripe> activityRef;
//
//        PaymentResultCallback(Activity_PayWithStripe activity) {
//            activityRef = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void onSuccess(PaymentIntentResult result) {
//            final Activity_PayWithStripe activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//
//            PaymentIntent paymentIntent = result.getIntent();
//            PaymentIntent.Status status = paymentIntent.getStatus();
//            if (status == PaymentIntent.Status.Succeeded) {
//                // Payment completed successfully
//                Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                String finalResult = gson.toJson(paymentIntent);
//                try {
//
//                    JSONObject jsonObject = new JSONObject(finalResult);
//                    JSONObject subObj = jsonObject.getJSONObject("paymentMethod");
//                    String transId = jsonObject.getString("id");
////                    JSONObject cardObj=jsonObject.getJSONObject("card");
////                    String CardCountry=cardObj.getString("country");
//                    Intent intent = new Intent();
//                    intent.putExtra("TransId", transId);
//                    activity.setResulActivity(
//                            intent
//
//                    );
//
//                } catch (Exception e) {
//
//                }
//                activity.showSuccessgMsg(
//                        "Payment completed",
//                        gson.toJson(paymentIntent)
//
//                );
////                Log.e("Log","Payment "+finalResult);
//            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
//                // Payment failed
//                activity.showErrorMessage(
//                        "Payment failed",
//                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
//
//                );
//            }
//        }
//
//        @Override
//        public void onError(Exception e) {
//            final Activity_PayWithStripe activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//
//            // Payment request failed – allow retrying using the same payment method
//            activity.showErrorMessage("Payment Failed", e.toString());
//        }
//    }
//
//    void displayAlert(String type, String Data, boolean t) {
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void showErrorMessage(String title, String message) {
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//        dialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
//        dialog.setTitleText(title)
//                .setContentText(message)
////                                .setCancelText("Exit!")
//                .setConfirmText("OK")
//                .showCancelButton(false)
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
////                                        new InitializeAppDb(mContext, true).execute();
//                    }
//
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
//                        sweetAlertDialog.dismissWithAnimation();
//                    }
//                })
//                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.cancel();
////                                        mContext.finish();
//                    }
//
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
//
//                    }
//                })
//                .show();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void showSuccessgMsg(String title, String message) {
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//        FBToast.successToast(Activity_PayWithStripe.this, "Transaction Completed Successfully!", FBToast.LENGTH_SHORT);
//    }
//
//    public void setResulActivity(Intent intent) {
//        setResult(RESULT_OK, intent);
//        finish();
//    }
//
//    public SweetAlertDialog showProgressMessage(String title, String message) {
//        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        dialog.setTitleText(title);
//        dialog.setContentText(message);
//        dialog.setCancelable(false);
//        dialog.show();
//
//
//        return dialog;
//    }
//}
