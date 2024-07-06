package com.example.app;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.AdmobAds.Admob;
import com.example.app.Models.UserModel;
import com.example.app.databinding.ActivityLuckyBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class LuckyActivity extends AppCompatActivity {

    ActivityLuckyBinding binding;
    private int count = 0;
    CountDownTimer timer;
    FirebaseFirestore firestore;
    private int currentCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLuckyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getSupportActionBar().hide();

        Admob.loadBannerAd(findViewById(R.id.bannerAd),LuckyActivity.this);

        firestore = FirebaseFirestore.getInstance();
        resetTimer();

        firestore.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserModel model = documentSnapshot.toObject(UserModel.class);

                        if (documentSnapshot.exists()){

                            binding.yourCoin.setText(model.getCoins()+"");
                            currentCoin = model.getCoins();
                        }

                    }
                });

        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        binding.luckyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Admob.showInterstitial(LuckyActivity.this,true);

                timer.start();
                binding.luckAnimation.playAnimation();

                Random random = new Random();
                int val = random.nextInt(5);
                binding.luckyWon.setText(val+"");

                firestore
                        .collection("users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .update("coins", FieldValue.increment(val));

                String ww = binding.luckyWon.getText().toString();

                binding.luckyBox.setVisibility(View.GONE);
                binding.luckAnimation.setVisibility(View.VISIBLE);
                binding.luckyWon.setVisibility(View.VISIBLE);
                binding.luckyTimer.setVisibility(View.VISIBLE);

                firestore.collection("users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                UserModel model = documentSnapshot.toObject(UserModel.class);
                                if (documentSnapshot.exists()){

                                    binding.yourCoin.setText(model.getCoins()+"");
                                    currentCoin = model.getCoins();

                                }

                            }
                        });


            }
        });


    }

    private void resetTimer(){

        timer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millsFinished) {
                binding.luckyTimer.setText(String.valueOf(millsFinished/1000));
            }
            @Override
            public void onFinish() {

                timer.cancel();
                binding.luckyTimer.setVisibility(View.GONE);
                binding.luckyBox.setVisibility(View.VISIBLE);
                binding.luckAnimation.pauseAnimation();
                binding.luckAnimation.setVisibility(View.GONE);
                binding.luckyWon.setVisibility(View.GONE);

            }
        };

    }

}