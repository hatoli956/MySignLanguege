package com.example.mysignlanguege.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.mysignlanguege.models.User;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.ImageUtil;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;

import java.util.List;

public class BusinessUserAdapter extends RecyclerView.Adapter<BusinessUserAdapter.BusinessViewHolder> {

    private List<Business> businessList;
    private Context context;

    public BusinessUserAdapter(List<Business> businessList, Context context) {
        this.businessList = businessList;
        this.context = context;
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

        business= new Business(business);

        // Set business data to the views
        holder.nameTextView.setText(business.getName());
        holder.categoryTextView.setText(business.getCategory());
        holder.cityTextView.setText(business.getCity());
        holder.phoneTextView.setText(business.getPhone());
        holder.emailTextView.setText(business.getEmail());
        holder.streetTextView.setText(business.getStreet());
        holder.websiteTextView.setText(business.getWebsite());
        holder.detailsTextView.setText(business.getDetails());


        // Load business image
        holder.businessImageView.setImageBitmap(ImageUtil.convertFrom64base(business.getImage()));

        User user = SharedPreferencesUtil.getUser(this.context);
        assert user != null;
        if(user.isAdmin())
        {
            holder.addToInterestedButton.setVisibility(View.INVISIBLE);
            // Handle "Add to Interested" button click
            Business finalBusiness = business;
            holder.addToInterestedButton.setOnClickListener(v -> addBusinessToInterestedList(finalBusiness));
        }
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    private void addBusinessToInterestedList(Business business) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve existing list of interested businesses
        String interestedBusinesses = sharedPreferences.getString("interestedBusinesses", "");
        interestedBusinesses += business.getId() + ","; // Append business ID

        // Save updated list of interested businesses
        editor.putString("interestedBusinesses", interestedBusinesses);
        editor.apply();

        User user = SharedPreferencesUtil.getUser(this.context);

       if(user != null) {
           DatabaseService databaseService = DatabaseService.getInstance();

           databaseService.writeData("Users/" + user.getId() + "/interestedBusinesses/" + business.getId(), business, new DatabaseService.DatabaseCallback<Void>() {
               @Override
               public void onCompleted(Void object) {

               }

               @Override
               public void onFailed(Exception e) {

               }
           });
       }
    }


    public static class BusinessViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, categoryTextView, cityTextView, phoneTextView, emailTextView;
        TextView streetTextView, websiteTextView, detailsTextView, adminTextView;
        Button addToInterestedButton;
        ImageView businessImageView;

        public BusinessViewHolder(View itemView) {
            super(itemView);

            // Initialize views
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
