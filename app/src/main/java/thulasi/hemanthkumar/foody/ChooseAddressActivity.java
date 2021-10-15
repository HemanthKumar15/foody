package thulasi.hemanthkumar.foody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChooseAddressActivity extends AppCompatActivity {

    private CardView home_cv,work_cv,other_cv;
    private ImageView home_img,work_img, other_img;
    private TextView home_txt,home_address;
    private TextView work_txt,work_address;
    private TextView other_txt,other_address;
    private DatabaseReference AddressRef;
    private String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);

        AddressRef = FirebaseDatabase.getInstance().getReference().child("address").child(getSharedPreferences("save",MODE_PRIVATE).getString("num","null"));
        home_cv = findViewById(R.id.home_cv);
        work_cv = findViewById(R.id.work_cv);
        other_cv = findViewById(R.id.other_cv);

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
                    Intent go = new Intent(ChooseAddressActivity.this,MapsActivity.class);
                    go.putExtra("pay",true);
                    startActivity(go);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    }


    private void SelectAddress(TextView txt, ImageView img,CardView cv, Integer i) {
        home_img.setImageResource(R.drawable.ic_home_black_24dp);
        work_img.setImageResource(R.drawable.ic_apartment_bk);
        other_img.setImageResource(R.drawable.ic_location_bk);

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

}