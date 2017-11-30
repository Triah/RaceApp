package case1.groupg.raceapp;

/**
 * Created by Nicolai on 29-11-2017.
 */

public class User {

    String ID;
    String username;
    String password;
    String email;
    /*int experiencePoints;
    int avatarID;*/

    public User(String ID, String username, String email, String password){
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.email = email;
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

   /*public int getExperiencePoints() {
        return experiencePoints;
    }*/


}
