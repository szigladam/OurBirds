package com.szigladam.ourbirds.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.szigladam.ourbirds.OurBirds;
import com.szigladam.ourbirds.R;

import java.util.ArrayList;
import java.util.Objects;

public class HabitatDropdownListView extends LinearLayout {

    private TextView selectHabitat;
    private ArrayList<String> habitatList;
    private ArrayAdapter<String> habitatAdapter;
    private Dialog dialog;

    public HabitatDropdownListView(Context context) {
        super(context);
        init(context);
    }

    public HabitatDropdownListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HabitatDropdownListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.view_habitat_dropdown_list, this);

        selectHabitat = findViewById(R.id.tv_selectHabitat);

        habitatList = new ArrayList<>();

        habitatAdapter = new ArrayAdapter<>(context, R.layout.dialog_spinner_dropdown_item, habitatList);

        selectHabitat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);

                dialog.setContentView(R.layout.dialog_spinner);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ListView listView = dialog.findViewById(R.id.lv_habitat);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectHabitat.setText(habitatAdapter.getItem(position));
                        dialog.dismiss();
                    }
                });

                listView.setAdapter(habitatAdapter);
            }
        });

    }

    public void getHabitatTypeList() {
        DatabaseReference habitatReference = OurBirds.getFirebaseDatabase().getReference("habitat");

        habitatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                habitatList.clear();

                for (DataSnapshot item : snapshot.getChildren()) {
                    String habitatType = item.child("habitatType").getValue(String.class);
                    habitatList.add(habitatType);
                }
                habitatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getSelectedHabitat() {
        return selectHabitat.getText().toString().trim();
    }
}
