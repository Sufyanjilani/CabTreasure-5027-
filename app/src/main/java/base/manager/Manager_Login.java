package base.manager;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;

import base.listener.Listener_Login;
import base.models.User;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_Login extends AsyncTask<String[], Void, String> {

    private User user;
    private Listener_Login listener;

    public Manager_Login(User user, Listener_Login listener) {
        this.user = user;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if (listener != null) {
                listener.onPre("start");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String response) {
        if (listener != null) {
            listener.onPost(response);
        }
    }

    @Override
    protected String doInBackground(String[]... params) {
        Gson gson = new Gson();

        HashMap<String, Object> appUserMap = new HashMap<>();
//        appUserMap.put("Token", CommonVariables.TOKEN);
        String token = AppConstants.getAppConstants().getToken();
        appUserMap.put("Token", token);
        appUserMap.put("appUser", user);

        String jsonString = gson.toJson(appUserMap);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "SigninAppUserNew")
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

