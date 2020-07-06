package team1kdictionary.com.onekdictionary.manhinhchinh;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import adapter.FolderAdapter;
import team1kdictionary.com.model.WordFolder;
import team1kdictionary.com.onekdictionary.R;
import team1kdictionary.com.onekdictionary.databinding.ActivityFavoriteBinding;
import team1kdictionary.com.onekdictionary.hienthitu.HienThiTuActivity;
import team1kdictionary.com.onekdictionary.luyentap.FlashCardActivity;
import team1kdictionary.com.onekdictionary.luyentap.LuyenTapActivity;

public class FavoriteActivity extends AppCompatActivity {

    String DATABASE="TuDienAnhviet.sqlite";
    String DB_PATH_SUFFIX="/databases/";
    String newID;
    String newName;
    String newFolderName;
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
        binding.btnViewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, HienThiTuActivity.class);
                intent.putExtra("FID",selectedFolder.getId());
                startActivity(intent);
            }
        });
        binding.imgbtnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFolder==null)
                {
                    Toast.makeText(FavoriteActivity.this,"Mời bạn chọn folder cần đổi",Toast.LENGTH_LONG).show();
                }
                else {
                   hienThiDoiTen();
                }
            }
        });


    }

    private void hienThiDoiTen() {
        final Dialog dialog = new Dialog(FavoriteActivity.this);
        dialog.setContentView(R.layout.rename_folder_item);
        final EditText edtNewName=(EditText) dialog.findViewById(R.id.edtNewName);
        Button btnRename=dialog.findViewById(R.id.btnRename);
        btnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFolderName=edtNewName.getText().toString();
                updateName();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void updateName() {
        ContentValues values=new ContentValues();
        values.put("name",newFolderName);
        int result=database.update("folder",values,"id=?",new String[] {selectedFolder.getId()});
        if (result>0)
        {
            Toast.makeText(FavoriteActivity.this,"Đổi tên folder thành công",Toast.LENGTH_SHORT).show();
            selectedFolder.setName(newFolderName);
            binding.lvWordFile.setAdapter(folderAdapter);
        }
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
        folderAdapter.clear();
        database = openOrCreateDatabase(DATABASE, MODE_PRIVATE, null);
        Cursor c = database.rawQuery("Select * From folder", null);
        while (c.moveToNext()) {
            String id=c.getString(0);
            String name = c.getString(1);
            int soTu=0;
            Cursor d=database.rawQuery("Select * From relationships",null);
            while (d.moveToNext())
            {
                String str=d.getString(1);
                if(str!=null&&str.compareTo(id)==0)
                    soTu++;
            }
            d.close();
            WordFolder folder = new WordFolder(id,name,soTu);
            folderAdapter.add(folder);
        }
        c.close();
        binding.lvWordFile.setAdapter(folderAdapter);
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
              //  WordAdapter newWordAdapter = new WordAdapter();
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
//ád
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
