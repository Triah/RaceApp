package case1.groupg.raceapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Nicolai on 29-11-2017.
 */

public class ProfileActivity extends Activity {

    ImageView leftArrowImage;
    ImageView bikeImage;
    ImageView rightArrowImage;

    ImageView myBikeHeader;
    TextView selectedBikeText;

    public static final int LEVEL_SEPARATOR_XP_AMOUNT = 100;
    int availabeBikes;
    int currentPage = 0;
    int currentBikeID = R.drawable.bike_1;

    private static int[] bikes = new int[]{
            R.drawable.bike_1,
            R.drawable.bike_2,
            R.drawable.bike_3,
            R.drawable.bike_4,
            R.drawable.bike_5
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        leftArrowImage = findViewById(R.id.leftBikeSelector);
        bikeImage = findViewById(R.id.bikeImage);
        rightArrowImage = findViewById(R.id.rightBikeSelector);
        myBikeHeader = findViewById(R.id.my_bike_header);
        selectedBikeText = findViewById(R.id.selected_bike_number);

        leftArrowImage.setImageResource(R.drawable.left_arrow);
        bikeImage.setImageResource(R.drawable.bike_1);
        rightArrowImage.setImageResource(R.drawable.right_arrow);
        myBikeHeader.setImageResource(R.drawable.my_bike);

        int playerXP = MainActivity.player.getExperiencePoints();
        if(playerXP < LEVEL_SEPARATOR_XP_AMOUNT){
            availabeBikes = 0;
        } else if(playerXP >= LEVEL_SEPARATOR_XP_AMOUNT && playerXP < 2 * LEVEL_SEPARATOR_XP_AMOUNT){
            availabeBikes = 1;
        } else if(playerXP >= 2 * LEVEL_SEPARATOR_XP_AMOUNT && playerXP < 3 * LEVEL_SEPARATOR_XP_AMOUNT){
            availabeBikes = 2;
        }else if(playerXP >= 3 * LEVEL_SEPARATOR_XP_AMOUNT && playerXP < 4 * LEVEL_SEPARATOR_XP_AMOUNT){
            availabeBikes = 3;
        }else if(playerXP >= 4 * LEVEL_SEPARATOR_XP_AMOUNT){
            availabeBikes = 4;
        }

        rightArrowImage.bringToFront();
        rightArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (availabeBikes > currentPage) {
                    currentPage++;
                    bikeImage.setImageResource(bikes[currentPage]);
                    currentBikeID = bikes[currentPage];
                    selectedBikeText.setText(currentPage+1 + " / " + (availabeBikes+1));
                }
            }
        });

        leftArrowImage.bringToFront();
        leftArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(0 < currentPage) {
                    currentPage--;
                    bikeImage.setImageResource(bikes[currentPage]);
                    currentBikeID = bikes[currentPage];
                    selectedBikeText.setText(currentPage+1 + " / " + (availabeBikes+1));
                }
            }
        });

        Typeface raceFont = Typeface.createFromAsset(getAssets(), "fonts/PassionOne-Regular.ttf");
        selectedBikeText.setTypeface(raceFont);
        selectedBikeText.setText(currentPage+1 + " / " + (availabeBikes+1));
    }

    /**
     * Returns the R.drawable ID for the currently selected bike
     * @return Currently selected bike's ID
     */
    public int getCurrentBikeID(){
        return currentBikeID;
    }
}
