package base.manager;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;

import base.listener.Listener_GetJobStatusWithPriceNew;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetJobStatusWithPriceNew extends AsyncTask<String[], Void, String> {

    private String bookingRef;
    private Listener_GetJobStatusWithPriceNew listener;

    public Manager_GetJobStatusWithPriceNew(String bookingRef, Listener_GetJobStatusWithPriceNew listener) {
        this.bookingRef = bookingRef;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String[]... params) {
        Gson gson = new Gson();

        String[] bookingRefNoArr = {bookingRef};
        HashMap<String, Object> map = new HashMap<>();
//        map.put("Token", CommonVariables.TOKEN);
        String token = AppConstants.getAppConstants().getToken();
        map.put("Token", token);
        map.put("defaultclientId", CommonVariables.clientid + "");
        map.put("refarray", bookingRefNoArr);

        String jsonString = gson.toJson(map);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "GetJobStatusWithPriceNew")
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
    protected void onPostExecute(String response) {
        if (listener != null) {
            listener.onComplete(response);
        }
    }

}

