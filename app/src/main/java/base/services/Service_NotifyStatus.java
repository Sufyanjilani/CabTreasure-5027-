package base.services;

import static base.utils.CommonMethods.checkIfHasNullForString;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.eurosoft.customerapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import base.activities.Activity_Login;
import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.fragments.Fragment_Main;
import base.listener.Listener_BookingCompleted;
import base.listener.Listener_UpdateBookingList;
import base.listener.MyListener;
import base.models.ChatModel;
import base.models.Model_BookingDetailsModel;
import base.newui.HomeFragment;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import base.utils.SharedPrefrenceHelper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Service_NotifyStatus extends Service {

    private static final String TAG = "Service_NotifyStatus";
    private static String METHOD_NAME = "GetJobStatusWithPrice";
    public static boolean isTrackingLive = false;
    private String status;
    private String refNo;
    private boolean IsStatusChanged = false;
    private ArrayList<String> listmessang;
    private SharedPrefrenceHelper mHelper;
    private DatabaseOperations mDatabaseOperations;
    private MediaPlayer mplay;
    private Listener_BookingCompleted listenerBookingCompleted;
    private Listener_UpdateBookingList listener_updateBookingList;
    private Context context;

    private IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public Service_NotifyStatus getService() {
            // Return this instance of SignalRService so clients can call public methods
            return Service_NotifyStatus.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
//        bindService()
        return mBinder;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 123) {
                if (listenerBookingCompleted != null) {
                    listenerBookingCompleted.bookingCompleted((Model_BookingDetailsModel) msg.obj);
                }
            }
        }
    };

    Runnable m_runRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isTrackingLive) {
                try {
                    ArrayList<Model_BookingDetailsModel> bookingList = mDatabaseOperations.getActiveBookings();
                    if (mDatabaseOperations.getActiveBookingsCount() <= 0) {
                        if (listenerBookingCompleted != null) {
                            listenerBookingCompleted.bookingCompleted(null);
                        }
                    } else {
                        updateStatusNoDialogRest(bookingList);
                    }
                } catch (Exception e) {
                }
            }

            handler.postDelayed(m_runRunnable, 10000);
        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            where = intent.getStringExtra("where");
//            if (where.contains("chat_screen")) {
//                submitSignal(Config.READ);
//            }
        }
    };

    private String where = "";

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mplay = MediaPlayer.create(Service_NotifyStatus.this, R.raw.messagetune);
        mDatabaseOperations = new DatabaseOperations(new DatabaseHelper(Service_NotifyStatus.this));
        registerReceiver(mMessageReceiver, new IntentFilter("abc"));

        handler.post(m_runRunnable);
        if (mHelper == null) {
            mHelper = new SharedPrefrenceHelper(Service_NotifyStatus.this);
        }
        mHelper.putVal(HomeFragment.isServiceRunningInBackground, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(702, buildForegroundNotification());
        }

        return START_STICKY;

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {

        if (!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(CommonVariables.ISUSERLOGIN, false)
                &&
                !PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(CommonVariables.enableSignup, "1").equals("0")
        ) {
            getApplicationContext().stopService(new Intent(getApplicationContext(), Service_NotifyStatus.class));
            Fragment_Main.cancelNotification(getApplicationContext());
        }

        String NOTIFICATION_CHANNEL_ID = getPackageName();
        String channelName = getResources().getString(R.string.app_name);
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        Intent mIntent = new Intent(Service_NotifyStatus.this, Fragment_Main.class)
//                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("bookingfrag", true);

        PendingIntent pIntent = PendingIntent.getActivity(Service_NotifyStatus.this, 0, mIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_notification)
                .setContentTitle(channelName)
                .setContentIntent(pIntent)
                .setContentText("Tap here to see your active bookings!")
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(221, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper == null) {
            mHelper = new SharedPrefrenceHelper(Service_NotifyStatus.this);
        }
        mHelper.putVal(HomeFragment.isServiceRunningInBackground, false);

        if (handler != null) {
            handler.removeCallbacks(m_runRunnable);
        }
        try {
            if (mMessageReceiver != null) {
                unregisterReceiver(mMessageReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onDestroy: ");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void NotificationBuilder(Model_BookingDetailsModel modelBookingDetailsModel) {
        try {

            try {
                refNo = modelBookingDetailsModel.getRefrenceNo();
            }catch (Exception ex){

            }

            if (!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(CommonVariables.ISUSERLOGIN, false)) {
                getApplicationContext().stopService(new Intent(getApplicationContext(), Service_NotifyStatus.class));
                Fragment_Main.cancelNotification(getApplicationContext());
                startActivity(new Intent(getApplicationContext(), Activity_Login.class));
                return;
            }

            Intent mIntent = new Intent(Service_NotifyStatus.this, Fragment_Main.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("bookingfrag", true);

            PendingIntent pIntent = PendingIntent.getActivity(Service_NotifyStatus.this, 0, mIntent, (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? PendingIntent.FLAG_IMMUTABLE : 0);
          Notification.Builder mNotification = new Notification.Builder(Service_NotifyStatus.this);
            mNotification.setContentTitle(getResources().getString(R.string.app_name));
            if (status.toLowerCase().equals("confirmed")) {
                mplay.start();
                mNotification.setContentText("Your booking is confirmed [" + refNo + "]");
            } else if (status.toLowerCase().equals("cancelled")) {
             /*   try {
                    if (pubnub != null) {
                        pubnub.unsubscribeAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                mplay.start();
                mNotification.setContentText("Your booking is cancelled [" + refNo + "]");

            } else if (status.toLowerCase().equals("onroute")) {
                mplay.start();
                mNotification.setContentText("Driver is on the way [" + refNo + "]");
            } else if (status.toLowerCase().equals("arrived")) {
                mplay.start();
                mNotification.setContentText("Your driver has arrived [" + refNo + "]");
            } else if (status.toLowerCase().equals("completed")) {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mplay.start();
                mNotification.setContentText("Journey Completed, Thank you");
                Message message = new Message();
                message.what = 123;
                message.obj = modelBookingDetailsModel;
                handler.sendMessage(message);
            } else if (status.toLowerCase().equals("passengeronboard") || status.toLowerCase().equals("pob")) {
                mplay.start();
                mNotification.setContentText("Welcome on onboard");
            }

            mNotification.setSmallIcon(R.mipmap.ic_notification);
            mNotification.setContentIntent(pIntent);
            mNotification.setAutoCancel(true);

            if (status.toLowerCase().equals("confirmed") || status.toLowerCase().equals("cancelled") ||
                    status.toLowerCase().equals("onroute") || status.toLowerCase().equals("arrived") || status.toLowerCase().equals("passengeronboard") || status.toLowerCase().equals("pob") || status.toLowerCase().equals("completed")) {
               // NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 /*int importance = NotificationManager.IMPORTANCE_HIGH;
                   NotificationChannel notificationChannel = new NotificationChannel("11001", getResources().getString(R.string.app_name), importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(getResources().getColor(R.color.app_border));
                    notificationChannel.enableVibration(false);*/

                   // assert notificationManager != null;
                  //  mNotification.setChannelId("11001");
                  //  notificationManager.createNotificationChannel(notificationChannel);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                 //   mNotification.setColor(getResources().getColor(R.color.color_inverse_black_footerBack));
                }
//                Notification notification = mNotification.build();

//                notificationManager.notify(0, notification);
//                notificationManager.cancel(1);
                if (status.toLowerCase().equals("cancelled") || status.toLowerCase().equals("completed")) {
                    if (mDatabaseOperations.getActiveBookingsCount() <= 0) {
                        if (mHelper == null) {
                            mHelper = new SharedPrefrenceHelper(Service_NotifyStatus.this);
                        }
                        mHelper.putVal(HomeFragment.isServiceRunningInBackground, false);
                        if (listenerBookingCompleted != null) {
                            listenerBookingCompleted.bookingCompleted(null);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void updateStatusNoDialogRest(final ArrayList<Model_BookingDetailsModel> models) {
        try {
            Log.e("ACTIVITY", "Called");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        IsStatusChanged = false;
                        int i = 0;
                        ArrayList<String> refNoArr = new ArrayList<>();
                        refNoArr.clear();
                        String jsonString = "";
                        for (Model_BookingDetailsModel model : models) {
                            if (model.getStatus().toLowerCase().trim().equals("onroute")
                                    || model.getStatus().toLowerCase().trim().equals("arrived")
                                    || model.getStatus().toLowerCase().trim().equals("passengeronboard")
                                    || model.getStatus().toLowerCase().trim().equals("pob")
                                    || model.getStatus().toLowerCase().trim().equals("onroute")) {
                                /*if (getPubnub() == null) {
                                    initPubNub(model.getRefrenceNo());
                                }*/
                            }
                            refNoArr.add(model.getRefrenceNo());


                            i++;
                        }
                        HashMap<String, Object> map = new HashMap<>();
//                        map.put("Token", CommonVariables.TOKEN);
                        String token = AppConstants.getAppConstants().getToken();
                        map.put("Token", token);
                        map.put("defaultclientId", "" + CommonVariables.clientid);
                        map.put("refarray", refNoArr);

                        jsonString = new Gson().toJson(map);
                        callApiGetStatus(jsonString, models);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void callApiGetStatus(String jsonString, ArrayList<Model_BookingDetailsModel> models) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "GetJobStatusWithPriceNew")
                .post(body)
                .build();


        String response = "";
        try (Response _response = client.newCall(request).execute()) {
            response = _response.body().string();


            if (!response.equals("")) {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("HasError")) {

                } else {
                    JSONObject dataObject = jsonObject.optJSONObject("Data");
                    if (dataObject != null) {

                        JSONArray bookingStatus = dataObject.optJSONArray("BookingStatus");
                        if (bookingStatus != null) {
                            Collections.reverse(models);
                            for (int j = 0; j < bookingStatus.length(); j++) {
                                JSONObject o = bookingStatus.optJSONObject(j);
                                String BookingStatus = checkIfHasNullForString(o.optString("BookingStatus"));
                                String Fares = checkIfHasNullForString(o.optString("Fares"));
                                String DriverStatus = checkIfHasNullForString(o.optString("DriverStatus"));
                                String Listener = checkIfHasNullForString(o.optString("Listener"));
                                String refNo = checkIfHasNullForString(o.optString("refNo"));
                                parseStatusResponse(BookingStatus, Fares, DriverStatus, Listener, refNo, models.get(j), j, models);
                            }
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            response = "";
        }
    }

    private synchronized void parseStatusResponse(String BookingStatus, String Fares, String DriverStatus, String Listener, String refNo, Model_BookingDetailsModel model, int pos, ArrayList<Model_BookingDetailsModel> models) {
        // 0 bstatus,1 fare, 2 driver status , 3 listner
        String mStatus = BookingStatus;
        listmessang = new ArrayList<String>();
        listmessang.add(BookingStatus);
        listmessang.add(Fares);
        listmessang.add(DriverStatus);
        listmessang.add(Listener);
        Log.e("ACTIVITY", "Status is " + mStatus);

        if (!listmessang.get(2).equals("")) {
            String[] ParseResponse = listmessang.get(2).split(",");
            if (!ParseResponse[0].equals("")) {
                if (ParseResponse[0].toLowerCase().equals("declined")) {
                    ParseResponse[0] = "Cancelled";

                } else if (ParseResponse[0].toLowerCase().equals("onroute")) {
                    ParseResponse[0] = "ONROUTE";


                } else if (ParseResponse[0].toLowerCase().equals("arrived")) {
                    ParseResponse[0] = "ARRIVED";

                } else if (ParseResponse[0].toLowerCase().equals("passengeronboard") || ParseResponse[0].toLowerCase().equals("pob")) {
                    ParseResponse[0] = "POB";

                } else if (ParseResponse[0].toLowerCase().equals("soontoclear")) {
                    ParseResponse[0] = "STC";

                } else if (ParseResponse[0].toLowerCase().equals("completed")) {
                    ParseResponse[0] = "COMPLETED";
                    if (mHelper == null) {
                        mHelper = new SharedPrefrenceHelper(Service_NotifyStatus.this);
                    }
                } else if (ParseResponse[0].toLowerCase().equals("available")) {
                    if (mStatus.toLowerCase().equals("confirmed")) {
                        ParseResponse[0] = mStatus;
                    } else if (mStatus.toLowerCase().equals("cancelled")) {
                        ParseResponse[0] = mStatus;
                    } else {
                        ParseResponse[0] = ParseResponse[0].toUpperCase();
                        ParseResponse[0] = "COMPLETED";
                        if (mHelper == null) {
                            mHelper = new SharedPrefrenceHelper(Service_NotifyStatus.this);
                        }
                    }
                }
                if (model.getStatus().equalsIgnoreCase(CommonVariables.STATUS_POB2)) {
                    model.setStatus("onboard");
                }
                if (model.getStatus().toLowerCase().trim().equals(ParseResponse[0].toLowerCase().trim()) == false) {
                    IsStatusChanged = true;
                    status = ParseResponse[0].toLowerCase();
                  //  refNo = model.getRefrenceNo();
                    if (ParseResponse[0].toLowerCase().trim().equals("onroute")) {
                                                   /* if (getPubnub() == null) {
                                                        initPubNub(refNo);
                                                    }*/
                    }
                    NotificationBuilder(model);
                    ////////////call refresh
                    callRefresh(model, models, ParseResponse[0], pos);
                }
                if (status.equalsIgnoreCase("completed")) {
                    mDatabaseOperations.UpdateBookingStatusAndFares(listmessang.get(1), ParseResponse[0], refNo);
                } else {
                    mDatabaseOperations.UpdateBookingStatus(ParseResponse[0], refNo);
                }
            }
        } else {

            if (model.getStatus().toLowerCase().equals(mStatus.toLowerCase()) == false) {

                IsStatusChanged = true;
                status = mStatus.toLowerCase();
                refNo = model.getRefrenceNo();
                NotificationBuilder(model);
                callRefresh(model, models, status, pos);
            }
            mDatabaseOperations.UpdateBookingStatus(listmessang.get(0), refNo);
        }


    }

    private void callRefresh(Model_BookingDetailsModel model, ArrayList<Model_BookingDetailsModel> models, String status, int pos) {
        if (model != null) {
            model.setStatus(status);
            if (model.getStatus().toLowerCase().trim().equals("completed") || model.getStatus().toLowerCase().trim().equals("cancelled")) {
                if (models.size() == 1) {
                    stopForeground(true);
                    stopSelf();
                }
                models.remove(pos);
            } else {
                models.set(pos, model);
            }

            if (listener_updateBookingList != null) {
                listener_updateBookingList.updateList(models);
            }
        }
    }

    private Notification buildForegroundNotification() {
        if (!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(CommonVariables.ISUSERLOGIN, false) &&
                !PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(CommonVariables.enableSignup, "1").equals("0")) {
            getApplicationContext().stopService(new Intent(getApplicationContext(), Service_NotifyStatus.class));
            Fragment_Main.cancelNotification(getApplicationContext());
        }


        Intent mIntent = new Intent(Service_NotifyStatus.this, Fragment_Main.class)
//                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("bookingfrag", true);

        PendingIntent pIntent = PendingIntent.getActivity(Service_NotifyStatus.this, 0, mIntent, (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? PendingIntent.FLAG_IMMUTABLE : 0);
        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setOngoing(true)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Tap here to see your active bookings!")
                .setContentIntent(pIntent)
                .setSmallIcon(R.mipmap.ic_notification);

        //.setTicker(getString(R.string.downloading));

        return (b.build());
    }

    public void setOnCompleteBookingListener(Listener_BookingCompleted listenerBookingCompleted) {
        this.listenerBookingCompleted = listenerBookingCompleted;
    }

    public void setOnUpdateBookingListener(Listener_UpdateBookingList listener) {
        this.listener_updateBookingList = listener;
    }

    private int msgCounter = 0;

    public void setMsgCounter(int msgCounter) {
        this.msgCounter = msgCounter;
    }

    public int getMsgCounter() {
        return msgCounter;
    }

/*
    private PubNub pubnub;

    public PubNub getPubnub() {
        return pubnub;
    }
*/

    private String clientUUID = "";
    private String theChannel = "the_guide";
    private String theEntry = "";
    private String who = "";

/*
    public void initPubNub(String channelId) {
        who = mHelper.getSettingModel().getName().trim();
        clientUUID = who;
        theEntry = who;

        theChannel = channelId;
        Log.d(TAG, "initPubNub: theChannel = " + theChannel);

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("pub-c-dd311b4d-27e0-4bdb-996c-162f28c35df1");
        pnConfiguration.setSubscribeKey("sub-c-ab8885ac-fc3c-11eb-b38c-d287984b4121");
        pnConfiguration.setUuid(clientUUID);

        pubnub = new PubNub(pnConfiguration);

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void message(PubNub pubnub, PNMessageResult event) {
                JsonObject message = event.getMessage().getAsJsonObject();
                Log.d(TAG, "TEST -- message: Service : " + message.toString());
                String from = message.get("from").getAsString();
                String msg = message.get("update").getAsString();
                sendMessageToActivity(from + ":" + msg, Config.ACTION_NEW_MESSAGE);

                try {
                    if (where.equals("")) {
                        NotificationBuilder(from + ": " + msg);
                        setMsgCounter(getMsgCounter() + 1);
                        submitSignal(Config.RECEIVED);
                    } else {
                        submitSignal(Config.READ);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void status(PubNub pubnub, PNStatus event) {
                if (event.getCategory().equals(PNStatusCategory.PNConnectedCategory)) {
                    Log.d(TAG, "TEST -- status:  Connected : ");
                } else if (event.getCategory().equals(PNStatusCategory.PNUnexpectedDisconnectCategory)) {
                    pubnub.reconnect();
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult event) {
                Log.d(TAG, "TEST -- presence: " + event.toString());
//                displayMessage("[PRESENCE: " + event.getEvent() + ']', "uuid: " + event.getUuid() + ", channel: " + event.getChannel());
            }

            // even if you don't need these callbacks, you still have include them
            // because we are extending an Abstract class
            @Override
            public void signal(PubNub pubnub, PNSignalResult event) {
                Log.d(TAG, "TEST -- signal: " + event.toString());

                if (event.getPublisher() != null && event.getPublisher().equals(who)) {
                    return;
                }
                if (event.getMessage().toString().trim().contains(Config.RECEIVED)) {
                    sendMessageToActivity("", Config.ACTION_RECEIVED);
                } else if (event.getMessage().toString().trim().contains(Config.READ)) {
                    sendMessageToActivity("", Config.ACTION_READ);
                } else if (event.getMessage().toString().trim().contains(Config.TYPING_START)) {
                    sendMessageToActivity("", Config.ACTION_START_TYPE);
                } else if (event.getMessage().toString().trim().contains(Config.TYPING_STOP)) {
                    sendMessageToActivity("", Config.ACTION_STOP_TYPE);
                }
            }

            @Override
            public void uuid(PubNub pubnub, PNUUIDMetadataResult pnUUIDMetadataResult) {
                Log.d(TAG, "TEST -- uuid: ");
            }

            @Override
            public void channel(PubNub pubnub, PNChannelMetadataResult pnChannelMetadataResult) {
                Log.d(TAG, "TEST -- channel: ");

            }

            @Override
            public void membership(PubNub pubnub, PNMembershipResult event) {
                Log.d(TAG, "TEST -- membership: ");
            }

            @Override
            public void messageAction(PubNub pubnub, PNMessageActionResult event) {
                Log.d(TAG, "TEST -- messageAction: ");
            }

            @Override
            public void file(PubNub pubnub, PNFileEventResult event) {
                Log.d(TAG, "TEST -- file: ");
            }
        });

        pubnub.subscribe()
                .channels(Arrays.asList(channelId))
                .withPresence()
                .execute();
    }
*/

    public void submitUpdate(String anEntry, String anUpdate) {
        JsonObject entryUpdate = new JsonObject();
        entryUpdate.addProperty("from", anEntry);
        entryUpdate.addProperty("update", anUpdate);

        Log.d(TAG, "submitUpdate: theChannel= " + theChannel);

        /*pubnub.publish()
                .channel(theChannel)
                .message(entryUpdate)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        Log.d(TAG, "onResponse:  " + status.toString());
                        if (status.getStatusCode() == 200) {
                            sendMessageToActivity(anEntry + ">>>" + anUpdate, Config.ACTION_MESSAGE_SENT);
                        }
                    }
                });*/
    }

    private void sendMessageToActivity(String text, String Action) {

        Intent intent = new Intent(Action);
        // You can also include some extra data.
        intent.putExtra("text", text);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void sendStatus(String status) {
       /* pubnub.signal()
                .message("1")
                .channel(theChannel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult pnPublishResult, PNStatus pnStatus) {
                        if (!pnStatus.isError()) {
                            Long timetoken = pnPublishResult.getTimetoken(); // signal message timetoken
                        } else {
                            pnStatus.getErrorData().getThrowable().printStackTrace();
                        }
                    }
                });*/
    }

    public void submitSignal(String status) {

//        1 = received , 2 = read , 3 = startTyping , 4 = stop typing
        Log.d(TAG, "submitSignal:  theChannel = " + theChannel);
      /*  pubnub.signal()
                .message(status)
                .channel(theChannel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult pnPublishResult, PNStatus pnStatus) {
                        if (!pnStatus.isError()) {
                            Long timetoken = pnPublishResult.getTimetoken(); // signal message timetoken
                        } else {
                            pnStatus.getErrorData().getThrowable().printStackTrace();
                        }
                    }
                });*/
    }

    public ArrayList<ChatModel> getCurrentChat(ProgressBar progressBar, MyListener listener) {
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<ChatModel> arrayList = new ArrayList<>();
      /*  pubnub.fetchMessages()
                .channels(Arrays.asList(theChannel))
                .end(Calendar.getInstance().getTimeInMillis())
                .maximumPerChannel(100)
                .async(new PNCallback<PNFetchMessagesResult>() {
                    @Override
                    public void onResponse(PNFetchMessagesResult result, PNStatus status) {
                        if (!status.isError()) {
                            try {
                                Map<String, List<PNFetchMessageItem>> channels = result.getChannels();
                                for (PNFetchMessageItem messageItem : channels.get(theChannel)) {
                                    Log.e("", "Message ++ " + messageItem.getMessage());
                                    JsonObject jsonObject = messageItem.getMessage().getAsJsonObject();
                                    ChatModel chatModel;
                                    if (jsonObject.get("from").getAsString().contains(mHelper.getSettingModel().getName())) {
                                        chatModel = new ChatModel(
                                                jsonObject.get("from").getAsString(),
                                                jsonObject.get("update").getAsString(),
                                                getDateFromMilis(messageItem.getTimetoken(), "dd-MMM HH:mm"),
                                                1);
                                    } else {
                                        chatModel = new ChatModel(
                                                jsonObject.get("from").getAsString(),
                                                jsonObject.get("update").getAsString(),
                                                getDateFromMilis(messageItem.getTimetoken(), "dd-MMM HH:mm"),
                                                2);
                                    }
                                    arrayList.add(chatModel);
                                }
                                if (listener != null) {
                                    listener.chatModel(arrayList);
                                } else {
                                    listener.chatModel(new ArrayList<ChatModel>());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (listener != null) {
                                    listener.chatModel(new ArrayList<ChatModel>());
                                }
                            }
                        } else {
                            status.getErrorData().getThrowable().printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });*/

        return arrayList;
    }

    public static String getDateFromMilis(long milliSeconds, String dateFormat) {
        // Activity_Signup a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        // Activity_Signup a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


//    protected void displayMessage(String messageType, String aMessage) {
//        String newLine = "\n";
//
//        final StringBuilder textBuilder = new StringBuilder()
//                .append(messageType)
//                .append(newLine)
//                .append(aMessage)
//                .append(newLine).append(newLine);
//
//        Log.d(TAG, "displayMessage: " + textBuilder.toString());
////                .append(messagesText.getText().toString());
//
////        Log.d(TAG, "displayMessage: " + message);
////        runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////                messagesText.setText(textBuilder.toString());
////            }
////        });
//    }
}
