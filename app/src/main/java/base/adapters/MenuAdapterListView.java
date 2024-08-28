package base.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eurosoft.customerapp.R;

import java.util.ArrayList;

import base.databaseoperations.DatabaseHelper;
import base.databaseoperations.DatabaseOperations;
import base.models.PromoModel;
import base.models.MenuModel;

public class MenuAdapterListView extends ArrayAdapter<MenuModel> {
    ArrayList<MenuModel> menuModelArrayList;

    public MenuAdapterListView(@NonNull Context context, ArrayList<MenuModel> menuModelArrayList) {
        super(context, R.layout.layout_adapter_item_menu, menuModelArrayList);
        this.menuModelArrayList = menuModelArrayList;
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = null;

        if (view != null) {
            rowView = view;

        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.layout_adapter_item_menu, parent, false);
        }

        if (position == menuModelArrayList.size() - 1) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 200, 0, 0);
            rowView.setLayoutParams(lp);
        }

        TextView txtTitle = (TextView) rowView.findViewById(R.id.menuTv);
        TextView badge = (TextView) rowView.findViewById(R.id.badge);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        ArrayList<PromoModel> promoModels = new DatabaseOperations(new DatabaseHelper(getContext())).getPromoDetails();
        if (getItem(position).getName().toLowerCase().contains("promotion")) {
            if (promoModels.size() > 0) {
                badge.setText("" + promoModels.size());
                badge.setVisibility(View.VISIBLE);
            } else {
                badge.setVisibility(View.GONE);
            }
        }

        txtTitle.setText(getItem(position).getName());
        imageView.setImageResource(getItem(position).getImage());

        return rowView;
    }
}
