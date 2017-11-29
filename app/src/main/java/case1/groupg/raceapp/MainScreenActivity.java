package case1.groupg.raceapp;

import android.app.Activity;
import android.os.Bundle;
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

    }
}
