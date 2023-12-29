package com.example.bookhub.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookhub.R;
import com.example.bookhub.adapters.BookRecyclerAdapter;
import com.example.bookhub.adapters.MyBookRecyclerAdapter;
import com.example.bookhub.databinding.FragmentMyBooksBinding;
import com.example.bookhub.models.Book;
import com.example.bookhub.util.Authentication;
import com.example.bookhub.util.Database;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;


public class MyBooksFragment extends Fragment {

    FragmentMyBooksBinding binding;
    MyBookRecyclerAdapter myBookRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyBooksBinding.inflate(inflater, container, false);




        fillRecyclerView();
        return binding.getRoot();
    }

    MyBookRecyclerAdapter.OnItemClickListener onItemClickListener = new MyBookRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(String placeId) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, BookPageFragment.newInstance(placeId));
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };
    MyBookRecyclerAdapter.OnDeleteClickListener onDeleteClickListener = new MyBookRecyclerAdapter.OnDeleteClickListener() {
        @Override
        public void onDeleteClick(String bookId) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this item")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Database.deleteBook(bookId)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Book deleted successfully
                                                // Now update the RecyclerView
                                                fillRecyclerView();
                                                // or use notifyItemRemoved(position) if you know the position
                                            } else {
                                                // Handle the deletion failure
                                            }
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    };

    private void fillRecyclerView() {
        Query query = Database.booksRef().whereEqualTo("writerId", Authentication.getUserId()).orderBy("creationTime", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Book> bookOptions = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query, Book.class)
                .build();
        myBookRecyclerAdapter = new MyBookRecyclerAdapter(bookOptions, onItemClickListener, onDeleteClickListener);
        binding.recyclerBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerBooks.setAdapter(myBookRecyclerAdapter);
        myBookRecyclerAdapter.startListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        myBookRecyclerAdapter.startListening();
    }
  @Override
    public void onResume() {
        super.onResume();
        myBookRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myBookRecyclerAdapter.stopListening();
    }
}