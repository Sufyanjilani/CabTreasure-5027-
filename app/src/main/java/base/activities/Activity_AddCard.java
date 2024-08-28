//package base.activities;
//
//import android.content.Intent;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import com.blogspot.atifsoftwares.animatoolib.Animatoo;
//import com.craftman.cardform.Card;
//import com.craftman.cardform.CardForm;
//import com.craftman.cardform.DataLoadListner;
//import com.craftman.cardform.OnPayBtnClickListner;
//import com.eurosofttech.kingscars.R;
//import com.tfb.fbtoast.FBToast;
//
//import base.fragments.Fragment_Main;
//import base.models.Model_CardDetails;
//import base.utils.SharedPrefrenceHelper;
//
//
//public class Activity_AddCard extends AppCompatActivity {
//    CardForm cardForm;
//    boolean isPayment = false;
//    SharedPrefrenceHelper sharedPrefrenceHelper;
//    boolean startup = false;
//    Bundle savedInstanceState;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_add_card);
//        sharedPrefrenceHelper = new SharedPrefrenceHelper(Activity_AddCard.this);
//        Model_CardDetails modelCardDetails = sharedPrefrenceHelper.getCardModel();
//        if (getIntent() != null && getIntent().hasExtra("payment")) {
//            isPayment = true;
//        }
//        cardForm = (CardForm) findViewById(R.id.card_form);
//        cardForm.setServerUseId(sharedPrefrenceHelper.getSettingModel().getUserServerID());
//
////        CardForm cardForm
//        if (modelCardDetails != null && !modelCardDetails.getCardNumber().equals("")) {
//            Card card = new Card(new Card.Builder(
//                    modelCardDetails.getCardNumber(),
//                    Integer.parseInt(modelCardDetails.getExpiryMonth()),
//                    Integer.parseInt(modelCardDetails.getExpiryYear()),
//                    modelCardDetails.getCVV()
//
//            ));
//        }
//
//
//        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
//            @Override
//            public void onClick(Card card) {
//                if (card != null) {
//                    String cardNo = card.getNumber();
//                    String cardName = card.getName();
//                    int ExpYear = card.getExpYear();
//                    int ExtMonth = card.getExpMonth() == null ? 0 : card.getExpMonth();
//                    String Cvc = card.getCVC();
//                    if (!cardNo.equals("") && ExpYear > 0 && ExtMonth > 0 && !Cvc.equals("")) {
//                        sharedPrefrenceHelper = new SharedPrefrenceHelper(Activity_AddCard.this);
//
//                        Model_CardDetails modelCardDetails = new Model_CardDetails();
//                        modelCardDetails.setCardNumber(cardNo);
//                        modelCardDetails.setCVV(Cvc);
//                        modelCardDetails.setExpiryMonth(String.valueOf(ExtMonth));
//                        modelCardDetails.setExpiryYear(String.valueOf(ExpYear));
////                modelCardDetails.setAddress(customerAddress.getText().toString());
//                        modelCardDetails.setCardName(cardName);
////                modelCardDetails.setPostCode(customerPostCode.getText().toString());
//                        sharedPrefrenceHelper.putCardModel(modelCardDetails);
////                        Toast.makeText(Activity_AddCard.this, "Add Card Successfully", Toast.LENGTH_SHORT).show();
//                        FBToast.infoToast(Activity_AddCard.this, "Add Card Successfully", FBToast.LENGTH_SHORT);
//                        if (!isPayment) {
//                            startActivity(new Intent(Activity_AddCard.this, Fragment_Main.class));
//                        }
////                        else if(startup){
////                            startActivity(new Intent(Activity_AddCard.this,Activity_Default_Payment.class));
////                        }
//                        Animatoo.animateSlideLeft(Activity_AddCard.this);
//                        finish();
//                    } else {
//                        Toast.makeText(Activity_AddCard.this, "Please enter complete card details", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//
//                    FBToast.infoToast(Activity_AddCard.this, "Card details removed successfully", FBToast.LENGTH_SHORT);
//                    Animatoo.animateSlideLeft(Activity_AddCard.this);
//                    finish();
//                }
//            }
//        });
//    }
//
//    DataLoadListner dataLoadListner;
//
//    public void setDataLoadListner(DataLoadListner onPayBtnClickListner) {
//        this.dataLoadListner = onPayBtnClickListner;
//    }
//
//    public void goBack(View v) {
//        if (isPayment) {
//
//        } else {
////            startActivity(new Intent(this, Activity_Default_Payment.class));
//        }
//        Animatoo.animateSlideRight(this);
//
//        finish();
//    }
//}
