package base.miscutilities;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import base.adapters.ViaListViewAdapter;

public class AddressModel implements Parcelable {
	public static final String TYPE_ADDRESS = "Address";
	public static final String TYPE_AIRPORT = "Airport";
	public static final String TYPE_STATION = "Station";
	public static final String TYPE_FAVOURITES = "Favourites";

	public Address address;
	public String door = "";
	public String flight = "";
	public String type = "";
	public String fromComing = "";

	public AddressModel(Address address, String type) {
		this(address, "", "", type);
	}

	public AddressModel(Address address, String door, String flight, String type) {
		this.address = address;
		this.door = door;
		this.flight = flight;
		this.type = type;
	}

	public AddressModel(Parcel source) {
		this.address = source.readParcelable(null);
		this.door = source.readString();
		this.flight = source.readString();
		this.type = source.readString();
		this.fromComing = source.readString();
	}

	/**
	 * Implementation for address retrieval
	 */
	@Override
	public String toString() {
		String ret = "";
		if (!door.isEmpty())
			ret = door + " " + ViaListViewAdapter.getAddressAsString(address);
		else
			ret = ViaListViewAdapter.getAddressAsString(address);
		if (address.getSubThoroughfare() != null) {
			ret = ret.replace(address.getSubThoroughfare(), "");
		}
		// Replace any double spaces..
		ret = ret.replace("  ", " ");
		return ret;
	}

	public String toString(boolean withDoorNo) {
		if (withDoorNo)
			return this.toString();
		else {
			String ret = "";
			ret = ViaListViewAdapter.getAddressAsString(address);
			if (address.getSubThoroughfare() != null) {
				ret = ret.replace(address.getSubThoroughfare(), "");
			}
			// Replace any double spaces..
			ret = ret.replace("  ", " ");
			return ret;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(address, 0);
		dest.writeString(door);
		dest.writeString(flight);
		dest.writeString(type);
		dest.writeString(fromComing);
	}

	public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {

		@Override
		public AddressModel[] newArray(int size) {
			return new AddressModel[size];
		}

		@Override
		public AddressModel createFromParcel(Parcel source) {
			return new AddressModel(source);
		}
	};
}
