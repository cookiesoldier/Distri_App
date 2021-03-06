package dream_team.distri_app.UI;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.acl.Owner;
import java.util.Timer;
import java.util.TimerTask;

import dream_team.distri_app.CurrentLogin;
import dream_team.distri_app.CurrentUser;
import dream_team.distri_app.R;

public class Frag_CreateRoom extends Fragment implements View.OnClickListener{

    Button btnSubmitRoom;
    EditText edtRoomName, edtType;

    String roomKey;

    private ProgressDialog progress;

    public Frag_CreateRoom() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag_create_room, container, false);

        btnSubmitRoom = (Button) rod.findViewById(R.id.btnSubmitRoom);

        edtRoomName =(EditText) rod.findViewById(R.id.edtRoomName);
        edtType = (EditText) rod.findViewById(R.id.edtPublicPrivate);


        btnSubmitRoom.setOnClickListener(this);
        return rod;

    }


    public void onClick(View v) {

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dismissLoadingDialog();
            }
        }, 5000);

        if (v == btnSubmitRoom) {
            showLoadingDialog();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        URL url = new URL("http://52.58.112.107:8080/HelpingTeacherServer2/HTSservlet");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("TASK", "CREATEROOM");
                            obj.put("TITLE", edtRoomName.getText().toString());
                            obj.put("OWNER", CurrentLogin.getUserName());
                            obj.put("TYPE", edtType.getText().toString());
                            obj.put("SESSIONKEY", CurrentLogin.getSessionKey());
                            obj.put("USERNAME",CurrentLogin.getUserName());

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

                        Log.d("ReturnMessage:", returnString);
                        JSONObject answer = new JSONObject(returnString);
                        JSONObject room = new JSONObject(answer.get("ROOM").toString());

                        if (answer.get("REPLY").equals("succes")){
                            roomKey = room.get("ROOMKEY").toString();
                            Log.d("ROOMKEY", roomKey);

                            if (answer.get("REPLY").equals("succes")) {

                                CurrentUser.getSubbedRoomsKeys().add(roomKey);
                                new UpdateUser().execute();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "Room is made", Toast.LENGTH_LONG).show();
                                    }

                                });



                            } else if(answer.get("REPLY").equals("failed")){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "Room not made", Toast.LENGTH_LONG).show();
                                    }

                                });
                            }



                        } else if(answer.get("REPLY").equals("failed")){
                            Log.d("ReturnMessage:", returnString);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), "Cant make the room", Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                        }

                        Log.d(returnString, "");


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Exception", e.toString());
                    }

                }

            }).start();

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

    private class UpdateUser extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                URL url = new URL("http://52.58.112.107:8080/HelpingTeacherServer2/HTSservlet");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                JSONObject obj = new JSONObject();
                try {
                    obj.put("TASK", "UPDATEUSER");
                    obj.put("SESSIONKEY", CurrentLogin.getSessionKey());
                    obj.put("USERNAME", CurrentLogin.getUserName());
                    obj.put("PASSWORD", CurrentUser.getPassword());
                    obj.put("LASTNAME",CurrentUser.getLastName());
                    obj.put("FIRSTNAME", CurrentUser.getFirstName());
                    obj.put("EMAIL", CurrentUser.getEmail());
                    obj.put("SUBBEDROOMS", CurrentUser.getSubbedRoomsKeys());

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
                JSONObject answerUpdateUser = new JSONObject(returnString);

                Log.d(returnString, "");

                return answerUpdateUser;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exception", e.toString());
            }


            return null;
        }
        @Override
        protected void onPostExecute(JSONObject result){
            Log.d("ServerSvarUSER", result.toString());
            dismissLoadingDialog();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragWindow, new Frag_menu())
                    .addToBackStack(null)
                    .commit();

        }
    }

}