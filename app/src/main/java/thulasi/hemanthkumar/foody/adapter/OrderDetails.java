package thulasi.hemanthkumar.foody.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import thulasi.hemanthkumar.foody.OrderProductsActivity;
import thulasi.hemanthkumar.foody.R;
import thulasi.hemanthkumar.foody.model.Cart;
import thulasi.hemanthkumar.foody.model.Order;
import thulasi.hemanthkumar.foody.params.sqlite;

public class OrderDetails extends FirebaseRecyclerAdapter<thulasi.hemanthkumar.foody.model.Cart, OrderDetails.OrderViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context context;

    public OrderDetails(@NonNull FirebaseRecyclerOptions<Cart> options, Context context) {
        super(options);
        this.context = context;
    }



    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull thulasi.hemanthkumar.foody.model.Cart model) {
        FirebaseDatabase.getInstance().getReference().child("Products").child(model.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.pname.setText(snapshot.child("name").getValue().toString());
                Picasso.get().load(snapshot.child("image").getValue().toString()).into(holder.pimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.pqty.setText("Qty: "+model.getQty());

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_product_card, parent, false);

        return new OrderViewHolder(view);


    }

    class OrderViewHolder extends RecyclerView.ViewHolder{

        TextView pname,pqty;
        ImageView pimg;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            pname = itemView.findViewById(R.id.pname);
            pqty = itemView.findViewById(R.id.pqty);
            pimg = itemView.findViewById(R.id.pimg);
        }
    }
}
