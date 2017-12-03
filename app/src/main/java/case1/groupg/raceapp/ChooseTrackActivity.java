package case1.groupg.raceapp;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class ChooseTrackActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_track);

        TabHost tabHost = findViewById(android.R.id.tabhost);

        TabHost.TabSpec createTrackTab = tabHost.newTabSpec("Create Track");
        createTrackTab.setIndicator("Create Track");
        createTrackTab.setIndicator("", getResources().getDrawable(R.drawable.add_track));
//        topTenTab.setContent(new Intent(this, TopTenTimesTab.class));
        createTrackTab.setContent(new Intent(this, DefineRouteActivity.class));

        TabHost.TabSpec topTenXp = tabHost.newTabSpec("Choose Track");
        topTenXp.setIndicator("Choose Track");
        topTenXp.setIndicator("", getResources().getDrawable(R.drawable.bike_1));
        topTenXp.setContent(new Intent(this, TrackChooserActivity.class));

        tabHost.addTab(createTrackTab);
        tabHost.addTab(topTenXp);
    }
}
