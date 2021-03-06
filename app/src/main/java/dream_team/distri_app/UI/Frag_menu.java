package dream_team.distri_app.UI;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import dream_team.distri_app.CurrentLogin;
import dream_team.distri_app.CurrentUser;
import dream_team.distri_app.CurrentRoom;
import dream_team.distri_app.R;

public class Frag_menu extends Fragment  implements View.OnClickListener {
    ImageButton btn_add;
    TextView textView;

    final AtomicBoolean guiUpdaterBool = new AtomicBoolean(false);
;
    private ProgressDialog progress;
    private JSONObject myUser;
    public static List<JSONObject> myRooms = new ArrayList<>();
    public static int keyRoomNumber;
    String roomKeyString;
    ArrayAdapter<String> roomListAdapter = null;
    List<String> roomNameList = new ArrayList<String>();



    public Frag_menu(){
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        myRooms.clear();
        roomNameList.clear();
        roomListAdapter.notifyDataSetChanged();
        roomListAdapter.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag_menu, container, false);

        btn_add = (ImageButton) rod.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        textView = (TextView) getActivity().findViewById(R.id.txtVListe);
        //Skulle gerne kaldes her omkkring...
        final Thread threadUpdate = updateMainThread();


        //RoomlistAdaptet for listViewet som vises i fragment.
        roomListAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, roomNameList);
        roomListAdapter.clear();
        ListView lv = (ListView)rod.findViewById(R.id.list);
        lv.setAdapter(roomListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                JSONObject currentRoom = myRooms.get(position);

                    CurrentRoom.setEventKeyList(createListEventKeys(currentRoom));

                try {
                    CurrentRoom.setOwner(currentRoom.get("OWNER").toString());
                    CurrentRoom.setRoomKey(currentRoom.get("ROOMKEY").toString());
                    CurrentRoom.setTitle(currentRoom.get("TITLE").toString());
                    CurrentRoom.setType(currentRoom.get("TYPE").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragWindow, new Frag_event())
                        .addToBackStack(null)
                        .commit();


                Toast.makeText(getActivity(), "Liste click Frag_room " + roomListAdapter.getItem(position),
                        Toast.LENGTH_SHORT).show();
                threadUpdate.interrupt();
                keyRoomNumber = position;
                Log.d("ROD ____--<<",""+myRooms.get(keyRoomNumber));
            }
        });
        return rod;
    }

    @NonNull
    private Thread updateMainThread() {
        showLoadingDialog();
        GetUser getUser = (GetUser) new GetUser().execute(CurrentLogin.getUserName());

        final Thread guiUpdate = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(guiUpdaterBool.get()){
                        //update GUI method
                        Log.d("MyUser from server", myUser.toString());
                        //kalder metode værk som opdaterer felter.

                        Log.d("myRooms size", " "+myRooms.size());


                            try {
                                updateGUImethod();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                       guiUpdaterBool.set(false);
                        dismissLoadingDialog();

                    }

                   // Log.d("Run Run", "while loop");

                }
            }
        });


        guiUpdate.start();
        return guiUpdate;
    }

    private List<String> createListEventKeys(JSONObject subbedrooms) {


        String eventKeyString = subbedrooms.toString();
        //Checker for om den er null, eller hvis brugeren ikke har nogle rum. Dog Kan den godt ha en størrelse på 2 da der kommer [] med fra serveren selv om den ikke rindeholder keys
        if (eventKeyString.equals(null) || eventKeyString.length() < 3) {
            Log.d("NO EVENT","No EVENT");
        } else {

            //Fjerne og ([),(]),(")
            eventKeyString = eventKeyString.replace('[', '_');
            eventKeyString = eventKeyString.replace(']', '_');
            eventKeyString = eventKeyString.replace('"', '_');
            eventKeyString = eventKeyString.replace('\\', '_');
            eventKeyString = eventKeyString.replaceAll("_", "");
            //splitter på , for at få de enkelte keys til de enkelte room.
            //eventKeyList = Arrays.asList(eventKeyString.split(","));

        }
        return  Arrays.asList(eventKeyString.split(","));
    }

    private void updateGUImethod() throws JSONException {

        populateUILists();

    }

    private void updateFragment(){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roomListAdapter.notifyDataSetChanged();
            }
        });




    }

    private void populateUILists() throws JSONException {
        roomNameList.clear();
        for (JSONObject u : myRooms){

            Log.d("myrooms size -->", myRooms.size() + "");
            String title = u.get("TITLE").toString();
            roomNameList.add(title);


        }

        updateFragment();
    }

    private void currentUserSet(JSONObject myUser) throws JSONException {

        JSONObject answerRoom = new JSONObject(myUser.get("USER").toString());

        roomKeyString = answerRoom.get("SUBBEDROOMS").toString();
        //Checker for om den er null, eller hvis brugeren ikke har nogle rum. Dog Kan den godt ha en størrelse på 2 da der kommer [] med fra serveren selv om den ikke rindeholder keys
        if (roomKeyString.equals(null) || roomKeyString.length() < 3) {
            Log.d("NO Room","No Room");
        } else {

            //Fjerne og ([),(]),(")
            roomKeyString = roomKeyString.replace('[', '_');
            roomKeyString = roomKeyString.replace(']', '_');
            roomKeyString = roomKeyString.replace('"', '_');
            roomKeyString = roomKeyString.replace('\\', '_');
            roomKeyString = roomKeyString.replaceAll("_", "");
            roomKeyString = roomKeyString.replaceAll(" ","");
            //splitter på , for at få de enkelte keys til de enkelte room.
            ArrayList<String> list = new ArrayList<>(Arrays.asList(roomKeyString.split(",")));
            CurrentUser.setSubbedRoomsKeys(list);

            CurrentUser.setFirstName(answerRoom.get("FIRSTNAME").toString());
            CurrentUser.setLastName(answerRoom.get("LASTNAME").toString());
            CurrentUser.setUserName(answerRoom.get("USERNAME").toString());
            CurrentUser.setEmail(answerRoom.get("EMAIL").toString());
            CurrentUser.setPassword(answerRoom.get("PASSWORD").toString());

        }
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

    private class GetUser extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            try {


                JSONObject obj = new JSONObject();
                try {
                    obj.put("TASK", "getuser");
                    obj.put("USERNAME", CurrentLogin.getUserName());
                    obj.put("SESSIONKEY", CurrentLogin.getSessionKey());
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
                JSONObject answerUser = new JSONObject(returnString);
                Log.d("preMethodDoneUSER", answerUser.toString());



                return answerUser;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exception", e.toString());
            }


            return null;
        }
        @Override
        protected void onPostExecute(JSONObject result){
            Log.d("ServerSvarUSER", result.toString());
            try {
                if(result.get("REPLY").toString().equals("succes")){
                    myUser = result;

                    currentUserSet(myUser);
                       myRooms.clear();
                    if(CurrentUser.getSubbedRoomsKeys().size() != 0) {
                        Log.d("room name", "" + CurrentUser.getSubbedRoomsKeys().size());
                        Log.d("room name", " --->  "+CurrentUser.getSubbedRoomsKeys().get(0));

                        for (String u : CurrentUser.getSubbedRoomsKeys()) {

                                if(u != null){
                                    GetRoom getRoom = (GetRoom) new GetRoom().execute(u);

                                Log.d("RoomKeyList ! Tom", "!Tom");
                                }
                        }
                    }
                    guiUpdaterBool.set(true);

                }else{
                    if(result.get("MESSAGE").toString().equals("???")){
                        //logggedout?
                        //Error?
                        //whatever
                        //Skift fragment??
                    }
                    //toast


                    dismissLoadingDialog();
                    //andre ting
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private class GetRoom extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {

                try {


                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("TASK", "getroom");
                        obj.put("USERNAME", CurrentLogin.getUserName());
                        obj.put("SESSIONKEY", CurrentLogin.getSessionKey());
                        obj.put("ROOMKEY", params[0]);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String combinedMessage = "?logininfo=" + obj.toString();
                    Log.d("CombinedMessage", combinedMessage);
                    URL url = new URL("http://52.58.112.107:8080/HelpingTeacherServer2/HTSservlet" + combinedMessage);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();


                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String returnString = "";
                    returnString = in.readLine();
                    Log.d("ReturnStringROOM", returnString);
                    JSONObject answerRoom = new JSONObject(returnString);
                    JSONObject room = new JSONObject(answerRoom.get("ROOM").toString());

                    Log.d("preMethodDoneROOM", answerRoom.toString());
                    return room;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Exception", e.toString());
                }



            return null;
        }
        @Override
        protected void onPostExecute(JSONObject result){
            Log.d("ServerSvarROOM", result.toString());
            myRooms.add(result);
            guiUpdaterBool.set(true);

        }
    }

}
