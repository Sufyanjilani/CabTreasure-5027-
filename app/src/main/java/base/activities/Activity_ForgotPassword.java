package base.activities;

import static base.utils.CommonMethods.checkIfHasNullForString;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.eurosoft.customerapp.R;
import com.tfb.fbtoast.FBToast;

import org.json.JSONObject;

import base.listener.Listener_ForgetPassword;
import base.manager.Manager_ForgetPassword;
import base.models.ParentPojo;
import base.utils.CommonMethods;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_ForgotPassword extends AppCompatActivity {
    //    private LinearLayout forgotBtn;
    private CardView buttonWithProgress;
    private EditText emailEt;
    private ImageView backIv;
    private ProgressBar progressBar;
    private TextView progressBarTextTv;
    private TextView forgotLabelTv;
    private TextView forgotDetailLabel;
    private ParentPojo p = new ParentPojo();
    private Listener_ForgetPassword listener_forgetPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonMethods.getInstance().setDarkAndNightColor(Activity_ForgotPassword.this);

        setContentView(R.layout.layout_forgot_password);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        init();

        listener();
    }

    private void init() {
        forgotLabelTv = findViewById(R.id.forgotLabelTv);
        forgotLabelTv.setText(p.getForgetHeaderText());
        forgotDetailLabel = findViewById(R.id.forgotDetailLabel);
        forgotDetailLabel.setText(p.getForgetSubHeading());


        progressBar = findViewById(R.id.progressBar);
        progressBarTextTv = findViewById(R.id.progressBarTextTv);
        progressBarTextTv.setText(p.getNextBtnText());

        backIv = findViewById(R.id.backIv);
        emailEt = findViewById(R.id.emailEt);
        emailEt.requestFocus();
        emailEt.setHint(p.getEmailHintEt());

//        forgotBtn = findViewById(R.id.forgotBtn);
        buttonWithProgress = findViewById(R.id.buttonWithProgress);
    }

    private void listener() {
        listener_forgetPassword = new Listener_ForgetPassword() {
            @Override
            public void onPre(String start) {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(emailEt.getWindowToken(), 0);
                    showProgress(buttonWithProgress, progressBarTextTv, progressBar);
                    backIv.setClickable(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPost(String result) {
                backIv.setClickable(true);
                resetProgress(buttonWithProgress, progressBarTextTv, progressBar);

                try {
                    if (!result.startsWith("error_")) {
                        JSONObject parentObject = new JSONObject(result);
                        if (parentObject.getBoolean("HasError")) {
                            String msg = checkIfHasNullForString(parentObject.optString("Message"));
                            msg = msg.replace("Invalid", "Unregistered");
                            new SweetAlertDialog(Activity_ForgotPassword.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Unable to get details")
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
                            JSONObject dataObject = parentObject.optJSONObject("Data");
                            if (dataObject != null) {
                                emailEt.setText("");
                                new SweetAlertDialog(Activity_ForgotPassword.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
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
                                FBToast.errorToast(Activity_ForgotPassword.this, checkIfHasNullForString(parentObject.optString("Message")), FBToast.LENGTH_SHORT);
                            }
                        }
                    } else {
                        result = result.replace("error_", "");
                        FBToast.errorToast(Activity_ForgotPassword.this, result, FBToast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        emailEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (emailEt.getText().toString() == null || !emailEt.getText().toString().contains("@")) {
                        emailEt.setError("Please Enter Email.");
                        Toast.makeText(Activity_ForgotPassword.this, "Please Enter Email.", Toast.LENGTH_SHORT).show();
                    } else {
                        new Manager_ForgetPassword(emailEt.getText().toString().trim(), listener_forgetPassword).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
                return false;
            }
        });

        buttonWithProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEt.getText().toString() == null || !emailEt.getText().toString().contains("@")) {
                    emailEt.setError("Please Enter Email.");
                    Toast.makeText(Activity_ForgotPassword.this, "Please Enter Email.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new Manager_ForgetPassword(emailEt.getText().toString().trim(), listener_forgetPassword).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (progressBar.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
    }

    private void showProgress(CardView cv, TextView progressBarTextTv, ProgressBar pb) {
        pb.setVisibility(View.VISIBLE);
        progressBarTextTv.setVisibility(View.GONE);
        cv.setClickable(false);
        cv.setCardElevation(1);
        cv.setCardBackgroundColor(ContextCompat.getColor(Activity_ForgotPassword.this, R.color.disable_text));
    }

    private void resetProgress(CardView cv, TextView progressBarTextTv, ProgressBar pb) {
        cv.setClickable(false);
        pb.setVisibility(View.GONE);
        progressBarTextTv.setVisibility(View.VISIBLE);

        cv.setClickable(true);
        cv.setCardElevation(10);
        cv.setCardBackgroundColor(ContextCompat.getColor(Activity_ForgotPassword.this, R.color.color_inverse_black_footerBack));
    }
}
