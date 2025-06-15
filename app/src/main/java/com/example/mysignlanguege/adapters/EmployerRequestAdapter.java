package com.example.mysignlanguege.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.EmployerRequest;
import com.example.mysignlanguege.screens.RequestDetailsActivity;

import java.util.List;

public class EmployerRequestAdapter extends RecyclerView.Adapter<EmployerRequestAdapter.RequestViewHolder> {

    private Context context;
    private List<EmployerRequest> requestList;

    public EmployerRequestAdapter(Context context, List<EmployerRequest> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        EmployerRequest request = requestList.get(position);
        holder.tvName.setText(request.fullName);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RequestDetailsActivity.class);
            intent.putExtra("userId", request.userId);
            intent.putExtra("fullName", request.fullName);
            intent.putExtra("reason", request.reason);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
