package team1kdictionary.com.onekdictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import team1kdictionary.com.onekdictionary.manhinhchinh.MainActivity;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
