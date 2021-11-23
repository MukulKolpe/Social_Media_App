package com.example.social_media_chat_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.social_media_chat_app.Adapter.TopStatusAdapter;
import com.example.social_media_chat_app.ModelClass.UserStatus;
import com.example.social_media_chat_app.R;
//import com.example.social_media_chat_app.databinding.ActivityStoriesBinding;

import java.util.ArrayList;

public class StoriesActivity extends AppCompatActivity {
  //  ActivityStoriesBinding binding;
    TopStatusAdapter statusAdapter;
    ArrayList<UserStatus> userStatuses;
    Icon story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     binding=ActivityStoriesBinding.inflate(getLayoutInflater());
    //    setContentView(binding.getRoot());
        userStatuses=new ArrayList<>();
        getSupportActionBar().hide();


        statusAdapter= new TopStatusAdapter(this,userStatuses);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
    //    binding.statusList.setLayoutManager(layoutManager);
    //    binding.statusList.setAdapter(statusAdapter);





    }
}