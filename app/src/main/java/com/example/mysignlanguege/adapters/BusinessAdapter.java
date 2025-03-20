package com.example.mysignlanguege.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.ImageUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder> {

    private List<Business> businessList;

    // Add constructor to initialize business list
    public BusinessAdapter(List<Business> businessList) {
        this.businessList = businessList;
    }

    @NonNull
    @Override
    public BusinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business, parent, false);
        return new BusinessViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessViewHolder holder, int position) {
        final Business business = businessList.get(position);

        // Set data to the views
        holder.nameTextView.setText(business.getName());
        holder.categoryTextView.setText(business.getCategory());
        holder.cityTextView.setText(business.getCity());
        holder.phoneTextView.setText(business.getPhone());
        holder.emailTextView.setText(business.getEmail());
        holder.streetTextView.setText(business.getStreet());
        holder.websiteTextView.setText(business.getWebsite());
        holder.detailsTextView.setText(business.getDetails());
        holder.businessImageView.setImageBitmap(ImageUtil.convertFrom64base(business.getImage()));

        // Handle remove button click
        holder.removeButton.setOnClickListener(v -> removeBusiness(business, position));
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    // Remove business from the list and Firebase
    private void removeBusiness(Business business, int position) {
        // Now remove the business from Firebase
        DatabaseService databaseService = DatabaseService.getInstance();

        // Log the business ID to ensure we're sending the correct ID to Firebase
        Log.d("BusinessAdapter", "Attempting to remove business with ID: " + business.getId());

        // Ensure you're using the correct Firebase node to remove the business
        // Assuming "businesses" is the node containing your business records
        databaseService.removeBusiness(business.getId(), new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                // Successfully removed from Firebase, you can show a Toast or log message if needed
                Log.d("BusinessAdapter", "Business removed from Firebase successfully");
                // Remove from the local list first
                businessList.remove(position);
                notifyItemRemoved(position); // Update the RecyclerView to remove the item visually
            }

            @Override
            public void onFailed(Exception e) {
                // Handle failure (e.g., show an error message)
                Log.e("BusinessAdapter", "Failed to remove business from Firebase", e);
            }
        });
    }



    public static class BusinessViewHolder extends RecyclerView.ViewHolder {

        ImageView businessImageView;
        TextView nameTextView, categoryTextView, cityTextView, phoneTextView, websiteTextView, detailsTextView;
        TextView emailTextView, streetTextView;
        Button removeButton; // Remove button

        public BusinessViewHolder(View itemView) {
            super(itemView);

            // Initialize views
            businessImageView = itemView.findViewById(R.id.ivBusiness);
            nameTextView = itemView.findViewById(R.id.tvNname);
            categoryTextView = itemView.findViewById(R.id.tvCategory);
            cityTextView = itemView.findViewById(R.id.tvCity);
            phoneTextView = itemView.findViewById(R.id.tvPhone);
            websiteTextView = itemView.findViewById(R.id.tvWebsite);
            detailsTextView = itemView.findViewById(R.id.tvDetails);
            emailTextView = itemView.findViewById(R.id.tvEmail);
            streetTextView = itemView.findViewById(R.id.tvStreet);
            removeButton = itemView.findViewById(R.id.btnRemove); // Initialize remove button
        }
    }
}
