package com.example.bookhub.util;

import androidx.annotation.NonNull;

import com.example.bookhub.models.Book;
import com.example.bookhub.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Database {

    private static FirebaseFirestore instance = FirebaseFirestore.getInstance();

    @NonNull
    public static CollectionReference usersRef() {
        return instance.collection("users");
    }
    @NonNull
    public static CollectionReference booksRef() {
        return instance.collection("books");
    }
    @NonNull
    public static Task<DocumentSnapshot> getBook(String bookId){
        return booksRef().document(bookId).get();
    }
    public static Task<DocumentSnapshot> getUser(String userId){

       return usersRef().document(userId).get();
    }
    public static Task<Void> writeNewUser(String userId, User newUser){
        return usersRef().document(userId).set(newUser);
    }

    public static Task<DocumentReference> addBook(List<String> downloadUrls , Book book) {
        book.setImages(downloadUrls);
        // Save the post to Firestore
        return booksRef().add(book);
    }
}
