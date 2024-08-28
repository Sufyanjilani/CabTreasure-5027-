/*
package base.manager;

import android.content.Context;
import android.os.AsyncTask;
import com.google.gson.Gson;
import base.listener.Listener_GetAllFaresFromDispatchNew;
import base.models.Model_BookingInformation;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import base.utils.CommonVariables;


  public class Manager_GetAllFaresFromWebDispatch extends AsyncTask<String[], Void, String> {

    private Context context;
    private Model_BookingInformation information;
    private Listener_GetAllFaresFromDispatchNew listener;
    private SweetAlertDialog mDialog;

    public Manager_GetAllFaresFromWebDispatch(Context context, Model_BookingInformation information, Listener_GetAllFaresFromDispatchNew listener) {
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


        Gson gson = new Gson();
        String jsonString = gson.toJson(information);

        OkHttpClient client = new OkHttpClient();
         RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.WEB_DISPATCH_BASE_URL +"GetAllFaresFromDispatchNew")
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
*/
