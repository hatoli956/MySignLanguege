package com.example.mysignlanguege.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

        // טיפול בלחיצה על כפתור "הגש מועמדות"
//        holder.btnApply.setOnClickListener(v -> {
//            if (applyListener != null) {
//                applyListener.onApplyJob(job);
//            }
//        });

        holder.btnApply.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822"); // ספציפי לאפליקציות מייל
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{job.getEmail()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "הגשת מועמדות למשרה: " + job.getJobTitle());
            intent.putExtra(Intent.EXTRA_TEXT, "שלום,\nאני מעוניין להגיש מועמדות למשרה שפרסמתם.");

            Intent chooser = Intent.createChooser(intent, "בחר אפליקציית דוא\"ל");

            try {
                context.startActivity(chooser);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "לא נמצאה אפליקציית דוא\"ל מותקנת", Toast.LENGTH_SHORT).show();
            }


        });

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
            btnApply = itemView.findViewById(R.id.btnApplyJob);  // ודא שזה האיידי בכפתור שלך
        }
    }
}
