package com.szigladam.ourbirds.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.szigladam.ourbirds.OurBirds;
import com.szigladam.ourbirds.R;
import com.szigladam.ourbirds.activity.HomeActivity;
import com.szigladam.ourbirds.activity.LoginActivity;
import com.szigladam.ourbirds.activity.StatActivity;
import com.szigladam.ourbirds.activity.UploadActivity;
import com.szigladam.ourbirds.activity.WatchActivity;

public class BottomNavigationBarView extends CoordinatorLayout implements BottomNavigationView.OnItemSelectedListener{

    FirebaseAuth auth;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fabAddWatch;

    public BottomNavigationBarView(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigationBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomNavigationBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(final Context context){
        View.inflate(context, R.layout.view_bottom_navigation_bar, this);

        auth = OurBirds.getFirebaseAuth();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fabAddWatch = findViewById(R.id.fab_addWatch);

        bottomNavigationView.setOnItemSelectedListener(this);

        fabAddWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UploadActivity.class));
            }

        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Context context = getContext();
        if (id == R.id.home) {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0);

        } else if (id == R.id.watch) {
            Intent intent = new Intent(context, WatchActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0);
        } else if (id == R.id.stat) {
            Intent intent = new Intent(context, StatActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0);
        }
        else if (id == R.id.logout) {
            auth.signOut();
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0);
            ((Activity) context).finish();
            //Toast.makeText(getContext(), "Sikeres kijelentkezés!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}