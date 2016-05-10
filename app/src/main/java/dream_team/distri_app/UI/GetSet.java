package dream_team.distri_app.UI;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;


public class GetSet {

    private String userName = frag_Login.userName;
    private String sessionKey = frag_Login.sessionKey;

    public static String subRoom,r1;

    String room;
    String[] roomListKey = new String[]{};
    String[] roomListeName = new String[]{
            userName
            , sessionKey
    };


    public JSONObject getUser(final String answerGetUser) {

        final AtomicBoolean done = new AtomicBoolean(false);
        final JSONObject foundUser = new JSONObject();

        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://52.58.112.107:8080/HelpingTeacherServer2/HTSservlet");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("TASK", "getuser");
                        obj.put("USERNAME", userName);
                        obj.put("SESSIONKEY", sessionKey);
                        obj.put("GETNAME", answerGetUser);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String combinedMessage = obj.toString();
                    Log.d("CombinedMessage", combinedMessage);
                    //http://developer.android.com/reference/java/net/HttpURLConnection.html
                    connection.setDoOutput(true);
                    //i would like to PUT
                    connection.setRequestMethod("PUT");
                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                    out.write(combinedMessage);
                    out.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String returnString = "";
                    returnString = in.readLine();
                    Log.d(returnString, "");
                    JSONObject answerGetUser = new JSONObject(returnString);


                    JSONObject answerRoom = new JSONObject(answerGetUser.get("USER").toString());

                    room = answerRoom.get("SUBBEDROOMS").toString();
                    Log.d("ReturnMessage2:", returnString);

                    answerGetUser.get(returnString);

                    room = room.replace('[',' ');
                    room = room.replace(']', ' ');
                    room = room.trim();

                    if (answerGetUser.get("REPLY").equals("succes")) {
                        Log.d(returnString, returnString);
                        Log.d("room", room);

                        roomListKey = room.split(",");
                        r1 = roomListKey[0];
                        Log.d("R1",r1);

                        done.set(true);

                    }
                    //else if (answerGetUser.get("REPLY").equals("failed")) {
                    //    Toast.makeText()

                    //}


                    } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Exception", e.toString());
                }

            }

        }).start();

        while (!done.get()) {

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    return foundUser;
    }


public JSONObject getUserV2( final String username){
    final JSONObject[] returnedAnswer = new JSONObject[1];
    final AtomicBoolean done = new AtomicBoolean(false);

    new AsyncTask() {
        @Override
        protected Object doInBackground(Object... executeParametre) {

            try {


            JSONObject obj = new JSONObject();
            try {
                obj.put("TASK", "getuser");
                obj.put("USERNAME", userName);
                obj.put("SESSIONKEY", sessionKey);
                obj.put("GETNAME", username);

            } catch (JSONException e) {
                e.printStackTrace();
            }
                String combinedMessage = "?logininfo=" + obj.toString();
                Log.d("CombinedMessage", combinedMessage);
                URL url = new URL("http://52.58.112.107:8080/HelpingTeacherServer2/HTSservlet"+combinedMessage);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();


                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String returnString = "";
                returnString = in.readLine();
                Log.d(returnString, returnString);
                JSONObject answer = new JSONObject(returnString);
                Log.d("preMethodDone", answer.toString());
                return answer;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }


            return "færdig!";  // <5>
        }


        @Override
        protected void onProgressUpdate(Object... progress) {

        }

        @Override
        protected void onPostExecute(Object result) {
            Log.d("ServerSvar",result.toString());
            returnedAnswer[0] = (JSONObject) result;
            done.set(true);


        }
    }.execute();

    return returnedAnswer[0];
}


}