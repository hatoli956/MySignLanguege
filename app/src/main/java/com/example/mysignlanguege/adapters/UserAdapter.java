package com.example.mysignlanguege.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.fName.setText(user.getfName());
        holder.lName.setText(user.getlName());
        holder.email.setText(user.getEmail());
        holder.phone.setText(user.getPhone());
        holder.password.setText(user.getPassword());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView fName, lName, email, phone, password;

        public UserViewHolder(View itemView) {
            super(itemView);
            fName = itemView.findViewById(R.id.fName);
            lName = itemView.findViewById(R.id.lName);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            password = itemView.findViewById(R.id.password);
        }
    }
}
