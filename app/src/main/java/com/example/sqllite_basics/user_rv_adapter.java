package com.example.sqllite_basics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class user_rv_adapter extends RecyclerView.Adapter<user_rv_adapter.MyViewHolder> {
    Context context;
    ArrayList<user_model> UserArrayList;

    public user_rv_adapter(Context context, ArrayList<user_model> UserArrayList) {
        this.context = context;
        this.UserArrayList = UserArrayList;
    }

    @NonNull
    @Override
    public user_rv_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_fragment, parent, false);
        return new user_rv_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull user_rv_adapter.MyViewHolder holder, int position) {
        holder.id.setText(UserArrayList.get(position).getId());
        holder.username.setText(UserArrayList.get(position).getUsername());
        holder.password.setText(UserArrayList.get(position).getPassword());
    }

    @Override
    public int getItemCount() {
        return UserArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView username;
        TextView password;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.user_id);
            username = itemView.findViewById(R.id.username);
            password = itemView.findViewById(R.id.password);

        }
    }
}
