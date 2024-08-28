package base.models;

public class Model_CardDetails {

	public static final String KEY_Card_MODEL = "CardModelKey";

	private String Name = "";
	private String Email = "";
	private String Address = "";
	private String Miles = "";
	private String Phone = "";
	private String Mobile = "";
	private String VehicleType = "";
	private String PaymentType = "";
	private String LoginID = "", Password = "";
	private String AccountNo = "";
	private boolean Verified = false;
	private String AccountWebID = "";

	public Model_CardDetails() {
	}

	public String getPin() {
		return AccountWebID;
	}

	public void setPin(String AccountWebID) {
		this.AccountWebID = AccountWebID;
	}

	public boolean isVerified() {
		return Verified;
	}

	public void setVerified(boolean arg) {
		this.Verified = arg;
	}

	public String getCardNumber() {
		return Name;
	}

	public void setCardNumber(String name) {
		this.Name = name;
	}

	public String getCVV() { return Email; }

	public void setCVV(String email) {
		this.Email = email;
	}
	public String getCardName() {
		return AccountNo;
	}

	public void setCardName(String arg) {
		this.AccountNo = arg;
	}
	public String getExpiryMonth() {
		return Address;
	}

	public void setExpiryMonth(String arg) {
		this.Address = arg;
	}

	public String getExpiryYear() {
		return Phone;
	}

	public void setExpiryYear(String arg) {
		this.Phone = arg;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String arg) {
		this.Mobile = arg;
	}

	public String getPaymentType() {
		return PaymentType;
	}

	public void setPaymentType(String arg) {
		this.PaymentType = arg;
	}

	public String getVehicleType() {
		return VehicleType;
	}

	public void setVehicleType(String arg) {
		this.VehicleType = arg;
	}

	public String getAccountNo() {
		return AccountNo;
	}

	public void setAccountNo(String arg) {
		this.AccountNo = arg;
	}

	public String getLoginID() {
		return LoginID;
	}

	public void setLoginID(String arg) {
		this.LoginID = arg;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String arg) {
		this.Password = arg;
	}

}
