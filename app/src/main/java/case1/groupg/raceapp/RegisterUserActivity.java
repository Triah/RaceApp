package case1.groupg.raceapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

/**
 * Created by Nicolai on 30-11-2017.
 */

public class RegisterUserActivity extends Activity {

    TextView registerErrorMessage;
    EditText registerUsername;
    EditText registerEmail;
    EditText registerPassword;
    EditText registerConfirmPassword;
    Button finishRegistrationButton;
    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.register_user_screen);

        registerErrorMessage = (TextView) findViewById(R.id.registerErrorMessage);
        registerUsername = (EditText) findViewById(R.id.registerUsername);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPassword = (EditText) findViewById(R.id.registerpassword);
        registerConfirmPassword = (EditText) findViewById(R.id.registerconfirmpassword);
        finishRegistrationButton = (Button) findViewById(R.id.registrationcompletebutton);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        finishRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!registerUsername.getText().toString().equals("") &&
                        registerPassword.getText().toString().equals(registerConfirmPassword.getText().toString()) &&
                        !registerEmail.getText().toString().equals("")){
                    String userID = UUID.randomUUID().toString();
                    User newUser = new User(userID, registerUsername.getText().toString(), registerEmail.getText().toString(), registerPassword.getText().toString());
                    databaseReference.child(userID).setValue(newUser);


                    /**
                     * redirect to login screen here
                     */


                    //going to add more rules here, i have done this before so i have a pretty good idea of how to cover all bases
                    // -Nicolai
                } else if(registerUsername.getText().toString().equals("")){
                    registerErrorMessage.setText("Please specify a username and try again!");
                } else if(registerEmail.getText().toString().equals("")){
                    registerErrorMessage.setText("Please specify an email adresse and try again!");
                } else if(!registerPassword.getText().toString().equals(registerConfirmPassword.getText().toString())){
                    registerErrorMessage.setText("The specified passwords do not match");
                }
            }
        });
    }
}
