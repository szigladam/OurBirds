package com.szigladam.ourbirds.widget;

import android.annotation.SuppressLint;
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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.szigladam.ourbirds.R;
import com.szigladam.ourbirds.model.BirdWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SortWatchListView extends LinearLayout {

    private TextView selectSortWatch;
    ArrayList<String> selectSortWatchList;
    private ArrayAdapter<String> selectSortWatchAdapter;
    private Dialog dialog;

    private List<BirdWatch> wWatchList;
    private RecyclerView.Adapter wAdapter;

    public SortWatchListView(Context context) {
        super(context);
        init(context);
    }

    public SortWatchListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SortWatchListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_sort_watch_list, this);

        selectSortWatch = findViewById(R.id.tv_sort_watch_list);

        selectSortWatchList = new ArrayList<>();

        selectSortWatchAdapter = new ArrayAdapter<>(context, R.layout.dialog_spinner_dropdown_item, getResources().getStringArray(R.array.sort_watch_list));

        selectSortWatch.setOnClickListener(new OnClickListener() {
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
                        selectSortWatch.setText(selectSortWatchAdapter.getItem(position));
                        dialog.dismiss();

                        switch (position) {
                            case 0:
                                sortListBy("Dátum szerint csökkenő");
                                break;
                            case 1:
                                sortListBy("Dátum szerint növekvő");
                                break;
                            case 2:
                                sortListBy("Madárfaj szerint");
                                break;
                            case 3:
                                sortListBy("Felhasználó szerint");
                                break;
                            case 4:
                                sortListBy("Település szerint");
                                break;
                            case 5:
                                sortListBy("Élőhely szerint");
                                break;
                        }
                    }
                });

                listView.setAdapter(selectSortWatchAdapter);

            }

        });
    }

    public void setwWatchList(List<BirdWatch> wWatchList){
        this.wWatchList = wWatchList;
    }

    public void setwAdapter(RecyclerView.Adapter wAdapter) {
        this.wAdapter = wAdapter;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void sortListBy (String criteria) {
        if (wWatchList == null || wAdapter == null) return;

        switch (criteria) {
            case "Dátum szerint csökkenő":
                Collections.sort(wWatchList, Comparator.comparing(BirdWatch::getWatchDate).reversed());
                break;
            case "Dátum szerint növekvő":
                Collections.sort(wWatchList, Comparator.comparing(BirdWatch::getWatchDate));
                break;
            case "Madárfaj szerint":
                Collections.sort(wWatchList, Comparator.comparing(BirdWatch::getBirdSpecies));
                break;
            case "Felhasználó szerint":
                Collections.sort(wWatchList, Comparator.comparing(BirdWatch::getUser));
                break;
            case "Település szerint":
                Collections.sort(wWatchList, Comparator.comparing(BirdWatch::getLocation));
                break;
            case "Élőhely szerint":
                Collections.sort(wWatchList, Comparator.comparing(BirdWatch::getHabitat));
                break;

        }
        wAdapter.notifyDataSetChanged();
    }

}
