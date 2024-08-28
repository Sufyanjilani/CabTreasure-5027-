package base.models;

public class Model_FindLost {
    private String lostItemName;
    private boolean isLost;


    public Model_FindLost(String lostItemName, boolean isLost) {
        this.lostItemName = lostItemName;
        this.isLost = isLost;
    }

    public String getLostItemName() {
        return lostItemName;
    }

    public void setLostItemName(String lostItemName) {
        this.lostItemName = lostItemName;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }
}
