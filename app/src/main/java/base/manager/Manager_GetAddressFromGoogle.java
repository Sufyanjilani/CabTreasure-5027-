package base.manager;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import base.activities.Activity_Splash;
import base.listener.Listener_GetAddress;
import base.utils.CommonVariables;

public class Manager_GetAddressFromGoogle {

    private String tempSearchString;
    private Activity activity;
    private Listener_GetAddress listListener;

    public Manager_GetAddressFromGoogle(Activity activity, String tempSearchString, Listener_GetAddress listListener) {
        this.activity = activity;
        this.tempSearchString = tempSearchString;
        this.listListener = listListener;
        onPreExecute();
        doInBackground();
    }

    protected void onPreExecute() {
        if (listListener != null) listListener.onStart(true);
    }// End onPreExecute()

    protected void doInBackground() {
        try {
            tempSearchString = tempSearchString.replace(" ", "+");
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + tempSearchString
                    + "&location=" + Activity_Splash.LAT + "," + Activity_Splash.LNG
                    + "&radius=100&components=country:US"
                    + "&key=" + CommonVariables.GOOGLE_API_KEY;

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    onPostExecute(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onPostExecute("error_" + error.getMessage());
                }
            });

            // Add the request to the RequestQueue.
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            onPostExecute("error_" + e.getMessage());
        }
    }

    protected void onPostExecute(String result) {
        if (listListener != null)
            listListener.onCompleteGetAddress(result);
    }
}