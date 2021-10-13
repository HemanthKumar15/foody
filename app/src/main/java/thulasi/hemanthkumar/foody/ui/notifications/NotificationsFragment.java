package thulasi.hemanthkumar.foody.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thulasi.hemanthkumar.foody.AddressActivity;
import thulasi.hemanthkumar.foody.ChooseAddressActivity;
import thulasi.hemanthkumar.foody.adapter.Cart;
import thulasi.hemanthkumar.foody.data.CartHandler;
import thulasi.hemanthkumar.foody.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    public FragmentNotificationsBinding binding;
    private RecyclerView cartView;
    private Cart cartAdapter;
    private ArrayList<thulasi.hemanthkumar.foody.model.Cart> cartitems;
    private ArrayAdapter<String> arrayAdapter;
    private Integer amount;
    public static TextView tamount;
    private Button proceed;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        proceed = binding.proceed;

        final TextView textView = binding.textNotifications;
        cartView = binding.cartrecycler;
        cartView.setHasFixedSize(true);
        cartView.setLayoutManager(new LinearLayoutManager(getContext()));
        tamount = binding.tamount;
        amount = 0;
        cartitems = new ArrayList<>();
        CartHandler db = new CartHandler(getContext());

        List<thulasi.hemanthkumar.foody.model.Cart> cartList = db.getCart();


        FirebaseDatabase.getInstance().getReference().addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (thulasi.hemanthkumar.foody.model.Cart cart: cartList) {
                                thulasi.hemanthkumar.foody.model.Cart items = new thulasi.hemanthkumar.foody.model.Cart();
                                String pc_name = snapshot.child(cart.getChild()).child(cart.getId().toString()).child("name").getValue().toString();
                                String pc_img = snapshot.child(cart.getChild()).child(cart.getId().toString()).child("image").getValue().toString();
                                items.setName(pc_name);
                                items.setImg(pc_img);
                                items.setId(cart.getId().toString());
                                items.setQty(cart.getQty().toString());
                                items.setPrice(cart.getPrice().toString());
                                items.setTotal(cart.getTotal().toString());
                                items.setChild(cart.getChild().toString());

                                cartitems.add(items);


                            }
                            UpdateAmount(getContext());

                            cartAdapter = new Cart(getContext(),NotificationsFragment.this,cartitems);
                            cartView.setAdapter(cartAdapter);



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
            );



        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });


        return root;
    }

    private void CheckData() {
        SharedPreferences pref = getActivity().getSharedPreferences("save",Context.MODE_PRIVATE);
        Intent go = new Intent(getContext(),ChooseAddressActivity.class);
        go.putExtra("amount",amount);
        startActivity(go);

    }

    public static void UpdateAmount(Context context) {
        CartHandler db = new CartHandler(context);
        List<thulasi.hemanthkumar.foody.model.Cart> cartList = db.getCart();
        Integer amount = 0;
        for (thulasi.hemanthkumar.foody.model.Cart cart: cartList) {
            amount = amount + Integer.valueOf(cart.getTotal());
        }
        tamount.setText("₹ "+amount);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
    public static void RefreshCart(NotificationsFragment context){

        context.ReloadFragment();
    }
    public void ReloadFragment(){

        getFragmentManager().beginTransaction().detach(NotificationsFragment.this).commit();
        getFragmentManager().beginTransaction().attach(NotificationsFragment.this).commit();

    }
}