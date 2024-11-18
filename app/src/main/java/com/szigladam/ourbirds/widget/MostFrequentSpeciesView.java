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

public class MostFrequentSpeciesView extends LinearLayout {
    public MostFrequentSpeciesView(Context context) {
        super(context);
        init(context);
    }

    public MostFrequentSpeciesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MostFrequentSpeciesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.view_most_frequent_species, this);

        TextView sMostFrequentSpecies, sMaxCountSpecies;

        sMostFrequentSpecies = findViewById(R.id.tv_most_frequent_species);
        sMaxCountSpecies = findViewById(R.id.tv_species_max_count);

        DatabaseReference ref = OurBirds.getFirebaseDatabase().getReference("birdwatch");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Integer> speciesCount = new HashMap<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String species = snapshot.child("birdSpecies").getValue(String.class);
                    if (species != null) {
                        speciesCount.put(species, speciesCount.getOrDefault(species, 0) + 1);
                    }
                }

                String mostFrequentSpecies = null;
                int maxCountSpecies = 0;
                for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
                    if (entry.getValue() > maxCountSpecies) {
                        mostFrequentSpecies = entry.getKey();
                        maxCountSpecies = entry.getValue();
                    }
                }

                if (mostFrequentSpecies != null) {
                    sMostFrequentSpecies.setText(mostFrequentSpecies);
                    sMaxCountSpecies.setText(String.valueOf(maxCountSpecies));

                } else {
                    sMostFrequentSpecies.setText("Nincs még madárfaj rögzítve!");
                    sMaxCountSpecies.setText(String.valueOf(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });
    }
}
