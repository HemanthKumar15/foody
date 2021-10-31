package thulasi.hemanthkumar.foody.ui.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import thulasi.hemanthkumar.foody.ChooseAddressActivity;
import thulasi.hemanthkumar.foody.MainActivity;
import thulasi.hemanthkumar.foody.OrderDetailsActivity;
import thulasi.hemanthkumar.foody.RegistrationActivity;
import thulasi.hemanthkumar.foody.databinding.FragmentNotificationsBinding;
import thulasi.hemanthkumar.foody.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private TextView user_txt,phone_txt,edit_txt,logininfo_txt;
    private CardView add_card,order_card;
    private Button logout_btn,login_btn;
    private MKLoader loader;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        user_txt = binding.userTxt;
        phone_txt = binding.phoneTxt;
        edit_txt = binding.editBtn;
        logininfo_txt = binding.logininfoTxt;
        add_card = binding.addCard;
        order_card= binding.orderCard;
        logout_btn = binding.logoutBtn;
        login_btn = binding.loginBtn;
        loader = binding.loader;
        String num = getContext().getSharedPreferences("save", Context.MODE_PRIVATE).getString("num","non_user");
        FirebaseDatabase.getInstance().getReference().child("users").child(num).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    user_txt.setText(snapshot.child("name").getValue().toString());
                    phone_txt.setText(num);
                    loader.setVisibility(View.GONE);
                    user_txt.setVisibility(View.VISIBLE);
                    phone_txt.setVisibility(View.VISIBLE);
                    edit_txt.setVisibility(View.VISIBLE);
                    order_card.setVisibility(View.VISIBLE);
                    add_card.setVisibility(View.VISIBLE);
                    logout_btn.setVisibility(View.VISIBLE);
                }
                else {
                    loader.setVisibility(View.GONE);
                    logininfo_txt.setVisibility(View.VISIBLE);
                    login_btn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getContext(), ChooseAddressActivity.class);
                go.putExtra("amount",-1);
                startActivity(go);
            }
        });

        order_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OrderDetailsActivity.class));
            }
        });

        edit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RegistrationActivity.class).putExtra("change",true));

            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("LOG-OUT??")
                        .setMessage("Do you want to logout? Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getContext(), RegistrationActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();


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