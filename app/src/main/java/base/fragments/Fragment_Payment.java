package base.fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static base.utils.CommonMethods.checkIfHasNullForString;
import static base.utils.CommonMethods.getAppVersion;
import static base.utils.CommonVariables.Currency;
import static base.utils.CommonVariables.JUDO_REGISTER_REQUEST;
import static base.utils.CommonVariables.KONNECT_PAY_REQUEST;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eurosoft.customerapp.R;
import com.google.gson.Gson;
import com.tfb.fbtoast.FBToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import base.activities.Activity_AccountMemberLogin;
import base.activities.Activity_AddStripeCardDetail;
import base.activities.Activity_HomePayment;
import base.activities.Activity_KonnectPayRegisterCardWeb;
import base.activities.PaymentViewJudo3ds;
import base.listener.Listener_DeleteCard_Konnect;
import base.listener.Listener_GetAll_cards_Konnect;
import base.listener.Listener_GetPayment;
import base.listener.Listener_Judo_AddCard;
import base.listener.Listener_Judo_GetAllCards;
import base.listener.Listener_Register_Card_KonnectPay;
import base.listener.Listener_Stripe_DeleteCard;
import base.listener.Listener_Stripe_GetAllCards;
import base.manager.Manager_DeleteCustomerCardsKonnect;
import base.manager.Manager_GetCustomerCardsKonnect;
import base.manager.Manager_GetPaymentUrl;
import base.manager.Manager_Judo_AddCard;
import base.manager.Manager_Judo_GetAllCards;
import base.manager.Manager_RegisterCardKonnectPay;
import base.manager.Manager_Stripe_DeleteCard;
import base.manager.Manager_Stripe_GetAllCards;
import base.models.CardJudoModel;
import base.models.KonnectCardModel;
import base.models.ParentPojo;
import base.models.Payment;
import base.models.SettingsModel;
import base.models.StripeCardModel;
import base.models.Stripe_Model;
import base.utils.CommonMethods;
import base.utils.CommonVariables;
import base.utils.Config;
import base.utils.SharedPrefrenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Fragment_Payment extends Fragment {

    public static final String REFERENCE = UUID.randomUUID().toString();

    private String GateWay = "";
    private Boolean isKonnectPay = false;
    private String selectedType = "";
    private String appVersion = "";

    private int selectedIndexFromAdapter;

    boolean isLoad = false;

    private TextView btnDone;
    private TextView paymentSubLabel;
    private TextView paymentTitleLabel;
    private ImageView imgBack;
    private RecyclerView paymentRv;
    private RecyclerView creditCardRv;


    private ParentPojo p = new ParentPojo();
    private SettingsModel settingsModel;
    private SharedPreferences sp;
    private SharedPrefrenceHelper spHelper;
    private SharedPreferences.Editor edit;

    private JudoCardsAdapter judoCardsAdapter;
    private PaymentAdapter paymentAdapter;
    private CardJudoModel cardJudoModelFromAdapter;

    // ArrayList
    private ArrayList<Payment> payments = new ArrayList<>();
    private ArrayList<CardJudoModel> judoCardList;
    private ArrayList<StripeCardModel> stripeCardModelArrayList = new ArrayList<>();
    private ArrayList<KonnectCardModel> konnectCardModelArrayList = new ArrayList<>();

    // Listener
    private Listener_Judo_AddCard listener_judo_addCard;
    private Listener_Register_Card_KonnectPay listener_Register_Card_KonnectPay;


    private Listener_Judo_GetAllCards listener_judo_getAllCards;
    private Listener_Stripe_GetAllCards listener_stripe_getAllCards;
    //  private Listener_GetCustomerCardsKonnect listener_GetCustomerCardsKonnect;
    private Listener_Stripe_DeleteCard listener_stripe_deleteCard;
    private Listener_DeleteCard_Konnect listener_DeleteCard_Konnect;

    private Listener_GetAll_cards_Konnect listener_GetAll_cards_Konnect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sp.edit();

        spHelper = new SharedPrefrenceHelper(getActivity());
        judoCardList = spHelper.getJudoCardList();

        settingsModel = spHelper.getSettingModel();

        GateWay = sp.getString(Config.Gateway, "").toLowerCase();
        isKonnectPay = sp.getString(Config.IsKonnectPay, "0").equals("1");
        selectedType = sp.getString(CommonVariables.paymentType, p.getCash());

        if (selectedType == null || selectedType.equals("")) {
            selectedType = p.getCash();
            edit.putString(CommonVariables.paymentType, selectedType);
            edit.commit();
        }
    }

    int count = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.layout_payment_activity, container, false);

        initPayments();
        init(ret);
        listener();

        try {
            appVersion = getAppVersion(requireContext());
        } catch (Exception ex) {

        }

        return ret;
    }

    @Override
    public void onResume() {
        super.onResume();


        if (isKonnectPay && selectedType.equalsIgnoreCase("credit card")) {
            getCardsKonnect();
        } else if (selectedType.equalsIgnoreCase("credit card") && GateWay.contains("judo")) {
            new Manager_Judo_GetAllCards(getContext(), getJsonForJudoCard(), listener_judo_getAllCards).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else if (selectedType.equalsIgnoreCase("credit card") && GateWay.contains(Config.Stripe)) {
            new Manager_Stripe_GetAllCards(getContext(), true, spHelper.getStripeCustomerId(), listener_stripe_getAllCards).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        loadPaymentList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case JUDO_REGISTER_REQUEST:
                handleRegisterCardResult(resultCode, data);
                break;
        }
        if (requestCode == 1010 && resultCode == RESULT_OK) {
            if (GateWay.contains(Config.Stripe)) {
                new Manager_Stripe_GetAllCards(getContext(), true, spHelper.getStripeCustomerId(), listener_stripe_getAllCards).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            //new GetAllStripeCardList();
        } else if (resultCode == RESULT_OK && requestCode == JUDO_REGISTER_REQUEST) {
            if (GateWay.contains("judo")) {
                new Manager_Judo_GetAllCards(getContext(), getJsonForJudoCard(), listener_judo_getAllCards).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);            // new JudoCardRequest(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } else if (resultCode == RESULT_OK && requestCode == KONNECT_PAY_REQUEST) {

            getCardsKonnect();
        }


    }

    private void getCardsKonnect() {
        new Manager_GetCustomerCardsKonnect(settingsModel.getUserServerID(), appVersion, listener_GetAll_cards_Konnect).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void initPayments() {
        if (!sp.getString(Config.PaymentTypes, "").equals("")) {

            String[] paymentTypes = sp.getString(Config.PaymentTypes, "").split(",");

            for (int i = 0; i < paymentTypes.length; i++) {

                if (paymentTypes[i].equalsIgnoreCase(Config.CASH)) {
                    payments.add(new Payment(p.getCash(), R.drawable.cash, selectedType.equalsIgnoreCase(p.getCash()) ? true : false, false, ""));

                } else if (paymentTypes[i].equalsIgnoreCase(Config.ACCOUNT)) {
                    payments.add(new Payment(p.getAccount(), R.drawable.ic_account, selectedType.equalsIgnoreCase(p.getAccount()) ? true : false, true, ""));

                } else if (paymentTypes[i].equalsIgnoreCase(Config.CARDCAR)) {
                    payments.add(new Payment(p.getCardInCar(), R.drawable.ic_credit_card, selectedType.equalsIgnoreCase(p.getCardInCar()) ? true : false, false, "(" + GateWay.toUpperCase() + ")"));
                } else if (sp.getString(Config.PaymentTypes, "").contains("3") || isKonnectPay) {
                    payments.add(new Payment(p.getCreditCard(), R.drawable.ic_credit_card, selectedType.equalsIgnoreCase(p.getCreditCard()) ? true : false, true, "(" + GateWay.toUpperCase() + ")"));
                }
            }


        }

    }

    private void init(View ret) {
        paymentTitleLabel = ret.findViewById(R.id.titleTv);
        paymentTitleLabel.setText(p.getPayment());

        paymentSubLabel = ret.findViewById(R.id.paymentSubLabel);
        paymentSubLabel.setText(p.getChoosePaymentMethodToAdd());

        btnDone = ret.findViewById(R.id.btnDone);
        btnDone.setText(p.getDone());

        imgBack = ret.findViewById(R.id.menuIv);

        creditCardRv = ret.findViewById(R.id.creditCardRv);
        creditCardRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        creditCardRv.hasFixedSize();

        paymentRv = ret.findViewById(R.id.paymentRv);
        paymentRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        paymentRv.hasFixedSize();

        creditCardRv.setVisibility((selectedType.equalsIgnoreCase(p.getCreditCard()) ? View.VISIBLE : GONE));
    }

    private void listener() {
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedType.toLowerCase().startsWith("credit")) {

                    if (GateWay.contains(Config.JUDOPAY) && spHelper.getJudoCardList().size() == 0) {
                        edit.putString(CommonVariables.paymentType, "cash");
                        edit.commit();
                        if (payments.size() > 0) {
                            paymentAdapter.setDefault(0);
                        }
                    }
                }

                ((Fragment_Main) getActivity()).toggleDrawer();
                getFragmentManager().popBackStack();
            }
        });

        btnDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedType.equalsIgnoreCase(p.getAccount())) {
                    if (sp.getBoolean(CommonVariables.ISMEMBERUSERLOGIN, false)) {
                        edit.putString(CommonVariables.paymentType, selectedType);
                        edit.commit();
                        ((Fragment_Main) getActivity()).onItemClick(null, null, 99, 0);
                    } else {
                        FBToast.errorToast(getActivity(), p.getAccDetailsNotFound(), FBToast.LENGTH_SHORT);
                    }

                } else if (selectedType.toLowerCase().equalsIgnoreCase(p.getCreditCard())) {

                    if (isKonnectPay) {
                        if (konnectCardModelArrayList.size() > 0) {
                            try {

                                KonnectCardModel konnectCardModel = konnectCardModelArrayList.get(selectedIndexFromAdapter);
                                spHelper.saveKonnectOneCard(konnectCardModel);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            edit.putString(CommonVariables.paymentType, selectedType);
                            edit.commit();
                            ((Fragment_Main) getActivity()).onItemClick(null, null, 99, 0);

                        } else {
                            FBToast.errorToast(getActivity(), p.getCardDetailsNotFound(), FBToast.LENGTH_SHORT);
                            return;
                        }
                    } else if (GateWay.contains(Config.JUDOPAY)) {
                        if (spHelper.getJudoCardList().size() > 0) {
                            try {
                                edit.putString(CommonVariables.paymentType, selectedType);
                                edit.commit();
                                ((Fragment_Main) getActivity()).onItemClick(null, null, 99, 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                            FBToast.errorToast(getActivity(), p.getCardDetailsNotFound(), FBToast.LENGTH_SHORT);
                        }

                    } else if (GateWay.contains(Config.WORLDPAY) || GateWay.equals(Config.Stripe)) {
                        if (stripeCardModelArrayList.size() > 0) {
                            try {
                                spHelper.saveToSharePrefForStripeForOneCard(new Stripe_Model(
                                        stripeCardModelArrayList.get(selectedIndexFromAdapter).getId(),
                                        stripeCardModelArrayList.get(selectedIndexFromAdapter).getLast4(),
                                        "Android" + "-" + spHelper.getSettingModel().getUserServerID() + "-" + System.currentTimeMillis(),
                                        stripeCardModelArrayList.get(selectedIndexFromAdapter).getCustomer(),
                                        "" + sp.getString(Config.Stripe_SecretKey, ""),
                                        Config.Stripe,
                                        (!sp.getString(CommonVariables.CurrencySymbol, "\u00A3").equals("$")) ? "GBP" + "" : Currency
                                ));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            edit.putString(CommonVariables.paymentType, selectedType);
                            edit.commit();
                            ((Fragment_Main) getActivity()).onItemClick(null, null, 99, 0);
                        } else {
                            FBToast.errorToast(getActivity(), p.getCardDetailsNotFound(), FBToast.LENGTH_SHORT);
                            return;
                        }
                    } else {
                        if (spHelper.getCardModel() != null && !spHelper.getCardModel().getCardNumber().equals("")) {
                            try {
                                edit.putString(CommonVariables.paymentType, selectedType);
                                edit.commit();
                                ((Fragment_Main) getActivity()).onItemClick(null, null, 99, 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            FBToast.errorToast(getActivity(), p.getCardDetailsNotFound(), FBToast.LENGTH_SHORT);
                        }
                    }
                } else {
                    edit.putString(CommonVariables.paymentType, selectedType);
                    edit.commit();
                    ((Fragment_Main) getActivity()).onItemClick(null, null, 99, 0);
                }

            }
        });

        // Stripe Get All Cards Listener
        listener_stripe_getAllCards = new Listener_Stripe_GetAllCards() {
            @Override
            public void onComplete(String result) {
                stripeCardModelArrayList.clear();

                try {
                    if (result.equalsIgnoreCase("No Card Found")) {
                        FBToast.errorToast(getContext(), result, FBToast.LENGTH_SHORT);
                        return;
                    }
                    if (result != null && !result.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("hasError")) {
                            } else {
                                if (jsonObject.getString("message").toLowerCase().contains("success")) {
                                    JSONObject responseCardList = jsonObject.getJSONObject("responseCardList");
                                    JSONArray datas = responseCardList.getJSONArray("data");

                                    for (int i = 0; i < datas.length(); i++) {
                                        JSONObject o = datas.getJSONObject(i);

                                        String id = o.getString("id");

                                        JSONObject cardObj = o.getJSONObject("card");
                                        String brand = cardObj.getString("brand");
                                        String country = cardObj.getString("country");
                                        String exp_month = cardObj.getString("exp_month");
                                        String exp_year = cardObj.getString("exp_year");
                                        String last4 = cardObj.getString("last4");

                                        String customer = o.getString("customer");
                                        String type = o.getString("type");

                                        StripeCardModel s = new StripeCardModel(id, customer, exp_month, exp_year, last4, country, brand, 0, false);
                                        stripeCardModelArrayList.add(s);
                                    }

                                    loadCreditCardsListForStripe();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        result = result.replace("error_", "");
                        FBToast.errorToast(getContext(), "Payment Failed\nUnable to process payment, Please try again later\n" + result, FBToast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Stripe Delete Card Listener
        listener_stripe_deleteCard = new Listener_Stripe_DeleteCard() {
            @Override
            public void onComplete(String result) {

                try {
                    stripeCardModelArrayList.clear();
                    if (result != null && !result.startsWith("false") && !result.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("hasError")) {
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                if (jsonObject.getString("message").toLowerCase().contains("success")) {
                                    new Manager_Stripe_GetAllCards(getContext(), true, spHelper.getStripeCustomerId(), listener_stripe_getAllCards).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Payment Failed\nUnable to process payment, Please try again later", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        listener_DeleteCard_Konnect = new Listener_DeleteCard_Konnect() {
            @Override
            public void onComplete(String result) {

                try {
                    stripeCardModelArrayList.clear();
                    if (result != null && !result.startsWith("false") && !result.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("hasError")) {
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                if (jsonObject.getString("message").toLowerCase().contains("success")) {
                                    getCardsKonnect();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Payment Failed\nUnable to process payment, Please try again later", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        listener_GetAll_cards_Konnect = new Listener_GetAll_cards_Konnect() {
            @Override
            public void onComplete(String result) {
                konnectCardModelArrayList.clear();

                try {
                    if (result != null && !result.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("hasError")) {
                            } else {
                                if (jsonObject.getString("message").toLowerCase().contains("success")) {
                                    JSONArray responseCardList = jsonObject.getJSONArray("responseCardList");


                                    for (int i = 0; i < responseCardList.length(); i++) {
                                        JSONObject cardRootObj = responseCardList.getJSONObject(i);
                                        KonnectCardModel card1 = new KonnectCardModel();
                                        try {
                                            String id = cardRootObj.getString("id");
                                            card1.setCardToken(id);
                                        } catch (Exception exception) {

                                        }

                                        JSONObject card = cardRootObj.getJSONObject("card");

                                        try {

                                            String exp_month = checkIfHasNullForString(card.getString("expMonth"));
                                            card1.setExpMonth(exp_month);

                                        } catch (Exception exception) {

                                        }

                                        try {
                                            String exp_year = checkIfHasNullForString(card.getString("expYear"));

                                            card1.setExpYear(exp_year);
                                        } catch (Exception exception) {

                                        }
                                        try {
                                            String last4 = checkIfHasNullForString(card.getString("last4"));
                                            card1.setLastFour(last4);

                                        } catch (Exception exception) {

                                        }

                                        try {
                                            String customerId = checkIfHasNullForString(cardRootObj.getString("customerId"));
                                            card1.setStripeCustomerId(customerId);
                                        } catch (Exception exception) {

                                        }

//                                        String customer = checkIfHasNullForString(cardRootObj.getString("customer"));
                                        try {
                                            String type = checkIfHasNullForString(cardRootObj.getString("type"));
                                        } catch (Exception exception) {

                                        }
                                        try {
                                            String brand = checkIfHasNullForString(card.getString("brand"));
                                            card1.setBrand(brand);
                                        } catch (Exception exception) {

                                        }
                                        try {


                                            String postalCode = checkIfHasNullForString(cardRootObj.getJSONObject("billingDetails").getJSONObject("address").getString("postalCode"));

                                            card1.setPostalCode(postalCode);
                                        } catch (Exception exception) {

                                        }
                                        try {
                                            String fingerprint = checkIfHasNullForString(card.getString("fingerprint"));

                                            card1.setFingerprint(fingerprint);


                                        } catch (Exception exception) {

                                        }

                                        String uniqueId = spHelper.getSettingModel().getUserServerID() + "" + System.currentTimeMillis();

                                        String currency = (!sp.getString(CommonVariables.CurrencySymbol, "\u00A3").equals("$")) ? "GBP" + "" : Currency;
//


                                        card1.setUniqueId(uniqueId);
                                        card1.setGatewayType(Config.Stripe);
                                        card1.setCurrency(currency);


                                        konnectCardModelArrayList.add(card1);
                                    }

                                    loadCreditCardsListForKonnect();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        result = result.replace("error_", "");
                        FBToast.errorToast(getContext(), "Payment Failed\nUnable to process payment, Please try again later\n" + result, FBToast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Judo Get All Cards Listener

        listener_judo_getAllCards = new Listener_Judo_GetAllCards() {
            @Override
            public void onComplete(String result) {
                creditCardRv.setVisibility(View.VISIBLE);

                try {
                    if (result != null) {
                        try {
                            JSONObject parentObject = new JSONObject(result);

                            if (parentObject.getBoolean("HasError")) {
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error")
                                        .setContentText(parentObject.getString("Message"))
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                //                                            new InitializeAppDb(Activity_Default_Payment.this, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                            }

                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                //										getActivity().finish();
                                            }

                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                            }
                                        })
                                        .show();
                            } else {
                                JSONArray jsonArray = new JSONArray(parentObject.getString("Data"));
                                ArrayList<CardJudoModel> cardDetailsModelArrayList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    boolean IsDefault = jsonObject.optBoolean("IsDefault");
                                    long RecordId = jsonObject.optLong("RecordId");
                                    String token = jsonObject.optString("CCDetails");

                                    CardJudoModel cardJudoModel = new CardJudoModel();
                                    /// NEW JUDO WORK
                                    cardJudoModel.setRawTokenString(token);
                                    try {
                                        String[] consumer__ = token.replace("Token|", "")
                                                .replace("consumer|", "")
                                                .replace("consumertoken|", "")
                                                .replace("lastfour|", "")
                                                .replace("enddate|", "")
                                                .replace("type|", "")
                                                .replace("receiptid|", "")
                                                .trim().split("<<<");

                                        try {
                                            cardJudoModel.setToken(consumer__[0]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            cardJudoModel.setConsumerReference(consumer__[1]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            cardJudoModel.setConsumerToken(consumer__[2]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            cardJudoModel.setLastFour(consumer__[3]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            cardJudoModel.setEndDate(consumer__[4]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            cardJudoModel.setReceiptid(consumer__[5]);
                                        } catch (Exception e) {
                                            cardJudoModel.setReceiptid("");
                                            e.printStackTrace();
                                        }
                                        cardJudoModel.setType(0);
                                        cardJudoModel.set3DS(true);
                                        cardJudoModel.setDefault(IsDefault);
                                        cardJudoModel.setCardId(RecordId);
                                        cardJudoModel.setCardLabel("+Add Card Label");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    cardDetailsModelArrayList.add(cardJudoModel);
                                }


                                if (cardDetailsModelArrayList.size() == 0) {
                                    spHelper.removeJudoCardModelArrayList();
                                    try {
                                        spHelper.saveCardJudoModel(new CardJudoModel());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (cardDetailsModelArrayList.size() > 0) {
                                    spHelper.putJudoCardModelArrayList(cardDetailsModelArrayList);
                                    try {
                                        spHelper.saveCardJudoModel(cardDetailsModelArrayList.get(0));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                isLoad = true;

                                loadCreditCardsListForJudo();

                               /* if (cardDetailsModelArrayList.size() == 1) {
                                    selectedIndexFromAdapter = 0;
                                    new Manager_Judo_AddCard(getContext(), 3, settingsModel.getEmail(), settingsModel.getPassword(), cardDetailsModelArrayList.get(0), judoCardList, listener_judo_addCard).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                    new SaveCardReciept(3).execute(cardDetailsModelArrayList.get(0));
                                }*/
                            }

                        } catch (Exception e) {
                            FBToast.errorToast(getActivity(), e.getMessage(), FBToast.LENGTH_SHORT);
                        }
                    } else {
                        FBToast.errorToast(getActivity(), "No Data found", FBToast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        // Judo Add Cards Listener
        listener_judo_addCard = new Listener_Judo_AddCard() {
            @Override
            public void onComplete(String result) {
                try {
                    if (result != null) {
                        try {
                            JSONObject parentObject = new JSONObject(result);

                            if (parentObject.getBoolean("HasError")) {
                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error")
                                        .setContentText(parentObject.getString("Message"))
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                //                                            new InitializeAppDb(Activity_Default_Payment.this, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                            }

                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                //										Activity_HomePayment.this.finish();
                                            }

                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                            }
                                        })
                                        .show();
                            } else {
                                String data = parentObject.getString("Data");

                                // add card
                                if (result.endsWith(">>>1")) {

                                }
                                // remove card
                                else if (result.endsWith(">>>2")) {

                                }

/*                                JSONArray jsonArray = new JSONArray(parentObject.getString("Data"));
                                ArrayList<CardJudoModel> cardDetailsModelArrayList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    boolean IsDefault = jsonObject.optBoolean("IsDefault");
                                    long RecordId = jsonObject.optLong("RecordId");
                                    String token = jsonObject.optString("CCDetails");

                                    CardJudoModel cardJudoModel = new CardJudoModel();

                                    try {
                                        String[] consumer__ = token.replace("Token|", "")
                                                .replace("consumer|", "")
                                                .replace("consumertoken|", "")
                                                .replace("lastfour|", "")
                                                .replace("enddate|", "")
                                                .replace("type|", "")
                                                .replace("receiptid|", "")
                                                .trim()
                                                .split("<<<");

                                        try {
                                            cardJudoModel.setToken(consumer__[0]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            cardJudoModel.setConsumerReference(consumer__[1]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            cardJudoModel.setConsumerToken(consumer__[2]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            cardJudoModel.setLastFour(consumer__[3]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            cardJudoModel.setEndDate(consumer__[4]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            cardJudoModel.setReceiptid(consumer__[5]);
                                        } catch (Exception e) {
                                            cardJudoModel.setReceiptid("");
                                            e.printStackTrace();
                                        }
                                        cardJudoModel.setType(0);
                                        cardJudoModel.set3DS(true);
                                        cardJudoModel.setDefault(IsDefault);
                                        cardJudoModel.setCardId(RecordId);
                                        /// NEW JUDO WORK
                                        cardJudoModel.setRawTokenString(token);
                                        cardJudoModel.setCardLabel("+Add Card Label");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    cardDetailsModelArrayList.add(cardJudoModel);
                                }

                                if (cardDetailsModelArrayList.size() == 0) {
                                    spHelper.removeJudoCardModelArrayList();
                                    try {
                                        spHelper.saveCardJudoModel(new CardJudoModel());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (cardDetailsModelArrayList.size() > 0) {
                                    spHelper.putJudoCardModelArrayList(cardDetailsModelArrayList);
                                    try {
                                        spHelper.saveCardJudoModel(cardDetailsModelArrayList.get(0));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }*/

                                isLoad = true;
                                new Manager_Judo_GetAllCards(getContext(), getJsonForJudoCard(), listener_judo_getAllCards).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                loadCreditCardsListForJudo();
/*
                                if (cardDetailsModelArrayList.size() == 1) {
                                    selectedIndexFromAdapter = 0;
                                    new Manager_Judo_AddCard(Activity_HomePayment.this, 3, settingsModel.getEmail(), settingsModel.getPassword(), cardDetailsModelArrayList.get(0), judoCardList, listener_judo_addCard).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                    new SaveCardReciept(3).execute(cardDetailsModelArrayList.get(0));
                                }*/
                            }

                        } catch (Exception e) {
                            FBToast.errorToast(getContext(), e.getMessage(), FBToast.LENGTH_SHORT);
                        }
                    } else {
                        FBToast.errorToast(getContext(), "No Data found", FBToast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        listener_Register_Card_KonnectPay = new Listener_Register_Card_KonnectPay() {
            SweetAlertDialog mDialog;

            @Override
            public void onComplete(String result) {
                try {
                    mDialog.dismiss();
                    if (result != null) {
                        try {
                            JSONObject parentObject = new JSONObject(result);

                            if (parentObject.getBoolean("HasError")) {
                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error")
                                        .setContentText(parentObject.getString("Message"))
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                //                                            new InitializeAppDb(Activity_Default_Payment.this, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                            }

                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                //										Activity_HomePayment.this.finish();
                                            }

                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                                            }
                                        })
                                        .show();
                            } else {
                                String url = parentObject.getString("Data");
                                launchKonnectPyRegisterCard(url);

                            }

                        } catch (Exception e) {
                            FBToast.errorToast(getContext(), e.getMessage(), FBToast.LENGTH_SHORT);
                        }
                    } else {
                        FBToast.errorToast(getContext(), "No Data found", FBToast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPre() {
                mDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                mDialog.setTitleText("");
                mDialog.setContentText(p.getPleaseWait());
                mDialog.setCancelable(false);
                mDialog.show();
            }
        };


    }

    private void launchKonnectPyRegisterCard(String url) {
        selectedType = "credit card";
        startActivityForResult(new Intent(getContext(), Activity_KonnectPayRegisterCardWeb.class).putExtra("paymentUrl", url), KONNECT_PAY_REQUEST);
    }

    private void handleRegisterCardResult(int resultCode, Intent data) {
        switch (resultCode) {
            /// New Judo Work
            case RESULT_OK:
//                JudoResult receipt = data.getParcelableExtra(AddCardClass.INSTANCE.getJUDORECEIPT());
//
//                CardJudoModel cardJudoModel = new CardJudoModel();
//                cardJudoModel.setToken(receipt.getCardDetails().getToken());
//                cardJudoModel.setEndDate(receipt.getCardDetails().getFormattedEndDate());
//                cardJudoModel.setLastFour(receipt.getCardDetails().getLastFour());
//                cardJudoModel.setConsumerToken(receipt.getConsumer().getConsumerToken());
//                cardJudoModel.setConsumerReference(receipt.getConsumer().getYourConsumerReference());
//                cardJudoModel.setType(receipt.getCardDetails().getType());
//                cardJudoModel.set3DS(true);
//                cardJudoModel.setReceiptid(receipt.getReceiptId());
//                ////arraylist
//                new Manager_Judo_AddCard(getContext(), 1, settingsModel.getEmail(), settingsModel.getPassword(), cardJudoModel, judoCardList, listener_judo_addCard).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
////                new SaveCardReciept(1).execute(cardJudoModel);


                String makeDecode = data.getStringExtra("transactionID");

                byte[] _data = android.util.Base64.decode(makeDecode, android.util.Base64.DEFAULT);
                String transactionID = "";
                try {
                    transactionID = new String(_data, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String pickDetail = transactionID;  // base 64 decode

                CardJudoModel cardJudoModel = new CardJudoModel();
                /// NEW JUDO WORK
                cardJudoModel.setRawTokenString(pickDetail);

                try {
                    String[] consumer__ = pickDetail.replace("Token|", "")
                            .replace("consumer|", "")
                            .replace("consumertoken|", "").replace("lastfour|", "").replace("enddate|", "").replace("type|", "").replace("receiptid|", "").trim().split("<<<");

                    try {
                        cardJudoModel.setToken(consumer__[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        cardJudoModel.setConsumerReference(consumer__[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        cardJudoModel.setConsumerToken(consumer__[2]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        cardJudoModel.setLastFour(consumer__[3]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        cardJudoModel.setEndDate(consumer__[4]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        cardJudoModel.setReceiptid(consumer__[5]);
                    } catch (Exception e) {
                        cardJudoModel.setReceiptid("");
                        e.printStackTrace();
                    }
                    cardJudoModel.setType(0);
                    cardJudoModel.set3DS(true);

                    new Manager_Judo_AddCard(getContext(), 1, settingsModel.getEmail(), settingsModel.getPassword(), cardJudoModel, judoCardList, listener_judo_addCard).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 3:
                break;
            case 4:
                FBToast.errorToast(getActivity(), "Invalid Card Details", FBToast.LENGTH_SHORT);
                break;
//            case Judo.RESULT_DECLINED:
//                FBToast.errorToast(getActivity(), "Card Declined!", FBToast.LENGTH_SHORT);
//                break;
        }
    }

    public void loadCreditCardsListForStripe() {
        try {
            if (stripeCardModelArrayList.size() > 0) {
                try {
                    if (sp.getString(Config.EnableCardHold, "0").equals("1")) {
                        // Hold Payment Work
                        spHelper.saveToSharePrefForStripeForOneCard(new Stripe_Model(
                                stripeCardModelArrayList.get(0).getId(),
                                stripeCardModelArrayList.get(0).getLast4(),
                                "Android" + "-" + spHelper.getSettingModel().getUserServerID() + "-" + System.currentTimeMillis(),
                                stripeCardModelArrayList.get(0).getCustomer(),
                                "" + sp.getString(Config.Stripe_SecretKey, ""),
                                Config.Stripe,
                                (!sp.getString(CommonVariables.CurrencySymbol, "\u00A3").equals("$")) ? "GBP" + "" : Currency));
                    } else {
                        // Don't Hold Payment Work
                        spHelper.saveToSharePrefForStripeForOneCard(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            creditCardRv.setVisibility((stripeCardModelArrayList.size() > 0) ? View.VISIBLE : GONE);
            StripeCardAdapter stripeCardAdapter = new StripeCardAdapter(getActivity(), stripeCardModelArrayList);
            creditCardRv.setAdapter(stripeCardAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCreditCardsListForKonnect() {
        try {
//            if (konnectCardModelArrayList.size() > 0) {
//                try {
//                    if (sp.getString(Config.EnableStripeHold, "0").equals("1")) {
//                        // Hold Payment Work
//                        spHelper.saveToSharePrefForStripeForOneCard(new Stripe_Model(
//                                stripeCardModelArrayList.get(0).getId(),
//                                stripeCardModelArrayList.get(0).getLast4(),
//                                "Android" + "-" + spHelper.getSettingModel().getUserServerID() + "-" + System.currentTimeMillis(),
//                                stripeCardModelArrayList.get(0).getCustomer(),
//                                "" + sp.getString(Config.Stripe_SecretKey, ""),
//                                Config.Stripe,
//                                (!sp.getString(CommonVariables.CurrencySymbol, "\u00A3").equals("$")) ? "GBP" + "" : "USD" + ""));
//                    } else {
//                        // Don't Hold Payment Work
//                        spHelper.saveToSharePrefForStripeForOneCard(null);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }


            creditCardRv.setVisibility((konnectCardModelArrayList.size() > 0) ? View.VISIBLE : GONE);

            KonnectCardModel selectedCard = spHelper.getKonnectOneCard();


            if (selectedCard != null) {
                if (!listHasSelectedCard(konnectCardModelArrayList, selectedCard)) {
                    spHelper.deletedKonnectCard();
                }
            }

            KonnectCardAdapter konnectCardAdapter = new KonnectCardAdapter(getActivity(), konnectCardModelArrayList, spHelper.getKonnectOneCard());
            creditCardRv.setAdapter(konnectCardAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean listHasSelectedCard(ArrayList<KonnectCardModel> konnectCardModelArrayList, KonnectCardModel selectedCard) {

        boolean hasCard = false;
        for (KonnectCardModel model : konnectCardModelArrayList) {
            if (model.getStripeCustomerId().equalsIgnoreCase(selectedCard.getStripeCustomerId())) {
                hasCard = true;
                break;
            }
        }

        return hasCard;
    }

    public void loadCreditCardsListForJudo() {
        try {
//            new Manager_Judo_GetAllCards(getContext(), getJsonForJudoCard(), listener_judo_getAllCards).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            ArrayList<CardJudoModel> arrayList = spHelper.getJudoCardList();
//            creditCardRv.setVisibility((arrayList.size() > 0) ? View.VISIBLE : GONE);

            boolean isFound = false;
            for (CardJudoModel model : arrayList) {
                if (model.isDefault()) {
                    isFound = true;
                    break;
                }
            }

            if (arrayList.size() > 0 && !isFound) {
                arrayList.get(0).setDefault(true);
            }

            judoCardsAdapter = new JudoCardsAdapter(getActivity(), arrayList);
            creditCardRv.setAdapter(judoCardsAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPaymentList() {
        paymentAdapter = new PaymentAdapter(getContext(), payments);
        paymentRv.setAdapter(paymentAdapter);
        paymentAdapter.notifyDataSetChanged();
    }

    private class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {
        private Context context;
        private ArrayList<Payment> arrayList;

        public PaymentAdapter(Context context, ArrayList<Payment> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.layout_adapter_item_payment, parent, false);

            return new MyViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Payment payment = arrayList.get(position);
            holder.paymentTextTv.setText(payment.getPaymentText());
            holder.gateWayTv.setText(payment.getGateWay());
            holder.paymentIconIv.setImageResource(payment.getPaymentIcon());

            if (payment.isSelected() && payment.getPaymentText().equalsIgnoreCase(p.getCreditCard())) {
                holder.selectRb.setChecked(true);

                holder.paymentIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.card_select));
                holder.viewBg.setBackgroundColor(ContextCompat.getColor(context, R.color.color_dark_gray_and_light_inverse));
                holder.addBtn.setText(p.getAddCard());
                holder.addBtn.setVisibility(View.VISIBLE);
//                holder.gateWayTv.setVisibility(View.VISIBLE);


            } else if (payment.isSelected() && payment.getPaymentText().equalsIgnoreCase(p.getAccount())) {
                holder.selectRb.setChecked(true);

                holder.viewBg.setBackgroundColor(ContextCompat.getColor(context, R.color.color_dark_gray_and_light_inverse));
                holder.addBtn.setVisibility(View.VISIBLE);
//                holder.gateWayTv.setVisibility(View.VISIBLE);

                if (sp.getBoolean(CommonVariables.ISMEMBERUSERLOGIN, false)) {
                    holder.addBtn.setText(p.getRemoveAccount());
                    holder.accname.setText(sp.getString(CommonVariables.MEMBERACCNAME, "-"));
                    holder.loggedinlyt.setVisibility(View.VISIBLE);
                } else {
                    holder.addBtn.setText(p.getAddAccount());
                    holder.loggedinlyt.setVisibility(GONE);
                }

            } else if (payment.isSelected() && payment.getPaymentText().equalsIgnoreCase(p.getCash())) {
                holder.selectRb.setChecked(true);
                holder.paymentIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.cash_select));
                holder.viewBg.setBackgroundColor(ContextCompat.getColor(context, R.color.color_dark_gray_and_light_inverse));
                holder.addBtn.setVisibility(GONE);
//                holder.gateWayTv.setVisibility(GONE);
                holder.loggedinlyt.setVisibility(GONE);

            } else if (payment.isSelected() && payment.getPaymentText().equalsIgnoreCase(p.getCardInCar())) {
                holder.selectRb.setChecked(true);
                holder.viewBg.setBackgroundColor(ContextCompat.getColor(context, R.color.color_dark_gray_and_light_inverse));
                holder.addBtn.setVisibility(GONE);
//                holder.gateWayTv.setVisibility(GONE);
                holder.loggedinlyt.setVisibility(GONE);

            } else {
                holder.selectRb.setChecked(false);
                holder.viewBg.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white_inverse));
                holder.addBtn.setVisibility(GONE);
//                holder.gateWayTv.setVisibility(GONE);
                holder.loggedinlyt.setVisibility(GONE);
            }

            holder.addBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.addBtn.getText().toString().equalsIgnoreCase(p.getRemoveAccount())) {
                        logoutAccount();
                    } else if (holder.addBtn.getText().toString().equalsIgnoreCase(p.getAddAccount())) {
                        addAccount();
                    } else if (holder.addBtn.getText().toString().equalsIgnoreCase(p.getAddCard())) {
                        addCreditCard();
                    }
                }
            });

            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefault(position);
                }
            });
        }

        private void addAccount() {
            Intent intent = new Intent(context, Activity_AccountMemberLogin.class);
            context.startActivity(intent);
            selectedType = "account";
        }

        /// New JUDO Work
        SweetAlertDialog mDialog;

        private void addCreditCard() {
            try {
                if (isKonnectPay) {
                    selectedType = "credit card";
                    new Manager_RegisterCardKonnectPay(settingsModel.getUserServerID(), appVersion, listener_Register_Card_KonnectPay).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else if (GateWay.contains(Config.JUDOPAY)) {
                    /// New Judo Pay
                    if (sp.getString("JudoId", "").equals("")) {
                        FBToast.infoToast(getContext(), "Judo Details not found", FBToast.LENGTH_SHORT);
                        return;
                    }
                    selectedType = "credit card";
                    new Manager_GetPaymentUrl(CommonMethods.getInstance().getPaymentGatway("" + R.string.app_name, sp, settingsModel), new Listener_GetPayment() {
                        @Override
                        public void onStart(boolean response) {
                            mDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                            mDialog.setTitleText("");
                            mDialog.setContentText(p.getPleaseWait());
                            mDialog.setCancelable(false);
                            mDialog.show();
                        }

                        @Override
                        public void onComplete(String response) {
                            try {
                                mDialog.dismissWithAnimation();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (response != null && !response.startsWith("false") && !response.equals("")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("HasError")) {
                                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("").setContentText(jsonObject.getString("Message")).setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }

                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                            }
                                        }).show();
                                    } else {
                                        String data = jsonObject.getString("Data");
                                        try {
                                            selectedType = "credit card";
                                            startActivityForResult(new Intent(getContext(), PaymentViewJudo3ds.class).putExtra("paymentUrl", data), JUDO_REGISTER_REQUEST);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else if (GateWay.contains(Config.Stripe)) {
                    if (sp.getString(Config.Stripe_PublishKey, "").equals("")) {
                        FBToast.warningToast(getContext(), "Stripe Details not found", FBToast.LENGTH_SHORT);
                        return;
                    }
                    Intent intent = new Intent(getActivity(), Activity_AddStripeCardDetail.class);
                    startActivityForResult(intent, 1010);

                }
                selectedType = "credit card";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void logoutAccount() {
            SweetAlertDialog mDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            mDialog.setContentText("Are You Sure You want to remove?")
                    .setTitleText("")
                    .showCancelButton(true)
                    .setConfirmText("Yes")
                    .setCancelText("No")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {

                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            edit.putString(CommonVariables.paymentType, "Cash");
                            edit.putBoolean(CommonVariables.ISMEMBERUSERLOGIN, false);
                            edit.commit();
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                        }
                    }).show();
        }

        public void setDefault(int pos) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (i == pos) {
                    if (arrayList.get(i).getPaymentText().equals(p.getCreditCard()) || arrayList.get(i).getPaymentText().equals(p.getAccount())) {
                        arrayList.get(i).setAddShown(true);
                    }
                    arrayList.get(i).setSelected(true);
                } else {
                    arrayList.get(i).setAddShown(false);
                    arrayList.get(i).setSelected(false);
                }
            }

            notifyDataSetChanged();

            if (arrayList.get(pos).getPaymentText().equalsIgnoreCase(p.getCash())) {
                creditCardRv.setVisibility(GONE);
                selectedType = p.getCash();

            } else if (arrayList.get(pos).getPaymentText().equalsIgnoreCase(p.getAccount())) {
                creditCardRv.setVisibility(GONE);
                selectedType = p.getAccount();

            } else if (arrayList.get(pos).getPaymentText().equalsIgnoreCase(p.getCardInCar())) {
                creditCardRv.setVisibility(GONE);
                selectedType = p.getCardInCar();

            } else {
                String g = arrayList.get(pos).getGateWay();

                if (isKonnectPay) {

                    selectedType = p.getCreditCard();
                    getCardsKonnect();
                } else if (arrayList.get(pos).getGateWay().contains("(JUDO)")) {
                    if (sp.getString("JudoId", "").equals("")) {
                        FBToast.infoToast(getContext(), "Judo Details not found", FBToast.LENGTH_SHORT);
                        return;
                    }
                    selectedType = p.getCreditCard();
                    new Manager_Judo_GetAllCards(getContext(), getJsonForJudoCard(), listener_judo_getAllCards).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else if (g.contains("(STRIPE)")) {
                    if (sp.getString(Config.Stripe_PublishKey, "").equals("")) {
                        FBToast.errorToast(getContext(), "Stripe Details not found", FBToast.LENGTH_SHORT);
                        return;
                    }
                    selectedType = p.getCreditCard();
                    new Manager_Stripe_GetAllCards(getContext(), true, spHelper.getStripeCustomerId(), listener_stripe_getAllCards).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView paymentTextTv;
            private TextView addBtn;
            private TextView accname;
            private TextView gateWayTv;
            private ImageView paymentIconIv;
            private RadioButton selectRb;
            private RelativeLayout viewBg;
            private RelativeLayout loggedinlyt;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                paymentTextTv = itemView.findViewById(R.id.paymentTextTv);
                addBtn = itemView.findViewById(R.id.addBtn);
                accname = itemView.findViewById(R.id.accname);
                gateWayTv = itemView.findViewById(R.id.gateWayTv);
                paymentIconIv = itemView.findViewById(R.id.paymentIconIv);
                selectRb = itemView.findViewById(R.id.selectRb);
                viewBg = itemView.findViewById(R.id.viewBg);
                loggedinlyt = itemView.findViewById(R.id.loggedinlyt);
            }
        }
    }

    private class JudoCardsAdapter extends RecyclerView.Adapter<JudoCardsAdapter.MyViewHolder> {
        int selectedIndex = 0;
        private Context context;
        private ArrayList<CardJudoModel> arrayList;

        private String msg = "";

        public JudoCardsAdapter(Context context, ArrayList<CardJudoModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.layout_adapter_item_card, parent, false);

            return new MyViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(@NonNull JudoCardsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            CardJudoModel c = arrayList.get(position);
            if (c != null) {
                if (c.isDefault()) {
                    selectedIndex = position;
                }
                holder.cardNoTv.setText("**** **** ****" + c.getLastFour());

          /*      if (c.getType() == WalletConstants.CardNetwork.VISA) {
                    holder.cardIv.setImageResource(R.drawable._visa);
                } else if (c.getType() == WalletConstants.CardNetwork.MASTERCARD) {
                    holder.cardIv.setImageResource(R.drawable._mastercard);
                } else if (c.getType() == WalletConstants.CardNetwork.AMEX) {
                    holder.cardIv.setImageResource(R.drawable._amex);
                } else if (c.getType() == WalletConstants.CardNetwork.JCB) {
                    holder.cardIv.setImageResource(R.drawable._jcb);
                }*/
//                else if (c.getType() == WalletConstants.CardNetwork.MAESTRO) {
//                    holder.cardIv.setImageResource(R.drawable._maestro);
//                } else if (c.getType() == WalletConstants.CardNetwork.SOLO) {
//                    holder.cardIv.setImageResource(R.drawable._solo);
//                } else if (c.getType() == WalletConstants.CardNetwork.SWITCH) {
//                    holder.cardIv.setImageResource(R.drawable._switch);
//                } else if (c.getType() == WalletConstants.CardNetwork.VISA_DEBIT) {
//                    holder.cardIv.setImageResource(R.drawable._visadebit);
//                } else if (c.getType() == WalletConstants.CardNetwork.VISA_ELECTRON) {
//                    holder.cardIv.setImageResource(R.drawable._visaelectron);
//                } else if (c.getType() == WalletConstants.CardNetwork.CHINA_UNION_PAY) {
//                    holder.cardIv.setImageResource(R.drawable._unionpay);
//                }
             /*   else {
                    holder.cardIv.setImageResource(R.drawable.ic_credit_card);
                }*/
                holder.cardIv.setImageResource(R.drawable.ic_credit_card);
                holder.expiresTv.setText("Expire " + c.getEndDate());

                if (c.isDefault()) {
                    holder.barLl.setVisibility(View.VISIBLE);
//                    holder.card_bg.setBackground(ContextCompat.getDrawable(context, R.drawable.select_card_bg));
                    holder.check_view.setVisibility(View.VISIBLE);
                } else {
                    holder.barLl.setVisibility(GONE);
//                    holder.card_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white_inverse));
                    holder.check_view.setVisibility(GONE);
                }

                // click
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardJudoModelFromAdapter = arrayList.get(position);
                        spHelper.saveCardJudoModel(cardJudoModelFromAdapter);
                        selectedIndexFromAdapter = position;
                        selectedIndex = position;
                        new Manager_Judo_AddCard(getContext(), 3, settingsModel.getEmail(), settingsModel.getPassword(), arrayList.get(position), judoCardList, listener_judo_addCard).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        new SaveCardReciept(3).execute(arrayList.get(position));
                    }
                });

                holder.deleteCardIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(p.getAreYouSure())
                                .setContentText("You want to remove card details?")
                                .setCancelText(p.getNo())
                                .setConfirmText("Delete")
                                .showCancelButton(true)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }

                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                    }
                                })
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        new Manager_Judo_AddCard(getContext(), 2, settingsModel.getEmail(), settingsModel.getPassword(), arrayList.get(position), judoCardList, listener_judo_addCard).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        // new SaveCardReciept(2).execute(arrayList.get(position));
                                    }

                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                                    }
                                })
                                .show();
                    }
                });
            }
        }

        public void setDefault(int pos) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (i == pos) {
                    arrayList.get(i).setDefault(true);
                } else {
                    arrayList.get(i).setDefault(false);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView cardNoTv, expiresTv;
            private ImageView check_view, deleteCardIv, cardIv;
            private LinearLayout barLl;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                cardNoTv = itemView.findViewById(R.id.cardNoTv);
                expiresTv = itemView.findViewById(R.id.expiresTv);
                check_view = itemView.findViewById(R.id.check_view);
                deleteCardIv = itemView.findViewById(R.id.deleteCardIv);
                cardIv = itemView.findViewById(R.id.cardIv);
                barLl = itemView.findViewById(R.id.barLl);
            }
        }
    }

    public class StripeCardAdapter extends RecyclerView.Adapter<StripeCardAdapter.MyViewHolder> {
        int selectedIndex = 0;
        private Context context;
        private ArrayList<StripeCardModel> arrayList;

        public StripeCardAdapter(Context context, ArrayList<StripeCardModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public StripeCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.layout_adapter_item_card, parent, false);
            return new StripeCardAdapter.MyViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(@NonNull StripeCardAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            StripeCardModel c = arrayList.get(position);
            holder.cardNoTv.setText("**** **** ****" + c.getLast4());

            if (c.isDefault()) {
                selectedIndex = position;
            }

 /*           if (c.getType() == WalletConstants.CardNetwork.VISA) {
                holder.cardIv.setImageResource(R.drawable._visa);
            } else if (c.getType() == WalletConstants.CardNetwork.MASTERCARD) {
                holder.cardIv.setImageResource(R.drawable._mastercard);
            } else if (c.getType() == WalletConstants.CardNetwork.AMEX) {
                holder.cardIv.setImageResource(R.drawable._amex);
            } else if (c.getType() == WalletConstants.CardNetwork.JCB) {
                holder.cardIv.setImageResource(R.drawable._jcb);
            }*/
//                else if (c.getType() == WalletConstants.CardNetwork.MAESTRO) {
//                    holder.cardIv.setImageResource(R.drawable._maestro);
//                } else if (c.getType() == WalletConstants.CardNetwork.SOLO) {
//                    holder.cardIv.setImageResource(R.drawable._solo);
//                } else if (c.getType() == WalletConstants.CardNetwork.SWITCH) {
//                    holder.cardIv.setImageResource(R.drawable._switch);
//                } else if (c.getType() == WalletConstants.CardNetwork.VISA_DEBIT) {
//                    holder.cardIv.setImageResource(R.drawable._visadebit);
//                } else if (c.getType() == WalletConstants.CardNetwork.VISA_ELECTRON) {
//                    holder.cardIv.setImageResource(R.drawable._visaelectron);
//                } else if (c.getType() == WalletConstants.CardNetwork.CHINA_UNION_PAY) {
//                    holder.cardIv.setImageResource(R.drawable._unionpay);
//                }
            else {
                holder.cardIv.setImageResource(R.drawable.ic_credit_card);
            }
            holder.cardIv.setImageResource(R.drawable.ic_credit_card);
            if (selectedIndex == position) {
                holder.barLl.setVisibility(View.VISIBLE);
                holder.check_view.setVisibility(View.VISIBLE);
            } else {
                holder.barLl.setVisibility(View.INVISIBLE);
                holder.check_view.setVisibility(View.INVISIBLE);
            }

            holder.expiresTv.setText("Expire " + c.getExp_month() + ", " + c.getExp_year());

            holder.deleteCardIv.setOnClickListener(view -> new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Delete Card Alert!")
                    .setContentText("Do you want to delete card?")
                    .setCancelText("Cancel")
                    .setConfirmText("Done")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            new Manager_Stripe_DeleteCard(context, arrayList.get(position).getId(), listener_stripe_deleteCard).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                    new DeleteCardStripe(arrayList.get(position).getId());
                        }

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                            sweetAlertDialog.cancel();
                        }
                    })
                    .show());

            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedIndex = position;
                    selectedIndexFromAdapter = position;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView cardNoTv, expiresTv;
            //            private RelativeLayout card_bg;
            private ImageView check_view, deleteCardIv, cardIv;
            private LinearLayout barLl;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                cardNoTv = itemView.findViewById(R.id.cardNoTv);
                expiresTv = itemView.findViewById(R.id.expiresTv);
                barLl = itemView.findViewById(R.id.barLl);
                check_view = itemView.findViewById(R.id.check_view);
                deleteCardIv = itemView.findViewById(R.id.deleteCardIv);
                cardIv = itemView.findViewById(R.id.cardIv);
            }
        }
    }


    public class KonnectCardAdapter extends RecyclerView.Adapter<KonnectCardAdapter.MyViewHolder> {
        int selectedIndex = 0;
        private Context context;
        private ArrayList<KonnectCardModel> arrayList;

        private KonnectCardModel selectedCard = null;


        public KonnectCardAdapter(Context context, ArrayList<KonnectCardModel> arrayList, KonnectCardModel selectedCard) {
            this.context = context;
            this.arrayList = arrayList;
            this.selectedCard = selectedCard;
        }

        @NonNull
        @Override
        public KonnectCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.layout_adapter_item_card, parent, false);
            return new KonnectCardAdapter.MyViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(@NonNull KonnectCardAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            KonnectCardModel card = arrayList.get(position);
            holder.cardNoTv.setText("**** **** ****" + card.getLastFour());

            try {
                if (card.getBrand() != null) {
                    if (card.getBrand().contains("mastercard")) {
                        holder.cardIv.setImageResource(R.drawable.mastercard);
                    }
                    else if (card.getBrand().contains("visa")) {
                        holder.cardIv.setImageResource(R.drawable._visa);
                    }
                    else if (card.getBrand().contains("amex")) {
                        holder.cardIv.setImageResource(R.drawable.amircan_exp);
                    }
                    else if (card.getBrand().contains("discover")) {
                        holder.cardIv.setImageResource(R.drawable.discover);
                    }
                    else{
                        holder.cardIv.setImageResource(R.drawable.ic_credit_card);
                    }

                } else {
                    holder.cardIv.setImageResource(R.drawable.ic_credit_card);
                }
            } catch (Exception ex) {
            }
            try {

                holder.expiresTv.setText("Expire " + card.getExpMonth() + ", " + card.getExpYear());
            }catch (Exception ex){
                ex.printStackTrace();
            }


            if (selectedCard != null) {
                if (card.getCardToken().equalsIgnoreCase(selectedCard.getCardToken())) {
                    holder.barLl.setVisibility(View.VISIBLE);
                    holder.check_view.setVisibility(View.VISIBLE);
                } else {
                    holder.barLl.setVisibility(View.INVISIBLE);
                    holder.check_view.setVisibility(View.INVISIBLE);
                }
            } else {
                if (selectedIndex == position) {
                    holder.barLl.setVisibility(View.VISIBLE);
                    holder.check_view.setVisibility(View.VISIBLE);
                } else {
                    holder.barLl.setVisibility(View.INVISIBLE);
                    holder.check_view.setVisibility(View.INVISIBLE);
                }
            }


            //   holder.expiresTv.setText("Expire " + card.getExp_month() + ", " + card.getExp_year());

            holder.deleteCardIv.setOnClickListener(view -> new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Delete Card Alert!")
                    .setContentText("Do you want to delete card?")
                    .setCancelText("Cancel")
                    .setConfirmText("Done")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            new Manager_DeleteCustomerCardsKonnect(context, settingsModel.getUserServerID(), card.getCardToken(), listener_DeleteCard_Konnect).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
                        }

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog, String InputText) {
                            sweetAlertDialog.cancel();
                        }
                    })
                    .show());

            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedCard = null;
                    selectedIndex = position;
                    selectedIndexFromAdapter = position;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView cardNoTv, expiresTv;
            //            private RelativeLayout card_bg;
            private ImageView check_view, deleteCardIv, cardIv;
            private LinearLayout barLl;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                cardNoTv = itemView.findViewById(R.id.cardNoTv);
                expiresTv = itemView.findViewById(R.id.expiresTv);
                barLl = itemView.findViewById(R.id.barLl);
                check_view = itemView.findViewById(R.id.check_view);
                deleteCardIv = itemView.findViewById(R.id.deleteCardIv);
                cardIv = itemView.findViewById(R.id.cardIv);
            }
        }
    }


    private String getJsonForJudoCard() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("PhoneNo", "");
        map.put("UserName", "");
        map.put("Passwrd", settingsModel.getPassword());
        map.put("defaultClientId", CommonVariables.clientid);
        map.put("uniqueValue", CommonVariables.clientid + "4321orue");
        map.put("Email", settingsModel.getEmail().trim());
        map.put("TokenDetails", "");
        map.put("PickDetails", "yes");
        Gson gson = new Gson();

        String jsonString = gson.toJson(map);
        return jsonString;
    }
}