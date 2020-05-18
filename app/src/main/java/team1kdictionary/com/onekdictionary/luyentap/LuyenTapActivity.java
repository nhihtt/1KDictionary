package team1kdictionary.com.onekdictionary.luyentap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import team1kdictionary.com.onekdictionary.R;
import team1kdictionary.com.onekdictionary.databinding.ActivityLuyenTapBinding;

public class LuyenTapActivity extends AppCompatActivity {
ActivityLuyenTapBinding binding;
String FID;
String folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLuyenTapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        layDuLieu();
        addEvents();

    }

    private void addEvents() {
        Button btnStarQuiz=findViewById(R.id.btnStartQuiz);
        btnStarQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });
    }

    private void startQuiz() {
        Intent intent= new Intent(LuyenTapActivity.this,QuizActivity.class);
        intent.putExtra("FID",FID);
        intent.putExtra("FolderName",folderName);
        startActivity(intent);
    }
    private void layDuLieu() {
        Intent intent= getIntent();
        FID = intent.getStringExtra("FID");
        folderName=intent.getStringExtra("FolderName");
        binding.txtQuizName.setText(folderName+" Quiz");
    }

}
