/*
package base.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eurosoft.customerapp.R;
import com.support.parser.RegexPatterns;
import com.support.parser.SoapHelper;
import com.tfb.fbtoast.FBToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.support.parser.PropertyInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.listener.Listener_GetAddress;
import base.listener.Listener_GetAirportDetails;
import base.listener.Listener_GetFlightDetails;
import base.listener.Listener_GetLatLngFromPlaceId;
import base.manager.Manager_GetAddressFromGoogle;
import base.manager.Manager_GetAirportDetails;
import base.manager.Manager_GetFlightDetails;
import base.manager.Manager_GetLatLnfFromPlaceId;
import base.models.Model_BookingDetailsModel;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.listener.Listener_OnSetResult;
import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.databaseoperations.InitializeAppDb;
import base.listener.Listener_SendAddressArrayList;
import base.manager.Manager_GetAddressDetails;
//import base.miscactivities.NewBookingDetails;
//import base.miscactivities.NewHomeSelect;
import base.utils.Config;
import base.models.LocAndField;
import base.utils.SharedPrefrenceHelper;
import base.models.ParentPojo;
import base.newui.HomeFragment;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ExActivity_SearchAddress extends AppCompatActivity implements
        Listener_OnSetResult,
        Listener_GetAddress,
        Listener_GetFlightDetails,
        Listener_GetAirportDetails,
        Listener_GetLatLngFromPlaceId {
    private static final String TAG = "NeNewBookingDetails";


    */
/*   Boxes  VIEWS  Start *//*

    private RelativeLayout pickup_box_Rl;
    private EditText pickupEt;

    private RelativeLayout via_1_box_Rl;
    private EditText via_1_Et;
    private ImageView delete_via_1_Iv;

    private RelativeLayout via_2_box_Rl;
    private EditText via_2_Et;
    private ImageView delete_via_2_Iv;

    private RelativeLayout dropoff_box_Rl;
    private EditText dropoffEt;

    */
/*   Boxes  VIEWS  End *//*



    private void setBoxViews(View v) {
        pickup_box_Rl = v.findViewById(R.id.pickup_box_Rl);
        pickupEt = v.findViewById(R.id.pickupEt);

        via_1_box_Rl = v.findViewById(R.id.via_1_box_Rl);
        via_1_box_Rl.setVisibility(GONE);
        via_1_Et = v.findViewById(R.id.via_1_Et);
        delete_via_1_Iv = v.findViewById(R.id.delete_via_1_Iv);

        via_2_box_Rl = v.findViewById(R.id.via_2_box_Rl);
        via_2_box_Rl.setVisibility(GONE);
        via_2_Et = v.findViewById(R.id.via_2_Et);
        delete_via_2_Iv = v.findViewById(R.id.delete_via_2_Iv);

        dropoff_box_Rl = v.findViewById(R.id.dropoff_box_Rl);
        dropoffEt = v.findViewById(R.id.dropoffEt);
    }


    // VIEWS
    private RelativeLayout chooseFromMap;
    private RelativeLayout homeRl;
    private RelativeLayout workRl;
    private LinearLayout homeAndWorkLl;

    private RecyclerView searchEditTextRv;
    private RecyclerView recentListRv;
    private RecyclerView rview;

    private EditText flightEditText;
    private ProgressBar progressBar;
    private ProgressBar filghtProgreeBar;

    private TextView setLocationOnMapLabel;
    private TextView setHomeTv;
    private TextView setWorkTv;
    private TextView addViaTv;
    private TextView hTv;
    private TextView wTv;

    private ImageView backIv;
    private ImageView deleteHomeIv;
    private ImageView deleteWorkIv;

    private Dialog dialog;
    private CardView doneBtn;

    // JAVA

    // STATIC
    public static final int HOME_CODE = 1;
    public static final int WORK_CODE = 2;

    public static final String KEY_Pick_Obj = "KEY_Pick_Obj";
    public static final String KEY_Drop_Obj = "KEY_Drop_Obj";
    public static final String KEY_RESELECT = "AddressReselect";
    public static final String ADDRESS = "Address";
    public static final String KEY_SHOW_WHAT = "ActivityString";
    public static final String SHOW_FOR_PICKUP = "Pickup";
    public static final String SHOW_FOR_DROPOFF = "Drop";
    public static final String KEY_SHOW_FOR = "ShowFor";
    public static final String KEY_HOME = "key_home";
    public static final String KEY_OFFICE = "key_office";


    public static final String KEY_RESULT_LATITUDE = "lat";
    public static final String KEY_RESULT_LONGITUDE = "lon";
    public static final String KEY_RESULT_BOOKING = "result";

    public static final String KEY_RESULT_BOOKING_drop = "result_drop";
    public static final String KEY_RESULT_LATITUDE_drop = "lat_drop";
    public static final String KEY_RESULT_LONGITUDE_drop = "lon_drop";


    public TextView dateTimeEt;

    // STRING
    private String mSearchString;
    private String allowanceTime = "";
    private String placeId = "";

    // INTEGER
    private int flightPosition = -1;
    private int indexEditTextPosition = 1;
    private int locAndFieldArrayListPosition = -1;
    private int mYear, mMonth, mDay, mHour, mMinute;

    // BOOLEAN
    private boolean isSelected = false;
    private boolean isAlreadyAvailable = false;
    private boolean stopCallBacks = false;
    private boolean isRunningGetAddress = false;
    private boolean isSearching = false;

    // CLASS
    private SharedPreferences sp = null;
    private LocAndField workAddress = null;
    private LocAndField homeAddress = null;
    private SharedPrefrenceHelper sharedPrefrenceHelper = null;
    private ParentPojo p = new ParentPojo();

    // ARRAY_LIST
    private ArrayList<LocAndField> savedList = new ArrayList<>();
    private ArrayList<Flight> flightNoArrayList = new ArrayList<>();
    private ArrayList<LocAndField> locAndFieldArrayList = new ArrayList<>();
    private ArrayList<LocAndField> searchEditTextArrayList = new ArrayList<>();

    // ADAPTER
    private RecentListAdapter recentListAdapter;
    private SearchEditTextAdapter searchEditTextAdapter;

    // Listeners

    // Listener
//    private Listener_SendAddressArrayList listListener;
//    private Listener_GetAddress listener_getAddress;
    // Handlers
    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if (!isRunningGetAddress && sp.getString(CommonVariables.enableOutsideUK, "0").equals("0")) {
                new Manager_GetAddressDetails(getActivity(), mSearchString, Activity_SearchAddress.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);  // 1 = Inside UK
            } else {
                new Manager_GetAddressFromGoogle(getActivity(), mSearchString, ExActivity_SearchAddress.this);   // 0 = Outside UK
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonMethods.getInstance().setDarkAndNightColorGreyWhite(ExActivity_SearchAddress.this);

        setContentView(R.layout.layout_search__address);
        setObjects();

        init();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        listener();
    }

    @Override
    public void setResult(Intent data) {
//        arrangeHomeAndWorkData(data);
        setHome();
        setWork();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5051 && resultCode == RESULT_OK) {
//            doneBtn.setVisibility(VISIBLE);
            try {
                setInitialData(data);

                if (searchEditTextArrayList.size() >= 2 && (!searchEditTextArrayList.get(1).getField().equals("") && !searchEditTextArrayList.get(1).getField().equals("null"))) {
                    makeRoute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHome();
        setWork();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HomeFragment.clickFrom = "pickup";
    }

    @Override
    public void onStart(boolean isStarted) {
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isRunningGetAddress = true;
    }

    private void setObjects() {
        mHandler = new Handler();
        sharedPrefrenceHelper = new SharedPrefrenceHelper(getActivity());
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        CommonVariables.Clientip = sp.getString(Config.ClientIp, "");
        CommonVariables.GOOGLE_API_KEY = sp.getString(Config.MapKey, "");
        InitializeAppDb.LAT = sp.getString(Config.BaseLat, "");
        InitializeAppDb.LNG = sp.getString(Config.BaseLng, "");
        HomeFragment.specialInstruction = "";
        HomeFragment.fromComing = "";
        HomeFragment.fromDoorNo = "";
    }

    private Activity getActivity() {
        return this;
    }

    private Activity getContext() {
        return this;
    }

    private void listener() {

        deleteHomeIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!hTv.getText().toString().equalsIgnoreCase(p.getAddHome()))
                        new SweetAlertDialog(ExActivity_SearchAddress.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(p.getAreYouSure())
                                .setContentText(p.getWannaDeleteHome() + "?")
                                .setCancelText(p.getNo())
                                .setConfirmText(p.getYes())
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        new SweetAlertDialog(ExActivity_SearchAddress.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText(p.getDeletedSuccessFully())
                                                .setContentText(p.getHomeLocationDeleted())
                                                .show();
                                        sharedPrefrenceHelper.removeAddressModel(sharedPrefrenceHelper.getSettingModel().getEmail() + "_" + KEY_HOME);
                                        setHome();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        deleteWorkIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!wTv.getText().toString().equalsIgnoreCase(p.getAddWork()))
                        new SweetAlertDialog(ExActivity_SearchAddress.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(p.getAreYouSure())
                                .setContentText(p.getWannaDeleteWork() + "?")
                                .setCancelText(p.getNo())
                                .setConfirmText(p.getYes())
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        new SweetAlertDialog(ExActivity_SearchAddress.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText(p.getDeletedSuccessFully())
                                                .setContentText(p.getWorkLocationDeleted())
                                                .show();
                                        sharedPrefrenceHelper.removeAddressModel(sharedPrefrenceHelper.getSettingModel().getEmail() + "_" + KEY_OFFICE);
                                        setWork();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        backIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopCallBacks = true;
                if (mHandler != null) {
                    mHandler.removeCallbacks(mRunnable);
                }
//                Intent intent = new Intent();
//                intent.putParcelableArrayListExtra("key_locAndFieldArrayList", searchEditTextArrayList);
//                setResult(RESULT_OK, intent);
                finish();
            }
        });

        addViaTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addViaTv.getText().toString().length() == 0) {
                    return;
                }

                if (searchEditTextArrayList.size() > 3) {
                    Toast.makeText(getContext(), "Can't add more than 2 Via", Toast.LENGTH_SHORT).show();
                    return;
                }

                LocAndField locAndField = searchEditTextArrayList.get(searchEditTextArrayList.size() - 1);
                searchEditTextArrayList.add(locAndField);

                locAndField = new LocAndField();
                searchEditTextArrayList.set(searchEditTextArrayList.size() - 2, locAndField);

                for (int i = 0; i < searchEditTextArrayList.size(); i++) {
                    searchEditTextArrayList.get(i).setProgressShowing("false");
                }

                isSelected = false;
                searchEditTextAdapter = new SearchEditTextAdapter(getContext(), searchEditTextArrayList);
                searchEditTextRv.setAdapter(searchEditTextAdapter);

                for (int i = 0; i < searchEditTextArrayList.size(); i++) {
                    if (searchEditTextArrayList.get(i).getField().equals("")) {
                        count = i;
                        break;
                    }
                }
                searchEditTextAdapter.setFocus(count, false);
            }
        });

        chooseFromMap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), Activity_SetLocationOnMap.class);
                if (searchEditTextArrayList.size() == 2 && indexEditTextPosition == 0) {
                    intent.putExtra("setFrom", "pickup");

                } else if (searchEditTextArrayList.size() == 2 && indexEditTextPosition == 1) {
                    intent.putExtra("setFrom", "dropoff");
                } else {
                    intent.putExtra("setFrom", "via");
                }

                intent.putExtra("indexEditTextPosition", indexEditTextPosition);
                intent.putParcelableArrayListExtra("key_locAndFieldArrayList", searchEditTextArrayList);
                startActivityForResult(intent, 5051);
            }
        });

        doneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                makeRoute();
            }
        });
//        doneBtn.setVisibility(VISIBLE);
        homeRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hTv.getText().toString().toLowerCase().trim().equals("add home")) {
                    startActivity(new Intent(ExActivity_SearchAddress.this, Activity_SearchAddressForHomeAndWork.class).putExtra("type", "home"));
                } else {
                    boolean alreadyPresend = false;
                    for (LocAndField locAndField : searchEditTextArrayList) {
                        if (locAndField.getField().trim().equalsIgnoreCase(homeAddress.getField().trim())) {
                            alreadyPresend = true;
                            break;
                        }
                    }

                    if (alreadyPresend) {
                        FBToast.warningToast(ExActivity_SearchAddress.this, "Address already present", FBToast.LENGTH_SHORT);
                        return;
                    }

                    homeAddress.setSelected(true);
                    searchEditTextArrayList.set(indexEditTextPosition, homeAddress);
                    isSelected = true;

                    for (int i = 0; i < searchEditTextArrayList.size(); i++) {
                        if (searchEditTextArrayList.get(i).getField().equals("")) {
                            count = i;
                            break;
                        }
                    }
                    searchEditTextAdapter.setFocus(count, false);


                    try {
                        if (searchEditTextArrayList.size() > 1) {
                            boolean isEmptyEt = false;
                            for (int i = 0; i < searchEditTextArrayList.size(); i++) {
                                if (searchEditTextArrayList.get(i).getField().equals("")) {
                                    isEmptyEt = true;
                                    break;
                                }
                            }
                            if (!isEmptyEt) {
                                makeRoute();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        workRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wTv.getText().toString().toLowerCase().trim().equals("add work"))
                    startActivity(new Intent(ExActivity_SearchAddress.this, Activity_SearchAddressForHomeAndWork.class).putExtra("type", "work"));
                else {
                    boolean alreadyPresend = false;
                    for (LocAndField locAndField : searchEditTextArrayList) {
                        if (locAndField.getField().trim().equalsIgnoreCase(workAddress.getField().trim())) {
                            alreadyPresend = true;
                            break;
                        }
                    }

                    if (alreadyPresend) {
                        FBToast.warningToast(ExActivity_SearchAddress.this, "Address already present", FBToast.LENGTH_SHORT);
                        return;
                    }

                    workAddress.setSelected(true);
                    searchEditTextArrayList.set(indexEditTextPosition, workAddress);
                    isSelected = true;

                    for (int i = 0; i < searchEditTextArrayList.size(); i++) {
                        if (searchEditTextArrayList.get(i).getField().equals("")) {
                            count = i;
                            break;
                        }
                    }
                    searchEditTextAdapter.setFocus(count, false);

                    try {
                        if (searchEditTextArrayList.size() > 1) {
                            boolean isEmptyEt = false;
                            for (int i = 0; i < searchEditTextArrayList.size(); i++) {
                                if (searchEditTextArrayList.get(i).getField().equals("")) {
                                    isEmptyEt = true;
                                    break;
                                }
                            }
                            if (!isEmptyEt) {
                                makeRoute();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        searchEditTextRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Hide keyboard code
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(ExActivity_SearchAddress.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    private void makeRoute() {
        if (locAndFieldArrayList.size() > 2 && (locAndFieldArrayList.get(1).getField() == null)) {
            Toast.makeText(getContext(), "Via should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < searchEditTextArrayList.size(); i++) {
            LocAndField l = searchEditTextArrayList.get(i);
            if (l.getField().equals("")) {
                searchEditTextArrayList.remove(i);
                searchEditTextAdapter.notifyDataSetChanged();
                break;
            }
        }

        try {
            HomeFragment.clickFrom = "pickup";
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("key_locAndFieldArrayList", searchEditTextArrayList);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        homeAndWorkLl = findViewById(R.id.homeAndWorkLl);

        setLocationOnMapLabel = findViewById(R.id.setLocationOnMapLabel);
        setLocationOnMapLabel.setText(p.getSetLocationOnMapLabel());

        progressBar = findViewById(R.id.progressBar);
        backIv = findViewById(R.id.backIv);

        deleteHomeIv = findViewById(R.id.deleteHomeIv);
        deleteWorkIv = findViewById(R.id.deleteWorkIv);

        searchEditTextRv = findViewById(R.id.searchEditTextRv);
        searchEditTextRv.setLayoutManager(new LinearLayoutManager(getContext()));
        searchEditTextRv.setHasFixedSize(true);

        recentListRv = findViewById(R.id.recentListRv);
        recentListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        recentListRv.setHasFixedSize(true);

        chooseFromMap = findViewById(R.id.chooseFromMap);

        hTv = findViewById(R.id.hTv);
        setHomeTv = findViewById(R.id.setHomeTv);
        homeRl = findViewById(R.id.homeRl);

        wTv = findViewById(R.id.wTv);
        setWorkTv = findViewById(R.id.setWorkTv);
        workRl = findViewById(R.id.workRl);

        addViaTv = findViewById(R.id.addViaTv);
        addViaTv.setText(" + " + p.getAddVia());
        backIv = findViewById(R.id.backIv);
        doneBtn = findViewById(R.id.doneBtn);

        setInitialData(getIntent());
        setRecentListInitialData();

    }

    private void setHome() {
        homeAddress = sharedPrefrenceHelper.getLocAndFieldModel(sharedPrefrenceHelper.getSettingModel().getEmail() + "_" + KEY_HOME);

        if (homeAddress != null && !homeAddress.equals("null") && !homeAddress.getField().equals("")) {
            hTv.setText(p.getHome());
            setHomeTv.setVisibility(VISIBLE);
            deleteHomeIv.setVisibility(VISIBLE);
            setHomeTv.setText(homeAddress.getField());
        } else {
            hTv.setText(p.getAddHome());
            setHomeTv.setVisibility(GONE);
            deleteHomeIv.setVisibility(GONE);
        }
    }

    private void setWork() {
        workAddress = sharedPrefrenceHelper.getLocAndFieldModel(sharedPrefrenceHelper.getSettingModel().getEmail() + "_" + KEY_OFFICE);

        if (workAddress != null && !workAddress.equals("null") && !workAddress.getField().equals("")) {
            wTv.setText(p.getWork());
            setWorkTv.setVisibility(VISIBLE);
            deleteWorkIv.setVisibility(VISIBLE);
            setWorkTv.setText(workAddress.getField());
        } else {
//            wTv.setText("ADD WORK");
            wTv.setText(p.getAddWork());
            setWorkTv.setVisibility(GONE);
            deleteWorkIv.setVisibility(GONE);
        }
    }

    private void setInitialData(Intent data) {

        try {
            addViaTv.setText((sp.getString(CommonVariables.enableVia, "1").equals("0")) ? "" : " + Add (Via)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchEditTextArrayList.clear();
        try {

            ArrayList<LocAndField> locAndFields = data.getParcelableArrayListExtra("key_locAndFieldArrayList");
            addDataForEditTextList(locAndFields);

            for (int i = 0; i < searchEditTextArrayList.size(); i++)
                searchEditTextArrayList.get(i).setProgressShowing("false");

            if (searchEditTextArrayList.size() == 2 && searchEditTextArrayList.get(searchEditTextArrayList.size() - 1).getField().equals("")) {
                count = 1;
            }
            searchEditTextAdapter = new SearchEditTextAdapter(getContext(), searchEditTextArrayList);
            searchEditTextRv.setAdapter(searchEditTextAdapter);
            searchEditTextAdapter.notifyDataSetChanged();
            for (int i = 0; i < searchEditTextArrayList.size(); i++) {
                if (searchEditTextArrayList.get(i).getField().equals("")) {
                    count = i;
                    break;
                }
            }
            searchEditTextAdapter.setFocus(count, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if ((HomeFragment.clickFrom.equals("drop")) && searchEditTextArrayList.size() == 2) {
                if (searchEditTextArrayList.get(0).getField().equals("")) {
                    indexEditTextPosition = 0;
                } else {
                    indexEditTextPosition = 1;
                }
            } else {
                indexEditTextPosition = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            indexEditTextPosition = 0;
        }
        try {
            searchEditTextAdapter.setFocus(indexEditTextPosition, false);
            searchEditTextAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addDataForEditTextList(ArrayList<LocAndField> locAndFields) {
        try {
            for (LocAndField locAndField : locAndFields) {
                LocAndField l = new LocAndField();
               */
/* l.setProgressShowing("false");
                try {
                    l.setField("" + l.getField());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    l.setLat("" + locAndField.getLat());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    l.setLon("" + locAndField.getLon());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    l.setAddressType("" + locAndField.getLocationType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    l.setDoorNo("" + locAndField.getDoor());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    l.setAddressModel(l.getA);
                } catch (Exception e) {
                    e.printStackTrace();
                }*//*

                if (l.getField().equals("")) {
                    searchEditTextArrayList.add(locAndField);
                }
            }
            if (locAndFields.size() == 1) {
                searchEditTextArrayList.add(new LocAndField());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRecentListInitialData() {
        try {
            ArrayList<Model_BookingDetailsModel> modelBookingDetailsModels = new DatabaseOperations(new DatabaseHelper(getActivity())).getHistoryBookingsDetails();
            List<String> ToAdd = new ArrayList<String>();

            for (int i = 0; i < modelBookingDetailsModels.size(); i++) {
                LocAndField bookingObj = new LocAndField();
                bookingObj.setField(modelBookingDetailsModels.get(i).getFromAddress());
                bookingObj.setDoorNo(modelBookingDetailsModels.get(i).getFromAddressDoorNO());
                bookingObj.setLat(modelBookingDetailsModels.get(i).getPickupLat());
                bookingObj.setLon(modelBookingDetailsModels.get(i).getPickupLon());
                bookingObj.setLocationType(modelBookingDetailsModels.get(i).getFromAddressType());

                LocAndField bookingObj2 = new LocAndField();
                bookingObj2.setField(modelBookingDetailsModels.get(i).gettoAddress());
                bookingObj2.setDoorNo(modelBookingDetailsModels.get(i).gettoAddressDoorNO());
                bookingObj2.setLat(modelBookingDetailsModels.get(i).getDropLat());
                bookingObj2.setLon(modelBookingDetailsModels.get(i).getDropLon());
                bookingObj2.setLocationType(modelBookingDetailsModels.get(i).gettoAddressType());

                if (i > 0) {
                    if (savedList.size() == 0) {
                        savedList.add(bookingObj);
                    }

                    if (savedList.size() > 0 && !ToAdd.contains(bookingObj.getField())) {
                        savedList.add(bookingObj);
                        ToAdd.add(bookingObj.getField());
                    }

                    if (savedList.size() > 0 && !ToAdd.contains(bookingObj2.getField())) {
                        savedList.add(bookingObj2);
                        ToAdd.add(bookingObj2.getField());
                    }

                } else {
                    savedList.add(bookingObj);
                    savedList.add(bookingObj2);
                    ToAdd.add(bookingObj.getField());
                    ToAdd.add(bookingObj2.getField());
                }
            }

            if (savedList.size() == 0) {
                recentListRv.setVisibility(View.GONE);
            } else {
                recentListRv.setVisibility(View.VISIBLE);
            }

            recentListAdapter = new RecentListAdapter(getContext(), savedList);
            recentListRv.setAdapter(recentListAdapter);
            recentListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void search() {
        mHandler.removeCallbacks(mRunnable);
        isRunningGetAddress = false;
        mHandler.postDelayed(mRunnable, 1000);
    }

    // Not For Google
    private void setData(String locationType) {
        isSelected = true;
        locAndFieldArrayList.get(locAndFieldArrayListPosition).setLocationType(locationType);
        locAndFieldArrayList.get(locAndFieldArrayListPosition).setSelected(true);
        searchEditTextArrayList.set(indexEditTextPosition, locAndFieldArrayList.get(locAndFieldArrayListPosition));
        searchEditTextAdapter.notifyDataSetChanged();

        for (int i = 0; i < searchEditTextArrayList.size(); i++) {
            if (searchEditTextArrayList.get(i).getField().equals("")) {
                count = i;
                break;
            }
        }
        searchEditTextAdapter.setFocus(count, false);
    }

    // For Google
    private void setData(LocAndField locAndField, String locationType) {
        isSelected = true;
        locAndField.setLocationType(locationType);
        locAndField.setSelected(true);
        locAndField.setField(locAndFieldArrayList.get(locAndFieldArrayListPosition).getField());
        searchEditTextArrayList.set(indexEditTextPosition, locAndField);
        searchEditTextAdapter.notifyDataSetChanged();
    }

    private void openDateDialog() {
        try {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);
                    String dd = CommonVariables.getFormattedDateStr(calendar, "dd/MM/yyyy");

                    String curr = getCurrentTIme();
                    if (dd.equals(curr)) {
                        HomeFragment.flightDateForSchedule = getCurrentTIme();
                        dateTimeEt.setText("Today");
                    } else if (!dd.equals(getCurrentTIme())) {
                        try {
                            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.set(Calendar.HOUR_OF_DAY, 23);
                            calendar1.set(Calendar.MINUTE, 59);
                            calendar1.set(Calendar.SECOND, 59);

                            Date date1 = formater.parse(dd);

                            if (date1.getTime() > calendar1.getTime().getTime()) {
                                calendar.add(Calendar.HOUR_OF_DAY, 23);
                                if (date1.getTime() > calendar1.getTime().getTime()) {
                                    HomeFragment.flightDateForSchedule = dd;
                                    dateTimeEt.setText(HomeFragment.flightDateForSchedule);
                                } else {
                                    HomeFragment.flightDateForSchedule = dd;
                                    dateTimeEt.setText("Tomorrow");
                                }
                            } else {
                                HomeFragment.flightDateForSchedule = getCurrentTIme();
                                dateTimeEt.setText("Today");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (flightEditText.getText().toString().trim().length() == 0) {
                        return;
                    }
//                    new GetFlightDetails().execute(flightEditText.getText().toString().trim());
                    new Manager_GetFlightDetails(ExActivity_SearchAddress.this, flightEditText.getText().toString().trim(), ExActivity_SearchAddress.this).execute();
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAirportInfo(String message) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.input_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;

        EditText mins = dialog.findViewById(R.id.nameEt);

        mins.setVisibility(GONE);

        TextView cancelBtn = dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setVisibility(GONE);

        TextView title = dialog.findViewById(R.id.title);
        title.setVisibility(GONE);


        TextView titleTv = dialog.findViewById(R.id.titleTv);
        title.setVisibility(GONE);

        TextView subTitleTv = dialog.findViewById(R.id.subTitleTv);
        subTitleTv.setText(message);

        TextView confirmBtn = dialog.findViewById(R.id.confirmBtn);
        confirmBtn.setText(p.getOk());

        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                new SweetAlertDialog(ExActivity_SearchAddress.this, SweetAlertDialog.FLIGHT_INPUT)
                        .setTitleText(p.getFlightDetails())
                        .setContentText(p.getAlreadyAtAirport() + "?")
                        .setConfirmText(p.getYes())
                        .showCancelButton(true)
                        .setCancelText(p.getSelectFlightNo())
                        .setFlightListener(new SweetAlertDialog.OnSweetFlightListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog, String flightNo, String comingFrom, String atAirport) {
                                sweetAlertDialog.dismissWithAnimation();
                                makeFinalRoute(null, "Airport");
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                makeFinalRoute(null, "Airport");
                            }

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                searchForFlightNo();
                                sDialog.cancel();
                            }

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                            }
                        })
                        .show();
            }
        });
        dialog.show();
    }

    RelativeLayout buttonTextRl;
    ProgressBar flightProgressBar;

    private void searchForFlightNo() {
        dialog = new Dialog(getActivity(), android.R.style.Widget_DeviceDefault);
        dialog.setContentView(R.layout.layout_search_flight_no);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dateTimeEt = dialog.findViewById(R.id.dateTimeEt);
        flightEditText = dialog.findViewById(R.id.flightNoEt);
        RelativeLayout searchButton = dialog.findViewById(R.id.searchButton);
        buttonTextRl = dialog.findViewById(R.id.buttonTextRl);
        ImageView closeBottomSheet = dialog.findViewById(R.id.closeBottomSheet);
        rview = dialog.findViewById(R.id.rv);
        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        rview.setHasFixedSize(true);
        flightProgressBar = dialog.findViewById(R.id.flightProgressBar);

        flightEditText.requestFocus();
        flightEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!isSearching) {
//                        new GetFlightDetails().execute(flightEditText.getText().toString().trim());
                        new Manager_GetFlightDetails(ExActivity_SearchAddress.this, flightEditText.getText().toString().trim(), ExActivity_SearchAddress.this).execute();
                    }
                    return true;
                }
                return false;
            }
        });


        if (HomeFragment.flightDateForSchedule.trim().contains("Today")) {
            HomeFragment.flightDateForSchedule = getCurrentTIme();
            dateTimeEt.setText("Today");
        } else if (HomeFragment.flightDateForSchedule.trim().contains("Tomorrow")) {
            HomeFragment.flightDateForSchedule = HomeFragment.flightDateForSchedule.replace(" | Tomorrow", "");
            dateTimeEt.setText("Tomorrow");
        } else {
            dateTimeEt.setText(HomeFragment.flightDateForSchedule.trim());
        }

        dateTimeEt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });

        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flightEditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(getContext(), "Please Enter Flight Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isSearching) {
//                    new GetFlightDetails().execute(flightEditText.getText().toString().trim());
                    new Manager_GetFlightDetails(ExActivity_SearchAddress.this, flightEditText.getText().toString().trim(), ExActivity_SearchAddress.this).execute();
                }
            }
        });

        closeBottomSheet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void specialNotes() {
        final Dialog innerDialog = new Dialog(getActivity());
        innerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        innerDialog.setCancelable(true);
        innerDialog.setContentView(R.layout.input_dialog);
        Window window = innerDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;

        EditText allowanceTimeEt = innerDialog.findViewById(R.id.nameEt);
        allowanceTimeEt.requestFocus();

        TextView titleTv = innerDialog.findViewById(R.id.titleTv);
        titleTv.setVisibility(GONE);

        TextView confirmBtn = innerDialog.findViewById(R.id.confirmBtn);

        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allowanceTimeEt.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Please enter minutes", Toast.LENGTH_SHORT).show();
                    return;
                }

                allowanceTime = allowanceTimeEt.getText().toString();
                HomeFragment.specialInstruction = allowanceTime;
                innerDialog.dismiss();
                dialog.dismiss();
                makeFinalRoute(null, "Airport");
            }
        });

        TextView cancelBtn = innerDialog.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                innerDialog.dismiss();
                dialog.dismiss();
                makeFinalRoute(null, "Airport");
            }
        });

        innerDialog.show();
    }

    private void makeFinalRoute(LocAndField locAndField, String locationType) {
        try {
            if (locAndField != null) {
                setData(locAndField, locationType);
            } else {
                setData(locationType);
            }
            recentListRv.setVisibility(GONE);
            homeAndWorkLl.setVisibility(VISIBLE);
            if (searchEditTextArrayList.size() > 1) {
                boolean isEmptyEt = false;
                for (int i = 0; i < searchEditTextArrayList.size(); i++) {
                    if (searchEditTextArrayList.get(i).getField().equals("")) {
                        isEmptyEt = true;
                        break;
                    }
                }
                if (!isEmptyEt) {
                    makeRoute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTIme() {
        String time = "";
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth, mDay);
        time = CommonVariables.getFormattedDateStr(calendar, "dd/MM/yyyy");
        return time;
    }

    int count = 0;

    private class RecentListAdapter extends RecyclerView.Adapter<RecentListAdapter.MyViewHolder> {

        private Context context;

        public RecentListAdapter(Context context, ArrayList<LocAndField> arrayList) {
            this.context = context;
            locAndFieldArrayList.clear();
            locAndFieldArrayList = arrayList;
        }

        @NonNull
        @Override
        public RecentListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.layout_adapter_item_searched, parent, false);
            return new RecentListAdapter.MyViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecentListAdapter.MyViewHolder h, @SuppressLint("RecyclerView") int position) {
            Log.d(TAG, "onBindViewHolder: ---- recent");
            LocAndField field = locAndFieldArrayList.get(h.getAdapterPosition());
            try {
                if (field.getLocationType().contains("Airport")) {
                    h.drwIcon.setImageResource(R.drawable.ic_airplane);
                } else {
                    h.drwIcon.setImageResource(R.drawable.ic_pin);
                }
            } catch (Exception e) {
                e.printStackTrace();
                h.drwIcon.setImageResource(R.drawable.ic_pin);
            }

            try {
                if (field.getDoorNo() != null && !field.getDoorNo().equals("")) {
                    h.text1.setText(field.getDoorNo().toUpperCase());
                    h.text2.setText(field.getField().toUpperCase());
                } else if (field.getSubAddress() != null) {
                    h.text1.setText(field.getField());
                    h.text2.setText(field.getSubAddress());

                } else {
                    if (getPostalCodeFromAddress(field.getField()).equals("")) {
                        String[] addresses = HomeFragment.getFilteredAddress(field.getField());
                        h.text1.setText(addresses[0] + addresses[1]);
                        h.text2.setVisibility(View.INVISIBLE);
                        HomeFragment.isPostalCodeAvailable = false;
                    } else {
                        HomeFragment.isPostalCodeAvailable = true;
                        String[] addresses = HomeFragment.getFilteredAddress(field.getField());
                        if (addresses[0].equals("") && !addresses[1].equals("")) {
                            h.text1.setText(addresses[1]);
                            h.text2.setText(addresses[0]);
                        } else {
                            h.text1.setText(addresses[0]);
                            h.text2.setText(addresses[1]);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                h.text2.setVisibility((h.text2.getText().toString().length() == 0) ? View.INVISIBLE : VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            h.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean alreadyPresend = false;
                    for (LocAndField locAndField : searchEditTextArrayList) {
                        if (locAndFieldArrayList.get(position).getField().trim().equalsIgnoreCase(locAndField.getField().trim())) {
                            alreadyPresend = true;
                            break;
                        }
                    }

                    if (alreadyPresend) {
                        FBToast.warningToast(ExActivity_SearchAddress.this, "Address already present", FBToast.LENGTH_SHORT);
                        return;
                    }

                    locAndFieldArrayListPosition = h.getAdapterPosition();
                    try {
                        placeId = field.getPlaceId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (indexEditTextPosition == 0 && field.getLocationType().contains("Airport")) {
                        new Manager_GetAirportDetails(ExActivity_SearchAddress.this, locAndFieldArrayList.get(locAndFieldArrayListPosition).getField(), p, ExActivity_SearchAddress.this).execute();
                    } else {
                        if (sp.getString(CommonVariables.enableOutsideUK, "0").equals("0")) {
                            makeFinalRoute(null, locAndFieldArrayList.get(locAndFieldArrayListPosition).getLocationType()); // 0 = Inside UK
                        } else {
                            // 1 = Outside Uk
                            new Manager_GetLatLnfFromPlaceId((Activity) context, "" + placeId, ExActivity_SearchAddress.this);
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return locAndFieldArrayList.size();
        }

        public String getPostalCodeFromAddress(String address) {
            try {
                String[] split = address.split(" ");
                String result = "";
                for (int i = split.length - 1; i >= 0; i--) {
                    result += (split[i] + " ");
                }
                String reverseAddress = result.trim();
                //  System.out.println(result.trim());
                String[] arr = reverseAddress.split(" ");

                String PostalCode = arr[1] + " " + arr[0];
                if (isAlphaNumeric(PostalCode)) {
                    return PostalCode;
                } else {
                    return "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        private boolean isNumericValue(String address) {

            boolean found = false;
            Pattern p = Pattern.compile(RegexPatterns.REGEX_ALPHANUMERIC);//<-- compile( not Compile(
            Matcher m = p.matcher(address.toLowerCase());  //<-- matcher( not Matcher
            if (m.find()) {  //<-- m not matcher

                found = true;
                return found;
            } else {
                return found;
            }
        }

        public boolean isAlphaNumeric(String postCode) {
//boolean islength=false;
            boolean[] validcode = {false, false};

            if (postCode != null) {
                String[] text = postCode.split(" ");
                for (int j = 0; j < text.length; j++) {
                    if (text[j].length() > 4) {
                        return false;
                    } else {
//                for (int i = 0; i < text[j].length(); ++i) {
//                    char c = text[j].charAt(i);
//                    if (Character.isDigit(c)) {
//                        numeric = true;
//                    } else if (Character.isLetter(c)) {
//                        alpha = true;
//                    } else {
//                        accepted = false;
//                        break;
//                    }
//                }
                        validcode[j] = isNumericValue(text[j]);// Then it is correct


                    }
                }
                return validcode[0] && validcode[1];// Then it is correct
            } else {
                return false;
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView text1;
            private TextView text2;
            private ImageView drwIcon;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                text1 = itemView.findViewById(R.id.text1);
                text2 = itemView.findViewById(R.id.text2);
                drwIcon = itemView.findViewById(R.id.drwIcon);
            }
        }
    }

    private class SearchEditTextAdapter extends RecyclerView.Adapter<SearchEditTextAdapter.MyViewHolder> {

        private Context context;
        private ArrayList<LocAndField> arrayList;

        public SearchEditTextAdapter(Context context, ArrayList<LocAndField> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.layout_adapter_item_add_edittext, parent, false);
            return new SearchEditTextAdapter.MyViewHolder(contactView);
        }

        boolean isFocusOnly;

        public void setFocus(int pos, boolean isFocusOnly) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (i == pos) {
                    arrayList.get(i).setHasFocus(true);
                } else {
                    arrayList.get(i).setHasFocus(false);
                }

            }
            this.isFocusOnly = isFocusOnly;
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder h, @SuppressLint("RecyclerView") int position) {
            Log.d(TAG, "onBindViewHolder: ---- search");
            LocAndField e = arrayList.get(position);
            try {
                if (e.isHasFocus()) {
                    h.editText.requestFocus();
                    indexEditTextPosition = position;
                } else {
                    h.editText.clearFocus();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                if (!isFocusOnly || e.getField().isEmpty()) {
                    h.editText.setText("" + e.getField());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                h.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            indexEditTextPosition = position;
                        }
                    }
                });
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (position == 0) {
                h.lineRl.setVisibility(GONE);
                h.iv.setVisibility(VISIBLE);
                setBackgroundDrawable(h, "pick up", R.drawable.point);
            } else if (arrayList.size() - 1 == position) {
                h.lineRl.setVisibility(GONE);
                h.iv.setVisibility(VISIBLE);
                setBackgroundDrawable(h, "drop off", R.drawable.ic_pin);
            } else {
                h.lineRl.setVisibility(VISIBLE);
                h.iv.setVisibility(GONE);
                setBackgroundDrawable(h, "add stop", R.drawable.ic_circle);
            }

            try {
                h.editTextCloseIv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (h.editText.getText().toString().isEmpty()) {
                            return;
                        }

                        try {
                            arrayList.set(position, new LocAndField());
                            isSelected = true;
                            isAlreadyAvailable = false;
//                            arrayList.get(position).setHasFocus(true);
                            setFocus(position, true);
//                            isFocusOnly = true;
//                            notifyDataSetChanged();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });
            } catch (Exception exception) {
                exception.printStackTrace();
            }
          */
/*  try {
                h.editText.setOnTouchListener((v, event) -> {

                    final int DRAWABLE_RIGHT = 2;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (h.editText.getRight() - h.editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            arrayList.set(position, new LocAndField());
                            isSelected = true;
                            isAlreadyAvailable = false;
                            setFocus(position, true);
                            return true;
                        }
                    }
                    return false;
                });
            } catch (Exception ee) {
                ee.printStackTrace();
            }*//*


            if (position > 0 && position < arrayList.size() - 1) {
                h.closeViaIv.setVisibility(View.VISIBLE);
            } else {
                h.closeViaIv.setVisibility(View.INVISIBLE);
            }

            h.closeViaIv.setOnClickListener(view -> {

                try {
                    if (h.getAdapterPosition() > 0 && h.getAdapterPosition() < arrayList.size() - 1) {
                        arrayList.remove(h.getAdapterPosition());
                        isSelected = true;
                        notifyDataSetChanged();
                        isAlreadyAvailable = false;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                try {
                    if (searchEditTextArrayList.size() > 1) {
                        boolean isEmptyEt = false;
                        for (int i = 0; i < searchEditTextArrayList.size(); i++) {
                            if (searchEditTextArrayList.get(i).getField().equals("")) {
                                isEmptyEt = true;
                                break;
                            }
                        }
                        if (!isEmptyEt) {
                            makeRoute();
                        } else {
                            for (int i = 0; i < searchEditTextArrayList.size(); i++) {
                                if (searchEditTextArrayList.get(i).getField().equals("")) {
                                    count = i;
                                    break;
                                }
                            }
                            setFocus(count, true);
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });

            h.editText.addTextChangedListener(new TextWatcher() {
                private Timer timer = new Timer();

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d(TAG, "onBindViewHolderafterTextChanged: ");
                    try {
                        boolean isSearchAllowed = true;
                        String s = h.editText.getText().toString();

                        if (indexEditTextPosition <= locAndFieldArrayList.size()) {
                            String m = h.editText.getText().toString();
                            String n = searchEditTextArrayList.get(indexEditTextPosition).getField();
                            boolean a = m == n;

                            if (!m.equals(n)) {
                                searchEditTextArrayList.get(indexEditTextPosition).setSelected(false);
                            } else {
                                isSearchAllowed = !searchEditTextArrayList.get(indexEditTextPosition).isSelected();
                            }
                            isSearchAllowed = isSearchAllowed ? !m.isEmpty() : isSearchAllowed;
                        }

                        if (arrayList.size() > 2 && h.getAdapterPosition() != indexEditTextPosition && h.editText.getText().toString().length() == 0) {
                            return;
                        }

                        if (arrayList.size() > 2 && h.getAdapterPosition() == indexEditTextPosition && h.editText.getText().toString().length() == 0) {
                            return;
                        }

                        if (arrayList.size() >= 1 && isSearchAllowed) {
                            isSelected = false;
                        }
                        */
/*if (searchEditTextArrayList.get(indexEditTextPosition).getField().equals("")) {
                            return;
                        }*//*

                        if (isSelected) {
                            return;
                        }

                        if (!h.editText.hasFocus() && h.editText.getText().toString().length() == 0) {
                            return;
                        }

                        if (h.editText.hasFocus() && h.editText.getText().toString().length() == 0) {
                            return;
                        }

                        try {
                            if (s.length() == 1 || s.length() == 0) {
                                recentListRv.setVisibility(GONE);
                                homeAndWorkLl.setVisibility(VISIBLE);
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        mSearchString = s;
                        timer.cancel();
                        timer = new Timer();

                        try {
                            chooseFromMap.setVisibility(View.INVISIBLE);
                            addViaTv.setVisibility(View.INVISIBLE);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        if (s.length() >= 2) {
                                            if (mRunnable != null) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        recentListRv.setVisibility(GONE);
                                                        homeAndWorkLl.setVisibility(GONE);
                                                        progressBar.setVisibility(ProgressBar.VISIBLE);
                                                    }
                                                });
                                                search();
                                            }
                                        }
                                    }
                                }, 1500
                        );
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
        }

        private void setBackgroundDrawable(MyViewHolder h, String text, int drawable) {
            h.fromToTv.setText(text);
            h.iv.setImageDrawable(ContextCompat.getDrawable(context, drawable));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public EditText editText;
            private TextView fromToTv;
            private ImageView closeViaIv;
            private ImageView editTextCloseIv;
            private ImageView iv;
            private RelativeLayout lineRl;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                editTextCloseIv = itemView.findViewById(R.id.editTextCloseIv);
                editText = itemView.findViewById(R.id.editText);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                fromToTv = itemView.findViewById(R.id.fromToTv);
                closeViaIv = itemView.findViewById(R.id.closeViaIv);
                iv = itemView.findViewById(R.id.iv);
                lineRl = itemView.findViewById(R.id.lineRl);

                editText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        keyboard.showSoftInput(editText, 0);
                    }
                }, 200);
            }
        }
    }


    Timer pickupTimer = new Timer();

    private void setBoxListener() {
        pickupEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (pickupEt.getText().toString().length() == 0) {
                        return;
                    }
                    pickupTimer.cancel();
                    pickupTimer = new Timer();
                    chooseFromMap.setVisibility(View.INVISIBLE);
                    addViaTv.setVisibility(View.INVISIBLE);

                    // disable rest

                    pickupTimer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    if (s.length() >= 2) {
                                        if (mRunnable != null) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    recentListRv.setVisibility(GONE);
                                                    homeAndWorkLl.setVisibility(GONE);
                                                    progressBar.setVisibility(ProgressBar.VISIBLE);
                                                }
                                            });
                                            search();
                                        }
                                    }
                                }
                            }, 1500
                    );
                } catch (Exception exception) {
                    exception.printStackTrace();
                }


            }
        });
    }

    private class Flight {
        private String ScheduleDateTime;
        private String Message;
        private String Date;
        private String ArrivalTerminal;
        private String ArrivingFrom;
        private String FlightNo;
        private String DefaultClientId;
        private String DateTime;
        private String Status;

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public String getScheduleDateTime() {
            return ScheduleDateTime;
        }

        public void setScheduleDateTime(String scheduleDateTime) {
            ScheduleDateTime = scheduleDateTime;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public String getArrivalTerminal() {
            return ArrivalTerminal;
        }

        public void setArrivalTerminal(String arrivalTerminal) {
            ArrivalTerminal = arrivalTerminal;
        }

        public String getArrivingFrom() {
            return ArrivingFrom;
        }

        public void setArrivingFrom(String arrivingFrom) {
            ArrivingFrom = arrivingFrom;
        }

        public String getFlightNo() {
            return FlightNo;
        }

        public void setFlightNo(String flightNo) {
            FlightNo = flightNo;
        }

        public String getDefaultClientId() {
            return DefaultClientId;
        }

        public void setDefaultClientId(String defaultClientId) {
            DefaultClientId = defaultClientId;
        }

        public String getDateTime() {
            return DateTime;
        }

        public void setDateTime(String dateTime) {
            DateTime = dateTime;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }
    }

    private class FlightNoAdapter extends RecyclerView.Adapter<FlightNoAdapter.MyViewHolder> {
        private Context context;
        private ArrayList<Flight> arrayList;

        public FlightNoAdapter(Context context, ArrayList<Flight> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.layout_adapter_item_flight_no, parent, false);
            return new FlightNoAdapter.MyViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder h, int position) {

            Flight f = arrayList.get(h.getAdapterPosition());

            h.flightNo.setText(f.getFlightNo());
            h.arriveFrom.setText(f.getArrivingFrom());

            String da = null;
            try {
                if (f.getScheduleDateTime().contains(" ")) {
                    String date[] = f.getScheduleDateTime().split(" ");
                    da = "";
                    if (date.length > 0) {
                        da = date[0];
                    }
                    if (date.length > 1) {
                        da += "\n" + date[1];
                    }
                } else {
                    da = f.getDateTime();
                }


            } catch (Exception e) {
                e.printStackTrace();
                da = "";
            }

            h.dateTime.setText(da);
            h.statusTv.setText(f.getStatus());

            h.itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    HomeFragment.fromDoorNo = arrayList.get(h.getAdapterPosition()).getFlightNo();
                    HomeFragment.fromComing = arrayList.get(h.getAdapterPosition()).getArrivingFrom();
                    specialNotes();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView flightNo;
            private TextView arriveFrom;
            private TextView dateTime;
            private TextView statusTv;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                flightNo = itemView.findViewById(R.id.flightNo);
                arriveFrom = itemView.findViewById(R.id.arriveFrom);
                dateTime = itemView.findViewById(R.id.dateTime);
                statusTv = itemView.findViewById(R.id.statusTv);
            }
        }
    }

    private void parseForGoogle(String result) throws Exception {
        ArrayList<LocAndField> arrayList = new ArrayList<>();
        arrayList.clear();
        JSONObject parentObject = new JSONObject(result);
        JSONArray prediction = parentObject.getJSONArray("predictions");
        for (int i = 0; i < prediction.length(); i++) {
            String description = prediction.getJSONObject(i).getString("description");
            String placeId = prediction.getJSONObject(i).getString("place_id");
            LocAndField locAndField = new LocAndField();
            locAndField.setField(description);
            locAndField.setPlaceId(placeId);
            if (locAndField.getField().contains("Airport")) {
                locAndField.setLocationType("Airport");
            } else {
                locAndField.setLocationType("Address");
            }
            arrayList.add(locAndField);
        }

        RecentListAdapter recentListAdapter = new RecentListAdapter(getContext(), arrayList);
        recentListRv.setAdapter(recentListAdapter);
        recentListRv.setVisibility(VISIBLE);
    }

    private void parseForDispatch(String result) throws Exception {
        ArrayList<LocAndField> arrayList = new ArrayList<>();
        arrayList.clear();
        JSONObject parentObject = new JSONObject(result);

        if (parentObject.getBoolean("HasError")) {
            FBToast.errorToast(getContext(), parentObject.optString("Message"), FBToast.LENGTH_SHORT);
        } else {

            JSONArray jsonArray = new JSONArray(parentObject.getString("Data"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject addressJson = jsonArray.getJSONObject(i);
                if (addressJson.getString("Latitude") == null || addressJson.getString("Latitude").equals("null"))
                    continue;

                String address = addressJson.getString("FullLocationName");

                LocAndField field = new LocAndField();
                field.setField(address);
                field.setLocationType(addressJson.getString("LocationType"));
                field.setLat(addressJson.getString("Latitude"));
                field.setLon(addressJson.getString("Longitude"));
                arrayList.add(field);
            }
            RecentListAdapter recentListAdapter = new RecentListAdapter(getContext(), arrayList);
            recentListRv.setAdapter(recentListAdapter);
            recentListRv.setVisibility(VISIBLE);
        }
    }

    // GetAddress
    @Override
    public void onCompleteGetAddress(String result) {
        try {
            chooseFromMap.setVisibility(View.VISIBLE);
            addViaTv.setVisibility(VISIBLE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        progressBar.setVisibility(GONE);
        isSelected = false;
        if (stopCallBacks) return;
        try {
            if (result.startsWith("error_")) {
                result = result.replace("error_", "");
                if (result.toLowerCase().startsWith("unable to resolve host")) {
                    FBToast.errorToast(getContext(), "Please check your internet connection and try again", FBToast.LENGTH_SHORT);
                } else
                    FBToast.errorToast(getContext(), result, FBToast.LENGTH_SHORT);
                return;
            }

            if (sp.getString(CommonVariables.enableOutsideUK, "0").equals("0")) {
                // 0 = Inside Uk
                parseForDispatch(result);
            } else {
                // 1 = Outside Uk
                parseForGoogle(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GetFlightDetails
    @Override
    public void onStartFlightDetails(boolean isStart) {
        isSearching = true;
        try {
            buttonTextRl.setVisibility(GONE);
            flightProgressBar.setVisibility(VISIBLE);
            flightNoArrayList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GetFlightDetails
    @Override
    public void onCompleteFlightDetails(String response) {
        isSearching = false;
        flightProgressBar.setVisibility(GONE);
        buttonTextRl.setVisibility(VISIBLE);
        try {
            if (response.startsWith("error_")) {
                response = response.replace("error_", "");
                FBToast.errorToast(getContext(), response, FBToast.LENGTH_SHORT);
            }
            if (response != null && !response.isEmpty()) {

                JSONObject parentObject = new JSONObject(response);

                if (parentObject.getString("Message").equalsIgnoreCase("Flight details not found")) {
                    Flight flight = new Flight();
                    flight.setMessage(parentObject.getString("Message"));
                    flightNoArrayList.add(flight);
                }
                Log.d("TAG", "doInBackground: " + parentObject);

                String FlightNo = parentObject.getString("FlightNo");
                String ScheduleDateTime = parentObject.getString("ScheduleDateTime");
                String DateTime = parentObject.getString("DateTime");

                String ArrivalTerminal = parentObject.getString("ArrivalTerminal");
                String ArrivingFrom = parentObject.getString("ArrivingFrom");
                String Message = parentObject.getString("Message");
                String Status = parentObject.getString("Status");

                Flight flight = new Flight();

                flight.setFlightNo(FlightNo);
                flight.setScheduleDateTime(ScheduleDateTime);
                if (Message.equals("null") || Message.equals(null))
                    Message = "";

                flight.setMessage(Message);
                flight.setStatus(Status);

                String[] dT = DateTime.trim().split(" ");
                if (dT.length > 1) {
                    HomeFragment.customTime = dT[1].trim();
                    flight.setArrivingFrom("Arriving at " + dT[1] + " from " + ArrivingFrom);
                    flight.setDateTime(dT[0].trim());
                    HomeFragment.customDate = dT[0].trim();
                } else {
                    flight.setArrivingFrom("Arriving From " + ArrivingFrom);
                }

                if (ArrivalTerminal.equals("null") || ArrivalTerminal.equals(null) || ArrivalTerminal.equals("")) {
                    ArrivalTerminal = "";
                } else {
                    ArrivalTerminal = " in T" + ArrivalTerminal;
                }
                flight.setArrivingFrom(flight.getArrivingFrom() + ArrivalTerminal);
                flightNoArrayList.add(flight);

                if (flightNoArrayList.get(0).getMessage().equalsIgnoreCase("Flight Details not found")) {
                    FBToast.errorToast(getContext(), flightNoArrayList.get(0).getMessage(), FBToast.LENGTH_SHORT);
                    flightNoArrayList.clear();
                    return;
                }

                FlightNoAdapter adapter = new FlightNoAdapter(getContext(), flightNoArrayList);
                rview.setAdapter(adapter);
            } else {
                FBToast.errorToast(getContext(), "Flight details not found.", FBToast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GetAirportDetails
    @Override
    public void onCompleteAirportDetails(String result) {
        try {
            if (result.startsWith("error_")) {
                result = result.replace("error_", "");
                FBToast.errorToast(getContext(), result, FBToast.LENGTH_SHORT);
                new Manager_GetLatLnfFromPlaceId(ExActivity_SearchAddress.this, "" + placeId, ExActivity_SearchAddress.this);
                return;
            }
            JSONObject object = new JSONObject(result);
            String response = object.getString("HasError");
            if (!response.equals("true")) {
                String message = object.getString("Data");
                if (message.length() != 0) {
                    showAirportInfo(message);
                } else {
                    new SweetAlertDialog(ExActivity_SearchAddress.this, SweetAlertDialog.FLIGHT_INPUT)
                            .setTitleText(p.getFlightDetails())
                            .setContentText(p.getAlreadyAtAirport() + "?")
                            .setConfirmText(p.getYes())
                            .showCancelButton(true)
                            .setCancelText(p.getSelectFlightNo())
                            .setFlightListener(new SweetAlertDialog.OnSweetFlightListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog, String flightNo, String comingFrom, String atAirport) {
                                    sweetAlertDialog.dismissWithAnimation();

                                    HomeFragment.fromDoorNo = "Already at airport.";
                                    makeFinalRoute(null, "Airport");
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    makeFinalRoute(null, "Airport");
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
                                    searchForFlightNo();
                                    sDialog.cancel();
                                }

                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                }
                            })
                            .show();
                }
            } else {
//                FBToast.errorToast(getContext(), object.getString("Message"), FBToast.LENGTH_SHORT);
                new SweetAlertDialog(getActivity(), SweetAlertDialog.FLIGHT_INPUT)
                        .setTitleText("Flight Details")
                        .setContentText("You want to delete your work location!")
                        .setConfirmText("Done")
                        .showCancelButton(true)
                        .setCancelText("Cancel")
                        .setFlightListener(new SweetAlertDialog.OnSweetFlightListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog, String flightNo, String comingFrom, String atAirport) {
                                String address = comingFrom;
                                String flightNumber = flightNo;

                                boolean isArrived = atAirport.trim().equals("0");

                                if (isArrived) {
                                    HomeFragment.fromDoorNo = "Already at airport.";
                                } else {
                                    if (flightNumber != null && !flightNumber.equals("")) {
                                        HomeFragment.fromDoorNo = flightNo;
                                        HomeFragment.fromComing = comingFrom;
                                    } else {
                                        FBToast.errorToast(getActivity(), "Please enter flight no", FBToast.LENGTH_SHORT);
                                        return;
                                    }
                                }

                                new Manager_GetLatLnfFromPlaceId(ExActivity_SearchAddress.this, "" + placeId, ExActivity_SearchAddress.this);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                            }

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                sweetAlertDialog.dismissWithAnimation();
//                            model.setSpecialNotes(InputText);

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
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    // GetGoogleLatLngFromPlaceId
    @Override
    public void onCompleteForGetGoogleLatLngFromPlaceId(String result) {
        try {
            LocAndField locAndField = new LocAndField();
            if (result.contains("error_")) {
//                result = result.replace("error_", "");
//                FBToast.errorToast(getContext(), result, FBToast.LENGTH_SHORT);
                return;
            }
            JSONObject jsonObject = new JSONObject(result);
            JSONObject resultRes = jsonObject.getJSONObject("result");
            JSONArray address_component = resultRes.getJSONArray("address_components");
            outerloop:
            for (int i = 0; i < address_component.length(); i++) {
                JSONObject subObj = address_component.getJSONObject(i);
                JSONArray types = subObj.getJSONArray("types");
                for (int k = 0; k < types.length(); k++) {
                    if (types.getString(k).equals("postal_code")) {
                        locAndField.setPostCode(subObj.getString("long_name"));
                        break outerloop;
                    }
                }
            }
            JSONObject geometry = resultRes.getJSONObject("geometry");

            JSONObject location = geometry.getJSONObject("location");

            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");

            locAndField.setLat("" + lat);
            locAndField.setLon("" + lng);

*/
/*
            if (locAndField.getLat().equals("null")) {
                Geocoder coder = new Geocoder(ExActivity_SearchAddress.this);
                String _address = locAndField.getField();
                String[] patternArray = _address.split(RegexPatterns.REGEX_UK_POST_CODE_WS);

                if (Pattern.compile(RegexPatterns.REGEX_UK_POST_CODE_WS).matcher(_address).find()) {
                    List<Address> list = null;
                    try {
                        list = coder.getFromLocationName(_address.replace(patternArray[0], ""), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (list != null && !list.isEmpty()) {
                        locAndField.setLat("" + list.get(0).getLatitude());
                        locAndField.setLon("" + list.get(0).getLongitude());
                    }
                }
            }
*//*


            makeFinalRoute(locAndField, "Address");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopCallBacks = true;
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        finish();
    }
}*/
