package com.example.social_media_chat_app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.social_media_chat_app.Adapter.GroupMessagesAdapter;
import com.example.social_media_chat_app.Adapter.MessagesAdapter;
import com.example.social_media_chat_app.ModelClass.Messages;
import com.example.social_media_chat_app.R;
import com.example.social_media_chat_app.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;
    GroupMessagesAdapter adapter;
    ArrayList<Messages> messagesArrayList;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    ProgressDialog dialog;
    String SenderUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primary_purple));

            SenderUID=FirebaseAuth.getInstance().getUid();
            database=FirebaseDatabase.getInstance();
            firebaseAuth=FirebaseAuth.getInstance();
            storage=FirebaseStorage.getInstance();

            dialog= new ProgressDialog(this);
            dialog.setMessage("Uploading image...");
            dialog.setCancelable(false);


            messagesArrayList=new ArrayList<>();
            adapter= new GroupMessagesAdapter(this, messagesArrayList);
            binding.messageAdapter.setLayoutManager(new LinearLayoutManager(this));
            binding.messageAdapter.setAdapter(adapter);
            database.getReference().child("public")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messagesArrayList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Messages messages=dataSnapshot.getValue(Messages.class);
                        messages.setMessageId(dataSnapshot.getKey());
                        messagesArrayList.add(messages);
                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            binding.sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message=binding.edtMessage.getText().toString();
                    Date date= new Date();
                    Messages messages= new Messages(message, SenderUID, date.getTime());
                    binding.edtMessage.setText("");

                    database.getReference().child("public")
                            .push()
                            .setValue(messages);


                }
            });
            binding.attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 25);
                }
            });
            binding.imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   finish();
                }
            });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==25){
            if(data != null){
                if(data.getData()!=null){
                    Uri selectedImage= data.getData();
                    Calendar calendar=Calendar.getInstance();
                    StorageReference reference=storage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                    dialog.show();
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath=uri.toString();
                                        String message=binding.edtMessage.getText().toString();
                                     /*   if(message.isEmpty()){
                                            Toast.makeText(ChatActivity.this, "Please Enter Valid Messages", Toast.LENGTH_SHORT).show();
                                            return;
                                        }*/
                                        binding.edtMessage.setText("");
                                        Date date=new Date();

                                        Messages messages= new Messages(message, SenderUID, date.getTime());
                                        messages.setMessage("photo");
                                        messages.setImageUrl(filePath);

                                        database=FirebaseDatabase.getInstance();

                                        database.getReference().child("public")
                                                .push()
                                                .setValue(messages);

                                        //    Toast.makeText(ChatActivity.this, filePath, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
    }

}