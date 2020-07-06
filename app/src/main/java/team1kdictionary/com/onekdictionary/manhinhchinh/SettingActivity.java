package team1kdictionary.com.onekdictionary.manhinhchinh;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import adapter.SettingAdapter;
import team1kdictionary.com.model.Settings;
import team1kdictionary.com.onekdictionary.R;
import team1kdictionary.com.onekdictionary.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    SettingAdapter settingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeActivity();
        settingAdapter = new SettingAdapter(SettingActivity.this, R.layout.item_settings);
        binding.lvSettings.setAdapter(settingAdapter);
        addSettingData();
    }

    private void addSettingData() {
        String[] arrSetting={"Nhắc nhở học từ vựng", "Chuyển sang giao diện DarkMode", "Báo lỗi", "Thông tin phiên bản"};
        settingAdapter.add(new Settings(R.drawable.img_alarm, "Nhắc nhở từ vựng"));
        settingAdapter.add(new Settings(R.drawable.img_darkmode, "Chuyển sang giao diện DarkMode"));
        settingAdapter.add(new Settings(R.drawable.img_messages, "Báo lỗi"));
        settingAdapter.add(new Settings(R.drawable.img_info, "Thông tin phiên bản"));
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




