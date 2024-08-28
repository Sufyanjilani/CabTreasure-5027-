package base.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;

import java.util.HashMap;

import base.activities.Activity_SearchAddressNew;
import base.listener.Listener_GetMeetAndGreetMessage;
import base.models.Flight;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
// meet and greet
public class Manager_GetMeetAndGreetMessage extends AsyncTask<String, Void, String> {
    private SweetAlertDialog mDialog;
   private Listener_GetMeetAndGreetMessage listener;
    private Context context;
    String locationName;
    int position;
Flight flightDetails;

    public Manager_GetMeetAndGreetMessage(Context context, Flight flightDetails, Listener_GetMeetAndGreetMessage listener){
        this.context = context;
        this.flightDetails = flightDetails;
        this.listener = listener;
    }


    //        public string GetAirportFlightCharges(int defaultClientId, string flightNo, string fetchString, string hashKey)


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.setTitleText("Getting Airport Details");
            mDialog.setContentText("Please wait..");
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// End onPreExecute()

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener != null)
            listener.onComplete(result);
        if (mDialog != null)
            mDialog.dismiss();

   return;
    }

    @Override
    protected String doInBackground(String... params) {

        Gson gson = new Gson();
        HashMap<String, Object> appUserMap = new HashMap<>();
        appUserMap.put("Token", CommonVariables.TOKEN);
        appUserMap.put("defaultClientId", CommonVariables.clientid);
        appUserMap.put("flightNo", flightDetails.getFlightNo());
        appUserMap.put("fetchString", CommonVariables.Clientip);


        String jsonString = gson.toJson(appUserMap);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "GetAirportFlightCharges")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}