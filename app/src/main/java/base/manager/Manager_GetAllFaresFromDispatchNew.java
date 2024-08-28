package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;

import base.listener.Listener_GetAllFaresFromDispatchNew;
import base.models.Model_BookingInformation;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetAllFaresFromDispatchNew extends AsyncTask<String[], Void, String> {

    private Context context;
    private Model_BookingInformation information;
    private Listener_GetAllFaresFromDispatchNew listener;
    private SweetAlertDialog mDialog;

    private Boolean isWebDispatch = false;

    public Manager_GetAllFaresFromDispatchNew(Context context,boolean isWebDispatch, Model_BookingInformation information, Listener_GetAllFaresFromDispatchNew listener) {
        this.isWebDispatch = isWebDispatch;
        this.context = context;
        this.information = information;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.setTitleText("Getting Fare");
            mDialog.setContentText("Please wait..");
            mDialog.setCancelable(false);
            mDialog.show();

            if (listener != null) {
                listener.onPre("start");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String[]... params) {

        HashMap<String, Object> appUserMap = new HashMap<>();

        String method = "GetAllFaresFromDispatchNew";

        if(isWebDispatch){
            method = "GetAllFaresDispatchNew";
            information.defaultclientId = CommonVariables.clientid+"";
        }

//        appUserMap.put("Token", CommonVariables.TOKEN);
        String token = AppConstants.getAppConstants().getToken();

        if(!isWebDispatch) {
            appUserMap.put("Token", token);
            appUserMap.put("hashKey", "" + CommonVariables.clientid + "4321orue");
            appUserMap.put("defaultclientId", "" + CommonVariables.clientid);
            appUserMap.put("fareType", "dispatch");
        }
        appUserMap.put("bookingInformation", information);

        Gson gson = new Gson();
        String jsonString = gson.toJson(appUserMap);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + method)
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
    protected void onPostExecute(String response) {
        if (listener != null) {
            listener.onPost(response);
        }

        if (mDialog != null)
            mDialog.dismiss();
    }
}

