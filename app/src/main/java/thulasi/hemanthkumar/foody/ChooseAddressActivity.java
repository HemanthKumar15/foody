package thulasi.hemanthkumar.foody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import thulasi.hemanthkumar.foody.data.CartHandler;
import thulasi.hemanthkumar.foody.model.Cart;

public class ChooseAddressActivity extends AppCompatActivity {

    private CardView home_cv,work_cv,other_cv;
    private ImageView home_img,work_img, other_img;
    private ImageButton homecancel_btn,workcancel_btn,othercancel_btn;
    private TextView home_txt,home_address;
    private TextView work_txt,work_address;
    private TextView other_txt,other_address;
    private DatabaseReference AddressRef;
    private String selection = "";
    private Button addaddress, next;
    private final int PERMISSION_ALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);

        AddressRef = FirebaseDatabase.getInstance().getReference().child("address").child(getSharedPreferences("save",MODE_PRIVATE).getString("num","null"));
        home_cv = findViewById(R.id.home_cv);
        work_cv = findViewById(R.id.work_cv);
        other_cv = findViewById(R.id.other_cv);

        addaddress = findViewById(R.id.add_btn);
        next = findViewById(R.id.next_btn);
        homecancel_btn = findViewById(R.id.homecancel_btn);
        workcancel_btn = findViewById(R.id.workcancel_btn);
        othercancel_btn = findViewById(R.id.othercancel_btn);

        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,

        };
        if (getIntent().getIntExtra("amount",0) == -1){
            next.setVisibility(View.GONE);
            TextView title = findViewById(R.id.title_txt);
            title.setText("Add/Change Address");
            homecancel_btn.setVisibility(View.VISIBLE);
            workcancel_btn.setVisibility(View.VISIBLE);
            othercancel_btn.setVisibility(View.VISIBLE);
        }

        home_img = findViewById(R.id.home_img_rd);
        work_img = findViewById(R.id.work_img_rd);
        other_img = findViewById(R.id.other_img_rd);

        home_txt = findViewById(R.id.home_txt_rd);
        work_txt = findViewById(R.id.work_txt_rd);
        other_txt = findViewById(R.id.other_txt_rd);

        home_address = findViewById(R.id.home_address_rd);
        work_address = findViewById(R.id.work_address_rd);
        other_address = findViewById(R.id.other_address_rd);


        AddressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CheckAvailableAddresses(snapshot);
                }
                else {
                    if (!hasPermissions(ChooseAddressActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(ChooseAddressActivity.this, PERMISSIONS, PERMISSION_ALL);
                    }
                    else {
                        Intent go = new Intent(ChooseAddressActivity.this, MapsActivity.class);
                        go.putExtra("pay", true);
                        startActivity(go);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermissions(ChooseAddressActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(ChooseAddressActivity.this, PERMISSIONS, PERMISSION_ALL);
                }
                else {
                    Intent go = new Intent(ChooseAddressActivity.this, MapsActivity.class);
                    go.putExtra("pay", true);
                    startActivity(go);
                }
            }
        });

        home_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection = "home";
                SelectAddress(home_txt,home_img,home_cv,R.drawable.ic_home_or);
            }
        });
        work_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection = "work";
                SelectAddress(work_txt,work_img,work_cv,R.drawable.ic_apartment_or);
            }
        });
        other_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection = "other";
                SelectAddress(other_txt,other_img,other_cv,R.drawable.ic_location_or);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(selection.equals(""))){
                    CartHandler db = new CartHandler(getApplicationContext());
                    List<Cart> cartList = db.getCart();
                    Integer amount = 0;
                    for (thulasi.hemanthkumar.foody.model.Cart cart: cartList) {
                        amount = amount + Integer.valueOf(cart.getTotal());
                    }

                    Intent go = new Intent(ChooseAddressActivity.this, PaymentActivity.class);
                    go.putExtra("place", selection);
                    go.putExtra("amount", amount);
                    startActivity(go);
                    finish();
                }
            }
        });
        homecancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ChooseAddressActivity.this)
                        .setTitle("Delete Address?")
                        .setMessage("Do you want to delete this address")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteAddress("home",home_cv);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int a = 1;
                                Log.d("TAG", "onClick: "+a);
                            }
                        })
                        .show();


            }

        });
        workcancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ChooseAddressActivity.this)
                        .setTitle("Delete Address?")
                        .setMessage("Do you want to delete this address")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteAddress("work",work_cv);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int a = 1;
                                Log.d("TAG", "onClick: "+a);
                            }
                        })
                        .show();


            }

        });
        othercancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ChooseAddressActivity.this)
                        .setTitle("Delete Address?")
                        .setMessage("Do you want to delete this address")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteAddress("other",other_cv);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int a = 1;
                                Log.d("TAG", "onClick: "+a);
                            }
                        })
                        .show();


            }

        });



    }

    private void DeleteAddress(String place, CardView cancel_cv) {
        FirebaseDatabase.getInstance().getReference().child("address")
                .child(getSharedPreferences("save", MODE_PRIVATE).getString("num", "non_user"))
                .child(place).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                   cancel_cv.setVisibility(View.GONE);
                }
            }
        });
    }


    private void SelectAddress(TextView txt, ImageView img,CardView cv, Integer i) {
        home_img.setImageResource(R.drawable.ic_home_black_24dp);
        work_img.setImageResource(R.drawable.ic_apartment_bk);
        other_img.setImageResource(R.drawable.ic_location_bk);

        if(getIntent().getIntExtra("amount",0) != -1) {
            home_txt.setTextColor(Color.BLACK);
            work_txt.setTextColor(Color.BLACK);
            other_txt.setTextColor(Color.BLACK);

            home_cv.setBackgroundResource(R.drawable.input_text_default);
            work_cv.setBackgroundResource(R.drawable.input_text_default);
            other_cv.setBackgroundResource(R.drawable.input_text_default);


            img.setImageResource(i);
            txt.setTextColor(Color.parseColor("#FF5722"));
            cv.setBackgroundResource(R.drawable.input_text_or);
        }


    }

    private void CheckAvailableAddresses(DataSnapshot snapshot) {
        if(snapshot.child("home").exists()){
            RetriveCardData(snapshot.child("home").child("door").getValue().toString(),snapshot.child("home").child("area").getValue().toString(),snapshot.child("home").child("address").getValue().toString(),home_cv,home_address);
        }
        if(snapshot.child("work").exists()){
            RetriveCardData(snapshot.child("work").child("door").getValue().toString(),snapshot.child("work").child("area").getValue().toString(),snapshot.child("work").child("address").getValue().toString(),work_cv,work_address);
        }
        if(snapshot.child("other").exists()){
            RetriveCardData(snapshot.child("other").child("door").getValue().toString(),snapshot.child("other").child("area").getValue().toString(),snapshot.child("other").child("address").getValue().toString(),other_cv,other_address);
        }
    }

    private void RetriveCardData(String door, String area, String address, CardView object_cv, TextView object_address) {
        object_address.setText(""+door+", "+area+"\n" + address);
        object_cv.setVisibility(View.VISIBLE);
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