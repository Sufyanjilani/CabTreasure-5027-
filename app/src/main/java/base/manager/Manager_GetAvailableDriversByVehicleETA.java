package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import base.listener.Listener_GetAvailableDriversManager;
import base.models.ReqAvailableDrivers;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetAvailableDriversByVehicleETA extends AsyncTask<Void, Void, String> {
    private static final String TAG = "GetAvailableDriversBy";
    private Context context;
    private ReqAvailableDrivers reqAvailableDrivers;
    private Listener_GetAvailableDriversManager listener;

    public Manager_GetAvailableDriversByVehicleETA(Context context, ReqAvailableDrivers reqAvailableDrivers, Listener_GetAvailableDriversManager listener) {
        this.context = context;
        this.reqAvailableDrivers = reqAvailableDrivers;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        try {
//            lat = mCurrentLocation.address.getLatitude();
//            lng = mCurrentLocation.address.getLongitude();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        HashMap<String, Object> appUserMap = new HashMap<>();
//        appUserMap.put("Token", CommonVariables.TOKEN);
        String token = AppConstants.getAppConstants().getToken();
        appUserMap.put("Token", token);
        appUserMap.put("Request", reqAvailableDrivers);

        String jsonString = new Gson().toJson(appUserMap);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "GetAvailableDriversByVehicleETA")
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
    }
}
