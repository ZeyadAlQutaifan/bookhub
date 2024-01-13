package com.example.bookhub.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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


    private OnItemClickListener onItemClickListener;


    public MyBookRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Book> options, OnItemClickListener onItemClickListener ) {
        super(options);
        this.onItemClickListener = onItemClickListener;


    }


    @Override
    protected void onBindViewHolder(@NonNull MyBookViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Book model) {
        holder.tvTitle.setText(model.getTitle());
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                // Set an item click listener for the popup menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_delete){
                            new AlertDialog.Builder(holder.itemView.getContext())
                                    .setTitle("Delete")
                                    .setMessage("Are you sure you want to delete this item")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Database.deleteBook(getSnapshots().getSnapshot(position).getId())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                notifyItemRemoved(position);
                                                                // If you have dynamic data or want to notify the adapter about
                                                                // changes affecting the entire dataset, use notifyItemRangeChanged
                                                          //      notifyItemRangeChanged(position, getItemCount());
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
                        return true;
                    }
                });
                // Show the popup menu
                popupMenu.show();
            }
        });
        if (model.getImages().size() >0){
            Glide.with(holder.itemView.getContext())
                    .load(model.getImages().get(0))
                    .into(holder.imgBook);
        }
        else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.no_image)
                    .into(holder.imgBook);
        }
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
        ImageView options;

        public MyBookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgBook = itemView.findViewById(R.id.imgBook);
            materialCardView = itemView.findViewById(R.id.materialCardView);
            options = itemView.findViewById(R.id.options);

            materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call onItemClick when an item is clicked
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getSnapshots().getSnapshot(getAdapterPosition()).getId());
                    }
                }
            });

        }
    }
}
