package base.activities;

import static base.utils.CommonMethods.validate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

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

//import static base.signup.CreatAccount.VALID_EMAIL_ADDRESS_REGEX;

public class Activity_Signup extends AppCompatActivity {

    private EditText emailEt;
    private EditText firstNameEt;
    private EditText lastNameEt;
    private EditText passEt;
    private TextView termsCond;
    private TextView errorTv;
    private TextView termsLast;
    private TextView terms;
    private TextView progressBarTextTv;
    private TextView signupLabelTv;

    private ImageView backIv;
    private ImageView errorIv;
    private ImageView toggleImageForPasswordIv;

    private CardView buttonWithProgress;
    private LinearLayout emailLyt;
    private LinearLayout mobileLyt;
    private LinearLayout termsLyt;

    private ProgressBar progressBar;

    private View view_line_1;
    private View view_line_2;
    private View view_line_3;
    private View view_line_4;
    private View view_line_5;

    private IntlPhoneInput phoneInput;

    // JAVA
    private SharedPreferences sp;
    private SharedPrefrenceHelper sharedPrefrenceHelper;
    private SettingsModel model;
    private boolean isEmailVerification;
    private String termsUrl = "";
    private String deviceid = "";
    private String deviceinfo = "";
    private boolean isShown = false;
    private ParentPojo p = new ParentPojo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonMethods.getInstance().setDarkAndNightColor(Activity_Signup.this);

        setContentView(R.layout.layout_create_account);

        init();

        listener();

    }

    @Override
    public void onBackPressed() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, Activity_Start.class));
       // Animatoo.animateSlideRight(this);
        finish();
    }

    private void init() {

        signupLabelTv = findViewById(R.id.signupLabelTv);
        signupLabelTv.setText("Register");

        toggleImageForPasswordIv = findViewById(R.id.toggleImageForPasswordIv);

        view_line_1 = findViewById(R.id.view_line_1);
        view_line_2 = findViewById(R.id.view_line_2);
        view_line_3 = findViewById(R.id.view_line_3);
        view_line_4 = findViewById(R.id.view_line_4);
        view_line_5 = findViewById(R.id.view_line_5);


        phoneInput = (IntlPhoneInput) findViewById(R.id.my_phone_input);
        phoneInput.setTextColor(getResources().getColor(R.color.color_black_inverse));
        phoneInput.setHintColor(getResources().getColor(R.color.hintTextColor));

        progressBar = findViewById(R.id.progressBar);
        progressBarTextTv = findViewById(R.id.progressBarTextTv);
        progressBarTextTv.setText("Create Profile");

        emailEt = findViewById(R.id.emailEt);
        emailEt.setHint(p.getEmailHintEt());

        firstNameEt = findViewById(R.id.firstNameEt);
        firstNameEt.requestFocus();
        firstNameEt.setHint(p.getFirstName());

        lastNameEt = findViewById(R.id.lastNameEt);
        lastNameEt.setHint(p.getLastName());

        passEt = findViewById(R.id.passEt);
        passEt.setHint(p.getPasswordHintEt());

        backIv = findViewById(R.id.backIv);
        errorIv = findViewById(R.id.errorIv);
        buttonWithProgress = findViewById(R.id.buttonWithProgress);
        emailLyt = findViewById(R.id.emaillyt);

        mobileLyt = findViewById(R.id.mobileLyt);
        termsLyt = findViewById(R.id.termsLyt);
        termsCond = findViewById(R.id.termsCond);
        errorTv = findViewById(R.id.errorTv);
        termsLast = findViewById(R.id.termsCond1);
        terms = findViewById(R.id.terms);
        terms.setText(p.getByRegisteringYouAgree());

        initData();
    }

    private void initData() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefrenceHelper = new SharedPrefrenceHelper(this);
        isEmailVerification = (sp.getString(Config.VerificationType, "1").equals("1")) ? false : true;
        mobileLyt.setVisibility(isEmailVerification ? View.VISIBLE : View.GONE);
        emailLyt.setVisibility(isEmailVerification ? View.GONE : View.VISIBLE);

        if (sp.getString(Config.TermsConditions, "").equals("") || !sp.getString(Config.TermsConditions, "").toLowerCase().contains("http") || sp.getString(Config.TermsConditions, "").toLowerCase().equals("null")) {
            termsLyt.setVisibility(View.GONE);
        } else {
            String[] splitArr = sp.getString(Config.TermsConditions, "").split("\\|");
            termsUrl = splitArr[2];
            termsCond.setText(splitArr[1]);

            if (splitArr[0].startsWith(splitArr[1])) {
                terms.setText(splitArr[1]);
                terms.setPaintFlags(termsCond.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                termsCond.setText(splitArr[0].replace(splitArr[1], ""));
                terms.setTextColor(getResources().getColor(R.color.red));
                termsLast.setVisibility(View.GONE);
                String finalTermsUrl = termsUrl;
                terms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            if (!finalTermsUrl.equals("") && finalTermsUrl.toLowerCase().contains("http")) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalTermsUrl));
                                startActivity(browserIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (splitArr[0].endsWith(splitArr[1])) {
                termsCond.setText(splitArr[1]);
                termsCond.setPaintFlags(termsCond.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                terms.setText(splitArr[0].replace(splitArr[1], ""));
                termsLast.setVisibility(View.GONE);
                termsCond.setTextColor(getResources().getColor(R.color.red));
                String finalTermsUrl1 = termsUrl;
                termsCond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            if (!finalTermsUrl1.equals("") && finalTermsUrl1.toLowerCase().contains("http")) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalTermsUrl1));
                                startActivity(browserIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                String[] splitSentence = splitArr[0].split(splitArr[1]);
                termsCond.setText(splitArr[1]);
                termsCond.setPaintFlags(termsCond.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                terms.setText(splitSentence[0]);
                termsLast.setVisibility(View.VISIBLE);
                termsLast.setText(splitSentence[1]);
                termsCond.setTextColor(getResources().getColor(R.color.red));
                String finalTermsUrl2 = termsUrl;
                termsCond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            if (!finalTermsUrl2.equals("") && finalTermsUrl2.toLowerCase().contains("http")) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalTermsUrl2));
                                startActivity(browserIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        if (getIntent() != null && getIntent().getStringExtra("from").equals("start_activity"))
            model = new SettingsModel();
        if (getIntent() != null && getIntent().getStringExtra("from").equals("enter_mobile"))
            model = sharedPrefrenceHelper.getSettingModel();


        firstNameEt.setText(model.getName());
        lastNameEt.setText(model.getlName());
        phoneInput.getInputText().setText(model.getPhoneNo());
        emailEt.setText(model.getEmail());
        passEt.setText(model.getPassword());

        if (firstNameEt.getText().toString().length() == 0) {
            firstNameEt.setFocusable(true);
            firstNameEt.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        view_line_1.setVisibility((firstNameEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);
        view_line_2.setVisibility((lastNameEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);
        view_line_3.setVisibility((emailEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);
        view_line_4.setVisibility((phoneInput.hasFocus()) ? View.VISIBLE : View.INVISIBLE);
        view_line_5.setVisibility((passEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);

        firstNameEt.setOnFocusChangeListener((view, b) -> view_line_1.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE)));
        lastNameEt.setOnFocusChangeListener((view, b) -> view_line_2.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE)));
        emailEt.setOnFocusChangeListener((view, b) -> view_line_3.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE)));
        phoneInput.setOnFocusChangeListener((view, b) -> view_line_4.setVisibility(((b) ? View.VISIBLE : View.GONE)));
        passEt.setOnFocusChangeListener((view, b) -> view_line_5.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE)));

    }

    private void listener() {

        toggleImageForPasswordIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShown) {
                    isShown = true;
                    passEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    toggleImageForPasswordIv.setImageResource(R.drawable.ic_visibility_black_24dp);
                } else {
                    isShown = false;
                    passEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    toggleImageForPasswordIv.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                }
            }
        });

        phoneInput.getInputText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    errorTv.setText("");
                    errorTv.setVisibility(View.GONE);
                    errorIv.setVisibility(View.GONE);
                }
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buttonWithProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();

            }
        });

        passEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        signUp();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        if (emailEt.getText().toString().length() > 0) {
            emailEt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final int DRAWABLE_RIGHT = 2;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (emailEt.getRight() - emailEt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            emailEt.setText("");
                            emailEt.requestFocus();
                            return true;
                        }
                    }


                    return false;
                }
            });
        }
    }

    private void signUp() {
        boolean validation = true;

        deviceid = Settings.Secure.getString(Activity_Signup.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (getFirstName().length() == 0) {
//            firstNameEt.setError("Required");
            FBToast.errorToast(this, "Required First Name", FBToast.LENGTH_SHORT);
            return;
        }

        if (getLastName().length() == 0) {
//            lastNameEt.setError("Required");
            FBToast.errorToast(this, "Required Last Name", FBToast.LENGTH_SHORT);
            return;
        }

        if (!isEmailVerification) {
            if (getEmail().length() == 0) {
                FBToast.errorToast(this, "Invalid Email", FBToast.LENGTH_SHORT);
                return;
            }

            if (!validate(getEmail())) {
                FBToast.errorToast(this, "Invalid Email", FBToast.LENGTH_SHORT);
                return;
            }


        } else {
            if (phoneInput.getNumber() != null) {
                if (!phoneInput.isValid()) {
                    FBToast.errorToast(this, "Invalid Mobile Number", FBToast.LENGTH_SHORT);
                    return;
                }
            } else {
                FBToast.errorToast(this, "Mobile Number Required", FBToast.LENGTH_SHORT);
                return;
            }


        }

        if (getPass().length() == 0) {
            FBToast.errorToast(this, "Required Password", FBToast.LENGTH_SHORT);
            return;
        }

        User user = new User();
        user.setUserName(firstNameEt.getText().toString().trim() + " " + lastNameEt.getText().toString().trim());
        user.setSubCompanyId(CommonVariables.SUB_COMPANY);
        model.setName(firstNameEt.getText().toString().trim());
        model.setlName(lastNameEt.getText().toString().trim());
        user.setPasswrd(passEt.getText().toString().trim());
        model.setPassword(user.getPasswrd());

        user.setDefaultClientId("" + CommonVariables.clientid);
        model._setDefaultclientId("" + CommonVariables.clientid);

        user.setDeviceInfo("Android - " + android.os.Build.VERSION.RELEASE);
        model.setDeviceInfo(user.getDeviceInfo());

        user.setEmail(emailEt.getText().toString().trim());
        model.setEmail(user.getEmail());

        user.setUniqueId(deviceid);
        model.setUniqueId(user.getUniqueId());

        user.setUniqueValue(CommonVariables.clientid + "4321orue");
        model.setUniqueValue(user.getUniqueValue());

        user.setPhoneNo(getPhoneNo());
        model.setPhoneNo(getPhoneNo());

        sharedPrefrenceHelper.putSettingModel(model);

        if (!isEmailVerification) {
            user.setSendSMS("0");

            user.setSubCompanyId(CommonVariables.SUB_COMPANY);

            model.setSubCompanyId(user.getSubCompanyId());

            new Manager_SignUpAppUserNew(user, new Listener_Login() {
                @Override
                public void onPre(String start) {
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(passEt.getWindowToken(), 0);
                        showProgress(buttonWithProgress, progressBarTextTv, progressBar);
                        backIv.setClickable(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPost(String response) {
                    resetProgress(buttonWithProgress, progressBarTextTv, progressBar);
                    backIv.setClickable(true);

                    if (response != null && !response.isEmpty()) {
                        try {
                            JSONObject parentObject = new JSONObject(response);

                            if (parentObject.getBoolean("HasError")) {
                                new SweetAlertDialog(Activity_Signup.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Warning!")
                                        .setContentText(parentObject.getString("Message"))
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
                                String result = parentObject.getString("Data");
                                if (result.toLowerCase().startsWith("false")) {
                                    FBToast.errorToast(Activity_Signup.this, "Email already exists", FBToast.LENGTH_SHORT);
                                } else {
                                    gotoEnterMobileActivity();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        FBToast.errorToast(Activity_Signup.this, "Please check your internet connection and try again!", FBToast.LENGTH_SHORT);
                    }
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
        else {
            gotoEnterMobileActivity();
        }
    }

    private void gotoEnterMobileActivity() {
        model = sharedPrefrenceHelper.getSettingModel();
        startActivity(new Intent(Activity_Signup.this, Activity_EnterMobile.class));
      //  Animatoo.animateSlideLeft(Activity_Signup.this);
        finish();
    }

    private String getFirstName() {
        return firstNameEt.getText().toString().trim();
    }

    private String getLastName() {
        return lastNameEt.getText().toString().trim();
    }

    private String getEmail() {
        return emailEt.getText().toString().trim();
    }

    private String getPass() {
        return passEt.getText().toString();
    }

    private String getPhoneNo() {
        String aa = "";
        if (sp.getString(Config.VerificationType, "1").equals("1")) {
            aa = "";
        } else {
            aa = phoneInput.getText().toString().trim().replace("+44", "0");
        }
        return aa;
    }

    private void showProgress(CardView cv, TextView progressBarTextTv, ProgressBar pb) {
        pb.setVisibility(View.VISIBLE);
        progressBarTextTv.setVisibility(View.GONE);

        cv.setClickable(false);
        cv.setCardElevation(1);
        cv.setCardBackgroundColor(ContextCompat.getColor(Activity_Signup.this, R.color.disable_text));
    }

    private void resetProgress(CardView cv, TextView progressBarTextTv, ProgressBar pb) {
        cv.setClickable(false);
        pb.setVisibility(View.GONE);
        progressBarTextTv.setVisibility(View.VISIBLE);

        cv.setClickable(true);
        cv.setCardElevation(10);
        cv.setCardBackgroundColor(ContextCompat.getColor(Activity_Signup.this, R.color.color_inverse_black_footerBack));
    }
}
