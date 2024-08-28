package base.fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
import static base.utils.CommonMethods.checkIfHasNullForString;
import static base.utils.CommonVariables.CurrencySymbol;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.amalbit.trail.RouteOverlayView;
import com.amalbit.trail.contract.GooglemapProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.eurosoft.customerapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tfb.fbtoast.FBToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

import base.activities.Activity_FinLost;
import base.activities.Activity_HomePayment;
import base.activities.Activity_SubmitForm;
import base.listener.Listener_GetAddressCoordinate;
import base.listener.Listener_GetDirectionManager;
import base.listener.Listener_GetDriverJourneyDetailsManager;
import base.listener.Listener_SubmitFeedback;
import base.manager.Manager_GetAddressCoordinates;
import base.manager.Manager_GetDriverJourneyDetails;
import base.manager.Manager_StripePrePayment;
import base.manager.Manager_SubmitFeedback;
import base.models.ClsLocationData;
import base.models.DriverInformation;
import base.models.FeedbackInformation;
import base.models.LocAndField;
import base.models.Model_BookingDetailsModel;
import base.models.ParentPojo;
import base.models.SettingsModel;
import base.models.ShareTracking;
import base.models.Stripe_Model;
import base.newui.HomeFragment;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

//import base.org.linphone.activities.LinphoneLauncherActivity;


public class Fragment_JobDetail extends Fragment {

    public static  boolean isVisiable = false;
    private static final String COMPLETED = "completed";
    private static final String CANCELLED = "cancelled";

    private TextView statusHeadingLabel;
    private TextView bookingStatusTv;
    private TextView bookingRefNoTv;
    private TextView faresTv;
    private TextView journeyTv;
    private TextView paymentTypeTv;
    private TextView vehicleName;
    private TextView pickUpTitleTv;
    private TextView pickUpSubTitleTv;
    private TextView dropOffTitleTv;
    private TextView dropOffSubTitleTv;

    private TextView receiptLabelTv;
    private TextView journeyLabel;
    private TextView fareLabel;
    private TextView paymentLabel;
    private TextView vehicleLabel;
    private TextView vehicleNameLabel;
    private TextView vehicleNoPlateLabel;
    private TextView ratingHead;
    private TextView dateTimeTv;
    private TextView viaPointsTv;
    private TextView addATipTv;
    private TextView recenterTv;

    private TextView driveNameTv;
    private TextView driverRatingTv;
    private TextView vehicleNameTv;
    private TextView vehicleNoPlateTv;

    private LinearLayout receiptLl;
    private LinearLayout driverDetailCv;
    private LinearLayout shoppingLabelLl;
    private LinearLayout viaLl;

    private RelativeLayout center_markerlayout;
    private CardView findLostItemCv;
    private CardView reportSafetyItemCv;


    private ImageView backIv;
    private ImageView driverImageIv;

    private RatingBar simpleRatingBar;

    // STRING
    private String lat_ = "";
    private String Dlat_ = "";

    // CLASS
    FeedbackInformation feedbackInformation;
    private ParentPojo p;
    private SharedPreferences sp;
    private ArrayList<String> params;
    private SharedPrefrenceHelper mHelper;
    private SupportMapFragment mapFragment;
    private RouteOverlayView mapOverlayView;
    private Model_BookingDetailsModel mBookingDetails;
    private DriverInformation driverInformation;
    private SettingsModel settingModel;
    private GoogleMap mMap;
    public ArrayList<LocAndField> locAndFieldArrayList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;

    // LISTENER
    private Listener_GetDirectionManager directionManagerListener;
    private Listener_GetDriverJourneyDetailsManager driverJourneyDetailsManagerListener;
    private Listener_SubmitFeedback listenerSubmitFeedback;
    private Listener_GetAddressCoordinate listener_getAddressCoordinate;
    public static String refNo;

    private void setDarkAndNightThemeColor1() {
        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.color_white_inverse));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
//                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.red));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setObjects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.layout_history_job, container, false);
//        getActivity().getWindow().setStatusBarColor(Color.parseColor("#000000"));// set status background white


        setDarkAndNightThemeColor1();

        init(ret);

        setInitData();

        listener();
        isVisiable = true;
        return ret;
    }

    private void setObjects() {
        params = new ArrayList();
        p = new ParentPojo();

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mHelper = new SharedPrefrenceHelper(getActivity());
        settingModel = mHelper.getSettingModel();

        if (getArguments() != null) {
            mBookingDetails = (Model_BookingDetailsModel) getArguments().getSerializable(CommonVariables.KEY_BOOKING_MODEL);
            refNo = mBookingDetails.getRefrenceNo();
        }
    }

    private void setDarkAndNightMapStyleColor(GoogleMap mMap) {
        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_night_style));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }

    }

    private void init(View ret) {

        recenterTv = ret.findViewById(R.id.recenterTv);
        findLostItemCv = ret.findViewById(R.id.findLostItemCv);
        reportSafetyItemCv = ret.findViewById(R.id.reportSafetyItemCv);

        center_markerlayout = ret.findViewById(R.id.center_markerlayout);
        center_markerlayout.setVisibility(GONE);

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_container);
        mapOverlayView = ret.findViewById(R.id.mapOverlayView);

        viaPointsTv = ret.findViewById(R.id.viaPointsTv);
        addATipTv = ret.findViewById(R.id.addATipTv);
        dateTimeTv = ret.findViewById(R.id.dateTimeTv);
        bookingStatusTv = ret.findViewById(R.id.bookingStatusTv);
        bookingRefNoTv = ret.findViewById(R.id.bookingRefNoTv);

        journeyTv = ret.findViewById(R.id.journeyTv);
        journeyTv.setVisibility(GONE);
        faresTv = ret.findViewById(R.id.faresTv);
        paymentTypeTv = ret.findViewById(R.id.paymentTypeTv);
        vehicleName = ret.findViewById(R.id.vehicleName);
        vehicleName.setVisibility(VISIBLE);

        driveNameTv = ret.findViewById(R.id.driveNameTv);
        driveNameTv.setVisibility(GONE);
        try {
            driverRatingTv = ret.findViewById(R.id.driverRatingTv);
            driverRatingTv.setText("2.0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        vehicleNameTv = ret.findViewById(R.id.vehicleNameTv);
        vehicleNoPlateTv = ret.findViewById(R.id.vehicleNoPlateTv);

        pickUpTitleTv = ret.findViewById(R.id.pickUpTitleTv);
        pickUpSubTitleTv = ret.findViewById(R.id.pickUpSubTitleTv);
        dropOffTitleTv = ret.findViewById(R.id.dropOffTitleTv);
        dropOffSubTitleTv = ret.findViewById(R.id.dropOffSubTitleTv);

        statusHeadingLabel = ret.findViewById(R.id.statusHeadingLabel);
        statusHeadingLabel.setText(p.getBookingStatus());

        receiptLabelTv = ret.findViewById(R.id.receiptLabelTv);
        receiptLabelTv.setText(p.getReceipt());


        try {
            fareLabel = ret.findViewById(R.id.fareLabel);
            fareLabel.setText(p.getFares());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            paymentLabel = ret.findViewById(R.id.paymentLabel);
            paymentLabel.setText(p.getPayment());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            vehicleNameLabel = ret.findViewById(R.id.vehicleNameLabel);
            vehicleNameLabel.setText(p.getVehicleName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            vehicleNoPlateLabel = ret.findViewById(R.id.vehicleNoPlateLabel);
            vehicleNoPlateLabel.setText(p.getNoPlate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ratingHead = ret.findViewById(R.id.ratingHead);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            viaLl = ret.findViewById(R.id.viaLl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            driverDetailCv = ret.findViewById(R.id.driverDetailCv);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            receiptLl = ret.findViewById(R.id.receiptLl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            backIv = ret.findViewById(R.id.backIv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            driverImageIv = ret.findViewById(R.id.driverImageIv);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            simpleRatingBar = ret.findViewById(R.id.simpleRatingBar);
        } catch (Exception e) {
            e.printStackTrace();
        }

        paymentTypeTv.setVisibility(VISIBLE);

        try {
            if (sp.getString(Config.ShowFares, "1").equals("0")) {
                faresTv.setVisibility(GONE);
            } else {
                faresTv.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        simpleRatingBar.setVisibility(GONE);
        ratingHead.setText(p.getTapToRateYourDriver());
        ratingHead.setGravity(Gravity.CENTER);
    }

    private void setTip() {
        try {
            String tip = mHelper.getTipFromBookingRef(mBookingDetails.getRefrenceNo());
            if (!tip.equals("")) {
                addATipTv.setText(sp.getString(CommonVariables.CurrencySymbol, "\u00A3") + String.format("%.2f", (Double.parseDouble(tip) + " Tip submitted")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listener() {

        recenterTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.getInstance()._showCurvedPolyline(getContext(), "JobDetail", "", locAndFieldArrayList, mMap, mapOverlayView, true);

            }
        });

        setTip();


        if (mBookingDetails.getStatus().toLowerCase().startsWith("complete") && mBookingDetails.getPaymentType().toLowerCase().startsWith("credit card") && sp.getString(CommonVariables.EnableTip, "0").equals("1")) {
            addATipTv.setVisibility(VISIBLE);
        } else {
            addATipTv.setVisibility(GONE);
        }

        addATipTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!addATipTv.getText().toString().equals("Add a tip")) {
                    return;
                }
                showTipDialog();
            }
        });

        if (mBookingDetails.getStatus().toLowerCase().startsWith("complete") && sp.getString(CommonVariables.EnableLostItemInquiry, "0").equals("1")) {
            findLostItemCv.setVisibility(VISIBLE);
        } else {
            findLostItemCv.setVisibility(GONE);
        }

        findLostItemCv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_FinLost.class));
            }
        });

        if (mBookingDetails.getStatus().toLowerCase().startsWith("complete") && sp.getString(CommonVariables.EnableComplain, "0").equals("1")) {
            reportSafetyItemCv.setVisibility(VISIBLE);
        } else {
            reportSafetyItemCv.setVisibility(GONE);
        }

        reportSafetyItemCv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_SubmitForm.class).putExtra("methodName", "ComplainList"));
            }
        });

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                try {
                    Fragment_JobDetail.this.mMap = mMap;
                    setDarkAndNightMapStyleColor(mMap);
                    mMap.getUiSettings().setZoomControlsEnabled(false);
                    mMap.getUiSettings().setTiltGesturesEnabled(false);
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                    mMap.getUiSettings().setRotateGesturesEnabled(false);
                    mMap.getUiSettings().setZoomGesturesEnabled(true);
                    mapOverlayView.addGoogleMapProvider(new GooglemapProvider() {
                        @Override
                        public GoogleMap getGoogleMapWeakReference() {
                            return mMap;
                        }
                    });
                    mMap.setOnCameraMoveListener(() -> {
                                try {
//                                    mapOverlayView.onCameraMove(mMap.getProjection(), mMap.getCameraPosition());
                                    mapOverlayView.onCameraMove();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                    );

                    try {
                        mapOverlayView.removeRoutes();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (mHelper.getLocAndFieldFromSavedBookings(mBookingDetails.getRefrenceNo()).size() == 0) {
                            boolean isWebDispatch = sp.getString(CommonVariables.IS_WEB_DISPATCH, "0").equals("1");
                            new Manager_GetAddressCoordinates(getContext(), false,setAndGetForAddressCoordinate(mBookingDetails), listener_getAddressCoordinate).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            try {
                                locAndFieldArrayList.clear();
                                locAndFieldArrayList = mHelper.getLocAndFieldFromSavedBookings(mBookingDetails.getRefrenceNo());
                                for (int i = 0; i < locAndFieldArrayList.size(); i++) {
                                    LocAndField l = locAndFieldArrayList.get(i);
                                    if ((l.getField() == null || l.getField().equals("")) && (l.getLat() == null || l.getLat().equals("0.0") || l.getLat().equals("")) && (l.getLon() == null || l.getLat().equals("0.0") || l.getLon().equals(""))) {
                                        locAndFieldArrayList.remove(i);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CommonMethods.getInstance()._showCurvedPolyline(getContext(), "JobDetail", "", locAndFieldArrayList, mMap, mapOverlayView, true);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        backIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getArguments().getString(CommonVariables.KEY_NEW_BOOKING) == "true") {
                    CommonVariables.AppMainActivity.ShowBookingList();
                    dismiss();
                } else {
                    dismiss();
                }
            }
        });

        // After Getting Coordinate from server
        listener_getAddressCoordinate = new Listener_GetAddressCoordinate() {
            @Override
            public void onComplete(String result) {
                result = checkIfHasNullForString(result);
                if (result.length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("HasError")) {
                        } else {
                            locAndFieldArrayList.clear();
                            JSONObject dataObject = jsonObject.optJSONObject("Data");
                            if (dataObject != null) {
                                JSONArray arrayObject = dataObject.optJSONArray("clsLocationDatas");
                                if (arrayObject != null) {
                                    for (int i = 0; i < arrayObject.length(); i++) {
                                        JSONObject ob = arrayObject.optJSONObject(i);
                                        if (ob != null) {
                                            locAndFieldArrayList.add(setAndGetLocAndField(ob.optString("keyword"), ob.optString("lat"), ob.optString("lng")));
                                        }
                                    }
                                    try {
                                        mHelper.saveLocAndFieldForSavedBookings(mBookingDetails.getRefrenceNo(), locAndFieldArrayList);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        try {
                                            locAndFieldArrayList = mHelper.getLocAndFieldFromSavedBookings(mBookingDetails.getRefrenceNo());
                                            for (int i = 0; i < locAndFieldArrayList.size(); i++) {
                                                LocAndField l = locAndFieldArrayList.get(i);
                                                if ((l.getField() == null || l.getField().equals("")) && (l.getLat() == null || l.getLat().equals("0.0") || l.getLat().equals("")) && (l.getLon() == null || l.getLat().equals("0.0") || l.getLon().equals(""))) {
                                                    locAndFieldArrayList.remove(i);
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        CommonMethods.getInstance()._showCurvedPolyline(getContext(), "JobDetail", "", locAndFieldArrayList, mMap, mapOverlayView, true);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPre() {

            }
        };

        driverJourneyDetailsManagerListener = new Listener_GetDriverJourneyDetailsManager() {
            @Override
            public void onComplete(String result) {
                if (result == null) {
                    mHelper.putVal("isfeedback_" + mBookingDetails.getRefrenceNo(), "1");
                }

                if (result != null && !result.isEmpty()) {
                    if (result != null && !result.isEmpty() && isAdded()) {
                        try {
                            JSONObject parentObject = new JSONObject(result);

                            String Message = parentObject.getString("Message");
                            JSONObject msJsonObject = new JSONObject(Message);

                            if (driverInformation == null) {
                                driverInformation = new DriverInformation();
                            }
                            driverInformation.setDriverImage(msJsonObject.optString("DriverImage"));
                            driverInformation.setDriverName(msJsonObject.optString("DriverName"));
                            driverInformation.setVehicleDetails(msJsonObject.optString("VehicleDetails"));
                            driverInformation.setVehicleNo(msJsonObject.optString("VehicleNo"));
                            driverInformation.setVehicleMake(msJsonObject.optString("VehicleMake"));
                            driverInformation.setVehicleColor(msJsonObject.optString("VehicleColor"));
                            driverInformation.setVehicleModel(msJsonObject.optString("VehicleModel"));

                            setDriverImage(driverInformation.getDriverImage(), driverImageIv);

                            driveNameTv.setText("Your ride with " + driverInformation.getDriverName());

                            String vehicleDetail[] = new String[0];
                            try {
                                vehicleDetail = driverInformation.getVehicleDetails().split("\\|");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                String vD = "";
                                try {
                                    vD = vehicleDetail[0];
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                vehicleNameTv.setText(driverInformation.getVehicleColor() + " | " + vD);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                vehicleNoPlateTv.setText("" + driverInformation.getVehicleNo());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            int rating = mHelper.getIntVal("rating_" + mBookingDetails.getRefrenceNo());
                            if (rating > 0) {
                                try {
                                    int rate = rating;
                                    simpleRatingBar.setNumStars(5);
                                    simpleRatingBar.setRating(rate);
                                    simpleRatingBar.setVisibility(VISIBLE);
                                    ratingHead.setVisibility(VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                simpleRatingBar.setVisibility(GONE);
                                ratingHead.setText(p.getTapToRateYourDriver());
                                ratingHead.setGravity(Gravity.CENTER);
                                ratingHead.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        showRatingsDialogue();
                                        ratingDialogDemo();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        if (mBookingDetails.getStatus().equalsIgnoreCase(COMPLETED)) {
            new Manager_GetDriverJourneyDetails("jobdetail", driverJourneyDetailsManagerListener, getContext(), p, getShareTracking()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        // Submit Feedback Listener
        listenerSubmitFeedback = new Listener_SubmitFeedback() {
            @Override
            public void onComplete(String response) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
                try {
                    if (response.startsWith("error_")) {
                        response = response.replace("error_", "");
                        FBToast.errorToast(getContext(), response, FBToast.LENGTH_LONG);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response != null && !response.startsWith("false") && !response.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("HasError")) {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("")
                                    .setContentText(jsonObject.getString("Message"))
                                    .setConfirmText("OK")
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
                            String data = jsonObject.getString("Data");
                            try {
                                String[] datas = data.split(">>>");

                                if (datas.length == 1) {
                                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("")
                                            .setContentText(jsonObject.getString("Message"))
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    mHelper.putIntVal("rating_" + mBookingDetails.getRefrenceNo(), ratings);
                                                    mHelper.putVal("feedback_" + mBookingDetails.getRefrenceNo(), feedbacktxt);
                                                    CommonVariables.AppMainActivity.ShowBookingList();
                                                    dismiss();
                                                }

                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                                }
                                            })
                                            .show();
                                }

                                if (datas.length == 2) {
                                    if (datas[1].length() > 0) {
                                        JSONObject json = new JSONObject(datas[1]);
                                        if (json.optBoolean("hasError")) {
                                        } else {
                                            String message = jsonObject.getString("message");
                                            if (message.startsWith("Status :succeeded")) {
                                                String TransactionID = jsonObject.getString("transactionID");
                                                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Success")
                                                        .setContentText(message + "\nTransaction successful: " + TransactionID)
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                                addATipTv.setText(sp.getString(CommonVariables.CurrencySymbol, "\u00A3") + String.format("%.2f", (Double.parseDouble(tipFares) + " Tip submitted")));
                                                                mHelper.saveTipFromBookingRef(mBookingDetails.getRefrenceNo(), tipFares);
                                                                mHelper.putVal("isfeedback_" + mBookingDetails.getRefrenceNo(), "1");
                                                                try {
                                                                    float tf = Float.parseFloat(tipFares.trim());
                                                                    if (tf > 0) {
                                                                        mHelper.putVal("tipfare_" + mBookingDetails.getRefrenceNo(), String.format("%.2f", Float.parseFloat(tipFares)));
                                                                    }
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                CommonVariables.AppMainActivity.ShowBookingList();
                                                                dismiss();
                                                            }

                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                                            }
                                                        })
                                                        .show();
                                            }
                                        }
                                    } else {
                                        // error
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    FBToast.errorToast(getContext(), "Payment Faile\nUnable to process payment, Please try again later", FBToast.LENGTH_LONG);
                }
            }
        };

        receiptLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showReceipt(mBookingDetails);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isVisiable = false;
    }

    private void setVias() {
        try {
            if (mBookingDetails.getViaPointsAsString() != null) {
                viaLl.setVisibility((mBookingDetails.getViaPointsAsString().equals("")) ? GONE : VISIBLE);
                String modelVia = "";
                if (mBookingDetails.getViaPointsAsString().contains(">>>")) {
                    String vias[] = mBookingDetails.getViaPointsAsString().split(">>>");
                    if (vias.length > 0) {
                        modelVia = vias[0];
                        if (vias.length > 1) {
                            modelVia = "1: " + modelVia + "\n2: " + vias[1];
                        }
                    }
                } else {
                    modelVia = "1: " + mBookingDetails.getViaPointsAsString();
                }
                viaPointsTv.setText(modelVia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInitData() {
        setVias();


        bookingRefNoTv.setText(p.getBookingRef() + ": " + mBookingDetails.getRefrenceNo());
        dateTimeTv.setText("" + mBookingDetails.getPickUpDate() + " " + mBookingDetails.getPickUpTime());
        faresTv.setText(sp.getString(CommonVariables.CurrencySymbol, "\u00A3") + String.format("%.2f", (Double.parseDouble(mBookingDetails.getOneWayFare()))));
        paymentTypeTv.setText("" + mBookingDetails.getPaymentType().toUpperCase());
        vehicleName.setText(mBookingDetails.getCar());

        String[] pickUp = HomeFragment.getFilteredAddress(mBookingDetails.getFromAddress());
        String[] dropOff = HomeFragment.getFilteredAddress(mBookingDetails.gettoAddress());

        pickUpTitleTv.setText("" + pickUp[0]);
        pickUpSubTitleTv.setText("" + pickUp[1]);

        dropOffTitleTv.setText("" + dropOff[0]);
        dropOffSubTitleTv.setText("" + dropOff[1]);

        if (mBookingDetails.getPickupLat() != null && !mBookingDetails.getPickupLat().equals("") && !mBookingDetails.getPickupLat().equals("0") && !mBookingDetails.getPickupLat().equals("0.0") && !mBookingDetails.getPickupLat().equals("null")) {
            lat_ = mBookingDetails.getPickupLat() + "," + mBookingDetails.getPickupLon();
        } else {
            lat_ = mBookingDetails.getFromAddress();
        }

        if (mBookingDetails.getDropLat() != null && !mBookingDetails.getDropLat().equals("") && !mBookingDetails.getDropLat().equals("0") && !mBookingDetails.getDropLat().equals("0.0") && !mBookingDetails.getDropLat().equals("null")) {
            Dlat_ = mBookingDetails.getDropLat() + "," + mBookingDetails.getDropLon();
        } else {
            Dlat_ = mBookingDetails.gettoAddress();
        }

        params.add(lat_);
        params.add(Dlat_);

        if (mBookingDetails.getViaPoints() != null && !mBookingDetails.getViaPoints().isEmpty()) {
            for (String via : mBookingDetails.getViaPoints())
                params.add(via);
        }

        String[] paramsString = new String[params.size()];
        for (int i = 0; i < params.size(); i++) {
            paramsString[i] = (String) params.get(0);
        }

//        str = new String[params.size()];
//        str[0] = String.valueOf(params.get(0));
//        str[1] = String.valueOf(params.get(1));


//        locAndFieldArrayList.add(setAndGetLocAndField("",params.get(0)))

        if (mBookingDetails.getStatus().equalsIgnoreCase(COMPLETED)) {
            try {
                driverDetailCv.setVisibility(GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bookingStatusTv.setText("" + COMPLETED.toUpperCase());
                bookingStatusTv.setTextColor(Color.parseColor("#23C552"));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bookingStatusTv.setTextColor(Color.GREEN);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (sp.getString(CommonVariables.EnableReceipt, "1").equals("0")) {
                    receiptLl.setVisibility(GONE);
                } else {
                    receiptLl.setVisibility(VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            findLostItemCv.setVisibility(VISIBLE);
            reportSafetyItemCv.setVisibility(VISIBLE);

        } else if (mBookingDetails.getStatus().equalsIgnoreCase(CANCELLED)) {
            try {
                driverDetailCv.setVisibility(GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bookingStatusTv.setText("" + CANCELLED.toUpperCase());
            bookingStatusTv.setTextColor(Color.parseColor("#F84F31"));
            receiptLl.setVisibility(GONE);
        } else {
            driverDetailCv.setVisibility(GONE);
            receiptLl.setVisibility(GONE);
            bookingStatusTv.setText("");

        }

    }

    private ShareTracking getShareTracking() {
        ShareTracking obj = new ShareTracking();
        obj.defaultClientId = (int) CommonVariables.clientid;
        obj.uniqueValue = CommonVariables.clientid + "4321orue";
        obj.SubCompanyId = CommonVariables.SUB_COMPANY;
        obj.UniqueId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        obj.DeviceInfo = "Android";
        obj.JobId = mBookingDetails.getRefrenceNo();
        return obj;
    }

    private void dismiss() {
        if (isAdded()) {
            getFragmentManager().popBackStack();
        }
    }

    int ratings = 2;
    String feedbacktxt = "";
    String tipFares = "";

    private void setDriverImage(String imageUrl, ImageView imageView) {
        try {
            Glide.with(getActivity())
                    .asBitmap()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FeedbackInformation getFeedbackInformation(String feedBack, int ratings, boolean IsFeedback) {
        feedbackInformation = new FeedbackInformation();
        feedbackInformation.BookingReference = mBookingDetails.getRefrenceNo();
        feedbackInformation.ClientName = settingModel.getName();
        feedbackInformation.Email = settingModel.getEmail();
        feedbackInformation.Location = mBookingDetails.gettoAddress();
        feedbackInformation.Message = feedBack;
        feedbackInformation.Rating = ratings;
        feedbackInformation.IsFeedback = IsFeedback;
        feedbackInformation.Title = settingModel.getName() + "_Review From Customer App";
        return feedbackInformation;
    }

    public static float roundfloat(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private void ratingDialogDemo() {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.layout_feedback);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView closeBottomSheet = dialog.findViewById(R.id.closeBottomSheet);
        closeBottomSheet.setVisibility(GONE);

        TextView _addATipTv = dialog.findViewById(R.id.addATipTv);
        _addATipTv.setVisibility(GONE);

        TextView driveNameTv = dialog.findViewById(R.id.driveNameTv);
        TextView driverRatingTv = dialog.findViewById(R.id.driverRatingTv);
        driverRatingTv.setVisibility(GONE);
        TextView ratingHead = dialog.findViewById(R.id.ratingHead);
        TextView vehicleNameTv = dialog.findViewById(R.id.vehicleNameTv);
        TextView vehicleNoPlateTv = dialog.findViewById(R.id.vehicleNoPlateTv);
        RatingBar simpleRatingBar = dialog.findViewById(R.id.simpleRatingBar);
        simpleRatingBar.setVisibility(GONE);
        RatingBar mainRatingBar = dialog.findViewById(R.id.mainRatingBar);
        ImageView driverImageIv = dialog.findViewById(R.id.driverImageIv);
        ImageView backIv = dialog.findViewById(R.id.backIv);
        TextView rateYourTrip = dialog.findViewById(R.id.rateYourTrip);
        rateYourTrip.setText(p.getRateYourTrip());
        CardView doneCv = dialog.findViewById(R.id.doneCv);
        TextView doneTv = dialog.findViewById(R.id.doneTv);
        doneTv.setText(p.getDone());

        EditText feedbackedit = (EditText) dialog.findViewById(R.id.feedbackedit);
        feedbackedit.setHint(p.getWriteYourFeedback());

        backIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        try {
            setDriverImage(driverInformation.getDriverImage(), driverImageIv);
        } catch (Exception e) {
            e.printStackTrace();
        }

        driverRatingTv.setText("0.0");

        try {
            driveNameTv.setText("" + driverInformation.getDriverName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String vehicleDetail[] = driverInformation.getVehicleDetails().split("\\|");
            vehicleNameTv.setText(driverInformation.getVehicleColor() + " | " + vehicleDetail[0]);
            vehicleNoPlateTv.setText("" + driverInformation.getVehicleNo());
        } catch (Exception e) {
            e.printStackTrace();
            vehicleNameTv.setVisibility(GONE);
            vehicleNoPlateTv.setVisibility(GONE);
        }

        try {
            int rating = mHelper.getIntVal("rating_" + mBookingDetails.getRefrenceNo());
            if (rating > 0) {
                try {
                    int rate = rating;
                    mainRatingBar.setNumStars(rate);
                    mainRatingBar.setRating(rate);
                    ratingHead.setVisibility(VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //            mainRatingBar.setNumStars(2);
                mainRatingBar.setRating(0);
                ratingHead.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CardView skipCv = dialog.findViewById(R.id.skipCv);
        TextView skipTv = dialog.findViewById(R.id.skipTv);
        skipTv.setText(p.getSkip());
        skipCv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mHelper == null) {
                    mHelper = new SharedPrefrenceHelper(getActivity());
                }
                mHelper.putVal("isfeedback_" + mBookingDetails.getRefrenceNo(), "1");
            }
        });

//        tip(dialog);

        tipWork(dialog);

        doneCv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mainRatingBar.getRating() <= 0) {
                        FBToast.warningToast(getContext(), "Please rate your driver.", FBToast.LENGTH_SHORT);
                        return;
                    }
                    feedbacktxt = feedbackedit.getText().toString();
                    int rate = ratings;
                    simpleRatingBar.setNumStars(rate);
                    simpleRatingBar.setRating(rate);
                    simpleRatingBar.setVisibility(VISIBLE);
                    ratingHead.setVisibility(VISIBLE);

                    IsFeedback = true;
                    feedbackInformation = getFeedbackInformation("" + feedbacktxt, ratings, IsFeedback);

                    if (sp.getString(CommonVariables.EnableTip, "0").equals("1")) {
                        if (tipFares.equals("")) {
                            feedbackInformation.CardDetail = null;
                            feedbackInformation.Tip = 0;
                            new Manager_SubmitFeedback(getContext(), feedbackInformation, listenerSubmitFeedback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            // open payment screen
                            startActivityForResult(new Intent(getActivity(), Activity_HomePayment.class)
                                    .putExtra("ForTip", "JobDetail")
                                    .putExtra("IsHold", "false")
                                    .putExtra("Amount", Double.parseDouble(tipFares)), 4444);
                        }
                    } else {
                        feedbackInformation.CardDetail = null;
                        feedbackInformation.Tip = 0;
                        new Manager_SubmitFeedback(getContext(), feedbackInformation, listenerSubmitFeedback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        mainRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratings = (int) rating;
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 4444 && resultCode == RESULT_OK) {
            try {
                if (mBookingDetails.getPaymentType().toLowerCase().startsWith("credit card") && sp.getString(CommonVariables.EnableTip, "0").equals("1")) {
                    if (data != null && !data.getStringExtra("pm").equals("")) {
                        String Gateway = sp.getString(Config.Gateway, "").toLowerCase();
                        if (Gateway.equalsIgnoreCase(Config.Stripe)) {
                            String pm = CommonMethods.checkIfHasNullForString(data.getStringExtra("pm"));
                            processStripePayment(pm, Double.parseDouble(tipFares));
                        }
                    } else {
                        FBToast.errorToast(getActivity(), "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                FBToast.errorToast(getActivity(), "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    boolean IsFeedback = false;

    private void processStripePayment(String pm, double amount) {
        try {
            if (amount == 0) {
                FBToast.warningToast(getActivity(), "Amount should be a greater than " + sp.getString(CurrencySymbol, "\u00A3") + "0.30", FBToast.LENGTH_SHORT);
                return;
            }

            if (pm.equals("")) {
                FBToast.errorToast(getContext(), "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                return;
            }

            new Manager_StripePrePayment(
                    getActivity(),
                    "" + pm,
                    "" + mHelper.getStripeCustomerId(),
                    "" + sp.getString(Config.Stripe_SecretKey, ""),
                    "" + amount,
                    response -> {
                        try {
                            if (response.startsWith("error_")) {
                                response = response.replace("error_", "");
                                FBToast.errorToast(getActivity(), response, FBToast.LENGTH_LONG);
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (response != null && !response.startsWith("false") && !response.equals("")) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("hasError")) {
                                    FBToast.errorToast(getActivity(), jsonObject.getString("message"), FBToast.LENGTH_LONG);
                                } else {
                                    if (jsonObject.getString("message").startsWith("Status :succeeded")) {
                                        String TransactionID = jsonObject.getString("transactionID");

                                        feedbackInformation.IsFeedback = IsFeedback;
                                        try {
                                            Stripe_Model sM = new Stripe_Model();
                                            sM.setTransactionId(TransactionID);
                                            sM.setTotalFares(roundfloat(Float.parseFloat(tipFares), 2));
                                            feedbackInformation.CardDetail = sM;
                                            feedbackInformation.Tip = Double.valueOf(sM.getTotalFares());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            return;
                                        }
                                        new Manager_SubmitFeedback(getContext(), feedbackInformation, listenerSubmitFeedback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                    } else {
                                        FBToast.errorToast(getActivity(), "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            FBToast.errorToast(getActivity(), "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                        }
                    })
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void tipWork(Dialog dialog) {
        String currencySymbol = sp.getString(CommonVariables.CurrencySymbol, "\u00A3");
        String preText = "Tip total: " + currencySymbol + "";

        RelativeLayout tipRl = dialog.findViewById(R.id.tipRl);
        if (mBookingDetails.getPaymentType().toLowerCase().startsWith("credit card") && sp.getString(CommonVariables.EnableTip, "0").equals("1")) {
            tipRl.setVisibility(VISIBLE);
        } else {
            tipRl.setVisibility(GONE);
        }

        TextView tip1Tv = dialog.findViewById(R.id.tip1Tv);
        TextView tip2Tv = dialog.findViewById(R.id.tip2Tv);
        TextView tip3Tv = dialog.findViewById(R.id.tip3Tv);

        TextView clickTipTv = dialog.findViewById(R.id.clickTipTv);
        TextView setTipTv = dialog.findViewById(R.id.setTipTv);

        EditText getTipTv = dialog.findViewById(R.id.getTipTv);
        RelativeLayout editableRl = dialog.findViewById(R.id.editableRl);
        RelativeLayout setTextRl = dialog.findViewById(R.id.setTextRl);
        ImageView clearIv = dialog.findViewById(R.id.clearIv);
        ImageView doneIv = dialog.findViewById(R.id.doneIv);

        tip1Tv.setText(currencySymbol + "1");
        tip2Tv.setText(currencySymbol + "2");
        tip3Tv.setText(currencySymbol + "5");

        setTipTv.setText(preText + "0");

        clickTipTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheme(getContext(), tip1Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(getContext(), tip2Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(getContext(), tip3Tv, R.color.grey_background, R.color.color_white_inverse);

                clickTipTv.setVisibility(GONE);
                editableRl.setVisibility(VISIBLE);
                getTipTv.requestFocus();
                getTipTv.setFocusable(true);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });


        tip1Tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                setTheme(getContext(), tip1Tv, R.color.color_inverse_black_footerBack, R.color.color_white_inverse);
                setTheme(getContext(), tip2Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);
                setTheme(getContext(), tip3Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);

                setTextRl.setVisibility(GONE);
                clickTipTv.setVisibility(VISIBLE);

                tipFares = "1.00";


            }
        });

        tip2Tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                setTheme(getContext(), tip2Tv, R.color.color_inverse_black_footerBack, R.color.color_white_inverse);
                setTheme(getContext(), tip1Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);
                setTheme(getContext(), tip3Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);

                setTextRl.setVisibility(GONE);
                clickTipTv.setVisibility(VISIBLE);

                tipFares = "2.00";

            }
        });

        tip3Tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                setTheme(getContext(), tip3Tv, R.color.color_inverse_black_footerBack, R.color.color_white_inverse);
                setTheme(getContext(), tip2Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);
                setTheme(getContext(), tip1Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);

                setTextRl.setVisibility(GONE);
                clickTipTv.setVisibility(VISIBLE);

                tipFares = "5.00";

            }
        });

        clearIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTipTv.setVisibility(VISIBLE);
                editableRl.setVisibility(GONE);
                setTextRl.setVisibility(GONE);

                setTipTv.setText(preText + "0.0");

                tipFares = "0.0";

                setTheme(getContext(), tip1Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(getContext(), tip2Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(getContext(), tip3Tv, R.color.grey_background, R.color.color_white_inverse);
            }
        });

        doneIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                clickTipTv.setVisibility(GONE);
                editableRl.setVisibility(GONE);
                setTextRl.setVisibility(VISIBLE);

                tipFares = getTipTv.getText().toString();
                getTipTv.setText("");

                try {
                    setTipTv.setText(preText + String.format("%.2f", Float.parseFloat(tipFares)));
                } catch (Exception e) {
                    e.printStackTrace();

                    clickTipTv.setVisibility(VISIBLE);
                    editableRl.setVisibility(GONE);
                    setTextRl.setVisibility(GONE);
                }

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });
    }

    private void showTipDialog() {
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.DialogStyle);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_tip, null);

        Objects.requireNonNull(bottomSheetDialog.getWindow()).setSoftInputMode(SOFT_INPUT_STATE_VISIBLE);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
//                BottomSheetDialog d = (BottomSheetDialog) dialog;
//                View bottomSheetInternal = d.findViewById(R.id.design_bottom_sheet);
//                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        bottomSheetDialog.show();

        TextView _addATipTv = sheetView.findViewById(R.id.addATipTv);
        _addATipTv.setVisibility(VISIBLE);

        View heightV = sheetView.findViewById(R.id.heightV);
        heightV.setVisibility(VISIBLE);

        ImageView closeBottomSheet = sheetView.findViewById(R.id.closeBottomSheet);
        closeBottomSheet.setVisibility(VISIBLE);
        closeBottomSheet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bottomSheetDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        String currencySymbol = sp.getString(CommonVariables.CurrencySymbol, "\u00A3");
        String preText = "Tip total: " + currencySymbol + "";

        RelativeLayout tipRl = bottomSheetDialog.findViewById(R.id.tipRl);
        tipRl.setVisibility(VISIBLE);

        TextView tip1Tv = bottomSheetDialog.findViewById(R.id.tip1Tv);
        TextView tip2Tv = bottomSheetDialog.findViewById(R.id.tip2Tv);
        TextView tip3Tv = bottomSheetDialog.findViewById(R.id.tip3Tv);

        TextView clickTipTv = bottomSheetDialog.findViewById(R.id.clickTipTv);
        TextView setTipTv = bottomSheetDialog.findViewById(R.id.setTipTv);

        EditText getTipTv = bottomSheetDialog.findViewById(R.id.getTipTv);
        RelativeLayout editableRl = bottomSheetDialog.findViewById(R.id.editableRl);
        RelativeLayout setTextRl = bottomSheetDialog.findViewById(R.id.setTextRl);
        ImageView clearIv = bottomSheetDialog.findViewById(R.id.clearIv);
        ImageView doneIv = bottomSheetDialog.findViewById(R.id.doneIv);

        tip1Tv.setText(currencySymbol + "1");
        tip2Tv.setText(currencySymbol + "2");
        tip3Tv.setText(currencySymbol + "5");

        setTipTv.setText(preText + "0");

        clickTipTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheme(getContext(), tip1Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(getContext(), tip2Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(getContext(), tip3Tv, R.color.grey_background, R.color.color_white_inverse);

                clickTipTv.setVisibility(GONE);
                editableRl.setVisibility(VISIBLE);
                getTipTv.requestFocus();
                getTipTv.setFocusable(true);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        doneIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tipFares = getTipTv.getText().toString();

                if (tipFares.equals("") || tipFares.equals("0")) {
                    Toast.makeText(getContext(), "Please enter tip" + tipFares, Toast.LENGTH_SHORT);
                    return;
                }

                clickTipTv.setVisibility(GONE);
                editableRl.setVisibility(GONE);
                setTextRl.setVisibility(VISIBLE);

                getTipTv.setText("");

                try {
                    setTipTv.setText(preText + String.format("%.2f", Float.parseFloat(tipFares)));
                } catch (Exception e) {
                    e.printStackTrace();

                    clickTipTv.setVisibility(VISIBLE);
                    editableRl.setVisibility(GONE);
                    setTextRl.setVisibility(GONE);
                }

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });

        tip1Tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                setTheme(getContext(), tip1Tv, R.color.color_inverse_black_footerBack, R.color.color_white_inverse);
                setTheme(getContext(), tip2Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);
                setTheme(getContext(), tip3Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);

                setTextRl.setVisibility(GONE);
                clickTipTv.setVisibility(VISIBLE);

                tipFares = "1.00";


            }
        });

        tip2Tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                setTheme(getContext(), tip2Tv, R.color.color_inverse_black_footerBack, R.color.color_white_inverse);
                setTheme(getContext(), tip1Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);
                setTheme(getContext(), tip3Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);

                setTextRl.setVisibility(GONE);
                clickTipTv.setVisibility(VISIBLE);

                tipFares = "2.00";

            }
        });

        tip3Tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                setTheme(getContext(), tip3Tv, R.color.color_inverse_black_footerBack, R.color.color_white_inverse);
                setTheme(getContext(), tip2Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);
                setTheme(getContext(), tip1Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);

                setTextRl.setVisibility(GONE);
                clickTipTv.setVisibility(VISIBLE);

                tipFares = "5.00";

            }
        });

        clearIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTipTv.setVisibility(VISIBLE);
                editableRl.setVisibility(GONE);
                setTextRl.setVisibility(GONE);

                setTipTv.setText(preText + "0.0");

                tipFares = "0.0";

                setTheme(getContext(), tip1Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(getContext(), tip2Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(getContext(), tip3Tv, R.color.grey_background, R.color.color_white_inverse);
            }
        });

        _addATipTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editableRl.getVisibility() == VISIBLE && !getTipTv.getText().toString().isEmpty()) {
                    tipFares = getTipTv.getText().toString();
                }

                if (tipFares.equals("") || tipFares.equals("0")) {
                    FBToast.errorToast(getContext(), "Error: Total tip is " + tipFares, FBToast.LENGTH_SHORT);
                    return;
                }

                feedbackInformation = getFeedbackInformation("", 0, false);

                startActivityForResult(new Intent(getActivity(), Activity_HomePayment.class)
                        .putExtra("ForTip", "JobDetail")
                        .putExtra("IsHold", "false")
                        .putExtra("Amount", Double.parseDouble(tipFares)), 4444);
            }
        });
    }

    private void showReceipt(Model_BookingDetailsModel model) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Widget_DeviceDefault);
        dialog.setContentView(R.layout.layout_receipt);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView closeBottomSheet = dialog.findViewById(R.id.closeBottomSheet);

        LinearLayout tripFaresLl = dialog.findViewById(R.id.tripFaresLl);
        LinearLayout companyPriceLl = dialog.findViewById(R.id.companyPriceLl);
        LinearLayout extraChargesLl = dialog.findViewById(R.id.extraChargesLl);
        LinearLayout agentFeesLl = dialog.findViewById(R.id.agentFeesLl);
        LinearLayout agentChargeLl = dialog.findViewById(R.id.agentChargeLl);
        LinearLayout congestionLl = dialog.findViewById(R.id.congestionLl);
        LinearLayout parkingLl = dialog.findViewById(R.id.parkingLl);
        LinearLayout waitingLl = dialog.findViewById(R.id.waitingLl);
        LinearLayout agentCommissionLl = dialog.findViewById(R.id.agentCommissionLl);
        LinearLayout surchargeAmountLl = dialog.findViewById(R.id.surchargeAmountLl);

        View tripFaresV = dialog.findViewById(R.id.tripFaresV);
        View companyPriceV = dialog.findViewById(R.id.companyPriceV);
        View extraChargesV = dialog.findViewById(R.id.extraChargesV);
        View agentFeesV = dialog.findViewById(R.id.agentFeesV);
        View agentChargeV = dialog.findViewById(R.id.agentChargeV);
        View congestionV = dialog.findViewById(R.id.congestionV);
        View parkingV = dialog.findViewById(R.id.parkingV);
        View waitingV = dialog.findViewById(R.id.waitingV);
        View agentCommissionV = dialog.findViewById(R.id.agentCommissionV);


        double tripFares = 0;
        try {
            tripFares = Double.parseDouble(String.valueOf(model.getFareRate()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        double companyPrice = 0;
        try {
            companyPrice = model.getCompanyPrice();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double extraCharges = 0;
        try {
            extraCharges = model.getExtraDropCharges();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double agentFees = 0;
        try {
            agentFees = model.getAgentFees();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double agentCharge = 0;
        try {
            agentCharge = model.getAgentCharge();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double congestion = 0;
        try {
            congestion = model.getCongestion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double parking = 0;
        try {
            parking = model.getParking();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double waiting = 0;
        try {
            waiting = model.getWaiting();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double agentCommission = 0;
        try {
            agentCommission = model.getAgentCommission();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double surchargeAmount = 0;
        try {
            surchargeAmount = model.getSurchargeAmount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        tripFaresLl.setVisibility((tripFares == 0) ? GONE : VISIBLE);
        companyPriceLl.setVisibility((companyPrice == 0) ? GONE : VISIBLE);
        extraChargesLl.setVisibility((extraCharges == 0) ? GONE : VISIBLE);
        agentFeesLl.setVisibility((agentFees == 0) ? GONE : VISIBLE);
        agentChargeLl.setVisibility((agentCharge == 0) ? GONE : VISIBLE);
        congestionLl.setVisibility((congestion == 0) ? GONE : VISIBLE);
        parkingLl.setVisibility((parking == 0) ? GONE : VISIBLE);
        waitingLl.setVisibility((waiting == 0) ? GONE : VISIBLE);
        agentCommissionLl.setVisibility((agentCommission == 0) ? GONE : VISIBLE);
        surchargeAmountLl.setVisibility((surchargeAmount == 0) ? GONE : VISIBLE);

        tripFaresV.setVisibility((tripFares == 0) ? GONE : VISIBLE);
        companyPriceV.setVisibility((companyPrice == 0) ? GONE : VISIBLE);
        extraChargesV.setVisibility((extraCharges == 0) ? GONE : VISIBLE);
        agentFeesV.setVisibility((agentFees == 0) ? GONE : VISIBLE);
        agentChargeV.setVisibility((agentCharge == 0) ? GONE : VISIBLE);
        congestionV.setVisibility((congestion == 0) ? GONE : VISIBLE);
        parkingV.setVisibility((parking == 0) ? GONE : VISIBLE);
        waitingV.setVisibility((waiting == 0) ? GONE : VISIBLE);
        agentCommissionV.setVisibility((agentCommission == 0) ? GONE : VISIBLE);


        TextView tripFaresTv = dialog.findViewById(R.id.tripFaresTv);
        TextView companyPriceTv = dialog.findViewById(R.id.companyPriceTv);
        TextView extraChargesTv = dialog.findViewById(R.id.extraChargesTv);
        TextView agentFeesTv = dialog.findViewById(R.id.agentFeesTv);
        TextView agentChargeTv = dialog.findViewById(R.id.agentChargeTv);
        TextView congestionTv = dialog.findViewById(R.id.congestionTv);
        TextView parkingTv = dialog.findViewById(R.id.parkingTv);
        TextView waitingTv = dialog.findViewById(R.id.waitingTv);
        TextView agentCommissionTv = dialog.findViewById(R.id.agentCommissionTv);
        TextView totalTv = dialog.findViewById(R.id.totalTv);
        TextView surchargeAmountTv = dialog.findViewById(R.id.surchargeAmountTv);

        TextView dateTv = dialog.findViewById(R.id.dateTv);
        dateTv.setText(model.getCusomerName());

        try {
            tripFaresTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (tripFares)));
        } catch (Exception e) {
            tripFaresTv.setText(sp.getString(CurrencySymbol, "\u00A3") + "");
            e.printStackTrace();
        }
        try {
            companyPriceTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (companyPrice)));
        } catch (Exception e) {
            companyPriceTv.setText(sp.getString(CurrencySymbol, "\u00A3") + "");
            e.printStackTrace();
        }
        try {
            extraChargesTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (extraCharges)));
        } catch (Exception e) {
            extraChargesTv.setText(sp.getString(CurrencySymbol, "\u00A3") + "");
            e.printStackTrace();
        }
        try {
            agentFeesTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (agentFees)));
        } catch (Exception e) {
            agentFeesTv.setText(sp.getString(CurrencySymbol, "\u00A3") + "");
            e.printStackTrace();
        }
        try {
            agentChargeTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (agentCharge)));
        } catch (Exception e) {
            agentChargeTv.setText(sp.getString(CurrencySymbol, "\u00A3") + "");
            e.printStackTrace();
        }
        try {
            congestionTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (congestion)));
        } catch (Exception e) {
            congestionTv.setText(sp.getString(CurrencySymbol, "\u00A3") + "");
            e.printStackTrace();
        }
        try {
            parkingTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (parking)));
        } catch (Exception e) {
            parkingTv.setText(sp.getString(CurrencySymbol, "\u00A3") + "");
            e.printStackTrace();
        }
        try {
            waitingTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (waiting)));
        } catch (Exception e) {
            waitingTv.setText(sp.getString(CurrencySymbol, "\u00A3") + "");
            e.printStackTrace();
        }
        try {
            agentCommissionTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (agentCommission)));
        } catch (Exception e) {
            agentCommissionTv.setText(sp.getString(CurrencySymbol, "\u00A3") + "");
            e.printStackTrace();
        }
        try {
            surchargeAmountTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (surchargeAmount)));
        } catch (Exception e) {
            surchargeAmountTv.setText(sp.getString(CurrencySymbol, "\u00A3") + "");
            e.printStackTrace();
        }

//        double total = tripFares +
//                companyPrice +
//                extraCharges +
//                agentFees +
//                agentCharge +
//                congestion +
//                parking +
//                waiting +
//                agentCommission +
//                surchargeAmount;

        try {
            totalTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", Double.parseDouble((model.getOneWayFare()))));
        } catch (Exception e) {
            totalTv.setText(sp.getString(CurrencySymbol, "\u00A3") + "");
            e.printStackTrace();
        }


        closeBottomSheet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private LocAndField setAndGetLocAndField(String locationName, String lat, String lng) {
        LocAndField locAndField = new LocAndField();
        locAndField.setField("" + locationName);
        locAndField.setLat("" + lat);
        locAndField.setLon("" + lng);
        locAndField.setLocationType("Address");
        locAndField.setDoorNo("");
        return locAndField;
    }

    private ArrayList<ClsLocationData> setAndGetForAddressCoordinate(Model_BookingDetailsModel mBookingDetails) {

        ArrayList<ClsLocationData> arrayList = new ArrayList();

        if (!mBookingDetails.getFromAddress().equals("")) {
            arrayList.add(new ClsLocationData((int) CommonVariables.localid,
                    "" + mBookingDetails.getFromAddress(), 0, 0,
                    "" + CommonVariables.GOOGLE_API_KEY,
                    "" + CommonVariables.Clientip,
                    CommonVariables.localid + "4321orue"));
        }

        try {
            if (mBookingDetails.getViaPointsAsString().contains(">>>")) {
                String vias[] = mBookingDetails.getViaPointsAsString().split(">>>");
                if (vias.length > 0) {
                    arrayList.add(new ClsLocationData((int) CommonVariables.localid,
                            "" + vias[0], 0, 0,
                            "" + CommonVariables.GOOGLE_API_KEY,
                            "" + CommonVariables.Clientip,
                            CommonVariables.localid + "4321orue"));
                    if (vias.length > 1) {
                        arrayList.add(new ClsLocationData((int) CommonVariables.localid,
                                "" + vias[1], 0, 0,
                                "" + CommonVariables.GOOGLE_API_KEY,
                                "" + CommonVariables.Clientip,
                                CommonVariables.localid + "4321orue"));
                    }
                }
            } else {
                arrayList.add(new ClsLocationData((int) CommonVariables.localid,
                        "" + mBookingDetails.getViaPointsAsString().trim(), 0, 0,
                        "" + CommonVariables.GOOGLE_API_KEY,
                        "" + CommonVariables.Clientip,
                        CommonVariables.localid + "4321orue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!mBookingDetails.gettoAddress().equals("")) {
            arrayList.add(new ClsLocationData((int) CommonVariables.localid,
                    "" + mBookingDetails.gettoAddress(), 0, 0,
                    "" + CommonVariables.GOOGLE_API_KEY,
                    "" + CommonVariables.Clientip,
                    CommonVariables.localid + "4321orue"));
        }


        return arrayList;

    }

    public void setTheme(Context context, TextView tv, int bgColor, int txtColor) {
        if (android.os.Build.VERSION.SDK_INT > 23) {
            // Do something for lollipop and above versions
            tv.setBackgroundTintList(ContextCompat.getColorStateList(context, bgColor));
            tv.setTextColor(ContextCompat.getColor(context, (txtColor)));
        } else {
            // do something for phones running an SDK before lollipop
            tv.setBackgroundTintList(context.getResources().getColorStateList(bgColor));
            tv.setTextColor(context.getResources().getColor((txtColor)));
        }
    }

}
