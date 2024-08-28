package base.models;

public class PaymentGateway {
    public String defaultClientId;
    public String CustomerId;
    public String CustomerName;
    public boolean IsSandbox;
    public String CustomerNumber;
    public String CustomerEmail;

    public double Amount;
    public String Currency;
    public String JudoId;
    public String APIToken;
    public String APISecret;
    public boolean IsRegisterCard;
    public String yourConsumerReference;
    public String Description ;
    public double DisplayAmount;

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public PaymentGateway(String defaultClientId, String customerId, String customerName, String customerNumber, String customerEmail, double amount, double displayAmount, String currency, String judoId, String APIToken, String APISecret, boolean isRegisterCard, boolean isSandbox, String yourConsumerReference, String description) {
        this.defaultClientId = defaultClientId;
        CustomerId = customerId;
        CustomerName = customerName;
        CustomerNumber = customerNumber;
        CustomerEmail = customerEmail;
        Amount = amount;
        DisplayAmount = displayAmount;
        Currency = currency;
        JudoId = judoId;
        this.APIToken = APIToken;
        this.APISecret = APISecret;
        IsRegisterCard = isRegisterCard;
        IsSandbox = isSandbox;
        this.yourConsumerReference = yourConsumerReference;
        Description =  description;
    }

    public boolean isSandbox() {
        return IsSandbox;
    }

    public void setSandbox(boolean sandbox) {
        IsSandbox = sandbox;
    }

    public double getDisplayAmount() {
        return DisplayAmount;
    }

    public void setDisplayAmount(double displayAmount) {
        DisplayAmount = displayAmount;
    }

    public String getDefaultClientId() {
        return defaultClientId;
    }

    public void setDefaultClientId(String defaultClientId) {
        this.defaultClientId = defaultClientId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        CustomerNumber = customerNumber;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getJudoId() {
        return JudoId;
    }

    public void setJudoId(String judoId) {
        JudoId = judoId;
    }

    public String getAPIToken() {
        return APIToken;
    }

    public void setAPIToken(String APIToken) {
        this.APIToken = APIToken;
    }

    public String getAPISecret() {
        return APISecret;
    }

    public void setAPISecret(String APISecret) {
        this.APISecret = APISecret;
    }

    public boolean isRegisterCard() {
        return IsRegisterCard;
    }

    public void setRegisterCard(boolean registerCard) {
        IsRegisterCard = registerCard;
    }

    public String getYourConsumerReference() {
        return yourConsumerReference;
    }

    public void setYourConsumerReference(String yourConsumerReference) {
        this.yourConsumerReference = yourConsumerReference;
    }
}
