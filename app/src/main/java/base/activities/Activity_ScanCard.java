package base.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;



public class Activity_ScanCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    Intent intent = new Intent(this, CardIOActivity.class);
//        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
//        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
//        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
//        intent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
//        intent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);// default: false
//        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
//        startActivityForResult(intent, Judo.CARD_SCANNING_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

/*
        if (Judo.CARD_SCANNING_REQUEST == requestCode) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
            if (scanResult != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Judo.JUDO_CARD, new Card.Builder()
                        .setCardNumber(scanResult.cardNumber)

                        .build());

                setResult(Judo.RESULT_CARD_SCANNED, resultIntent);
            }
            finish();
        }
*/
    }
}
