package team1kdictionary.com.onekdictionary.luyentap;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import team1kdictionary.com.model.Word;
import team1kdictionary.com.onekdictionary.R;
import team1kdictionary.com.onekdictionary.databinding.ActivityFlashCardBinding;

public class FlashCardActivity extends AppCompatActivity {
    ActivityFlashCardBinding binding;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;
    private  ArrayList<Word> listWordStudying = new ArrayList<>();
    SQLiteDatabase database=null;
    String DATABASE_NAME="TuDienAnhviet.sqlite";
    String DB_PATH_SUFFIX="/databases/";
    Integer soTu;
    String FID;
    ArrayList<String> listWID=new ArrayList<>();
    ArrayList<Word> a=new ArrayList<>();
    Integer viTriTuHienTai=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityFlashCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        layDuLieu();
        addControls();
        hienThiDuLieu();
        addEvents();
    }

    private void hienThiDuLieu() {
        if(listWordStudying.size()!=0){
            binding.txtSoTu.setText(viTriTuHienTai+1+"/"+soTu);
        binding.txtFront.setText(listWordStudying.get(viTriTuHienTai).getEng());
        binding.txtBack.setText(listWordStudying.get(viTriTuHienTai).toString());
        }
    }

    private void layDuLieu() {
        Intent intent= getIntent();
        FID = intent.getStringExtra("FID");
        String folderName=intent.getStringExtra("FolderName");
        binding.txtFolder.setText(folderName);
    }

    private void addEvents() {
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viTriTuHienTai=viTriTuHienTai+1;
                if(viTriTuHienTai<soTu) {
                   hienThiDuLieu();
                }
                else {
                    viTriTuHienTai=0;
                   hienThiDuLieu();
                }
            }
        });
        binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viTriTuHienTai=viTriTuHienTai-1;
                if(viTriTuHienTai>=0) {
                  hienThiDuLieu();
                }
                else {
                    viTriTuHienTai=soTu-1;
                   hienThiDuLieu();
                }
            }
        });
    }

    private void addControls() {
        findViews();
        timTuTrongFolder();
        addWords();
        loadAnimations();
        changeCameraDistance();


    }



    private void changeCameraDistance() {
        int distance = 1000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }

    private void findViews() {
        mCardBackLayout = findViewById(R.id.card_back);
        mCardFrontLayout = findViewById(R.id.card_front);
    }

    public void flipCard(View view) {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
        }
    }
   private void addWords() {
        listWordStudying.clear();
 /*       database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor c = database.rawQuery("Select * From folder Where foldername = ...", null);
        while (c.moveToNext()) {
            int id=c.getInt(0);
            String word = c.getString(1);
            String mean = c.getString(2);

            Word vocabulary = new Word(word, null, null, mean,null);
            listWordStudying.add(vocabulary);
//           allWordAdapter.add(vocabulary);
        }

        c.close();

       //data mẫu
        Word voca1 = new Word("father", null, null, "bố",null);
        listWordStudying.add(voca1);
        Word voca2 = new Word("mother", null, null, "mẹ",null);
        listWordStudying.add(voca2);
        Word voca3 = new Word("history", null, null, "lịch sử",null);
        listWordStudying.add(voca3);
        Word voca4 = new Word("question", null, null, "câu hỏi",null);
        soTu=listWordStudying.size()+1;
        listWordStudying.add(voca4);*/
    for(int i=0;i<a.size();i++)
    {
        if(i==a.size()-1)
        {
            soTu=listWordStudying.size()+1;
        }
        listWordStudying.add(a.get(i));
    }

    }
   private void timTuTrongFolder() {
       soTu=listWordStudying.size()+1;
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        String query="Select * From relationships Where FID = '"+FID+"'";
        Cursor c = database.rawQuery(query, null);
        while (c.moveToNext()) {
            String WID=c.getString(0);
            listWID.add(WID);
        }
        c.close();

        for(int i=0;i<listWID.size();i++)
        {
            int id=Integer.parseInt(listWID.get(i));
            query="Select * From data Where _id = "+id;
            c=database.rawQuery(query, null);
            while (c.moveToNext()) {
                Word w = new Word(id, null, null, null, null,null);
                w.setIdword(c.getInt(0));
                w.setEng(c.getString(1));
                w.setRawMean(c.getString(2));
                w.setMeanAndPronounce();

                a.add(w);
            }
            c.close();
        }
        if(listWordStudying.size()!=0)
        {
            Toast.makeText(FlashCardActivity.this,"Thành công", Toast.LENGTH_LONG).show();
        }

    }

}
