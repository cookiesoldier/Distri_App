package dream_team.distri_app;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NaelRashdeen on 21/05/16.
 */
public class CurrentRoom {

    static private String title;
    static private String roomKey;
    static private String type;
    static private String owner;
    static private List<String> eventKeyList;


    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        CurrentRoom.title = title;
    }

    public static String getRoomKey() {
        return roomKey;
    }

    public static void setRoomKey(String roomKey) {
        CurrentRoom.roomKey = roomKey;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        CurrentRoom.type = type;
    }

    public static String getOwner() {
        return owner;
    }

    public static void setOwner(String owner) {
        CurrentRoom.owner = owner;
    }

    public static List<String> getEventKeyList() {
        return eventKeyList;
    }

    public static void setEventKeyList(List<String> eventKeyList) {
        CurrentRoom.eventKeyList = eventKeyList;
    }


    public static JSONObject getRoomJSON(){


        return null;
    }



}
