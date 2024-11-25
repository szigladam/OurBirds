package com.szigladam.ourbirds.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.szigladam.ourbirds.R;

public class LocationMapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap googleMap;
    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    FrameLayout flMap;
    private LocationCallback locationCallback;
    private boolean firstLoad =true;

    // Az ActivityResultLauncher példány létrehozása
    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                if (fineLocationGranted != null && fineLocationGranted) {
                    getLocation();  // Engedélyek megvannak
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    getLocation();  // Ha csak a hozzávetőleges helyet engedélyezik
                } else {
                    // Engedélyek elutasítva
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_map, container, false);
        flMap = view.findViewById(R.id.fr_locationMap);
        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fr_locationMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(LocationMapFragment.this);
        }

        checkPermissions();

        return view;
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Engedélykérés
            requestPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            //Ha rendben helymeghatározás
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 10000) // Magas pontosság, 10 másodperces intervallum
                .setMinUpdateIntervalMillis(5000) // Leggyorsabb 5 másodperc
                .setWaitForAccurateLocation(false) // Nem vár a rendkívül pontos helyre
                .build();

        locationCallback =new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                currentLocation = locationResult.getLastLocation();
                updateMapLocation();
            }
        };
        fusedClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            updateMapLocation();
    }

    //Térkép frissítése
    private void updateMapLocation() {
        if (currentLocation != null && googleMap != null) {
            double Longitude = getCurrentLongitude();
            double Latitude = getCurrentLatitude();
            LatLng latLng = new LatLng(Latitude, Longitude);

            googleMap.clear();

            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(String.valueOf(Latitude)+", "+String.valueOf(Longitude));
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            googleMap.addMarker(markerOptions);


            if (firstLoad) {
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));  // Zoom 15-re
                firstLoad = false;
            } else {
                float zoomSize = googleMap.getCameraPosition().zoom;
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomSize));
            }
        }
    }

    //Szélesség lekérdezése
    public double getCurrentLatitude() {
        if (currentLocation != null) {
            return currentLocation.getLatitude();
        } else {
            return 0.0;
        }
    }

    //Hosszúság lekérdezése
    public double getCurrentLongitude() {
        if (currentLocation != null) {
            return currentLocation.getLongitude();
        } else {
            return 0.0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fusedClient != null && locationCallback != null) {
            fusedClient.removeLocationUpdates(locationCallback);
        }
    }
}

