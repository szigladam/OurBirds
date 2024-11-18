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

public class RegisteredUserCountView extends LinearLayout {
    public RegisteredUserCountView(Context context) {
        super(context);
        init(context);
    }

    public RegisteredUserCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RegisteredUserCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.view_registered_user_count, this);

        TextView sRegisteredUserCount;
        sRegisteredUserCount= findViewById(R.id.tv_registered_user);

        DatabaseReference ref = OurBirds.getFirebaseDatabase().getReference("users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long countRegistered = dataSnapshot.getChildrenCount();
                sRegisteredUserCount.setText(String.valueOf(countRegistered));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });

    }
}
