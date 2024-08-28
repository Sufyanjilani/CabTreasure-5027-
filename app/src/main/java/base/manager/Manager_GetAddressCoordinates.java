package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import base.listener.Listener_GetAddressCoordinate;
import base.models.ClsLocationData;
import base.utils.CommonVariables;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetAddressCoordinates extends AsyncTask<Void, Void, String> {

    private Context context;
    private ArrayList<ClsLocationData> dataArrayList;
    private Listener_GetAddressCoordinate listener;

    private boolean isWebDispatch = false;

    public Manager_GetAddressCoordinates(Context context,boolean isWebDispatch, ArrayList<ClsLocationData> dataArrayList, Listener_GetAddressCoordinate listener) {
        this.context = context;
        this.isWebDispatch = isWebDispatch;
        this.dataArrayList = dataArrayList;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null) {
            listener.onPre();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (listener != null) {
            listener.onComplete(result);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String, Object> map = new HashMap<>();

        String jsonString ="";
        String url = CommonVariables.BASE_URL + "GetAddressCoordinates";
        if(isWebDispatch){
            try {
                String address = dataArrayList.get(0).keyword;
                url = CommonVariables.BASE_URL + "GetLocationLatLng?search=" + address+"="+CommonVariables.clientid;
            }catch (Exception ex){

                ex.printStackTrace();
            }
        }else{
            map.put("request", dataArrayList);
            jsonString = new Gson().toJson(map);
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(url)
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
