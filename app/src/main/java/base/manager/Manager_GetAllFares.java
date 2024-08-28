package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import base.listener.Listener_GetAllFares;

public class Manager_GetAllFares extends AsyncTask<Void, Void, String> {
    private Context context;
    private Listener_GetAllFares listener;

    public Manager_GetAllFares(Context context, Listener_GetAllFares listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null) {
            listener.onStart("start");
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener != null) {
            listener.onComplete(result);
        }
    }// End onPostExecute()
}// End class PrefetchData
