package thulasi.hemanthkumar.foody;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity {
    private Button skip,add_btn;
    private final int PLACE_PICKER_REQUEST = 15;
    private final int PERMISSION_ALL = 1;
    private boolean pay;
    public static Context addressContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        add_btn = findViewById(R.id.address_btn);
        addressContext = getApplicationContext();
        skip = findViewById(R.id.skip);
        pay = getIntent().getBooleanExtra("pay",false);
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,

        };



        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this,MainActivity.class));
                finish();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermissions(AddressActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(AddressActivity.this, PERMISSIONS, PERMISSION_ALL);
                }

                else {

//                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                    try {
//                        startActivityForResult(builder.build(AddressActivity.this), PLACE_PICKER_REQUEST);
//
//                    } catch (GooglePlayServicesRepairableException e) {
//                        Toast.makeText(AddressActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//                    } catch (GooglePlayServicesNotAvailableException e) {
//                        Toast.makeText(AddressActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        Toast.makeText(AddressActivity.this, "" + e, Toast.LENGTH_SHORT).show();
//                    }
                    startActivity(new Intent(AddressActivity.this,MapsActivity.class).putExtra("pay",pay));
                }



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data,this);
                StringBuilder stringBuilder = new StringBuilder();
                double latitude = place.getLatLng().latitude;
                double longitude =place.getLatLng().longitude;



                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitude,longitude,1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    // Only if available else return NULL
                    Intent go = new Intent(AddressActivity.this, SaveAddressActivity.class);
                    go.putExtra("address",address);
                    go.putExtra("pin",postalCode);
                    startActivity(go);
                } catch (IOException e) {
                    Toast.makeText(AddressActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }


            }
            else {
                Toast.makeText(getApplicationContext(),""+resultCode+data, Toast.LENGTH_SHORT).show();
            }

            } else {
                Toast.makeText(getApplicationContext(), "Fix request", Toast.LENGTH_SHORT).show();
            }

        }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL: {
                if (grantResults.length > 0) {
                    boolean flag = true;
                    for (int i =0;i<permissionsList.length;i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            flag = false;

                        }

                    }
                    if (flag) {

                    }

                }
                return;
            }
        }
    }


}
