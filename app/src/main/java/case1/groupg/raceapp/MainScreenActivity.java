package case1.groupg.raceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Nicolai on 29-11-2017.
 */

public class MainScreenActivity extends Activity {

    Button chooseTrackButton;
    Button viewHighscoreButton;
    Button viewProfileButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        chooseTrackButton = (Button) findViewById(R.id.choosetrackbutton);
        viewHighscoreButton = (Button) findViewById(R.id.highscorebutton);
        viewProfileButton = (Button) findViewById(R.id.avatarbutton);

        chooseTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseTrack = new Intent(v.getContext(),DefineRouteActivity.class);
                startActivityForResult(chooseTrack,0);
            }
        });

        viewHighscoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewHighscore = new Intent(v.getContext(),HighscoreActivity.class);
                startActivityForResult(viewHighscore,0);
            }
        });

        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewProfile = new Intent(v.getContext(),ProfileActivity.class);
                startActivityForResult(viewProfile,0);
            }
        });

    }
}
