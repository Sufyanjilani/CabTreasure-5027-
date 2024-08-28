package base.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;


import com.eurosoft.customerapp.R;

import base.models.SettingsModel;
import base.utils.SharedPrefrenceHelper;

/**
 * Created by ayu on 15/12/2017.
 */

public class PaymentViewJudo3ds extends AppCompatActivity {
    WebView web;
    ProgressDialog progressDialog;
    String currentPageUrl = "", paymentUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentview);
        web = (WebView) findViewById(R.id.webview);
        paymentUrl = getIntent().getStringExtra("paymentUrl");
//        paymentUrl = "https://api-eurosofttech.co.uk/Sandbox-Judo3DS2/JudoPay/PreAuths?data=eyJCb29raW5nSWQiOjEwMCwiQW1vdW50IjoxLjAxLCJEaXNwbGF5QW1vdW50IjoxLjAxLCJDdXJyZW5jeSI6IkdCUCIsIkRlZmF1bHRDbGllbnRJZCI6IlRFU1QiLCJEZXNjcmlwdGlvbiI6IjIwYjkwMzBlLWI4YjMtNGE3Yy1hYWVkLWYxMDU5MTIxOTgzNyIsIlVwZGF0ZVVybCI6Imh0dHA6Ly9sb2NhbGhvc3Q6NjQ5ODIvanVkb3BheWFwaS9VcGRhdGVzdGF0dXMiLCJDdXN0b21lck5hbWUiOiJhaGFtZWQgTml5YXMiLCJDdXN0b21lck51bWJlciI6IjA3OTA3MjcwMzc5IiwiQ3VzdG9tZXJFbWFpbCI6ImZhemxhbkBldXJvc29mdHRlY2guY28udWsiLCJKdWRvSWQiOiIxMDA0NDY2MTYiLCJBUElUb2tlbiI6InBKQklKNUFncTFSeEJ6OUUiLCJBUElTZWNyZXQiOiJiZjk4NzYwMmFhZDBmNjEwOTkyYmQ0ZDA2NTZlZjhhNjM1YTNlNGU1YzMwZjY2ZDFkNmVlZTkzOGQxN2Q0OGEwIiwiSXNSZWdpc3RlckNhcmQiOnRydWUsInlvdXJDb25zdW1lclJlZmVyZW5jZSI6Ik5vVkFFeXQ5RTN4MUFNUkUiLCJ5b3VyUGF5bWVudFJlZmVyZW5jZSI6IjE1NTIxZTJjLTllZWItNDE5Ny04ODhiLWJhMWY3ZTI5OTc0MyIsfQ";
//        paymentUrl = "https://api-eurosofttech.co.uk/Sandbox-Judo3DS2/JudoPay/PreAuths?data=eyJCb29raW5nSWQiOjEwMCwiQW1vdW50IjoxLjAxLCJEaXNwbGF5QW1vdW50IjoxLjAxLCJDdXJyZW5jeSI6IkdCUCIsIkRlZmF1bHRDbGllbnRJZCI6IlRFU1QiLCJEZXNjcmlwdGlvbiI6IjIwYjkwMzBlLWI4YjMtNGE3Yy1hYWVkLWYxMDU5MTIxOTgzNyIsIlVwZGF0ZVVybCI6Imh0dHA6Ly9sb2NhbGhvc3Q6NjQ5ODIvanVkb3BheWFwaS9VcGRhdGVzdGF0dXMiLCJDdXN0b21lck5hbWUiOiJhaGFtZWQgTml5YXMiLCJDdXN0b21lck51bWJlciI6IjA3OTA3MjcwMzc5IiwiQ3VzdG9tZXJFbWFpbCI6ImZhemxhbkBldXJvc29mdHRlY2guY28udWsiLCJKdWRvSWQiOiIxMDA0NDY2MTYiLCJBUElUb2tlbiI6InBKQklKNUFncTFSeEJ6OUUiLCJBUElTZWNyZXQiOiJiZjk4NzYwMmFhZDBmNjEwOTkyYmQ0ZDA2NTZlZjhhNjM1YTNlNGU1YzMwZjY2ZDFkNmVlZTkzOGQxN2Q0OGEwIiwiSXNSZWdpc3RlckNhcmQiOnRydWUsInlvdXJDb25zdW1lclJlZmVyZW5jZSI6Ik5vVkFFeXQ5RTN4MUFNUkUiLCJ5b3VyUGF5bWVudFJlZmVyZW5jZSI6IjE1NTIxZTJjLTllZWItNDE5Ny04ODhiLWJhMWY3ZTI5OTc4MyIsfQ==";
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsModel userModel = new SharedPrefrenceHelper(this).getSettingModel();


        web.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
//        if (getIntent().hasExtra("fare")) {

        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(paymentUrl);

//    https://secure.worldpay.com/wcc/purchase?instId=1161493
//    web.loadUrl("https://secure.worldpay.com/wcc/purchase?instId=1161493&cartId=asdf&amount="+getIntent().getStringExtra("fare")+"&currency=GBP&desc=Pay online for your journey&name="+userModel.getName()+"&address1="+""+"&town=HARROW&postcode=75600&cardtype=Visa&country=UK&email="+userModel.getEmail()+"&testMode=0");
//            web.loadUrl("https://secure.worldpay.com/wcc/purchase?instId=" + sp.getString(Config.InstId, "") + "&cartId=asdf&amount=" + getIntent().getStringExtra("fare") + "&currency=GBP&desc=Pay online for your journey&name=" + userModel.getName() + "&address1=" + "" + "&town=HARROW&postcode=75600&cardtype=Visa&country=UK&email=" + userModel.getEmail() + "&testMode=0");
//        } else {
//            finish();
//        }
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
            currentPageUrl = view.getUrl();
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
            try {
                if (currentPageUrl.contains("ConfirmTransaction") && html.contains("clsjudopayment")) {
                    String[] htmlArray = htmlString.split("  <input type=");
                    if (htmlArray.length >= 2) {
                        String[] transactionFieldArray = htmlArray[2].split("=");
                        if (transactionFieldArray.length > 2) {
                            String TransactionID = "";

                            TransactionID = transactionFieldArray[2].replace("</div>", "").replace("\"", "").replace(">", "").trim();
                            String TransactionID1 = TransactionID.replace("\n", "").replace("<script src", "");
                            String TransactionID2 = TransactionID1.replace(" ", "").replace("<", "").replace("script", "").replace("src", "");
                            String TransactionID3 = TransactionID2.replace("/main/body/head", "").trim();
                            Intent intent = new Intent();
                            intent.putExtra("transactionID", TransactionID3);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                    }

                }
            } catch (Exception e) {

            }
//            String[] strings=htmlString.split("td width");
//            Log.e("","html is"+strings);


//            if (htmlString.contains("Your payment transaction completed successfully")) {
//                String[] htmlArray = htmlString.split("Transaction ID: ");
//                String TransactionID = "";
//
//                TransactionID = htmlArray[1].split("</p>")[0];
//                Intent intent = new Intent();
//                intent.putExtra("transactionID", TransactionID);
//                setResult(RESULT_OK, intent);
//                finish();
//
//
////            if(FStatus.contains("Refusée")){
////              //  Toast.makeText(PaymentView.this,"Payment Failed",Toast.LENGTH_LONG).show();
////                intent.putExtra("status","Payment Failed");
////               setResult(RESULT_CANCELED,intent);
////              finish();
////
////            }else{
////                intent.putExtra("status",FStatus);
////
////                intent.putExtra("dateTimeTrans",FdateTimeTrans);
////                intent.putExtra("transactionID",FTransactionId);
////                setResult(RESULT_OK,intent);
////                finish();
////            }
//            }
        }
    }
}
