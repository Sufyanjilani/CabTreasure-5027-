package base.listener;

import java.util.ArrayList;

import base.models.LocAndField;

public interface Listener_SendAddressArrayList {
    void onPreExecute(boolean start);

    void setList(ArrayList<LocAndField> arrayList);
}
