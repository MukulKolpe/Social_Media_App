package com.example.social_media_chat_app.Adapter;

import static com.example.social_media_chat_app.Activity.ChatActivity.rImage;
import static com.example.social_media_chat_app.Activity.ChatActivity.sImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_media_chat_app.ModelClass.Messages;
import com.example.social_media_chat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==ITEM_SEND){
            View view= LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new senderViewHolder(view);


        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.receiver_layout_item,parent,false);
            return new ReceiverViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages=messagesArrayList.get(position);

        if(holder.getClass()==senderViewHolder.class){
            senderViewHolder viewHolder=(senderViewHolder) holder;
            viewHolder.txtmessage.setText(messages.getMessage());
            Picasso.get().load(sImage).into(viewHolder.circleImageView);
        }else {
            ReceiverViewHolder viewHolder=(ReceiverViewHolder) holder;
            viewHolder.txtmessage.setText(messages.getMessage());
            Picasso.get().load(rImage).into(viewHolder.circleImageView);

        }

    }

    @Override
    public int getItemCount() {

        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return ITEM_SEND;
        }else {
            return ITEM_RECEIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtmessage;
        public senderViewHolder(@NonNull View itemView) {

            super(itemView);
            circleImageView=itemView.findViewById(R.id.profile_image);
            txtmessage=itemView.findViewById(R.id.txtMessages);
        }
    }
    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtmessage;
        public ReceiverViewHolder(@NonNull View itemView) {

            super(itemView);
            circleImageView=itemView.findViewById(R.id.profile_image);
            txtmessage=itemView.findViewById(R.id.txtMessages);
        }
    }
}
