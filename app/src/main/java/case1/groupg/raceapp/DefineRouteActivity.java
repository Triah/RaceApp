package case1.groupg.raceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static case1.groupg.raceapp.MainActivity.player;

/**
 * Created by Nicolai on 01-12-2017.
 */

public class DefineRouteActivity extends Activity {

    EditText startAddresse;
    EditText endAddresse;
    Button confirmAddresse;
    Button confirmStartAddresseButton;
    Button confirmEndAddresseButton;
    TextView textView;
    double startLat;
    double startLng;
    double endLat;
    double endLng;

    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceUser;

    RequestQueue queue;
    String apiKey = "G4Y1nT2wA3fhwASVsxORu61nqbQhlCms";
    String country = "DK";

    public static ArrayList<Track> tracks = new ArrayList<>(); // All tracks, that anyone ever raced

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.define_route_screen);

        if(tracks.size() != 0){
            tracks.clear();
        }

        queryDataFromFireBase();

        queue = Volley.newRequestQueue(this);

        startAddresse = (EditText) findViewById(R.id.startAdresse);
        endAddresse = (EditText) findViewById(R.id.endAddresse);
        confirmAddresse = (Button) findViewById(R.id.acceptAddresseButton);
        confirmStartAddresseButton = (Button) findViewById(R.id.confirmStart);
        confirmEndAddresseButton = (Button) findViewById(R.id.confirmEnd);
        textView = (TextView) findViewById(R.id.hintAndErrorView);

        databaseReference = FirebaseDatabase.getInstance().getReference("tracks");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");

        confirmAddresse.setEnabled(false);

        confirmStartAddresseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmStartAdresse();
            }
        });

        confirmEndAddresseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmEndAddresse();
            }
        });

        confirmAddresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean trackExists = false;

                for(int i = 0; i< tracks.size(); i++){
                    if(tracks.get(i).startAddress.toLowerCase().equals(startAddresse.getText().toString().toLowerCase()) &&
                            tracks.get(i).endAddress.toLowerCase().equals((endAddresse.getText().toString().toLowerCase()))){
                            trackExists = true;
                    }

                }
                if(!trackExists){
                    Track t = new Track(startLat, 1234, endLat, startLng, endLng, null,
                            startAddresse.getText().toString(), endAddresse.getText().toString());
                    tracks.add(t);
                    updateTracksDatabase(); // Uploads the list of tracks to FireBase including this new track
                    tryCreatingRoute();
                } else {
                    return;
                }
            }
        });
    }

    public void queryDataFromFireBase(){
        databaseReference = FirebaseDatabase.getInstance().getReference("tracks");
        databaseReference.orderByChild("startAddress").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Track track = dataSnapshot.getValue(Track.class);
                tracks.add(track);
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

    public void confirmEndAddresse(){
        if(!endAddresse.getText().toString().equals("")){
            String[] strings = endAddresse.getText().toString().split(" ");
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i<strings.length;i++){
                if(i != strings.length-1){
                    sb.append(strings[i] + "%20");
                } else {
                    sb.append(strings[i]);
                }
            }
            String url = "http://www.mapquestapi.com/geocoding/v1/address?key=" +
                    apiKey +
                    "&location=" +
                    sb.toString() + "," +
                    country;
            StringRequest stringRequestEnd = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String responseStringArray[] = response.substring(0,1000).split(":");
                    StringBuilder responseSb = new StringBuilder();
                    responseSb.append(responseStringArray[36]);
                    responseSb.append(responseStringArray[37]);
                    responseSb.append(responseStringArray[38]);

                    String[] getCloseToLatLng = responseSb.toString().split("\"");
                    StringBuilder latlngSb = new StringBuilder();

                    latlngSb.append(getCloseToLatLng[2]);
                    latlngSb.append(getCloseToLatLng[4]);

                    String[] finalSplitLatLng = latlngSb.toString().split(",|\\}");
                    String lat = finalSplitLatLng[0];
                    String lng = finalSplitLatLng[1];
                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lng);
                    //the if statement has the max and min values within Fyn
                    if(latitude < 55.7 && latitude > 54.9 && longitude > 9.6 && longitude < 10.9) {
                        endLat = Double.parseDouble(lat);
                        endLng = Double.parseDouble(lng);
                        textView.setText("End Address Accepted");
                        if(startLat != 0 && startLng != 0){
                            confirmAddresse.setEnabled(true);
                        }
                    } else {
                        textView.setText("The addresse could not be found, please try another. Example: Exampleroad 22, 5000");
                        return;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("gr8 b8 m8 i r8 8/8");
                }
            });
            queue.add(stringRequestEnd);
        }
    }



    public void confirmStartAdresse() {
        if (!startAddresse.getText().toString().equals("")) {
            String[] strings = startAddresse.getText().toString().split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < strings.length; i++) {
                if (i != strings.length - 1) {
                    sb.append(strings[i] + "%20");
                } else {
                    sb.append(strings[i]);
                }
            }
            String url = "http://www.mapquestapi.com/geocoding/v1/address?key=" + apiKey + "&location=" + sb.toString() + "," + country;
            StringRequest stringRequestStart = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                        String responseStringArray[] = response.substring(0, 1000).split(":");
                        StringBuilder responseSb = new StringBuilder();
                        responseSb.append(responseStringArray[36]);
                        responseSb.append(responseStringArray[37]);
                        responseSb.append(responseStringArray[38]);

                        String[] getCloseToLatLng = responseSb.toString().split("\"");
                        StringBuilder latlngSb = new StringBuilder();

                        latlngSb.append(getCloseToLatLng[2]);
                        latlngSb.append(getCloseToLatLng[4]);

                        String[] finalSplitLatLng = latlngSb.toString().split(",|\\}");
                        String lat = finalSplitLatLng[0];
                        String lng = finalSplitLatLng[1];

                        double latitude = Double.parseDouble(lat);
                        double longitude = Double.parseDouble(lng);
                    //the if statement has the max and min values within Fyn
                        if(latitude < 55.7 && latitude > 54.9 && longitude > 9.6 && longitude < 10.9) {
                        startLat = Double.parseDouble(lat);
                        startLng = Double.parseDouble(lng);
                        textView.setText("Start Address Accepted");
                        if(endLat != 0 && endLng != 0){
                            confirmAddresse.setEnabled(true);
                        }
                        } else {
                        textView.setText("The addresse could not be found, please try another. Example: Exampleroad 22, 5000");
                        return;
                        }
                        startLat = Double.parseDouble(lat);
                        startLng = Double.parseDouble(lng);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("gr8 b8 m8 i r8 8/8");
                }
            });
            queue.add(stringRequestStart);
        }
    }


    public void tryCreatingRoute(){
        Intent createRoute = new Intent(this, MainActivity.class);
        createRoute.putExtra("startLat", startLat);
        createRoute.putExtra("startLng", startLng);
        createRoute.putExtra("endLat", endLat);
        createRoute.putExtra("endLng", endLng);
        createRoute.putExtra("startAddresse", startAddresse.getText().toString());
        createRoute.putExtra("endAddresse", endAddresse.getText().toString());
        startActivityForResult(createRoute,0);
    }

    private void updateTracksDatabase(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tracks");
        databaseReference.setValue(tracks); // Uploading all tracks
    }
}
