package dream_team.distri_app;

/**
 * Created by Kenneth on 21/05/16.
 */
public class CurrentLogin {
    static private String userName;
    static private String passWord;
    static private String sessionKey;


    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        CurrentLogin.userName = userName;
    }

    public static String getPassWord() {
        return passWord;
    }

    public static void setPassWord(String passWord) {
        CurrentLogin.passWord = passWord;
    }

    public static String getSessionKey() {
        return sessionKey;
    }

    public static void setSessionKey(String sessionKey) {
        CurrentLogin.sessionKey = sessionKey;
    }


}
