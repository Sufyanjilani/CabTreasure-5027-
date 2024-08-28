package base.models;

public class SearchCategoryModel {


    private String title;
    private int  icon;
    private String keyWork;


    public SearchCategoryModel(String title, int icon,String keyWork) {
        this.title = title;
        this.icon = icon;
        this.keyWork = keyWork;

    }


    public String getKeyWork() {
        return keyWork;
    }

    public void setKeyWork(String keyWork) {
        this.keyWork = keyWork;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


}
