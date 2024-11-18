package com.szigladam.ourbirds.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.szigladam.ourbirds.R;
import com.szigladam.ourbirds.widget.BottomNavigationBarView;
import com.szigladam.ourbirds.widget.MostActiveUserView;
import com.szigladam.ourbirds.widget.MostFrequentHabitatView;
import com.szigladam.ourbirds.widget.MostFrequentLocationView;
import com.szigladam.ourbirds.widget.MostFrequentSpeciesView;
import com.szigladam.ourbirds.widget.RegisteredUserCountView;
import com.szigladam.ourbirds.widget.WatchedSpeciesCountView;

public class StatActivity extends AppCompatActivity {

    RegisteredUserCountView registeredUserCountView;
    WatchedSpeciesCountView watchedSpeciesCountView;
    MostFrequentSpeciesView mostFrequentSpeciesView;
    MostActiveUserView mostActiveUserView;
    MostFrequentLocationView mostFrequentLocationView;
    MostFrequentHabitatView mostFrequentHabitatView;
    BottomNavigationBarView bottomNavigationBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        registeredUserCountView = findViewById(R.id.rucv_user);
        watchedSpeciesCountView = findViewById(R.id.wscv_species);
        mostFrequentSpeciesView = findViewById(R.id.mfsv_species);
        mostActiveUserView = findViewById(R.id.mauv_user);
        mostFrequentLocationView = findViewById(R.id.mflv_location);
        mostFrequentHabitatView = findViewById(R.id.mfhv_habitat);

        bottomNavigationBarView = findViewById(R.id.bnbv_stat);

    }
}