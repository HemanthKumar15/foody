package thulasi.hemanthkumar.foody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import thulasi.hemanthkumar.foody.data.CartHandler;
import thulasi.hemanthkumar.foody.model.Cart;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private TextView cashtxt;
    private TextView cardtxt;
    private Integer amount;
    private CardView cashcard,onlinecard;
    private MKLoader loader;
    private String ProductId;
    private DatabaseReference child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        child = FirebaseDatabase.getInstance().getReference().child("orders")
                .child(getSharedPreferences("save",MODE_PRIVATE).getString("num","0"));
        cashtxt = findViewById(R.id.ta1);
        cardtxt = findViewById(R.id.ta2);
        loader = findViewById(R.id.loader);
        cashcard = findViewById(R.id.cashcard);
        onlinecard = findViewById(R.id.onlinecard);

        amount = getIntent().getIntExtra("amount",0);

        cashtxt.setText("₹ "+amount.toString());
        cardtxt.setText("₹ "+amount.toString());
        cashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runloader();
                sendData(child);
            }
        });
        onlinecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runloader();
                sendData(FirebaseDatabase.getInstance().getReference().child("orders")
                        .child(getSharedPreferences("save",MODE_PRIVATE).getString("num","0")));
                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_OPNTnwAh6MYbR5");
                checkout.setImage(R.drawable.foody);
                // JSON Object
                JSONObject object = new JSONObject();
                try {
                    object.put("name","Hemanth Kumar");
                    object.put("description","Sending test money");
                    object.put("theme.color","#FF5722");
                    object.put("currency","INR");
                    object.put("amount",Math.round(Float.parseFloat(amount.toString())*100));
                    object.put("prefill.contact",getSharedPreferences("save",MODE_PRIVATE).getString("num","0"));


                    checkout.open(PaymentActivity.this,object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void sendData(DatabaseReference child) {
        CartHandler db = new CartHandler(this);
        List<Cart> items = db.getCart();
        Integer amount = 0;
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat DateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat TimeFormatter = new SimpleDateFormat("HH:mm:ss");
        String currentDate = DateFormatter.format(calender.getTime());
        String currentTime = TimeFormatter.format(calender.getTime());
        ProductId = currentDate+currentTime;
        for (thulasi.hemanthkumar.foody.model.Cart cart: items) {

            HashMap<String,Object> map = new HashMap<>();

            map.put("id",cart.getId());
            map.put("name",cart.getName());
            map.put("price",cart.getPrice());
            map.put("qty",cart.getQty());
            map.put("img",cart.getImg());

            child.child(ProductId).child(cart.getId()).child(ProductId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }

    private void runloader() {
        cashcard.setEnabled(false);
        onlinecard.setEnabled(false);
        loader.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPaymentSuccess(String s) {

        String Cname = getSharedPreferences("save",MODE_PRIVATE).getString("name","0");
        String Cnum = getSharedPreferences("save",MODE_PRIVATE).getString("num","0");
        HashMap<String,Object> map = new HashMap<>();

        map.put("num",Cnum);
        map.put("name",Cname);
        map.put("place",getIntent().getStringExtra("place"));
        map.put("Total",amount);
        map.put("payid",s);
        map.put("payMethod","Online paid");

        FirebaseDatabase.getInstance().getReference().child("orderdetails").child(Cnum)
                .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    runAlert(s);
                }
            }
        });
    }

    private void runAlert(String s) {


        Intent go = new Intent(PaymentActivity.this,SuccessfulPaymentActivity.class);
        go.putExtra("id",s);
        startActivity(go);
        finish();


    }

    @Override
    public void onPaymentError(int i, String s) {
        FirebaseDatabase.getInstance().getReference().child("orders")
                .child(getSharedPreferences("save",MODE_PRIVATE).getString("num","0")).child(ProductId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }
                    }
                });
        Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();

    }
}