package team1kdictionary.com.model;

public class WordFolder {
    private String id;
    private String name;
    private int soTu;

    public int getSoTu() {
        return soTu;
    }

    public void setSoTu(int soTu) {
        this.soTu = soTu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WordFolder() {
    }

    public WordFolder(String id, String name, int soTu) {
        this.id = id;
        this.name = name;
        this.soTu=soTu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
