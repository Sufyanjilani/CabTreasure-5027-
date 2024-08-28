package base.manager;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;

import base.listener.Listener_ForgetPassword;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_ForgetPassword extends AsyncTask<String[], Void, String> {

    private String email;
    private Listener_ForgetPassword listener;

    public Manager_ForgetPassword(String email, Listener_ForgetPassword listener) {
        this.email = email;
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
        HashMap<String, Object> map = new HashMap<>();
//        map.put("Token", CommonVariables.TOKEN);
        String token = AppConstants.getAppConstants().getToken();
        map.put("Token", token);
        map.put("defaultclientId", CommonVariables.localid + "");
        map.put("uniqueValue", CommonVariables.localid + "4321orue");
        String jsons= "{" +
                "PhoneNo:" + "\"" + "" + "\"" + "," +
                "UniqueId:" + "\"" + "" + "\"" + "," +
                "DeviceInfo:" + "\"" + "" + "\"" + "," +
                "UserName:" + "\"" + "" + "\"" + "," +
                "Email:" + "\"" + email + "\"" + "," +
                "Passwrd:" + "\"" + "" + "\"" + "," +
                "Address:" + "\"" + "" + "\"" + "," +
                "Telephone:" + "\"" + "" + "\"" + "," +
                "SendSMS:" + "\"" + "" + "\"" +
                "}";
        map.put("jsonString", jsons);

        String jsonString = gson.toJson(map);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "ForgetPasswordAppUser")
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

