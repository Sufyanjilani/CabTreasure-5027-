package base.models;




public class KonnectCardModel {
    private String cardToken;
    private String lastFour;
    private String uniqueId;
    private String stripeCustomerId;

    private String gatewayType;
    private String Currency;
    private String expYear;
    private String expMonth;
    private String fingerprint;
    private String funding;
    private String postalCode;
    private String brand;


    private double totalFares;
    private String transactionId;

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public KonnectCardModel() {
    }



    public KonnectCardModel(String cardToken, String lastFour, String uniqueId, String stripeCustomerId, String gatewayType, String Currency) {
        this.cardToken = cardToken;
        this.lastFour = lastFour;
        this.uniqueId = uniqueId;
        this.stripeCustomerId = stripeCustomerId;
        this.gatewayType = gatewayType;
        this.Currency = Currency;
        transactionId="";
    }



    public void setTotalFares(double totalFares) {
        this.totalFares = totalFares;
    }

    public String getCardToken() {
        return cardToken;
    }

    public String getLastFour() {
        return lastFour;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public double getTotalFares() {
        return totalFares;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public void setLastFour(String lastFour) {
        this.lastFour = lastFour;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public String getCurrency() {
        return Currency;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getFunding() {
        return funding;
    }

    public void setFunding(String funding) {
        this.funding = funding;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }
}


