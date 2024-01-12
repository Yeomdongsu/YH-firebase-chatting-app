package com.dongsu.yhfire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dongsu.yhfire.adapter.ChatAdapter;
import com.dongsu.yhfire.dto.ChatInfo;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ChatActivity extends AppCompatActivity {

    EditText editContent;
    Button btnSend;

    RecyclerView recyclerView;
    ChatAdapter adapter;
    ArrayList<ChatInfo> chatInfoArrayList = new ArrayList<>();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    String chatName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatName = getIntent().getStringExtra("chatName");
        String userName = getIntent().getStringExtra("userName");

        Log.i("AAAAAAAAAAA", "chatName : " + chatName + "userName : " + userName);

        editContent = findViewById(R.id.editContent);
        btnSend = findViewById(R.id.btnSend);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    // 키보드가 올라가는 상황일 때 스크롤을 맨 아래로 이동
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollToBottom();
                        }
                    }, 100); // 포커스 전환이 완료된 후에 스크롤을 하도록 딜레이를 줌
                }

            }
        });

        adapter = new ChatAdapter(ChatActivity.this, chatInfoArrayList, userName);
        recyclerView.setAdapter(adapter);

        getChatData();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editContent.getText().toString();

                if (content.isEmpty()){
                    Snackbar.make(btnSend, "내용을 입력하세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                ChatInfo chatInfo = new ChatInfo(userName, content);
                databaseReference.child("chat").child(chatName).push().setValue(chatInfo);
                editContent.setText("");

            }
        });

    }

    private void getChatData() {
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatInfo chatInfo = snapshot.getValue(ChatInfo.class);
                chatInfo.key = snapshot.getKey();

                chatInfoArrayList.add(chatInfo);

                adapter.notifyDataSetChanged();

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollToBottom();
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String removedKey = snapshot.getKey();

                // Iterator를 이용하여 안전하게 순회하고 삭제
                Iterator<ChatInfo> iterator = chatInfoArrayList.iterator();
                while (iterator.hasNext()) {
                    ChatInfo chatInfo = iterator.next();
                    if (removedKey.equals(chatInfo.key)) {
                        iterator.remove();
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void scrollToBottom() {
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }
}