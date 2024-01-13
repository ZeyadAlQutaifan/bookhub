package com.example.bookhub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookhub.R;
import com.example.bookhub.adapters.BookRecyclerAdapter;
import com.example.bookhub.components.FilterDialog;
import com.example.bookhub.databinding.FragmentDashboardBinding;
import com.example.bookhub.models.Book;
import com.example.bookhub.util.Database;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class DashboardFragment extends Fragment {

    FragmentDashboardBinding binding;
    Query query;
    BookRecyclerAdapter bookRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        query = Database.booksRef();
        fillList(query);






        return binding.getRoot();
    }

    private void fillList(Query query){
        FirestoreRecyclerOptions<Book> bookOptions = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query, Book.class)
                .build();
        BookRecyclerAdapter.OnItemClickListener itemClickListener = new BookRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(String placeId) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, BookPageFragment.newInstance(placeId));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        bookRecyclerAdapter = new BookRecyclerAdapter(bookOptions, itemClickListener);
        binding.recyclerBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerBooks.setAdapter(bookRecyclerAdapter);
        bookRecyclerAdapter.startListening();
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


    private Query buildCriteria(FilterDialog filterDialog) {
        query = Database.booksRef();
        if (!filterDialog.getUniversityText().isEmpty() && filterDialog.getUniversityText() != null) {
            query = query.whereEqualTo("university", filterDialog.getUniversityText());
        }
        if (!filterDialog.getDepartmentsText().isEmpty() && filterDialog.getDepartmentsText() != null) {
            query = query.whereEqualTo("department", filterDialog.getDepartmentsText());
        }
        if (!filterDialog.getCategoryText().isEmpty() && filterDialog.getCategoryText() != null) {
            query = query.whereEqualTo("type", filterDialog.getCategoryText());
        }
        query = query.orderBy("creationTime" , Query.Direction.DESCENDING);
        return query;
    }

}