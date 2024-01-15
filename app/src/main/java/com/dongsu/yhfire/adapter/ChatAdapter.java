package com.dongsu.yhfire.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dongsu.yhfire.R;
import com.dongsu.yhfire.dto.ChatInfo;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    Context context;
    ArrayList<ChatInfo> chatInfoArrayList;
    String currentUser = "";

    public ChatAdapter(Context context, ArrayList<ChatInfo> chatInfoArrayList, String currentUser) {
        this.context = context;
        this.chatInfoArrayList = chatInfoArrayList;
        this.currentUser = currentUser;
    }

    @Override
    public int getItemViewType(int position) {
        ChatInfo chatInfo = chatInfoArrayList.get(position);

        return chatInfo.name.equals(currentUser) ? 1 : 0;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (viewType == 1) ? R.layout.chat_row_right : R.layout.chat_row;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);

        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatInfo chatInfo = chatInfoArrayList.get(position);

        if (chatInfo.name.equals("master")){
            holder.txtName.setText("<<< Master >>>");
            holder.txtContent.setText(chatInfo.content);
            return;
        }

        holder.txtName.setText(chatInfo.name);
        holder.txtContent.setText(chatInfo.content);
    }

    @Override
    public int getItemCount() {
        return chatInfoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtContent;
        ImageView imgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtContent = itemView.findViewById(R.id.txtContent);
            imgView = itemView.findViewById(R.id.imgView);
        }

    }
}
