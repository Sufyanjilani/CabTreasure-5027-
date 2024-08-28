package base.utils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.android.gms.maps.model.JointType.ROUND;
import static base.utils.CommonVariables.Currency;
import static base.utils.CommonVariables.VALID_EMAIL_ADDRESS_REGEX;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;


import com.amalbit.trail.OverlayPolyline;
import com.amalbit.trail.RouteOverlayView;
import com.eurosoft.customerapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.models.LocAndField;
import base.models.PaymentGateway;
import base.models.SettingsModel;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CommonMethods {
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private static CommonMethods instance;
    public Marker lineFromMarker, lineToMarker;

    public static String getAppVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "N/A";
        }
    }

    public static Double checkIfHasNullForDouble(String key) {
        try {
            key = checkIfHasNullForString(key);
            if (key.equals("")) {
                return Double.valueOf(0);
            } else {
                return Double.valueOf(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Double.valueOf(0);
        }
    }

    public static String checkIfHasNullForString(String key) {
        if (key == null) {
            return "";
        } else if (key.equals("")) {
            return "";
        } else if (key.equals("null")) {
            return "";
        } else {
            return key;
        }
    }

    public static CommonMethods getInstance() {
        if (instance == null) {
            return instance = new CommonMethods();
        }
        return instance;
    }

    public static int extractDigit(String input) {

        try {
            String regex = "\\d+";

            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            } else {
                return 0;
            }
        }catch (Exception ex){
            return  0;
        }
    }

    public void setDarkAndNightColor(Activity activity) {
        int nightModeFlags = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        Window window = activity.getWindow();
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
//                getWindow().setStatusBarColor(Color.parseColor("#2C2C2C")); // gray
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
                window.setStatusBarColor(ContextCompat.getColor(activity, R.color.color_gray_and_footer_inverse));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_NO:
//                getWindow().setStatusBarColor(Color.parseColor("#BC9254")); // footer
                window.setStatusBarColor(ContextCompat.getColor(activity, R.color.color_gray_and_footer_inverse));// set status background white
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
//                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_bg_white));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
    }

    public void setDarkAndNightColorBlackWhite(Activity activity) {
        int nightModeFlags = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        Window window = activity.getWindow();
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
//                getWindow().setStatusBarColor(Color.parseColor("#2C2C2C")); // gray
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
                window.setStatusBarColor(ContextCompat.getColor(activity, R.color.color_white_inverse));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_NO:
//                getWindow().setStatusBarColor(Color.parseColor("#BC9254")); // footer
                window.setStatusBarColor(ContextCompat.getColor(activity, R.color.color_white_inverse));// set status background white
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
//                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_bg_white));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
    }

    /// New Judo Work
    public PaymentGateway getPaymentGatway(String appName, SharedPreferences sp, SettingsModel settingsModel) {
        PaymentGateway payment = new PaymentGateway(
                "" + CommonVariables.clientid,
                "" + settingsModel.getUserServerID(),
                "" + settingsModel.getName(),
                "" + settingsModel.getMobile(),
                "" + settingsModel.getEmail(),
                1.01,
                1.01,
                "" + Currency,
                "" + sp.getString(Config.JudoId, ""),
                "" + sp.getString(Config.JudoToken, ""),
                "" + sp.getString(Config.JudoSecret, ""),
                false,
                true,
                "" + UUID.randomUUID().toString(),
                appName + " - Register Card"
        );
        return payment;
    }

    public void setDarkAndNightColorGreyWhite(Activity activity) {
        int nightModeFlags = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        Window window = activity.getWindow();
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
//                getWindow().setStatusBarColor(Color.parseColor("#2C2C2C")); // gray
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
                window.setStatusBarColor(ContextCompat.getColor(activity, R.color.color_card_inverse));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_NO:
//                getWindow().setStatusBarColor(Color.parseColor("#BC9254")); // footer
                window.setStatusBarColor(ContextCompat.getColor(activity, R.color.color_card_inverse));// set status background white
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
//                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_bg_white));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
    }

    private LatLng getLatLng(LocAndField locAndField) {
        LatLng latLng = new LatLng(Double.parseDouble(locAndField.getLat()), Double.parseDouble(locAndField.getLon()));
        return latLng;
    }

    public void setMarkers(Context context, String isRebook, String eta, ArrayList<LocAndField> locAndFieldArrayList, GoogleMap mMap, boolean isrouteComplete) {
        try {
            lineFromMarker = setFromMarkerLabels(context, isRebook, eta, locAndFieldArrayList.get(0), mMap, isrouteComplete);

            lineToMarker = setToMarkerLabels(context, isRebook, locAndFieldArrayList.get(locAndFieldArrayList.size() - 1), mMap, isrouteComplete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void _showCurvedPolyline(Context context, String isRebook, String eta, ArrayList<LocAndField> locAndFieldArrayList, GoogleMap mMap, RouteOverlayView mapOverlayView, boolean isrouteComplete) {

        // SET FROM  AND  TO  MARKER
        setMarkers(context, isRebook, eta, locAndFieldArrayList, mMap, isrouteComplete);

        // SET VIA
        try {
            if (locAndFieldArrayList.size() > 2) {
                for (int i = 1; i < locAndFieldArrayList.size() - 1; i++) {
                    LocAndField locAndField = locAndFieldArrayList.get(i);
                    setVia(context, getLatLng(locAndField), mMap, locAndField);
                }

                // Pickup(0) to Via 1 (1)
                try {
                    createPolyline(getLatLng(locAndFieldArrayList.get(0)), getLatLng(locAndFieldArrayList.get(1)), mMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Via 1(2) to Via 2(3)
                try {
                    createPolyline(getLatLng(locAndFieldArrayList.get(1)), getLatLng(locAndFieldArrayList.get(2)), mMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Via 2(2) to Drop(3)
                try {
                    createPolyline(getLatLng(locAndFieldArrayList.get(2)), getLatLng(locAndFieldArrayList.get(3)), mMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // CREATE ARC
        _setArc(context, isRebook, locAndFieldArrayList, mMap, mapOverlayView);

        // SET CAMERA UPDATE
        try {
            zoom2((Activity) context, mMap, isRebook, locAndFieldArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createPolyline(LatLng start, LatLng end, GoogleMap mMap) {
        List<LatLng> latLngList = new ArrayList<>();
        latLngList.add(start);
        latLngList.add(end);

        List<PatternItem> pattern = Arrays.asList(new Dash(30), new Gap(20));

        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.GRAY);
        polyOptions.startCap(new RoundCap());
        polyOptions.endCap(new RoundCap());
        polyOptions.jointType(ROUND);
        polyOptions.width(5);
        polyOptions.addAll(latLngList);
        polyOptions.pattern(pattern);
        mMap.addPolyline(polyOptions);
    }


    public List<LatLng> getList(ArrayList<LocAndField> arrayList) {
        List<LatLng> a = new ArrayList<>();
        a.add(getLatLng(arrayList.get(0)));
        a.add(new LatLng(0, 0));
        a.add(getLatLng(arrayList.get(arrayList.size() - 1)));
        return a;
    }

    public void _setArc(Context context, String isRebook, ArrayList<LocAndField> locAndFieldArrayList, GoogleMap mMap, RouteOverlayView mapOverlayView) {
        if (isRebook.equals("JobTracking") || isRebook.equals("JobDetail")) {
            mMap.setOnMapLoadedCallback(() -> {
                try {
                    createRouteBuilder(context, locAndFieldArrayList, mMap, mapOverlayView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            if (locAndFieldArrayList.size() > 2) {
                try {
                    createRouteBuilder(context, locAndFieldArrayList, mMap, mapOverlayView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mMap.setOnMapLoadedCallback(() -> {
                    try {
                        createRouteBuilder(context, locAndFieldArrayList, mMap, mapOverlayView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private void createRouteBuilder(Context mContext, ArrayList<LocAndField> arrayList, GoogleMap mMapDirections, RouteOverlayView mapOverlayView) {
        try {
           /* new Route.Builder(mapOverlayView)
                    .setRouteType(RouteOverlayView.RouteType.ARC)
                    .setCameraPosition(mMapDirections.getCameraPosition())
                    .setProjection(mMapDirections.getProjection())
                    .setLatLngs(getList(arrayList))
                    .setBottomLayerColor(ContextCompat.getColor(mContext, R.color.footerBack)) // footerBack
                    .setTopLayerColor(ContextCompat.getColor(mContext, R.color.color_black_inverse)) // color_black_inverse
                    .setRouteShadowColor(ContextCompat.getColor(mContext, R.color.invisible))
                    .create();*/
            OverlayPolyline arcOverlayPolyline = new OverlayPolyline.Builder(mapOverlayView)
                    .setRouteType(RouteOverlayView.RouteType.ARC)
                    .setCameraPosition(mMapDirections.getCameraPosition())
                    .setProjection(mMapDirections.getProjection())
                    .setLatLngs(getList(arrayList))
                    .setBottomLayerColor(ContextCompat.getColor(mContext, R.color.footerBack)) // color_inverse_black_footerBack
                    .setTopLayerColor(ContextCompat.getColor(mContext, R.color.color_gray_and_footer_inverse)) // color_black_inverse
                    .setRouteShadowColor(ContextCompat.getColor(mContext, R.color.invisible))
                    .create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVia(Context context, LatLng latLng, GoogleMap mMap, LocAndField locAndField) {
        MarkerOptions via = new MarkerOptions();
//        via.icon(BitmapDescriptorFactory.fromResource(R.drawable.via));
        via.icon(BitmapDescriptorFactory.fromBitmap(createCustomVia(context, locAndField)));
        via.position(latLng);
        Marker marker = mMap.addMarker(via);
        marker.setTag(locAndField.getField());
    }

    public Marker setFromMarkerLabels(Context context, String fromScreen, String eta, LocAndField locAndField, GoogleMap mMap, boolean isrouteComplete) {


        float from_x = 0.03f; // right|left
        float from_y = 0.12f; // top|bottom


        MarkerOptions from = new MarkerOptions();
        from.position(getLatLng(locAndField));
        from.anchor(from_x, from_y);

        String subString = locAndField.getField();
        try {
            if (subString.length() > 40) {
                subString = subString.substring(0, 40);
                subString = subString + "...";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (locAndField.getField().equals("")) {

                Bitmap fromBitmap = createCustomPickDropMarker(context, subString, "" + eta, true, fromScreen);

                BitmapDescriptor fromBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(fromBitmap);


                from.icon(fromBitmapDescriptor);
            }
            else {

               Bitmap  fromBitmap = createCustomPickDropMarker(context, locAndField.getField(), "" + eta, true ,fromScreen);
/*
                Matrix matrix = new Matrix();
                matrix.postRotate(240);*/
                //matrix.preScale(1.0f, -1.0f);
         /*       Bitmap scaledBitmap = Bitmap.createScaledBitmap(fromBitmap, fromBitmap.getWidth(), fromBitmap.getHeight(), true);

                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

*/

                BitmapDescriptor fromBitmapDescriptor =  BitmapDescriptorFactory.fromBitmap(fromBitmap);


                from.icon(fromBitmapDescriptor);
            }

            if (fromScreen.equals("JobTracking") || fromScreen.equals("JobDetail")) {
                from.anchor(from_x + 0.5f, from_y + 0.4f);
                Marker marker = mMap.addMarker(from);
                marker.setTag("pickup");
                return marker;
            }

            if (isrouteComplete) {
                Marker marker = mMap.addMarker(from);
                marker.setTag("pickup");
                return marker;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap createFlippedBitmap(Bitmap source, boolean xFlip, boolean yFlip) {
        Matrix matrix = new Matrix();
        matrix.postScale(xFlip ? -1 : 1, yFlip ? -1 : 1, source.getWidth() / 2f, source.getHeight() / 2f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public TextView etaTv;

    public Bitmap createCustomPickDropMarker(Context context, String title, String eta, boolean isPickup, String fromScreen) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_marker_pick_drop, null);
        TextView titleTv = marker.findViewById(R.id.titleTv);
        etaTv = marker.findViewById(R.id.etaTv);
        TextView  totalTimeForArrival = marker.findViewById(R.id.totalTimeForArrival);
        TextView  subname = marker.findViewById(R.id.subname);

        RelativeLayout container = marker.findViewById(R.id.container);
        LinearLayout  llTime = marker.findViewById(R.id.llTime);

        ImageView pickUp = marker.findViewById(R.id.pickUp);
        ImageView dropOff = marker.findViewById(R.id.dropOff);

        title = title.replace("Already at airport.", "");

        try{

            String[] words  = title.toLowerCase().split(" ");
            String carNamesCap ="";
            for(String s : words){
                carNamesCap = carNamesCap + " "+ capitizeString(s);
            }
            titleTv.setText(carNamesCap);
        }catch (Exception ex){
            titleTv.setText(title);
        }



//        titleTv.setText(title);

        String et = "";

        if (isPickup) {
            llTime.setVisibility(VISIBLE);
            et = eta;

            try {
                String timeUnit = "Mins";
                String[] ets = et.split(" ");
                for (int i = 0; ets.length > i; i++) {
                    if (i == 0) {

                        et = ets[i];

                    } else {
                        if (!ets[i].equals(" ")) {
                            timeUnit = ets[i];
                        }


                    }
                }
                totalTimeForArrival.setText(et);
                subname.setText(timeUnit);
            }catch (Exception ex){
                totalTimeForArrival.setText(et);
                subname.setText("");
            }

//            if(et != null || !et.equals("")){
//                totalTimeForArrival.setText(et);
//            }

            //marker.findViewById(R.id.arr).setVisibility(GONE);

            pickUp.setVisibility(VISIBLE);
            dropOff.setVisibility(GONE);
            etaTv.setText("ETA " + et.toLowerCase());
            etaTv.setVisibility((et.length() != 0) ? VISIBLE : GONE);
        } else {
            llTime.setVisibility(GONE);
            et = "";
            dropOff.setVisibility(VISIBLE);
            pickUp.setVisibility(GONE);
            etaTv.setVisibility((et.length() != 0) ? VISIBLE : GONE);
        }

        if (fromScreen.equals("JobTracking") || fromScreen.equals("JobDetail")) {
            container.setVisibility(View.INVISIBLE);
            try {
               marker.findViewById(R.id.cvRoot).setVisibility(GONE);
            }catch (Exception ex){

            }
        } else {
            container.setVisibility(VISIBLE);

            try {
                marker.findViewById(R.id.cvRoot).setVisibility(VISIBLE);
            }catch (Exception ex){

            }
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
       // bitmap = shadarBitmap(bitmap);

        Canvas canvas = new Canvas(bitmap);

        //shadow

       int color  = context.getResources().getColor(R.color.app_black);
//         Paint mShadow = new Paint();
//        mShadow.setShadowLayer(10.0f, 5.0f, 5.0f, color);
//
//        mShadow.setColor(color);
//        canvas.drawBitmap(bitmap, 10.0f, 10.0f, mShadow);


        marker.draw(canvas);
        return bitmap;
    }


    public Bitmap shadarBitmap(Bitmap bm) {

        Bitmap circleBitmap = Bitmap.createBitmap(bm.getWidth(),
                bm.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader(bm, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);


        return circleBitmap;
    }





    public Marker setToMarkerLabels(Context context, String fromScreen, LocAndField locAndField, GoogleMap mMap, boolean isrouteComplete) {
        float to_x = 0.03f; // top | bottom
        float to_y = 0.12f; // left|right

        MarkerOptions to = new MarkerOptions();
        to.anchor(to_x, to_y);
        to.position(getLatLng(locAndField));

        String TosubString = locAndField.getField();
        try {
            if (TosubString.length() > 40) {
                TosubString = TosubString.substring(0, 40);
                TosubString = TosubString + "...";
            }
        } catch (Exception e) {
            e.printStackTrace();
            TosubString = "";
        }

        if (locAndField.getField().equals("")) {
            to.icon(BitmapDescriptorFactory.fromBitmap(createCustomPickDropMarker(context, TosubString, "", false, fromScreen)));
        } else {
            to.icon(BitmapDescriptorFactory.fromBitmap(createCustomPickDropMarker(context, locAndField.getField(), "", false, fromScreen)));
        }
        try {

//            to.anchor(0.06f, 0.06f);
            if (fromScreen.equals("JobTracking") || fromScreen.equals("JobDetail")) {
                to.anchor(to_x + 0.5f, to_x + 0.4f);
                Marker marker = mMap.addMarker(to);
                marker.setTag("drop");
                return marker;
            }

            if (isrouteComplete) {
                Marker marker = mMap.addMarker(to);
                marker.setTag("drop");
                return marker;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void zoom2(Activity context, GoogleMap mMap, String isRebook, ArrayList<LocAndField> locAndFieldArrayList) {
        try {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            float percentTodivide = 0.30f;
            int ancho = size.x;
            if (size.y >= 2000) {
                percentTodivide = 0.55f;
            }
            int alto = (int) (size.y - (size.y * percentTodivide));
            if (alto > 1500) {
                alto = 1500;
            }
            List<LatLng> copiedPoints = new ArrayList<LatLng>();
            copiedPoints.add(getLatLng(locAndFieldArrayList.get(0)));
            copiedPoints.add(getLatLng(locAndFieldArrayList.get(locAndFieldArrayList.size() - 1)));
            if (isRebook.equals("JobDetail")) {
//                alto = (int) (size.y - (size.y * 0.50));
                centerIncidentRouteOnMap(copiedPoints, ancho, alto, 300, mMap);
            } else if (isRebook.equals("JobTracking")) {
//                alto = (int) (size.y - (size.y * 0.50));
                centerIncidentRouteOnMap(copiedPoints, ancho, alto, 300, mMap);
            } else {
                centerIncidentRouteOnMap(copiedPoints, ancho, alto, 280, mMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void centerIncidentRouteOnMap(List<LatLng> copiedPoints, int ancho, int alto, int padding, GoogleMap mMap) {
        double minLat = Integer.MAX_VALUE;
        double maxLat = Integer.MIN_VALUE;
        double minLon = Integer.MAX_VALUE;
        double maxLon = Integer.MIN_VALUE;
        for (LatLng point : copiedPoints) {
            maxLat = Math.max(point.latitude, maxLat);
            minLat = Math.min(point.latitude, minLat);
            maxLon = Math.max(point.longitude, maxLon);
            minLon = Math.min(point.longitude, minLon);
        }
        final LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(maxLat, maxLon)).include(new LatLng(minLat, minLon)).build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, ancho, alto, padding));
    }

    public static  String capitizeString(String name){
        String captilizedString="";

        if(name.trim().equals(",") || name.trim().equals(".") || name.trim().equals("!")){
            captilizedString = name.trim();
        }
        else if(!name.trim().equals("")){
            captilizedString = name.substring(0,1).toUpperCase() + name.substring(1);
        }
        return captilizedString;
    }

    public static Bitmap createCustomVia(Context context, LocAndField locAndField) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_via, null);
        TextView titleTv = marker.findViewById(R.id.titleTv);


           titleTv.setText(locAndField.getField());




        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        return bitmap;
    }

    public void message(Activity activity, String msg, SweetAlertDialog mDialog) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                if (!msg.equals("")) {
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setTheme(Context context, TextView tv, int bgColor, int txtColor) {
        if (android.os.Build.VERSION.SDK_INT > 23) {
            // Do something for lollipop and above versions
            tv.setBackgroundTintList(ContextCompat.getColorStateList(context, bgColor));
            tv.setTextColor(ContextCompat.getColor(context, (txtColor)));
        } else {
            // do something for phones running an SDK before lollipop
            tv.setBackgroundTintList(context.getResources().getColorStateList(bgColor));
            tv.setTextColor(context.getResources().getColor((txtColor)));
        }
    }

    public LatLng convertStringToClassLatLng(String _latLng) {
        try {
            String latLngs[] = _latLng.split(",");
            double lat = Double.parseDouble(latLngs[0]);
            double lng = Double.parseDouble(latLngs[1]);
            return new LatLng(lat, lng);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
