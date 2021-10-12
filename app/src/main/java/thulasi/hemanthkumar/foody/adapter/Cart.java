package thulasi.hemanthkumar.foody.adapter;

import android.content.Context;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

import thulasi.hemanthkumar.foody.R;
import thulasi.hemanthkumar.foody.data.CartHandler;
import thulasi.hemanthkumar.foody.ui.notifications.NotificationsFragment;

public class Cart extends RecyclerView.Adapter<Cart.ViewHolder> {
    private Context context;
    private List<thulasi.hemanthkumar.foody.model.Cart> cartList;
    private NotificationsFragment fContext;



    public Cart(Context context, NotificationsFragment fContext, List<thulasi.hemanthkumar.foody.model.Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
        this.fContext = fContext;
    }

    @NonNull
    @Override
    public Cart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Cart.ViewHolder holder, int position) {
        thulasi.hemanthkumar.foody.model.Cart cart = cartList.get(position);
        holder.pname.setText(cart.getName().substring(0,1).toUpperCase() + cart.getName().substring(1).toLowerCase());
        holder.pprice.setText("₹ "+cart.getPrice()+" per plate");
        holder.pqty.setText(cart.getQty());
        Picasso.get().load(cart.getImg()).into(holder.pimage);
        holder.qtycontrol.setVisibility(View.VISIBLE);
        CartHandler db = new CartHandler(context);

        holder.padd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.valueOf(holder.pqty.getText().toString());
                qty = qty + 1;
                thulasi.hemanthkumar.foody.model.Cart Ucart = new thulasi.hemanthkumar.foody.model.Cart();
                Ucart.setChild(cart.getChild());
                Ucart.setName(cart.getName().toString());
                Ucart.setPrice(cart.getPrice().toString());
                Ucart.setId(cart.getId().toString());
                Ucart.setQty(String.valueOf(qty));
                Ucart.setTotal(String.valueOf(Integer.valueOf(cart.getPrice())*Integer.valueOf(qty)));
                db.updateCart(Ucart);
                NotificationsFragment.UpdateAmount(context);
                holder.pqty.setText(String.valueOf(qty));
            }
        });
        holder.psub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.valueOf(holder.pqty.getText().toString());
                if (qty > 1) {
                    qty = qty - 1;
                    thulasi.hemanthkumar.foody.model.Cart Ucart = new thulasi.hemanthkumar.foody.model.Cart();
                    Ucart.setChild(cart.getChild());
                    Ucart.setName(cart.getName().toString());
                    Ucart.setPrice(cart.getPrice().toString());
                    Ucart.setId(cart.getId().toString());
                    Ucart.setQty(String.valueOf(qty));
                    Ucart.setTotal(String.valueOf(Integer.valueOf(cart.getPrice())*Integer.valueOf(qty)));
                    db.updateCart(Ucart);
                    NotificationsFragment.UpdateAmount(context);
                    holder.pqty.setText(String.valueOf(qty));
                }
                else {
                    db.deleteCart(cart.getId().toString());
                    NotificationsFragment.RefreshCart(fContext);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView pname,pprice,pqty;
        ImageView pimage;
        ImageButton psub,padd;
        Button add;
        LinearLayout qtycontrol;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            pname = itemView.findViewById(R.id.pname);
            pimage = itemView.findViewById(R.id.pimage);
            pprice = itemView.findViewById(R.id.pprice);
            add = itemView.findViewById(R.id.add);
            padd = itemView.findViewById(R.id.padd);
            pqty = itemView.findViewById(R.id.pqty);
            psub = itemView.findViewById(R.id.psub);
            qtycontrol = itemView.findViewById(R.id.qtycontrol);



        }



        @Override
        public void onClick(View v) {

        }
    }
}
