//package base.manager;
//
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import base.listener.Listener_GetAddressCoordinate;
//import base.models.ClsLocationData;
//import base.utils.CommonVariables;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//public class Manager_GetAddressCoordinates_WebDispatch extends AsyncTask<Void, Void, String> {
//
//    private Listener_GetAddressCoordinate listener;
//
//    private String address;
//
//    public Manager_GetAddressCoordinates_WebDispatch(String address, Listener_GetAddressCoordinate listener) {
//        this.address = address;
//        this.listener = listener;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        if (listener != null) {
//            listener.onPre();
//        }
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        super.onPostExecute(result);
//
//        if (listener != null) {
//            listener.onComplete(result);
//        }
//    }
//
//    @Override
//    protected String doInBackground(Void... params) {
//
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "");
//        Request request = new Request.Builder()
//                .url(CommonVariables.WEB_DISPATCH_BASE_URL + "GetLocationLatLng?search=" + address)
//                .post(body)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//}
