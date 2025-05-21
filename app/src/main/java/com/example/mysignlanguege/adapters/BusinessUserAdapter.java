package com.example.mysignlanguege.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.Login;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.ImageUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BusinessUserAdapter extends RecyclerView.Adapter<BusinessUserAdapter.BusinessViewHolder> {

    private List<Business> businessList;
    private Context context;
    private OnBusinessInteractionListener interactionListener;
    private OnBusinessRemoveListener removeListener;
    private boolean isInterestedView;

    // ממשק להוספה
    public interface OnBusinessInteractionListener {
        void onAddToInterestedClicked(Business business);
    }

    // ממשק להסרה
    public interface OnBusinessRemoveListener {
        void onBusinessRemove(Business business);
    }

    public void setOnBusinessInteractionListener(OnBusinessInteractionListener listener) {
        this.interactionListener = listener;
    }

    public void setOnBusinessRemoveListener(OnBusinessRemoveListener listener) {
        this.removeListener = listener;
    }

    public BusinessUserAdapter(List<Business> businessList, Context context) {
        this.businessList = businessList;
        this.context = context;
        this.isInterestedView = context.getClass().getSimpleName().equals("InterestedBusinessesActivity");
    }

    @NonNull
    @Override
    public BusinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_business_user, parent, false);
        return new BusinessViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessViewHolder holder, int position) {
        Business business = businessList.get(position);

        business = new Business(business);

        holder.nameTextView.setText(business.getName());
        holder.categoryTextView.setText(business.getCategory());
        holder.cityTextView.setText(business.getCity());
        holder.phoneTextView.setText(business.getPhone());
        holder.emailTextView.setText(business.getEmail());
        holder.streetTextView.setText(business.getStreet());
        holder.websiteTextView.setText(business.getWebsite());
        holder.detailsTextView.setText(business.getDetails());

        String imageBase64 = business.getImage();
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            holder.businessImageView.setImageBitmap(ImageUtil.convertFrom64base(imageBase64));
            holder.businessImageView.setVisibility(View.VISIBLE);
        } else {
            holder.businessImageView.setVisibility(View.GONE);
        }

        if (isInterestedView) {
            holder.addToInterestedButton.setText("Remove");
            Business finalBusiness = business;
            holder.addToInterestedButton.setOnClickListener(v -> {
                if (removeListener != null) {
                    removeListener.onBusinessRemove(finalBusiness);
                }
            });
        } else {
            holder.addToInterestedButton.setText("Add to Interested");
            Business finalBusiness = business;
            holder.addToInterestedButton.setOnClickListener(v -> {
                if (Login.user != null) {
                    addBusinessToInterestedList(finalBusiness);
                } else {
                    Toast.makeText(context, "Please log in first", Toast.LENGTH_SHORT).show();
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
                        // עדכון SharedPreferences
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
        ImageView businessImageView;

        public BusinessViewHolder(View itemView) {
            super(itemView);

            businessImageView = itemView.findViewById(R.id.ivBusinessImage);
            nameTextView = itemView.findViewById(R.id.tvName);
            categoryTextView = itemView.findViewById(R.id.tvCategory);
            cityTextView = itemView.findViewById(R.id.tvCity);
            phoneTextView = itemView.findViewById(R.id.tvPhone);
            emailTextView = itemView.findViewById(R.id.tvEmail);
            streetTextView = itemView.findViewById(R.id.tvStreet);
            websiteTextView = itemView.findViewById(R.id.tvWebsite);
            detailsTextView = itemView.findViewById(R.id.tvDetails);
            addToInterestedButton = itemView.findViewById(R.id.btnAddToInterested);
        }
    }
}
