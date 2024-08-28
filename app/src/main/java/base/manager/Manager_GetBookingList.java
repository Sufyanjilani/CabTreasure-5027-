package base.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;

import com.google.gson.Gson;

import java.util.HashMap;

import base.listener.Listener_GetAllBooking;
import base.models.Model_BookingModel;
import base.models.ParentPojo;
import base.models.SettingsModel;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetBookingList extends AsyncTask<String, Void, String> {
    private static final String Booking_information = "jsonString";
    private static final String HASHKEY_VALUE = "4321orue";

    private SweetAlertDialog mDialog;
    private String FilterType;
    private boolean isActive;
    private boolean shouldShowProgress = true;
    private Context context;
    private ParentPojo p;
    private SharedPrefrenceHelper mHelper;
    private Listener_GetAllBooking listener;

    public Manager_GetBookingList(Boolean shouldShowProgress ,Context context, ParentPojo p, String FilterType, boolean isActive, Listener_GetAllBooking listener) {

        this.shouldShowProgress = shouldShowProgress;
        this.context = context;
        mHelper = new SharedPrefrenceHelper(context);
        this.p = p;
        this.FilterType = FilterType;
        this.isActive = isActive;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(shouldShowProgress)
        try {
            mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            mDialog.setTitleText(p.getGettingBookingList());
            mDialog.setContentText(p.getPleaseWait());
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (mDialog != null)
            mDialog.dismiss();

        if (listener != null)
            listener.onComplete(result);
    }

    @Override
    protected String doInBackground(String... params) {
        SettingsModel model = mHelper.getSettingModel();
        String deviceid = null;
        try {
            deviceid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Model_BookingModel obj = new Model_BookingModel();
        obj.defaultClientId = (int) CommonVariables.clientid;
        obj.uniqueValue = CommonVariables.clientid + HASHKEY_VALUE;
        obj.UniqueId = deviceid;
        obj.DeviceInfo = "Android";
        obj.SubCompanyId = CommonVariables.SUB_COMPANY;
        obj.UserName = model.getName();
        obj.CustomerId = model.getUserServerID();
        obj.PhoneNo = model.getPhoneNo();
        obj.Email = model.getEmail();
        obj.FilterType = FilterType;



        HashMap<String, Object> map = new HashMap<>();
//        map.put("Token", CommonVariables.TOKEN);
        String token = AppConstants.getAppConstants().getToken();
        map.put("Token", token);
        map.put("confirmedBooking", obj);
        String jsonString = new Gson().toJson(map);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "GetAllBookingsNew")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
