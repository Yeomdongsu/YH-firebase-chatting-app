package com.dongsu.yhfire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dongsu.yhfire.adapter.MainAdapter;
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

    // 유저 등급 확인 버튼
    Button btnCheck;

    // 등급에 따라 화면에 띄울 레이아웃
    LinearLayout roomLayout;
    LinearLayout madeRoomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editChatName = findViewById(R.id.editChatName);
        editUserName = findViewById(R.id.editUserName);
        btnCheck = findViewById(R.id.btnCheck);
        btnStart = findViewById(R.id.btnStart);
        roomLayout = findViewById(R.id.roomLayout);
        madeRoomLayout = findViewById(R.id.madeRoomLayout);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        adapter = new MainAdapter(MainActivity.this, roomList);
        recyclerView.setAdapter(adapter);

        getRoomList();

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editUserName.getText().toString().trim();

                // 키보드를 숨김
                InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editUserName.getWindowToken(), 0);

                if (userName.isEmpty()){
                    Snackbar.make(btnCheck, "입력하고 눌러주세요.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sp = getSharedPreferences("yhFire", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name", userName);
                editor.apply();

                if (userName.equals("master")){
                    ShowAlertDialog("master");
                } else {
                    ShowAlertDialog("일반 회원");
                }

            }
        });

        // master 유저일 경우만
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

    private void ShowAlertDialog(String grade) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle("유저 등급 확인");

        if (grade.equals("master")){
            builder.setMessage("master 유저입니다.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    roomLayout.setVisibility(View.VISIBLE);
                    madeRoomLayout.setVisibility(View.VISIBLE);
                }
            });

        } else {
            builder.setMessage("일반 유저입니다.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    roomLayout.setVisibility(View.GONE);
                    madeRoomLayout.setVisibility(View.VISIBLE);
                }
            });
        }

        builder.show();
    }
}