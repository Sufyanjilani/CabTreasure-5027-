package base.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static base.utils.CommonVariables.CurrencySymbol;
import static base.utils.Config.ShowFares;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eurosoft.customerapp.BuildConfig;
import com.eurosoft.customerapp.R;
import com.tfb.fbtoast.FBToast;

import java.util.ArrayList;

import base.models.ExtrasModel;
import base.models.SettingsModel;
import base.utils.CommonMethods;
import base.utils.SharedPrefrenceHelper;
import base.models.ParentPojo;

public class Activity_AddExtras extends AppCompatActivity {
    SettingsModel settingsModel;
    RecyclerView recyclerView;
    EditText nottxt;
    TextView totalExtras, journeyTotalTv, grandTotal;
    CardView btnPromo;
    TextView doneBtn;
    double journeyFare;
    public ArrayList<ExtrasModel> extrasList = null;
    public static int sizeOfExtrasList = 0;
    ArrayList<ExtrasModel> extrasModel;
    ArrayList<ExtrasModel> tempList = new ArrayList<>();
    ExtrasAdapter extrasAdapter;
    TextView promotitle;
    TextView extrasJourneyChargesLabel;
    TextView extrasExtraChargesLabel;
    private SharedPreferences sp;
    ParentPojo p = new ParentPojo();
    LinearLayout faresLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonMethods.getInstance().setDarkAndNightColorBlackWhite(Activity_AddExtras.this);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.layout_add_extras);


        faresLl = findViewById(R.id.faresLl);
        faresLl.setVisibility((sp.getString(ShowFares, "1").equals("0")) ? GONE : VISIBLE);

        recyclerView = findViewById(R.id.extraList);
        btnPromo = findViewById(R.id.btnPromo);

        promotitle = findViewById(R.id.promotitle);
        promotitle.setText(p.getExtras().toUpperCase());

        doneBtn = findViewById(R.id.doneBtn);
        doneBtn.setText(p.getDone());

        totalExtras = findViewById(R.id.totalExtras);
        journeyTotalTv = findViewById(R.id.journeyTotalTv);
        grandTotal = findViewById(R.id.grandtotal);
        nottxt = findViewById(R.id.not_txt);
        nottxt.setHint(p.getEnterDriverNotes());

        extrasJourneyChargesLabel = findViewById(R.id.extrasJourneyChargesLabel);
        extrasJourneyChargesLabel.setText(p.getJourneyCharges());

        extrasExtraChargesLabel = findViewById(R.id.extrasExtraChargesLabel);
        extrasExtraChargesLabel.setText(p.getExtraCharges());

        try {
            View view = getCurrentFocus();
            if (view != null) {
                nottxt.clearFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        } catch (Exception e) {
        }

        extrasList = new SharedPrefrenceHelper(this).getExtraList();
        extrasModel = new ArrayList<>();

        extrasModel.addAll(extrasList);

        try {
            String notes = getIntent().getStringExtra("drvNotes");
            float tt = 0;
            for (int i = 0; i < extrasList.size(); i++) {
                ExtrasModel model = extrasList.get(i);
                if (model.Price != null) {
                    tt += model.Price;
                }
            }

            journeyFare = getIntent().getDoubleExtra("journeyCharges", 0);

            if (notes != null && !notes.equals("")) {
                nottxt.setText(notes);
            }

            extrasAdapter = new ExtrasAdapter(extrasList);
            recyclerView.setAdapter(extrasAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            settingsModel = new SharedPrefrenceHelper(Activity_AddExtras.this).getSettingModel();

            findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ((HomeFragment) Fragment_Main.mainContext.getSupportFragmentManager().findFragmentByTag("Current")).extrasList = tempList;

//                    extrasModel.clear();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
            btnPromo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    double grandTotalValue = Double.parseDouble(grandTotal.getText().toString().replace(sp.getString(CurrencySymbol, "\u00A3"), ""));
                    setResult(RESULT_OK, new Intent()
                            .putExtra("drvNotes", nottxt.getText().toString())
                            .putExtra("extraTotal", totalPrice))
                    ;
                    new SharedPrefrenceHelper(Activity_AddExtras.this).putExtraList(extrasAdapter.getList());
                    finish();
                }
            });
            journeyTotalTv.setText("\u00A3" + String.format("%.2f", (journeyFare - tt)));
            journeyFare = journeyFare - tt;
//            if ((journeyFare - tt) == 0) {
//                journeyTotalTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", journeyFare));
//            } else {
//                journeyTotalTv.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", journeyFare - tt));
//                journeyFare = journeyFare - tt;
//            }
            tt = 0;

            double ex = 0;
            for (int i = 0; i < extrasList.size(); i++) {
                ExtrasModel model = extrasList.get(i);
                if (model.Price != null) {
                    ex += model.Price;
                }
            }
            double journey = Double.parseDouble(journeyTotalTv.getText().toString().replace(sp.getString(CurrencySymbol, "\u00A3"), "").replace(" ", ""));

            grandTotal.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", (journey + ex)));
            ex = 0;

        } catch (Exception e) {
        }
    }


    double totalPrice = 0;
    boolean isFirstRun = true;

    private class ExtrasAdapter extends RecyclerView.Adapter<ExtrasAdapter.ExtrasViewHolder> {
        ArrayList<ExtrasModel> extrasModel;

        ExtrasAdapter(ArrayList<ExtrasModel> extrasModel) {
            this.extrasModel = extrasModel;
//            settingsModel=spHelper.getSettingModel();
        }

        public ArrayList<ExtrasModel> getList() {
            return extrasModel;
        }

        @NonNull
        @Override
        public ExtrasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            LayoutInflater layoutInflater = LayoutInflater.from(Activity_AddExtras.this);
            View view = layoutInflater.inflate(R.layout.extras_child, viewGroup, false);
            ExtrasViewHolder extrasViewHolder = new ExtrasViewHolder(view);
            return extrasViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ExtrasAdapter.ExtrasViewHolder viewHolder, final int i) {
            try {
                if (isFirstRun) {
                    if (tempList.size() == extrasList.size()) {
                        tempList.clear();
                    }
                    tempList.add(extrasModel.get(i));
                }
                if (extrasModel.get(i).Qty > 0) {
                    float total = extrasModel.get(i).ChargesPerQty * extrasModel.get(i).Qty;
                    extrasModel.get(i).Price = total;
                    viewHolder.checkBox_.setChecked(true);
                    if (sp.getString(ShowFares, "1").equals("0")) {
                        viewHolder.title.setText(extrasModel.get(i).Name);
                        viewHolder.checkBox_.setText(extrasModel.get(i).Name);
                    } else {
                        viewHolder.title.setText(extrasModel.get(i).Name + "(" + sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", total) + ")");
                        viewHolder.checkBox_.setText(extrasModel.get(i).Name + "(" + sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", total) + ")");
                    }
                } else {
                    extrasModel.get(i).Price = 0f;
                    viewHolder.title.setText(extrasModel.get(i).Name);
                    viewHolder.checkBox_.setText(extrasModel.get(i).Name);
                    viewHolder.checkBox_.setChecked(false);
                }

                viewHolder._count.setText("" + extrasModel.get(i).Qty);

                viewHolder.minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (extrasModel.get(i).Qty > 0) {
                            extrasModel.get(i).Qty -= 1;
                            totalPrice = 0f;
                            isFirstRun = false;
                            notifyDataSetChanged();
                        }
                    }
                });

                viewHolder.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (extrasModel.get(i).Qty < extrasModel.get(i).MaxQty) {
                            extrasModel.get(i).Qty += 1;
                            totalPrice = 0f;
                            isFirstRun = false;
                            notifyDataSetChanged();
                        } else {
                            FBToast.errorToast(Activity_AddExtras.this, "Maximum Limit Exceeded!", FBToast.LENGTH_SHORT);
                        }
                    }
                });

                if (extrasModel.get(i).AttributeType != null && extrasModel.get(i).AttributeType.toLowerCase().equals("checkbox")) {
                    viewHolder.controll_lyt.setVisibility(View.GONE);
                    viewHolder.checkBox_.setVisibility(View.VISIBLE);
                    viewHolder.title.setVisibility(View.GONE);
                } else {
                    viewHolder.controll_lyt.setVisibility(View.VISIBLE);
                    viewHolder.checkBox_.setVisibility(View.GONE);
                    viewHolder.title.setVisibility(View.VISIBLE);
                }

                viewHolder.checkBox_.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (extrasModel.get(i).AttributeType != null && extrasModel.get(i).AttributeType.toLowerCase().equals("checkbox")) {
                            if (b) {
                                extrasModel.get(i).Qty = 1;
                                totalPrice = 0f;
                                notifyDataSetChanged();
                            } else {
                                extrasModel.get(i).Qty = 0;
                                totalPrice = 0f;
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
                totalPrice += extrasModel.get(i).Price;

                if (totalPrice > 0) {
                    totalExtras.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", totalPrice));
                } else {
                    totalExtras.setText(sp.getString(CurrencySymbol, "\u00A3") + "0.00");
                }

                double gTotal = journeyFare + totalPrice;
                grandTotal.setText(sp.getString(CurrencySymbol, "\u00A3") + String.format("%.2f", gTotal));

            } catch (Exception ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }
        }

        @Override
        public int getItemCount() {
            return extrasModel.size();
        }

        class ExtrasViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkBox_;
            LinearLayout controll_lyt;
            TextView title, minus, _count, plus;

            public ExtrasViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.labelhead);
                minus = itemView.findViewById(R.id.minus_adult);
                _count = itemView.findViewById(R.id.adult_count);
                plus = itemView.findViewById(R.id.plus_adult);
                checkBox_ = itemView.findViewById(R.id.checkbox_);
                controll_lyt = itemView.findViewById(R.id.controllLyt);
            }
        }
    }

}
