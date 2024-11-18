package com.szigladam.ourbirds;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class OurBirds extends Application {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseDatabase firebaseDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true); //Offline támogatás engedélyezése

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public static FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

}
