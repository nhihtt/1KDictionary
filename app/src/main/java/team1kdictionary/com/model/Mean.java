package team1kdictionary.com.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Mean {
    private String type;
    private ArrayList<String> mean;

    public Mean() {
    }

    public Mean(String type,ArrayList<String> mean) {
        this.type = type;
        this.mean = mean;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getMean() {
        return mean;
    }

    public void setMean(ArrayList<String> mean) {
        this.mean = mean;
    }

    @NonNull
    @Override
    public String toString() {
        String show=type;
        for (String str:mean
             ) {
            show+="\n"+str;
        }
        return show;
    }
}
