package team1kdictionary.com.onekdictionary.hienthitu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import adapter.WordAdapter;
import team1kdictionary.com.model.Word;
import team1kdictionary.com.onekdictionary.R;
import team1kdictionary.com.onekdictionary.databinding.ActivityHienThiTuBinding;

public class HienThiTuActivity extends AppCompatActivity {
    ActivityHienThiTuBinding binding;
    String DATABASE="TuDienAnhviet.sqlite";
    String DB_PATH_SUFFIX="/databases/";
    SQLiteDatabase database=null;
    ArrayList<Word> listWords=new ArrayList<>();
    WordAdapter wordAdapter;
    String FID;
    String WID;
    Word word;
    ArrayList<String>listWID=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=  ActivityHienThiTuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        xuLyLayDuLieuTuBenKia();
        timTuTrongFolder();
        hienThiTu();
        addControls();
    }
    private void addControls() {
        //khởi tạo adapter
        wordAdapter = new WordAdapter(HienThiTuActivity.this, R.layout.word_item,listWords);
        binding.gvShowWords.setAdapter(wordAdapter);
    }

    private void hienThiTu() {
        database = openOrCreateDatabase(DATABASE, MODE_PRIVATE, null);
        for(int i=0;i<listWID.size();i++)
        {
            int id=Integer.parseInt(listWID.get(i));
            String query="Select * From data Where _id = "+id;
           Cursor c=database.rawQuery(query, null);
            while (c.moveToNext()) {
  //              String word=c.getString(1);
                Word w = new Word(id, null, null, null, null,null);
                w.setIdword(c.getInt(0));
                w.setEng(c.getString(1));
               w.setRawMean(c.getString(2));
               w.setMeanAndPronounce();

                listWords.add(w);
            }
            c.close();
        }

    }

    private void timTuTrongFolder() {
    //    soTu=listWordStudying.size()+1;
        database = openOrCreateDatabase(DATABASE, MODE_PRIVATE, null);
        String query="Select * From relationships Where FID = '"+FID+"'";
        Cursor c = database.rawQuery(query, null);
        while (c.moveToNext()) {
            String WID=c.getString(0);
            listWID.add(WID);
        }
        c.close();
        if(listWID.size()==0)
        {
            Toast.makeText(this,"yes",Toast.LENGTH_LONG).show();
        }

    }

    private void xuLyLayDuLieuTuBenKia() {
        Intent intent= getIntent();
    //    binding.txtMean.setText(intent.getStringExtra("txtWord"));
        FID=intent.getStringExtra("FID");
   // Toast.makeText(this,FID,Toast.LENGTH_LONG).show();
    }
}
