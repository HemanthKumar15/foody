package thulasi.hemanthkumar.foody.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import thulasi.hemanthkumar.foody.MapsActivity;
import thulasi.hemanthkumar.foody.OrderDetailsActivity;
import thulasi.hemanthkumar.foody.OrderProductsActivity;
import thulasi.hemanthkumar.foody.R;
import thulasi.hemanthkumar.foody.params.sqlite;

public class Order extends FirebaseRecyclerAdapter<thulasi.hemanthkumar.foody.model.Order, Order.OrderViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context context;

    public Order(@NonNull FirebaseRecyclerOptions<thulasi.hemanthkumar.foody.model.Order> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull thulasi.hemanthkumar.foody.model.Order model) {

        String exp_time = null;
        DateFormat timeformatter = new SimpleDateFormat("hh:mm:ss");
        DateFormat newtimeformatter = new SimpleDateFormat("hh:mm");

        try {
            Date time = timeformatter.parse(model.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            calendar.add(Calendar.MINUTE, 30);
            time = calendar.getTime();
            exp_time = newtimeformatter.format(time);
            holder.exp_txt.setText("Expected Time: "+exp_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.order_id.setText("ORDER #"+ String.valueOf(sqlite.ORDER_INDEX));
        sqlite.ORDER_INDEX = (Integer.valueOf(sqlite.ORDER_INDEX)+1);
        holder.status_txt.setText("Status: "+model.getStatus());
        holder.price_txt.setText("₹ "+model.getTotal());
        holder.seeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(context, OrderProductsActivity.class);
                go.putExtra("id",model.getId());
                go.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(go);
            }
        });

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_card, parent, false);

        return new OrderViewHolder(view);


    }

    class OrderViewHolder extends RecyclerView.ViewHolder{

        TextView exp_txt,price_txt,order_id,status_txt;
        Button seeBtn;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            exp_txt = itemView.findViewById(R.id.exp_time);
            price_txt = itemView.findViewById(R.id.price);
            order_id = itemView.findViewById(R.id.order_id);
            status_txt = itemView.findViewById(R.id.status);
            seeBtn = itemView.findViewById(R.id.detail_btn);
        }
    }
}
