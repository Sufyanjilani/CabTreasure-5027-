package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import base.listener.Listener_Judo_AddCard;
import base.models.CardJudoModel;
import base.models.Model_AppUserDetail;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_Judo_AddCard extends AsyncTask<String, Void, String> {
    private SweetAlertDialog mDialog;
    private Context context;
    private int isAddToken;
    private String email;
    private String pass;
    private CardJudoModel receipt;
    private ArrayList<CardJudoModel> judoCardList;
    private Listener_Judo_AddCard listener;


    public Manager_Judo_AddCard(Context context, int isAddToken, String email, String pass, CardJudoModel receipt, ArrayList<CardJudoModel> judoCardList, Listener_Judo_AddCard listener) {
        this.context = context;
        this.isAddToken = isAddToken;
        this.email = email;
        this.pass = pass;
        this.receipt = receipt;
        this.judoCardList = judoCardList;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        try {
            mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            if (isAddToken == 1) {
                mDialog.setTitleText("Saving Card Details");
            } else if (isAddToken == 2) {
                mDialog.setTitleText("Removing Card Details");
            } else {
                mDialog.setTitleText("Updating Changes");
            }
            mDialog.setContentText("Please Wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... s) {
        try {

            String jsonString = "";
            String consumerDetails =
                    "Token|" + receipt.getToken()
                            + "<<<consumer|" + receipt.getConsumerReference()
                            + "<<<consumertoken|" + receipt.getConsumerToken()
                            + "<<<lastfour|" + receipt.getLastFour()
                            + "<<<enddate|" + receipt.getEndDate()
                            + "<<<receiptid|" + receipt.getReceiptid();

            Model_AppUserDetail modelAppUserDetail = new Model_AppUserDetail();
            modelAppUserDetail.setPasswrd(pass);
            modelAppUserDetail.setEmail(email.trim());
            modelAppUserDetail.setPickDetails("yes");
            modelAppUserDetail.setDefaultClientId(CommonVariables.clientid);
            modelAppUserDetail.setUniqueValue(CommonVariables.clientid + "4321orue");

            if (isAddToken == 1) {
                modelAppUserDetail.setTokenDetails(consumerDetails);
                modelAppUserDetail.setRecordId(0);
                modelAppUserDetail.setDefaultRecordId(0);
                modelAppUserDetail.setDefaultTokenDetails("");
                try {
                    modelAppUserDetail.setDefault(judoCardList.size() == 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (isAddToken == 2)
            {
                modelAppUserDetail.setRecordId(receipt.getCardId());
                modelAppUserDetail.setDefault(receipt.isDefault());
                modelAppUserDetail.setTokenDetails("");

                if (receipt.isDefault()) {
                    if (judoCardList != null && judoCardList.size() > 0) {
                        try {
                            modelAppUserDetail.setDefaultRecordId(judoCardList.get(0).getCardId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String consumerDetails_ =
                                "Token|" + judoCardList.get(0).getToken()
                                        + "<<<consumer|" + judoCardList.get(0).getConsumerReference()
                                        + "<<<consumertoken|" + judoCardList.get(0).getConsumerToken()
                                        + "<<<lastfour|" + judoCardList.get(0).getLastFour()
                                        + "<<<enddate|" + judoCardList.get(0).getEndDate()
                                        + "<<<receiptid|" + receipt.getReceiptid();


                        modelAppUserDetail.setDefaultTokenDetails(consumerDetails_);

                    } else {
                        modelAppUserDetail.setDefaultRecordId(0);
                        modelAppUserDetail.setDefaultTokenDetails("");
                    }

                }
                else {
                    modelAppUserDetail.setDefaultRecordId(0);
                    modelAppUserDetail.setDefaultTokenDetails("");
                }
            } else if (isAddToken == 3) {
                modelAppUserDetail.setDefaultRecordId(0);
                modelAppUserDetail.setDefault(true);
                modelAppUserDetail.setRecordId(receipt.getCardId());
                modelAppUserDetail.setTokenDetails(consumerDetails);
                modelAppUserDetail.setDefaultTokenDetails("");
            }

            Gson gson = new Gson();
            HashMap<String, Object> rootMap = new HashMap<>();
            String token = AppConstants.getAppConstants().getToken();

            String userModel =gson.toJson(modelAppUserDetail);

            JSONObject data = new JSONObject(userModel);

            String strData = data.toString(4);
            rootMap.put("jsonString",strData);
            rootMap.put("Token",token);




            jsonString = gson.toJson(rootMap);



            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
            Request request = new Request.Builder()
                    .url(CommonVariables.BASE_URL + "UpdateAppUserDetail")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                return responseBody + ">>>" + isAddToken;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }


/*

            SoapHelper.Builder builder = new SoapHelper.Builder(CommonVariables.SERVICE, context)
                    .setMethodName("UpdateAppUserDetail", true)
                    .addProperty("jsonString", jsonString, PropertyInfo.STRING_CLASS);

            String response = builder.getResponse();*/


        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mDialog.dismiss();

        if (listener != null)
            listener.onComplete(result);
    }


    private void  getBody(){

    }
}
