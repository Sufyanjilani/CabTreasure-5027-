package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.support.parser.PropertyInfo;
import com.support.parser.SoapHelper;

import base.listener.Listener_VerifyPromotionCode;
import base.models.ConfirmedBooking;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Manager_VerifyPromotionCode extends AsyncTask<String, Void, String> {

    private Context context;
    private ConfirmedBooking obj;
    private Listener_VerifyPromotionCode listener;
    private SweetAlertDialog mDialog;

    public Manager_VerifyPromotionCode(Context context, ConfirmedBooking obj, Listener_VerifyPromotionCode listener) {
        this.context = context;
        this.obj = obj;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.setTitleText("Verifying code");
            mDialog.setContentText("Please wait..");
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
        String json_String = new Gson().toJson(obj);
        SoapHelper.Builder builder = new SoapHelper.Builder(CommonVariables.SERVICE, context);
        builder
                .setMethodName("VerifyPromotion", true)
                .addProperty("jsonString", json_String, PropertyInfo.STRING_CLASS);
        try {
            return builder.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
