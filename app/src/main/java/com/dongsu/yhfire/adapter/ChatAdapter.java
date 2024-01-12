package com.dongsu.yhfire.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dongsu.yhfire.R;
import com.dongsu.yhfire.dto.ChatInfo;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    Context context;
    ArrayList<ChatInfo> chatInfoArrayList;

    public ChatAdapter(Context context, ArrayList<ChatInfo> chatInfoArrayList) {
        this.context = context;
        this.chatInfoArrayList = chatInfoArrayList;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_row, parent, false);

        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatInfo chatInfo = chatInfoArrayList.get(position);

        holder.txtName.setText(chatInfo.name + " :");
        holder.txtContent.setText(chatInfo.content);
    }

    @Override
    public int getItemCount() {
        return chatInfoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtContent = itemView.findViewById(R.id.txtContent);

        }
    }
}
