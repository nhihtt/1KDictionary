package team1kdictionary.com.model;

public class Relationship {
    private String WID;
    private String FID;

    public Relationship() {
    }

    public Relationship(String WID, String FID) {
        this.WID = WID;
        this.FID = FID;
    }

    public String getWID() {
        return WID;
    }

    public void setWID(String WID) {
        this.WID = WID;
    }

    public String getFID() {
        return FID;
    }

    public void setFID(String FID) {
        this.FID = FID;
    }
}
