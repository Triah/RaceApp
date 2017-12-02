package case1.groupg.raceapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by barneygarda on 2017. 12. 02..
 */

public class TopTenListAdatper extends ArrayAdapter<User>  {
    public TopTenListAdatper(@NonNull Context context, ArrayList<User> resource) {
        super(context, R.layout.activity_top_ten_times_tab, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View topTenView = inflater.inflate(R.layout.top_ten_row, parent, false);

        Typeface raceFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/PassionOne-Regular.ttf");

        TextView topPlayer = topTenView.findViewById(R.id.top_ten_player); topPlayer.setTypeface(raceFont);
        TextView topValue = topTenView.findViewById(R.id.top_ten_value); topValue.setTypeface(raceFont);

        User u = getItem(position);
        topPlayer.setText(u != null ? u.getUsername() : "N/A");
        topValue.setText(u != null ? String.valueOf(u.getExperiencePoints()) : "0");

        return topTenView;
    }
}
