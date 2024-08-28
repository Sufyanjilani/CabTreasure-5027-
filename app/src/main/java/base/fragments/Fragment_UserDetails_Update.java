package base.fragments;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.eurosoft.customerapp.R;
import com.support.parser.RegexPatterns;

import java.util.regex.Pattern;

import base.listener.Listener_OnSetResult;
import base.models.SettingsModel;
import base.utils.SharedPrefrenceHelper;

public class Fragment_UserDetails_Update extends DialogFragment implements OnClickListener, OnEditorActionListener {

    public static final String KEY_USER_NAME = "keyUserName";
    public static final String KEY_USER_EMAIL = "keyUserEmail";
    public static final String KEY_USER_PHONE = "keyUserPhone";
    public static final String KEY_USER_MOBILE = "keyUserMobile";
    public static String updateWhat = "";
    public static final String KEY_FIRST_TIME = "keyFirstTime";

    public static final String TAG_USER_BACKSTACK = "userStack";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, getTheme());
        setCancelable(false);
    }

    EditText field;
    TextView pageHeader, PageMessage, Title_Tv;
    String addOrUpdate[] = {"Add", "Update"};

    Button btn;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.layout_user_detail_update, container, false);
        btn = ret.findViewById(R.id.btnDone);

        Title_Tv = ((TextView) ret.findViewById(R.id.textcv_UserDetails));
        pageHeader = ((TextView) ret.findViewById(R.id.update_title));
        PageMessage = (TextView) ret.findViewById(R.id.message);

        ret.findViewById(R.id.imgBack).setOnClickListener(this);
        btn.setOnClickListener(this);

        SettingsModel model = new SharedPrefrenceHelper(getActivity()).getSettingModel();

        field = ((EditText) ret.findViewById(R.id.updateField));
        field.requestFocus();
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (Exception e) {

        }

        if (getArguments() != null) {
            if (getArguments().getString("updating", "").equals("name")) {
                field.setHint("Enter Username");
                field.setText(model.getName());
                pageHeader.setText("Name");

                if (field.length() == 0)
                    PageMessage.setText("Add your name so driver can easily identify from whom they are picking");
                else
                    PageMessage.setText("Update your name so driver can easily identify from whom they are picking");

            } else if (getArguments().getString("updating", "").equals("mobile")) {
                field.setHint("Enter Mobile Number");
                field.setText(model.getPhoneNo());
                field.setInputType(InputType.TYPE_CLASS_NUMBER);
                pageHeader.setText("Mobile Number");
                if (field.length() == 0)
                    PageMessage.setText("Add your mobile number so driver can easily contact you");
                else
                    PageMessage.setText("Update your mobile number so driver can easily contact you");

            } else if (getArguments().getString("updating", "").equals("email")) {
                field.setHint("Enter Email Address");
                field.setText(model.getEmail());
                pageHeader.setText("Email Address");

                if (field.length() == 0)
                    PageMessage.setText("Add your email address so you can easily connect with our services");
                else
                    PageMessage.setText("Update your email address so you can easily connect with our services");

            } else if (getArguments().getString("updating", "").equals("address")) {
                field.setHint("Enter Address");
                field.setText(model.getAddress());
                pageHeader.setText("Add Address");

                if (field.length() == 0)
                    PageMessage.setText("Add your address (optional)");
                else
                    PageMessage.setText("Enter your address (optional)");
            }

            if (field.length() == 0) {
                Title_Tv.setText(addOrUpdate[0]);
                btn.setText(addOrUpdate[0]);
            } else {
                Title_Tv.setText(addOrUpdate[1]);
                btn.setText(addOrUpdate[1]);
            }

            updateWhat = getArguments().getString("updating", "");
        }

        return ret;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDone) {
            btnDoneClicked();
        } else if (v.getId() == R.id.imgBack) {
            dismiss();
        } else if (v.getId() == R.id.menu_btn) {
            ((Fragment_Main) getActivity()).toggleDrawer();
        }
    }

    private void btnDoneClicked() {
        boolean valid = false;
        SettingsModel model = new SharedPrefrenceHelper(getActivity()).getSettingModel();
        boolean isFieldEmpty = ((EditText) getView().findViewById(R.id.updateField)).getText().toString().isEmpty();
        String fieldData = field.getText().toString();
        if (updateWhat.equals("name")) {
            if (!isFieldEmpty) {
                valid = true;
                model.setName(fieldData);
                dismiss();
            } else {
                String message = "Please enter valid name.\n";

                new AlertDialog.Builder(getActivity()).setTitle("User Details").setMessage(message).setPositiveButton("OK", null).show();
            }
        } else if (updateWhat.equals("mobile")) {

            String isValidPhoneText = ((TextView) getView().findViewById(R.id.updateField)).getText().toString().trim();
            boolean isValidPhoneNumber = Pattern.matches(RegexPatterns.REGEX_UK_NUMBERS, isValidPhoneText);
            if (isValidPhoneNumber) {
                valid = true;
                model.setPhoneNo(fieldData.trim());
                dismiss();
            } else {
                String message = "Valid mobile number required.\n";
                new AlertDialog.Builder(getActivity()).setTitle("User Details").setMessage(message).setPositiveButton("OK", null).show();
            }
        } else if (updateWhat.equals("email")) {
            String isValidEmailText = ((TextView) getView().findViewById(R.id.updateField)).getText().toString();
            boolean isValidEmail = !isValidEmailText.isEmpty();
            if (isValidEmail) {
                valid = true;
                model.setEmail(fieldData);
                dismiss();
            } else {
                String message = "Valid e-mail required\n";
                new AlertDialog.Builder(getActivity()).setTitle("User Details").setMessage(message).setPositiveButton("OK", null).show();
            }
        } else if (updateWhat.equals("address")) {
            if (!isFieldEmpty) {
                valid = true;
                model.setAddress(fieldData);
                dismiss();
            } else {
                String message = "Please enter valid address.\n";
                new AlertDialog.Builder(getActivity()).setTitle("User Details").setMessage(message).setPositiveButton("OK", null).show();
            }
        }

        if (mListener != null && valid) {
            new SharedPrefrenceHelper(getActivity()).putSettingModel(model);
            Intent intent = new Intent();
            intent.putExtra(updateWhat, fieldData);
            mListener.setResult(intent);

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
    public void onDestroy() {
        super.onDestroy();
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }
}
