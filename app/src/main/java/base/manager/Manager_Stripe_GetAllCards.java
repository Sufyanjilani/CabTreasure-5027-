package base.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import base.activities.Activity_HomePayment;
import base.listener.Listener_Stripe_GetAllCards;
import base.utils.Config;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_Stripe_GetAllCards extends AsyncTask<String, Void, String> {

    private Context context;
    private boolean showDialog;
    private String stripeCustomerId;
    private Listener_Stripe_GetAllCards listener;
    private SweetAlertDialog mDialog;
    private SharedPreferences sp;
    private String BASE_URL_FOR_STRIPE = "https://www.api-eurosofttech.co.uk/stripeserverapi/";

    public Manager_Stripe_GetAllCards(Context context, boolean showDialog, String stripeCustomerId, Listener_Stripe_GetAllCards listener) {
        this.context = context;
        this.showDialog = showDialog;
        this.stripeCustomerId = stripeCustomerId;
        this.listener = listener;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showDialog) {
            try {
                mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                mDialog.setTitleText("Getting Card List");
                mDialog.setContentText("Please wait..");
                mDialog.setCancelable(false);
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            if (stripeCustomerId.length() == 0) {
                return "No Card Found";
            }

            JSONObject json = new JSONObject();
            json.put("APISecret", "" + sp.getString(Config.Stripe_SecretKey, ""));
            json.put("customerId", stripeCustomerId);

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(8, TimeUnit.SECONDS);
            clientBuilder.readTimeout(8, TimeUnit.SECONDS);
            OkHttpClient client = clientBuilder.build();
            String url = BASE_URL_FOR_STRIPE + "Customer-List-All-Cards/";
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
            return "error_" + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (showDialog) {
            try {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (listener != null) {
            listener.onComplete(result);
        }
    }
}
