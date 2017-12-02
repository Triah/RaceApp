package case1.groupg.raceapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by Nicolai on 30-11-2017.
 */

public class WarningActivity extends Activity {

    ImageView warningImage;
    TextView warningText;
    ImageView acceptImage;

    @Override
    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);

        warningImage = findViewById(R.id.warning_image);
        warningText = findViewById(R.id.warning_text);
        acceptImage = findViewById(R.id.accept_image);

        warningImage.setImageResource(R.drawable.warning_sign);
        warningText.setText(R.string.warning_text);
        acceptImage.setImageResource(R.drawable.accept_warning);

        Typeface raceFont = Typeface.createFromAsset(getAssets(), "fonts/PassionOne-Regular.ttf");
        warningText.setTypeface(raceFont);

        acceptImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(toLoginIntent,0);
            }
        });

//        warningButton = (Button) findViewById(R.id.warningbutton);
//        warningButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent toLoginIntent = new Intent(v.getContext(), LoginActivity.class);
//                startActivityForResult(toLoginIntent,0);
//            }
//        });
    }
}
