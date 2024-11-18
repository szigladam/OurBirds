package com.szigladam.ourbirds.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.szigladam.ourbirds.R;

public class StoredLocationFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap googleMap;
    private LatLng storedLatLng;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stored_location, container, false);

        // Térkép inicializálása
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fr_storedLocation);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (storedLatLng != null) {
            showStoredLocation(storedLatLng);
        }

    }

    public void showStoredLocation(LatLng latLng) {
        storedLatLng = latLng;
        if (googleMap != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng).title(String.valueOf(latLng.latitude)+" ,"+String.valueOf(latLng.longitude)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }
    }
}
