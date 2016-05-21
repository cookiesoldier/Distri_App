package dream_team.distri_app.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import dream_team.distri_app.CurrentLogin;
import dream_team.distri_app.CurrentRoom;
import dream_team.distri_app.R;

public class Frag_event extends Fragment implements View.OnClickListener {

    ImageButton btn_addEvent;
    final AtomicBoolean guiUpdaterBool = new AtomicBoolean(false);
    ProgressDialog progress;
    String eventkey;
    String eventKeyString;

    ArrayAdapter<String> eventListAdapter;
    public static List<JSONObject> myEvents = new ArrayList<>();
    List<String> eventNameList = new ArrayList<String>();

    public Frag_event() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag_event,container,false);

        btn_addEvent = (ImageButton) rod.findViewById(R.id.btn_addEvent);
        btn_addEvent.setOnClickListener(this);
    Log.d("fragevent Created","<------------");


        GetRoom getRoom = (GetRoom) new GetRoom().execute(CurrentRoom.getRoomKey());

        final Thread guiUpdate = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(guiUpdaterBool.get()){
                        //update GUI method



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







        eventListAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, eventNameList);
        eventListAdapter.clear();
        ListView lv = (ListView)rod.findViewById(R.id.list);
        lv.setAdapter(eventListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragWindow, new Frag_room())
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(getActivity(), "Liste click Frag_room " + eventListAdapter.getItem(position),
                        Toast.LENGTH_SHORT).show();
                guiUpdate.interrupt();


            }
        });

        return rod;
    }

    private void updateGUImethod() throws JSONException {

        populateUILists();

    }

    private void updateFragment(){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                eventListAdapter.notifyDataSetChanged();
            }
        });




    }

    private void populateUILists() throws JSONException {
        eventNameList.clear();

        for (JSONObject u : myEvents){

            Log.d("myrooms size -->", myEvents.size() + "");
            String title = u.get("TITLE").toString();
            eventNameList.add(title);


        }

        updateFragment();
    }

    public void onClick(View v) {
        if (v == btn_addEvent) {
            Log.d("skift til create event","");
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragWindow, new Frag_CreateEvent())
                    .addToBackStack(null)
                    .commit();
            Toast.makeText(getActivity(), "Create Event ",
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

    private void createEventList(JSONObject myEvent) throws JSONException {

        JSONObject answerEvent = new JSONObject(myEvent.get("ROOM").toString());
        eventKeyString = answerEvent.get("EVENTKEYS").toString();
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
            CurrentRoom.setEventKeyList(Arrays.asList(eventKeyString.split(",")));
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
                    obj.put("ROOMKEY",params[0]);

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
                Log.d("ReturnStringRoom", returnString);
                JSONObject answerUser = new JSONObject(returnString);
                Log.d("preMethodDoneROOMJSON", answerUser.toString());



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


                    createEventList(result);
                    //myRooms.clear();
                    if(CurrentRoom.getEventKeyList().size() != 0) {
                        Log.d("room name", "" + CurrentRoom.getEventKeyList().size());
                        Log.d("room name", " --->  " + CurrentRoom.getEventKeyList().get(0));

                        for (String u : CurrentRoom.getEventKeyList()) {

                            if(u != null){
                                GetEvent getEvent = (GetEvent) new GetEvent().execute(u);

                                Log.d("EventKey list ! Tom", "!Tom");
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

    private class GetEvent extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            try {


                JSONObject obj = new JSONObject();
                try {
                    obj.put("TASK", "getevent");
                    obj.put("USERNAME", CurrentLogin.getUserName());
                    obj.put("SESSIONKEY", CurrentLogin.getSessionKey());
                    obj.put("EVENTKEY", params[0]);

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
                Log.d("ReturnStringEVENT", returnString);
                JSONObject answerEvent = new JSONObject(returnString);
                JSONObject event = new JSONObject(answerEvent.get("EVENT").toString());

                Log.d("preMethodDoneEVENT", answerEvent.toString());
                return event;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exception", e.toString());
            }



            return null;
        }
        @Override
        protected void onPostExecute(JSONObject result){
            Log.d("ServerSvarEvent", result.toString());
            myEvents.add(result);
            guiUpdaterBool.set(true);

        }
    }



    }

