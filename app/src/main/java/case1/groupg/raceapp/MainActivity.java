package case1.groupg.raceapp;

import android.Manifest;
import android.app.Activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public GoogleApiClient apiClient;
    private static int LOCATION_PERMISSION = 2;
    public double latitude;
    public double longitude;
    GpsLocationListener mService;
    boolean mBound = false;
    MapView map;
    IMapController mapController;
    public static final String BROADCAST_RECOGNIZED_ACTIVITY_ID = "case1.groupg.raceapp.BROADCAST_RECOGNIZED_ACTIVITY_ID";
    public static final String BROADCAST_RECOGNIZED_ACTIVITY_TEXT = "case1.groupg.raceapp.BROADCAST_RECOGNIZED_ACTIVITY_TEXT";
    private Timer timer;

    //broadcast receiver for getting recognized activity from detector service
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(BROADCAST_RECOGNIZED_ACTIVITY_ID)){
                final String text = intent.getStringExtra(BROADCAST_RECOGNIZED_ACTIVITY_TEXT);
                Toast.makeText(getApplicationContext(), "Recognized activity: " + text, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override public void onCreate(Bundle savedInstanceState) {

        doPermissions();
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(9);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);

        //connecting to the Google API Client to do activity recognition
        apiClient = new GoogleApiClient.Builder(this).addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        apiClient.connect();

        //broadcast receiver to get recognized activity from service
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_RECOGNIZED_ACTIVITY_ID);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(broadcastReceiver, intentFilter);

        //time the calls for the updating of position
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateLocation();
            }
        }, 0, 1000);
    }

    public void updateLocation(){
        this.runOnUiThread(timerTick);
    }

    private Runnable timerTick = new Runnable() {
        @Override
        public void run() {
            if(mBound){
                latitude = mService.getLatitude();
                longitude = mService.getLongitude();
                mapController.setCenter(new GeoPoint(latitude, longitude));
                mapController.setZoom(17);
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this, GpsLocationListener.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void doPermissions() {
        // If we haven't been granted the permission to use "fine location"
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION);
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            GpsLocationListener.GetLocationBinder locationService = (GpsLocationListener.GetLocationBinder) service;
            mService = locationService.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0){
            mBound = false;
        }
    };

    //not sure we need this if we want to make it record gps stuff while app is minimized
    @Override
    protected void onStop(){
        super.onStop();
        if(mBound){
            unbindService(mConnection);
            mBound = false;
        }
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }


    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent(this, RecognizedActivityService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(apiClient, 1000, pendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "Connection to apiClient suspended :(", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}