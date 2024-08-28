package base.activities;

import static base.utils.CommonMethods.checkIfHasNullForString;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.eurosoft.customerapp.BuildConfig;
import com.eurosoft.customerapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import base.databaseoperations.AddressesDatabase;
import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.fragments.Fragment_Main;
import base.listener.Listener_GetInitializeDetailsFromDispatch;
import base.manager.Manager_GetInitializeDetailsFromDispatch;
import base.models.SettingsModel;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_Splash extends AppCompatActivity {

    private AsyncTask initialDetails;

    private static int INITIAL_REQUEST = 1337;
    public static String LAT = "";
    public static String LNG = "";

    private int REQUEST_LOCATION;

    private SharedPreferences sp;
    private SharedPrefrenceHelper helper;
    private SharedPreferences.Editor edit;
    private DatabaseOperations mDatabaseOperations;
    private DatabaseHelper mDatabaseHelper;

    private int vcount;


    public String pendingTitle = "";
    public String pendingMessage = "";
    public boolean isDialogueInPending = false;
    public boolean isAcitvityInBackgroud = false;
    private boolean isFinish = false;

    public FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Handler handler;
    private LocationRequest mLocationRequest;
    private ProgressDialog mProgress;
    private ArrayList<String> vehicleImage = new ArrayList<>();
    private Listener_GetInitializeDetailsFromDispatch listener_getInitializeDetailsFromDispatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_splash_screen);

        FirebaseApp.initializeApp(this);

        createObject();

        listener();

        requestPermissions();
    }

    private void createObject() {
        try {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

            handler = new Handler();

            if (!isTaskRoot()
                    && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                    && getIntent().getAction() != null
                    && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            AddressesDatabase dbhelper = new AddressesDatabase(this);
            dbhelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sp = PreferenceManager.getDefaultSharedPreferences(Activity_Splash.this);

        helper = new SharedPrefrenceHelper(this);
        edit = sp.edit();

        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseOperations = new DatabaseOperations(mDatabaseHelper);

        vcount = mDatabaseOperations.getvehiclesCount();


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void listener() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
            }
        };

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Activity_Splash.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(Activity_Splash.this, location -> {
            if (location != null) {
                try {
                    Log.d("TAG", "onCreate: Splash Location " + location.getLatitude() + ", " + location.getLongitude());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(e -> {
        });

        listener_getInitializeDetailsFromDispatch = new Listener_GetInitializeDetailsFromDispatch() {
            @Override
            public void onStart() {
               /* mProgress = new ProgressDialog(Activity_Splash.this);
                mProgress.setTitle("Fetching Vehicle List");
                mProgress.setMessage("Please Wait..");
                mProgress.show();*/
            }

            @Override
            public void onComplete(String result) {
                try {
                    if (result != null && !result.isEmpty()) {
                        try {
                            JSONObject parentObject = new JSONObject(result);
                            if (parentObject.getBoolean("HasError")) {
                                if (!isAcitvityInBackgroud) {
                                    showDialogue("Try Again", checkIfHasNullForString(parentObject.optString("Message")));
                                } else {
                                    isDialogueInPending = true;
                                    pendingTitle = "Try Again";
                                    pendingMessage = checkIfHasNullForString(parentObject.optString("Message"));
                                }
                            } else {
                                sp.edit()
                                        .putString("isStationLoaded", "0")
                                        .putString("isStoreLoaded", "0")
                                        .putString("isAirportLoaded", "0")
                                        .putString("isPromoLoaded", "0")
                                        .commit();

                                JSONObject jsonObject = new JSONObject(checkIfHasNullForString(parentObject.optString("Data")));
                                if (jsonObject != null) {
                                    String token = checkIfHasNullForString(parentObject.optString("Token"));

//                                    // New Singleton Work Implementation Here For Token
                                    AppConstants.getAppConstants().applicationContext(Activity_Splash.this);
                                    AppConstants.getAppConstants().removeToken();
                                    AppConstants.getAppConstants().putToken(token);
                                    AppConstants.getAppConstants().getToken();
//                                    CommonVariables.TOKEN = token;

                                    String VersionUpgradeObj = checkIfHasNullForString(jsonObject.optString("VersionUpgrade"));

                                    if (!VersionUpgradeObj.equals("")) {

                                        JSONObject j = new JSONObject(VersionUpgradeObj);

                                        String Priority = j.optString("Priority");
                                        float serverAppVersion = Float.parseFloat(j.optString("Android"));
                                        String Message = j.optString("Message");

                                        float versionName = Float.parseFloat(BuildConfig.VERSION_NAME);

                                        if (serverAppVersion > versionName) {
                                            if (Integer.parseInt(Priority) == 1) {
                                                new SweetAlertDialog(Activity_Splash.this, SweetAlertDialog.WARNING_TYPE)
                                                        .setTitleText(Message)
                                                        .setContentText("")
                                                        .setConfirmText("Update Now")
                                                        .setCancelText("Update Later")
                                                        .showCancelButton(true)
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                                try {
                                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                                    finish();
                                                                } catch (Exception anfe) {
                                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
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
                                                                continueWorking(jsonObject);
                                                            }

                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                                                sweetAlertDialog.cancel();
                                                            }
                                                        })
                                                        .show();
                                                return;
                                            } else if (Integer.parseInt(Priority) == 2) {
                                                new SweetAlertDialog(Activity_Splash.this, SweetAlertDialog.UPDATE_TYPE)
                                                        .setTitleText(Message)
                                                        .setContentText("")
                                                        .setConfirmText("Update Now")
                                                        .setCancelText("  Exit   ")
                                                        .showCancelButton(true)
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                                try {
                                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                                } catch (Exception anfe) {
                                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
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
                                                                finish();
                                                            }

                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                                                sweetAlertDialog.cancel();
                                                            }
                                                        })
                                                        .show();
                                                return;
                                            } else {
                                                return;
                                            }

                                        } else {
                                            continueWorking(jsonObject);
                                        }


                                    } else {

                                        continueWorking(jsonObject);

                                    }


                                } else {
                                    continueWorking(jsonObject);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            failedToSyncDataFromServer();
                        }
                    } else {
                        failedToSyncDataFromServer();
                    }
                } catch (Exception e) {
                    failedToSyncDataFromServer();
                }
            }
        };


    }

    private void callInitialize() {
        try {
            if (!isFinishing()) {
                if (initialDetails != null) {
                    initialDetails.cancel(true);
                }
                if(listener_getInitializeDetailsFromDispatch == null){
                    listener();
                }
                // initialDetails = new InitializeAppDb(Activity_Splash.this, true).execute();
                initialDetails = new Manager_GetInitializeDetailsFromDispatch("" + CommonVariables.clientid, "4321orue", listener_getInitializeDetailsFromDispatch).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAcitvityInBackgroud = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isDialogueInPending && isAcitvityInBackgroud) {
            isDialogueInPending = false;

            try {
                showDialogue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isAcitvityInBackgroud = false;
    }

    private void showDialogue() {

        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(pendingTitle)
                .setContentText(pendingMessage)
                .setCancelText("Exit!")
                .setConfirmText("Retry")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        startUp();
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        finish();
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })
                .show();
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length >= 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (isNetworkAvailable()) {
                    startUp();
                } else if (vcount == 0 && !isNetworkAvailable()) {
                    new AlertDialog.Builder(this).setTitle("No Internet Connection")
                            .setMessage("You need active internet connection for this app")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Activity_Splash.this.finish();
                                }
                            }).show();
                } else {
                    startUp();
                }

            } else {
                // Permission was denied or request was cancelled
                if (isNetworkAvailable()) {
                    startUp();
                } else if (vcount == 0 && !isNetworkAvailable()) {
                    new AlertDialog.Builder(this).setTitle("No Internet Connection")
                            .setMessage("You need active internet connection for this app")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Activity_Splash.this.finish();
                                }
                            }).show();
                } else {
                    startUp();
                }
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

    private void startLocationUpdates(LocationRequest mLocationRequest, LocationCallback locationCallback) {

        if (ActivityCompat.checkSelfPermission(Activity_Splash.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Activity_Splash.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (fusedLocationClient != null) {
            fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
        }
    }

    public void stopLocationUpdates() {
        try {
            if (locationCallback != null) {
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startUp() {
        startLocationUpdates(mLocationRequest, locationCallback);
        if (handler != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callInitialize();
                }
            }, 2000);
        }
    }

    private void failedToSyncDataFromServer() {
        new SweetAlertDialog(Activity_Splash.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Try Again")
                .setContentText("Failed to sync data from server!")
                .setCancelText("Exit!")
                .setConfirmText("Retry")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        //  new InitializeAppDb(Activity_Splash.this, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        new Manager_GetInitializeDetailsFromDispatch("" + CommonVariables.clientid, "4321orue", listener_getInitializeDetailsFromDispatch).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        finish();
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })
                .show();
    }

    private void continueWorking(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.optJSONArray("VehicleList");
            if (jsonArray != null) {
                mDatabaseOperations.deleteVehicle();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject res = jsonArray.optJSONObject(i);
                    if (res != null) {
// Storing JSON item in a Variable
                        int Id = res.getInt("Id");
                        String Name = checkIfHasNullForString(res.optString("Name"));
//                        if (Name.equalsIgnoreCase("shopping collection")) {
//                            continue;
//                        }
                        String TotalPassengers = checkIfHasNullForString(res.optString("TotalPassengers"));
                        String TotalHandLuggages = checkIfHasNullForString(res.optString("TotalHandLuggages"));
                        String TotalLuggages = checkIfHasNullForString(res.optString("TotalLuggages"));
                        String SortOrderNo = checkIfHasNullForString(res.optString("SortOrderNo"));
                        String VehicleImage = checkIfHasNullForString(res.optString("VehicleImage"));
                        vehicleImage.add(Name + "=" + VehicleImage);
                        if (SortOrderNo.equals("null")) {
                            SortOrderNo = "0";
                        }
                        if (Id >= 0) {
                            mDatabaseOperations.insertVehicle(Id, Name, TotalPassengers, TotalHandLuggages, TotalLuggages, SortOrderNo);
                        }
                    }
                }

                CommonVariables.GOOGLE_API_KEY = checkIfHasNullForString(jsonObject.optString("MapKey"));
                CommonVariables.Clientip = checkIfHasNullForString(jsonObject.optString("Clientip"));

                JSONObject coordinates = jsonObject.optJSONObject("BaseAddress").optJSONObject("Coordinate");
                if (coordinates != null) {
                    LAT = checkIfHasNullForString(coordinates.optString("Lat"));
                    LNG = checkIfHasNullForString(coordinates.optString("Lng"));

                    edit.putString(Config.BaseLat, LAT);
                    edit.putString(Config.BaseLng, LNG);

                    edit.putString(Config.BaseAddress, checkIfHasNullForString(jsonObject.optJSONObject("BaseAddress").optString(Config.BaseAddress)));
                }

                edit.putString(Config.MapKey, checkIfHasNullForString(jsonObject.optString("MapKey")));
                edit.putString(Config.ClientIp, CommonVariables.Clientip);
                edit.putString(Config.PaymentTypes, checkIfHasNullForString(jsonObject.optString(Config.PaymentTypes)));


                // Hide Marker Work
                try {
                    boolean hideCurrentLocation = jsonObject.getBoolean("HideCurrentLocation");
                    edit.putString(Config.HideCurrentLocation, hideCurrentLocation ? "1" : "0");

                } catch (Exception e) {
                    e.printStackTrace();

                }



                try {
                    String defaultType = sp.getString(CommonVariables.paymentType, "cash");
                    String defaultTypeId = "";

                    if (defaultType.toLowerCase().trim().equals("cash")) {
                        defaultTypeId = Config.CASH;
                    } else if (defaultType.toLowerCase().trim().equals("account")) {
                        defaultTypeId = Config.ACCOUNT;
                    } else if (defaultType.toLowerCase().trim().equals("credit card")) {
                        defaultTypeId = Config.CARD;
                    } else {
                        defaultTypeId = Config.CARDCAR;
                    }

                    if (!checkIfHasNullForString(jsonObject.optString(Config.PaymentTypes)).contains(defaultTypeId)) {
                        String[] splitAr = jsonObject.optString(Config.PaymentTypes).split(",");
                        if (splitAr[0].toLowerCase().trim().equals(Config.CASH)) {
                            edit.putString(CommonVariables.paymentType, "cash");
                        } else if (splitAr[0].toLowerCase().trim().equals(Config.CARD)) {
                            edit.putString(CommonVariables.paymentType, "credit card");
                        } else if (splitAr[0].toLowerCase().trim().equals(Config.ACCOUNT)) {
                            edit.putString(CommonVariables.paymentType, "account");
                        } else {
                            edit.putString(CommonVariables.paymentType, "card in car");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 0 hide, 1 show
                String tip = checkIfHasNullForString(jsonObject.optString(CommonVariables.EnableTip));
                if (tip.equals("")) tip = "0";
                edit.putString(CommonVariables.EnableTip, tip);


                // 0 hide, 1 show
                String EnableLostItemInquiry = checkIfHasNullForString(jsonObject.optString(CommonVariables.EnableLostItemInquiry));
                if (EnableLostItemInquiry.equals("")) EnableLostItemInquiry = "0";
                edit.putString(CommonVariables.EnableLostItemInquiry, EnableLostItemInquiry);


                // 0 hide, 1 show
                String EnableComplain = checkIfHasNullForString(jsonObject.optString(CommonVariables.EnableComplain));
                if (EnableComplain.equals("null")) EnableComplain = "0";
                edit.putString(CommonVariables.EnableComplain, EnableComplain);


                try {
                    edit.putFloat(Config.CounterTime, (float) jsonObject.getDouble(Config.CounterTime));
                } catch (Exception e) {
                    edit.putFloat(Config.CounterTime, 4f);
                    e.printStackTrace();
                }

                edit.putString(CommonVariables.EnableReceipt, checkIfHasNullForString(jsonObject.optString(CommonVariables.EnableReceipt)));
                edit.putString(CommonVariables.enableSignup, checkIfHasNullForString(jsonObject.optString("enableSignup")));
                edit.putString(CommonVariables.enableVia, checkIfHasNullForString(jsonObject.optString(CommonVariables.enableVia)));
                edit.putString(Config.ShowFares, checkIfHasNullForString(jsonObject.optString(Config.ShowFares)));
                edit.putString(CommonVariables.CurrencySymbol, checkIfHasNullForString(jsonObject.optString(CommonVariables.CurrencySymbol)));

                String distanceCal = checkIfHasNullForString(jsonObject.optString(CommonVariables.distanceUnit));
                if (distanceCal.toLowerCase().startsWith("kilo")) distanceCal = "km";
                edit.putString(CommonVariables.distanceUnit, distanceCal.toLowerCase());

                // 0 = Inside UK
                // 1 = Outside UK
                String enableOutsideUK = checkIfHasNullForString(jsonObject.optString(CommonVariables.enableOutsideUK));
                if (enableOutsideUK.equals("")) enableOutsideUK = "1";

                edit.putString(CommonVariables.enableOutsideUK, enableOutsideUK);

                String  isWebDispatch = checkIfHasNullForString(jsonObject.optString(CommonVariables.IS_WEB_DISPATCH));

                if (isWebDispatch.equals("")) isWebDispatch = "0";

                edit.putString(CommonVariables.IS_WEB_DISPATCH, isWebDispatch);


                edit.putString(CommonVariables.EnableGoogleForSuggestion, "0");
                edit.putString(CommonVariables.ENABLE_DELETE_Account, "1");

                edit.putString(CommonVariables.EnablePayAfterBookingClearOnStrip, "0");




                edit.putString(CommonVariables.EnableMeetAndGreet, "0");


                edit.putString(CommonVariables.Enable_ForeGround_Service, "1");

                edit.putString(CommonVariables.Restrict_Message, checkIfHasNullForString(jsonObject.optString(CommonVariables.Restrict_Message)));
                edit.putString(CommonVariables.Restrict_Vehicle, checkIfHasNullForString(jsonObject.optString(CommonVariables.Restrict_Vehicle)));
                edit.putString(Config.ResendReciept, checkIfHasNullForString(jsonObject.optString(Config.ResendReciept)));
                edit.putString(Config.EstimatedFareTxt, checkIfHasNullForString(jsonObject.optString(Config.EstimatedFareTxt)));

                edit.putString(Config.SendReceiptOnClear, checkIfHasNullForString(jsonObject.optString(Config.SendReceiptOnClear)));
                edit.putString(Config.LostProperty, checkIfHasNullForString(jsonObject.optString(Config.LostProperty)));
                // foreground service setting
                edit.putString(Config.FetchBookingsFromServer, checkIfHasNullForString(jsonObject.optString(Config.FetchBookingsFromServer)));

                edit.putString(Config.ZeroFareText, checkIfHasNullForString(jsonObject.optString(Config.ZeroFareText)));
                String VerificationType = checkIfHasNullForString(jsonObject.optString(Config.VerificationType));
                edit.putString(Config.VerificationType, VerificationType);

                String CreditCardSurcharge = checkIfHasNullForString(jsonObject.optString(Config.CreditCardSurcharge));
                if (!CreditCardSurcharge.equals("")) {
                    String[] splitArr = CreditCardSurcharge.split("\\|");
                    edit.putString(Config.CreditCardSurchargeType, splitArr[1]);
                    edit.putString(Config.CreditCardSurcharge, splitArr[0]);
                }

                edit.putString(Config.ShoppingEnabled, checkIfHasNullForString(jsonObject.optString(Config.ShoppingEnabled)));
                edit.putString(Config.ShoppingNotice, checkIfHasNullForString(jsonObject.optString(Config.ShoppingNotice)));

                try {
                    edit.putFloat(Config.maxMilesLimit, jsonObject.getInt(Config.maxMilesLimit));
                } catch (Exception e) {
                    e.printStackTrace();
                    edit.putInt(Config.maxMilesLimit, 0);
                }

                edit.putString(Config.TermsConditions, checkIfHasNullForString(jsonObject.optString(Config.TermsConditions)));
                edit.putString(Config.isShareTracking, checkIfHasNullForString(jsonObject.optString(Config.isShareTracking)));
                edit.putString(Config.CardSuccessMsg, checkIfHasNullForString(jsonObject.optString(Config.CardSuccessMsg)));//"We may charge a small amount(£1.01) to confirm your card details.This is immediately refunded"); //jsonObject.getString(Config.CardSuccessMsg));
                edit.putString(Config.ShowEmergency, checkIfHasNullForString(jsonObject.optString(Config.ShowEmergency)));
                edit.putString(Config.VEHICLEMOVEMENTTIMER, checkIfHasNullForString(jsonObject.optString(Config.VEHICLEMOVEMENTTIMER)));

                if (!checkIfHasNullForString(jsonObject.optString("JudoId")).equals("")) {
                    edit.putString(Config.JudoId, checkIfHasNullForString(jsonObject.optString("JudoId")));
                    edit.putString(Config.JudoSecret, checkIfHasNullForString(jsonObject.optString("JudoSecret")));
                    edit.putString(Config.JudoToken, checkIfHasNullForString(jsonObject.optString("JudoToken")));

                }

/*                Judo ID: 100602119
                3DS-2
                Sandbox tokens
                Token: M88Zt4Sa0KZxhw1A
                Secret: 12e25f7f3c12971ba362f89b2c1d2066cf9e0198b321b58203742f96558ffea3



                Live tokens
                Token: Yo9m9ByG0QCYsTRh
                Secret: 770a4a1aa4c3c17264f2a0d1f9ad3c7f7f23155daf5cec5d419600a2ae81420c
                */

                String pk = checkIfHasNullForString(jsonObject.optString("PublishKey"));
                String sk = checkIfHasNullForString(jsonObject.optString("SecretKey"));

                
                edit.putString(Config.Stripe_PublishKey, pk);
                edit.putString(Config.Stripe_SecretKey, sk);

                // 1 is Hold Payment
                // 0 is Don't Hold Payment
                String val = checkIfHasNullForString(jsonObject.optString(Config.EnableCardHold));
                if (val.equals("")) val = "0"; // 0 is Don't Hold Payment
                edit.putString(Config.EnableCardHold, val);

                edit.putString(Config.Gateway, checkIfHasNullForString(jsonObject.optString("Gateway")));
                edit.putString(Config.IsKonnectPay, checkIfHasNullForString(jsonObject.optString(Config.IsKonnectPay)));

                if(BuildConfig.DEBUG){
//                    edit.putString(Config.IsKonnectPay, "0");
//                    edit.putString(Config.Gateway, checkIfHasNullForString(jsonObject.optString("Gateway")));
//                    pk = "pk_test_51OF8Z1JqqexWjMTXopJO2FgI3l0FPXYGw59fSmx6Lg6aJGgrOAinIZTRuQVOgncOZfjXfr86hhHGZpErEeWyOinB00PiyhjnDi";
//                    sk = "sk_test_51OF8Z1JqqexWjMTXaMRa2Qus63u2WoGF2EU2LtUQo83JPEupdZ7UWJMrIvJTrSYfPfp2Y6PiM7yfgFVMH5qSPRQ1002mXuTjWk";
//
//                    edit.putString(Config.Stripe_PublishKey, pk);
//                    edit.putString(Config.Stripe_SecretKey, sk);

                }

                edit.putString(Config.InstId, checkIfHasNullForString(jsonObject.optString(Config.InstId)));
                edit.putString(Config.ShowPromoList, checkIfHasNullForString(jsonObject.optString(Config.ShowPromoList)));



                edit.putString(Config.PSPId, checkIfHasNullForString(jsonObject.optString(Config.PSPId)));
                edit.putString(Config.APIUserId, checkIfHasNullForString(jsonObject.optString(Config.APIUserId)));
                edit.putString(Config.APIPassword, checkIfHasNullForString(jsonObject.optString(Config.APIPassword)));
                edit.putString(Config.ApplyPromotion, checkIfHasNullForString(jsonObject.optString(Config.ApplyPromotion)));


                JSONObject connectObj = jsonObject.optJSONObject("CallConnect");
                if (connectObj != null) {
                    edit.putString(SharedPrefrenceHelper.DrvConnectHost, checkIfHasNullForString(connectObj.optString("CallConnectHostName")));
                    edit.putString(SharedPrefrenceHelper.DrvConnectPass, checkIfHasNullForString(connectObj.optString("CallConnectPassword")));
                    edit.putString(SharedPrefrenceHelper.DrvConnectPort, checkIfHasNullForString(connectObj.optString("CallConnectPort")));
                    edit.putString(SharedPrefrenceHelper.DrvConnectUsername, checkIfHasNullForString(connectObj.optString("CallConnectUserName")));
                    edit.putBoolean(SharedPrefrenceHelper.DrvConnectEnabled, connectObj.optBoolean("IsCallConnectActive"));
                }
                edit.commit();
                // this method will remove airports and station that save in local database  during
                removeAirportAndStation();
                downloadVehicleImages(vehicleImage);


            } else {
                failedToSyncDataFromServer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            failedToSyncDataFromServer();
        }
    }

    private void removeAirportAndStation(){
        try {
            mDatabaseOperations.removeAirports();
            mDatabaseOperations.removeStations();
            sp.edit().putString("isStationLoaded", "0").commit();
            sp.edit().putString("isAirportLoaded", "0").apply();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void downloadVehicleImages(ArrayList<String> carList) {
        try {
            if (carList.size() > 0) {
                String[] carlistitem = carList.get(0).split("=");
                Glide.with(Activity_Splash.this)
                        .asBitmap()
                        .load(carlistitem[1])
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                goAhead();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(resource, 70, 108, false);
                                saveImageToInternalStorage(carlistitem[0], scaledBitmap);
                                goAhead();
                            }

                        });
            } else {
                goAhead();
            }
        } catch (Exception e) {
            goAhead();
        }
    }

    public boolean saveImageToInternalStorage(String caritem, Bitmap image) {
        try {
            removeFile(caritem);
            FileOutputStream fos = openFileOutput(caritem.toLowerCase() + ".png", Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

    public boolean removeFile(String name) {
        File dir = getFilesDir();
        File file = new File(dir, name + ".png");
        boolean delete = false;
        if (file.exists()) {
            delete = file.delete();
        } else {
            delete = true;
        }
        return delete;
    }
    @SuppressLint("MissingPermission")
    private void goAhead() {
        try {
            try {
                fusedLocationClient.getLastLocation();
                stopLocationUpdates();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mDatabaseOperations.getAllVehiclesNames().length > 0 && !CommonVariables.GOOGLE_API_KEY.equals("")) {

                if (sp.getString(CommonVariables.ISAuthorized, "").equals("0")) {
                    SettingsModel model = helper.getSettingModel();
                    startActivity(new Intent(Activity_Splash.this, Activity_AuthorizeCode.class)
                            .putExtra("MobileNo", model.getPhoneNo())
                            .putExtra("Name", model.getName())
                            .putExtra("Email", model.getEmail())
                            .putExtra("address", model.getAddress())
                            .putExtra("telno", model.getPhoneNo())
                            .putExtra("Password", model.getPassword())
                            .putExtra("ip", CommonVariables.Clientip));

                    finish();

                } else if (!sp.getBoolean(CommonVariables.ISUSERLOGIN, false)) {
                    if (!sp.getString(CommonVariables.enableSignup, "1").equals("0")) {
                        startActivity(new Intent(Activity_Splash.this, Activity_Start.class));
                        finish();
                    } else {
                        Activity_Splash.this.startActivity(new Intent(Activity_Splash.this, Fragment_Main.class));
                        Activity_Splash.this.finish();
                    }
                } else {
                    Activity_Splash.this.startActivity(new Intent(Activity_Splash.this, Fragment_Main.class));
                    Activity_Splash.this.finish();
                }

            } else {
                if (!isAcitvityInBackgroud) {
                    showDialogue("Try Again", "Failed to sync data from server!");
                } else {
                    isDialogueInPending = true;
                    pendingTitle = "Try Again";
                    pendingMessage = "Failed to sync data from server!";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialogue(String Title, String message) {
        pendingTitle = Title;

        pendingMessage = message;
        new SweetAlertDialog(Activity_Splash.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(Title)
                .setContentText(message)
                .setCancelText("Exit!")
                .setConfirmText("Retry")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
//                        new InitializeAppDb(Activity_Splash.this, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        new Manager_GetInitializeDetailsFromDispatch("" + CommonVariables.clientid, "4321orue", listener_getInitializeDetailsFromDispatch).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        finish();
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })
                .show();
    }

    public void requestPermissions() {
     /*   if (Build.VERSION.SDK_INT >= 23) {
            if (!canAccessLocation()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Location Permission")
                        .setMessage("Location access is required to show map within the app.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
//                                    requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                if (isNetworkAvailable()) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            startUp();
                        }
                    });
                } else if (vcount == 0 && !isNetworkAvailable()) {
                    new AlertDialog.Builder(this).setCancelable(false).setTitle("No Internet Connection")
                            .setMessage("You need an active internet connection for this app")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Activity_Splash.this.finish();
                                }
                            }).show();
                } else {
                    startUp();
                }
            }

        } else {
            if (isNetworkAvailable()) {
                startUp();
            } else if (vcount == 0 && !isNetworkAvailable()) {
                new AlertDialog.Builder(this).setTitle("No Internet Connection")
                        .setMessage("You need active internet connection for this app")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Activity_Splash.this.finish();
                            }
                        }).show();
            } else {
                startUp();
            }
        }
*/
        if (Build.VERSION.SDK_INT >= 23) {

            ArrayList<String> permissionsList = new ArrayList<>();

            int fineLocationPermission = getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getPackageName());

            int coarseLocationPermission2 = getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getPackageName());
            if (Build.VERSION.SDK_INT >= 33) {
                int postNotificationPermission = getPackageManager().checkPermission(Manifest.permission.POST_NOTIFICATIONS, getPackageName());
                if (postNotificationPermission != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(Manifest.permission.POST_NOTIFICATIONS);
                }
            }
            if (fineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (coarseLocationPermission2 != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            if (permissionsList.size() > 0) {
                String[] permissions = new String[permissionsList.size()];
                permissions = permissionsList.toArray(permissions);
                requestPermissions(permissions, REQUEST_LOCATION);
            } else {
                if (isNetworkAvailable()) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            startUp();
                        }
                    });
                } else if (!isNetworkAvailable()) {
                    new AlertDialog.Builder(this).setCancelable(false).setTitle("No Internet Connection")
                            .setMessage("You need an active internet connection for this app")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Activity_Splash.this.finish();
                                }
                            }).show();
                } else {
                    startUp();
                }
            }
        } else {
            if (isNetworkAvailable()) {
                startUp();
            } else if (!isNetworkAvailable()) {
                new AlertDialog.Builder(this).setTitle("No Internet Connection")
                        .setMessage("You need active internet connection for this app")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Activity_Splash.this.finish();
                            }
                        }).show();
            } else {
                startUp();
            }
        }
    }

}