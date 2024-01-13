package com.example.bookhub;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.bookhub.databinding.ActivityUpdateBinding;
import com.example.bookhub.models.User;
import com.example.bookhub.util.Authentication;
import com.example.bookhub.util.Database;
import com.example.bookhub.util.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class UpdateActivity extends AppCompatActivity {

    ActivityUpdateBinding binding;
    String userImageUri;
    Uri newUserImageUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Database.getUser(Authentication.getUserId()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        progressDialog.dismiss();
                        User user = documentSnapshot.toObject(User.class);
                        fillUserInfo(user);
                    }
                }
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateActivity.this)
                        .setTitle("Save")
                        .setMessage("Are you sure you want to update")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateUserInfo();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

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
        setContentView(binding.getRoot());


    }

    private void updateUserInfo() {
        progressDialog.show();
        String username = Objects.requireNonNull(binding.etFullName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.txtUserEmail.getText()).toString().trim();
        String phone = Objects.requireNonNull(binding.txtUserPhone.getText()).toString().trim();
        if (username.isEmpty()) {
            binding.etFullName.setError("Required!");
            binding.etFullName.requestFocus();
            return;
        }

        User newUser = new User();
        newUser.setFullName(username);
        newUser.setEmail(email);
        newUser.setDeviceTokens("");
        newUser.setPhone(phone);
        if (newUserImageUri != null) {
            Storage.uploadImage(newUserImageUri, Authentication.getUserId()).addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    newUser.setImageUri(s);
                    Database.updateUser(Authentication.getUserId(), newUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    finish();
                                }
                            });
                }
            });
        } else {
            newUser.setImageUri(userImageUri);
            Database.updateUser(Authentication.getUserId(), newUser)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
        }
    }

    private void fillUserInfo(User user) {
        userImageUri = user.getImageUri();
        binding.etFullName.setText(user.getFullName());
        binding.txtUserPhone.setText(user.getPhone());
        binding.txtUserEmail.setText(user.getEmail());
        binding.btnSave.setEnabled(true);
        binding.txtUserPhone.setEnabled(false);
        binding.txtUserEmail.setEnabled(false);
        Glide.with(this)
                .load(user.getImageUri())
                .into(binding.userImage);

    }

    ActivityResultLauncher<Intent> pickMedia =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.getData() != null) {
                            // Single image selected
                            Uri selectedImageUri = data.getData();
                            newUserImageUri = selectedImageUri;
                            binding.userImage.setImageURI(newUserImageUri);
                        }

                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

}