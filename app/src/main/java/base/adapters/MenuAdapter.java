package base.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import base.models.MenuModel;

import com.eurosoft.customerapp.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MenuModel> arrayList;

    public MenuAdapter(Context context, ArrayList<MenuModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

//    public View getView(int position, View view, ViewGroup parent) {
////        View rowView = null;
////        if (view != null) {
////            rowView = view;
////        } else {
////            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////            rowView = inflater.inflate(R.layout.layout_adapter_item_menu, parent, false);
////        }
////        if (position == model.length - 1) {
////            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////            lp.setMargins(0, 200, 0, 0);
////            rowView.setLayoutParams(lp);
////        }
//
////        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
////        TextView badge = (TextView) rowView.findViewById(R.id.badge);
////        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
//
////        ArrayList<PromoModel> promoModels = new DatabaseOperations(new DatabaseHelper(getContext())).getPromoDetails();
////        if (getItem(position).itemname.toLowerCase().contains("promotion")) {
////            if (promoModels.size() > 0) {
////                badge.setText("" + promoModels.size());
////                badge.setVisibility(View.VISIBLE);
////            } else {
////                badge.setVisibility(View.GONE);
////            }
////        }
//
//
////        txtTitle.setText(getItem(position).itemname);
////        imageView.setImageResource(getItem(position).imgid);
//        return rowView;
//
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_item_menu, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder h, int position) {
        MenuModel model = arrayList.get(h.getAdapterPosition());

        try {
            h.menuTv.setText("" + model.getName());
            h.menuIv.setImageResource(model.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        ArrayList<PromoModel> promoModels = new DatabaseOperations(new DatabaseHelper(context)).getPromoDetails();
////        if (model.getName().toLowerCase().contains("promotion")) {
////            if (promoModels.size() > 0) {
////                h.badge.setText("" + promoModels.size());
////                h.badge.setVisibility(View.VISIBLE);
////            } else {
////                h.badge.setVisibility(View.GONE);
////            }
////        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView badge;
        public TextView menuTv;
        public ImageView menuIv;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            menuTv = itemView.findViewById(R.id.menuTv);
            menuIv = itemView.findViewById(R.id.menuIv);
            badge = itemView.findViewById(R.id.badge);
        }
    }
}