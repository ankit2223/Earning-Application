package com.example.app.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.Adapters.TasksAdapter;
import com.example.app.Models.TasksModel;
import com.example.app.Models.UserModel;
import com.example.app.R;
import com.example.app.RedeemActivity;
import com.example.app.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    FragmentHomeBinding binding;
    ArrayList<TasksModel>list;
    TasksAdapter adapter;
    FirebaseFirestore firestore;

    Dialog dialog;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        firestore = FirebaseFirestore.getInstance();

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading_dialog);

        dialog.show();

        loadUserData();
        list = new ArrayList<>();

        list.add(new TasksModel("Spin wheel", R.drawable.spin));
        list.add(new TasksModel("Scratch card", R.drawable.card));
        list.add(new TasksModel("Guess number", R.drawable.guess));
        list.add(new TasksModel("Flip and win", R.drawable.flip));
        list.add(new TasksModel("Lucky Box", R.drawable.lucky));
        list.add(new TasksModel("Watch video", R.drawable.video));
        list.add(new TasksModel("Refer and Earn", R.drawable.refer));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvTasks.setLayoutManager((layoutManager));

        adapter = new TasksAdapter(getContext(),list);
        binding.rvTasks.setAdapter(adapter);

        binding.btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RedeemActivity.class);
                startActivity(intent);
            }
        });


        return binding.getRoot();
    }

    private void loadUserData() {
        firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel model = documentSnapshot.toObject(UserModel.class);
                        if (documentSnapshot.exists()) {
                            binding.userName.setText(model.getName());
                            binding.userCoin.setText(model.getCoins()+"");
                            binding.totalCoins.setText(model.getCoins()+"");

                            Picasso.get()
                                    .load(model.getProfile())
                                    .placeholder(R.drawable.profile)
                                    .into(binding.profileImage);

                            dialog.dismiss();
                        }

                    }
                });
    }
}