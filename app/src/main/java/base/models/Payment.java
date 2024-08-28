package base.models;

public class Payment {
    private String paymentText;
    private int paymentIcon;
    private boolean isSelected;
    private boolean isAddShown;
    private String gateWay;

    public Payment(String paymentText, int paymentIcon, boolean isSelected, boolean isAddShown, String gateWay) {
        this.paymentText = paymentText;
        this.paymentIcon = paymentIcon;
        this.isSelected = isSelected;
        this.isAddShown = isAddShown;
        this.gateWay = gateWay;
    }


    public boolean isAddShown() {
        return isAddShown;
    }

    public void setAddShown(boolean addShown) {
        isAddShown = addShown;
    }

    public String getPaymentText() {
        return paymentText;
    }

    public int getPaymentIcon() {
        return paymentIcon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setPaymentText(String paymentText) {
        this.paymentText = paymentText;
    }

    public void setPaymentIcon(int paymentIcon) {
        this.paymentIcon = paymentIcon;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getGateWay() {
        return gateWay;
    }

    public void setGateWay(String gateWay) {
        this.gateWay = gateWay;
    }
}
