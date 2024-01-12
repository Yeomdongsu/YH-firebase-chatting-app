package com.dongsu.yhfire.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dongsu.yhfire.ChatActivity;
import com.dongsu.yhfire.MainActivity;
import com.dongsu.yhfire.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    Context context;
    ArrayList<String> roomList;

    public MainAdapter(Context context, ArrayList<String> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_row, parent, false);

        return new MainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String roomName = roomList.get(position);

        holder.txtRoomName.setText(roomName);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRoomName;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRoomName = itemView.findViewById(R.id.txtRoomName);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();

                    String chatName = roomList.get(index);

                    SharedPreferences sp = context.getSharedPreferences("yhFire", Context.MODE_PRIVATE);
                    String userName = sp.getString("name", "");

                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("chatName", chatName);
                    intent.putExtra("userName", userName);
                    ((MainActivity) context).startActivity(intent);
                }
            });
        }
    }
}
