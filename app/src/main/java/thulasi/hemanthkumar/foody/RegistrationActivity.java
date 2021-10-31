package thulasi.hemanthkumar.foody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {

	private EditText name,phone;
	private TextView title;
	private FloatingActionButton next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		name = findViewById(R.id.name);
		phone = findViewById(R.id.phone);
		next = findViewById(R.id.next);
		title = findViewById(R.id.titlechange);
		if(getIntent().getBooleanExtra("change",false)){
			title.setText("Edit profile");
			phone.setText(getSharedPreferences("save",MODE_PRIVATE).getString("num","0").substring(3));
			FirebaseDatabase.getInstance().getReference().child("users")
					.child(getSharedPreferences("save",MODE_PRIVATE).getString("num","non user"))
					.addValueEventListener(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot snapshot) {
							if(snapshot.exists()){
								name.setText(snapshot.child("name").getValue().toString());
							}
						}

						@Override
						public void onCancelled(@NonNull DatabaseError error) {

						}
					});
		}

		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String fname = name.getText().toString();
				String fphone = phone.getText().toString().replace(" ","");
				if(TextUtils.isEmpty(fname)){
					Toast.makeText(RegistrationActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
				}
				else if(fphone.length() != 10){
					Toast.makeText(RegistrationActivity.this, "Please enter a 10-digit number", Toast.LENGTH_SHORT).show();
				}
				else {
					Intent go = new Intent(RegistrationActivity.this,VerificationActivity.class);
					go.putExtra("name", fname);
					go.putExtra("num", "+91"+fphone);
					startActivity(go);
				}
			}
		});
	}
}
