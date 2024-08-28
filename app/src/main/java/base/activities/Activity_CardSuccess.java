package base.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.eurosoft.customerapp.R;

import base.fragments.Fragment_Main;
import base.utils.Config;
import base.models.SettingsModel;
import base.utils.SharedPrefrenceHelper;

public class Activity_CardSuccess extends AppCompatActivity {
    SettingsModel settingsModel;
    TextView txtDetails;
    Button btnPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_card_success);
//        spHelper=new SharedPrefrenceHelper(getActivity());
        settingsModel = new SharedPrefrenceHelper(Activity_CardSuccess.this).getSettingModel();
        btnPromo = findViewById(R.id.btnPromo);
        String transDetails = PreferenceManager.getDefaultSharedPreferences(this).getString(Config.CardSuccessMsg, "");

        txtDetails = findViewById(R.id.txtdetails);
        txtDetails.setText(transDetails);
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().hasExtra("start_up_")) {
                    startActivity(new Intent(Activity_CardSuccess.this, Fragment_Main.class));
                    //Animatoo.animateSlideLeft(Activity_CardSuccess.this);
                } else {
                    setResult(RESULT_OK);
                }
                finish();
            }
        });
        btnPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().hasExtra("start_up_")) {
                    startActivity(new Intent(Activity_CardSuccess.this, Fragment_Main.class));
                  //  Animatoo.animateSlideLeft(Activity_CardSuccess.this);
                } else {
                    setResult(RESULT_OK);
                }
                finish();
            }
        });
    }
}
