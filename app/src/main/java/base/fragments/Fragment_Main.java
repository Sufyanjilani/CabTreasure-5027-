package base.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static base.utils.CommonVariables.CurrencySymbol;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.eurosoft.customerapp.BuildConfig;
import com.eurosoft.customerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.support.parser.PropertyInfo;
import com.support.parser.SoapHelper;
import com.tfb.fbtoast.FBToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import base.activities.Activity_HomePayment;
import base.activities.Activity_Splash;
import base.activities.Activity_Start;
import base.adapters.MenuAdapterListView;
import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.listener.Listener_BookingCompleted;
import base.listener.Listener_SubmitFeedback;
import base.listener.Listener_UpdateBookingList;
import base.manager.Manager_StripePrePayment;
import base.manager.Manager_SubmitFeedback;
import base.models.DriverInformation;
import base.models.FeedbackInformation;
import base.models.MenuModel;
import base.models.Model_BookingDetailsModel;
import base.models.ParentPojo;
import base.models.SettingsModel;
import base.models.ShareTracking;
import base.models.Stripe_Model;
import base.newui.HomeFragment;
import base.services.Service_NotifyStatus;
import base.utils.AppConstants;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Fragment_Main extends FragmentActivity implements OnItemClickListener, Listener_BookingCompleted, Listener_UpdateBookingList {
    public static Fragment_Main mainContext;
    public String miles, userId;
    private DatabaseOperations mDatabaseOperations;
    private ListView mListView;
    private RecyclerView menuRv;
    private Fragment replacement = null;
    private String[] itemname;
    private Integer[] imgid;
    private MenuAdapterListView mAdapter;
    //    public SharedPreferences firstBooking;
//    public static final String MyPREFERENCES = "MyPrefs";
//    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private SharedPrefrenceHelper mHelper;
    private SettingsModel userModel;
    private ParentPojo p = new ParentPojo();
    private View footer;
    private DrawerLayout mDrawerLayout;
    private View header;
    public static TextView NameTv, PhoneTv, EmailTv;
    private Dialog ratingDialogue;
    public static final String KEY_REDIRECTED = "keyRedirected";
    private DriverInformation driverInformation;
    private FeedbackInformation feedbackInformation;
    public static boolean fromTracking = false;

    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromTracking = false;
        setDarkAndNightThemeColor();
        setContentView(R.layout.layout_new_main_fragment);
        createObject();
        init();
        initData();
        listener();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        boolean isnotifyBooking = false;
        Bundle extrass = getIntent().getExtras();

        if (extrass != null) {
            if (extrass.getBoolean("bookingfrag")) {
                isnotifyBooking = extrass.getBoolean("bookingfrag");
                GotoIntent(isnotifyBooking);
            }
        }
    }

    private void setDarkAndNightThemeColor() {
        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
//                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.color_white_inverse));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
//                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_bg_white));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
    }

//    private void initMenuList() {
//        menuRv = findViewById(R.id.menuRv);
//        menuRv.setLayoutManager(new LinearLayoutManager(this));
//        menuRv.setHasFixedSize(true);
//
//        ArrayList<MenuModel> menuModels = new ArrayList<>();
//        menuModels.add(new MenuModel(p.getYourTrips(), R.drawable.ic_note));
//        menuModels.add(new MenuModel(p.getPayment(), R.drawable.ic_cash));
//        menuModels.add(new MenuModel(p.getUserProfile(), R.drawable.ic_setting));
//        menuModels.add(new MenuModel(p.getInviteYourFriend(), R.drawable.shareapp));
//
//        if (sp.getString(Config.ShowPromoList, "0").equals("1")) {
//            menuModels.add(new MenuModel(p.getPromotions(), R.drawable.promo));
//        }
//
//        menuModels.add(new MenuModel(p.getAbout(), R.drawable.ic_person));
//
//        MenuAdapter menuAdapter = new MenuAdapter(this, menuModels);
//        menuRv.setAdapter(menuAdapter);
//        menuAdapter.notifyDataSetChanged();
//    }

    private void listener() {
        ((LinearLayout) header.findViewById(R.id.headerLytHeading)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
                onItemClick(null, null, 3, 0);
            }
        });
        ((LinearLayout) header.findViewById(R.id.header_layout2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
                onItemClick(null, null, 99, 0);
            }
        });
        mListView.setOnItemClickListener(this);

        listenerSubmitFeedback = new Listener_SubmitFeedback() {
            @Override
            public void onComplete(String response) {
                Context context = Fragment_Main.this;

                if (response != null && !response.startsWith("false") && !response.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("HasError")) {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
                                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("")
                                            .setContentText(jsonObject.getString("Message"))
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    try {
                                                        feedbackDialog.dismiss();
                                                        ratingDialogue = null;
                                                        mHelper.putIntVal("rating_" + feedbackInformation.BookingReference, ratings);
                                                        mHelper.putVal("feedback_" + feedbackInformation.BookingReference, feedbackInformation.Message);
                                                        CommonVariables.AppMainActivity.ShowBookingList();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
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
                                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Success")
                                                        .setContentText(message + "\nTransaction successful: " + TransactionID)
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                                feedbackDialog.dismiss();

                                                                ratingDialogue = null;
                                                                mHelper.putIntVal("rating_" + feedbackInformation.BookingReference, ratings);
                                                                mHelper.putVal("feedback_" + feedbackInformation.BookingReference, feedbackInformation.Message);

                                                                try {
                                                                    float tf = Float.parseFloat(tipFares.trim());
                                                                    if (tf > 0) {
                                                                        mHelper.putVal("tipfare_" + feedbackInformation.BookingReference, String.format("%.2f", Float.parseFloat(tipFares)));
                                                                    }
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                CommonVariables.AppMainActivity.ShowBookingList();
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
                    FBToast.errorToast(getApplicationContext(), "Payment Faile\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                }
            }
        };
    }

    private void createObject() {

        try {
            from_screen = (getIntent().getStringExtra("from_auth") == null) ? "" : getIntent().getStringExtra("from_auth");

        } catch (Exception e) {
            from_screen = "";
            e.printStackTrace();
        }
        mainContext = this;
        driverInformation = new DriverInformation();
        feedbackInformation = new FeedbackInformation();
        CommonVariables.AppMainActivity = this;

        mHelper = new SharedPrefrenceHelper(this);
        userModel = mHelper.getSettingModel();
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        mDatabaseOperations = new DatabaseOperations(new DatabaseHelper(getApplicationContext()));

        rotateScreen();

        Boolean isComingFromBookingConfirmed = false;
        Bundle extrass = getIntent().getExtras();
        if (extrass != null) {
            if (extrass.getBoolean("bookingfrag")) {
                isComingFromBookingConfirmed = true;
            }
        }

        if (isComingFromBookingConfirmed) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, FragmentGenerator.getFragment(FragmentGenerator.BOOKING), "current").commit();
        } else if (userModel.getName().equals("")) {
            onItemClick(null, null, 3, 0);
        } else if (getIntent() != null && getIntent().getBooleanExtra("promofrag", false)) {
            onItemClick(null, null, 5, 0);
        } else {
            if (from_screen.equals("auth_screen")) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, FragmentGenerator.getFragment(FragmentGenerator.PaymentType), "current").commit();
            } else {
                if (mDatabaseOperations.getActiveBookingsCount() > 0) {
                    // foreground service setting
                    if(sp.getString(CommonVariables.Enable_ForeGround_Service,"0").equals("1")) {
                        try {
                            startService(new Intent(this, Service_NotifyStatus.class));
                            bindServiceWithActivity();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, FragmentGenerator.getFragment(FragmentGenerator.BOOKING), "current").commit();
                } else {
                    try {
                        if (isServiceRunning(Service_NotifyStatus.class)) {
                            stopService(new Intent(this, Service_NotifyStatus.class));
                            unBindService();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, FragmentGenerator.getFragment(FragmentGenerator.HOME), "current").commit();
                }
            }
        }

        if (!sp.getString(Config.ShowPromoList, "0").equals("1")) {
            itemname = new String[]{p.getYourTrips(), p.getPayment(), p.getUserProfile(), p.getInviteYourFriend(), p.getAbout(), ""};
            imgid = new Integer[]{R.drawable.ic_note, R.drawable.ic_cash, R.drawable.ic_setting, R.drawable.shareapp, R.drawable.ic_person, 0};
        } else {
            itemname = new String[]{p.getYourTrips(), p.getPayment(), p.getUserProfile(), p.getInviteYourFriend(), p.getPromotions(), p.getAbout(), ""};
            imgid = new Integer[]{R.drawable.ic_note, R.drawable.ic_cash, R.drawable.ic_setting, R.drawable.shareapp, R.drawable.promo, R.drawable.ic_person, 0};
        }

        ArrayList<MenuModel> menuModelArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < itemname.length; i++) {
                try {
                    MenuModel menuModel = new MenuModel(itemname[i], imgid[i]);
                    menuModelArrayList.add(menuModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mAdapter = new MenuAdapterListView(this, menuModelArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void init() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(android.R.drawable.dark_header, Gravity.RIGHT);
        mListView = (ListView) findViewById(R.id.left_drawer);
        mListView.setBackgroundResource(R.drawable.curve_list_view);
        AppConstants.getAppConstants().applicationContext(this);
        header = getLayoutInflater().inflate(R.layout.layout_navigation_header, mListView, false);
        NameTv = header.findViewById(R.id.U_Name);
        PhoneTv = header.findViewById(R.id.U_Phone);
        EmailTv = header.findViewById(R.id.U_Email);

        footer = getLayoutInflater().inflate(R.layout.footer, mListView, false);
    }

    String from_screen = "";

    private void initData() {

        if (!userModel.getName().contains(userModel.getlName())) {
            NameTv.setText(userModel.getName() +" " + userModel.getlName());
        } else {
            NameTv.setText(userModel.getName());
        }
//        NameTv.setText(userModel.getName().toUpperCase());
        PhoneTv.setText(userModel.getPhoneNo());
        EmailTv.setText(userModel.getEmail());

        mListView.addHeaderView(header);
        FirebaseApp.initializeApp(this);

        if (!sp.getString(CommonVariables.enableSignup, "1").equals("0")) {
            mListView.addFooterView(footer);
        }
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                try {
                    if (!task.isSuccessful()) {
//                                Log.e("", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().toString();

                    Log.e("Main", "Token =" + token);
                    if (sp.getString(Config.FCMTOKEN + "_" + userModel.getUserServerID(), "").equals("") || !sp.getString(Config.FCMTOKEN, "").equals(token)) {
                        new UpdateFCMTokenToServer().execute(token);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


       /* FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        try {
                            if (!task.isSuccessful()) {
//                                Log.e("", "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            Log.e("Main", "Token =" + token);
                            if (sp.getString(Config.FCMTOKEN + "_" + userModel.getUserServerID(), "").equals("") || !sp.getString(Config.FCMTOKEN, "").equals(token)) {
                                new UpdateFCMTokenToServer().execute(token);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });*/

        try {
            mListView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sp.getString(Config.ShowPromoList, "0").equals("1") && sp.getString("isPromoLoaded", "0").equals("0")) {
            new GetPromotions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void GotoIntent(boolean isBooking) {
        if (isBooking) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, FragmentGenerator.getFragment(FragmentGenerator.BOOKING), "current").commit();
        }
    }

    public void toggleDrawer() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            if (mDrawerLayout != null) {
                if (mDrawerLayout.isDrawerOpen(mListView)) {
                    mDrawerLayout.closeDrawer(mListView);
                } else {
                    userModel = new SharedPrefrenceHelper(Fragment_Main.this).getSettingModel();
                    if (!userModel.getName().contains(userModel.getlName())) {
                        ((TextView) header.findViewById(R.id.U_Name)).setText(userModel.getName().toUpperCase()+" "+userModel.getlName().toUpperCase());
                    } else {
                        ((TextView) header.findViewById(R.id.U_Name)).setText(userModel.getName().toUpperCase());
                    }

                    Bitmap bitmap = getThumbnail(userModel.getUserServerID() + ".jpeg");
                    if (bitmap != null) {
                        int width = bitmap.getWidth();
                        int Height = bitmap.getHeight();
                        if (width > 700) {
                            int scaledWidth = width / 2;
                            int scaledHeight = Height / 2;
                            bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false);
                        }
                        ((ImageView) header.findViewById(R.id.userPic)).setImageBitmap(bitmap);
                    } else {
                        ((ImageView) header.findViewById(R.id.userPic)).setImageDrawable(getResources().getDrawable(R.drawable.userprofile));
                    }
                    mListView.removeHeaderView(header);
                    mListView.addHeaderView(header);
                    mDrawerLayout.openDrawer(mListView);
                }
            }
        }
    }

    public void ShowBookingList() {
        Fragment replacement = FragmentGenerator.getFragment(FragmentGenerator.BOOKING);

        if (replacement != null) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit).replace(R.id.content_frame, replacement, "current").commit();
            mDrawerLayout.closeDrawer(mListView);
        }
    }

    public void ShowBookingList(boolean isAsap, Bundle bundle) {
        Fragment replacement = FragmentGenerator.getFragment((isAsap) ? FragmentGenerator.Tracking : FragmentGenerator.BOOKING);
        if (bundle != null && isAsap) {
            replacement.setArguments(bundle);
        }
        if (replacement != null) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit).replace(R.id.content_frame, replacement, "current").commit();
            mDrawerLayout.closeDrawer(mListView);
        }
    }

    public boolean saveImageToInternalStorage(String caritem, Bitmap image) {

        try {
// Use the compress method on the Bitmap object to write image to
// the OutputStream
            removeFile(caritem);
            FileOutputStream fos = openFileOutput(caritem.toLowerCase() + ".jpeg", Context.MODE_PRIVATE);

// Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

    public boolean removeFile(String name) {
        File dir = getFilesDir();
        File file = new File(dir, name + ".jpeg");
        boolean delete = false;
        if (file.exists()) {
            delete = file.delete();
        } else {
            delete = true;
        }
        return delete;
    }

    public Bitmap getThumbnail(String filename) {

//        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;
        Bitmap thumbnail = null;


// If no file on external storage, look in internal storage
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            if (parent != null) {
                toggleDrawer();
            }

            switch (position) {
                case 99:
                    replacement = FragmentGenerator.getFragment(FragmentGenerator.HOME);
                    HomeFragment.isHomeVissibel = true;
                    break;

                case 1:
                    replacement = FragmentGenerator.getFragment(FragmentGenerator.BOOKING);
                    HomeFragment.isHomeVissibel = false;
                    break;

                case 2:
                    replacement = FragmentGenerator.getFragment(FragmentGenerator.PaymentType);
                    HomeFragment.isHomeVissibel = false;
                    break;

                case 3:
                    replacement = FragmentGenerator.getFragment(FragmentGenerator.SETTING);
                    HomeFragment.isHomeVissibel = false;
                    break;

                case 5:
                    if (sp.getString(Config.ShowPromoList, "0").equals("1")) {
                        replacement = FragmentGenerator.getFragment(FragmentGenerator.PROMO);
                        HomeFragment.isHomeVissibel = false;
                    } else {


                        replacement = FragmentGenerator.getFragment(FragmentGenerator.Info);
                        HomeFragment.isHomeVissibel = false;
                        if (parent == null) {
                            Bundle args = new Bundle();
                            args.putBoolean(KEY_REDIRECTED, true);
                            replacement.setArguments(args);
                            HomeFragment.isHomeVissibel = false;
                        }
                    }
                    break;

                case 4:
                    try {
                        String appname = getResources().getString(R.string.app_name);
                        String pkgName = getPackageName();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TITLE, appname);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "Download " + appname + " app from the below link: \nhttps://play.google.com/store/apps/details?id=" + pkgName);
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, "Invite via..."));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 6:
                    if (sp.getString(Config.ShowPromoList, "0").equals("1")) {
                        replacement = FragmentGenerator.getFragment(FragmentGenerator.Info);
                        HomeFragment.isHomeVissibel = false;
                        if (parent == null) {
                            Bundle args = new Bundle();
                            args.putBoolean(KEY_REDIRECTED, true);
                            replacement.setArguments(args);
                            HomeFragment.isHomeVissibel = false;
                        }
                    }
                    break;

                case 7:
                case 8:

                  @SuppressLint("ResourceType") String confirmButtonColor =  getResources().getString(R.color.color_gray_and_footer_inverse);
                    new SweetAlertDialog(Fragment_Main.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("")
                            .setContentText(p.getAreYouSureLogout())
                            .setCancelText(p.getNo())
                            .setConfirmText(p.getYes())
                            .setConfirmButtonBackgroundColor(confirmButtonColor)
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    removeAllSharedPreferencesDataBeforeLogout();

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
                    break;

                default:
                    break;
            }

            if (replacement != null) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit)
                        .replace(R.id.content_frame, replacement, "current").commit();

            }
        }
    }

    public static void cancelNotification(Context ctx) {
        NotificationManager notifManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }

    @Override
    public void bookingCompleted(Model_BookingDetailsModel modelBookingDetailsModel) {
        if (modelBookingDetailsModel == null) {
            unBindService();
            stopService(new Intent(this, Service_NotifyStatus.class));
            return;
        }

        try {
            if (getSupportFragmentManager().findFragmentByTag("current") instanceof Fragment_AllBooking) {
                Fragment_AllBooking bookingsActivity = (Fragment_AllBooking) getSupportFragmentManager().findFragmentByTag("current");
                bookingsActivity.getFromDb();
            }
        } catch (Exception e) {
        }
        if (mHelper.getVal("driverName_" + modelBookingDetailsModel.getRefrenceNo()) != null && !mHelper.getVal("driverName_" + modelBookingDetailsModel.getRefrenceNo()).equals("")) {

            if (ratingDialogue == null) {
//                ratingDialogue = showRatingsDialogue(modelBookingDetailsModel);
                ratingDialogue = ratingDialogDemo(modelBookingDetailsModel);
//                ratingDialogDemo();
                try {
                    unBindService();
                    stopService(new Intent(this, Service_NotifyStatus.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (!isJourneyDetailsRunning) {
                new getDriverJourneyDetails(modelBookingDetailsModel).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    @Override
    public void updateList(ArrayList<Model_BookingDetailsModel> models) {
        if (models == null) {
            return;
        }
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getSupportFragmentManager().findFragmentByTag("current") instanceof Fragment_AllBooking) {
                        Fragment_AllBooking bookingsActivity = (Fragment_AllBooking) getSupportFragmentManager().findFragmentByTag("current");
                        bookingsActivity._refreshList(models);
                        //            ((Fragment_AllBooking) bookingList)._refreshList(models);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class FragmentGenerator {
        public static final String HOME = "home";
        public static final String BOOKING = "booking";
        public static final String SETTING = "setting";
        public static final String FAVOURITES = "favourites";
        public static final String ASAP = "Pickup Date & Time";
        public static final String Tracking = "Track Your Vehicle";
        public static final String Info = "Info";
        public static final String PROMO = "PROMO";
        public static final String PaymentType = "Payment";

        public static Fragment getFragment(String type) {
            Fragment ret = new HomeFragment();
            if (type.equals(HOME))
                ret = new HomeFragment();
            else if (type.equals(BOOKING))
//                ret = new BookingsActivity();
                ret = new Fragment_AllBooking();
            else if (type.equals(SETTING))
                ret = new Fragment_UserProfile();
            else if (type.equals(ASAP))
                ret = new Fragment_PickupDateTime();
            else if (type.equals(Tracking))
                ret = new Fragment_Tracking();
            else if (type.equals(Info))
                ret = new Fragment_About();
            else if (type.equals(PaymentType))
                ret = new Fragment_Payment();
            else if (type.equals(PROMO))
                ret = new Fragment_Promo();

            return ret;
        }
    }

    public void showUserDetailsPopUp() {
        new AlertDialog.Builder(this).setMessage("User details are missing. Please enter user details").setTitle("User Details")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onItemClick(null, null, 3, 0);
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);

        if (arg0 == 4444) {
            if (sp.getString(CommonVariables.EnableTip, "0").equals("1")) {
                // Tip enable
                if (arg2 != null && !arg2.getStringExtra("pm").equals("")) {
                    String Gateway = sp.getString(Config.Gateway, "").toLowerCase();
                    if (Gateway.equalsIgnoreCase(Config.Stripe)) {
                        String pm = CommonMethods.checkIfHasNullForString(arg2.getStringExtra("pm"));
                        processStripePayment(pm, Double.parseDouble(tipFares));
                    }
                } else
                    FBToast.errorToast(this, "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
            } else
                // Tip Not enable
                FBToast.errorToast(this, "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
        }

        Fragment frg = getSupportFragmentManager().findFragmentByTag("current");
        if (frg != null) {
            frg.onActivityResult(arg0, arg1, arg2);
        }
        if (arg0 == 200 && arg1 != RESULT_OK) {
            rotateScreen();
//			Toast.makeText(Fragment_Main.this, "Location needs to enable to perform some action in this application", Toast.LENGTH_LONG).show();
//			if (android.provider.Settings.System.getInt(getContentResolver(),
//					Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
//				Toast.makeText(getApplicationContext(), "Rotation ON", Toast.LENGTH_SHORT).show();
//
//			} else {
//				Toast.makeText(getApplicationContext(), "Rotation OFF", Toast.LENGTH_SHORT).show();
//
//				Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, true ? 1 : 0);
//			}
        }
    }

    private void processStripePayment(String pm, double amount) {
        try {
            if (amount == 0) {
                FBToast.warningToast(this, "Amount should be a greater than " + sp.getString(CurrencySymbol, "\u00A3") + "0.30", FBToast.LENGTH_SHORT);
                return;
            }

            if (pm.equals("")) {
                FBToast.errorToast(this, "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                return;
            }

            new Manager_StripePrePayment(
                    this,
                    "" + pm,
                    "" + mHelper.getStripeCustomerId(),
                    "" + sp.getString(Config.Stripe_SecretKey, ""),
                    "" + amount,
                    response -> {
                        try {
                            if (response.startsWith("error_")) {
                                response = response.replace("error_", "");
                                FBToast.errorToast(this, response, FBToast.LENGTH_LONG);
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (response != null && !response.startsWith("false") && !response.equals("")) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("hasError")) {
                                    FBToast.errorToast(this, jsonObject.getString("message"), FBToast.LENGTH_LONG);
                                } else {
                                    if (jsonObject.getString("message").startsWith("Status :succeeded")) {
                                        String TransactionID = jsonObject.getString("transactionID");

                                        feedbackInformation.IsFeedback = true;
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

                                        new Manager_SubmitFeedback(this, feedbackInformation, listenerSubmitFeedback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                    } else {
                                        FBToast.errorToast(this, "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            FBToast.errorToast(this, "Payment Failed\nUnable to process payment, Please try again later", FBToast.LENGTH_SHORT);
                        }
                    })
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

    public interface IOnBackPressed {
        boolean _onBackPressed();
    }

    private IOnBackPressed OnBreakListen;

    public void setOnBackPressListen(IOnBackPressed onBackPressListen) {
        OnBreakListen = onBackPressListen;
    }

    public void unRegisterBackListener() {
        OnBreakListen = null;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("ISHOME", "" + HomeFragment.isHomeVissibel);
        Fragment home = getSupportFragmentManager().findFragmentByTag("current");
        //	Fragment bookings = getSupportFragmentManager().findFragmentByTag("current");
        boolean isHome = home != null && home instanceof HomeFragment;
        //boolean isBooking = home != null && home instanceof NewBookingDetails;
        if (isHome && home.isVisible() && count == 0) {
            if (OnBreakListen != null) {
                OnBreakListen._onBackPressed();
            } else {
                exitDialogue();
            }
        } else if (HomeFragment.isHomeVissibel) {
            if (OnBreakListen != null) {
                OnBreakListen._onBackPressed();
            } else {
                exitDialogue();
            }
        } else if (count > 0) {
            if (count == 2) {
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().popBackStack();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit)
                    .replace(R.id.content_frame, new HomeFragment(), "current").commit();
        }
    }

    public void exitDialogue() {



        @SuppressLint("ResourceType") String confirmButtonColor =  getResources().getString(R.color.color_gray_and_footer_inverse);

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("")
                .setContentText(p.getAreYouSureExit())
                .setCancelText(p.getNo())
                .setConfirmText(p.getYes())
                .setConfirmButtonBackgroundColor(confirmButtonColor)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        finish();
                        sDialog.dismiss();
                        System.exit(0);
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

    public static void setMiles(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getMiles(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ArrayList<Model_BookingDetailsModel> lastCompletedBooking = mDatabaseOperations.getLastCompletedBookings();
            if (lastCompletedBooking.size() > 0) {
                String feedbackstatus = mHelper.getVal("isfeedback_" + lastCompletedBooking.get(0).getRefrenceNo());
                if (!feedbackstatus.equals("1")) {
                    if (mHelper.getVal("driverName_" + lastCompletedBooking.get(0).getRefrenceNo()) != null && !mHelper.getVal("driverName_" + lastCompletedBooking.get(0).getRefrenceNo()).equals("")) {

                        if (ratingDialogue == null) {
//                            ratingDialogue = showRatingsDialogue(lastCompletedBooking.get(0));
                            ratingDialogue = ratingDialogDemo(lastCompletedBooking.get(0));
                        }
                    } else {
                        if (!isJourneyDetailsRunning) {
                            new getDriverJourneyDetails(lastCompletedBooking.get(0)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                }
            }

        } catch (Exception e) {

        }
        if (mService != null) {
            mService.setOnCompleteBookingListener(Fragment_Main.this);
            mService.setOnUpdateBookingListener(Fragment_Main.this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mService != null) {
            mService.setOnCompleteBookingListener(null);

        }
        if (ratingDialogue != null && ratingDialogue.isShowing()) {
            ratingDialogue.dismiss();
        }
    }

    public void bindServiceWithActivity() {

        // foreground service setting
        if(sp.getString(CommonVariables.Enable_ForeGround_Service,"0").equals("1")) {
            try {
                Intent intent = new Intent();
                intent.setClass(Fragment_Main.this, Service_NotifyStatus.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Service_NotifyStatus mService;
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Service_NotifyStatus.LocalBinder localBinder = (Service_NotifyStatus.LocalBinder) service;
            mService = localBinder.getService();
            mService.setOnCompleteBookingListener(Fragment_Main.this);
            mService.setOnUpdateBookingListener(Fragment_Main.this);
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Log.e("", "");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("", "");
        }
    };

    public void unBindService() {
        try {
            unbindService(mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void rotateScreen() {
        if (isTablet(this)) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.System.canWrite(getApplicationContext())) {
                    new AlertDialog.Builder(this)
                            .setTitle("Require Permission")
                            .setMessage("Enable screen rotation for perfect view")
                            .setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                                    startActivityForResult(intent, 200);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    if (Settings.System.getInt(getContentResolver(),
                            Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                        Toast.makeText(getApplicationContext(), "Rotation ON", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Rotation OFF", Toast.LENGTH_SHORT).show();

                        Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, true ? 1 : 0);
                    }
                }
//				if(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS)){
//					if (android.provider.Settings.System.getInt(getContentResolver(),
//							Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
//						Toast.makeText(getApplicationContext(), "Rotation ON", Toast.LENGTH_SHORT).show();
//
//					} else {
//						Toast.makeText(getApplicationContext(), "Rotation OFF", Toast.LENGTH_SHORT).show();
//
//						Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, true ? 1 : 0);
//					}
//				}	else{
//					requestPermissions(new String[]{
//									Manifest.permission.WRITE_SETTINGS
//							},
//							1121);
//				}
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unBindService();
        } catch (Exception e) {
        }

    }

    private class UpdateFCMTokenToServer extends AsyncTask<String, Void, String> {
        private String METHOD_NAME = "UpdateAppUserNotification";


        private static final String KEY_DEFAULT_CLIENT_ID = "defaultclientId";
        private static final String KEY_HASHKEY = "hashKey";

        private static final String Booking_information = "jsonString";
        private static final String HASHKEY_VALUE = "4321orue";

        private SweetAlertDialog mDialog;



        String token = "";

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

            if (result != null && !result.isEmpty()) {
                try {
                    JSONObject parentObject = new JSONObject(result);

                    if (parentObject.getBoolean("HasError")) {

                    } else {
                        sp.edit().putString(Config.FCMTOKEN + "_" + userModel.getUserServerID(), token).commit();
                    }
                } catch (Exception e) {

                }

            }


        }

        @Override
        protected String doInBackground(String... params) {
try {


    String deviceid = Settings.Secure.getString(Fragment_Main.this.getContentResolver(),
            Settings.Secure.ANDROID_ID);
    ShareTracking obj = new ShareTracking();
    obj.defaultClientId = (int) CommonVariables.clientid;
    obj.uniqueValue = CommonVariables.clientid + HASHKEY_VALUE;
    obj.UniqueId = deviceid;
    obj.DeviceInfo = "Android";
    obj.SubCompanyId = CommonVariables.SUB_COMPANY;
    obj.CustomerId = userModel.getUserServerID();
    obj.Email = userModel.getEmail();
    obj.NotifyToken = params[0];

    token = params[0];



    String userModel =new Gson().toJson(obj);

    JSONObject data = new JSONObject(userModel);

    String strData = data.toString(4);


    HashMap<String, Object> rootMap = new HashMap<>();
    String token = AppConstants.getAppConstants().getToken();

    rootMap.put("jsonString", strData);
    rootMap.put("Token", token);

    String jsonString = new Gson().toJson(rootMap);

    okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();

    okhttp3.RequestBody body = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json"), jsonString);
    okhttp3.Request request = new okhttp3.Request.Builder()
            .url(CommonVariables.BASE_URL + "UpdateAppUserNotification")
            .post(body)
            .build();

    try (okhttp3.Response response = client.newCall(request).execute()) {
        return Objects.requireNonNull(response.body()).string();
    } catch (Exception e) {
        e.printStackTrace();
        return "";
    }
}catch (Exception ex){
    ex.printStackTrace();
    return  null;
}
        }

    }

    private class GetPromotions extends AsyncTask<String, Void, String> {

        DatabaseOperations databaseOperations;

        private static final String Booking_information = "jsonString";
        private static final String HASHKEY_VALUE = "4321orue";

        private SweetAlertDialog mDialog;


        public GetPromotions() {

        }

        String token = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                databaseOperations = new DatabaseOperations(new DatabaseHelper(Fragment_Main.this));
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
                        FBToast.errorToast(Fragment_Main.this, parentObject.getString("Message"), FBToast.LENGTH_SHORT);
                    } else {
                        JSONArray jsonArray = new JSONArray(parentObject.getString("Data"));
                        sp.edit().putString("isPromoLoaded", "1");
                        databaseOperations.deletePromo();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject promoObj = jsonArray.getJSONObject(i);
                            if (promoObj != null && !promoObj.equals("") && !promoObj.equals("PromotionCode")) {
                                databaseOperations.insertPromoCode(
                                        promoObj.getString("PromotionCode"),
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
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
        }

        @Override
        protected String doInBackground(String... params) {


try {
    String deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


    HashMap<String, Object> appUserMap = new HashMap<>();
    String promotionJson = "{\"UniqueId\":\"" + deviceid + "\"," +
            "\"hashKey\":\"" + HASHKEY_VALUE + "\"," +
            "\"DeviceInfo\":\"Android\"," +
            "\"CustomerId\":\"" + userModel.getUserServerID() + "\"," +
            "\"defaultClientId\":\"" + CommonVariables.clientid + "\"," +
            "\"Email\":\"" + userModel.getEmail() + "\"}";
    appUserMap.put(Booking_information, promotionJson);
    appUserMap.put("Token", CommonVariables.TOKEN);

    Gson gson = new Gson();
    String jsonrequest = gson.toJson(appUserMap);

    OkHttpClient client = new OkHttpClient();
    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonrequest);
    Request request = new Request.Builder()
            .url(CommonVariables.BASE_URL + "GetPromotion")
            .post(body)
            .build();

    try {
        Response response = client.newCall(request).execute();
        return response.body().string();


    } catch (Exception e) {
        e.printStackTrace();
        return e.getMessage();
    }
}catch (Exception ex){
    ex.printStackTrace();
    return  "";
}

//            End Here Rest Api Work

        }

    }

    int ratings = 2;
    String feedbacktxt = "";
    String tipFares = "";
    boolean isJourneyDetailsRunning = false;

    private class getDriverJourneyDetails extends AsyncTask<String, Void, String> {
        private String METHOD_NAME = "GetDriverJourneyDetails";

        private String METHOD_NAME_ALL = "SaveCustomerReviews";

        private static final String KEY_DEFAULT_CLIENT_ID = "defaultclientId";
        private static final String KEY_HASHKEY = "hashKey";

        private static final String Booking_information = "jsonString";
        private static final String HASHKEY_VALUE = "4321orue";

        private SweetAlertDialog mDialog;
        Model_BookingDetailsModel mBookingDetails;


        public getDriverJourneyDetails(Model_BookingDetailsModel modelBookingDetailsModel) {
            mBookingDetails = modelBookingDetailsModel;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                isJourneyDetailsRunning = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            isJourneyDetailsRunning = false;
            if (result != null && !result.isEmpty()) {
                if (result != null && !result.isEmpty()) {

                    if (mHelper == null) {
                        mHelper = new SharedPrefrenceHelper(Fragment_Main.this);
                    }
                    try {
                        JSONObject parentObject = new JSONObject(result);
                        if (parentObject.getBoolean("HasError")) {
                        } else {
                            JSONObject jsonObject = new JSONObject(parentObject.getString("Data"));

                            String DrvName = jsonObject.optString("DriverName");
                            String VehDetails = jsonObject.optString("VehicleDetails");
                            String DrvImage = jsonObject.optString("DriverImage").replace("\",", "");

                            if (!DrvName.equals("") && !DrvName.equals("null")) {
                                mHelper.putVal("driverName_" + mBookingDetails.getRefrenceNo(), DrvName);
                            }
                            if (!VehDetails.equals("") && !VehDetails.equals("null")) {
                                mHelper.putVal("vehDetails_" + mBookingDetails.getRefrenceNo(), VehDetails);
                            }
                            if (!DrvImage.equals("") && !DrvImage.equals("null")) {
                                mHelper.putVal("driverImage_" + mBookingDetails.getRefrenceNo(), DrvImage);
                            }
                            if (ratingDialogue == null) {
//                                ratingDialogue = showRatingsDialogue(mBookingDetails);
                                ratingDialogue = ratingDialogDemo(mBookingDetails);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }


        }

        @Override
        protected String doInBackground(String... params) {

            mHelper = new SharedPrefrenceHelper(Fragment_Main.this);
            SettingsModel model = mHelper.getSettingModel();
            String deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            ShareTracking obj = new ShareTracking();
            obj.defaultClientId = (int) CommonVariables.clientid;
            obj.uniqueValue = CommonVariables.clientid + HASHKEY_VALUE;
            obj.UniqueId = deviceid;
            obj.SubCompanyId = CommonVariables.SUB_COMPANY;
            obj.DeviceInfo = "Android";
            obj.JobId = mBookingDetails.getRefrenceNo();





            final String json_String = new Gson().toJson(obj);
            SoapHelper.Builder builder = new SoapHelper.Builder(CommonVariables.SERVICE, Fragment_Main.this);
            builder.setMethodName("GetDriverJourneyDetails", true)
                    .addProperty(Booking_information, json_String, PropertyInfo.STRING_CLASS);

            try {
                return builder.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

                /* OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + "GetDriverJourneyDetails")
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (getSupportFragmentManager().findFragmentByTag("current") instanceof HomeFragment && hasFocus) {
//            HomeFragment homeFragment = getSupportFragmentManager().findFragmentByTag("current");
        }
    }


    private Listener_SubmitFeedback listenerSubmitFeedback;

    private void tipWork(Dialog dialog, Model_BookingDetailsModel mBookingDetails) {
        Context context = Fragment_Main.this;
        String currencySymbol = sp.getString(CommonVariables.CurrencySymbol, "\u00A3");
        String preText = "Tip total: " + currencySymbol + "";

        RelativeLayout tipRl = dialog.findViewById(R.id.tipRl);
        try {
            if (mBookingDetails.getPaymentType().toLowerCase().startsWith("credit card") && sp.getString(CommonVariables.EnableTip, "0").equals("1")) {
                tipRl.setVisibility(VISIBLE);
            } else {
                tipRl.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        clickTipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheme(context, tip1Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(context, tip2Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(context, tip3Tv, R.color.grey_background, R.color.color_white_inverse);

                clickTipTv.setVisibility(GONE);
                editableRl.setVisibility(VISIBLE);
                getTipTv.requestFocus();
                getTipTv.setFocusable(true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });


        tip1Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTheme(context, tip1Tv, R.color.color_inverse_black_footerBack, R.color.color_white_inverse);
                setTheme(context, tip2Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);
                setTheme(context, tip3Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);

                setTextRl.setVisibility(GONE);
                clickTipTv.setVisibility(VISIBLE);

                tipFares = "1.00";


            }
        });

        tip2Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTheme(context, tip2Tv, R.color.color_inverse_black_footerBack, R.color.color_white_inverse);
                setTheme(context, tip1Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);
                setTheme(context, tip3Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);

                setTextRl.setVisibility(GONE);
                clickTipTv.setVisibility(VISIBLE);

                tipFares = "2.00";

            }
        });

        tip3Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTheme(context, tip3Tv, R.color.color_inverse_black_footerBack, R.color.color_white_inverse);
                setTheme(context, tip2Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);
                setTheme(context, tip1Tv, R.color.color_gray_and_light_inverse, R.color.color_black_inverse);

                setTextRl.setVisibility(GONE);
                clickTipTv.setVisibility(VISIBLE);

                tipFares = "5.00";

            }
        });

        clearIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTipTv.setVisibility(VISIBLE);
                editableRl.setVisibility(GONE);
                setTextRl.setVisibility(GONE);

                setTipTv.setText(preText + "0.0");

                tipFares = "0.0";

                setTheme(context, tip1Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(context, tip2Tv, R.color.grey_background, R.color.color_white_inverse);
                setTheme(context, tip3Tv, R.color.grey_background, R.color.color_white_inverse);
            }
        });

        doneIv.setOnClickListener(new View.OnClickListener() {
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

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });
    }

    private void setDriverImage(String imageUrl, ImageView imageView) {
        try {
            Glide.with(getBaseContext())
                    .asBitmap()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Dialog feedbackDialog;

    private Dialog ratingDialogDemo(Model_BookingDetailsModel mBookingDetails) {
        feedbackDialog = new Dialog(this, android.R.style.Widget_DeviceDefault);
        feedbackDialog.setContentView(R.layout.layout_feedback);
        Window window = feedbackDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        feedbackDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView driveNameTv = feedbackDialog.findViewById(R.id.driveNameTv);
        TextView driverRatingTv = feedbackDialog.findViewById(R.id.driverRatingTv);
        driverRatingTv.setVisibility(GONE);
        TextView ratingHead = feedbackDialog.findViewById(R.id.ratingHead);
        TextView vehicleNameTv = feedbackDialog.findViewById(R.id.vehicleNameTv);
        TextView vehicleNoPlateTv = feedbackDialog.findViewById(R.id.vehicleNoPlateTv);
        RatingBar simpleRatingBar = feedbackDialog.findViewById(R.id.simpleRatingBar);
        simpleRatingBar.setVisibility(GONE);
        RatingBar mainRatingBar = feedbackDialog.findViewById(R.id.mainRatingBar);
        ImageView driverImageIv = feedbackDialog.findViewById(R.id.driverImageIv);
        ImageView backIv = feedbackDialog.findViewById(R.id.backIv);
        TextView rateYourTrip = feedbackDialog.findViewById(R.id.rateYourTrip);
        rateYourTrip.setText(p.getRateYourTrip());
        CardView doneCv = feedbackDialog.findViewById(R.id.doneCv);
        TextView doneTv = feedbackDialog.findViewById(R.id.doneTv);
        doneTv.setText(p.getDone());

        EditText feedbackedit = (EditText) feedbackDialog.findViewById(R.id.feedbackedit);
        feedbackedit.setHint(p.getWriteYourFeedback());

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackDialog.dismiss();
            }
        });

        try {
            String dr_imgUrl = mHelper.getVal("driverImage_" + mBookingDetails.getRefrenceNo());
            driverInformation.setDriverImage(dr_imgUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String dr_name = mHelper.getVal("driverName_" + mBookingDetails.getRefrenceNo());
            driverInformation.setDriverName(dr_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String vehDetails = mHelper.getVal("vehDetails_" + mBookingDetails.getRefrenceNo());
            driverInformation.setVehicleDetails(vehDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
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
        String vD = "";
        try {
            vD = driverInformation.getVehicleDetails();
            if (vD.contains("|")) {
                String vehicleDetail[] = vD.split("\\|");
                if (vehicleDetail.length == 3) {
                    if (vehicleDetail[1].contains("-")) {
                        String[] dd = vehicleDetail[1].split("-");
                        driverInformation.setVehicleNo(dd[0]);
                    } else {
                        driverInformation.setVehicleNo(vehicleDetail[1]);
                    }
                    vehicleNameTv.setText(vehicleDetail[2] + " | " + vehicleDetail[0]);
                } else
                    vehicleNameTv.setText(driverInformation.getVehicleColor() + " | " + vehicleDetail[0]);
            } else {
                vehicleNameTv.setText(driverInformation.getVehicleColor() + " | " + vD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String dddvD[] = vD.split("\\|");
            vehicleNoPlateTv.setText("" + dddvD[1]);
        } catch (Exception e) {
            e.printStackTrace();
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

        doneCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mainRatingBar.getRating() <= 0) {
                    FBToast.warningToast(Fragment_Main.this, "Please rate your driver.", FBToast.LENGTH_SHORT);
                    return;
                }

                try {
                    feedbacktxt = feedbackedit.getText().toString();
                    int rate = ratings;
                    simpleRatingBar.setNumStars(rate);
                    simpleRatingBar.setRating(rate);
                    simpleRatingBar.setVisibility(VISIBLE);
                    ratingHead.setVisibility(VISIBLE);

                    feedbackInformation = getFeedbackInformation(feedbacktxt, ratings, true, mBookingDetails);
                    if (sp.getString(CommonVariables.EnableTip, "0").equals("1")) {
                        if (tipFares.equals("")) {
                            feedbackInformation.CardDetail = null;
                            feedbackInformation.Tip = 0;
                            new Manager_SubmitFeedback(Fragment_Main.this, feedbackInformation, listenerSubmitFeedback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            // open payment screen
                            startActivityForResult(new Intent(Fragment_Main.this, Activity_HomePayment.class)
                                    .putExtra("ForTip", "JobDetail")
                                    .putExtra("IsHold", "false")
                                    .putExtra("Amount", Double.parseDouble(tipFares)), 4444);
                        }
                    } else {
                        feedbackInformation.CardDetail = null;
                        feedbackInformation.Tip = 0;
                        new Manager_SubmitFeedback(Fragment_Main.this, feedbackInformation, listenerSubmitFeedback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    feedbackDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        CardView skipCv = feedbackDialog.findViewById(R.id.skipCv);
        TextView skipTv = feedbackDialog.findViewById(R.id.skipTv);
        skipTv.setText(p.getSkip());
        skipCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackDialog.dismiss();
                if (mHelper == null) {
                    mHelper = new SharedPrefrenceHelper(getApplicationContext());
                }
                mHelper.putVal("isfeedback_" + mBookingDetails.getRefrenceNo(), "1");
                ratingDialogue = null;
            }
        });

        tipWork(feedbackDialog, mBookingDetails);



        mainRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratings = (int) rating;
            }
        });

        feedbackDialog.show();

        return feedbackDialog;
    }

    public static float roundfloat(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private FeedbackInformation getFeedbackInformation(String feedBack, int ratings, boolean IsFeedback, Model_BookingDetailsModel mBookingDetails) {
        feedbackInformation = new FeedbackInformation();
        feedbackInformation.BookingReference = mBookingDetails.getRefrenceNo();
        feedbackInformation.ClientName = userModel.getName();
        feedbackInformation.Email = userModel.getEmail();
        feedbackInformation.Location = mBookingDetails.gettoAddress();
        feedbackInformation.Message = feedBack;
        feedbackInformation.Rating = ratings;
        feedbackInformation.IsFeedback = IsFeedback;
        feedbackInformation.Title = userModel.getName() + "_Review From Customer App";
        return feedbackInformation;
    }

    public void setTheme(Context context, TextView tv, int bgColor, int txtColor) {
        if (android.os.Build.VERSION.SDK_INT > 23) {
            // Do something for lollipop and above versions
            tv.setBackgroundTintList(ContextCompat.getColorStateList(context, bgColor));
            tv.setTextColor(ContextCompat.getColor(context, (txtColor)));
        } else {
            // do something for phones running an SDK before lollipop
            tv.setBackgroundTintList(getResources().getColorStateList(bgColor));
            tv.setTextColor(getResources().getColor((txtColor)));
        }
    }

    public void removeAllSharedPreferencesDataBeforeLogout() {
        try {
            userId = userModel.getLoginID();
            if (!userId.equals("")) {
                String[] userIDarr = userId.split("-");
                if (userIDarr[0].toLowerCase().equals("fb")) {
                } else if (userIDarr[0].toLowerCase().equals("g")) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor edit = sp.edit();
        try {
            unBindService();
        } catch (Exception e) {
            e.printStackTrace();
        }

        edit.putBoolean(CommonVariables.ISUSERLOGIN, false);
        edit.putBoolean("isGoogle", false);
        edit.putString(CommonVariables.paymentType, "");
        edit.commit();

        userModel.setLoginID("");
        userModel.setName("");
        userModel.setEmail("");
        userModel.setPhoneNo("");

        sp.edit().putString(Config.FCMTOKEN + "_" + userModel.getUserServerID(), "").commit();

        SharedPrefrenceHelper helper = new SharedPrefrenceHelper(Fragment_Main.this);
        helper.putSettingModel(userModel);
        helper.removeLastReciept();

        // remove for stripe from shared prefs
        helper.removeStripeCustomerId();
        helper.deletedKonnectCard();
        helper.removeToSharePrefForStripeForOneCard();
        try {
            mDatabaseOperations.DeleteAllBooking();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!sp.getBoolean(CommonVariables.ISUSERLOGIN, false)) {

            stopService(new Intent(Fragment_Main.this, Service_NotifyStatus.class));
            cancelNotification(getBaseContext());
            startActivity(new Intent(Fragment_Main.this, Activity_Start.class));
            finish();
        }
    }

}
