package case1.groupg.raceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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

    static ArrayList<User> usersList;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.register_user_screen);

        usersList = new ArrayList<>();

        queryDataFromFireBase();

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
                        !registerEmail.getText().toString().equals("")){ // Valid registration fields
                    final String userID = UUID.randomUUID().toString();
                    final User newUser = new User(userID, registerUsername.getText().toString(),
                            registerEmail.getText().toString(), registerPassword.getText().toString());

                    // Checking if user already exists or not
                    queryDataFromFireBase();
                    boolean usernameAlreadyExists = false;
                    System.out.println("$$$: " + usersList.size());
                    for (int i = 0; i < usersList.size(); i++) {
                        User user = usersList.get(i);
                        System.out.println("***** " + user.username + " ** " + user.password + " *****");
                        if(user.username.equals(registerUsername.getText().toString())){ // Already exists
                            Toast.makeText(RegisterUserActivity.this, "Username already exists, please pick another",
                                    Toast.LENGTH_SHORT).show();
                            usernameAlreadyExists = true;
                        }
                    }
                    if(!usernameAlreadyExists){
                        databaseReference.child(userID).setValue(newUser);
                        // Navigating back to the login screen
                        Intent login = new Intent(v.getContext(), LoginActivity.class);
                        startActivityForResult(login, 0);
                    }

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

    public void queryDataFromFireBase(){
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("username").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User u = dataSnapshot.getValue(User.class);
                System.out.println("&&&&& " + u.username + " ** " + u.password + " *****");
                usersList.add(u);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}