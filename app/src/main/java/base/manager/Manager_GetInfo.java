package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;

import base.listener.Listener_GetInfo;
import base.models.ParentPojo;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetInfo extends AsyncTask<String[], Void, String> {

    private Context context;
    private ParentPojo p;
    private Listener_GetInfo listener;
    private SweetAlertDialog mProgressDialog;

    public Manager_GetInfo(Context context, ParentPojo p, Listener_GetInfo listener) {
        this.context = context;
        this.p = p;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mProgressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mProgressDialog.setTitleText(p.getGettingInformation());
            mProgressDialog.setContentText(p.getPleaseWait());
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (listener != null) {
            listener.onComplete(response);
        }
    }

    @Override
    protected String doInBackground(String[]... params) {
        Gson gson = new Gson();

        HashMap<String, Object> appUserMap = new HashMap<>();

        String token = AppConstants.getAppConstants().getToken();
        appUserMap.put("Token", token);
        appUserMap.put("defaultClientId", CommonVariables.localid + "");
        appUserMap.put("encKey", CommonVariables.localid + "4321orue");

        String jsonString = gson.toJson(appUserMap);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "GetInfo")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}

