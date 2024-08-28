package base.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.support.parser.DirectionsJSONParser.milesString;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.eurosoft.customerapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import base.utils.CommonVariables;
import base.fragments.Fragment_Tracking;
import base.listener.Listener_Booking;
import base.listener.Listener_CancelBookingApp;
import base.fragments.Fragment_Main;
import base.fragments.Fragment_JobDetail;
import base.models.Model_BookingDetailsModel;
import base.utils.Config;
import base.models.LocAndField;
import base.utils.SharedPrefrenceHelper;
import base.models.ParentPojo;
import base.newui.HomeFragment;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Model_BookingDetailsModel> bookingModels;

    public static final String Track_Drive = "Track Driver";
    public static final String Cancel_Booking = "Cancel Booking";
    public static final String Show_On_Map = "Track";
    public static final String Show_Details = "Show Details";
    public static final String Rebook_Now = "Rebook Now";

    // STATUS (Rebook and Show)
    public static final String COMPLETED = "Completed";
    public static final String CANCELLED = "cancelled";
    public static final String WAITING = "Waiting";

    // STATUS (Track and Cancelled)
    public static final String CONFIRMING = "Confirming";
    public static final String CONFIRMED = "Confirmed";

    // STATUS (Track)
    public static final String ON_ROUTE = "onroute";
    //    public static final String PASSENGER_ON_BOARD = "passengeronboard";
//    public static final String POB = "pob";
    public static final String ONBOARD = "onboard";
    public static final String PASSENGER_ON_BOARD = "PassengerOnBoard";
    public static final String STC = "stc";
    public static final String ARRIVED = "arrived";
    public String statusColor = "#ffffff";


    public static boolean isTracking = false;
    public int showWhat = 0;
    private ParentPojo p = new ParentPojo();
    private SharedPreferences sp;
    private Listener_CancelBookingApp listenerCancelBookingApp;
    private SharedPrefrenceHelper sharedPrefrenceHelper;

    public BookingAdapter(Context context, ArrayList<Model_BookingDetailsModel> bookingModels, int showWhat, SharedPreferences sp, SharedPrefrenceHelper sharedPrefrenceHelper, Listener_CancelBookingApp listenerCancelBookingApp) {
        this.context = context;
        this.bookingModels = bookingModels;
        this.showWhat = showWhat;
        this.sp = sp;
        this.sharedPrefrenceHelper = sharedPrefrenceHelper;
        this.listenerCancelBookingApp = listenerCancelBookingApp;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_item_booking_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder h, int position) {
        Model_BookingDetailsModel model = bookingModels.get(position);
        setConditionsForColors(model.getStatus());
        Log.d("TAG", "onBindViewHolder: STATUS : " + model.getStatus());
        String[] Pick_Address = HomeFragment.getFilteredAddress(model.getFromAddress());
        String[] Drop_Address = HomeFragment.getFilteredAddress(model.gettoAddress());

        h.C_drop.setText(Drop_Address[0]);
        h.Sub_Address_drop.setText(Drop_Address[1]);

        if (Pick_Address[0].equals("")) {
            h.C_Address.setText(Pick_Address[1]);
            h.Sub_Address.setText(Pick_Address[0]);

        } else {
            h.C_Address.setText(Pick_Address[0]);
            h.Sub_Address.setText(Pick_Address[1]);
        }

        h.carName.setText("" + model.getCar());
        h.C_refNO.setText("Ref : " + model.getRefrenceNo());
        h.C_Time.setText(model.getPickUpDate() + " " + model.getPickUpTime());


        if (model.getStatus().equalsIgnoreCase(PASSENGER_ON_BOARD) || model.getStatus().equalsIgnoreCase("POB")) {
            h.C_Status.setText("onboard");
        } else
            h.C_Status.setText(model.getStatus().toLowerCase());

        // set vias
        String via = "";
        try {
            Log.d("TAG", "onBindViewHolder:  VIA = " + model.getViaPointsAsString());
            if (model.getViaPointsAsString().length() == 0) {
                h.txtviaAddress1.setText("");
                h.txtviaAddress2.setText("");
                h.txtviaAddress1.setVisibility(GONE);
                h.txtviaAddress2.setVisibility(GONE);
                h.ViaHead.setVisibility(GONE);
            } else {
                if (getVias(model.getViaPointsAsString())[1].length() == 0) {
                    h.txtviaAddress1.setText("1. " + getVias(model.getViaPointsAsString())[0]);
                    h.txtviaAddress1.setVisibility(VISIBLE);
                    h.txtviaAddress2.setVisibility(GONE);
                    h.ViaHead.setVisibility(VISIBLE);

                } else if (getVias(model.getViaPointsAsString())[1].length() > 3) {
                    h.txtviaAddress1.setText("1. " + getVias(model.getViaPointsAsString())[0]);
                    h.txtviaAddress2.setText("2. " + getVias(model.getViaPointsAsString())[1]);
                    h.txtviaAddress1.setVisibility(VISIBLE);
                    h.txtviaAddress2.setVisibility(VISIBLE);
                    h.ViaHead.setVisibility(VISIBLE);

                } else {
                    h.txtviaAddress1.setText("");
                    h.txtviaAddress2.setText("");
                    h.txtviaAddress1.setVisibility(GONE);
                    h.txtviaAddress2.setVisibility(GONE);
                    h.ViaHead.setVisibility(GONE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            h.txtviaAddress1.setText("");
            h.txtviaAddress2.setText("");
            h.txtviaAddress1.setVisibility(GONE);
            h.txtviaAddress2.setVisibility(GONE);
            h.ViaHead.setVisibility(GONE);
        }
        // set shopping visible and gone
        try {
            h.shoppingLabel.setVisibility((model.getCar() != null && model.getCar().toLowerCase().contains("shopping")) ? VISIBLE : GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set fares visible and gone
        try {
            if (sp.getString(Config.ShowFares, "1").equals("0")) {
                h.fare_txt.setVisibility(GONE);
            } else {
                h.fare_txt.setVisibility(VISIBLE);
            }
            h.fare_txt.setText(sp.getString(CommonVariables.CurrencySymbol, "\u00A3") + "" + String.format("%.2f", Double.parseDouble(model.getOneWayFare())));

        } catch (Exception e) {
            h.fare_txt.setText(sp.getString(CommonVariables.CurrencySymbol, "\u00A3") + "" + String.format("%.2f", 0.000));
            e.printStackTrace();
        }

        setViews(h.C_track, h.C_cancel, h.actionLyt, h.lineView, model.getStatus(), model);

        // set Color According To Status
        h.C_Status_lyt.getBackground().setColorFilter(Color.parseColor(statusColor), PorterDuff.Mode.SRC_ATOP);

        h.C_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (h.C_track.getText().toString().trim().equals(Track_Drive.trim()) || h.C_track.getText().toString().trim().equals(Show_On_Map.trim())) {
                    ShowDialog(model);

                }

                if (h.C_track.getText().toString().trim().equals(Rebook_Now.trim())) {
                    HomeFragment.isClickedRebooked = true;
                    ShowDialogHistory(model);
                }
            }
        });

        h.C_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.C_cancel.getText().toString().trim().equals(Show_Details.trim())) {
                    ShowDialog(model);
                }

                if (h.C_cancel.getText().toString().trim().equals(Cancel_Booking.trim())) {
                    CancelBooking(model.getRefrenceNo());
                }
            }
        });


        if (showWhat == 0 && milesString != null && !milesString.equals("") && sharedPrefrenceHelper.getVal(model.getRefrenceNo()).equals("")) {
            sharedPrefrenceHelper.putVal(model.getRefrenceNo(), milesString);
        }
    }

    private void ShowDialogHistory(Model_BookingDetailsModel model) {
        try {
            if (sharedPrefrenceHelper.getLocAndFieldFromSavedBookings(model.getRefrenceNo()).size() != 0) {
                ArrayList<LocAndField> locAndFieldArrayList = sharedPrefrenceHelper.getLocAndFieldFromSavedBookings(model.getRefrenceNo());
                final Bundle argss = new Bundle();
                Fragment bookings = new HomeFragment();
                argss.putParcelableArrayList("key_locAndFieldArrayList", locAndFieldArrayList);
                argss.putString("vehicle", model.getCar());
                argss.putBoolean("isShopping", model.getReturnFare() != null && model.getReturnFare().equals("Shopping Collection"));
                argss.putBoolean("again", true);
                bookings.setArguments(argss);
                ((Fragment_Main) context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, bookings, "current").commit();
            } else {
                gotoHomeFragment(model);
            }

           /* if (sharedPrefrenceHelper.getLocAndFieldFromSavedBookings(model.getRefrenceNo()).size() != 0) {
                ArrayList<LocAndField> locAndFieldArrayList = sharedPrefrenceHelper.getLocAndFieldFromSavedBookings(model.getRefrenceNo());
                if (locAndFieldArrayList.size() == 2) {
                    model.setPickupLat(locAndFieldArrayList.get(0).getLat());
                    model.setPickupLon(locAndFieldArrayList.get(0).getLon());

                    model.setDropLat(locAndFieldArrayList.get(1).getLat());
                    model.setDropLon(locAndFieldArrayList.get(1).getLon());
                }
            }*/
        } catch (Exception e) {
            gotoHomeFragment(model);
        }
    }

    private void gotoHomeFragment(Model_BookingDetailsModel model) {
        ArrayList<LocAndField> locAndFields = new ArrayList<>();

        LocAndField fromLoc = new LocAndField();
        fromLoc.setField(model.getFromAddress());
        fromLoc.setLat(model.getPickupLat());
        fromLoc.setLon(model.getPickupLon());
        fromLoc.setLocationType(model.getFromAddressType());
        locAndFields.add(fromLoc);


        try {
            if (model.getViaPointsAsString().contains(">>>")) {
                if (getVias(model.getViaPointsAsString()).length == 1) {
                    LocAndField via_1_LocAndField = new LocAndField();
                    via_1_LocAndField.setField(getVias(model.getViaPointsAsString())[0]);
                    via_1_LocAndField.setLat("0");
                    via_1_LocAndField.setLon("0");
                    via_1_LocAndField.setLocationType("");
                    locAndFields.add(via_1_LocAndField);

                    if (getVias(model.getViaPointsAsString()).length == 2) {
                        LocAndField via_2_LocAndField = new LocAndField();
                        via_2_LocAndField.setField(getVias(model.getViaPointsAsString())[1]);
                        via_2_LocAndField.setLat("0");
                        via_2_LocAndField.setLon("0");
                        via_2_LocAndField.setLocationType("");
                        locAndFields.add(via_2_LocAndField);
                    }
                }
            } else {
                LocAndField via_1_LocAndField = new LocAndField();
                via_1_LocAndField.setField(model.getViaPointsAsString().trim());
                via_1_LocAndField.setLat("0");
                via_1_LocAndField.setLon("0");
                via_1_LocAndField.setLocationType("");
                locAndFields.add(via_1_LocAndField);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (model.getViaPoints().size() > 0) {
            ArrayList<String> vias = model.getViaPoints();
            ArrayList<Address> viapoints = new ArrayList<Address>();
            for (int i = 0; i < vias.size(); i++) {
                Address viaAddress = getLocationFromAddress(vias.get(i));
                LocAndField locAndField = new LocAndField();
                locAndField.setField(viaAddress.getAddressLine(0));
                locAndField.setLat("0");
                locAndField.setLon("0");
                locAndField.setLocationType("Address");
                locAndFields.add(locAndField);
            }
        }

        LocAndField toLoc = new LocAndField();
        toLoc.setField(model.gettoAddress());
        toLoc.setLat(model.getDropLat());
        toLoc.setLon(model.getDropLon());
        toLoc.setLocationType(model.gettoAddressType());
        locAndFields.add(toLoc);


        final Bundle argss = new Bundle();
        Fragment bookings = new HomeFragment();

        argss.putParcelableArrayList("key_locAndFieldArrayList", locAndFields);
        argss.putString("vehicle", model.getCar());
        argss.putBoolean("isShopping", model.getReturnFare() != null && model.getReturnFare().equals("Shopping Collection"));
        argss.putBoolean("again", true);

        bookings.setArguments(argss);
        ((Fragment_Main) context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, bookings, "current").commit();

    }

    public void ShowDialog(Model_BookingDetailsModel model) {
        HomeFragment.isHomeVissibel = false;
        if (model.getStatus().equalsIgnoreCase(CANCELLED) || model.getStatus().equalsIgnoreCase(COMPLETED)) {
            Fragment_JobDetail tracking = new Fragment_JobDetail();
            Bundle trackArgs = new Bundle();

            trackArgs.putSerializable(CommonVariables.KEY_BOOKING_MODEL, model);
            trackArgs.putSerializable("iscompleted", "1");
            tracking.setArguments(trackArgs);
            if (context instanceof FragmentActivity)
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fall, R.anim.fall_below, R.anim.fall, R.anim.fall_below)
                        .add(android.R.id.content, tracking, null)
                        .addToBackStack(null).commit();
        } else {
            Fragment_Tracking tracking = new Fragment_Tracking();
            Bundle trackArgs = new Bundle();
            trackArgs.putSerializable(CommonVariables.KEY_BOOKING_MODEL, model);
            tracking.setArguments(trackArgs);
            if (context instanceof FragmentActivity)
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fall, R.anim.fall_below, R.anim.fall, R.anim.fall_below).add(android.R.id.content, tracking, null)
                        .addToBackStack(null).commit();
        }

    }

    private void CancelBooking(final String ref) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("")
                .setContentText("Are you sure, you want to cancel this booking??")
                .setCancelText(p.getNo())
                .setConfirmText("Yes!")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        if (listenerCancelBookingApp != null) {
                            listenerCancelBookingApp.onComplete("CANCEL" + ref);
                        }
//                        context.getApplicationContext().cancelBooking("CANCEL", ref);
//                        new BookingStatusChangeTask().execute(new String[]{BookingStatusChangeTask.TASK_CANCEL, ref});
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                    }
                })
                .show();
    }

    public Address getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();


            return location;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setConditionsForColors(String status) {
        // status = confirmed | completed
        if (status.equalsIgnoreCase(CONFIRMED) || status.equalsIgnoreCase(COMPLETED)) {
            statusColor = "#23C552";
        } else if (status.equalsIgnoreCase(CANCELLED)) {
            statusColor = "#F84F31";
        } else if (status.equalsIgnoreCase(ON_ROUTE)) {
            statusColor = "#a8a8a8";//63cad8

        } else if (status.equalsIgnoreCase(PASSENGER_ON_BOARD) || status.equalsIgnoreCase("POB")) {
            statusColor = "#7cd992";
        } else if (status.equalsIgnoreCase(ARRIVED)) {
            statusColor = "#28CC2D";
        } else if (status.equalsIgnoreCase(STC)) {
            statusColor = "#3581d8";
        } else {
            statusColor = "#000000";
        }
    }

    private void setViews(TextView tv_1, TextView tv_2, LinearLayout actionLyt, View line, String status, Model_BookingDetailsModel model) {
        // status = confirmed (Track and Cancel)
        if (status.equalsIgnoreCase(CONFIRMED) || status.equalsIgnoreCase(WAITING)) {
            tv_1.setText(Track_Drive);
            tv_1.setVisibility(VISIBLE);

            tv_2.setText(Cancel_Booking);
            tv_2.setVisibility(VISIBLE);

            actionLyt.setWeightSum(2);

            // status = completed (Rebook and Show)
        } else if (status.equalsIgnoreCase(COMPLETED)) {
            tv_1.setText(Rebook_Now);
            tv_1.setVisibility(VISIBLE);

            tv_2.setText(Show_Details);
            tv_2.setVisibility(VISIBLE);

            actionLyt.setWeightSum(2);

            // status = cancelled ()
        } else if (status.equalsIgnoreCase(CANCELLED)) {
            tv_1.setText(Rebook_Now);
            tv_1.setVisibility(VISIBLE);

            tv_2.setText(Show_Details);
            tv_2.setVisibility(VISIBLE);

            actionLyt.setWeightSum(2);

            // status = onroute (Track)
            // (status.equalsIgnoreCase(ON_ROUTE) || status.equalsIgnoreCase(ONBOARD) || status.equalsIgnoreCase(ARRIVED) || status.equalsIgnoreCase(STC))
        } else {
            if (!isTracking) {
                isTracking = true;
                ShowDialog(model);
            }
            tv_1.setText(Track_Drive);
            tv_1.setVisibility(VISIBLE);

            tv_2.setText("");
            tv_2.setVisibility(GONE);

            line.setVisibility(GONE);

            actionLyt.setWeightSum(1);
        }
    }

    @Override
    public int getItemCount() {
        return bookingModels.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView C_refNO;
        private TextView C_Time;
        private TextView C_Address;
        private TextView Sub_Address;
        private TextView C_drop;
        private TextView Sub_Address_drop;
        private TextView carName;
        private TextView fare_txt;
        private TextView C_Status;
        private TextView C_track;
        private TextView C_cancel;
        private TextView ViaHead;
        private TextView txtviaAddress1;
        private TextView txtviaAddress2;
        private View lineView;
        private LinearLayout actionLyt;
        private LinearLayout shoppingLabel;
        private LinearLayout C_Status_lyt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lineView = itemView.findViewById(R.id.lineView);
            C_refNO = itemView.findViewById(R.id.C_refNO);
            C_Time = itemView.findViewById(R.id.C_Time);
            C_Address = itemView.findViewById(R.id.C_Address);
            Sub_Address = itemView.findViewById(R.id.Sub_Address);
            C_drop = itemView.findViewById(R.id.C_drop);
            Sub_Address_drop = itemView.findViewById(R.id.Sub_Address_drop);
            carName = itemView.findViewById(R.id.carName);
            fare_txt = itemView.findViewById(R.id.fare_txt);
            C_Status = itemView.findViewById(R.id.C_Status);
            C_track = itemView.findViewById(R.id.C_track);
            C_cancel = itemView.findViewById(R.id.C_cancel);
            ViaHead = itemView.findViewById(R.id.ViaHead);
            txtviaAddress1 = itemView.findViewById(R.id.txtviaAddress1);
            txtviaAddress2 = itemView.findViewById(R.id.txtviaAddress2);
            actionLyt = itemView.findViewById(R.id.actionLyt);
            shoppingLabel = itemView.findViewById(R.id.shoppingLabel);
            C_Status_lyt = itemView.findViewById(R.id.C_Status_lyt);

        }
    }

    private String[] getVias(String via) {
        String[] vias = new String[2];
        if (via.contains(">>>")) {
            vias = via.split(">>>");
            return vias;
        } else {
            if (via.length() == 0) {
                vias[0] = "";
            } else {
                vias[0] = via;
            }
            vias[1] = "";
            return vias;
        }
    }
}
