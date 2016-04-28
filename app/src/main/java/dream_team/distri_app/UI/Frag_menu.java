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
import java.net.HttpURLConnection;
import java.net.URL;

import dream_team.distri_app.R;

public class Frag_menu extends Fragment implements View.OnClickListener{
    ImageButton btn_add;
    TextView textView;

    private String userName = frag_Login.userName;
    private String sessionKey = frag_Login.sessionKey;

    String room;
    String[] roomListe = new String[] {
            userName
            ,sessionKey
            //,room
            ,"Donut"
            ,"Eclair"
            ,"Froyo"
            ,"Gingerbread"
            ,"Honeycomb"
            ,"Ice Cream SandWich"
            ,"Jelly Bean"
            ,"KitKat"
            ,"1"
            ,"2"
            ,"3"
            ,"4"
            ,"mac"
            ,"Windows"
            ,"linux"
            ,"abe"
            ,"kage"
                };

    public Frag_menu(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag_menu, container, false);

        btn_add = (ImageButton) rod.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        textView = (TextView) getActivity().findViewById(R.id.textViewListe);

        new Thread(new Runnable() {
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("TASK", "getroom");
                        obj.put("USERNAME", userName);
                        obj.put("SESSIONKEY", sessionKey);

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

                    if (answer.get("REPLY").equals("succes")) {
                        room = answer.get("ROOM").toString();
                        Log.d(returnString,returnString);
                    }
                    Log.d("ReturnMessage:", returnString);
                    in.close();



                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }

            }

        }).start();


        final ArrayAdapter<String> roomListAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, roomListe);

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
              Toast.LENGTH_SHORT).show();


        }

    }

}
