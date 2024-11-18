package com.szigladam.ourbirds.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.szigladam.ourbirds.OurBirds;
import com.szigladam.ourbirds.R;

import java.util.HashMap;
import java.util.Map;

public class MostFrequentLocationView extends LinearLayout {
    public MostFrequentLocationView(Context context) {
        super(context);
        init(context);
    }

    public MostFrequentLocationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MostFrequentLocationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.view_most_frequent_location, this);

        TextView sMostFrequentLocation, sMaxCountLocation;

        sMostFrequentLocation = findViewById(R.id.tv_most_frequent_location);
        sMaxCountLocation = findViewById(R.id.tv_location_max_count);

        DatabaseReference ref = OurBirds.getFirebaseDatabase().getReference("birdwatch");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Integer> locationCount = new HashMap<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String location = snapshot.child("location").getValue(String.class);
                    if (location != null) {
                        locationCount.put(location, locationCount.getOrDefault(location, 0) + 1);
                    }
                }

                String mostFrequentLocation = null;
                int maxCountLocation = 0;
                for (Map.Entry<String, Integer> entry : locationCount.entrySet()) {
                    if (entry.getValue() > maxCountLocation) {
                        mostFrequentLocation = entry.getKey();
                        maxCountLocation = entry.getValue();
                    }
                }

                if (mostFrequentLocation != null) {
                    sMostFrequentLocation.setText(mostFrequentLocation);
                    sMaxCountLocation.setText(String.valueOf(maxCountLocation));

                } else {
                    sMostFrequentLocation.setText("Nincs még helység rögzítve!");
                    sMaxCountLocation.setText(String.valueOf(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });
    }
}
