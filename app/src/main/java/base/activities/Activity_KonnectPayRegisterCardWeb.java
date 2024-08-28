package base.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eurosoft.customerapp.R;

import base.models.SettingsModel;
import base.utils.SharedPrefrenceHelper;

public class Activity_KonnectPayRegisterCardWeb extends AppCompatActivity {

    WebView web;
    ProgressDialog progressDialog;
    String currentPageUrl = "", paymentUrl;

    String successUrl = "Success";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentview);
        web = (WebView) findViewById(R.id.webview);
        paymentUrl = getIntent().getStringExtra("paymentUrl");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();
      //  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    //    SettingsModel userModel = new SharedPrefrenceHelper(this).getSettingModel();


        web.setWebViewClient(new MyWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(paymentUrl);

    }

     class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

         @Override
         public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            if(request.getUrl().toString().contains(successUrl)){
                setResultOk();
                view.destroy();
            }
             return super.shouldOverrideUrlLoading(view, request);
         }

         @Override
         public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
             super.onReceivedError(view, request, error);
         }

         @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            currentPageUrl = view.getUrl();
            progressDialog.dismiss();
        }


    }



    // To handle &quot;Back&quot; key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void setResultOk(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


}