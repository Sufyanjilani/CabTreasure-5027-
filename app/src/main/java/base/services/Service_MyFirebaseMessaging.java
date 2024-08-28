package base.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.eurosoft.customerapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import base.fragments.Fragment_AllBooking;
import base.fragments.Fragment_Main;
import base.fragments.Fragment_Tracking;
import base.models.SettingsModel;
import base.models.ShareTracking;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Service_MyFirebaseMessaging extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> map = remoteMessage.getData();
            String title = map.get("Title");
            String description = map.get("Message");
            if (Fragment_AllBooking.isVisiable) {
                try {

                    notifyFragment("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sendNotification(title, description);

        }

        else if (remoteMessage.getNotification() != null) {
            if (Fragment_AllBooking.isVisiable) {
                notifyFragment("");
            }

            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

        }


    }

    private void notifyFragment(String data){
        // foreground service setting
        try {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Service_MyFirebaseMessaging.this);
        if(sp.getString(CommonVariables.Enable_ForeGround_Service,"0").equals("0")) {

                Intent intent = new Intent(CommonVariables.refresh_booking_action);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }

            } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onNewToken(String token) {

        sendRegistrationToServer(token);
    }

    private void scheduleJob() {

    }


    private void handleNow() {

    }


    private void sendRegistrationToServer(String token) {
        new UpdateFCMTokenToServer().execute(token);
    }

    private class UpdateFCMTokenToServer extends AsyncTask<String, Void, String> {
        private String METHOD_NAME = "UpdateAppUserNotification";


        private static final String KEY_DEFAULT_CLIENT_ID = "defaultclientId";
        private static final String KEY_HASHKEY = "hashKey";

        private static final String Booking_information = "jsonString";
        private static final String HASHKEY_VALUE = "4321orue";

        private SweetAlertDialog mDialog;


        public UpdateFCMTokenToServer() {

        }

        String token = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
//                mDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
//                mDialog.setTitleText("Sending Receipt");
//                mDialog.setContentText("Please wait..");
//                mDialog.setCancelable(false);
//                mDialog.show();
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
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Service_MyFirebaseMessaging.this);
                        sp.edit().putString(Config.FCMTOKEN, token).commit();
                    }
                } catch (Exception e) {

                }

            }
        }

        @Override
        protected String doInBackground(String... params) {

            SettingsModel userModel = new SharedPrefrenceHelper(Service_MyFirebaseMessaging.this).getSettingModel();


            try {

                String deviceid = Settings.Secure.getString(Service_MyFirebaseMessaging.this.getContentResolver(),
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

                String strObj = new Gson().toJson(obj);

                JSONObject data = new JSONObject(strObj);

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

           /* final String json_String = new Gson().toJson(obj);
            SoapHelper.Builder builder = new SoapHelper.Builder(CommonVariables.SERVICE, Fragment_Main.this);
            builder
                    .setMethodName("UpdateAppUserNotification", true)
                    .addProperty(Booking_information, json_String, PropertyInfo.STRING_CLASS);


            try {
                return builder.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }*/

                //return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }

    }

    private void sendNotification(String title, String messageBody) {
        if (title == null || title.equals("") || title.equals("null")) {
            title = getResources().getString(R.string.app_name);
        }

        Intent intent = new Intent(this, Fragment_Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        intent   .putExtra("promofrag", true);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_ONE_SHOT);
//        Uri customSoundUri = Uri.parse("android.resource://"
//                + getPackageName() + "/"
//                + R.raw.messagetune);

        Uri customSoundUri = Uri.parse("android.resource://"
                + getPackageName() + "/"
                + R.raw.messagetune);

        Intent mIntent = new Intent(this, Fragment_Main.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("bookingfrag", true);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, mIntent, (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? PendingIntent.FLAG_IMMUTABLE : 0);


        String channelId = "110012";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_notification)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setAutoCancel(true)
                        .setSound(customSoundUri)
                        .setContentIntent(pIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getResources().getColor(R.color.color_inverse_black_footerBack));
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    title,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(customSoundUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1104 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        startService(new Intent(this, Service_MyFirebaseMessaging.class));
    }
}
