package com.example.mysignlanguege.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.Job;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private Context context;
    private List<Job> jobList;
    private OnJobDeleteListener deleteListener;


    public JobAdapter(Context context, List<Job> jobList, OnJobDeleteListener deleteListener) {
        this.context = context;
        this.jobList = jobList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.tvTitle.setText(job.getJobTitle());
        holder.tvSalary.setText("שכר: " + job.getSalary());
        holder.tvEmail.setText("אימייל: " + job.getEmail());
        holder.tvEmployerName.setText("מעסיק: " + job.getEmployerName());

        // טיפול בלחיצה על כפתור מחיקה
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteJob(job);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public interface OnJobDeleteListener {
        void onDeleteJob(Job job);
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSalary, tvEmail, tvEmployerName;
        ImageButton btnDelete;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvEmployerName = itemView.findViewById(R.id.tvEmployerName);
            btnDelete = itemView.findViewById(R.id.btnDeleteJob);
        }
    }
}
