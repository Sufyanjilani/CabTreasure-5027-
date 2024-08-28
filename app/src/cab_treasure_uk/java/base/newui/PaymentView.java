package base.newui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eurosoft.customerapp.R;

import base.utils.Config;
import base.models.SettingsModel;
import base.utils.SharedPrefrenceHelper;

/**
 * Created by ayu on 15/12/2017.
 */

public class PaymentView extends AppCompatActivity {
    WebView web;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentview);
        web = (WebView) findViewById(R.id.webview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsModel userModel = new SharedPrefrenceHelper(this).getSettingModel();


        web.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        if (getIntent().hasExtra("fare")) {


            web.setWebViewClient(new myWebClient());
            web.getSettings().setJavaScriptEnabled(true);
//    https://secure.worldpay.com/wcc/purchase?instId=1161493
//    web.loadUrl("https://secure.worldpay.com/wcc/purchase?instId=1161493&cartId=asdf&amount="+getIntent().getStringExtra("fare")+"&currency=GBP&desc=Pay online for your journey&name="+userModel.getName()+"&address1="+""+"&town=HARROW&postcode=75600&cardtype=Visa&country=UK&email="+userModel.getEmail()+"&testMode=0");
            web.loadUrl("https://secure.worldpay.com/wcc/purchase?instId=" + sp.getString(Config.InstId, "") + "&cartId=asdf&amount=" + getIntent().getStringExtra("fare") + "&currency=GBP&desc=Pay online for your journey&name=" + userModel.getName() + "&address1=" + "" + "&town=HARROW&postcode=75600&cardtype=Visa&country=UK&email=" + userModel.getEmail() + "&testMode=0");
        } else {
            finish();
        }
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            progressDialog.dismiss();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

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

    private class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void showHTML(String html) {

            String htmlString = html;
            Log.e("", "html is" + html);
//            String[] strings=htmlString.split("td width");
//            Log.e("","html is"+strings);
            if (htmlString.contains("Your payment transaction completed successfully")) {
                String[] htmlArray = htmlString.split("Transaction ID: ");
                String TransactionID = "";

                TransactionID = htmlArray[1].split("</p>")[0];
                Intent intent = new Intent();
                intent.putExtra("transactionID", TransactionID);
                setResult(RESULT_OK, intent);
                finish();


//            if(FStatus.contains("Refusée")){
//              //  Toast.makeText(PaymentView.this,"Payment Failed",Toast.LENGTH_LONG).show();
//                intent.putExtra("status","Payment Failed");
//               setResult(RESULT_CANCELED,intent);
//              finish();
//
//            }else{
//                intent.putExtra("status",FStatus);
//
//                intent.putExtra("dateTimeTrans",FdateTimeTrans);
//                intent.putExtra("transactionID",FTransactionId);
//                setResult(RESULT_OK,intent);
//                finish();
//            }
            }
        }
    }
}
