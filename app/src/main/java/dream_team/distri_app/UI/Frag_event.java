package dream_team.distri_app.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import dream_team.distri_app.R;

public class Frag_event extends Fragment implements View.OnClickListener {
    ImageButton btn_add;
    public Frag_event() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag_event,container,false);

        btn_add = (ImageButton) rod.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        return rod;
    }
    public void onClick(View v) {
        if (v == btn_add) {
            Toast.makeText(getActivity(), "Stadig under opbygning!!! =)",
                    Toast.LENGTH_LONG).show();
        }
    }
}