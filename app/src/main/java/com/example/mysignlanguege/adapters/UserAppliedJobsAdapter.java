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

public class UserAppliedJobsAdapter extends RecyclerView.Adapter<UserAppliedJobsAdapter.AppliedJobViewHolder> {

    private Context context;
    private List<Job> jobList;
    private OnJobRemoveListener removeListener;

    public UserAppliedJobsAdapter(Context context, List<Job> jobList, OnJobRemoveListener removeListener) {
        this.context = context;
        this.jobList = jobList;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public AppliedJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job_application, parent, false);
        return new AppliedJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedJobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.tvTitle.setText(job.getJobTitle());
        holder.tvSalary.setText("שכר: " + job.getSalary());
        holder.tvEmail.setText("אימייל: " + job.getEmail());
        holder.tvEmployerName.setText("מעסיק: " + job.getEmployerName());

        // Change apply button to remove button (icon and action)
        holder.btnApply.setImageResource(android.R.drawable.ic_menu_delete);
        holder.btnApply.setContentDescription("הסר משרה");
        holder.btnApply.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onRemoveJob(job, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public interface OnJobRemoveListener {
        void onRemoveJob(Job job, int position);
    }

    public static class AppliedJobViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSalary, tvEmail, tvEmployerName;
        ImageButton btnApply;

        public AppliedJobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvEmployerName = itemView.findViewById(R.id.tvEmployerName);
            btnApply = itemView.findViewById(R.id.btnApplyJob);
        }
    }
}
