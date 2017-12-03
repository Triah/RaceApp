package case1.groupg.raceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Nicolai on 29-11-2017.
 */

public class MainScreenActivity extends Activity {

//    Button chooseTrackButton;
//    Button viewHighscoreButton;
//    Button viewProfileButton;

    ImageView appIcon;
    ImageView trackChooser;
    ImageView highScores;
    ImageView profile;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

//        chooseTrackButton = (Button) findViewById(R.id.choosetrackbutton);
//        viewHighscoreButton = (Button) findViewById(R.id.highscorebutton);
//        viewProfileButton = (Button) findViewById(R.id.avatarbutton);

        appIcon = findViewById(R.id.app_icon_icon);
        trackChooser = findViewById(R.id.choose_track_icon);
        highScores = findViewById(R.id.high_scores_icon);
        profile = findViewById(R.id.profile_icon);

        appIcon.setImageResource(R.drawable.app_icon);
        trackChooser.setImageResource(R.drawable.choose_track);
        highScores.setImageResource(R.drawable.high_scores);
        profile.setImageResource(R.drawable.profile);

        trackChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseTrack = new Intent(v.getContext(),ChooseTrackActivity.class);
                startActivityForResult(chooseTrack,0);
            }
        });

        highScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewHighscore = new Intent(v.getContext(),HighscoreActivity.class);
                startActivityForResult(viewHighscore,0);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewProfile = new Intent(v.getContext(),ProfileActivity.class);
                startActivityForResult(viewProfile,0);
            }
        });

    }
}
