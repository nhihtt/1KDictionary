package team1kdictionary.com.onekdictionary.manhinhchinh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import adapter.SettingAdapter;
import team1kdictionary.com.model.Settings;
import team1kdictionary.com.onekdictionary.R;
import team1kdictionary.com.onekdictionary.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    SettingAdapter settingAdapter;
    public static List<Settings> arrSettings=new ArrayList<>();
    int modeType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getMode();
        changeActivity();
        settingAdapter = new SettingAdapter(SettingActivity.this, R.layout.item_settings, arrSettings);


        addEvents();
    }

    private void getMode() {

        modeType = AppCompatDelegate.getDefaultNightMode();


         if (modeType == AppCompatDelegate.MODE_NIGHT_YES) {
           binding.btnDarkmode.setText("Chuyển sang chế độ ban ngày");
        } else if (modeType == AppCompatDelegate.MODE_NIGHT_NO) {
             binding.btnDarkmode.setText("Chuyển sang chế độ ban đêm");
        }
    }


    private void addEvents() {

        binding.btnDarkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btnDarkmode.getText()=="Chuyển sang chế độ ban đêm"){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    binding.btnDarkmode.setText("Chuyển sang chế độ ban ngày");
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    binding.btnDarkmode.setText("Chuyển sang chế độ ban đêm");
                }
            }
        });
    }

    private void changeActivity() {
        //Set Home Selected
        binding.navBottom.setSelectedItemId(R.id.setting);
        //Perform ItemSelectedListener
        binding.navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case  R.id.setting:
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.favorite:
                        startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case  R.id.history:
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case  R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }


}




