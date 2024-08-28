package base.models;

public class Model_SubmitForm {
    private String text;
    private boolean isLost;
    private String from;

    public Model_SubmitForm(String text, String from) {
        this.text = text;
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }
}
