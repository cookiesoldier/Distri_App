package dream_team.distri_app.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.FrameLayout;



import java.net.URL;

import dream_team.distri_app.R;

public class MainActivity  extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout f1 = (FrameLayout) findViewById(R.id.fragWindow);

        if (savedInstanceState == null) {
            Fragment fragment = new frag_Login();
            getSupportFragmentManager().beginTransaction().add(R.id.fragWindow, fragment)  // tom container i layout
                    .commit();
        }




    }


}
