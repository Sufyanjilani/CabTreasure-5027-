package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import base.listener.Listener_GetFlightDetails;
import base.newui.HomeFragment;
import base.utils.CommonVariables;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetFlightDetails extends AsyncTask<Void, Void, String> {
    private Context context;
    private String text;
    private Listener_GetFlightDetails listener;

    public Manager_GetFlightDetails(Context context, String text, Listener_GetFlightDetails listener) {
        this.context = context;
        this.text = text;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null) {
            listener.onStartFlightDetails(true);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
          /*  String values = "[\"" +
                    "defaultClientId\":\"" + CommonVariables.clientid
                    + "\",\"" +
                    "InputDateTime\": \"" + HomeFragment.flightDateForSchedule.replace("-", "/")
                    + "\",\"" +
                    "FlightNo\": \"" + text
                    + "\"   ]";*/

            HashMap<String, Object> map = new HashMap<>();
            map.put("defaultClientId", "" + CommonVariables.clientid);
            map.put("InputDateTime", HomeFragment.flightDateForSchedule.replace("-", "/"));
            map.put("FlightNo", text);
            String json = new Gson().toJson(map);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + "GetFDetails")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "error_" + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener != null) {
            listener.onCompleteFlightDetails(result);
        }
    }// End onPostExecute()
}// End class PrefetchData
