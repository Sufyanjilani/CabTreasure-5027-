package base.activities

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import base.utils.CommonVariables
import com.eurosoft.customerapp.R

class ActivityDeleteAccount : AppCompatActivity() {


    private val deleteAccountUrl = "https://treasurecloud.co.uk/customerappdemo/index.aspx?AccountId="+CommonVariables.clientid
    val successDeleteAccountUrl = "https://treasurecloud.co.uk/customerappdemo/Success.aspx"
    private var webView:WebView? = null
    private var ivClose:ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)

        webView = findViewById(R.id.webView)
        ivClose = findViewById(R.id.ivClose)

        webView?.settings?.javaScriptEnabled = true
        webView?.webViewClient =webViewClient

        webView?.webChromeClient = WebChromeClient()
        webView?.loadUrl(deleteAccountUrl)


        ivClose?.setOnClickListener {
            finish()
        }
    }

    private val webViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

            val url = request?.url?.toString()
            if (url != null && url.equals(successDeleteAccountUrl,true)) {
                showDeleteSuccessDialog()
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    private fun showDeleteSuccessDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_layout, null)

        val dialogHeading: TextView = dialogView.findViewById(R.id.dialogHeading)
        val dialogMessage: TextView = dialogView.findViewById(R.id.dialogMessage)
        val okButton: Button = dialogView.findViewById(R.id.okButton)
        // Set your custom dialog properties here
        dialogHeading.text = "Success"
        dialogMessage.text = "Account Successfully Deleted"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        okButton.setOnClickListener {
            dialog.dismiss()
            setResult(Activity.RESULT_OK)
            finish()
        }

        dialog.show()
    }
}