package com.example.social_media_chat_app.Adapter;

import static com.example.social_media_chat_app.Activity.ChatActivity.rImage;
import static com.example.social_media_chat_app.Activity.ChatActivity.sImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_media_chat_app.ModelClass.Messages;
import com.example.social_media_chat_app.ModelClass.Users;
import com.example.social_media_chat_app.R;
import com.example.social_media_chat_app.databinding.GroupReceiverItemBinding;
import com.example.social_media_chat_app.databinding.GroupSenderItemBinding;
import com.example.social_media_chat_app.databinding.ReceiverLayoutItemBinding;
import com.example.social_media_chat_app.databinding.SenderLayoutItemBinding;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public GroupMessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.group_sender_item, parent, false);
            return new senderViewHolder(view);


        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.group_receiver_item, parent, false);
            return new ReceiverViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);

        int reactions[] = new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if(pos < 0)
                return false;

            if (holder.getClass() == senderViewHolder.class) {
                senderViewHolder viewHolder = (senderViewHolder) holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);

            } else {
                ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            messages.setFeeling(pos);
            FirebaseDatabase.getInstance().getReference()
                    .child("public")
                    .child(messages.getMessageId()).setValue(messages);
            return true; // true is closing popup, false is requesting a new selection
        });

        if (holder.getClass() == senderViewHolder.class) {
            senderViewHolder viewHolder = (senderViewHolder) holder;

            if(messages.getMessage().equals("photo")){
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.txtMessages.setVisibility(View.GONE);
                Picasso.get().load(messages.getImageUrl()).placeholder(R.drawable.placeholder).into(viewHolder.binding.image);
            }
            FirebaseDatabase.getInstance().getReference()
                    .child("user")
                    .child(messages.getSenderId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Users users=snapshot.getValue(Users.class);
                                viewHolder.binding.name.setText("@" + users.getName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            viewHolder.binding.txtMessages.setText(messages.getMessage());
            if(messages.getFeeling() >= 0){
               viewHolder.binding.feeling.setImageResource(reactions[messages.getFeeling()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }else{
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }
            viewHolder.binding.txtMessages.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });
            viewHolder.binding.image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });
            Picasso.get().load(sImage).placeholder(R.drawable.placeholder_profile_image).into(viewHolder.binding.profileImage);
        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;

            if(messages.getMessage().equals("photo")){
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.txtMessages.setVisibility(View.GONE);
                Picasso.get().load(messages.getImageUrl()).placeholder(R.drawable.placeholder).into(viewHolder.binding.image);
            }
            viewHolder.binding.txtMessages.setText(messages.getMessage());

            FirebaseDatabase.getInstance().getReference()
                    .child("user")
                    .child(messages.getSenderId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Users users=snapshot.getValue(Users.class);
                                viewHolder.binding.name.setText("@"+  users.getName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            if(messages.getFeeling() >= 0){
                viewHolder.binding.feeling.setImageResource(reactions[messages.getFeeling()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }else{
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }
            viewHolder.binding.txtMessages.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });
            viewHolder.binding.image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });
            Picasso.get().load(rImage).placeholder(R.drawable.placeholder_profile_image).into(viewHolder.binding.profileImage);

        }

    }

    @Override
    public int getItemCount() {

        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
      //  CircleImageView circleImageView;
     //   TextView txtmessage;
        GroupSenderItemBinding binding;

        public senderViewHolder(@NonNull View itemView) {

            super(itemView);
         //   circleImageView = itemView.findViewById(R.id.profile_image);
         //   txtmessage = itemView.findViewById(R.id.txtMessages);
            binding = GroupSenderItemBinding.bind(itemView);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
       // CircleImageView circleImageView;
      //  TextView txtmessage;
        GroupReceiverItemBinding binding;

        public ReceiverViewHolder(@NonNull View itemView) {

            super(itemView);
           // circleImageView = itemView.findViewById(R.id.profile_image);
          //  txtmessage = itemView.findViewById(R.id.txtMessages);
            binding = GroupReceiverItemBinding.bind(itemView);
        }
    }
}
