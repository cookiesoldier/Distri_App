package dream_team.distri_app.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import dream_team.distri_app.R;

public class Frag_menu extends Fragment implements View.OnClickListener{
    ImageButton btn_add;
    TextView textView;
    String[] roomListe = new String[] {
            "Cupcake"
            ,"Donut"
            ,"Eclair"
            ,"Froyo"
            ,"Gingerbread"
            ,"Honeycomb"
            ,"Ice Cream SandWich"
            ,"Jelly Bean"
            ,"KitKat"
            ,"1"
            ,"2"
            ,"3"
            ,"4"
            ,"mac"
            ,"Windows"
            ,"linux"
            ,"abe"
            ,"kage"
                };

    public Frag_menu(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.fragment_frag_menu, container, false);

        btn_add = (ImageButton) rod.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        textView = (TextView) getActivity().findViewById(R.id.textViewListe);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, roomListe);

        ListView lv = (ListView)rod.findViewById(R.id.list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragWindow, new Frag_room())
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(getActivity(), "Liste click Frag_room " + adapter.getItem(position),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return rod;
    }
    public void onClick(View v){
        if(v == btn_add){
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragWindow, new Frag_room())
                    .addToBackStack(null)
                    .commit();
           Toast.makeText(getActivity(), "Button click Frag_Room ",
              Toast.LENGTH_SHORT).show();
        }

    }

}
