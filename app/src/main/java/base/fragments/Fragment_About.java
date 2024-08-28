package base.fragments;

import static base.utils.CommonMethods.checkIfHasNullForString;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eurosoft.customerapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import org.json.JSONObject;

import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.listener.Listener_GetInfo;
import base.manager.Manager_GetInfo;
import base.models.ParentPojo;
import base.utils.Config;

public class Fragment_About extends Fragment {

    private static final String REGEX_NUMBER_SEPERATORS = "[\\,\\\\\\/]";

    private TextView aboutTitleLabel;
    private TextView websitelabel;
    private TextView versionTv;
    private TextView addressTv;
    private TextView totalTimeForArrival;
    private TextView subname;

    private LinearLayout rl;
    private LinearLayout layout_phone;
    private LinearLayout layout_email;
    private LinearLayout layout_powered;

    private ImageView menuIv;
    private ImageView workIv;

    // String
    private String Appverison = "1.0";

    // Objects
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private ParentPojo p = new ParentPojo();
    private SharedPreferences sharedPreferences;
    private DatabaseOperations mOperation;

    private String PhoneNumber = "";
    private String ReceivingEmail = "";
    private String WebSiteAddress = "";
    private String URL = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setObjects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.layout_about, container, false);

        init(ret);

        initViews();

        listener();

        return ret;
    }

    private void initViews() {
        addressTv.setText(sharedPreferences.getString(Config.BaseAddress, ""));
    }

    private void setObjects() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mOperation = new DatabaseOperations(new DatabaseHelper(getActivity()));
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            Appverison = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listener() {

        layout_email.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:" + ReceivingEmail));
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        layout_phone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = PhoneNumber.trim();
                final String[] numbers = number.split(REGEX_NUMBER_SEPERATORS);

                if (numbers != null && numbers.length > 1) {
                    new AlertDialog.Builder(getActivity()).setTitle("Select Number").setItems(numbers, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, int which) {

                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", numbers[which].trim(), null));
                            startActivity(intent);
                        }
                    }).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",  number.trim(), null));
                    startActivity(intent);
                }
            }
        });

        layout_powered.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!WebSiteAddress.toLowerCase().contains("http"))
                        WebSiteAddress = "http://" + WebSiteAddress;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebSiteAddress));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        menuIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Fragment_Main) getActivity()).toggleDrawer();
                getFragmentManager().popBackStack();
            }
        });

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                setDarkAndNightThemeColor();
                lockMap();

                String baseLat = sharedPreferences.getString(Config.BaseLat, "0");
                String baseLng = sharedPreferences.getString(Config.BaseLng, "0");
                if (baseLat != null && !baseLat.equals("")) {
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(baseLat), Double.parseDouble(baseLng)), 17);
                    googleMap.animateCamera(update);

//                    MarkerOptions markerOptions = new MarkerOptions();
//                    markerOptions.position(new LatLng(Double.parseDouble(baseLat), Double.parseDouble(baseLng)));
//                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
//                    googleMap.addMarker(markerOptions);
                } else {
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(51.509865, -0.118092), 15);
                    googleMap.animateCamera(update);
                }
            }
        });

        new Manager_GetInfo(getContext(), p, new Listener_GetInfo() {
            @Override
            public void onComplete(String result) {
                result = checkIfHasNullForString(result);
                if (!result.isEmpty() && isAdded()) {
                    try {
                        JSONObject parentObject = new JSONObject(result);
                        if (parentObject.getBoolean("HasError")) {
                            getDataFromDatabase();
                        } else {
                            JSONObject dataObject = parentObject.optJSONObject("Data");
                            if (dataObject != null) {
                                JSONObject webSettingsObject = dataObject.optJSONObject("webSettings");
                                if (webSettingsObject != null) {
                                    PhoneNumber = checkIfHasNullForString(webSettingsObject.optString("PhoneNumber"));
                                    ReceivingEmail = checkIfHasNullForString(webSettingsObject.optString("ReceivingEmail"));
                                    WebSiteAddress = checkIfHasNullForString(webSettingsObject.optString("WebSiteAddress"));
                                    URL = checkIfHasNullForString(webSettingsObject.optString("URL"));

                                    mOperation.inserInfo(PhoneNumber, ReceivingEmail, WebSiteAddress, URL);
                                }
                            }
                        }
                    } catch (Exception e) {
                        getDataFromDatabase();
                    }
                } else {
                    getDataFromDatabase();
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getDataFromDatabase() {
        try {
            String[] values = mOperation.getInfoValues();
            if (values != null && isAdded()) {
                PhoneNumber = values[0];
                ReceivingEmail = values[1];
                WebSiteAddress = values[2];
                URL = values[3];
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void init(View ret) {
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_container);

        workIv = ret.findViewById(R.id.workIv);
        workIv.setVisibility(View.VISIBLE);

        rl = ret.findViewById(R.id.rl);
        rl.setVisibility(View.VISIBLE);

        totalTimeForArrival = ret.findViewById(R.id.totalTimeForArrival);
        totalTimeForArrival.setVisibility(View.GONE);

        subname = ret.findViewById(R.id.subname);
        subname.setVisibility(View.GONE);


        addressTv = ret.findViewById(R.id.addressTv);

        versionTv = ret.findViewById(R.id.versionTv);
        versionTv.setText("V " + Appverison);
        versionTv.setVisibility(Appverison.trim().equals("") ? View.GONE : View.VISIBLE);

        menuIv = ret.findViewById(R.id.menuIv);

        layout_email = ret.findViewById(R.id.layout_email);
        layout_phone = ret.findViewById(R.id.layout_phone);
        layout_powered = ret.findViewById(R.id.layout_powered);



        aboutTitleLabel = ret.findViewById(R.id.titleTv);
        aboutTitleLabel.setText(p.getAbout());

        websitelabel = ret.findViewById(R.id.websitelabel);
        websitelabel.setText(p.getWebsite());
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

    private void lockMap() {
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
//			mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
    }
}