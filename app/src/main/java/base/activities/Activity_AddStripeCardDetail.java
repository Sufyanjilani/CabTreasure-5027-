package base.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.eurosoft.customerapp.R;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.SetupIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmSetupIntentParams;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.SetupIntent;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.view.CardInputWidget;
import com.tfb.fbtoast.FBToast;

import org.json.JSONObject;

import base.listener.Listener_SaveCustomerId;
import base.listener.Listener_Stripe_AddCard;
import base.manager.Manager_SaveCustomerId;
import base.manager.Manager_Stripe_AddCard;
import base.models.SettingsModel;
import base.utils.CommonMethods;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_AddStripeCardDetail extends AppCompatActivity {

    private SettingsModel settingsModel;
    private CardInputWidget cardInputWidget;
    private Button addButton;
    private Stripe stripe;
    private SharedPrefrenceHelper helper;
    private SharedPreferences sp;
    private ProgressBar pb;
    private ImageView imgBack;

    PaymentSheet.CustomerConfiguration customerConfig;
    PaymentSheet paymentSheet;
    String setupIntentClientSecret;

    // Listener
    Listener_Stripe_AddCard listener_stripe_addCard;
    Listener_SaveCustomerId listener_saveCustomerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_add_payment_stripe_activity);

        CommonMethods.getInstance().setDarkAndNightColorBlackWhite(Activity_AddStripeCardDetail.this);

        setInitData();

        init();

        listener();


        // new strip
        initPayment();

        if (sp.getString(Config.Stripe_PublishKey, "").equals("")) {
            FBToast.warningToast(Activity_AddStripeCardDetail.this, "No Stripe Key Found", FBToast.LENGTH_SHORT);
            return;
        }
        // new strip
        new Manager_Stripe_AddCard(Activity_AddStripeCardDetail.this, helper.getStripeCustomerId(), settingsModel, listener_stripe_addCard).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                new RequestPaymentIntentFromServe();


    }
    // new strip
    public void initPayment(){
        try {
            paymentSheet = new PaymentSheet((ComponentActivity) this, this::onPaymentSheetResult);
            PaymentConfiguration.init(getApplicationContext(), sp.getString(Config.Stripe_PublishKey, ""));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    // new strip
    private void onPaymentSheetResult(
            final PaymentSheetResult paymentSheetResult
    ) {
        pb.setVisibility(View.GONE);
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d("Stripe", "Canceled");
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            String message =  ((PaymentSheetResult.Failed) paymentSheetResult).getError().getMessage();
            showDialogue("Failed",message);
            Log.e("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Log.d("Stripe", "Completed");
            new Manager_SaveCustomerId(Activity_AddStripeCardDetail.this, true,
                    helper.getStripeCustomerId(), settingsModel, listener_saveCustomerId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
        else{
            showDialogue("Failed","Something went wrong");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmSetupIntent
        stripe.onSetupResult(requestCode, data, new ApiResultCallback<SetupIntentResult>() {
            @Override
            public void onSuccess(@NonNull SetupIntentResult result) {
                SetupIntent setupIntent = result.getIntent();
                SetupIntent.Status status = setupIntent.getStatus();
                if (status == SetupIntent.Status.Succeeded) {
                    // Setup completed successfully
                    String id = setupIntent.getPaymentMethodId();
                    new Manager_SaveCustomerId(Activity_AddStripeCardDetail.this, true,
                            helper.getStripeCustomerId(), settingsModel, listener_saveCustomerId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else if (status == SetupIntent.Status.RequiresPaymentMethod) {
                    // Setup failed – allow retrying
                    FBToast.errorToast(Activity_AddStripeCardDetail.this, "Payment Failed", FBToast.LENGTH_LONG);
                }
            }

            @Override
            public void onError(@NonNull Exception e) {
                e.printStackTrace();
                pb.setVisibility(View.GONE);
                FBToast.errorToast(Activity_AddStripeCardDetail.this, "" + e.getMessage(), FBToast.LENGTH_LONG);
            }
        });
    }

    private void setInitData() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        helper = new SharedPrefrenceHelper(this);
        settingsModel = helper.getSettingModel();

        try {
            stripe = new Stripe(Activity_AddStripeCardDetail.this, sp.getString(Config.Stripe_PublishKey, ""));///publicsh key
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        pb = findViewById(R.id.pb);
        imgBack = findViewById(R.id.imgBack);
        cardInputWidget = findViewById(R.id.cardInputWidget);
        cardInputWidget.requestFocus();//cardinput stripe
        addButton = findViewById(R.id.addButton);
    }

    private void listener() {
        listener_stripe_addCard = new Listener_Stripe_AddCard() {
            @Override
            public void onComplete(String result) {
                pb.setVisibility(View.VISIBLE);

                try {
                    if (result != null && !result.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean hasError = jsonObject.getBoolean("hasError");
                            if (hasError) {
                                if (jsonObject.getString("message").startsWith("No such customer")) {
                                    helper.putStripeCustomerId("");
                                    new Manager_Stripe_AddCard(Activity_AddStripeCardDetail.this, helper.getStripeCustomerId(), settingsModel, listener_stripe_addCard).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    pb.setVisibility(View.GONE);
                                    FBToast.errorToast(Activity_AddStripeCardDetail.this, jsonObject.getString("message"), FBToast.LENGTH_SHORT);
                                }
                            } else {
                                if (jsonObject.getString("message").contains("success")) {
                                    String clientSecret = jsonObject.getString("clientSecret");
                                    String customerId = jsonObject.getString("customerId");

                                    if (customerId != null && !customerId.equals("")) {
                                        helper.putStripeCustomerId(customerId);
                                    }

                                    // new stripe sheet
                                    customerConfig = new PaymentSheet.CustomerConfiguration(
                                            customerId,
                                            sp.getString(Config.Stripe_PublishKey, ""));
                                    setupIntentClientSecret = clientSecret;
                                    presentPaymentSheet(customerConfig,setupIntentClientSecret);
//                                    PaymentMethodCreateParams.Card card = cardInputWidget.getPaymentMethodCard();
//                                    if (card != null) {
//                                        PaymentMethod.BillingDetails billingDetails = new PaymentMethod.BillingDetails.Builder().build();
//                                        // Create SetupIntent confirm parameters with the above
//                                        PaymentMethodCreateParams paymentMethodParams = PaymentMethodCreateParams.create(card, billingDetails);
//                                        ConfirmSetupIntentParams confirmParams = ConfirmSetupIntentParams.create(paymentMethodParams, clientSecret);
//                                        stripe.confirmSetupIntent((ComponentActivity) Activity_AddStripeCardDetail.this, confirmParams);
//                                    }
                                } else {
                                    pb.setVisibility(View.GONE);
//                                    message("", mDialog);
                                }
                            }
                        } catch (Exception e) {
                            pb.setVisibility(View.GONE);
                            e.printStackTrace();
//                            message("", mDialog);
                        }
                    } else {
                        if (result.startsWith("error_")) {
                            result = result.replace("error_", "");
                            FBToast.errorToast(Activity_AddStripeCardDetail.this, "Payment Failed\nUnable to process payment, Please try again later.\n" + result, FBToast.LENGTH_SHORT);
                        }
                        //message(, mDialog);
                        pb.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pb.setVisibility(View.GONE);

                }
            }
        };

        listener_saveCustomerId = new Listener_SaveCustomerId() {
            @Override
            public void onComplete(String result) {
                pb.setVisibility(View.GONE);
                try {
                    JSONObject parentObject = new JSONObject(result);
                    if (parentObject.getBoolean("HasError")) {
                        showDialogue("Try Again", parentObject.getString("Message"));
                    } else {
                        Intent in = new Intent();
                        setResult(RESULT_OK, in);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }
            }
        };

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (cardInputWidget.getCard() == null) {
//                    FBToast.warningToast(Activity_AddStripeCardDetail.this, "Please Enter card", FBToast.LENGTH_SHORT);
//                    return;
//                }
                if (sp.getString(Config.Stripe_PublishKey, "").equals("")) {
                    FBToast.warningToast(Activity_AddStripeCardDetail.this, "No Stripe Key Found", FBToast.LENGTH_SHORT);
                    return;
                }
                new Manager_Stripe_AddCard(Activity_AddStripeCardDetail.this, helper.getStripeCustomerId(), settingsModel, listener_stripe_addCard).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                new RequestPaymentIntentFromServe();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


    // new strip
    private void presentPaymentSheet(PaymentSheet.CustomerConfiguration confug,String setupIntentClientSecret) {
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder(getString(com.amalbit.trail.R.string.app_name)+"")
                .customer(confug)
                .allowsDelayedPaymentMethods(true)
                .build();
        paymentSheet.presentWithSetupIntent(setupIntentClientSecret,
                configuration);

        pb.setVisibility(View.GONE);

    }

    private void showDialogue(String Title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(Title)
                .setContentText(message)
                .setCancelText("Exit!")
                .setConfirmText("Retry")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        new Manager_SaveCustomerId(Activity_AddStripeCardDetail.this, true, helper.getStripeCustomerId(), settingsModel, listener_saveCustomerId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        new SaveCustomerID(true, helper.getStripeCustomerId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        finish();
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })
                .show();
    }
}
