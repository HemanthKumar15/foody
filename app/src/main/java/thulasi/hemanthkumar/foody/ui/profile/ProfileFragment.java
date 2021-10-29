package thulasi.hemanthkumar.foody.ui.profile;

import android.content.Context;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import thulasi.hemanthkumar.foody.databinding.FragmentNotificationsBinding;
import thulasi.hemanthkumar.foody.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private TextView user_txt,phone_txt,edit_txt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        user_txt = binding.userTxt;
        phone_txt = binding.phoneTxt;
        edit_txt = binding.editBtn;
        String num = getContext().getSharedPreferences("save", Context.MODE_PRIVATE).getString("num","0");
        FirebaseDatabase.getInstance().getReference().child("users").child(num).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    user_txt.setVisibility(View.VISIBLE);
                    phone_txt.setVisibility(View.VISIBLE);
                    edit_txt.setVisibility(View.VISIBLE);
                    user_txt.setText(snapshot.child("name").getValue().toString());
                    phone_txt.setText(num);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}