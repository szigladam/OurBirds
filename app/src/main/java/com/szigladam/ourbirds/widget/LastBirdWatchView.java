package com.szigladam.ourbirds.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.szigladam.ourbirds.OurBirds;
import com.szigladam.ourbirds.R;
import com.szigladam.ourbirds.fragment.StoredLocationFragment;

public class LastBirdWatchView extends LinearLayout {
    private StoredLocationFragment storedLocationFragment;

    public LastBirdWatchView(Context context) {
        super(context);
        init(context);
    }

    public LastBirdWatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LastBirdWatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.view_last_bird_watch, this);

        TextView lastSpecies, lastUser, lastDate, lastHabitat, lastLocation;
        FrameLayout frameLayout;

        lastSpecies = findViewById(R.id.tv_last_species);
        lastUser = findViewById(R.id.tv_last_user);
        lastDate = findViewById(R.id.tv_last_date);
        lastHabitat = findViewById(R.id.tv_last_habitat);
        lastLocation = findViewById(R.id.tv_last_location);

        frameLayout = findViewById(R.id.fl_stored_fragment);

        int orientation = getResources().getConfiguration().orientation;
        int fragmentWidth = getResources().getDisplayMetrics().widthPixels;
        int fragmentHeight;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentHeight = (int) (fragmentWidth * 0.6);
        } else {
            fragmentHeight = (int) (fragmentWidth * 0.4);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, fragmentHeight);
        frameLayout.setLayoutParams(layoutParams);


        if (context instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            storedLocationFragment = new StoredLocationFragment();
            fragmentTransaction.replace(R.id.fl_stored_fragment, storedLocationFragment);
            fragmentTransaction.commit();
        }

        DatabaseReference dbRef = OurBirds.getFirebaseDatabase().getReference("birdwatch");

        Query lastWatchQuery = dbRef.orderByChild("watchDate").limitToLast(1);

        lastWatchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot watchSnapshot) {

                if (watchSnapshot.exists()) {
                    DataSnapshot lastWatchSnapshot = watchSnapshot.getChildren().iterator().next();

                    String species = lastWatchSnapshot.child("birdSpecies").getValue(String.class);
                    String user = lastWatchSnapshot.child("user").getValue(String.class);
                    String date = lastWatchSnapshot.child("watchDate").getValue(String.class);
                    String habitat = lastWatchSnapshot.child("habitat").getValue(String.class);
                    String location = lastWatchSnapshot.child("location").getValue(String.class);

                    Double latitude = lastWatchSnapshot.child("latitude").getValue(Double.class);
                    Double longitude = lastWatchSnapshot.child("longitude").getValue(Double.class);

                    lastSpecies.setText(species);
                    lastUser.setText(user);
                    lastDate.setText(date);
                    lastHabitat.setText(habitat);
                    lastLocation.setText(location);

                    storedLocationFragment.showStoredLocation(new LatLng(latitude, longitude));

                } else {
                    lastSpecies.setText("Nincs adat");
                    lastUser.setText("Nincs adat");
                    lastDate.setText("Nincs adat");
                    lastHabitat.setText("Nincs adat");
                    lastLocation.setText("Nincs adat");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e("Firebase", "Adatbázis hiba", databaseError.toException());
            }
        });

    }
}
