package team1kdictionary.com.onekdictionary.manhinhchinh;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import adapter.WordAdapter;
import team1kdictionary.com.model.Mean;
import team1kdictionary.com.model.MyCustomDialog;
import team1kdictionary.com.model.Word;
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

    //Image function
    private static final int CAMERA_REQUEST_CODE=200;
    private static final int STORAGE_REQUEST_CODE=400;
    private static final int IMAGE_PICK_GALLERY_CODE=1000;
    private static final int IMAGE_PICK_CAMERA_CODE=1001;
    private static String IMAGE_TO_TEXT="";
    String cameraPermission[];
    String storagePermission[];
    Uri image_uri;

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
        } catch (Exception ex) {
            Toast.makeText(this, "Error " + ex.toString(), Toast.LENGTH_LONG).show();
        }
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
            searchView.setQuery(SPEECH_TO_TEXT, true);
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
        if (requestCode == 113 && resultCode == RESULT_OK && data != null) {
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
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_GALLERY_CODE){
            //got image from gallery now crop it
            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidline
                    .start(this);
        }
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CAMERA_CODE){
            //got iamge from camera now crop it
            CropImage.activity(image_uri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidline
                    .start(this);
        }
        //get cropped image
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode== RESULT_OK){
                Uri resultUri = result.getUri();
                //set image to ImageView
                binding.imgPick.setImageURI(resultUri);
                //get drawable bitmap for text recognization
                BitmapDrawable bitmapDrawable= (BitmapDrawable) binding.imgPick.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer textRecognizer= new TextRecognizer.Builder(getApplicationContext()).build();
                if(!textRecognizer.isOperational()){
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                }
                else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    //get text from sb until there is no text
                    for(int i=0; i<items.size();i++)
                    {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }
                    //set text to IMAGE_TO_TEXT
                    IMAGE_TO_TEXT = sb.toString().trim();
                    searchView.setFocusable(true);
                    searchView.setIconified(false);
                    searchView.requestFocusFromTouch();
                    searchView.setQuery(IMAGE_TO_TEXT, true);
                    tempList.clear();
                    for (int i = 0; i < itemsWordList.size(); i++) {
                        if (itemsWordList.get(i).getEng().startsWith(IMAGE_TO_TEXT)) {
                            tempList.add(itemsWordList.get(i));
                        }
                    }
                    gvDic.deferNotifyDataSetChanged();
                    MyCustomDialog.setWordForDialog(tempList, gvDic, MainActivity.this, myDialog);
                }
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
                Toast.makeText(this, "ERROR: "+error, Toast.LENGTH_SHORT).show();
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
//            HashMap<String,String> sauTach= tachTu(mean);
//           Word vocabulary = new Word(id,word, sauTach.get("PhatAm"), sauTach.get("Loai1"), sauTach.get("Nghia1"), null);

            Word vocabulary = new Word(id,word,null,mean,null,null);
            if(vocabulary.getRawMean()!=null) {
               vocabulary.setMeanAndPronounce();
            }
//            setMeanAndPronounce(mean,vocabulary);
            itemsWordList.add(vocabulary);
//           allWordAdapter.add(vocabulary);

        }
        gvDic = findViewById(R.id.gvDic);
        gvDic.setAdapter(allWordAdapter);
        c.close();
    }

    private void setMeanAndPronounce(String mean, Word word) {
        Mean nghia=new Mean();
        ArrayList<Mean> listNghia=new ArrayList();
        ArrayList<String> mn=new ArrayList<>();
        String pronounce=""+tachPhatAm(mean);

        String[] str= mean.split("[*]");
        if(str.length>1)
        {
            for(int i=1;i<str.length;i++)
            {
                String a=str[i];
                String[] str2=a.split("-");
                if(str2.length>1)
                {
                    for (int z=0;z<str2.length;z++)
                    {
                        if(z==0)
                            nghia.setType(str2[z].trim());
                        else
                            mn.add(str2[z].trim());

                    }
                    nghia.setMean(mn);
                }
                listNghia.add(nghia);
            }
            word.setNghia(listNghia);
            word.setPronounce(pronounce);
        }
    }


    private String tachPhatAm(String word) {
        String[] arr=word.split("/");
        String phatAm="";
        if(arr.length>1)
            phatAm+=arr[1];
        return phatAm.trim();
    }

    private void addControls() {
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
                        finish();
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
        if(id == R.id.mnImg)
        {
            showImageImportDialog();
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
                gvDic.setVisibility(View.VISIBLE);
                allWordAdapter.getFilter().filter(s);
                if (s.compareTo("") == 0) {
//                    gvDic.setVisibility(View.INVISIBLE);
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

//    @Override
//    protected void onPause() {
//        myDialog = new MyCustomDialog(MainActivity.this);
//        Toast.makeText(MainActivity.this, "On Pause", Toast.LENGTH_SHORT).show();
//        super.onPause();
//    }

    private void showImageImportDialog() {
        // item to display dialog
        String[] items= {" Camera", " Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        // set title
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0)
                {
                    if(!checkCameraPermission())
                    {
                        // Camera permission was not allow, Request it
                        requestCameraPermission();
                    }
                    else
                    {
                        // Permission allow, take pic
                        pickCamera();
                    }
                }
                if(which == 1)
                {
                    // Gallery options clicked
                    if(!checkStoragePermission())
                    {
                        // Storage permission was not allow, Request it
                        requestStoragePermission();
                    }
                    else
                    {
                        // Permission allow, take pic
                        pickGallery();
                    }
                }
            }
        });
        dialog.create().show(); // show dialog

    }

    private void pickGallery() {
        // intent to take image from gallery
        Intent galleryIntent= new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickCamera() {
        // intent to take image from camera, it also save in storage to get high quality image
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pic"); // title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To Text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    //handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        pickCamera();
                    }
                    else{
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickGallery();
                    }
                    else{
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

}
