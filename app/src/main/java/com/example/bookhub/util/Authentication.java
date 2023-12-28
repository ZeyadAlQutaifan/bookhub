package com.example.bookhub.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Authentication {
    private static FirebaseAuth instance = FirebaseAuth.getInstance();
    public static Task<AuthResult> login(String email , String password){
        return instance.signInWithEmailAndPassword(email, password);
    }
    public static String getUserId() {
        return instance.getCurrentUser().getUid();
    }
    public static Task<AuthResult> createUser(String email , String password){
        return instance.createUserWithEmailAndPassword(email, password);
    }

}
