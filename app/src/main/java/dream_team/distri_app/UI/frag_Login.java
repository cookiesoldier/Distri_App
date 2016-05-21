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
import java.util.Timer;
import java.util.TimerTask;

import dream_team.distri_app.CurrentLogin;
import dream_team.distri_app.R;


/**

 */
public class frag_Login extends Fragment implements View.OnClickListener {

    Button btnCreateUser, btnLogin;
    EditText edtUsername, edtPassword;

    private ProgressDialog progress;

    public frag_Login() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag__login, container, false);


        btnLogin = (Button) rod.findViewById(R.id.btnLogin);
        btnCreateUser = (Button) rod.findViewById(R.id.btnCreateNewUser);
        edtUsername = (EditText) rod.findViewById(R.id.edtUsername);
        edtPassword = (EditText) rod.findViewById(R.id.edtPassword);
        btnLogin.setOnClickListener(this);
        btnCreateUser.setOnClickListener(this);


        return rod;
    }
    public void onClick(View v) {
        if (v == btnLogin) {

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
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("TASK", "loginauth");
                                obj.put("USERNAME", edtUsername.getText().toString());
                                obj.put("PASSWORD", edtPassword.getText().toString());

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
                                CurrentLogin.setUserName(edtUsername.getText().toString());
                                CurrentLogin.setSessionKey(answer.get("SESSIONKEY").toString());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
                                    }
                                });

                                Log.d(returnString, returnString);
                                dismissLoadingDialog();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.fragWindow, new Frag_menu())
                                        .addToBackStack(null)
                                        .commit();

                            } else if (answer.get("REPLY").equals("failed")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "Login Failed!", Toast.LENGTH_LONG).show();
                                    }
                                });
                                Log.d("CreateUserERROR:", "Something went wrong in server");
                                dismissLoadingDialog();
                            }

                            Log.d("ReturnMessage:", returnString);
                            in.close();



                        } catch (Exception e) {
                            Log.d("Exception", e.toString());
                        }
                        if(timer.equals(5000)) {
                            //Toast.makeText(getActivity().getApplicationContext(), "Something went wrong" + "Please try again",
                            //      Toast.LENGTH_LONG).show();
                            dismissLoadingDialog();
                        }

                    }

                }).start();

            }


        else if (v == btnCreateUser){
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragWindow, new Frag_CreateUser())
                    .addToBackStack(null)
                    .commit();
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