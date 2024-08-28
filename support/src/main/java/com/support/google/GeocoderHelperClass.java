/*
package com.support.google;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GeocoderHelperClass {
    private Handler mHandler;
    private int mMaxResults = 1;
    private SetResult mSetResultInterface;
    private Context mContext;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public GeocoderHelperClass(Context context) {
        mHandler = new Handler(Looper.getMainLooper());
        mContext = context;
    }

    public GeocoderHelperClass setMaxResults(int maxResult) {
        mMaxResults = maxResult;
        return this;
    }

    public void execute(String location) {
        execute((Object[]) new String[]{location});
    }

    public void execute(double lat, double lon) {
        execute((Object[]) new Double[]{lat, lon});
    }

    String error = "";

    private void execute(final Object... objects) {
        try {
            if (objects != null && Geocoder.isPresent()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Address> list = new ArrayList<Address>();
                        List<Address> Templist = new ArrayList<Address>();
                        String formatedAdress = null;
                        try {
                            if (objects.length >= 2 && objects[0] instanceof Double) {
                                double lat = (Double) objects[0];
                                double lon = (Double) objects[1];
                                final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                                String DirectionClien = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "&sensor=false&key=" + sp.getString("googlemapkey", "");
                                OkHttpClient client = new OkHttpClient();

                                Request request = new Request.Builder()
                                        .url(DirectionClien)
                                        .build();

                                Response response = null;
                                try {
                                    response = client.newCall(request).execute();
                                    JSONObject jsonData = new JSONObject(response.body().string());
                                    try {
                                        error = jsonData.getString("error_message");
                                    } catch (Exception e) {
                                        error = "";
                                        e.printStackTrace();
                                    }
                                    JSONArray jsonarr = jsonData.getJSONArray("results");
                                    JSONObject jsonobj = jsonarr.getJSONObject(0);

                                    JSONArray types = jsonobj.getJSONArray("types");
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<String>>() {
                                    }.getType();
                                    List<String> typeList = gson.fromJson(types.toString(), type);
                                    if (typeList.contains("point_of_interest") || typeList.contains("transit_station") || typeList.contains("locality") || typeList.contains("political")) {
                                        jsonobj = jsonarr.getJSONObject(1);
//                                      formatedAdress = jsonobj.getString("formatted_address");
                                    }
                                    formatedAdress = jsonobj.getString("formatted_address");
//                                    if (types.length() == 2 && types.getString(0).equals("locality") && types.getString(1).equals("political")) {
//                                        jsonobj = jsonarr.getJSONObject(1);
//                                        formatedAdress = jsonobj.getString("formatted_address");
//                                        Log.e("Tag", "Address" + formatedAdress);
//                                    } else {
//
//                                        Log.e("Tag", "Address" + formatedAdress);
//                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();

                                }

                                Address a = new Address(Locale.UK);
                                a.setAddressLine(0, formatedAdress);
                                a.setLatitude(lat);
                                a.setLongitude(lon);


                                list = new ArrayList<Address>();
                                list.add(a);


                                //	 GetDataFromGoogle.execute(DirectionClien);

                                //list = new Geocoder(mContext, Locale.UK).getFromLocation(lat, lon, mMaxResults);
                                //	list=formatedAdress;
                                //list.add((Address) formatedAdress);
                            } else if (objects.length >= 1 && objects[0] instanceof String) {
                                String location = (String) objects[0];
                                list = new Geocoder(mContext, Locale.UK).getFromLocationName(location, mMaxResults);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            final List<Address> result = list;
                            if (result != null && result.size() > 0) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mSetResultInterface != null)
                                            mSetResultInterface.onGetResult(result, error);
                                    }
                                });
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mSetResultInterface != null)
                                            mSetResultInterface.onGetResult(null, error);
                                    }
                                });
                            }

                        }
                    }
                }, "GeoCoderRunner").start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GeocoderHelperClass setResultInterface(SetResult result) {
        mSetResultInterface = result;
        return this;
    }

//    public interface SetResult {
//        public abstract void onGetResult(List<Address> list);
//    }

    public interface SetResult {
        public abstract void onGetResult(List<Address> list, String error);
    }
}
*/
