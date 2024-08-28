package base.activities;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eurosoft.customerapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tfb.fbtoast.FBToast;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import base.adapters.SubmitFormAdapter;
import base.fragments.Fragment_JobDetail;
import base.listener.Listener_List;
import base.listener.Listener_MyComplete;
import base.listener.Listener_SubmitForm;
import base.manager.Manager_GetListFromServer;
import base.manager.Manager_SubmitForm;
import base.models.Model_SubmitForm;
import base.models.SettingsModel;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_SubmitForm extends AppCompatActivity {

    private String methodName;

    private ImageView backIv;
    private TextView titleTv;
    private TextView refNoTv;
    private TextView progressBarTextTv;
    private EditText descriptionEt;

    private ProgressBar pb;
    private ProgressBar progressBar;
    private ScrollView viewsSV;
    private IntlPhoneInput phoneInput;
    private RecyclerView rv;
    private LinearLayout findLostItemLv;
    private CardView buttonWithProgress;
    private BottomSheetDialog dialog;

    private Listener_SubmitForm listener_submitForm;
    private Listener_List listener_list;
    private Listener_MyComplete listener_myComplete;
    private SubmitFormAdapter adapter;
    private ArrayList<Model_SubmitForm> arrayList;
    private SettingsModel settingsModel;
    private SharedPrefrenceHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_submit_form);
        setObjects();

        init();

        setText();

        listener();
    }

    private void setObjects() {
        CommonMethods.getInstance().setDarkAndNightColor(this);
//        getWindow().setStatusBarColor(Color.parseColor("#000000"));// set status background white
        helper = new SharedPrefrenceHelper(this);
        settingsModel = helper.getSettingModel();
        arrayList = new ArrayList<>();
        try {
            methodName = getIntent().getStringExtra("methodName");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setText() {
        findLostItemLv.setVisibility(methodName.equals("InquiryList") ? View.VISIBLE : View.GONE);

        refNoTv.setText("Booking Ref: " + Fragment_JobDetail.refNo);
        refNoTv.setVisibility(Fragment_JobDetail.refNo.equals("") ? View.GONE : View.VISIBLE);
    }

    private void init() {
        backIv = findViewById(R.id.backIv);
        titleTv = findViewById(R.id.titleTv);
        refNoTv = findViewById(R.id.refNoTv);

        rv = findViewById(R.id.complainRv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        findLostItemLv = findViewById(R.id.findLostItemLv);
        findLostItemLv.setVisibility(View.GONE);
        viewsSV = findViewById(R.id.viewsSV);
        viewsSV.setVisibility(View.GONE);

        phoneInput = (IntlPhoneInput) findViewById(R.id.my_phone_input);
        phoneInput.setTextColor(getResources().getColor(R.color.color_black_inverse));
        phoneInput.setHintColor(getResources().getColor(R.color.color_black_inverse));
        descriptionEt = findViewById(R.id.descriptionEt);

        progressBar = findViewById(R.id.progressBar);
        progressBarTextTv = findViewById(R.id.progressBarTextTv);
        pb = findViewById(R.id.pb);


        buttonWithProgress = findViewById(R.id.buttonWithProgress);
    }

    private void listener() {
        // show  list
        listener_list = new Listener_List() {
            @Override
            public void onComplete(String result) {
                pb.setVisibility(View.GONE);
                try {
                    JSONObject parentObject = new JSONObject(result);
                    String data = parentObject.getString("Data");
                    JSONArray datas = new JSONArray(data);
                    arrayList.clear();
                    for (int i = 0; i < datas.length(); i++) {
                        String ob = datas.getString(i);
                        arrayList.add(new Model_SubmitForm(ob, methodName));
                    }
                    loadList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // For show bottom sheet
        listener_myComplete = new Listener_MyComplete() {
            @Override
            public void onComplete(String result) {
                bottomSheet(result);
            }
        };

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (methodName.equals("InquiryList"))
                    startActivity(new Intent(Activity_SubmitForm.this, Activity_FinLost.class));
                finish();
            }
        });

//        progressBarTextTv.setTextColor(ContextCompat.getColor(this, R.color.color_black_inverse));
//        buttonWithProgress.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color_inverse_black_footerBack));

        buttonWithProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String text = "";
                ArrayList<Integer> selectedList = adapter.getDataList();
                for (int i = 0; i < selectedList.size(); i++)
                    text += arrayList.get(selectedList.get(i)).getText() + "|";

                if (selectedList.size() == 0) {
                    FBToast.warningToast(Activity_SubmitForm.this, "Please at least select one item form the list.", FBToast.LENGTH_SHORT);
                    return;
                }

                if (descriptionEt.getText().toString().trim().isEmpty()) {
                    FBToast.warningToast(Activity_SubmitForm.this, "Please enter your lost item description for verification.", FBToast.LENGTH_SHORT);
                    return;
                }

//                if (selectedList.size() == 0) {
//                    text = "other";
//                }

                submit("" + text, "" + descriptionEt.getText().toString());
            }
        });

        new Manager_GetListFromServer(this, methodName, listener_list).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void submit(String title, String desc) {
        String number = "";
        try {
            try {
                number = phoneInput.getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if (number.trim().equals("") && !settingsModel.getPhone().equals(""))
//                number = settingsModel.getPhone();

//            if (number.trim().equals("") && !settingsModel.getMobile().equals(""))
//                number = settingsModel.getMobile();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (methodName.equals("InquiryList") && number.isEmpty()) {
            FBToast.warningToast(Activity_SubmitForm.this, "Phone number is mandatory", FBToast.LENGTH_LONG);
            return;
        }

        showProgress(buttonWithProgress, progressBarTextTv, progressBar);

        HashMap<String, Object> map = new HashMap<>();
        map.put("defaultClientId", CommonVariables.clientid);
        map.put("uniqueValue", CommonVariables.clientid + "4321orue");
        map.put("DeviceInfo", "Android - " + android.os.Build.VERSION.RELEASE);
        map.put("JobId", Fragment_JobDetail.refNo);
        map.put("CustomerId", settingsModel.getUserServerID());
        map.put("CustomerName", settingsModel.getName());
        map.put("PhoneNo", number);
        map.put("LostTitle", title);
        map.put("LostDescription", desc.trim());

        Gson gson = new Gson();
        String data = gson.toJson(map);

        new Manager_SubmitForm(this, methodName.equals("ComplainList") ? methodName.replace("ComplainList", "ComplainSubmit") : methodName.replace("InquiryList", "InquirySubmit"), data, new Listener_SubmitForm() {
            @Override
            public void onComplete(String result) {
                resetProgress(buttonWithProgress, progressBarTextTv, progressBar);
                try {
                    JSONObject parentObject = new JSONObject(result);
                    if (parentObject.getBoolean("HasError")) {

                        new SweetAlertDialog(Activity_SubmitForm.this, SweetAlertDialog.ERROR_TYPE)
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
                        String data = parentObject.getString("Data");
                        if (data.length() > 0) {
                            new SweetAlertDialog(Activity_SubmitForm.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText(parentObject.getString("Data"))
                                    .setConfirmText("OK")
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
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void loadList() {
        adapter = new SubmitFormAdapter(this, arrayList, methodName.equals("ComplainList") ? listener_myComplete : null);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        viewsSV.setVisibility(View.VISIBLE);
    }

    private void bottomSheet(String message) {
        dialog = new BottomSheetDialog(this, R.style.DialogStyle);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_complaint, null);

        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(SOFT_INPUT_STATE_VISIBLE);
        dialog.setContentView(sheetView);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
//                BottomSheetDialog d = (BottomSheetDialog) dialog;
//                View bottomSheetInternal = d.findViewById(R.id.design_bottom_sheet);
//                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        dialog.show();

        EditText descriptionEt = sheetView.findViewById(R.id.descriptionEt);
        TextView promotitle = sheetView.findViewById(R.id.promotitle);
        promotitle.setText("" + message);

        TextView doneBtn = sheetView.findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = descriptionEt.getText().toString().trim();
                if (desc.length() == 0) {
                    descriptionEt.setError("Required");
                    return;
                }
                submit("" + message, "" + descriptionEt.getText().toString());
                dialog.dismiss();
            }
        });

    }

    private void showProgress(CardView cv, TextView progressBarTextTv, ProgressBar pb) {
        pb.setVisibility(View.VISIBLE);
        progressBarTextTv.setVisibility(View.GONE);

        cv.setClickable(false);
        cv.setCardElevation(1);
        cv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.disable_text));
    }

    private void resetProgress(CardView cv, TextView progressBarTextTv, ProgressBar pb) {
        cv.setClickable(false);
        pb.setVisibility(View.GONE);
        progressBarTextTv.setVisibility(View.VISIBLE);

        cv.setClickable(true);
        cv.setCardElevation(10);
        cv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color_inverse_black_footerBack));
    }
}
