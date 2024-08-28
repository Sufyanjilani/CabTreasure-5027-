package base.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eurosoft.customerapp.R;

import base.miscutilities.AddressModel;

public class ViaListViewAdapter extends ArrayAdapter<AddressModel> implements OnClickListener {

    public ViaListViewAdapter(Context context, int resource) {
        super(context, resource);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ret = inflater.inflate(R.layout.via_list_layout, parent, false);
        ((TextView) ret.findViewById(R.id.txtAddressVia)).setText(getAddressAsString(getItem(position).address));
        ret.findViewById(R.id.btnMinus).setTag(position);
        ret.findViewById(R.id.btnMinus).setOnClickListener(this);
        return ret;
    }
//	get

    public static String getAddressAsString(Address address) {
        StringBuilder addressBuilder = new StringBuilder();

        int lines = address.getMaxAddressLineIndex();
        if (lines != -1) {
            for (int i = 0; i <= lines; i++) {
                if (i != lines)
                    addressBuilder.append(address.getAddressLine(i)).append(", ");
                else
                    addressBuilder.append(address.getAddressLine(i));
            }
        }
        return addressBuilder.toString();
    }

    public void addAddress(AddressModel address) {
        add(address);
        if (mNotifier != null)
            mNotifier.onListChanged(0001);
    }

    @Override
    public void onClick(View v) {
        remove(getItem((Integer) v.getTag()));
        if (mNotifier != null)
            mNotifier.onListChanged((Integer) v.getTag());
    }

    public OnListChangeNotifier mNotifier;

    public void setNotifier(OnListChangeNotifier notifier) {
        mNotifier = notifier;
    }

    public interface OnListChangeNotifier {
        public abstract void onListChanged(int post);
    }
}
