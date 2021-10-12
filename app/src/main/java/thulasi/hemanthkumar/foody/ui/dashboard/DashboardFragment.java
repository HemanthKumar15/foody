package thulasi.hemanthkumar.foody.ui.dashboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import thulasi.hemanthkumar.foody.R;
import thulasi.hemanthkumar.foody.adapter.Main;
import thulasi.hemanthkumar.foody.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private RecyclerView searchView;
    private EditText search;
    private Main adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        search = binding.search;
        searchView = binding.searchView;
        searchView.setHasFixedSize(true);
        searchView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<thulasi.hemanthkumar.foody.model.Main> options =
                new FirebaseRecyclerOptions.Builder<thulasi.hemanthkumar.foody.model.Main>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products"), thulasi.hemanthkumar.foody.model.Main.class)
                        .build();
        adapter = new thulasi.hemanthkumar.foody.adapter.Main(options,getContext());
        searchView.setAdapter(adapter);



        search.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    searchView.setLayoutManager(new LinearLayoutManager(getContext()));
                    FirebaseRecyclerOptions<thulasi.hemanthkumar.foody.model.Main> options =
                            new FirebaseRecyclerOptions.Builder<thulasi.hemanthkumar.foody.model.Main>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("name").startAt(s.toString().toLowerCase()), thulasi.hemanthkumar.foody.model.Main.class)
                                    .build();
                    adapter = new thulasi.hemanthkumar.foody.adapter.Main(options,getContext());
                    searchView.setAdapter(adapter);
                    adapter.startListening();


                }

            }
        });


        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
        adapter.startListening();
    }
}