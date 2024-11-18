package com.szigladam.ourbirds.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.szigladam.ourbirds.OurBirds;
import com.szigladam.ourbirds.R;
import com.szigladam.ourbirds.adapter.WatchAdapter;
import com.szigladam.ourbirds.model.BirdWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WatchListView extends LinearLayout {
    RecyclerView watchListRecyclerView;
    private List<BirdWatch> watchList;
    private WatchAdapter adapter;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;


    public WatchListView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public WatchListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WatchListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(final Context context) {
        View.inflate(context, R.layout.view_watch_list, this);

        watchListRecyclerView = findViewById(R.id.rcv_watch_list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        watchListRecyclerView.setLayoutManager(gridLayoutManager);

        watchList = new ArrayList<>();
        adapter = new WatchAdapter(context, watchList);
        watchListRecyclerView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        databaseReference = OurBirds.getFirebaseDatabase().getReference("birdwatch");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                watchList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    BirdWatch birdWatch = itemSnapshot.getValue(BirdWatch.class);
                    watchList.add(birdWatch);
                }

                Collections.reverse(watchList);
                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

    }

    public List<BirdWatch> getWatchList() {
        return watchList;
    }

    public WatchAdapter getAdapter() {
        return adapter;
    }
}