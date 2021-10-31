package thulasi.hemanthkumar.foody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import thulasi.hemanthkumar.foody.adapter.Order;
import thulasi.hemanthkumar.foody.adapter.OrderDetails;
import thulasi.hemanthkumar.foody.model.Cart;

public class OrderProductsActivity extends AppCompatActivity {

    private RecyclerView product_view;
    private DatabaseReference databaseReference;
    private OrderDetails adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_products);
        product_view = findViewById(R.id.product_view);
        product_view.setLayoutManager(new LinearLayoutManager(this));
        product_view.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("orders")
                .child(getSharedPreferences("save",MODE_PRIVATE).getString("num","0"))
                .child(getIntent().getStringExtra("id"));
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(databaseReference,Cart.class).build();
        adapter = new OrderDetails(options,getApplicationContext());
        product_view.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}