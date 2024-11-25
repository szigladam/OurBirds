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

public class MostFrequentHabitatView extends LinearLayout {
    public MostFrequentHabitatView(Context context) {
        super(context);
        init(context);
    }

    public MostFrequentHabitatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MostFrequentHabitatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.view_most_frequent_habitat, this);

        TextView sMostFrequentHabitat, sMaxCountHabitat;

        sMostFrequentHabitat = findViewById(R.id.tv_most_frequent_habitat);
        sMaxCountHabitat = findViewById(R.id.tv_habitat_max_count);

        DatabaseReference ref = OurBirds.getFirebaseDatabase().getReference("birdwatch");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Integer> habitatCount = new HashMap<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String habitat = snapshot.child("habitat").getValue(String.class);
                    if (habitat != null) {
                        habitatCount.put(habitat, habitatCount.getOrDefault(habitat, 0) + 1);
                    }
                }

                String mostFrequentHabitat = null;
                int maxCountHabitat = 0;
                for (Map.Entry<String, Integer> entry : habitatCount.entrySet()) {
                    if (entry.getValue() > maxCountHabitat) {
                        mostFrequentHabitat = entry.getKey();
                        maxCountHabitat = entry.getValue();
                    }
                }

                if (mostFrequentHabitat != null) {
                    sMostFrequentHabitat.setText(mostFrequentHabitat);
                    sMaxCountHabitat.setText(String.valueOf(maxCountHabitat));
                } else {
                    sMostFrequentHabitat.setText("Nincs még élőhely rögzítve!");
                    sMaxCountHabitat.setText(String.valueOf(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });
    }
}
