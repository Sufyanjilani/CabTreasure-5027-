package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import base.listener.Listener_AppTrackingDetails;
import base.models.TrackingInformation;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_AppTrackingDetails extends AsyncTask<Void, Void, String> {
    private Context context;
    private Listener_AppTrackingDetails listener;
    private TrackingInformation trackingInformation;

    public Manager_AppTrackingDetails(Context context, TrackingInformation trackingInformation, Listener_AppTrackingDetails listener) {
        this.context = context;
        this.trackingInformation = trackingInformation;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String, Object> map = new HashMap<>();
        String aaa = new Gson().toJson(trackingInformation);
        map.put("json", aaa);
//        map.put("Token", CommonVariables.TOKEN);
        String token = AppConstants.getAppConstants().getToken();
        map.put("Token", token);

//        String jsonString = new Gson().toJson(map);
        String jsonString = "{\n" +
                "  \"json\": \"{\\n \\\"MapType\\\" : 1,\\n \\\"defaultClientId\\\" : \\\"" + trackingInformation.defaultClientId + "\\\",\\n \\\"toLatLng\\\" : \\\""+trackingInformation.toLatLng+"\\\",\\n \\\"MapKey\\\" : \\\""+ trackingInformation.MapKey+"\\\",\\n \\\"DriverId\\\" : \\\"" + trackingInformation.DriverId + "\\\",\\n \\\"JobId\\\" : \\\"" + trackingInformation.JobId + "\\\",\\n \\\"uniqueValue\\\" : \\\"" + trackingInformation.uniqueValue + "\\\",\\n \\\"fromLatLng\\\" : \\\"" + trackingInformation.fromLatLng + "\\\"}\",\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "  \"Token\": \"" + token + "\"\n" +
                "}";


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "AppTrackingDetails")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener != null) {
            listener.onComplete(result);
        }
    }// End onPostExecute()
}// End class PrefetchData
