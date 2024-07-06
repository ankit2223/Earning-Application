package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.app.AdmobAds.Admob;
import com.example.app.Models.UserModel;
import com.example.app.databinding.ActivityReferBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReferActivity extends AppCompatActivity {

    ActivityReferBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    private String oppositeUID;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getSupportActionBar().hide();

        Admob.loadBannerAd(findViewById(R.id.bannerAd), ReferActivity.this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

         loadData();


        binding.copyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(ReferActivity.this.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Data", binding.copyCode.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(ReferActivity.this, "Referral Code copied", Toast.LENGTH_SHORT).show();

            }
        });

        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String referCode = binding.referCode.getText().toString();

                String shareBody = "Hey, I am using best earning app. Join using my invite code to instantly get 100"
                        + "coins. My invite code is " + referCode;

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(intent);

            }
        });

    }

    private void loadData() {

        firestore.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserModel model = documentSnapshot.toObject(UserModel.class);

                        if (documentSnapshot.exists()){

                            binding.totalCoi.setText(model.getCoins()+"");
                            binding.referCode.setText(model.getReferCode());
                        }

                    }
                });


    }

}