package com.szigladam.ourbirds.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.szigladam.ourbirds.R;
import com.szigladam.ourbirds.activity.DetailsActivity;
import com.szigladam.ourbirds.model.BirdWatch;

import java.util.List;

public class WatchAdapter extends RecyclerView.Adapter<WatchViewHolder> {

    private Context context;
    private List<BirdWatch> watchList;

    public WatchAdapter(Context context, List<BirdWatch> watchList) {
        this.context = context;
        this.watchList = watchList;
    }

    @NonNull
    @Override
    public WatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new WatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchViewHolder holder, int position) {
        holder.watchedBird.setText(watchList.get(position).getBirdSpecies());
        holder.watchUser.setText(watchList.get(position).getUser());
        holder.location.setText(watchList.get(position).getLocation());
        holder.habitat.setText(watchList.get(position).getHabitat());
        holder.watchDate.setText(watchList.get(position).getWatchDate());

        holder.watchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, DetailsActivity.class);
                intent.putExtra("Species", watchList.get(holder.getAdapterPosition()).getBirdSpecies());
                intent.putExtra("WatchUser", watchList.get(holder.getAdapterPosition()).getUser());
                intent.putExtra("Location", watchList.get(holder.getAdapterPosition()).getLocation());
                intent.putExtra("Habitat", watchList.get(holder.getAdapterPosition()).getHabitat());
                intent.putExtra("Watch Date", watchList.get(holder.getAdapterPosition()).getWatchDate());
                //Stringre konvertálás
                intent.putExtra("Latitude", String.valueOf(watchList.get(holder.getAdapterPosition()).getLatitude()));
                intent.putExtra("Longitude", String.valueOf(watchList.get(holder.getAdapterPosition()).getLongitude()));

                intent.putExtra("Comment", watchList.get(holder.getAdapterPosition()).getComment());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return watchList.size();
    }
}

class WatchViewHolder extends RecyclerView.ViewHolder{

    TextView watchedBird, location,  watchDate, watchUser, habitat;//, latitude, longitude, comment;
    CardView watchCard;

    public WatchViewHolder(@NonNull View itemView) {
        super(itemView);

        watchCard = itemView.findViewById(R.id.cv_watchCard);
        watchedBird = itemView.findViewById(R.id.tv_watched_bird);
        watchUser = itemView.findViewById(R.id.tv_watch_user);
        location = itemView.findViewById(R.id.tv_location);
        habitat = itemView.findViewById(R.id.tv_habitat);
        watchDate = itemView.findViewById(R.id.tv_watch_date);
    }
}