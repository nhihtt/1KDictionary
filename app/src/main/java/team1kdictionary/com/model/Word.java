package team1kdictionary.com.model;

import android.widget.ImageView;

import team1kdictionary.com.onekdictionary.R;

public class Word {
    private Integer idword;
    private String eng;
    private String pronounce;
    private String type;
    private String meaning;
    private int isFavorite;
    private String history;

    public Word() {
    }

    public Integer getIdword() {
        return idword;
    }

    public void setIdword(Integer idword) {
        this.idword = idword;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Word(int id,String eng, String pronounce, String type, String meaning, String history) {
        this.idword=id;
        this.eng = eng;
        this.pronounce = pronounce;
        this.type = type;
        this.meaning = meaning;
        this.history=history;
    }



    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getPronounce() {
        return pronounce;
    }

    public void setPronounce(String pronounce) {
        this.pronounce = pronounce;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }
}
