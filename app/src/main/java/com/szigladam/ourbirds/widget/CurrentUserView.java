package com.szigladam.ourbirds.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.szigladam.ourbirds.OurBirds;
import com.szigladam.ourbirds.R;

public class CurrentUserView extends LinearLayout {
    public CurrentUserView(Context context) {
        super(context);
        init(context);
    }

    public CurrentUserView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CurrentUserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.view_current_user, this);

        FirebaseAuth auth;
        TextView currentUser, userWatchCount;

        auth = OurBirds.getFirebaseAuth();

        currentUser = findViewById(R.id.tv_current_user);
        userWatchCount = findViewById(R.id.tv_user_watch_count);

        FirebaseUser authUser = auth.getCurrentUser();
        if (authUser != null) {

            String displayName = authUser.getDisplayName();

            currentUser.setText(displayName);

            DatabaseReference dbRef = OurBirds.getFirebaseDatabase().getReference("birdwatch");
            Query userWatchQuery = dbRef.orderByChild("user").equalTo(displayName);

            userWatchQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count = 0;
                    if (dataSnapshot.exists()) {
                        count = dataSnapshot.getChildrenCount();
                    }

                    userWatchCount.setText(String.valueOf(count));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Adatbázis hiba
                }
            });

        } else {
            Log.d("User Info", "Nincs bejelentkezett felhasználó");
        }
    }
}
