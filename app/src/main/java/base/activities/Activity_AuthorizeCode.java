package base.activities;

import static base.utils.CommonMethods.checkIfHasNullForString;
import static base.utils.CommonMethods.validate;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/*import com.blogspot.atifsoftwares.animatoolib.Animatoo;*/
import com.eurosoft.customerapp.R;
/*import com.poovam.pinedittextfield.SquarePinField;*/
import com.mukesh.OtpView;
import com.tfb.fbtoast.FBToast;

import org.json.JSONObject;

import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.fragments.Fragment_Main;
import base.listener.Listener_Login;
import base.manager.Manager_AuthorizeAppUserNew;
import base.manager.Manager_ResendAuthCode;
import base.models.ParentPojo;
import base.models.SettingsModel;
import base.models.User;
import base.services.SmsBroadcastReceiver;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_AuthorizeCode extends AppCompatActivity implements SmsBroadcastReceiver.Listener {
    private static final int SMS_PERMISSION_CODE = 8963;

    // VIEWS
    private TextView subTitleTv;
    private TextView phoneNoTv;
    private TextView titleTv;

    private ImageView backIv;
    private ImageView menuIv;

    private Button confirmBtn;
    private TextView resendCodeTimerTv;
    private Button resendBtn;
    private TextView resendCodeAfterTv;
    private TextView secondsTv;


    private SweetAlertDialog pdLoading;
    private LinearLayout resendLyt;

    // JAVA CLASS
    private ParentPojo p;
    private String ValidationCode = "";
    private CountDownTimer countDownTimer;
    private boolean isEmailVerification = false;
    private SmsBroadcastReceiver smsBroadcastReceiver;
    private OtpView inputPin;

    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private SharedPrefrenceHelper sharedPrefrenceHelper;
    private DatabaseHelper databaseHelper;
    private DatabaseOperations operations;

    private SettingsModel model;
    private Listener_Login listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_authorize_code);

        setObject();

        init();

        listener();
    }

    private void setObject() {
        CommonMethods.getInstance().setDarkAndNightColorBlackWhite(Activity_AuthorizeCode.this);

        p = new ParentPojo();

        databaseHelper = new DatabaseHelper(this);
        operations = new DatabaseOperations(databaseHelper);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefrenceHelper = new SharedPrefrenceHelper(this);
        model = sharedPrefrenceHelper.getSettingModel();
        edit = sp.edit();

        pdLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
    }

    private void init() {
        menuIv = findViewById(R.id.menuIv);
        menuIv.setVisibility(View.GONE);

        backIv = findViewById(R.id.backIv);
        backIv.setVisibility(View.VISIBLE);

        titleTv = findViewById(R.id.titleTv);
        titleTv.setText(p.getVerifyEmail());

        confirmBtn = findViewById(R.id.confirmBtn);
        confirmBtn.setText(p.getConfirm());

        subTitleTv = findViewById(R.id.subTitleTv);
        subTitleTv.setText(p.getSubHeadingAuthorize());

        phoneNoTv = findViewById(R.id.phoneNoTv);

        inputPin = findViewById(R.id.squareField);

        resendBtn = findViewById(R.id.resendBtn);
        resendBtn.setText(p.getBtnAuthorizeResendCode());

        resendCodeAfterTv = findViewById(R.id.resendCodeAfterTv);
        resendCodeAfterTv.setText(p.getBtnAuthorizeP1());

        secondsTv = findViewById(R.id.secondsTv);
        secondsTv.setText(p.getBtnAuthorizeP2());

        resendCodeTimerTv = findViewById(R.id.resendCodeTimerTv);
        resendLyt = findViewById(R.id.resendLyt);

        isEmailVerification = (sp.getString(Config.VerificationType, "1").equals("1")) ? false : true;
        titleTv.setText((sp.getString(Config.VerificationType, "1").equals("1")) ? "Verify Mobile Number" : "Verify Email Address");
        subTitleTv.setText(isEmailVerification ? "We have sent you a verification code on your email address " : "We have sent you a verification code on your number");
        phoneNoTv.setText(isEmailVerification ? model.getEmail() : model.getPhoneNo());
    }

    private void listener() {
        listener = new Listener_Login() {
            @Override
            public void onPre(String start) {
                pdLoading.setTitleText("Validating Code.");
                pdLoading.setContentText("Please wait..");
                pdLoading.show();
            }

            @Override
            public void onPost(String response) {
                pdLoading.dismiss();
                response = checkIfHasNullForString(response);
                if (!response.equals("")) {
                    try {
                        JSONObject parentObject = new JSONObject(response);
                        if (parentObject.getBoolean("HasError")) {
                            new SweetAlertDialog(Activity_AuthorizeCode.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Invalid Code!")
                                    .setContentText(checkIfHasNullForString(parentObject.getString("Message")))
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }

                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                        }
                                    })
                                    .show();
                        } else {
                            if (parentObject.getString("TokenValidate").equals("ValidToken")) {

                                JSONObject dataObj = parentObject.optJSONObject("Data");
                                if (dataObj != null) {
                                    JSONObject customerDetailsObj = dataObj.optJSONObject("customerDetails");
                                    if (customerDetailsObj != null) {
                                        String serverId = checkIfHasNullForString(customerDetailsObj.optString("CustomerId"));

                                        try {
                                            operations.INSERTAccount(model.getName() + " " + model.getlName(), "1", ValidationCode, CommonVariables.Clientip, model.getEmail(), model.getPhoneNo(), model.getPassword());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        edit.putString(CommonVariables.USerLogin, model.getEmail());
                                        edit.putBoolean(Config.ISFIRSTSIGNUP, true);
                                        edit.putBoolean(CommonVariables.ISUSERLOGIN, true);
                                        edit.putString(CommonVariables.ISAuthorized, "1");
                                        edit.putString(CommonVariables.MEMBERACCEMAIL, "");
                                        edit.putString(CommonVariables.MEMBERACCNAME, "");
                                        edit.putBoolean(CommonVariables.ISMEMBERUSERLOGIN, false);
                                        edit.commit();


                                        model.setUserServerID(serverId);
                                        model.setAccountNo("");
                                        model.setAccountWebID("");
                                        model.setAddress("");
                                        model.setPaymentType("cash");
                                        sharedPrefrenceHelper.putSettingModel(model);

                                        startActivity(new Intent(Activity_AuthorizeCode.this, Fragment_Main.class).putExtra("from_auth", "auth_screen"));
                                     //   Animatoo.animateSlideLeft(Activity_AuthorizeCode.this);
                                        finish();
                                    }
                                }
                            } else {
                                FBToast.errorToast(Activity_AuthorizeCode.this, "please try again", Toast.LENGTH_LONG);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                }
            }
        };

        startTimer();

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putString(CommonVariables.ISAuthorized, "");
                edit.commit();

                startActivity(new Intent(Activity_AuthorizeCode.this, Activity_EnterMobile.class));
               // Animatoo.animateSlideLeft(Activity_AuthorizeCode.this);
                finish();
            }
        });

        resendBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                showResendDialogue();

            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ValidationCode = inputPin.getText().toString().trim();
                if (ValidationCode.length() == 4) {
                    verificationafterregistration();
                } else {
                    FBToast.errorToast(Activity_AuthorizeCode.this, "Invalid Code", Toast.LENGTH_SHORT);
                    return;
                }
            }
        });
    }

    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onTextReceived(String text) {
        ValidationCode = text;

        new Manager_AuthorizeAppUserNew(new User(), listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new SignUp_AuthCode().execute();
    }

    public void verificationafterregistration() {
        User user = new User();
        user.setUserName(model.getName() + " " + model.getlName());
        user.setPasswrd(model.getPassword());
        user.setSubCompanyId(CommonVariables.SUB_COMPANY);
        user.setDefaultClientId("" + model._getDefaultclientId());
        user.setDeviceInfo(model.getDeviceInfo());
        user.setEmail(model.getEmail());
        user.setUniqueId(model.getUniqueId());
        user.setUniqueValue(model.getUniqueValue());
        user.setPhoneNo(model.getPhoneNo());
        user.setCode(ValidationCode);
        new Manager_AuthorizeAppUserNew(user, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void startTimer() {
        resendLyt.setVisibility(View.VISIBLE);
        resendBtn.setVisibility(View.GONE);
        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                resendCodeTimerTv.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                resendLyt.setVisibility(View.GONE);
                resendBtn.setVisibility(View.VISIBLE);
                resendBtn.setClickable(true);
                resendBtn.setEnabled(true);
            }

        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        try {
            if (smsBroadcastReceiver != null) {
                unregisterReceiver(smsBroadcastReceiver);
            }
        } catch (Exception e) {

        }
    }

    public void showResendDialogue() {

        String Message = isEmailVerification ? "is your email address correct?" : "is your mobile number correct?";
        String reEnterTxt = isEmailVerification ? "Re-enter email" : "Re-enter Mobile No";

        new SweetAlertDialog(Activity_AuthorizeCode.this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(isEmailVerification ? model.getEmail() : model.getPhoneNo())
                .setContentText(Message)
                .setConfirmText(p.getYes())
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        //new Resend_AuthCode().execute();
                        resendCode();
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })

                .setCancelText(reEnterTxt)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismissWithAnimation();
                        String Message = "Enter Mobile Number";

                        if (isEmailVerification) {
                            Message = "Enter Email Address";

                        }
                        new SweetAlertDialog(Activity_AuthorizeCode.this, SweetAlertDialog.EDITTEXT_TYPE)
                                .setTitleText(Message)
                                .setConfirmText("Send Code")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    }

                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                        if (isEmailVerification) {
                                            if (InputText.length() == 0) {
                                                FBToast.errorToast(Activity_AuthorizeCode.this, "Please Enter Email Address", Toast.LENGTH_SHORT);
                                                return;
                                            }
                                            if (InputText.equals("")) {
                                                FBToast.errorToast(Activity_AuthorizeCode.this, "Please Enter Email Address", FBToast.LENGTH_SHORT);
                                                return;
                                            } else {
                                                boolean ValidEmail = validate(InputText);
                                                String email[] = InputText.split("@");
                                                if (email.length > 1 && email[1].contains("..")) {
                                                    FBToast.errorToast(Activity_AuthorizeCode.this, "Invalid Email Address", FBToast.LENGTH_SHORT);
                                                    return;
                                                }
                                                if (!ValidEmail) {
                                                    FBToast.errorToast(Activity_AuthorizeCode.this, "Invalid Email Address", FBToast.LENGTH_SHORT);
                                                    return;
                                                }
                                            }
                                            model.setEmail(InputText);
                                            subTitleTv.setText("We have sent you a verification code on your email address ");
                                            phoneNoTv.setText(model.getEmail());

                                        } else {
                                            if (InputText.length() == 0) {
                                                FBToast.errorToast(Activity_AuthorizeCode.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT);
                                                return;
                                            }
                                            model.setPhoneNo(InputText);
                                            subTitleTv.setText("We have sent you a verification code on your number ");
                                            phoneNoTv.setText(model.getPhoneNo());
                                        }
                                        sweetAlertDialog.dismissWithAnimation();
                                        resendCode();
                                    }
                                }).showCancelButton(true)
                                .setCancelText("Cancel")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }

                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                }).show();
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })
                .show();
    }

    private void resendCode() {
        User user = new User();
        user.setUserName(model.getName() + " " + model.getlName());
        user.setPasswrd(model.getPassword());
        user.setDefaultClientId("" + model._getDefaultclientId());
        user.setDeviceInfo(model.getDeviceInfo());
        user.setEmail(model.getEmail());
        user.setSubCompanyId(CommonVariables.SUB_COMPANY);
        user.setUniqueId(model.getUniqueId());
        user.setUniqueValue(model.getUniqueValue());
        user.setPhoneNo(model.getPhoneNo());

        new Manager_ResendAuthCode(user, new Listener_Login() {
            @Override
            public void onPre(String start) {

            }

            @Override
            public void onPost(String response) {
                pdLoading.dismiss();
                response = checkIfHasNullForString(response);
                if (!response.equals("")) {
                    try {
                        JSONObject parentObject = new JSONObject(response);
                        if (parentObject.getBoolean("HasError")) {
                            new SweetAlertDialog(Activity_AuthorizeCode.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Invalid Code!")
                                    .setContentText(checkIfHasNullForString(parentObject.getString("Message")))
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            try {
                                                pdLoading.dismiss();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                        }
                                    })
                                    .show();
                        } else {
                            if (parentObject.getString("TokenValidate").equals("ValidToken")) {
                                try {
                                    FBToast.infoToast(getApplicationContext(), "Code sent successfully", Toast.LENGTH_LONG);
                                    startTimer();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                FBToast.errorToast(getApplicationContext(), "please try again", Toast.LENGTH_LONG);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isSmsPermissionGranted()) {
                        smsBroadcastReceiver = new SmsBroadcastReceiver();
                        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
                    }
                    // permission was granted, yay! Do the
                    // SMS related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
