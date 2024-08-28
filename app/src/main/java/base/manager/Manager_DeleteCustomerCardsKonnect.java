package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;

import base.listener.Listener_DeleteAccount;
import base.listener.Listener_DeleteCard_Konnect;
import base.listener.Listener_Register_Card_KonnectPay;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_DeleteCustomerCardsKonnect extends AsyncTask<Void, Void, String> {


    private String customerId = "";
    private String paymentMethodId = "";

    private final Context context;
    private SweetAlertDialog mDialog;
    private Listener_DeleteCard_Konnect listener;


    public Manager_DeleteCustomerCardsKonnect(Context context, String customerId, String paymentMethodId, Listener_DeleteCard_Konnect listListener) {
        this.context = context;
        this.customerId = customerId;
        this.paymentMethodId = paymentMethodId;
        this.listener = listListener;
    }

    @Override
    protected void onPostExecute(String result) {

        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (listener != null)
            listener.onComplete(result);
    }

    @Override
    protected String doInBackground(Void... params) {

        HashMap<String, Object> map = new HashMap<>();


        map.put("paymentMethodId", paymentMethodId);
        map.put("ClientId", CommonVariables.clientid);

   map.put("customerId", customerId);
        //map.put("customerId", 389);
        Gson gson = new Gson();

        String jsonString = gson.toJson(map);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "DeleteCustomerCards")
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
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.setTitleText("Removing Card");
            mDialog.setContentText("Please wait..");
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// End onPreExecute()

}
