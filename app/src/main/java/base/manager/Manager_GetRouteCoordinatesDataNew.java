package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;

import base.listener.Listener_GetRouteCoordinatesDataNew;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetRouteCoordinatesDataNew extends AsyncTask<String[], Void, String> {

    private Context mContext;
    private HashMap<String, Object> map;
    private boolean isWebDispatch;
    private Listener_GetRouteCoordinatesDataNew listener;
    private SweetAlertDialog mDialog;

    public Manager_GetRouteCoordinatesDataNew(Context mContext, boolean isWebDispatch,HashMap<String, Object> map, Listener_GetRouteCoordinatesDataNew listener) {
        this.mContext = mContext;
        this.isWebDispatch = isWebDispatch;
        this.map = map;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.setTitleText("Getting Directions");
            mDialog.setContentText("Please Wait");
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String[]... params) {

        String endpoint = "GetRouteCoordinatesData";

        if(isWebDispatch){
            endpoint = "GetRouteCoordinatesDataRTF";
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(map);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + endpoint)
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
        try {
            mDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listener != null) {
            listener.onComplete(response);
        }
    }
}


