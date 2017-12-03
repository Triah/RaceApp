package case1.groupg.raceapp;

/**
 * Created by Nicolai on 03-12-2017.
 */

public class TrackTime {

    public String id;
    public long startTime;
    public long endTime;
    public User user;

    public TrackTime(String id, long startTime, long endTime, User user) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public User getUser() {
        return user;
    }
}
