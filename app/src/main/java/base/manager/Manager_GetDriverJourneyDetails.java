package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import base.listener.Listener_GetDriverJourneyDetailsManager;
import base.models.ParentPojo;
import base.models.ShareTracking;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetDriverJourneyDetails extends AsyncTask<String, Void, String> {
    private SweetAlertDialog mDialog;
    private Context context;
    private ParentPojo p;
    private ShareTracking shareTracking;
    private Listener_GetDriverJourneyDetailsManager driverJourneyDetailsManagerListener;
    String from;

    public Manager_GetDriverJourneyDetails(Listener_GetDriverJourneyDetailsManager driverJourneyDetailsManagerListener, Context context, ParentPojo p, ShareTracking shareTracking) {
        this.driverJourneyDetailsManagerListener = driverJourneyDetailsManagerListener;
        this.context = context;
        this.p = p;
        this.shareTracking = shareTracking;
    }

    public Manager_GetDriverJourneyDetails(String from, Listener_GetDriverJourneyDetailsManager driverJourneyDetailsManagerListener, Context context, ParentPojo p, ShareTracking shareTracking) {
        this.from = from;
        this.driverJourneyDetailsManagerListener = driverJourneyDetailsManagerListener;
        this.context = context;
        this.p = p;
        this.shareTracking = shareTracking;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (from.equals("jobdetail")) {
            return;
        }
        try {
            mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
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
        if (driverJourneyDetailsManagerListener != null) {
            driverJourneyDetailsManagerListener.onComplete(result);
        }
        if (from.equals("jobdetail")) {
            return;
        }

        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    protected String doInBackground(String... params) {
  /*      SoapHelper.Builder builder = new SoapHelper.Builder(CommonVariables.SERVICE, context);
        builder
                .setMethodName("GetDriverJourneyDetails", true)
                .addProperty("jsonString", "json_String", PropertyInfo.STRING_CLASS);
*/
        HashMap<String, Object> map = new HashMap<>();
//        map.put("Token", CommonVariables.TOKEN);
        String token = AppConstants.getAppConstants().getToken();
        map.put("Token", token);
        map.put("uniqueValue", CommonVariables.clientid + "4321orue");
        map.put("defaultclientId", "" + CommonVariables.clientid);
        map.put("jsonString", shareTracking);
        String jsonString = new Gson().toJson(map);
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
    }
}
