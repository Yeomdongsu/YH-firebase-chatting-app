package com.dongsu.yhfire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dongsu.yhfire.adapter.MainAdapter;
import com.dongsu.yhfire.dto.ChatInfo;
import com.dongsu.yhfire.dto.ChatRoomList;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editChatName;
    EditText editUserName;
    Button btnStart;
    RecyclerView recyclerView;
    MainAdapter adapter;
    ArrayList<String> roomList = new ArrayList<>();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editChatName = findViewById(R.id.editChatName);
        editUserName = findViewById(R.id.editUserName);
        btnStart = findViewById(R.id.btnStart);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        adapter = new MainAdapter(MainActivity.this, roomList);
        recyclerView.setAdapter(adapter);

        getRoomList();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatName = editChatName.getText().toString().trim();
                String userName = editUserName.getText().toString().trim();

                if (chatName.isEmpty() || userName.isEmpty()){
                    Snackbar.make(btnStart, "모두 입력하세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("chatName", chatName);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

    }

    private void getRoomList() {
        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                roomList.add(snapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                roomList.remove(snapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}