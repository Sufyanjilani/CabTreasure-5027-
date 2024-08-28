package base.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static base.newui.HomeFragment.isServiceRunningInBackground;
import static base.utils.CommonMethods.checkIfHasNullForString;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eurosoft.customerapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import base.adapters.BookingAdapter;
import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.listener.Listener_CancelBookingApp;
import base.listener.Listener_GetAllBooking;
import base.manager.Manager_CancelBookingApp;
import base.manager.Manager_GetBookingList;
import base.models.Model_BookingDetailsModel;
import base.models.ParentPojo;
import base.services.Service_NotifyStatus;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class Fragment_AllBooking extends Fragment {
    LocalBroadcastManager bm;
    public static boolean isVisiable = false;

    private RecyclerView rv;
    private TextView notFoundTv;
    private TextView historyTab;
    private TextView scheduleTab;
    private TextView titleTv;

    private View historyView;
    private View scheduleView;

    private ImageView backIv;
    private SwipeRefreshLayout swipeRefresh;

    // JAVA
    private int showWhat = 1;
    private boolean isLoadedForSchedule = false;
    private boolean isLoadedForHistory = false;

    private ParentPojo p;
    private Context context;
    private SharedPreferences sp;
    private BookingAdapter bookingAdapter;
    private SharedPrefrenceHelper mHelper;
    private DatabaseOperations mDatabaseOperations;
    private ArrayList<Model_BookingDetailsModel> modelBookingDetailsModels;

    // LISTENER
    private Listener_CancelBookingApp listenerCancelBookingApp;
    private Listener_GetAllBooking listener_getAllBooking;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        isLoadedForSchedule = false;
        isLoadedForHistory = false;
        p = new ParentPojo();
        modelBookingDetailsModels = new ArrayList<>();
        mHelper = new SharedPrefrenceHelper(context);
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        mDatabaseOperations = new DatabaseOperations(new DatabaseHelper(context));
        registerBroadcastRecevier();

        isVisiable = true;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_booking_tabs, container, false);

//        setDarkAndNightThemeColor();

        init(view);
        //   setUpList();
        listener();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (showWhat == 1) {
            isVisiable = true;
        }
    }

    private void init(View view) {

        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setHasFixedSize(true);

        notFoundTv = view.findViewById(R.id.notfound);
        notFoundTv.setText(p.getTextNoBooking());

        historyTab = view.findViewById(R.id.HistoryTab);
        historyTab.setText(p.getTabTwo());

        scheduleTab = view.findViewById(R.id.currTab);
        scheduleTab.setText(p.getTabOne());

        historyView = view.findViewById(R.id.HistoryLine);
        scheduleView = view.findViewById(R.id.currLine);

        backIv = view.findViewById(R.id.menuIv);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        titleTv = view.findViewById(R.id.titleTv);
        titleTv.setText(p.getYourTrips());
        // foreground service setting
        if (sp.getString(CommonVariables.Enable_ForeGround_Service, "0").equals("1")) {
            if (!mHelper.getBoolVal(isServiceRunningInBackground) && mDatabaseOperations.getActiveBookingsCount() > 0) {
                context.startService(new Intent(context, Service_NotifyStatus.class));
                ((Fragment_Main) context).bindServiceWithActivity();
            }
        }

//
//        timer.schedule(
//                new TimerTask() {
//                    @Override
//                    public void run() {
//                        if (modelBookingDetailsModels.size() != preViousListSize) {
//                            loadList();
//                        }
//                    }
//                }, 0, 5000
//        );
    }

    private void listener() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    swipeRefresh.setRefreshing(false);
                    if (sp.getString(Config.FetchBookingsFromServer, "0").equals("1")) {
                        getFromServer(true);
                    } else {
                        getFromDb();
                    }
                } catch (Exception e) {
                    swipeRefresh.setRefreshing(false);
                    e.printStackTrace();
                }
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Fragment_Main) getActivity()).toggleDrawer();
            }
        });

        scheduleTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWhat = 1;
                setTab();

                isVisiable = true;
            }
        });

        historyTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWhat = 2;
                setTab();
                isVisiable = false;
            }
        });


        listener_getAllBooking = new Listener_GetAllBooking() {
            @Override
            public void onComplete(String result) {
                result = checkIfHasNullForString(result);

                if (result.length() > 0) {
                    try {
                        if (showWhat == 1)
                            mDatabaseOperations.DeleteAllSchedule();
                        else if (showWhat == 2)
                            mDatabaseOperations.DeleteAllHistory();
                        else
                            mDatabaseOperations.DeleteAllBooking();

                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("HasError")) {

                        } else {
                            if (jsonObject.optString("TokenValidate").equalsIgnoreCase("ValidToken")) {
                                double total = 0;
                                ArrayList<Model_BookingDetailsModel> arrayList = new ArrayList<Model_BookingDetailsModel>();
                                JSONObject dataObject = jsonObject.optJSONObject("Data");
                                if (dataObject != null) {
                                    JSONArray arr = dataObject.optJSONArray("Json");
                                    if (arr != null) {
                                        for (int i = 0; i < arr.length(); i++) {

                                            Model_BookingDetailsModel bookingObj = new Model_BookingDetailsModel();
                                            JSONObject obj = arr.optJSONObject(i);
                                            if (obj != null) {
                                                String SpecialRequirements = "";
                                                double FareRate = 0,
                                                        Waiting = 0,
                                                        Parking = 0,
                                                        Congestion = 0,
                                                        AgentCharge = 0,
                                                        AgentFees = 0,
                                                        ExtraDropCharges = 0,
                                                        MeetAndGreetCharges = 0,
                                                        ServicesCharges = 0,
                                                        CashFares = 0,
                                                        CashRate = 0,
                                                        AgentCommission = 0,
                                                        CompanyPrice = 0,
                                                        BookingFee = 0;

                                                try {
                                                    BookingFee = Double.parseDouble(obj.getString("BookingFee"));
                                                } catch (Exception e) {
                                                    BookingFee = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    Waiting = Double.parseDouble(obj.getString("Waiting"));
                                                } catch (Exception e) {
                                                    Waiting = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    Parking = Double.parseDouble(obj.getString("Parking"));
                                                } catch (Exception e) {
                                                    Parking = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    Congestion = Double.parseDouble(obj.getString("Congestion"));
                                                } catch (Exception e) {
                                                    Congestion = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    CompanyPrice = Double.parseDouble(obj.getString("CompanyPrice"));
                                                } catch (Exception e) {
                                                    CompanyPrice = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    FareRate = Double.parseDouble(obj.getString("FareRate"));
                                                } catch (Exception e) {
                                                    FareRate = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    MeetAndGreetCharges = Double.parseDouble(obj.getString("MeetAndGreetCharges"));
                                                } catch (Exception e) {
                                                    MeetAndGreetCharges = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    ServicesCharges = Double.parseDouble(obj.getString("Services"));
                                                } catch (Exception e) {
                                                    ServicesCharges = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    CashFares = Double.parseDouble(obj.getString("CashFares"));
                                                } catch (Exception e) {
                                                    CashFares = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    AgentCharge = Double.parseDouble(obj.getString("AgentCharge"));
                                                } catch (Exception e) {
                                                    CashRate = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    ExtraDropCharges = Double.parseDouble(obj.getString("ExtraDropCharges"));
                                                } catch (Exception e) {
                                                    ExtraDropCharges = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    AgentFees = Double.parseDouble(obj.getString("AgentFees"));
                                                } catch (Exception e) {
                                                    AgentCommission = 0;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    SpecialRequirements = obj.getString("SpecialRequirements");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    bookingObj.setFromAddressType(((obj.getInt("FromLocTypeId") == 7)) ? "Address" : "Airport");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    bookingObj.settoAddressType(((obj.getInt("ToLocTypeId") == 7)) ? "Address" : "Airport");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                bookingObj.setFareRate(FareRate);
                                                bookingObj.setBookingFee(BookingFee);
                                                bookingObj.setWaiting(Waiting);
                                                bookingObj.setParking(Parking);
                                                bookingObj.setCongestion(Congestion);
                                                bookingObj.setAgentCharge(AgentCharge);
                                                bookingObj.setAgentFees(AgentFees);
                                                bookingObj.setExtraDropCharges(ExtraDropCharges);
                                                bookingObj.setMeetAndGreet(MeetAndGreetCharges);
                                                bookingObj.setServiceCharges(ServicesCharges);
                                                bookingObj.setAgentCommission(AgentCommission);
                                                bookingObj.setCompanyPrice(CompanyPrice);
                                                bookingObj.setSpecialNotes(SpecialRequirements);
                                                bookingObj.setRefrenceNo(obj.getString("Id"));

                                                mHelper.putVal("isfeedback_" + bookingObj.getRefrenceNo(), "1");
                                                bookingObj.setPickUpDate(obj.getString("PickupDateTime").split(" ")[0]);
                                                bookingObj.setPickUpTime(obj.getString("PickupDateTime").split(" ")[1]);
                                                bookingObj.setJournyType(2);
                                                bookingObj.setFromAddress(obj.getString("FromAddress"));
                                                bookingObj.settoAddress(obj.getString("ToAddress"));
                                                bookingObj.setFromAddressDoorNO(obj.optString("FromDoorNo"));
                                                bookingObj.settoAddressDoorNO(obj.optString("ToDoorNo"));
                                                if (bookingObj.gettoAddressDoorNO() == null || bookingObj.gettoAddressDoorNO() == "null") {
                                                    bookingObj.settoAddressDoorNO("");
                                                }
                                                bookingObj.setCar(obj.getString("VehicleType"));
                                                bookingObj.setCusomerName(obj.getString("CustomerName"));
                                                bookingObj.setCusomerMobile(obj.getString("CustomerMobileNo"));
                                                bookingObj.setCusomerEmail(obj.getString("CustomerEmail"));
                                                Log.d("TAG", "onComplete: StatusName : " + obj.getString("StatusName"));
                                                String status = obj.getString("StatusName").trim();

                                                bookingObj.setStatus(obj.getString("StatusName").equals("Waiting") ? "Waiting" : obj.getString("StatusName"));

                                                bookingObj.setPaymentType(obj.getString("PaymentType"));

                                                if (bookingObj.getPaymentType().trim().toLowerCase().equals("account")) {
                                                    total = BookingFee + Waiting + Parking + Congestion + CompanyPrice + MeetAndGreetCharges + ServicesCharges + CashFares + CashRate + ExtraDropCharges + AgentCommission + AgentFees + AgentCharge;
                                                } else {
                                                    total = BookingFee + Waiting + Parking + Congestion + FareRate + MeetAndGreetCharges + ServicesCharges + CashFares + CashRate + ExtraDropCharges + AgentCommission + AgentFees + AgentCharge;
                                                }

                                                if (CompanyPrice >= FareRate) {
                                                    total = BookingFee + Waiting + Parking + Congestion + CompanyPrice + MeetAndGreetCharges + ServicesCharges + CashFares + CashRate + ExtraDropCharges + AgentCommission + AgentFees + AgentCharge;
                                                } else {
                                                    total = BookingFee + Waiting + Parking + Congestion + FareRate + MeetAndGreetCharges + ServicesCharges + CashFares + CashRate + ExtraDropCharges + AgentCommission + AgentFees + AgentCharge;
                                                }

                                                bookingObj.setOneWayFare(String.valueOf(total));
                                                String via1 = "";
                                                String via2 = "";
                                                String viasss = obj.getString("ViaString");
                                                if (viasss.length() > 0) {
                                                    if (viasss.contains(">>")) {
                                                        String[] viaStringArr = viasss.split(">>");
                                                        if (viaStringArr.length == 1) {
                                                            if (!viaStringArr[0].trim().equals("")) {
                                                                via1 = viaStringArr[0].replace("Via 1 :", "").trim();
                                                            }
                                                        }
                                                        if (viaStringArr.length == 2) {
                                                            if (!viaStringArr[0].trim().equals("")) {
                                                                via1 = viaStringArr[0].replace("Via 1 :", "").trim();
                                                            }
                                                            if (!viaStringArr[1].trim().equals("")) {
                                                                via2 = viaStringArr[1].replace("2 :", "").trim();
                                                            }
                                                        }

                                                        if (via2.equals("")) {
                                                            bookingObj.setViaPointsAsString(via1);
                                                        } else {
                                                            bookingObj.setViaPointsAsString(via1 + ">>>" + via2);
                                                        }
                                                    } else {
                                                        via1 = viasss.replace("Via 1 :", "").trim();
                                                        bookingObj.setViaPointsAsString(via1);
                                                    }
                                                } else {
                                                    bookingObj.setViaPointsAsString("");
                                                }

                                                bookingObj.setReturnFare(bookingObj.getCar().toLowerCase().equals("shopping collection") ? "Shopping Collection" : "0.00");

                                                arrayList.add(bookingObj);
                                            }
                                        }

                                        if (arrayList.size() > 0 && !Fragment_Main.fromTracking) {
                                            for (int i = 0; i < arrayList.size(); i++) {
                                                if (
                                                        arrayList.get(i).getStatus().toLowerCase().equals("onroute") ||
                                                                arrayList.get(i).getStatus().toLowerCase().equals("arrived") ||
                                                                arrayList.get(i).getStatus().toLowerCase().equals("stc") ||
                                                                arrayList.get(i).getStatus().toLowerCase().equals("pob") ||
                                                                arrayList.get(i).getStatus().toLowerCase().equals("passengeronboard")
                                                ) {
                                                    BookingAdapter.isTracking = false;
                                                    break;
                                                }
                                            }
                                        }

                                        mDatabaseOperations.swapAddressToRecent();
                                        mDatabaseOperations.insertBulkBookings(arrayList);

                                        if (isAdded()) {
                                            getFromDb();
                                        }
                                        sp.edit().putString(Config.ISBOOKINGLOADED, "1").commit();
                                        // foreground service setting
                                        if (sp.getString(CommonVariables.Enable_ForeGround_Service, "0").equals("1")) {

                                            if (!mHelper.getBoolVal(isServiceRunningInBackground) && mDatabaseOperations.getActiveBookingsCount() > 0) {
                                                context.startService(new Intent(context, Service_NotifyStatus.class));
                                                ((Fragment_Main) context).bindServiceWithActivity();
                                            }
                                        }
                                    } else {
                                        if (isAdded()) {
                                            getFromDb();
                                        }
                                    }
                                } else {
                                    if (isAdded()) {
                                        getFromDb();
                                    }
                                }
                            } else {
                                if (isAdded()) {
                                    getFromDb();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (isAdded()) {
                            getFromDb();
                        }
                    }
                } else {
                    if (isAdded()) {
                        getFromDb();
                    }
                }
            }
        };

        if (sp.getString(Config.FetchBookingsFromServer, "0").equals("1")) {
            getFromServer(true);
        } else {
            getFromDb();
        }

    }

    private void showCancel() {
        getFromServer(true);
        Log.d("TAG", "CALL AFTER CANCELLED getFromDb :BEFORE  CANCELLED  ");
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Success")
                .setContentText("Booking has been cancelled!")
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Log.d("TAG", "CALL AFTER CANCELLED getFromDb :AFTER CANCELLED  ");

                    }

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                    }
                })
                .show();
    }

    private void setTab() {
        modelBookingDetailsModels.clear();
        if (showWhat == 1) {
            isVisiable = true;
            historyTab.setTextColor(getResources().getColor(R.color.disable_text));
            historyView.setVisibility(View.INVISIBLE);
            scheduleTab.setTextColor(getResources().getColor(R.color.footerBack));
            scheduleView.setVisibility(VISIBLE);
//            getFromDb();
            //modelBookingDetailsModels = mDatabaseOperations.getActiveBookings();
        }

        if (showWhat == 2) {
            isVisiable = false;
            scheduleTab.setTextColor(getResources().getColor(R.color.disable_text));
            scheduleView.setVisibility(View.INVISIBLE);
            historyTab.setTextColor(getResources().getColor(R.color.footerBack));
            historyView.setVisibility(VISIBLE);
//            getFromDb();
            //modelBookingDetailsModels = mDatabaseOperations.getHistoryBookings();
        }

        if (!isLoadedForSchedule) {
            isLoadedForSchedule = true;
            isVisiable = true;
//            new GetBookingList(showWhat, true).execute();
            new Manager_GetBookingList(true,getContext(), p, "" + showWhat, true, listener_getAllBooking).execute();
        } else if (!isLoadedForHistory) {
            isLoadedForHistory = true;
            isVisiable = false;
//            new GetBookingList(showWhat, true).execute();
            new Manager_GetBookingList(true,getContext(), p, "" + showWhat, true, listener_getAllBooking).execute();

        } else {
            getFromDb();
        }
    }

    private void loadList() {
        listenerCancelBookingApp = result -> {
            String bookingRef = "";
            if (result.startsWith("CANCEL")) {
                bookingRef = result.replace("CANCEL", "");
                new Manager_CancelBookingApp(getContext(), listenerCancelBookingApp).execute(new String[]{"CANCEL", bookingRef});
            } else {
                try {
                    if (result != null) {
                        JSONObject parentObject = new JSONObject(result);
                        if (parentObject.getBoolean("HasError")) {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("")
                                    .setContentText(parentObject.getString("Message"))
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
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
                        } else {
                            if (checkIfHasNullForString(parentObject.optString("TokenValidate")).equals("ValidToken")) {
                                try {
                                    mDatabaseOperations.UpdateBookingStatus(CommonVariables.STATUS_CANCLED, bookingRef);
                                    mHelper.removeRefNoAsapValue(bookingRef);
                                    if (mDatabaseOperations.getActiveBookings().size() <= 0) {
                                        ((Fragment_Main) context).unBindService();
                                    }
                                    // foreground service setting
                                    if (sp.getString(CommonVariables.Enable_ForeGround_Service, "0").equals("1")) {
                                        ((FragmentActivity) context).stopService(new Intent(context, Service_NotifyStatus.class));
                                    }
                                    Log.d("TAG", "CALL AFTER CANCELLED getFromDb :BEFORE CANCELLED  ");
                                    showCancel();
                                } catch (Exception e) {
                                    Toast.makeText(context, "Unable to cancel booking now, try again later.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(context, "Unable to cancel booking now, try again later.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        try {
            Collections.sort(modelBookingDetailsModels, (o1, o2) -> Integer.valueOf(o2.getRefrenceNo()).compareTo(Integer.valueOf((o1.getRefrenceNo()))));
            bookingAdapter = new BookingAdapter(context, modelBookingDetailsModels, showWhat, sp, mHelper, listenerCancelBookingApp);
            rv.setAdapter(bookingAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        bookingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isVisiable = false;
    }

    public void _refreshList(ArrayList<Model_BookingDetailsModel> modelBookingDetailsModels) {
        try {
            this.modelBookingDetailsModels.clear();
            this.modelBookingDetailsModels.addAll(modelBookingDetailsModels);
//            this.modelBookingDetailsModels = modelBookingDetailsModels;
//        bookingAdapter.notifyDataSetChanged();
//        bookingAdapter.refreshList(modelBookingDetailsModels);
            loadList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFromServer(Boolean shouldShowProgress) {
        isLoadedForSchedule = true;
//        new GetBookingList(showWhat, true).execute();
        new Manager_GetBookingList(shouldShowProgress,getContext(), p, "" + showWhat, true, listener_getAllBooking).execute();
    }

    public void getFromDb() {
        modelBookingDetailsModels.clear();
        modelBookingDetailsModels = ((showWhat == 1) ? mDatabaseOperations.getActiveBookings() : mDatabaseOperations.getHistoryBookings());
        Log.d("TAG", "CALL AFTER CANCELLED getFromDb:  " + modelBookingDetailsModels.size());
        try {
            notFoundTv.setVisibility((modelBookingDetailsModels.size() == 0) ? VISIBLE : GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            rv.setVisibility((modelBookingDetailsModels.size() == 0) ? GONE : VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadList();
    }


    private void registerBroadcastRecevier(){
        if (sp.getString(CommonVariables.Enable_ForeGround_Service, "0").equals("0")) {
            try {
                bm = LocalBroadcastManager.getInstance(getContext());
                IntentFilter actionReceiver = new IntentFilter();
                actionReceiver.addAction(CommonVariables.refresh_booking_action);
                bm.registerReceiver(onStatusChanged, actionReceiver);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (bm != null && onStatusChanged != null) {
                bm.unregisterReceiver(onStatusChanged);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private BroadcastReceiver onStatusChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
               getFromServer(false);
            }
        }
    };
}
