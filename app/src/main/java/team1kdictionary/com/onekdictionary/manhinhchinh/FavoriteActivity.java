package team1kdictionary.com.onekdictionary.manhinhchinh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import adapter.FolderAdapter;
import adapter.WordAdapter;
import team1kdictionary.com.model.Word;
import team1kdictionary.com.model.WordFolder;
import team1kdictionary.com.onekdictionary.R;
import team1kdictionary.com.onekdictionary.databinding.ActivityFavoriteBinding;
import team1kdictionary.com.onekdictionary.luyentap.FlashCardActivity;
import team1kdictionary.com.onekdictionary.luyentap.LuyenTapActivity;
import team1kdictionary.com.onekdictionary.manhinhchinh.HistoryActivity;
import team1kdictionary.com.onekdictionary.manhinhchinh.MainActivity;
import team1kdictionary.com.onekdictionary.manhinhchinh.SettingActivity;

public class FavoriteActivity extends AppCompatActivity {

    WordAdapter favoriteWordAdapter;
    WordFolder favoriteFolder = new WordFolder("fd0", "Favorite", favoriteWordAdapter);
    FolderAdapter folderAdapter;
    FolderAdapter nowStudyAdapter;
    WordFolder selectedFolder;
    ActivityFavoriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeActivity();
        addControls();
        addEvents();

        }



    private void addEvents() {
        binding.lvWordFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WordFolder folder = folderAdapter.getItem(position);
                selectedFolder = folder;
                binding.txtNowStudyingName.setText(selectedFolder.getName());
            }
        });
        binding.btnViewFlashCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent= new Intent(FavoriteActivity.this,FlashCardActivity.class);
               intent.putExtra("FolderName",binding.txtNowStudyingName.getText().toString());
               startActivity(intent);
            }
        });
        binding.btnQuizGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FavoriteActivity.this, LuyenTapActivity.class);
                intent.putExtra("FolderName",binding.txtNowStudyingName.getText().toString());
                startActivity(intent);
            }
        });



    }


    private void addControls() {
        //khởi tạo adapter
        folderAdapter = new FolderAdapter(FavoriteActivity.this, R.layout.folder_item);
        //gán adapter cho listview
        createDefault();
        binding.lvWordFile.setAdapter(folderAdapter);

     //   nowStudyAdapter=new FolderAdapter(FavoriteActivity.this, R.layout.word_folder_item);
      //  binding.lvNowStudy.setAdapter(nowStudyAdapter);
    }

    private void createDefault() {
        folderAdapter.add(favoriteFolder);
    }

    private void changeActivity() {
        //Set Home Selected
        binding.navBottom.setSelectedItemId(R.id.favorite);
        //Perform ItemSelectedListener
        binding.navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favorite:
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.setting:
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_folder, menu);
        final MenuItem mnuNewFolder = menu.findItem(R.id.mnuNewFolder);
        MenuItem mnuDeleteFolder = menu.findItem(R.id.mnuDeleteFolder);

        return super.onCreateOptionsMenu(menu);
    }

    public void themFolder(MenuItem item) {
        WordFolder folder = new WordFolder();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuNewFolder:
                hienThiTaoFolder();
                break;
            case R.id.mnuDeleteFolder:
                xoaFolder();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void xoaFolder() {

        if (selectedFolder != null) {
            folderAdapter.remove(selectedFolder);
        }
    }


    private void hienThiTaoFolder() {

        final Dialog dialog = new Dialog(FavoriteActivity.this);
        dialog.setContentView(R.layout.create_folder_item);
        final EditText edtNewFolderName = dialog.findViewById(R.id.edtNewFolderName);
        Button btnCreateNewFolder = dialog.findViewById(R.id.btnCreateFolder);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCreateNewFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newID = "fd" + (folderAdapter.getCount());
                WordFolder newFolder = new WordFolder();
                WordAdapter newWordAdapter = new WordAdapter();
                newFolder.setName(edtNewFolderName.getText().toString());
                newFolder.setAdpaterWordOfFolder(newWordAdapter);
                newFolder.setId(newID);
                folderAdapter.add(newFolder);
                dialog.dismiss();

            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }





}
