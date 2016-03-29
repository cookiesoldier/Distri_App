package dream_team.distri_app.UI;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import dream_team.distri_app.R;


public class Frag_CreateUser extends Fragment implements View.OnClickListener {

    Button btnSubmit;
    EditText edtUsername, edtPassword1,edtPassword2;

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




        return rod;
    }


    public void onClick(View v) {
        if (v ==btnSubmit) {

            //do Stuff
        }

    }




}
