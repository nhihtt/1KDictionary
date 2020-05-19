package team1kdictionary.com.onekdictionary.manhinhchinh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import adapter.WordAdapter;
import team1kdictionary.com.model.MyCustomDialog;
import team1kdictionary.com.model.Word;
import team1kdictionary.com.onekdictionary.R;
import team1kdictionary.com.onekdictionary.databinding.ActivityHistoryBinding;


import static team1kdictionary.com.onekdictionary.manhinhchinh.MainActivity.tuDaTimKiem;

public class HistoryActivity extends AppCompatActivity {

    WordAdapter adapterWord;
    ActivityHistoryBinding binding;
    SQLiteDatabase database=null;
    String DATABASE_NAME="TuDienAnhviet.sqlite";
    String DB_PATH_SUFFIX="/databases/";
    GridView gvWordList;
    WordAdapter allWordAdapter;
    SearchView searchView;
    ArrayList<String> text_matched;
    Dialog historyDialog;
    public static List<Word> itemsWordListHistory = new ArrayList<>();
    public static List<Word> tempWordListHistory = new ArrayList<>();
    public static int RECOGNIZER_RESULT = 1;
    public static String SPEECH_TO_TEXT = "";
    public static Intent speechIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeActivity();
        addWords();
        addControls();
        addEvents();
    }

    private void addEvents() {
        voiceRecognization();
    }

    private void changeActivity() {
        //Set Home Selected
        binding.navBottom.setSelectedItemId(R.id.history);
        //Perform ItemSelectedListener
        binding.navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case  R.id.history:
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.favorite:
                        startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case  R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    private void addWords() {
        itemsWordListHistory.clear();
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        Cursor c = database.rawQuery("Select * From data Where history = 1", null);
        while (c.moveToNext()) {
            int id=c.getInt(0);
            String word = c.getString(1);
            String mean = c.getString(2);

            Word vocabulary = new Word(word, null, null, mean,null);
            itemsWordListHistory.add(vocabulary);
                Word vocabulary = new Word(id,word, null, null,mean,null);
                itemsWordListHistory.add(vocabulary);
//           allWordAdapter.add(vocabulary);



        }

        c.close();

    }

    private void addControls() {
        historyDialog = new MyCustomDialog(HistoryActivity.this);
        gvWordList = findViewById(R.id.gvWordsList);
        allWordAdapter = new WordAdapter(HistoryActivity.this, R.layout.word_item, itemsWordListHistory);
        gvWordList.setAdapter(allWordAdapter);
        MyCustomDialog.setWordForDialog(itemsWordListHistory, gvWordList, HistoryActivity.this, historyDialog);
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

            tempWordListHistory.clear();
            for (int i = 0; i < itemsWordListHistory.size(); i++) {
                if (itemsWordListHistory.get(i).getEng().startsWith(SPEECH_TO_TEXT)) {
                    tempWordListHistory.add(itemsWordListHistory.get(i));
//                            Toast.makeText(MainActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                }
            }
            MyCustomDialog.setWordForDialog(tempWordListHistory, gvWordList, HistoryActivity.this, historyDialog);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        final MenuItem mnSearch = menu.findItem(R.id.mnsearch);
        searchView = (SearchView) mnSearch.getActionView();
        searchView.setQueryHint("Nhập từ tìm kiếm ở đây");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try{
                    //Lưu từ đã search vào history
                    database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
                    database.execSQL("Update data SET history=? Where word=?", new String[]{"1", query});
                    tuDaTimKiem.add(query);

                    tempWordListHistory.clear();
                    for (int i = 0; i < itemsWordListHistory.size(); i++) {
                        if (itemsWordListHistory.get(i).getEng().startsWith(query)) {
                            tempWordListHistory.add(itemsWordListHistory.get(i));
//                            Toast.makeText(MainActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
//                    setWordForDialog(tempList);
                    MyCustomDialog.setWordForDialog(tempWordListHistory, gvWordList, HistoryActivity.this
                    , historyDialog);
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
                    MyCustomDialog.setWordForDialog(itemsWordListHistory, gvWordList, HistoryActivity.this,
                            historyDialog);
                } else {
                    MyCustomDialog.setWordForDialog(tempWordListHistory, gvWordList, HistoryActivity.this,
                            historyDialog);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnsearch) {
            return true;
        }
        if (id == R.id.mnvoice) {
            startActivityForResult(speechIntent, RECOGNIZER_RESULT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
