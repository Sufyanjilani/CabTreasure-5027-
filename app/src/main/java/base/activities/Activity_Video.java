package base.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.eurosoft.customerapp.R;

import base.fragments.Fragment_Main;
import base.utils.CommonMethods;
import base.utils.CommonVariables;

public class Activity_Video extends AppCompatActivity {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash_video);

        sp = PreferenceManager.getDefaultSharedPreferences(this);


        getWindow().setStatusBarColor(Color.parseColor("#000000"));// set status background white
        CommonMethods.getInstance().setDarkAndNightColor(Activity_Video.this);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        VideoView videoview = (VideoView) findViewById(R.id.vv);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.speedy_cabs);
        videoview.setVideoURI(uri);
        videoview.start();

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (!sp.getBoolean(CommonVariables.ISUSERLOGIN, false)) {
                    if (!sp.getString(CommonVariables.enableSignup, "1").equals("0")) {
                        startActivity(new Intent(Activity_Video.this, Activity_Start.class));
                        finish();
                    } else {
                        startActivity(new Intent(Activity_Video.this, Fragment_Main.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(Activity_Video.this, Fragment_Main.class));
                    finish();
                }
/*                startActivity(new Intent(Activity_Video.this, Fragment_Main.class));
                finish();*/
            }
        });
    }
}
