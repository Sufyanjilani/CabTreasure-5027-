package base.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eurosoft.customerapp.R;
import com.tfb.fbtoast.FBToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.fragments.Fragment_Main;
import base.listener.Listener_MemberAccount;
import base.manager.Manager_MemberAccount;
import base.models.ParentPojo;
import base.models.SettingsModel;
import base.models.UserModel;
import base.newui.HomeFragment;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.SharedPrefrenceHelper;

public class Activity_AccountMemberLogin extends AppCompatActivity {

    TextView accname;


    public static final String CASH = "Cash";
    public static final String ACCOUNT = "Account";
    String deviceid, varificationip;

    String MobileNo, Email;
    ProgressDialog pdLoading;
    List<UserModel> usermodel = new ArrayList<UserModel>();
    Activity context = Activity_AccountMemberLogin.this;
    String METHOD_NAME = "GetWebAccountDetails", response;
    int userid = 0;
    boolean startup = false;
    boolean isBooking = false;

    // Views
    private TextView titleTv;
    private TextView accountLoginBtn;
    private TextView accountLogoutBtn;
    private ImageView backIv;
    private ImageView menuIv;

    private View view_line_1;
    private View view_line_2;
    private View view_line_3;

    private EditText accountIdEt;
    private EditText accountLoginIdEt;
    private EditText accountPasswordEt;
    private ImageView toggleImageForPasswordIv;

    private LinearLayout accountLoginLayoutLl;
    private LinearLayout accountLogoutLayoutLl;

    // Java
    private String accountId;
    private String accountLoginId;
    private String accountPassword;
    private boolean isShown = false;

    private SharedPreferences sp;
    private SharedPrefrenceHelper sharedPrefrenceHelper;

    private ParentPojo p = new ParentPojo();
    private Context mContext = Activity_AccountMemberLogin.this;
    private Listener_MemberAccount listener_memberAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_account_login);
//        setDarkAndNightThemeColor();
//        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.color_gray_and_black_inverse));

        CommonMethods.getInstance().setDarkAndNightColor(Activity_AccountMemberLogin.this);

        initObject();
        init();
        initData();
        listener();
    }

    private void initObject() {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefrenceHelper = new SharedPrefrenceHelper(Activity_AccountMemberLogin.this);
        startup = getIntent() != null && getIntent().hasExtra("startup");
        isBooking = getIntent() != null && getIntent().hasExtra("isBooking");

        pdLoading = new ProgressDialog(this);
        usermodel = new ArrayList<UserModel>();
    }

    private void init() {
        toggleImageForPasswordIv = findViewById(R.id.toggleImageForPasswordIv);

        titleTv = findViewById(R.id.titleTv);
        titleTv.setText(p.getAccountDetails());

        backIv = findViewById(R.id.backIv);
        backIv.setVisibility(View.VISIBLE);

/*        menuIv = findViewById(R.id.menuIv);
        menuIv.setVisibility(View.GONE);*/

        accountIdEt = findViewById(R.id.accountIdEt);
        accountLoginIdEt = findViewById(R.id.accountLoginIdEt);
        accountPasswordEt = findViewById(R.id.accountPasswordEt);

        accountLoginBtn = findViewById(R.id.accountLoginBtn);
        accountLogoutBtn = findViewById(R.id.accountLogoutBtn);

        accname = findViewById(R.id.accname);
        accountLoginLayoutLl = findViewById(R.id.accountLoginLayoutLl);
        accountLogoutLayoutLl = findViewById(R.id.accountLogoutLayoutLl);

        view_line_1 = findViewById(R.id.view_line_1);
        view_line_2 = findViewById(R.id.view_line_2);
        view_line_3 = findViewById(R.id.view_line_3);

    }

    public void initData() {

        if (sp.getBoolean(CommonVariables.ISMEMBERUSERLOGIN, false)) {
            accountLogoutLayoutLl.setVisibility(View.VISIBLE);
            accountLoginLayoutLl.setVisibility(View.GONE);
            accountLoginBtn.setVisibility(View.GONE);

            try {
                accname.setText(sp.getString(CommonVariables.MEMBERACCNAME, "-"));
            } catch (Exception e) {
                accountLogoutLayoutLl.setVisibility(View.GONE);
                accountLoginLayoutLl.setVisibility(View.VISIBLE);
                accountLoginBtn.setVisibility(View.VISIBLE);
            }

        } else {
            accountLogoutLayoutLl.setVisibility(View.GONE);
            accountLoginLayoutLl.setVisibility(View.VISIBLE);
            accountLoginBtn.setVisibility(View.VISIBLE);
        }

        if (accountIdEt.getText().toString().length() == 0) {
            accountIdEt.setFocusable(true);
            accountIdEt.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        //        if (accountPasswordEt.getText().toString().length() == 0) {
//            accountPasswordEt.setFocusable(true);
//            accountPasswordEt.requestFocus();
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//        }

        view_line_1.setVisibility((accountIdEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);
        view_line_2.setVisibility((accountLoginIdEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);
        view_line_3.setVisibility((accountPasswordEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);
    }

    private void clearEt(EditText editText) {
        try {
            editText.setOnTouchListener(new View.OnTouchListener() {
                final int DRAWABLE_RIGHT = 2;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int leftEdgeOfRightDrawable = editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                        if (event.getRawX() >= leftEdgeOfRightDrawable) {
                            editText.requestFocus();
                            editText.setText("");
                            return true;
                        }
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void listener() {

        clearEt(accountIdEt);
        clearEt(accountLoginIdEt);

        listener_memberAccount = new Listener_MemberAccount() {
            @Override
            public void onComplete(String result) {
                try {
                    if (result != null && !result.isEmpty()) {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("ResponseMessage") != null && !jsonObject.getString("ResponseMessage").equals("") && !jsonObject.getString("ResponseMessage").equals("null")) {
//                            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
//                                    .setTitleText("Activity_Login Failed!")
//                                    .setContentText(jsonObject.getString("ResponseMessage"))
//                                    .setConfirmText("OK")
//                                    .showCancelButton(false)
//                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sDialog) {
//                                            sDialog.dismissWithAnimation();
//
//                                        }
//
//                                        @Override
//                                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
//
//                                        }
//                                    })
//                                    .show();
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SettingsModel userSettings = new SharedPrefrenceHelper(getApplicationContext()).getSettingModel();
                        if (jsonObject.getString("CompanyName") != null && !jsonObject.getString("CompanyName").equals("null")) {
                            userSettings.setAccountWebID(jsonObject.getString("CompanyID"));
                            Toast.makeText(getApplicationContext(), "Account Verified", Toast.LENGTH_SHORT).show();
                            userSettings.setVerified(true);
                            userSettings.setAccountNo(jsonObject.getString("AccountNo"));
                            userSettings.setAccountWebID(jsonObject.getString("CompanyID"));
                            userSettings.setAddress(jsonObject.getString("CompanyAddress"));

                            SharedPreferences.Editor edit = sp.edit();
                            edit.putBoolean(CommonVariables.ISMEMBERUSERLOGIN, true);
                            edit.putString(CommonVariables.MEMBERACCNAME, jsonObject.getString("CompanyName"));
                            edit.putString(CommonVariables.MEMBERACCEMAIL, userSettings.getEmail());
                            edit.commit();
                            SharedPreferences prefs = getSharedPreferences("Store Data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("LoginId", jsonObject.getString("LoginID"));
                            editor.commit();

                            try {
                                sharedPrefrenceHelper.putSettingModel(userSettings);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                SettingsModel model = sharedPrefrenceHelper.getSettingModel();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            accname.setText(jsonObject.getString("CompanyName"));
                            accountLoginBtn.setVisibility(View.GONE);
                            accountLoginLayoutLl.setVisibility(View.GONE);
                            accountLogoutLayoutLl.setVisibility(View.VISIBLE);
                            if (startup) {
                                startActivity(new Intent(Activity_AccountMemberLogin.this, Fragment_Main.class));
                                finish();
                            } else if (isBooking) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    } else if (response != null && !response.isEmpty()) {
                        FBToast.errorToast(mContext, response, Toast.LENGTH_SHORT);
                    } else {
                        FBToast.errorToast(mContext, "Unable to connect to service, Please check your internet connection", Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        accountIdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                view_line_1.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE));
            }
        });
        accountLoginIdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                view_line_2.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE));
            }
        });
        accountPasswordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                view_line_3.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE));
            }
        });


        accountLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                accountId = accountIdEt.getText().toString();
                accountLoginId = accountLoginIdEt.getText().toString();
                accountPassword = accountPasswordEt.getText().toString();

                try {
                    boolean validation = true;
                    String problem = "", pass = "12345fasiha";

                    if (deviceid == null || deviceid.equals("")) {
                        deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    }

                    usermodel = new DatabaseOperations(new DatabaseHelper(getApplicationContext())).getUsers();

                    if (usermodel.size() > 0) {
                        for (int i = 0; i < usermodel.size(); i++) {
                            if (usermodel.get(i).getname().equals(accountId)) {
                                userid = i;
                                break;
                            }
                        }

                        MobileNo = usermodel.get(userid).getmobileno();
                        varificationip = usermodel.get(userid).getip();
                        Email = usermodel.get(userid).getEmail();
                    }

                    if (accountId.equals("")) {
                        validation = false;
                        problem = "Please Write User Name";
                        accountIdEt.setError("Please enter account id");
                    }

                    if (accountLoginId.equals("")) {
                        problem = "Please Write Activity_Login id";
                        accountLoginIdEt.setError("Please enter login id");
                        validation = false;
                    }

                    if (accountPassword.equals("")) {
                        validation = false;

                        problem = "Please Write Password";
                        accountPasswordEt.setError("Please enter password");
                    }
                    if (validation) {
                        new Manager_MemberAccount(Activity_AccountMemberLogin.this, accountId, accountLoginId, accountPassword, listener_memberAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        accountLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Activity_AccountMemberLogin.this).setTitle("Are You Sure You want to logout?")
                        .setPositiveButton(p.getYes(), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HomeFragment.isHomeVissibel = false;
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putBoolean(CommonVariables.ISMEMBERUSERLOGIN, false);
                                edit.putString(CommonVariables.paymentType, "cash");
                                edit.putBoolean("isGoogle", false);
                                edit.commit();

                                accountLogoutLayoutLl.setVisibility(View.GONE);
                                accountLoginBtn.setVisibility(View.VISIBLE);
                                accountLoginLayoutLl.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton(p.getNo(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startup) {
//                    startActivity(new Intent(Activity_AccountMemberLogin.this, Activity_Default_Payment.class));
                }
                //Animatoo.animateSlideRight(Activity_AccountMemberLogin.this);
                finish();
            }
        });

        toggleImageForPasswordIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShown) {
                    isShown = true;
                    accountPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    toggleImageForPasswordIv.setImageResource(R.drawable.ic_visibility_black_24dp);
                } else {
                    isShown = false;
                    accountPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    toggleImageForPasswordIv.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5
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
        if (startup) {
//            startActivity(new Intent(Activity_AccountMemberLogin.this, Activity_Default_Payment.class));
        }
        finish();
    }
}


