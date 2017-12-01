package case1.groupg.raceapp;

/**
 * Created by Nicolai on 29-11-2017.
 */

public class User {
    String ID;
    String username;
    String password;
    String email;
    int experiencePoints = 0;
//    int avatarID;

    // No args constructor for FireBase
    public User(){}

    // Used to create new user
    public User(String ID, String username, String email, String password){
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Used to load user from database
    public User(String ID, String username, String email, String password, int experiencePoints){
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.experiencePoints = experiencePoints;
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
}
