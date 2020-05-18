package team1kdictionary.com.onekdictionary.manhinhchinh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

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

    String DATABASE="TuDienAnhviet.sqlite";
    String DB_PATH_SUFFIX="/databases/";
    String newID;
    String newName;
    SQLiteDatabase database=null;
    FolderAdapter folderAdapter;
    WordFolder selectedFolder;
    ActivityFavoriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeActivity();
        addControls();
        displayFolderList();
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
            if(selectedFolder!=null) {
                Intent intent = new Intent(FavoriteActivity.this, FlashCardActivity.class);
                intent.putExtra("FID", selectedFolder.getId());
                intent.putExtra("FolderName", binding.txtNowStudyingName.getText().toString());
                startActivity(intent);
            }
           }
        });
        binding.btnQuizGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFolder != null) {
                    Intent intent = new Intent(FavoriteActivity.this, LuyenTapActivity.class);
                    intent.putExtra("FID", selectedFolder.getId());
                    intent.putExtra("FolderName", binding.txtNowStudyingName.getText().toString());
                    startActivity(intent);
                }
            }
        });



    }


    private void addControls() {
        //khởi tạo adapter
        folderAdapter = new FolderAdapter(FavoriteActivity.this, R.layout.folder_item);
        //gán adapter cho listview
        binding.lvWordFile.setAdapter(folderAdapter);

     //   nowStudyAdapter=new FolderAdapter(FavoriteActivity.this, R.layout.word_folder_item);
      //  binding.lvNowStudy.setAdapter(nowStudyAdapter);
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

    private void displayFolderList() {
        database = openOrCreateDatabase(DATABASE, MODE_PRIVATE, null);
        Cursor c = database.rawQuery("Select * From folder", null);
        while (c.moveToNext()) {
            String id=c.getString(0);
            String name = c.getString(1);
            WordFolder folder = new WordFolder(id,name);
            folderAdapter.add(folder);
        }
        c.close();
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





    private void hienThiTaoFolder() {

        final Dialog dialog = new Dialog(FavoriteActivity.this);
        dialog.setContentView(R.layout.create_folder_item);
        final EditText edtNewFolderName = dialog.findViewById(R.id.edtNewFolderName);
        Button btnCreateNewFolder = dialog.findViewById(R.id.btnCreateFolder);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCreateNewFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newID = "fd" + (folderAdapter.getCount());
                newName=edtNewFolderName.getText().toString();
                WordFolder newFolder = new WordFolder();
                WordAdapter newWordAdapter = new WordAdapter();
                newFolder.setName(newName);
                newFolder.setId(newID);
                if(addFolder()!=0) {
                    folderAdapter.add(newFolder);
                    Toast.makeText(FavoriteActivity.this, "Tạo folder thành công", Toast.LENGTH_LONG).show();
                }
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

    private int addFolder() {
        ContentValues values=new ContentValues();
        values.put("id",newID);
        values.put("name",newName);
        int result= (int) database.insert("folder",null,values);
        return result;
    }


    private void xoaFolder() {

        if (selectedFolder != null) {
            if(deleteFolder()!=0) {
                Toast.makeText(FavoriteActivity.this,"Xóa folder thành công", Toast.LENGTH_LONG).show();
                folderAdapter.remove(selectedFolder);
                capNhatID();
            }
        }
    }

    private int deleteFolder() {
        String id=selectedFolder.getId();
        int result=database.delete("folder","id=?",new String[] {id});
        database.delete("relationships","FID=?",new String[]{id});
        return result;
    }

    public void capNhatID()
    {
        if(folderAdapter.getCount()!=0)
        {
            for(int i=0;i<folderAdapter.getCount();i++)
            {
                selectedFolder=folderAdapter.getItem(i);
                ContentValues contentValues=new ContentValues();
                newID="fd"+i;
                contentValues.put("id",newID);
                if(database.update("folder",contentValues,"id=?",new String[]{selectedFolder.getId()})!=0) {
                    selectedFolder.setId("fd" + i);
                }
            }
        }
    }



}
