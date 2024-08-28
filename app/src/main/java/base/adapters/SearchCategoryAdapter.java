package base.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eurosoft.customerapp.R;


import java.util.ArrayList;

import base.models.MenuModel;
import base.models.SearchCategoryModel;


public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.SearchCategorViewHolder> {

    private final SearchItemClickListener clickListener;

    int  selectedPosition = 0;
    private ArrayList<SearchCategoryModel> arrayList;

    public SearchCategoryAdapter(ArrayList<SearchCategoryModel> arrayList,SearchItemClickListener listener) {

        this.arrayList = arrayList;
        this.clickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDefault(){
        selectedPosition = 0;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public SearchCategorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_category_item, parent, false);
        return new SearchCategorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCategorViewHolder h, int position) {
        SearchCategoryModel model = arrayList.get(position);

        h.bind(model);



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SearchCategorViewHolder extends RecyclerView.ViewHolder {
        public CardView cvRoot;
        public TextView tvTitle;
        public ImageView ivIcon;


        public SearchCategorViewHolder(@NonNull View itemView) {
            super(itemView);
            cvRoot = itemView.findViewById(R.id.cvRoot);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivIcon = itemView.findViewById(R.id.ivIcon);

        }

        public void bind(SearchCategoryModel model) {
            tvTitle.setText(model.getTitle());

            if (getAbsoluteAdapterPosition() == selectedPosition) {
                cvRoot.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.app_color_primary_white));
                ivIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.color_white_inverse), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                cvRoot.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.color_gray_and_white_inverse));
                ivIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.color_black_inverse
                ), android.graphics.PorterDuff.Mode.SRC_IN);

            }

            try {
                ivIcon.setImageDrawable(itemView.getContext().getResources().getDrawable(model.getIcon()));
            } catch (Exception ex) {

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View v) {

                    if(getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION) {
                        selectedPosition = getAbsoluteAdapterPosition();
                        notifyDataSetChanged();
                        clickListener.onItemClick(getAbsoluteAdapterPosition(),model);
                    }

                }
            });
        }
    }


    public interface SearchItemClickListener {
        void onItemClick(int position, SearchCategoryModel model);

    }
}