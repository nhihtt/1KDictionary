package adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import team1kdictionary.com.model.WordFolder;
import team1kdictionary.com.onekdictionary.R;

public class FolderAdapter extends ArrayAdapter<WordFolder> {
    Activity context;
    int resource;

    public FolderAdapter(@NonNull Activity context, int resource) {

        super(context, resource);
        this.context=context;
        this.resource=resource;
    }
//change
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = this.context.getLayoutInflater().inflate(this.resource, null);
        final WordFolder folder = getItem(position);
        TextView txtFolderName=view.findViewById(R.id.txtFolderName);
       // ImageButton imgbtnRenameFolder=view.findViewById(R.id.imgbtnRenameFolder);
        //ImageButton imgbtnDeleteFolder=view.findViewById(R.id.imgbtnRenameFolder);
      //  Button btnViewWord=view.findViewById(R.id.btnViewWord);
      //  Button btnViewFlashCard=view.findViewById(R.id.btnViewFlashCard);
      //  Button btnViewQuizGame=view.findViewById(R.id.btnQuizGame);
       /* imgbtnRenameFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtNewName=view.findViewById(R.id.txtNewName);
                folder.setName(edtNewName.getText().toString());
            }
        }); */
        txtFolderName.setText(folder.getName());
        TextView txtSoTu=view.findViewById(R.id.txtAmount);
        txtSoTu.setText("Số từ : "+folder.getSoTu());

       // txtNowStudy.setText(folder.getName());
        return view;
    }


}
