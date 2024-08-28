//package base.miscutilities;
//
//public class CardJudoModel {
//
//	public static final String KEY_SETTINGS_MODEL = "SettingsModelKey";
//
//	private String Name = "";
//	private boolean is3DS = false;
//	private int type = 0;
//	private String Email = "";
//	private String Address = "";
//	private String Miles = "";
//	private String Phone = "";
//	private String Mobile = "";
//	private String VehicleType = "";
//	private String PaymentType = "Cash";
//	private String LoginID = "", Password = "";
//	private String AccountNo = "";
//	private boolean Verified = false;
//	private String AccountWebID = "";
//
//	public CardJudoModel() {
//	}
//
//	public String getAccountWebID() {
//		return AccountWebID;
//	}
//
//	public void setAccountWebID(String AccountWebID) {
//		this.AccountWebID = AccountWebID;
//	}
//
//	public boolean isVerified() {
//		return Verified;
//	}
//
//	public void setVerified(boolean arg) {
//		this.Verified = arg;
//	}
//
//	public String getToken() {
//		return Name;
//	}
//
//	public void setToken(String name) {
//		this.Name = name;
//	}
//
//	public String getConsumerToken() { return Email; }
//
//	public void setConsumerToken(String email) {
//		this.Email = email;
//	}
//
//	public String getConsumerReference() {
//		return Address;
//	}
//
//	public void setConsumerReference(String arg) {
//		this.Address = arg;
//	}
//
//	public String getLastFour() {
//		return Phone;
//	}
//
//	public void setLastFour(String arg) {
//		this.Phone = arg;
//	}
//
//	public String getEndDate() {
//		return Mobile;
//	}
//
//	public void setEndDate(String arg) {
//		this.Mobile = arg;
//	}
//
//	public String getPaymentType() {
//		return PaymentType;
//	}
//
//	public void setPaymentType(String arg) {
//		this.PaymentType = arg;
//	}
//
//	public String getVehicleType() {
//		return VehicleType;
//	}
//
//	public void setVehicleType(String arg) {
//		this.VehicleType = arg;
//	}
//
//	public String getAccountNo() {
//		return AccountNo;
//	}
//
//	public void setAccountNo(String arg) {
//		this.AccountNo = arg;
//	}
//
//	public String getLoginID() {
//		return LoginID;
//	}
//
//	public void setLoginID(String arg) {
//		this.LoginID = arg;
//	}
//	public int getType() {
//		return type;
//	}
//
//	public void setType(int arg) {
//		this.type = arg;
//	}
//	public String getPassword() {
//		return Password;
//	}
//
//	public void setPassword(String arg) {
//		this.Password = arg;
//	}
//	public boolean get3ds() {
//		return is3DS;
//	}
//
//	public void set3DS(boolean is3DS) {
//		this.is3DS = is3DS;
//	}
//
//}

package base.models;

public class CardJudoModel {

    public static final String KEY_SETTINGS_MODEL = "SettingsModelKey";

    private String Name = "";
    private boolean is3DS = false;
    private int type = 0;
    private long cardId = 0;
    private String Email = "";
    private String Address = "";
    private String Miles = "";
    private String Phone = "";
    private String Mobile = "";
    private String VehicleType = "";
    private boolean isDefault;
    private String PaymentType = "Cash";
    private String LoginID = "", Password = "";
    private String AccountNo = "";
    private boolean Verified = false;
    private String AccountWebID = "";
    private String CardLabel = "";
    private int SelectedIndex = 0;

    private String receiptid = "";

    public void setReceiptid(String receiptid) {
        this.receiptid = receiptid;
    }

    public String getReceiptid() {
        return receiptid;
    }

    public void setSelectedIndex(int selectedIndex) {
        SelectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return SelectedIndex;
    }

    public void setCardLabel(String cardLabel) {
        CardLabel = cardLabel;
    }

    public String getCardLabel() {
        return CardLabel;
    }

    public CardJudoModel() {
    }


    public String getAccountWebID() {
        return AccountWebID;
    }

    public void setAccountWebID(String AccountWebID) {
        this.AccountWebID = AccountWebID;
    }

    public boolean isVerified() {
        return Verified;
    }

    public void setVerified(boolean arg) {
        this.Verified = arg;
    }

    public String getToken() {
        return Name;
    }

    public void setToken(String name) {
        this.Name = name;
    }

    public String getConsumerToken() {
        return Email;
    }

    public void setConsumerToken(String email) {
        this.Email = email;
    }

    public String getConsumerReference() {
        return Address;
    }

    public void setConsumerReference(String arg) {
        this.Address = arg;
    }

    public String getLastFour() {
        return Phone;
    }

    public void setLastFour(String arg) {
        this.Phone = arg;
    }

    public String getEndDate() {
        return Mobile;
    }

    public void setEndDate(String arg) {
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

    public int getType() {
        return type;
    }

    public void setType(int arg) {
        this.type = arg;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String arg) {
        this.Password = arg;
    }

    public boolean get3ds() {
        return is3DS;
    }

    public void set3DS(boolean is3DS) {
        this.is3DS = is3DS;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    /// New JUDO Work

    private String rawTokenString = "";

    public String getRawTokenString() {
        return rawTokenString;
    }

    public void setRawTokenString(String rawTokenString) {
        this.rawTokenString = rawTokenString;
    }
}
