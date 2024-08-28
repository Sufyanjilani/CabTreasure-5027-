package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import base.listener.Listener_Judo_GetAllCards;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_Judo_GetAllCards extends AsyncTask<String, Void, String> {
    private SweetAlertDialog mProgressDialog;
    private Context context;
    private String json;
    private Listener_Judo_GetAllCards listener;

    public Manager_Judo_GetAllCards(Context context, String json, Listener_Judo_GetAllCards listener) {
        this.context = context;
        this.json = json;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        try {
            mProgressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mProgressDialog.setTitleText("Requesting Card Details");
            mProgressDialog.setContentText("Please Wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// End onPreExecute()

    @Override
    protected String doInBackground(String... s) {
        HashMap<String, Object> appUserMap = new HashMap<>();
//        appUserMap.put("Token", CommonVariables.TOKEN);
        String token = AppConstants.getAppConstants().getToken();
        appUserMap.put("Token", token);
        appUserMap.put("jsonString", json);

        String jsonString = new Gson().toJson(appUserMap);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "requestCCList")
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
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mProgressDialog.dismiss();

        if (listener != null)
            listener.onComplete(result);
    }
}
