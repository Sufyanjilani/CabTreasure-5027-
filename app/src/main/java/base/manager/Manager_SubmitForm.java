package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.support.parser.SoapHelper;

import com.support.parser.PropertyInfo;

import base.listener.Listener_SubmitForm;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Manager_SubmitForm extends AsyncTask<Void, Void, String> {
    private Context context;
    private String data;
    private String methodName;
    private Listener_SubmitForm listener;
    private SweetAlertDialog mProgressDialog;

    public Manager_SubmitForm(Context context, String methodName, String data, Listener_SubmitForm listener) {
        this.context = context;
        this.methodName = methodName;
        this.data = data;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        try {
            mProgressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mProgressDialog.setTitleText("Submitting.");
            mProgressDialog.setContentText("Please Wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return new SoapHelper.Builder(CommonVariables.SERVICE, context)
                    .setMethodName(methodName, true)
                    .addProperty("defaultClientId", CommonVariables.clientid, PropertyInfo.LONG_CLASS)
                    .addProperty("jsonString", data, PropertyInfo.STRING_CLASS)
                    .addProperty("uniqueValue", CommonVariables.clientid + "4321orue", PropertyInfo.STRING_CLASS)
                    .getResponse();

        } catch (Exception e) {
            e.printStackTrace();
        }


               /* OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + methodName)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }*/
        return null;
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
