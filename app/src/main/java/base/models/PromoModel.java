package base.models;

public class PromoModel {

	public static final String KEY_SETTINGS_MODEL = "SettingsModelKey";

	private String Name = "";
	private String total = "";
	private String used = "";
	private String promoId = "";
	private String Email = "";
	private String Address = "";
	private String PromoCode = "";
	private String Miles = "";
	private String Phone = "";
	private String MaxDiscount = "";
	private String MinFares = "";
	private String Mobile = "";
	private String VehicleType = "";
	private String PaymentType = "0";
	private String LoginID = "", Password = "";
	private String AccountNo = "";
	private boolean Verified = false;
	private String AccountWebID = "";
	public static final String KEY_PR_CODE = "PR_CODE";
	public static final String KEY_PR__TITLE= "PR__TITLE";
	public static final String KEY_PR_MSG = "PR_MSG";
	public static final String KEY_PR_STRTDATE = "PR_STRTDATE";
	public static final String KEY_PR_ENDDATE = "PR_ENDDATE";
	public PromoModel() {
	}

	public String getPROMOTIONTYPEID() {
		return AccountWebID;
	}

	public void setPROMOTIONTYPEID(String AccountWebID) {
		this.AccountWebID = AccountWebID;
	}

	public boolean isVerified() {
		return Verified;
	}

	public void setVerified(boolean arg) {
		this.Verified = arg;
	}
	public String getMaxDiscount() {
		return MaxDiscount;
	}

	public void setMaxDiscount(String MaxDiscount) {
		this.MaxDiscount = MaxDiscount;
	}
	public String getMinFares() {
		return MinFares;
	}

	public void setMinFares(String MinFares) {
		this.MinFares = MinFares;
	}
	public String getTitle() {
		return Name;
	}

	public void setTitle(String name) {
		this.Name = name;
	}
	public String getpromoserverId() {
		return promoId;
	}

	public void setPromoServerId(String name) {
		this.promoId = name;
	}
	public String getused() {
		return used;
	}

	public void setUsed(String name) {
		this.used = name;
	}
	public String gettotal() {
		return total;
	}

	public void setTotal(String name) {
		this.total = name;
	}

	public String getMsg() { return Email; }

	public void setMsg(String email) {
		this.Email = email;
	}

//	public String getPrmCode() {
//		return Address;
//	}
//
//	public void setPrmCode(String arg) {
//		this.Address = arg;
//	}

	public String getEndDate() {
		return Phone;
	}

	public void setEndDate(String arg) {
		this.Phone = arg;
	}

	public String getStrtDate() {
		return Mobile;
	}

	public void setStrtDate(String arg) {
		this.Mobile = arg;
	}

	public String getPromoValue() {
		return PaymentType;
	}

	public void setPromoValue(String arg) {
		this.PaymentType = arg;
	}

	public String getVehicleType() {
		return VehicleType;
	}

	public void setVehicleType(String arg) {
		this.VehicleType = arg;
	}

	public String getPromoType() {
		return AccountNo;
	}

	public void setPromType(String arg) {
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
	public String getPromoCode() {
		return PromoCode;
	}

	public void setPromoCode(String arg) {
		this.PromoCode = arg;
	}

}
