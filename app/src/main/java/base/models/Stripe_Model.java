package base.models;

public class Stripe_Model {
    private String cardToken;
    private String lastFour;
    private String uniqueId;
    private String stripeCustomerId;
    private String stripeSecretKey;
    private String gatewayType;
    private String Currency;
    private double totalFares;
    private String transactionId;

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Stripe_Model() {
    }

    public Stripe_Model(String cardToken, String lastFour, String uniqueId, String stripeCustomerId, String stripeSecretKey, String gatewayType, String Currency, double totalFares,String transactionId) {
        this.cardToken = cardToken;
        this.lastFour = lastFour;
        this.uniqueId = uniqueId;
        this.stripeCustomerId = stripeCustomerId;
        this.stripeSecretKey = stripeSecretKey;
        this.gatewayType = gatewayType;
        this.Currency = Currency;
        this.totalFares = totalFares;
        this.transactionId = transactionId;
    }

    public Stripe_Model(String cardToken, String lastFour, String uniqueId, String stripeCustomerId, String stripeSecretKey, String gatewayType, String Currency) {
        this.cardToken = cardToken;
        this.lastFour = lastFour;
        this.uniqueId = uniqueId;
        this.stripeCustomerId = stripeCustomerId;
        this.stripeSecretKey = stripeSecretKey;
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

    public void setStripeSecretKey(String stripeSecretKey) {
        this.stripeSecretKey = stripeSecretKey;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }
}
