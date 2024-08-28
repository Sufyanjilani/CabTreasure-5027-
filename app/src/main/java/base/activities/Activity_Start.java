package base.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eurosoft.customerapp.R;

import base.utils.CommonMethods;

public class Activity_Start extends AppCompatActivity {
    TextView loginBtnTv;
    TextView registerBtnTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_start);

//        getWindow().setStatusBarColor(Color.parseColor("#000000"));// set status background white
        CommonMethods.getInstance().setDarkAndNightColor(Activity_Start.this);

        loginBtnTv = findViewById(R.id.loginBtnTv);
        loginBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Start.this, Activity_Login.class));
               // Animatoo.animateSlideLeft(Activity_Start.this);
                finish();
            }
        });

        registerBtnTv = findViewById(R.id.registerBtnTv);
        registerBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Start.this, Activity_Signup.class).putExtra("from","start_activity"));
              //  Animatoo.animateSlideLeft(Activity_Start.this);
                finish();
            }
        });
    }
}
