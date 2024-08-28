package base.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static base.utils.CommonMethods.checkIfHasNullForString;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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

import com.eurosoft.customerapp.BuildConfig;
import com.eurosoft.customerapp.R;
import com.support.parser.RegexPatterns;
import com.tfb.fbtoast.FBToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.listener.Listener_GetAddress;
import base.listener.Listener_GetAddressCoordinate;
import base.listener.Listener_GetAirportDetails;
import base.listener.Listener_GetFlightDetails;
import base.listener.Listener_GetLatLngFromPlaceId;
import base.listener.Listener_OnSetResult;
import base.manager.Manager_GetAddressCoordinates;

import base.manager.Manager_GetAddressDetails;
import base.manager.Manager_GetAddressFromGoogle;
import base.manager.Manager_GetAirportDetails;
import base.manager.Manager_GetFlightDetails;
import base.manager.Manager_GetLatLnfFromPlaceId;
import base.models.ClsLocationData;
import base.models.Flight;
import base.models.LocAndField;
import base.models.Model_BookingDetailsModel;
import base.models.ParentPojo;
import base.newui.HomeFragment;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_SearchAddress extends AppCompatActivity implements
        Listener_OnSetResult,
        Listener_GetAddress,
        Listener_GetFlightDetails,
        Listener_GetLatLngFromPlaceId {
    private static final String TAG = "NeNewBookingDetails";

    /*Boxes  VIEWS  Start */

    boolean clickedFromRecentList = false;

    private Timer pickupTimer = new Timer();

    // For pick up
    private RelativeLayout pickup_box_Rl;
    private EditText pickupEt;
    private ImageView pickUpClearEdittextIv;

    // for via 1
    private RelativeLayout via_1_box_Rl;
    private EditText via_1_Et;
    private ImageView delete_via_1_Iv;
    private ImageView via_1_ClearEdittextIv;

    // for via 2
    private RelativeLayout via_2_box_Rl;
    private EditText via_2_Et;
    private ImageView delete_via_2_Iv;
    private ImageView via_2_ClearEdittextIv;

    // For drop off
    private RelativeLayout dropoff_box_Rl;
    private EditText dropoffEt;
    private ImageView dropOffClearEdittextIv;
    private LocAndField filed;

    /*Boxes  VIEWS  End */

    private void setBoxViews() {
        pickup_box_Rl = findViewById(R.id.pickup_box_Rl);
        pickupEt = findViewById(R.id.pickupEt);
        pickUpClearEdittextIv = findViewById(R.id.pickUpClearEdittextIv);

        via_1_box_Rl = findViewById(R.id.via_1_box_Rl);
        via_1_box_Rl.setVisibility(GONE);
        via_1_Et = findViewById(R.id.via_1_Et);
        via_1_ClearEdittextIv = findViewById(R.id.via_1_ClearEdittextIv);
        delete_via_1_Iv = findViewById(R.id.delete_via_1_Iv);

        via_2_box_Rl = findViewById(R.id.via_2_box_Rl);
        via_2_box_Rl.setVisibility(GONE);
        via_2_Et = findViewById(R.id.via_2_Et);
        via_2_ClearEdittextIv = findViewById(R.id.via_2_ClearEdittextIv);
        delete_via_2_Iv = findViewById(R.id.delete_via_2_Iv);

        dropoff_box_Rl = findViewById(R.id.dropoff_box_Rl);
        dropoffEt = findViewById(R.id.dropoffEt);
        dropOffClearEdittextIv = findViewById(R.id.dropOffClearEdittextIv);

    }

    private void resetScreen() {
        // Hide Marker Work
        if (sp.getString(Config.HideCurrentLocation, "0").equals("1")) {
            chooseFromMap.setVisibility(GONE);
        } else {
            chooseFromMap.setVisibility(View.VISIBLE);
        }
        homeAndWorkLl.setVisibility(VISIBLE);
        recentListRv.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }

    private void clearFocusOfAllEditText() {
        pickupEt.clearFocus();
        via_1_Et.clearFocus();
        via_2_Et.clearFocus();
        dropoffEt.clearFocus();
    }

    private void setPickUpBoxListener() {
        pickupEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setDrawable(pickupEt);
                if (b) {
//                    clickedFromRecentList = false;
                    pickupEt.requestFocus();
                } else {
                    pickupEt.clearFocus();
                }
            }
        });

        pickupEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pickupEt.hasFocus()) {
                    try {
                        if (clickedFromRecentList) {
                            clickedFromRecentList = false;
                            return;
                        }
                        String sss = pickupEt.getText().toString();

                        if (sss.length() == 0) {
                            resetScreen();
                            setEnableAllEditText(pickupEt, via_1_Et, via_2_Et, dropoffEt);
                            setEnableImageViewClear(pickUpClearEdittextIv, via_1_ClearEdittextIv, via_2_ClearEdittextIv, dropOffClearEdittextIv);
                            return;
                        }

                        // disable all view except pick-up
                        setFirstEnableAndRestDisableEditText(pickupEt, via_1_Et, via_2_Et, dropoffEt);
                        setFirstEnableAndRestDisableImageViewClear(pickUpClearEdittextIv, via_1_ClearEdittextIv, via_2_ClearEdittextIv, dropOffClearEdittextIv);

                        pickupTimer.cancel();
                        pickupTimer = new Timer();
//                        chooseFromMap.setVisibility(View.INVISIBLE);
                        // addViaTv.setVisibility(View.INVISIBLE);

                        // disable rest
                        mSearchString = sss;
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
            }
        });

        // Clear Edit Text Box
        pickUpClearEdittextIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newLocAndFieldList.set(0, new LocAndField());
                clearFocusOfAllEditText();
                pickupEt.requestFocus();
                pickupEt.setText("");
            }
        });
    }

    private void setDropOffBoxListener() {
        dropoffEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setDrawable(dropoffEt);
                if (b) {
                    dropoffEt.requestFocus();
                } else {
                    dropoffEt.clearFocus();
                }
            }
        });

        dropoffEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (dropoffEt.hasFocus()) {
                    try {
                        if (clickedFromRecentList) {
                            clickedFromRecentList = false;
                            return;
                        }

                        String sss = dropoffEt.getText().toString();

                        if (sss.length() == 0) {
                            resetScreen();
                            setEnableAllEditText(pickupEt, via_1_Et, via_2_Et, dropoffEt);
                            setEnableImageViewClear(pickUpClearEdittextIv, via_1_ClearEdittextIv, via_2_ClearEdittextIv, dropOffClearEdittextIv);
                            return;
                        }

                        // disable all view except drop-off
                        setFirstEnableAndRestDisableEditText(dropoffEt, via_1_Et, via_2_Et, pickupEt);
                        setFirstEnableAndRestDisableImageViewClear(dropOffClearEdittextIv, via_1_ClearEdittextIv, via_2_ClearEdittextIv, pickUpClearEdittextIv);

                        pickupTimer.cancel();
                        pickupTimer = new Timer();
                        chooseFromMap.setVisibility(View.INVISIBLE);
                        //  addViaTv.setVisibility(View.INVISIBLE);

                        mSearchString = sss;
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
            }
        });

        // Clear Edit Text Box
        dropOffClearEdittextIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newLocAndFieldList.set(newLocAndFieldList.size() - 1, new LocAndField());
                clearFocusOfAllEditText();
                dropoffEt.requestFocus();
                dropoffEt.setText("");
            }
        });
    }

    private void setDrawable(View view) {
        view.setBackground(ContextCompat.getDrawable(Activity_SearchAddress.this, view.hasFocus() ? R.drawable.glowing_border : R.drawable.all_round));
    }

    private void setVia_1_BoxListener() {
        via_1_Et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setDrawable(via_1_Et);
                if (b) {
                    via_1_Et.requestFocus();
                } else {
                    via_1_Et.clearFocus();
                }
            }
        });

        via_1_Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (via_1_Et.hasFocus()) {
                    try {
                        if (clickedFromRecentList) {
                            clickedFromRecentList = false;
                            return;
                        }
                        String sss = via_1_Et.getText().toString();

                        if (sss.length() == 0) {
                            resetScreen();
                            setEnableAllEditText(pickupEt, via_1_Et, via_2_Et, dropoffEt);
                            setEnableImageViewClear(pickUpClearEdittextIv, via_1_ClearEdittextIv, via_2_ClearEdittextIv, dropOffClearEdittextIv);
                            return;
                        }

                        // disable all view except pick-up
                        setFirstEnableAndRestDisableEditText(via_1_Et, pickupEt, via_2_Et, dropoffEt);
                        setFirstEnableAndRestDisableImageViewClear(via_1_ClearEdittextIv, pickUpClearEdittextIv, via_2_ClearEdittextIv, dropOffClearEdittextIv);

                        pickupTimer.cancel();
                        pickupTimer = new Timer();
                        chooseFromMap.setVisibility(View.INVISIBLE);
                        // addViaTv.setVisibility(View.INVISIBLE);

                        // disable rest
                        mSearchString = sss;
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
            }
        });

        delete_via_1_Iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                via_1_Et.setText("");
                newLocAndFieldList.remove(1);

                if (via_2_box_Rl.getVisibility() == VISIBLE) {
                    via_1_Et.setText(via_2_Et.getText().toString());
                    via_2_box_Rl.setVisibility(GONE);
                } else {
                    via_1_box_Rl.setVisibility(GONE);
                }

                makeRoute();
            }
        });

        // Clear Edit Text Box
        via_1_ClearEdittextIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newLocAndFieldList.set(1, new LocAndField());
                clearFocusOfAllEditText();
                via_1_Et.requestFocus();
                via_1_Et.setText("");
            }
        });
    }

    private void setVia_2_BoxListener() {
        via_2_Et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setDrawable(via_2_Et);
                if (b) {
                    via_2_Et.requestFocus();
                } else {
                    via_2_Et.clearFocus();
                }
            }
        });

        via_2_Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (via_2_Et.hasFocus()) {
                    try {
                        if (clickedFromRecentList) {
                            clickedFromRecentList = false;
                            return;
                        }
                        String sss = via_2_Et.getText().toString();

                        if (sss.length() == 0) {
                            resetScreen();
                            setEnableAllEditText(pickupEt, via_1_Et, via_2_Et, dropoffEt);
                            setEnableImageViewClear(pickUpClearEdittextIv, via_1_ClearEdittextIv, via_2_ClearEdittextIv, dropOffClearEdittextIv);
                            return;
                        }

                        // disable all view except pick-up
                        setFirstEnableAndRestDisableEditText(via_2_Et, pickupEt, via_1_Et, dropoffEt);
                        setFirstEnableAndRestDisableImageViewClear(via_2_ClearEdittextIv, pickUpClearEdittextIv, via_1_ClearEdittextIv, dropOffClearEdittextIv);

                        pickupTimer.cancel();
                        pickupTimer = new Timer();
                        chooseFromMap.setVisibility(View.INVISIBLE);
                        // addViaTv.setVisibility(View.INVISIBLE);

                        // disable rest
                        mSearchString = sss;
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
            }
        });

        delete_via_2_Iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                via_2_Et.setText("");
                newLocAndFieldList.remove(2);
                via_2_box_Rl.setVisibility(GONE);
                makeRoute();
            }
        });
        // Clear Edit Text Box
        via_2_ClearEdittextIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newLocAndFieldList.set(2, new LocAndField());
                clearFocusOfAllEditText();
                via_2_Et.requestFocus();
                via_2_Et.setText("");
            }
        });
    }

    private void setFirstEnableAndRestDisableEditText(EditText editText1, EditText editText2, EditText editText3, EditText editText4) {
        editText1.setEnabled(true);
        editText2.setEnabled(false);
        editText3.setEnabled(false);
        editText4.setEnabled(false);
    }

    private void setFirstEnableAndRestDisableImageViewClear(ImageView iv1, ImageView iv2, ImageView iv3, ImageView iv4) {
        iv1.setEnabled(true);
        iv2.setEnabled(false);
        iv3.setEnabled(false);
        iv4.setEnabled(false);
        chooseFromMap.setEnabled(false);
    }

    private void setEnableAllEditText(EditText editText1, EditText editText2, EditText editText3, EditText editText4) {
        editText1.setEnabled(true);
        editText2.setEnabled(true);
        editText3.setEnabled(true);
        editText4.setEnabled(true);
        chooseFromMap.setEnabled(true);
    }

    private void setEnableImageViewClear(ImageView iv1, ImageView iv2, ImageView iv3, ImageView iv4) {
        iv1.setEnabled(true);
        iv2.setEnabled(true);
        iv3.setEnabled(true);
        iv4.setEnabled(true);
    }

    // VIEWS
    private RelativeLayout chooseFromMap;
    private RelativeLayout homeRl;
    private RelativeLayout workRl;
    private LinearLayout homeAndWorkLl;

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
    public static final String KEY_SHOW_FOR = "ShowFor";
    public static final String KEY_HOME = "key_home";
    public static final String KEY_OFFICE = "key_office";
    public static final String KEY_RESULT_BOOKING = "result";

    public TextView dateTimeEt;

    // STRING
    private String mSearchString;
    private String allowanceTime = "";
    private String placeId = "";

    // INTEGER
    private int flightPosition = -1;
    private int indexEditTextPosition = 1;
    private int locAndFieldArrayListPosition = 0;
    private int mYear, mMonth, mDay, mHour, mMinute;

    // BOOLEAN
    private boolean isSelected = false;
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

    // Handlers
    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if(!isRunningGetAddress) {
                boolean isWebDispatch = sp.getString(CommonVariables.IS_WEB_DISPATCH, "0").equals("1");
                boolean isGoogleEnable = sp.getString(CommonVariables.EnableGoogleForSuggestion, "0").equals("1");
                if (isGoogleEnable) {
                    new Manager_GetAddressFromGoogle(getActivity(), mSearchString, Activity_SearchAddress.this);
                } else {
                    new Manager_GetAddressDetails(mSearchString, isWebDispatch, Activity_SearchAddress.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonMethods.getInstance().setDarkAndNightColorGreyWhite(Activity_SearchAddress.this);

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
                resetScreen();
                clearFocusOfAllEditText();

                setInitialData(data, "onActivityResult");

                makeRoute();
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
        Activity_Splash.LAT = sp.getString(Config.BaseLat, "");
        Activity_Splash.LNG = sp.getString(Config.BaseLng, "");
        HomeFragment.specialInstruction = "";

    }

    private Activity getActivity() {
        return this;
    }

    private Activity getContext() {
        return this;
    }

    private void listener() {

        setPickUpBoxListener();
        setDropOffBoxListener();
        setVia_1_BoxListener();
        setVia_2_BoxListener();

        deleteHomeIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!hTv.getText().toString().equalsIgnoreCase(p.getAddHome()))
                        new SweetAlertDialog(Activity_SearchAddress.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(p.getAreYouSure())
                                .setContentText(p.getWannaDeleteHome() + "?")
                                .setCancelText(p.getNo())
                                .setConfirmText(p.getYes())
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        new SweetAlertDialog(Activity_SearchAddress.this, SweetAlertDialog.SUCCESS_TYPE)
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
                        new SweetAlertDialog(Activity_SearchAddress.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(p.getAreYouSure())
                                .setContentText(p.getWannaDeleteWork() + "?")
                                .setCancelText(p.getNo())
                                .setConfirmText(p.getYes())
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        new SweetAlertDialog(Activity_SearchAddress.this, SweetAlertDialog.SUCCESS_TYPE)
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
//                stopCallBacks = true;
//                if (mHandler != null) {
//                    mHandler.removeCallbacks(mRunnable);
//                }
////                Intent intent = new Intent();
////                intent.putParcelableArrayListExtra("key_locAndFieldArrayList", searchEditTextArrayList);
////                setResult(RESULT_OK, intent);
//                finish();
                //back button new work
                gotoToHome();
            }
        });

        addViaTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newLocAndFieldList.size() > 3) {
                    Toast.makeText(getContext(), "Can't add more than 2 Via", Toast.LENGTH_SHORT).show();
                    return;
                }

                LocAndField locAndField = newLocAndFieldList.get(newLocAndFieldList.size() - 1);
                newLocAndFieldList.add(locAndField);

                locAndField = new LocAndField();
                newLocAndFieldList.set(newLocAndFieldList.size() - 2, locAndField);

                if (newLocAndFieldList.size() == 3) {
                    via_1_box_Rl.setVisibility(VISIBLE);
                    via_2_box_Rl.setVisibility(GONE);
                }

                if (newLocAndFieldList.size() == 4) {
                    via_1_box_Rl.setVisibility(VISIBLE);
                    via_2_box_Rl.setVisibility(VISIBLE);
                }
            }
        });

        chooseFromMap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Activity_SetLocationOnMap.class);
                if (newLocAndFieldList.size() == 2) {
                    if (pickupEt.hasFocus()) {
                        intent.putExtra("setFrom", "pickup");
                        indexEditTextPosition = 0;
                    }
                    if (dropoffEt.hasFocus()) {
                        intent.putExtra("setFrom", "dropoff");
                        indexEditTextPosition = 1;
                    }
                }
                if (newLocAndFieldList.size() == 3) {
                    if (pickupEt.hasFocus()) {
                        intent.putExtra("setFrom", "pickup");
                        indexEditTextPosition = 0;
                    }
                    if (via_1_Et.hasFocus()) {
                        intent.putExtra("setFrom", "via 1");
                        indexEditTextPosition = 1;

                    }
                    if (dropoffEt.hasFocus()) {
                        intent.putExtra("setFrom", "dropoff");
                        indexEditTextPosition = 2;
                    }
                }
                if (newLocAndFieldList.size() == 4) {
                    if (pickupEt.hasFocus()) {
                        intent.putExtra("setFrom", "pickup");
                        indexEditTextPosition = 0;
                    }
                    if (via_1_Et.hasFocus()) {
                        intent.putExtra("setFrom", "via 1");
                        indexEditTextPosition = 1;

                    }
                    if (via_2_Et.hasFocus()) {
                        intent.putExtra("setFrom", "via 2");
                        indexEditTextPosition = 2;
                    }
                    if (dropoffEt.hasFocus()) {
                        intent.putExtra("setFrom", "dropoff");
                        indexEditTextPosition = 3;
                    }
                }

                intent.putExtra("indexEditTextPosition", indexEditTextPosition);
                intent.putParcelableArrayListExtra("key_locAndFieldArrayList", newLocAndFieldList);
                startActivityForResult(intent, 5051);
            }
        });

        doneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                makeRoute();
            }
        });

        homeRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hTv.getText().toString().toLowerCase().trim().equals("add home")) {
                    startActivity(new Intent(Activity_SearchAddress.this, Activity_SearchAddressForHomeAndWork.class).putExtra("setFrom", "home"));
                } else {

                    clickedFromRecentList = true;

                    if (pickupEt.hasFocus()) {

                        if (checkIfAlReadyAvailable(homeAddress)) return;

                        clearFocusOfAllEditText();
                        newLocAndFieldList.set(0, homeAddress);
                        pickupEt.setText(homeAddress.getField());
                        pickupEt.requestFocus();
                    }

                    if (dropoffEt.hasFocus()) {

                        if (checkIfAlReadyAvailable(homeAddress)) return;

                        clearFocusOfAllEditText();
                        newLocAndFieldList.set(newLocAndFieldList.size() - 1, homeAddress);
                        dropoffEt.setText(homeAddress.getField());
                        dropoffEt.requestFocus();
                    }

                    if (newLocAndFieldList.size() == 3) {
                        if (via_1_Et.hasFocus()) {

                            if (checkIfAlReadyAvailable(homeAddress)) return;

                            clearFocusOfAllEditText();
                            newLocAndFieldList.set(1, homeAddress);
                            via_1_Et.setText(homeAddress.getField());
                            via_1_Et.requestFocus();
                        }
                    }

                    if (newLocAndFieldList.size() == 4) {
                        if (via_1_Et.hasFocus()) {

                            if (checkIfAlReadyAvailable(homeAddress)) return;

                            clearFocusOfAllEditText();
                            newLocAndFieldList.set(1, homeAddress);
                            via_1_Et.setText(homeAddress.getField());
                            via_1_Et.requestFocus();
                        }
                        if (via_2_Et.hasFocus()) {

                            if (checkIfAlReadyAvailable(homeAddress)) return;

                            clearFocusOfAllEditText();
                            newLocAndFieldList.set(2, homeAddress);
                            via_2_Et.setText(homeAddress.getField());
                            via_2_Et.requestFocus();
                        }
                    }

                    makeRoute();
                }
            }
        });

        workRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wTv.getText().toString().toLowerCase().trim().equals("add work")) {
                    startActivity(new Intent(Activity_SearchAddress.this, Activity_SearchAddressForHomeAndWork.class).putExtra("setFrom", "work"));
                } else {

                    clickedFromRecentList = true;

                    if (pickupEt.hasFocus()) {

                        if (checkIfAlReadyAvailable(workAddress)) return;

                        clearFocusOfAllEditText();
                        newLocAndFieldList.set(0, workAddress);
                        pickupEt.setText(workAddress.getField());
                        pickupEt.requestFocus();
                    }

                    if (dropoffEt.hasFocus()) {

                        if (checkIfAlReadyAvailable(workAddress)) return;

                        clearFocusOfAllEditText();
                        newLocAndFieldList.set(newLocAndFieldList.size() - 1, workAddress);
                        dropoffEt.setText(workAddress.getField());
                        dropoffEt.requestFocus();
                    }

                    if (newLocAndFieldList.size() == 3) {
                        if (via_1_Et.hasFocus()) {

                            if (checkIfAlReadyAvailable(workAddress)) return;

                            clearFocusOfAllEditText();
                            newLocAndFieldList.set(1, workAddress);
                            via_1_Et.setText(workAddress.getField());
                            via_1_Et.requestFocus();
                        }
                    }

                    if (newLocAndFieldList.size() == 4) {
                        if (via_1_Et.hasFocus()) {

                            if (checkIfAlReadyAvailable(workAddress)) return;

                            clearFocusOfAllEditText();
                            newLocAndFieldList.set(1, workAddress);
                            via_1_Et.setText(workAddress.getField());
                            via_1_Et.requestFocus();
                        }
                        if (via_2_Et.hasFocus()) {

                            if (checkIfAlReadyAvailable(workAddress)) return;

                            clearFocusOfAllEditText();
                            newLocAndFieldList.set(2, workAddress);
                            via_2_Et.setText(workAddress.getField());
                            via_2_Et.requestFocus();
                        }
                    }

                    makeRoute();
                }
            }
        });
    }

    private boolean checkIfAlReadyAvailable(LocAndField locAndField) {
        boolean isAvailable = false;
        if (pickupEt.getText().toString().equals(locAndField.getField())) {
            FBToast.infoToast(Activity_SearchAddress.this, "Address Already available", FBToast.LENGTH_SHORT);
            isAvailable = true;
        }
        if (dropoffEt.getText().toString().equals(locAndField.getField())) {
            FBToast.infoToast(Activity_SearchAddress.this, "Address Already available", FBToast.LENGTH_SHORT);
            isAvailable = true;
        }
        if (via_1_Et.getText().toString().equals(locAndField.getField())) {
            FBToast.infoToast(Activity_SearchAddress.this, "Address Already available", FBToast.LENGTH_SHORT);
            isAvailable = true;
        }
        if (via_2_Et.getText().toString().equals(locAndField.getField())) {
            FBToast.infoToast(Activity_SearchAddress.this, "Address Already available", FBToast.LENGTH_SHORT);
            isAvailable = true;
        }
        return isAvailable;
    }

    String locationType = "Address";

    private void makeRoute() {

        if (_for.toLowerCase().equals("pickup")) {
            newLocAndFieldList.get(0).setLocationType(locationType);
        }

        if (_for.toLowerCase().equals("dropoff")) {
            newLocAndFieldList.get(newLocAndFieldList.size() - 1).setLocationType(locationType);
        }

        if (_for.toLowerCase().equals("via 1")) {
            newLocAndFieldList.get(1).setLocationType(locationType);
        }

        if (_for.toLowerCase().equals("via 2")) {
            newLocAndFieldList.get(2).setLocationType(locationType);
        }

        boolean isEmpty = false;

        for (int i = 0; i < newLocAndFieldList.size(); i++) {
            if (newLocAndFieldList.get(i).getField().equals("")) {
                isEmpty = true;
            }
        }
        if (!isEmpty) {
            try {
                HomeFragment.clickFrom = "pickup";
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("key_locAndFieldArrayList", newLocAndFieldList);
                setResult(RESULT_OK, intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //back button new work
    private void gotoToHome() {
        stopCallBacks = true;
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        try {
            Intent intent = new Intent();
            intent.putExtra("isReset", true);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String _for = "";
    private void makeRoute(LocAndField locAndField) {
        boolean isEmpty = false;

        if (_for.toLowerCase().equals("pickup")) {
            locAndField.setField(pickupEt.getText().toString());
            newLocAndFieldList.set(0, locAndField);
        }

        if (_for.toLowerCase().equals("dropoff")) {
            locAndField.setField(dropoffEt.getText().toString());
            newLocAndFieldList.set(newLocAndFieldList.size() - 1, locAndField);
        }

        if (_for.toLowerCase().equals("via 1")) {
            locAndField.setField(via_1_Et.getText().toString());
            newLocAndFieldList.set(1, locAndField);
        }

        if (_for.toLowerCase().equals("via 2")) {
            locAndField.setField(via_2_Et.getText().toString());
            newLocAndFieldList.set(2, locAndField);
        }

        for (int i = 0; i < newLocAndFieldList.size(); i++) {
            if (newLocAndFieldList.get(i).getField().equals("")) {
                isEmpty = true;
            }
        }
        if (!isEmpty) {
            try {
                HomeFragment.clickFrom = "pickup";
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("key_locAndFieldArrayList", newLocAndFieldList);
                setResult(RESULT_OK, intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void getAddressCoordinates(ArrayList<LocAndField> requestBody,boolean forAirPort){

        boolean isWebDispatch = sp.getString(CommonVariables.IS_WEB_DISPATCH, "0").equals("1");


        new Manager_GetAddressCoordinates(getContext(),isWebDispatch, getBodyForAddressCoordinate(requestBody), new Listener_GetAddressCoordinate() {
            SweetAlertDialog mDialog;

            @Override
            public void onPre() {

                mDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                mDialog.setTitleText("Getting details");
                mDialog.setContentText(p.getPleaseWait());
                mDialog.setCancelable(false);
                mDialog.show();
            }

            @Override
            public void onComplete(String result) {
                try {
                    mDialog.dismiss();
                    JSONObject parentObject = new JSONObject(result);

                    if (parentObject.getBoolean("HasError")) {
                    } else {
                        LocAndField requestedFiled = requestBody.get(0);
                        JSONObject data = parentObject.getJSONObject("Data");
                        if(isWebDispatch){
                            JSONArray clsLocationDatas = data.getJSONArray("searchLocations");
                            if(clsLocationDatas.length() > 0 ){
                                JSONObject field = clsLocationDatas.getJSONObject(0);
                                String lat = field.getString("Latitude");
                                String lng = field.getString("Longitude");
                                requestedFiled.setLat(lat);
                                requestedFiled.setLon(lng);
                            }
                        }else {
                            JSONArray clsLocationDatas = data.getJSONArray("clsLocationDatas");
                            if (clsLocationDatas.length() > 0) {
                                JSONObject field = clsLocationDatas.getJSONObject(0);
                                String lat = field.getString("lat");
                                String lng = field.getString("lng");
                                requestedFiled.setLat(lat);
                                requestedFiled.setLon(lng);
                            }
                        }
                        if (forAirPort) {
                            requestedFiled.locationType ="Airport";
                            getAirportDetails(requestedFiled);
                        } else {
                            makeRoute(requestedFiled);
                        }

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }



    private ArrayList<ClsLocationData> getBodyForAddressCoordinate(ArrayList<LocAndField> list) {

        ArrayList<ClsLocationData> arrayList = new ArrayList();

        for (int i = 0; i < list.size(); i++) {
            LocAndField locAndField = list.get(i);

            arrayList.add(new ClsLocationData((int) CommonVariables.localid,
                    "" + locAndField.getField(), 0, 0,
                    "" + CommonVariables.GOOGLE_API_KEY,
                    "" + CommonVariables.Clientip,
                    CommonVariables.localid + "4321orue"));
        }
        return arrayList;

    }

    private void init() {
        setBoxViews();
        homeAndWorkLl = findViewById(R.id.homeAndWorkLl);

        setLocationOnMapLabel = findViewById(R.id.setLocationOnMapLabel);
        setLocationOnMapLabel.setText(p.getSetLocationOnMapLabel());

        progressBar = findViewById(R.id.progressBar);
        backIv = findViewById(R.id.backIv);

        deleteHomeIv = findViewById(R.id.deleteHomeIv);
        deleteWorkIv = findViewById(R.id.deleteWorkIv);

        recentListRv = findViewById(R.id.recentListRv);
        recentListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        recentListRv.setHasFixedSize(true);

        chooseFromMap = findViewById(R.id.chooseFromMap);
        try {
            // Hide Marker Work
            if (sp.getString(Config.HideCurrentLocation, "0").equals("1")) {
                chooseFromMap.setVisibility(GONE);
            } else {
                chooseFromMap.setVisibility(VISIBLE);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

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

        setInitialData(getIntent(), "");
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

    private void setInitialData(Intent data, String from) {

        try {
            String addVai = sp.getString(CommonVariables.enableVia, "1");
            if (addVai.equals("0")) {
                addViaTv.setVisibility(View.INVISIBLE);
            }
            addViaTv.setText((addVai.equals("0")) ? "" : " + Add (Via)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchEditTextArrayList.clear();
        try {
            newLocAndFieldList.clear();
            ArrayList<LocAndField> locAndFields = data.getParcelableArrayListExtra("key_locAndFieldArrayList");///
            addDataForEditTextList(locAndFields, from);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (HomeFragment.clickFrom.equals("pickup")) {
                pickupEt.requestFocus();
                setDrawable(pickupEt);
                via_1_Et.clearFocus();
                via_2_Et.clearFocus();
                dropoffEt.clearFocus();
            }

            if (HomeFragment.clickFrom.equals("drop")) {

                dropoffEt.requestFocus();
                setDrawable(dropoffEt);
                via_1_Et.clearFocus();
                via_2_Et.clearFocus();
                pickupEt.clearFocus();
            }

            if (HomeFragment.clickFrom.equals("via 1")) {
                via_1_Et.requestFocus();

                via_2_Et.clearFocus();
                dropoffEt.clearFocus();
                pickupEt.clearFocus();
            }

            if (HomeFragment.clickFrom.equals("via 2")) {
                via_2_Et.requestFocus();

                via_1_Et.clearFocus();
                dropoffEt.clearFocus();
                pickupEt.clearFocus();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addDataForEditTextList(ArrayList<LocAndField> locAndFields, String from) {
        try {

            for (LocAndField locAndField : locAndFields) {
                LocAndField l = new LocAndField();
                newLocAndFieldList.add(locAndField);
            }

            if (newLocAndFieldList.size() == 0) {
                newLocAndFieldList.add(new LocAndField());
            }

            if (newLocAndFieldList.size() == 1) {
                newLocAndFieldList.add(new LocAndField());
            }

            // if list size is 2
            if (newLocAndFieldList.size() == 2) {
                via_1_box_Rl.setVisibility(GONE);
                via_2_box_Rl.setVisibility(GONE);

                // Pick Up
                if (!newLocAndFieldList.get(0).getField().equals("")) {
                    pickupEt.setText(newLocAndFieldList.get(0).getField()); ///
                    if (!from.equals("onActivityResult")) {
                        pickupEt.clearFocus();
                        dropoffEt.requestFocus();
                    }
                }
                if (newLocAndFieldList.get(0).getField().equals("")) {
                    pickupEt.setText(newLocAndFieldList.get(0).getField());
                    if (!from.equals("onActivityResult")) {
                        pickupEt.requestFocus();
                        dropoffEt.clearFocus();
                    }
                }

                // Drop Off
                if (!newLocAndFieldList.get(1).getField().equals("")) {
                    dropoffEt.setText(newLocAndFieldList.get(1).getField());
                    if (!from.equals("onActivityResult")) {
                        dropoffEt.clearFocus();
                        pickupEt.requestFocus();
                    }
                }
                if (newLocAndFieldList.get(1).getField().equals("")) {
                    dropoffEt.setText(newLocAndFieldList.get(1).getField());
                    if (!from.equals("onActivityResult")) {
                        dropoffEt.requestFocus();
                        pickupEt.clearFocus();
                    }
                }
            }

            // if list size is 3
            if (newLocAndFieldList.size() == 3) {
                via_1_box_Rl.setVisibility(GONE);

                // Pick Up
                if (!newLocAndFieldList.get(0).getField().equals("")) {
                    pickupEt.setText(newLocAndFieldList.get(0).getField());
                    pickupEt.clearFocus();
                }
                if (newLocAndFieldList.get(0).getField().equals("")) {
                    pickupEt.setText(newLocAndFieldList.get(0).getField());
                    if (!from.equals("onActivityResult")) {
                        pickupEt.requestFocus();
                    }
                }

                // Via 1
                via_1_box_Rl.setVisibility(VISIBLE);
                if (!newLocAndFieldList.get(1).getField().equals("")) {
                    via_1_Et.setText(newLocAndFieldList.get(1).getField());
                    via_1_Et.clearFocus();
                }
                if (newLocAndFieldList.get(1).getField().equals("")) {
                    via_1_Et.setText(newLocAndFieldList.get(1).getField());
                    if (!from.equals("onActivityResult")) {
                        via_1_Et.requestFocus();
                    }
                }


                // Drop Off
                if (!newLocAndFieldList.get(2).getField().equals("")) {
                    dropoffEt.setText(newLocAndFieldList.get(2).getField());
                    dropoffEt.clearFocus();
                }
                if (newLocAndFieldList.get(2).getField().equals("")) {
                    dropoffEt.setText(newLocAndFieldList.get(2).getField());
                    if (!from.equals("onActivityResult")) {
                        dropoffEt.requestFocus();
                    }
                }
            }

            // if list size is 4
            if (newLocAndFieldList.size() == 4) {
                via_1_box_Rl.setVisibility(VISIBLE);
                via_2_box_Rl.setVisibility(VISIBLE);

                // Pick Up
                if (!newLocAndFieldList.get(0).getField().equals("")) {
                    pickupEt.setText(newLocAndFieldList.get(0).getField());
                    pickupEt.clearFocus();
                }
                if (newLocAndFieldList.get(0).getField().equals("")) {
                    pickupEt.setText(newLocAndFieldList.get(0).getField());
                    if (!from.equals("onActivityResult")) {
                        pickupEt.requestFocus();
                    }
                }

                // Via 1
                if (!newLocAndFieldList.get(1).getField().equals("")) {
                    via_1_Et.setText(newLocAndFieldList.get(1).getField());
                    via_1_Et.clearFocus();
                }
                if (newLocAndFieldList.get(1).getField().equals("")) {
                    via_1_Et.setText(newLocAndFieldList.get(1).getField());
                    if (!from.equals("onActivityResult")) {
                        via_1_Et.requestFocus();
                    }
                }


                // Via 2
                if (!newLocAndFieldList.get(2).getField().equals("")) {
                    via_2_Et.setText(newLocAndFieldList.get(2).getField());
                    via_2_Et.clearFocus();
                }
                if (newLocAndFieldList.get(2).getField().equals("")) {
                    via_2_Et.setText(newLocAndFieldList.get(2).getField());
                    if (!from.equals("onActivityResult")) {
                        via_2_Et.requestFocus();
                    }
                }


                // Drop Off
                if (!newLocAndFieldList.get(3).getField().equals("")) {
                    dropoffEt.setText(newLocAndFieldList.get(3).getField());
                    dropoffEt.clearFocus();
                }
                if (newLocAndFieldList.get(3).getField().equals("")) {
                    dropoffEt.setText(newLocAndFieldList.get(3).getField());
                    if (!from.equals("onActivityResult")) {
                        dropoffEt.requestFocus();
                    }
                }

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
    }

    // For Google
    private void setData(LocAndField locAndField, String locationType) {
        isSelected = true;
        locAndField.setLocationType(locationType);
        locAndField.setSelected(true);
        locAndField.setField(locAndFieldArrayList.get(locAndFieldArrayListPosition).getField());
        searchEditTextArrayList.set(indexEditTextPosition, locAndField);
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
                            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

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
                    new Manager_GetFlightDetails(Activity_SearchAddress.this, flightEditText.getText().toString().trim(), Activity_SearchAddress.this).execute();
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
                new SweetAlertDialog(Activity_SearchAddress.this, SweetAlertDialog.FLIGHT_INPUT)
                        .setTitleText(p.getFlightDetails())
                        .setContentText(p.getAlreadyAtAirport() + "?")
                        .setConfirmText(p.getYes())
                        .showCancelButton(true)
                        .setCancelText(p.getSelectFlightNo())
                        .setFlightListener(new SweetAlertDialog.OnSweetFlightListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog, String flightNo, String comingFrom, String atAirport) {
                                sweetAlertDialog.dismissWithAnimation();
                                makeRoute();
//                                makeFinalRoute(null, "Airport");
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                makeRoute();
//                                makeFinalRoute(null, "Airport");
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
                        new Manager_GetFlightDetails(Activity_SearchAddress.this, flightEditText.getText().toString().trim(), Activity_SearchAddress.this).execute();
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
                    new Manager_GetFlightDetails(Activity_SearchAddress.this, flightEditText.getText().toString().trim(), Activity_SearchAddress.this).execute();
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
                makeRoute();
            }
        });

        TextView cancelBtn = innerDialog.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                innerDialog.dismiss();
                dialog.dismiss();
                makeRoute();

//                makeFinalRoute(null, "Airport");
            }
        });

        innerDialog.show();
    }

    ArrayList<LocAndField> newLocAndFieldList = new ArrayList<>();

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
//            locAndFieldArrayList.clear();
            locAndFieldArrayList = arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.layout_adapter_item_searched, parent, false);
            return new MyViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecentListAdapter.MyViewHolder h, @SuppressLint("RecyclerView") int position) {
            Log.d(TAG, "onBindViewHolder: ---- recent");
            LocAndField field = locAndFieldArrayList.get(h.getAdapterPosition());
            try {
                if (field.getLocationType().contains("Airport") || field.getField().toLowerCase().contains("airport")) {
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

                    clickedFromRecentList = true;
                    resetScreen();
                    try {
                        placeId = field.getPlaceId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(pickupEt.hasFocus()){
                        HomeFragment.fromDoorNo = "";
                        HomeFragment.fromComing = "";
                        _for = "pickup";
                        newLocAndFieldList.set(0, field);
                        pickupEt.setText(field.getField());

                        if (field.getLocationType().contains("Airport") || field.getField().toLowerCase().contains("airport")) {
                            locationType = "Airport";

                            if(isLatlngEmpty(field)) {
                                ArrayList<LocAndField> list = new ArrayList<LocAndField>();
                                list.add(field);
                                getAddressCoordinates(list, true);
                            }

                            else{
                                getAirportDetails(field);
                            }
                        }

                        if(newLocAndFieldList.size() == 2 && dropoffEt.getText().toString().trim().isEmpty()){
                            pickupEt.clearFocus();
                            dropoffEt.requestFocus();
                            createRequestLatLang(field);

                        }

                        else if(newLocAndFieldList.size() == 3 ){
                            if(via_1_Et.getText().toString().trim().isEmpty()){
                                pickupEt.clearFocus();
                                via_1_Et.requestFocus();
                                createRequestLatLang(field);
                            }
                            else if(dropoffEt.getText().toString().trim().isEmpty()){
                                pickupEt.clearFocus();
                                dropoffEt.requestFocus();
                                createRequestLatLang(field);
                            }
                            else{
                                createRequestLatLang(field);
                            }
                        }
                        else if(newLocAndFieldList.size() == 4 ){

                            if(via_1_Et.getText().toString().trim().isEmpty()){
                                pickupEt.clearFocus();
                                via_1_Et.requestFocus();
                                createRequestLatLang(field);
                            }

                            else if(via_2_Et.getText().toString().trim().isEmpty()){
                                pickupEt.clearFocus();
                                via_2_Et.requestFocus();
                                createRequestLatLang(field);
                            }

                            else if(dropoffEt.getText().toString().trim().isEmpty()){
                                pickupEt.clearFocus();
                                dropoffEt.requestFocus();
                                createRequestLatLang(field);
                            }
                            else{
                                createRequestLatLang(field);
                            }
                        }
                        else{
                            createRequestLatLang(field);
                        }

                    }

                    else if (dropoffEt.hasFocus()) {
                        if (field.getLocationType().contains("Airport") || field.getField().toLowerCase().contains("airport")) {
                            locationType = "Airport";
                        }
                        _for = "dropoff";
                        newLocAndFieldList.set((newLocAndFieldList.size() - 1), field);
                        dropoffEt.setText(field.getField());

                        if(newLocAndFieldList.size() == 2 && pickupEt.getText().toString().trim().isEmpty()){
                            dropoffEt.clearFocus();
                            pickupEt.requestFocus();
                            createRequestLatLang(field);
                        }
                        else if(newLocAndFieldList.size() == 3 && via_1_Et.getText().toString().trim().isEmpty()){
                            dropoffEt.clearFocus();
                            via_1_Et.requestFocus();
                            createRequestLatLang(field);
                        }
                        else if(newLocAndFieldList.size() == 4 && via_2_Et.getText().toString().trim().isEmpty()){
                            dropoffEt.clearFocus();
                            via_2_Et.requestFocus();
                            createRequestLatLang(field);
                        }
                        else{
                            createRequestLatLang(field);
                        }

                    }

                    else if (via_1_Et.hasFocus()) {
                        if (field.getLocationType().contains("Airport") || field.getField().toLowerCase().contains("airport")) {locationType = "Airport";}
                        newLocAndFieldList.set(1, field);
                        via_1_Et.setText(field.getField());
                        _for = "via 1";
                        if(newLocAndFieldList.size() == 3){

                            if(pickupEt.getText().toString().trim().isEmpty()){
                                via_1_Et.clearFocus();
                                pickupEt.requestFocus();
                                createRequestLatLang(field);
                            }

                            else if(dropoffEt.getText().toString().trim().isEmpty()){
                                via_1_Et.clearFocus();
                                dropoffEt.requestFocus();
                                createRequestLatLang(field);
                            }else{
                                createRequestLatLang(field);
                            }

                        }
                        else if(newLocAndFieldList.size() == 4){

                            if(pickupEt.getText().toString().trim().isEmpty()){
                                via_1_Et.clearFocus();
                                pickupEt.requestFocus();
                                createRequestLatLang(field);
                            }
                            else if(via_2_Et.getText().toString().trim().isEmpty()){
                                via_1_Et.clearFocus();
                                via_2_Et.requestFocus();
                                createRequestLatLang(field);
                            }
                            else if(dropoffEt.getText().toString().trim().isEmpty()){
                                via_1_Et.clearFocus();
                                dropoffEt.requestFocus();
                                createRequestLatLang(field);
                            }

                            else{
                                createRequestLatLang(field);
                            }

                        }
                        else{
                            createRequestLatLang(field);
                        }

                    }

                    else if (via_2_Et.hasFocus()) {
                        if (field.getLocationType().contains("Airport") || field.getField().toLowerCase().contains("airport")) {
                            locationType = "Airport";
                        }
                        newLocAndFieldList.set(2, field);
                        via_2_Et.setText(field.getField());
                        _for = "via 2";
                        if(pickupEt.getText().toString().trim().isEmpty()){
                            via_2_Et.clearFocus();
                            pickupEt.requestFocus();
                            createRequestLatLang(field);
                        }
                        else if(via_1_Et.getText().toString().trim().isEmpty()){
                            via_2_Et.clearFocus();
                            via_1_Et.requestFocus();
                            createRequestLatLang(field);
                        }
                        else if(dropoffEt.getText().toString().trim().isEmpty()){
                            via_2_Et.clearFocus();
                            dropoffEt.requestFocus();
                            createRequestLatLang(field);
                        }
                        else{
                            createRequestLatLang(field);
                        }
                    }

                    else{
                        createRequestLatLang(field);
                    }

                }
            });
        }

        public boolean isLatlngEmpty(LocAndField field ){
            return  field.getField().equals("")
                    || field.getLat().equals("")
                    || field.getLon().equals("")
                    || field.getLat().equals("0")
                    || field.getLon().equals("0");

        }

        public void createRequestLatLang(LocAndField filed) {
            boolean isGoogleEnable = sp.getString(CommonVariables.EnableGoogleForSuggestion, "0").equals("1");
            // boolean isWebDispatch = sp.getString(CommonVariables.IS_WEB_DISPATCH, "0").equals("0");

            if(isGoogleEnable){
                new Manager_GetLatLnfFromPlaceId((Activity) context, "" + placeId, Activity_SearchAddress.this);
            }else{

                if(filed.getField().equals("")
                        || filed.getLat().equals("")
                        || filed.getLon().equals("")
                        || filed.getLat().equals("0")
                        || filed.getLon().equals("0")
                        || filed.getLat().equals("0.0")
                        || filed.getLon().equals("0.0")){
                    ArrayList<LocAndField> list = new ArrayList<LocAndField>();
                    list.add(filed);
                    getAddressCoordinates(list,false);
                }
                else {
                    makeRoute();
                }
            }
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


    private void getAirportDetails(LocAndField field){
        new Manager_GetAirportDetails(Activity_SearchAddress.this, field.getField(), p, new Listener_GetAirportDetails() {
            @Override
            public void onCompleteAirportDetails(String result) {

                try {
                    if (result.startsWith("error_")) {
                        result = result.replace("error_", "");
                        FBToast.errorToast(getContext(), result, FBToast.LENGTH_SHORT);
                        new Manager_GetLatLnfFromPlaceId(Activity_SearchAddress.this, "" + placeId, Activity_SearchAddress.this);
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    String response = object.getString("HasError");
                    if (!response.equals("true")) {
                        String message = object.getString("Data");
                        if (message.length() != 0) {
                            showAirportInfo(message);
                        } else {
                            new SweetAlertDialog(Activity_SearchAddress.this, SweetAlertDialog.FLIGHT_INPUT)
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
                                            makeRoute();

//                                    makeFinalRoute(null, "Airport");
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            makeRoute();
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
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.FLIGHT_INPUT)
                                .setTitleText("Flight Details")
                                .setContentText("You want to delete your work location!")
                                .setConfirmText("Yes")
                                .showCancelButton(true)
                                .setCancelText(p.getSelectFlightNo())
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
                                                //FBToast.errorToast(getActivity(), "Please enter flight no", FBToast.LENGTH_SHORT);
                                                //return;
                                            }
                                        }
                                        HomeFragment.fromDoorNo = "Already at airport.";
                                        new Manager_GetLatLnfFromPlaceId(Activity_SearchAddress.this, "" + placeId, Activity_SearchAddress.this);
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
                                        if (sp.getString(CommonVariables.enableOutsideUK, "0").equals("0")) {
                                            searchForFlightNo();
                                        } else {
                                            showMenuDialogAirport();
                                        }
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
        }).execute();

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

    ArrayList<LocAndField> arrayListLocAndField = new ArrayList<>();

    private void parseForDispatch(String result) {
        try {
            arrayListLocAndField.clear();
            JSONObject parentObject = new JSONObject(result);

            if (parentObject.getBoolean("HasError")) {
                FBToast.errorToast(getContext(), parentObject.optString("Message"), FBToast.LENGTH_SHORT);
            } else {

                JSONObject dataobj = parentObject.optJSONObject("Data");
                if (dataobj != null) {
                    JSONArray jsonArray = dataobj.optJSONArray("searchLocations");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject addressJson = jsonArray.getJSONObject(i);
                            String address = checkIfHasNullForString(addressJson.getString("FullLocationName"));
                            LocAndField field = new LocAndField();
                            field.setField(address);
                            field.setLocationType(checkIfHasNullForString(addressJson.getString("LocationType")));
                            field.setLat(checkIfHasNullForString(addressJson.getString("Latitude")));
                            field.setLon(checkIfHasNullForString(addressJson.getString("Longitude")));
                            if (!field.getLat().equals("") && !field.getLon().equals("")) {
                                arrayListLocAndField.add(field);
                            }
                        }
                    }
                }
            }

            RecentListAdapter recentListAdapter = new RecentListAdapter(getContext(), arrayListLocAndField);
            recentListRv.setAdapter(recentListAdapter);
            recentListRv.setVisibility(VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GetAddress
    @Override
    public void onCompleteGetAddress(String result) {
        try {
            setEnableImageViewClear(pickUpClearEdittextIv, via_1_ClearEdittextIv, via_2_ClearEdittextIv, dropOffClearEdittextIv);
            setEnableAllEditText(pickupEt, via_1_Et, via_2_Et, dropoffEt);
            // Hide Marker Work
            if (sp.getString(Config.HideCurrentLocation, "0").equals("1")) {
                chooseFromMap.setVisibility(GONE);
            } else {
                chooseFromMap.setVisibility(VISIBLE);
            }
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

            if (sp.getString(CommonVariables.EnableGoogleForSuggestion, "0").equals("0")) {
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
                JSONObject dataObject = parentObject.optJSONObject("Data");
                if (dataObject != null) {
                    JSONArray flightDataArr = dataObject.optJSONArray("flightData");
                    if (flightDataArr != null) {
                        String FlightNo = checkIfHasNullForString(flightDataArr.getJSONObject(0).getString("FlightNo"));
                        String ScheduleDateTime = checkIfHasNullForString(flightDataArr.getJSONObject(0).getString("ScheduleDateTime"));
                        String DateTime = checkIfHasNullForString(flightDataArr.getJSONObject(0).getString("DateTime"));

                        String ArrivalTerminal = checkIfHasNullForString(flightDataArr.getJSONObject(0).getString("ArrivalTerminal"));
                        String ArrivingFrom = checkIfHasNullForString(flightDataArr.getJSONObject(0).getString("ArrivingFrom"));
                        String Message = checkIfHasNullForString(flightDataArr.getJSONObject(0).getString("Message"));
                        String Status = checkIfHasNullForString(flightDataArr.getJSONObject(0).getString("Status"));

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
                    }
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

    private void showMenuDialogAirport() {
        Dialog dialog = new Dialog(this, android.R.style.Widget_DeviceDefault);
        dialog.setContentView(R.layout.dialog_airport);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        EditText flightNoEt = dialog.findViewById(R.id.flightNoEt);
        EditText comingFromEt = dialog.findViewById(R.id.comingFromEt);
        EditText allowanceTimeEt = dialog.findViewById(R.id.minutesEt);

        TextView leftTv = dialog.findViewById(R.id.leftTv);
        leftTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String comingFrom = comingFromEt.getText().toString().trim();
                String flightNo = flightNoEt.getText().toString().trim();
                String allowanceTime = allowanceTimeEt.getText().toString().trim();

                if (flightNo != null && !flightNo.equals("")) {
                    HomeFragment.fromDoorNo = flightNo;
                    HomeFragment.fromComing = comingFrom;
                } else {
                    FBToast.errorToast(getActivity(), "Please enter flight no", FBToast.LENGTH_SHORT);
                    return;
                }

                if (allowanceTime.length() == 0) {
                    Toast.makeText(getContext(), "Please enter minutes", Toast.LENGTH_SHORT).show();
                    return;
                }

                allowanceTime = allowanceTimeEt.getText().toString();
                HomeFragment.specialInstruction = allowanceTime;
                new Manager_GetLatLnfFromPlaceId(Activity_SearchAddress.this, "" + placeId, Activity_SearchAddress.this);
                dialog.dismiss();
            }
        });

        TextView rightTv = dialog.findViewById(R.id.rightTv);
        rightTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

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
            locAndField.setLocationType("Address");

            JSONArray types = resultRes.optJSONArray("types");
            for (int i = 0; i < types.length(); i++) {
                String type = types.getString(i);
                if (type.equalsIgnoreCase("airport")) {
                    locAndField.setLocationType("Airport");
                    break;
                }
            }
            makeRoute(locAndField);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        stopCallBacks = true;
//        if (mHandler != null) {
//            mHandler.removeCallbacks(mRunnable);
//        }
//        finish();
//back button new work
        gotoToHome();
    }
}