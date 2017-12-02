package case1.groupg.raceapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TopTenXPTab extends Activity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    static ArrayList<User> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten_xptab);

        usersList = new ArrayList<>();
        queryDataFromFireBase();

        System.out.println("OCREATE--- " + usersList.size());

//        ListAdapter topTenAdapter = new TopTenListAdatper(this,  usersList);
//        ListView topTenXPView = findViewById(R.id.top_ten_xp_list);
//        topTenXPView.setAdapter(topTenAdapter);

        System.out.println("--- OCREATE");
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        usersList = new ArrayList<>();
//        queryDataFromFireBase();
//
//        System.out.println("ORESUME--- " + usersList.size());
//
//        ListAdapter topTenAdapter = new TopTenListAdatper(this,  usersList);
//        ListView topTenXPView = findViewById(R.id.top_ten_xp_list);
//        topTenXPView.setAdapter(topTenAdapter);
//    }

    public void queryDataFromFireBase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("experiencePoints").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User u = dataSnapshot.getValue(User.class);
                System.out.println("--TOPTENXP " + u.username + " ** " + u.password + " *****");
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

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { // This is called, when all data is loaded... <3
                Collections.sort(usersList, new Comparator<User>() {
                    @Override
                    public int compare(User u1, User u2) {
                        return u2.getExperiencePoints() - u1.getExperiencePoints();
                    }
                });
                usersList = new ArrayList<User>(usersList.subList(0, usersList.size() < 10 ? usersList.size() : 10));

                ListAdapter topTenAdapter = new TopTenListAdatper(TopTenXPTab.this, usersList);
                ListView topTenXPView = findViewById(R.id.top_ten_xp_list);
                topTenXPView.setAdapter(topTenAdapter);

                System.out.println("&&^&^&&&^&^ : " + usersList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
