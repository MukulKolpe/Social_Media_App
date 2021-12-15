package com.example.social_media_chat_app.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_media_chat_app.Adapter.UserAdapter;
import com.example.social_media_chat_app.ModelClass.Users;
import com.example.social_media_chat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    CircleImageView img_setting;
    ImageView storyBtn,groupBtn;
    View StoryBtnLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        usersArrayList= new ArrayList<>();
        groupBtn=findViewById(R.id.groupBtn);
        groupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Public channel", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, GroupChatActivity.class));
            }
        });
     //   getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primary_purple));
        }

        DatabaseReference reference=database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users= dataSnapshot.getValue(Users.class);
                    if(!users.getUid().equals(FirebaseAuth.getInstance().getUid()))
                        usersArrayList.add(users);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mainUserRecyclerView=findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UserAdapter(HomeActivity.this, usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);
        storyBtn=findViewById(R.id.storyBtn);
        img_setting=findViewById(R.id.img_setting);

        storyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Stories", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, StoriesActivity.class));
            }
        });

     /*   imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(HomeActivity.this, R.style.Dialoge);

                dialog.setContentView(R.layout.dialog_layout);
                dialog.show();
                TextView yesBtn, noBtn;

                yesBtn=dialog.findViewById(R.id.yesBtn);
                noBtn=dialog.findViewById(R.id.noBtn);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });*/
        DatabaseReference reference1=database.getReference().child("user").child(auth.getUid());

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email=snapshot.child("email").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String status=snapshot.child("status").getValue().toString();
                String image=snapshot.child("imageUri").getValue().toString();

                Picasso.get().load(image).placeholder(R.drawable.placeholder_profile_image).into(img_setting);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
            }
        });



        if(auth.getCurrentUser()==null){
            startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));

        }
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        String currentId=FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId=FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }*/
}