package base.listener;

import java.util.ArrayList;

import base.models.Model_BookingDetailsModel;

public interface Listener_UpdateBookingList {
    public abstract void updateList( ArrayList<Model_BookingDetailsModel> models);
}
