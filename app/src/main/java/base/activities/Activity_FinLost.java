package base.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.eurosoft.customerapp.R;

import org.json.JSONObject;

import base.fragments.Fragment_JobDetail;
import base.listener.Listener_CallOffice;
import base.manager.Manager_CallOffice;
import base.utils.CommonMethods;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_FinLost extends AppCompatActivity {
    private static final String REGEX_NUMBER_SEPERATORS = "[\\,\\\\\\/\\-]";

    private CardView cardView1;
    private CardView cardView2;
    private ImageView backIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonMethods.getInstance().setDarkAndNightColor(this);

        setContentView(R.layout.layout_find_lost);


        cardView1 = findViewById(R.id.callDriverAboutLostItemCv);
        cardView2 = findViewById(R.id.findLostItemCv);

        backIv = findViewById(R.id.backIv);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!Fragment_JobDetail.refNo.equals("")) {
                        new Manager_CallOffice(Activity_FinLost.this, Fragment_JobDetail.refNo, new Listener_CallOffice() {
                            @Override
                            public void onComplete(String result) {
                                try {
                                    JSONObject parentObject = new JSONObject(result);
                                    if (parentObject.getBoolean("HasError")) {
                                        new SweetAlertDialog(Activity_FinLost.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("")
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
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.cancel();
                                                    }

                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                                    }
                                                })
                                                .show();
                                    } else {
                                        String number = parentObject.getString("Data");
                                        if (number.length() > 0) {
                                            final String[] numbers = number.split(REGEX_NUMBER_SEPERATORS);

                                            if (numbers != null && numbers.length > 1) {
                                                new AlertDialog.Builder(getApplicationContext()).setTitle("Select Number").setItems(numbers, new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(final DialogInterface dialog, int which) {

                                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", numbers[which].trim(), null));
                                                        startActivity(intent);
                                                    }
                                                }).show();

                                            } else {
                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number.trim(), null));
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_FinLost.this, Activity_SubmitForm.class).putExtra("methodName", "InquiryList"));
                finish();
            }
        });
    }
}
