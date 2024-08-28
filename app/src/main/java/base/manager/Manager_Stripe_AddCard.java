package base.manager;

import static base.utils.CommonVariables.Currency;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import base.listener.Listener_Stripe_AddCard;
import base.models.SettingsModel;
import base.utils.CommonVariables;
import base.utils.Config;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_Stripe_AddCard extends AsyncTask<String, Void, String> {

    private Context context;
    private String stripeCustomerId;
    private SettingsModel settingsModel;
    private Listener_Stripe_AddCard listener;
    private SweetAlertDialog mDialog;
    private SharedPreferences sp;
    private String BASE_URL_FOR_STRIPE = "https://www.api-eurosofttech.co.uk/stripeserverapi/";

    public Manager_Stripe_AddCard(Context context, String stripeCustomerId, SettingsModel settingsModel, Listener_Stripe_AddCard listener) {
        this.context = context;
        this.stripeCustomerId = stripeCustomerId;
        this.settingsModel = settingsModel;
        this.listener = listener;
        sp = PreferenceManager.getDefaultSharedPreferences(context);

    }

    @Override
    protected void onPreExecute() {
        try {
            mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.setTitleText("Adding card");
            mDialog.setContentText("Please wait..");
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            JSONObject json = new JSONObject();
            json.put("APISecret", "" + sp.getString(Config.Stripe_SecretKey, ""));
            json.put("Email", "" + settingsModel.getEmail());
            json.put("Name", "" + settingsModel.getName());
            json.put("PhoneNumber", "" + settingsModel.getPhoneNo());
            json.put("Amount", "0");
            json.put("Description", "");
            json.put("Currency", (!sp.getString(CommonVariables.CurrencySymbol, "\u00A3").equals("$")) ? "GBP" + "" : Currency);

            if (stripeCustomerId.equals("")) {
                json.put("CustomerID", JSONObject.NULL);
            } else {
                json.put("CustomerID", stripeCustomerId);
            }


            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(8, TimeUnit.SECONDS);
            clientBuilder.readTimeout(8, TimeUnit.SECONDS);
            OkHttpClient client = clientBuilder.build();
            String url = BASE_URL_FOR_STRIPE + "create-Customer-Service/";

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
        if (mDialog != null) {
            mDialog.dismiss();
        }

        if (listener != null) {
            listener.onComplete(result);
        }
    }
}
