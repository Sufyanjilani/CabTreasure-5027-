package base.manager;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import base.listener.Listener_GetLatLngFromPlaceId;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Manager_GetLatLnfFromPlaceId {

    private String placeId;
    private Activity activity;
    private Listener_GetLatLngFromPlaceId listListener;
    private SweetAlertDialog mProgressDialog;

    public Manager_GetLatLnfFromPlaceId(Activity activity, String placeId, Listener_GetLatLngFromPlaceId listListener) {
        this.activity = activity;
        this.placeId = placeId;
        this.listListener = listListener;
        onPreExecute();
        doInBackground();
    }

    private void onPreExecute() {
        try {
            mProgressDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
            mProgressDialog.setTitleText("");
            mProgressDialog.setContentText("Please Wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doInBackground() {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&key=" + CommonVariables.GOOGLE_API_KEY;
        try {
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
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listListener != null) {
            listListener.onCompleteForGetGoogleLatLngFromPlaceId(result);
        }
    }
}