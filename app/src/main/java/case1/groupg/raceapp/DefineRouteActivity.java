package case1.groupg.raceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Nicolai on 01-12-2017.
 */

public class DefineRouteActivity extends Activity {

    EditText startAddresse;
    EditText endAddresse;
    Button confirmAddresse;
    double startLat;
    double startLng;

    //this is just for checking viability of response
    Button checkResultButton;
    RequestQueue queue;
    String apiKey = "G4Y1nT2wA3fhwASVsxORu61nqbQhlCms";
    String country = "DK";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.define_route_screen);

        queue = Volley.newRequestQueue(this);


        startAddresse = (EditText) findViewById(R.id.startAdresse);
        endAddresse = (EditText) findViewById(R.id.endAddresse);
        confirmAddresse = (Button) findViewById(R.id.acceptAddresseButton);
        checkResultButton = (Button) findViewById(R.id.checkResponseButton);

        checkResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResponse();
            }
        });

        confirmAddresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryCreatingRoute();
            }
        });
    }

    /**
     * TODO: bind to gps service to collect data about current location for the starting position in the next screen
     */

    public void getResponse(){
        if(!startAddresse.getText().toString().equals("")){
            String[] strings = startAddresse.getText().toString().split(" ");
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i<strings.length;i++){
                if(i != strings.length-1){
                    sb.append(strings[i] + "%20");
                } else {
                    sb.append(strings[i]);
                }

            }
            String url = "http://www.mapquestapi.com/geocoding/v1/address?key=" + apiKey + "&location=" + sb.toString() + "," + country;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("response is: " + response.substring(0,1000));
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

                    startLat = Double.parseDouble(lat);
                    startLng = Double.parseDouble(lng);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("gr8 b8 m8 i r8 8/8");
                }
            });
            queue.add(stringRequest);
        }
    }


    public void tryCreatingRoute(){
        if(!startAddresse.getText().toString().equals("") && !endAddresse.getText().toString().equals("")){
            Intent createRoute = new Intent(this, MainActivity.class);
            createRoute.putExtra("StartAddresse", startAddresse.getText().toString());
            createRoute.putExtra("EndAddresse", endAddresse.getText().toString());
            startActivityForResult(createRoute,0);
        }
    }

}
