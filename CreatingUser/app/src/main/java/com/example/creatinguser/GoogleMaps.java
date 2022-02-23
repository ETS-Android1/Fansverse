package com.example.creatinguser;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.creatinguser.databinding.ActivityGoogleMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Adding markers based on their coordinates and then adding them to a map
        LatLng interlude = new LatLng(33.788542199826665, -118.13353625782406);
        LatLng blondies = new LatLng(33.79668758396616, -118.14309084425682);
        LatLng portCity = new LatLng(33.78254749337177, -118.14340355111402);
        LatLng crookedDuck = new LatLng(33.783980693882555, -118.13390584960761);
        LatLng mvpGrill = new LatLng(33.7957953681204, -118.12622952011328);

        mMap.addMarker(new MarkerOptions().position(interlude).title("Interlude pub"));
        mMap.addMarker(new MarkerOptions().position(blondies).title("Blondie's"));
        mMap.addMarker(new MarkerOptions().position(portCity).title("Port City Tavern"));
        mMap.addMarker(new MarkerOptions().position(crookedDuck).title("The Crooked Duck"));
        mMap.addMarker(new MarkerOptions().position(mvpGrill).title("MVP Grill"));



        //second value is zoom, can be from 1 to 20
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(interlude, 15));

        //mMap.setOnMarkerClickListener((OnMarkerClickListener) this);

        


        //public void openBlondies()
        //{
        //    Intent intent = new Intent(this, blondie_info.xml);
        //    startActivity(intent);
        //}

    }
}