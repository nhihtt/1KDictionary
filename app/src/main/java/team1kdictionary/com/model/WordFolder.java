package team1kdictionary.com.model;

import adapter.WordAdapter;

public class WordFolder {
    private String id;
    private String name;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WordFolder() {
    }

    public WordFolder(String id, String name) {
        this.id = id;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
