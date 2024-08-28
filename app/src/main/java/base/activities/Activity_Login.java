package base.activities;

import static base.utils.CommonMethods.checkIfHasNullForString;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

/*import com.blogspot.atifsoftwares.animatoolib.Animatoo;*/
import com.eurosoft.customerapp.R;
import com.tfb.fbtoast.FBToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.fragments.Fragment_Main;
import base.listener.Listener_Login;
import base.manager.Manager_Login;
import base.models.CardJudoModel;
import base.models.ParentPojo;
import base.models.SettingsModel;
import base.models.User;
import base.models.UserModel;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_Login extends AppCompatActivity {

    private EditText emailEt;
    private EditText passEt;
    private TextView forgetPassTv;
    private ImageView backIv;
    private ImageView toggleImageForPasswordIv;
    private CardView buttonWithProgress;

    // JAVA
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private SharedPrefrenceHelper sharedPrefrenceHelper;
    private DatabaseHelper databaseHelper;
    private DatabaseOperations operations;
    private SettingsModel model;

    private String email, pass, MobileNo, Email;
    private String deviceid;
    private int userid = 0;
    private boolean isShown = false;
    private View view_line_1;
    private View view_line_2;
    private ProgressBar progressBar;
    private TextView progressBarTextTv;
    private TextView loginLabelTv;
    private TextView loginDetailLabelTv;
    private ParentPojo p;
    private List<UserModel> usermodel = new ArrayList<UserModel>();
    private Listener_Login listener_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setStatusBarColor(Color.parseColor("#000000"));// set status background white
//        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.color_gray_and_black_inverse));// set status background white

        CommonMethods.getInstance().setDarkAndNightColor(Activity_Login.this);

        setContentView(R.layout.layout_login);

        initObject();

        init();

        listener();
    }

    private void init() {
        loginLabelTv = findViewById(R.id.loginLabelTv);
        loginLabelTv.setText(p.getLoginBtnText());

        loginDetailLabelTv = findViewById(R.id.loginDetailLabelTv);
        loginDetailLabelTv.setText(p.getLoginSubHeading());

        progressBar = findViewById(R.id.progressBar);
        progressBarTextTv = findViewById(R.id.progressBarTextTv);
        progressBarTextTv.setText(p.getNextBtnText());

        view_line_1 = findViewById(R.id.view_line_1);
        view_line_2 = findViewById(R.id.view_line_2);
        toggleImageForPasswordIv = findViewById(R.id.toggleImageForPasswordIv);

        emailEt = findViewById(R.id.emailEt);
        emailEt.setHint(p.getEmailHintEt());

        passEt = findViewById(R.id.passEt);
        passEt.setHint(p.getPasswordHintEt());

        backIv = findViewById(R.id.backIv);

        forgetPassTv = findViewById(R.id.forgetPassTv);
        forgetPassTv.setText(p.getForgotPassText());

        buttonWithProgress = findViewById(R.id.buttonWithProgress);

        initData();
    }

    private void initObject() {
        p = new ParentPojo();
        databaseHelper = new DatabaseHelper(Activity_Login.this);
        operations = new DatabaseOperations(databaseHelper);

        sharedPrefrenceHelper = new SharedPrefrenceHelper(Activity_Login.this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        edit = sp.edit();
    }

    private void initData() {


        if (!sp.getString(CommonVariables.USerLogin, "").equals("")) {
            String name = sp.getString(CommonVariables.USerLogin, "");
            emailEt.setText(name);
        }

        if (emailEt.getText().toString().length() == 0) {
            emailEt.setFocusable(true);
            emailEt.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else {
            passEt.setFocusable(true);
            passEt.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        view_line_1.setVisibility((emailEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);
        view_line_2.setVisibility((passEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);

        emailEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                view_line_1.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE));
            }
        });

        try {
            emailEt.setOnTouchListener(new View.OnTouchListener() {
                final int DRAWABLE_RIGHT = 2;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int leftEdgeOfRightDrawable = emailEt.getRight() - emailEt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                        if (event.getRawX() >= leftEdgeOfRightDrawable) {
                            emailEt.requestFocus();
                            emailEt.setText("");
                            return true;
                        }
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        passEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                view_line_2.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE));
            }
        });
    }

    private void listener() {

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        forgetPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_ForgotPassword.class));
            }
        });

        buttonWithProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        passEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        login();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

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

        listener_login = new Listener_Login() {
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
                            new SweetAlertDialog(Activity_Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Login Failed!")
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

                            JSONObject dataObject = parentObject.optJSONObject("Data");
                            if (dataObject != null) {
                                JSONObject userDetails = dataObject.optJSONObject("userDetails");
                                if (userDetails != null) {
                                    ArrayList<String> userDetailList = new ArrayList<>();
                                    userDetailList.addAll(operations.getUser(email));

                                    if (userDetailList.size() == 0) {
                                        userDetailList.add(email);
                                        userDetailList.add(Email);
                                        userDetailList.add(MobileNo);
                                    }

                                    model = sharedPrefrenceHelper.getSettingModel();

                                    String serverId = checkIfHasNullForString(userDetails.optString("CustomerId"));
                                    model.setName(checkIfHasNullForString(userDetails.optString("UserName")));
                                    model.setEmail(checkIfHasNullForString(userDetails.optString("Email")));
                                    model.setPhoneNo(checkIfHasNullForString(userDetails.optString("PhoneNo")));
                                    model.setAddress(checkIfHasNullForString(userDetails.optString("Address")));
                                    if (checkIfHasNullForString(model.getPhoneNo()).equals("")) {
                                        model.setPhoneNo(checkIfHasNullForString(userDetails.optString("Telephone")));
                                    }
                                    model.setUserServerID(serverId);
                                    model.setPassword(pass);

                                    try {
                                        JSONObject promoObj = userDetails.optJSONObject("JobPromotion");
                                        if (promoObj != null) {
                                            operations.deletePromo();
                                            operations.insertPromoCode(
                                                    promoObj.getString("PromotionCode"),
                                                    promoObj.getString("PromotionTitle"),
                                                    promoObj.getString("PromotionMessage"),
                                                    promoObj.getString("PromotionStartDateTime"),
                                                    promoObj.getString("PromotionEndDateTime"),
                                                    promoObj.getString("DiscountTypeId"),
                                                    promoObj.getString("Charges"),
                                                    promoObj.getString("PromotionId"),
                                                    promoObj.getString("Totaljourney"),
                                                    promoObj.getString("Used"),
                                                    promoObj.getString("PromotionTypeID"),
                                                    promoObj.getString("MaximumDiscount"),
                                                    promoObj.getString("MinimumFare"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (!model.getEmail().equalsIgnoreCase(sp.getString(CommonVariables.MEMBERACCEMAIL, "").toLowerCase())) {
                                        edit.putString(CommonVariables.MEMBERACCEMAIL, "");
                                        edit.putString(CommonVariables.MEMBERACCNAME, "");
                                        edit.putBoolean(CommonVariables.ISMEMBERUSERLOGIN, false);
                                        edit.commit();

                                        model.setAccountNo("");
                                        model.setAccountWebID("");
                                        model.setAddress("");
                                    }
                                    sharedPrefrenceHelper.putSettingModel(model);

                                    try {
                                        String pickDetail = checkIfHasNullForString(userDetails.optString("PickDetails"));
                                        Log.d("TAG", "onPostExecute:  " + pickDetail);
//                        Token=odbCUVYqzWGtL9YO38PhdwoWOBDezxJq<<<consumer=fasfas<<<consumertoken=asfasfas<<<lastfour=1009<enddate=0421
                                        if (!pickDetail.equals("")) {
                                            if (pickDetail.contains("<<<")) {
                                                String[] CardData = userDetails.getString("PickDetails").split("<<<");
                                                boolean isReceiptId = userDetails.getString("PickDetails").toLowerCase().contains("receiptid");
                                                for (int i = 0; i < CardData.length; i++) {
                                                    CardData[i] = CardData[i].replace("Token|", "");
                                                    CardData[i] = CardData[i].replace("enddate|", "");
                                                    CardData[i] = CardData[i].replace("consumer|", "");
                                                    CardData[i] = CardData[i].replace("consumertoken|", "");
                                                    CardData[i] = CardData[i].replace("lastfour|", "");
                                                    CardData[i] = CardData[i].replace("type|", "");
                                                    CardData[i] = CardData[i].replace("is3DS|", "");
                                                    CardData[i] = CardData[i].replace("receiptid|", "");
                                                }

                                                CardJudoModel cardJudoModel = new CardJudoModel();
                                                cardJudoModel.setToken(CardData[0]);
                                                if (!CardData[4].contains("/")) {
                                                    try {
                                                        String[] carddata = CardData[4].split("(?!^)");
                                                        CardData[4] = carddata[0] + carddata[1] + "/" + carddata[2] + carddata[3];
                                                    } catch (Exception e) {
                                                    }
                                                }
                                                cardJudoModel.setEndDate(CardData[4]);
                                                cardJudoModel.setLastFour(CardData[3]);
                                                cardJudoModel.setConsumerToken(CardData[2]);
                                                cardJudoModel.setConsumerReference(CardData[1]);
                                                try {
                                                    if (isReceiptId) {
                                                        cardJudoModel.setReceiptid(CardData[5]);
                                                    }
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    cardJudoModel.setType(Integer.parseInt(CardData[5]));
                                                } catch (Exception e) {
                                                }

                                                try {
                                                    cardJudoModel.set3DS(CardData[6].equals("true"));
                                                } catch (Exception e) {
                                                }
                                                sharedPrefrenceHelper.saveCardJudoModel(cardJudoModel);
                                            } else {
                                                sharedPrefrenceHelper.putStripeCustomerId(pickDetail);
                                                sharedPrefrenceHelper.removeLastReciept();
                                            }
                                        } else {
                                            sharedPrefrenceHelper.putStripeCustomerId(pickDetail);
                                            sharedPrefrenceHelper.removeLastReciept();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                edit.putString(CommonVariables.USerLogin, email);
                                edit.putBoolean(CommonVariables.ISUSERLOGIN, true);
                                edit.putString(CommonVariables.paymentType, "cash");
                                edit.commit();
                                startActivity(new Intent(getApplicationContext(), Fragment_Main.class));
                                finish();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    FBToast.errorToast(getApplicationContext(), "Please check your internet connection and try again!", FBToast.LENGTH_LONG);
                }
            }
        };
    }

    private void login() {
        boolean validation = true;
        if (deviceid == null || deviceid.equals("")) {
            deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        email = emailEt.getText().toString().trim();
        pass = passEt.getText().toString();

        if (emailEt.getText().toString().length() == 0) {
            emailEt.setError("Required");
            return;
        }

        if (passEt.getText().toString().length() == 0) {
            passEt.setError("Required");
            return;
        }

        usermodel = new ArrayList<UserModel>();
        usermodel = operations.loginUser(email, pass);

        if (usermodel.size() > 0) {
            MobileNo = usermodel.get(userid).getmobileno();
            Email = usermodel.get(userid).getEmail();
        }

        if (validation) {
            User user = new User();
            user.setPhoneNo(MobileNo);
            user.setUserName(email);
            user.setEmail(email);
            user.setPasswrd(pass);
            user.setPickDetails("yes");
            user.setSubCompanyId(CommonVariables.SUB_COMPANY);
            user.setDefaultClientId("" + CommonVariables.clientid);
            user.setHashKey("4321orue");
            user.setUniqueValue(CommonVariables.clientid + "4321orue");
            new Manager_Login(user, listener_login).execute();
        } else {
            FBToast.errorToast(getApplicationContext(), "Invalid Email/Password", FBToast.LENGTH_SHORT);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, Activity_Start.class));
        //Animatoo.animateSlideRight(this);
        finish();
    }

    private void showProgress(CardView cv, TextView progressBarTextTv, ProgressBar pb) {
        pb.setVisibility(View.VISIBLE);
        progressBarTextTv.setVisibility(View.GONE);

        cv.setClickable(false);
        cv.setCardElevation(1);
        cv.setCardBackgroundColor(ContextCompat.getColor(Activity_Login.this, R.color.disable_text));
    }

    private void resetProgress(CardView cv, TextView progressBarTextTv, ProgressBar pb) {
        cv.setClickable(false);
        pb.setVisibility(View.GONE);
        progressBarTextTv.setVisibility(View.VISIBLE);

        cv.setClickable(true);
        cv.setCardElevation(10);
        cv.setCardBackgroundColor(ContextCompat.getColor(Activity_Login.this, R.color.color_inverse_black_footerBack));
    }
}
