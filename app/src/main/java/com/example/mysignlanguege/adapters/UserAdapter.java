package com.example.mysignlanguege.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.UserDetailsActivity;
import com.example.mysignlanguege.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
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

        // לחיצה עוברת לדף פרטי משתמש
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra("USER_ID", user.getId());
            context.startActivity(intent);
        });

        // לחצן מחיקה
        holder.btnDelete.setOnClickListener(v -> {
            String userId = user.getId();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            ref.removeValue().addOnSuccessListener(aVoid -> {
                userList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, userList.size());
                Toast.makeText(context, "המשתמש נמחק בהצלחה", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "שגיאה במחיקה: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView fName;
        Button btnDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            fName = itemView.findViewById(R.id.fName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
