    package com.example.mysignlanguege.adapters;

    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.os.Handler;
    import android.os.Looper;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.mysignlanguege.R;
    import com.example.mysignlanguege.models.Business;

    import java.io.InputStream;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.util.List;

    public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder> {

        private List<Business> businessList;

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
            Business business = businessList.get(position);

            // Set data to the views
            holder.nameTextView.setText(business.getName());
            holder.categoryTextView.setText(business.getCategory());
            holder.cityTextView.setText(business.getCity());
            holder.phoneTextView.setText(business.getPhone());
            holder.emailTextView.setText(business.getEmail());
            holder.streetTextView.setText(business.getStreet());
            holder.websiteTextView.setText(business.getWebsite());
            holder.detailsTextView.setText(business.getDetails());

            // Set image dynamically
            if (business.getImage() != null && !business.getImage().isEmpty()) {
                loadImageFromUrl(business.getImage(), holder.businessImageView);
            }
        }

        @Override
        public int getItemCount() {
            return businessList.size();
        }

        // Method to load image from URL
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

        // ViewHolder class to hold the views
        public static class BusinessViewHolder extends RecyclerView.ViewHolder {

            ImageView businessImageView;
            TextView nameTextView, categoryTextView, cityTextView, phoneTextView, websiteTextView, detailsTextView;
            TextView emailTextView, streetTextView;

            public BusinessViewHolder(View itemView) {
                super(itemView);

                // Initialize views
                businessImageView = itemView.findViewById(R.id.businessImageView);
                nameTextView = itemView.findViewById(R.id.nameTextView);
                categoryTextView = itemView.findViewById(R.id.categoryTextView);
                cityTextView = itemView.findViewById(R.id.cityTextView);
                phoneTextView = itemView.findViewById(R.id.phoneTextView);
                websiteTextView = itemView.findViewById(R.id.websiteTextView);
                detailsTextView = itemView.findViewById(R.id.detailsTextView);
                emailTextView = itemView.findViewById(R.id.emailTextView);
                streetTextView = itemView.findViewById(R.id.streetTextView);
            }
        }
    }
