package com.example.social_media_chat_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_media_chat_app.ModelClass.UserStatus;
import com.example.social_media_chat_app.ModelClass.Users;
import com.example.social_media_chat_app.R;
//import com.example.social_media_chat_app.databinding.ItemStoryBinding;

import java.util.ArrayList;


public class TopStatusAdapter extends RecyclerView.Adapter<TopStatusAdapter.TopStatusViewHolder>{

    Context context;
    ArrayList<UserStatus> userStatuses;

    public TopStatusAdapter(Context context, ArrayList<UserStatus> userStatuses){
        this.context=context;
        this.userStatuses=userStatuses;
    }
    @NonNull
    @Override
    public TopStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_story,parent,false);

        return new TopStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStatusViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class TopStatusViewHolder extends RecyclerView.ViewHolder {
  //      ItemStoryBinding binding;
        public TopStatusViewHolder(@NonNull View itemView) {
            super(itemView);
    //        binding=ItemStoryBinding.bind(itemView);
        }
    }
}
