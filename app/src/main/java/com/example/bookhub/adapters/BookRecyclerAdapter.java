package com.example.bookhub.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookhub.R;
import com.example.bookhub.models.Book;
import com.example.bookhub.models.User;
import com.example.bookhub.util.Database;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

public class BookRecyclerAdapter extends FirestoreRecyclerAdapter<Book , BookRecyclerAdapter.BookViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(String placeId);
    }

    private OnItemClickListener onItemClickListener;
    public BookRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Book> options , OnItemClickListener onItemClickListener) {
        super(options);
        this.onItemClickListener = onItemClickListener;

    }

    @Override
    protected void onBindViewHolder(@NonNull BookRecyclerAdapter.BookViewHolder holder, int position, @NonNull Book model) {
        holder.tvTitle.setText(model.getTitle());
        holder.chDepartment.setText(model.getDepartment());
        holder.chpUniversity.setText(model.getUniversity());
        holder.chType.setText(model.getType());

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


        // TODO : set user data
        Database.getUser(model.getWriterId()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                       User user =  documentSnapshot.toObject(User.class);
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public BookRecyclerAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_card, parent, false);

        return new BookViewHolder(view);
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        CardView materialCardView;

        TextView tvTitle ;
        ImageView imgBook;
        Chip chpUniversity ;
        Chip chDepartment ;
        Chip chType ;

         public BookViewHolder(@NonNull View itemView) {
            super(itemView);
             tvTitle = itemView.findViewById(R.id.tvTitle) ;
             imgBook = itemView.findViewById(R.id.imgBook) ;
             chpUniversity = itemView.findViewById(R.id.chpUniversity) ;
             chDepartment = itemView.findViewById(R.id.chDepartment) ;
             chType = itemView.findViewById(R.id.chType) ;
             materialCardView = itemView.findViewById(R.id.materialCardView);
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
