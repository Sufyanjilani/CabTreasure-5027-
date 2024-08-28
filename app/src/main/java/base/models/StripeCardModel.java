package base.models;

public class StripeCardModel {
    private String id;
    private String customer;
    private String exp_month;
    private String exp_year;
    private String last4;
    private String country;
    private String brand;
    private boolean isDefault;
    private int type = 0;

    public StripeCardModel(String id, String customer, String exp_month, String exp_year, String last4, String country, String brand, int type, boolean isDefault) {
        this.id = id;
        this.customer = customer;
        this.exp_month = exp_month;
        this.exp_year = exp_year;
        this.last4 = last4;
        this.country = country;
        this.brand = brand;
        this.isDefault = isDefault;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public String getExp_month() {
        return exp_month;
    }

    public String getExp_year() {
        return exp_year;
    }

    public String getLast4() {
        return last4;
    }

    public String getCountry() {
        return country;
    }

    public String getBrand() {
        return brand;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
