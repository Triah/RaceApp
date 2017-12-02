package case1.groupg.raceapp;

import java.util.List;

/**
 * Created by Nicolai on 29-11-2017.
 */

public class Track {

    double latitudeStart;
    double length;
    double latitudeEnd;
    double longitudeStart;
    double longitudeEnd;
    List<String> usersWhichHaveCompleted;

    public Track(double latitudeStart, double length, double latitudeEnd, double longitudeStart, double longitudeEnd, List<String> usersWhichHaveCompleted) {
        this.latitudeStart = latitudeStart;
        this.length = length;
        this.latitudeEnd = latitudeEnd;
        this.longitudeStart = longitudeStart;
        this.longitudeEnd = longitudeEnd;
        this.usersWhichHaveCompleted = usersWhichHaveCompleted;
    }

    public double getLatitudeStart() {
        return latitudeStart;
    }

    public double getLength() {
        return length;
    }

    public double getLatitudeEnd() {
        return latitudeEnd;
    }

    public double getLongitudeStart() {
        return longitudeStart;
    }

    public double getLongitudeEnd() {
        return longitudeEnd;
    }

    public List<String> getUsersWhichHaveCompleted() {
        return usersWhichHaveCompleted;
    }
}
