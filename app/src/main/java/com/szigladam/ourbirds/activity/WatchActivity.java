package com.szigladam.ourbirds.activity;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.szigladam.ourbirds.R;
import com.szigladam.ourbirds.widget.BottomNavigationBarView;
import com.szigladam.ourbirds.widget.SortWatchListView;
import com.szigladam.ourbirds.widget.WatchListView;

public class WatchActivity extends AppCompatActivity {

    SortWatchListView sortWatchListView;
    WatchListView watchListView;

    BottomNavigationBarView bottomNavigationBarView;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        sortWatchListView = findViewById(R.id.swlv_watch_list);
        watchListView = findViewById(R.id.wlv_watch_bird);

        bottomNavigationBarView = findViewById(R.id.bnbv_watch);

        sortWatchListView.setwWatchList(watchListView.getWatchList());
        sortWatchListView.setwAdapter(watchListView.getAdapter());

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