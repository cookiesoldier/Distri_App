package dream_team.distri_app.UI;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import java.util.concurrent.atomic.AtomicBoolean;

import dream_team.distri_app.R;

public class Frag_menu extends Fragment  implements View.OnClickListener {
    ImageButton btn_add;
    TextView textView;
    GetSet getset;
    final AtomicBoolean guiUpdaterBool = new AtomicBoolean(false);
    private String userName = frag_Login.userName;
    private String sessionKey = frag_Login.sessionKey;
    private ProgressDialog progress;
    public static String subRoom,r1;
    private JSONObject myUser,myRoom;
    String roomKeyString,keyString;

    String[] roomKeyList =new String[]{};
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
        showLoadingDialog();
        getset = new GetSet();
        GetUser getUser = (GetUser) new GetUser().execute(userName);
       // GetRoom getRoom = (GetRoom) new GetRoom().execute(userName);

        Thread guiUpdate = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(guiUpdaterBool.get()){
                        //update GUI method
                        Log.d("MyUser from server", myUser.toString());
                        //kalder metode værk som opdaterer felter.



                        guiUpdaterBool.set(false);
                        dismissLoadingDialog();

                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("Run Run", "while loop");

                }
            }
        });
        guiUpdate.start();







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

    public void showLoadingDialog() {

        if (progress == null) {
            progress = new ProgressDialog(getActivity());
            progress.setTitle("Loading");
            progress.setMessage("Wait while Loading...");
        }
        progress.show();
    }

    public void dismissLoadingDialog() {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    private class GetUser extends AsyncTask<String, Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            try {


                JSONObject obj = new JSONObject();
                try {
                    obj.put("TASK", "getuser");
                    obj.put("USERNAME", userName);
                    obj.put("SESSIONKEY", sessionKey);
                    obj.put("GETNAME",params[0]);

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
                Log.d("ReturnStringUSER", returnString);
                JSONObject answer = new JSONObject(returnString);
                Log.d("preMethodDoneUSER", answer.toString());

                if(answer.get("REPLY").equals("succes")){
                    Log.d("SvarFraServerUSER",answer.toString());
                    JSONObject answerRoom = new JSONObject(answer.get("USER").toString());
                    roomKeyString = answerRoom.get("SUBBEDROOMS").toString();
                    roomKeyString = roomKeyString.replace('[', ' ');
                    roomKeyString = roomKeyString.replace(']', ' ');
                    roomKeyString = roomKeyString.trim();
                    roomKeyList = roomKeyString.split(",");
                    Log.d("Key 1", roomKeyList[0]);


                }

                return answer;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exception", e.toString());
            }


            return null;
        }
        @Override
        protected void onPostExecute(JSONObject result){
            Log.d("ServerSvarUSER",result.toString());
            myUser = result;
            guiUpdaterBool.set(true);

        }
    }

/*
    private class GetRoom extends AsyncTask<String, Void,JSONObject>{


        @Override
        protected JSONObject doInBackground(String... params) {
            try {


                JSONObject obj = new JSONObject();
                try {
                    obj.put("TASK", "getroom");
                    obj.put("USERNAME", userName);
                    obj.put("SESSIONKEY", sessionKey);
                    obj.put("ROOMKEY",roomKeyList[0]);

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
                Log.d("ReturnStringROOM", returnString);
                JSONObject answer = new JSONObject(returnString);
                Log.d("preMethodDoneROOM", answer.toString());

                if(answer.get("REPLY").equals("succes")){
                    Log.d("SvarFraServerROOM",answer.toString());



                }

                return answer;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exception", e.toString());
            }


            return null;
        }
        @Override
        protected void onPostExecute(JSONObject result){
            Log.d("ServerSvarROOM",result.toString());
            myRoom = result;
            guiUpdaterBool.set(true);

        }
    }
    */
}
