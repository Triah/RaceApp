package case1.groupg.raceapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import static case1.groupg.raceapp.ProfileActivity.LEVEL_SEPARATOR_XP_AMOUNT;

public class MyLVLTab extends Activity {

    TextView xpText;
    ImageView levelMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lvltab);

        xpText = findViewById(R.id.xp_text);
        levelMeter = findViewById(R.id.level_meter);

        Typeface raceFont = Typeface.createFromAsset(getAssets(), "fonts/PassionOne-Bold.ttf");
        xpText.setTypeface(raceFont);
        xpText.setText("myXP: " + MainActivity.player.getExperiencePoints());

        int playerXP = MainActivity.player.getExperiencePoints();
        if(playerXP < LEVEL_SEPARATOR_XP_AMOUNT){
            levelMeter.setImageResource(R.drawable.level_1);
        } else if(playerXP >= LEVEL_SEPARATOR_XP_AMOUNT && playerXP < 2 * LEVEL_SEPARATOR_XP_AMOUNT){
            levelMeter.setImageResource(R.drawable.level_2);
        } else if(playerXP >= 2 * LEVEL_SEPARATOR_XP_AMOUNT && playerXP < 3 * LEVEL_SEPARATOR_XP_AMOUNT){
            levelMeter.setImageResource(R.drawable.level_3);
        }else if(playerXP >= 3 * LEVEL_SEPARATOR_XP_AMOUNT && playerXP < 4 * LEVEL_SEPARATOR_XP_AMOUNT){
            levelMeter.setImageResource(R.drawable.level_4);
        }else if(playerXP >= 4 * LEVEL_SEPARATOR_XP_AMOUNT){
            levelMeter.setImageResource(R.drawable.level_5);
        }
    }
}
