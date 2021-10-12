package thulasi.hemanthkumar.foody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapView;
    private String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Float DefaultZoom = 15f;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng pickLatLng;
    private Button pick_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        pick_btn = findViewById(R.id.pick_btn);
        mapView.getMapAsync(this);
        pick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(pickLatLng.latitude, pickLatLng.longitude,1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    // Only if available else return NULL
                    Intent go = new Intent(MapsActivity.this, SaveAddressActivity.class);
                    go.putExtra("address",address);
                    go.putExtra("pin",postalCode);
                    startActivity(go);
                    finish();
                } catch (IOException e) {
                    Toast.makeText(MapsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapView.onResume();
        getCurrentLocation();
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                pickLatLng = latLng;
                mMap.clear();
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(address));
                    pick_btn.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    return;
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        mMap.setMyLocationEnabled(true);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{

            Task<Location> location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()){
                        Location currentLocation = task.getResult();
                        if (currentLocation != null){
                            moveCamera(
                                    new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),
                                    DefaultZoom
                            );
                        }
                    }
                    else {
                        Toast.makeText(MapsActivity.this, "Location not found. please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){
            Log.e("MAPS", "getCurrentLocationError: "+ e);
        }
    }

    private void moveCamera(LatLng latLng, Float defaultZoom) {
        pickLatLng = latLng;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,defaultZoom));
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here!!!"));
        pick_btn.setVisibility(View.VISIBLE);




    }
}