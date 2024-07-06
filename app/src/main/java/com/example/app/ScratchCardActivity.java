package com.example.app;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.example.app.AdmobAds.Admob;
import com.example.app.Models.UserModel;
import com.example.app.databinding.ActivityScratchCardBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class ScratchCardActivity extends AppCompatActivity {

    ActivityScratchCardBinding binding;
    CountDownTimer timer;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    private int currentCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScratchCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadCoin();
        resetTimer();


        Admob.loadBannerAd(findViewById(R.id.bannerAd), ScratchCardActivity.this);

        Random random = new Random();
        int val = random.nextInt(100);
        binding.scratchCoin.setText(val+"");
        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        binding.scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {

                Admob.showRewarded(ScratchCardActivity.this,true);
                Random randoms = new Random();
                int val = randoms.nextInt(100);
                binding.scratchCoin.setText(val+"");

                int scratchedCoin = Integer.parseInt(binding.scratchCoin.getText().toString());
                firestore.collection("users").document(auth.getUid())
                        .update("coins", FieldValue.increment(scratchedCoin));

                Toast.makeText(ScratchCardActivity.this, val+" Coins Added Successfully", Toast.LENGTH_SHORT).show();

                loadCoin();



                binding.scratchTime.setVisibility(View.VISIBLE);
                timer.start();


            }


            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {

                if (percent>=0.5){

                    Log.d("Reveal Percentage","onRevealPercentChangedListener:" + String.valueOf(percent));

                }
            }
        });

    }

    private void loadCoin() {
        firestore.collection("users").document(auth.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserModel model = documentSnapshot.toObject(UserModel.class);
                        if (documentSnapshot.exists()){

                            binding.totalCoins.setText(model.getCoins()+"");
                            currentCoins = model.getCoins();
                        }
                    }
                });
    }

    private void resetTimer(){


        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

                binding.scratchTime.setText(String.valueOf(l/1000));

            }


            @Override
            public void onFinish() {

                binding.scratchView.mask();
                timer.cancel();
                binding.scratchTime.setVisibility(View.GONE);
            }
        };
    }
}