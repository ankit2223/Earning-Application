package com.example.app.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app.LoginActivity;
import com.example.app.Models.UserModel;
import com.example.app.PaymentRequestActivity;
import com.example.app.R;
import com.example.app.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    FirebaseAuth auth;

    FirebaseFirestore firestore;

    FirebaseStorage storage;
    Uri profileUri;

    ProgressDialog progressDialog;

    Dialog dialog;

    public ProfileFragment() {
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Profile Uploading");
        progressDialog.setMessage("We are uploading your profile");

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading_dialog);

        dialog.show();

        loadUserData();

        binding.fetchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

        binding.privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( Intent.ACTION_VIEW,Uri.   parse("http://www.youtube.com/@Anket-mp7zr")));
            }
        });
        binding.terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( Intent.ACTION_VIEW,Uri.   parse("http://www.youtube.com/")));
            }
        });

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String shareBody = "Hey, I am using best Earning App";
               Intent intent = new Intent(Intent.ACTION_SEND);
               intent.setType("type/plain");
               intent.putExtra(Intent.EXTRA_TEXT,shareBody);
               startActivity(intent);
            }
        });

        binding.transactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PaymentRequestActivity.class);
                startActivity(intent);
            }
        });

        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                            binding.usersName.setText(model.getName());
                            binding.userEmail.setText(model.getEmail());


                            Picasso.get()
                                    .load(model.getProfile())
                                    .placeholder(R.drawable.profile)
                                    .into(binding.profileImage);

                            dialog.dismiss();

                        }

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==2){
            if (data!=null){
                profileUri = data.getData();
                binding.profileImage.setImageURI(profileUri);

                updateProfile(profileUri);
            }
        }
    }

    private void updateProfile(Uri profileUri) {
        progressDialog.show();

        final StorageReference reference = storage.getReference().child("profile").child(FirebaseAuth.getInstance().getUid());

        reference.putFile(profileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                                        .update("profile",uri.toString());
                                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

                    }
                });
    }
}

