package thulasi.hemanthkumar.foody.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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

import thulasi.hemanthkumar.foody.MainActivity;
import thulasi.hemanthkumar.foody.R;
import thulasi.hemanthkumar.foody.data.CartHandler;
import thulasi.hemanthkumar.foody.model.Cart;
import thulasi.hemanthkumar.foody.model.Order;

public class Main extends FirebaseRecyclerAdapter<thulasi.hemanthkumar.foody.model.Main, Main.MainViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context Mcontext;
    public Main(@NonNull FirebaseRecyclerOptions<thulasi.hemanthkumar.foody.model.Main> options,Context context) {
        super(options);
        Mcontext = context;
    }



    @Override
    protected void onBindViewHolder(@NonNull MainViewHolder holder, int i, @NonNull thulasi.hemanthkumar.foody.model.Main model) {
        holder.pname.setText(model.getName().substring(0,1).toUpperCase() + model.getName().substring(1).toLowerCase());
        holder.pprice.setText("₹ "+model.getPrice());

        Picasso.get().load(model.getImage()).into(holder.pimage);
        CartHandler db = new CartHandler(Mcontext);
        Boolean cartExist = false;

        for (thulasi.hemanthkumar.foody.model.Cart cart: db.getCart()) {
            if(model.getId().toString().equals(cart.getId().toString()) ){
                cartExist = true;

                holder.pqty.setText(cart.getQty());
                break;
            }
        }

        if (!cartExist){
                holder.add.setVisibility(View.VISIBLE);

            }
        else {
                holder.qtycontrol.setVisibility(View.VISIBLE);
            }



        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.add.setVisibility(View.INVISIBLE);

                CartHandler cartHandler = new CartHandler(Mcontext);
                Cart cart = new Cart();
                cart.setChild("Products");
                cart.setName(model.getName().toString());
                cart.setPrice(model.getPrice().toString());
                cart.setTotal(model.getPrice().toString());
                cart.setId(model.getId().toString());
                cart.setQty("1");

                cartHandler.AddCart(cart,Mcontext);

                holder.qtycontrol.setVisibility(View.VISIBLE);
                holder.pqty.setText("1");

            }
        });
        holder.padd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.valueOf(holder.pqty.getText().toString());
                qty = qty + 1;
                Cart cart = new Cart();
                cart.setChild("Products");
                cart.setName(model.getName().toString());
                cart.setPrice(model.getPrice().toString());
                cart.setId(model.getId().toString());
                cart.setQty(String.valueOf(qty));
                cart.setTotal(String.valueOf(Integer.valueOf(model.getPrice())*Integer.valueOf(qty)));
                db.updateCart(cart);

                holder.pqty.setText(String.valueOf(qty));
            }
        });
        holder.psub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.valueOf(holder.pqty.getText().toString());
                if (qty > 1) {
                    qty = qty - 1;
                    Cart cart = new Cart();
                    cart.setChild("Products");
                    cart.setName(model.getName().toString());
                    cart.setPrice(model.getPrice().toString());
                    cart.setId(model.getId().toString());
                    cart.setQty(String.valueOf(qty));
                    cart.setTotal(String.valueOf(Integer.valueOf(model.getPrice())*Integer.valueOf(qty)));
                    db.updateCart(cart);
                    holder.pqty.setText(String.valueOf(qty));
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
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_card, parent, false);

        return new MainViewHolder(view);
    }

    class MainViewHolder extends RecyclerView.ViewHolder{
        TextView pname,pprice,pqty;
        ImageView pimage;
        Button add;
        ImageButton psub,padd;
        LinearLayout qtycontrol;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            pname = itemView.findViewById(R.id.pname);
            pimage = itemView.findViewById(R.id.pimage);
            pprice = itemView.findViewById(R.id.pprice);
            add = itemView.findViewById(R.id.add);
            padd = itemView.findViewById(R.id.padd);
            pqty = itemView.findViewById(R.id.pqty);
            psub = itemView.findViewById(R.id.psub);
            qtycontrol = itemView.findViewById(R.id.qtycontrol);
        }
    }
}
