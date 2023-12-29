package com.example.bookhub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookhub.R;
import com.example.bookhub.models.ChatRoom;
import com.example.bookhub.models.User;
import com.example.bookhub.util.Database;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoom, ChatsRecyclerAdapter.ChatRoomViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(String placeId);
    }

    private OnItemClickListener onItemClickListener;

    public ChatsRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoom> options , OnItemClickListener onItemClickListener) {
        super(options);
        this.onItemClickListener = onItemClickListener ;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position, @NonNull ChatRoom model) {
        Database.getUser(model.getReceiverId()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        User user =  documentSnapshot.toObject(User.class);
                        holder.txtUserName.setText(user.getFullName());
                        Glide.with(holder.itemView.getContext())
                                .load(user.getImageUri())
                                .into(holder.userImage);
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chats, parent, false);

        return new ChatRoomViewHolder(view);
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName ;
        CircleImageView userImage ;

        LinearLayout layoutUser ;
        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            userImage = itemView.findViewById(R.id.userImage);
            layoutUser = itemView.findViewById(R.id.layoutUser);
            layoutUser.setOnClickListener(new View.OnClickListener() {
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
