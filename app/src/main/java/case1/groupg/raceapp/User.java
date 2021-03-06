package case1.groupg.raceapp;

import java.util.ArrayList;

/**
 * Created by Nicolai on 29-11-2017.
 */

public class User {
    String id;
    String username;
    String password;
    String email;
    int experiencePoints = 0;
    boolean currentlyRacing;
    Track currentTrack;

    int bikeID = R.drawable.bike_1;

    private class TrackTime {
        Track track;
        long time;

        public TrackTime(Track track, long time) {
            this.track = track;
            this.time = time;
        }
    }

    ArrayList<TrackTime> trackTimes = new ArrayList<>();
    ArrayList<Track> racedTracks = new ArrayList<>();

    // No args constructor for FireBase
    public User(){}

    // Used to create new user
    public User(String id, String username, String email, String password, boolean currentlyRacing, Track currentTrack){
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.currentlyRacing = currentlyRacing;
        this.currentTrack = currentTrack;
    }

    // Used to load user from database
    public User(String ID, String username, String email, String password, int experiencePoints, boolean currentlyRacing, Track currentTrack){
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.experiencePoints = experiencePoints;
        this.currentlyRacing = currentlyRacing;
        this.currentTrack = currentTrack;
    }

    public String getId() {
        return id;
    }

    public boolean isCurrentlyRacing() {
        return currentlyRacing;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public void addExperiencePoint(int metersTravelled, int multiplier){
        experiencePoints += (metersTravelled * multiplier);
    }

    public void addTrackTime(Track track, long time){
        trackTimes.add(new TrackTime(track, time));
    }

    public void addRacedTrack(Track track){
        racedTracks.add(track);
    }

    public int getBikeID() {
        return bikeID;
    }

    public void setBikeID(int bikeID) {
        this.bikeID = bikeID;
    }
}
