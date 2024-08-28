package base.activities;

import static android.view.View.GONE;
import static base.activities.Activity_SearchAddress.KEY_HOME;
import static base.activities.Activity_SearchAddress.KEY_OFFICE;
import static base.utils.CommonMethods.checkIfHasNullForString;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.eurosoft.customerapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import base.listener.Listener_GetAvailableDriversManager;
import base.manager.Manager_GetAvailableDriversByVehicleETA;
import base.models.LocAndField;
import base.models.ParentPojo;
import base.models.ReqAvailableDrivers;
import base.newui.Driver;
import base.newui.HomeFragment;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;

public class Activity_SetLocationOnMap extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, Listener_GetAvailableDriversManager {

    private static final String TAG = "SetLocationOnMapActivit";
    private static final int REQUEST_LOCATION = 99;
    private static final int REQUEST_CHECK_SETTINGS = 1021;

    //    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private Handler mCameraHandler;
    private LocationCallback locationCallback;
    private SupportMapFragment mapFragment;
    private TextView setArrTv;
    ImageView topIv;
    ImageView backIv;
    private float vehicleTimer;
    int indexEditTextPosition;
    private SharedPreferences sp;
    private TextView doneBtn;
    private TextView setFromTv;
    private TextView totalTimeForArrival;
    private TextView subname;
    private ArrayList<LocAndField> locAndFields = new ArrayList<>();
    private SharedPrefrenceHelper helper;
    private ParentPojo p = new ParentPojo();
    private LocAndField locAndField = null;
    private int locType = 1;
    private ArrayList<Marker> markers = new ArrayList<>();
    private double latpick;
    private double longpick;
    private boolean isMovingCamera = false;
    private boolean firstLoad = true;
    private ImageView pinpinIv;

    private CardView currentLocationCv;
    private View locationButton;

    private Handler handle = new Handler();

    private Runnable m_runnable = new Runnable() {
        @Override
        public void run() {
            locType = 1;
            new Manager_GetAvailableDriversByVehicleETA(Activity_SetLocationOnMap.this, getReqAvailableDrivers(), Activity_SetLocationOnMap.this).execute();
            if (vehicleTimer > 0) {
                handle.postDelayed(m_runnable, (long) vehicleTimer);
            }
        }
    };

    private Runnable mNewCameraMoveCallback = new Runnable() {
        @Override
        public void run() {
            forFirstTimeOnly = false;
            try {
                preLat = mMap.getCameraPosition().target.latitude;
                preLon = mMap.getCameraPosition().target.longitude;
                totalTimeForArrival.setText("...");
                subname.setVisibility(View.GONE);
                Log.d(TAG, "run: run: run: 4");
                if (handle != null) {//replace driveLoc
                    handle.removeCallbacks(m_runnable);
                    handle.post(m_runnable);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (vehicleTimer > 0) {
                mCameraHandler.postDelayed(mNewCameraMoveCallback, (long) vehicleTimer);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonMethods.getInstance().setDarkAndNightColorGreyWhite(Activity_SetLocationOnMap.this);

        setContentView(R.layout.layout_search_from_map);

        initObject();
        init();

        try {
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        listener();

//        if (handle != null) {
//            handle.removeCallbacks(m_runnable);
//            handle.post(m_runnable);
//        }
    }

    @Override
    public void onConnected(Bundle bundle) {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        getLastLocationFromFusedApi();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length >= 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                startLocationUpdates();
            } else {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK) {
//            mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//            mDialog.setTitleText("Please wait .. ");
//            mDialog.setContentText("You will be taking to your current location");
//            mDialog.setCancelable(false);
//            mDialog.show();
            startLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            stopLocationUpdates();

          /*  try {
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
                    mGoogleApiClient.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } catch (Exception e) {
        }

    }

    private GoogleApiClient mGoogleClient;

    private synchronized void buildGoogleApiClient() {
        try {
            mGoogleClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
            mGoogleClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        pinpinIv = findViewById(R.id.pinpinIv);
        currentLocationCv = findViewById(R.id.currentLocationCv);
        totalTimeForArrival = findViewById(R.id.totalTimeForArrival);
        subname = findViewById(R.id.subname);
        backIv = findViewById(R.id.backIv);
        topIv = findViewById(R.id.topIv);
        doneBtn = findViewById(R.id.doneBtn);
        setFromTv = findViewById(R.id.setFromTv);

        setArrTv = findViewById(R.id.setArrTv);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
    }

    private void initObject() {
        sp = PreferenceManager.getDefaultSharedPreferences(Activity_SetLocationOnMap.this);
        helper = new SharedPrefrenceHelper(this);
        vehicleTimer = Float.parseFloat(sp.getString(Config.VEHICLEMOVEMENTTIMER, "0.0")) * 1000;
        mCameraHandler = new Handler();
    }

    double preLat = 0;
    double preLon = 0;
    String setFrom = "";
    boolean forFirstTimeOnly = false;

    private void initData() {
        try {
            setFrom = getIntent().getStringExtra("setFrom");
            locAndFields = getIntent().getParcelableArrayListExtra("key_locAndFieldArrayList");
            indexEditTextPosition = getIntent().getIntExtra("indexEditTextPosition", 0);
            Log.d(TAG, "initData: 222 :  " + setFrom + " : " + preLat + "," + preLon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        totalTimeForArrival.setText("N/A");

        forPickUp_DropOff_Via1_Via2();
        for_Home_Work();
/*
        if (setFrom != null) {
            if (setFrom.equalsIgnoreCase("pickup")) {
                totalTimeForArrival.setVisibility(View.VISIBLE);
                pinpinIv.setVisibility(GONE);
                setFromTv.setText("Pick Up");
            }

            if (setFrom.equalsIgnoreCase("dropoff")) {
                totalTimeForArrival.setVisibility(GONE);
                subname.setVisibility(GONE);
                pinpinIv.setVisibility(View.VISIBLE);
                setFromTv.setText("Drop off");
            }

            if (setFrom.equalsIgnoreCase("via 1")) {
                totalTimeForArrival.setVisibility(GONE);
                pinpinIv.setVisibility(GONE);
                setFromTv.setText("Via 1");
            }

            if (setFrom.equalsIgnoreCase("via 2")) {
                totalTimeForArrival.setVisibility(GONE);
                pinpinIv.setVisibility(GONE);
                setFromTv.setText("Via 2");
            }

            if (setFrom.equalsIgnoreCase("home")) {
                totalTimeForArrival.setVisibility(GONE);
                pinpinIv.setVisibility(GONE);
                setFromTv.setText("Home");
            }

            if (!locAndFields.get(indexEditTextPosition).getField().equals("")) {
                setArrTv.setText(locAndFields.get(indexEditTextPosition).getField());
                preLat = Double.parseDouble(locAndFields.get(indexEditTextPosition).getLat());
                preLon = Double.parseDouble(locAndFields.get(indexEditTextPosition).getLon());
                Log.d(TAG, "initData: 111 :  " + setFrom + " : " + preLat + "," + preLon);
            }

            if (locAndFields.get(indexEditTextPosition).getField().equals("")) {
                setArrTv.setText(HomeFragment.locsName);
                preLat = HomeFragment.lats;
                preLon = HomeFragment.lons;
                Log.d(TAG, "initData: 222 :  " + setFrom + " : " + preLat + "," + preLon);
            }

            locAndField = new LocAndField();

            if (locAndFields.get(indexEditTextPosition).getField().equals(""))
                locAndField.setField(HomeFragment.locsName);
            else
                locAndField.setField(locAndFields.get(indexEditTextPosition).getField());

            locAndField.setLat("" + preLat);
            locAndField.setLon("" + preLon);
            locAndField.setLocationType("Address");
            locAndField.setDoorNo("");

            Log.d(TAG, "initData: 333 :  " + setFrom + " : " + preLat + "," + preLon);
            forFirstTimeOnly = true;
            if (handle != null) {
                handle.removeCallbacks(m_runnable);
                handle.post(m_runnable);
            }
        }
*/

        checkLocationIsEnabled();
    }

    private void forPickUp_DropOff_Via1_Via2() {
        try {
            if (setFrom != null) {
                if (setFrom.equalsIgnoreCase("pickup")) {
                    totalTimeForArrival.setVisibility(View.VISIBLE);
                    pinpinIv.setVisibility(GONE);
                    setFromTv.setText("Pick Up");
                }

                if (setFrom.equalsIgnoreCase("dropoff")) {
                    totalTimeForArrival.setVisibility(GONE);
                    subname.setVisibility(GONE);
                    pinpinIv.setVisibility(View.VISIBLE);
                    setFromTv.setText("Drop off");
                }

                if (setFrom.equalsIgnoreCase("via 1")) {
                    totalTimeForArrival.setVisibility(GONE);
                    pinpinIv.setVisibility(View.VISIBLE);
                    subname.setVisibility(GONE);
                    setFromTv.setText("Via 1");
                }

                if (setFrom.equalsIgnoreCase("via 2")) {
                    totalTimeForArrival.setVisibility(GONE);
                    pinpinIv.setVisibility(View.VISIBLE);
                    subname.setVisibility(GONE);
                    setFromTv.setText("Via 2");
                }

                if (!locAndFields.get(indexEditTextPosition).getField().equals("")) {
                    setArrTv.setText(locAndFields.get(indexEditTextPosition).getField());
                    preLat = Double.parseDouble(locAndFields.get(indexEditTextPosition).getLat());
                    preLon = Double.parseDouble(locAndFields.get(indexEditTextPosition).getLon());
                    Log.d(TAG, "initData: 111 :  " + setFrom + " : " + preLat + "," + preLon);
                }

                if (locAndFields.get(indexEditTextPosition).getField().equals("")) {
                    setArrTv.setText(HomeFragment.locsName);
                    preLat = HomeFragment.lats;
                    preLon = HomeFragment.lons;
                    Log.d(TAG, "initData: 222 :  " + setFrom + " : " + preLat + "," + preLon);
                }

                locAndField = new LocAndField();

                if (locAndFields.get(indexEditTextPosition).getField().equals(""))
                    locAndField.setField(HomeFragment.locsName);
                else
                    locAndField.setField(locAndFields.get(indexEditTextPosition).getField());

                locAndField.setLat("" + preLat);
                locAndField.setLon("" + preLon);
                locAndField.setLocationType("Address");
                locAndField.setDoorNo("");

                Log.d(TAG, "initData: 333 :  " + setFrom + " : " + preLat + "," + preLon);
                forFirstTimeOnly = true;
                if (handle != null) {
                    handle.removeCallbacks(m_runnable);
                    handle.post(m_runnable);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void for_Home_Work() {
        if (setFrom.equalsIgnoreCase("home")) {
            totalTimeForArrival.setVisibility(GONE);
            pinpinIv.setVisibility(View.VISIBLE);
            subname.setVisibility(GONE);

            setFromTv.setText("Home");
            preLat = HomeFragment.lats;
            preLon = HomeFragment.lons;
            setArrTv.setText(HomeFragment.locsName);
            forFirstTimeOnly = true;
            if (handle != null) {
                handle.removeCallbacks(m_runnable);
                handle.post(m_runnable);
            }
        }

        if (setFrom.equalsIgnoreCase("work")) {
            totalTimeForArrival.setVisibility(GONE);
            pinpinIv.setVisibility(View.VISIBLE);
            subname.setVisibility(GONE);

            setFromTv.setText("Work");
            preLat = HomeFragment.lats;
            preLon = HomeFragment.lons;
            setArrTv.setText(HomeFragment.locsName);
            if (handle != null) {
                handle.removeCallbacks(m_runnable);
                handle.post(m_runnable);
            }
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
                        status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listener() {
        currentLocationCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forFirstTimeOnly = false;
                if (mMap != null) {
                    if (locationButton != null)
                        locationButton.callOnClick();
                }

            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (setFrom.toLowerCase().startsWith("home") || setFrom.toLowerCase().startsWith("work")) {
                    Intent intent = new Intent(Activity_SetLocationOnMap.this, Activity_SearchAddressForHomeAndWork.class);
                    intent.putExtra("setFrom", setFrom);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Activity_SetLocationOnMap.this, Activity_SearchAddress.class);
                    intent.putParcelableArrayListExtra("key_locAndFieldArrayList", locAndFields);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick:setFraaaaaaaom =  " + setFrom);

                try {
                    if (setFrom.equalsIgnoreCase("via 1") || setFrom.equalsIgnoreCase("via 2") || setFrom.toLowerCase().trim().startsWith("pick") || setFrom.toLowerCase().trim().startsWith("drop")) {
                        workDone();
                    } else {

                        if (setFrom.toLowerCase().trim().equals("home")) {
                            helper.putLocAndFieldModel(helper.getSettingModel().getEmail() + "_" + KEY_HOME, locAndField);
                            finish();
                        }
                        if (setFrom.toLowerCase().trim().equals("work")) {
                            helper.putLocAndFieldModel(helper.getSettingModel().getEmail() + "_" + KEY_OFFICE, locAndField);
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onClick: " + e.getMessage());
                }
            }
        });

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                try {
                    mMap = googleMap;

                    setDarkAndNightThemeColor();

                    if (Build.VERSION.SDK_INT >= 23) {

                        if (ActivityCompat.checkSelfPermission(Activity_SetLocationOnMap.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Activity_SetLocationOnMap.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_SetLocationOnMap.this);
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

                        mMap.setMyLocationEnabled(true);
                    }

                    mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onCameraMoveStarted(int i) {
                            if (i == REASON_GESTURE) {
                                isMovingCamera = true;
                                firstLoad = false;
                            }
                        }
                    });


                    mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {

                            if (isMovingCamera) {
                                setArrTv.setText("Getting Location ..\n");

                                if (mCameraHandler != null) {
                                    mCameraHandler.removeCallbacks(mNewCameraMoveCallback);
                                    mCameraHandler.postDelayed(mNewCameraMoveCallback, 500);
                                }
                            }
                        }
                    });


                    View mapView = mapFragment.getView();
                    if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
                        locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                        locationButton.setVisibility(GONE);
                    }

                    try {
                        mMap.setMyLocationEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mMap.getUiSettings().setCompassEnabled(false);
                    // checkLocationIsEnabled();


                    CameraPosition cameraPosition = new CameraPosition.Builder().
                            target(new LatLng(preLat, preLon)).
                            zoom(16).
                            bearing(0).
                            build();

                    CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    if (update != null && mMap != null) {
                        mMap.animateCamera(update);
                    }
//                    if (handle != null) {
//                        handle.removeCallbacks(m_runnable);
//                        handle.postDelayed(m_runnable, 1500);
//                    }

//                    if (!latlng.isEmpty()) {
//
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        String provider = manager.getBestProvider(new Criteria(), true);

                        if (provider != null) {

                            if (ActivityCompat.checkSelfPermission(Activity_SetLocationOnMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Activity_SetLocationOnMap.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                Location loc = manager.getLastKnownLocation(provider);
                                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 16);
                                mMap.animateCamera(update);
                            }
                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) {
                    Log.d("TAG", "onLocationResult: Location is null");
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    Log.d("TAG", "onLocationResult: " + location.getLatitude() + " " + location.getLongitude());

                    if (location != null) {

                        latpick = location.getLatitude();
                        longpick = location.getLongitude();

                        if (handle != null) {
                            Log.d(TAG, "run: run: run: 1");
                            handle.removeCallbacks(m_runnable);
                            handle.post(m_runnable);
                        }

                        stopLocationUpdates();

                        break;
                    }
                }
            }
        };
    }

    private List<String> movingDrivers = new ArrayList<>();

    @Override
    public void onComplete(String result) {
        result = checkIfHasNullForString(result);

        if (result.length() > 0) {
            try {
                JSONObject parentObject = new JSONObject(result);
                if (parentObject.getBoolean("HasError")) {
                } else {
                    JSONObject dataObject = parentObject.optJSONObject("Data");

                    if (dataObject != null) {

                        JSONObject object = dataObject.optJSONObject("availableDriverDetails");

                        String locationName = object.optString("LocationName");

                        if (!forFirstTimeOnly) {
                            setArrTv.setText(locationName);
                        }

                        try {
                            locAndField = new LocAndField();
                            locAndField.setField(locationName);
                            locAndField.setLat("" + latpick);
                            locAndField.setLon("" + longpick);
                            locAndField.setLocationType("Address");
                            locAndField.setDoorNo("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (dataObject.has("listofavailabledrivers")) {

                            if (setFrom.toLowerCase().startsWith("pick")) {
                                pinpinIv.setVisibility(GONE);
                                totalTimeForArrival.setVisibility(View.VISIBLE);
                                if (totalTimeForArrival.equals("N/A")) {
                                    subname.setVisibility(GONE);
                                } else {
                                    subname.setVisibility(View.VISIBLE);
                                }
                            } else {
                                totalTimeForArrival.setVisibility(GONE);
                                pinpinIv.setVisibility(View.VISIBLE);
                                subname.setVisibility(GONE);
                            }

                            JSONArray listOfAvailableDrivers = dataObject.getJSONArray("listofavailabledrivers");

                            setAvailableDriver(listOfAvailableDrivers);

                            for (int i = 0; i < driverslist1.size(); i++) {

                                MarkerOptions marker = new MarkerOptions().position(new LatLng(driverslist1.get(i).getLatitude(), driverslist1.get(i).getLongitude()));

                                if (driverslist1.get(i).getNearest().equals("1")) {

                                    if (this != null && getResources() != null && getResources() != null) {
                                        if (!log.equals("")) {
                                            if (log.contains("\r\n")) {
                                                log = log.replace("\r\n", "");
                                            }
                                        }
                                    }

                                    Bitmap bitmap = getThumbnail("selectedCar.toLowerCase()" + ".png");

                                    if (bitmap == null) {
                                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.avcar));
                                    } else {
                                        marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                    }
                                } else {
                                    Bitmap bitmap = getThumbnail("selectedCar.toLowerCase()" + ".png");
                                    if (bitmap == null) {
                                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.avcar));
                                    } else {
                                        marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
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
                                                                runOnUiThread(new Runnable() {
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
                            Log.d(TAG, "listener: " + log);
                            log = log.replace("Min(s)", "");
                            if (log.trim().equals("") || log.trim().equals("N/A")) {
                                log = "N/A";
                                subname.setVisibility(View.GONE);
                            } else {
                                subname.setVisibility(View.VISIBLE);
                            }
                            totalTimeForArrival.setText(log + "");

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
                        } else {
                            if (setFrom.toLowerCase().startsWith("pick")) {
                                pinpinIv.setVisibility(GONE);
                                totalTimeForArrival.setVisibility(View.VISIBLE);
                                if (totalTimeForArrival.equals("N/A")) {
                                    subname.setVisibility(GONE);
                                } else {
                                    subname.setVisibility(View.VISIBLE);
                                }
                            } else {
                                totalTimeForArrival.setVisibility(GONE);
                                pinpinIv.setVisibility(View.VISIBLE);
                                subname.setVisibility(GONE);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setArrTv.setText("Not found");
        }
    }

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

    private void animateMarkerNew(final LatLng destination, final Marker marker) {
        try {
            if (marker != null && marker.getTag() != null) {
                final LatLng startPosition = marker.getPosition();
                final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);
                LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

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
                            float v = animation.getAnimatedFraction();
                            LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                            marker.setPosition(newPosition);
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

    private boolean isMarkerRotating = false;

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

    private List<String> driverslist2 = new ArrayList<>();
    private List<Driver> driverslist1 = new ArrayList<>();

    String log = "";

    private void setAvailableDriver(JSONArray array) {
        try {
            if (vehicleTimer <= 0) {
                for (int i = 0; i < markers.size(); i++) {
                    markers.get(i).remove();
                }
                markers.clear();
                driverslist2.clear();
            }
            float[] tempDistance = new float[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Driver drive = new Driver();
                drive.setDriveNo(obj.getString("DriverNo"));
                currentMarkers.add(obj.getString("DriverNo"));
                drive.setLatitude(obj.getDouble("Latitude"));
                drive.setLongitude(obj.getDouble("Longitude"));
                if (!driverslist2.contains(obj.getString("DriverNo"))) {
                    driverslist2.add(obj.getString("DriverNo"));
                    MarkerOptions marker = new MarkerOptions().position(
                            new LatLng(
                                    drive.getLatitude(),
                                    drive.getLongitude()));
                    Bitmap bitmap = getThumbnail("selectedCar.toLowerCase()" + ".png");
                    if (bitmap == null) {
                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.avcar));
                    } else {
                        marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    }
                    Marker carMarker = mMap.addMarker(marker);
                    try {
                        carMarker.setRotation(getBearing(new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude), carMarker.getPosition()));
                    } catch (Exception e) {
                    }
                    carMarker.setTag(drive.getDriveNo());
                    markers.add(carMarker);
                }
                if (i == 0) {
                    drive.setNearest("1");
                    log = obj.getString("ETA");
                }
                driverslist1.add(drive);
                if (!log.equals("")) {
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public Bitmap getThumbnail(String filename) {
        Bitmap thumbnail = null;
        if (thumbnail == null) {
            try {
                File filePath = getFileStreamPath(filename);
                FileInputStream fi = new FileInputStream(filePath);
                thumbnail = BitmapFactory.decodeStream(fi);
            } catch (Exception ex) {
                Log.e(" on internal storage", ex.getMessage());
            }
        }
        return thumbnail;
    }

    private List<String> currentMarkers = new ArrayList<>();

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (fusedLocationClient != null) {
            fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        try {
            if (locationCallback != null) {
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public int getLocationMode() {
        try {
            return Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void workDone() {
        try {
            locAndFields.set(indexEditTextPosition, locAndField);

            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("key_locAndFieldArrayList", locAndFields);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String data = getIntent().getStringExtra("type");
//        if (data == null) data = "";

//        if (!data.equals("")) {
//            if (data.equals("home") || data.equals("work")) {
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra(Activity_SearchAddress.KEY_RESULT_BOOKING, locAndFields.get(indexEditTextPosition).getField());
//                returnIntent.putExtra(Activity_SearchAddress.KEY_RESULT_LATITUDE, locAndFields.get(indexEditTextPosition).getLat());
//                returnIntent.putExtra(Activity_SearchAddress.KEY_RESULT_LONGITUDE, locAndFields.get(indexEditTextPosition).getLon());
//                setResult(RESULT_OK, returnIntent);
//                finish();
//            } else if (data.equals("home")) {
//                Intent intent = new Intent();
//                intent.putParcelableArrayListExtra("key_locAndFieldArrayList", locAndFields);
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        } else {
//            Intent intent = new Intent();
//            intent.putParcelableArrayListExtra("key_locAndFieldArrayList", locAndFields);
//            setResult(RESULT_OK, intent);
//            finish();
//        }
    }

    private void setDarkAndNightThemeColor() {
        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_night_style));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
    }


    private ReqAvailableDrivers getReqAvailableDrivers() {

        ReqAvailableDrivers reqAvailableDrivers = new ReqAvailableDrivers();
        reqAvailableDrivers.defaultClientId = (int) CommonVariables.localid;

        try {
            latpick = preLat;
            longpick = preLon;
        } catch (Exception e) {
            e.printStackTrace();
        }

        reqAvailableDrivers.latitude = latpick;
        reqAvailableDrivers.longitude = longpick;

        Log.d(TAG, "run: run: run: Location Name : " + preLat + "," + preLon);
        Log.d(TAG, "run: run: run: Location Name : " + reqAvailableDrivers.latitude + "," + reqAvailableDrivers.longitude);
        reqAvailableDrivers.vehicleName = "ANY VEHICLE";
        reqAvailableDrivers.locType = locType;
        reqAvailableDrivers.MapKey = CommonVariables.GOOGLE_API_KEY;
        reqAvailableDrivers.mapType = 0;
        reqAvailableDrivers.uniqueValue = CommonVariables.clientid + "4321orue";
        return reqAvailableDrivers;
    }
}
