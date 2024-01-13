package com.example.bookhub.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.bookhub.R;
import com.example.bookhub.adapters.RecyclerImageFromGalleryAdapter;
import com.example.bookhub.databinding.FragmentAddBookBinding;
import com.example.bookhub.models.Book;
import com.example.bookhub.util.Authentication;
import com.example.bookhub.util.Database;
import com.example.bookhub.util.Storage;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.util.ArrayList;

public class AddBookFragment extends Fragment {
    FragmentAddBookBinding binding ;
    ArrayList<Uri> chosenImagesUriList = new ArrayList<>();
    private RecyclerImageFromGalleryAdapter recyclerImageFromGalleryAdapter;
    private static final int REQUEST_CODE = 1; // You can choose any request code
    private static final int READ_PERMISSION = 101;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBookBinding.inflate(inflater , container , false);
        String[] universities = getResources().getStringArray(R.array.jordan_universities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, universities);
        binding.etUniversity.setAdapter(adapter);

        initPickImagesRecyclerView();
        String[] departments = getResources().getStringArray(R.array.departments_array);
        ArrayAdapter<String> departmentsAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, departments);
        binding.etDepartments.setAdapter(departmentsAdapter);

        String[] category = getResources().getStringArray(R.array.category_array);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, category);
        binding.etCategory.setAdapter(categoryAdapter);
        binding.fabAdImages.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");

            pickMedia.launch(intent);
        });
        
        binding.btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Book book = new Book(); 
                book.setWriterId(Authentication.getUserId());
                book.setCreationTime(System.currentTimeMillis());
                book.setReserved(false);
                book.setUniversity(binding.etUniversity.getText().toString());
                book.setDepartment(binding.etDepartments.getText().toString());
                book.setType(binding.etCategory.getText().toString());
                book.setTitle(binding.etBookTitle.getText().toString().trim());
                Storage.uploadImages(chosenImagesUriList, binding.etBookTitle.getText().toString()).addOnSuccessListener(downloadUrls -> {
                    // downloadUrls is a List<String> containing the download URLs for all uploaded images

                    // Save the post to Firestore with the download URLs
                    Database.addBook(downloadUrls , book).addOnSuccessListener(documentReference -> {
                        progressDialog.dismiss();
                        finishAndGoBackToManin();
                    }).addOnFailureListener(e -> {
                        // Handle the failure to save post to Firestore
                    });
                }).addOnFailureListener(e -> {
                    // Handle the failure to upload images
                });
            }
        });
        return binding.getRoot();
    }

    private void finishAndGoBackToManin() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Begin a FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new one
        fragmentTransaction.replace(R.id.fragment_container, new DashboardFragment());

        // Add the transaction to the back stack (optional)
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }


    private void initPickImagesRecyclerView() {

        recyclerImageFromGalleryAdapter = new RecyclerImageFromGalleryAdapter(chosenImagesUriList);
        binding.recyclerImagesFromGallery.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerImagesFromGallery.setAdapter(recyclerImageFromGalleryAdapter);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
        }
    }
    ActivityResultLauncher<Intent> pickMedia =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.getData() != null) {
                            chosenImagesUriList.clear();
                            // Single image selected
                            Uri selectedImageUri = data.getData();
                            chosenImagesUriList.add(selectedImageUri);
                        } else if (data.getClipData() != null) {
                            // Multiple images selected
                            ClipData clipData = data.getClipData();
                            if (clipData.getItemCount() < 11) {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    Uri selectedImageUri = clipData.getItemAt(i).getUri();
                                    chosenImagesUriList.add(selectedImageUri);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Not allowed to pick more than 10 images", Toast.LENGTH_LONG).show();
                            }
                        }
                        recyclerImageFromGalleryAdapter.notifyDataSetChanged();

                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });



}