package com.example.mysignlanguege.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.Job;

import java.util.List;

public class JobApplicationAdapter extends RecyclerView.Adapter<JobApplicationAdapter.JobViewHolder> {

    private Context context;
    private List<Job> jobList;
    private OnJobApplyListener applyListener;

    public JobApplicationAdapter(Context context, List<Job> jobList, OnJobApplyListener applyListener) {
        this.context = context;
        this.jobList = jobList;
        this.applyListener = applyListener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job_application, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.tvTitle.setText(job.getJobTitle());
        holder.tvSalary.setText("שכר: " + job.getSalary());
        holder.tvEmail.setText("אימייל: " + job.getEmail());
        holder.tvEmployerName.setText("מעסיק: " + job.getEmployerName());

        // **Added: Notify listener when user clicks "apply" button**
        holder.btnApply.setOnClickListener(v -> {
            if (applyListener != null) {
                applyListener.onApplyJob(job);
            }
        });

        // --- Removed the old email intent here to handle it in activity ---
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public interface OnJobApplyListener {
        void onApplyJob(Job job);
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSalary, tvEmail, tvEmployerName;
        ImageButton btnApply;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvEmployerName = itemView.findViewById(R.id.tvEmployerName);
            btnApply = itemView.findViewById(R.id.btnApplyJob);
        }
    }
}
