package com.szigladam.ourbirds.activity;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.szigladam.ourbirds.OurBirds;
import com.szigladam.ourbirds.R;
import com.szigladam.ourbirds.fragment.LocationMapFragment;
import com.szigladam.ourbirds.model.BirdWatch;
import com.szigladam.ourbirds.widget.HabitatDropdownListView;
import com.szigladam.ourbirds.widget.SpeciesDropdownListView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class UploadActivity extends AppCompatActivity {

    ImageView backArrow;
    SpeciesDropdownListView species;
    HabitatDropdownListView habitat;
    EditText  comment;
    Button saveButton;

    LocationMapFragment fragment;

    FrameLayout frameLayout;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private long maxId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        backArrow = findViewById(R.id.iv_upload_backArrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        frameLayout = findViewById(R.id.fl_fragment_upload);

        int orientation = getResources().getConfiguration().orientation;
        int fragmentWidth = getResources().getDisplayMetrics().widthPixels;
        int fragmentHeight;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentHeight = (int) (fragmentWidth * 0.8);
        } else {
            fragmentHeight = (int) (fragmentWidth * 0.4);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, fragmentHeight);
        frameLayout.setLayoutParams(layoutParams);
        fragment = new LocationMapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_upload, fragment).commit();

        species = findViewById(R.id.sdlv_species);
        habitat = findViewById(R.id.hdlv_habitat);
        comment = findViewById(R.id.et_comment);

        saveButton = findViewById(R.id.btn_save);


        database = OurBirds.getFirebaseDatabase();
        reference = database.getReference().child("birdwatch");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    maxId = (snapshot.getChildrenCount());

                species.getSpeciesList();
                habitat.getHabitatTypeList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

    }

    private void uploadData() {

        if (!isOnline()){
            Toast.makeText(this, "Jelenleg offline vagy, az adat mentve, szinkronizálás később.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UploadActivity.this, WatchActivity.class));
        }

        int bid = (int) maxId + 1;

        String spec = species.getSelectedSpecies().trim();

        String hab = habitat.getSelectedHabitat().trim();
        String com = comment.getText().toString().trim();

        if (spec.isEmpty() || hab.isEmpty()) {
            Toast.makeText(this, "Fajt és élőhelyet is kell választani!", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDateTime timeNow = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm:ss");
        String watchDate = timeNow.format(dtf);

        double lati = fragment.getCurrentLatitude();
        double longi = fragment.getCurrentLongitude();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String loc = "";
        try {
            addresses = geocoder.getFromLocation(lati, longi, 1);
            loc = addresses.get(0).getLocality();

        } catch (IOException e) {
            loc="***Nem meghatározható***";
            Toast.makeText(this, "Helyszolgáltatás nem elérhető.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        FirebaseAuth auth = OurBirds.getFirebaseAuth();
        FirebaseUser currentUser = auth.getCurrentUser();
        String currUser;
        if (currentUser != null) {
            currUser = currentUser.getDisplayName();
        }else{
            currUser="";
        }

        BirdWatch birdWatch = new BirdWatch(bid, spec, watchDate, loc, lati, longi, hab, com, currUser);
        String key = reference.push().getKey();

        reference.child(key).setValue(birdWatch).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(UploadActivity.this, WatchActivity.class));
                    Toast.makeText(UploadActivity.this, "Megfigyelés mentve", Toast.LENGTH_SHORT).show();
                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
