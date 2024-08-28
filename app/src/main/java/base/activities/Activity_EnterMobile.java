package base.activities;

import static base.utils.CommonMethods.validate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.eurosoft.customerapp.R;
import com.tfb.fbtoast.FBToast;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONObject;

import base.listener.Listener_Login;
import base.manager.Manager_SignUpAppUserNew;
import base.models.ParentPojo;
import base.models.SettingsModel;
import base.models.User;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_EnterMobile extends AppCompatActivity {
    private Button confirmBtn;
    private EditText emailEt;
    private TextView subTitleTv;
    private TextView titleTv;
    private ImageView backIv;
    private ImageView menuIv;
    private IntlPhoneInput phoneInput;
    private LinearLayout mobileLyt, emailLyt;


    // JAVA
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private SharedPrefrenceHelper sharedPrefrenceHelper;
    private SettingsModel model;
    private ParentPojo p;

    private boolean isEmailVerification = false;
    private String varificationip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_enter_mobile);

        initObject();

        init();

        listener();
    }

    private void initObject() {
        CommonMethods.getInstance().setDarkAndNightColorBlackWhite(Activity_EnterMobile.this);

        p = new ParentPojo();

        sharedPrefrenceHelper = new SharedPrefrenceHelper(this);
        model = sharedPrefrenceHelper.getSettingModel();

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        edit = sp.edit();
    }

    private void init() {
        menuIv = findViewById(R.id.menuIv);
        menuIv.setVisibility(View.GONE);

        backIv = findViewById(R.id.backIv);
        backIv.setVisibility(View.VISIBLE);

        confirmBtn = findViewById(R.id.confirmBtn);
        confirmBtn.setText(p.getConfirm());

        emailEt = findViewById(R.id.emailEt);

        subTitleTv = findViewById(R.id.subTitleTv);
        subTitleTv.setText(p.getSubHeadingMobile());

        titleTv = findViewById(R.id.titleTv);
        titleTv.setText(p.getEnterEmail());

        phoneInput = (IntlPhoneInput) findViewById(R.id.my_phone_input);

        mobileLyt = findViewById(R.id.mobileLyt);
        emailLyt = findViewById(R.id.emaillyt);

        isEmailVerification = (sp.getString(Config.VerificationType, "1").equals("1")) ? false : true;

        mobileLyt.setVisibility((sp.getString(Config.VerificationType, "1").equals("1")) ? View.VISIBLE : View.GONE);
        emailLyt.setVisibility((sp.getString(Config.VerificationType, "1").equals("1")) ? View.GONE : View.VISIBLE);

        emailEt.setText("" + model.getEmail());
        phoneInput.getInputText().setText(model.getPhoneNo());

        String title = (sp.getString(Config.VerificationType, "1").equals("1")) ? "Enter Mobile No" : "Enter Email";
        titleTv.setText(title);

        String subTitle = (sp.getString(Config.VerificationType, "1").equals("1")) ? "Enter your country and mobile number and we will send you a verification code via sms" : "Enter your email address and we will send you a verification code via email";
        subTitleTv.setText(subTitle);
    }

    private void listener() {
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_EnterMobile.this, Activity_Signup.class).putExtra("from", "enter_mobile"));
              //  Animatoo.animateSlideRight(Activity_EnterMobile.this);
                finish();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                SettingsModel _model = model;

                if (isEmailVerification) {
                    if (getEmail().length() == 0) {
                        FBToast.errorToast(Activity_EnterMobile.this, "Invalid Email", FBToast.LENGTH_SHORT);
                        return;
                    }

                    if (!validate(getEmail())) {
                        FBToast.errorToast(Activity_EnterMobile.this, "Invalid Email", FBToast.LENGTH_SHORT);
                        return;
                    }

                    model.setEmail(getEmail());

                    showDialogue(getEmail());
                } else {
                    if (phoneInput.getNumber() != null) {
                        if (!phoneInput.isValid()) {
                            FBToast.errorToast(Activity_EnterMobile.this, "Invalid Mobile Number", FBToast.LENGTH_SHORT);
                            return;
                        }
                    } else {
                        FBToast.errorToast(Activity_EnterMobile.this, "Mobile Number Required", FBToast.LENGTH_SHORT);
                        return;
                    }
                    model.setPhoneNo(getPhoneNo());
                    showDialogue(getPhoneNo());
                }
            }
        });
    }

    public void showDialogue(String mobileno) {
        String Message =
                (!isEmailVerification) ?
                        "A sms verification code will be sent to your mobile number. " + mobileno + " is your mobile number correct?"
                        :
                        "A verification code will be sent to your email address. " + mobileno + " is your email address correct?";

        new SweetAlertDialog(Activity_EnterMobile.this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Verification")
                .setContentText(Message)
                .setConfirmText(p.getYes())
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                        SweetAlertDialog pDialog = new SweetAlertDialog(Activity_EnterMobile.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setRimColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Sending verification code...");
                        pDialog.setCancelable(false);

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

                        new Manager_SignUpAppUserNew(user, new Listener_Login() {
                            @Override
                            public void onPre(String start) {
                                try {
                                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(emailEt.getWindowToken(), 0);
                                    backIv.setClickable(false);
                                    pDialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onPost(String response) {
                                try {
                                    pDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                backIv.setClickable(true);

                                if (response != null && !response.isEmpty()) {
                                    try {
                                        JSONObject parentObject = new JSONObject(response);
                                        if (parentObject.getBoolean("HasError")) {
                                            String msg = parentObject.getString("Message");
                                            msg = msg.replace("Registered", "registered");

                                            new SweetAlertDialog(Activity_EnterMobile.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Registration Failed!")
                                                    .setContentText(msg)
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
                                            String result = parentObject.getString("TokenValidate");

                                            if (result.equals("ValidToken")) {
                                                edit.putString(CommonVariables.ISAuthorized, "0");
                                                edit.commit();
                                                sharedPrefrenceHelper.putSettingModel(model);
                                                CommonVariables.Clientip = varificationip = sp.getString("Clientip", "");
                                                startActivity(new Intent(Activity_EnterMobile.this, Activity_AuthorizeCode.class).putExtra("ip", varificationip));
                                                finish();
                                            } else if (result.toLowerCase().startsWith("this")) {
                                                FBToast.errorToast(Activity_EnterMobile.this, result, FBToast.LENGTH_SHORT);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(Activity_EnterMobile.this, "Problems in getting data. Please check your internet connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })
                .setCancelText(p.getNo())
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                }).show();
    }

    private String getPhoneNo() {
        return (sp.getString(Config.VerificationType, "1").equals("1")) ? "" : phoneInput.getText().toString().trim().replace("+44", "0");
    }

    private String getEmail() {
        return emailEt.getText().toString().trim();
    }
}
