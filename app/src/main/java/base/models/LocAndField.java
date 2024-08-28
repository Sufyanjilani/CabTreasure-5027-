package base.models;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;


public class LocAndField implements Parcelable {

    private String Lat, Lon;
    private String Field;
    private boolean isSelected;
    private String DoorNo;
    private String SubAddress;
    private String addressType;
    public String edittextSetHint;
    public String edittextSetText;
    public String fromTo;
    public String locationType="";
    public String dateTime;
    private String isProgressShowing;
    private String flightNo;
    private String fromComing;
    private String postCode;
    private String placeId;
    private boolean hasFocus;

    public boolean isHasFocus() {
        return hasFocus;
    }

    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public static final Creator<LocAndField> CREATOR = new Creator<LocAndField>() {
        @Override
        public LocAndField createFromParcel(Parcel in) {
            return new LocAndField(in);
        }

        @Override
        public LocAndField[] newArray(int size) {
            return new LocAndField[size];
        }
    };

    protected LocAndField(Parcel in) {
        Lat = in.readString();
        Lon = in.readString();
        Field = in.readString();
        DoorNo = in.readString();
        SubAddress = in.readString();
        addressType = in.readString();
        edittextSetHint = in.readString();
        edittextSetText = in.readString();
        fromTo = in.readString();
        locationType = in.readString();
        dateTime = in.readString();
        flightNo = in.readString();
        fromComing = in.readString();
        isProgressShowing = in.readString();
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getFromTo() {
        return fromTo;
    }

    public void setFromTo(String fromTo) {
        this.fromTo = fromTo;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public LocAndField() {
        Lat = Lon = Field = DoorNo = "";
    }

    public void setDoorNo(String arg) {
        this.DoorNo = arg;
    }

    public String getSubAddress() {
        return SubAddress;
    }

    public void setSubAddress(String arg) {
        this.SubAddress = arg;
    }

    public String getDoorNo() {
        return DoorNo;
    }

    public void setLat(String arg) {
        this.Lat = arg;
    }

    public String getLat() {
        return Lat;
    }

    public void setLon(String arg) {
        this.Lon = arg;
    }

    public String getLon() {
        return Lon;
    }

    public void setField(String arg) {
        this.Field = arg;
    }

    public String getField() {
        return Field;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String isProgressShowing() {
        return isProgressShowing;
    }

    public void setProgressShowing(String progressShowing) {
        isProgressShowing = progressShowing;
    }

    public String getEdittextSetHint() {
        return edittextSetHint;
    }

    public void setEdittextSetHint(String edittextSetHint) {
        this.edittextSetHint = edittextSetHint;
    }

    public String getEdittextSetText() {
        return edittextSetText;
    }

    public void setEdittextSetText(String edittextSetText) {
        this.edittextSetText = edittextSetText;
    }

    public String getIsProgressShowing() {
        return isProgressShowing;
    }

    public void setIsProgressShowing(String isProgressShowing) {
        this.isProgressShowing = isProgressShowing;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getFromComing() {
        return fromComing;
    }

    public void setFromComing(String fromComing) {
        this.fromComing = fromComing;
    }

    public static Creator<LocAndField> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Lat);
        parcel.writeString(Lon);
        parcel.writeString(Field);
        parcel.writeString(DoorNo);
        parcel.writeString(SubAddress);
        parcel.writeString(addressType);
        parcel.writeString(edittextSetHint);
        parcel.writeString(edittextSetText);
        parcel.writeString(fromTo);
        parcel.writeString(locationType);
        parcel.writeString(dateTime);
        parcel.writeString(flightNo);
        parcel.writeString(fromComing);
        parcel.writeString(isProgressShowing);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
