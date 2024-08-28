package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import java.util.HashMap;
import base.listener.Listener_SaveCustomerId;
import base.models.CardJudoModel;
import base.models.SettingsModel;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_SaveCustomerId extends AsyncTask<CardJudoModel, Void, String> {

    private Context context;
    private boolean isAddToken;
    private String CustomerID;
    private SettingsModel settingsModel;
    private Listener_SaveCustomerId listener;
    private SweetAlertDialog mDialog;

    public Manager_SaveCustomerId(Context context, boolean isAddToken, String CustomerID, SettingsModel settingsModel, Listener_SaveCustomerId listener) {
        this.context = context;
        this.isAddToken = isAddToken;
        this.CustomerID = CustomerID;
        this.settingsModel = settingsModel;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.setTitleText("Saving Details");
            mDialog.setContentText("Please wait..");
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(CardJudoModel... strings) {
        try {
/*            String jsonString = "";
            if (isAddToken) {
                jsonString = "{" +
                        "PhoneNo:" + "\"" + "" + "\"" + "," +
                        "UserName:" + "\"" + "" + "\"" + "," +
                        "Passwrd:" + "\"" + settingsModel.getPassword() + "\"" + "," +
                        "Email:" + "\"" + settingsModel.getEmail().trim() + "\"" + "," +
                        "TokenDetails:" + "\"" + CustomerID + "\"" + "," +
                        "PickDetails:" + "\"" + "yes" + "\"" + "," +
                        "StripeCustomerId:" + "\"" + CustomerID + "\"" +
                        "}";
            }*/
            String token = AppConstants.getAppConstants().getToken();
            HashMap<String, Object> rootMap = new HashMap<>();
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("uniqueValue",CommonVariables.clientid + "4321orue");
            dataMap.put("IsDefault",false);
            dataMap.put("Email",settingsModel.getEmail().trim());
            dataMap.put("TokenDetails",CustomerID);
            dataMap.put("PickDetails","yes");
            dataMap.put("PhoneNumber",settingsModel.getPhoneNo());
            dataMap.put("defaultClientId",CommonVariables.clientid);
            dataMap.put("RecordId","0");
            dataMap.put("Passwrd",settingsModel.getPassword());
            rootMap.put("jsonString",dataMap);
            rootMap.put("uniqueValue",CommonVariables.clientid + "4321orue");
            rootMap.put("defaultClientId",CommonVariables.clientid);
            rootMap.put("Token",token);


            String requestBody = new Gson().toJson(rootMap);
            /*            {
 “uniqueValue” : “50624321orue”,
 “jsonString” : {
  “IsDefault” : false,
  “Email” : “test@yopmail.com”,
  “PickDetails” : “yes”,
  “PhoneNumber” : “01213456789",
  “defaultClientId” : “5062",
  “TokenDetails” : “cus_NxaAXAzsj7Jgia”,
  “DefaultRecordId” : 0,
  “RecordId” : 0,
  “uniqueValue” : “50624321orue”,
  “Passwrd” : “123"
            },
 “defaultclientId” : “5062"
            }*/


    /*        SoapHelper.Builder builder = new SoapHelper.Builder(CommonVariables.SERVICE, context)
                    .setMethodName("UpdateAppUser", true)
                    .addProperty("defaultClientId", CommonVariables.clientid, PropertyInfo.LONG_CLASS)
                    .addProperty("jsonString", jsonString, PropertyInfo.STRING_CLASS)
                    .addProperty("uniqueValue", CommonVariables.clientid + "4321orue", PropertyInfo.STRING_CLASS);

            return builder.getResponse();*/





            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody);
            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + "UpdateAppUser")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        if (mDialog != null) {
            mDialog.dismiss();
        }

        if (listener != null) {
            listener.onComplete(result);
        }
    }
}
