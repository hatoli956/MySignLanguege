package com.example.mysignlanguege.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.screens.Login;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.ImageUtil;

import java.util.List;

public class BusinessUserAdapter extends RecyclerView.Adapter<BusinessUserAdapter.BusinessViewHolder> {

    private List<Business> businessList;
    private Context context;
    private int layoutId;
    private OnBusinessInteractionListener interactionListener;
    private OnBusinessRemoveListener removeListener;
    private OnBusinessClickListener clickListener;
    private boolean isInterestedView;

    public interface OnBusinessInteractionListener {
        void onAddToInterestedClicked(Business business);
    }

    public interface OnBusinessRemoveListener {
        void onBusinessRemove(Business business);
    }

    public interface OnBusinessClickListener {
        void onBusinessClicked(Business business);
    }

    public void setOnBusinessInteractionListener(OnBusinessInteractionListener listener) {
        this.interactionListener = listener;
    }

    public void setOnBusinessRemoveListener(OnBusinessRemoveListener listener) {
        this.removeListener = listener;
    }

    public void setOnBusinessClickListener(OnBusinessClickListener listener) {
        this.clickListener = listener;
    }

    public BusinessUserAdapter(List<Business> businessList, Context context, int layoutId) {
        this.businessList = businessList;
        this.context = context;
        this.layoutId = layoutId;
        this.isInterestedView = context.getClass().getSimpleName().equals("InterestedBusinessesActivity");
    }

    @NonNull
    @Override
    public BusinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new BusinessViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessViewHolder holder, int position) {
        Business finalBusiness = new Business(businessList.get(position));

        holder.nameTextView.setText(finalBusiness.getName());
        holder.categoryTextView.setText(finalBusiness.getCategory());
        holder.cityTextView.setText(finalBusiness.getCity());
        holder.phoneTextView.setText(finalBusiness.getPhone());
        holder.emailTextView.setText(finalBusiness.getEmail());
        holder.streetTextView.setText(finalBusiness.getStreet());
        holder.websiteTextView.setText(finalBusiness.getWebsite());
        holder.detailsTextView.setText(finalBusiness.getDetails());

        String imageBase64 = finalBusiness.getImageUrl();
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            holder.businessImageView.setImageBitmap(ImageUtil.convertFrom64base(imageBase64));
            holder.businessImageView.setVisibility(View.VISIBLE);
        } else {
            holder.businessImageView.setVisibility(View.GONE);
        }

        if (holder.addToInterestedButton != null) {
            if (isInterestedView) {
                holder.addToInterestedButton.setText("Remove");
                holder.addToInterestedButton.setOnClickListener(v -> {
                    if (removeListener != null) {
                        removeListener.onBusinessRemove(finalBusiness);
                    }
                });
            } else {
                holder.addToInterestedButton.setText("Add to Interested");
                holder.addToInterestedButton.setOnClickListener(v -> {
                    if (Login.user != null) {
                        addBusinessToInterestedList(finalBusiness);
                    } else {
                        Toast.makeText(context, "Please log in first", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        // Disable item click to avoid confusion, only button clicks are handled
        holder.itemView.setOnClickListener(null);

        if (holder.btnViewJobs != null) {
            holder.btnViewJobs.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onBusinessClicked(finalBusiness);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    private void addBusinessToInterestedList(Business business) {
        if (Login.user == null) return;

        DatabaseService databaseService = DatabaseService.getInstance();
        databaseService.writeData("Users/" + Login.user.getId() + "/interestedBusinesses/" + business.getId(),
                business, new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void object) {
                        Toast.makeText(context, "Added to interested businesses", Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                        String interestedBusinesses = sharedPreferences.getString("interestedBusinesses", "");
                        if (!interestedBusinesses.contains(business.getId())) {
                            interestedBusinesses += business.getId() + ",";
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("interestedBusinesses", interestedBusinesses);
                            editor.apply();
                        }
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Toast.makeText(context, "Failed to add to interested businesses", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class BusinessViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, categoryTextView, cityTextView, phoneTextView, emailTextView;
        TextView streetTextView, websiteTextView, detailsTextView;
        Button addToInterestedButton;
        Button btnViewJobs;
        ImageView businessImageView;

        public BusinessViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tvName);
            categoryTextView = itemView.findViewById(R.id.tvCategory);
            cityTextView = itemView.findViewById(R.id.tvCity);
            phoneTextView = itemView.findViewById(R.id.tvPhone);
            emailTextView = itemView.findViewById(R.id.tvEmail);
            streetTextView = itemView.findViewById(R.id.tvStreet);
            websiteTextView = itemView.findViewById(R.id.tvWebsite);
            detailsTextView = itemView.findViewById(R.id.tvDetails);
            businessImageView = itemView.findViewById(R.id.ivBusinessImage);
            addToInterestedButton = itemView.findViewById(R.id.btnAddToInterested);
            btnViewJobs = itemView.findViewById(R.id.btnViewJobs);
        }
    }
}
