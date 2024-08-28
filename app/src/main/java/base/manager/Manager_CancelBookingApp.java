package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.listener.Listener_CancelBookingApp;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_CancelBookingApp extends AsyncTask<String[], Void, String> {
    private SweetAlertDialog mProgressDialog;
    private String response = null;
    private Context context;
    private Listener_CancelBookingApp listener;

    public Manager_CancelBookingApp(Context context, Listener_CancelBookingApp listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mProgressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mProgressDialog.setTitleText("Cancelling");
            mProgressDialog.setContentText("Please Wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// End onPreExecute()

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listener != null)
            listener.onComplete(result);
    }// End onPostExecute()

    @Override
    protected String doInBackground(String[]... params) {
        String[] task = params[0];
        if (task != null) {
            DatabaseOperations mDatabaseOperations = new DatabaseOperations(new DatabaseHelper(context));
            if (task[0].equals("DELETE")) {
                mDatabaseOperations.DeleteBooking(task[1]);
                return null;
            } else if (task[0].equals("CANCEL")) {
                try {
                    HashMap<String, Object> appUserMap = new HashMap<>();
                    String token = AppConstants.getAppConstants().getToken();
                    appUserMap.put("Token",token );
                    appUserMap.put("defaultClientId", CommonVariables.localid);
                    appUserMap.put("refNo", task[1]);
                    appUserMap.put("uniqueValue", CommonVariables.localid + task[1] + "4321orue");
                    appUserMap.put("postedFrom", CommonVariables.DEVICE_TYPE);

                    String _jsonString = new Gson().toJson(appUserMap);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();

                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), _jsonString);
                    Request request = new Request.Builder()
                            .url(CommonVariables.BASE_URL + "CancelBookingAppNew")
                            .post(body)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        return response.body().string();

                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }

                } catch (Exception e) {
                    response = "error|" + e.getMessage();
                    e.printStackTrace();
                }
            }
        }
        return response;
    }// End dAoInBackground
}// End class PrefetchData
