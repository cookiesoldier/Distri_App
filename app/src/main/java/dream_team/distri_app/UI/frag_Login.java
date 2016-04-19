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
            if (usernameCheck()) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragWindow, new Frag_menu())
                        .addToBackStack(null)
                        .commit();
            } else {
                //??
            }


        } else if (v == btnCreateUser) {
            //Skal skifte til createUserFragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragWindow, new Frag_CreateUser())
                    .addToBackStack(null)
                    .commit();

        }

    }

    public boolean usernameCheck() {

        showLoadingDialog();
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:8080/HelpingTeacherServer2/HTSservlet");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("TASK", "loginauth");
                        obj.put("USERNAME", edtUsername.getText().toString());
                        obj.put("PASSWORD", edtPassword.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String combinedMessage = obj.toString();
                    Log.d("CombinedMessage", combinedMessage);
                    //http://developer.android.com/reference/java/net/HttpURLConnection.html
                    //connection.setDoOutput(true);
                    //I would like to get a confirmed login - perhaps session ID
                    connection.setRequestMethod("GET");
                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                    out.write(combinedMessage);
                    out.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String returnString = "";
                    returnString = in.readLine();
                    if (returnString.equals("Succes")) {
                        dismissLoadingDialog();
                    } else {
                        Log.d("CreateUserERROR:", "Something went wrong in server");
                        dismissLoadingDialog();
                    }
                    Log.d("ReturnMessage:", returnString);
                    in.close();


                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }

            }

        }).start();

        if (edtUsername.getTextSize() <= 8)
            return true;
        else {
            Toast.makeText(getActivity(), " Fejl i Username. Username skal være under 8 bogstaver =)",
                    Toast.LENGTH_LONG).show();
            return false;
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