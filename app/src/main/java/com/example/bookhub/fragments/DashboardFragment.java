package com.example.bookhub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.bookhub.R;
import com.example.bookhub.adapters.BookRecyclerAdapter;
import com.example.bookhub.databinding.FragmentDashboardBinding;
import com.example.bookhub.models.Book;
import com.example.bookhub.util.Database;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class DashboardFragment extends Fragment {

    FragmentDashboardBinding binding;
    Query query;
    BookRecyclerAdapter bookRecyclerAdapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        query = Database.booksRef();
        FirestoreRecyclerOptions<Book> bookOptions = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query, Book.class)
                .build();
        bookRecyclerAdapter = new BookRecyclerAdapter(bookOptions) ;
        binding.recyclerBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerBooks.setAdapter(bookRecyclerAdapter);
        String[] universities = getResources().getStringArray(R.array.jordan_universities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, universities);
        binding.etUniversity.setAdapter(adapter);


        String[] departments = getResources().getStringArray(R.array.departments_array);
        ArrayAdapter<String> departmentsAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, departments);
        binding.etDepartments.setAdapter(departmentsAdapter);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        bookRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        bookRecyclerAdapter.stopListening();
    }

}