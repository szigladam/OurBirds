package com.szigladam.ourbirds.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class SpeciesDropdownListView extends LinearLayout {

    TextView selectSpecies;
    ArrayList<String> speciesList;
    ArrayAdapter<String> speciesAdapter;
    Dialog dialog;

    public SpeciesDropdownListView(Context context) {
        super(context);
        init(context);
    }

    public SpeciesDropdownListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SpeciesDropdownListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.view_species_dropdown_list, this);

        selectSpecies = findViewById(R.id.tv_select);
        speciesList = new ArrayList<>();

        speciesAdapter = new ArrayAdapter<>(context, R.layout.dialog_spinner_dropdown_item, speciesList);

        selectSpecies.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);

                dialog.setContentView(R.layout.dialog_spinner_searchable);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.et_searchSpecies);
                ListView listView = dialog.findViewById(R.id.lv_species);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        speciesAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectSpecies.setText(speciesAdapter.getItem(position));
                        dialog.dismiss();
                    }
                });

                listView.setAdapter(speciesAdapter);

            }
        });
    }

    public void getSpeciesList() {
        DatabaseReference speciesReference = OurBirds.getFirebaseDatabase().getReference("species");

        speciesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                speciesList.clear();

                for (DataSnapshot item : snapshot.getChildren()) {
                    String species = item.child("hunName").getValue(String.class);
                    speciesList.add(species);
                }
                speciesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getSelectedSpecies() {
        return selectSpecies.getText().toString().trim();
    }
}
