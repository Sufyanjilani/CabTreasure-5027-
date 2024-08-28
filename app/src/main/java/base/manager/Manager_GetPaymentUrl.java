package base.manager;

import android.os.AsyncTask;

import com.google.gson.Gson;

import base.listener.Listener_GetPayment;
import base.models.PaymentGateway;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Manager_GetPaymentUrl extends AsyncTask<Void, Void, String> {

    private PaymentGateway payment;
    private Listener_GetPayment listListener;

    public Manager_GetPaymentUrl(PaymentGateway payment, Listener_GetPayment listListener) {
        this.payment = payment;
        this.listListener = listListener;
    }

    @Override
    protected void onPreExecute() {
        if (listListener != null) listListener.onStart(true);
    }// End onPreExecute()

    @Override
    protected String doInBackground(Void... params) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(payment);
        String url = "https://cabtreasureappapi.co.uk/CabTreasureWebApi/Home/GetPaymentUrl?json=" + jsonString;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (listListener != null)
            listListener.onComplete(result);
    }
}