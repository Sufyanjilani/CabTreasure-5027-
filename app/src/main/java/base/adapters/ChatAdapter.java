package base.adapters;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eurosoft.customerapp.R;

import java.util.ArrayList;

import base.utils.SharedPrefrenceHelper;
import base.models.ChatModel;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ChatModel> arrayList;
    private SharedPrefrenceHelper helper;

    public ChatAdapter(Context context, ArrayList<ChatModel> arrayList, SharedPrefrenceHelper helper) {
        this.context = context;
        this.arrayList = arrayList;
        this.helper = helper;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_item_chat, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder h, int position) {
        ChatModel m = arrayList.get(position);

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) h.mainRl.getLayoutParams();

        // 1 = Unread
        // 2 = Read
        if (m.getMessageStatus() == 1) {
            h.checkStatusIv.setColorFilter(ContextCompat.getColor(context, R.color.gray_btn_bg_pressed_color), android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (m.getMessageStatus() == 2) {
            h.checkStatusIv.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            h.checkStatusIv.setColorFilter(ContextCompat.getColor(context, R.color.gray_btn_bg_pressed_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        if (m.getFrom().trim().equalsIgnoreCase(helper.getSettingModel().getName().trim())) {
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            h.mainRl.setGravity(Gravity.RIGHT);
            h.checkStatusIv.setVisibility(View.VISIBLE);

        } else {
//            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            h.mainRl.setGravity(Gravity.LEFT);
            h.checkStatusIv.setVisibility(View.GONE);
        }

        h.textTv.setText(m.getText());
        h.dateTv.setText(m.getDate());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textTv;
        private TextView dateTv;
        private ImageView checkStatusIv;
        private RelativeLayout mainRl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textTv = itemView.findViewById(R.id.textTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            checkStatusIv = itemView.findViewById(R.id.checkStatusIv);
            mainRl = itemView.findViewById(R.id.mainRl);
        }
    }
}
