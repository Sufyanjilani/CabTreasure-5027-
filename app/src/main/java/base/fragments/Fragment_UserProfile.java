package base.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.eurosoft.customerapp.R;
import com.google.gson.Gson;
import com.support.parser.RegexPatterns;

import com.tfb.fbtoast.FBToast;

import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import base.activities.ActivityDeleteAccount;
import base.activities.Activity_AccountMemberLogin;
import base.activities.Activity_AddStripeCardDetail;
import base.activities.Activity_Start;
import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.listener.Listener_DeleteAccount;
import base.manager.Manager_DeleteAccount;
import base.manager.Manager_SaveCustomerId;
import base.models.LocAndField;
import base.models.UserModel;
import base.newui.HomeFragment;
import base.services.Service_NotifyStatus;
import base.utils.AppConstants;
import base.utils.CommonVariables;
import base.utils.CommonVariables.FontType;
import base.listener.Listener_OnSetResult;
import base.activities.Activity_SearchAddress;
import base.activities.Activity_SearchAddressForHomeAndWork;
import base.models.SettingsModel;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import base.models.ParentPojo;
import base.activities.Activity_ResetPassword;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static base.activities.Activity_SearchAddress.KEY_HOME;
import static base.activities.Activity_SearchAddress.KEY_OFFICE;
import static base.fragments.Fragment_Main.PhoneTv;
//import static base.miscactivities.FlightNo.KEY_Address_Type;
//import static base.miscactivities.FlightNo.KEY_COMING_FROM;
//import static base.miscactivities.FlightNo.KEY_FLIGHT_NO;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONObject;

import com.support.parser.PropertyInfo;

public class Fragment_UserProfile extends DialogFragment implements OnEditorActionListener, Listener_OnSetResult {

//public static final String KEY_USER_EMAIL = "keyUserEmail";
//public static final String KEY_USER_PHONE = "keyUserPhone";
//public static final String KEY_USER_MOBILE = "keyUserMobile";
//public static final String KEY_FIRST_TIME = "keyFirstTime";
//public static final String TAG_USER_BACKSTACK = "userStack";


    //private LinearLayout homelyt;
//private LinearLayout worklyt;
//private LinearLayout homelyt2;


    private static final int RESULT_LOAD_IMG = 221;
    public static final String KEY_DOOR_NUMBER = "keyDoorNumber";
    public static final String KEY_USER_NAME = "keyUserName";
    private static final int DELETE_RESULT = 22215;

    private ImageView deleteHomeIv;
    private ImageView deleteWorkIv;
    private ImageView userPic;
    private ImageView editLabel;
    private ImageView menuIv;
    private ImageView edit_phone_iv;


    private TextView hTv;
    private TextView wTv;
    private TextView setHomeTv;
    private TextView setWorkTv;
    private TextView userDetailTitleLabel;
    private TextView nameLabel;
    private TextView mobileLabel;
    private TextView emailLabel;
    private TextView changePasswordLabel;
    private TextView changePasswordSubLabel;
    private TextView name;
    private TextView mobile;
    private TextView email;
    private TextView removePic;

    private RelativeLayout mobileLayoutRl;
    private RelativeLayout emailLayoutRl;
    private RelativeLayout nameLayoutRl;
    private RelativeLayout homeRl;
    private RelativeLayout workRl;
    private RelativeLayout changePasswordRl;
    private RelativeLayout deleteAccountRl;
    private CardView userPicCv;

    private ParentPojo p;
    private SharedPrefrenceHelper sharedPrefrenceHelper;
    private SharedPreferences sp;
    private SettingsModel model;

//    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
//        ExifInterface ei = new ExifInterface(image_absolute_path + ".jpeg");
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                return rotate(bitmap, 90);
//
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                return rotate(bitmap, 180);
//
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                return rotate(bitmap, 270);
//
//            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//                return flip(bitmap, true, false);
//
//            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//                return flip(bitmap, false, true);
//
//            default:
//                return bitmap;
//        }
//    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

//    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
//        Matrix matrix = new Matrix();
//        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//    }

//    public static String getPath(final Context context, final Uri uri) {
//
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//
//                // TODO handle non-primary volumes
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{
//                        split[1]
//                };
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }

//    /**
//     * Get the value of the data column for this Uri. This is useful for
//     * MediaStore Uris, and other file-based ContentProviders.
//     *
//     * @param context       The context.
//     * @param uri           The Uri to query.
//     * @param selection     (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     */
//    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {
//                column
//        };
//
//        try {
//            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//                    null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int column_index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(column_index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return null;
//    }

//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is ExternalStorageProvider.
//     */
//    public static boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }

//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is DownloadsProvider.
//     */
//    public static boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//    }
    //    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is MediaProvider.
//     */
//    public static boolean isMediaDocument(Uri uri) {
//        return "com.android.providers.media.documents".equals(uri.getAuthority());
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(isAdded()  && getActivity() != null) {
            if (requestCode == RESULT_LOAD_IMG) {
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        int width = selectedImage.getWidth();
                        int Height = selectedImage.getHeight();
                        if (width > 700) {
                            int scaledWidth = width / 2;
                            int scaledHeight = Height / 2;
                            selectedImage = Bitmap.createScaledBitmap(selectedImage, scaledWidth, scaledHeight, false);
                        }
                        userPic.setImageBitmap(selectedImage);
                        editLabel.setVisibility(View.VISIBLE);
                        removePic.setVisibility(View.VISIBLE);
                        ((Fragment_Main) getActivity()).saveImageToInternalStorage(model.getUserServerID(), selectedImage);


                    } catch (Exception e) {
                        e.printStackTrace();
                        FBToast.errorToast(getActivity(), "Something went wrong", FBToast.LENGTH_LONG);
                    }

                } else {
                    FBToast.warningToast(getActivity(), "You haven't picked Image", FBToast.LENGTH_LONG);
                }
            } else if (requestCode == DELETE_RESULT) {
                if (isAdded() && getActivity() != null)
                    if (resultCode == RESULT_OK) {
                        try {
                            ((Fragment_Main) getActivity()).removeAllSharedPreferencesDataBeforeLogout();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        setHome();
        setWork();
    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void ShowDialog(final int type) {
//
//        String[] arr = {"Search Address", "Airports", "Stations", "Current Location", "Favourites"};
//
//        final Bundle extras = new Bundle();
//        final NewHomeSelect details = new NewHomeSelect();
//        extras.putInt("type", type);
//        details.setArguments(extras);
//
//        details.setOnSetListener(this);
//
//        try {
//            extras.putString(NewBookingDetails.KEY_SHOW_WHAT, NewBookingDetails.ADDRESS);
//            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fall, R.anim.fall_below, R.anim.fall, R.anim.fall_below)
//                    .add(android.R.id.content, details, null).addToBackStack(null).commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    private void setDarkAndNightThemeColor() {
        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.color_white_inverse));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
//                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.red));// set status background white
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, getTheme());
        setCancelable(false);
        initObject();
    }

    private void initObject() {
        p = new ParentPojo();
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPrefrenceHelper = new SharedPrefrenceHelper(getActivity());
        model = sharedPrefrenceHelper.getSettingModel();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.layout_user_profile, container, false);
        setDarkAndNightThemeColor();

        init(ret);

        initData();

        listener();

        return ret;
    }

    private void initData() {

        if (sp.getString(CommonVariables.enableSignup, "1").equals("0")) {
            mobileLayoutRl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoUpdate("mobile", mobile.getText().toString());

                }
            });
            emailLayoutRl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoUpdate("email", email.getText().toString());
                }
            });
            nameLayoutRl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoUpdate("name", name.getText().toString());

                }
            });
            changePasswordRl.setVisibility(View.GONE);
        }

        Bitmap bitmap = ((Fragment_Main) getActivity()).getThumbnail(sharedPrefrenceHelper.getSettingModel().getUserServerID() + ".jpeg");

        if (bitmap != null) {
            int width = bitmap.getWidth();
            int Height = bitmap.getHeight();
            if (width > 700) {
                int scaledWidth = width / 2;
                int scaledHeight = Height / 2;
                bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false);
            }
            userPic.setImageBitmap(bitmap);
            editLabel.setVisibility(View.VISIBLE);
            removePic.setVisibility(View.VISIBLE);
        }

        CommonVariables.setFont(getActivity(), name, FontType.Regular);
        CommonVariables.setFont(getActivity(), mobile, FontType.Regular);
        CommonVariables.setFont(getActivity(), email, FontType.Regular);
    }

    private void listener() {
        homeRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!hTv.getText().toString().toLowerCase().trim().equals("add home"))
                    return;

                startActivity(new Intent(getContext(), Activity_SearchAddressForHomeAndWork.class).putExtra("setFrom", "home"));
            }
        });
        workRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wTv.getText().toString().toLowerCase().trim().equalsIgnoreCase("work"))
                    return;

                startActivity(new Intent(getContext(), Activity_SearchAddressForHomeAndWork.class).putExtra("setFrom", "work"));
            }
        });
        deleteHomeIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(p.getAreYouSure())
                            .setContentText("You want to delete your home location!")
                            .setCancelText(p.getNo())
                            .setConfirmText(p.getYes())
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Deleted!")
                                            .setContentText("Home location has been deleted!")
                                            .show();
                                    sharedPrefrenceHelper.removeAddressModel(model.getEmail() + "_" + KEY_HOME);
                                    setHome();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        deleteWorkIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!wTv.getText().toString().equalsIgnoreCase("Add Work"))
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(p.getAreYouSure())
                                .setContentText("You want to delete your work location!")
                                .setCancelText(p.getNo())
                                .setConfirmText(p.getYes())
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Deleted!")
                                                .setContentText("Work location has been deleted!")
                                                .show();
                                        sharedPrefrenceHelper.removeAddressModel(sharedPrefrenceHelper.getSettingModel().getEmail() + "_" + KEY_OFFICE);
                                        setWork();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        removePic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("")
                        .setContentText("Are you sure you want to remove profile picture?")
                        .setCancelText(p.getNo())
                        .setConfirmText(p.getYes())
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                ((Fragment_Main) getActivity()).removeFile(model.getUserServerID());
                                userPic.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                                editLabel.setVisibility(View.GONE);
                                removePic.setVisibility(View.GONE);
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
        });

        menuIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Fragment_Main) getActivity()).toggleDrawer();
            }
        });
        changePasswordRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_ResetPassword.class));

            }
        });


        deleteAccountRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccountDialog();

            }
        });


        userPicCv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMG);
            }
        });
        edit_phone_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editNumber();
            }
        });
    }


    private void deleteAccountDialog(){

        @SuppressLint("ResourceType") String confirmButtonColor =  getResources().getString(R.color.color_gray_and_footer_inverse);
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Delete Account")
                .setContentText("Deleting your account is irreversible and will permanently remove all your data. If you have concerns or feedback, please share them with us before proceeding.")
                .setCancelText(p.getNo())
                .setConfirmText(p.getYes())
                .setConfirmButtonBackgroundColor(confirmButtonColor)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        launchDeleteActivity();

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

    private void launchDeleteActivity(){
        if(isAdded()) {
            Intent intent = new Intent(getContext(), ActivityDeleteAccount.class);
            startActivityForResult(intent, DELETE_RESULT);
        }
    }
    private void init(View ret) {
        edit_phone_iv = ret.findViewById(R.id.edit_phone_iv);

        userDetailTitleLabel = ret.findViewById(R.id.titleTv);
        userDetailTitleLabel.setText(p.getUserProfile());

        nameLabel = ret.findViewById(R.id.nameLabel);
        nameLabel.setText(p.getName());

        mobileLabel = ret.findViewById(R.id.mobileLabel);
        mobileLabel.setText(p.getMobile());

        emailLabel = ret.findViewById(R.id.emailLabel);
        emailLabel.setText(p.getEmail());

        changePasswordLabel = ret.findViewById(R.id.changePasswordLabel);
        changePasswordLabel.setText(p.getChangePassword());

        changePasswordSubLabel = ret.findViewById(R.id.changePasswordSubLabel);
        changePasswordSubLabel.setText(p.getTapToChange());

        name = ret.findViewById(R.id.txtName);

        if (!model.getName().contains(model.getlName())) {
        name.setText(model.getName() +" " + model.getlName());
        } else {
        name.setText(model.getName());
        }

        mobile = ret.findViewById(R.id.txtMobile);
        mobile.setText(model.getPhoneNo());

        email = ret.findViewById(R.id.txtEmail);
        email.setText(model.getEmail());

        hTv = ret.findViewById(R.id.hTv);

        setHomeTv = ret.findViewById(R.id.setHomeTv);
        homeRl = ret.findViewById(R.id.homeRl);
        removePic = ret.findViewById(R.id.removePic);
        menuIv = ret.findViewById(R.id.menuIv);
        changePasswordRl = ret.findViewById(R.id.changePasswordRl);
        deleteAccountRl = ret.findViewById(R.id.deleteAccountRl);
        emailLayoutRl = ret.findViewById(R.id.emailLayoutRl);
        nameLayoutRl = ret.findViewById(R.id.nameLayoutRl);
        userPicCv = ret.findViewById(R.id.userPicCv);
        wTv = ret.findViewById(R.id.wTv);
        setWorkTv = ret.findViewById(R.id.setWorkTv);
        workRl = ret.findViewById(R.id.workRl);
        deleteHomeIv = ret.findViewById(R.id.deleteHomeIv);
        deleteWorkIv = ret.findViewById(R.id.deleteWorkIv);
        userPic = ret.findViewById(R.id.userPic);
        editLabel = ret.findViewById(R.id.editLabel);
        mobileLayoutRl = ret.findViewById(R.id.mobileLayoutRl);


        if(sp.getString(CommonVariables.ENABLE_DELETE_Account, "0").equals("0")){
            deleteAccountRl.setVisibility(GONE);
        }
    }

    public void gotoUpdate(String update_val, String updateVal) {
        if (update_val.equals("more")) {
//            FavoriteLocations details_update = new FavoriteLocations();
//            details_update.setOnSetResultListener(this);
//            Bundle args = new Bundle();

//            details_update.setArguments(args);
//            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fall, R.anim.fall_below, R.anim.fall, R.anim.fall_below)
//                    .add(android.R.id.content, details_update, null).commit();
            return;
        }
        Fragment_UserDetails_Update details_update = new Fragment_UserDetails_Update();
        details_update.setOnSetResultListener(this);
        Bundle args = new Bundle();
        args.putString("updating", update_val);
        args.putString("updatingVal", update_val);
        details_update.setArguments(args);
    }

    String MobileNo = "";
    Dialog dialog;

    private void editNumber() {
        dialog = new Dialog(getActivity(), android.R.style.Widget_DeviceDefault);
        dialog.setContentView(R.layout.update_user_number);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView menuIv = dialog.findViewById(R.id.menuIv);
        menuIv.setVisibility(GONE);

        TextView titleTv = dialog.findViewById(R.id.titleTv);
        titleTv.setText("Update Number");

        TextView errMobile = dialog.findViewById(R.id.errMobile_);

        IntlPhoneInput phoneInput = (IntlPhoneInput) dialog.findViewById(R.id.my_phone_input);
        phoneInput.setTextColor(getResources().getColor(R.color.color_black_inverse));
        phoneInput.setHintColor(getResources().getColor(R.color.edit_field_mobile_hint_text_color));

        phoneInput.getInputText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    errMobile.setVisibility(View.INVISIBLE);
                }
            }
        });

        TextView update = dialog.findViewById(R.id.update_title);
        update.setText("Update Number");

        ProgressBar pb = dialog.findViewById(R.id.pb);
        ImageView closeBottomSheet = dialog.findViewById(R.id.backIv);
        closeBottomSheet.setVisibility(VISIBLE);

        LinearLayout btnDone = dialog.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validation = true;
                if (phoneInput.getError() != null) {
                    MobileNo = phoneInput.getError().toString();
                }

                if (phoneInput.getNumber() != null) {
                    MobileNo = phoneInput.getText().toString().replace("+44", "0");

                    if (phoneInput.isValid()) {
                        validation = true;
                    } else {
                        validation = false;
                        errMobile.setText("Invalid Mobile Number");
                        errMobile.setVisibility(View.VISIBLE);
                    }
                } else {
                    validation = false;
                    errMobile.setText("Mobile Number Required");
                    errMobile.setVisibility(View.VISIBLE);
                }

                if (validation) {
                    new UpdatePhoneNumber(pb, MobileNo).execute();
                }
            }
        });

        closeBottomSheet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private class UpdatePhoneNumber extends AsyncTask<String, Void, String> {

        public static final String METHOD_NAME = "UpdateAppUserInfo";
        ProgressBar pb;
        String phoneNumber;

        public UpdatePhoneNumber(ProgressBar pb, String phoneNumber) {
            this.pb = pb;
            this.phoneNumber = phoneNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (pb != null) {
                    pb.setVisibility(VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }// End onPreExecute()

        protected void onPostExecute(String result) {
            try {
                super.onPostExecute(result);
                if (pb != null) {
                    pb.setVisibility(GONE);
                }

                if (result == null || result.equals("")) {

                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("Message");

                    if (!response.getBoolean("HasError")) {

                        Toast.makeText(getContext(), message + "", Toast.LENGTH_SHORT).show();

                        if (dialog != null) {
                            dialog.dismiss();
                        }

                        SharedPrefrenceHelper sharedPrefrenceHelper = new SharedPrefrenceHelper(getContext());
                        SettingsModel model = sharedPrefrenceHelper.getSettingModel();
                        model.setPhoneNo(MobileNo);
                        sharedPrefrenceHelper.putSettingModel(model);

                        try {
                            PhoneTv.setText(MobileNo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ((TextView) getView().findViewById(R.id.txtMobile)).setText("" + MobileNo);

                    } else {
                        Toast.makeText(getContext(), message + "", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                SharedPrefrenceHelper sharedPrefrenceHelper = new SharedPrefrenceHelper(getContext());
                SettingsModel settingsModel = sharedPrefrenceHelper.getSettingModel();

                String response = null;
                String token = AppConstants.getAppConstants().getToken();
           /*     String jsonString = "{ request:{" +
                        "defaultClientId:" + CommonVariables.clientid + "," +
                        "uniqueValue:" + "\"" + CommonVariables.clientid + "4321orue" + "\"" + "," +
                        "PhoneNo:" + "\"" + phoneNumber + "\"" + "," +
                        "UserName:" + "\"" + "" + "\"" + "," +
                        "Passwrd:" + "\"" + settingsModel.getPassword() + "\"" + "," +
                        "Email:" + "\"" + settingsModel.getEmail().trim() + "\"" +
                        "},"+"Token:" +"\"" +token+ "}" +"\"";*/

                HashMap<String, Object> userData = new HashMap<>();
                HashMap<String, Object> parentmap = new HashMap<>();
                userData.put("defaultClientId", CommonVariables.clientid);
                userData.put("uniqueValue", CommonVariables.clientid + "4321orue");
                userData.put("PhoneNo", phoneNumber);
                userData.put("Passwrd", settingsModel.getPassword());
                userData.put("Email", settingsModel.getEmail().trim());
                parentmap.put("request", userData);
                parentmap.put("Token", token);

                String jsonString = new Gson().toJson(parentmap);
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonString);
                Request request = new Request.Builder()
                        .url(CommonVariables.BASE_URL + "UpdateAppUserInfo")
                        .post(body)
                        .build();

                Response mresponse = client.newCall(request).execute();
                response = mresponse.body().string();


                return response;

            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }
    }

    private void btnDoneClicked() {
        boolean isNameEmpty = ((TextView) getView().findViewById(R.id.txtName)).getText().toString().isEmpty();

        String isValidEmailText = ((TextView) getView().findViewById(R.id.txtEmail)).getText().toString();
        boolean isValidEmail = !isValidEmailText.isEmpty();

        String isValidPhoneText = ((TextView) getView().findViewById(R.id.txtMobile)).getText().toString();
        boolean isValidPhoneNumber = Pattern.matches(RegexPatterns.REGEX_UK_NUMBERS, isValidPhoneText);

        if (!isNameEmpty && isValidEmail && isValidPhoneNumber) {

            String name = ((TextView) getView().findViewById(R.id.txtName)).getText().toString();
//            String Address = ((TextView) getView().findViewById(R.id.Addresstxt)).getText().toString();
            String mobile = ((TextView) getView().findViewById(R.id.txtMobile)).getText().toString();
            String email = ((TextView) getView().findViewById(R.id.txtEmail)).getText().toString();

            model.setName(name);
            model.setPhoneNo(mobile);
//            model.setAddress(Address);
            model.setEmail(email);

            new SharedPrefrenceHelper(getActivity()).putSettingModel(model);
            try {
                ((Fragment_Main) getActivity()).onItemClick(null, null, 99, 0);
            } catch (Exception e) {

            }
//			if (mListener != null) {
//
//				Intent intent = new Intent();
//				intent.putExtra(KEY_USER_NAME, name);
//				intent.putExtra(KEY_USER_EMAIL, email);
//				intent.putExtra(KEY_USER_MOBILE, mobile);
//				intent.putExtra(KEY_USER_PHONE, Address);
//				mListener.setResult(intent);
//
//			}
//			dismiss();
        } else {
            String message = "User details are missing.\n";
            if (isNameEmpty)
                message += "\nName required.";
            if (!isValidPhoneNumber)
                message += "\nValid mobile number required";
            if (!isValidEmail)
                message += "\nValid e-mail required";
            new AlertDialog.Builder(getActivity()).setTitle("User Details").setMessage(message).setPositiveButton("OK", null).show();

        }
    }

    private Listener_OnSetResult mListener;

    public void setOnSetResultListener(Listener_OnSetResult listener) {
        mListener = listener;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            btnDoneClicked();
        }
        return false;
    }

    @Override
    public void setResult(Intent intent) {
        if (intent.getStringExtra("mobile") != null) {
            mobile.setText(intent.getStringExtra("mobile"));
        } else if (intent.getStringExtra("address") != null) {
//            Address.setText(intent.getStringExtra("address"));

        } else if (intent.getStringExtra("name") != null) {
            name.setText(intent.getStringExtra("name"));
        } else if (intent.getStringExtra("email") != null) {
            email.setText(intent.getStringExtra("email"));
        } else if (intent.getStringExtra(Activity_SearchAddress.KEY_RESULT_BOOKING) != null) {
//            try {
//                String address = intent.getStringExtra(Activity_SearchAddress.KEY_RESULT_BOOKING);
//                String door = intent.getStringExtra(KEY_DOOR_NUMBER);
//                String flight = intent.getStringExtra(KEY_FLIGHT_NO);
//                String lat = intent.getStringExtra(Activity_SearchAddress.KEY_RESULT_LATITUDE);
//                String addressType = intent.getStringExtra(KEY_Address_Type);
//
//                String lon = intent.getStringExtra(Activity_SearchAddress.KEY_RESULT_LONGITUDE);
//                String fromComing = intent.getStringExtra(KEY_COMING_FROM);
//                int showType = intent.getIntExtra("type", 1);
//                fromComing = fromComing != null ? fromComing : "";
//                door = door != null ? door : "";
//                flight = flight != null ? flight : "";
//
//                if (!flight.isEmpty())
//                    door = flight;
//                try {
//                    android.location.Address addressclass = new Address(Locale.getDefault());
//                    addressclass.setAddressLine(0, address);
//                    addressclass.setLatitude(Double.parseDouble(lat));
//                    addressclass.setLongitude(Double.parseDouble(lon));
//                    AddressModel mCurrentLocation = new AddressModel(addressclass, door, flight, "Address");
//                    mCurrentLocation.fromComing = fromComing;
//                    if (showType == 1) {
////                        homeAddress = mCurrentLocation;
//                        sharedPrefrenceHelper.putAddressModel(KEY_HOME, mCurrentLocation);
//                    } else {
////                        workAddress = mCurrentLocation;
//                        sharedPrefrenceHelper.putAddressModel(KEY_OFFICE, mCurrentLocation);
////                        worklyt.setVisibility(View.VISIBLE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }

//            } catch (Exception e) {
//            }
        }
    }

    private void setHome() {
        LocAndField homeAddress = sharedPrefrenceHelper.getLocAndFieldModel(sharedPrefrenceHelper.getSettingModel().getEmail() + "_" + KEY_HOME);

        if (homeAddress != null && !homeAddress.equals("null") && !homeAddress.getField().equals("")) {
            hTv.setText(p.getHome());
            setHomeTv.setVisibility(VISIBLE);
            deleteHomeIv.setVisibility(VISIBLE);
            setHomeTv.setText(homeAddress.getField());

        } else {
            hTv.setText(p.getAddHome());
            setHomeTv.setVisibility(GONE);
            deleteHomeIv.setVisibility(GONE);
        }
    }

    private void setWork() {
        LocAndField workAddress = sharedPrefrenceHelper.getLocAndFieldModel(sharedPrefrenceHelper.getSettingModel().getEmail() + "_" + KEY_OFFICE);

        if (workAddress != null && !workAddress.equals("null") && !workAddress.getField().equals("")) {
            wTv.setText(p.getWork());
            setWorkTv.setVisibility(VISIBLE);
            deleteWorkIv.setVisibility(VISIBLE);
            setWorkTv.setText(workAddress.getField());
        } else {
            wTv.setText(p.getAddWork());
            setWorkTv.setVisibility(GONE);
            deleteWorkIv.setVisibility(GONE);
        }
    }

}
