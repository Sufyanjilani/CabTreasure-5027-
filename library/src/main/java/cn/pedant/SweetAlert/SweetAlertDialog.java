package cn.pedant.SweetAlert;


import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SweetAlertDialog extends Dialog implements View.OnClickListener {
    public static final String CurrencySymbol = "CurrencySymbol";
    private View mDialogView;
    public static final String ShowFares = "showFares";
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private boolean isCustomButtonColorProvidedForConfirm = false;
    private Animation mErrorInAnim;
    private AnimationSet mErrorXInAnim;
    private AnimationSet mSuccessLayoutAnimSet;
    private Animation mSuccessBowAnim;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private String mTitleText;
    private String mContentText;
    private int mSelectedCar, mSelectedPayment;
    private int mSelectedVia;
    private boolean mShowCancel;
    private boolean mShowContent;
    private String mCancelText;
    private String mConfirmText;

    private  String confirmButtonColor;

    String[] paymentArr;
    private ArrayList<AnyVehicleFareModel> mAnyFareList;
    private ViaAddresses[] mViaList;
    private int mAlertType;
    private FrameLayout mErrorFrame;
    private FrameLayout mUpdateFrame;
    private FrameLayout mFlightFrame;
    private FrameLayout mSuccessFrame;
    private FrameLayout mProgressFrame, edit_viaframe;
    private SuccessTickView mSuccessTick;
    private ImageView mUpdateX;
    private ImageView mErrorX;
    private View mSuccessLeftMask;
    private View mSuccessRightMask;
    private Drawable mCustomImgDrawable;
    private ImageView mCustomImage;
    private Button mConfirmButton;
    private Button mCancelButton;
    ProgressBar progressBar;
    private ProgressHelper mProgressHelper;
    private FrameLayout mWarningFrame, edit_frame, list_frame;
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;
    private OnSweetItemClickListener mItemClickListener;
    private OnSweetListChangeListener mListChangeListener;
    private OnSweetViaDetailListener mViaDetailsListener;
    private boolean mCloseFromCancel;

    public static final int NORMAL_TYPE = 0;
    public static final int UPDATE_TYPE = 12;

    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;
    public static final int EDITTEXT_TYPE = 6;
    public static final int LIST_TYPE = 7;
    public static final int LIST_TYPE_VIA = 8;
    public static final int VIA_INPUT = 9;
    public static final int FLIGHT_INPUT = 10;
    public static final int PAYMENT_LIST_TYPE = 11;
    private EditText not_txt, viaName, viaphone;
    private String mInputText;
    RecyclerView Carlistview;
    private EditText flighttxt, flightcoming;
    private OnSweetFlightListener mFlightListener;
    private RadioGroup airportGrp;
    private ImageView clearall;

    public static interface OnSweetClickListener {
        public void onClick(SweetAlertDialog sweetAlertDialog);

        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText);
    }

    public static interface OnSweetItemClickListener {
        public void onClick(SweetAlertDialog sweetAlertDialog, AnyVehicleFareModel anyVehicleFareModel, int selectpos);
    }

    public static interface OnSweetListChangeListener {
        public void onClick(SweetAlertDialog sweetAlertDialog, ViaAddresses viaAddresses, int selectpos);
    }

    public static interface OnSweetViaDetailListener {
        public void onClick(SweetAlertDialog sweetAlertDialog, String name, String mobile);
    }

    public static interface OnSweetFlightListener {
        public void onClick(SweetAlertDialog sweetAlertDialog, String flightNo, String comingFrom, String atAirport);
    }

    public SweetAlertDialog(Context context) {
        this(context, NORMAL_TYPE);
    }

    public Context mContext;
    boolean isShowVehicleFare = true;

    public SweetAlertDialog(Context context, int alertType, boolean isShowVehicleFare) {
        super(context, R.style.alert_dialog);
        this.isShowVehicleFare = isShowVehicleFare;
        mContext = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mProgressHelper = new ProgressHelper(context);
        mAlertType = alertType;
        mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);
        // 2.3.x system don't support alpha-animation on layer-list drawable
        // remove it from animation set
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            List<Animation> childAnims = mErrorXInAnim.getAnimations();
            int idx = 0;
            for (; idx < childAnims.size(); idx++) {
                if (childAnims.get(idx) instanceof AlphaAnimation) {
                    break;
                }
            }
            if (idx < childAnims.size()) {
                childAnims.remove(idx);
            }
        }
        mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.success_bow_roate);
        mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);

        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            SweetAlertDialog.super.cancel();
                        } else {
                            SweetAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        // dialog overlay fade out
        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = getWindow().getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);
    }

    public SweetAlertDialog(Context context, int alertType) {
        super(context, R.style.alert_dialog);
        mContext = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mProgressHelper = new ProgressHelper(context);
        mAlertType = alertType;
        mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);
        // 2.3.x system don't support alpha-animation on layer-list drawable
        // remove it from animation set
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            List<Animation> childAnims = mErrorXInAnim.getAnimations();
            int idx = 0;
            for (; idx < childAnims.size(); idx++) {
                if (childAnims.get(idx) instanceof AlphaAnimation) {
                    break;
                }
            }
            if (idx < childAnims.size()) {
                childAnims.remove(idx);
            }
        }
        mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.success_bow_roate);
        mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);

        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            SweetAlertDialog.super.cancel();
                        } else {
                            SweetAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // dialog overlay fade out
        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = getWindow().getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);
    }

    //ToggleSwitch toggleSwitch;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleTextView = (TextView) findViewById(R.id.title_text);
        mContentTextView = (TextView) findViewById(R.id.content_text);
        mErrorFrame = (FrameLayout) findViewById(R.id.error_frame);
        mErrorX = (ImageView) mErrorFrame.findViewById(R.id.error_x);
        mUpdateFrame = (FrameLayout) findViewById(R.id.update_frame);
        mUpdateX = (ImageView) mUpdateFrame.findViewById(R.id.update_x);
        mSuccessFrame = (FrameLayout) findViewById(R.id.success_frame);
        mFlightFrame = (FrameLayout) findViewById(R.id.edit_flightframe);
        edit_viaframe = (FrameLayout) findViewById(R.id.edit_viaframe);
        not_txt = (EditText) findViewById(R.id.not_txt);
        clearall = (ImageView) findViewById(R.id.clearall);
        clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                not_txt.setText("");
            }
        });
        flighttxt = (EditText) findViewById(R.id.flight_notinp);
        viaphone = (EditText) findViewById(R.id.via_mobile);
        viaName = (EditText) findViewById(R.id.via_name);
        flightcoming = (EditText) findViewById(R.id.flight_coming);
        airportGrp = (RadioGroup) findViewById(R.id.airportGrp);
        mProgressFrame = (FrameLayout) findViewById(R.id.progress_dialog);
        mSuccessTick = (SuccessTickView) mSuccessFrame.findViewById(R.id.success_tick);
        mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
        mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
        mCustomImage = (ImageView) findViewById(R.id.custom_image);
        mWarningFrame = (FrameLayout) findViewById(R.id.warning_frame);
        edit_frame = (FrameLayout) findViewById(R.id.edit_frame);
        list_frame = (FrameLayout) findViewById(R.id.list_frame);
        mConfirmButton = (Button) findViewById(R.id.confirm_button);//start from set Adapter
        mCancelButton = (Button) findViewById(R.id.cancel_button);
        Carlistview = (RecyclerView) findViewById(R.id.carslistview);
        mProgressHelper.setProgressWheel((ProgressBar) findViewById(R.id.progressWheel));
        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        setTitleText(mTitleText);
        setContentText(mContentText);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        setConfirmButtonBackgroundColor(confirmButtonColor);
        setInput(mInputText);
        if (mAlertType == LIST_TYPE) {

            setListAdapter(mAnyFareList, mSelectedCar);
        }
        if (mAlertType == PAYMENT_LIST_TYPE) {

            setPaymentAdapter(paymentArr, mSelectedPayment);
        }
        if (mAlertType == LIST_TYPE_VIA) {
            setViaListAdapter(mViaList);
        }
        changeAlertType(mAlertType, true);
//        try {
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.copyFrom(this.getWindow().getAttributes());
//                int dialogWidth = lp.width;
//                int dialogHeight = lp.height;
//
//                if (dialogHeight > 500) {
//                    this.getWindow().setLayout(dialogWidth, 500);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
    }

    private void restore() {
        mCustomImage.setVisibility(View.GONE);
        mErrorFrame.setVisibility(View.GONE);
        mUpdateFrame.setVisibility(View.GONE);
        mSuccessFrame.setVisibility(View.GONE);
        mWarningFrame.setVisibility(View.GONE);
        mProgressFrame.setVisibility(View.GONE);
        mConfirmButton.setVisibility(View.VISIBLE);

        mConfirmButton.setBackgroundResource(R.drawable.blue_button_background);
        mErrorFrame.clearAnimation();
        mErrorX.clearAnimation();
        mUpdateX.clearAnimation();
        mSuccessTick.clearAnimation();
        mSuccessLeftMask.clearAnimation();
        mSuccessRightMask.clearAnimation();
    }

    private void playAnimation() {
        if (mAlertType == UPDATE_TYPE) {
            mUpdateFrame.startAnimation(mErrorInAnim);
            mUpdateX.startAnimation(mErrorXInAnim);
        } else if (mAlertType == ERROR_TYPE) {
            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        } else if (mAlertType == SUCCESS_TYPE) {
            mSuccessTick.startTickAnim();
            mSuccessRightMask.startAnimation(mSuccessBowAnim);
        }
    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
                restore();
            }
            switch (mAlertType) {
                case UPDATE_TYPE:
                    mUpdateFrame.setVisibility(View.VISIBLE);
                    break;
                case ERROR_TYPE:
                    mErrorFrame.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS_TYPE:
                    mSuccessFrame.setVisibility(View.VISIBLE);
                    // initial rotate layout of success mask
                    mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                    mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
                    break;

                case WARNING_TYPE:
                    //COMMENT HERE
                    if(!isCustomButtonColorProvidedForConfirm) {
                        mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    }
                    mCancelButton.setBackgroundResource(R.drawable.gray_button_background);
                    mWarningFrame.setVisibility(View.VISIBLE);
                    break;
                case EDITTEXT_TYPE:
//                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    mContentTextView.setVisibility(View.GONE);
                    edit_frame.setVisibility(View.VISIBLE);
                    break;
                case FLIGHT_INPUT:
//                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    mContentTextView.setVisibility(View.GONE);
                    mFlightFrame.setVisibility(View.VISIBLE);
                    break;
                case LIST_TYPE:
//                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    mContentTextView.setVisibility(View.GONE);
                    list_frame.setVisibility(View.VISIBLE);
                    break;
                case PAYMENT_LIST_TYPE:
//                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    mContentTextView.setVisibility(View.GONE);
                    list_frame.setVisibility(View.VISIBLE);
                    break;
                case VIA_INPUT:
//                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    mContentTextView.setVisibility(View.GONE);
                    edit_viaframe.setVisibility(View.VISIBLE);
                    break;
                case LIST_TYPE_VIA:
//                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
                    mContentTextView.setVisibility(View.GONE);
                    list_frame.setVisibility(View.VISIBLE);
                    break;
                case CUSTOM_IMAGE_TYPE:
                    setCustomImage(mCustomImgDrawable);
                    break;
                case PROGRESS_TYPE:
                    mProgressFrame.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(View.GONE);
                    break;
            }
            if (!fromCreate) {
                playAnimation();
            }
        }
    }

    public int getAlerType() {
        return mAlertType;
    }

    public void changeAlertType(int alertType) {
        changeAlertType(alertType, false);
    }


    public String getTitleText() {
        return mTitleText;
    }

    public SweetAlertDialog setTitleText(String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        if (mTitleTextView != null && text != null && text.equals("")) {
            mTitleTextView.setVisibility(GONE);
        }
        return this;
    }

    public SweetAlertDialog setCustomImage(Drawable drawable) {
        mCustomImgDrawable = drawable;
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage.setVisibility(View.VISIBLE);
            mCustomImage.setImageDrawable(mCustomImgDrawable);
        }
        return this;
    }

    public SweetAlertDialog setCustomImage(int resourceId) {
        return setCustomImage(getContext().getResources().getDrawable(resourceId));
    }

    public String getContentText() {
        return mContentText;
    }

    public SweetAlertDialog setContentText(String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText(true);
            mContentTextView.setText(mContentText);
        }
        return this;
    }

    public boolean isShowCancelButton() {
        return mShowCancel;
    }

    public SweetAlertDialog showCancelButton(boolean isShow) {
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public boolean isShowContentText() {
        return mShowContent;
    }

    public SweetAlertDialog showContentText(boolean isShow) {
        mShowContent = isShow;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public String getCancelText() {
        return mCancelText;
    }

    public SweetAlertDialog setCancelText(String text) {
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
        }
        return this;
    }

    public String getConfirmText() {
        return mConfirmText;
    }

    public String getConfirmButtonColor() {
        return confirmButtonColor;
    }

    public void setConfirmButtonColor(String color) {
       confirmButtonColor =color;
    }

    public SweetAlertDialog setConfirmText(String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public SweetAlertDialog setConfirmButtonBackgroundColor(String color) {
        try {
            confirmButtonColor = color;
            if (mConfirmButton != null && confirmButtonColor != null) {
                int mcolor = Color.parseColor(color);
                Drawable background =ContextCompat.getDrawable(mContext,R.drawable.blue_button_background);

                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable)background).getPaint().setColor(mcolor);
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable)background).setColor(mcolor);;
                }

                else if (background instanceof ColorDrawable) {
                    ((ColorDrawable)background).setColor(mcolor);;
                }
                else if (background instanceof StateListDrawable) {

                    DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) background.getConstantState();
                    Drawable[] children = drawableContainerState.getChildren();
                    Drawable selectedItem = (Drawable) children[0];
                    //Drawable unselectedItem = (Drawable) children[1];
                    GradientDrawable selectedDrawable = (GradientDrawable) selectedItem;
                 //   GradientDrawable unselectedDrawable = (GradientDrawable) unselectedItem;
                    selectedDrawable.setColor(mcolor);
                   // unselectedDrawable.setColor(mcolor);
                    isCustomButtonColorProvidedForConfirm = true;

                        mConfirmButton.setBackground(selectedDrawable);
                }




              //  mConfirmButton.setBack(mcolor);
            }
        }catch (Exception ex){
            isCustomButtonColorProvidedForConfirm = false;

        }
        return this;
    }

    public float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public SweetAlertDialog setListAdapter(ArrayList<AnyVehicleFareModel> text, int selected) {
//        if(mAnyFareList!=nul)
        mAnyFareList = text;
        mSelectedCar = selected;
        if (Carlistview != null && mAnyFareList != null && mContext != null) {

            Carlistview.setAdapter(new Cars_Adapter(mContext, mAnyFareList, selected));
            Carlistview.setLayoutManager(new LinearLayoutManager(mContext));
            if (mAnyFareList.size() >= 6) {
                try {
                    int sizeInPx = (int) convertDpToPx(mContext, 450f);
                    LinearLayout.LayoutParams lp =
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, sizeInPx);
                    Carlistview.setLayoutParams(lp);
                } catch (Exception e) {

                }
            }
//            try {
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.copyFrom(this.getWindow().getAttributes());
//                int dialogWidth = lp.width;
//                int dialogHeight = lp.height;
//
//                if (dialogHeight > 500) {
//                    this.getWindow().setLayout(dialogWidth, 500);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }
        return this;
    }

    public SweetAlertDialog setPaymentAdapter(String[] text, int selected) {
//        if(mAnyFareList!=nul)
        paymentArr = text;
        mSelectedPayment = selected;
        if (Carlistview != null && paymentArr != null && mContext != null) {

            Carlistview.setAdapter(new Payment_Adapter(mContext, text, selected));
            Carlistview.setLayoutManager(new LinearLayoutManager(mContext));
            if (text.length >= 6) {
                try {
                    int sizeInPx = (int) convertDpToPx(mContext, 500f);
                    LinearLayout.LayoutParams lp =
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, sizeInPx);
                    Carlistview.setLayoutParams(lp);
                } catch (Exception e) {

                }
            }
//            try {
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.copyFrom(this.getWindow().getAttributes());
//                int dialogWidth = lp.width;
//                int dialogHeight = lp.height;
//
//                if (dialogHeight > 500) {
//                    this.getWindow().setLayout(dialogWidth, 500);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }
        return this;
    }

    public SweetAlertDialog setViaListAdapter(ViaAddresses[] vialist) {
//        if(mAnyFareList!=nul)
        mViaList = vialist;

        if (Carlistview != null && mViaList != null && mContext != null) {

            Carlistview.setAdapter(new Via_Adapter(mContext, mViaList));
            Carlistview.setLayoutManager(new LinearLayoutManager(mContext));
        }
        return this;
    }

    public SweetAlertDialog setInput(String text) {
        mInputText = text;
        if (not_txt != null) {
            not_txt.setText(mInputText);
            if (mTitleText != null && mTitleText.equals("Driver Notes")) {
                not_txt.setMinLines(4);
                if (clearall != null) {
                    clearall.setVisibility(View.VISIBLE);
                }

            } else if (mTitleText != null && mTitleText.toLowerCase().contains("pickup notes")) {
                not_txt.setHint("Door Number or Navigation Instructions");
                not_txt.setMinLines(1);

            } else if (mTitleText != null && mTitleText.toLowerCase().contains("label")) {
                not_txt.setHint("Gym,School etc");
                not_txt.setMinLines(1);

            } else if (mTitleText != null && mTitleText.toLowerCase().contains("password")) {
                not_txt.setHint("Enter Your Password Here");
                not_txt.setMinLines(1);
                not_txt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else if (mTitleText != null && mTitleText.toLowerCase().contains("mobile number")) {
                not_txt.setHint("Enter Your Mobile Number Here");
                not_txt.setMinLines(1);
                not_txt.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (mTitleText != null && mTitleText.toLowerCase().contains("enter email address")) {
                not_txt.setHint("Enter Your Email Address Here");
                not_txt.setMinLines(1);
//                not_txt.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (mTitleText != null && (mTitleText.toLowerCase().contains("pickup") || mTitleText.toLowerCase().contains("address"))) {
                not_txt.setHint("Edit Address");
                not_txt.setMinLines(1);
                if (clearall != null) {
                    clearall.setVisibility(View.VISIBLE);
                }
//                not_txt.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            }
        }

        return this;
    }

    public SweetAlertDialog setCancelClickListener(OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public SweetAlertDialog setConfirmClickListener(OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    public SweetAlertDialog setItemlickListener(OnSweetItemClickListener listener) {
        mItemClickListener = listener;
        return this;
    }

    public SweetAlertDialog setListChangeListener(OnSweetListChangeListener listener) {
        mListChangeListener = listener;
        return this;
    }

    public SweetAlertDialog setViaDetailListener(OnSweetViaDetailListener listener) {
        mViaDetailsListener = listener;
        return this;
    }

    public SweetAlertDialog setFlightListener(OnSweetFlightListener listener) {
        mFlightListener = listener;
        return this;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
        playAnimation();
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        mConfirmButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(SweetAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                if (mAlertType == EDITTEXT_TYPE) {
                    mConfirmClickListener.onClick(SweetAlertDialog.this, not_txt.getText().toString());
                } else if (mAlertType == LIST_TYPE) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onClick(SweetAlertDialog.this, mAnyFareList.get(mSelectedCar), mSelectedCar);
                    }

//                else if(mAlertType == LIST_TYPE_VIA){
////                    if(mListChangeListener!=null){
////                        mListChangeListener.onClick(SweetAlertDialog.this,mViaList[mSelectedVia],mSelectedVia);
////                    }
//                }
                    else {
                        mConfirmClickListener.onClick(SweetAlertDialog.this);
                    }
                } else if (mAlertType == PAYMENT_LIST_TYPE) {
                    if (mItemClickListener != null) {
                        AnyVehicleFareModel anyVehicleFareModel = new AnyVehicleFareModel();
                        anyVehicleFareModel.setName(paymentArr[mSelectedPayment]);
                        mItemClickListener.onClick(SweetAlertDialog.this, anyVehicleFareModel, mSelectedPayment);
                    }
//                else if(mAlertType == LIST_TYPE_VIA){
////                    if(mListChangeListener!=null){
////                        mListChangeListener.onClick(SweetAlertDialog.this,mViaList[mSelectedVia],mSelectedVia);
////                    }
//                }
                    else {
                        mConfirmClickListener.onClick(SweetAlertDialog.this);
                    }
                } else if (mAlertType == FLIGHT_INPUT) {
                    if (mFlightListener != null) {
                        mFlightListener.onClick(SweetAlertDialog.this, flighttxt.getText().toString(), flightcoming.getText().toString(), airportGrp.getCheckedRadioButtonId() == R.id.yesRadio ? "0" : "1");
                    }
                } else if (mAlertType == VIA_INPUT) {
                    if (mViaDetailsListener != null) {
                        mViaDetailsListener.onClick(SweetAlertDialog.this, viaName.getText().toString(), viaphone.getText().toString());
                    }
                } else {
                    mConfirmClickListener.onClick(SweetAlertDialog.this);
//
                }
            } else {
                dismissWithAnimation();
            }
        }
    }

    public static int selectedPos = 0;

    public ProgressHelper getProgressHelper() {
        return mProgressHelper;
    }

    class Payment_Adapter extends RecyclerView.Adapter<Payment_Adapter.VIEWHOLDER> {

        Context context;
        String[] carNamesList;
        int selectedCar;

        public Payment_Adapter(Context context, String[] VehicleNames, int selectedCar) {
            this.context = context;
            carNamesList = VehicleNames;
            this.selectedCar = selectedCar;

//            CurrSelectedCar=carNames[0];
        }
        //    @Override
//    public int getCount() {
//        return 4;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//        int cars[]={R.drawable.bc_saloon_un, R.drawable.bc_estate_un, R.drawable.bc_mpv_un, R.drawable.bc_executive_un};
//        int selectedcars[]={R.drawable.bc_saloon_s, R.drawable.bc_estate_s, R.drawable.bc_mpv_s, R.drawable.bc_executive_s};
//  List<String> VehicleNames=new DatabaseOperations(new DatabaseHelper(context)).getAllVehiclesNames();

        //{"Saloon","Estate","Executive","MPV","Van"};
        @Override
        public VIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.payment_item_dialogue, parent, false);
            //return convertView;
            VIEWHOLDER viewholder = new VIEWHOLDER(view);

            return viewholder;
        }

        @Override
        public void onBindViewHolder(final VIEWHOLDER holder, @SuppressLint("RecyclerView") final int position) {
            String payment = carNamesList[position];
//            String TempCar=carNames.getName();
//            if(TempCar.contains("CAR")) {
//                holder.cartxt.setText(TempCar.replace(" CAR",""));
//            }else{
//                holder.cartxt.setText(TempCar);
//            }
//            holder.mContainer.setCardBackgroundColor(context.getResources().getColor(R.color.active));
            ((CardView) holder.container).setCardBackgroundColor(context.getResources().getColor(R.color.grey_background));
//holder.container.setBackgroundColor(context.getResources().getColor(R.color.drawer_head_bg));
//            ((CardView) holder.container).setCardBackgroundColor(context.getResources().getColor(R.color.drawer_head_bg));
            if (position == selectedCar) {
                holder.selectMask.setVisibility(View.VISIBLE);
//                if( holder.cartxt.getText().toString().equalsIgnoreCase("saloon")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_saloon_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }else if( holder.cartxt.getText().toString().toLowerCase().equalsIgnoreCase("estate")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_estate_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }
//
//                else if( holder.cartxt.getText().toString().toLowerCase().contains("wheelchair")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_wheelchair_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }else if(holder.cartxt.getText().toString().contains("9")||holder.cartxt.getText().toString().contains("8")|| holder.cartxt.getText().toString().toLowerCase().contains("mini bus")||holder.cartxt.getText().toString().equalsIgnoreCase("7 Seater")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_van_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }
//                else if( holder.cartxt.getText().toString().toLowerCase().contains("Coach")|| holder.cartxt.getText().toString().toLowerCase().contains("bus")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_coach_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }
//                else if(holder.cartxt.getText().toString().toLowerCase().contains("black cab")||holder.cartxt.getText().toString().contains("MPV")||holder.cartxt.getText().toString().equalsIgnoreCase("6 Seater")||holder.cartxt.getText().toString().contains("5")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_mpv_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }else{
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_executive_s));
//                }
//                holder.cartxt.setTextColor(context.getResources().getColor(R.color.car_underline));

            } else {
                holder.selectMask.setVisibility(View.GONE);
//                holder.carimg.setBackgroundColor(context.getResources().getColor(R.color.drawer_head_bg));
//                holder.mContainer.setCardBackgroundColor(context.getResources().getColor(R.color.drawer_head_bg));
//                if( holder.cartxt.getText().toString().equalsIgnoreCase("saloon")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_saloon_s));
//                }else if( holder.cartxt.getText().toString().toLowerCase().equalsIgnoreCase("estate")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_estate_s));
//                }
//                else if( holder.cartxt.getText().toString().toLowerCase().contains("wheelchair")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_wheelchair_s));
//                }
//                else if(holder.cartxt.getText().toString().contains("9")||holder.cartxt.getText().toString().contains("8")||holder.cartxt.getText().toString().equalsIgnoreCase("7 Seater")|| holder.cartxt.getText().toString().toLowerCase().contains("mini bus")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_van_s));
//                }
//                else if( holder.cartxt.getText().toString().toLowerCase().contains("Coach")|| holder.cartxt.getText().toString().toLowerCase().contains("bus")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_coach_s));
//                }
//                else if( holder.cartxt.getText().toString().equalsIgnoreCase("7 Seater")||holder.cartxt.getText().toString().toLowerCase().contains("black cab")||holder.cartxt.getText().toString().contains("MPV")||holder.cartxt.getText().toString().equalsIgnoreCase("6 Seater")||holder.cartxt.getText().toString().contains("5")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_mpv_s));
//                }else{
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_executive_s));
//                }
//                holder.carimg.setImageDrawable(context.getResources().getDrawable(cars[position]));
//                holder.cartxt.setTextColor(context.getResources().getColor(R.color.vehicle_txt));
            }
//            holder.carFares.setText("\u00A3 "+carNames.getSingleFare());
//            if( isShowVehicleFare){
//                holder.carFares.setVisibility(View.VISIBLE);
//            }else{
//                holder.carFares.setVisibility(View.GONE);
//            }
//            holder.carHlag.setText(carNames.getHandLuggagese());
//            holder.carlag.setText(carNames.getSuitCase());
            if (payment.toLowerCase().equals("cash")) {
                holder.imageCard.setImageDrawable(context.getResources().getDrawable(R.drawable.p_cash));
            } else if (payment.toLowerCase().equals("account")) {
                holder.imageCard.setImageDrawable(context.getResources().getDrawable(R.drawable.p_account));
            } else {
                holder.imageCard.setImageDrawable(context.getResources().getDrawable(R.drawable.p_card));
            }
            holder.cartxt.setText(payment);
//            holder.carPass.setText(carNames.getTotalPassengers());
//            if(position==carNames.length-1){
//                holder.frw_line.setVisibility(View.VISIBLE);
//            }


            holder.container.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    selectedCar = position;
                    mSelectedPayment = position;
                    notifyDataSetChanged();
                  /*  YoYo.with(Techniques.BounceIn)
                            .duration(400)
                            .repeat(0)
                            .playOn(holder.container);*/

                }
            });

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return carNamesList.length;
        }

        class VIEWHOLDER extends RecyclerView.ViewHolder {
            ImageView imageCard;


            TextView cartxt;
            //            TextView   carPass,carHlag,carlag,carFares;
            View container, frw_line, selectMask;

            public VIEWHOLDER(View itemView) {
                super(itemView);
                container = itemView;
                selectMask = (View) itemView.findViewById(R.id.selectMask);
                imageCard = (ImageView) itemView.findViewById(R.id.imageCard);

                cartxt = (TextView) itemView.findViewById(R.id.carname);
//                carPass=(TextView) itemView.findViewById(R.id.carPass) ;
//                carHlag=(TextView) itemView.findViewById(R.id.carhlag) ;
//                carlag=(TextView) itemView.findViewById(R.id.carlag) ;
//                carFares=(TextView) itemView.findViewById(R.id.carFares) ;

            }
        }

    }

    class Cars_Adapter extends RecyclerView.Adapter<Cars_Adapter.VIEWHOLDER> {

        Context context;
        ArrayList<AnyVehicleFareModel> carNamesList;
        int selectedCar;

        public Cars_Adapter(Context context, ArrayList<AnyVehicleFareModel> VehicleNames, int selectedCar) {
            this.context = context;
            carNamesList = VehicleNames;
            this.selectedCar = selectedCar;

//            CurrSelectedCar=carNames[0];
        }
        //    @Override
//    public int getCount() {
//        return 4;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//        int cars[]={R.drawable.bc_saloon_un, R.drawable.bc_estate_un, R.drawable.bc_mpv_un, R.drawable.bc_executive_un};
//        int selectedcars[]={R.drawable.bc_saloon_s, R.drawable.bc_estate_s, R.drawable.bc_mpv_s, R.drawable.bc_executive_s};
//  List<String> VehicleNames=new DatabaseOperations(new DatabaseHelper(context)).getAllVehiclesNames();

        //{"Saloon","Estate","Executive","MPV","Van"};
        @Override
        public VIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.cars_items_dialogue, parent, false);
            //return convertView;
            VIEWHOLDER viewholder = new VIEWHOLDER(view);

            return viewholder;
        }

        @Override
        public void onBindViewHolder(final VIEWHOLDER holder, @SuppressLint("RecyclerView") final int position) {
            AnyVehicleFareModel carNames = carNamesList.get(position);
            String TempCar = carNames.getName();
            if (TempCar.contains("CAR")) {
                holder.cartxt.setText(TempCar.replace(" CAR", ""));
            } else {
                holder.cartxt.setText(TempCar);
            }
//            holder.mContainer.setCardBackgroundColor(context.getResources().getColor(R.color.active));
            ((CardView) holder.container).setCardBackgroundColor(context.getResources().getColor(R.color.active));
//holder.container.setBackgroundColor(context.getResources().getColor(R.color.drawer_head_bg));
//            ((CardView) holder.container).setCardBackgroundColor(context.getResources().getColor(R.color.drawer_head_bg));
            if (position == selectedCar) {
                holder.selectMask.setVisibility(View.VISIBLE);
                if (holder.cartxt.getText().toString().equalsIgnoreCase("saloon")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_saloon_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
                } else if (holder.cartxt.getText().toString().toLowerCase().equalsIgnoreCase("estate")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_estate_s));
                  /*  YoYo.with(Techniques.BounceIn)
                            .duration(400)
                            .repeat(0)
                            .playOn(holder.container);*/
                } else if (holder.cartxt.getText().toString().toLowerCase().contains("wheelchair")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_wheelchair_s));
   /*                 YoYo.with(Techniques.BounceIn)
                            .duration(400)
                            .repeat(0)
                            .playOn(holder.container);*/
                } else if (holder.cartxt.getText().toString().contains("9") || holder.cartxt.getText().toString().contains("8") || holder.cartxt.getText().toString().toLowerCase().contains("mini bus") || holder.cartxt.getText().toString().equalsIgnoreCase("7 Seater")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_van_s));
                  /*  YoYo.with(Techniques.BounceIn)
                            .duration(400)
                            .repeat(0)
                            .playOn(holder.container);*/
                } else if (holder.cartxt.getText().toString().toLowerCase().contains("Coach") || holder.cartxt.getText().toString().toLowerCase().contains("bus")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_coach_s));
                 /*   YoYo.with(Techniques.BounceIn)
                            .duration(400)
                            .repeat(0)
                            .playOn(holder.container);*/
                } else if (holder.cartxt.getText().toString().toLowerCase().contains("black cab") || holder.cartxt.getText().toString().contains("MPV") || holder.cartxt.getText().toString().equalsIgnoreCase("6 Seater") || holder.cartxt.getText().toString().contains("5")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_mpv_s));
               /*     YoYo.with(Techniques.BounceIn)
                            .duration(400)
                            .repeat(0)
                            .playOn(holder.container);*/
                } else {
               /*     YoYo.with(Techniques.BounceIn)
                            .duration(400)
                            .repeat(0)
                            .playOn(holder.container);*/

                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_executive_s));
                }
//                holder.cartxt.setTextColor(context.getResources().getColor(R.color.car_underline));

            } else {
                holder.selectMask.setVisibility(View.GONE);
//                holder.carimg.setBackgroundColor(context.getResources().getColor(R.color.drawer_head_bg));
//                holder.mContainer.setCardBackgroundColor(context.getResources().getColor(R.color.drawer_head_bg));
                if (holder.cartxt.getText().toString().equalsIgnoreCase("saloon")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_saloon_s));
                } else if (holder.cartxt.getText().toString().toLowerCase().equalsIgnoreCase("estate")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_estate_s));
                } else if (holder.cartxt.getText().toString().toLowerCase().contains("wheelchair")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_wheelchair_s));
                } else if (holder.cartxt.getText().toString().contains("9") || holder.cartxt.getText().toString().contains("8") || holder.cartxt.getText().toString().equalsIgnoreCase("7 Seater") || holder.cartxt.getText().toString().toLowerCase().contains("mini bus")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_van_s));
                } else if (holder.cartxt.getText().toString().toLowerCase().contains("Coach") || holder.cartxt.getText().toString().toLowerCase().contains("bus")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_coach_s));
                } else if (holder.cartxt.getText().toString().equalsIgnoreCase("7 Seater") || holder.cartxt.getText().toString().toLowerCase().contains("black cab") || holder.cartxt.getText().toString().contains("MPV") || holder.cartxt.getText().toString().equalsIgnoreCase("6 Seater") || holder.cartxt.getText().toString().contains("5")) {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_mpv_s));
                } else {
                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_executive_s));
                }
//                holder.carimg.setImageDrawable(context.getResources().getDrawable(cars[position]));
//                holder.cartxt.setTextColor(context.getResources().getColor(R.color.vehicle_txt));
            }

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

            holder.carFares.setText(sp.getString(CurrencySymbol, "\u00A3") + (String.format("%.2f", Float.parseFloat(carNames.getSingleFare()) + carNames.getExtraCharges())));

            try {
                if (sp.getString(ShowFares, "1").equals("0")) {
                    holder.carFares.setVisibility(View.GONE);
                } else {
                    if (isShowVehicleFare) {
                        holder.carFares.setVisibility(View.VISIBLE);
                    } else {
                        holder.carFares.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.carFares.setVisibility(View.GONE);
            }

            holder.carHlag.setText(carNames.getHandLuggagese());
            holder.carlag.setText(carNames.getSuitCase());
            holder.cartxt.setText(carNames.getName());
            holder.carPass.setText(carNames.getTotalPassengers());
//            if(position==carNames.length-1){
//                holder.frw_line.setVisibility(View.VISIBLE);
//            }


            holder.container.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    selectedCar = position;
                    mSelectedCar = position;
                    notifyDataSetChanged();


                }
            });

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return carNamesList.size();
        }

        class VIEWHOLDER extends RecyclerView.ViewHolder {
            ImageView carimg;


            TextView cartxt, carPass, carHlag, carlag, carFares;
            View container, frw_line, selectMask;

            public VIEWHOLDER(View itemView) {
                super(itemView);
                container = itemView;
                selectMask = (View) itemView.findViewById(R.id.selectMask);
                carimg = (ImageView) itemView.findViewById(R.id.carimage);

                cartxt = (TextView) itemView.findViewById(R.id.carname);
                carPass = (TextView) itemView.findViewById(R.id.carPass);
                carHlag = (TextView) itemView.findViewById(R.id.carhlag);
                carlag = (TextView) itemView.findViewById(R.id.carlag);
                carFares = (TextView) itemView.findViewById(R.id.carFares);

            }
        }

    }

    class Via_Adapter extends RecyclerView.Adapter<Via_Adapter.VIEWHOLDER> {

        Context context;
        ArrayList<ViaAddresses> viaAddresses;

        //        int selectedCar;
        public Via_Adapter(Context context, ViaAddresses[] viaAddresses) {
            this.context = context;
            this.viaAddresses = new ArrayList<ViaAddresses>(Arrays.asList(viaAddresses));
//            this.selectedCar=selectedCar;

//            CurrSelectedCar=carNames[0];
        }
        //    @Override
//    public int getCount() {
//        return 4;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//        int cars[]={R.drawable.bc_saloon_un, R.drawable.bc_estate_un, R.drawable.bc_mpv_un, R.drawable.bc_executive_un};
//        int selectedcars[]={R.drawable.bc_saloon_s, R.drawable.bc_estate_s, R.drawable.bc_mpv_s, R.drawable.bc_executive_s};
//  List<String> VehicleNames=new DatabaseOperations(new DatabaseHelper(context)).getAllVehiclesNames();

        //{"Saloon","Estate","Executive","MPV","Van"};
        @Override
        public VIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.via_item_dialogue, parent, false);
            //return convertView;
            VIEWHOLDER viewholder = new VIEWHOLDER(view);

            return viewholder;
        }

        @Override
        public void onBindViewHolder(final VIEWHOLDER holder, @SuppressLint("RecyclerView") final int position) {
            final ViaAddresses viaAddress = viaAddresses.get(position);

//            holder.mContainer.setCardBackgroundColor(context.getResources().getColor(R.color.active));
//            ((CardView) holder.container).setCardBackgroundColor(context.getResources().getColor(R.color.active));
//holder.container.setBackgroundColor(context.getResources().getColor(R.color.drawer_head_bg));
//            ((CardView) holder.container).setCardBackgroundColor(context.getResources().getColor(R.color.drawer_head_bg));

//                holder.selectMask.setVisibility(View.VISIBLE);
//                if( holder.cartxt.getText().toString().equalsIgnoreCase("saloon")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_saloon_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }else if( holder.cartxt.getText().toString().toLowerCase().equalsIgnoreCase("estate")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_estate_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }
//
//                else if( holder.cartxt.getText().toString().toLowerCase().contains("wheelchair")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_wheelchair_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }else if(holder.cartxt.getText().toString().contains("9")||holder.cartxt.getText().toString().contains("8")|| holder.cartxt.getText().toString().toLowerCase().contains("mini bus")||holder.cartxt.getText().toString().equalsIgnoreCase("7 Seater")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_van_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }
//                else if( holder.cartxt.getText().toString().toLowerCase().contains("Coach")|| holder.cartxt.getText().toString().toLowerCase().contains("bus")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_coach_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }
//                else if(holder.cartxt.getText().toString().toLowerCase().contains("black cab")||holder.cartxt.getText().toString().contains("MPV")||holder.cartxt.getText().toString().equalsIgnoreCase("6 Seater")||holder.cartxt.getText().toString().contains("5")) {
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_mpv_s));
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//                }else{
//                    YoYo.with(Techniques.BounceIn)
//                            .duration(400)
//                            .repeat(0)
//                            .playOn(holder.container);
//
//                    holder.carimg.setImageDrawable(context.getResources().getDrawable(R.drawable.bc_executive_s));
//                }
//                holder.cartxt.setTextColor(context.getResources().getColor(R.color.car_underline));


            holder.viatxt.setText(viaAddress.Viaaddress);
//            holder.carHlag.setText(carNames.getHandLuggagese());
//            holder.carlag.setText(carNames.getSuitCase());
//            holder.cartxt.setText(carNames.getName());
//            holder.carPass.setText(carNames.getTotalPassengers());
//            if(position==carNames.length-1){
//                holder.frw_line.setVisibility(View.VISIBLE);
//            }


            holder.delvia.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    viaAddresses.remove(position);
                    mSelectedVia = position;
                    if (mListChangeListener != null) {
                        mListChangeListener.onClick(SweetAlertDialog.this, mViaList[position], position);
                    }
                    notifyDataSetChanged();


                }
            });

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return viaAddresses.size();
        }

        public class VIEWHOLDER extends RecyclerView.ViewHolder {
            ImageView delvia;


            TextView viatxt;

            public VIEWHOLDER(View itemView) {
                super(itemView);
//                container=itemView;
//                selectMask=(View) itemView.findViewById(R.id.selectMask);
                delvia = (ImageView) itemView.findViewById(R.id.delvia);

                viatxt = (TextView) itemView.findViewById(R.id.viatxt);
//                carPass=(TextView) itemView.findViewById(R.id.carPass) ;
//                carHlag=(TextView) itemView.findViewById(R.id.carhlag) ;
//                carlag=(TextView) itemView.findViewById(R.id.carlag) ;
//                carFares=(TextView) itemView.findViewById(R.id.carFares) ;

            }
        }

    }
}
