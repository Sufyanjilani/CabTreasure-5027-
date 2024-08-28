package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.support.parser.SoapHelper;

import com.support.parser.PropertyInfo;

import base.listener.Listener_CallOffice;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Manager_CallOffice extends AsyncTask<Void, Void, String> {
    private Context context;
    private String data;
    private Listener_CallOffice listener;
    private SweetAlertDialog mProgressDialog;

    public Manager_CallOffice(Context context, String data, Listener_CallOffice listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
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
        try {
            return new SoapHelper.Builder(CommonVariables.SERVICE, context)
                    .setMethodName("CallOffice", true)
                    .addProperty("defaultClientId", CommonVariables.clientid, PropertyInfo.LONG_CLASS)
                    .addProperty("jsonString", data, PropertyInfo.STRING_CLASS)
                    .addProperty("uniqueValue", CommonVariables.clientid + "4321orue", PropertyInfo.STRING_CLASS)
                    .getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
