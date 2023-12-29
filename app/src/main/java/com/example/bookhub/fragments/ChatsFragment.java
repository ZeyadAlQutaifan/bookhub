package com.example.bookhub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookhub.R;
import com.example.bookhub.adapters.ChatsRecyclerAdapter;
import com.example.bookhub.databinding.FragmentChatsBinding;
import com.example.bookhub.models.Book;
import com.example.bookhub.models.ChatRoom;
import com.example.bookhub.util.Authentication;
import com.example.bookhub.util.Database;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class ChatsFragment extends Fragment {

    FragmentChatsBinding binding;
    ChatsRecyclerAdapter chatsRecyclerAdapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        fillChatsList();
        return binding.getRoot();
    }

    private void fillChatsList() {
        Query query = Database.chatsRef().whereEqualTo("senderId" , Authentication.getUserId()).orderBy("creationDate", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatRoom> chatOptions = new FirestoreRecyclerOptions.Builder<ChatRoom>()
                .setQuery(query, ChatRoom.class)
                .build();


        ChatsRecyclerAdapter.OnItemClickListener itemClickListener = new ChatsRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(String placeId) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,  ChatFragment.newInstance(placeId));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };

        chatsRecyclerAdapter = new ChatsRecyclerAdapter(chatOptions , itemClickListener);
        binding.recyclerChats.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerChats.setAdapter(chatsRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        chatsRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        chatsRecyclerAdapter.stopListening();
    }

}