package dream_team.distri_app.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import dream_team.distri_app.CurrentLogin;
import dream_team.distri_app.R;

public class Frag_room extends Fragment implements View.OnClickListener{

    ImageButton btn_add;
    TextView txtV;
    private String userName = ("UserName: " + CurrentLogin.getUserName());

    public Frag_room() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag_room,container,false);

        txtV = (TextView) rod.findViewById(R.id.txtV);
        txtV.setText(userName);

        btn_add = (ImageButton) rod.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        return rod;
    }
    public void onClick(View v) {
        if (v == btn_add) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragWindow, new Frag_event())
                    .addToBackStack(null)
                    .commit();
            Toast.makeText(getActivity(), "Event)",
                    Toast.LENGTH_LONG).show();
        }
    }
}
