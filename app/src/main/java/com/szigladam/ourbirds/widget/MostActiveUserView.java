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

public class MostActiveUserView extends LinearLayout {
    public MostActiveUserView(Context context) {
        super(context);
        init(context);
    }

    public MostActiveUserView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MostActiveUserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.view_most_active_user, this);

        TextView sMostActiveUser, sMaxCountUser;

        sMostActiveUser = findViewById(R.id.tv_most_active_user);
        sMaxCountUser = findViewById(R.id.tv_user_max_count);

        DatabaseReference ref = OurBirds.getFirebaseDatabase().getReference("birdwatch");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Integer> userCount = new HashMap<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String user = snapshot.child("user").getValue(String.class);
                    if (user != null) {
                        userCount.put(user, userCount.getOrDefault(user, 0) + 1);
                    }
                }

                String mostActiveUser = null;
                int maxCountUser = 0;
                for (Map.Entry<String, Integer> entry : userCount.entrySet()) {
                    if (entry.getValue() > maxCountUser) {
                        mostActiveUser = entry.getKey();
                        maxCountUser = entry.getValue();
                    }
                }

                if (mostActiveUser != null) {
                    sMostActiveUser.setText(mostActiveUser);
                    sMaxCountUser.setText(String.valueOf(maxCountUser));

                } else {
                    sMostActiveUser.setText("Nincs még megfigyelő rögzítve!");
                    sMaxCountUser.setText(String.valueOf(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });
    }
}
