package base.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Set;

import base.miscutilities.AddressModel;
import base.models.CardJudoModel;
import base.models.ExtrasModel;
import base.models.KonnectCardModel;
import base.models.LocAndField;
import base.models.Model_CardDetails;
import base.models.SettingsModel;
import base.models.Stripe_Model;
import base.newui.HomeFragment;

//import static base.miscactivities.NewBookingDetails.ADDRESS;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class SharedPrefrenceHelper {

    private Context mContext;
    public static final String DrvConnectHost = "DrvConnectHost", DrvConnectUsername = "DrvConnectUsername", DrvConnectPass = "DrvConnectPass", DrvConnectPort = "DrvConnectPort", DrvConnectEnabled = "DrvConnectEnabled";

    public SharedPrefrenceHelper(Context appContext) {
        mContext = appContext;
    }

    public void saveTipFromBookingRef(String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, "" + value);
        edit.apply();
    }

    public String getTipFromBookingRef(String key) {
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            return sp.getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void saveToSharePrefForStripeForOneCard(Stripe_Model stripe_model) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = sp.edit();

        Gson gson = new Gson();
        String json = gson.toJson(stripe_model);
        edit.putString("stripe_model", json);
        edit.apply();
    }

    public Stripe_Model getSharePrefForStripeForOneCard() {
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            Gson gson = new Gson();
            String json = sp.getString("stripe_model", "");

            Type type = new TypeToken<Stripe_Model>() {
            }.getType();

            if (json != null || json.equals("null") || json.length() > 0) {
                return gson.fromJson(json, type);
            } else {
                return new Stripe_Model();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Stripe_Model();
        }
    }



    public void saveKonnectOneCard(KonnectCardModel konnectCardModel) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = sp.edit();

        Gson gson = new Gson();
        String json = gson.toJson(konnectCardModel);
        edit.putString("konnect_model", json);
        edit.apply();
    }



    public KonnectCardModel getKonnectOneCard() {
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            Gson gson = new Gson();
            String json = sp.getString("konnect_model", "");

            Type type = new TypeToken<KonnectCardModel>() {
            }.getType();

            if (json != null || json.equals("null") || json.length() > 0) {
                return gson.fromJson(json, type);
            } else {
                return new KonnectCardModel();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new KonnectCardModel();
        }
    }

    public void deletedKonnectCard() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = sp.edit();

        edit.putString("konnect_model", null);
        edit.apply();
    }

    public void putSettingModel(Object obj) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(SettingsModel.KEY_SETTINGS_MODEL, json).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putFirstRun() {
        // AnyVehicleModel mvehicle =new AnyVehicleModel();

        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString("isFirstRun", "3").commit();
    }

    public String getFirstRun() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString("isFirstRun", "0");


    }

    public void updateSettingModel(Object obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(SettingsModel.KEY_SETTINGS_MODEL, json).commit();
    }

    public void putJudoCardModelArrayList(ArrayList<CardJudoModel> cardDetailsModelArrayList) {
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor edit = sp.edit();

            Gson gson = new Gson();
            String json = gson.toJson(cardDetailsModelArrayList);
            edit.putString("card_list_judo", json);
            edit.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CardJudoModel> getJudoCardList() {
        ArrayList<CardJudoModel> cardJudoModelArrayList;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = sp.getString("card_list_judo", "");

        Type type = new TypeToken<ArrayList<CardJudoModel>>() {
        }.getType();

        cardJudoModelArrayList = gson.fromJson(json, type);
        if (cardJudoModelArrayList == null) {
            cardJudoModelArrayList = new ArrayList<>();
        }
        return cardJudoModelArrayList;
    }

    public void putExtraList(ArrayList<ExtrasModel> cardDetailsModelArrayList) {
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor edit = sp.edit();

            Gson gson = new Gson();
            String json = gson.toJson(cardDetailsModelArrayList);
            edit.putString("card_list_extra", json);
            edit.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ExtrasModel> getExtraList() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = sp.getString("card_list_extra", "");

        Type type = new TypeToken<ArrayList<ExtrasModel>>() {
        }.getType();

        if (json != null || json.equals("null") || json.length() > 0) {
            return gson.fromJson(json, type);
        } else {
            return null;
        }
    }

    public void removeJudoCardModelArrayList() {
        try {
            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .remove("card_list_judo")
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeExtrasList() {
        try {
            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .remove("card_list_extra")
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putCardModel(Object obj) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            SettingsModel settingsModel = getSettingModel();
            PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(Model_CardDetails.KEY_Card_MODEL + settingsModel.getUserServerID(), json).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String TOKEN_RECEIPT_KEY = "Judo-SampleApp-TokenReceipt";
    private static final String TOKEN_CardJUDO_KEY = "Judo-CardJUDO-TokenReceipt";

    public void putVal(String key, boolean val) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean(key, val).commit();
    }

    public boolean getBoolVal(String key) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(key, false);
    }

    public void saveCardJudoModel(CardJudoModel receipt) {
        CardJudoModel cardJudoModel = new CardJudoModel();
        cardJudoModel.setToken(receipt.getToken());
        cardJudoModel.setConsumerToken(receipt.getConsumerToken());
        cardJudoModel.setConsumerReference(receipt.getConsumerReference());
        cardJudoModel.setEndDate(receipt.getEndDate());
        cardJudoModel.setLastFour(receipt.getLastFour());
        cardJudoModel.setCardLabel(receipt.getCardLabel());
        cardJudoModel.setReceiptid(receipt.getReceiptid());
        Object obj = cardJudoModel;
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        SettingsModel settingsModel = getSettingModel();
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(TOKEN_CardJUDO_KEY + settingsModel.getUserServerID(), json)
                .commit();
    }

    public CardJudoModel getCardJudoModel() {
        Gson gson = new Gson();
        SettingsModel settingsModel = getSettingModel();
        String json = PreferenceManager.getDefaultSharedPreferences(mContext).getString(TOKEN_CardJUDO_KEY + settingsModel.getUserServerID(), null);
        CardJudoModel obj = gson.fromJson(json, CardJudoModel.class);
        return obj;
    }

    public void removeLastReciept() {
        try {
            SettingsModel settingsModel = getSettingModel();
            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .remove(TOKEN_CardJUDO_KEY + settingsModel.getUserServerID())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveReceipt() {


    }

//    public Receipt getLastReceipt() {
//        String tokenReceiptJson = PreferenceManager.getDefaultSharedPreferences(mContext)
//                .getString(TOKEN_RECEIPT_KEY, null);
//
//        if (tokenReceiptJson != null) {
//            Gson gson = new Gson();
//            return gson.fromJson(tokenReceiptJson, Receipt.class);
//        }
//        return null;
//    }

    public Model_CardDetails getCardModel() {

        Model_CardDetails obj = null;
        try {
            Gson gson = new Gson();
            SettingsModel settingsModel = getSettingModel();
            String json = PreferenceManager.getDefaultSharedPreferences(mContext).getString(Model_CardDetails.KEY_Card_MODEL + settingsModel.getUserServerID(), "");
            obj = gson.fromJson(json, Model_CardDetails.class);
            if (obj == null) {
                return new Model_CardDetails();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void putLocAndFieldModel(String key, LocAndField locAndField) {
        Gson gson = new Gson();
        String json = gson.toJson(locAndField);
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(key, json).commit();
    }

    public LocAndField getLocAndFieldModel(String key) {
        LocAndField locAndField = new LocAndField();
        try {
            String json = PreferenceManager.getDefaultSharedPreferences(mContext).getString(key, "");
            JSONObject jsonObject = new JSONObject(json);
            locAndField.setField(jsonObject.getString("Field"));
            locAndField.setLat(jsonObject.getString("Lat"));
            locAndField.setLon(jsonObject.getString("Lon"));
            locAndField.setLocationType(jsonObject.getString("locationType"));
            locAndField.setDoorNo(jsonObject.getString("DoorNo"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locAndField;
    }

    public void removeAddressModel(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPreferences.edit().remove(key).commit();
    }

    // ASAP
    public void putRefNoAsapValue(String keyRefNo, boolean isAsap) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean("asap_" + keyRefNo, isAsap).commit();
    }

    public boolean getRefNoAsapValue(String keyRefNo) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("asap_" + keyRefNo, false);
    }

    public void removeRefNoAsapValue(String keyRefNo) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPreferences.edit().remove("asap_" + keyRefNo).commit();
    }

    public void removeCardModel() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPreferences.edit().remove(Model_CardDetails.KEY_Card_MODEL).commit();
    }

    public LatLng getDriverLastLocation() {

        Gson gson = new Gson();
        String json = PreferenceManager.getDefaultSharedPreferences(mContext).getString("LastLatLng", "");

        return gson.fromJson(json, LatLng.class);

    }

    public void removeDriverLastLocation() {


//		editor;
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().remove("LastLatLng").apply();

    }

    SharedPreferences preferences;

    public void setDriverLastLocation(Object obj) {

        // AnyVehicleModel mvehicle =new AnyVehicleModel();
//		SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
//		editor;
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString("LastLatLng", json).apply();
//		editor.apply();
    }

    public SettingsModel getSettingModel() {
        SettingsModel obj = new SettingsModel();
        try {
            Gson gson = new Gson();
            String json = PreferenceManager.getDefaultSharedPreferences(mContext).getString(SettingsModel.KEY_SETTINGS_MODEL, "");
            obj = gson.fromJson(json, SettingsModel.class);
            if (obj == null) {
                obj = new SettingsModel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

/*
    public void putSettingModel(String key, Object obj) {

        // AnyVehicleModel mvehicle =new AnyVehicleModel();
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key, json);
        editor.apply();
    }
*/

    public void putFromTemp(Object obj) {

        // AnyVehicleModel mvehicle =new AnyVehicleModel();
        SharedPreferences.Editor editor = preferences.edit();
        String txt = (String) obj;

        editor.putString(HomeFragment.KEY_FROM_LOCATION, txt);
        editor.apply();
    }

    public void putVal(String key, String val) {

        // AnyVehicleModel mvehicle =new AnyVehicleModel();

//		SharedPreferences.Editor editor = preferences.edit();
//
//
//		editor.putString(key, val);
//		editor.apply();
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(key, val).commit();
    }

    public int getIntVal(String key) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(key, 0);
    }

    public void putIntVal(String key, int val) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putInt(key, val).commit();
    }

    public String getVal(String key) {


//		String obj =(Object) preferences.getString(HomeFragment.KEY_TO_LOCATION, "");
//		AddressModel json =(AddressModel) obj;

        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(key, "");

    }

    public AddressModel getFromTemp(String key) {


        Object obj = preferences.getString(HomeFragment.KEY_FROM_LOCATION, "");
        AddressModel json = (AddressModel) obj;

        return json;

    }

    public void putViaTemp(ArrayList<String> viaaddresses) {


        SharedPreferences.Editor editor = preferences.edit();


        editor.putStringSet(HomeFragment.KEY_VIA_LOCATION, (Set<String>) viaaddresses);
        editor.apply();


    }

    public SettingsModel getSettingModel(String key) {

        Gson gson = new Gson();
        String json = preferences.getString(key, "");
        SettingsModel obj = gson.fromJson(json, SettingsModel.class);
        if (obj == null) {
            return new SettingsModel();
        }
        return obj;
    }

    public void putStripeCustomerId(String CustomerId) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString("StripeCustomerId", CustomerId).commit();
    }

    public String getStripeCustomerId() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString("StripeCustomerId", "");
    }

    public void removeStripeCustomerId() {
        try {
            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .remove("StripeCustomerId")
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeToSharePrefForStripeForOneCard() {
        try {
            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .remove("stripe_model")
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLocAndFieldForSavedBookings(String bookingRefKey, ArrayList<LocAndField> locAndFields) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(locAndFields);
            edit.putString(CommonVariables.localid + "_" + bookingRefKey + "_LocAndField", json);
            edit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LocAndField> getLocAndFieldFromSavedBookings(String bookingRefKey) {
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            Gson gson = new Gson();
            String json = sp.getString(CommonVariables.localid + "_" + bookingRefKey + "_LocAndField", "");
            if (json != null && json.length() > 0) {
                Type type = new TypeToken<ArrayList<LocAndField>>() {
                }.getType();
                return gson.fromJson(json, type);
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}