package base.manager;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;

import base.activities.Activity_Splash;
import base.listener.Listener_DeleteAccount;
import base.listener.Listener_GetAddress;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Manager_DeleteAccount extends AsyncTask<Void, Void, String> {


    private String customerId = "";
    private Listener_DeleteAccount listListener;

    public Manager_DeleteAccount(String customerId, Listener_DeleteAccount listListener) {
        this.customerId = customerId;
        this.listListener = listListener;
    }

    @Override
    protected void onPostExecute(String result) {
        if (listListener != null)
            listListener.onPost(result);
    }

    @Override
    protected String doInBackground(Void... params) {

        HashMap<String, Object> map = new HashMap<>();
        String token = AppConstants.getAppConstants().getToken();
        map.put("Token", token);
        map.put("defaultclientId", CommonVariables.clientid);
        map.put("hashKey", CommonVariables.clientid + "4321orue");
        map.put("CustomerId", customerId);
        Gson gson = new Gson();

        String jsonString = gson.toJson(map);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "DeleteAccountByCustomerId")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listListener != null)
            listListener.onPre();;
    }// End onPreExecute()
}