package base.manager;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.HashMap;

import base.activities.Activity_Splash;
import base.listener.Listener_GetAddress;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetAddressDetails extends AsyncTask<Void, Void, String> {


    private String search = "";
    private  boolean isWebDispatch = false;
    private Listener_GetAddress listListener;

    public Manager_GetAddressDetails(String search, boolean isWebDispatch,Listener_GetAddress listListener) {
        this.search = search;
        this.isWebDispatch = isWebDispatch;
        this.listListener = listListener;
    }

    @Override
    protected void onPostExecute(String result) {
        if (listListener != null)
            listListener.onCompleteGetAddress(result);
    }

    @Override
    protected String doInBackground(Void... params) {
        String jsonString ="";
        String url = CommonVariables.BASE_URL + "GetLocationDataNew";;


     /*   if(isWebDispatch){
           // url = CommonVariables.WEB_DISPATCH_BASE_URL + "GetLocationDataNew?search="+search;
        }*/

        if(isWebDispatch){
            url = CommonVariables.BASE_URL + "GetLocationDataNewForDispatch?search="+search +"="+CommonVariables.clientid;
        }
        else {
            HashMap<String, Object> placesRequest = new HashMap<>();
            placesRequest.put("keyword", search);
            placesRequest.put("lat", Activity_Splash.LAT);
            placesRequest.put("lng", Activity_Splash.LNG);

            placesRequest.put("apiKey", CommonVariables.GOOGLE_API_KEY);
            placesRequest.put("placeType", "Address");

            placesRequest.put("hashKey", CommonVariables.clientid + "4321orue");
            placesRequest.put("defaultclientId", "" + CommonVariables.clientid);

            placesRequest.put("radiusInMiles", "-1");

            HashMap<String, Object> map = new HashMap<>();
            String token = AppConstants.getAppConstants().getToken();
            map.put("Token", token);
            map.put("getPlacesRequest", placesRequest);

            Gson gson = new Gson();
            jsonString = gson.toJson(map);
        }


        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        https://www.treasureonlineapi.co.uk/CabTreasureWebApi/Home/GetLocationDataNewForDispatch?search=columbia=5520
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
        if (listListener != null)
            listListener.onStart(true);
    }// End onPreExecute()
}