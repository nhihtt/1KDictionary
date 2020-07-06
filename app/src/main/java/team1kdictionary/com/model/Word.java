package team1kdictionary.com.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Word {
    private Integer idword;
    private String eng;
    private String pronounce;
    private String rawMean;
    private ArrayList<Mean> nghia;
//    private String type;
//    private String meaning;
    private String history;



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

    public Word(Integer idword, String eng, String pronounce, String rawMean, ArrayList<Mean> nghia, String history) {
        this.idword = idword;
        this.eng = eng;
        this.pronounce = pronounce;
        this.rawMean = rawMean;
        this.nghia = nghia;
        this.history = history;
    }

    public String getRawMean() {
        return rawMean;
    }

    public void setRawMean(String rawMean) {
        this.rawMean = rawMean;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getPronounce() {
        if(pronounce!=null && pronounce!="")
        return "/"+pronounce+"/";
        else
            return pronounce;
    }

    public void setPronounce(String pronounce) {
        this.pronounce = pronounce;
    }

//    public String getType() { return type; }

//    public void setType(String type) {     this.type = type; }

//    public String getMeaning() {     return meaning; }

//    public void setMeaning(String meaning) {     this.meaning = meaning; }


    public ArrayList<Mean> getNghia() {
        return nghia;
    }

    public void setNghia(ArrayList<Mean> nghia) {
        this.nghia = nghia;
    }

    @NonNull
    @Override
    public String toString() {
        String show="";
        if( this.nghia!=null)
        {
            for (Mean mean:nghia
                 ) {
                show+=mean.toString()+"\n"+"\n";
            }
        }
        else
        {
            show+=rawMean;
        }
        return show;
    }

    public void setMeanAndPronounce()
    {
            ArrayList<Mean> listNghia=new ArrayList();
            String pronounce = "" + tachPhatAm(rawMean);
            setPronounce(pronounce);
            String[] str = rawMean.split("[*]");
            if (str.length > 1) {
                for (int i = 1; i < str.length; i++) {
                    Mean detail = new Mean();
                    ArrayList<String> mn = new ArrayList<>();
                    String a = str[i];
                    String[] str2 = a.split("-");
                    if (str2.length > 1) {
                        for (int z = 0; z < str2.length; z++) {
                            if (z == 0)
                                detail.setType(str2[z].trim());
                            else
                                mn.add(str2[z].trim());
                        }
                        detail.setMean(mn);
                    }
                    listNghia.add(detail);
                }
                setNghia(listNghia);
            }
}


    private String tachPhatAm(String word)
    {
        String[] arr=word.split("/");
        String phatAm="";
        if(arr.length>1)
            phatAm+=arr[1];
        return phatAm.trim();
    }

}
