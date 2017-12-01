package case1.groupg.raceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by Nicolai on 30-11-2017.
 */

public class WarningActivity extends Activity {

    Button warningButton;

    @Override
    public void onCreate(Bundle savedInstanceState){

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warning_screen);

        warningButton = (Button) findViewById(R.id.warningbutton);
        warningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginIntent = new Intent(v.getContext(), LoginActivity.class);
                startActivityForResult(toLoginIntent,0);
            }
        });
    }
}
