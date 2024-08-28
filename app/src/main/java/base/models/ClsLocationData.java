package base.models;

public class ClsLocationData {
    public int defaultClientId;
    public String keyword;
    public double lat;
    public double lng;
    public String apiKey;
    public String fetchString;
    public String hashKey;

    public ClsLocationData(String keyword, double lat, double lng) {
        this.keyword = keyword;
        this.lat = lat;
        this.lng = lng;
    }

    public ClsLocationData(int defaultClientId, String keyword, double lat, double lng, String apiKey, String fetchString, String hashKey) {
        this.defaultClientId = defaultClientId;
        this.keyword = keyword;
        this.lat = lat;
        this.lng = lng;
        this.apiKey = apiKey;
        this.fetchString = fetchString;
        this.hashKey = hashKey;
    }
}
