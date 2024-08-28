package base.fragments;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eurosoft.customerapp.R;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import com.tfb.fbtoast.FBToast;

import org.json.JSONArray;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import base.utils.CommonVariables;
import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.models.ShareTracking;
import base.models.PromoModel;
import base.models.SettingsModel;
import base.utils.SharedPrefrenceHelper;
import base.models.ParentPojo;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.CLIPBOARD_SERVICE;

public class Fragment_Promo extends Fragment implements OnClickListener {

    private static final String REGEX_NUMBER_SEPERATORS = "[\\,\\\\\\/\\-]";

    public static final String REFERENCE = UUID.randomUUID().toString();
    private SharedPreferences sp;
    private SharedPrefrenceHelper spHelper;
    RecyclerView recyclerView;
    ArrayList<PromoModel> promoList = new ArrayList<>();
    DatabaseOperations databaseOperations;
    TextView promotionTitleLabel;
    TextView notAvail;
    ParentPojo p = new ParentPojo();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.promo_activity, container, false);

        spHelper = new SharedPrefrenceHelper(getActivity());
        settingsModel = spHelper.getSettingModel();
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());


        promotionTitleLabel = ret.findViewById(R.id.promotionTitleLabel);
        promotionTitleLabel.setText(p.getPromotions());

        notAvail = ret.findViewById(R.id.notAvail);
        notAvail.setText(p.getNoPromotionsAvailable());

        ret.findViewById(R.id.imgBack).setOnClickListener(this);
        databaseOperations = new DatabaseOperations(new DatabaseHelper(getActivity()));
        recyclerView = (RecyclerView) ret.findViewById(R.id.promolist);

        ret.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        new GetPromotions().execute();
        return ret;

    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor edit = sp.edit();
        //	edit.putString(CommonVariables.USerLogin, "");

        if (v.getId() == R.id.imgBack) {

            ((Fragment_Main) getActivity()).toggleDrawer();
            getFragmentManager().popBackStack();
        }


    }

    SettingsModel settingsModel;

    private class GetPromotions extends AsyncTask<String, Void, String> {
        private String METHOD_NAME = "GetPromotion";


        private static final String KEY_DEFAULT_CLIENT_ID = "defaultclientId";
        private static final String KEY_HASHKEY = "hashKey";

        private static final String Booking_information = "jsonString";
        private static final String HASHKEY_VALUE = "4321orue";

        private SweetAlertDialog mDialog;


        public GetPromotions() {

        }

        String token = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                mDialog.setTitleText("Getting Details");
                mDialog.setContentText("Please wait..");
                mDialog.setCancelable(false);
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//			Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
            if (result != null && !result.isEmpty()) {
                try {
                    JSONObject parentObject = new JSONObject(result);

                    if (parentObject.getBoolean("HasError")) {
                        FBToast.errorToast(getActivity(), parentObject.getString("Message"), FBToast.LENGTH_SHORT);
                    } else {

                        JSONObject parentObject2 = new JSONObject(parentObject.getString("Data"));
                        JSONArray jsonArray = new JSONArray(parentObject2.getString("JobPromotionlist"));
                        databaseOperations.deletePromo();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject promoObj = jsonArray.getJSONObject(i);
                            if (promoObj != null && !promoObj.equals("") && !promoObj.equals("PromotionCode")) {
//                                    operations.deletePromo();
                                databaseOperations.insertPromoCode(
                                        promoObj.getString("PromotionCode"),
                                        promoObj.getString("PromotionTitle"),
                                        promoObj.getString("PromotionMessage"),
                                        promoObj.getString("PromotionStartDateTime"),
                                        promoObj.getString("PromotionEndDateTime"),
                                        promoObj.getString("DiscountTypeId"),
                                        promoObj.getString("Charges"),
                                        promoObj.getString("PromotionId"),
                                        promoObj.getString("Totaljourney"),
                                        promoObj.getString("Used"),
                                        promoObj.getString("PromotionTypeID"),
                                        promoObj.getString("MaximumDiscount"),
                                        promoObj.getString("MinimumFare"));
                                PromoModel promoModel = new PromoModel();
                                promoModel.setPromoCode(promoObj.getString("PromotionCode"));
                                promoModel.setTitle(promoObj.getString("PromotionTitle"));
                                promoModel.setMsg(promoObj.getString("PromotionMessage"));
                                promoModel.setStrtDate(promoObj.getString("PromotionStartDateTime"));
                                promoModel.setEndDate(promoObj.getString("PromotionEndDateTime"));
                                promoModel.setPromType(promoObj.getString("DiscountTypeId"));
                                promoModel.setPROMOTIONTYPEID(promoObj.getString("PromotionTypeID"));
                                promoModel.setPromoValue(promoObj.getString("Charges"));
                                promoModel.setPromoServerId(promoObj.getString("PromotionId"));
                                promoModel.setTotal(promoObj.getString("Totaljourney"));
                                promoModel.setUsed(promoObj.getString("Used"));
                                promoModel.setMaxDiscount(promoObj.optString("MaximumDiscount"));
                                promoModel.setMinFares(promoObj.optString("MinimumFare"));
                                promoList.add(promoModel);

//
                            }

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        if (promoList.size() > 0) {
                            recyclerView.setAdapter(new PromoAdapter());
                            recyclerView.setVisibility(View.VISIBLE);
                            getView().findViewById(R.id.notAvail).setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            getView().findViewById(R.id.notAvail).setVisibility(View.VISIBLE);
                        }

                    }
                } catch (Exception e) {
                    FBToast.errorToast(getActivity(), "No Promotions Available!", FBToast.LENGTH_SHORT);
                }
            } else {
                FBToast.errorToast(getActivity(), "Please check your internet and check again later", FBToast.LENGTH_SHORT);
            }

            if (mDialog != null) {

                mDialog.dismiss();
            }

        }

        @Override
        protected String doInBackground(String... params) {


            try {
              ;
                String deviceid = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

                HashMap<String, Object> appUserMap = new HashMap<>();
                String promotionJson = "{\"UniqueId\":\"" + deviceid + "\"," +
                        "\"hashKey\":\"" + HASHKEY_VALUE + "\"," +
                        "\"DeviceInfo\":\"Android\"," +
                        "\"CustomerId\":\"" + settingsModel.getUserServerID() + "\"," +
                        "\"defaultClientId\":\"" + CommonVariables.clientid + "\"," +
                        "\"Email\":\"" + settingsModel.getEmail() + "\"}";
                appUserMap.put(Booking_information, promotionJson);
                appUserMap.put("Token", CommonVariables.TOKEN);

                Gson gson = new Gson();
                String jsonrequest = gson.toJson(appUserMap);

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonrequest);
                Request request = new Request.Builder()
                        .url(CommonVariables.BASE_URL + "GetPromotion")
                        .post(body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();


                } catch (Exception e) {
                    e.printStackTrace();
                    return e.getMessage();
                }
            }catch (Exception ex){
                ex.printStackTrace();
                return  "";
            }
        }

    }

    class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.PromoViewHolder> {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.getDefault());
        SettingsModel settingsModel;

        PromoAdapter() {
            settingsModel = spHelper.getSettingModel();
        }

        @NonNull
        @Override
        public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.promo_child, viewGroup, false);
            PromoViewHolder promoViewHolder = new PromoViewHolder(view);
            return promoViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PromoViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
            try {
//				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                viewHolder.title.setText(promoList.get(i).getTitle());
                viewHolder.msg.setText(promoList.get(i).getMsg());
                if (promoList.get(i).gettotal().equals("0") || promoList.get(i).gettotal().equals("")) {
                    viewHolder.journeyLimit.setVisibility(View.GONE);
                } else {
                    viewHolder.journeyLimit.setText("Journeys Left : " + promoList.get(i).getused() + "/" + promoList.get(i).gettotal());
                }
                viewHolder.code.setText("Code : " + promoList.get(i).getPromoCode());
                viewHolder.promoEndDate.setText("Valid till " + promoList.get(i).getEndDate());
                viewHolder.promoStartDate.setText("Starts From " + promoList.get(i).getStrtDate());
                if (promoList.get(i).getMaxDiscount() != null && !promoList.get(i).getMaxDiscount().equals("") && !promoList.get(i).getMaxDiscount().equals("null") && !promoList.get(i).getMaxDiscount().equals("0.0") && !promoList.get(i).getMaxDiscount().equals("0.00")) {
                    viewHolder.maxDiscount.setText("Maximum Discount : \u00A3" + promoList.get(i).getMaxDiscount());
                    viewHolder.maxDiscount.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.maxDiscount.setVisibility(View.GONE);
                }
                if (promoList.get(i).getMinFares() != null && !promoList.get(i).getMinFares().equals("") && !promoList.get(i).getMinFares().equals("null") && !promoList.get(i).getMinFares().equals("0.0") && !promoList.get(i).getMinFares().equals("0.00")) {
                    viewHolder.minFare.setText("Minimum fares to avail promotion : \u00A3" + promoList.get(i).getMinFares());
                    viewHolder.minFare.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.minFare.setVisibility(View.GONE);
                }

                viewHolder.code.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("promcode_", promoList.get(i).getPromoCode());
                        clipboard.setPrimaryClip(clip);
                        FBToast.successToast(getActivity(), "Code copied!", FBToast.LENGTH_SHORT);
                    }
                });

//				Date d = sdf.parse(promoList.get(i).getEndDate());
//				long Extime=d.getTime();
//				long CurrTime=new Date().getTime();
//				long diff=CurrTime-Extime;
//				if(diff<0||!promoList.get(i).gettotal().equals(promoList.get(i).getused())){
////				    viewHolder.pr_apply.setVisibility(View.VISIBLE);
//					LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,5);
//
//					viewHolder.title.setLayoutParams(params);
//					viewHolder.label.setVisibility(View.GONE);
//					if(settingsModel.getPromoCode().equals(promoList.get(i).getPromoCode())){
//						viewHolder.pr_apply.setText("APPLIED");
//						viewHolder.pr_apply.setClickable(false);
//						viewHolder.pr_apply.setBackgroundColor(getResources().getColor(R.color.login_field_shadow));
//					}else{
//						viewHolder.pr_apply.setText("APPLY");
//
//					}
//				}else{
//
//					viewHolder.pr_apply.setVisibility(View.GONE);
//					viewHolder.label.setVisibility(View.VISIBLE);
//					LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,4);
//
//					viewHolder.title.setLayoutParams(params);
//				}
//				viewHolder.pr_apply.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						if(!settingsModel.getPromoCode().equals(promoList.get(i).getPromoCode())) {
//							settingsModel.setPromoCode(promoList.get(i).getPromoCode());
//							spHelper.putSettingModel(settingsModel);
//							Toast.makeText(getActivity(), "Promotion Code [" + promoList.get(i).getPromoCode() + "] is applied", Toast.LENGTH_LONG).show();
//						}
//					}
//				});
//				Log.e("tag",""+diff);
            } catch (Exception ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }

        }

        @Override
        public int getItemCount() {
            return promoList.size();
        }

        class PromoViewHolder extends RecyclerView.ViewHolder {
            TextView title, msg, label, code, pr_apply, promoEndDate, promoStartDate, journeyLimit, maxDiscount, minFare;

            public PromoViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.promotitle);
                msg = itemView.findViewById(R.id.promoMsg);
                label = itemView.findViewById(R.id.statusLabel);
                code = itemView.findViewById(R.id.promoCode);
                pr_apply = itemView.findViewById(R.id.apply_code);
                promoEndDate = itemView.findViewById(R.id.promoEndDate);
                promoStartDate = itemView.findViewById(R.id.promostrtDate);
                journeyLimit = itemView.findViewById(R.id.journeyLimit);
                maxDiscount = itemView.findViewById(R.id.maxDiscount);
                minFare = itemView.findViewById(R.id.minFares);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

}