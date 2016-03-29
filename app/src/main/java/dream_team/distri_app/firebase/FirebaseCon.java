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

            myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");

            myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                }

                @Override
                public void onCancelled(FirebaseError error) {
                }
            });
        }
    }


