package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import base.listener.Listener_ValidateBookingInfo;
import base.models.Model_ValidateBookingInfo;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_ValidateBookingInfo extends AsyncTask<Void, Void, String> {
    private Context context;
    private Model_ValidateBookingInfo modelValidateBookingInfo;
    private Listener_ValidateBookingInfo listener;
    private SweetAlertDialog mProgressDialog;

    public Manager_ValidateBookingInfo(Context context, Model_ValidateBookingInfo modelValidateBookingInfo, Listener_ValidateBookingInfo listener) {
        this.context = context;
        this.modelValidateBookingInfo = modelValidateBookingInfo;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mProgressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mProgressDialog.setTitleText("");
            mProgressDialog.setContentText("Please Wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String, Object> appUserMap = new HashMap<>();
//        appUserMap.put("Token", CommonVariables.TOKEN);
        String token = AppConstants.getAppConstants().getToken();
        appUserMap.put("Token", token);
        appUserMap.put("request", modelValidateBookingInfo);

        String jsonString = new Gson().toJson(appUserMap);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "ValidateBookingInfo")
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
