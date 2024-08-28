package base.manager;

import static base.utils.CommonVariables.Currency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;



import org.json.JSONException;
import org.json.JSONObject;

import com.support.parser.PropertyInfo;

import java.util.concurrent.TimeUnit;

import base.activities.Activity_HomePayment;
import base.listener.Listener_CallOffice;
import base.listener.Listener_StripePrePayment;
import base.utils.CommonVariables;
import base.utils.Config;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_StripePrePayment extends AsyncTask<Void, Void, String> {
    private Context context;
    private String pm;
    private String stripeId;
    private String secretKey;
    private String amount;
    private SweetAlertDialog mProgressDialog;
    private Listener_StripePrePayment listener;

    public Manager_StripePrePayment(Context context, String pm, String stripeId, String secretKey, String amount, Listener_StripePrePayment listener) {
        this.context = context;
        this.pm = pm;
        this.stripeId = stripeId;
        this.secretKey = secretKey;
        this.amount = amount;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        try {
            mProgressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mProgressDialog.setTitleText("Proceeding Payment");
            mProgressDialog.setContentText("Please Wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            JSONObject json = new JSONObject();
            json.put("Confirm", "true");
            json.put("OffSession", "true");
            try {
                json.put("Amount", (Double.parseDouble(amount) * 100));
            } catch (Exception e) {
                e.printStackTrace();
                json.put("Amount", 0);
            }
            json.put("PaymentMethod", pm);
            json.put("Currency", (!sp.getString(CommonVariables.CurrencySymbol, "\u00A3").equals("$")) ? "GBP" + "" : Currency);
            json.put("APISecret", secretKey);
            json.put("CustomerID", stripeId);

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(8, TimeUnit.SECONDS);
            clientBuilder.readTimeout(8, TimeUnit.SECONDS);
            OkHttpClient client = clientBuilder.build();
            String url = "https://www.api-eurosofttech.co.uk/stripeserverapi/createpaymentintent/";
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Response rawResponse = client.newCall(request).execute();
            String response = rawResponse.body().string();
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listener != null) {
            listener.onComplete(result);
        }
    }// End onPostExecute()
}// End class PrefetchData
