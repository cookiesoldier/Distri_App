package dream_team.distri_app.firebase;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Martin Vieth on 29-03-2016.
 */
public class FirebaseCon {
        Context context;
        Firebase myFirebaseRef;


        public FirebaseCon(Context context){
            this.context = context;
            setupFirebase();

        }


        public void setupFirebase() {

            Firebase.setAndroidContext(context);

            myFirebaseRef = new Firebase("https://helpingteach.firebaseio.com/");


        }


        public void createUser(String username, String password){



        }
    }


