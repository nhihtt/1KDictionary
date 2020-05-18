package team1kdictionary.com.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import team1kdictionary.com.onekdictionary.R;
import team1kdictionary.com.onekdictionary.manhinhchinh.MainActivity;

import static android.app.Activity.RESULT_OK;
import static team1kdictionary.com.onekdictionary.manhinhchinh.MainActivity.itemSelected;

public class MyCustomDialog extends Dialog {
    ImageView imgFav, imgSound, imgFolder;
    TextView tvWord, tvInfo, txtClose;
    Button btnSpeechToText;
    GridView gvDic;

    TextToSpeech textToSpeech;
    Integer dem = 0;

    int RECOGNIZER_RESULT = 1;
    String SPEECH_TO_TEXT = "";
    Intent speechIntent;

    Activity context;

    public MyCustomDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
        setContentView(R.layout.custom_dialog);
        addControls();
        addEvents();
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
                Dialog folderDialog = new FolderCustomDialog(context);
                folderDialog.show();
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

    public static void setWordForDialog(final List<Word> listItem, GridView gvDic, final Activity context,
                                        final Dialog myDialog) {
        gvDic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                myDialog.show();
                // Set item selected
                // itemsWWordList là List<Word> lưu toàn bộ từ trong database
                // itemSelected là kiểu Word
                itemSelected = listItem.get(position);

                final String word = itemSelected.getEng();
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
                        context.startActivityForResult(MainActivity.speechIntent, 113);
                    }
                });
            }
        });
    }

    private static int spaceCount(String str) {
        int spaceCount = 0;
        for (char c : str.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
            }
        }
        return spaceCount;
    }
}
