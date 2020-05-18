package team1kdictionary.com.onekdictionary.luyentap;

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
import team1kdictionary.com.onekdictionary.databinding.ActivityQuizBinding;

public class QuizActivity extends AppCompatActivity {
    ActivityQuizBinding binding;
    private ArrayList<Word> listWordStudying = new ArrayList<>();
    SQLiteDatabase database=null;
    String DATABASE_NAME="TuDienAnhviet.sqlite";
    String DB_PATH_SUFFIX="/databases/";
    Integer soTu;
    Integer viTriTuHienTai=0;
    String FID;
    String folderName;
    String value;
    ArrayList<String> listWID=new ArrayList<>();
    ArrayList<Word> temp=new ArrayList<>();
    Integer score=0;
    Integer diemMoiCau=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        layDuLieu();
      //  addWords();
        timTuTrongFolder();
        addWords();
        hienThiDuLieu();
        addEvents();
    }

    private void addEvents() {
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer=binding.edtAnswer.getText().toString().toLowerCase();
                String eng=listWordStudying.get(viTriTuHienTai).getEng().toString().toLowerCase();
                if(eng.contains(answer)){
                    binding.txtKetQua.setText("Bạn đã trả lời đúng");
                    score=score+diemMoiCau;
                }
                else{
                    binding.txtKetQua.setText("Bạn đã trả lời sai. Đáp án đúng là: "+listWordStudying.get(viTriTuHienTai).getEng());
                }
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viTriTuHienTai=viTriTuHienTai+1;
                if(viTriTuHienTai<soTu){
                    binding.txtKetQua.setText("");
                    binding.edtAnswer.setText("Your answer");
                    hienThiDuLieu();
                }
                else{
                    Intent intent= new Intent(QuizActivity.this,KetQuaActivity.class);
                    intent.putExtra("FolderName",binding.txtFolderName.getText().toString());
                    intent.putExtra("Score",score);
                    startActivity(intent);
                }

            }
        });
    }

    private void hienThiDuLieu() {
        binding.txtScore.setText("Score: "+score.toString());
        binding.txtQuestionCount.setText("Câu hỏi: "+(viTriTuHienTai+1)+"/"+soTu);
        binding.txtQuestion.setText(listWordStudying.get(viTriTuHienTai).getMeaning());

    }

    private void layDuLieu() {
        Intent intent= getIntent();
        FID = intent.getStringExtra("FID");
        folderName=intent.getStringExtra("FolderName");
        binding.txtFolderName.setText(value);
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
                Word w = new Word();
                w.setIdword(c.getInt(0));
                w.setEng(c.getString(1));
                w.setMeaning(c.getString(2));
                temp.add(w);
            }
            c.close();
        }
        if(listWordStudying.size()!=0)
        {
            Toast.makeText(QuizActivity.this,"Thành công", Toast.LENGTH_LONG).show();
        }

    }
    private void addWords() {
        listWordStudying.clear();
        //database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
       /* Cursor c = database.rawQuery("Select * From data Where foldername = ...", null);
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
        Word voca1 = new Word("father", null, null, "bố", null);
        listWordStudying.add(voca1);
        Word voca2 = new Word("mother", null, null, "mẹ", null);
        listWordStudying.add(voca2);
        Word voca3 = new Word("history", null, null, "lịch sử", null);
        listWordStudying.add(voca3);
        Word voca4 = new Word("question", null, null, "câu hỏi", null);
        soTu = listWordStudying.size() + 1;
        diemMoiCau=100/soTu;
        listWordStudying.add(voca4);*/
        for(int i=0;i<temp.size();i++)
        {
            if(i==temp.size()-1)
            {
                soTu=listWordStudying.size()+1;
            }
            listWordStudying.add(temp.get(i));
        }
    }
}
