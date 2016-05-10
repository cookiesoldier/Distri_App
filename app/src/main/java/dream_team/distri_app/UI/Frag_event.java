package dream_team.distri_app.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import dream_team.distri_app.R;

public class Frag_event extends Fragment implements View.OnClickListener {

    ImageButton btn_addEvent;

    ProgressDialog progress;
    private String sessionKey = frag_Login.sessionKey;
    private String userName = frag_Login.userName;
    String eventkey;

    public Frag_event() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag_event,container,false);

        btn_addEvent = (ImageButton) rod.findViewById(R.id.btn_addEvent);
        btn_addEvent.setOnClickListener(this);

        return rod;
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

    public void ListEvents(){
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dismissLoadingDialog();
            }
        }, 5000);

            showLoadingDialog();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        URL url = new URL("http://52.58.112.107:8080/HelpingTeacherServer2/HTSservlet");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("TASK", "getevent");
                            obj.put("SESSIONKEY", sessionKey);
                            obj.put("USERNAME", userName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String combinedMessage = obj.toString();
                        Log.d("GeteventCombined", combinedMessage);
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
                        JSONObject event = new JSONObject(answer.get("EVENT").toString());

                        if (answer.get("REPLY").equals("succes")){
                            eventkey = event.get("EVENTKEYS").toString();
                            userName = answer.get("USERNAME").toString();
                            sessionKey = answer.get("SESSIONKEY").toString();
                            Log.d("ReturnMessage:", returnString);
                            Log.d(answer.get("EVENT").toString(), "event");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), "Event Created", Toast.LENGTH_LONG).show();
                                }
                            });

                        } else if(answer.get("REPLY").equals("failed")){
                            Log.d("ReturnMessage:", returnString);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), "Event not Created", Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                        }

                        Log.d(returnString, "");


                    } catch (Exception e) {
                        Log.d("Exception", e.toString());
                    }

                }

            }).start();

        }

    }

