package com.example.bookhub.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookhub.LoginActivity;
import com.example.bookhub.R;
import com.example.bookhub.models.Book;
import com.example.bookhub.util.Database;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.makeramen.roundedimageview.RoundedImageView;

public class MyBookRecyclerAdapter extends FirestoreRecyclerAdapter<Book, MyBookRecyclerAdapter.MyBookViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(String book);
    }
    public interface OnDeleteClickListener {
        void onDeleteClick(String bookId);
    }


    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public MyBookRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Book> options, OnItemClickListener onItemClickListener ,  OnDeleteClickListener onDeleteClickListener) {
        super(options);
        this.onItemClickListener = onItemClickListener;
        this.onDeleteClickListener = onDeleteClickListener;

    }


    @Override
    protected void onBindViewHolder(@NonNull MyBookViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Book model) {
        holder.tvTitle.setText(model.getTitle());
        holder.chDepartment.setText(model.getDepartment());
        holder.chpUniversity.setText(model.getUniversity());
        Glide.with(holder.itemView.getContext())
                .load(model.getImages().get(0))
                .into(holder.imgBook);

    }

    @NonNull
    @Override
    public MyBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_book_card, parent, false);
        return new MyBookViewHolder(view);
    }

    public class MyBookViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView materialCardView;

        TextView tvTitle;
        RoundedImageView imgBook;
        Chip chpUniversity;
        Chip chDepartment;
        ExtendedFloatingActionButton btnDelete;

        public MyBookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgBook = itemView.findViewById(R.id.imgBook);
            chpUniversity = itemView.findViewById(R.id.chpUniversity);
            chDepartment = itemView.findViewById(R.id.chDepartment);
            materialCardView = itemView.findViewById(R.id.materialCardView);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call onItemClick when an item is clicked
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getSnapshots().getSnapshot(getAdapterPosition()).getId());
                    }
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(getSnapshots().getSnapshot(getAdapterPosition()).getId());
                    }
                }
            });
        }
    }
}
