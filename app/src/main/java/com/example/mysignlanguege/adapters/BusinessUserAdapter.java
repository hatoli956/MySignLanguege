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

        if(!Login.isAdmin) {
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



       if( Login.user!=null) {
           DatabaseService databaseService = DatabaseService.getInstance();

           databaseService.writeData("Users/" + Login.user.getId() + "/interestedBusinesses/" + business.getId(), business, new DatabaseService.DatabaseCallback<Void>() {
               @Override
               public void onCompleted(Void object) {

               }

               @Override
               public void onFailed(Exception e) {

               }
           });


       }
    }
    private void loadImageFromUrl(final String imageUrl, final ImageView imageView) {
        // Use a separate thread to download the image (to avoid blocking UI thread)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(input);

                    // Run the code to update UI on the main thread
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
            if(Login.isAdmin)
                addToInterestedButton.setVisibility(View.INVISIBLE);
        }
    }
}
