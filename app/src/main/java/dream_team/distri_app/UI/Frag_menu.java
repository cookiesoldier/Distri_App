package dream_team.distri_app.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import dream_team.distri_app.R;

public class Frag_menu extends Fragment implements View.OnClickListener{
    Button btn_add;

    public Frag_menu(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag_menu,container,false);

        btn_add = (Button) rod.findViewById(R.id.btn_add);

        btn_add.setOnClickListener(this);
        return rod;
    }
    public void onClick(View v){
        if(v == btn_add){
           Toast.makeText(getActivity(), "Stadig under opbygning!!! =)",
              Toast.LENGTH_LONG).show();
        }

    }


}
