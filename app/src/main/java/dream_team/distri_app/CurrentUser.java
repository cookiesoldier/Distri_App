package dream_team.distri_app;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Kenneth on 21/05/16.
 */
public class CurrentUser {

    static private String firstName;
    static private String lastName;
    static private String userName;
    static private String email;
    static private String password;
    static private List<String> subbedRoomsKeys;



    private CurrentUser(){


    }



    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        CurrentUser.firstName = firstName;
    }



    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        CurrentUser.lastName = lastName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        CurrentUser.password = password;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        CurrentUser.userName = userName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        CurrentUser.email = email;
    }

    public static List<String> getSubbedRoomsKeys() {
        return subbedRoomsKeys;
    }

    public static void setSubbedRoomsKeys(List<String> subbedRoomsKeys) {
        CurrentUser.subbedRoomsKeys = subbedRoomsKeys;
    }


    public  static JSONObject userToJSON() throws JSONException {

       JSONObject user = new JSONObject();

        user.put("USERNAME",getUserName());
        user.put("FIRSTNAME",getFirstName());
        user.put("LASTNAME",getLastName());
        user.put("EMAIL",getEmail());
        user.put("PASSWORD",getPassword());
        user.put("SUBBEDROOMS", getSubbedRoomsKeys());

        return user;
    }



}
