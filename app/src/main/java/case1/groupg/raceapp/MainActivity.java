package case1.groupg.raceapp;

import android.app.Activity;


import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.util.Parameters.Algorithms;
import com.graphhopper.util.Parameters.Routing;
import com.graphhopper.util.PointList;
import com.graphhopper.util.StopWatch;

import org.oscim.android.MapView;
import org.oscim.android.canvas.AndroidGraphics;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.core.GeoPoint;
import org.oscim.event.Gesture;
import org.oscim.event.GestureListener;
import org.oscim.event.MotionEvent;
import org.oscim.layers.Layer;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.layers.vector.PathLayer;
import org.oscim.layers.vector.geometries.Style;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.source.mapfile.MapFileTileSource;

import java.util.Timer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int NEW_MENU_ID = Menu.FIRST + 1;
    private MapView mapView;
    private GraphHopper hopper;
    private GeoPoint start;
    private GeoPoint end;
    private volatile boolean prepareInProgress = false;
    private volatile boolean shortestPathRunning = false;
    private String currentArea = "odense"; //not sure if this is actually needed because i can change it with no effect
    private File mapsFolder;
    private ItemizedLayer<MarkerItem> itemizedLayer;
    private PathLayer pathLayer;
    boolean centered = false;
    double distanceCurrentAndStart;
    double distanceCurrentAndEnd;

    public GoogleApiClient apiClient;
    private static int LOCATION_PERMISSION = 2;
    public double latitude;
    public double longitude;
    GpsLocationListener mService;
    boolean mBound = false;
    public static final String BROADCAST_RECOGNIZED_ACTIVITY_ID = "case1.groupg.raceapp.BROADCAST_RECOGNIZED_ACTIVITY_ID";
    public static final String BROADCAST_RECOGNIZED_ACTIVITY_TEXT = "case1.groupg.raceapp.BROADCAST_RECOGNIZED_ACTIVITY_TEXT";
    private Timer timer;
    double startLat;
    double startLng;
    double endLat;
    double endLng;
    public GeoPoint startRoutePosition;
    public GeoPoint endRoutePosition;
    String id;

    public static User player = null; // The user, who is logged in, and plays the game
    public static ArrayList<Track> tracks = new ArrayList<>();
    DatabaseReference trackTimeDatabaseReference;
    DatabaseReference usersTableDatabaseReference;
    boolean isRacing = false; // for testing purposes
    long startTime;
    long endTime;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trackTimeDatabaseReference = FirebaseDatabase.getInstance().getReference("trackTimes");
        usersTableDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        doPermissions();
        //this leaves out the map untill it has been chosen i believe
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //this draws the three linear layouts
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mapView = new MapView(this);

        //setContentView(mapView);
        Intent intentPoints = getIntent();
        startLat = intentPoints.getDoubleExtra("startLat",0);
        startLng = intentPoints.getDoubleExtra("startLng",0);
        endLat = intentPoints.getDoubleExtra("endLat",0);
        endLng = intentPoints.getDoubleExtra("endLng",0);

        //the positions for the markers
        startRoutePosition = new GeoPoint(startLat, startLng);
        endRoutePosition = new GeoPoint(endLat,endLng);

        //broadcast receiver to get recognized activity from service
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_RECOGNIZED_ACTIVITY_ID);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(broadcastReceiver, intentFilter);

        if (Build.VERSION.SDK_INT >= 19) {
            //check for avaliable memory this is most likely for the map
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                logUser("GraphHopper is not usable without an external storage!");
                return;
            }
            //storage place for the map
            mapsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "/graphhopper/maps/");
        } else
            //alternative position
            mapsFolder = new File(Environment.getExternalStorageDirectory(), "/graphhopper/maps/");

        // create the folder if it does not exist
        if (!mapsFolder.exists())
            mapsFolder.mkdirs();

        //executes the fetching of the files which have been downloaded already
        //TODO: This must be refactored to just initialize the map instead of choosing it
        chooseAreaFromLocal();


        /**
         * this needs to be reworked for the new location stuff
         * TODO: Find the proper way to set the user lat, lng as the starting point -- commented out for now
         */

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateLocation();
            }
        }, 0, 5000);
        }


    public void updateLocation() {
        if (!isReady()) {
            return;
        }
         else{
            if(start == null && end == null) {
            this.runOnUiThread(timerTick);
            }
            if(mBound){
                if(mBound) {
                    latitude = mService.getLatitude();
                    longitude = mService.getLongitude();
                    this.runOnUiThread(updateMapCenter);
                }
            }
        }
    }

    private Runnable updateMapCenter = new Runnable() {
        @Override
        public void run() {
            if(!centered){
                mapView.map().setMapPosition(latitude, longitude, 1<<15);
                centered = true;
            }
            GeoPoint currentLocation = new GeoPoint(latitude,longitude);

//            Drawable drawable = getResources().getDrawable(R.drawable.marker_icon_red);
            Drawable drawable = getResources().getDrawable(R.drawable.bike_1_icon);
            Bitmap bitmap = AndroidGraphics.drawableToBitmap(drawable);

            MarkerSymbol markerSymbol = new MarkerSymbol(bitmap, 0.5f, 1);
            MarkerItem markerItem = new MarkerItem("currentPosition", "", currentLocation);
            markerItem.setMarker(markerSymbol);
            if(itemizedLayer.getItemList().size() >= 3){
                itemizedLayer.getItemList().remove(2);
            }
            itemizedLayer.addItem(markerItem);
            mapView.map().updateMap(true);

            distanceCurrentAndStart = distanceBetweenSetPointAndCurrent(startLat,startLng,latitude,longitude);
            distanceCurrentAndEnd = distanceBetweenSetPointAndCurrent(endLat,endLng,latitude,longitude);

            if(distanceCurrentAndStart < 100.00 && !isRacing){
                logUser("You are close enough to begin the race, press and hold on the screen to begin race");
            }
        }
    };


    private Runnable timerTick = new Runnable() {
        @Override
        public void run() {
                start = startRoutePosition;
                end = endRoutePosition;
                shortestPathRunning = true;
                itemizedLayer.addItem(createMarkerItem(startRoutePosition, R.drawable.marker_icon_green));
                itemizedLayer.addItem(createMarkerItem(endRoutePosition, R.drawable.marker_icon_red));
                mapView.map().updateMap(true);
                calcPath(start.getLatitude(), start.getLongitude(), end.getLatitude(),
                        end.getLongitude());
        }
    };


    public static double distanceBetweenSetPointAndCurrent(double setLat, double setLng, double currentLat, double currentLng){
        final int R = 6371;
        double latDistance = Math.toRadians(currentLat - setLat);
        double lonDistance = Math.toRadians(currentLng - setLng);
        double a = Math.sin(latDistance/2)*Math.sin(latDistance/2)
                + Math.cos(Math.toRadians(setLat))*Math.cos(Math.toRadians(currentLat))
                * Math.sin(lonDistance/2)*Math.sin(lonDistance/2);
        double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double distance = R * c * 1000;

        distance = Math.pow(distance,2);

        return Math.sqrt(distance);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this, GpsLocationListener.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mBound){
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hopper != null)
            hopper.close();

        hopper = null;
        System.gc();

        mapView.map().destroy();
    }

    private void doPermissions() {
        // If we haven't been granted the permission to use "fine location"
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION);
            }
        }
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


    boolean isReady() {
        // only return true if already loaded
        if (hopper != null)
            return true;

        if (prepareInProgress) {
            //logUser("Preparation still in progress");
            return false;
        }
        //logUser("Prepare finished but hopper not ready. This happens when there was an error while loading the files");
        return false;
    }

    /**
     * as the files get initialized the current area gets set to the string value of the parameter
     * this method is used in chooseAreaFromLocal with the selected spinner item
     * @param area
     */
    private void initFiles(String area) {
        prepareInProgress = true;
        currentArea = area;
        getFilesFolder();
    }

    /**
     * this picks the files and adds them to the list called nameList
     */
    private void chooseAreaFromLocal() {
        List<String> nameList = new ArrayList<>();
        String[] files = mapsFolder.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename != null
                        && (filename.endsWith(".ghz") || filename
                        .endsWith("-gh"));
            }
        });
        //this adds everything to nameList
        Collections.addAll(nameList, files);

        //if nothing is there do nothing
        if (nameList.isEmpty())
            return;

        //hardcoded beacuase we never need another map than denmark
        initFiles("denmark");

    }

    //getTheFiles
    void getFilesFolder() {
        final File areaFolder = new File(mapsFolder, currentArea + "-gh");
        loadMap(areaFolder);
        return;
    }

    //this loads the map based on which file is selected which is defined by the parameter.
    //TODO: Use an intent to set the starting position and update this continuously
    void loadMap(File areaFolder) {
        logUser("loading map");

        mapView.map().layers().add(new MapEventsReceiver(mapView.map()));

        // Map file source
        MapFileTileSource tileSource = new MapFileTileSource();
        tileSource.setMapFile(new File(areaFolder, currentArea + ".map").getAbsolutePath());
        VectorTileLayer l = mapView.map().setBaseMap(tileSource);
        mapView.map().setTheme(VtmThemes.DEFAULT);
        mapView.map().layers().add(new BuildingLayer(mapView.map(), l));
        mapView.map().layers().add(new LabelLayer(mapView.map(), l));

        // Markers layer
        itemizedLayer = new ItemizedLayer<>(mapView.map(), (MarkerSymbol) null);
        mapView.map().layers().add(itemizedLayer);

        // Map position
        mapView.map().setMapPosition(55.39, 10.38 , 1 << 15);

        //here the map is set to the content view, this is a fairly odd way of doing it but it makes sense when using files.
        setContentView(mapView);
        loadGraphStorage();
    }

    //i have no clue what this does honestly, but it looks like it tries to create the graph
    //TODO: Find out what the fuck this does
    void loadGraphStorage() {
        new GHAsyncTask<Void, Void, Path>() {
            protected Path saveDoInBackground(Void... v) throws Exception {
                GraphHopper tmpHopp = new GraphHopper().forMobile();
                tmpHopp.load(new File(mapsFolder, currentArea).getAbsolutePath() + "-gh");
                log("found graph " + tmpHopp.getGraphHopperStorage().toString() + ", nodes:" + tmpHopp.getGraphHopperStorage().getNodes());
                hopper = tmpHopp;
                return null;
            }

            protected void onPostExecute(Path o) {
                if (hasError()) {
                    logUser("An error happened while creating graph:"
                            + getErrorMessage());
                } else {
                    logUser("Finished loading graph. Long press to define where to start and end the route.");
                }

                finishPrepare();
            }
        }.execute();
    }

    //basic setting method
    private void finishPrepare() {
        prepareInProgress = false;
    }

    //WARNING: DO NOT FUCK WITH THIS METHOD!
    private PathLayer createPathLayer(PathWrapper response) {
        Style style = Style.builder()
                .fixed(true)
                .generalization(Style.GENERALIZATION_SMALL)
                .strokeColor(0x9900cc33)
                .strokeWidth(4 * getResources().getDisplayMetrics().density)
                .build();
        PathLayer pathLayer = new PathLayer(mapView.map(), style);
        List<GeoPoint> geoPoints = new ArrayList<>();
        PointList pointList = response.getPoints();
        for (int i = 0; i < pointList.getSize(); i++)
            geoPoints.add(new GeoPoint(pointList.getLatitude(i), pointList.getLongitude(i)));
        pathLayer.setPoints(geoPoints);
        return pathLayer;
    }

    //drawing markers do not fuck with this stuff. I fucking hate messing up with OpenGL stuff
    //OpenGL method needs to be left the fuck alone
    @SuppressWarnings("deprecation")
    private MarkerItem createMarkerItem(GeoPoint p, int resource) {
        Drawable drawable = getResources().getDrawable(resource);
        Bitmap bitmap = AndroidGraphics.drawableToBitmap(drawable);
        MarkerSymbol markerSymbol = new MarkerSymbol(bitmap, 0.5f, 1);
        MarkerItem markerItem = new MarkerItem("", "", p);
        markerItem.setMarker(markerSymbol);
        return markerItem;
    }

    //calculation of the path, this uses Dijkstra to calculate the path it seems, do not mess with this it should be fine
    //TODO: Find out if we can cut this down a bit, probably not though -- low priority
    public void calcPath(final double fromLat, final double fromLon,
                         final double toLat, final double toLon) {

        log("calculating path ...");
        new AsyncTask<Void, Void, PathWrapper>() {
            float time;

            protected PathWrapper doInBackground(Void... v) {
                StopWatch sw = new StopWatch().start();
                GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon).
                        setAlgorithm(Algorithms.DIJKSTRA_BI);
                req.getHints().
                        put(Routing.INSTRUCTIONS, "true");
                GHResponse resp = hopper.route(req);
                time = sw.stop().getSeconds();
                return resp.getBest();
            }

            protected void onPostExecute(PathWrapper resp) {
                if (!resp.hasErrors()) {

                    log("from:" + fromLat + "," + fromLon + " to:" + toLat + ","
                            + toLon + " found path with distance:" + resp.getDistance()
                            / 1000f + ", nodes:" + resp.getPoints().getSize() + ", time:"
                            + time + " " + resp.getDebugInfo());
                    logUser("the route is " + (int) (resp.getDistance() / 100) / 10f
                            + "km long, time:" + resp.getTime() / 60000f + "min, debug:" + time);

                    pathLayer = createPathLayer(resp);
                    mapView.map().layers().add(pathLayer);
                    mapView.map().updateMap(true);
                } else {
                    logUser("Error:" + resp.getErrors());
                }
                shortestPathRunning = false;
            }
        }.execute();
    }

    /**
     * toast and logging methods
     * TODO: decide if we want these methods, they are fairly pointless
     * @param str
     */
    private void log(String str) {
        Log.i("GH", str);
    }

    //TODO: see above
    private void logUser(String str) {
        log(str);
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    class MapEventsReceiver extends Layer implements GestureListener {
        //makes sure the events are based on the map
        MapEventsReceiver(org.oscim.map.Map map) {
            super(map);
        }

        @Override
        public boolean onGesture(Gesture g, MotionEvent e) {
            if (g instanceof Gesture.LongPress) {
                GeoPoint p = mMap.viewport().fromScreenPoint(e.getX(), e.getY());
                return onLongPress(p);
            }
            return false;
        }
    }

    private boolean onLongPress(GeoPoint p) {
        String idString = "" + startLat + startLng + endLat + endLng + player.getUsername();
        final String id = idString.replace(".", "");
        final User user = player;
        TrackTime trackTime = new TrackTime(id,0,0,user);
        if(trackTimeDatabaseReference.child(id) == null){
            trackTimeDatabaseReference.child(id).setValue(trackTime);
        }

        System.out.println(id);
        if(startLat != 0 &&
                startLng != 0 &&
                latitude != 0 &&
                longitude != 0){
            if(distanceCurrentAndStart < 100.00 && !isRacing){
                //set player.iscurrentlyracing to true
                logUser("The race has begun! Have fun! Take care of yourself");
                //create intent to start watch!
                startTime = System.currentTimeMillis();
                isRacing = true;
                trackTimeDatabaseReference.child(id).child("startTime").setValue(startTime);
                return true;
            }
            if(distanceCurrentAndEnd < 100 && isRacing){
                //set player.iscurrentlyracing to false
                //stop timer and get time since start in milliseconds
                endTime = System.currentTimeMillis();
                trackTimeDatabaseReference.child(id).child("endTime").setValue(endTime);
                long time =  endTime - startTime;
                long seconds = (int) (time/1000) % 60;
                logUser("Your time was recorded to be: " + seconds + " seconds");
                isRacing = false;
                return true;
            }
        }
        return true;
    }
}
