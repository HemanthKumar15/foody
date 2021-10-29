package thulasi.hemanthkumar.foody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import thulasi.hemanthkumar.foody.model.Main;
import thulasi.hemanthkumar.foody.model.Order;

public class OrderDetailsActivity extends AppCompatActivity {

    private RecyclerView orderView;
    private DatabaseReference OrderRef;
    private thulasi.hemanthkumar.foody.adapter.Order adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        orderView = findViewById(R.id.orderView);
        OrderRef = FirebaseDatabase.getInstance().getReference().child("orderdetails")
                .child(getSharedPreferences("save",MODE_PRIVATE).getString("num","0"));
        orderView.setHasFixedSize(true);
        orderView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Order> options =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(OrderRef, thulasi.hemanthkumar.foody.model.Order.class)
                        .build();
        adapter = new thulasi.hemanthkumar.foody.adapter.Order(options,getApplicationContext());
        orderView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}