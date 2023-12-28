package com.example.bookhub;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bookhub.databinding.ActivityRegisterBinding;
import com.example.bookhub.models.User;
import com.example.bookhub.util.Authentication;
import com.example.bookhub.util.Database;
import com.example.bookhub.util.Storage;
import com.example.bookhub.util.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding ;
    Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        binding.tvAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                pickMedia.launch(intent);
            }
        });

    }

    private void createUser() {

        String username = Objects.requireNonNull(binding.etFullName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();

        if(username.isEmpty()){
            binding.etFullName.setError("Required!");
            binding.etFullName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            binding.etEmail.setError("Required!");
            binding.etEmail.requestFocus();
            return;
        }

        if (!Validator.isValidEmail(email)){
            binding.etEmail.setError("invalid email");
            binding.etEmail.requestFocus();
            return;
        }
        if (!Validator.isValidPassword(password)){
            binding.etPassword.setError("must be 8 or more");
            binding.etPassword.requestFocus();
            return;
        }
        if (!Validator.isPasswordMatches(password , confirmPassword)){
            binding.etConfirmPassword.setError("must be match");
            binding.etConfirmPassword.requestFocus();
            return;
        }
        if (imgUri == null){
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
            return;
        }
        User newUser = new User();
        newUser.setFullName(username);
        newUser.setEmail(email);
        newUser.setDeviceTokens("");


        Authentication.createUser(email , password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Storage.uploadImage(imgUri, authResult.getUser().getUid() ).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        newUser.setImageUri(s);
                        Database.writeNewUser(authResult.getUser().getUid() , newUser)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(RegisterActivity.this , MainActivity.class));
                                        finish();
                                    }
                                });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "error while creating account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ActivityResultLauncher<Intent> pickMedia =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.getData() != null) {
                            // Single image selected
                            Uri selectedImageUri = data.getData();
                            imgUri = selectedImageUri;
                            binding.userImg.setImageURI(imgUri);

                        }

                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });


}