package dream_team.distri_app.UI;

import android.app.ProgressDialog;
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
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import dream_team.distri_app.R;


public class Frag_CreateEvent extends Fragment implements View.OnClickListener {

    Button btnSubmitEvent;
    EditText edtTitle;

    private ProgressDialog progress;
    private String sessionKey = frag_Login.sessionKey;
    private String username = frag_Login.userName;
    String eventKey;

    Timestamp stamp = new Timestamp(System.currentTimeMillis());
    Date date = new Date(stamp.getTime());


    public Frag_CreateEvent() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag__create_event, container, false);

        btnSubmitEvent = (Button) rod.findViewById(R.id.btnSubmitEvent);
        btnSubmitEvent.setOnClickListener(this);
        edtTitle = (EditText) rod.findViewById(R.id.edtTitle);

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

        if (v == btnSubmitEvent) {
            showLoadingDialog();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        URL url = new URL("http://52.58.112.107:8080/HelpingTeacherServer2/HTSservlet");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("TASK", "CREATEEVENT");
                            obj.put("CREATOR", username );
                            obj.put("TITLE", edtTitle.getText().toString());
                            obj.put("TIMESTAMP", date);
                            obj.put("SESSIONKEY", sessionKey);
                            obj.put("USERNAME", username);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String combinedMessage = obj.toString();
                        Log.d("CreateEventCombined", combinedMessage);
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
                        JSONObject answer = new JSONObject(returnString);
                        JSONObject event = new JSONObject(answer.get("EVENT").toString());

                        Log.d(returnString, "");

                        Log.d("CreateEventReturn1:", returnString);

                        if (answer.get("REPLY").equals("succes")){
                            eventKey = event.get("EVENTKEY").toString();
                            Log.d("EVENTKEY", eventKey);
//
//                            Log.d("ReturnMessage:", returnString);
//                            Log.d(answer.get("EVENT").toString(), "event");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), "Event Created", Toast.LENGTH_LONG).show();
                                }
                            });
                            dismissLoadingDialog();
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.fragWindow, new Frag_event())
                                    .addToBackStack(null)
                                    .commit();


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


}
