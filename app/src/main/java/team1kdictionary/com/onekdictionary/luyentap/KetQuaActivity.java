package team1kdictionary.com.onekdictionary.luyentap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import team1kdictionary.com.onekdictionary.databinding.ActivityKetQuaBinding;
import team1kdictionary.com.onekdictionary.manhinhchinh.MainActivity;

public class KetQuaActivity extends AppCompatActivity {
    ActivityKetQuaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityKetQuaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        layDuLieu();
        addEvents();
    }

    private void addEvents() {
        binding.btnVeTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(KetQuaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void layDuLieu() {
        Intent intent= getIntent();
        String folderName = intent.getStringExtra("FolderName");
        String score=intent.getStringExtra("Score");
        binding.txtScore.setText("Your Score: "+score);
    }
}
