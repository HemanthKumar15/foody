package thulasi.hemanthkumar.foody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    private String num, name,code,id;
    private FirebaseAuth mAuth;
    private TextView hint, resend;
    private OtpView totp;
    private MKLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        name = getIntent().getStringExtra("name");
        num = getIntent().getStringExtra("num");
        hint = findViewById(R.id.hint);
        hint.setText("Enter the One-Time Password (OTP) sent to "+ num + ".");
        totp = findViewById(R.id.otp_view);
        resend = findViewById(R.id.resend);
        loader = findViewById(R.id.loader);
        mAuth = FirebaseAuth.getInstance();
        SendOtp();
        totp.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                loader.setVisibility(View.VISIBLE);
                code = otp;
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, code);
                signInWithPhoneAuthCredentials(credential);
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOtp();
            }
        });

    }

    private void SendOtp() {
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long l) {
                resend.setText("Didn't received OTP? "+l/1000);
                resend.setEnabled(false);
                resend.setTextColor(Color.BLACK);
            }

            @Override
            public void onFinish() {
                resend.setText("Didn't received OTP? "+"Resend");
                resend.setEnabled(true);
                resend.setTextColor(Color.BLUE);

            }
        }.start();

         PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 num,
                 60,
                 TimeUnit.SECONDS,
                 this,
                 new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                     @Override
                     public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                         VerificationActivity.this.id = id;
                     }

                     @Override
                     public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential){
                         signInWithPhoneAuthCredentials(phoneAuthCredential);
                     }

                     @Override
                     public void onVerificationFailed(@NonNull FirebaseException e) {
                         Toast.makeText(VerificationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                     }
                 }
         );
    }
    private void signInWithPhoneAuthCredentials(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> Atask) {


                        if(Atask.isSuccessful()){
                            SharedPreferences pref = getSharedPreferences("save",MODE_PRIVATE);
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("num", num);
                            edit.apply();

                            HashMap<String,Object> map = new HashMap<>();
                            map.put("name", name);
                            map.put("num", num);

                            FirebaseDatabase.getInstance().getReference().child("users").child(num)
                                    .setValue(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                FirebaseUser user = Atask.getResult().getUser();
                                                loader.setVisibility(View.GONE);
                                                Intent go = new Intent(VerificationActivity.this, AddressActivity.class);
                                                startActivity(go);
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(VerificationActivity.this,e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                        else {
                            Toast.makeText(VerificationActivity.this, Atask.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
