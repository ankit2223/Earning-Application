package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.Adapters.PaymentRequestModel;
import com.example.app.Adapters.PaypalRedeemAdapter;
import com.example.app.Adapters.RedeemAdapter;
import com.example.app.Models.RedeemModel;
import com.example.app.Models.UserModel;
import com.example.app.databinding.ActivityRedeemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RedeemActivity extends AppCompatActivity {

    ActivityRedeemBinding binding;
    ArrayList<RedeemModel> list;
    ArrayList<RedeemModel> paypalList;
    RedeemAdapter adapter;
    PaypalRedeemAdapter paypalRedeemAdapter;
    Dialog dialog;
    AppCompatButton cancelBtn,redeemBtn;
    ImageView paymentMethodLog;
    TextView paymentMethod;
    EditText edtAmount,edtNumber;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    int availableCoin;
    //int requestCoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRedeemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.payment_dialog);

        if (dialog.getWindow() !=null){

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }



        cancelBtn = dialog.findViewById(R.id.cancelBtn);
        redeemBtn = dialog.findViewById(R.id.redeemAmountBtn);
        edtAmount = dialog.findViewById(R.id.edtAmount);
        edtNumber = dialog.findViewById(R.id.tranNumber);
        paymentMethodLog = dialog.findViewById(R.id.trLogo);
        paymentMethod = dialog.findViewById(R.id.payMethods);

        list = new ArrayList<>();

        list.add(new RedeemModel(10000,R.drawable.payt));
        list.add(new RedeemModel(15000,R.drawable.payt));
        list.add(new RedeemModel(25000,R.drawable.payt));
        list.add(new RedeemModel(40000,R.drawable.payt));
        list.add(new RedeemModel(45000,R.drawable.payt));
        list.add(new RedeemModel(50000,R.drawable.payt));


        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.recyPaytm.setLayoutManager(layoutManager1);

        adapter = new RedeemAdapter(this, list, new RedeemAdapter.AddListener() {
            @Override
            public void onLongClick(int position) {

                if (position==0){

                    checkCoinPaytm("please collect more coins",10000);

                } else if (position==1) {

                    checkCoinPaytm("please collect more coins",15000);

                }
                else if (position==2) {

                    checkCoinPaytm("please collect more coins",25000);

                }
                else if (position==3) {

                    checkCoinPaytm("please collect more coins",40000);

                }
                else if (position==4) {

                    checkCoinPaytm("please collect more coins",45000);

                }
                else if (position==5) {

                    checkCoinPaytm("please collect more coins",50000);

                }

            }
        });
        binding.recyPaytm.setAdapter(adapter);

        paypalList = new ArrayList<>();


        paypalList.add(new RedeemModel(415000,R.drawable.paypal));
        paypalList.add(new RedeemModel(830000,R.drawable.paypal));
        paypalList.add(new RedeemModel(900000,R.drawable.paypal));
        paypalList.add(new RedeemModel(950000,R.drawable.paypal));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.recyPaypal.setLayoutManager(layoutManager);

        paypalRedeemAdapter = new PaypalRedeemAdapter(this, paypalList, new PaypalRedeemAdapter.AddListener() {
            @Override
            public void onLongClick(int position) {

                if (position==0){

                  checkCoin("please collect more coins",415000);

                } else if (position==1) {

                    checkCoin("please collect more coins",830000);

                }
                else if (position==2) {

                    checkCoin("please collect more coins",900000);

                }
                else if (position==3) {

                    checkCoin("please collect more coins",950000);

                }

            }
        });
        binding.recyPaypal.setAdapter(paypalRedeemAdapter);

        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String withdrawalMethod = paymentMethod.getText().toString();
                int requestCoin = Integer.parseInt(edtAmount.getText().toString());
                uploadRedeemReq(withdrawalMethod,requestCoin);

            }
        });


    }

    private void getData() {

        firestore.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserModel model = documentSnapshot.toObject(UserModel.class);
                        if (documentSnapshot.exists()){

                            availableCoin = model.getCoins();
                            binding.totalCoi.setText(model.getCoins()+"");
                            binding.avilableCoins.setText(model.getCoins()+"");
                            availableCoin = model.getCoins();

                        }

                    }
                });

    }

    private void uploadRedeemReq(String withdrawalMethod, int requestCoin) {

        int withdCoin = Integer.parseInt(edtAmount.getText().toString());
        String mobNumber = edtNumber.getText().toString();

        Calendar calForDate= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("dd-MM-yy");
        String date= currentDate.format(calForDate.getTime());

        PaymentRequestModel model = new PaymentRequestModel(
                withdrawalMethod,
                mobNumber,
                "false",
                date,
                withdCoin
        );

        

        firestore.collection("redeem").document(FirebaseAuth.getInstance().getUid()).collection("request").document()
                .set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    upDateCoin(requestCoin);

                }
            }
        });



    }

    private void upDateCoin(int requestCoin) {

        firestore
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(-requestCoin)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        firestore
                                .collection("users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .update("redeemStatus","true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(RedeemActivity.this, "transaction completed, Please check Transaction History", Toast.LENGTH_SHORT).show();
                                        getData();

                                        dialog.dismiss();
                                    }
                                });
                    }
                });



    }

    public void checkCoin(String check,int coin){

        if (availableCoin>=coin){

            paymentMethodLog.setImageResource(R.drawable.paypal);
            paymentMethod.setText("Paypal");
            edtNumber.setHint("Enter your email");
            dialog.show();
        }
        else {
            Toast.makeText(RedeemActivity.this, check, Toast.LENGTH_SHORT).show();
        }

    }

    public void checkCoinPaytm(String check,int coin){

        if (availableCoin>=coin){

            paymentMethodLog.setImageResource(R.drawable.payt);
            paymentMethod.setText("Paytm");
            edtNumber.setHint("Enter paytm number");
            dialog.show();
        }
        else {
            Toast.makeText(RedeemActivity.this, check, Toast.LENGTH_SHORT).show();
        }

    }

}