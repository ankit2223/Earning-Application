package com.example.app;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.AdmobAds.Admob;
import com.example.app.Models.UserModel;
import com.example.app.databinding.ActivityFlipCardBinding;
import com.example.app.databinding.ActivityForgetBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class FlipCardActivity extends AppCompatActivity {

    ActivityFlipCardBinding binding;
    Dialog dialog;
    TextView wonCoins;
    Button btnGetCoins;

    private int count = 0;

    CountDownTimer timer;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlipCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.flip_dialog);

        if (dialog.getWindow() != null){

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

        }
        wonCoins = dialog.findViewById(R.id.wonCoins);
        btnGetCoins = dialog.findViewById(R.id.btnGetCoins);

        resetTimer();
        loadCoin();

        Random random = new Random();
        int val = random.nextInt(50);
        binding.num1.setText(val+"");

        Random random2 = new Random();
        int val2 = random.nextInt(60);
        binding.num2.setText(val2+"");

        Random random3 = new Random();
        int val3 = random.nextInt(30);
        binding.num3.setText(val3+"");

        Random random4 = new Random();
        int val4 = random.nextInt(98);
        binding.num4.setText(val4+"");

        binding.gift1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonCoins.setText(val+"");

//                Admob.showRewarded(FlipCardActivity.this,true);
                adCoins(val);

                binding.gift1.setVisibility(View.GONE);
                dialog.show();


            }
        });
        binding.gift2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonCoins.setText(val2+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                adCoins(val2);

                binding.gift2.setVisibility(View.GONE);
                dialog.show();


            }
        });
        binding.gift3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonCoins.setText(val3+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                adCoins(val3);

                binding.gift3.setVisibility(View.GONE);
                dialog.show();


            }
        });
        binding.gift4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonCoins.setText(val4+"");

                Admob.showRewarded(FlipCardActivity.this,true);
                adCoins(val4);

                binding.gift4.setVisibility(View.GONE);
                dialog.show();


            }
        });

        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnGetCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int availableCoins = Integer.parseInt(binding.totalCoins.getText().toString());
                int won = Integer.parseInt(wonCoins.getText().toString());

                int finalCoins = availableCoins + won;
                binding.totalCoins.setText(finalCoins+"");
                dialog.dismiss();

                binding.flipCardTimer.setVisibility(View.VISIBLE);
                timer.start();
                cardDisable();
            }
        });


    }

    private void resetTimer() {

        timer = new CountDownTimer(5000, 1000 ) {
            @Override
            public void onTick(long l) {

                binding.flipCardTimer.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                timer.cancel();
                binding.flipCardTimer.setVisibility(View.GONE);
                cardEnable();

            }
        };
    }

    private void cardEnable() {

        binding.gift1.setEnabled(true);
        binding.gift2.setEnabled(true);
        binding.gift3.setEnabled(true);
        binding.gift4.setEnabled(true);
    }

    private void cardDisable() {

        binding.gift1.setEnabled(false);
        binding.gift2.setEnabled(false);
        binding.gift3.setEnabled(false);
        binding.gift4.setEnabled(false);
    }

    private void adCoins(int val) {

        firestore.collection("users").document(auth.getUid())
                .update("coins", FieldValue.increment(val));
    }

    private void loadCoin() {

        firestore.collection("users").document(auth.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserModel model = documentSnapshot.toObject(UserModel.class);
                        if (documentSnapshot.exists()){

                            binding.totalCoins.setText(model.getCoins()+"");
                        }
                    }
                });
    }
}