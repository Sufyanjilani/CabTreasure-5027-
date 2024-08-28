//package base.models;
//
//import static com.google.android.gms.maps.model.JointType.ROUND;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Point;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Display;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.core.content.ContextCompat;
//
//import com.amalbit.trail.RouteOverlayView;
//import com.eurosofttech.kingscars.R;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.Dash;
//import com.google.android.gms.maps.model.Gap;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.PatternItem;
//import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.android.gms.maps.model.RoundCap;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import base.newui.HomeFragment;
//import base.utils.CommonMethods;
//import cn.pedant.SweetAlert.SweetAlertDialog;
//
//public class CommonMethodsObject {
//    public Marker lineFromMarker, lineToMarker;
//
//    private LatLng getLatLng(LocAndField locAndField) {
//        LatLng latLng = new LatLng(Double.parseDouble(locAndField.getLat()), Double.parseDouble(locAndField.getLon()));
//        return latLng;
//    }
//
//    public void _showCurvedPolyline(Context context, String isRebook, ArrayList<LocAndField> locAndFieldArrayList, GoogleMap mMap, RouteOverlayView mapOverlayView) {
//
//        // SET FROM  AND  TO  MARKER
//        lineFromMarker = setFromMarkerLabels(context, isRebook, locAndFieldArrayList.get(0), mMap);
//        lineToMarker = setToMarkerLabels(context, isRebook, locAndFieldArrayList.get(locAndFieldArrayList.size() - 1), mMap);
//
//        // SET VIA
//        try {
//            if (locAndFieldArrayList.size() > 2) {
//                for (int i = 1; i < locAndFieldArrayList.size() - 1; i++) {
//                    LocAndField locAndField = locAndFieldArrayList.get(i);
//                    setVia(context, getLatLng(locAndField), mMap, locAndField);
//                }
//
//
//                // Pickup(0) to Via 1 (1)
//                try {
//                    createPolyline(getLatLng(locAndFieldArrayList.get(0)), getLatLng(locAndFieldArrayList.get(1)), mMap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                // Via 1(2) to Via 2(3)
//                try {
//                    createPolyline(getLatLng(locAndFieldArrayList.get(1)), getLatLng(locAndFieldArrayList.get(2)), mMap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                // Via 2(2) to Drop(3)
//                try {
//                    createPolyline(getLatLng(locAndFieldArrayList.get(2)), getLatLng(locAndFieldArrayList.get(3)), mMap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // CREATE ARC
//        _setArc(context, isRebook, locAndFieldArrayList, mMap, mapOverlayView);
//
//        // SET CAMERA UPDATE
//        try {
//            zoom2((Activity) context, mMap, isRebook, locAndFieldArrayList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void createPolyline(LatLng start, LatLng end, GoogleMap mMap) {
//        List<LatLng> latLngList = new ArrayList<>();
//        latLngList.add(start);
//        latLngList.add(end);
//
//        List<PatternItem> pattern = Arrays.asList(new Dash(30), new Gap(20));
//
//        PolylineOptions polyOptions = new PolylineOptions();
//        polyOptions.color(Color.GRAY);
//        polyOptions.startCap(new RoundCap());
//        polyOptions.endCap(new RoundCap());
//        polyOptions.jointType(ROUND);
//        polyOptions.width(5);
//        polyOptions.addAll(latLngList);
//        polyOptions.pattern(pattern);
//        mMap.addPolyline(polyOptions);
//    }
//
//    public List<LatLng> getList(ArrayList<LocAndField> arrayList) {
//        List<LatLng> a = new ArrayList<>();
//        a.add(getLatLng(arrayList.get(0)));
//        a.add(new LatLng(0, 0));
//        a.add(getLatLng(arrayList.get(arrayList.size() - 1)));
//        return a;
//    }
//
//    public void _setArc(Context context, String isRebook, ArrayList<LocAndField> locAndFieldArrayList, GoogleMap mMap, RouteOverlayView mapOverlayView) {
//        if (isRebook.equals("JobTracking") || isRebook.equals("JobDetail")) {
//            mMap.setOnMapLoadedCallback(() -> {
//                try {
//                    createRouteBuilder(context, locAndFieldArrayList, mMap, mapOverlayView);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        } else {
//            if (locAndFieldArrayList.size() > 2) {
//                try {
//                    createRouteBuilder(context, locAndFieldArrayList, mMap, mapOverlayView);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                mMap.setOnMapLoadedCallback(() -> {
//                    try {
//                        createRouteBuilder(context, locAndFieldArrayList, mMap, mapOverlayView);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//        }
//    }
//
//    private void createRouteBuilder(Context mContext, ArrayList<LocAndField> arrayList, GoogleMap mMapDirections, RouteOverlayView mapOverlayView) {
//        try {
//            new Route.Builder(mapOverlayView)
//                    .setRouteType(RouteOverlayView.RouteType.ARC)
//                    .setCameraPosition(mMapDirections.getCameraPosition())
//                    .setProjection(mMapDirections.getProjection())
//                    .setLatLngs(getList(arrayList))
//                    .setBottomLayerColor(ContextCompat.getColor(mContext, R.color.footerBack)) // footerBack
//                    .setTopLayerColor(ContextCompat.getColor(mContext, R.color.color_black_inverse)) // color_black_inverse
//                    .setRouteShadowColor(ContextCompat.getColor(mContext, R.color.invisible))
//                    .create();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setVia(Context context, LatLng latLng, GoogleMap mMap, LocAndField locAndField) {
//        MarkerOptions via = new MarkerOptions();
////        via.icon(BitmapDescriptorFactory.fromResource(R.drawable.via));
//        via.icon(BitmapDescriptorFactory.fromBitmap(createCustomVia(context, locAndField)));
//        via.position(latLng);
//        mMap.addMarker(via);
//    }
//
//    public Marker setFromMarkerLabels(Context context, String fromScreen, LocAndField locAndField, GoogleMap mMap) {
//
//        float from_x = 0.03f; // right|left
//        float from_y = 0.12f; // top|bottom
//
//        MarkerOptions from = new MarkerOptions();
//        from.position(getLatLng(locAndField));
//        from.anchor(from_x, from_y);
//
//        String subString = locAndField.getField();
//        try {
//            if (subString.length() > 40) {
//                subString = subString.substring(0, 40);
//                subString = subString + "...";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            if (locAndField.getField().equals("")) {
//                from.icon(BitmapDescriptorFactory.fromBitmap(HomeFragment.createCustomPickDropMarker(context, subString, "", true, fromScreen)));
//            } else {
//                from.icon(BitmapDescriptorFactory.fromBitmap(HomeFragment.createCustomPickDropMarker(context, locAndField.getField(), "", true, fromScreen)));
//            }
//
//            mMap.addMarker(from);
//            Marker marker = mMap.addMarker(from);
//            marker.setTag("pickup");
//            return marker;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public Marker setToMarkerLabels(Context context, String fromScreen, LocAndField locAndField, GoogleMap mMap) {
//        float to_x = 0.03f; // top | bottom
//        float to_y = 0.12f; // left|right
//
//        MarkerOptions to = new MarkerOptions();
//        to.anchor(to_x, to_y);
//        to.position(getLatLng(locAndField));
//
//        String TosubString = locAndField.getField();
//        try {
//            if (TosubString.length() > 40) {
//                TosubString = TosubString.substring(0, 40);
//                TosubString = TosubString + "...";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            TosubString = "";
//        }
//
//        if (locAndField.getField().equals("")) {
//            to.icon(BitmapDescriptorFactory.fromBitmap(HomeFragment.createCustomPickDropMarker(context, TosubString, "", false, fromScreen)));
//        } else {
//            to.icon(BitmapDescriptorFactory.fromBitmap(HomeFragment.createCustomPickDropMarker(context, locAndField.getField(), "", false, fromScreen)));
//        }
//        try {
//
////            to.anchor(0.06f, 0.06f);
//            mMap.addMarker(to);
//            Marker marker = mMap.addMarker(to);
//            marker.setTag("drop");
//            return marker;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private void zoom2(Activity context, GoogleMap mMap, String isRebook, ArrayList<LocAndField> locAndFieldArrayList) {
//        try {
//            Display display = context.getWindowManager().getDefaultDisplay();
//            Point size = new Point();
//            display.getSize(size);
//            float percentTodivide = 0.20f;
//            int ancho = size.x;
//            if (size.y >= 2000) {
//                percentTodivide = 0.50f;
//            }
//            int alto = (int) (size.y - (size.y * percentTodivide));
//            if (alto > 1500) {
//                alto = 1500;
//            }
//            List<LatLng> copiedPoints = new ArrayList<LatLng>();
//            copiedPoints.add(getLatLng(locAndFieldArrayList.get(0)));
//            copiedPoints.add(getLatLng(locAndFieldArrayList.get(locAndFieldArrayList.size() - 1)));
//            if (isRebook.equals("JobDetail")) {
//                alto = (int) (size.y - (size.y * 0.40));
//                centerIncidentRouteOnMap(copiedPoints, ancho, alto, 320, mMap);
//            } else if (isRebook.equals("JobTracking")) {
//                alto = (int) (size.y - (size.y * 0.40));
//                centerIncidentRouteOnMap(copiedPoints, ancho, alto, 300, mMap);
//            } else {
//                Log.d("TAG", "zoom2: height : " + ((int) (size.y - (size.y * percentTodivide))) + " = " + size.y + " - " + (size.y * percentTodivide));
//                centerIncidentRouteOnMap(copiedPoints, ancho, alto, 290, mMap);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void centerIncidentRouteOnMap(List<LatLng> copiedPoints, int ancho, int alto, int padding, GoogleMap mMap) {
//        double minLat = Integer.MAX_VALUE;
//        double maxLat = Integer.MIN_VALUE;
//        double minLon = Integer.MAX_VALUE;
//        double maxLon = Integer.MIN_VALUE;
//        for (LatLng point : copiedPoints) {
//            maxLat = Math.max(point.latitude, maxLat);
//            minLat = Math.min(point.latitude, minLat);
//            maxLon = Math.max(point.longitude, maxLon);
//            minLon = Math.min(point.longitude, minLon);
//        }
//        final LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(maxLat, maxLon)).include(new LatLng(minLat, minLon)).build();
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, ancho, alto, padding));
//    }
//
//    public  Bitmap createCustomVia(Context context, LocAndField locAndField) {
//        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_via, null);
//        TextView titleTv = marker.findViewById(R.id.titleTv);
//        titleTv.setText(locAndField.getField());
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
//        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
//        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
//        marker.buildDrawingCache();
//        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        marker.draw(canvas);
//        return bitmap;
//    }
//
//    public void message(Activity activity, String msg, SweetAlertDialog mDialog) {
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (mDialog != null && mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
//                if (!msg.equals("")) {
//                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    public void setTheme(Context context, TextView tv, int bgColor, int txtColor) {
//        if (android.os.Build.VERSION.SDK_INT > 23) {
//            // Do something for lollipop and above versions
//            tv.setBackgroundTintList(ContextCompat.getColorStateList(context, bgColor));
//            tv.setTextColor(ContextCompat.getColor(context, (txtColor)));
//        } else {
//            // do something for phones running an SDK before lollipop
//            tv.setBackgroundTintList(context.getResources().getColorStateList(bgColor));
//            tv.setTextColor(context.getResources().getColor((txtColor)));
//        }
//    }
//
//
//    public LatLng convertStringToClassLatLng(String _latLng) {
//        try {
//            String latLngs[] = _latLng.split(",");
//            double lat = Double.parseDouble(latLngs[0]);
//            double lng = Double.parseDouble(latLngs[1]);
//            return new LatLng(lat, lng);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
