package com.example.bookhub.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bookhub.R;
import com.example.bookhub.UpdateActivity;
import com.example.bookhub.databinding.FragmentProfileBinding;
import com.example.bookhub.models.User;
import com.example.bookhub.util.Authentication;
import com.example.bookhub.util.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Database.getUser(Authentication.getUserId()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        fillUserInfo(user);
                    }
                }
            }

        });
    }

    private void fillUserInfo(User user) {
        binding.txtUserName.setText(user.getFullName());
        binding.txtUserPhone.setText(user.getPhone());
        binding.txtUserEmail.setText(user.getEmail());
        Glide.with(getActivity())
                .load(user.getImageUri())
                .into(binding.userImage);
        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateActivity.class);
                // Start the new Activity
                startActivity(intent);
            }
        });
    }

}