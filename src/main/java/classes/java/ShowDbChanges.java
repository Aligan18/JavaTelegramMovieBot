package classes.java;

import com.google.firebase.database.*;

import java.io.IOException;

public class ShowDbChanges implements Runnable {
    public void run() {
        Firebase fbs = null;
        try {
            fbs = new Firebase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DatabaseReference ref = fbs.getDb()
                .getReference("/");
        ref.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object document = dataSnapshot.getValue();

            }


            public void onCancelled(DatabaseError error) {
                System.out.print("Error: " + error.getMessage());
            }
        });


    }
}