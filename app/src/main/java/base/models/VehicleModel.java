package base.models;

public class VehicleModel {
	// private variables

	private int Id; /* id is auto increment */

	private String Name; // NAME
	private String TotalPassengers; // TOTALPASSENGERS
	private String TotalHandLuggages; // TOTALHANDLUGGAGES
	private String TotalLugages; // TOTALLUGGAGES
	private int SortOderNo; // SORTORDERNO
	private int serverId; // SORTORDERNO
	private int extraChargesForBabySeat; // SORTORDERNO


	public int getExtraChargesForBabySeat() {
		return extraChargesForBabySeat;
	}

	public void setExtraChargesForBabySeat(int extraChargesForBabySeat) {
		this.extraChargesForBabySeat = extraChargesForBabySeat;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getServerId() {
		return serverId;
	}

	// Empty constructor
	public VehicleModel() {
		Name = "";
		TotalPassengers = "";
		TotalHandLuggages = "";
		TotalLugages = "";
	}// End Empty constructor

	// constructor using fields
	public VehicleModel(String name, String totalPassengers, String totalHandLuggages, String totalLugages,
			int sortOderNo) {

		this.Name = name;
		this.TotalPassengers = totalPassengers;
		this.TotalHandLuggages = totalHandLuggages;
		this.TotalLugages = totalLugages;
		this.SortOderNo = sortOderNo;
	}// End constructor

	public int getId() {
		return Id;

	}

	public void setId(int id) {
		this.Id = id;
	}

	// getting Name
	public String getName() {
		return Name;
	}

	// setting Name
	public void setName(String name) {
		Name = name;
	}

	// getting TotalPassengers
	public String getTotalPassengers() {
		return TotalPassengers;
	}

	// setting TotalPassengers
	public void setTotalPassengers(String totalPassengers) {
		this.TotalPassengers = totalPassengers;
	}

	// getting TotalHandLuggages
	public String getTotalHandLuggages() {
		return TotalHandLuggages;
	}

	// setting TotalHandLuggages
	public void setTotalHandLuggages(String totalHandLuggages) {
		this.TotalHandLuggages = totalHandLuggages;
	}

	// getting TotalLugages
	public String getTotalLugages() {
		return TotalLugages;
	}

	// setting TotalLugages
	public void setTotalLugages(String totalLugages) {
		this.TotalLugages = totalLugages;
	}

	// getting SortOderNo
	public int getSortOderNo() {
		return SortOderNo;
	}

	// setting SortOderNo
	public void setSortOderNo(int sortOderNo) {
		this.SortOderNo = sortOderNo;
	}

	@Override
	public String toString() {
		return Name;
	}

}// End class VehicleModel

