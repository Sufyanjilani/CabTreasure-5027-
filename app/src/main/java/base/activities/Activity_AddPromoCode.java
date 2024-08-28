package base.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.eurosoft.customerapp.R;

import com.tfb.fbtoast.FBToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.listener.Listener_VerifyPromotionCode;
import base.manager.Manager_VerifyPromotionCode;
import base.models.ConfirmedBooking;
import base.models.LocAndField;
import base.models.Model_BookingDetailsModel;
import base.models.ParentPojo;
import base.models.SettingsModel;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_AddPromoCode extends AppCompatActivity {
    private SettingsModel settingsModel;
    private EditText inpPromo;
    private Button btnPromo;
    private String From, FromType, To, ToType;
    private SharedPreferences sp;
    private SweetAlertDialog sweetAlertDialog;
    private Model_BookingDetailsModel model;
    private String UpdatedFares;
    private ParentPojo p = new ParentPojo();
    private ArrayList<LocAndField> locAndFieldArrayList = new ArrayList<>();
    private Listener_VerifyPromotionCode listener_verifyPromotionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonMethods.getInstance().setDarkAndNightColorBlackWhite(Activity_AddPromoCode.this);
        setContentView(R.layout.layout_enter_promo);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        settingsModel = new SharedPrefrenceHelper(Activity_AddPromoCode.this).getSettingModel();
        btnPromo = findViewById(R.id.btnPromo);
        btnPromo.setText(p.getApply());
        inpPromo = findViewById(R.id.inpPromo);
        inpPromo.setHint(p.getEnterCodeHere());
        inpPromo.requestFocus();
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        locAndFieldArrayList.clear();
        locAndFieldArrayList = getIntent().getParcelableArrayListExtra("key_locAndFieldArrayList");

        try {
            From = getIntent().getStringExtra("From");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FromType = getIntent().getStringExtra("FromType");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            To = getIntent().getStringExtra("ToAddress");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ToType = getIntent().getStringExtra("ToType");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            UpdatedFares = getIntent().getStringExtra("UpdatedFares");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            model = (Model_BookingDetailsModel) getIntent().getSerializableExtra(CommonVariables.KEY_BOOKING_MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Promocode = inpPromo.getText().toString().trim();
                if (Promocode.equals("")) {
                    FBToast.errorToast(Activity_AddPromoCode.this, "Please enter code first!", FBToast.LENGTH_LONG);
                } else {
                    if (sp.getInt(Config.PROMO_ATTEMPT_COUNT, 0) >= 3) {
                        sweetAlertDialog = new SweetAlertDialog(Activity_AddPromoCode.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Too Many Invalid Attempts!")
                                .setContentText("You attempt 3 invalid code request so you cannot avail any promotion in this booking")
//                                .setCancelText("Cancel")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
//                                        onAddressChangeListener(true);
                                        //   new InitializeAppDb(mContext, true).execute();
                                    }

                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
//                                        mContext.finish();
                                    }

                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                    }
                                })
                                .show();
                    } else {
                        new Manager_VerifyPromotionCode(Activity_AddPromoCode.this, setAndGetConfirmedBooking(), listener_verifyPromotionCode).execute();
//                        new VerifyPromotionCode().execute(Promocode, From, FromType, To, ToType);
                    }
                }
            }
        });


        listener_verifyPromotionCode = new Listener_VerifyPromotionCode() {
            @Override
            public void onComplete(String result) {
                try {
                    if (result != null && !result.isEmpty()) {
                        try {
                            JSONObject parentObject = new JSONObject(result);

                            if (parentObject.getBoolean("HasError")) {
                                FBToast.errorToast(Activity_AddPromoCode.this, parentObject.getString("Message"), FBToast.LENGTH_LONG);
                                int codeCount = sp.getInt(Config.PROMO_ATTEMPT_COUNT, 0) + 1;
                                sp.edit().putInt(Config.PROMO_ATTEMPT_COUNT, codeCount).commit();
                            } else {
                                JSONObject promoObj = new JSONArray(parentObject.getString("Data")).getJSONObject(0);
                                if (promoObj.getString("Totaljourney").equals("0") || (!promoObj.getString("Totaljourney").equals("0") && !promoObj.getString("Totaljourney").equals(promoObj.getString("Used")))) {
                                    new DatabaseOperations(new DatabaseHelper(Activity_AddPromoCode.this)).deletePromoByCode(inpPromo.getText().toString().trim());
                                    new DatabaseOperations(new DatabaseHelper(Activity_AddPromoCode.this))
                                            .insertPromoCode(promoObj.getString("PromotionCode"),
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

                                    Intent intent = new Intent();
                                    intent.putExtra("pr_code", promoObj.getString("PromotionCode"));
                                    intent.putExtra("pr_code_minimum_fare", promoObj.getString("MinimumFare"));
                                    intent.putExtra("PromotionStartDateTime", promoObj.getString("PromotionStartDateTime"));
                                    intent.putExtra("PromotionEndDateTime", promoObj.getString("PromotionEndDateTime"));

                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    FBToast.errorToast(Activity_AddPromoCode.this, "Promotion Journey Limit Exceeded", FBToast.LENGTH_LONG);
                                }
                            }
                        } catch (Exception e) {
                            FBToast.errorToast(Activity_AddPromoCode.this, "Invalid Promo Code", FBToast.LENGTH_LONG);
                            int codeCount = sp.getInt(Config.PROMO_ATTEMPT_COUNT, 0) + 1;
                            sp.edit().putInt(Config.PROMO_ATTEMPT_COUNT, codeCount).commit();
                        }
                    } else {
                        FBToast.errorToast(Activity_AddPromoCode.this, "Please check your internet connection and try again later", FBToast.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ConfirmedBooking setAndGetConfirmedBooking() {
        ConfirmedBooking obj = new ConfirmedBooking();
        String deviceid = Settings.Secure.getString(Activity_AddPromoCode.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        obj.pickupDate = model.getPickUpDate();
        obj.pickupTime = model.getPickUpTime();
        obj.returnDate = model.getReturnDate();
        obj.returnTime = model.getReturnTime();
        obj.fromAddress = model.getFromAddress();
        obj.toAddress = model.gettoAddress();
        obj.customerName = model.getCusomerName();
        obj.customerEmail = model.getCusomerEmail();
        obj.customerPhoneNo = model.getCusomerPhone();
        obj.customerMobileNo = model.getCusomerMobile();
        obj.CustomerId = settingsModel.getUserServerID();
        obj.PaymentType = model.getPaymentType();
        obj.specialInstruction = model.getSpecialNotes();
        obj.journeyType = 1;
        obj.returnFareRate = "0.0";
        obj.fareRate = UpdatedFares;//  model.getOneWayFare();
        obj.specialInstruction = model.getSpecialNotes();
        obj.fromDoorNo = "";//model.getFromAddressType().toLowerCase() == "airport" ? model.getFromAddressFlightNo() : model.getFromAddressDoorNO();
        obj.vehicleName = model.getCar();
        obj.fromComing = model.getFromAddressCommingFrom();
        obj.viaAddress = model.getViaPointsAsString();
        obj.accountType = "";
        obj.accountId = ((model.getPaymentType().equalsIgnoreCase("account")) ? settingsModel.getAccountWebID() : "");
        obj.fromLocType = model.getFromAddressType();
        obj.toLocType = model.gettoAddressType();
        obj.key = "";

        obj.defaultClientId = (int) CommonVariables.clientid;
        obj.uniqueValue = CommonVariables.clientid + "4321orue";
        obj.UniqueId = deviceid;
        obj.DeviceInfo = "Android";
        obj.CustomerId = settingsModel.getUserServerID();
        obj.Email = settingsModel.getEmail();
        obj.PromotionCode = inpPromo.getText().toString().trim();
        return obj;
    }
}
