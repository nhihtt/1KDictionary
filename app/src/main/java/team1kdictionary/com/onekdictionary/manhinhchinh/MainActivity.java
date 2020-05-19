package team1kdictionary.com.onekdictionary.manhinhchinh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import team1kdictionary.com.model.Http;
import team1kdictionary.com.model.MyCustomDialog;
import team1kdictionary.com.model.Word;
import adapter.WordAdapter;
import team1kdictionary.com.onekdictionary.R;
import team1kdictionary.com.onekdictionary.databinding.ActivityMainBinding;
import team1kdictionary.com.onekdictionary.databinding.CustomDialogBinding;

public class MainActivity extends AppCompatActivity {
    String DATABASE_NAME="TuDienAnhviet.sqlite";
    String DB_PATH_SUFFIX="/databases/";
    SQLiteDatabase database=null;
    ActivityMainBinding binding;
    CustomDialogBinding customDialogBinding;
    GridView gvDic;
    WordAdapter allWordAdapter;
    public static String compareWord = "";

    public static List<String> tuDaTimKiem=new ArrayList<>();
    public static List<Word> itemsWordList = new ArrayList<>();
    public static List<Word> tempList = new ArrayList<>();

    public static Word itemSelected;
    ArrayList<String> text_matched;
    public static MyCustomDialog myDialog;

    public static int RECOGNIZER_RESULT = 1;
    public static String SPEECH_TO_TEXT = "";
    public static Intent speechIntent;
    private static SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeActivity();
        processCopy();
        addControls();
        addEvents();
    }

    private void addEvents() {
        try {
            displayWordList();
            voiceRecognization();
//            translateParagraph();
            checkPronounce();
        } catch (Exception ex) {
            Toast.makeText(this, "Error " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

//    private void translateParagraph() {
//        binding.btnTranslate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String translationString = binding.edtParagraph.getText().toString();
//                try {
//                    Http.post(translationString, "en", "es", new JsonHttpResponseHandler()
//                    {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                            try {
//                                JSONObject serverResp = new JSONObject(response.toString());
//                                JSONObject jsonObject = serverResp.getJSONObject("data");
//                                JSONArray transObject = jsonObject.getJSONArray("translations");
//                                JSONObject transObject2 =  transObject.getJSONObject(0);
//                                binding.edtResult.setText(transObject2.getString("translatedText"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        };
//                    });
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }


//    private void googleTextTranslate() {
//        FirebaseTranslatorOptions options =
//                new FirebaseTranslatorOptions.Builder()
//                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
//                        .setTargetLanguage(FirebaseTranslateLanguage.VI)
//                        .build();
//        final FirebaseTranslator englishVietNamTranslator =
//                FirebaseNaturalLanguage.getInstance().getTranslator(options);
//    }

    private void checkPronounce() {

    }

    private void voiceRecognization() {
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Đang lắng nghe...");
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK && data != null) {
            text_matched = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            SPEECH_TO_TEXT = text_matched.get(0).toString();
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
            searchView.setQuery(SPEECH_TO_TEXT, false);
            tempList.clear();
            for (int i = 0; i < itemsWordList.size(); i++) {
                if (itemsWordList.get(i).getEng().startsWith(SPEECH_TO_TEXT)) {
                    tempList.add(itemsWordList.get(i));
//                            Toast.makeText(MainActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                }
            }
            MyCustomDialog.setWordForDialog(tempList, gvDic, MainActivity.this, myDialog);
//            Toast.makeText(MainActivity.this, "Word: "+compareWord, Toast.LENGTH_LONG).show();
//            if(text_matched.contains(compareWord))
//            {
//                Toast.makeText(MainActivity.this, "100% XUẤT SẮC", Toast.LENGTH_LONG).show();
//            }


        }
        if (requestCode == 113 && resultCode == RESULT_OK && data != null)
        {
            for (int i = 0; i < itemsWordList.size(); i++) {
                if (itemsWordList.get(i).getEng().startsWith(SPEECH_TO_TEXT)) {
                    tempList.add(itemsWordList.get(i));
//                            Toast.makeText(MainActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                }
            }
            MyCustomDialog.setWordForDialog(tempList, gvDic, MainActivity.this, myDialog);

            text_matched = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            List<String> text_result = new ArrayList<>();
            for(int i=0; i<text_matched.size();i++)
            {
                text_result.add(text_matched.get(i).toLowerCase());
            }
            if(text_result.contains(compareWord) && text_result.size()==1)
            {
                Toast.makeText(MainActivity.this, "100% XUẤT SẮC", Toast.LENGTH_LONG).show();
            }
            else if(text_result.contains(compareWord) && text_result.size()==2)
            {
                Toast.makeText(MainActivity.this, "80% TỐT", Toast.LENGTH_LONG).show();
            }
            else if(text_result.contains(compareWord) && text_result.size()==3)
            {
                Toast.makeText(MainActivity.this, "70% TẠM ỔN", Toast.LENGTH_LONG).show();
            }
            else if(text_result.contains(compareWord) && text_result.size()==4)
            {
                Toast.makeText(MainActivity.this, "50% HÃY LUYỆN TẬP THÊM", Toast.LENGTH_LONG).show();
            }
            else if(text_result.contains(compareWord) && text_result.size()==5)
            {
                Toast.makeText(MainActivity.this, "HÃY XEM LẠI CÁCH PHÁT ÂM PHÍA TRÊN", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "HÃY XEM LẠI CÁCH PHÁT ÂM PHÍA TRÊN", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int spaceCount(String str) {
        int spaceCount = 0;
        for (char c : str.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
            }
        }
        return spaceCount;
    }

    private void displayWordList() {
        itemsWordList.clear();
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor c = database.rawQuery("Select * From data Where _id > 56", null);
        while (c.moveToNext()) {
            int id=c.getInt(0);
            String word = c.getString(1);
            String mean = c.getString(2);

            Word vocabulary = new Word(id,word, null, null, mean, null);
            itemsWordList.add(vocabulary);
//           allWordAdapter.add(vocabulary);

        }
        gvDic = findViewById(R.id.gvDic);
        gvDic.setAdapter(allWordAdapter);
        c.close();
    }

    private void addControls() {
        gvDic = findViewById(R.id.gvDic);
        allWordAdapter = new WordAdapter(MainActivity.this, R.layout.word_item, itemsWordList);
        gvDic.setAdapter(allWordAdapter);
//        setWordForDialog(itemsWordList);
        myDialog = new MyCustomDialog(MainActivity.this);
        MyCustomDialog.setWordForDialog(itemsWordList, gvDic, MainActivity.this, myDialog);

    }


    private void changeActivity() {
        //Set Home Selected
        binding.navBottom.setSelectedItemId(R.id.home);
        //Perform ItemSelectedListener
        binding.navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case  R.id.home:
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
                    case  R.id.setting:
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnsearch) {
            return true;
        }
        if (id == R.id.mnvoice) {
            try {
                startActivityForResult(speechIntent, RECOGNIZER_RESULT);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        "Cái điện thoại cùi bắp này không có hỗ trợ Google AI",
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        final MenuItem mnSearch = menu.findItem(R.id.mnsearch);
        searchView = (SearchView) mnSearch.getActionView();
        searchView.setQueryHint("Nhập từ tìm kiếm ở đây");
        searchView.setQuery(SPEECH_TO_TEXT, false);
        searchView.setIconifiedByDefault(false);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try{
                    //Lưu từ đã search vào history
                    database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
                    database.execSQL("Update data SET history=? Where word=?", new String[]{"1", query});
                    tuDaTimKiem.add(query);

                    tempList.clear();
                    for (int i = 0; i < itemsWordList.size(); i++) {
                        if (itemsWordList.get(i).getEng().startsWith(query)) {
                            tempList.add(itemsWordList.get(i));
//                            Toast.makeText(MainActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
//                    setWordForDialog(tempList);
                    MyCustomDialog.setWordForDialog(tempList, gvDic, MainActivity.this, myDialog);
                }
                catch (Exception ex){
                    Log.e("LOI",ex.toString());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                allWordAdapter.getFilter().filter(s);
                if (s.compareTo("") == 0) {
                    MyCustomDialog.setWordForDialog(itemsWordList, gvDic, MainActivity.this, myDialog);
                } else {
                    MyCustomDialog.setWordForDialog(tempList, gvDic, MainActivity.this, myDialog);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void processCopy(){
        try {
            File dbFile = getDatabasePath(DATABASE_NAME);
            if (!dbFile.exists()) {
                copyDatabaseFromAsset();
                Toast.makeText(MainActivity.this,"Sao chép cơ sở dữ liệu vào hệ thống điện thoại thành công",
                        Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex){
            Toast.makeText(MainActivity.this,ex.toString(),Toast.LENGTH_LONG).show();
            Log.e("LOI",ex.toString());
        }
    }
    private String getDatabasePath(){
        return getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME;
    }
    private void copyDatabaseFromAsset() {
        try{
            InputStream myInput=getAssets().open(DATABASE_NAME);
            String outFileName=getDatabasePath();
            File f=new File(getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdir();
            }
            OutputStream myOutput=new FileOutputStream(outFileName);
            byte []buffer=new byte[1024];
            int length;
            while ((length=myInput.read(buffer))>0){
                myOutput.write(buffer,0,length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception ex){
            Log.e("LOI",ex.toString());
        }
    }

    @Override
    protected void onPause() {
        myDialog = new MyCustomDialog(MainActivity.this);
        Toast.makeText(MainActivity.this, "On Pause", Toast.LENGTH_SHORT).show();
        super.onPause();
    }
}
