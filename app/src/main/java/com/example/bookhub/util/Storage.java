package com.example.bookhub.util;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Storage {
    public interface OnImageUploadListener {
        void onImageUploadSuccess(String downloadUrl);
        void onImageUploadFailure(Exception e);
    }
    private static FirebaseStorage instance = FirebaseStorage.getInstance();

    public static StorageReference books = instance.getReference().child("books");
    public static StorageReference users = instance.getReference().child("users");

    public static Task<String> uploadImage(Uri imageUri, String userId) {
        StorageReference imageRef = users.child(userId + System.currentTimeMillis()).child(UUID.randomUUID().toString());

        return imageRef.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Return the download URL
                    return imageRef.getDownloadUrl();
                })
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            return downloadUri.toString();
                        } else {
                            throw new Exception("Download URL is null");
                        }
                    } else {
                        throw task.getException();
                    }
                });
    }

    public static Task<List<String>> uploadImages(List<Uri> imageUris , String bookTitle) {

        List<Task<Uri>> uploadTasks = new ArrayList<>();

        for (Uri imageUri : imageUris) {
            StorageReference imageRef = books.child(bookTitle + System.currentTimeMillis()).child(UUID.randomUUID().toString());
            uploadTasks.add(imageRef.putFile(imageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Return the download URL
                return imageRef.getDownloadUrl();
            }));
        }
        // Combine all tasks into a single task
        return Tasks.whenAllSuccess(uploadTasks);
    }
}
