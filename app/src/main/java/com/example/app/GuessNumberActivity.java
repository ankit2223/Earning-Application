package com.example.app;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.AdmobAds.Admob;
import com.example.app.Models.UserModel;
import com.example.app.databinding.ActivityGuessNumberBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class GuessNumberActivity extends AppCompatActivity {

    ActivityGuessNumberBinding binding;

    CountDownTimer timer;

    private int givenNumber, guessNumber;

    FirebaseAuth auth;

    FirebaseFirestore firestore;

    private int currentCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuessNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        resetTimer();
        loadCoin();

        Random random = new Random();
        givenNumber = random.nextInt(60);
        binding.firstCardNum.setText(givenNumber+"");

        Random random2 = new Random();
        guessNumber = random2.nextInt(60);
        binding.secondCardNum.setText(guessNumber+"");

        binding.btnHigh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Admob.showRewarded(GuessNumberActivity.this, true);

                    binding.question.setVisibility(View.GONE);
                    binding.secondCardNum.setVisibility(View.VISIBLE);
                    binding.timer.setVisibility(View.VISIBLE);
                    timer.start();

                    if (guessNumber>givenNumber){
                        firestore.collection("users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .update("coins", FieldValue.increment(100));

                        loadCoin();

                        binding.result.setText("You Win 100 Coins");
                        binding.result.setVisibility(View.VISIBLE);
                        binding.btnHigh.setEnabled(false);
                        binding.btnLow.setEnabled(false);
                    }
                    else {

                        firestore.collection("users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .update("coins", FieldValue.increment(-50));

                        loadCoin();

                        binding.result.setText("You loss 50 Coins");
                        binding.result.setVisibility(View.VISIBLE);
                        binding.btnHigh.setEnabled(false);
                        binding.btnLow.setEnabled(false);
                    }
                }
            });

        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        binding.view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        binding.btnLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Admob.showRewarded(GuessNumberActivity.this, true);

                binding.question.setVisibility(View.GONE);
                binding.secondCardNum.setVisibility(View.VISIBLE);
                binding.timer.setVisibility(View.VISIBLE);
                timer.start();

                if (guessNumber>givenNumber){
                    firestore.collection("users")
                            .document(FirebaseAuth.getInstance().getUid())
                            .update("coins", FieldValue.increment(100));

                    loadCoin();

                    binding.result.setText("You Win 100 Coins");
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
                }
                else {

                    firestore.collection("users")
                            .document(FirebaseAuth.getInstance().getUid())
                            .update("coins", FieldValue.increment(-50));

                    loadCoin();

                    binding.result.setText("You loss 50 Coins");
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
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
                            currentCoin = model.getCoins();
                        }
                    }
                });
    }
    private void resetTimer(){


        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

                binding.timer.setText(String.valueOf(l/1000));

            }


            @Override
            public void onFinish() {

                binding.question.setVisibility(View.VISIBLE);
                binding.secondCardNum.setVisibility(View.GONE);
                timer.cancel();

                binding.timer.setVisibility(View.GONE);

                Random random = new Random();
                givenNumber = random.nextInt(60);
                binding.firstCardNum.setText(givenNumber+"");


                Random random2 = new Random();
                guessNumber = random2.nextInt(60);
                binding.secondCardNum.setText(guessNumber+"");

                binding.btnLow.setEnabled(true);
                binding.btnHigh.setEnabled(true);
                binding.result.setEnabled(true);

            }
        };
    }
}