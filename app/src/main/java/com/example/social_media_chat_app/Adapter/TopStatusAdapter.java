package com.example.social_media_chat_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_media_chat_app.Activity.StoriesActivity;
import com.example.social_media_chat_app.ModelClass.UserStatus;
import com.example.social_media_chat_app.ModelClass.Users;
import com.example.social_media_chat_app.ModelClass.status;
import com.example.social_media_chat_app.R;
import com.example.social_media_chat_app.databinding.ItemStoryBinding;
import com.squareup.picasso.Picasso;
//import com.example.social_media_chat_app.databinding.ItemStoryBinding;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;


public class TopStatusAdapter extends RecyclerView.Adapter<TopStatusAdapter.TopStatusViewHolder> {

    Context context;
    ArrayList<UserStatus> userStatuses;

    public TopStatusAdapter(Context context, ArrayList<UserStatus> userStatuses) {
        this.context = context;
        this.userStatuses = userStatuses;

    }

    @NonNull
    @Override
    public TopStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);

        return new TopStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStatusViewHolder holder, int position) {
        UserStatus userStatus= userStatuses.get(position);
        status lastStatus=userStatus.getStatuses().get(userStatus.getStatuses().size()-1);

        Picasso.get().load(lastStatus.getImageUrl()).placeholder(R.drawable.placeholder).into(holder.binding.image);
        holder.binding.circularStatusView.setPortionsCount(userStatus.getStatuses().size());
        holder.binding.userName.setText(userStatus.getName());
        holder.binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyStory> myStories = new ArrayList<>();
                for(com.example.social_media_chat_app.ModelClass.status status : userStatus.getStatuses()){
                    myStories.add(new MyStory(status.getImageUrl()));

                }
                new StoryView.Builder(((StoriesActivity)context).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(userStatus.getName()) // Default is Hidden
                        .setSubtitleText("") // Default is Hidden
                        .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class TopStatusViewHolder extends RecyclerView.ViewHolder {
        ItemStoryBinding binding;

        public TopStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStoryBinding.bind(itemView);
        }
    }
}
