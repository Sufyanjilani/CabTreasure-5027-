package base.models;

public class ChatModel {
    private String from;
    private String text;
    private String date;
    private int messageStatus;

    public ChatModel(String from, String text, String date, int messageStatus) {
        this.from = from;
        this.text = text;
        this.date = date;
        this.messageStatus = messageStatus;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }
}

