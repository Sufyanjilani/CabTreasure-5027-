package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import base.listener.Listener_GetAirportDetails;
import base.models.ParentPojo;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetAirportDetails extends AsyncTask<Void, Void, String> {
    private Context context;
    private String text;
    private Listener_GetAirportDetails listener;
    private SweetAlertDialog mDialog;
    private ParentPojo p;

    public Manager_GetAirportDetails(Context context, String text, ParentPojo p, Listener_GetAirportDetails listener) {
        this.context = context;
        this.text = text;
        this.p = p;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.setTitleText(p.getGettingAirportDetails());
            mDialog.setContentText(p.getPleaseWait());
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
           /* return new SoapHelper.Builder(CommonVariables.SERVICE, context)
                    .setMethodName("GetAirportDetails", true)
                    .addProperty("defaultClientId", CommonVariables.clientid, PropertyInfo.STRING_CLASS)
                    .addProperty("locationName", text, PropertyInfo.STRING_CLASS)
                    .addProperty("locationType", "Airport", PropertyInfo.STRING_CLASS)
                    .addProperty("fetchString", CommonVariables.Clientip, PropertyInfo.STRING_CLASS)
                    .addProperty("hashKey", CommonVariables.clientid + "4321orue", PropertyInfo.STRING_CLASS).getResponse();
*/
            HashMap<String, Object> parentmap = new HashMap<>();
            parentmap.put("defaultClientId", CommonVariables.clientid);
            parentmap.put("uniqueValue", CommonVariables.clientid + "4321orue");
            parentmap.put("locationName", text);
            parentmap.put("locationType", "Airport");
            parentmap.put("fetchString", CommonVariables.Clientip);
            parentmap.put("hashKey", CommonVariables.clientid + "4321orue");


            String jsonString = new Gson().toJson(parentmap);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + "GetAirportDetails")
                    .post(body)
                    .build();

            Response mresponse = client.newCall(request).execute();
           return mresponse.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (mDialog != null) {
            mDialog.dismiss();
        }

        if (listener != null) {
            listener.onCompleteAirportDetails(result);
        }
    }// End onPostExecute()
}// End class PrefetchData
