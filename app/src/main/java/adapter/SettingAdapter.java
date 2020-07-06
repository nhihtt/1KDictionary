package adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import team1kdictionary.com.model.Settings;
import team1kdictionary.com.onekdictionary.R;

public class SettingAdapter extends ArrayAdapter<Settings> {

    Activity context;
    int resource;
    public SettingAdapter(@NonNull Activity context, int resource, List<Settings> arrSettings) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View custom= context.getLayoutInflater().inflate(resource, null);
        //Lưu ý: ImageView (imgAvatar), textView(txtName, txtPhone)
        // 3 đứa này nằm trong custom nha
        // Nên muốn truy suất thì phải truy suất trong custom
        ImageView imgSetting = custom.findViewById(R.id.imgSettings);
        TextView txtSettings = custom.findViewById(R.id.txtSettings);
        // Cái đối số position là vị trí dòng dữ liệu được nhân bản
        Settings sets= getItem(position);
        imgSetting.setImageResource(sets.getAvatar());
        txtSettings.setText(sets.getSetting());
        return custom;
    }
}
