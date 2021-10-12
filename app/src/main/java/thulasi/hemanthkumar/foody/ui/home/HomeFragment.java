package thulasi.hemanthkumar.foody.ui.home;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import thulasi.hemanthkumar.foody.MainActivity;
import thulasi.hemanthkumar.foody.R;
import thulasi.hemanthkumar.foody.adapter.Secondary;
import thulasi.hemanthkumar.foody.data.CartHandler;
import thulasi.hemanthkumar.foody.databinding.FragmentHomeBinding;
import thulasi.hemanthkumar.foody.model.Cart;
import thulasi.hemanthkumar.foody.model.Main;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private RecyclerView recycler,secview;

    private DatabaseReference ProRef;
    private thulasi.hemanthkumar.foody.adapter.Main adapter;
    private thulasi.hemanthkumar.foody.adapter.Secondary SecAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        recycler = binding.main;
        secview = binding.secview;

        ProRef = FirebaseDatabase.getInstance().getReference().child("Products");
        secview.setHasFixedSize(true);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Main> options =
                new FirebaseRecyclerOptions.Builder<Main>()
                        .setQuery(ProRef, Main.class)
                        .build();
        adapter = new thulasi.hemanthkumar.foody.adapter.Main(options,getContext());
        recycler.setAdapter(adapter);

        secview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        FirebaseRecyclerOptions<Main> SecOptions = new FirebaseRecyclerOptions.Builder<Main>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Trending"),Main.class)
                .build();
        SecAdapter = new Secondary(SecOptions,getContext());
        secview.setAdapter(SecAdapter);






        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onStart() {
        super.onStart();
//        CartHandler cartHandler = new CartHandler(getContext());
//        Cart cart = new Cart();
//        cart.setChild("Trending");
//        cart.setName("Name");
//        cart.setPrice("56");
//        cart.setId("456jk");
//        cart.setQty("5");
//
//        cartHandler.AddCart(cart);
        CartHandler cartHandler = new CartHandler(getContext());





    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SecAdapter.startListening();
        adapter.startListening();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        SecAdapter.startListening();
        adapter.startListening();
    }
}