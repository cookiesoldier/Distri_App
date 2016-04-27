package dream_team.distri_app.UI;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
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
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import dream_team.distri_app.R;


public class Frag_CreateUser extends Fragment implements View.OnClickListener {

    Button btnSubmit;
    EditText edtUsername, edtPassword1,edtPassword2,edtEmail,edtFirstName,edtLastName;

    private ProgressDialog progress;

    public Frag_CreateUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag__create_user, container, false);

        btnSubmit =(Button) rod.findViewById(R.id.btnSubmit);

        edtUsername =(EditText) rod.findViewById(R.id.edtUsername);
        edtPassword1 =(EditText) rod.findViewById(R.id.edtPassword1);
        edtPassword2 = (EditText) rod.findViewById(R.id.edtPassword2);
        edtEmail = (EditText) rod.findViewById(R.id.edtEmail);
        edtFirstName = (EditText) rod.findViewById(R.id.edtFirstName);
        edtLastName = (EditText) rod.findViewById(R.id.edtLastName);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(10); //Filter to 10 characters
        edtUsername.setFilters(filters);


        btnSubmit.setOnClickListener(this);
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

        if (v == btnSubmit) {
            showLoadingDialog();
            if( edtUsername.getText().toString().toLowerCase().contains("username") ||
                    edtPassword1.getText().toString().contains("password")){
                dismissLoadingDialog();

                Toast.makeText(getActivity().getApplicationContext(), "You password cannot contain password or your username cannot contain username", Toast.LENGTH_LONG).show();

            }else if(!edtPassword1.getText().toString().equals(edtPassword2.getText().toString())){
                dismissLoadingDialog();

                Toast.makeText(getActivity().getApplicationContext(), "passwords dosent match", Toast.LENGTH_LONG).show();

            }
            else{

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            URL url = new URL("http://52.58.112.107:8080/HelpingTeacherServer2/HTSservlet");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("TASK", "CREATEUSER");
                                obj.put("USERNAME", edtUsername.getText().toString());
                                obj.put("PASSONE", edtPassword2.getText().toString());
                                obj.put("PASSTWO", edtPassword1.getText().toString());
                                obj.put("",edtEmail.getText().toString());
                                obj.put("",edtFirstName.getText().toString());
                                obj.put("",edtLastName.getText().toString());
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
                            JSONObject answer = new JSONObject(returnString);

                            Log.d(returnString, "");
                            if (returnString.equals(obj)) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "User Created!!", Toast.LENGTH_LONG).show();
                                    }
                                });
                                dismissLoadingDialog();
                            } else if(answer.get("REPLY").equals("failed")){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "User already exists!", Toast.LENGTH_LONG).show();
                                    }
                                });
                                dismissLoadingDialog();
                            } else {
                                //Log.d("CreateUserERROR:", "Something went wrong in server");
                                //dismissLoadingDialog();
                            }
                            Log.d("ReturnMessage:", returnString);
                            in.close();


                        } catch (Exception e) {
                            Log.d("Exception", e.toString());
                        }
                        if(timer.equals(5000)) {
                            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong" + "Please try again",
                                  Toast.LENGTH_LONG).show();
                            dismissLoadingDialog();
                        }

                }

                }).start();


            }}
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
