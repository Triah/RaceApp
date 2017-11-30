package case1.groupg.raceapp;

import java.util.List;

/**
 * Created by Nicolai on 29-11-2017.
 */

public class Track {

    float latitudeStart;
    float length;
    float latitudeEnd;
    float longitudeStart;
    float longitudeEnd;
    List<String> usersWhichHaveCompleted;

    public Track(float latitudeStart, float length, float latitudeEnd, float longitudeStart, float longitudeEnd, List<String> usersWhichHaveCompleted) {
        this.latitudeStart = latitudeStart;
        this.length = length;
        this.latitudeEnd = latitudeEnd;
        this.longitudeStart = longitudeStart;
        this.longitudeEnd = longitudeEnd;
        this.usersWhichHaveCompleted = usersWhichHaveCompleted;
    }

    public float getLatitudeStart() {
        return latitudeStart;
    }

    public float getLength() {
        return length;
    }

    public float getLatitudeEnd() {
        return latitudeEnd;
    }

    public float getLongitudeStart() {
        return longitudeStart;
    }

    public float getLongitudeEnd() {
        return longitudeEnd;
    }

    public List<String> getUsersWhichHaveCompleted() {
        return usersWhichHaveCompleted;
    }
}
