package base.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static base.utils.CommonMethods.checkIfHasNullForString;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eurosoft.customerapp.R;
import com.support.parser.RegexPatterns;
import com.tfb.fbtoast.FBToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.listener.Listener_GetAddress;
import base.listener.Listener_GetAddressCoordinate;
import base.listener.Listener_GetLatLngFromPlaceId;
import base.manager.Manager_GetAddressCoordinates;
import base.manager.Manager_GetAddressDetails;
import base.manager.Manager_GetAddressFromGoogle;
import base.manager.Manager_GetLatLnfFromPlaceId;
import base.models.ClsLocationData;
import base.models.LocAndField;
import base.newui.HomeFragment;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class Activity_SearchAddressForHomeAndWork extends AppCompatActivity implements
        Listener_GetAddress,
        Listener_GetLatLngFromPlaceId {

    private static final String TAG = "Activity_SearchAddressForHomeAndWork";

    // VIEWS
    private ProgressBar progressBar;
    private RelativeLayout chooseFromMap;
    private LinearLayout homeAndWorkLl;
    private View view_line;

    private RecyclerView recentListRv;

    private TextView addViaTv;
    private ImageView backIv;

    private CardView doneBtn;

    // JAVA

    // INTEGER
    private int indexEditTextPosition = 1;
    private int locAndFieldArrayListPosition = -1;

    // BOOLEAN
    private boolean stopCallBacks = false;
    private boolean isRunningGetAddress = false;
    private boolean isSelected = false;

    // STRING
    private String mSearchString;

    // STATIC
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

    // ARRAY_LIST
    private ArrayList<LocAndField> searchEditTextArrayList = new ArrayList<>();
    private ArrayList<LocAndField> locAndFieldArrayList = new ArrayList<>();

    private SharedPrefrenceHelper helper;
    private SharedPreferences sp = null;
    private String placeId = "";


    private RelativeLayout pickup_box_Rl;
    private EditText pickupEt;
    private TextView pickupTv;
    private ImageView pickUpClearEdittextIv;


    private RelativeLayout via_1_box_Rl;
    private RelativeLayout via_2_box_Rl;
    private RelativeLayout dropoff_box_Rl;
    boolean clickedFromRecentList = false;
    private Timer pickupTimer = new Timer();

    private void setBoxViews() {
        pickup_box_Rl = findViewById(R.id.pickup_box_Rl);
        pickup_box_Rl.setVisibility(VISIBLE);
        pickupTv = findViewById(R.id.pickupTv);
        pickupEt = findViewById(R.id.pickupEt);
        pickupEt.requestFocus();
        pickupTv.setText("Add " + getIntent().getStringExtra("setFrom"));
        pickUpClearEdittextIv = findViewById(R.id.pickUpClearEdittextIv);

        via_1_box_Rl = findViewById(R.id.via_1_box_Rl);
        via_1_box_Rl.setVisibility(GONE);

        via_2_box_Rl = findViewById(R.id.via_2_box_Rl);
        via_2_box_Rl.setVisibility(GONE);

        dropoff_box_Rl = findViewById(R.id.dropoff_box_Rl);
        dropoff_box_Rl.setVisibility(GONE);

        setPickUpBoxListener();
    }

    private void setPickUpBoxListener() {
        pickupEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
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
                            return;
                        }

                        pickupTimer.cancel();
                        pickupTimer = new Timer();
                        chooseFromMap.setVisibility(View.INVISIBLE);
                        addViaTv.setVisibility(View.INVISIBLE);

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
                pickupEt.requestFocus();
                pickupEt.setText("");
            }
        });
    }


    // Handlers
    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {

            if(!isRunningGetAddress) {
                boolean isWebDispatch = sp.getString(CommonVariables.IS_WEB_DISPATCH, "0").equals("1");
                boolean isGoogleEnable = sp.getString(CommonVariables.EnableGoogleForSuggestion, "0").equals("1");
                if (isGoogleEnable) {
                    new Manager_GetAddressFromGoogle(getActivity(), mSearchString, Activity_SearchAddressForHomeAndWork.this);
                } else {
                    new Manager_GetAddressDetails(mSearchString, isWebDispatch, Activity_SearchAddressForHomeAndWork.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }

        }
    };

    private Activity getActivity() {
        return this;
    }

    private Activity getContext() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonMethods.getInstance().setDarkAndNightColorGreyWhite(Activity_SearchAddressForHomeAndWork.this);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        setContentView(R.layout.layout_search__address);
        mHandler = new Handler();
        helper = new SharedPrefrenceHelper(this);
        try {
            init();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            listener();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void init() {

        setBoxViews();
        view_line = findViewById(R.id.view_line);
        view_line.setVisibility(GONE);
        backIv = findViewById(R.id.backIv);
        homeAndWorkLl = findViewById(R.id.homeAndWorkLl);
        homeAndWorkLl.setVisibility(GONE);

        progressBar = findViewById(R.id.progressBar);

        recentListRv = findViewById(R.id.recentListRv);
        recentListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        recentListRv.setHasFixedSize(true);

        chooseFromMap = findViewById(R.id.chooseFromMap);

        // Hide Marker Work
        try{

            if(sp.getString(Config.HideCurrentLocation,"0").equals("1")) {
                chooseFromMap.setVisibility(GONE);
            }
            else{
                chooseFromMap.setVisibility(VISIBLE);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        addViaTv = findViewById(R.id.addViaTv);
        addViaTv.setText("");

        backIv = findViewById(R.id.backIv);

        doneBtn = findViewById(R.id.doneBtn);
        doneBtn.setVisibility(GONE);

        searchEditTextArrayList.add(new LocAndField());
    }

    private void listener() {
        backIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopCallBacks = true;
                if (mHandler != null) {
                    mHandler.removeCallbacks(mRunnable);
                }
                finish();
            }
        });

        chooseFromMap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Activity_SearchAddressForHomeAndWork.this, Activity_SetLocationOnMap.class);
                    intent.putExtra("setFrom", getIntent().getStringExtra("setFrom"));
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void search() {
        mHandler.removeCallbacks(mRunnable);
        isRunningGetAddress = false;
        mHandler.postDelayed(mRunnable, 1000);
    }

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

            LocAndField field = locAndFieldArrayList.get(position);
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

            h.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    locAndFieldArrayListPosition = h.getAdapterPosition();

                    try {
                        placeId = field.getPlaceId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    boolean isGoogleEnable = sp.getString(CommonVariables.EnableGoogleForSuggestion, "0").equals("1");

                    if(isGoogleEnable){
                        new Manager_GetLatLnfFromPlaceId((Activity) context, "" + placeId, Activity_SearchAddressForHomeAndWork.this);
                    }else{

                        LocAndField filed = locAndFieldArrayList.get(position);

                        if(filed.getField().equals("")
                                || filed.getLat().equals("")
                                || filed.getLon().equals("")
                                || filed.getLat().equals("0")
                                || filed.getLon().equals("0")){
                            ArrayList<LocAndField> list = new ArrayList<LocAndField>();
                            list.add(filed);
                            getAddressCoordinates(list);

                        }else {
                            arrangeHomeAndWorkData(locAndFieldArrayList.get(position));
                        }
                    }
//                    if (sp.getString(CommonVariables.enableOutsideUK, "0").equals("0")) {
//                        // 0 = Inside UK
//                        arrangeHomeAndWorkData(locAndFieldArrayList.get(position));
//                    } else {
//                        // 1 = Outside Uk
//                        new Manager_GetLatLnfFromPlaceId((Activity) context, "" + placeId, Activity_SearchAddressForHomeAndWork.this);
//                    }
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

    private void arrangeHomeAndWorkData(LocAndField locAndField) {
        try {
            String key = "";

            if (getIntent().getStringExtra("setFrom").equals("home")) {
                key = KEY_HOME;

            } else if (getIntent().getStringExtra("setFrom").equals("work")) {
                key = KEY_OFFICE;
            }


            helper.putLocAndFieldModel(helper.getSettingModel().getEmail() + "_" + key, locAndField);

            finish();

        } catch (Exception e) {
            e.printStackTrace();
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
        try {
            arrayList.clear();
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
                                arrayList.add(field);
                            }
                        }
                    }
                }
            }

            RecentListAdapter recentListAdapter = new RecentListAdapter(getContext(), arrayList);
            recentListRv.setAdapter(recentListAdapter);
            recentListRv.setVisibility(VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getAddressCoordinates(ArrayList<LocAndField> requestBody){
        boolean isWebDispatch = sp.getString(CommonVariables.IS_WEB_DISPATCH, "0").equals("1");
        new Manager_GetAddressCoordinates(getContext(),isWebDispatch, getBodyForAddressCoordinate(requestBody), new Listener_GetAddressCoordinate() {

            SweetAlertDialog mDialog;
            @Override
            public void onPre() {

                mDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                mDialog.setTitleText("");
                mDialog.setContentText("Please wait..");
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
                        LocAndField requestedFiled  = requestBody.get(0);
                        JSONObject data = parentObject.getJSONObject("Data");
                        JSONArray clsLocationDatas = data.getJSONArray("clsLocationDatas");
                        if(clsLocationDatas.length() > 0){
                            JSONObject field = clsLocationDatas.getJSONObject(0);
                            String lat = field.getString("lat");
                            String lng = field.getString("lng");
                            requestedFiled.setLat(lat);
                            requestedFiled.setLon(lng);

                            arrangeHomeAndWorkData(requestedFiled);
                        }

                    }

                }catch (Exception ex){
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

    @Override
    public void onCompleteGetAddress(String result) {
        progressBar.setVisibility(GONE);
        isSelected = false;
        if (stopCallBacks) return;
        try {
            if (result.startsWith("error_")) {
                result = result.replace("error_", "");
                FBToast.errorToast(getContext(), result, FBToast.LENGTH_SHORT);
                return;
            }

            if (sp.getString(CommonVariables.EnableGoogleForSuggestion, "0").equals("1")) {
                parseForGoogle(result);

            } else {

                parseForDispatch(result);
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

            locAndField.setField(locAndFieldArrayList.get(locAndFieldArrayListPosition).getField());
            locAndField.setLat("" + lat);
            locAndField.setLon("" + lng);
            locAndField.setAddressType("Address");
            arrangeHomeAndWorkData(locAndField);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
