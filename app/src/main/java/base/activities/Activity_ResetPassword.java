package base.activities;

import static base.utils.CommonMethods.checkIfHasNullForString;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eurosoft.customerapp.R;
import com.tfb.fbtoast.FBToast;

import org.json.JSONObject;

import base.listener.Listener_Login;
import base.manager.Manager_ChangePasswordAppUserNew;
import base.models.SettingsModel;
import base.models.User;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_ResetPassword extends AppCompatActivity {
    private LinearLayout changePasswordLl;
    private EditText newPasswordEt, currentPasswordEt, confirmPasswordEt;
    private CheckBox togglePass;

    private View view_line_1;
    private View view_line_2;
    private View view_line_3;

    private boolean isShown1 = false;
    private boolean isShown2 = false;
    private boolean isShown3 = false;

    private ProgressBar progressBar;
    private TextView progressBarTextTv;

    private ImageView backIv;
    private ImageView toggleImageForPasswordIv1;
    private ImageView toggleImageForPasswordIv2;
    private ImageView toggleImageForPasswordIv3;
    private SettingsModel settingsModel;

    private Listener_Login listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonMethods.getInstance().setDarkAndNightColor(Activity_ResetPassword.this);

        setContentView(R.layout.layout_change_password);

        init();

        listener();

    }

    private void init() {
        settingsModel = new SharedPrefrenceHelper(Activity_ResetPassword.this).getSettingModel();

        toggleImageForPasswordIv1 = findViewById(R.id.toggleImageForPasswordIv1);
        toggleImageForPasswordIv2 = findViewById(R.id.toggleImageForPasswordIv2);
        toggleImageForPasswordIv3 = findViewById(R.id.toggleImageForPasswordIv3);

        progressBar = findViewById(R.id.progressBar);
        progressBarTextTv = findViewById(R.id.progressBarTextTv);
        progressBarTextTv.setText("Next");

        view_line_1 = findViewById(R.id.view_line_1);
        view_line_2 = findViewById(R.id.view_line_2);
        view_line_3 = findViewById(R.id.view_line_3);

        togglePass = findViewById(R.id.togglePass);

        currentPasswordEt = findViewById(R.id.currentPasswordEt);
        currentPasswordEt.requestFocus();
        currentPasswordEt.setFocusable(true);

        newPasswordEt = findViewById(R.id.newPasswordEt);
        confirmPasswordEt = findViewById(R.id.confirmPasswordEt);

        backIv = findViewById(R.id.backIv);
        changePasswordLl = findViewById(R.id.changePasswordLl);

        view_line_1.setVisibility((currentPasswordEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);
        view_line_2.setVisibility((newPasswordEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);
        view_line_2.setVisibility((confirmPasswordEt.hasFocus()) ? View.VISIBLE : View.INVISIBLE);


        currentPasswordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                view_line_1.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE));
            }
        });
        newPasswordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                view_line_2.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE));
            }
        });
        confirmPasswordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                view_line_3.setVisibility(((b) ? View.VISIBLE : View.INVISIBLE));
            }
        });
    }

    private void listener() {
        listener = new Listener_Login() {
            @Override
            public void onPre(String start) {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(confirmPasswordEt.getWindowToken(), 0);

                    progressBar.setVisibility(View.VISIBLE);
                    progressBarTextTv.setText("Please Wait .. ");
                    changePasswordLl.setClickable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPost(String response) {
                progressBar.setVisibility(View.GONE);
                progressBarTextTv.setText("Next");
                changePasswordLl.setClickable(true);

                response = checkIfHasNullForString(response);

                if (!response.equals("")) {
                    try {
                        JSONObject parentObject = new JSONObject(response);
                        if (parentObject.getBoolean("HasError")) {
                            new SweetAlertDialog(Activity_ResetPassword.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText(checkIfHasNullForString(parentObject.optString("Message")))
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
                            if (checkIfHasNullForString(parentObject.optString("TokenValidate")).equals("ValidToken")) {
                                new SweetAlertDialog(Activity_ResetPassword.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText(checkIfHasNullForString(parentObject.optString("Message")))
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                finish();
                                            }

                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                            }
                                        })
                                        .show();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    FBToast.errorToast(Activity_ResetPassword.this, "Check your internet connection and try again!", FBToast.LENGTH_SHORT);
                }
            }
        };

        toggleImageForPasswordIv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShown1) {
                    isShown1 = true;
                    currentPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    toggleImageForPasswordIv1.setImageResource(R.drawable.ic_visibility_black_24dp);
                } else {
                    isShown1 = false;
                    currentPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    toggleImageForPasswordIv1.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                }
            }
        });
        toggleImageForPasswordIv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShown2) {
                    isShown2 = true;
                    newPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    toggleImageForPasswordIv2.setImageResource(R.drawable.ic_visibility_black_24dp);
                } else {
                    isShown2 = false;
                    newPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    toggleImageForPasswordIv2.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                }
            }
        });
        toggleImageForPasswordIv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShown3) {
                    isShown3 = true;
                    confirmPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    toggleImageForPasswordIv3.setImageResource(R.drawable.ic_visibility_black_24dp);
                } else {
                    isShown3 = false;
                    confirmPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    toggleImageForPasswordIv3.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                }
            }
        });


        togglePass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // show
                    currentPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    newPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // hide
                    currentPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    newPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        confirmPasswordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (currentPasswordEt.getText().toString() != null && currentPasswordEt.getText().toString().equals("")) {
                        Toast.makeText(Activity_ResetPassword.this, "Please Enter Your Current Password.", Toast.LENGTH_SHORT).show();

                    } else if (newPasswordEt.getText().toString() != null && newPasswordEt.getText().toString().equals("")) {
                        Toast.makeText(Activity_ResetPassword.this, "Please Enter Your New Password.", Toast.LENGTH_SHORT).show();
                        return false;
                    } else if (confirmPasswordEt.getText().toString() != null && confirmPasswordEt.getText().toString().equals("")) {
                        Toast.makeText(Activity_ResetPassword.this, "Please Confirm Your New Password.", Toast.LENGTH_SHORT).show();
                        return false;
                    } else if (confirmPasswordEt.getText().toString() != null && !confirmPasswordEt.getText().toString().equals("") &&
                            !confirmPasswordEt.getText().toString().equals(newPasswordEt.getText().toString())) {
                        Toast.makeText(Activity_ResetPassword.this, "Password not matched!", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        callChangePassword();
                    }
                }
                return false;
            }
        });

        changePasswordLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPasswordEt.getText().toString() != null &&
                        currentPasswordEt.getText().toString().equals("")) {
                    Toast.makeText(Activity_ResetPassword.this, "Please Enter Your Current Password.", Toast.LENGTH_SHORT).show();

                } else if (newPasswordEt.getText().toString() != null &&
                        newPasswordEt.getText().toString().equals("")) {
                    Toast.makeText(Activity_ResetPassword.this, "Please Enter Your New Password.", Toast.LENGTH_SHORT).show();
                    return;

                } else if (confirmPasswordEt.getText().toString() != null && confirmPasswordEt.getText().toString().equals("")) {
                    Toast.makeText(Activity_ResetPassword.this, "Please Confirm Your New Password.", Toast.LENGTH_SHORT).show();
                    return;

                } else if (confirmPasswordEt.getText().toString() != null && !confirmPasswordEt.getText().toString().equals("") &&
                        !confirmPasswordEt.getText().toString().equals(newPasswordEt.getText().toString())) {
                    Toast.makeText(Activity_ResetPassword.this, "Password not matched!", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    callChangePassword();
                }
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void callChangePassword() {
        User user = new User();
        user.setPhoneNo(settingsModel.getPhoneNo());
        user.setUserName(settingsModel.getName());
        user.setEmail(settingsModel.getEmail());
        user.setPasswrd(currentPasswordEt.getText().toString().trim());
        user.setUniqueId(settingsModel.getUniqueId());
        user.setDeviceInfo(settingsModel.getDeviceInfo());
        user.setPasswrd(currentPasswordEt.getText().toString().trim());
        user.setCustomerId(settingsModel.getUserServerID());
        user.setNewPassword(newPasswordEt.getText().toString().trim());
        user.setDefaultClientId("" + CommonVariables.clientid);
        user.setSubCompanyId(CommonVariables.SUB_COMPANY);
        user.setUniqueValue(CommonVariables.clientid + "4321orue");
        user.setAddress("");
        user.setTelephone("");
        user.setSendSMS("");
        user.setPickDetails("yes");
        user.setHashKey("4321orue");

        new Manager_ChangePasswordAppUserNew(user, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (progressBar.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
    }

}
