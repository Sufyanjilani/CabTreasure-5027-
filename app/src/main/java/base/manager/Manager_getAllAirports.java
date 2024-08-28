/*
package base.manager;

import static android.view.View.VISIBLE;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.widget.ProgressBar;

import com.eurosoft.customerapp.R;
import com.google.gson.Gson;
import com.support.parser.PropertyInfo;
import com.support.parser.SoapHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.activities.Activity_SearchAddressNew;
import base.models.LocAndField;
import base.models.ShareTracking;
import base.utils.CommonVariables;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Manager_getAllAirports extends AsyncTask<String, Void, List<LocAndField>> {

        private SweetAlertDialog mDialog;
         private   Context context;
        public Manager_getAllAirports(Context context){
            this.context =context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                try {
                    mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);

                    mDialog.setTitleText("Getting Airports");
                    mDialog.setContentText("Please wait..");
                    mDialog.setCancelable(false);
                    mDialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }// End onPreExecute()


        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onPostExecute(List<LocAndField> result) {
            super.onPostExecute(result);


            try {

                if (result != null && result.size() <= 0) {

                    List<LocAndField> airPortlistLocel = dbhelper.getAirports();

                    int drawbleID = 0;
                    mDatabaseOperations.removeAirports();
                    mDatabaseOperations.insertAirports(airPortlistLocel);

                    ArrayList<LocAndField> airPorList = new ArrayList(airPortlistLocel);
                    recentListAdapter = new Activity_SearchAddressNew.RecentListAdapter(getActivity(), airPorList, R.drawable.ic_shopping);
                    recentListRv.setAdapter(recentListAdapter);
                    progressBar.setVisibility(ProgressBar.INVISIBLE);


                } else if (result != null && result.size() > 0) {
                    int drawbleID = 0;
                    mDatabaseOperations.removeAirports();
                    mDatabaseOperations.insertAirports(result);

//                    notfound.setVisibility(View.GONE);

                    sp.edit().putString("isAirportLoaded", "1").commit();

                    ArrayList<LocAndField> resultsArrylist = new ArrayList<LocAndField>(result);
                    recentListAdapter = new Activity_SearchAddressNew.RecentListAdapter(getActivity(), resultsArrylist, R.drawable.ic_shopping);
                    recentListRv.setAdapter(recentListAdapter);

                    recentListRv.setVisibility(VISIBLE);

                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }

                stopcallbacks = false;

                progressBar.setVisibility(ProgressBar.INVISIBLE);

                if (mDialog != null) {
                    mDialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
                stopcallbacks = false;
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        }


        @Override
        protected List<LocAndField> doInBackground(String... params) {
            try {
                ToSearch = params[0];

                String AddressVarName = "LocationName";
                ArrayList<LocAndField> ListFields = new ArrayList<LocAndField>();

                if (ListFields.isEmpty()) {
                    try {
                        String response = null;
                        String deviceid = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                        ShareTracking obj = new ShareTracking();
                        obj.defaultClientId = (int) CommonVariables.clientid;
                        obj.uniqueValue = CommonVariables.clientid + "4321orue";
                        obj.UniqueId = deviceid;
                        obj.DeviceInfo = "Android";
                        obj.CustomerId = userModel.getUserServerID();

                        final String json_String = new Gson().toJson(obj);
                        response = new SoapHelper.Builder(CommonVariables.SERVICE, getActivity())
                                .setMethodName("GetAirports", true)
                                .addProperty("jsonString", json_String, PropertyInfo.STRING_CLASS).getResponse();
                        locType = "";

                        if (response != null && !response.isEmpty()) {
                            JSONObject parentObject = new JSONObject(response);

                            if (parentObject.getBoolean("HasError")) {
                            } else {

                                ListFields = parseForOtherLocations(response);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return ListFields;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

*/
