package thulasi.hemanthkumar.foody.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import thulasi.hemanthkumar.foody.R;
import thulasi.hemanthkumar.foody.data.CartHandler;
import thulasi.hemanthkumar.foody.model.Cart;

public class Secondary extends FirebaseRecyclerAdapter<thulasi.hemanthkumar.foody.model.Main, Secondary.SecondaryViewHolder> {

    private Context Mcontext;
    public Secondary(@NonNull FirebaseRecyclerOptions<thulasi.hemanthkumar.foody.model.Main> options,Context context) {
        super(options);
        Mcontext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull SecondaryViewHolder holder, int i, @NonNull thulasi.hemanthkumar.foody.model.Main model) {
        holder.tname.setText(model.getName().substring(0,1).toUpperCase() + model.getName().substring(1).toLowerCase());
        holder.tprice.setText("₹ "+model.getPrice());

        Picasso.get().load(model.getImage()).into(holder.timg);
        CartHandler db = new CartHandler(Mcontext);
        Boolean cartExist = false;

        for (thulasi.hemanthkumar.foody.model.Cart cart: db.getCart()) {
            if(model.getId().toString().equals(cart.getId().toString()) ){
                cartExist = true;

                holder.tqty.setText(cart.getQty());
                break;
            }
        }

        if (!cartExist){
            holder.add.setVisibility(View.VISIBLE);

        }
        else {
            holder.add.setVisibility(View.INVISIBLE);
            holder.qtycontrol.setVisibility(View.VISIBLE);
        }



        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.add.setVisibility(View.INVISIBLE);

                CartHandler cartHandler = new CartHandler(Mcontext);
                thulasi.hemanthkumar.foody.model.Cart cart = new thulasi.hemanthkumar.foody.model.Cart();
                cart.setChild("Trending");
                cart.setName(model.getName().toString());
                cart.setPrice(model.getPrice().toString());
                cart.setTotal(model.getPrice().toString());
                cart.setId(model.getId().toString());
                cart.setQty("1");

                cartHandler.AddCart(cart,Mcontext);

                holder.qtycontrol.setVisibility(View.VISIBLE);
                holder.tqty.setText("1");

            }
        });
        holder.tadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.valueOf(holder.tqty.getText().toString());
                qty = qty + 1;
                thulasi.hemanthkumar.foody.model.Cart cart = new thulasi.hemanthkumar.foody.model.Cart();
                cart.setChild("Trending");
                cart.setName(model.getName().toString());
                cart.setPrice(model.getPrice().toString());
                cart.setId(model.getId().toString());
                cart.setQty(String.valueOf(qty));
                cart.setTotal(String.valueOf(Integer.valueOf(model.getPrice())*Integer.valueOf(qty)));
                db.updateCart(cart);

                holder.tqty.setText(String.valueOf(qty));
            }
        });
        holder.tsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.valueOf(holder.tqty.getText().toString());
                if (qty > 1) {
                    qty = qty - 1;
                    thulasi.hemanthkumar.foody.model.Cart cart = new Cart();
                    cart.setChild("Trending");
                    cart.setName(model.getName().toString());
                    cart.setPrice(model.getPrice().toString());
                    cart.setId(model.getId().toString());
                    cart.setQty(String.valueOf(qty));
                    cart.setTotal(String.valueOf(Integer.valueOf(model.getPrice())*Integer.valueOf(qty)));
                    db.updateCart(cart);
                    holder.tqty.setText(String.valueOf(qty));
                }
                else {
                    db.deleteCart(model.getId().toString());
                    holder.qtycontrol.setVisibility(View.INVISIBLE);
                    holder.add.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @NonNull
    @Override
    public SecondaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sec_card, parent, false);

        return new SecondaryViewHolder(view);
    }

    class SecondaryViewHolder extends RecyclerView.ViewHolder{
        TextView tname,tprice,tqty;
        ImageView timg;
        Button add;
        ImageButton tsub,tadd;
        LinearLayout qtycontrol;

        public SecondaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tname = itemView.findViewById(R.id.tname);
            timg = itemView.findViewById(R.id.timg);
            tprice = itemView.findViewById(R.id.tprice);
            add = itemView.findViewById(R.id.sadd);
            tadd = itemView.findViewById(R.id.padd);
            tsub = itemView.findViewById(R.id.psub);
            qtycontrol = itemView.findViewById(R.id.qtycontrol);
            tqty = itemView.findViewById(R.id.pqty);
        }
    }
}
