package base.newui;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
import static base.activities.Activity_SearchAddress.KEY_HOME;
import static base.activities.Activity_SearchAddress.KEY_OFFICE;
import static base.fragments.Fragment_Main.EmailTv;
import static base.fragments.Fragment_Main.NameTv;
import static base.fragments.Fragment_Main.PhoneTv;
import static base.newui.HomeFragment.AddressLocationType.Pickup;
import static base.utils.CommonMethods.checkIfHasNullForDouble;
import static base.utils.CommonMethods.checkIfHasNullForString;
import static base.utils.CommonVariables.Currency;
import static base.utils.CommonVariables.CurrencySymbol;
import static base.utils.CommonVariables.JUDO_REGISTER_REQUEST;
import static base.utils.Config.CASH;
import static base.utils.Config.ShowFares;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.amalbit.trail.RouteOverlayView;
import com.amalbit.trail.contract.GooglemapProvider;
import com.eurosoft.customerapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.support.parser.PropertyInfo;
import com.support.parser.SoapHelper;
import com.tfb.fbtoast.FBToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import base.activities.Activity_AddExtras;
import base.activities.Activity_AddPromoCode;
import base.activities.Activity_HomePayment;
import base.activities.Activity_SearchAddress;
import base.activities.Activity_SearchAddressForHomeAndWork;
import base.adapters.BookingAdapter;
import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.fragments.Fragment_Main;
import base.fragments.Fragment_PickupDateTime;
import base.fragments.Fragment_PickupTime;
import base.fragments.Fragment_Tracking;
import base.listener.Listener_GetAddressCoordinate;
import base.listener.Listener_GetAllFaresFromDispatchNew;
import base.listener.Listener_GetAvailableDriversManager;
import base.listener.Listener_GetInfo;
import base.listener.Listener_GetRouteCoordinatesDataNew;
import base.listener.Listener_OnSetResult;
import base.listener.Listener_OnSetResultList;
import base.listener.Listener_Stripe_GetAllCards;
import base.manager.Manager_GetAddressCoordinates;
import base.manager.Manager_GetAllFaresFromDispatchNew;

import base.manager.Manager_GetAvailableDriversByVehicleETA;
import base.manager.Manager_GetInfo;
import base.manager.Manager_GetRouteCoordinatesDataNew;
import base.manager.Manager_StripePrePayment;
import base.manager.Manager_Stripe_GetAllCards;
import base.manager.Manager_ValidateBookingInfo;
import base.models.CardJudoModel;
import base.models.ClsLocationData;
import base.models.ConfirmedBooking;
import base.models.ExtrasModel;
import base.models.KonnectCardModel;
import base.models.LocAndField;
import base.models.Model_BookingDetailsModel;
import base.models.Model_BookingInformation;
import base.models.Model_ValidateBookingInfo;
import base.models.ParentPojo;
import base.models.PromoModel;
import base.models.PromotionModel;
import base.models.ReqAvailableDrivers;
import base.models.SettingsModel;
import base.models.StripeCardModel;
import base.models.Stripe_Model;
import base.models.VehicleModel;
import base.services.Service_NotifyStatus;
import base.utils.AppConstants;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.AnyVehicleFareModel;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.ViaAddresses;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment implements
        OnCameraChangeListener,
        OnClickListener,
        Listener_OnSetResult,
        Listener_OnSetResultList,
        OnBackStackChangedListener,
        GooglemapProvider,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, Fragment_Main.IOnBackPressed {

    public static final String KEY_BOOKING_MODEL = "keyReviewBundle";
    // STATIC STRING
    public static final String TAG = "test";
    public static final String KEY_FROM_LOCATION = "keyFromLocation";
    //meet and greet
    private String isMeetnGreet = "0";
    public static final String KEY_VIA_LOCATION = "keyViaLocation";
    public static final String KEY_TIME = "keyTimeStatus";
    public static final String KEY_MILAGE = "keyMilage";
    public static final String KEY_RESULT_BOOKING = "result";
    public static final String KEY_SELECTED_VEHICLE = "selectedvehicle";
    public static final String KEY_RESULT_LATITUDE = "lat";
    public static final String KEY_RESULT_LONGITUDE = "lon";
    public static final String TAG_HOME_STACK = "homeStack";
    public static String isServiceRunningInBackground = "isServiceRunningInBackground";
    public static String CurrSelectedCar = "";
    public static String from = "";
    public static String selectedCar = "";
    public static String address;
    public static String flightDateForSchedule = "Today";
    public static String allowanceTime = "";

    // STATIC INT-DOUBLE
    public static final int REQUEST_CHECK_SETTINGS = 1021;
    private static final int Account_Success = 1223;
    public static final int REQUEST_LOCATION = 223;
    public static int paymentSelection = 0;
    public static int selectedPos = 0;
    public static String refNoForAsap = "";
    public static double lats, lons;

    // STATIC BOOLEAN
    public static boolean alreadInFav = false;
    public static boolean isChooseFromList = false;
    public static boolean isHomeVissibel = true;
    public static boolean isPostalCodeAvailable = false;
    public static boolean forFirstTimeOnly = true;
    public static boolean isFirstTime = true;
    public static boolean isClickedRebooked = false;
    public static String customDate = "";
    public static String customTime = "";
    public static String _comingFrom = "";
    public static String _fightNo = "";
    public static String specialInstruction = "";
    public static String fromComing = "";
    public static String fromDoorNo = "";

    // STRING
    private String driverlat = "00.0000000";
    private String driverlog = "0.000000000";
    private String mPickupTime = "";
    private String mPickupDate = "";
    private String latdriver = "";
    private String longdriver = "";
    private String Gateway = "";
    private Boolean IsKonnectPay = false;

    private static String log = "";
    private String pickup;
    private String estimatedFareText = "";
    private String _msg = "";
    private String title = "";
    private String buttonText = "";
    private String tempDate = "";
    private String VerifiedCode = "";
    private String RouteCoordinates = "";
    private String journeyMiles = "0";
    private String PickupToDestinationTime;
    private String shoppingFares = "";
    private String promoCodeMiniMumFare;
    private String PromotionStartDateTime;
    private String PromotionEndDateTime;
    private String currentSelectedFaresOfVehicle = "-1";
    String selectedPaymentType = "";
    // INTEGER
    private int CurrSelectedCarId = -1;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int accountLoggedIn = -1;
    private final int REQUEST_CODE_WorldPay = 1131;
    private double latpick;
    private double longpick;
    private double extraTotalBabySeat = 0;
    private float vehicleTimer;
    private double finalPrice = 0f;

    // BOOLEAN
    private boolean isMovingCamera = false;
    private boolean firstLoad = true;
    private boolean isShopping = false;
    private boolean isShowVehicleFare = false;
    private boolean isPendingDirectionDialogue = false;
    private boolean isActivityInBackground = false;
    private boolean isrouteComplete = false;
    private boolean isAddressSelected = false;
    private boolean isMarkerRotating = false;
    private boolean isRouteSuccessFull = false;
    private boolean fromPickLocation = false;
    private boolean confirmbtn = true;

    // LIST
    private List path = new ArrayList<>();
    private List<String> driverslist2 = new ArrayList<>();
    private List<VehicleModel> carNames = new ArrayList<>();
    private List<String> currentMarkers = new ArrayList<>();
    private List<Driver> driverslist1 = new ArrayList<>();
    private List<String> movingDrivers = new ArrayList<>();
    private ArrayList<PromoModel> promList = new ArrayList<>();
    private ArrayList<ExtrasModel> extrasList = new ArrayList<>();
    public ArrayList<LocAndField> locAndFieldArrayList = new ArrayList<>();
    private ArrayList<AnyVehicleFareModel> mFareModel = new ArrayList<AnyVehicleFareModel>();
    private ArrayList<Marker> markers = new ArrayList<>();

    // VIEWS
    private SweetAlertDialog mDialog;
    private SweetAlertDialog sweetAlertDialog;

    private TextView setHomeTv;
    private TextView setWorkTv;
    private TextView hTv;
    private TextView wTv;
    private TextView usernameTv;
    private TextView asaptxt;
    private TextView shoppingnotice;
    private TextView ETA_txt2;
    private TextView cardcartxt;
    private TextView dateBottom;
    private TextView timeBottom;
    private TextView confirmbottom;
    private TextView promotxt;
    private TextView asap, asap15, asap30;
    private TextView dateTimeTxt;
    private TextView fare_txt;
    private TextView fare_txt1;
    private TextView fare_txtlabel;
    private TextView ZeroFareTxt;
    private TextView cardWhereTo;
    private TextView confirm_booking;
    private TextView dayNightTv;
    public TextView cashtxt;

    private ImageView estimatedFareIcon;
    private ImageView sunIv;
    private ImageView closeBottomSheet;
    private ImageView menu_btn;
    private ImageView cashImg;
    private ImageView promo_image;

    private LinearLayout showOrderSheet;
    private LinearLayout addPromo;
    private LinearLayout home_part_2_cv;
    private LinearLayout home_part_1_cv;
    private LinearLayout cash_s_lyt;
    private LinearLayout DrvNotes;
    private LinearLayout shoppingVehicleLayout;
    private LinearLayout viewBarLl;


    private CardView etacard;
    private CardView cardWhereTo1;
    private CardView currentLocationCv;

    private View locationButton;

    private RelativeLayout homeRl;
    private RelativeLayout workRl;
    private RelativeLayout center_markerlayout;

    private BottomSheetDialog builder;
    private BottomSheetDialog dialog;
    private CheckBox openBottomSheet;

    // CLASS
    private SupportMapFragment mapFragment;
    private LocAndField mCurrentLocationLocAndField;
    private AddressLocationType mCurrentAddressType = Pickup;
    private GoogleApiClient mGoogleClient;
    private GoogleMap mMap;
    private SharedPreferences sp;
    private Animation animBounce;
    private Activity mContext;
    private SettingsModel userModel;

    String _CardDetails = "";
    String stripCardToken = "";
    String stripCustomerId = "";

    private Marker fromMarker;
    private Model_BookingDetailsModel model;
    private SharedPrefrenceHelper mHelper;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location CurrLoc;
    private PromoModel promoModel;
    //    private Marker fromMarkerRoute = null;
    public static Location mCurrLoc;
    private LocAndField workAddress, homeAddress = null;
    private ParentPojo p = new ParentPojo();
    private int locType = 1;
    private boolean isFromRebook = false;
    private RouteOverlayView mapOverlayView;
    private BottomSheetBehavior sheetBehavior;
    private RelativeLayout mapViewRl;
    private boolean isSignupPromoApplied = false;

    // listener
    private Listener_GetAddressCoordinate listener_getAddressCoordinate;
    private Listener_Stripe_GetAllCards listener_stripe_getAllCards;
    private Listener_GetAvailableDriversManager availableDriversManagerListener;
    // Java
    private DatabaseOperations mDatabaseOperations;

    private RecyclerView rvVehicle;
    private VehicleListAdapter vehicleAdapter;
    private TextView SurgeFareTxt;
    boolean oneTime = true;
    private String isRebook = "";
    private int countForHits = 0;
    public static String locsName = "";
    String preValue = "";
    String firstValueOnly = "";
    // HANDLERS
//    private Handler mCameraHandler;

    /*private final Runnable mCameraMoveCallback = new Runnable() {
        @Override
        public void run() {
            try {
                if ((!isChooseFromList && !isrouteComplete && (locAndFieldArrayList.size() == 0)) || (!isChooseFromList)) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };*/

    private Runnable m_runnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: run: run: m_runnable  : " + latdriver + "," + longdriver);

            ReqAvailableDrivers reqAvailableDrivers = new ReqAvailableDrivers();
            reqAvailableDrivers.defaultClientId = (int) CommonVariables.localid;

            try {
                if (getArguments() != null && getArguments().getBoolean("again", false)) {
                    reqAvailableDrivers.latitude = Double.parseDouble(locAndFieldArrayList.get(0).getLat());
                    reqAvailableDrivers.longitude = Double.parseDouble(locAndFieldArrayList.get(0).getLon());
                } else {
                    reqAvailableDrivers.latitude = Double.parseDouble(latdriver);//CurrLoc.getLongitude();
                    reqAvailableDrivers.longitude = Double.parseDouble(longdriver);//CurrLoc.getLatitude();
                }

                lats = reqAvailableDrivers.latitude;
                lons = reqAvailableDrivers.longitude;
            } catch (Exception e) {
                e.printStackTrace();
                reqAvailableDrivers.latitude = 0;
            }


            Log.d(TAG, "run: run: run: ---  : " + latdriver + "," + longdriver);


            reqAvailableDrivers.vehicleName = selectedCar;//selectedCar;
            reqAvailableDrivers.locType = 1;// locType;
            reqAvailableDrivers.MapKey = CommonVariables.GOOGLE_API_KEY;
            reqAvailableDrivers.mapType = 0;
            reqAvailableDrivers.uniqueValue = CommonVariables.clientid + "4321orue";

            new Manager_GetAvailableDriversByVehicleETA(getContext(), reqAvailableDrivers, availableDriversManagerListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            if (vehicleTimer > 0) {
                handle.postDelayed(m_runnable, (long) vehicleTimer);
            }
        }
    };

    private Handler handle = new Handler() {
        public void handleMessage(final android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 30) {
            }
        }
    };
    boolean makeArcAgain = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObjects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View ret = inflater.inflate(R.layout.layout_home_activity, container, false);

        init(ret);

        initData(container);

        listener(ret);

        return ret;
    }

    private void initObjects() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        BookingAdapter.isTracking = false;
        flightDateForSchedule = "Today";
        Fragment_Main.fromTracking = false;
        extraTotalBabySeat = 0;
        mContext = getActivity();
        model = new Model_BookingDetailsModel();
        locAndFieldArrayList = new ArrayList<>();
        mHelper = new SharedPrefrenceHelper(mContext);
        sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        userModel = mHelper.getSettingModel();
        mDatabaseOperations = new DatabaseOperations(new DatabaseHelper(mContext));

        Gateway = sp.getString(Config.Gateway, "").toLowerCase();
        IsKonnectPay = sp.getString(Config.IsKonnectPay, "0").equals("1");

        estimatedFareText = sp.getString(Config.EstimatedFareTxt, "");
        vehicleTimer = Float.parseFloat(sp.getString(Config.VEHICLEMOVEMENTTIMER, "0.0")) * 1000;
        selectedPaymentType = sp.getString(CommonVariables.paymentType, "Cash").toLowerCase();
        model.setPaymentType(selectedPaymentType);

        carNames = mDatabaseOperations.getAllVehicles();
        promList = mDatabaseOperations.getPromoDetails();
        customDate = "";
        customTime = "";
    }

    private void initData(ViewGroup container) {
        if (userModel.getUserServerID() == null || userModel.getUserServerID().equals("")) {
            new GetIdByEmail().execute(userModel);
        }

        NameTv.setText(userModel.getName().toUpperCase());
        PhoneTv.setText(userModel.getPhoneNo());
        EmailTv.setText(userModel.getEmail());

        shoppingnotice.setSelected(true);

        sp.edit().putInt(Config.PROMO_ATTEMPT_COUNT, 0).commit();

        if (sp.getString(Config.ApplyPromotion, "0").equals("1")) {
            if (sp.getBoolean(Config.ISFIRSTSIGNUP, false)) {
                new GetSignupPromotions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                if (!userModel.getPromoCode().equals("")) {
                    VerifiedCode = userModel.getPromoCode();
                    isSignupPromoApplied = true;
                }
            }
        }


        if (sp.getString(Config.ShoppingEnabled, "0").equals("1")) {
            showOrderSheet.setVisibility(VISIBLE);
            openBottomSheet.setVisibility(VISIBLE);
            if (!sp.getString(Config.ShoppingNotice, "").equals("")) {
                shoppingnotice.setText(sp.getString(Config.ShoppingNotice, ""));
            }
        } else {
            showOrderSheet.setVisibility(GONE);
        }

        cash_s_lyt.setVisibility(View.VISIBLE);

        firstLoad = true;

        setUpBottomSheetDateTime();


//        if (isClickedRebooked) {
//            cardWhereTo1.setVisibility(GONE);
//        } else {
//            cardWhereTo1.setVisibility(VISIBLE);
//        }


        setDefaultPaymentType();


        if (carNames.size() > 0) {
            CurrSelectedCar = carNames.get(0).getName();
            CurrSelectedCarId = carNames.get(0).getServerId();
        }

        HomeFragment.setCarName(carNames.get(0).getName());
        selectedPos = 0;
        CurrSelectedCar = carNames.get(0).getName();
        CurrSelectedCarId = carNames.get(0).getServerId();

        if (carNames.size() > 0) {
            selectedCar = carNames.get(0).getName();
        }
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
                                        if (ob != null)

                                            // Hide Marker Work
                                            if (ob.optDouble("lat") != 0 && ob.optDouble("lng") != 0 && !sp.getString(Config.HideCurrentLocation, "0").equals("1"))
                                                locAndFieldArrayList.add(setAndGetLocAndField("" + ob.optString("keyword"), "" + ob.optDouble("lat"), "" + ob.optDouble("lng")));


                                    }

                                    for (int i = 0; i < locAndFieldArrayList.size(); i++) {
                                        LocAndField l = locAndFieldArrayList.get(i);
                                        if ((l.getField() == null || l.getField().equals("")) && (l.getLat() == null || l.getLat().equals("0.0") || l.getLat().equals("")) && (l.getLon() == null || l.getLat().equals("0.0") || l.getLon().equals(""))) {
                                            locAndFieldArrayList.remove(i);
                                        }
                                    }

                                    try {
                                        for (int i = 0; i < locAndFieldArrayList.size(); i++) {
                                            LocAndField l = locAndFieldArrayList.get(i);
                                            if ((l.getField() == null || l.getField().equals("")) && (l.getLat() == null || l.getLat().equals("0.0") || l.getLat().equals("")) && (l.getLon() == null || l.getLat().equals("0.0") || l.getLon().equals(""))) {
                                                locAndFieldArrayList.remove(i);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    onAddressChangeListener(true);
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
    }

    private void againWork() {
        vehicleSelectedIndex = 0;
        isFromRebook = true;
        isrouteComplete = true;
        locAndFieldArrayList.clear();
        locAndFieldArrayList = getArguments().getParcelableArrayList("key_locAndFieldArrayList");

        if (checkForLatLngIsZero()) {
            new Manager_GetAddressCoordinates(getContext(),false, setAndGetForAddressCoordinate(), listener_getAddressCoordinate).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            onAddressChangeListener(true);
        }

        model.setPickUpDate(getCurrentDate());

//        CurrSelectedCar = getArguments().getString("vehicle");

        VehicleModel vehicleModel = mDatabaseOperations.getVehicleByName(CurrSelectedCar);
        CurrSelectedCarId = vehicleModel.getServerId();

        if (!carNames.toString().toLowerCase().contains(CurrSelectedCar.toLowerCase())) {
            if (carNames.size() > 0) {
                CurrSelectedCar = carNames.get(0).getName();
                CurrSelectedCarId = carNames.get(0).getServerId();
            }
        }

        isShopping = getArguments().getBoolean("isShopping");
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

    private ArrayList<ClsLocationData> setAndGetForAddressCoordinate() {

        ArrayList<ClsLocationData> arrayList = new ArrayList();

        for (int i = 0; i < locAndFieldArrayList.size(); i++) {
            LocAndField locAndField = locAndFieldArrayList.get(i);

            arrayList.add(new ClsLocationData((int) CommonVariables.localid,
                    "" + locAndField.getField(), 0, 0,
                    "" + CommonVariables.GOOGLE_API_KEY,
                    "" + CommonVariables.Clientip,
                    CommonVariables.localid + "4321orue"));
        }
        return arrayList;

    }

    private void setDefaultPaymentType() {
        String paymentTypes = sp.getString(Config.PaymentTypes, "");
        String[] paymentArr = paymentTypes.split(",");

        if (selectedPaymentType.equalsIgnoreCase("cash")) {
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cash));
            paymentSelection = 0;
            cashtxt.setText("Cash");

        } else if (selectedPaymentType.equalsIgnoreCase("credit card")) {
            sameWork(null, "credit card");

            paymentSelection = (paymentTypes.contains(Config.ACCOUNT) && paymentTypes.contains(CASH)) ? 2 : 1;
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cash));
            if (paymentArr.length == 1) {
                paymentSelection = 0;
            }
            cashtxt.setText("Card");

        } else if (selectedPaymentType.equalsIgnoreCase("account")) {
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_account));
            paymentSelection = (paymentTypes.contains(CASH)) ? 1 : 0;
            cashtxt.setText("Account");

        } else {
//            String[] paymentTypesArr = paymentTypes.split(",");
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_credit_card));
            paymentSelection = paymentArr.length - 1;
            cashtxt.setText(selectedPaymentType);
        }
    }

    private void init(View ret) {
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_container);
        mapOverlayView = ret.findViewById(R.id.mapOverlayView);

        center_markerlayout = ret.findViewById(R.id.center_markerlayout);
        center_markerlayout.setVisibility(GONE);
        cash_s_lyt = ret.findViewById(R.id.cash_s_lyt);
        SurgeFareTxt = ret.findViewById(R.id.SurgeFareTxt);
        DrvNotes = ret.findViewById(R.id.DrvNotes);
        shoppingVehicleLayout = ret.findViewById(R.id.shoppingVehicleLayout);

        sunIv = ret.findViewById(R.id.sunIv);
        promo_image = ret.findViewById(R.id.promo_image);
//        nopin = ret.findViewById(R.id.nopin);
//        noareapin = ret.findViewById(R.id.noareapin);
        cashImg = ret.findViewById(R.id.cashImg);
        cashtxt = ret.findViewById(R.id.cashtxt);
        cashtxt.setText(p.getCash());

        cardcartxt = ret.findViewById(R.id.cardcartxt);
        home_part_2_cv = ret.findViewById(R.id.home_part_2_cv);
        cardWhereTo1 = ret.findViewById(R.id.cardWhereTo1);
        currentLocationCv = ret.findViewById(R.id.currentLocationCv);

        cardWhereTo = ret.findViewById(R.id.cardWhereTo);
        cardWhereTo.setText(p.getWhereTo());

        home_part_1_cv = ret.findViewById(R.id.home_part_1_cv);
        fare_txt = ret.findViewById(R.id.fare_txt);
        ZeroFareTxt = ret.findViewById(R.id.ZeroFareTxt);
        fare_txt1 = ret.findViewById(R.id.fare_txt2);
        fare_txtlabel = ret.findViewById(R.id.fare_txt);
        fare_txtlabel.setVisibility(GONE);
        confirm_booking = ret.findViewById(R.id.confirm_booking);
//        confirm_booking.setText(p.getConfirmBooking());

       /* dayNightTv = ret.findViewById(R.id.dayNightTv);
        dayNightTv.setText(p.getLetsGet());*/

        menu_btn = ret.findViewById(R.id.menu_btn);
        ETA_txt2 = ret.findViewById(R.id.ETA_txtlabel2);


        hTv = ret.findViewById(R.id.hTv);
        hTv.setText(p.getHome());
        setHomeTv = ret.findViewById(R.id.setHomeTv);
        homeRl = ret.findViewById(R.id.homeRl);

        wTv = ret.findViewById(R.id.wTv);
        wTv.setText(p.getWork());
        setWorkTv = ret.findViewById(R.id.setWorkTv);
        workRl = ret.findViewById(R.id.workRl);

        usernameTv = ret.findViewById(R.id.usernameTv);
        openBottomSheet = ret.findViewById(R.id.openBottomSheet);
        showOrderSheet = ret.findViewById(R.id.showOrderSheet);

        dateTimeTxt = ret.findViewById(R.id.dateTimeTxt);

        asaptxt = ret.findViewById(R.id.asaptxt);
        addPromo = ret.findViewById(R.id.promoLyt_);

        promotxt = ret.findViewById(R.id.promotxt);
        shoppingnotice = ret.findViewById(R.id.shoppingnotice);


        estimatedFareIcon = ret.findViewById(R.id.meterInfo);

        mCurrentAddressType = AddressLocationType.Dropoff;
        etacard = ret.findViewById(R.id.eta_card);

        TextView shoppingLabel = ret.findViewById(R.id.shoppingLabel);
        shoppingLabel.setText(p.getShopping());

        TextView extrasLabel = ret.findViewById(R.id.extrasLabel);
        extrasLabel.setText("Notes");

        promotxt.setText(p.getAddPromo());

        setHome();
        setWork();

    }


    private void getLastLocationFromFusedApi() {
        try {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                    Log.d(TAG, "onLocationResult: LOCATION CALLBACK fusedLocationClient.getLastLocation()");

                    afterGettingLocation(location);
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Log.d(TAG, "onConnected: " + e.getMessage());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listener(View ret) {

        if(Gateway.equalsIgnoreCase(Config.Stripe)) {

            listener_stripe_getAllCards = new Listener_Stripe_GetAllCards() {
                @Override
                public void onComplete(String result) {
                    try {
                        if (result != null && !result.startsWith("false") && !result.equals("")) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("hasError")) {
                                } else {
                                    if (jsonObject.getString("message").toLowerCase().contains("success")) {
                                        JSONObject responseCardList = jsonObject.getJSONObject("responseCardList");
                                        JSONArray datas = responseCardList.getJSONArray("data");
                                        ArrayList<StripeCardModel> stripeCardModelArrayList = new ArrayList<>();
                                        for (int i = 0; i < datas.length(); i++) {
                                            JSONObject o = datas.getJSONObject(i);

                                            String id = o.getString("id");

                                            JSONObject cardObj = o.getJSONObject("card");
                                            String brand = cardObj.getString("brand");
                                            String country = cardObj.getString("country");
                                            String exp_month = cardObj.getString("exp_month");
                                            String exp_year = cardObj.getString("exp_year");
                                            String last4 = cardObj.getString("last4");

                                            String customer = o.getString("customer");
                                            String type = o.getString("type");

                                            StripeCardModel s = new StripeCardModel(id, customer, exp_month, exp_year, last4, country, brand, 0, false);
                                            stripeCardModelArrayList.add(s);

                                        }

                                        if (stripeCardModelArrayList.size() == 1) {
                                            try {
                                                mHelper.saveToSharePrefForStripeForOneCard(new Stripe_Model(
                                                        "" + stripeCardModelArrayList.get(0).getId(),
                                                        "" + stripeCardModelArrayList.get(0).getLast4(),
                                                        "Android" + "-" + mHelper.getSettingModel().getUserServerID() + "-" + System.currentTimeMillis(),
                                                        "" + stripeCardModelArrayList.get(0).getCustomer(),
                                                        "" + sp.getString(Config.Stripe_SecretKey, ""),
                                                        "" + Config.Stripe,
                                                        (!sp.getString(CommonVariables.CurrencySymbol, "\u00A3").equals("$")) ? "GBP" + "" : Currency
                                                ));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getContext(), "Payment Failed\nUnable to process payment, Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            try {
                Stripe_Model m = mHelper.getSharePrefForStripeForOneCard();
                if (m.getCardToken().length() == 0) {
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (sp.getString(Config.EnableCardHold, "").equals("1")) {
                    if (Gateway.contains(Config.Stripe) && !sp.getString(Config.Stripe_SecretKey, "").equals("")) {
                        new Manager_Stripe_GetAllCards(getContext(), false, mHelper.getStripeCustomerId(), listener_stripe_getAllCards).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }

        }

        try {
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cardWhereTo1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                whereTo();
            }
        });

        currentLocationCv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    if (locationButton != null)
                        locationButton.callOnClick();
                }
//                mMap.setOnMyLocationButtonClickListener(() -> {
//                    isChooseFromList = false;
//                    firstLoad = false;
//                    isMovingCamera = true;
//                    return false;
//                });

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(CurrLoc.getLatitude(), CurrLoc.getLongitude()), 15));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);
            }
        });


        openBottomSheet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showOrderSheet.setVisibility(VISIBLE);

                    if (confirm_booking.getVisibility() != VISIBLE) {
                        unlockMap();

                    } else {
                        ZeroFareTxt.setVisibility(GONE);


                        double total = calculateTotalFares(0);
                        if (model.getPaymentType().toLowerCase().contains("credit") && !sp.getString(Config.CreditCardSurchargeType, "").equals("")) {
                            try {
                                if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().equals("amount")) {
                                    total += Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0"));
                                    model.setsurchargeAmount(Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")));
                                } else if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().contains("percent")) {
                                    Float surchargedAmount = (Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")) / 100) * Float.parseFloat(shoppingFares);
                                    total += surchargedAmount;
                                    model.setsurchargeAmount(surchargedAmount);
                                }
                            } catch (Exception e) {
                            }
                        }

                        fare_txt.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (total)));
                    }
                    if (!sp.getString(Config.ShoppingNotice, "").equals("")) {
                        shoppingnotice.setVisibility(VISIBLE);
                    }

                    openOrderDetailsSheet();
                } else {

                    if (confirm_booking.getVisibility() != VISIBLE) {
                        if (locAndFieldArrayList.size() == 1) {
                            LocAndField locAndField = locAndFieldArrayList.get(0);
                            if (getLocationMode() != 1) {
                                CameraPosition cameraPosition = new CameraPosition.Builder().
                                        target(getLatLng(locAndField)).
                                        tilt(45).
                                        zoom(15).
                                        bearing(0).
                                        build();
                                CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                if (update != null && mMap != null) {
                                    mMap.animateCamera(update);
                                }
                            }
                            lockMap();
                        }
                    } else {
                        try {
                            finalPrice = Float.parseFloat(model.getOneWayFare());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (finalPrice <= 0 && !sp.getString(Config.ZeroFareText, "").equals("")) {
                            ZeroFareTxt.setText(sp.getString(Config.ZeroFareText, ""));
                            ZeroFareTxt.setVisibility(VISIBLE);
                        } else {
                            if (sp.getString(ShowFares, "1").equals("0")) {

                                ZeroFareTxt.setText(sp.getString(Config.ZeroFareText, ""));
                                ZeroFareTxt.setVisibility(VISIBLE);
                            } else {

                                fare_txt.setVisibility(VISIBLE);
                                ZeroFareTxt.setVisibility(GONE);
                            }
                        }
                        double total = calculateTotalFares(0);
                        if (model.getPaymentType().toLowerCase().contains("credit") && !sp.getString(Config.CreditCardSurchargeType, "").equals("")) {
                            try {
                                if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().equals("amount")) {
                                    total += Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0"));
                                    model.setsurchargeAmount(Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")));
                                } else if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().contains("percent")) {
                                    Float surchargedAmount = (Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")) / 100) * Float.parseFloat(model.getOneWayFare());
                                    total += surchargedAmount;
                                    model.setsurchargeAmount(surchargedAmount);
                                }

                            } catch (Exception e) {
                            }
                        }
                        fare_txt.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (total)));
                    }

                    showOrderSheet.setVisibility(GONE);
                    shoppingnotice.setVisibility(GONE);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }
        });

        homeRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hTv.getText().toString().toLowerCase().equals("home")) {

                    makeRoute(null, mHelper.getLocAndFieldModel(mHelper.getSettingModel().getEmail() + "_" + KEY_HOME));

                } else if (hTv.getText().toString().toLowerCase().trim().equals("add home")) {

                    startActivity(new Intent(getContext(), Activity_SearchAddressForHomeAndWork.class).putExtra("setFrom", "home"));
                }
            }
        });

        workRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wTv.getText().toString().toLowerCase().equals("work")) {
                    makeRoute(null, mHelper.getLocAndFieldModel(mHelper.getSettingModel().getEmail() + "_" + KEY_OFFICE));

                } else if (wTv.getText().toString().toLowerCase().trim().equals("add work")) {
                    startActivity(new Intent(getContext(), Activity_SearchAddressForHomeAndWork.class).putExtra("setFrom", "work"));
                }
            }
        });

        asap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, 0);
                timeBottom.setText(CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_TIME));

                dateBottom.setText(CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_DATE));

                asap.setTextColor(getResources().getColor(R.color.color_white_inverse));
                asap15.setTextColor(getResources().getColor(R.color.color_black_inverse));
                asap30.setTextColor(getResources().getColor(R.color.color_black_inverse));

                asap.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapfilled));
                asap15.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
                asap30.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
            }
        });

        asap15.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, 15);
                timeBottom.setText(CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_TIME));
                dateBottom.setText(CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_DATE));


                asap.setTextColor(getResources().getColor(R.color.color_black_inverse));
                asap15.setTextColor(getResources().getColor(R.color.color_white_inverse));
                asap30.setTextColor(getResources().getColor(R.color.color_black_inverse));

                asap15.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapfilled));
                asap.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
                asap30.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
            }
        });

        asap30.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, 30);
                timeBottom.setText(CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_TIME));
                dateBottom.setText(CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_DATE));

                asap.setTextColor(getResources().getColor(R.color.color_black_inverse));
                asap15.setTextColor(getResources().getColor(R.color.color_black_inverse));
                asap30.setTextColor(getResources().getColor(R.color.color_white_inverse));

                asap30.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapfilled));
                asap15.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
                asap.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
            }
        });

        dateBottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_PickupDateTime timer = new Fragment_PickupDateTime();
                timer.setOnSetResultListener(new Listener_OnSetResult() {
                    @Override
                    public void setResult(Intent intent) {
                        String date = intent.getStringExtra(Fragment_PickupDateTime.KEY_DATE);
                        dateBottom.setText(date);
                        tempDate = date;
                    }
                });
                timer.show(getFragmentManager(), null);
            }
        });

        timeBottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_PickupTime timer = new Fragment_PickupTime();
                Bundle args = new Bundle();
                if (tempDate == null) {
                    tempDate = "";
                }
                args.putString(Fragment_PickupDateTime.KEY_RAW_DATE, tempDate);
                timer.setArguments(args);
                timer.setOnSetResultListener(new Listener_OnSetResult() {

                    @Override
                    public void setResult(Intent intent) {
                        String date = intent.getStringExtra(Fragment_PickupDateTime.KEY_DATE);
                        String time = intent.getStringExtra(Fragment_PickupDateTime.KEY_TIME);
                        timeBottom.setText(time);
                        SharedPrefrenceHelper mHelper = new SharedPrefrenceHelper(mContext);
//                        SettingsModel model = mHelper.getSettingModel();

                        if (userModel.getName() != null && !userModel.getName().isEmpty()) {

                        } else {
                            ((Fragment_Main) mContext).showUserDetailsPopUp();
                        }
                    }
                });
                timer.show(getFragmentManager(), null);
            }
        });

        showOrderSheet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrderDetailsSheet();
            }
        });

        addPromo.setOnClickListener(this);
        estimatedFareIcon.setOnClickListener(this);
        openBottomSheet.setOnClickListener(this);
        DrvNotes.setOnClickListener(this);
        cash_s_lyt.setOnClickListener(this);
        menu_btn.setOnClickListener(this);
        confirm_booking.setOnClickListener(this);

        ret.findViewById(R.id.asaplyt2).setOnClickListener(this);

        mPickupDate = getCurrentDate();
        mPickupTime = getCurrentTime();

        greetingMessage();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (mDialog != null) {
                    mDialog.dismissWithAnimation();
                }

                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        CurrLoc = location;
                        mCurrLoc = location;
                        latdriver = "" + location.getLatitude();
                        longdriver = "" + location.getLongitude();

                        if (handle != null) {
                            Log.d(TAG, "run: run: run: 1" + latdriver + "," + longdriver);
                            handle.removeCallbacks(m_runnable);
                            handle.post(m_runnable);
                        }
                        stopLocationUpdates();

                        if (locAndFieldArrayList.size() == 0) {

                            isChooseFromList = true;
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            latpick = latitude;
                            longpick = longitude;
                            driverlat = String.valueOf(latpick);
                            driverlog = String.valueOf(longpick);
                            if (getLocationMode() != 1) {
                                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                                mMap.animateCamera(update);
                            } else {
                                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(51.509865, -0.118092), 15);
                                mMap.animateCamera(update);
                            }
                        } else {
                            if (getArguments() != null && getArguments().getBoolean("again", false)) {
                            } else {
                                Picklocation();
                            }
                        }

                        break;
                    }
                }
            }
        };

        // Get Driver ETA
        availableDriversManagerListener = result -> {

            result = checkIfHasNullForString(result);

            if (result.length() > 0 && isAdded()) {
                try {
                    JSONObject parentObject = new JSONObject(result);
                    if (parentObject.getBoolean("HasError")) {
                    } else {

                        JSONObject dataObject = parentObject.optJSONObject("Data");
                        if (dataObject != null) {
                            JSONObject object = dataObject.optJSONObject("availableDriverDetails");
                            String locationName = object.optString("LocationName");
                            locsName = locationName;
                            Log.d(TAG, "doInBackground:GetAvailableDriversByVehicleETA " + locationName);

                            mCurrentLocationLocAndField = new LocAndField();
                            mCurrentLocationLocAndField.setField("" + locationName);
                            mCurrentLocationLocAndField.setLat("" + latpick);
                            mCurrentLocationLocAndField.setLon("" + longpick);
                            mCurrentLocationLocAndField.setLocationType("Address");
                            mCurrentLocationLocAndField.setDoorNo("");

                            try {
                                setGeoCoderResponse(mCurrentLocationLocAndField);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            JSONArray listofavailabledrivers = object.optJSONArray("listofavailabledrivers");
                            if (listofavailabledrivers != null) {

                                setAvailableDriver(listofavailabledrivers);

                                for (int i = 0; i < driverslist1.size(); i++) {
                                    MarkerOptions marker = new MarkerOptions().position(new LatLng(driverslist1.get(i).getLatitude(), driverslist1.get(i).getLongitude()));

                                    if (driverslist1.get(i).getNearest().equals("1")) {

                                        if (mContext != null && getResources() != null && mContext.getResources() != null) {
                                            if (!log.equals("")) {

                                                if (log.contains("\r\n")) {
                                                    log = log.replace("\r\n", "");
                                                }
                                            }
                                        }

                                        try {
                                            Bitmap bitmap = getThumbnail(selectedCar.toLowerCase() + ".png");

                                            if (bitmap == null) {
                                                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.avcar));
                                            } else {
                                                marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            Bitmap bitmap = getThumbnail(selectedCar.toLowerCase() + ".png");
                                            if (bitmap == null) {
                                                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.avcar));
                                            } else {
                                                marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if (markers.size() > 0 && vehicleTimer > 0) {
                                        try {
                                            for (int k = 0; k < markers.size(); k++) {
                                                int finalK = k;
                                                if (markers.get(k).getTag() != null) {
                                                    Log.e("Marker List", i + " marker" + markers.get(k).getTag());
                                                }
                                                if (markers.get(k).getTag() != null && markers.get(k).getTag().equals(driverslist1.get(i).getDriveNo())) {
                                                    LatLng latLng = new LatLng(driverslist1.get(i).getLatitude(), driverslist1.get(i).getLongitude());

                                                    if (latLng.latitude != markers.get(k).getPosition().latitude && !movingDrivers.contains(markers.get(k).getTag())) {
                                                        if (!movingDrivers.contains(markers.get(k).getTag().toString())) {

                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    getActivity().runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            try {
                                                                                rotateMarker(markers.get(finalK), (float) bearingBetweenLocations(markers.get(finalK).getPosition(), latLng));
                                                                                animateMarkerNew(latLng, markers.get(finalK));
                                                                            } catch (Exception e) {
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }).start();
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                createMarker(mCurrentLocationLocAndField, locationName, log);

                                if (currentMarkers.size() > 0 && vehicleTimer > 0) {
                                    for (int i = 0; i < markers.size(); i++) {
                                        Marker marker = markers.get(i);

                                        if (marker.getTag() == null || !currentMarkers.contains(marker.getTag())) {
                                            marker.remove();
                                            markers.remove(i);
                                            driverslist2.remove(i);
                                        }
                                    }
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void afterGettingLocation(Location location) {
        if (location != null) {
            try {
//                location.setLatitude(mMap.getCameraPosition().target.latitude);
//                location.setLongitude(mMap.getCameraPosition().target.longitude);
                CurrLoc = location;
                mCurrLoc = location;
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                latpick = latitude;
                longpick = longitude;

                stopLocationUpdates();

                if (locAndFieldArrayList != null) {

                    isChooseFromList = true;

                    latpick = location.getLatitude();
                    longpick = location.getLongitude();

                    driverlat = String.valueOf(latpick);
                    driverlog = String.valueOf(longpick);
                    latdriver = driverlat;
                    longdriver = driverlog;
                    pickup = driverlat + "," + driverlog;

                    if (handle != null) {//replace driveLoc
                        Log.d(TAG, "run: run: run: 3 " + latdriver + "," + longdriver);

                        handle.removeCallbacks(m_runnable);
                        handle.post(m_runnable);
                    }

                    if (getLocationMode() != 1) {
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                        mMap.animateCamera(update);
                    } else {
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(51.509865, -0.118092), 15);
                        mMap.animateCamera(update);
                    }
                } else {
                    Picklocation();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            startLocationUpdates();
        }
    }

    private void setGeoCoderResponse(LocAndField locAndField) {
        if (isAddressSelected) {
            isAddressSelected = false;
            return;
        }
        if (locAndField != null && isAdded()) {
            String address = locAndField.getField();//ViaListViewAdapter.getAddressAsString(add);
            pickup = address;

            if (address == null || address.equals("null") || address.equals("")) {
            } else {
                driverlat = String.valueOf(latpick);
                driverlog = String.valueOf(longpick);
                latdriver = String.valueOf(latpick);
                longdriver = String.valueOf(longpick);

                if (locAndFieldArrayList.size() == 1) {
                    locAndFieldArrayList.set(0, mCurrentLocationLocAndField);
                }
                mCurrentAddressType = AddressLocationType.Pickup;


            }
            return;
        } else {
            if (isAdded()) {
                mCurrentAddressType = AddressLocationType.Pickup;
            }
        }
    }

    private void setAvailableDriver(JSONArray array) {
        try {
            if (vehicleTimer <= 0) {
                for (int i = 0; i < markers.size(); i++) {
                    markers.get(i).remove();
                }
                markers.clear();
                driverslist2.clear();
            }
            //mMap.clear();
//            float[] tempDistance = new float[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Driver drive = new Driver();
                drive.setDriveNo(obj.getString("DriverNo"));
                currentMarkers.add(obj.getString("DriverNo"));
                drive.setLatitude(obj.getDouble("Latitude"));
                drive.setLongitude(obj.getDouble("Longitude"));
                if (!driverslist2.contains(obj.getString("DriverNo"))) {
                    driverslist2.add(obj.getString("DriverNo"));
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(drive.getLatitude(), drive.getLongitude()));
                    try {
                        Bitmap bitmap = getThumbnail(selectedCar.toLowerCase() + ".png");
                        if (bitmap == null) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.avcar));
                        } else {
                            marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Marker carMarker = mMap.addMarker(marker);
                    try {
                        carMarker.setRotation(getBearing(getLatLng(mCurrentLocationLocAndField), carMarker.getPosition()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    carMarker.setTag(drive.getDriveNo());
                    markers.add(carMarker);
                }
                if (i == 0) {
                    drive.setNearest("1");
                    log = obj.getString("ETA");

                }
                driverslist1.add(drive);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("TTTTESTETTTEST", "setAvailableDriver: log : EAT  " + log);
        try {
            if (array.length() == 0) {
                log = "";
            }
            String et = log;
            et = et.replace("\r\n", "");
            if (locAndFieldArrayList.size() > 0) {

                //do
                setMapZoomable(false);
                CommonMethods.getInstance()._showCurvedPolyline(getContext(), isRebook, et, locAndFieldArrayList, mMap, mapOverlayView, isrouteComplete);
            } else {
                setMapZoomable(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setMapZoomable(true);

        }
    }

    public void setMapZoomable(boolean shoudZoom) {
        try {
            if (shoudZoom) {
                mMap.setMaxZoomPreference(mMap.getMaxZoomLevel());
            } else {
                mMap.setMaxZoomPreference(mMap.getMaxZoomLevel() - 3.0f);
            }

//        mMap.getUiSettings().setZoomGesturesEnabled(shoudZoom);
        } catch (Exception ex) {
            mMap.setMaxZoomPreference(mMap.getMaxZoomLevel());
        }
    }

    private void setUpBottomSheetDateTime() {
        builder = new BottomSheetDialog(mContext, R.style.DialogStyle);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheetview, null);

        Objects.requireNonNull(builder.getWindow()).setSoftInputMode(SOFT_INPUT_STATE_VISIBLE);
        builder.setContentView(sheetView);
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
            }
        });
        // builder.show();
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(sheetView);
//        bottomSheetBehavior.setPeekHeight(340);
        dateBottom = sheetView.findViewById(R.id.item1);
        asap = sheetView.findViewById(R.id.asap);
        asap.setText(p.getAsap());

        asap15 = sheetView.findViewById(R.id.asap15);
        asap30 = sheetView.findViewById(R.id.asap30);
        timeBottom = sheetView.findViewById(R.id.item2);
        confirmbottom = sheetView.findViewById(R.id.item3);

        confirmbottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(dateBottom.getText().toString(), timeBottom.getText().toString());
            }
        });
    }

    /*private void setUpBottomSheetDateTime(ViewGroup container) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_sheetview, container, false);
        dateBottom = view.findViewById(R.id.item1);
        asap = view.findViewById(R.id.asap);
        asap.setText(p.getAsap());

        asap15 = view.findViewById(R.id.asap15);
        asap30 = view.findViewById(R.id.asap30);
        timeBottom = view.findViewById(R.id.item2);
        confirmbottom = view.findViewById(R.id.item3);

        confirmbottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(dateBottom.getText().toString(), timeBottom.getText().toString());
            }
        });

        builder = new BottomSheet.Builder(mContext);
        builder.setView(view)
                .setContentType(BottomSheet.LIST)
                .setFullWidth(true)
                .setTitleMultiline(false)
                .setBackgroundColor(mContext.getResources().getColor(R.color.color_gray_and_white_inverse))
                .setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                })
                .setCallback(new BottomSheetCallback() {
                    @Override
                    public void onShown() {
                    }

                    @Override
                    public void onDismissed() {
                    }
                });


    }*/

    private void changeDate(String dateBottom, String timeBottom) {
        try {
            boolean unchanged = false;
            unchanged = model.getPickUpDate() != null && model.getPickUpTime() != null && model.getPickUpDate().equals(dateBottom) && model.getPickUpTime().equals(timeBottom);

            if (isDateAfter(dateBottom + " " + timeBottom)) {
                mPickupDate = dateBottom;
                mPickupTime = timeBottom;
                HomeFragment.this.model.setPickUpTime(mPickupTime);
                HomeFragment.this.model.setPickUpDate(mPickupDate);
                dateTimeTxt.setText("Pickup Time : " + model.getPickUpDate() + " " + model.getPickUpTime());

                String text = isDateAfterText(dateBottom + " " + timeBottom);

                if (text.contains("15")) {
                    asap.setTextColor(getResources().getColor(R.color.color_black_inverse));
                    asap15.setTextColor(getResources().getColor(R.color.app_bg_white));
                    asap30.setTextColor(getResources().getColor(R.color.color_black_inverse));

                    asap.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
                    asap15.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapfilled));
                    asap30.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));

                    etacard.setVisibility(GONE);

                    asaptxt.setText("In 15 Mins");

                } else if (text.contains("30")) {

                    asap.setTextColor(getResources().getColor(R.color.color_black_inverse));
                    asap15.setTextColor(getResources().getColor(R.color.color_black_inverse));
                    asap30.setTextColor(getResources().getColor(R.color.app_bg_white));

                    asap.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
                    asap15.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
                    asap30.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapfilled));

                    etacard.setVisibility(GONE);

                    asaptxt.setText("In 30 Mins");
                } else if (text.toLowerCase().contains("asap")) {
                    asap.setTextColor(getResources().getColor(R.color.app_bg_white));
                    asap15.setTextColor(getResources().getColor(R.color.color_black_inverse));
                    asap30.setTextColor(getResources().getColor(R.color.color_black_inverse));

                    asap.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapfilled));
                    asap15.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
                    asap30.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));

                    etacard.setVisibility(GONE);

                    asaptxt.setText("Asap");
                } else {
                    asaptxt.setText("Custom");

                    asap30.setTextColor(getResources().getColor(R.color.color_black_inverse));
                    asap15.setTextColor(getResources().getColor(R.color.color_black_inverse));
                    asap.setTextColor(getResources().getColor(R.color.color_black_inverse));
                    asap30.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
                    asap15.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
                    asap.setBackground(getResources().getDrawable(R.drawable.rounded_corners_asapoutline));
                    etacard.setVisibility(VISIBLE);
                    isFromRebook = false;
                }

                String test = asaptxt.getText().toString();
                Log.d("TAG", "onClick: " + test);
                if (test.equalsIgnoreCase("ASAP")) {
                    makeArcAgain = false;
                    confirm_booking.setText("CONFIRM BOOKING");
                } else {
                    makeArcAgain = true;
                    confirm_booking.setText("SCHEDULE BOOKING");
                }
                if (!unchanged) {
                    if (locAndFieldArrayList.size() > 1) {
                        String via = "";
//                        ViaAddresses[] listofVia = new ViaAddresses[mViaListViewAdapter.getCount()];

//                        for (int i = 0; i < mViaListViewAdapter.getCount(); i++) {
//                            listofVia[i] = new ViaAddresses();
//                            listofVia[i].Viaaddress = mViaListViewAdapter.getItem(i).toString();
//                            listofVia[i].Viatype = mViaListViewAdapter.getItem(i).type;
//                        }
                        if (userModel.getName() != null && !userModel.getName().isEmpty()) {
                            callGetFares(true);
                        } else {
                            ((Fragment_Main) mContext).showUserDetailsPopUp();
                        }
                    }
                }
                customDate = "";
                customTime = "";
                builder.dismiss();
            } else {
                FBToast.warningToast(mContext, "Kindly select the correct date and time", FBToast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        countForHits = 0;
        try {
            userModel = mHelper.getSettingModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        onBackStackChanged();
        isActivityInBackground = false;
        if (isPendingDirectionDialogue) {
            isPendingDirectionDialogue = false;
            showDirectionDialogue(null);
        }
        getFragmentManager().addOnBackStackChangedListener(this);
        try {
            ((Fragment_Main) mContext).setOnBackPressListen(HomeFragment.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isrouteComplete) {
            if (locationButton != null)
                locationButton.callOnClick();
            try {
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude), 15);
                mMap.animateCamera(update);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setHome();
        setWork();
    }

    @Override
    public void onPause() {
        super.onPause();
        isActivityInBackground = true;
        stopLocationUpdates();
        getFragmentManager().removeOnBackStackChangedListener(this);
    }

    private boolean checkForLatLngIsZero() {
        boolean isZero = false;
        for (LocAndField locAndField : locAndFieldArrayList) {
            if (locAndField.getLat().equals("0") && locAndField.getLon().equals("0")) {
                isZero = true;
                break;
            }
        }
        return isZero;
    }

    @Override
    public void onCameraChange(CameraPosition arg0) {

    }

    @Override
    public boolean _onBackPressed() {
        ((Fragment_Main) getActivity()).exitDialogue();
        return false;
    }

    @Override
    public void setResultList(ArrayList<LocAndField> list) {
        locAndFieldArrayList = list;
        onAddressChangeListener(true);
    }

    @Override
    public void setResult(Intent data) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handle != null) {
            handle.removeCallbacks(m_runnable);
        }
        if (mContext != null) {

            if (mPickupDate != null && !mPickupDate.isEmpty())
                mContext.getIntent().putExtra(KEY_TIME, mPickupDate + "/" + mPickupTime);
            else
                mContext.getIntent().putExtra(KEY_TIME, "");
        }
    }

    @Override
    public void onBackStackChanged() {
        if (getView() != null) {
            InputMethodManager manager = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(getView().getWindowToken(), 0, null);
        }
    }

    String pr_code = "";
    boolean isRemoveDiscountIsTrue = true;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.promoLyt_) {
            if (promotxt.getText().toString().toLowerCase().contains("add")) {
                isRemoveDiscountIsTrue = false;
                LocAndField fromLocAndField = null;
                LocAndField toLocAndField = null;
                try {
                    fromLocAndField = locAndFieldArrayList.get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    toLocAndField = locAndFieldArrayList.get(locAndFieldArrayList.size() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!fromLocAndField.equals(null) || !toLocAndField.equals(null)) {
                    Intent intent = new Intent(getContext(), Activity_AddPromoCode.class);
                    intent.putExtra(KEY_BOOKING_MODEL, model);
                    intent.putParcelableArrayListExtra("key_locAndFieldArrayList", locAndFieldArrayList);
                    startActivityForResult(intent, Config.ADD_PROMO_REQUEST);

                } else {
                    return;
                }

            } else {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(p.getAreYouSure())
                        .setContentText(p.getYouWantToRemovePromoCodeFromThisJourney())
                        .setCancelText(p.getNo())
                        .setConfirmText(p.getYes())
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                isRemoveDiscountIsTrue = true;
                                VerifiedCode = "";
                                model.setPromoDiscountedValue(0);
                                promotxt.setText("Add Promo");
                                promo_image.setImageResource(R.drawable.promo);

                                callForChange();
//                                removeDiscount();
                                if (isSignupPromoApplied) {
                                    isSignupPromoApplied = false;
                                }
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

        } else if (v.getId() == R.id.cash_s_lyt) {
            gotoHomePayment4080(4080);
        } else if (v.getId() == R.id.DrvNotes) {
            if (!selectedCar.equalsIgnoreCase("mpv 8")) {
                showDrvNotes();
                return;
            }

            if (extrasList != null && extrasList.size() > 0) {
                double total = 0;
                try {
                    total = mFareModel.get(selectedPos).getFinalFares();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (openBottomSheet.isChecked()) {
                    total = Float.parseFloat(shoppingFares);
                }

                if (model.getPaymentType().toLowerCase().contains("credit") && !sp.getString(Config.CreditCardSurchargeType, "").equals("")) {
                    try {
                        if (sp.getString(Config.CreditCardSurchargeType, "").equals("amount")) {
                            total += Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0"));
                            model.setsurchargeAmount(Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")));
                        } else if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().contains("percent")) {
                            Float surchargedAmount = (Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")) / 100) * Float.parseFloat(shoppingFares);
                            total += surchargedAmount;
                            model.setsurchargeAmount(surchargedAmount);
                        }
                    } catch (Exception e) {
                    }
                }
//meet and greet
                if (sp.getString(CommonVariables.EnableMeetAndGreet, "0").equals("1")) {
                    startActivityForResult(new Intent(getActivity(), Activity_AddExtras.class).putExtra("journeyCharges", total).putExtra("drvNotes", model.getSpecialNotes()).putExtra("isMeetGreet", isMeetnGreet.equals("1")), 989);

                } else {

                    startActivityForResult(new Intent(getActivity(), Activity_AddExtras.class).putExtra("journeyCharges", total).putExtra("drvNotes", model.getSpecialNotes()), 989);
                }
            } else {
                new GetExtras(true).execute();
            }
        } else if (v.getId() == R.id.menu_btn) {
            mapOverlayView.removeRoutes();
            _CardDetails = "";
            fromPickLocation = false;
            isClickedRebooked = false;
            customDate = "";
            customTime = "";
            _fightNo = "";
            _comingFrom = "";
            flightDateForSchedule = "Today";
            if (locAndFieldArrayList != null && locAndFieldArrayList.size() >= 2) {
//                locAndFieldArrayList.clear();
//                if (model != null) {
//                    model.setExtraTotal(0);
//                }
//                if (extrasList != null) {
//                    extrasList.clear();
//                    mHelper.removeExtrasList();
//                }
//                ((Fragment_Main) getActivity()).onItemClick(null, null, 99, 0);

                //back button new work
                if (true) {
                    clickFrom = "drop";
                    goToDropOff();
                    return;
                }
                resetToHome();
            } else {
                ((Fragment_Main) mContext).toggleDrawer();
            }
        }
        if (v.getId() == R.id.meterInfo) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("")
                    .setContentText(estimatedFareText)
                    .setConfirmText(p.getOk())
                    .showCancelButton(false)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                        }
                    }).show();

        } else if (v.getId() == R.id.confirm_booking && confirmbtn == true) {
            confirmbtn = false;
            confirmBooking();
        } else if (v.getId() == R.id.asaplyt2) {
            if (model != null && (model.getPickUpDate() == null || model.getPickUpDate().equals("") || !isDateAfter(model.getPickUpDate() + " " + model.getPickUpTime()))) {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.MINUTE, 0);
                String formattedDate = CommonVariables.getFormattedDate(date, CommonVariables.FORMAT_DATE_TIME);
                if (formattedDate != null && formattedDate.contains(" ")) {
                    String[] splittedDate = formattedDate.split(" ");
                    if (splittedDate != null && splittedDate.length >= 2) {
                        String dates = splittedDate[0];
                        String times = splittedDate[1];
                        model.setPickUpDate(dates);
                        model.setPickUpTime(times);
                        mPickupDate = dates;
                        mPickupTime = times;
                    }
                }
            } else {
                mPickupDate = model.getPickUpDate();
                mPickupTime = model.getPickUpTime();
//                asap.setTextColor(ContextCompat.getColor(getContext(), R.color.app_bg_white));
//                asap15.setTextColor(ContextCompat.getColor(getContext(), R.color.app_bg_white));
//                asap30.setTextColor(ContextCompat.getColor(getContext(), R.color.app_bg_white));
            }
            dateBottom.setText(mPickupDate);
            timeBottom.setText(mPickupTime);
            builder.show();
        }
    }

    //back button new work
    private void resetToHome() {
        locAndFieldArrayList.clear();
        if (model != null) {
            model.setExtraTotal(0);
        }
        if (extrasList != null) {
            extrasList.clear();
            mHelper.removeExtrasList();
        }
        //do here
        ((Fragment_Main) getActivity()).onItemClick(null, null, 99, 0);
    }

    String pm = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            confirmbtn = true;
            if (requestCode == JUDO_REGISTER_REQUEST) {
                handleRegisterCardResult(resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == REQUEST_CODE_WorldPay) {
                if (resultCode == Activity.RESULT_OK) {
                    String format = CommonVariables.getDateFormat(CommonVariables.FORMAT_DATE_TIME);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
                    long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

                    Calendar date = Calendar.getInstance();
                    long t = date.getTimeInMillis();
                    Date afterAddingTenMins = new Date(t + (0 * ONE_MINUTE_IN_MILLIS));
                    String formattedDate = dateFormat.format(afterAddingTenMins);

                    try {
                        if (Gateway.contains(Config.Stripe)) {
                            if (!model.getTransactionId().equals("")) {
                                if (sp.getString(Config.EnableCardHold, "").equals("1")) {
                                    model.setPaymentType("Credit Card");
                                } else {
                                    model.setPaymentType("Credit Card(PAID)");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    model.setPaymentType("Credit Card(PAID)");
                    model.setTransactionId(data.getStringExtra("transactionID"));
                    model.setCreateTime(formattedDate);

                    Model_BookingDetailsModel[] params = {model};
                    new SaveBooking().execute(params);
                } else {
                    model.setPaymentType("credit card");
                    FBToast.warningToast(getActivity(), "User cancelled payment.", Toast.LENGTH_LONG);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == Account_Success && resultCode == RESULT_OK) {
                model.setPaymentType("Account");
                cashtxt.setText("Account");
                cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_credit_card));
                paymentSelection = accountLoggedIn;
                callGetFares(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == 989) {

                extrasList = mHelper.getExtraList();
                if (resultCode == RESULT_OK) {
                    String Fares = openBottomSheet.isChecked() ? shoppingFares : model.getOneWayFare();
                    model.setSpecialNotes(data.getStringExtra("drvNotes"));
                    //                model.setExtraTotal(data.getDoubleExtra("extraTotal", 0f));
                    double total = data.getDoubleExtra("extraTotal", 0f);
                    //double total = calculateTotalFares(model.getExtraTotal());

                    //                for (AnyVehicleFareModel a : mFareModel) {
                    //                    double f = a.getFinalFares() + model.getExtraTotal();
                    //                    a.setFinalFares(f);
                    //                    Log.d(TAG, "onActivityResult: Final Fare : " + a.getFinalFares());
                    //                }
                    //                vehicleAdapter.notifyDataSetChanged();
                    if (model.getPaymentType().toLowerCase().contains("credit") && !sp.getString(Config.CreditCardSurchargeType, "").equals("")) {
                        try {
                            if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().equals("amount")) {
                                total += Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0"));
                                model.setsurchargeAmount(Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")));
                            } else if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().contains("percent")) {
                                Float surchargedAmount = (Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")) / 100) * Float.parseFloat(Fares);
                                total += surchargedAmount;
                                model.setsurchargeAmount(surchargedAmount);
                            }
                        } catch (Exception e) {
                        }
                    }

                    if (model.getExtraTotal() > total) {
                        for (AnyVehicleFareModel a : mFareModel) {
                            double f = a.getFinalFares() - model.getExtraTotal();
                            f += total;
                            a.setFinalFares(f);
                        }
                        model.setExtraTotal(total);
                    } else {
                        model.setExtraTotal(total);
                        for (AnyVehicleFareModel a : mFareModel) {
                            double totalFaresFromGetQuote = 0;
                            double f = 0;
                            try {
                                double oneWayFare = 0;
                                if (!sp.getBoolean(CommonVariables.ISMEMBERUSERLOGIN, false) && model.getPaymentType().toLowerCase().contains("credit") && model.getCompanyPrice() > finalPrice) {
                                    oneWayFare = model.getCompanyPrice();
                                    totalFaresFromGetQuote = model.getExtraCharges() + model.getAgentFees() + model.getAgentCharge() + model.getCongestion() + model.getParking() + model.getWaiting() + model.getAgentCommission() + oneWayFare + model.getExtraTotal() + model.getBookingFee();
                                    f = totalFaresFromGetQuote;
                                }

                                if (model.getPaymentType().equalsIgnoreCase("Account")) {
                                    oneWayFare = model.getCompanyPrice();
                                } else {
                                    try {
                                        oneWayFare = Double.parseDouble(a.getSingleFare());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        f = 0;
                                    }
                                }
                                totalFaresFromGetQuote = model.getExtraCharges() + model.getAgentFees() + model.getAgentCharge() + model.getCongestion() + model.getParking() + model.getWaiting() + model.getAgentCommission() + oneWayFare + model.getExtraTotal() + model.getBookingFee();
                                f = totalFaresFromGetQuote;
                            } catch (Exception e) {
                                e.printStackTrace();
                                f = 0;
                            }

                            a.setFinalFares(f);
                        }
                    }
                    extraTotalBabySeat = model.getExtraTotal();
                    vehicleAdapter.notifyDataSetChanged();
                    fare_txt.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", total));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == Config.ADD_PROMO_REQUEST) {
                if (resultCode == RESULT_OK) {
                    if (data != null && data.hasExtra("pr_code")) {

//                        here Promocode successfully add then enable to FareTextView
//                        isRemoveDiscountIsTrue = false;

                        //applyDiscount(data.getStringExtra("pr_code"));
                        pr_code = data.getStringExtra("pr_code");
                        promoCodeMiniMumFare = data.getStringExtra("pr_code_minimum_fare");
                        PromotionStartDateTime = data.getStringExtra("PromotionStartDateTime");
                        PromotionEndDateTime = data.getStringExtra("PromotionEndDateTime");

                        callForChange();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == Config.ADD_DRV_NOTES) {
                if (resultCode == RESULT_OK) {
                    if (data != null && data.hasExtra("drvnotes")) {
                        model.setSpecialNotes(data.getStringExtra("drvnotes"));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            if (requestCode == 4311 && resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }

                selectedPaymentType = sp.getString(CommonVariables.paymentType, "Cash").toLowerCase();
                model.setPaymentType(selectedPaymentType);

                if (model.getPaymentType().toLowerCase().startsWith("credit")) {

                    if (IsKonnectPay) {

                        // konnect card already saved in sp on activity home payment
                        // Hold Payment Work
                        double fares = mFareModel.get(vehicleSelectedIndex).getFinalFares();
                        KonnectCardModel model = mHelper.getKonnectOneCard();

                        model.setTotalFares(fares);
                        Gson gson = new Gson();
                        String jss = gson.toJson(model);

                        stripCardToken = model.getCardToken();
                        stripCustomerId = model.getStripeCustomerId();
                        _CardDetails = jss;

                    }
                    else if (Gateway.equalsIgnoreCase(Config.Stripe)) {
                        if (sp.getString(Config.EnableCardHold, "0").equals("1")) {
                            // Hold Payment Work
                            double fares = mFareModel.get(vehicleSelectedIndex).getFinalFares();

                            mHelper.saveToSharePrefForStripeForOneCard(
                                    new Stripe_Model(
                                            "" + data.getStringExtra("pm"),
                                            "" + data.getStringExtra("last4"),
                                            "Android" + "-" + mHelper.getSettingModel().getUserServerID() + "-" + System.currentTimeMillis(),
                                            "" + data.getStringExtra("customerId"),
                                            "" + sp.getString(Config.Stripe_SecretKey, ""),
                                            "" + Config.Stripe,
                                            (!sp.getString(CommonVariables.CurrencySymbol, "\u00A3").equals("$")) ? "GBP" + "" : Currency,
                                            fares,
                                            ""
                                    ));

                            Stripe_Model model = mHelper.getSharePrefForStripeForOneCard();
                            model.setTotalFares(fares);


                            Gson gson = new Gson();
                            String jss = gson.toJson(model);
                            _CardDetails = jss;
                            stripCardToken = model.getCardToken();
                            stripCustomerId = model.getStripeCustomerId();

//                            _CardDetails = "";
//                            CardToken = model.getCardToken();

                        }
                        else {
                            pm = data.getStringExtra("pm");
                        }
                    }
                    else if (Gateway.equalsIgnoreCase(Config.JUDOPAY)) {
                        try {
//                            new SaveBooking(mHelper.getCardJudoModel()).execute(model);
                        } catch (Exception e) {
                            model.setTransactionId("");
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == 1101 && resultCode != RESULT_OK) {
                Toast.makeText(getContext(), "User Cancelled Payment!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == 1101 && resultCode == RESULT_OK) {
                if (data != null & data.hasExtra("TransId")) {
                    model.setTransactionId(data.getStringExtra("TransId"));
                    new SaveBooking().execute(model);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == REQUEST_CHECK_SETTINGS && resultCode != RESULT_OK) {
                Toast.makeText(getContext(), "Location needs to enable to perform some action in this application", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK) {
                mDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                mDialog.setTitleText("Please wait .. ");
                mDialog.setContentText("You will be taking to your current location");
                mDialog.setCancelable(false);
                mDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isAdded() && !getActivity().isFinishing()) {
                            startLocationUpdates();
                        }
                    }
                }, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == 4080 && resultCode == RESULT_OK) {
                selectedPaymentType = sp.getString(CommonVariables.paymentType, "Cash").toLowerCase();
                model.setPaymentType(selectedPaymentType);


                if (selectedPaymentType.toLowerCase().startsWith("credit")) {

                    if (IsKonnectPay) {

                        // konnect card already saved in sp on activity home payment
                        // Hold Payment Work
                        double fares = mFareModel.get(vehicleSelectedIndex).getFinalFares();
                        KonnectCardModel model = mHelper.getKonnectOneCard();

                        model.setTotalFares(fares);
                        Gson gson = new Gson();
                        String jss = gson.toJson(model);
                        stripCardToken = model.getCardToken();
                        stripCustomerId = model.getStripeCustomerId();
                        _CardDetails = jss;

                    }

                 else   if (Gateway.equalsIgnoreCase(Config.Stripe)) {

                        if (sp.getString(Config.EnableCardHold, "0").equals("1")) {
                            // Hold Payment Work
                            double fares = mFareModel.get(vehicleSelectedIndex).getFinalFares();

                            mHelper.saveToSharePrefForStripeForOneCard(
                                    new Stripe_Model(
                                            "" + data.getStringExtra("pm"),
                                            "" + data.getStringExtra("last4"),
                                            "Android" + "-" + mHelper.getSettingModel().getUserServerID() + "-" + System.currentTimeMillis(),
                                            "" + data.getStringExtra("customerId"),
                                            "" + sp.getString(Config.Stripe_SecretKey, ""),
                                            "" + Config.Stripe,
                                            (!sp.getString(CommonVariables.CurrencySymbol, "\u00A3").equals("$")) ? "GBP" + "" : Currency,
                                            fares,
                                            ""
                                    ));

                            Stripe_Model model = mHelper.getSharePrefForStripeForOneCard();
                            model.setTotalFares(fares);

                            stripCardToken = model.getCardToken();
                            stripCustomerId = model.getStripeCustomerId();

                            Gson gson = new Gson();
                            String jss = gson.toJson(model);
                            _CardDetails = jss;
                        }
                        else {
                            pm = data.getStringExtra("pm");
                        }
                    }

                    try {
                        if (Gateway.equalsIgnoreCase(Config.JUDOPAY)) {
                            try {

                                new SaveBooking(mHelper.getCardJudoModel()).execute(model);
                                return;
                            } catch (Exception e) {
                                model.setTransactionId("");
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                callForChange();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (requestCode == 5050 && resultCode == RESULT_OK) {
                try {
                    if (data.hasExtra("isReset") && data.getBooleanExtra("isReset", false)) {
                        if (locAndFieldArrayList != null && locAndFieldArrayList.size() > 1) {
                            resetToHome();
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                makeRoute(data, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == REQUEST_LOCATION) {
                if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onMapReady(mMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap arg0) {
        try {
            mMap = arg0;

            setDarkAndNightMapStyleColor();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    mMap.setMyLocationEnabled(true);
                    currentLocationCv.setVisibility(VISIBLE);
                }

            } else {
                mMap.setMyLocationEnabled(true);
                currentLocationCv.setVisibility(VISIBLE);
            }

            if (Build.VERSION.SDK_INT >= 23) {

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Location Permission Required")
                            .setMessage("Location access is required to auto detect your location otherwise you have to select your location manually.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    requestPermissions(new String[]{
                                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                                            REQUEST_LOCATION);
                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show();
                }
            }

            mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());


            mMap.setOnMarkerClickListener(marker -> {
                try {
                    Log.d(TAG, "onMapReady: " + marker.getTag().toString());

                    if (marker.getTag() != null && marker.getTag().toString().equalsIgnoreCase("pickup")) {
                        clickFrom = "pickup";
                    } else if (marker.getTag() != null && marker.getTag().toString().equalsIgnoreCase("drop")) {
                        clickFrom = "drop";
                    } else {
                        if (locAndFieldArrayList.size() == 3 && (marker.getTag().toString().toLowerCase().trim().equals(locAndFieldArrayList.get(1).getField().toLowerCase().trim()))) {
                            clickFrom = "via 1";
                        } else if (locAndFieldArrayList.size() == 4 && (marker.getTag().toString().toLowerCase().trim().equals(locAndFieldArrayList.get(1).getField().toLowerCase().trim()))) {
                            clickFrom = "via 1";
                        } else if (locAndFieldArrayList.size() == 4 && (marker.getTag().toString().toLowerCase().trim().equals(locAndFieldArrayList.get(2).getField().toLowerCase().trim()))) {
                            clickFrom = "via 2";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                goToDropOff();
                return marker.getTitle() == null || marker.getSnippet().equals("");
            });

//            mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                @Override
//                public void onCameraMoveStarted(int i) {
//                    if (i == REASON_GESTURE) {
//                        isChooseFromList = false;
//                        isMovingCamera = true;
//                        isAddressSelected = false;
//                        firstLoad = false;
//                    }
//                }
//            });

//            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//                @Override
//                public void onCameraIdle() {
//                    if (isFirstTime) {
//                        return;
//                    }
//                    if (!firstLoad && isMovingCamera) {
//                        isMovingCamera = false;
//                        mCameraHandler.removeCallbacks(mCameraMoveCallback);
//                        mCameraHandler.postDelayed(mCameraMoveCallback, 0);
//                    }
//                }
//            });

            View mapView = mapFragment.getView();
            if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
                locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                locationButton.setVisibility(GONE);

            }

            if (getArguments() != null && getArguments().getBoolean("again", false)) {
                againWork();
            } else if (getArguments() != null && getArguments().getParcelable("pickDetail") != null) {
                isChooseFromList = true;
            }


            mMap.setOnCameraChangeListener(this);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                String provider = manager.getBestProvider(new Criteria(), true);

                if (provider != null) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Location loc = manager.getLastKnownLocation(provider);
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 15);
                        mMap.animateCamera(update);
                    }
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        //onAddressChangeListener(true);
        mapOverlayView.addGoogleMapProvider(this);
        mMap.getUiSettings().setCompassEnabled(false);
        checkLocationIsEnabled();

        mMap.setOnCameraMoveListener(() -> {
                    try {
//                        mapOverlayView.onCameraMove(mMap.getProjection(), mMap.getCameraPosition());
                        mapOverlayView.onCameraMove();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        getLastLocationFromFusedApi();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStop() {
        super.onStop();
        try {

            if (mGoogleClient != null && mGoogleClient.isConnected())
                mGoogleClient.disconnect();
        } catch (Exception e) {
        }
        try {
            ((Fragment_Main) mContext).unRegisterBackListener();
        } catch (Exception e) {

        }
        try {
            if (sweetAlertDialog != null) {
                sweetAlertDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    private void confirmBooking() {
        try {
            if (!promoCodeMiniMumFare.equals("")) {
                String f = fare_txt.getText().toString().replace(sp.getString(CurrencySymbol, "\u00A3"), "").trim();
                if (Double.parseDouble(promoCodeMiniMumFare) > Double.parseDouble(f)) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("")
                            .setContentText("This Promotion would apply if minimum fare is " + promoCodeMiniMumFare)
                            .setConfirmText(p.getOk())
                            .showCancelButton(false)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }

                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                }
                            }).show();
                    confirmbtn = true;
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!PromotionEndDateTime.equals("")) {
                String currentDate = mPickupDate;
                String finalDate = PromotionEndDateTime;

                SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                SimpleDateFormat format2 = new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.getDefault());

                Date date1 = format1.parse(currentDate);
                Date date2 = format2.parse(finalDate);

                if (date1.getTime() > date2.getTime()) {
                    Log.d(TAG, "onCreateView: cannot apply promocode");

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("")
                            .setContentText("This is promo is applicable only\n" + PromotionStartDateTime + " till " + PromotionEndDateTime)
                            .setConfirmText(p.getOk())
                            .showCancelButton(false)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }

                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                }
                            }).show();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!restrictionVehicle(CurrSelectedCarId)) return;

        String tempNotes = "";
        if (shoppingVehicleLayout.getVisibility() == VISIBLE) {
            if (model.getSetOrderDetails().isEmpty() || model.getorderName().isEmpty()) {
                FBToast.warningToast(mContext, "Please enter order details first!", FBToast.LENGTH_SHORT);
                openOrderDetailsSheet();
                return;
            } else {
                tempNotes = "Order Number - " + model.getSetOrderDetails() + "\r\n Name on Order - " + model.getorderName() + "\r\n";
                model.setorderSummary(tempNotes);
                SaveBooking();
            }
        } else {
            SaveBooking();
        }
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
    }

    private void setHome() {
        homeAddress = mHelper.getLocAndFieldModel(mHelper.getSettingModel().getEmail() + "_" + KEY_HOME);

        if (homeAddress != null && !homeAddress.equals("null") && !homeAddress.getField().equals("")) {
            hTv.setText(p.getHome());
            setHomeTv.setVisibility(VISIBLE);
            setHomeTv.setText(homeAddress.getField());
        } else {
            hTv.setText(p.getAddHome());
            setHomeTv.setVisibility(GONE);
        }
    }

    private void setWork() {
        workAddress = mHelper.getLocAndFieldModel(mHelper.getSettingModel().getEmail() + "_" + KEY_OFFICE);

        if (workAddress != null && !workAddress.equals("null") && !workAddress.getField().equals("")) {
//            wTv.setText("WORK");
            wTv.setText(p.getWork());
            setWorkTv.setVisibility(VISIBLE);
            setWorkTv.setText(workAddress.getField());
        } else {
//            wTv.setText("ADD WORK");
            wTv.setText(p.getAddWork());
            setWorkTv.setVisibility(GONE);
        }
    }

    @Override
    public GoogleMap getGoogleMapWeakReference() {
        return mMap;
    }

    public enum AddressLocationType {
        Pickup, Dropoff, Via, Driverlocation
    }

    public double calculateTotalFaresForVehicleAdapter(double vehicleFares,
                                                       double babySeatExtras) {
        double totalFaresFromGetQuote = 0;
        try {
            totalFaresFromGetQuote = model.getExtraCharges() + model.getAgentFees() + model.getAgentCharge() + model.getCongestion() + model.getParking() + model.getWaiting() + model.getAgentCommission() + vehicleFares + babySeatExtras + model.getBookingFee();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalFaresFromGetQuote;
    }

    public double calculateTotalFares(double babySeatExtras) {
        double totalFaresFromGetQuote = 0;
        try {
            double oneWayFare = 0;
            if (!sp.getBoolean(CommonVariables.ISMEMBERUSERLOGIN, false) && model.getPaymentType().toLowerCase().contains("credit") && model.getCompanyPrice() > finalPrice) {
                oneWayFare = model.getCompanyPrice();
                totalFaresFromGetQuote = model.getExtraCharges() + model.getAgentFees() + model.getAgentCharge() + model.getCongestion() +
                        model.getParking() + model.getWaiting() + model.getAgentCommission() + oneWayFare + babySeatExtras;
                return totalFaresFromGetQuote;
            }

            if (model.getPaymentType().equalsIgnoreCase("Cash")) {
                oneWayFare = Double.parseDouble(model.getOneWayFare());
            } else {
                oneWayFare = model.getCompanyPrice();
            }

            totalFaresFromGetQuote = model.getExtraCharges() + model.getAgentFees() + model.getAgentCharge() + model.getCongestion() +
                    model.getParking() + model.getWaiting() + model.getAgentCommission() + oneWayFare + babySeatExtras + model.getBookingFee();
            return totalFaresFromGetQuote;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getLocationMode() {
        try {
            return Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void makeRoute(Intent data, LocAndField locAndField) {
        locAndFieldArrayList.clear();

        if (locAndField == null) {
            locAndFieldArrayList = data.getParcelableArrayListExtra("key_locAndFieldArrayList");
        }

        if (data == null) {
            if (mCurrentLocationLocAndField == null) {
                locAndFieldArrayList.add(locAndField);
                goToDropOff();
            } else {

                locAndFieldArrayList.add(mCurrentLocationLocAndField);
                locAndFieldArrayList.add(locAndField);
            }
        }

        try {
            try {
                if (customDate.length() == 0) {

                } else {
                    mPickupDate = customDate;
                    SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date dateObj = curFormater.parse(mPickupDate);
                    SimpleDateFormat curFormater2 = new SimpleDateFormat(CommonVariables.getDateFormat(CommonVariables.FORMAT_DATE), Locale.getDefault());
                    mPickupDate = curFormater2.format(dateObj);
                    mPickupDate = mPickupDate.replaceAll("/", "-");
                    confirm_booking.setText("SCHEDULE BOOKING");
                    makeArcAgain = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            model.setPickUpDate(mPickupDate);
            model.setPickUpTime((customTime.length() == 0) ? mPickupTime : customTime);

        } catch (Exception e) {
            e.printStackTrace();
        }

        onAddressChangeListener(true);
    }

    public String isDateAfterText(String date_Time) {
        try {
            Calendar now = Calendar.getInstance();
            Date date1 = new SimpleDateFormat(CommonVariables.getDateFormat(CommonVariables.FORMAT_DATE_TIME), Locale.getDefault()).parse(date_Time);
            now.set(Calendar.SECOND, 0);
            long sum = date1.getTime() - now.getTime().getTime();
            float seconds = sum / 1000;
            float mins = seconds / 60;
            if (mins <= 10) {
                return "ASAP";
            } else if (mins > 10 && mins < 25) {
                return "In 15 mins";
            } else if (mins <= 30) {
                return "In 30 mins";
            } else {
                return "Custom";
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                Calendar now = Calendar.getInstance();
                Date date1 = new SimpleDateFormat(CommonVariables.getDateFormat(CommonVariables.FORMAT_DATE_TIME), Locale.US).parse(date_Time);
                now.set(Calendar.SECOND, 0);
                long sum = date1.getTime() - now.getTime().getTime();
                float seconds = sum / 1000;
                float mins = seconds / 60;
                if (mins <= 10) {
                    return "ASAP";
                } else if (mins > 10 && mins < 25) {
                    return "In 15 mins";
                } else if (mins <= 30) {
                    return "In 30 mins";
                } else {
                    return "Custom";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return "ASAP";
    }

    boolean isDefault = false;

    public boolean isDateAfter(String date_Time) {
        try {
            Calendar now = Calendar.getInstance();
            Date date1 = new SimpleDateFormat(CommonVariables.getDateFormat(CommonVariables.FORMAT_DATE_TIME), Locale.getDefault()).parse(date_Time);

            now.set(Calendar.SECOND, 0);
            if (!date1.before(now.getTime())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

            try {
                Calendar now = Calendar.getInstance();
                Date date1 = new SimpleDateFormat(CommonVariables.getDateFormat(CommonVariables.FORMAT_DATE_TIME), Locale.US).parse(date_Time);

                now.set(Calendar.SECOND, 0);
                if (!date1.before(now.getTime())) {
                    return true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }


    public Bitmap getThumbnail(String filename) {
        Bitmap thumbnail = null;
        try {
            if (thumbnail == null) {
                try {
                    File filePath = mContext.getFileStreamPath(filename);
                    FileInputStream fi = new FileInputStream(filePath);
                    thumbnail = BitmapFactory.decodeStream(fi);
                } catch (Exception ex) {
                    Log.e(" on internal storage", ex.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbnail;
    }

    public Bitmap createCustomMarker(Context context, String _name, String time) {
        Log.d(TAG, "createCustomMarker: " + time);

        //5 mins , 1 hour 5 min
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_marker, null);
        //if(_name)
//        _name = _name.toLowerCase().replace("minute", "min");
//        _name = _name.toLowerCase().replace("minutes", "mins");
//        _name = _name.toLowerCase().replace("hour", "hr");
//        _name = _name.toLowerCase().replace(",", " ");
//        _name = _name.toLowerCase().replace("  ", " ");

//        String[] splitArr = _name.split(" ");


        //  Hide Marker Work
        if (sp.getString(Config.HideCurrentLocation, "0").equals("1")) {
            marker.findViewById(R.id.addressLyt).setVisibility(GONE);
        }

        TextView snippetTvTime = marker.findViewById(R.id.title);

        try {
            try {

                String[] words = _name.toLowerCase().split(" ");
                String carNamesCap = "";
                for (String s : words) {
                    carNamesCap = carNamesCap + " " + CommonMethods.capitizeString(s);
                }
                snippetTvTime.setText(carNamesCap);

            } catch (Exception ex) {
                snippetTvTime.setText(_name);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView txt_name = marker.findViewById(R.id.totalTimeForArrival);

        TextView txt_name_sub = marker.findViewById(R.id.subname);
        if (_name.equals("")) {
            txt_name_sub.setVisibility(GONE);
            txt_name.setText("N/A");
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) txt_name.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            txt_name.setLayoutParams(params);
        } else {
            if (time.equals("")) {
                txt_name.setText("N/A");
                txt_name_sub.setVisibility(GONE);
            } else {
                time = time.toLowerCase().replace("min", "");
                time = time.toLowerCase().replace("(", "");
                time = time.toLowerCase().replace(")", "");
                time = time.toLowerCase().replace(" ", "");
                time = time.toLowerCase().replace("  ", "");
                time = time.toLowerCase().replace("s", "");
                txt_name.setText(time.trim());
                txt_name_sub.setVisibility(VISIBLE);
            }
//            if (splitArr.length >= 4) {
//                txt_name.setText(splitArr[0]);
//                txt_name_sub.setText(splitArr[2] + " " + splitArr[3]);
//            } else {
//                txt_name.setText(splitArr[0]);
//                txt_name_sub.setText(splitArr[1]);
//            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) txt_name.getLayoutParams();
            params.setMargins(0, -4, 0, 0);
            txt_name.setLayoutParams(params);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    public static String clickFrom = "";

    private void setDarkAndNightMapStyleColor() {
        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mContext, R.raw.map_night_style));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mContext, R.raw.map_style));
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }

    }

    private void showDirectionDialogue(String msg) {
        _msg = msg;

        if (_msg == null) {
            _msg = "Error in getting direction.";
            buttonText = "Try Again";
            title = buttonText;
        } else {
            buttonText = "Call Office";
            title = "";
        }

        sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(title)
                .setContentText(_msg)
                .setCancelText(p.getCancel())
                .setConfirmText(buttonText)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        if (buttonText.equals("Call Office")) {
                            new Manager_GetInfo(getContext(), p, new Listener_GetInfo() {
                                @Override
                                public void onComplete(String result) {
                                    result = checkIfHasNullForString(result);
                                    if (!result.isEmpty() && isAdded()) {
                                        try {
                                            JSONObject parentObject = new JSONObject(result);
                                            if (parentObject.getBoolean("HasError")) {
                                            } else {
                                                JSONObject dataObject = parentObject.optJSONObject("Data");
                                                if (dataObject != null) {
                                                    JSONObject webSettingsObject = dataObject.optJSONObject("webSettings");
                                                    if (webSettingsObject != null) {
                                                        String ReceivingEmail = checkIfHasNullForString(webSettingsObject.optString("ReceivingEmail"));
                                                        String WebSiteAddress = checkIfHasNullForString(webSettingsObject.optString("WebSiteAddress"));
                                                        String URL = checkIfHasNullForString(webSettingsObject.optString("URL"));

                                                        String PhoneNumber = checkIfHasNullForString(webSettingsObject.optString("PhoneNumber"));
                                                        String[] numbers = PhoneNumber.split("[\\,\\\\\\/\\-]");

                                                        if (numbers != null && numbers.length > 0) {
                                                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", numbers[0].trim(), null));
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        } else if (buttonText.equals("Try Again")) {
                            onAddressChangeListener(true);
                        }

                        return;
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
                        sweetAlertDialog.cancel();
                    }
                })
                .show();
    }

    private void sameWork(Intent data, String name) {
        String payType = "";

        if (data != null) {
            payType = data.getStringExtra("selectedType");
        } else if (name != "") {
            payType = name;
        } else {
            String[] paymentTypes = sp.getString(Config.PaymentTypes, "").split(",");
            String paymenttext = paymentTypes[0];

            if (paymenttext.equals(CASH)) {
                paymenttext = "Cash";
            } else if (paymenttext.equals(Config.CARDCAR)) {
                paymenttext = "Card In Car";
            } else if (paymenttext.equals(Config.CARD)) {
                paymenttext = "Card";
            } else if (paymenttext.equals(Config.ACCOUNT)) {
                paymenttext = "Account";
            }
            payType = paymenttext;
        }

        model.setPaymentType(payType);
        if (payType.equalsIgnoreCase("Card In Car")) {
            cashtxt.setText("Card In Car");
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_credit_card));
        } else if (payType.equalsIgnoreCase("cash")) {
            cashtxt.setText("Cash");
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cash));

        } else if (payType.equalsIgnoreCase("credit card")) {
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_credit_card));

            if (Gateway.contains(Config.JUDOPAY)) {
                ArrayList<CardJudoModel> cardJudoModelArrayList = mHelper.getJudoCardList();
                if (cardJudoModelArrayList.size() == 0) {
                    cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_credit_card));
                    cashtxt.setText("Card");
                    return;
                }

                int saveSelectedIndex = mHelper.getIntVal("cardSelectedIndex");
                if (cardJudoModelArrayList.size() <= saveSelectedIndex) {
                    return;
                }
                CardJudoModel c = cardJudoModelArrayList.get(saveSelectedIndex);
                cashtxt.setText(" XX-" + c.getLastFour());

           /*     if (c.getType() == WalletConstants.CardNetwork.VISA) {
                    cashImg.setImageResource(R.drawable._visa);
                } else if (c.getType() == WalletConstants.CardNetwork.MASTERCARD) {
                    cashImg.setImageResource(R.drawable._mastercard);
                } else if (c.getType() ==WalletConstants. CardNetwork.MAESTRO) {
                    cashImg.setImageResource(R.drawable._maestro);
                } else if (c.getType() == CardNetwork.SOLO) {
                    cashImg.setImageResource(R.drawable._solo);
                } else if (c.getType() == CardNetwork.SWITCH) {
                    cashImg.setImageResource(R.drawable._switch);
                } else if (c.getType() == CardNetwork.AMEX) {
                    cashImg.setImageResource(R.drawable._amex);
                } else if (c.getType() == CardNetwork.JCB) {
                    cashImg.setImageResource(R.drawable._jcb);
                } else if (c.getType() == CardNetwork.VISA_DEBIT) {
                    cashImg.setImageResource(R.drawable._visadebit);
                } else if (c.getType() == CardNetwork.VISA_ELECTRON) {
                    cashImg.setImageResource(R.drawable._visaelectron);
                } else if (c.getType() == CardNetwork.CHINA_UNION_PAY) {
                    cashImg.setImageResource(R.drawable._unionpay);
                } else {

                }
*/
                cashImg.setImageResource(R.drawable.ic_credit_card);
//                if (c.getType() == WalletConstants.CardNetwork.VISA) {
//                    cashImg.setImageResource(R.drawable._visa);
//                } else if (c.getType() == WalletConstants.CardNetwork.MASTERCARD) {
//                    cashImg.setImageResource(R.drawable._mastercard);
//                } else if (c.getType() == WalletConstants.CardNetwork.AMEX) {
//                    cashImg.setImageResource(R.drawable._amex);
//                } else if (c.getType() == WalletConstants.CardNetwork.JCB) {
//                    cashImg.setImageResource(R.drawable._jcb);
//                } else {
//                    cashImg.setImageResource(R.drawable.ic_credit_card);
//                }

            }


        } else if (payType.equalsIgnoreCase("account")) {
            cashtxt.setText("Account");
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_credit_card));
        }
    }

    public static void setCarName(String CarName) {
        selectedCar = CarName;
    }

    private void performPickUpPressed(boolean isFromList) {
        if (!restrictionVehicle(CurrSelectedCarId)) return;

        if (mCurrentLocationLocAndField != null) {

            isChooseFromList = true;
            mCurrentAddressType = Pickup;

            if (!isFromList) {
                setCurrentLocation(false);
            }
            if (locAndFieldArrayList.size() == 1) {
                goToDropOff();
            }
        } else {
            isChooseFromList = true;
            mCurrentAddressType = Pickup;

            if (!isFromList) {
                setCurrentLocation(false);
            }
            clickFrom = "pickup";
            locAndFieldArrayList.clear();
            locAndFieldArrayList.add(new LocAndField());

            if (locAndFieldArrayList.size() == 1) {
                goToDropOff();
            }
//            addressNotfound();
        }
    }

    private void openOrderDetailsSheet() {
        dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_grocery, null);
        EditText bottomSheetEt = sheetView.findViewById(R.id.bottomSheetEt);
        EditText orderNameEt = sheetView.findViewById(R.id.bottomSheetEt2);

        TextView clearFilterTv = sheetView.findViewById(R.id.clearFilterTv);

        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(SOFT_INPUT_STATE_VISIBLE);
        dialog.setContentView(sheetView);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                if (!model.getSetOrderDetails().equals("")) {
                    bottomSheetEt.setText(model.getSetOrderDetails());
                }
                if (!model.getorderName().equals("")) {
                    orderNameEt.setText(model.getorderName());
                }
//                BottomSheetDialog d = (BottomSheetDialog) dialog;
//                View bottomSheetInternal = d.findViewById(R.id.design_bottom_sheet);
//                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
//                bottomSheetEt.requestFocus();
//
//                if (bottomSheetEt.getText().toString().length() > 0) {
//                    clearFilterTv.setVisibility(VISIBLE);
//                } else {
//                    clearFilterTv.setVisibility(GONE);
//                }
            }
        });
        dialog.show();
        TextView doneBtn = sheetView.findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetEt.getText().toString().isEmpty() && orderNameEt.getText().toString().isEmpty()) {
                    FBToast.warningToast(mContext, "Please enter your order details!", FBToast.LENGTH_SHORT);
                    return;
                } else if (bottomSheetEt.getText().toString().isEmpty()) {
                    FBToast.warningToast(mContext, "Please enter your order number!", FBToast.LENGTH_SHORT);
                    return;
                } else if (orderNameEt.getText().toString().isEmpty()) {
                    FBToast.warningToast(mContext, "Please enter name on order!", FBToast.LENGTH_SHORT);
                    return;
                }
                model.setSetOrderDetails(bottomSheetEt.getText().toString());
                model.setorderName(orderNameEt.getText().toString());


                shoppingVehicleLayout.setVisibility(VISIBLE);
                rvVehicle.setVisibility(GONE);

                InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                dialog.dismiss();
            }
        });
        closeBottomSheet = sheetView.findViewById(R.id.closeBottomSheet);
        closeBottomSheet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        clearFilterTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog
                        .setTitleText("Clear Filter")
                        .setContentText("Do you want to clear filter?")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .showCancelButton(false)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                model.setSetOrderDetails("");
                                model.setorderName("");

                                shoppingVehicleLayout.setVisibility(GONE);
                                rvVehicle.setVisibility(VISIBLE);

                                dialog.dismiss();
                            }

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                sweetAlertDialog.dismissWithAnimation();
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
        });
    }

    boolean firstTime = true;

    private void afterRouteBottomSheetOfVehicle() {
//        int vehicleTextAndBarHeight = 105;
//
//        if (mFareModel.size() == 1) {
//            peekHeight = 123;
//
//        } else if (mFareModel.size() == 2) {
//            peekHeight = (123 * 2);
//
//        } else {
//            peekHeight = (123 * 3);
//        }

//        peekHeight = peekHeight + vehicleTextAndBarHeight;
//        peakHeight = 280;
        LinearLayout layoutBottomSheet;

        viewBarLl = getView().findViewById(R.id.viewBarLl);
        mapViewRl = getView().findViewById(R.id.mapViewRl);
        layoutBottomSheet = getView().findViewById(R.id.bottom_sheet);
        TextView swipeText = getView().findViewById(R.id.swipeText);
        layoutBottomSheet.setVisibility(VISIBLE);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        layoutBottomSheet.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutBottomSheet.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // View hidden = layoutBottomSheet.getChildAt(1);
                if (firstTime) {
                    firstTime = false;
                    // sheetBehavior.setPeekHeight(600);
                }
            }
        });

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        swipeText.setText(p.getSwipeDown());
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        swipeText.setText(p.getSwipeUp());
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

    }

    private void setCurrentLocation(boolean isComingFromList) {
        try {
            if (mCurrentLocationLocAndField != null) {
                switch (mCurrentAddressType) {
                    case Dropoff:
                        setToLocAndFieldArrayList(mCurrentLocationLocAndField);
                        onAddressChangeListener(true);
                        break;

                    case Pickup:
                        if (!isComingFromList) {
                            setFromLocAndFieldArrayList(mCurrentLocationLocAndField);
                        }
                        Picklocation();

                        break;

                    case Via:
                        /*List<String> addressList = getViaAddressesName();
                        if (addressList.contains(mCurrentLocation.toString())) {
                            Toast.makeText(mContext, "Address already added", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mViaListViewAdapter.addAddress(mCurrentLocation);*/
                        break;

                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Picklocation() {
        try {
            fromPickLocation = true;
            if (locAndFieldArrayList != null && locAndFieldArrayList.size() > 0) {
                LocAndField from = locAndFieldArrayList.get(0);

                String fromString = from.getLat() + "," + from.getLon();
                latpick = Double.parseDouble(from.getLat());
                latdriver = Double.toString(latpick);
                longpick = Double.parseDouble(from.getLon());
                longdriver = Double.toString(longpick);
                pickup = fromString;
            }
            onAddressChangeListener(locAndFieldArrayList.size() > 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void goToDropOff() {
        if (locAndFieldArrayList.size() == 0 && mCurrentLocationLocAndField == null) {
            locAndFieldArrayList.add(new LocAndField());
        }
        if (locAndFieldArrayList.size() == 0 && mCurrentLocationLocAndField != null) {
            locAndFieldArrayList.add(mCurrentLocationLocAndField);
        }
        // Hide Marker Work
        if (locAndFieldArrayList.size() == 1 && sp.getString(Config.HideCurrentLocation, "0").equals("1")) {
            if (mCurrentLocationLocAndField != null && locAndFieldArrayList.get(0).getField().equals(mCurrentLocationLocAndField.getField())) {
                locAndFieldArrayList.clear();
                locAndFieldArrayList.add(new LocAndField());
            }
        }
        Intent intent = new Intent(getContext(), Activity_SearchAddress.class);
        // Intent intent = new Intent(getContext(), Activity_SearchAddressNew.class);
        intent.putParcelableArrayListExtra("key_locAndFieldArrayList", locAndFieldArrayList);
        startActivityForResult(intent, 5050);
    }

    public void onAddressChangeListener(boolean isMapClear) {
        try {
            if (isMapClear) {
                mMap.clear();
                if (markers.size() > 0) {
                    for (int i = 0; i < markers.size(); i++) {
                        markers.get(i).remove();
                    }
                    markers.clear();
                    driverslist2.clear();
                }
            }

            if (locAndFieldArrayList.size() > 1) {
                LocAndField from = (locAndFieldArrayList.size() > 0) ? locAndFieldArrayList.get(0) : null;
                LocAndField to = (locAndFieldArrayList.size() > 1) ? locAndFieldArrayList.get(locAndFieldArrayList.size() - 1) : null;

                if (CurrLoc != null) {
                    CurrLoc.setLatitude(Double.parseDouble(from.getLat()));
                    CurrLoc.setLongitude(Double.parseDouble(from.getLon()));
                }

                try {
                    model.setFromLatLng(from.getLat() + "," + from.getLon());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    model.setToLatLng(to.getLat() + "," + to.getLon());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    mapOverlayView.removeRoutes();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                boolean isWebDispatch = sp.getString(CommonVariables.IS_WEB_DISPATCH, "0").equals("1");

                new Manager_GetRouteCoordinatesDataNew(getContext(), isWebDispatch, getValueForRouteCoordinate(), new Listener_GetRouteCoordinatesDataNew() {
                    @Override
                    public void onComplete(String response) {
                        String text = "";
                        String error_message = "";

                        if (sp.getString(CommonVariables.enableOutsideUK, "0").equals("0")) {
                            // 0 = Inside UK
                        } else {
                            // 1 = Outside Uk
                            try {
                                model.setPickupLat(locAndFieldArrayList.get(0).getLat());
                                model.setPickupLon(locAndFieldArrayList.get(0).getLon());
                                model.setFromLatLng(locAndFieldArrayList.get(0).getLat() + "," + locAndFieldArrayList.get(0).getLon());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.setDropLat(locAndFieldArrayList.get(1).getLat());
                                model.setDropLon(locAndFieldArrayList.get(1).getLon());
                                model.setToLatLng(locAndFieldArrayList.get(1).getLat() + "," + locAndFieldArrayList.get(1).getLon());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        makeArcAgain = true;


                        if (response != null && !response.isEmpty()) {
                            try {
                                JSONObject parentObject = new JSONObject(response);
                                if (parentObject.getBoolean("HasError")) {
                                    showDirectionDialogue(parentObject.getString("Message"));
                                    return;

                                } else {
                                    JSONObject dataJsonObject = parentObject.optJSONObject("Data");
                                    JSONObject clsDispatchDeadMileagesjson;
                                    if (dataJsonObject != null) {
                                        if (isWebDispatch) {
                                            clsDispatchDeadMileagesjson = dataJsonObject;
                                        } else {
                                            clsDispatchDeadMileagesjson = dataJsonObject.optJSONObject("clsDispatchDeadMileagesjson");
                                        }
                                        if (clsDispatchDeadMileagesjson != null) {

                                            if (sp.getString(CommonVariables.distanceUnit, "miles").equals("km")) {
                                                journeyMiles = checkIfHasNullForString(clsDispatchDeadMileagesjson.optString("Kilometers"));
                                            } else {
                                                journeyMiles = checkIfHasNullForString(clsDispatchDeadMileagesjson.optString("Distance"));
                                            }

                                            String duration = checkIfHasNullForString(clsDispatchDeadMileagesjson.optString("Duration"));
                                            PickupToDestinationTime = CommonMethods.extractDigit(duration) + "";

                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        unlockMap();

                        if (text == null) {
                            if (isActivityInBackground) {
                                isPendingDirectionDialogue = true;
                            } else {
                                showDirectionDialogue(null);
                            }
                            return;
                        }


                        try {
                            if (text.contains(">>")) {
                                String __text[] = text.split(">>");
                                RouteCoordinates = __text[1];
                                text = __text[0];
                            } else {
                                text = text;
                                RouteCoordinates = "";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        isrouteComplete = true;

                        ETA_txt2.setVisibility(GONE);
                        menu_btn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_back__));

                        if (locAndFieldArrayList.size() > 1) {
                            if (userModel.getName() != null && !userModel.getName().isEmpty()) {
                                callGetFares(false);
                            } else {
                                ((Fragment_Main) mContext).showUserDetailsPopUp();
                            }
                        } else {
                            Toast.makeText(mContext, "Please select pickup and drop off points", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } else {
                unlockMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFromLocAndFieldArrayList(LocAndField model) {
        if (locAndFieldArrayList != null) {
            if (locAndFieldArrayList.size() == 0) {
                locAndFieldArrayList.add(model);
            } else if (locAndFieldArrayList.size() == 1) {
                locAndFieldArrayList.set(0, model);
            }
        }
    }

    private void setToLocAndFieldArrayList(LocAndField model) {
        if (locAndFieldArrayList != null) {
//            LocAndField l = new LocAndField();
//            l.setAddressModel(model);

            if (locAndFieldArrayList.size() == 1) {
                locAndFieldArrayList.add(model);
            } else if (locAndFieldArrayList.size() == 2) {
                locAndFieldArrayList.set(1, model);
            }
        }
    }

    private void SaveBooking() {

        LocAndField from = locAndFieldArrayList.get(0);
        LocAndField to = locAndFieldArrayList.get(locAndFieldArrayList.size() - 1);

        if (model != null && model.getPickUpTime() == null) {

            String format = CommonVariables.getDateFormat(CommonVariables.FORMAT_DATE_TIME);
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

            Calendar date = Calendar.getInstance();
            long t = date.getTimeInMillis();
            Date afterAddingTenMins = new Date(t + (0 * ONE_MINUTE_IN_MILLIS));
            String formattedDate = dateFormat.format(afterAddingTenMins);
            if (formattedDate != null && formattedDate.contains(" ")) {
                String[] splittedDate = formattedDate.split(" ");
                if (splittedDate != null && splittedDate.length >= 2) {
                    String dates = splittedDate[0];
                    String times = splittedDate[1];
                    model.setPickUpDate(dates);
                    model.setPickUpTime(times);
                    mPickupDate = dates;
                    mPickupTime = times;
                }
            }
        }

        model.setCusomerEmail(userModel.getEmail());
        model.setCusomerName(userModel.getName());
        model.setCusomerMobile(userModel.getPhoneNo());
        model.setCusomerPhone(userModel.getPhoneNo());

        model.setFromAddressFlightNo(from.getFlightNo());
        model.setFromAddressType(locAndFieldArrayList.get(0).getLocationType());
        model.setFromAddressCommingFrom((from.getFromComing()));
        model.setPickupLat(from.getLat() + "");
        model.setPickupLon(from.getLon() + "");
        model.settoAddressDoorNO(to.getDoorNo());
        model.settoAddressType(locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getLocationType());
        model.setDropLat(to.getLat() + "");
        model.setDropLon(to.getLon() + "");

        model.setFromAddressCommingFrom(to.getFromComing());

        model.setJournyType(CommonVariables.JOURNY_ONEWAY);


        if (model.getJournyType() == CommonVariables.JOURNY_RETURN && model.getReturnDate().isEmpty()) {
            Toast.makeText(mContext, "Please enter return date", Toast.LENGTH_LONG).show();
            return;
        }

        String selectedPaymentMethod = model.getPaymentType();
        if (model.getOneWayFare() == null || model.getOneWayFare().equals("")) {
            mPickupDate = model.getPickUpDate();
            mPickupTime = model.getPickUpTime();
            callGetFares(true);

        } else {
            if (selectedPaymentMethod.equals("") || selectedPaymentMethod.equals("enter payment method") || selectedPaymentMethod.equals(null)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Alert!")
                        .setMessage("Please select a valid payment method.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();

            } else if (selectedPaymentMethod.equalsIgnoreCase("credit card")) {

                if (IsKonnectPay) {
                        if (_CardDetails.length() > 0 && !stripCustomerId.equals("") && !stripCardToken.equals(""))
                        {
                            new SaveBooking().execute(model);
                        }
                        else {
                            gotoHomePayment(4311);
                        }
                }
                else if (Gateway.equalsIgnoreCase(Config.Stripe)) {
                    if (sp.getString(Config.EnableCardHold, "").equals("1")) {
                        if (_CardDetails.length() > 0) {
                            new SaveBooking().execute(model);
                        } else {
                            gotoHomePayment(4311);
                        }
                    } else {
                        try {
                            mHelper.removeToSharePrefForStripeForOneCard();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (pm.length() == 0 && model.getTransactionId().length() == 0) {
                            gotoHomePayment(4311);
                        } else if (pm.length() > 0 && model.getTransactionId().length() == 0) {
                            processStripePayment();
                        } else {
                            Model_BookingDetailsModel[] params = {model};
                            new SaveBooking().execute(params);
                        }
                    }
                }
                else if (Gateway.equalsIgnoreCase("worldpay")) {
                    model.setPaymentType("credit card");
                    startActivityForResult(new Intent(getActivity(), PaymentView.class).putExtra("fare", openBottomSheet.isChecked() ? shoppingFares : model.getOneWayFare()), REQUEST_CODE_WorldPay);
                }
                else if (Gateway.equalsIgnoreCase(Config.JUDOPAY)) {
                    model.setPaymentType("credit card");
                    final CardJudoModel receipt = mHelper.getCardJudoModel();
                    if (receipt != null) {
                        Model_BookingDetailsModel[] params = {model};
                        new SaveBooking(receipt).execute(params);

                    } else {
                        gotoHomePayment4080(4080);
                        /*sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
                        sweetAlertDialog.setTitleText("Card Details Not Found!")
                                .setContentText("Add card details to make booking via card")
                                .setCancelText(p.getCancel())
                                .setConfirmText("Add Card Details")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        performRegisterCard();
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
                                .show();*/
                    }
                }

            } else if (selectedPaymentMethod.equalsIgnoreCase("account")) {
                if (sp.getBoolean(CommonVariables.ISMEMBERUSERLOGIN, false) && userModel.getAccountWebID() != null && !userModel.getAccountWebID().equals("")) {
                    Model_BookingDetailsModel[] params = {model};
                    new SaveBooking().execute(params);

                } else {
                    sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitleText("Account Details Not Found!")
                            .setContentText("Add account details to make booking via account")
                            .setCancelText(p.getCancel())
                            .setConfirmText("Add Details")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    gotoHomePayment(4311);
//                                    startActivityForResult(new Intent(getActivity(), Activity_AccountMemberLogin.class).putExtra("isBooking", true), Account_Success);
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

            } else {
                Model_BookingDetailsModel[] params = {model};
                new SaveBooking().execute(params);
            }
        }
    }

    private void showDialog(JSONArray promoObjarry) {
        try {
            TextView title, msg, label, code, pr_apply, promoEndDate;
            PromoModel promoList = new PromoModel();

            for (int i = 0; i < promoObjarry.length(); i++) {
                JSONObject promoObj = promoObjarry.getJSONObject(i);

                try {
                    promoList.setPromoCode(promoObj.optString("PromotionCode"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    promoList.setTitle(promoObj.optString("PromotionTitle"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    promoList.setMsg(promoObj.optString("PromotionMessage"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    promoList.setStrtDate(promoObj.optString("PromotionStartDateTime"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    promoList.setEndDate(promoObj.optString("PromotionEndDateTime"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    promoList.setPromType(promoObj.optString("DiscountTypeId"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    promoList.setPromoValue(promoObj.optString("Charges"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    promoList.setPromoServerId(promoObj.optString("PromotionId"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    promoList.setTotal(promoObj.optString("Totaljourney"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    promoList.setUsed(promoObj.optString("Used"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    promoList.setPROMOTIONTYPEID(promoObj.optString("PromotionTypeID"));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.promo_child_dialogue);

            title = dialog.findViewById(R.id.promotitle);
            msg = dialog.findViewById(R.id.promoMsg);
            label = dialog.findViewById(R.id.statusLabel2);
            label.setVisibility(VISIBLE);
            label.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            code = dialog.findViewById(R.id.promoCode);
            pr_apply = dialog.findViewById(R.id.apply_code);
            promoEndDate = dialog.findViewById(R.id.promoEndDate);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 5);
            title.setLayoutParams(params);
            title.setText(promoList.getTitle());
            msg.setText(promoList.getMsg());
            code.setText("Code : " + promoList.getPromoCode());
            promoEndDate.setText("Valid till " + promoList.getEndDate());
            //	boolean isValid=checkValidity(promoList.get(0).getEndDate());

            pr_apply.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new VerifyPromotionCode().execute(promoList.getPromoCode());
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {

        }
    }

    private void handleRegisterCardResult(int resultCode, Intent data) {
        switch (resultCode) {
            case 2:
              /*  JudoResult receipt = data.getParcelableExtra(AddCardClass.INSTANCE.getJUDORECEIPT());
                CardJudoModel cardJudoModel = new CardJudoModel();
                cardJudoModel.setToken(receipt.getCardDetails().getToken());
                cardJudoModel.setEndDate(receipt.getCardDetails().getFormattedEndDate());
                cardJudoModel.setLastFour(receipt.getCardDetails().getLastFour());
                cardJudoModel.setConsumerToken(receipt.getConsumer().getConsumerToken());
                cardJudoModel.setConsumerReference(receipt.getConsumer().getYourConsumerReference());
                cardJudoModel.setType(receipt.getCardDetails().getType());
                cardJudoModel.set3DS(true);
                cardJudoModel.setReceiptid(receipt.getReceiptId());
                new SaveCardReciept().execute(cardJudoModel);*/
                break;


            case 3:
                break;

            case 4:
                FBToast.errorToast(getActivity(), "Invalid Card Details", FBToast.LENGTH_SHORT);
                break;

//            case Judo.RESULT_DECLINED:
//                FBToast.errorToast(getActivity(), "Card Declined!", FBToast.LENGTH_SHORT);
//                break;
        }
    }

    private void gotoHomePayment(int code) {
        try {
            confirmbtn = true;
//            String fare = fare_txt.getText().toString().replace(sp.getString(CurrencySymbol, "\u00A3"), "");
            if (vehicleSelectedIndex == -1) {
                vehicleSelectedIndex = 0;
            }
            double fare = mFareModel.get(vehicleSelectedIndex).getFinalFares();
            Intent intent = new Intent(getActivity(), Activity_HomePayment.class);
            intent.putExtra("Amount", fare);

            try {
                Model_ValidateBookingInfo info = new Model_ValidateBookingInfo(
                        "",
                        CommonVariables.SUB_COMPANY,
                        "" + mPickupDate,
                        "" + mPickupTime,
                        "" + locAndFieldArrayList.get(0).getField(),
                        "" + locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getField(),
                        "" + locAndFieldArrayList.get(0).getLat() + "," + locAndFieldArrayList.get(0).getLon(),
                        "" + locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getLat() + "," + locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getLon(),
                        CommonVariables.clientid + "4321orue",
                        String.valueOf(CommonVariables.clientid));
                Gson gson = new Gson();
                String infoObject = gson.toJson(info);
                intent.putExtra("ValidateBookingInfo", infoObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            startActivityForResult(intent, code);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoHomePayment4080(int code) {
        try {
            confirmbtn = true;
            Intent intent = new Intent(getActivity(), Activity_HomePayment.class);
            startActivityForResult(intent, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void buildGoogleApiClient() {
        try {
            mGoogleClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
            mGoogleClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkLocationIsEnabled() {
        try {
            if (mGoogleClient == null) {
                buildGoogleApiClient();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)).build();

            LocationServices.SettingsApi.checkLocationSettings(mGoogleClient, locationSettingsRequest).setResultCallback((ResultCallback<Result>) result -> {
                final Status status = result.getStatus();
                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED && status.hasResolution()) {

                    try {
                        status.startResolutionForResult(mContext, REQUEST_CHECK_SETTINGS);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lockMap() {
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        //			mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    private void unlockMap() {
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        //			mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            if (fusedLocationClient != null) {
                fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: run: run: 100 : " + latdriver);
                        if (latdriver.equals("") || latdriver == "0" && isAdded()) {
                            if (handle != null) {//replace driveLoc
                                Log.d(TAG, "run: run: run: 4");
                                handle.removeCallbacks(m_runnable);
                                handle.post(m_runnable);
                            }
                        }
                    }
                }, 3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopLocationUpdates() {
        try {
            Log.d(TAG, "onLocationResult: LOCATION CALLBACK STOP LOCATION");

            if (locationCallback != null) {
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void animateMarkerNew(final LatLng destination, final Marker marker) {
        try {
            if (marker != null && marker.getTag() != null) {
                final LatLng startPosition = marker.getPosition();
                final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);
                final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);

                if (!movingDrivers.contains(marker.getTag().toString())) {
                    movingDrivers.add(marker.getTag().toString());
                }
                valueAnimator.setDuration((long) vehicleTimer);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        try {
                            if (isAdded()) {
                                float v = animation.getAnimatedFraction();
                                LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                                marker.setPosition(newPosition);
                            }
                        } catch (Exception ex) {
                            movingDrivers.remove(marker.getTag().toString());
                        }
                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        try {
                            movingDrivers.remove(marker.getTag().toString());
                        } catch (Exception e) {
                        }
                    }
                });
                valueAnimator.start();
            }

        } catch (Exception e) {
            try {
                movingDrivers.remove(marker.getTag().toString());
            } catch (Exception e1) {

            }
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
                        handler.postDelayed(this, 50);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    public void applyDiscount(String code) {
        try {
            VerifiedCode = code;
            double finalPrice = 0f;
            if (openBottomSheet.isChecked()) {
                model.setOneWayFare(shoppingFares);
            }
            promotxt.setText(code.toUpperCase());
            promotxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, cn.pedant.SweetAlert.R.drawable.ic_cancel_black_24dp, 0);
            finalPrice = calculateTotalFares(0);
            if (finalPrice > 0) {
                if (!isSignupPromoApplied) {
                    promoModel = mDatabaseOperations.getPromoDetailsByCode(code);
                } else {
                    promoModel = new PromoModel();
                    JSONObject promoObj = new JSONObject(sp.getString(Config.SignupPromoString, ""));
                    promoModel.setPromoCode(promoObj.getString("PromotionCode"));
                    promoModel.setTitle(promoObj.getString("PromotionTitle"));
                    promoModel.setMsg(promoObj.getString("PromotionMessage"));
                    promoModel.setStrtDate(promoObj.getString("PromotionStartDateTime"));
                    promoModel.setEndDate(promoObj.getString("PromotionEndDateTime"));
                    promoModel.setPromType(promoObj.getString("DiscountTypeId"));
                    promoModel.setPromoValue(promoObj.getString("Charges"));
                    promoModel.setPromoServerId(promoObj.getString("PromotionId"));
                    promoModel.setTotal(promoObj.getString("Totaljourney"));
                    promoModel.setUsed(promoObj.getString("Used"));
                    promoModel.setPROMOTIONTYPEID(promoObj.getString("PromotionTypeID"));
                }
                if (promoModel != null && !promoModel.getPromoValue().equals("0")) {
                    double discountedVal = 0;
                    if (promoModel.getPromoType().toLowerCase().trim().equals("1")) {
                        discountedVal = calculateTotalFares(0) * Float.parseFloat(promoModel.getPromoValue()) / 100;
                    } else {
                        discountedVal = Float.parseFloat(promoModel.getPromoValue());
                    }

                    if (promoModel.getMaxDiscount() != null && !promoModel.getMaxDiscount().equals("") && !promoModel.getMaxDiscount().equals("null")) {
                        try {
                            float maxDiscount = Float.parseFloat(promoModel.getMaxDiscount());
                            if (discountedVal > maxDiscount) {
                                finalPrice = finalPrice - maxDiscount;
                            } else {
                                model.setOneWayFare("0.00");
                                fare_txt1.setVisibility(VISIBLE);
                                fare_txt.setTextColor(getResources().getColor(R.color.promo_background));
                                finalPrice -= discountedVal;
                            }
                        } catch (Exception e) {
                            finalPrice -= discountedVal;
                        }
                    } else {
                        finalPrice -= discountedVal;
                    }

                    fare_txt.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", finalPrice));

                    double faresOrg = calculateTotalFares(0);
                    fare_txt1.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", faresOrg));
                    fare_txt1.setPaintFlags(fare_txt1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    if (finalPrice <= 0) {
                        fare_txt.setText(sp.getString(CurrencySymbol, "\u00A3") + "0.00");
                    } else {
                        model.setOneWayFare(String.format("%.2f", finalPrice));
                        fare_txt1.setVisibility(VISIBLE);
                        fare_txt.setTextColor(getResources().getColor(R.color.promo_background));
                    }
                } else {
                    //removeDiscount("");
                }
            } else {

                ZeroFareTxt.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            //removeDiscount("");
        }
    }

    private double getDiscountedFares(String code, double vehicleFare) {
        double finalPrice = vehicleFare;
        if (code.equals("")) {
            return finalPrice;
        }
        try {
            VerifiedCode = code;
            if (finalPrice > 0) {
                if (!isSignupPromoApplied) {
                    promoModel = mDatabaseOperations.getPromoDetailsByCode(code);
                } else {
                    promoModel = new PromoModel();
                    JSONObject promoObj = new JSONObject(sp.getString(Config.SignupPromoString, ""));
                    promoModel.setPromoCode(promoObj.getString("PromotionCode"));
                    promoModel.setTitle(promoObj.getString("PromotionTitle"));
                    promoModel.setMsg(promoObj.getString("PromotionMessage"));
                    promoModel.setStrtDate(promoObj.getString("PromotionStartDateTime"));
                    promoModel.setEndDate(promoObj.getString("PromotionEndDateTime"));
                    promoModel.setPromType(promoObj.getString("DiscountTypeId"));
                    promoModel.setPromoValue(promoObj.getString("Charges"));
                    promoModel.setPromoServerId(promoObj.getString("PromotionId"));
                    promoModel.setTotal(promoObj.getString("Totaljourney"));
                    promoModel.setUsed(promoObj.getString("Used"));
                    promoModel.setPROMOTIONTYPEID(promoObj.getString("PromotionTypeID"));
                }

                if (promoModel != null && !promoModel.getPromoValue().equals("0")) {
                    double discountedVal = getDiscountedValue(promoModel, finalPrice);
                    finalPrice = getMaxDiscount(promoModel, finalPrice, discountedVal);
                    promotxt.setText("Remove Promo");
                    promo_image.setImageResource(cn.pedant.SweetAlert.R.drawable.ic_cancel_black_24dp);

                } else {
                    finalPrice = vehicleFare;
                }
            } else {
                finalPrice = vehicleFare;
            }
        } catch (Exception e) {
            finalPrice = 0;
        }
        return finalPrice;
    }

    private double getMaxDiscount(PromoModel promoModel, double finalPrice, double discountedVal) {
        try {
            if (promoModel.getMaxDiscount() != null && !promoModel.getMaxDiscount().equals("") && !promoModel.getMaxDiscount().equals("0") && !promoModel.getMaxDiscount().equals("null")) {
                float maxDiscount = Float.parseFloat(promoModel.getMaxDiscount());
                finalPrice = (discountedVal > maxDiscount) ? (finalPrice - maxDiscount) : (finalPrice - discountedVal);
            } else {
                finalPrice = finalPrice - discountedVal;
            }

            if (finalPrice <= 0) {
                finalPrice = 0;
            }
        } catch (Exception e) {
            finalPrice -= discountedVal;
        }
        return finalPrice;
    }

    private double getDiscountedValue(PromoModel promoModel, double fares) {
        double discountedVal = 0;
        if (promoModel.getPromoType().toLowerCase().trim().equals("1")) {
            discountedVal = fares * (Double.parseDouble(promoModel.getPromoValue()) / 100);
        } else {
            discountedVal = Float.parseFloat(promoModel.getPromoValue());
        }
        return discountedVal;
    }

    public void removeDiscount() {
        promotxt.setText("Add Promo");
        promo_image.setImageResource(R.drawable.promo);

        VerifiedCode = "";
        double finalPrice = 0f;

        finalPrice = Double.parseDouble(fare_txt1.getText().toString().replace(sp.getString(CurrencySymbol, "\u00A3"), ""));

        if (finalPrice > 0) {
            fare_txt.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", finalPrice));
            fare_txt.setVisibility(VISIBLE);
            fare_txt1.setVisibility(GONE);
            model.setOneWayFare(String.format("%.2f", finalPrice));
        }

        PromotionEndDateTime = "";
        PromotionStartDateTime = "";
        promoCodeMiniMumFare = "";
    }

    private void showDrvNotes() {
        //create soft keyboard object
        //1.USE

        String SpecialNotes = "";
        if (model != null) {
            SpecialNotes = model.getSpecialNotes();
        }

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_driver_notes);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText et = dialog.findViewById(R.id.et);
        et.setText(SpecialNotes);
        et.setFocusable(true);
        et.requestFocus();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


        Button donebtn = dialog.findViewById(R.id.doneTv);
        donebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et.getText().toString().trim().length() == 0) {
                    et.setError("No description found!");
                    return;
                }
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                et.clearFocus();
                model.setSpecialNotes(et.getText().toString().trim());
                dialog.dismiss();
            }
        });

        Button cancelbtn = dialog.findViewById(R.id.cancelTv);
        cancelbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                et.clearFocus();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void whereTo() {
        //  Hide Marker Work
        clickFrom = sp.getString(Config.HideCurrentLocation, "0").equals("1") ? "pickup" : "drop";
        performPickUpPressed(false);
    }

    private void callForChange() {

        if (mPickupDate.trim().equals("")) {
            if (model.getPickUpDate().isEmpty()) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                Date c = Calendar.getInstance().getTime();
                String formattedDate = df.format(c);
                mPickupDate = formattedDate;
            }
        }

        String selectedType = sp.getString(CommonVariables.paymentType, "Cash");
        cashtxt.setText(selectedType);

        if (selectedType.toLowerCase().equalsIgnoreCase(p.getCreditCard())) {
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_credit_card));
        } else if (selectedType.toLowerCase().equalsIgnoreCase(p.getCash())) {
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cash));
        } else if (selectedType.toLowerCase().equalsIgnoreCase(p.getAccount())) {
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_account));
        }
        model.setPaymentType(selectedType);

        if (selectedType.equalsIgnoreCase(p.getCreditCard())) {
            cashtxt.setText(p.getCreditCard());
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_credit_card));

        } else if (selectedType.equalsIgnoreCase(p.getCash())) {
            cashtxt.setText(p.getCash());
            cashImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cash));
        }

        if (selectedType.equalsIgnoreCase(p.getAccount())) {
            cashtxt.setText(p.getAccount());
        }

        callGetFares(true);
    }

    private void showVehicle(View view) {
//        SurgePriceTv = view.findViewById(R.id.SurgePriceTv);
//        if (model.getSurgePrice() > 0) {
//            SurgePriceTv.setVisibility(VISIBLE);
//            SurgePriceTv.setText("Surge x" + model.getSurgePrice());
//        } else {
//            SurgePriceTv.setVisibility(GONE);
//        }

        RelativeLayout SurgeFareRl = view.findViewById(R.id.SurgeFareRl);
        if (model.getSurgePrice() > 0 && model.getSurgePrice() != 1) {
            SurgeFareRl.setVisibility(VISIBLE);
            SurgeFareTxt.setText(model.getSurgePrice() + "x" + " | Peak factor ");
        } else {
            SurgeFareRl.setVisibility(GONE);
        }

        rvVehicle = view.findViewById(R.id.rvVehicle);
        rvVehicle.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVehicle.setHasFixedSize(true);

        for (AnyVehicleFareModel a : mFareModel) {
            double total = 0;
            try {
                total = calculateTotalFaresForVehicleAdapter(Double.parseDouble(a.getSingleFare()), model.getExtraTotal());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (model.getSurgePrice() > 0) {
                total = total * model.getSurgePrice();
                a.setFinalFares(total);
            } else {
                a.setFinalFares(total);
            }
        }


        try {
            if (vehicleSelectedIndex > -1) {
                model.setCar(mFareModel.get(vehicleSelectedIndex).getName());
                model.setOneWayFare(mFareModel.get(vehicleSelectedIndex).getSingleFare());
                model.setExtraCharges(mFareModel.get(vehicleSelectedIndex).getExtraCharges());
                model.setAgentFees(mFareModel.get(vehicleSelectedIndex).getAgentFees());
                model.setAgentCharge(mFareModel.get(vehicleSelectedIndex).getAgentCharge());
                model.setCongestion(mFareModel.get(vehicleSelectedIndex).getCongestion());
                model.setParking(mFareModel.get(vehicleSelectedIndex).getParking());
                model.setWaiting(mFareModel.get(vehicleSelectedIndex).getWaiting());
                model.setAgentCommission(mFareModel.get(vehicleSelectedIndex).getAgentCommission());
                model.setCompanyPrice(mFareModel.get(vehicleSelectedIndex).getCompanyPrice());
                model.setBookingFee(mFareModel.get(vehicleSelectedIndex).getBookingFee());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        vehicleAdapter = new VehicleListAdapter(getContext(), mFareModel);
        rvVehicle.setAdapter(vehicleAdapter);
        vehicleAdapter.setSelection(selectedPos);
    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    //Method for finding bearing between two points
    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

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

        return brng;
    }

    private boolean restrictionVehicle(int serverId) {
        boolean isEmpty = false;

        String msg = sp.getString(CommonVariables.Restrict_Message, "");
        String veh = sp.getString(CommonVariables.Restrict_Vehicle, "");

        if (msg.length() == 0) {
            isEmpty = true;
        } else if (veh.length() == 0) {
            isEmpty = true;
        } else {
            String[] vehArr = veh.split(",");
            if (isRestrictedVehicleSelected(vehArr, serverId)) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("")
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
                isEmpty = false;
            } else {
                isEmpty = true;
            }
        }
        return isEmpty;
    }

    private boolean isRestrictedVehicleSelected(String[] vehArr, int serverId) {
        boolean isEmpty = false;

        try {
            if (vehArr.length > 0) {
                for (int i = 0; i < vehArr.length; i++) {
                    Log.d(TAG, "restrictionVehicle: " + Integer.parseInt(vehArr[i]) + "=" + serverId);
                    if (Integer.parseInt(vehArr[i]) == serverId) {
                        isEmpty = true;
                        break;
                    }
                }
            } else {
                isEmpty = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isEmpty;
    }

    private Double getValueFromResponse(JSONObject obj, String key) {
        try {
            return obj.getDouble(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }

    }

    public static String[] getFilteredAddress(String address) {
        String[] split = address.split(" ");
        int minIndex = split.length - 2;
        String result = "";
        String subTxt = "";
        if (split.length >= 4 && (split[split.length - 3].equalsIgnoreCase("london") || split[split.length - 3].equalsIgnoreCase("belfast") || split[split.length - 3].equalsIgnoreCase("coventry"))) {
            minIndex = split.length - 3;
        }
        for (int i = 0; i < split.length; i++) {
            if (i >= (minIndex)) {
                subTxt += (split[i] + " ");
            } else {
                result += (split[i] + " ");
            }
        }
        String arr[] = {result, subTxt};
        return arr;
    }

    private void greetingMessage() {
        String message = "";
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int image = 0;

        if (timeOfDay >= 0 && timeOfDay < 12) {
            message = "Good Morning";
            image = R.drawable.ic_sun;
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            message = "Good Afternoon";
            image = R.drawable.ic_sun;
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            message = "Good Evening";
            image = R.drawable.ic_night;
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            //message = "Good Night";
            message = "Good Evening";
            image = R.drawable.ic_night;
        } else
            message = "";

        String fName = userModel.getName().toUpperCase();
        String names[] = fName.split(" ");

        if (names.length > 0) {
            message = message + " " + names[0];
        } else {
            message = message + " " + fName;
        }

        usernameTv.setText("" + message);
        sunIv.setImageResource(image);
    }

    private ViaAddresses[] getViaslist() {
        ViaAddresses listofVia[] = null;

        if (locAndFieldArrayList.size() == 3) {
            listofVia = new ViaAddresses[1];
            LocAndField locAndField = locAndFieldArrayList.get(1);
            ViaAddresses viaAddresses = new ViaAddresses();
            viaAddresses.Viaaddress = locAndField.getField();
            viaAddresses.Viatype = locAndField.getLocationType();
            listofVia[0] = viaAddresses;
        }
        if (locAndFieldArrayList.size() == 4) {
            listofVia = new ViaAddresses[2];

            LocAndField locAndField1 = locAndFieldArrayList.get(1);
            ViaAddresses viaAddresses1 = new ViaAddresses();
            viaAddresses1.Viaaddress = locAndField1.getField();
            viaAddresses1.Viatype = locAndField1.getLocationType();
            listofVia[0] = viaAddresses1;


            LocAndField locAndField2 = locAndFieldArrayList.get(1);
            ViaAddresses viaAddresses2 = new ViaAddresses();
            viaAddresses2.Viaaddress = locAndField2.getField();
            viaAddresses2.Viatype = locAndField2.getLocationType();
            listofVia[1] = viaAddresses2;
        }
        return listofVia;
    }

    private static final String JSON_CAR_NAME = "Name";
    private static final String JSON_ONEWAY_FARE = "Fare";
    private static final String JSON_RETURN_FARE = "ReturnFare";
    private static final String JSON_LUGGAGE = "NoOfLuggages";
    private static final String JSON_PASSAENGERS = "NoOfPassengers";
    private static final String JSON_HAND_LUGGAGE = "HandLuggages";
    boolean isValid = false;

    private void callGetFares(boolean isAll) {
        callGetFaresDesktopCloud(isAll);
    }



    private void callGetFaresDesktopCloud(boolean isAll) {
        Model_BookingInformation obj = getAllFaresFromDispatchObject();
        boolean isWebDispatch = sp.getString(CommonVariables.IS_WEB_DISPATCH, "0").equals("1");
        new Manager_GetAllFaresFromDispatchNew(getContext(),isWebDispatch, obj, new Listener_GetAllFaresFromDispatchNew() {
            @Override
            public void onPre(String start) {
                try {
                    isRouteSuccessFull = false;
                    if (makeArcAgain) {
                        try {
                            mapOverlayView.removeRoutes();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    dateTimeTxt.setVisibility(GONE);
                    home_part_1_cv.setVisibility(GONE);
                    home_part_2_cv.setVisibility(GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPost(String response) {

                // greet and meet

                String tempMeetGreet = "0";
                response = checkIfHasNullForString(response);
                if (!response.equals("")) {
                    try {
                        JSONObject parentObject = new JSONObject(response);
                        if (parentObject.getBoolean("HasError")) {
                            if (isActivityInBackground) {
                                return;
                            }

                            sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Try Again");
                            sweetAlertDialog.setContentText(checkIfHasNullForString(parentObject.getString("Message")));
                            sweetAlertDialog.setCancelText(p.getCancel());
                            sweetAlertDialog.setConfirmText("Try Again");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    onAddressChangeListener(true);
                                }

                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                }
                            });
                            sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }

                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                }
                            });
                            sweetAlertDialog.show();

                        }
                        else {

                            JSONObject dataObject = parentObject.optJSONObject("Data");

                            mFareModel.clear();

                            if (dataObject != null) {
                                JSONArray ClsDispatchFareslistObject = dataObject.optJSONArray("ClsDispatchFareslist");
                                if (ClsDispatchFareslistObject != null) {
                                    for (int i = 0; i < ClsDispatchFareslistObject.length(); i++) {
                                        JSONObject obj = ClsDispatchFareslistObject.getJSONObject(i);
                                        if (obj != null) {


                                            setSurgePriceWork(obj.optString("SurgePrice"));

                                            AnyVehicleFareModel faremodel = new AnyVehicleFareModel();

                                            if (model.getPaymentType().toLowerCase().contains("account"))
                                                faremodel.setCompanyPrice(checkIfHasNullForDouble(obj.optString("CompanyPrice")));
                                            faremodel.setExtraCharges(checkIfHasNullForDouble(obj.optString("ExtraCharges")));
                                            faremodel.setAgentFees(checkIfHasNullForDouble(obj.optString("AgentFees")));
                                            faremodel.setAgentCharge(checkIfHasNullForDouble(obj.optString("AgentCharge")));
                                            faremodel.setCongestion(checkIfHasNullForDouble(obj.optString("Congestion")));
                                            faremodel.setParking(checkIfHasNullForDouble(obj.optString("Parking")));
                                            faremodel.setWaiting(checkIfHasNullForDouble(obj.optString("Waiting")));
                                            faremodel.setAgentCommission(checkIfHasNullForDouble(obj.optString("AgentCommission")));
                                            faremodel.setBookingFee(checkIfHasNullForDouble(obj.optString("BookingFee")));
                                            faremodel.setName(checkIfHasNullForString(obj.optString("Name")));
                                            finalPrice = checkIfHasNullForDouble(obj.optString("Fare"));
                                            faremodel.setSingleFare(String.format("%.2f", finalPrice));
                                            faremodel.setIsQuoted(checkIfHasNullForString(obj.optString("IsQuoted")));
                                            faremodel.setReturnFare(checkIfHasNullForString(obj.optString("ReturnFare")));
                                            faremodel.setSuitCase(checkIfHasNullForString(obj.optString("NoOfLuggages")));
                                            faremodel.setTotalPassengers(checkIfHasNullForString(obj.optString("NoOfPassengers")));
                                            faremodel.setHandLuggages(checkIfHasNullForString(obj.optString("HandLuggages")));

                                            if (CurrSelectedCar.equals(faremodel.getName())) {
                                                model.setExtraCharges(faremodel.getExtraCharges());
                                                model.setAgentFees(faremodel.getAgentFees());
                                                model.setAgentCharge(faremodel.getAgentCharge());
                                                model.setCongestion(faremodel.getCongestion());
                                                model.setParking(faremodel.getParking());
                                                model.setWaiting(faremodel.getWaiting());
                                                model.setAgentCommission(faremodel.getAgentCommission());
                                                model.setCompanyPrice(faremodel.getCompanyPrice());
                                                model.setBookingFee(faremodel.getBookingFee());
                                                model.setIsQuoted(faremodel.getIsQuoted());
                                                model.setCar(checkIfHasNullForString(obj.optString("Name")));
                                                model.setReturnFare(checkIfHasNullForString(obj.optString("ReturnFare")));
                                                model.setluggages(checkIfHasNullForString(obj.optString("NoOfLuggages")));
                                                model.setPassengers(checkIfHasNullForString(obj.optString("NoOfPassengers")));
                                                model.setHandluggages(checkIfHasNullForString(obj.optString("HandLuggages")));
                                                model.setOneWayFare("" + checkIfHasNullForDouble(obj.optString("Fare")));

                                                if (finalPrice <= 0 & !sp.getString(Config.ZeroFareText, "").equals("")) {

                                                    ZeroFareTxt.setText(sp.getString(Config.ZeroFareText, ""));
                                                    ZeroFareTxt.setVisibility(VISIBLE);
                                                } else {
                                                    ZeroFareTxt.setVisibility(GONE);
                                                }

                                                if (isValid) {
                                                    if (promList.get(0).getPromoType().toLowerCase().trim().equals("1")) {
                                                        finalPrice -= Double.parseDouble(model.getOneWayFare()) * Double.parseDouble(promList.get(0).getPromoValue()) / 100;
                                                    } else {
                                                        finalPrice = Double.parseDouble(model.getOneWayFare()) - Double.parseDouble(promList.get(0).getPromoValue());
                                                    }

                                                    fare_txt.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (calculateTotalFares(0))));
                                                    fare_txt1.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (checkIfHasNullForDouble(obj.optString("Fare")))));
                                                    fare_txt1.setPaintFlags(fare_txt1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                                    if (finalPrice <= 0) {
                                                        fare_txt.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (calculateTotalFares(0))));
                                                        model.setOneWayFare("" + model.getOneWayFare());
                                                    } else {
                                                        model.setOneWayFare(String.format("%.2f", finalPrice));
                                                        fare_txt1.setVisibility(VISIBLE);
                                                        fare_txt.setTextColor(getResources().getColor(R.color.promo_background));
                                                    }

                                                } else {
                                                    model.setOneWayFare(String.format("%.2f", finalPrice));
                                                    model.setOrignalFares(String.format("%.2f", finalPrice));

                                                    double babySeat = model.getExtraTotal();
                                                    double total = calculateTotalFares(babySeat);
                                                    fare_txt.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (total)));
                                                    estimatedFareIcon.setVisibility(estimatedFareText.equals("") ? GONE : GONE);
                                                }
                                            }

                                            if (faremodel.getName().equalsIgnoreCase("shopping collection")) {
                                                float tempFares = Float.parseFloat(model.getOneWayFare());
                                                shoppingFares = String.format("%.2f", tempFares);
                                                if (openBottomSheet.isChecked() || isShopping) {
                                                    if (isShopping) {
                                                        openBottomSheet.setChecked(true);
                                                        isShopping = false;
                                                    }
                                                    double total = calculateTotalFares(0);
                                                    if (model.getPaymentType().toLowerCase().contains("credit") && !sp.getString(Config.CreditCardSurchargeType, "").equals("")) {
                                                        try {
                                                            if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().equals("amount")) {
                                                                total += Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0"));
                                                                model.setsurchargeAmount(Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")));
                                                            } else if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().contains("percent")) {
                                                                Float surchargedAmount = (Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")) / 100) * Float.parseFloat(shoppingFares);
                                                                total += surchargedAmount;
                                                                model.setsurchargeAmount(surchargedAmount);
                                                            }
                                                        } catch (Exception e) {
                                                        }
                                                    }

                                                    fare_txt.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (total)));

                                                    if (tempFares <= 0 && !sp.getString(Config.ZeroFareText, "").equals("")) {
                                                        ZeroFareTxt.setText(sp.getString(Config.ZeroFareText, ""));
                                                        ZeroFareTxt.setVisibility(VISIBLE);
                                                    } else {
                                                        ZeroFareTxt.setVisibility(GONE);
                                                    }
                                                }
                                            }
                                            int ccount = 0;
                                            for (VehicleModel vm : carNames) {
                                                ++ccount;
                                                faremodel.setServerId(vm.getServerId());
                                                if (vm.getName().equalsIgnoreCase(faremodel.getName())) {
                                                    Log.d(TAG, "onPostExecute: " + faremodel.getName() + " | " + faremodel.getSingleFare());
                                                    mFareModel.add(faremodel);
                                                    break;
                                                }
                                            }

                                            //meet and greet
                                            if (sp.getString(CommonVariables.EnableMeetAndGreet, "0").equals("1")) {
                                                if (i == 0) {
                                                    try {
                                                        tempMeetGreet = obj.getString("IsMeetAndGreet");
                                                        if (tempMeetGreet == null) {
                                                            tempMeetGreet = "0";
                                                        }
                                                    } catch (Exception ex) {
                                                        tempMeetGreet = "0";
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }

                            if (!VerifiedCode.equals("")) {
                                if (isSignupPromoApplied) {
                                    try {
                                        /* JSONObject promoObj = new JSONObject(dataObject.getJSONObject(0).getString("PromotionDetails"));
                                        if (!promoObj.getString("PromotionCode").equals("")) {
                                            sp.edit().putString(Config.SignupPromoString, promoObj.toString()).commit();
                                            //applyDiscount(VerifiedCode);
                                        } */
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            home_part_2_cv.setVisibility(VISIBLE);
                            home_part_1_cv.setVisibility(GONE);

                            isRouteSuccessFull = true;

                            fare_txtlabel.setVisibility(isPostalCodeAvailable ? GONE : GONE);

                            if (fromMarker != null) {
                                fromMarker.remove();
                                fromMarker = null;
                            }

                            model.setExtraTotal(extraTotalBabySeat);
                            model.setCusomerName(userModel.getName());
                            model.setCusomerMobile(userModel.getPhoneNo());
                            model.setCusomerPhone(userModel.getPhoneNo());
                            model.setCusomerEmail(userModel.getEmail());
                            try {
                                model.setFromAddressDoorNO(locAndFieldArrayList.get(0).getDoorNo());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.setFromAddressFlightNo(locAndFieldArrayList.get(0).getFlightNo());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.setFromAddress(locAndFieldArrayList.get(0).getField());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.setFromAddressType(locAndFieldArrayList.get(0).getLocationType());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.setFromAddressCommingFrom(locAndFieldArrayList.get(0).getFromComing());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.setPickupLat(locAndFieldArrayList.get(0).getLat() + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.setPickupLon(locAndFieldArrayList.get(0).getLon() + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.settoAddress(locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getField());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.settoAddressDoorNO(locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getDoorNo());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.settoAddressType(locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getLocationType());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.setDropLat(locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getLat());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                model.setDropLon(locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getLon());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String viaString = "";
                            for (int i = 1; i < locAndFieldArrayList.size() - 1; i++) {
                                viaString += locAndFieldArrayList.get(i).getField() + "||" + ">>>";
                            }
                            model.setViaPointsAsString(viaString);

                            if (!isAll)
                                model.setJournyType(CommonVariables.JOURNY_ONEWAY);

                            if (sp.getString(ShowFares, "1").equals("0")) {
                                ZeroFareTxt.setText(sp.getString(Config.ZeroFareText, ""));
                                ZeroFareTxt.setVisibility(VISIBLE);
                            } else {
                                fare_txt.setVisibility(VISIBLE);
                            }


                            addPromo.setVisibility((sp.getString(Config.ApplyPromotion, "0").equals("1") && sp.getString(ShowFares, "0").equals("1")) ? VISIBLE : GONE);

                            showVehicle(getView());

                            menu_btn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_back__));
                            menu_btn.setVisibility(VISIBLE);

                            dateTimeTxt.setText(model.getPickUpDate() + " " + model.getPickUpTime());

                            if (asaptxt.getText().toString().toLowerCase().contains(p.getAsap().toLowerCase()) || asaptxt.getText().toString().toLowerCase().contains(p.getBookLater().toLowerCase())) {
                                dateTimeTxt.setVisibility(GONE);
                            } else {
                                dateTimeTxt.setVisibility(VISIBLE);
                            }
                            if (confirm_booking.getText().toString().toLowerCase().startsWith("schedu")) {
                                dateTimeTxt.setVisibility(VISIBLE);
                            }
                            if (isFromRebook) {
                                dateTimeTxt.setVisibility(GONE);
                            }

                            afterRouteBottomSheetOfVehicle();


                            try {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (makeArcAgain) {

                                            //do
                                            setMapZoomable(false);
                                            CommonMethods.getInstance()._showCurvedPolyline(getContext(), isRebook, log, locAndFieldArrayList, mMap, mapOverlayView, isrouteComplete);
                                        }
                                    }
                                }, 500);
                            } catch (Exception e) {
                                e.printStackTrace();
                                setMapZoomable(true);
                            }

                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mMap.setMyLocationEnabled(false);
                            currentLocationCv.setVisibility(GONE);
                            ZeroFareTxt.setVisibility((finalPrice <= 0 && !sp.getString(Config.ZeroFareText, "").equals("")) ? VISIBLE : GONE);

                            if (sp.getString(CommonVariables.EnableMeetAndGreet, "0").equals("1")) {
                                try {
                                    //meet and greet
                                    if (isMeetnGreet.equals("1") && tempMeetGreet.equals("0")) {
                                        isMeetnGreet = tempMeetGreet;
                                        new GetExtras(2).execute();
                                    }
                                    isMeetnGreet = tempMeetGreet;
                                    if (isMeetnGreet.equals("1")) {
                                        new GetExtras(1).execute();
                                    }
                                } catch (Exception ex) {

                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        currentLocationCv.setVisibility(VISIBLE);
                        sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText(p.getTryAgain())
                                .setContentText(p.getProblemsgettingData())
                                .setCancelText(p.getCancel())
                                .setConfirmText(p.getTryAgain())
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        onAddressChangeListener(true);
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
                } else {
                    if (isActivityInBackground) {
                        return;
                    }

                    sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText(p.getTryAgain())
                            .setContentText(p.getProblemsgettingData())
                            .setCancelText(p.getCancel())
                            .setConfirmText(p.getTryAgain())
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    onAddressChangeListener(true);
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
            }

        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void setSurgePriceWork(String surgePrice) {
        double SurgePrice = checkIfHasNullForDouble(surgePrice);
        if (SurgePrice == 0) {
            SurgePrice = 1;
        }
        if (SurgePrice > 0 && SurgePrice != 1) {
            model.setSurgePrice(SurgePrice);
            if (peakFactorAlert == null)
                peakFactorAlert = showPeakFactorDialog();
            SurgeFareTxt.setText("- " + model.getSurgePrice() + "x" + " | Peak factor");
            SurgeFareTxt.setVisibility(VISIBLE);
        } else {
            SurgePrice = 1;
            SurgeFareTxt.setVisibility(GONE);
            model.setSurgePrice(SurgePrice);
        }
    }

    public static class BookingResultModel {
        private Model_BookingDetailsModel model;
        private String result;
    }

    double _fares = 0;
    public static double _asap_fares = 0;

    public static String getAndroidVersion() {
        String versionName = "";
        try {
            versionName = "" + com.eurosoft.customerapp.BuildConfig.VERSION_NAME;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    private class SaveBooking extends AsyncTask<Model_BookingDetailsModel, Void, String> {

        private SweetAlertDialog mDialog;
        CardJudoModel receipt;
        private boolean isJudoPay = false;
        private String orgFare = "";

        public SaveBooking() {
            isJudoPay = false;
        }

        public SaveBooking(CardJudoModel receipt) {
            this.receipt = receipt;
            isJudoPay = true;
            if (VerifiedCode != null && !VerifiedCode.equals("")) {
                orgFare = fare_txt1.getText().toString().replace(sp.getString(CurrencySymbol, "\u00A3"), "").replace(" ", "");
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
                mDialog.setTitleText("Saving Booking");
                mDialog.setContentText("Please Wait");
                mDialog.setCancelable(false);
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        String attributeList = "";

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            result = checkIfHasNullForString(result);
            confirmbtn = true;
            if (result.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("HasError")) {
                        if (isActivityInBackground) {
                            return;
                        }
                        if (checkIfHasNullForString(jsonObject.optString("Message")).toLowerCase().startsWith("carderror:")) {
                            try {
                                mHelper.removeLastReciept();
                                mHelper.removeJudoCardModelArrayList();
                                try {
                                    mHelper.saveCardJudoModel(new CardJudoModel());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("")
                                    .setContentText(checkIfHasNullForString(jsonObject.optString("Message").replace("carderror:", "")))
                                    .setCancelText("Cancel")
                                    .setConfirmText("Add Card")
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {

                                            startActivityForResult(new Intent(getActivity(), Activity_HomePayment.class), 4080);
                                            sDialog.dismissWithAnimation();
                                        }

                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                            sweetAlertDialog.dismissWithAnimation();
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
                            sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("")
                                    .setContentText(checkIfHasNullForString(jsonObject.optString("Message")))
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            //                                        new InitializeAppDb(mContext, true).execute();
                                        }

                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                            sweetAlertDialog.dismissWithAnimation();
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
                        }
                    } else {
                        if (jsonObject.optString("TokenValidate").equalsIgnoreCase("ValidToken")) {
                            JSONObject dataObject = jsonObject.optJSONObject("Data");
                            if (dataObject != null) {
                                String JobRefNo = dataObject.optString("JobRefNo");

                                // foreground service setting
                                if (sp.getString(CommonVariables.Enable_ForeGround_Service, "0").equals("1")) {
                                    mContext.startService(new Intent(mContext, Service_NotifyStatus.class));
                                    try {
                                        ((Fragment_Main) getActivity()).bindServiceWithActivity();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                userModel.setPromoCode("");
                                mHelper.putSettingModel(userModel);

                                try {
                                    _fares = mFareModel.get(vehicleSelectedIndex).getFinalFares();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                sp.edit().putString("extraDetails_" + JobRefNo, attributeList).commit();

                                model.setRefrenceNo(JobRefNo);
                                model.setStatus(CommonVariables.STATUS_WAITING);

                                try {
                                    mHelper.saveLocAndFieldForSavedBookings(JobRefNo, locAndFieldArrayList);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                fromDoorNo = "";
                                // promotion work fare set to discounted fare
                                try {
                                    if (VerifiedCode != null && !VerifiedCode.equals("")) {
                                        if (!preValue.equals("")) {
                                            _fares = Double.parseDouble(preValue);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                String paymentType = model.getPaymentType();

                                if (paymentType.equalsIgnoreCase("card in car")) {
                                    paymentType = "Cash";
                                }
                                mDatabaseOperations.insertBooking(
                                        JobRefNo,
                                        model.getPickUpDate(),
                                        model.getPickUpTime(),
                                        model.getReturnDate(),
                                        model.getReturnTime(),
                                        "" + model.getJournyType(),
                                        model.getFromAddressDoorNO(),
                                        model.gettoAddressDoorNO(),
                                        model.getFromAddressFlightNo(),
                                        model.getFromAddressCommingFrom(),
                                        model.getFromAddress(),
                                        model.gettoAddress(),
                                        model.getCusomerName(),
                                        model.getCusomerMobile(),
                                        model.getCusomerPhone(),
                                        model.getCar(),
                                        paymentType,
                                        model.getStatus(),
                                        "" + _fares,
                                        openBottomSheet.isChecked() ? "Shopping Collection" : "0.00",
                                        model.getFromAddressType(),
                                        model.gettoAddressType(),
                                        model.getDropLat(),
                                        model.getDropLon(),
                                        model.getPickupLat(),
                                        model.getPickupLon(),
                                        model.getViaPointsAsString(),
                                        model.getTransactionId(),
                                        model.getPassengers(),
                                        model.getluggages(),
                                        model.getHandluggages()
                                );
                                try {
                                    mHelper.removeExtrasList();
                                    extrasList.clear();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (mDialog != null) {
                                    mDialog.dismiss();
                                }
                                HomeFragment.selectedPos = 0;

                                boolean isAsap = false;

                                if (!confirm_booking.getText().toString().toLowerCase().startsWith("schedul")) {
                                    _asap_fares = _fares;
                                    refNoForAsap = JobRefNo;
                                    mHelper.putRefNoAsapValue(JobRefNo, true);
                                    isAsap = true;
                                } else {
                                    _asap_fares = 0;
                                }
                                Fragment_Tracking tracking = new Fragment_Tracking();
                                Bundle trackArgs = new Bundle();
                                trackArgs.putSerializable(KEY_BOOKING_MODEL, model);
                                trackArgs.putBoolean("is_from_home", true);
                                tracking.setArguments(trackArgs);
                                CommonVariables.AppMainActivity.ShowBookingList(isAsap, trackArgs);
                                getFragmentManager().popBackStack();
                                getFragmentManager().popBackStack();


                            }
                        } else {
                            FBToast.errorToast(mContext, "Invalid Token", FBToast.LENGTH_SHORT);
                            //                                startActivity(new Intent(((Fragment_Main) getActivity()), Activity_Splash.class));
                            //                                ((Fragment_Main) getActivity()).finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                FBToast.errorToast(mContext, "Please check your internet connection and try again", FBToast.LENGTH_SHORT);
            }

            if (mDialog != null)
                mDialog.dismiss();
        }

        @Override
        protected String doInBackground(Model_BookingDetailsModel... params) {
            if (params != null && params.length > 0) {
                String judoDetails = "";

                Model_BookingDetailsModel model = params[0];
                if (model.getPaymentType().equalsIgnoreCase("credit card")) {
                    model.setPaymentType(p.getCreditCard());
                }
                String notes = model.getSpecialNotes();
                String tempNotes = "";
                BookingResultModel resultModel = new BookingResultModel();
                resultModel.model = model;
                try {
                    try {
                        if (model.getPaymentType().equalsIgnoreCase(p.getCreditCard())) {

                            if(IsKonnectPay){
                                model.setPaymentType("Credit Card");
                            }
                            else if (Gateway.contains(Config.Stripe)) {
                                if (sp.getString(Config.EnableCardHold, "").equals("1") && !_CardDetails.equals("")) {
                                    model.setPaymentType("Credit Card"); // for hold payment
                                } else {
                                    try {
                                        if (!model.getTransactionId().equals("")) {
                                            model.setPaymentType("Credit Card(PAID)"); // for don't hold payment
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else if (Gateway.contains(Config.JUDOPAY)) {
                                try {
                                    if (isJudoPay && receipt != null) {
                                        judoDetails = "Token|" + receipt.getToken()
                                                + "<<<consumer|" + receipt.getConsumerReference()
                                                + "<<<consumertoken|" + receipt.getConsumerToken()
                                                + "<<<lastfour|" + receipt.getLastFour()
                                                + "<<<enddate|" + receipt.getEndDate();

                                        if (receipt.getReceiptid() != null && !receipt.getReceiptid().equals("")) {
                                            judoDetails += "<<<receiptid|" + receipt.getReceiptid();
                                        }

                                        model.setTransactionId(judoDetails);

                                        /// NEW JUDO WORK
                                        //  model.setTransactionId(receipt.getRawTokenString());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Gson gson = new Gson();
                    ConfirmedBooking confirmedBooking = new ConfirmedBooking();
                    confirmedBooking.Version = getAndroidVersion();

                    Log.d("TAG", "doInBackground: model.getPaymentType() | " + model.getPaymentType());
                    String pt = model.getPaymentType();
                    if (pt.toLowerCase().startsWith("credit") && !_CardDetails.equals("")) {
                        confirmedBooking.CardDetails = _CardDetails;


                        if(IsKonnectPay){
                            confirmedBooking.IsKonnectPay = "1";
                            confirmedBooking.HoldCardPayment = sp.getString(Config.EnableCardHold, "0");
                        }

                        else if (Gateway.contains(Config.Stripe)){
                            confirmedBooking.IsKonnectPay = "0";
                            if (sp.getString(Config.EnableCardHold, "").equals("1")) {
                                confirmedBooking.CardDetails = "";
                                confirmedBooking.transactionId = stripCardToken;
                                confirmedBooking.StripeCustomerId = stripCustomerId;
                            } else {
                                confirmedBooking.CardDetails = _CardDetails;
                            }
                        }
                        else{
                            confirmedBooking.IsKonnectPay = "0";
                        }

                    }
                    if (extrasList != null && extrasList.size() > 0) {

                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < extrasList.size(); i++) {
                            if (extrasList.get(i).Qty > 0) {
                                try {
                                    JSONObject jsonObject = new JSONObject(gson.toJson(extrasList.get(i)));
                                    jsonArray.put(jsonObject);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        if (jsonArray.length() > 0) {
                            confirmedBooking.AttributesList = jsonArray.toString();
                            attributeList = jsonArray.toString();
                        }
                    }

                    confirmedBooking.pickupDate = model.getPickUpDate();
                    if (model.getPickUpDate().isEmpty()) {
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        Date c = Calendar.getInstance().getTime();
                        String formattedDate = df.format(c);
                        confirmedBooking.pickupDate = formattedDate;
                    }
                    confirmedBooking.pickupTime = model.getPickUpTime();

                    if (confirmedBooking.pickupTime.equals("")) {
                        confirmedBooking.pickupTime = getCurrentTime();
                    }
                    confirmedBooking.CreditCardSurcharge = sp.getString(Config.CreditCardSurcharge, "") + "|" + sp.getString(Config.CreditCardSurchargeType, "");

                    if (confirmedBooking.CreditCardSurcharge.equals("|")) {
                        confirmedBooking.CreditCardSurcharge = confirmedBooking.CreditCardSurcharge.replace("|", "");
                    }
                    if (asaptxt.getText().toString().equalsIgnoreCase("custom")) {
                        confirmedBooking.ASAPBooking = "";
                    }
                    if (asaptxt.getText().toString().equalsIgnoreCase("in 15 mins")) {
                        confirmedBooking.ASAPBooking = "";
                    }
                    if (asaptxt.getText().toString().equalsIgnoreCase("in 30 mins")) {
                        confirmedBooking.ASAPBooking = "";
                    }


                    if (asaptxt.getText().toString().equalsIgnoreCase("ASAP") || asaptxt.getText().toString().equalsIgnoreCase("Book Later")) {
                        confirmedBooking.ASAPBooking = "ASAP,1";
                    }

                    if (dateTimeTxt.getVisibility() == VISIBLE) {
                        confirmedBooking.ASAPBooking = "";
                    }

                    confirmedBooking.SubcompanyId = CommonVariables.SUB_COMPANY;
                    confirmedBooking.PostedFrom = "Android";
                    confirmedBooking.returnDate = model.getReturnDate();
                    confirmedBooking.returnTime = model.getReturnTime();
                    confirmedBooking.fromAddress = model.getFromAddress();
                    confirmedBooking.toAddress = model.gettoAddress();
                    confirmedBooking.customerName = model.getCusomerName();
                    confirmedBooking.customerEmail = model.getCusomerEmail();
//                    confirmedBooking.customerPhoneNo = model.getCusomerPhone();
                    confirmedBooking.customerPhoneNo = "";
                    confirmedBooking.customerMobileNo = model.getCusomerMobile();
                    confirmedBooking.IsQuoted = model.getIsQuoted();

                    confirmedBooking.CustomerId = userModel.getUserServerID();

                    confirmedBooking.fromLatLng = model.getFromLatLng();
                    confirmedBooking.toLatLng = model.getToLatLng();

                    confirmedBooking.ExtraCharges = model.getExtraCharges();
                    confirmedBooking.AgentFees = model.getAgentFees();
                    confirmedBooking.AgentCharge = model.getAgentCharge();
                    confirmedBooking.Congestion = model.getCongestion();
                    confirmedBooking.Parking = model.getParking();
                    confirmedBooking.Waiting = model.getWaiting();
                    confirmedBooking.BookingFee = model.getBookingFee();

                    confirmedBooking.AgentCommission = model.getAgentCommission();
                    confirmedBooking.PaymentType = model.getPaymentType();

                    if (confirmedBooking.PaymentType.equalsIgnoreCase("Account")) {
                        confirmedBooking.CompanyPrice = model.getCompanyPrice();
                    }

                    try {
                        if (VerifiedCode != null && !VerifiedCode.equals("")) {
                            PromotionModel promotionModel = new PromotionModel();
                            promotionModel.PromotionCode = promoModel.getPromoCode();
                            promotionModel.PromotionTitle = promoModel.getTitle();
                            promotionModel.PromotionMessage = promoModel.getMsg();
                            promotionModel.PromotionStartDateTime = promoModel.getStrtDate();
                            promotionModel.PromotionEndDateTime = promoModel.getEndDate();
                            promotionModel.DiscountTypeId = promoModel.getPromoType();
                            promotionModel.Charges = promoModel.getPromoValue();
                            promotionModel.PromotionId = promoModel.getpromoserverId();
                            promotionModel.Totaljourney = promoModel.gettotal();
                            promotionModel.Used = promoModel.getused();
                            promotionModel.PromotionTypeID = promoModel.getPROMOTIONTYPEID();
                            promotionModel.MaximumDiscount = promoModel.getMaxDiscount();
                            promotionModel.MinimumFare = promoModel.getMinFares();
                            confirmedBooking.PromotionDetails = gson.toJson(promotionModel);
                            confirmedBooking.PromotionCode = promoModel.getPromoCode();
                            confirmedBooking.PromotionId = promoModel.getpromoserverId();
                            confirmedBooking.PromotionTypeID = promoModel.getPROMOTIONTYPEID();

                        }

                    } catch (Exception e) {

                    }
                    confirmedBooking.journeyType = CommonVariables.JOURNY_TYPE;
                    confirmedBooking.returnFareRate = "0.0";

                    if (shoppingVehicleLayout.getVisibility() == VISIBLE) {
                        confirmedBooking.specialInstruction = model.getorderSummary() + model.getSpecialNotes();
                        confirmedBooking.fromDoorNo = "Shopping Collection";
                        confirmedBooking.vehicleName = "Shopping Collection";

                    } else {
                        if (VerifiedCode != null && !VerifiedCode.equals("")) {
                            String m = model.getOneWayFare();
                            double mm = Double.parseDouble(m);
                            confirmedBooking.specialInstruction = "Promotion " + promoModel.getPromoCode() + " applied on " + mm + " for the discount of " + promoModel.getPromoValue();
                        } else {
                            confirmedBooking.specialInstruction = model.getSpecialNotes();
                        }

                        try {
                            confirmedBooking.fromDoorNo = confirmedBooking.specialInstruction + "\r\n " + model.getFromAddressType().toLowerCase() == "airport" ? model.getFromAddressFlightNo() : model.getFromAddressDoorNO();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        confirmedBooking.fromDoorNo = fromDoorNo;
                        confirmedBooking.vehicleName = model.getCar();

                        if (VerifiedCode != null && !VerifiedCode.equals("")) {
                            if (preValue.equals("") || preValue.equals("0.00") || preValue.equals("0.0") || preValue.equals("0")) {
                                preValue = firstValueOnly;
                            }
                            confirmedBooking.discountedFare = "" + preValue;

//                            Here i commected work
//                            confirmedBooking.discountedFare = "" + model.getPromoDiscountedValue();
                        } else {
                            confirmedBooking.discountedFare = "";
                        }
                        try {
                            String m = model.getOneWayFare();
                            double mm = Double.parseDouble(m);
                            confirmedBooking.fareRate = String.format("%.2f", mm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    confirmedBooking.fromComing = fromComing;
                    confirmedBooking.viaAddress = model.getViaPointsAsString();
                    confirmedBooking.accountType = "";

                    confirmedBooking.accountId = (model.getPaymentType().equalsIgnoreCase("account")) ? userModel.getAccountWebID() : "0";
                    try {
                        confirmedBooking.fromLocType = locAndFieldArrayList.get(0).getLocationType();
                    } catch (Exception e) {
                        e.printStackTrace();
                        confirmedBooking.fromLocType = "";
                    }
                    try {
                        confirmedBooking.toLocType = locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getLocationType();
                    } catch (Exception e) {
                        e.printStackTrace();
                        confirmedBooking.toLocType = "";
                    }
                    try {
                        if (confirmedBooking.transactionId == null || confirmedBooking.transactionId.isEmpty()) {
                            confirmedBooking.transactionId = model.getTransactionId();
                        }
                    } catch (Exception ex) {

                    }
                    confirmedBooking.key = "";
                    confirmedBooking.miles = "" + journeyMiles;

                    if (confirmedBooking.specialInstruction.trim().length() > 0 && specialInstruction.length() > 0) {
                        confirmedBooking.specialInstruction = confirmedBooking.specialInstruction + "\r " + ", " + specialInstruction + " minutes after flight has landed";
                    } else {
                        if (specialInstruction.length() > 0) {
                            if (confirmedBooking.specialInstruction != null || confirmedBooking.specialInstruction.length() > 0) {
                                confirmedBooking.specialInstruction = confirmedBooking.specialInstruction + "\r " + specialInstruction + " minutes after flight has landed";
                            } else {
                                confirmedBooking.specialInstruction = specialInstruction + " minutes after flight has landed";
                            }
                        }
                    }

                    if (confirmedBooking.PaymentType.equalsIgnoreCase("card in car")) {
                        confirmedBooking.PaymentType = "cash";
                        if (confirmedBooking.specialInstruction.length() == 0) {
                            confirmedBooking.specialInstruction = "Card In Car";
                        } else if (confirmedBooking.specialInstruction.contains("Card In Car, ")) {
                            confirmedBooking.specialInstruction = "Card In Car, " + confirmedBooking.specialInstruction;
                        } else {
                            confirmedBooking.specialInstruction = "Card In Car, " + confirmedBooking.specialInstruction;
                        }
                    }


                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("Token", CommonVariables.TOKEN);
                    String token = AppConstants.getAppConstants().getToken();
                    map.put("Token", token);
                    map.put("uniqueValue", CommonVariables.clientid + "4321orue");
                    map.put("defaultclientId", "" + CommonVariables.clientid);
                    map.put("confirmedBooking", confirmedBooking);
                    String jsonString = gson.toJson(map);
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);


                    Request request = new Request.Builder()
                            .url(CommonVariables.BASE_URL + "ConfirmedBookingNew")
                            .post(body)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        return response.body().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        }
    }

    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = mContext.getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            TextView tvTitle = myContentsView.findViewById(R.id.title);
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = myContentsView.findViewById(R.id.snippet);
            tvSnippet.setVisibility(View.VISIBLE);
            tvSnippet.setText(marker.getSnippet());
            animBounce = AnimationUtils.loadAnimation(mContext.getApplicationContext(), R.anim.bounce);
            animBounce.setRepeatCount(Animation.INFINITE);
            myContentsView.startAnimation(animBounce);
            return myContentsView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    private class GetSignupPromotions extends AsyncTask<String, Void, String> {
        private final String METHOD_NAME = "GetSignupPromotion";
        private static final String KEY_DEFAULT_CLIENT_ID = "defaultclientId";
        private static final String KEY_HASHKEY = "hashKey";
        private static final String Booking_information = "jsonString";
        private static final String HASHKEY_VALUE = "4321orue";

        public GetSignupPromotions() {

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
            if (result != null && !result.isEmpty() && isAdded()) {
                try {
                    JSONObject parentObject = new JSONObject(result);

                    if (parentObject.getBoolean("HasError")) {
                        //FBToast.errorToast(getActivity(), parentObject.getString("Message"), FBToast.LENGTH_SHORT);
                    } else {
                        try {
                            JSONObject parentObject2 = new JSONObject(parentObject.getString("Data"));
                            JSONArray promoObj = new JSONArray(parentObject2.getString("JobPromotionlist"));
                            sp.edit().putBoolean(Config.ISFIRSTSIGNUP, false).commit();
                            sp.edit().putString(Config.SignupPromoString, promoObj.toString()).commit();
                            showDialog(promoObj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String deviceid = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

//            ShareTracking obj = new ShareTracking();
//            obj.defaultClientId = (int) CommonVariables.clientid;
//            obj.uniqueValue = CommonVariables.clientid + HASHKEY_VALUE;
//            obj.UniqueId = deviceid;
//            obj.DeviceInfo = "Android";
//            obj.SubCompanyId = CommonVariables.SUB_COMPANY;
//            obj.CustomerId = userModel.getUserServerID();
//            obj.Email = userModel.getEmail();
//            obj.PromotionTypeId = "1";

            //            Rest Api Work

            HashMap<String, Object> appUserMap = new HashMap<>();
            String promotionJson = "{\"UniqueId\":\"" + deviceid + "\"," +
                    "\"PromotionTypeId\":\"1\"," +
                    "\"DeviceInfo\":\"Android\"," +
                    "\"CustomerId\":\"" + userModel.getUserServerID() + "\"," +
                    "\"defaultClientId\":\"" + CommonVariables.clientid + "\"," +
                    "\"uniqueValue\":\"" + CommonVariables.clientid + HASHKEY_VALUE + "\"," +
                    "\"Email\":\"" + userModel.getEmail() + "\"}";

            appUserMap.put(Booking_information, promotionJson);
            appUserMap.put("Token", CommonVariables.TOKEN);

            Gson gson = new Gson();
            String jsonrequest = gson.toJson(appUserMap);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonrequest);
            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + "GetSignupPromotion")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }


        }
    }

    AlertDialog peakFactorAlert;

    private class GetIdByEmail extends AsyncTask<SettingsModel, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

//        SettingsModel userModel;

        @Override
        protected void onPostExecute(String result1) {
            super.onPostExecute(result1);
            if (result1 != null && !result1.isEmpty()) {
                try {
                    JSONObject parentObject = new JSONObject(result1);

                    if (parentObject.getBoolean("HasError")) {

                    } else {
                        String userId = parentObject.getString("Data");

                        userModel.setUserServerID(userId);
                        new SharedPrefrenceHelper(getActivity()).putSettingModel(userModel);
                    }

                } catch (Exception e) {
                }
            }

        }

        @Override
        protected String doInBackground(SettingsModel... params) {

            userModel = params[0];
            String deviceinfo = "Android - " + android.os.Build.VERSION.RELEASE;
            String jsonString = "{" +
                    "PhoneNo:" + "\"" + userModel.getPhoneNo() + "\"" + "," +
                    "UniqueId:" + "\"" + "" + "\"" + "," +
                    "DeviceInfo:" + "\"" + deviceinfo + "\"" + "," +
                    "UserName:" + "\"" + userModel.getName() + "\"" + "," +

                    "SubCompanyId:" + "\"" + CommonVariables.SUB_COMPANY + "\"" + "," +

                    "Email:" + "\"" + userModel.getEmail() + "\"" + "," +
                    "Passwrd:" + "\"" + userModel.getPassword() + "\"" + "," +
                    "SendSMS:\"0\"" +
                    "}";

            SoapHelper.Builder builder = new SoapHelper.Builder(CommonVariables.SERVICE, getActivity())
                    .setMethodName("GetCustomerIdByEmail", true)
                    .addProperty("defaultClientId", CommonVariables.clientid, PropertyInfo.LONG_CLASS)
                    .addProperty("jsonString", jsonString, PropertyInfo.STRING_CLASS)
                    .addProperty("uniqueValue", CommonVariables.clientid + "4321orue", PropertyInfo.STRING_CLASS);

            try {
                return builder.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }



           /* OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + "GetCustomerIdByEmail")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }*/


            return null;
        }
    }

    private class GetExtras extends AsyncTask<String, Void, String> {
        private String METHOD_NAME = "GetExtraChargesList";


        private static final String KEY_DEFAULT_CLIENT_ID = "defaultclientId";
        private static final String KEY_HASHKEY = "hashKey";

        private static final String Booking_information = "jsonString";
        private static final String HASHKEY_VALUE = "4321orue";

        private SweetAlertDialog mDialog;

        private int isMeetGreetCall = 0;
        private boolean isButtonClick = false;

        public GetExtras(boolean isButtonClick) {
            this.isButtonClick = isButtonClick;
        }

        public GetExtras(int isMeetGreetCall) {
            this.isMeetGreetCall = isMeetGreetCall;
        }

        String token = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                mDialog.setTitleText(p.getGettinDetails());
                mDialog.setContentText(p.getPleaseWait());
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
                try {
                    JSONObject parentObject = new JSONObject(result);

                    if (parentObject.getBoolean("HasError")) {
                        //showDrvNotes();
                        FBToast.errorToast(getActivity(), parentObject.getString("Message"), FBToast.LENGTH_SHORT);
                    } else {

                        if (!sp.getString(CommonVariables.EnableMeetAndGreet, "0").equals("1")) {
                            processExtraCharges(parentObject);
                        } else {
                            JSONObject ExtraArr2 = new JSONObject(parentObject.getString("Data"));
                            JSONArray ExtraArr = new JSONArray(ExtraArr2.getString("attributesList"));

                            if (ExtraArr.length() <= 0) {
                                if (mDialog != null) {
                                    mDialog.dismiss();
                                }
                                if (isButtonClick) {
                                    showDrvNotes();
                                }
                                return;
                            }

                            if (extrasList != null) {
                                extrasList.clear();
                            } else {
                                extrasList = new ArrayList<>();
                            }
                            Gson gson = new Gson();
                            String extraStr = "";
                            if (isMeetGreetCall == 1 && isMeetnGreet.equals("1")) {

                                Float price = 0f;
                                for (int i = 0; i < ExtraArr.length(); i++) {
                                    ExtrasModel extrasModelTemp = gson.fromJson(ExtraArr.getJSONObject(i).toString(), ExtrasModel.class);

                                    if ((isMeetGreetCall == 1 || isMeetGreetCall == 2) && (extrasModelTemp.Name.toLowerCase().contains("meet and greet") || extrasModelTemp.Name.toLowerCase().contains("meet & greet"))) {

                                        extrasModelTemp.Qty = isMeetGreetCall == 1 ? 1 : 0;
                                        price = extrasModelTemp.ChargesPerQty * extrasModelTemp.Qty;
                                    }
                                }

                                processExtraCharges(isButtonClick, isMeetGreetCall, ExtraArr);

                            } else {
                                processExtraCharges(isButtonClick, isMeetGreetCall, ExtraArr);
                            }
                        }

                    }
                } catch (Exception e) {
                    if (isButtonClick) {
                        showDrvNotes();
                    }
                }
            } else {
                if (isButtonClick) {
                    showDrvNotes();
                }
            }
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String deviceid = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

            HashMap<String, Object> getextramap = new HashMap<>();
            HashMap<String, Object> appMap = new HashMap<>();
//                    appUserMap.put("Token", CommonVariables.TOKEN);
            String token = AppConstants.getAppConstants().getToken();
            appMap.put("defaultClientId", CommonVariables.clientid);
            appMap.put("UniqueId", deviceid);
            appMap.put("DeviceInfo", "Android");
            appMap.put("Email", userModel.getEmail());
            appMap.put("uniqueValue", CommonVariables.clientid + HASHKEY_VALUE);
            appMap.put("CustomerId", userModel.getUserServerID());


            getextramap.put("request", appMap);
            getextramap.put("Token", token);

            String _jsonString = new Gson().toJson(getextramap);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), _jsonString);
            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + "GetExtraChargesList")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

        }
    }

    public void processExtraCharges(JSONObject parentObject) {
        try {
            JSONArray ExtraArr = new JSONArray(parentObject.getString("Data"));
            if (ExtraArr.length() <= 0) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                showDrvNotes();
                return;
            }
            if (extrasList != null) {
                extrasList.clear();
            } else {
                extrasList = new ArrayList<>();
            }
            Gson gson = new Gson();
            String extraStr = "";
            for (int i = 0; i < ExtraArr.length(); i++) {
                ExtrasModel extrasModel = gson.fromJson(ExtraArr.getJSONObject(i).toString(), ExtrasModel.class);
                ExtrasModel extrasModel1 = gson.fromJson(ExtraArr.getJSONObject(i).toString(), ExtrasModel.class);
                extrasList.add(extrasModel);
            }
            mHelper.putExtraList(extrasList);

            if (extrasList.size() > 0) {
                double total = mFareModel.get(selectedPos).getFinalFares();
                if (openBottomSheet.isChecked()) {
                    total = Float.parseFloat(shoppingFares);
                }
                if (model.getPaymentType().toLowerCase().contains("credit") && !sp.getString(Config.CreditCardSurchargeType, "").equals("")) {
                    try {
                        if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().equals("amount")) {
                            total += Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0"));
                            model.setsurchargeAmount(Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")));
                        } else if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().contains("percent")) {
                            Float surchargedAmount = (Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")) / 100) * Float.parseFloat(shoppingFares);
                            total += surchargedAmount;
                            model.setsurchargeAmount(surchargedAmount);
                        }
                    } catch (Exception e) {
                    }
                }
                startActivityForResult(new Intent(getActivity(), Activity_AddExtras.class).putExtra("journeyCharges", total).putExtra("drvNotes", model.getSpecialNotes()), 989);
            } else {
                showDrvNotes();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showDrvNotes();
        }
    }

    public void processExtraCharges(boolean isButtonClick, int isMeetGreetCall, JSONArray ExtraArr) {
        try {
            ArrayList<ExtrasModel> tempExtraList = new SharedPrefrenceHelper(getActivity()).getExtraList();
            Gson gson = new Gson();

            for (int i = 0; i < ExtraArr.length(); i++) {
                ExtrasModel extrasModel = gson.fromJson(ExtraArr.getJSONObject(i).toString(), ExtrasModel.class);

                //meet and greet start
                if ((isMeetGreetCall == 1 || isMeetGreetCall == 2) && (extrasModel.Name.toLowerCase().contains("meet and greet") || extrasModel.Name.toLowerCase().contains("meet & greet"))) {
                    if (isMeetnGreet.equals("1")) {
                        extrasModel.Qty = isMeetGreetCall == 1 ? 1 : 0;
                    } else {
                        extrasModel.Qty = 0;
                    }

                    extrasModel.Price = extrasModel.ChargesPerQty * extrasModel.Qty;


//// meet and greet
                    if (model.getExtraTotal() > extrasModel.ChargesPerQty && isMeetGreetCall == 2) {
                        double extratotal1 = model.getExtraTotal() - extrasModel.Price;
                        model.setExtraTotal(extratotal1);


                    } else {
                        model.setExtraTotal(extrasModel.Price);
                    }
                    double total = calculateTotalFares(model.getExtraTotal());

                    if (model.getPaymentType().toLowerCase().contains("credit") && !sp.getString(Config.CreditCardSurchargeType, "").equals("")) {
                        try {
                            if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().equals("amount")) {
                                total += Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0"));
                                model.setsurchargeAmount(Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")));
                            } else if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().contains("percent")) {
                                String Fares = openBottomSheet.isChecked() ? shoppingFares : model.getOneWayFare();
                                Float surchargedAmount = (Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")) / 100) * Float.parseFloat(Fares);
                                total += surchargedAmount;
                                model.setsurchargeAmount(surchargedAmount);
                            }
                        } catch (Exception e) {
                        }
                    }
                    fare_txt.setText("\u00A3" + String.format("%.2f", total));


                } else if ((isMeetGreetCall == 1 || isMeetGreetCall == 2) && tempExtraList != null) {
                    for (int j = 0; j < tempExtraList.size(); j++) {
                        if (extrasModel.Name.equals(tempExtraList.get(j).Name)) {
                            if (tempExtraList.get(j).Qty > extrasModel.Qty) {
                                extrasModel.Qty = tempExtraList.get(j).Qty;
                                extrasModel.Price = tempExtraList.get(j).Price;
                            }
                        }
                    }

                }
//meet and greet end
                extrasList.add(extrasModel);
            }

            mHelper.putExtraList(extrasList);
            updateFareAfterGreetAndMeet();
            //MEET AND GREET
            if (extrasList.size() > 0) {

                if (isMeetGreetCall == 0) {
                    double total = mFareModel.get(selectedPos).getFinalFares();
                    if (openBottomSheet.isChecked()) {
                        total = Float.parseFloat(shoppingFares);
                    }
                    if (model.getPaymentType().toLowerCase().contains("credit") && !sp.getString(Config.CreditCardSurchargeType, "").equals("")) {
                        try {
                            if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().equals("amount")) {
                                total += Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0"));
                                model.setsurchargeAmount(Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")));
                            } else if (sp.getString(Config.CreditCardSurchargeType, "").toLowerCase().contains("percent")) {
                                Float surchargedAmount = (Float.parseFloat(sp.getString(Config.CreditCardSurcharge, "0")) / 100) * Float.parseFloat(shoppingFares);
                                total += surchargedAmount;
                                model.setsurchargeAmount(surchargedAmount);
                            }
                        } catch (Exception e) {
                        }
                    }


                    if (sp.getString(CommonVariables.EnableMeetAndGreet, "0").equals("1")) {
                        //meet and greet
                        startActivityForResult(new Intent(getActivity(), Activity_AddExtras.class)
                                .putExtra("journeyCharges", total)
                                .putExtra("isMeetGreet", isMeetnGreet.equals("1"))
                                .putExtra("drvNotes", model.getSpecialNotes()), 989);
                    } else {
                        startActivityForResult(new Intent(getActivity(), Activity_AddExtras.class).putExtra("journeyCharges", total).putExtra("drvNotes", model.getSpecialNotes()), 989);

                    }


                }


            } else {
                if (isButtonClick) {
                    showDrvNotes();
                } else {

                }
            }

        } catch (Exception e) {
            if (isButtonClick) {
                showDrvNotes();
            }
        }
    }

    public void updateFareAfterGreetAndMeet() {

//        if (isMeetnGreet.equals("1")) {
//            for (AnyVehicleFareModel vechFare : mFareModel) {
//
//              //  vechFare.setExtraCharges(vechFare.getExtraCharges() + model.getExtraTotal());
//            }

        showVehicle(getView());
        //}
    }

    private class SaveCardReciept extends AsyncTask<CardJudoModel, Void, String> {
        private SweetAlertDialog mDialog;
        CardJudoModel receipt;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
                mDialog.setTitleText(p.getSavingCardDetails());
                mDialog.setContentText(p.getPleaseWait());
                mDialog.setCancelable(false);
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(CardJudoModel... strings) {

            SharedPrefrenceHelper spHelper = new SharedPrefrenceHelper(mContext);
//            SettingsModel settingsModel = spHelper.getSettingModel();
            receipt = strings[0];
            String consumerDetails =
                    "Token|" + receipt.getToken() +
                            "<<<consumer|" + receipt.getConsumerReference() +
                            "<<<consumertoken|" + receipt.getConsumerToken() +
                            "<<<lastfour|" + receipt.getLastFour() +
                            "<<<enddate|" + receipt.getEndDate() +
                            "<<<receiptid|" + receipt.getReceiptid();

            String jsonString = "{" +
                    "PhoneNo:" + "\"" + "" + "\"" + "," +
                    "UserName:" + "\"" + "" + "\"" + "," +
                    "Passwrd:" + "\"" + userModel.getPassword() + "\"" + "," +
                    "Email:" + "\"" + userModel.getEmail().trim() + "\"" + "," +
                    "TokenDetails:" + "\"" + consumerDetails + "\"" + "," +
                    "PickDetails:\"yes\"" +
                    "}";

         /*   SoapHelper.Builder builder = new SoapHelper.Builder(CommonVariables.SERVICE, getActivity())
                    .setMethodName("UpdateAppUser", true)
                    .addProperty("defaultClientId", CommonVariables.clientid, PropertyInfo.LONG_CLASS)
                    .addProperty("jsonString", jsonString, PropertyInfo.STRING_CLASS)
                    .addProperty("uniqueValue", CommonVariables.clientid + "4321orue", PropertyInfo.STRING_CLASS);

            try {
                return builder.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }
            */

            HashMap<String, Object> appUserMap = new HashMap<>();
//            appUserMap.put("Token", CommonVariables.TOKEN);
            String token = AppConstants.getAppConstants().getToken();
            appUserMap.put("Token", token);
            appUserMap.put("defaultClientId", CommonVariables.clientid);
            appUserMap.put("jsonString", jsonString);
            appUserMap.put("uniqueValue", CommonVariables.clientid + "4321orue");

            String _jsonString = new Gson().toJson(appUserMap);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), _jsonString);
            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + "UpdateAppUser")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && receipt != null) {
                try {
                    JSONObject parentObject = new JSONObject(result);
                    if (parentObject.getBoolean("HasError")) {
                        if (isActivityInBackground) {
                            return;
                        }
                        sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText(p.getError())
                                .setContentText(parentObject.getString("Message"))

                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        //										new InitializeAppDb(mContext, true).execute();
                                    }

                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        //										mContext.finish();
                                    }

                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                    }
                                })
                                .show();
                    } else {
                        CardJudoModel cardJudoModel = new CardJudoModel();
                        cardJudoModel.setToken(receipt.getToken());
                        cardJudoModel.setLastFour(receipt.getLastFour());
                        cardJudoModel.setEndDate(receipt.getEndDate());
                        cardJudoModel.setConsumerReference(receipt.getConsumerReference());
                        cardJudoModel.setConsumerToken(receipt.getConsumerToken());
                        cardJudoModel.setType(receipt.getType());
                        cardJudoModel.set3DS(receipt.get3ds());
                        cardJudoModel.setReceiptid(receipt.getReceiptid());
                        cardJudoModel.setRawTokenString(receipt.getRawTokenString());
                        new SharedPrefrenceHelper(mContext).saveCardJudoModel(cardJudoModel);
                        FBToast.successToast(mContext, parentObject.getString("Message"), FBToast.LENGTH_SHORT);
                        SaveBooking();
                    }
                } catch (Exception e) {
                    FBToast.errorToast(mContext, "Please check your internet connection and try again!", FBToast.LENGTH_SHORT);
                }
            } else {
                FBToast.errorToast(mContext, "Please check your internet connection and try again!", FBToast.LENGTH_SHORT);
            }
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }
    }

    private class VerifyPromotionCode extends AsyncTask<String, Void, String> {
        private final String METHOD_NAME = "VerifyPromotion";


        private static final String KEY_DEFAULT_CLIENT_ID = "defaultclientId";
        private static final String KEY_HASHKEY = "hashKey";

        private static final String Booking_information = "jsonString";
        private static final String HASHKEY_VALUE = "4321orue";

        private SweetAlertDialog mDialog;


        public VerifyPromotionCode() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                mDialog.setTitleText(p.getVerifyingCode());
                mDialog.setContentText(p.getPleaseWait());
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
                try {
                    JSONObject parentObject = new JSONObject(result);

                    if (parentObject.getBoolean("HasError")) {
                        FBToast.errorToast(getActivity(), parentObject.getString("Message"), FBToast.LENGTH_SHORT);
                    } else {
                        JSONObject promoObj = new JSONArray(parentObject.getString("Data")).getJSONObject(0);

                        SharedPrefrenceHelper spHelper = new SharedPrefrenceHelper(mContext);
//                        SettingsModel settingsModel = spHelper.getSettingModel();
                        userModel.setPromoCode(promoObj.getString("PromotionCode"));
                        spHelper.putSettingModel(userModel);
                        sp.edit().putString(Config.SignupPromoString, promoObj.toString()).commit();
                        isSignupPromoApplied = true;
                        VerifiedCode = promoObj.getString("PromotionCode");
                        FBToast.successToast(mContext, "Promotion Code [" + promoObj.getString("PromotionCode") + "] is applied", FBToast.LENGTH_LONG);

                    }
                } catch (Exception e) {
                    sp.edit().putString(Config.SignupPromoString, "").commit();
                    isSignupPromoApplied = false;
                    FBToast.errorToast(getActivity(), "Invalid Promo Code", FBToast.LENGTH_SHORT);
                }
            } else {
                sp.edit().putString(Config.SignupPromoString, "").commit();
                isSignupPromoApplied = false;
                FBToast.errorToast(getActivity(), "Please check your internet connection and try again later", FBToast.LENGTH_SHORT);
            }

            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            Gson gson = new Gson();

            String deviceid = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

//            ShareTracking obj = new ShareTracking();
//            obj.defaultClientId = (int) CommonVariables.clientid;
//            obj.uniqueValue = CommonVariables.clientid + HASHKEY_VALUE;
//            obj.UniqueId = deviceid;
//            obj.SubCompanyId = CommonVariables.SUB_COMPANY;
//            obj.DeviceInfo = "Android";
//            obj.CustomerId = userModel.getUserServerID();
//            obj.Email = userModel.getEmail();
//            obj.PromotionCode = params[0];
//            obj.FromAddress = "";
//            obj.FromType = "";
//            obj.ToAddress = "";
//            obj.ToType = "";

            ConfirmedBooking verifypromotion = new ConfirmedBooking();

            verifypromotion.ASAPBooking = "";

            verifypromotion.CardDetails = "";
            verifypromotion.PaymentType = userModel.getPaymentType();
            verifypromotion.accountId = (model.getPaymentType().equalsIgnoreCase("account")) ? userModel.getAccountWebID() : "0";
            verifypromotion.accountType = "";

            verifypromotion.PostedFrom = CommonVariables.DEVICE_TYPE;
            verifypromotion.Version = getAndroidVersion();
            ;
            verifypromotion.defaultClientId = (int) CommonVariables.clientid;
            verifypromotion.uniqueValue = CommonVariables.clientid + HASHKEY_VALUE;
            verifypromotion.DeviceInfo = CommonVariables.DEVICE_TYPE;
            verifypromotion.UniqueId = deviceid;

            verifypromotion.pickupDate = "";
            verifypromotion.pickupTime = "";
            verifypromotion.returnDate = "";
            verifypromotion.returnTime = "";

            verifypromotion.Email = userModel.getEmail();
            verifypromotion.CustomerId = userModel.getUserServerID();
            verifypromotion.customerEmail = userModel.getEmail();
            verifypromotion.customerMobileNo = userModel.getMobile();
            verifypromotion.customerName = userModel.getName();
            verifypromotion.customerPhoneNo = "";

            verifypromotion.AgentCharge = 0.0;
            verifypromotion.AgentCommission = 0.0;
            verifypromotion.AgentFees = 0.0;
            verifypromotion.Congestion = 0.0;
            verifypromotion.ExtraCharges = 0.0;
            verifypromotion.Parking = 0.0;
            verifypromotion.Waiting = 0.0;
            verifypromotion.CompanyPrice = 0.0;
            verifypromotion.IsQuoted = "0";

            verifypromotion.returnFareRate = "0.0";

            verifypromotion.PromotionCode = params[0];
            verifypromotion.promocode = params[0];

            verifypromotion.fromAddress = "";
            verifypromotion.fromComing = "";
            verifypromotion.fromDoorNo = "";
            verifypromotion.fromLocType = "";
            verifypromotion.journeyType = CommonVariables.JOURNY_TYPE;
            verifypromotion.key = "";
            verifypromotion.toLocType = "";
            verifypromotion.toAddress = "toAddress";

            verifypromotion.vehicleName = "";
            verifypromotion.specialInstruction = "";


            HashMap<String, Object> map = new HashMap<>();
//                    map.put("Token", CommonVariables.TOKEN);
            String token = AppConstants.getAppConstants().getToken();
            map.put("jsonString", verifypromotion);
            map.put("Token", token);
            String jsonString = gson.toJson(map);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);


            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + "VerifyPromotion")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    int vehicleSelectedIndex = 0;
    MotionLayout lastSlectedLayout = null;
    LinearLayout LastSelectedDescription = null;
    // RelativeLayout lastPassen = null;
    //RelativeLayout lastPassen1 = null;
    // TextView lastTvCarEta = null;
    TextView lastSlectedFareTv = null;


    private class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.MyViewHolder> {
        private int height = 0;
        private final Context context;
        private final ArrayList<AnyVehicleFareModel> arrayList;
        private int selectedPosition;
        private boolean checkItemClick = true;

        public VehicleListAdapter(Context context, ArrayList<AnyVehicleFareModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.layout_adapter_item_car_new, parent, false);
            try {
                contactView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                height = contactView.getMeasuredHeight();
                if (arrayList.size() > 3) {
                    height = height * 2;
                    height = height + 70;
                } else if (arrayList.size() == 1) {
                    height = height * 2;
                } else if (arrayList.size() >= 2) {
                    height = height * 4;
                } else {
                    height = height * arrayList.size();
                }

                height += viewBarLl.getHeight();
                sheetBehavior.setPeekHeight(height);
                home_part_2_cv.post(new Runnable() {
                    @Override
                    public void run() {
                        int _height = home_part_2_cv.getHeight();
                        _height = _height + height;
                        mapViewRl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

                        //CODE FOR ADD MARGINS
                        RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) mapViewRl.getLayoutParams();
                        relativeParams.bottomMargin = _height;
                        mapViewRl.setLayoutParams(relativeParams);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "showVehicle: " + e.getMessage());
            }

            return new MyViewHolder(contactView);
        }

        boolean isOnProgress = false;

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            AnyVehicleFareModel carNames = arrayList.get(holder.getAdapterPosition());

            if (selectedPosition == position) {
                //  setEtaForVehicle(carNames.getName(), holder.tvCarEta);
                lastSlectedLayout = holder.rvVehicleRoot;
                //  holder.tvCarEta.setVisibility(VISIBLE);
                //        holder.passen1.setVisibility(VISIBLE);
                //   holder.passen.setVisibility(GONE);
                //   lastPassen = holder.passen;
                //   lastPassen1 = holder.passen1;
                //   lastTvCarEta = holder.tvCarEta;
                LastSelectedDescription = holder.description;
                lastSlectedFareTv = holder.carFaresTv;
                // LastSelectedDescription.setOrientation(LinearLayout.VERTICAL);
                selectedPosition = position;
                holder.rvVehicleRoot.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.vechicle_item_selected_background));
                TransitionManager.beginDelayedTransition(holder.rvVehicleRoot);
                holder.carFaresTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_footer__white_inverse));
                holder.rvVehicleRoot.transitionToState(R.id.end);
                isOnProgress = false;
            } else {
                //  holder.tvCarEta.setVisibility(GONE);
                //   holder.passen.setVisibility(VISIBLE);
                // holder.passen1.setVisibility(GONE);

                holder.rvVehicleRoot.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.vechicle_item_unselected_background));
                TransitionManager.beginDelayedTransition(holder.rvVehicleRoot);
                holder.rvVehicleRoot.transitionToState(R.id.start);
                holder.carFaresTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_black_inverse));
                // holder.rvVehicleRoot.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.color_light_gray_inverse));

            }

//            holder.rvVehicleRoot.setBackground(carNames.isCurrentSelect() ? R.drawable.vechicle_item_selected_background : R.color.color_light_gray_inverse );
//            holder.viewBg.setVisibility(carNames.isCurrentSelect() ? View.VISIBLE : GONE);

            if (carNames.getName().equalsIgnoreCase("saloon")) {
                setVehicle(holder, R.drawable.saloon);
            } else if (carNames.getName().equalsIgnoreCase("estate")) {
                setVehicle(holder, R.drawable.saloon);

            } else if (carNames.getName().contains("wheelchair")) {
                setVehicle(holder, R.drawable.wheelchair);

            } else if (carNames.getName().contains("9") ||
                    carNames.getName().contains("8") ||
                    carNames.getName().contains("mini bus") ||
                    carNames.getName().equalsIgnoreCase("7 Seater") ||
                    carNames.getName().contains("Coach") ||
                    carNames.getName().contains("bus")) {
                setVehicle(holder, R.drawable.minibus);

            } else if (carNames.getName().contains("black cab") ||
                    carNames.getName().contains("MPV") ||
                    carNames.getName().equalsIgnoreCase("6 Seater") ||
                    carNames.getName().contains("5")) {
                setVehicle(holder, R.drawable.mpv);

            } else {
                setVehicle(holder, R.drawable.executive);
            }

            Log.d(TAG, "onBindViewHolder: ZeroFareText= " + sp.getString(Config.ZeroFareText, ""));

            holder.fareRl.setVisibility((sp.getString(ShowFares, "1").equals("0")) ? GONE : VISIBLE);

            if (!isRemoveDiscountIsTrue) {
                double ddddfff = getDiscountedFares(pr_code, carNames.getFinalFares());
                String fare = sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", ddddfff);
                holder.carFaresTv.setText("" + fare);

                // Strike
                holder.fare_txt2.setVisibility(VISIBLE);
                holder.fare_txt2.setPaintFlags(holder.fare_txt2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.fare_txt2.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", carNames.getFinalFares()));

                if (position == 0) {
                    firstValueOnly = String.format("%.2f", getDiscountedFares(pr_code, Double.parseDouble(carNames.getSingleFare())));
//                    preValue = String.format("%.2f", getDiscountedFares(pr_code, Double.parseDouble(carNames.getSingleFare())));
//                    model.setPromoDiscountedValue(Double.parseDouble(preValue));
                }
            } else {
                holder.fare_txt2.setVisibility(GONE);
                holder.carFaresTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (carNames.getFinalFares())));
            }

            try {

                String[] words = carNames.getName().toLowerCase().split(" ");
                String carNamesCap = "";
                for (String s : words) {
                    carNamesCap = carNamesCap + " " + capitizeString(s);
                }


                holder.carNameTv.setText(carNamesCap);
            } catch (Exception ex) {
                holder.carNameTv.setText(carNames.getName());
            }


            holder.carPassTv.setText("x" + carNames.getTotalPassengers());
            // holder.carPassTv1.setText("x" + carNames.getTotalPassengers());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    if (isOnProgress) {
                        return;
                    }
//                        setSelection(position);
//                        click(holder.getAbsoluteAdapterPosition(),carNames);
                    if (selectedPosition == position) {
                        return;
                    }
                    isOnProgress = true;
                    selectedPosition = position;

                    if (!isRemoveDiscountIsTrue) {
                        preValue = String.format("%.2f", getDiscountedFares(pr_code, Double.parseDouble(carNames.getSingleFare())));
                    } else {
                        preValue = String.format("%.2f", Double.parseDouble(carNames.getSingleFare()));
                    }
                    try {
                        model.setPromoDiscountedValue(Double.parseDouble(preValue));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        model.setOneWayFare(arrayList.get(position).getSingleFare());
                        model.setExtraCharges(arrayList.get(position).getExtraCharges());
                        model.setAgentFees(arrayList.get(position).getAgentFees());
                        model.setAgentCharge(arrayList.get(position).getAgentCharge());
                        model.setCongestion(arrayList.get(position).getCongestion());
                        model.setParking(arrayList.get(position).getParking());
                        model.setWaiting(arrayList.get(position).getWaiting());
                        model.setAgentCommission(arrayList.get(position).getAgentCommission());
                        model.setCompanyPrice(arrayList.get(position).getCompanyPrice());
                        model.setBookingFee(arrayList.get(position).getBookingFee());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    vehicleSelectedIndex = position;
                    selectedCar = carNames.getName();
                    selectedPos = holder.getAdapterPosition();
                    //  setSelection(holder.getAdapterPosition());
                    model.setCar(carNames.getName());
                    if (handle != null) {//replace driveLoc
                        Log.d(TAG, "run: run: run: 6");

                        handle.removeCallbacks(m_runnable);
                        handle.post(m_runnable);
                    }
                    lastSlectedFareTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_black_inverse));
                    lastSlectedLayout.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.vechicle_item_unselected_background));
                    TransitionManager.beginDelayedTransition(lastSlectedLayout);
                    lastSlectedLayout.transitionToState(R.id.start);
                    holder.itemView.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            //lastPassen.setVisibility(VISIBLE);
                            //  lastPassen1.setVisibility(GONE);
                            //    lastTvCarEta.setVisibility(GONE);
                            isOnProgress = false;
                        }
                    }, 300);
                    //   LastSelectedDescription.setOrientation(LinearLayout.VERTICAL);
                    holder.carFaresTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_footer__white_inverse));
                    holder.rvVehicleRoot.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.vechicle_item_selected_background));
                    TransitionManager.beginDelayedTransition(holder.rvVehicleRoot);
                    holder.rvVehicleRoot.transitionToState(R.id.end);
                    //setEtaForVehicle(carNames.getName(), holder.tvCarEta);

                    holder.itemView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //holder.passen.setVisibility(GONE);
                            //  holder.passen1.setVisibility(VISIBLE);
                            // holder.tvCarEta.setVisibility(VISIBLE);

                            lastSlectedLayout = holder.rvVehicleRoot;
                            arrayList.get(selectedPosition).setCurrentSelect(false);
                            carNames.setCurrentSelect(true);
                            selectedPosition = position;
                            //lastPassen = holder.passen;
                            //    lastPassen1 = holder.passen1;
                            //  lastTvCarEta = holder.tvCarEta;
                            lastSlectedFareTv = holder.carFaresTv;
                            isOnProgress = false;
                        }
                    }, 300);

                }
            });
        }


        public void click(int position, AnyVehicleFareModel carNames) {
            selectedPosition = position;

            if (!isRemoveDiscountIsTrue) {
                preValue = String.format("%.2f", getDiscountedFares(pr_code, Double.parseDouble(carNames.getSingleFare())));
            } else {
                preValue = String.format("%.2f", Double.parseDouble(carNames.getSingleFare()));
            }
            try {
                model.setPromoDiscountedValue(Double.parseDouble(preValue));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                model.setOneWayFare(arrayList.get(position).getSingleFare());
                model.setExtraCharges(arrayList.get(position).getExtraCharges());
                model.setAgentFees(arrayList.get(position).getAgentFees());
                model.setAgentCharge(arrayList.get(position).getAgentCharge());
                model.setCongestion(arrayList.get(position).getCongestion());
                model.setParking(arrayList.get(position).getParking());
                model.setWaiting(arrayList.get(position).getWaiting());
                model.setAgentCommission(arrayList.get(position).getAgentCommission());
                model.setCompanyPrice(arrayList.get(position).getCompanyPrice());
                model.setBookingFee(arrayList.get(position).getBookingFee());
            } catch (Exception e) {
                e.printStackTrace();
            }

            vehicleSelectedIndex = position;
            selectedCar = carNames.getName();
            selectedPos = position;

            model.setCar(carNames.getName());
            if (handle != null) {//replace driveLoc
                Log.d(TAG, "run: run: run: 6");
                handle.removeCallbacks(m_runnable);
                handle.post(m_runnable);
            }
        }


        private String capitizeString(String name) {
            String captilizedString = "";
            if (!name.trim().equals("")) {
                captilizedString = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
            return captilizedString;
        }

        public String getFares() {
            return "" + arrayList.get(selectedPos).getFinalFares();
        }

        private void setSelection(int pos) {


            for (int i = 0; i < arrayList.size(); i++)
                arrayList.get(i).setCurrentSelect((i == pos) ? true : false);

            notifyDataSetChanged();
        }

        public void setVehicle(MyViewHolder holder, int vehicleId) {
            holder.carIv.setImageDrawable(context.getResources().getDrawable(vehicleId));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView fare_txt2;
            private View viewBg;
            private ImageView carIv;
            private TextView carNameTv;
            private TextView carPassTv;
            // private TextView carPassTv1;
            public TextView carFaresTv;
            // public TextView tvCarEta;
            private RelativeLayout fareRl;
            private MotionLayout rvVehicleRoot;
            private CardView cardView;
            private LinearLayout description;
            // private RelativeLayout passen1;
            private RelativeLayout passen;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                // cardView = itemView.findViewById(R.id.cardView);
                //viewBg = itemView.findViewById(R.id.viewBg);
                carIv = itemView.findViewById(R.id.carIv);

                //  tvCarEta = itemView.findViewById(R.id.tvCarEta);

                carNameTv = itemView.findViewById(R.id.carNameTv);

                carPassTv = itemView.findViewById(R.id.carPassTv);
                //carPassTv1 = itemView.findViewById(R.id.carPassTv1);


                fareRl = itemView.findViewById(R.id.fareRl);
                ///   passen1 = itemView.findViewById(R.id.passen1);
                passen = itemView.findViewById(R.id.passen);

                rvVehicleRoot = itemView.findViewById(R.id.motionLayout);
                description = itemView.findViewById(R.id.description);
                carFaresTv = itemView.findViewById(R.id.carFaresTv);
                //tvCarEta = itemView.findViewById(R.id.tvCarEta);

                fare_txt2 = itemView.findViewById(R.id.fare_txt2);
            }
        }

        public void setEtaForVehicle(String carName, TextView tvEta) {

            ReqAvailableDrivers reqAvailableDrivers = new ReqAvailableDrivers();
            reqAvailableDrivers.defaultClientId = (int) CommonVariables.localid;

            try {
                if (getArguments() != null && getArguments().getBoolean("again", false)) {
                    reqAvailableDrivers.latitude = Double.parseDouble(locAndFieldArrayList.get(0).getLat());
                    reqAvailableDrivers.longitude = Double.parseDouble(locAndFieldArrayList.get(0).getLon());
                } else {
                    reqAvailableDrivers.latitude = Double.parseDouble(latdriver);//CurrLoc.getLongitude();
                    reqAvailableDrivers.longitude = Double.parseDouble(longdriver);//CurrLoc.getLatitude();
                }

                lats = reqAvailableDrivers.latitude;
                lons = reqAvailableDrivers.longitude;
            } catch (Exception e) {
                e.printStackTrace();
                reqAvailableDrivers.latitude = 0;
            }


            Log.d(TAG, "run: run: run: ---  : " + latdriver + "," + longdriver);


            reqAvailableDrivers.vehicleName = carName;//selectedCar;
            reqAvailableDrivers.locType = 1;// locType;
            reqAvailableDrivers.MapKey = CommonVariables.GOOGLE_API_KEY;
            reqAvailableDrivers.mapType = 0;
            reqAvailableDrivers.uniqueValue = CommonVariables.clientid + "4321orue";


            new Manager_GetAvailableDriversByVehicleETA(getContext(), reqAvailableDrivers, new Listener_GetAvailableDriversManager() {
                @Override
                public void onComplete(String result) {


                    result = checkIfHasNullForString(result);

                    if (result.length() > 0 && isAdded()) {
                        try {
                            JSONObject parentObject = new JSONObject(result);
                            if (parentObject.getBoolean("HasError")) {
                                tvEta.setText("");
                            } else {

                                JSONObject dataObject = parentObject.optJSONObject("Data");
                                if (dataObject != null) {
                                    JSONObject object = dataObject.optJSONObject("availableDriverDetails");

                                    JSONArray listofavailabledrivers = object.optJSONArray("listofavailabledrivers");

                                    if (listofavailabledrivers != null) {
                                        if (listofavailabledrivers.length() > 0) {
                                            //  setAvailableDriver(listofavailabledrivers);
                                            String eta = listofavailabledrivers.getJSONObject(0).getString("ETA").replace("\r\n", "");
                                            mContext.runOnUiThread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    try {

                                                        if (eta.equalsIgnoreCase("0 Min(s)")) {
                                                            tvEta.setText("ETA : 1 Min(s)");
                                                        } else {
                                                            tvEta.setText("ETA : " + eta);
                                                        }
                                                    } catch (Exception ex) {
                                                        tvEta.setText("");
                                                    }
                                                }
                                            });


                                        } else {
                                            tvEta.setText("");
                                        }
                                    } else {
                                        tvEta.setText("");
                                    }


                                }
                            }
                        } catch (Exception ex) {


                        }
                    }


                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private AlertDialog showPeakFactorDialog() {
//        final Dialog dialog = new Dialog(getContext());
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//
//        wlp.gravity = Gravity.CENTER;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
//        window.setAttributes(wlp);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.setContentView(R.layout.layout_peak_factor);
//        dialog.show();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_peak_factor, null);

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

        TextView peakFactorTv = alertDialog.findViewById(R.id.peakFactorTv);
        try {
            peakFactorTv.setText(model.getSurgePrice() + "X");
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView confirm = alertDialog.findViewById(R.id.confirm);
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                peakFactorAlert = null;
                alertDialog.dismiss();
            }
        });
        return alertDialog;

    }

    private void createMarker(LocAndField from, String locationName, String time) {
        if (!isrouteComplete) {
            // mMap.clear();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(getLatLng(from));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(mContext, locationName, time)));
            mMap.addMarker(markerOptions).setTag("pickup");
        } else {
        }
    }

    private LatLng getLatLng(LocAndField locAndField) {
        LatLng latLng = new LatLng(Double.parseDouble(locAndField.getLat()), Double.parseDouble(locAndField.getLon()));
        return latLng;
    }

    double amount = 0;

    private void processStripePayment() {
        try {
            try {
//                String _fare = fare_txt.getText().toString().replace(sp.getString(CurrencySymbol, "\u00A3"), "");
                String _fare = vehicleAdapter.getFares();

                double fare = Double.parseDouble(_fare);
                amount = fare;
            } catch (Exception e) {
                e.printStackTrace();
                confirmbtn = true;
                amount = 0;
            }

            if (amount == 0) {
                confirmbtn = true;
                FBToast.warningToast(getActivity(), "Amount should be a greater than " + sp.getString(CurrencySymbol, "\u00A3") + "0.30", FBToast.LENGTH_SHORT);
                return;
            }

            if (model.getPickUpDate().length() == 0) {
                mPickupDate = getCurrentDate();
                mPickupDate = getCurrentTime();

                model.setPickUpDate(mPickupDate);
                model.setPickUpTime(mPickupTime);
            }

            Model_ValidateBookingInfo info = new Model_ValidateBookingInfo(
                    "",
                    CommonVariables.SUB_COMPANY,
                    "" + model.getPickUpDate(),
                    "" + model.getPickUpTime(),
                    "" + locAndFieldArrayList.get(0).getField(),
                    "" + locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getField(),
                    "" + locAndFieldArrayList.get(0).getLat() + "," + locAndFieldArrayList.get(0).getLon(),
                    "" + locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getLat() + "," + locAndFieldArrayList.get(locAndFieldArrayList.size() - 1).getLon(),
                    CommonVariables.clientid + "4321orue",
                    String.valueOf(CommonVariables.clientid));

            new Manager_ValidateBookingInfo(getActivity(), info, result -> {
                try {
                    if (result.startsWith("error_")) {
                        result = result.replace("error_", "");
                        FBToast.errorToast(getActivity(), result, FBToast.LENGTH_SHORT);
                        confirmbtn = true;
                        return;
                    }

                    JSONObject parentObject = new JSONObject(result);

                    if (parentObject.getBoolean("HasError")) {
                        confirmbtn = true;
                        FBToast.errorToast(getActivity(), parentObject.optString("Message"), FBToast.LENGTH_SHORT);
                    } else {
                        //direct payment
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
                                            confirmbtn = true;
                                            return;
                                        }
                                    } catch (Exception e) {
                                        confirmbtn = true;
                                        e.printStackTrace();
                                    }
                                    if (response != null && !response.startsWith("false") && !response.equals("")) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getBoolean("hasError")) {
                                                confirmbtn = true;
                                                FBToast.errorToast(getActivity(), jsonObject.getString("message"), FBToast.LENGTH_LONG);
                                            } else {
                                                if (jsonObject.getString("message").startsWith("Status :succeeded")) {
                                                    String TransactionID = jsonObject.getString("transactionID");
                                                    model.setTransactionId(TransactionID);
                                                    SaveBooking();
                                                } else {
                                                    confirmbtn = true;
                                                    FBToast.errorToast(getActivity(), "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        confirmbtn = true;
                                        FBToast.errorToast(getActivity(), "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                                    }
                                })
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private HashMap<String, Object> getValueForRouteCoordinate() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mapKey", CommonVariables.GOOGLE_API_KEY);
        map.put("region", CommonVariables.COUNTRY);
        map.put("uniqueKey", CommonVariables.clientid + "4321orue");
        map.put("defaultclientId", "" + CommonVariables.clientid);
        String token = AppConstants.getAppConstants().getToken();
        map.put("Token", token);
        map.put("bookingInformation", getBookingInformation());
        return map;
    }

    private HashMap<String, Object> getBookingInformation() {
        LocAndField from = locAndFieldArrayList.get(0);
        LocAndField to = locAndFieldArrayList.get(locAndFieldArrayList.size() - 1);

        ViaAddresses[] a = getViaslist();


        HashMap<String, Object> map = new HashMap<>();
        map.put("FromAddress", from.getLat() + "," + from.getLon());
        map.put("ToAddress", to.getLat() + "," + to.getLon());
        map.put("DistanceType", "shortest");
        map.put("Miles", "");
        ArrayList<ViaAddresses> viaArrayList = new ArrayList();
        try {
            if (locAndFieldArrayList.size() == 3) {
                LocAndField locAndField = locAndFieldArrayList.get(1);
                ViaAddresses via = new ViaAddresses();
                via.ViaLatLng = locAndField.getLat() + "," + locAndField.getLon();
                via.Viaaddress = locAndField.getField();

                if (locAndField.locationType.isEmpty()) {
                    via.Viatype = "Address";
                } else {
                    via.Viatype = locAndField.locationType;
                }
                viaArrayList.add(via);
            }
            if (locAndFieldArrayList.size() == 4) {
                LocAndField locAndField1 = locAndFieldArrayList.get(1);
                ViaAddresses via1 = new ViaAddresses();
                via1.ViaLatLng = locAndField1.getLat() + "," + locAndField1.getLon();
                via1.Viaaddress = locAndField1.getField();


                if (locAndField1.locationType.isEmpty()) {
                    via1.Viatype = "Address";
                } else {
                    via1.Viatype = locAndField1.locationType;
                }

                viaArrayList.add(via1);
                // vai 2
                LocAndField locAndField = locAndFieldArrayList.get(2);
                ViaAddresses via = new ViaAddresses();
                via.ViaLatLng = locAndField.getLat() + "," + locAndField.getLon();
                via.Viaaddress = locAndField.getField();
                if (locAndField.locationType.isEmpty()) {
                    via.Viatype = "Address";
                } else {
                    via.Viatype = locAndField.locationType;
                }

                viaArrayList.add(via);
            }
            map.put("Via", viaArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("isRebook", "0");
        map.put("Mileage", "0");
        return map;
    }

    private Model_BookingInformation getAllFaresFromDispatchObject() {
        LocAndField from = locAndFieldArrayList.get(0);
        LocAndField to = locAndFieldArrayList.get(locAndFieldArrayList.size() - 1);

        Model_BookingInformation obj = new Model_BookingInformation();

        obj.vehicle = "";
        obj.PostedFrom = "Android";

        obj.FromAddress = from.getField();
        obj.FromLatLng = from.getLat() + "," + from.getLon();
        obj.FromType = from.getLocationType();

        obj.ToAddress = to.getField();
        obj.ToLatLng = to.getLat() + "," + to.getLon();
        obj.ToType = to.getLocationType();
        //meet and greet
        obj.flightNo = fromDoorNo;
        obj.Miles = journeyMiles;

        obj.PaymentType = model.getPaymentType();
        obj.CustomerId = userModel.getUserServerID();
        obj.RouteCoordinates = "";
        obj.PickupDateTime = mPickupDate + " " + mPickupTime;
        obj.SubcompanyId = CommonVariables.SUB_COMPANY;
        obj.PickupToDestinationTime = PickupToDestinationTime;

        // Via
        try {
            ArrayList<ViaAddresses> viaArrayList = new ArrayList();
            try {
                if (locAndFieldArrayList.size() == 3) {
                    LocAndField locAndField = locAndFieldArrayList.get(1);
                    ViaAddresses via = new ViaAddresses();
                    via.Viaaddress = locAndField.getField();
                    via.ViaLatLng = locAndField.getLat() + "," + locAndField.getLon();
                    via.Viatype = "Address";
                    viaArrayList.add(via);
                }
                if (locAndFieldArrayList.size() == 4) {
                    LocAndField locAndField1 = locAndFieldArrayList.get(1);
                    ViaAddresses via1 = new ViaAddresses();
                    via1.Viaaddress = locAndField1.getField();
                    via1.ViaLatLng = locAndField1.getLat() + "," + locAndField1.getLon();
                    if (locAndField1.locationType.isEmpty()) {
                        via1.Viatype = "Address";
                    } else {
                        via1.Viatype = locAndField1.locationType;
                    }

                    viaArrayList.add(via1);


                    LocAndField locAndField = locAndFieldArrayList.get(2);
                    ViaAddresses via = new ViaAddresses();
                    via.Viaaddress = locAndField.getField();
                    via.ViaLatLng = locAndField.getLat() + "," + locAndField.getLon();
                    if (locAndField1.locationType.isEmpty()) {
                        via.Viatype = "Address";
                    } else {
                        via.Viatype = locAndField1.locationType;
                    }

                    viaArrayList.add(via);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            obj.Via = viaArrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // PromotionCode
        try {
            obj.PromotionCode = ((isSignupPromoApplied && !VerifiedCode.equals("")) ? VerifiedCode : "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Date Time
        if (obj.PickupDateTime.trim().equals("")) {
            mPickupDate = getCurrentDate();
            mPickupTime = getCurrentTime();
            obj.PickupDateTime = mPickupDate + " " + mPickupTime;
        } else {
            obj.PickupDateTime = mPickupDate + " " + mPickupTime;
        }

        if (sp.getBoolean(CommonVariables.ISMEMBERUSERLOGIN, false) && userModel.getAccountWebID() != null && !userModel.getAccountWebID().equals("")) {
            if (model.getPaymentType().toLowerCase().contains("account")) {
                obj.CompanyId = userModel.getAccountWebID();
            } else {
                obj.CompanyId = "0";
            }
        } else {
            obj.CompanyId = "0";
        }

        return obj;
    }
}