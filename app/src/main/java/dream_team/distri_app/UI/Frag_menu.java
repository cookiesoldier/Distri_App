package dream_team.distri_app.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import dream_team.distri_app.R;

import static dream_team.distri_app.UI.GetSet.*;

public class Frag_menu extends Fragment  implements View.OnClickListener {
    ImageButton btn_add;
    TextView textView;
    GetSet getset;

    private String userName = frag_Login.userName;
    private String sessionKey = frag_Login.sessionKey;

    public static String subRoom;
    public static String r1;

    String room;
    String[] roomListKey =new String[]{};
    String[] roomListeName = new String[] {
            userName
            ,sessionKey
            };

    public Frag_menu(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag_menu, container, false);

        btn_add = (ImageButton) rod.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        textView = (TextView) getActivity().findViewById(R.id.txtVListe);


        //Skulle gerne kaldes her omkkring...

        getset = new GetSet();
        roomListKey = getset.roomListKey;

        Log.d("Key",roomListKey[0].toString());
        /*room = getset.room.toString();
        room = room.replace('[',' ');
        room = room.replace(']', ' ');
        room = room.trim();

/*
        new Thread(new Runnable() {
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("TASK", "getuser");
                        obj.put("USERNAME", userName);
                        obj.put("SESSIONKEY", sessionKey);
                        obj.put("GETNAME",userName);

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
                    Log.d(returnString,returnString);
                    JSONObject answer = new JSONObject(returnString);

                    JSONObject answerRoom = new JSONObject(answer.get("USER").toString());

                    room = answerRoom.get("SUBBEDROOMS").toString();


                    Log.d("ReturnMessageROOM:", returnString);

                    room = room.replace('[',' ');
                    room = room.replace(']', ' ');
                    room = room.trim();

                    if (answer.get("REPLY").equals("succes")) {
                        Log.d(returnString,returnString);
                        Log.d("room",room);

                        roomListKey = room.split(",");
                        r1 = roomListKey[0];
                        Log.d("R1",r1);


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                            try {
                            JSONObject objGetRoomName = new JSONObject();
                                objGetRoomName.put("TASK", "getroom");
                                objGetRoomName.put("USERNAME", userName);
                                objGetRoomName.put("SESSIONKEY", sessionKey);
                                objGetRoomName.put("ROOMKEY", r1);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                                }
                            });

                        String combinedMessage2 = obj.toString();
                        Log.d("CombinedMessage", combinedMessage2);
                        //http://developer.android.com/reference/java/net/HttpURLConnection.html
                        connection.setDoOutput(true);
                        //i would like to PUT
                        connection.setRequestMethod("PUT");
                        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                        out.write(combinedMessage);
                        out.close();

                        BufferedReader in2 = new BufferedReader(new InputStreamReader(connection.getInputStream()));


                        String returnStringRoomName = "";
                        returnString = in2.readLine();
                        Log.d(returnString,returnString);
                        JSONObject answerRoomName = new JSONObject(returnStringRoomName);

                        Log.d("ReturnMessageROOM:", returnStringRoomName);

                        if (answerRoomName.get("REPLY").equals("succes")){
                           String roomName = answerRoomName.get("ROOM").toString();
                            Log.d("r1 ROOMNAME",roomName);
                        }


                        Toast.makeText(getActivity(), "Liste of rooms  " , Toast.LENGTH_SHORT).show();


                    } else if(answer.get("REPLY").equals("failed")){
                        Toast.makeText(getActivity(), " cant get the list " , Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(getActivity(), "fejl 40" , Toast.LENGTH_SHORT).show();
                    }
                    Log.d("ReturnMessage:", returnString);
                    in.close();




                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }

            }

        }).start();

*/

        final ArrayAdapter<String> roomListAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, roomListeName);

        ListView lv = (ListView)rod.findViewById(R.id.list);
        lv.setAdapter(roomListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragWindow, new Frag_room())
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(getActivity(), "Liste click Frag_room " + roomListAdapter.getItem(position),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return rod;
    }



    public void onClick(View v){
        if(v == btn_add){
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragWindow, new Frag_CreateRoom())
                    .addToBackStack(null)
                    .commit();
           Toast.makeText(getActivity(), "Create Room ",
              Toast.LENGTH_LONG).show();


        }

    }

}
