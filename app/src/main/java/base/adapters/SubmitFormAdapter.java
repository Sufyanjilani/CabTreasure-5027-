package base.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eurosoft.customerapp.R;

import java.util.ArrayList;

import base.listener.Listener_MyComplete;
import base.models.Model_FindLost;
import base.models.Model_SubmitForm;

public class SubmitFormAdapter extends RecyclerView.Adapter<SubmitFormAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Model_SubmitForm> arrayList;
    private ArrayList<Integer> selectedList = new ArrayList<>();
    private Listener_MyComplete listener_myComplete;

    public SubmitFormAdapter(Context context, ArrayList<Model_SubmitForm> arrayList, Listener_MyComplete listener_myComplete) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener_myComplete = listener_myComplete;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_item_submit_form, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder h, int position) {
        Model_SubmitForm m = arrayList.get(position);

        if (m.getFrom().equals("ComplainList")) {
            h.curverBgRl.setVisibility(View.GONE);
            h.switchS.setVisibility(View.GONE);
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener_myComplete != null) {
                        listener_myComplete.onComplete(m.getText());
                    }
                }
            });

        } else {
            h.curverBgRl.setVisibility(View.VISIBLE);
            h.switchS.setChecked(m.isLost() ? true : false);
            h.switchS.setVisibility(View.VISIBLE);
            h.switchS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        arrayList.get(position).setLost(true);
                        selectedList.add(position);
                    } else {
                        arrayList.get(position).setLost(false);
                        selectedList.remove((Integer) position);
                    }
                }
            });
        }

        h.numberTv.setText("" + (position + 1));
        h.complainTv.setText(m.getText());
    }

    public ArrayList<Integer> getDataList() {
        return selectedList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView numberTv;
        private TextView complainTv;
        private Switch switchS;
        private RelativeLayout curverBgRl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTv = itemView.findViewById(R.id.numberTv);
            complainTv = itemView.findViewById(R.id.complainTv);
            switchS = itemView.findViewById(R.id.switchS);
            curverBgRl = itemView.findViewById(R.id.curverBgRl);
        }
    }
}
