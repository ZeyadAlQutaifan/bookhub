package com.example.bookhub.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.bookhub.databinding.FragmentBookPageBinding;
import com.example.bookhub.models.Book;
import com.example.bookhub.models.User;
import com.example.bookhub.util.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookPageFragment extends Fragment {

    FragmentBookPageBinding binding ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    List<SlideModel> imageList = new ArrayList<SlideModel>();

    private String bookId;

    public BookPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BookPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookPageFragment newInstance(String param1) {
        BookPageFragment fragment = new BookPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookPageBinding.inflate(inflater,container,false);

        Database.getBook(bookId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Book book = documentSnapshot.toObject(Book.class);
                for (String uri : book.getImages()) {
                    imageList.add(new SlideModel(uri, null, ScaleTypes.CENTER_CROP));
                }
                binding.imageSlider.setImageList(imageList);
                binding.txtBootTitle.setText(book.getTitle());
                binding.txtBookUniversity.setText(book.getUniversity());
                binding.txtBookDepartment.setText(book.getDepartment());
                Database.getUser(book.getWriterId()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){
                                User user =  documentSnapshot.toObject(User.class);
                                binding.tvUserName.setText(user.getFullName());
                                Glide.with(getActivity())
                                        .load(user.getImageUri())
                                        .into(binding.imgUser);
                                binding.btnCall.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + user.getPhone()));
                                        startActivity(dialIntent);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
        return binding.getRoot();
    }
}