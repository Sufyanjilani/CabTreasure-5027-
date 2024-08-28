package base.manager;

import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;

import base.listener.Listener_GetInitializeDetailsFromDispatch;
import base.utils.CommonVariables;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Manager_GetInitializeDetailsFromDispatch extends AsyncTask<String, Void, String> {

    private String hashKey;
    private String defaultclientId;
    private Listener_GetInitializeDetailsFromDispatch listener;

    public Manager_GetInitializeDetailsFromDispatch(String defaultclientId, String hashKey, Listener_GetInitializeDetailsFromDispatch listener) {
        this.defaultclientId = defaultclientId;
        this.hashKey = hashKey;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null) {
            listener.onStart();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(null, new byte[0]);
        Request request = new Request.Builder()
                .url(CommonVariables.BASE_URL + "GetInitialDetailsFromDispatch?defaultclientId=" + defaultclientId + "&hashKey=" + hashKey)
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
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener != null)
            listener.onComplete(result);
    }
}