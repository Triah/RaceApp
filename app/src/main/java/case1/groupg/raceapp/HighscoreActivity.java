package case1.groupg.raceapp;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by Nicolai on 29-11-2017.
 */

public class HighscoreActivity extends TabActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_screen);

        TabHost tabHost = findViewById(android.R.id.tabhost);

        TabHost.TabSpec topTenTab = tabHost.newTabSpec("Top Ten Times");
        topTenTab.setIndicator("Top Ten Times");
        topTenTab.setIndicator("", getResources().getDrawable(R.drawable.top_ten_times));
        topTenTab.setContent(new Intent(this, TopTenTimesTab.class));

        TabHost.TabSpec topTenXp = tabHost.newTabSpec("Top Ten XP");
        topTenXp.setIndicator("Top Ten XP");
        topTenXp.setIndicator("", getResources().getDrawable(R.drawable.top_ten_xp));
        topTenXp.setContent(new Intent(this, TopTenXPTab.class));

        TabHost.TabSpec myLVL = tabHost.newTabSpec("My Level");
        myLVL.setIndicator("My Level");
        myLVL.setIndicator("", getResources().getDrawable(R.drawable.my_level));
        myLVL.setContent(new Intent(this, TopTenXPTab.class));

        tabHost.addTab(topTenTab);
        tabHost.addTab(topTenXp);
        tabHost.addTab(myLVL);
    }
}
