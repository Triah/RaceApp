package case1.groupg.raceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Nicolai on 30-11-2017.
 */

public class LoginActivity extends Activity{

    EditText username;
    EditText password;
    Button loginButton;
    Button registerButton;

    ImageView appIcon;

    DatabaseReference databaseReference;

    ArrayList<User> usersList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        usersList = new ArrayList<>();
        queryDataFromFireBase();

        username = (EditText) findViewById(R.id.usernameText);
        password = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.loginbutton);
        registerButton = (Button) findViewById(R.id.registerbutton);
        appIcon = findViewById(R.id.app_icon);
        appIcon.setImageResource(R.drawable.app_icon);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("") || password.getText().toString().equals("")){ // Invalid login fields
                    Toast.makeText(LoginActivity.this, "Please fill all login fields", Toast.LENGTH_SHORT).show();
                } else { // Valid login fields
                    System.out.println("==========");
                    queryDataFromFireBase();
                    System.out.println("==========");

                    MainActivity.player = null;
                    for (int i = 0; i < usersList.size(); i++) {
                        User user = usersList.get(i);
                        if(user.username.equals(username.getText().toString()) &&
                                user.password.equals(password.getText().toString())){
                            MainActivity.player = user;
                        }
                    }

                    if(MainActivity.player != null){
                        // Navigating to the main menu
                        Intent login = new Intent(v.getContext(), MainScreenActivity.class);
                        startActivityForResult(login, 0);
                    } else {
                        Toast.makeText(LoginActivity.this, "Wrong username and password combination", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(v.getContext(), RegisterUserActivity.class);
                startActivityForResult(register,0);
            }
        });
    }

    public void queryDataFromFireBase(){
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("username").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User u = dataSnapshot.getValue(User.class);
                usersList.add(u);
                System.out.println("***** " + u.username + " ** " + u.password + " *****");
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

        // Querying ArrayList from FireBase
//        databaseReference = FirebaseDatabase.getInstance().getReference("tracks");
//        databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayList<Track> al = (ArrayList<Track>) dataSnapshot.getValue();
//                DefineRouteActivity.tracks = al;
//                System.out.println("#######********##########*************#####");
//                System.out.println(DefineRouteActivity.tracks.getClass());
//                System.out.println(DefineRouteActivity.tracks.size());
//                System.out.println(DefineRouteActivity.tracks.get(0));
//                Collection<Track> values = DefineRouteActivity.tracks;
//                ArrayList<Track> tt = new ArrayList<>(values);
//                for (int i = 0; i < tt.size(); i++) {
//                    Track track = tt.get(i);
//                    System.out.println("wow");
//                }
//
//                System.out.println("#######********##########*************##### END END END");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}
