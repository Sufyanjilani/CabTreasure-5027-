package base.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.eurosoft.customerapp.BuildConfig;
import com.eurosoft.customerapp.R;
import com.tfb.fbtoast.FBToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.DialogFragment;

import base.utils.CommonVariables;
import base.listener.Listener_OnSetResult;

public class
Fragment_PickupTime extends DialogFragment implements OnClickListener {
    private TimePicker mTimePicker;
    private DatePicker mDatePicker;
    Button cancel;
    RelativeLayout asap, min15, min20;
    TextView min15txt, min20txt, minasaptxt;
    public static final String KEY_TIME = "time";
    public static final String KEY_DATE = "date";

    /*
     *	@author: Kumail Raza Lakhani
     *	Date: 29-June-2016
     */

    Date select;
    int day, month, year, hour, customHour, customdate, mdate, min;
    int curhour, currmin;
    /*
     *	Date: 29-June-2016
     *	END ->
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, getTheme());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.new_pickup_time_dialog, container, false);
        asap = (RelativeLayout) ret.findViewById(R.id.asaptime);
        min15 = (RelativeLayout) ret.findViewById(R.id.fiveteenT);
        min20 = (RelativeLayout) ret.findViewById(R.id.TwentyT);
        minasaptxt = (TextView) ret.findViewById(R.id.txtasap);
        min15txt = (TextView) ret.findViewById(R.id.txt15);
        min20txt = (TextView) ret.findViewById(R.id.txt20);
        ret.findViewById(R.id.btnDone).setOnClickListener(this);
        ret.findViewById(R.id.btnASAP).setOnClickListener(this);
        ret.findViewById(R.id.asaptime).setOnClickListener(this);
        ret.findViewById(R.id.fiveteenT).setOnClickListener(this);
        ret.findViewById(R.id.TwentyT).setOnClickListener(this);
        mTimePicker = (TimePicker) ret.findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        mDatePicker = (DatePicker) ret.findViewById(R.id.datePicker);
        mDatePicker.setMinDate(System.currentTimeMillis() - 1000);

        currmin = mTimePicker.getCurrentMinute();
        curhour = mTimePicker.getCurrentHour();
        cancel = (Button) ret.findViewById(R.id.btncancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //getFragmentManager().popBackStack();
            }
        });


        /*
         *	@author: Kumail Raza Lakhani
         *	Date: 29-June-2016
         */
        ///if(BuildConfig.FLAVOR.equals("PurpleMincab")) {


        if (BuildConfig.FLAVOR.equals("whizcars") || BuildConfig.FLAVOR.equals("PurpleMincab")) {
//			  day = mDatePicker.getDayOfMonth() ;
//			  month = mDatePicker.getMonth();
//			  year = mDatePicker.getYear();
//
//			  hour = mTimePicker.getCurrentHour();
//			  min = mTimePicker.getCurrentMinute();
//
//			   if(BuildConfig.FLAVOR.equals("PurpleMincab")) {
//				  customHour = mTimePicker.getCurrentHour() + 6;
//				   customdate = mDatePicker.getDayOfMonth() + 6;
//			      }else {
//				   customHour = mTimePicker.getCurrentHour() + 2;
//			  }

            int hour = 0;

            if (BuildConfig.FLAVOR.equals("whizcars"))
                hour = 2;
            else if (BuildConfig.FLAVOR.equals("PurpleMincab"))
                hour = 6;


            day = mDatePicker.getDayOfMonth();
            month = mDatePicker.getMonth();
            year = mDatePicker.getYear();


            //mDatePicker.updateDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
			/*  Calendar cal = Calendar.getInstance();
			    cal.add(Calendar.HOUR, hour);
			//  select =cal.getTime();

		//	cal.set(year, month, day, hour, min);

			  mDatePicker.updateDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
			  mTimePicker.setCurrentHour(cal.get(Calendar.HOUR));
			  mTimePicker.setCurrentMinute(cal.get(Calendar.MINUTE));*/

        }
        /*
         *	Date: 29-June-2016
         *	END ->
         */

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnDone) {

			/*Calendar call = Calendar.getInstance();
			call.add(Calendar.HOUR, hour);
			mDatePicker.updateDate(call.get(Calendar.YEAR),call.get(Calendar.MONTH), call.get(Calendar.DATE));
			mTimePicker.setCurrentHour(call.get(Calendar.HOUR));
			mTimePicker.setCurrentMinute(call.get(Calendar.MINUTE));*/

            day = mDatePicker.getDayOfMonth();
            month = mDatePicker.getMonth();
            year = mDatePicker.getYear();

            hour = mTimePicker.getCurrentHour();
            min = mTimePicker.getCurrentMinute();
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day, hour, min);





            /*
             *	@author: Kumail Raza Lakhani
             *	Date: 29-June-2016
             *	Restricting user can only make booking after 2 hours from current time or later.
             *	Whiz Cars
             */

////			OLD CODE
//			Calendar now = Calendar.getInstance();
//			now.add(Calendar.MINUTE, 10);
//
//			if (!cal.before(now)) {
//
//				Intent intent = new Intent();
//				intent.putExtra(KEY_DATE, CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_DATE));
//				intent.putExtra(KEY_TIME, CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_TIME));
//
//				if (mListener != null)
//					mListener.setResult(intent);
//				dismiss();
//			} else
//				Toast.makeText(getActivity(), "Please Select Date and time 10 minutes from now", Toast.LENGTH_SHORT).show();

            //if(BuildConfig.FLAVOR.equals("PurpleMincab")) {
            if (BuildConfig.FLAVOR.equals("whizcars") || BuildConfig.FLAVOR.equals("PurpleMincab")) {
                Calendar calendar = Calendar.getInstance();

                if (BuildConfig.FLAVOR.equals("PurpleMincab")) {
                    int hours = 6;
                    calendar.add(Calendar.HOUR, hours);

                } else {


                    calendar.add(Calendar.HOUR, 2);
                }
//				calendar.add(Calendar.MINUTE, -1);

                Date c = calendar.getTime();




				/*SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
				//Date date = new Date();
			    time = _12HourSDF.format(c);*/
                //Log.v("TAG URL Data ", String.valueOf(time));


                Date bookingDateTime = cal.getTime();

				/*_12HourSDF = new SimpleDateFormat("hh:mm a");

				String selecttime = _12HourSDF.format(bookingDateTime);

				Log.v("TAG URL Data ", String.valueOf(bookingDateTime));*/
                //if(c >= bookingDateTime)
                if (c.after(bookingDateTime)) {
                    // error msg
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Alert:")
                                    .setMessage("Please make a booking After 6 Hour.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            // TODO
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();
                        }
                    });

                } else {
                    //Calendar now = Calendar.getInstance();
                    //now.add(Calendar.MINUTE, 30);

                    //if (!cal.before(now)) {

                    //if (cal.before(now)) {
                    Intent intent = new Intent();
                    intent.putExtra(KEY_DATE, CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_DATE));
                    intent.putExtra(KEY_TIME, CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_TIME));
                    CommonVariables.Timestatus = 1;

                    if (mListener != null)
                        mListener.setResult(intent);


                    dismiss();
                    //} else
                    //Toast.makeText(getActivity(), "Please Select Date and time 10 minutes from now", Toast.LENGTH_SHORT).show();
                }
            }
            //this
            else {
                Calendar now = Calendar.getInstance();
                now.add(Calendar.MINUTE, 10);
                try {
                    if (getArguments() != null && !getArguments().getString(Fragment_PickupDateTime.KEY_RAW_DATE).equals("")) {
                        Date date1 = new SimpleDateFormat(CommonVariables.getDateFormat(CommonVariables.FORMAT_DATE), Locale.getDefault()).parse(getArguments().getString(Fragment_PickupDateTime.KEY_RAW_DATE));

                        if (date1.getDate() > now.get(Calendar.DAY_OF_MONTH)) {
                            Intent intent = new Intent();
                            intent.putExtra(KEY_DATE, getArguments().getString(Fragment_PickupDateTime.KEY_RAW_DATE));
                            intent.putExtra(KEY_TIME, CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_TIME));

                            if (mListener != null)
                                mListener.setResult(intent);
                            dismiss();
                        } else if (date1.getDate() < now.get(Calendar.DAY_OF_MONTH) && date1.getMonth() > now.get(Calendar.MONTH)) {
                            Intent intent = new Intent();
                            intent.putExtra(KEY_DATE, getArguments().getString(Fragment_PickupDateTime.KEY_RAW_DATE));
                            intent.putExtra(KEY_TIME, CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_TIME));

                            if (mListener != null)
                                mListener.setResult(intent);
                            dismiss();
                        } else if (date1.getDate() == now.get(Calendar.DAY_OF_MONTH)) {
                            if (!cal.before(now)) {

                                Intent intent = new Intent();
                                intent.putExtra(KEY_DATE, CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_DATE));
                                intent.putExtra(KEY_TIME, CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_TIME));

                                if (mListener != null)
                                    mListener.setResult(intent);
                                dismiss();
                                //getFragmentManager().popBackStack();
                            } else {
                                FBToast.warningToast(getActivity(), "Please select time 10 minutes from now", FBToast.LENGTH_SHORT);
                            }
                        } else if (date1.getYear() > now.getTime().getYear()) {
                            Intent intent = new Intent();
                            intent.putExtra(KEY_DATE, getArguments().getString(Fragment_PickupDateTime.KEY_RAW_DATE));
                            intent.putExtra(KEY_TIME, CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_TIME));

                            if (mListener != null)
                                mListener.setResult(intent);
                            dismiss();
                        }
                    } else {
                        if (!cal.before(now)) {

                            Intent intent = new Intent();
                            intent.putExtra(KEY_DATE, CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_DATE));
                            intent.putExtra(KEY_TIME, CommonVariables.getFormattedDate(cal, CommonVariables.FORMAT_TIME));

                            if (mListener != null)
                                mListener.setResult(intent);
                            dismiss();
                            //getFragmentManager().popBackStack();
                        } else {
                            FBToast.warningToast(getActivity(), "Please select time 10 minutes from now", FBToast.LENGTH_SHORT);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
            /*
             *	Date: 29-June-2016
             *	END ->
             */
        } else if (v.getId() == R.id.btnASAP) {
            Calendar now = Calendar.getInstance();
            /*
             *	@author: Kumail Raza Lakhani
             *	Date: 12-July-2016
             *	Restricting user can only make booking after 2 hours from current time or later.
             *	Whiz Cars
             */
			/*if(BuildConfig.FLAVOR.equals("PurpleMincab"))
				now.add(Calendar.HOUR, 6);*/

            if (BuildConfig.FLAVOR.equals("whizcars") || BuildConfig.FLAVOR.equals("PurpleMincab"))

                if (BuildConfig.FLAVOR.equals("PurpleMincab")) {
                    now.add(Calendar.HOUR, 6);
                } else {
                    now.add(Calendar.HOUR, 2);
                }
            /*
             *	Date: 12-July-2016
             *	END ->
             */
            //	now.add(Calendar.MINUTE, 10);

            Intent intent = new Intent();
            intent.putExtra(KEY_DATE, CommonVariables.getFormattedDate(now, CommonVariables.FORMAT_DATE));
            intent.putExtra(KEY_TIME, CommonVariables.getFormattedDate(now, CommonVariables.FORMAT_TIME));

            if (mListener != null)
                mListener.setResult(intent);
            dismiss();
        } else if (v.getId() == R.id.asaptime) {
            if (getActivity() != null) {
                if (getResources() != null) {
                    minasaptxt.setTextColor(getResources().getColor(R.color.app_bg_white));
                    min15txt.setTextColor(getResources().getColor(R.color.vehicle_txt));
                    min20txt.setTextColor(getResources().getColor(R.color.vehicle_txt));
                    asap.setBackground(getResources().getDrawable(R.drawable.avatar_circle));
                    min15.setBackground(getResources().getDrawable(R.drawable.avatar_circle));
                    min20.setBackground(getResources().getDrawable(R.drawable.avatar_circle));
                }
            }
            day = mDatePicker.getDayOfMonth();
            month = mDatePicker.getMonth();
            year = mDatePicker.getYear();
            final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs


            hour = mTimePicker.getCurrentHour();
            min = mTimePicker.getCurrentMinute();
            int currentmin = currmin + 10;
            //customHour = mTimePicker.getCurrentMinute();

            Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.MINUTE, 10);
            cal.set(year, month, day, min, hour);
//if(currentmin>=50){
            Calendar date = Calendar.getInstance();
            long t = date.getTimeInMillis();
            Date afterAddingTenMins = new Date(t + (10 * ONE_MINUTE_IN_MILLIS));
            mTimePicker.setCurrentHour(afterAddingTenMins.getHours());
            mTimePicker.setCurrentMinute(afterAddingTenMins.getMinutes());
//}else{
//	mTimePicker.setCurrentHour(curhour);
//	mTimePicker.setCurrentMinute(currentmin);
//}

            /*
             *	Date: 12-July-2016
             *	END ->
             */
            //	now.add(Calendar.MINUTE, 10);

//			Intent intent = new Intent();
//			intent.putExtra(KEY_DATE, CommonVariables.getFormattedDate(now, CommonVariables.FORMAT_DATE));
//			intent.putExtra(KEY_TIME, CommonVariables.getFormattedDate(now, CommonVariables.FORMAT_TIME));
//
//			if (mListener != null)
//				mListener.setResult(intent);
//			dismiss();
        } else if (v.getId() == R.id.fiveteenT) {
            final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
            if (getActivity() != null) {
                if (getResources() != null) {
                    minasaptxt.setTextColor(getResources().getColor(R.color.vehicle_txt));
                    min15txt.setTextColor(getResources().getColor(R.color.app_bg_white));
                    min20txt.setTextColor(getResources().getColor(R.color.vehicle_txt));
                    asap.setBackground(getResources().getDrawable(R.drawable.avatar_circle));
                    min15.setBackground(getResources().getDrawable(R.drawable.avatar_circle));
                    min20.setBackground(getResources().getDrawable(R.drawable.avatar_circle));
                }
            }
            Calendar date = Calendar.getInstance();
            long t = date.getTimeInMillis();
            Date afterAddingTenMins = new Date(t + (15 * ONE_MINUTE_IN_MILLIS));
            day = mDatePicker.getDayOfMonth();
            month = mDatePicker.getMonth();
            year = mDatePicker.getYear();

            int currentmin = currmin + 15;
            hour = mTimePicker.getCurrentHour();
            min = mTimePicker.getCurrentMinute();

            customHour = mTimePicker.getCurrentMinute();

            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day, hour, min);

            mTimePicker.setCurrentHour(afterAddingTenMins.getHours());
            mTimePicker.setCurrentMinute(afterAddingTenMins.getMinutes());


            /*
             *	Date: 12-July-2016
             *	END ->
             */
            //	now.add(Calendar.MINUTE, 10);

//			Intent intent = new Intent();
//			intent.putExtra(KEY_DATE, CommonVariables.getFormattedDate(now, CommonVariables.FORMAT_DATE));
//			intent.putExtra(KEY_TIME, CommonVariables.getFormattedDate(now, CommonVariables.FORMAT_TIME));
//
//			if (mListener != null)
//				mListener.setResult(intent);
//			dismiss();
        } else if (v.getId() == R.id.TwentyT) {

            day = mDatePicker.getDayOfMonth();
            month = mDatePicker.getMonth();
            year = mDatePicker.getYear();
            int currentmin = currmin + 20;
            if (getActivity() != null) {
                if (getResources() != null) {
                    minasaptxt.setTextColor(getResources().getColor(R.color.vehicle_txt));
                    min15txt.setTextColor(getResources().getColor(R.color.vehicle_txt));
                    min20txt.setTextColor(getResources().getColor(R.color.app_bg_white));
                    asap.setBackground(getResources().getDrawable(R.drawable.avatar_circle));
                    min15.setBackground(getResources().getDrawable(R.drawable.avatar_circle));
                    min20.setBackground(getResources().getDrawable(R.drawable.avatar_circle));
                }
            }
            hour = mTimePicker.getCurrentHour();
            min = mTimePicker.getCurrentMinute();

            customHour = mTimePicker.getCurrentMinute() + 20;

            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day, hour, min);
            Calendar date = Calendar.getInstance();
            final long ONE_MINUTE_IN_MILLIS = 60000;
            long t = date.getTimeInMillis();
            Date afterAddingTenMins = new Date(t + (20 * ONE_MINUTE_IN_MILLIS));
            mTimePicker.setCurrentHour(afterAddingTenMins.getHours());
            mTimePicker.setCurrentMinute(afterAddingTenMins.getMinutes());

            /*
             *	Date: 12-July-2016
             *	END ->
             */
            //	now.add(Calendar.MINUTE, 10);

//			Intent intent = new Intent();
//			intent.putExtra(KEY_DATE, CommonVariables.getFormattedDate(now, CommonVariables.FORMAT_DATE));
//			intent.putExtra(KEY_TIME, CommonVariables.getFormattedDate(now, CommonVariables.FORMAT_TIME));
//
//			if (mListener != null)
//				mListener.setResult(intent);
//			dismiss();
        }
    }

    private Listener_OnSetResult mListener;

    public void setOnSetResultListener(Listener_OnSetResult result) {
        mListener = result;
    }

    /*
     *	@author: Kumail Raza Lakhani
     *	Date: 29-June-2016
     *	Restricting user can only make booking after 2 hours from current time or later.
     *	Whiz Cars
     */
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
    /*
     *	Date: 29-June-2016
     *	END ->
     */

}
