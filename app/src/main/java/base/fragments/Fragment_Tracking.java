package base.fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.android.gms.maps.model.JointType.ROUND;
import static base.adapters.BookingAdapter.CANCELLED;
import static base.adapters.BookingAdapter.CONFIRMED;
import static base.adapters.BookingAdapter.ON_ROUTE;
import static base.utils.CommonMethods.checkIfHasNullForString;
import static base.utils.CommonVariables.CurrencySymbol;
import static base.utils.Config.ShowFares;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amalbit.trail.RouteOverlayView;
import com.amalbit.trail.contract.GooglemapProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.eurosoft.customerapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.gson.Gson;
import com.support.parser.PropertyInfo;
import com.support.parser.SoapHelper;
import com.tfb.fbtoast.FBToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import base.activities.Activity_HomePayment;
import base.adapters.BookingAdapter;
import base.adapters.ChatAdapter;
import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.listener.Listener_AppTrackingDetails;
import base.listener.Listener_CancelBookingApp;
import base.listener.Listener_GetAddressCoordinate;
import base.listener.Listener_GetJobStatusWithPriceNew;
import base.listener.Listener_SubmitFeedback;
import base.listener.MyListener;
import base.manager.Manager_AppTrackingDetails;
import base.manager.Manager_CancelBookingApp;
import base.manager.Manager_GetAddressCoordinates;
import base.manager.Manager_GetJobStatusWithPriceNew;
import base.manager.Manager_StripePrePayment;
import base.manager.Manager_SubmitFeedback;
import base.models.ChatModel;
import base.models.ClsLocationData;
import base.models.DriverInformation;
import base.models.FeedbackInformation;
import base.models.LocAndField;
import base.models.Model_BookingDetailsModel;
import base.models.ParentPojo;
import base.models.SettingsModel;
import base.models.ShareTracking;
import base.models.Stripe_Model;
import base.models.TrackingInformation;
import base.newui.HomeFragment;
import base.services.Service_NotifyStatus;
import base.utils.AppConstants;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Fragment_Tracking extends Fragment implements OnClickListener {
    public static  boolean isVisiable = false;
    // static
    private static final String TAG = "Fragment_Tracking";
    private static final String ONROUTE = "onroute";
    private static final String ARRIVED = "arrived";
    private static final String POB = "PassengerOnBoard";
    private static final String COMPLETED = "Completed";
    private static final String STC = "SoonToClear";
    private static final String Available = "available";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 43;

    // View
    private View fare_view;

    // CardView
    private CardView shareTracking;

    // LinearLayout
    private LinearLayout shoppingLabel;
    private LinearLayout drv_Lyt;
    private LinearLayout fare_lyt;
    private LinearLayout viaLl;
    private LinearLayout estimationTimeLl;
    private LinearLayout b_info_lyt;


    // RelativeLayout
    private RelativeLayout bottomLl;
    private RelativeLayout driverChatRl;
    private RelativeLayout driverCallRl;
    private RelativeLayout marker_center_pin;
    private CardView findingNearestDriverRl;

    // TextView
    private TextView recenterTv;
    private TextView cancelRideTv;
    private TextView bookingref;
    private TextView DrvCar_Name;
    private TextView JT_jstatus;
    private TextView JT_payment;
    private TextView dateTimeTv;
    private TextView esttime;
    private TextView drvRatingTxt;
    private TextView pickUpTitleTv, pickUpSubTitleTv;
    private TextView textViewDistance;
    private TextView fare_txt;
    private TextView dropOffTitleTv;
    private TextView dropOffSubTitleTv;
    private TextView viahead;
    private TextView notestxt;
    private TextView Car_plate;
    private TextView badgeDrTv;
    private TextView chatStatusTv;
    private TextView viaPointsTv;


    // ImageView
    private ImageView imgBack;
    private ImageView driverImage;

    // String
    private String DriverContactNo = "";
    private String drvConHost = "";
    private String drvConusername = "";
    private String drvConPass = "";
    private String drvConPort = "";
    private String tipFares = "";
    private String msgToDriver = "";
    private String feedbacktxt = "";
    private String lat;
    private String lon;
    private String URL = "https://maps.googleapis.com/maps/api/directions/json?";
    private String mDriverId;
    private String mDriverName;
    private String mDriverIp;
    private String mDriverStatus;
    private String ShareTrackingFlag = "0";

    // boolean
    private boolean isRatingInPending = false;
    private boolean isUp = true;
    private boolean isStopped = false;
    private boolean isAsap = false;
    boolean isMarkerRotating = false;
    private boolean IsFirstExecute = false;

    // int
    private int ratings = 2;

    // Class
    private DriverInformation driverInformation;
    private Dialog ratingDialogue;
    private ValueAnimator valueAnimator;
    private LatLng oldLocation;
    //    private AsyncTask etaAsync;
    private FragmentManager fm;
    private SupportMapFragment mapFragment;
    private Marker fromMaker;
    private PolylineOptions polylineOptions;
    private PolylineOptions blackPolylineOptions;
    private Polyline blackPolyline;
    private Polyline greyPolyLine;
    private DatabaseOperations mDatabaseOperations;
    private Marker marker;
    private SharedPreferences sp;
    private SharedPrefrenceHelper sharedPrefrenceHelper;
    private Model_BookingDetailsModel mBookingDetails;
    private SweetAlertDialog progressDialogue;
    private CountDownTimer countDownTimer;
    private FeedbackInformation feedbackInformation;
    int nightModeFlags;
    // handler
    private Handler handler;
    private Handler mUpdateHandler;

    private TextView statusHeadingLabel;
    private TextView fareLabel;
    private TextView driverNameTv;

    private LinearLayout driverDetailCv;

    private TextView paymentLabel;

    private Dialog chatDialog;
    private RecyclerView chatRv;
    private ChatAdapter chatAdapter;
    private ParentPojo p = new ParentPojo();
    private ArrayList<ChatModel> chatModels = new ArrayList<>();
    private RouteOverlayView mapOverlayView;
    private GoogleMap mMap;
    public ArrayList<LocAndField> locAndFieldArrayList = new ArrayList<>();
    private SettingsModel settingsModel;

    // LISTENER
    private Listener_AppTrackingDetails listener_appTrackingDetails;
    private Listener_GetAddressCoordinate listener_getAddressCoordinate;
    private Listener_SubmitFeedback listenerSubmitFeedback;


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if (intent.getAction().contains(Config.ACTION_NEW_MESSAGE)) {
                String message = intent.getStringExtra("text");

                String[] fromAndMsg = message.split(":");

                if (!fromAndMsg[0].trim().equalsIgnoreCase(sharedPrefrenceHelper.getSettingModel().getName().trim())) {
                    chatModels.add(new ChatModel(fromAndMsg[0], fromAndMsg[1], getCurrentDate() + " " + getCurrentTime(), 1));
                }
                try {
                    if (((Fragment_Main) getActivity()).mService != null) {
                        badgeDrTv.setText("" + ((Fragment_Main) getActivity()).mService.getMsgCounter());
                    } else {
                        badgeDrTv.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    chatRv.smoothScrollToPosition(chatModels.size());
                    chatAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (intent.getAction().contains(Config.ACTION_READ)) {
                try {
                    for (int i = 0; i < chatModels.size(); i++) {
                        if (chatModels.get(i).getFrom().trim().equalsIgnoreCase(sharedPrefrenceHelper.getSettingModel().getName().trim())) {
                            chatModels.get(i).setMessageStatus(2);
                        } else {
                            chatModels.get(i).setMessageStatus(-1);
                        }
                    }
                    if (chatDialog != null && chatDialog.isShowing()) {
                        chatAdapter.notifyDataSetChanged();
                        chatRv.scrollToPosition(chatModels.size() - 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                try {
//                    if (chatDialog.isShowing()) {
//                        for (int i = 0; i < chatModels.size(); i++) {
//                            if (chatModels.get(i).getMessageStatus() == 1) {
//                                chatModels.get(i).setMessageStatus(2);
//                            } else {
//                                break;
//                            }
//                        }
//                        chatAdapter.notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            } else if (intent.getAction().contains(Config.ACTION_RECEIVED)) {
                Log.d(TAG, "onReceive: Received...  ");
                try {
                    for (int i = 0; i < chatModels.size(); i++) {
                        if (chatModels.get(i).getFrom().trim().equalsIgnoreCase(sharedPrefrenceHelper.getSettingModel().getName().trim())) {
                            if (chatModels.get(i).getMessageStatus() < 2) {
                                chatModels.get(i).setMessageStatus(1);
                            }
                        } else {
                            chatModels.get(i).setMessageStatus(-1);
                        }
                    }
                    if (chatDialog != null && chatDialog.isShowing()) {
                        chatAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                try {
//
//                    for (int i = 0; i < chatModels.size(); i++) {
//                        if (chatModels.get(i).getFrom().equals("Client")) {
//                            chatModels.get(i).setStatus(1);
//                        } else {
//                            chatModels.get(i).setStatus(-1);
//                        }
//                    }
//                    chatAdapter.notifyDataSetChanged();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            } else if (intent.getAction().contains(Config.ACTION_START_TYPE)) {
                Log.d(TAG, "onReceive: Typing Activity_Start...  ");
                try {
                    chatStatusTv.setText("Typing ..");
                    chatStatusTv.setVisibility(VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (intent.getAction().equals(Config.ACTION_STOP_TYPE)) {
                Log.d(TAG, "onReceive: Typing Stop...  ");
                try {
                    chatStatusTv.setText("");
                    chatStatusTv.setVisibility(GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (intent.getAction().contains(Config.ACTION_MESSAGE_SENT)) {
                try {
                    String[] textSplit = intent.getStringExtra("text").split(">>>");
                    if (textSplit[0].trim().equalsIgnoreCase(sharedPrefrenceHelper.getSettingModel().getName().trim())) {
                        chatModels.add(new ChatModel(textSplit[0], textSplit[1], getCurrentDate() + " " + getCurrentTime(), 1));
                    }
                    try {
                        chatRv.smoothScrollToPosition(chatModels.size());
                        chatAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Runnable m_Runnable = () -> {
        signalSent = false;
        ((Fragment_Main) getActivity()).mService.submitSignal(Config.TYPING_STOP);
    };

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            if (mUpdateHandler != null)
                mUpdateHandler.removeCallbacks(this);

            if (mDriverStatus != null && !mDriverStatus.isEmpty()) {


                if (mDriverStatus.equalsIgnoreCase(ONROUTE) || mDriverStatus.equalsIgnoreCase(ARRIVED) || mDriverStatus.equalsIgnoreCase(POB) || mDriverStatus.equalsIgnoreCase(STC)) {

                    if (mDriverId != null && !mDriverId.isEmpty()) {
                        new Manager_AppTrackingDetails(getContext(), getTrackingDetails(), listener_appTrackingDetails).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    mUpdateHandler.postDelayed(this, 9000);
                    return;
                } else if (mDriverStatus.equalsIgnoreCase(COMPLETED) || mDriverStatus.equalsIgnoreCase(Available)) {

                    if (sharedPrefrenceHelper == null) {
                        sharedPrefrenceHelper = new SharedPrefrenceHelper(getActivity());
                    }
                    String feedbackstatus = sharedPrefrenceHelper.getVal("isfeedback_" + mBookingDetails.getRefrenceNo());
                    if (!feedbackstatus.equals("1")) {

                        if (progressDialogue != null && isAdded()) {
                            progressDialogue.dismiss();
                        }
                    }

                    return;
                }
            }

            if (mDriverStatus == null || mDriverStatus.equalsIgnoreCase(ONROUTE) || mDriverStatus.equalsIgnoreCase(ARRIVED) || mDriverStatus.equalsIgnoreCase("Waiting") || mDriverStatus.equalsIgnoreCase("Waiting") || mDriverStatus.equalsIgnoreCase("confirmed")) {
                if (!mBookingDetails.getRefrenceNo().equals(null) || !mBookingDetails.getRefrenceNo().equals("null")) {
                    new Manager_GetJobStatusWithPriceNew(mBookingDetails.getRefrenceNo(), new Listener_GetJobStatusWithPriceNew() {
                        @Override
                        public void onComplete(String response) {
                            response = checkIfHasNullForString(response);
                            if (!response.equals("") && isAdded()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject dataObject = jsonObject.optJSONObject("Data");
                                    if (dataObject != null) {
                                        JSONArray bookingStatus = dataObject.optJSONArray("BookingStatus");
                                        if (bookingStatus != null) {
                                            for (int j = 0; j < bookingStatus.length(); j++) {
                                                JSONObject o = bookingStatus.optJSONObject(j);
                                                String BookingStatus = checkIfHasNullForString(o.optString("BookingStatus"));
                                                String Fares = checkIfHasNullForString(o.optString("Fares"));
                                                String DriverStatus = checkIfHasNullForString(o.optString("DriverStatus"));
                                                String Listener = checkIfHasNullForString(o.optString("Listener"));
                                                String refNo = checkIfHasNullForString(o.optString("refNo"));
                                                ArrayList<String> listmessang = new ArrayList<String>();
                                                listmessang.add(BookingStatus);
                                                listmessang.add(Fares);
                                                listmessang.add(DriverStatus);
                                                listmessang.add(Listener);
                                                if (!listmessang.get(2).equals("")) {
                                                    String[] parsedResponse = listmessang.get(2).split(",");
                                                    if (parsedResponse != null && parsedResponse.length >= 3) {
                                                        if (parsedResponse[0].toLowerCase().equals("available") && BookingStatus.toLowerCase().equals("confirmed")) {
                                                            mDriverStatus = BookingStatus;
                                                        } else {
                                                            mDriverStatus = parsedResponse[0];
                                                            if (mDriverStatus.toLowerCase().trim().equals("onroute") && !mBookingDetails.getStatus().trim().toLowerCase().equals("onroute")) {
                                                            }
                                                            try {
                                                                findingNearestDriverRl.setVisibility(GONE);
                                                                bottomLl.setVisibility(VISIBLE);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            mDriverId = parsedResponse[1];
                                                            mDriverName = parsedResponse[2];
                                                            mDriverIp = listmessang.get(3);

                                                            // driver info Visibility new work
                                                            try {
                                                                if(mDriverName != null || !mDriverName.equals("")) {
                                                                    if (driverDetailCv.getVisibility() == View.GONE) {
                                                                        driverDetailCv .setVisibility(VISIBLE);
                                                                    }
                                                                }

                                                            }catch (Exception ex){

                                                            }
                                                            driverNameTv.setText("" + mDriverName);


                                                            if (getView() != null) {
                                                                updateStatus(mDriverStatus);
                                                                if (IsFirstExecute == false) {
                                                                    IsFirstExecute = true;
                                                                    new Handler().post(run);
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    mDriverStatus = BookingStatus;
                                                    updateStatus(BookingStatus);
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (isAdded()) {
                                    FBToast.errorToast(getActivity(), "Unable to get details, Please check your internet connection", FBToast.LENGTH_SHORT);
                                    try {
                                        updateStatus(mBookingDetails.getStatus());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    /*((Fragment_Main) getActivity()).mService.initPubNub(mBookingDetails.getRefrenceNo());*//*

                                                        }
                                                        try {
                                                            findingNearestDriverRl.setVisibility(GONE);
                                                            bottomLl.setVisibility(VISIBLE);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        mDriverId = parsedResponse[1];
                                                        mDriverName = parsedResponse[2];
                                                        mDriverIp = listmessang.get(3);

                                                        driverNameTv.setText("" + mDriverName);
                                                        if (getView() != null) {
                                                            //                                        ((TextView) getView().findViewById(R.id.JT_dname)).setText(mDriverName);
                                                            //                                        ((TextView) getView().findViewById(R.id.JT_dname)).setText(mDriverName);
                                                            updateStatus(mDriverStatus);

                                                            if (IsFirstExecute == false) {
                                                                IsFirstExecute = true;
                                                                new Handler().post(run);
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                mDriverStatus = mStatus;
                                                updateStatus(mStatus);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    if (isAdded()) {
                                        FBToast.errorToast(getActivity(), "Unable to get details, Please check your internet connection", FBToast.LENGTH_SHORT);
                                        try {
                                            updateStatus(mBookingDetails.getStatus());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
*/
                }
            }
            mUpdateHandler.postDelayed(this, 6000);
        }
    };

    public String statusColor = "#ffffff";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPrefrenceHelper = new SharedPrefrenceHelper(getActivity());
        settingsModel = sharedPrefrenceHelper.getSettingModel();
        mDatabaseOperations = new DatabaseOperations(new DatabaseHelper(getActivity()));
        BookingAdapter.isTracking = true;
        mUpdateHandler = new Handler();
        driverInformation = new DriverInformation();


        nightModeFlags = getContext().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.layout_tracking_job, container, false);
        handler = new Handler(Looper.getMainLooper());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.ACTION_READ);
        intentFilter.addAction(Config.ACTION_RECEIVED);
        intentFilter.addAction(Config.ACTION_NEW_MESSAGE);
        intentFilter.addAction(Config.ACTION_START_TYPE);
        intentFilter.addAction(Config.ACTION_STOP_TYPE);
        intentFilter.addAction(Config.ACTION_MESSAGE_SENT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, intentFilter);
//        setDarkAndNightThemeColor1();

        init(ret);
        // driverCallAndChatVisibility(VISIBLE, VISIBLE);

        listener();
        isVisiable = true;
        return ret;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fm = getFragmentManager();
    }

    @Override
    public void onResume() {
        super.onResume();

        Service_NotifyStatus.isTrackingLive = true;

        if (isRatingInPending) {
//            ratingDialogue = showRatingsDialogue();
            ratingDialogue = ratingDialogDemo();
        } else {
            if (isStopped) {
                isStopped = false;
                try {
                    Model_BookingDetailsModel modelBookingDetailsModel = mDatabaseOperations.getbookingbyRefference(mBookingDetails.getRefrenceNo()).get(0);
                    if (modelBookingDetailsModel.getStatus().equalsIgnoreCase("completed")) {
                        updateStatus(modelBookingDetailsModel.getStatus());
                        ratingDialogue = ratingDialogDemo();
                    } else {
                        if (mUpdateHandler != null) {
                            mUpdateHandler.post(run);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (etaAsync != null) {
//            etaAsync.cancel(true);
//        }
        Service_NotifyStatus.isTrackingLive = false;

        if (mUpdateHandler != null) {
            mUpdateHandler.removeCallbacks(run);
        }

        isVisiable = false;
//if(ratin)
    }

    @Override
    public void onStop() {
        super.onStop();

        Service_NotifyStatus.isTrackingLive = false;
    }

    @Override
    public void onPause() {
        super.onPause();

        Service_NotifyStatus.isTrackingLive = false;
        isStopped = true;
        if (ratingDialogue != null) {
            ratingDialogue.dismiss();
            isRatingInPending = true;
        }
        if (mUpdateHandler != null) {
            mUpdateHandler.removeCallbacks(run);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // foreground service setting
        if(sp.getString(CommonVariables.Enable_ForeGround_Service,"0").equals("1")) {
            Service_NotifyStatus.isTrackingLive = true;
        }

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.imgBack) {
            Fragment_Main.fromTracking = true;
            BookingAdapter.isTracking = true;
            CommonVariables.AppMainActivity.ShowBookingList();
            dismiss();
        }
        if (v.getId() == R.id.shareTrack) {
//            ratingDialogDemo();
            new getNumberFromServer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        if (v.getId() == R.id.driverCallRl) {
            if (!DriverContactNo.equals("")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", DriverContactNo.trim(), null));
                startActivity(intent);
            } else {
                FBToast.errorToast(getActivity(), "Driver contact number is not available", FBToast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMSMessage();
//					Toast.makeText(getActivity(), "SMS sent.",
//							Toast.LENGTH_LONG).show();
                } else {
                    FBToast.errorToast(getActivity(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG);
                    return;
                }
            }
        }

    }

    public void moveCarOnMapOffline(LatLng newLocation) {
//        oldLocation latlng
        if (oldLocation != null && oldLocation.latitude != newLocation.latitude) {
            if (marker == null) {
                Bitmap bitmap = getThumbnail(mBookingDetails.getCar() + ".png");
                if (bitmap == null) {
                    marker = mMap.addMarker(new MarkerOptions().position(newLocation)
                            .flat(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.avcar)));
                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(oldLocation)
                            .flat(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                }
            }
            try {
                rotateMarker(marker, (float) bearingBetweenLocations(marker.getPosition(), newLocation));
                animateMarkerNew(newLocation, marker);
            } catch (Exception e) {

            }
            oldLocation = newLocation;
        } else {
            if (marker == null) {
                Bitmap bitmap = getThumbnail(mBookingDetails.getCar() + ".png");
                if (bitmap == null) {
                    marker = mMap.addMarker(new MarkerOptions().position(newLocation)
                            .flat(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.avcar)));

                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(newLocation)
                            .flat(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                }

                try {
                    LatLngBounds.Builder builder = LatLngBounds.builder();
                    String[] from = mBookingDetails.getFromLatLng().split(",");
                    String[] to = mBookingDetails.getToLatLng().split(",");

                    Double fromLat = Double.valueOf(from[0]);
                    Double fromLang = Double.valueOf(from[1]);

                    Double toLat = Double.valueOf(to[0]);
                    Double toLng = Double.valueOf(to[1]);

                    builder.include(new LatLng(fromLat, fromLang));
                    builder.include(new LatLng(toLat, toLng));


                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
                }catch (Exception ex){
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                            .target(marker.getPosition())
                            .zoom(6)
                            .build()));
                }

            }

            oldLocation = newLocation;
//            if (oldLocation == null) {
            sharedPrefrenceHelper.setDriverLastLocation(newLocation);
//            }
        }

    }

    public void shareTracking(String url) {
        try {
            String appname = getResources().getString(R.string.app_name);
            String pkgName = getActivity().getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, appname + "\nTracking Link " + "\n" + url);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Invite via..."));
        } catch (Exception e) {
        }
    }

    private void init(View ret) {
        driverNameTv = ret.findViewById(R.id.JT_dname);
        driverDetailCv = ret.findViewById(R.id.driverDetailCv);
        mapOverlayView = ret.findViewById(R.id.mapOverlayView);

        statusHeadingLabel = ret.findViewById(R.id.statusHeadingLabel);
        statusHeadingLabel.setText(p.getBookingStatus());

        fareLabel = ret.findViewById(R.id.fareLabel);
        fareLabel.setText(p.getFares());

        paymentLabel = ret.findViewById(R.id.paymentLabel);
        paymentLabel.setText(p.getPayment());

        cancelRideTv = ((TextView) ret.findViewById(R.id.cancelRideTv));
        cancelRideTv.setVisibility(VISIBLE);
        cancelRideTv.setClickable(false);
        JT_payment = ((TextView) ret.findViewById(R.id.JT_payment));
        shoppingLabel = ((LinearLayout) ret.findViewById(R.id.shoppingLabel));
        dateTimeTv = ret.findViewById(R.id.dateTimeTv);
        imgBack = ret.findViewById(R.id.imgBack);
        bookingref = ret.findViewById(R.id.bookingref);
        JT_jstatus = ret.findViewById(R.id.JT_jstatus);

        pickUpTitleTv = ret.findViewById(R.id.pickUpTitleTv);
        pickUpSubTitleTv = ret.findViewById(R.id.pickUpSubTitleTv);

        dropOffTitleTv = ret.findViewById(R.id.dropOffTitleTv);
        dropOffSubTitleTv = ret.findViewById(R.id.dropOffSubTitleTv);


        shareTracking = (CardView) ret.findViewById(R.id.shareTrack);

        drvRatingTxt = ret.findViewById(R.id.JT_drating);
        DrvCar_Name = ret.findViewById(R.id.JT_vehname);
        Car_plate = ret.findViewById(R.id.JT_vehplate);

        badgeDrTv = ret.findViewById(R.id.badgeDrTv);
        badgeDrTv.setVisibility(GONE);

        try {
            if (((Fragment_Main) getActivity()).mService != null) {
                ((Fragment_Main) getActivity()).mService.setMsgCounter(0);
                if (((Fragment_Main) getActivity()).mService.getMsgCounter() == 0) {
                    badgeDrTv.setVisibility(GONE);
                }
                badgeDrTv.setText("" + ((Fragment_Main) getActivity()).mService.getMsgCounter());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viahead = ret.findViewById(R.id.ViaHead);
        viaPointsTv = ret.findViewById(R.id.viaPointsTv);
//        recenterTv = ret.findViewById(R.id.recenterTv);
        notestxt = ret.findViewById(R.id.not_txt);
        bottomLl = ret.findViewById(R.id.bottomLl);
        findingNearestDriverRl = ret.findViewById(R.id.findingNearestDriverRl);

        esttime = ((TextView) ret.findViewById(R.id.esttime));
        esttime.setText(p.getEstTime());

        drv_Lyt = (LinearLayout) ret.findViewById(R.id.drv_info_lyt);
        driverImage = (ImageView) ret.findViewById(R.id.driver_Image);
        textViewDistance = ret.findViewById(R.id.textViewDistance);
        fare_lyt = (LinearLayout) ret.findViewById(R.id.fare_lyt);
        viaLl = (LinearLayout) ret.findViewById(R.id.viaLl);
        b_info_lyt = ret.findViewById(R.id.b_info_lyt);
        b_info_lyt.setWeightSum(2);
        estimationTimeLl = ret.findViewById(R.id.estimationTimeLl);
        estimationTimeLl.setVisibility(GONE);

        fare_txt = ret.findViewById(R.id.fare_txt);
//        fare_view = (View) ret.findViewById(R.id.fare_view);
//        status2 = ret.findViewById(R.id.status2);

        driverChatRl = ret.findViewById(R.id.driverChatRl);
        driverCallRl = ret.findViewById(R.id.driverCallRl);
        marker_center_pin = ret.findViewById(R.id.marker_center_pin);
        marker_center_pin.setVisibility(GONE);

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_container);
//        try {
//            mapFragment.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap googleMap) {
////                    Fragment_Tracking.this.onMapReady(googleMap);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void setDarkAndNightThemeColor() {
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

    private void setInitData() {

        setVias();

        try {
            ShareTrackingFlag = sp.getString(Config.isShareTracking, "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mBookingDetails.setFromLatLng(mBookingDetails.getPickupLat() + "," + mBookingDetails.getPickupLon());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mBookingDetails.setToLatLng(mBookingDetails.getDropLat() + "," + mBookingDetails.getDropLon());
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateStatus(mBookingDetails.getStatus());

        if (sharedPrefrenceHelper.getRefNoAsapValue(mBookingDetails.getRefrenceNo())) {
            findingNearestDriverRl.setVisibility(VISIBLE);
            bottomLl.setVisibility(GONE);
            try {
                startTimer(sp.getFloat(Config.CounterTime, 0.5f));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            findingNearestDriverRl.setVisibility(GONE);
            bottomLl.setVisibility(VISIBLE);
        }

        JT_payment.setText(mBookingDetails.getPaymentType().toUpperCase().replace(" CREDIT", ""));

        if (mBookingDetails.getReturnFare() != null && mBookingDetails.getReturnFare().equals("Shopping Collection")) {
            shoppingLabel.setVisibility(VISIBLE);
        } else {
            shoppingLabel.setVisibility(GONE);
        }

        try {
            viaLl.setVisibility((mBookingDetails.getViaPointsAsString().equals(null) || mBookingDetails.getViaPointsAsString().equals("")) ? GONE : VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            dateTimeTv.setText("" + mBookingDetails.getPickUpDate() + " " + mBookingDetails.getPickUpTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            bookingref.setText(p.getBookingRef() + ": " + mBookingDetails.getRefrenceNo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Service_NotifyStatus.isTrackingLive = true;
        try {
            if (getArguments().getString("iscompleted") != null || mBookingDetails.getStatus().toLowerCase().equals("waiting") || mBookingDetails.getStatus().toLowerCase().equals("confirmed")) {
                new Manager_AppTrackingDetails(getContext(), getTrackingDetails(), listener_appTrackingDetails).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!mBookingDetails.getSpecialNotes().equals("")) {
                notestxt.setText(mBookingDetails.getSpecialNotes());
                notestxt.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String[] Addresse = HomeFragment.getFilteredAddress(mBookingDetails.getFromAddress());
            String[] AddresseDrop = HomeFragment.getFilteredAddress(mBookingDetails.gettoAddress());

            pickUpTitleTv.setText(Addresse[0]);
            pickUpSubTitleTv.setText(Addresse[1]);

            dropOffTitleTv.setText(AddresseDrop[0]);
            dropOffSubTitleTv.setText(AddresseDrop[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            if (sp.getString(Config.ShowFares, "1").equals("0")) {
                fare_lyt.setVisibility(GONE);
//                fare_view.setVisibility(GONE);

            } else {
                fare_lyt.setVisibility(VISIBLE);
                fare_txt.setVisibility(VISIBLE);
//                fare_view.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            boolean isFromhome =false;
            try{
                isFromhome = getArguments().getBoolean("is_from_home");
            }catch (Exception ex){
                isFromhome = false;
            }


            double f = 0;
            if(isFromhome) {

                if (HomeFragment._asap_fares == 0) {
                    f = Double.parseDouble(mBookingDetails.getOneWayFare());
                } else {
                    f = HomeFragment._asap_fares;
                }
            }else{
                f = Double.parseDouble(mBookingDetails.getOneWayFare());
            }
            fare_txt.setText(sp.getString(CommonVariables.CurrencySymbol, "\u00A3") + String.format("%.2f", (f)));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void listener() {
        imgBack.setOnClickListener(this);
        shareTracking.setOnClickListener(this);
        driverCallRl.setOnClickListener(this);

        try {
            getView().setOnTouchListener(new OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        cancelRideTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelBooking(mBookingDetails.getRefrenceNo());
            }
        });

        driverChatRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showChatDialog("Chat with Driver");
            }
        });


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                try {
                    Fragment_Tracking.this.mMap = mMap;
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
                        if (sharedPrefrenceHelper.getLocAndFieldFromSavedBookings(mBookingDetails.getRefrenceNo()).size() == 0) {
                            boolean isWebDispatch = sp.getString(CommonVariables.IS_WEB_DISPATCH, "0").equals("1");
                            new Manager_GetAddressCoordinates(getContext(),false, setAndGetForAddressCoordinate(mBookingDetails), listener_getAddressCoordinate).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            ArrayList<LocAndField> locAndFieldArrayList = sharedPrefrenceHelper.getLocAndFieldFromSavedBookings(mBookingDetails.getRefrenceNo());
                            mBookingDetails.setFromLatLng(locAndFieldArrayList.get(0).getLat() + "," + locAndFieldArrayList.get(0).getLon());
                            mBookingDetails.setToLatLng(locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getLat() + "," + locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getLon());

                            try {
                                locAndFieldArrayList = sharedPrefrenceHelper.getLocAndFieldFromSavedBookings(mBookingDetails.getRefrenceNo());
                                for (int i = 0; i < locAndFieldArrayList.size(); i++) {
                                    LocAndField l = locAndFieldArrayList.get(i);
                                    if ((l.getField() == null || l.getField().equals("")) && (l.getLat() == null || l.getLat().equals("0.0") || l.getLat().equals("")) && (l.getLon() == null || l.getLat().equals("0.0") || l.getLon().equals(""))) {
                                        locAndFieldArrayList.remove(i);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CommonMethods.getInstance()._showCurvedPolyline(getContext(), "JobTracking", "", locAndFieldArrayList, mMap, mapOverlayView, true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // After Getting Coordinate from server
        listener_getAddressCoordinate = new Listener_GetAddressCoordinate() {
            @Override
            public void onComplete(String result) {

                if (result != null && result.length() > 0) {
                    try {
                        JSONObject parentObject = new JSONObject(result);

                        if (parentObject.getBoolean("HasError")) {
                        } else {
                            JSONArray datas = new JSONArray(parentObject.getString("Data"));
                            if (datas.length() >= 2) {
                                mBookingDetails.setPickupLat("" + datas.getJSONObject(0).getDouble("lat"));
                                mBookingDetails.setDropLat("" + datas.getJSONObject(datas.length() - 1).getDouble("lat"));
                                mBookingDetails.setPickupLon("" + datas.getJSONObject(0).getDouble("lng"));
                                mBookingDetails.setDropLon("" + datas.getJSONObject(datas.length() - 1).getDouble("lng"));

                                try {
                                    mBookingDetails.setFromLatLng(mBookingDetails.getPickupLat() + "," + mBookingDetails.getPickupLon());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    mBookingDetails.setToLatLng(mBookingDetails.getDropLat() + "," + mBookingDetails.getDropLon());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            for (int i = 0; i < datas.length(); i++) {
                                JSONObject ob = datas.getJSONObject(i);
                                locAndFieldArrayList.add(setAndGetLocAndField("" + ob.optString("keyword"), "" + ob.optDouble("lat"), "" + ob.optDouble("lng")));
                            }
                            try {
                                sharedPrefrenceHelper.saveLocAndFieldForSavedBookings(mBookingDetails.getRefrenceNo(), locAndFieldArrayList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            try {
                                if (mBookingDetails.getStatus().equalsIgnoreCase(ONROUTE) || mBookingDetails.getStatus().equalsIgnoreCase("confirmed") || mBookingDetails.getStatus().equalsIgnoreCase("confirming") || mBookingDetails.getStatus().equalsIgnoreCase("waiting")) {
                                    try {
                                        locAndFieldArrayList = sharedPrefrenceHelper.getLocAndFieldFromSavedBookings(mBookingDetails.getRefrenceNo());
                                        for (int i = 0; i < locAndFieldArrayList.size(); i++) {
                                            LocAndField l = locAndFieldArrayList.get(i);
                                            if ((l.getField() == null || l.getField().equals("")) && (l.getLat() == null || l.getLat().equals("0.0") || l.getLat().equals("")) && (l.getLon() == null || l.getLat().equals("0.0") || l.getLon().equals(""))) {
                                                locAndFieldArrayList.remove(i);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        locAndFieldArrayList = sharedPrefrenceHelper.getLocAndFieldFromSavedBookings(mBookingDetails.getRefrenceNo());
                                        for (int i = 0; i < locAndFieldArrayList.size(); i++) {
                                            LocAndField l = locAndFieldArrayList.get(i);
                                            if ((l.getField() == null || l.getField().equals("")) && (l.getLat() == null || l.getLat().equals("0.0") || l.getLat().equals("")) && (l.getLon() == null || l.getLat().equals("0.0") || l.getLon().equals(""))) {
                                                locAndFieldArrayList.remove(i);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    CommonMethods.getInstance()._showCurvedPolyline(getContext(), "JobTracking", "", locAndFieldArrayList, mMap, mapOverlayView, true);
                                } else {
                                   /* if (mDriverStatus != null || mDriverStatus.equalsIgnoreCase(ONROUTE) || mDriverStatus.equalsIgnoreCase(ARRIVED) ||
                                            mDriverStatus.equalsIgnoreCase(POB) || mDriverStatus.equalsIgnoreCase("pob") ||
                                            mDriverStatus.equalsIgnoreCase("stc") ||
                                            mDriverStatus.equalsIgnoreCase(STC)) {
                                        return;
                                    }*/
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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

        // Driver List
        listener_appTrackingDetails = result -> {

            if (result == null) {

            }


            String[] splitted;
            String status = "";
            String LocName = "";
            String driverImg = "";
            String DriverName = "";
            String VehicleDetails = "";
            if (result != null && !result.isEmpty() && isAdded()) {
                try {
                    JSONObject parentObject = new JSONObject(result);

                    if (parentObject.getBoolean("HasError")) {
                        FBToast.errorToast(getActivity(), parentObject.getString("Message"), FBToast.LENGTH_SHORT);
                    } else {
                        JSONObject jsonObject = new JSONObject(parentObject.getString("Data"));
                        try {
                            if (drv_Lyt.getVisibility() == View.GONE) {
                                drv_Lyt.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (driverInformation == null) {
                            driverInformation = new DriverInformation();
                        }
                        try {
                            driverInformation.setWorkStatus(CommonMethods.checkIfHasNullForString(jsonObject.optString("WorkStatus")));
                            driverInformation.setDiverLat(CommonMethods.checkIfHasNullForString(jsonObject.optString("Latitude")));
                            driverInformation.setDiverLng(CommonMethods.checkIfHasNullForString(jsonObject.optString("Longitude")));
                            driverInformation.setDriverImage(CommonMethods.checkIfHasNullForString(jsonObject.optString("DriverImage")));
                            driverInformation.setDriverName(CommonMethods.checkIfHasNullForString(jsonObject.optString("DriverName")));
                            driverInformation.setVehicleDetails(CommonMethods.checkIfHasNullForString(jsonObject.optString("Vehicle")));
                            driverInformation.setEta(CommonMethods.checkIfHasNullForString(jsonObject.optString("ETA")));
                            driverInformation.setDistance(CommonMethods.checkIfHasNullForString(jsonObject.optString("Distance")));

                            // visible here
                            b_info_lyt.setWeightSum((sp.getString(ShowFares, "1").equals("0") ? 2 : 3));

                            estimationTimeLl.setVisibility(VISIBLE);

                            textViewDistance.setText("" + driverInformation.getEta());

                            try {
                                if(mDriverName != null || !mDriverName.equals("")) {
                                    if (driverDetailCv.getVisibility() == View.GONE) {
                                        driverDetailCv .setVisibility(VISIBLE);
                                    }
                                }

                            }catch (Exception ex){

                            }

                            driverNameTv.setText("" + driverInformation.getDriverName());
//                            DrvCar_Name.setText("" + driverInformation.get());

                            try {
                                String vD = driverInformation.getVehicleDetails();
                                if (vD.contains("|")) {
                                    String vehicleDetail[] = vD.split("\\|");
                                    DrvCar_Name.setText(driverInformation.getVehicleColor() + " | " + vehicleDetail[0]);
                                } else {
                                    DrvCar_Name.setText(driverInformation.getVehicleColor() + " | " + vD);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Car_plate.setText("" + driverInformation.getVehicleNo());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            SharedPreferences.Editor editor = sp.edit();
                            try {
                                lat = jsonObject.getString("Latitude");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                lon = jsonObject.getString("Longitude");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                LocName = jsonObject.getString("LocationName");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                driverImg = jsonObject.getString("DriverImage");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                DriverContactNo = jsonObject.getString("Contact");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                DriverName = jsonObject.getString("DriverName");

                                try {
                                    if(mDriverName != null || !mDriverName.equals("")) {
                                        if (driverDetailCv.getVisibility() == View.GONE) {
                                            driverDetailCv .setVisibility(VISIBLE);
                                        }
                                    }

                                }catch (Exception ex){

                                }


                                driverNameTv.setText("" + DriverName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                VehicleDetails = jsonObject.getString("Vehicle");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                String drvRating = jsonObject.getString("Rating");
                                if (drvRating != null && !drvRating.equals("null") && !drvRating.equals("0") && !drvRating.equals("")) {
                                    sharedPrefrenceHelper.putVal("drvRating_" + mBookingDetails.getRefrenceNo(), drvRating);

                                    try {
                                        drvRatingTxt.setText(String.format("%.1f", (drvRating)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        drvRatingTxt.setVisibility(GONE);
                                    }
                                    drvRatingTxt.setVisibility(VISIBLE);
                                } else {
                                    drvRatingTxt.setVisibility(GONE);
                                }
                            } catch (Exception e) {
                                drvRatingTxt.setVisibility(GONE);
                            }

                            try {
                                ArrayList<LatLng> RouteCoordinatesList = new ArrayList<>();

                                String rCC = jsonObject.getString("RouteCoordinates");
                                String RouteCoordinates[] = rCC.split("\\|");
                                for (int i = 0; i < RouteCoordinates.length; i++) {
                                    String[] cor = RouteCoordinates[i].split(",");
                                    double ll = 0;
                                    try {
                                        ll = Double.parseDouble(cor[0]);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    double lg = 0;
                                    try {
                                        lg = Double.parseDouble(cor[1]);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    LatLng position = null;
                                    try {
                                        position = new LatLng(ll, lg);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        RouteCoordinatesList.add(position);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isAdded()) {
                                            if (valueAnimator != null) {
                                                valueAnimator.cancel();
                                            }
                                            if (greyPolyLine != null) {
                                                greyPolyLine.remove();
                                            }
                                            if (blackPolyline != null) {
                                                blackPolyline.remove();
                                            }

                                            try {
                                                valueAnimator = polylineFlasher(mMap, RouteCoordinatesList);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                drvRatingTxt.setVisibility(GONE);
                            }


                            if (jsonObject.has("drvConHost")) {
                                drvConHost = jsonObject.getString("drvConHost");
                                drvConusername = jsonObject.getString("drvConusername");
                                drvConPass = jsonObject.getString("drvConPass");
                                drvConPort = jsonObject.getString("drvConPort");
                                if (drvConHost.equals("") || drvConusername.equals("") || drvConPass.equals("") || drvConPort.equals("")) {
                                    editor.putString(SharedPrefrenceHelper.DrvConnectHost, "");
                                    editor.putString(SharedPrefrenceHelper.DrvConnectPass, "");
                                    editor.putString(SharedPrefrenceHelper.DrvConnectPort, "");
                                    editor.putString(SharedPrefrenceHelper.DrvConnectUsername, "");
                                    editor.commit();
                                } else {
                                    editor.putString(SharedPrefrenceHelper.DrvConnectHost, drvConHost);
                                    editor.putString(SharedPrefrenceHelper.DrvConnectPass, drvConPass);
                                    editor.putString(SharedPrefrenceHelper.DrvConnectPort, drvConPort);
                                    editor.putString(SharedPrefrenceHelper.DrvConnectUsername, drvConusername);
                                    editor.commit();
                                }
                            } else {
                                editor.putString(SharedPrefrenceHelper.DrvConnectHost, "213.171.205.21");
                                editor.putString(SharedPrefrenceHelper.DrvConnectPass, "dEP7lcni");
                                editor.putString(SharedPrefrenceHelper.DrvConnectPort, "9000");
                                editor.putString(SharedPrefrenceHelper.DrvConnectUsername, "Eurosofttech-4444");
                                editor.commit();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            splitted = result.split("=");
                            if (splitted != null && splitted.length >= 4) {
                                lat = splitted[0];
                                lon = splitted[1];
                                status = splitted[2];
                                LocName = splitted[3];
                                driverImg = "";

                                if (splitted.length >= 5) {
                                    driverImg = splitted[4];

                                }
                            }
                        }

                        if (!VehicleDetails.equals("")) {
                            String[] vehArr = VehicleDetails.split("\\|");
                            try {
                                if (vehArr.length > 2) {
                                    String[] vehMake = vehArr[1].split("-");

                                    DrvCar_Name.setText(vehArr[0].toUpperCase());
                                    if (vehMake.length != 0) {
                                        if (vehMake.length == 2) {
                                            DrvCar_Name.setText(vehArr[2].toUpperCase() + " " + vehMake[1].toUpperCase());
                                        } else if (vehMake.length >= 3) {
                                            DrvCar_Name.setText(vehArr[2].toUpperCase() + " " + vehMake[1].toUpperCase() + " " + vehMake[2].toUpperCase());
                                        } else if (vehMake.length == 1 && vehArr[0].equals("")) {

                                        } else {
                                            DrvCar_Name.setText(vehArr[2].toUpperCase() + " " + vehArr[0].toUpperCase());
                                        }

                                        driverInformation.setVehicleNo(vehMake[0].toUpperCase());
                                        Car_plate.setText(driverInformation.getVehicleNo());
                                    } else {
                                        DrvCar_Name.setText(vehArr[2].toUpperCase() + " " + vehMake[0].toUpperCase());
                                    }

                                } else if (vehArr.length == 2) {
                                    if (!vehArr[1].equals("") && !vehArr[1].equals("--")) {
                                        DrvCar_Name.setText(vehArr[0] + "\nPlate No: " + vehArr[1].replace("-", ""));
                                        Car_plate.setText("" + vehArr[1]);
                                    } else {
                                        DrvCar_Name.setText(vehArr[0] + "\nPlate No: -");
                                    }
//                                    DrvCar_Name.setText(vehArr[0] + "\nPlate No: " + vehArr[1].replace("-", ""));
                                    //  Car_plate.setVisibility(GONE);
                                } else {
                                    DrvCar_Name.setText(vehArr[0]);
                                    //  Car_plate.setVisibility(GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            status = jsonObject.getString("WorkStatus");
                            Log.d("TAG", "listener:status = " + status);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mDriverStatus = status;
                        updateStatus(status);

                        try {
                            LatLng position = null;
                            try {
                                double latD = Double.parseDouble(lat);
                                double lonD = Double.parseDouble(lon);
                                position = new LatLng(latD, lonD);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                mapOverlayView.removeRoutes();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (CommonMethods.getInstance().lineFromMarker != null) {
                                    CommonMethods.getInstance().lineToMarker.remove();
                                    CommonMethods.getInstance().lineFromMarker.remove();
                                }
                                if (marker == null) {
                                    mMap.clear();
                                    CommonMethods.getInstance().lineFromMarker = null;
                                    CommonMethods.getInstance().lineToMarker = null;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
//                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
//                                        .target(new LatLng(Double.parseDouble(driverInformation.getDiverLng()),Double.parseDouble(driverInformation.getDiverLng())))
//                                        .zoom(17)
//                                        .build()));
//                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
////                                for (Marker marker : marker) {
////                                }
//                                builder.include(marker.getPosition());
//
//                                LatLngBounds bounds = builder.build();
//                                int padding = 50; // offset from edges of the map in pixels
//                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
////                                mMap.moveCamera(cu);
//                                mMap.animateCamera(cu);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                moveCarOnMapOffline(position);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (fromMaker == null && mMap != null) {
                                    LatLng Pickposition = new LatLng(Double.parseDouble(mBookingDetails.getPickupLat()), Double.parseDouble(mBookingDetails.getPickupLon()));
                                    fromMaker = mMap.addMarker(new MarkerOptions()
                                            .position(Pickposition)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bc_passengerpin)));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                if (mDriverStatus.equalsIgnoreCase(ARRIVED) && marker != null) {
                                    marker.setTitle("Driver [" + DriverName + "] has arrived");
                                    marker.showInfoWindow();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                if (mDriverStatus.equalsIgnoreCase(POB) && marker != null) {
                                    marker.setTitle("");
                                    marker.hideInfoWindow();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                if (driverImg != null && driverImg.length() > 0 && isAdded()) {
                                    try {
                                        sharedPrefrenceHelper.putVal("driverImage_" + mBookingDetails.getRefrenceNo(), driverImg);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        sharedPrefrenceHelper.putVal("driverName_" + mBookingDetails.getRefrenceNo(), mDriverName);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        sharedPrefrenceHelper.putVal("vehDetails_" + mBookingDetails.getRefrenceNo(), VehicleDetails);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    driverImage.setVisibility(View.VISIBLE);

                                    try {
                                        setDriverImage(driverImg, driverImage);
                                    } catch (Exception e) {
                                        String ee = e.getMessage();
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                String ee = e.getMessage();
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            String ee = e.getMessage();
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (progressDialogue != null && isAdded()) {
                progressDialogue.dismiss();
            }
        };

        // submit feedback listener
        listenerSubmitFeedback = new Listener_SubmitFeedback() {
            @Override
            public void onComplete(String response) {
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
                                                    sharedPrefrenceHelper.putIntVal("rating_" + mBookingDetails.getRefrenceNo(), ratings);
                                                    sharedPrefrenceHelper.putVal("feedback_" + mBookingDetails.getRefrenceNo(), feedbacktxt);
                                                    sharedPrefrenceHelper.putVal("isfeedback_" + mBookingDetails.getRefrenceNo(), "1");
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
                                                                sharedPrefrenceHelper.saveTipFromBookingRef(mBookingDetails.getRefrenceNo(), tipFares);
                                                                sharedPrefrenceHelper.putVal("isfeedback_" + mBookingDetails.getRefrenceNo(), "1");
                                                                try {
                                                                    float tf = Float.parseFloat(tipFares.trim());
                                                                    if (tf > 0) {
                                                                        sharedPrefrenceHelper.putVal("tipfare_" + mBookingDetails.getRefrenceNo(), String.format("%.2f", Float.parseFloat(tipFares)));
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
                    FBToast.errorToast(getContext(), "Something went wrong!", FBToast.LENGTH_SHORT);
                }
            }
        };

        if (getArguments() != null) {
            mBookingDetails = (Model_BookingDetailsModel) getArguments().getSerializable(CommonVariables.KEY_BOOKING_MODEL);
            if (mBookingDetails != null) {
                setInitData();
                new Handler().post(run);
            }
        } else {
            CommonVariables.AppMainActivity.ShowBookingList();
            dismiss();
        }
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

    private void showChatDialog(String fromWhere) {

        Intent intent = new Intent("abc");
        intent.putExtra("where", "chat_screen");
        getActivity().sendBroadcast(intent);

/*        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("pub-c-dd311b4d-27e0-4bdb-996c-162f28c35df1");
        pnConfiguration.setSubscribeKey("sub-c-ab8885ac-fc3c-11eb-b38c-d287984b4121");
        pnConfiguration.setUuid(clientUUID);

        if (pubnub == null) {
            pubnub = new PubNub(pnConfiguration);
        }*/

        try {
            if (((Fragment_Main) getActivity()).mService != null) {
                ((Fragment_Main) getActivity()).mService.setMsgCounter(0);
                if (((Fragment_Main) getActivity()).mService.getMsgCounter() == 0) {
                    badgeDrTv.setVisibility(GONE);
                }
                badgeDrTv.setText("" + ((Fragment_Main) getActivity()).mService.getMsgCounter());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatDialog = new Dialog(getActivity(), android.R.style.Widget_DeviceDefault);

        chatDialog.setContentView(R.layout.layout_chat);
        Window window = chatDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);

        chatDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ProgressBar pb = chatDialog.findViewById(R.id.pb);

        TextView chatTitleTv = chatDialog.findViewById(R.id.chatTitleTv);
        chatTitleTv.setText("" + fromWhere);
        EditText chatEt = chatDialog.findViewById(R.id.chatEt);
        chatEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    if (!signalSent) {
                        signalSent = true;
                        if (((Fragment_Main) getActivity()).mService != null) {
                            ((Fragment_Main) getActivity()).mService.submitSignal(Config.TYPING_START);
                        }
                    }
                } else {
                    if (!signalSent) {
                        signalSent = true;
                        if (((Fragment_Main) getActivity()).mService != null) {
                            ((Fragment_Main) getActivity()).mService.submitSignal(Config.TYPING_STOP);
                        }
                    }


                }
                handler.removeCallbacks(m_Runnable);
                handler.postDelayed(m_Runnable, 2000);
            }
        });

        chatEt.requestFocus();


        chatStatusTv = chatDialog.findViewById(R.id.chatStatusTv);
        chatStatusTv.setVisibility(GONE);
        ImageView sendIv = chatDialog.findViewById(R.id.sendIv);

        ImageView closeBottomSheet = chatDialog.findViewById(R.id.closeBottomSheet);
        chatRv = chatDialog.findViewById(R.id.rv);
        chatRv.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRv.setHasFixedSize(true);

        if (chatModels != null || chatModels.size() == 0) {
            ((Fragment_Main) getActivity()).mService.getCurrentChat(pb, new MyListener() {
                @Override
                public void chatModel(ArrayList<ChatModel> chatModelss) {
                    chatModels = chatModelss;
                    chatAdapter = new ChatAdapter(getContext(), chatModels, sharedPrefrenceHelper);
                    chatRv.setAdapter(chatAdapter);
                    try {
                        chatRv.scrollToPosition(chatModels.size() - 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        sendIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatEt.getText().toString().trim().length() == 0) {
                    chatEt.setError("Can't send empty message");
                    return;
                }
                if (((Fragment_Main) getActivity()).mService != null) {
                    ((Fragment_Main) getActivity()).mService.submitUpdate(sharedPrefrenceHelper.getSettingModel().getName().trim(), chatEt.getText().toString());
                }
                chatEt.setText("");
            }
        });

        closeBottomSheet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("abc");
                intent.putExtra("where", "");
                getActivity().sendBroadcast(intent);

                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
                chatDialog.dismiss();
            }
        });

        chatDialog.show();
    }

    boolean signalSent = false;

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("dd-MMM", Locale.getDefault()).format(new Date());
    }

    private void CancelBooking(String ref) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("")
                .setContentText(p.getAreYouSureYouWantToCancelThisBooking())
                .setCancelText(p.getNo())
                .setConfirmText(p.getYes())
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        new Manager_CancelBookingApp(getContext(), new Listener_CancelBookingApp() {
                            @Override
                            public void onComplete(String result) {
                                handleCancelResponse(result,ref);

                            }
                        }).execute(new String[]{"CANCEL", ref});
//                        new BookingStatusChangeTask().execute(new String[]{BookingStatusChangeTask.TASK_CANCEL, ref});
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
    }

/*
    private class BookingStatusChangeTask extends AsyncTask<String[], Void, Void> {
        public static final String TASK_DELETE = "DELETE";
        public static final String TASK_CANCEL = "CANCEL";

        private static final String HASH_KEY = "4321orue";

        private static final String RESPONSE_SUCCESS = "success";

        private SweetAlertDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mProgressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                mProgressDialog.setTitleText(p.getCancelling());
                mProgressDialog.setContentText(p.getPleaseWait());
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }// End onPreExecute()

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();


        }// End onPostExecute()

        @Override
        protected Void doInBackground(String[]... params) {

            String[] task = params[0];

            String response = null;
            if (task != null) {
                DatabaseOperations mDatabaseOperations = new DatabaseOperations(new DatabaseHelper(getContext()));
                if (task[0].equals(TASK_DELETE)) {

                    mDatabaseOperations.DeleteBooking(task[1]);
                    return null;
                } else if (task[0].equals(TASK_CANCEL)) {
                    try {

                        response = new SoapHelper.Builder(CommonVariables.SERVICE, getContext())
                                .setMethodName("CancelBookingApp", true)
                                .addProperty("defaultClientId", CommonVariables.localid, PropertyInfo.INTEGER_CLASS)
                                .addProperty("refNo", task[1], PropertyInfo.STRING_CLASS)
                                .addProperty("uniqueValue", CommonVariables.localid + task[1] + HASH_KEY, PropertyInfo.STRING_CLASS)
                                .addProperty("postedFrom", CommonVariables.DEVICE_TYPE, PropertyInfo.STRING_CLASS).getResponse();

                        if (response != null) {
                            JSONObject parentObject = new JSONObject(response);
                            if (parentObject.getBoolean("HasError")) {
                                try {
                                    ((FragmentActivity) getContext()).runOnUiThread(new Runnable() {
                                        public void run() {
                                            if (mProgressDialog != null)
                                                mProgressDialog.dismiss();
                                            try {
                                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("")
                                                        .setContentText(parentObject.getString("Message"))
                                                        .setConfirmText(p.getOk())
                                                        .showCancelButton(false)
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                                //                                        new InitializeAppDb(mContext, true).execute();
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
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                } catch (Exception e) {
                                }

                            } else {
                                if (parentObject.getString("Data") != null && parentObject.getString("Data").equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                    try {
                                        mDatabaseOperations.UpdateBookingStatus(CommonVariables.STATUS_CANCLED, task[1]);

                                        if (mDatabaseOperations.getActiveBookings().size() <= 0) {
                                            try {
                                                ((Fragment_Main) getContext()).unBindService();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        ((FragmentActivity) getContext()).runOnUiThread(new Runnable() {
                                            public void run() {

                                                ((FragmentActivity) getContext()).stopService(new Intent(getContext(), Service_NotifyStatus.class));
                                                if (mProgressDialog != null)
                                                    mProgressDialog.dismiss();

                                                CommonVariables.AppMainActivity.ShowBookingList();
                                                dismiss();
                                            }
                                        });
                                    } catch (Exception e) {
                                        ((FragmentActivity) getContext()).runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getContext(), "Unable to cancel booking now, try again later.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                } else {
                                    try {
                                        ((FragmentActivity) getContext()).runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getContext(), "Unable to cancel booking now, try again later.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (IOException | XmlPullParserException | RuntimeException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }// End dAoInBackground
    }// End class PrefetchData
*/

    private void dismiss() {
        if (fm != null)
            fm.popBackStack();

        if (handler != null) {
            handler.removeCallbacks(run);
        }
    }


    private void showErrorDialog(String message){
        try {
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("")
                    .setContentText(message )
                    .setConfirmText(p.getOk())
                    .showCancelButton(false)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            //                                        new InitializeAppDb(mContext, true).execute();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleCancelResponse(String response,String ref){
        try {
            final String RESPONSE_SUCCESS = "success";
            if (response != null && !response.equals("")) {
                JSONObject parentObject = new JSONObject(response);
                if (parentObject.getBoolean("HasError")) {

                    showErrorDialog(parentObject.getString("Message"));

                } else {
                    JSONObject dataJson =  parentObject.getJSONObject("Data");
                    String bookingStatus =  dataJson.getString("BookingStatus");
                    if (bookingStatus.equals("true")) {
                        try {
                            mDatabaseOperations.UpdateBookingStatus(CommonVariables.STATUS_CANCLED, ref);

                            if (mDatabaseOperations.getActiveBookings().size() <= 0) {
                                try {
                                    ((Fragment_Main) getContext()).unBindService();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            ((FragmentActivity) getContext()).runOnUiThread(new Runnable() {
                                public void run() {

                                    CommonVariables.AppMainActivity.ShowBookingList();
                                    dismiss();
                                }
                            });
                        } catch (Exception e) {
                            ((FragmentActivity) getContext()).runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getContext(), "Unable to cancel booking now, try again later.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        try {
                            ((FragmentActivity) getContext()).runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getContext(), "Unable to cancel booking now, try again later.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void setConditionsAndStatusForColors(String status) {
        // status = confirmed | completed
        if (status.equalsIgnoreCase(CONFIRMED) || status.equalsIgnoreCase(COMPLETED)) {
            statusColor = "#23C552";
        } else if (status.equalsIgnoreCase(CANCELLED)) {
            statusColor = "#F84F31";
        } else if (status.equalsIgnoreCase(ON_ROUTE)) {
            statusColor = "#a8a8a8";
        } else if (status.equalsIgnoreCase(POB)) {
            statusColor = "#7cd992";
        } else if (status.equalsIgnoreCase(ARRIVED)) {
            statusColor = "#28CC2D";
        } else if (status.equalsIgnoreCase(STC)) {
            statusColor = "#3581d8";
        } else {

            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    statusColor = "#ffffff";
                    break;

                case Configuration.UI_MODE_NIGHT_NO:

                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    statusColor = "#000000";
                    break;
            }

        }

        JT_jstatus.setTextColor(Color.parseColor(statusColor));
        if (status.equalsIgnoreCase("SoonToClear")) {
            String ss = status.replace("SoonToClear", "STC");
            JT_jstatus.setText(ss);
        } else if (status.equalsIgnoreCase(POB)) {
            String ss = status.replace(POB, "OnBoard");
            JT_jstatus.setText(ss);
        } else {
            JT_jstatus.setText(status);
        }

        try {
            mDatabaseOperations.UpdateBookingStatus(status, mBookingDetails.getRefrenceNo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatus(String status) {
        try {
            if (mBookingDetails != null) {
                setConditionsAndStatusForColors(status);

                if (status.equalsIgnoreCase(CONFIRMED)) {
                    try {
                        driverCallAndChatVisibility(GONE, GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (status.equalsIgnoreCase("waiting")) {
                    try {
                        countDownTimer.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        findingNearestDriverRl.setVisibility(GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        driverCallAndChatVisibility(GONE, GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (status.equalsIgnoreCase(COMPLETED) || status.equalsIgnoreCase(Available)) {
                    //   if (status.equalsIgnoreCase(COMPLETED)) {

                    if(status.equalsIgnoreCase(COMPLETED)){
                        status = "COMPLETED";
                    }
                    try {
                        /*if (((Fragment_Main) getActivity()).mService.getPubnub() != null) {
                            ((Fragment_Main) getActivity()).mService.getPubnub().unsubscribeAll();
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (isAdded() && Service_NotifyStatus.isTrackingLive) {
                        if (ratingDialogue == null) {
                            ratingDialogue = ratingDialogDemo();
                            isRatingInPending = false;
                        }
                    } else {
                        isRatingInPending = true;
                    }

                    try {
                        Fragment fragment = ((Fragment_Main) getActivity()).getSupportFragmentManager().findFragmentByTag("current");
                        if (fragment instanceof Fragment_AllBooking) {
                            ((Fragment_AllBooking) fragment).getFromDb();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ((Fragment_Main) getActivity()).unBindService();
                    ((Fragment_Main) getActivity()).stopService(new Intent(getActivity(), Service_NotifyStatus.class));


                    try {
                        countDownTimer.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    findingNearestDriverRl.setVisibility(GONE);
                    driverCallAndChatVisibility(GONE, GONE);
                }

                if (status.equalsIgnoreCase(POB)) {
                    status = status.replace("PassengerOnBoard", "ONBOARD");

                    if (ShareTrackingFlag.equals("2") || ShareTrackingFlag.equals("1")) {
                        //   shareTracking.setVisibility(VISIBLE);
                    }

                    try {
                        MarkerOptions from = new MarkerOptions();
                        from.icon(BitmapDescriptorFactory.fromResource(R.drawable.bc_passengerpin));
                        double lat = Double.parseDouble(mBookingDetails.getDropLat());
                        double lng = Double.parseDouble(mBookingDetails.getDropLon());
                        LatLng positionFrom = new LatLng(lat, lng);
                        from.position(positionFrom);
                        if (fromMaker != null) {
                            fromMaker.remove();
                        }
                        fromMaker = mMap.addMarker(from);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    findingNearestDriverRl.setVisibility(GONE);
                    driverCallAndChatVisibility(GONE, GONE);
                }

                if (status.equalsIgnoreCase(STC) || status.equalsIgnoreCase("STC")) {

                    if (ShareTrackingFlag.equals("2") || ShareTrackingFlag.equals("1")) {
                        //  shareTracking.setVisibility(VISIBLE);
                    }
//                    try {
//                        mDatabaseOperations.UpdateBookingStatus("STC", mBookingDetails.getRefrenceNo());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    try {
                        MarkerOptions from = new MarkerOptions();
                        from.icon(BitmapDescriptorFactory.fromResource(R.drawable.bc_passengerpin));
                        double lat = Double.parseDouble(mBookingDetails.getDropLat());
                        double lng = Double.parseDouble(mBookingDetails.getDropLon());
                        LatLng positionFrom = new LatLng(lat, lng);
                        from.position(positionFrom);
                        if (fromMaker != null) {
                            fromMaker.remove();
                        }
                        fromMaker = mMap.addMarker(from);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    findingNearestDriverRl.setVisibility(GONE);
                    driverCallAndChatVisibility(GONE, GONE);

                }

                if (status.equalsIgnoreCase("cancelled")) {
                    try {
                       /* if (((Fragment_Main) getActivity()).mService.getPubnub() != null) {
                            ((Fragment_Main) getActivity()).mService.getPubnub().unsubscribeAll();
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ((Fragment_Main) getActivity()).unBindService();
                    ((Fragment_Main) getActivity()).stopService(new Intent(getActivity(), Service_NotifyStatus.class));

                    driverCallAndChatVisibility(GONE, GONE);
                    findingNearestDriverRl.setVisibility(GONE);

                }

                if (status.equalsIgnoreCase(ARRIVED)) {
                    if (ShareTrackingFlag.equals("1")) {
                        //  shareTracking.setVisibility(VISIBLE);
                    }
                    driverCallAndChatVisibility(GONE, GONE);
                }

                if (status.equalsIgnoreCase(ONROUTE)) {
                    LatLng Pickposition = new LatLng(Double.parseDouble(mBookingDetails.getPickupLat()), Double.parseDouble(mBookingDetails.getPickupLon()));
                    fromMaker = mMap.addMarker(new MarkerOptions()
                            .position(Pickposition)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bc_passengerpin)));
                    try {
                        sharedPrefrenceHelper.removeRefNoAsapValue(mBookingDetails.getRefrenceNo());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (ShareTrackingFlag.equals("1")) {
                            //  shareTracking.setVisibility(VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        findingNearestDriverRl.setVisibility(GONE);
                        driverCallAndChatVisibility(VISIBLE, GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSMSMessage() {
        String phoneNo = DriverContactNo;
//		message = txtMessage.getText().toString();

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            try {
                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(DriverContactNo, null, msgToDriver, null, null);
                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(p.getMessageSentSuccessfully())
                        .show();
            } catch (Exception e) {
                FBToast.errorToast(getActivity(),
                        "SMS faild, please try again later!",
                        Toast.LENGTH_LONG);
                e.printStackTrace();
            }
// new SweetAlertDialog()
        }
    }

    private void rotateMarker(final Marker marker, final float toRotation) {
        if (!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 50);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    private void animateMarkerNew(final LatLng destination, final Marker marker) {
        try {
            if (marker != null) {

                final LatLng startPosition = marker.getPosition();
                final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(marker.getPosition())
                        .zoom(mMap.getCameraPosition().zoom)
                        .build()));

                LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();
                try {
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                    valueAnimator.setDuration(8000); // duration 3 second
                    valueAnimator.setInterpolator(new LinearInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            try {
                                if (isAdded()) {
                                    float v = animation.getAnimatedFraction();

                                    LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                                    marker.setPosition(newPosition);
                                    //                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                    //                            .target(newPosition)
                                    //                            .zoom(15.5f)
                                    //                            .build()));
                                    //                        bearingBetweenLocations(startPosition,newPosition);
                                    //                                marker.setRotation(getBearing(startPosition, newPosition));

                                }
                            } catch (Exception ex) {
                                //I don't care atm..
                                //                            movingDrivers.remove(marker.getTag().toString());
                            }
                        }
                    });
                    valueAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
                    valueAnimator.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
    }

    private Float bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
//if(brng==0.0){
//	return getBearing(latLng1,latLng2);
//}
        return (float) brng;
    }

    public static float roundfloat(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public Bitmap getThumbnail(String filename) {

//        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;
        Bitmap thumbnail = null;


// If no file on external storage, look in internal storage
        if (thumbnail == null) {
            try {
                File filePath = getActivity().getFileStreamPath(filename);
                FileInputStream fi = new FileInputStream(filePath);
                thumbnail = BitmapFactory.decodeStream(fi);
            } catch (Exception ex) {
                Log.e(" on internal storage", ex.getMessage());
            }
        }
        return thumbnail;
    }

    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    private ValueAnimator polylineFlasher(GoogleMap mMap, List<LatLng> polyLineList) {
        try {
            Collections.reverse(polyLineList);
            polylineOptions = new PolylineOptions();
            polylineOptions.color(getActivity().getResources().getColor(R.color.color_black_inverse));
            polylineOptions.width(5);
            polylineOptions.startCap(new RoundCap());
            polylineOptions.endCap(new RoundCap());
            polylineOptions.jointType(ROUND);
            polylineOptions.addAll(polyLineList);
            try {
                greyPolyLine = mMap.addPolyline(polylineOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }

            blackPolylineOptions = new PolylineOptions();
            blackPolylineOptions.width(7);
            blackPolylineOptions.color(getActivity().getResources().getColor(R.color.red));
            blackPolylineOptions.startCap(new RoundCap());
            blackPolylineOptions.endCap(new RoundCap());
            blackPolylineOptions.jointType(ROUND);
            blackPolylineOptions.addAll(polyLineList);

            try {
                blackPolyline = mMap.addPolyline(blackPolylineOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
            polylineAnimator.setDuration(5000);
            polylineAnimator.setRepeatCount(ValueAnimator.INFINITE);
            polylineAnimator.setInterpolator(new LinearInterpolator());

            polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    List<LatLng> points = greyPolyLine.getPoints();
                    int percentValue = (int) valueAnimator.getAnimatedValue();
                    int size = points.size();
                    int newPoints = (int) (size * (percentValue / 100.0f));
                    List<LatLng> p = points.subList(0, newPoints);
                    blackPolyline.setPoints(p);
                }
            });
            polylineAnimator.start();
            return polylineAnimator;
        } catch (Exception e) {
            return null;
        }
    }

    private class ResendEmailReceipt extends AsyncTask<String, Void, String> {
        private String METHOD_NAME = "RequestReceipt";


        private static final String KEY_DEFAULT_CLIENT_ID = "defaultclientId";
        private static final String KEY_HASHKEY = "hashKey";

        private static final String Booking_information = "jsonString";
        private static final String HASHKEY_VALUE = "4321orue";

        private SweetAlertDialog mDialog;


        public ResendEmailReceipt() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                sharedPrefrenceHelper = new SharedPrefrenceHelper(getActivity());
                String deviceid = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);


                ShareTracking obj = new ShareTracking();
                obj.SubCompanyId = CommonVariables.SUB_COMPANY;
                obj.defaultClientId = (int) CommonVariables.clientid;
                obj.uniqueValue = CommonVariables.clientid + HASHKEY_VALUE;
                obj.UniqueId = deviceid;
                obj.DeviceInfo = "Android";

                obj.JobId = mBookingDetails.getRefrenceNo();
                String token = AppConstants.getAppConstants().getToken();

                HashMap<String, Object> rootMap = new HashMap<>();
                rootMap.put("jsonString", obj);
                rootMap.put("Token", token);
//        map.put("Token", CommonVariables.TOKEN);


                String jsonString = new Gson().toJson(rootMap);

//            obj.Title = model.getName() + "_Review From Customer App";

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
                Request request = new Request.Builder()
                        .url(CommonVariables.BASE_URL + "RequestReceipt")
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    return response.body().string();

                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }

            }catch (Exception ex){
                ex.printStackTrace();
                return "";
            }

          /*  final String json_String = new Gson().toJson(obj);
            SoapHelper.Builder builder = new SoapHelper.Builder(CommonVariables.SERVICE, getActivity());
            builder.setMethodName("RequestReceipt", true)
                    .addProperty(Booking_information, json_String, PropertyInfo.STRING_CLASS);


            try {
                return builder.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }*/


        }

    }

    private class getNumberFromServer extends AsyncTask<String, Void, String> {
        private String METHOD_NAME = "ShareJourney";

        private String METHOD_NAME_ALL = "SaveCustomerReviews";

        private static final String KEY_DEFAULT_CLIENT_ID = "defaultclientId";
        private static final String KEY_HASHKEY = "hashKey";

        private static final String Booking_information = "jsonString";
        private static final String HASHKEY_VALUE = "4321orue";

        private ProgressDialog mDialog;


        public getNumberFromServer() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mDialog = new ProgressDialog(getActivity());
                mDialog.setTitle("Getting Details");
                mDialog.setMessage("Please wait..");
                mDialog.setCancelable(false);
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null && !result.isEmpty()) {
                if (result != null && !result.isEmpty() && isAdded()) {
                    try {
                        JSONObject parentObject = new JSONObject(result);

                        if (parentObject.getBoolean("HasError")) {
                            FBToast.errorToast(getActivity(), parentObject.getString("Message"), FBToast.LENGTH_SHORT);

                        } else {
//                            JSONObject jsonObject = new JSONObject(parentObject.getString("Data"));
                            shareTracking(parentObject.getString("Data"));
                        }
                    } catch (Exception e) {

                    }
                }
            }

            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            sharedPrefrenceHelper = new SharedPrefrenceHelper(getActivity());
            SettingsModel model = sharedPrefrenceHelper.getSettingModel();
            String deviceid = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            ShareTracking obj = new ShareTracking();
            obj.defaultClientId = (int) CommonVariables.clientid;
            obj.uniqueValue = CommonVariables.clientid + HASHKEY_VALUE;
            obj.UniqueId = deviceid;
            obj.SubCompanyId = CommonVariables.SUB_COMPANY;
            obj.DeviceInfo = "Android";
            obj.JobId = mBookingDetails.getRefrenceNo();
            obj.DriverId = mDriverId;


            final String json_String = new Gson().toJson(obj);
            SoapHelper.Builder builder = new SoapHelper.Builder(CommonVariables.SERVICE, getActivity());
            builder.setMethodName(METHOD_NAME, true)
                    .addProperty(Booking_information, json_String, PropertyInfo.STRING_CLASS);




               /* OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + "ShareJourney")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }*/





            try {
                return builder.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (sharedPrefrenceHelper == null) {
                sharedPrefrenceHelper = new SharedPrefrenceHelper(getActivity());
            }
            sharedPrefrenceHelper.putVal("isfeedback_" + mBookingDetails.getRefrenceNo(), "1");
            return null;
        }

    }

    private void startTimer(double time) {
        try {
            long _time = (long) (3000 * time);

            new CountDownTimer(_time, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    try {
                        findingNearestDriverRl.setVisibility(GONE);
                        bottomLl.setVisibility(VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void driverCallAndChatVisibility(int visibilityForCall, int visibilityForChat) {
        driverCallRl.setVisibility(visibilityForCall);
        driverChatRl.setVisibility(visibilityForChat);
    }

    private void setDriverImage(String imageUrl, ImageView imageView) {
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
    }

    private TrackingInformation getTrackingDetails() {
        TrackingInformation trackingInformation = new TrackingInformation();
        trackingInformation.defaultClientId = (int) CommonVariables.localid;
        trackingInformation.fromLatLng = "" + mBookingDetails.getFromLatLng();
        trackingInformation.toLatLng = "" + mBookingDetails.getToLatLng();
        trackingInformation.VehicleTypeId = 0;
        trackingInformation.MapKey = CommonVariables.GOOGLE_API_KEY;
        trackingInformation.MapType = 1;
        trackingInformation.DriverId = "" + mDriverId;
        trackingInformation.JobId = "" + mBookingDetails.getRefrenceNo();
        trackingInformation.uniqueValue = CommonVariables.localid + "4321orue";
        return trackingInformation;
    }

    // ```   `````  Feedback work  ```` ```` //

    private Dialog ratingDialogDemo() {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Widget_DeviceDefault);
        dialog.setContentView(R.layout.layout_feedback);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView closeBottomSheet = dialog.findViewById(R.id.closeBottomSheet);
        closeBottomSheet.setVisibility(GONE);

        TextView addATipTv = dialog.findViewById(R.id.addATipTv);
        addATipTv.setVisibility(GONE);

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
            if (driverInformation.getDriverImage() == null || driverInformation.getDriverImage().equals("")) {
                driverInformation.setDriverImage(sharedPrefrenceHelper.getVal("driverImage_" + mBookingDetails.getRefrenceNo()));
            }
            setDriverImage(driverInformation.getDriverImage(), driverImageIv);
        } catch (Exception e) {
            e.printStackTrace();
        }

        driverRatingTv.setText("1.0");

        try {
            driveNameTv.setText("" + driverInformation.getDriverName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        vehicleNameTv.setText("" + DrvCar_Name.getText().toString());
        vehicleNoPlateTv.setText("" + Car_plate.getText().toString());

        try {
            int rating = sharedPrefrenceHelper.getIntVal("rating_" + mBookingDetails.getRefrenceNo());
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
                if (sharedPrefrenceHelper == null) {
                    sharedPrefrenceHelper = new SharedPrefrenceHelper(getActivity());
                }
                sharedPrefrenceHelper.putVal("isfeedback_" + mBookingDetails.getRefrenceNo(), "1");
                CommonVariables.AppMainActivity.ShowBookingList();

                dismiss();
            }
        });

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

                    feedbackInformation = getFeedbackInformation(feedbacktxt, ratings, IsFeedback);

                    if (sp.getString(CommonVariables.EnableTip, "0").equals("1")) {
                        if (tipFares.equals("")) {
                            feedbackInformation.CardDetail = null;
                            feedbackInformation.Tip = 0;
                            new Manager_SubmitFeedback(getContext(), feedbackInformation, listenerSubmitFeedback).execute();
                        } else {
                            // open payment screen
                            startActivityForResult(new Intent(getActivity(), Activity_HomePayment.class), 4444);
                        }
                    } else {
                        feedbackInformation.CardDetail = null;
                        feedbackInformation.Tip = 0;
                        new Manager_SubmitFeedback(getContext(), feedbackInformation, listenerSubmitFeedback).execute();
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
        if (sp.getString(Config.SendReceiptOnClear, "").equals("1")) {
            new ResendEmailReceipt().execute();
        }
        return dialog;
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

    private FeedbackInformation getFeedbackInformation(String feedBack, int ratings, boolean IsFeedback) {
        feedbackInformation = new FeedbackInformation();
        feedbackInformation.defaultclientId = "" + CommonVariables.clientid;
        feedbackInformation.hashKey = CommonVariables.clientid + "4321orue";
        feedbackInformation.BookingReference = mBookingDetails.getRefrenceNo();
        feedbackInformation.ClientName = settingsModel.getName();
        feedbackInformation.Email = settingsModel.getEmail();
        feedbackInformation.Location = mBookingDetails.gettoAddress();
        feedbackInformation.Message = feedBack;
        feedbackInformation.Rating = ratings;
        feedbackInformation.IsFeedback = IsFeedback;
        feedbackInformation.Title = settingsModel.getName() + "_Review From Customer App";
        return feedbackInformation;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 4444 && resultCode == RESULT_OK) {
            try {
                if (mBookingDetails.getPaymentType().toLowerCase()
                        .startsWith("credit card") &&
                        sp.getString(CommonVariables.EnableTip, "0").equals("1")) {
                    if (data != null && !checkIfHasNullForString(data.getStringExtra("pm"))
                            .equals("")) {
                        String Gateway = sp.getString(Config.Gateway, "").toLowerCase();
                        if (Gateway.equalsIgnoreCase(Config.Stripe)) {
                            String pm = checkIfHasNullForString(data.getStringExtra("pm"));
                            processStripePayment(pm, Double.parseDouble(tipFares));
                        }
                    } else {
                        FBToast.errorToast(getActivity(), "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                    }
                } else {
                    FBToast.warningToast(getActivity(), "Tip is not enabled.", FBToast.LENGTH_SHORT);
                }
            } catch (Exception e) {
                e.printStackTrace();
                FBToast.errorToast(getActivity(), "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    boolean IsFeedback = true;

    private void processStripePayment(String pm, double amount) {
        try {
            if (amount == 0) {
                FBToast.warningToast(getActivity(), "Amount should be a greater than " + sp.getString(CurrencySymbol, "\u00A3") + "0.30", FBToast.LENGTH_SHORT);
                return;
            }

            if (pm.equals("")) {
                FBToast.errorToast(getActivity(), "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                return;
            }

            new Manager_StripePrePayment(
                    getActivity(),
                    "" + pm,
                    "" + sharedPrefrenceHelper.getStripeCustomerId(),
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
}