package dream_team.distri_app.UI;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import dream_team.distri_app.R;


public class Frag_CreateUser extends Fragment implements View.OnClickListener {

    Button btnSubmit;
    EditText edtUsername, edtPassword1,edtPassword2;
    Integer doubledValue =0;

    public Frag_CreateUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag__create_user, container, false);

        btnSubmit =(Button) rod.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        edtUsername =(EditText) rod.findViewById(R.id.edtUsername);
        edtPassword1 =(EditText) rod.findViewById(R.id.edtPassword1);
        edtPassword2 = (EditText) rod.findViewById(R.id.edtPassword2);




        return rod;
    }


    public void onClick(View v) {
        if (v == btnSubmit) {
            usernameCheck();

            new Thread(new Runnable() {
                public void run() {

                    try {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragWindow, new Frag_room())
                                .addToBackStack(null)
                                .commit();

                        URL url = new URL("http://10.0.2.2:8080/MyServletProject/DoubleMeServlet");
                        URLConnection connection = url.openConnection();

                        String inputString = edtUsername.getText().toString();
                        //inputString = URLEncoder.encode(inputString, "UTF-8");

                        Log.d("inputString", inputString);

                        connection.setDoOutput(true);
                        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                        out.write(inputString);
                        out.close();

                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        String returnString = "";
                        doubledValue = 0;

                        while ((returnString = in.readLine()) != null) {
                            doubledValue = Integer.parseInt(returnString);
                        }
                        in.close();

                        /*
                        runOnUiThread(new Runnable() {
                            public void run() {

                                edtUsername.setText(doubledValue.toString());

                            }
                        });
                        */

                    } catch (Exception e) {
                        Log.d("Exception", e.toString());
                    }


                }
            }).start();


        }
    }


    public void usernameCheck() {
        //InputFilter[] filterArray = new InputFilter[1];
        //filterArray[0] = new InputFilter.LengthFilter(8);
        //edtUsername.setFilters(filterArray);
        int maxLength = 8;
        edtUsername.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        if (edtUsername.getFilters().length <= 8){
            Toast.makeText(getActivity(), " Username Succesful =)", Toast.LENGTH_LONG).show();
        } else if (edtUsername.getFilters().length > 8){
            Toast.makeText(getActivity(), " Fejl i username. Må ikke være over 8 bogstaver.", Toast.LENGTH_LONG).show();
        }

    }
}
