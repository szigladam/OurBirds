package com.szigladam.ourbirds.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.szigladam.ourbirds.R;
import com.szigladam.ourbirds.fragment.StoredLocationFragment;

public class DetailsActivity extends AppCompatActivity {

    ImageView backArrow;
    TextView dSpecies, dWatchUser, dLocation, dHabitat, dWatchDate, dLatitude, dLongitude, dComment;
    FrameLayout frameLayout;
    StoredLocationFragment storedLocationFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        backArrow = findViewById(R.id.iv_details_backArrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dSpecies = findViewById(R.id.tv_species_details);
        dWatchUser = findViewById(R.id.tv_watch_user_details);
        dLocation = findViewById(R.id.tv_location_details);
        dHabitat = findViewById(R.id.tv_habitat_details);
        dWatchDate = findViewById(R.id.tv_watch_date_details);
        dLatitude = findViewById(R.id.tv_latitude_details);
        dLongitude = findViewById(R.id.tv_longitude_details);
        dComment = findViewById(R.id.tv_comment_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dSpecies.setText(bundle.getString("Species"));
            dWatchUser.setText(bundle.getString("WatchUser"));
            dLocation.setText(bundle.getString("Location"));
            dHabitat.setText(bundle.getString("Habitat"));
            dWatchDate.setText(bundle.getString("Watch Date"));
            dLatitude.setText(bundle.getString("Latitude"));
            dLongitude.setText(bundle.getString("Longitude"));
            dComment.setText(bundle.getString("Comment"));

            frameLayout = findViewById(R.id.fl_stored_details);

            int orientation = getResources().getConfiguration().orientation;
            int fragmentWidth = getResources().getDisplayMetrics().widthPixels;
            int fragmentHeight;

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                fragmentHeight = (int) (fragmentWidth * 0.9);
            } else {
                fragmentHeight = (int) (fragmentWidth * 0.4);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, fragmentHeight);
            frameLayout.setLayoutParams(layoutParams);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            storedLocationFragment = new StoredLocationFragment();
            fragmentTransaction.replace(R.id.fl_stored_details, storedLocationFragment);
            fragmentTransaction.commit();

            if (bundle != null) {
                double latitude = Double.parseDouble(bundle.getString("Latitude", "0"));
                double longitude = Double.parseDouble(bundle.getString("Longitude", "0"));

                fragmentManager.executePendingTransactions();
                storedLocationFragment.showStoredLocation(new LatLng(latitude, longitude));

            }
        }
    }

}