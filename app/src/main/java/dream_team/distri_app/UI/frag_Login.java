package dream_team.distri_app.UI;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dream_team.distri_app.R;


/**

 */
public class frag_Login extends Fragment implements View.OnClickListener {

    Button btnCreateUser, btnLogin;
    EditText edtUsername, edtPassword;



    public frag_Login() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag__login,container,false);


        btnLogin = (Button) rod.findViewById(R.id.btnLogin);
        btnCreateUser = (Button) rod.findViewById(R.id.btnCreateNewUser);
        edtUsername = (EditText) rod.findViewById(R.id.edtUsername);
        edtPassword  = (EditText) rod.findViewById(R.id.edtPassword);
        btnLogin.setOnClickListener(this);
        btnCreateUser.setOnClickListener(this);


        return rod;
    }


    public void onClick(View v) {

        if(v == btnLogin){
            usernameCheck();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragWindow, new Frag_menu())
                    .addToBackStack(null)
                    .commit();

        }else if(v== btnCreateUser){
            //Skal skifte til createUserFragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragWindow, new Frag_CreateUser())
                    .addToBackStack(null)
                    .commit();

        }

    }

    public boolean usernameCheck() {
        if (edtUsername.getTextSize()<= 8)
            return true;
        else {
            Toast.makeText(getActivity(), " Fejl i Username. Username skal være under 8 bogstaver =)",
              Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
