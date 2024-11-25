package com.szigladam.ourbirds.activity;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.szigladam.ourbirds.R;
import com.szigladam.ourbirds.widget.BottomNavigationBarView;
import com.szigladam.ourbirds.widget.CurrentUserView;
import com.szigladam.ourbirds.widget.InfoTextView;
import com.szigladam.ourbirds.widget.LastBirdWatchView;

public class HomeActivity extends AppCompatActivity {

    InfoTextView infoTextView;
    CurrentUserView currentUserView;
    LastBirdWatchView lastBirdWatchView;

    BottomNavigationBarView bottomNavigationBarView;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        infoTextView = findViewById(R.id.itv_info);
        currentUserView = findViewById(R.id.cuv_main);
        lastBirdWatchView = findViewById(R.id.lbwv_main);

        bottomNavigationBarView = findViewById(R.id.bnbv_main);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_layout);
        dialog = builder.create();

    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.dismiss();
    }
}