package thulasi.hemanthkumar.foody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.HashMap;

public class SaveAddressActivity extends AppCompatActivity {
    private EditText address, door_no, area;
    private String addressline;
    private Button confirm;
    private DatabaseReference ref;
    private SharedPreferences saves;
    private String num;
    private MKLoader loader;
    private RadioButton homeRd,workRd,otherRd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_address);
        addressline = getIntent().getStringExtra("address");
        address = findViewById(R.id.address);
        door_no = findViewById(R.id.door);
        area = findViewById(R.id.area);
        homeRd = findViewById(R.id.home_rd);
        workRd = findViewById(R.id.work_rd);
        otherRd = findViewById(R.id.other_rd);
        address.setText(addressline);
        confirm = findViewById(R.id.confirm_btn);
        saves = getSharedPreferences("save",MODE_PRIVATE);
        num = saves.getString("num","");

        ref = FirebaseDatabase.getInstance().getReference();
        loader = findViewById(R.id.loader2);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(address.getText().toString().replace(" ",""))){
                    Toast.makeText(SaveAddressActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(door_no.getText().toString().replace(" ",""))) {
                    Toast.makeText(SaveAddressActivity.this, "Please enter your House / Flat / Block No.", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(area.getText().toString().replace(" ",""))){
                    Toast.makeText(SaveAddressActivity.this, "Please enter your Apartment / Road / Area name", Toast.LENGTH_SHORT).show();

                }
                else{
                    String category = "home";
                    loader.setVisibility(View.VISIBLE);
                    if (otherRd.isChecked()){
                        category = "other";
                    }
                    else if (workRd.isChecked()){
                        category = "work";
                    }
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("address", address.getText().toString());
                    map.put("door", door_no.getText().toString());
                    map.put("area", area.getText().toString());
                    ref.child("address").child(num).child(category).setValue(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    loader.setVisibility(View.INVISIBLE);
                                    if (task.isSuccessful()){
                                        Intent go = new Intent(SaveAddressActivity.this,MainActivity.class);
                                        startActivity(go);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }
}