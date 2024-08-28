package base.manager;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;

import base.listener.Listener_GetAll_cards_Konnect;
import base.listener.Listener_GetCustomerCardsKonnect;
import base.listener.Listener_Stripe_GetAllCards;

import base.utils.CommonVariables;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Manager_GetCustomerCardsKonnect extends AsyncTask<Void, Void, String> {


    private String customerId = "";
    private String version = "";
    private Listener_GetAll_cards_Konnect listListener;


/*  "customerId": 332,
          "version": "12.1",
          "ClientId": 5405,
          "OS": "Android"*/
    public Manager_GetCustomerCardsKonnect(String customerId, String version , Listener_GetAll_cards_Konnect listListener) {
        this.customerId = customerId;
        this.version = version;
        this.listListener = listListener;
    }

    @Override
    protected void onPostExecute(String response) {
        if (listListener != null)
            listListener.onComplete(response);
    }

    @Override
    protected String doInBackground(Void... params) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("version", version);
        map.put("OS", "Android");
        map.put("ClientId", CommonVariables.clientid);
       map.put("customerId", customerId);
       // map.put("customerId", 389);

        Gson gson = new Gson();

        String jsonString = gson.toJson(map);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "GetCustomerCards")
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


    }// End onPreExecute()

}