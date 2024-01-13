package com.example.bookhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bookhub.databinding.ActivityLognBinding;
import com.example.bookhub.util.Authentication;
import com.example.bookhub.util.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    ActivityLognBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLognBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                login();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });
    }

    private void login() {
        String email = binding.loginEmailEditText.getText().toString();
        String password = binding.loginPasswordEditText.getText().toString();
        if (email.isEmpty()) {
            binding.loginEmailEditText.setError("Required!");
            binding.loginEmailEditText.requestFocus();
            return;
        }

        if (!Validator.isValidEmail(email)) {
            binding.loginEmailEditText.setError("invalid email");
            binding.loginEmailEditText.requestFocus();
            return;
        }
        Authentication.login(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });
    }


}