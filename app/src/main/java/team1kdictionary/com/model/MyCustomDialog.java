package team1kdictionary.com.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapter.Relationship;
import team1kdictionary.com.onekdictionary.R;
import team1kdictionary.com.onekdictionary.manhinhchinh.MainActivity;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static team1kdictionary.com.onekdictionary.manhinhchinh.MainActivity.itemSelected;

public class MyCustomDialog extends Dialog {
    ImageView imgFav, imgSound, imgFolder;
    TextView tvWord, tvInfo, txtClose;
    Button btnSpeechToText;
    GridView gvDic;
    static int WID;
    TextToSpeech textToSpeech;
    Integer dem = 0;
    ArrayList<String>selectedFoldersID=new ArrayList<>();
    int RECOGNIZER_RESULT = 1;
    String SPEECH_TO_TEXT = "";
    Intent speechIntent;
    ArrayList<WordFolder> lstFolder=new ArrayList<>();
    String DATABASE="TuDienAnhviet.sqlite";
    //    String DATABASE_NAME="Folder.sqlite";
    String DB_PATH_SUFFIX="/databases/";
    SQLiteDatabase database=null;
    String[] str;
    Activity context;
    Relationship rls;

    public MyCustomDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
        setContentView(R.layout.custom_dialog);
        addControls();
        addEvents();
        displayFolderList();

    }

    private void addEvents() {
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dem == 0) {
                    imgFav.setImageResource(R.drawable.ic_favorite_white);
                    dem += 1;
                } else if (dem == 1) {
                    imgFav.setImageResource(R.drawable.ic_favorite_border_white);
                    dem -= 1;
                }
            }
        });


        imgSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = tvWord.getText().toString();
                int speech = textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH,
                        null);
            }
        });

        imgFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Dialog folderDialog = new FolderCustomDialog(context);
                //  folderDialog.show();
                hienThiThemVaoFolder();

            }
        });
    }

    private void addControls() {
        imgFav = findViewById(R.id.imgFav);
        imgSound = findViewById(R.id.imgSound);
        imgFolder = findViewById(R.id.imgFolder);
        tvWord = findViewById(R.id.tvWord);
        tvInfo = findViewById(R.id.tvInfo);
        txtClose = findViewById(R.id.txtClose);
        btnSpeechToText = findViewById(R.id.btnTestSpeak);
        setTitle("Word");
        setCanceledOnTouchOutside(true);

        textToSpeech = new TextToSpeech(context.getApplicationContext()
                , new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }
    //cái này mới đúng nè :v
    private void hienThiThemVaoFolder() {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Chọn Folder:");
        b.setMultiChoiceItems(str, null,new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //Nếu người dùng chọn
                if (isChecked) {
                    //Thêm người dùng người dùng chọn vào ArrayList
                    selectedFoldersID.add("fd"+which);
                }
            }
        });
        b.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String wid=WID+"";
                database.delete("relationships","WID=?",new String[]{wid});
                if(selectedFoldersID.size()!=0)
                {
                    for (int i=0;i<selectedFoldersID.size();i++) {
                        ContentValues values = new ContentValues();
                        values.put("WID", wid);
                        values.put("FID",selectedFoldersID.get(i));
                        database.insert("relationships",null,values);
                    }
                }
            }
        });
        b.show();
    }
    private void displayFolderList() {
        //  folderAdapter.clear();
        database = context.openOrCreateDatabase(DATABASE, MODE_PRIVATE, null);
        Cursor c = database.rawQuery("Select * From folder", null);
        while (c.moveToNext()) {
            String id=c.getString(0);
            String name = c.getString(1);
            WordFolder folder = new WordFolder(id,name);
            lstFolder.add(folder);
        }
        c.close();
        str=new String[lstFolder.size()];
        for(int i=0; i<lstFolder.size();i++)
        {
            str[i]=lstFolder.get(i).getName();
        }
    }

    public static void setWordForDialog(final List<Word> listItem, GridView gvDic, final Activity context) {
        gvDic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Intent intent = new Intent(context, MyCustomDialog.class);
                final Dialog myDialog = new MyCustomDialog(context);
                myDialog.show();
                // Set item selected
                // itemsWWordList là List<Word> lưu toàn bộ từ trong database
                // itemSelected là kiểu Word
                itemSelected = listItem.get(position);
                WID=itemSelected.getIdword();
                String word = itemSelected.getEng();
                String mean = itemSelected.getMeaning();

                TextView tvWord = myDialog.findViewById(R.id.tvWord);
                TextView tvInfo = myDialog.findViewById(R.id.tvInfo);
                tvWord.setText(word);
                tvInfo.setText(mean);
                MainActivity.compareWord = word;

                // Set SpeechToText for button on popUp
                Button btnSpeechToText = myDialog.findViewById(R.id.btnTestSpeak);
                btnSpeechToText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        myDialog.dismiss();
//                        Dialog newDialog = new FolderCustomDialog(context);
//                        newDialog.show();
                        context.startActivityForResult(MainActivity.speechIntent, MainActivity.RECOGNIZER_RESULT);
                    }
                });
            }
        });

    }
}
