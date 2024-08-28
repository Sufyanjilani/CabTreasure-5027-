package base.manager;

import android.os.AsyncTask;

import com.google.gson.Gson;

import base.listener.Listener_Register_Card_KonnectPay;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.HashMap;

import base.listener.Listener_DeleteAccount;
import base.utils.AppConstants;
import base.utils.CommonVariables;


public class Manager_RegisterCardKonnectPay extends AsyncTask<Void, Void, String> {


    private String customerId = "";
    private String version = "";

    private Listener_Register_Card_KonnectPay listListener;

    public Manager_RegisterCardKonnectPay(String customerId,  String version, Listener_Register_Card_KonnectPay listListener) {
        this.customerId = customerId;
        this.version = version;
        this.listListener = listListener;
    }

    @Override
    protected void onPostExecute(String result) {
        if (listListener != null)
            listListener.onComplete(result);
    }

    @Override
    protected String doInBackground(Void... params) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("OS", "Android");
        map.put("version", version);
        map.put("ClientId", CommonVariables.clientid );
      map.put("customerId", customerId);
       // map.put("customerId", 389);

        Gson gson = new Gson();

        String jsonString = gson.toJson(map);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "RegisterCardKonnectPay")
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
            listListener.onPre();
    }// End onPreExecute()

}