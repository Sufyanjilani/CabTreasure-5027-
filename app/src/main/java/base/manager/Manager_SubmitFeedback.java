package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import base.listener.Listener_SubmitFeedback;
import base.models.FeedbackInformation;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_SubmitFeedback extends AsyncTask<String, Void, String> {
    private Context context;
    private FeedbackInformation feedbackinformation;
    private Listener_SubmitFeedback listener;
    private SweetAlertDialog mDialog;

    public Manager_SubmitFeedback(Context context, FeedbackInformation feedbackinformation, Listener_SubmitFeedback listener) {
        this.context = context;
        this.feedbackinformation = feedbackinformation;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.setTitleText("Saving Feedback");
            mDialog.setContentText("Please Wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (mDialog != null) {
            mDialog.dismiss();
        }

        if (listener != null) {
            listener.onComplete(result);
        }
    }

    @Override
    protected String doInBackground(String... params) {

        HashMap<String, Object> appUserMap = new HashMap<>();
//        appUserMap.put("Token", CommonVariables.TOKEN);
        String token = AppConstants.getAppConstants().getToken();
        appUserMap.put("Token", token);
        feedbackinformation.defaultclientId = "" + CommonVariables.clientid;
        feedbackinformation.hashKey = CommonVariables.clientid + "4321orue";
        appUserMap.put("request", feedbackinformation);

        String _json_String = new Gson().toJson(appUserMap);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), _json_String);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "SaveCustomerReviewsTip")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
