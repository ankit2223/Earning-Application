 package com.example.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.Models.UserModel;
import com.example.app.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

 public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    FirebaseAuth auth;

    FirebaseFirestore firestore;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Your Account");
        progressDialog.setMessage("Your Account is creating");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                String name = binding.edtName.getText().toString();
                String email =  binding.edtEmail.getText().toString();
                String password = binding.edtPassword.getText().toString();
                if (name.isEmpty()){
                    binding.edtName.setError("Enter your name");
                }
                else if (email.isEmpty()){
                    binding.edtEmail.setError("Enter email");
                }
                else if (password.isEmpty()){
                    binding.edtPassword.setError("Enter Password");
                }
                else {
                    progressDialog.show();
                    storeUserData(name, email, password);
                }

            }
        });


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

     private void storeUserData(String name, String email, String password) {
        auth.createUserWithEmailAndPassword(binding.edtEmail.getText().toString(),binding.edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    String referCode = email.substring(0,email.lastIndexOf("@"));

                    UserModel model = new UserModel(name, email, password,
                            "https://firebasestorage.googleapis.com/v0/b/earning-app-ba670.appspot.com/o/profile.png?alt=media&token=2cba0cc7-d09f-4463-8f21-d4be50575b7e",
                            referCode,
                            "false",
                            5,
                            5,
                            5);
                    String id = task.getResult().getUser().getUid();

                    firestore.collection("users").document(id).set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful()){
                             progressDialog.dismiss();
                             Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                             startActivity(intent);
                             finish();

                         }
                         else {
                             progressDialog.dismiss();
                             Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                         }
                        }
                    });

                }
                else {

                    Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
     }
 }

