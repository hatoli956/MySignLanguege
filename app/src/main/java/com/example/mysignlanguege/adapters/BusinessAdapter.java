package com.example.mysignlanguege.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.screens.JobsList;
import com.example.mysignlanguege.screens.BusinessDetailsActivity;
import com.example.mysignlanguege.screens.UpdateBusiness;
import com.example.mysignlanguege.utils.ImageUtil;

import java.util.List;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder> {

    private List<Business> businesses;
    private OnBusinessInteractionListener listener;
    private Context context;
    private int layoutResource; // ✅ תמיכה בפריסה מותאמת

    public interface OnBusinessInteractionListener {
        void onDeleteBusinessClicked(Business business);
        void onClickListener(Business business);
    }

    // ✅ בנאי רגיל - ממשיך להשתמש בפריסת employer
    public BusinessAdapter(Context context, List<Business> businesses, OnBusinessInteractionListener listener) {
        this(context, businesses, listener, R.layout.item_business_employer);
    }

    // ✅ בנאי מורחב - מאפשר לבחור layout
    public BusinessAdapter(Context context, List<Business> businesses, OnBusinessInteractionListener listener, int layoutResource) {
        this.context = context;
        this.businesses = businesses;
        this.listener = listener;
        this.layoutResource = layoutResource;
    }

    @NonNull
    @Override
    public BusinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutResource, parent, false);
        return new BusinessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessViewHolder holder, int position) {
        Business business = businesses.get(position);
        holder.nameTextView.setText(business.getName());

        Log.d("DEBUG_ADAPTER", "ImageUrl length: " + (business.getImageUrl() != null ? business.getImageUrl().length() : "null"));

        holder.deleteBusinessButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteBusinessClicked(business);
            }
        });

        holder.nameTextView.setOnClickListener(v -> listener.onClickListener(business));
        holder.nameTextView.setText(business.getName());



        // ✅ הגנה מפני שדות חסרים בלייאאוט הפשוט
        if (holder.tvCategory != null) holder.tvCategory.setText(business.getCategory());
        if (holder.tvCity != null) holder.tvCity.setText(business.getCity());
        if (holder.tvPhone != null) holder.tvPhone.setText(business.getPhone());
        if (holder.tvEmail != null) holder.tvEmail.setText(business.getEmail());
        if (holder.tvStreet != null) holder.tvStreet.setText(business.getStreet());
        if (holder.tvWebsite != null) holder.tvWebsite.setText(business.getWebsite());
        if (holder.tvDetails != null) holder.tvDetails.setText(business.getDetails());

        String imageBase64 = business.getImageUrl();
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            if (holder.ivBusinessImage != null) {
                holder.ivBusinessImage.setImageBitmap(ImageUtil.convertFrom64base(imageBase64));
                holder.ivBusinessImage.setVisibility(View.VISIBLE);
            }
        } else {
            if (holder.ivBusinessImage != null) {
                holder.ivBusinessImage.setVisibility(View.GONE);
            }
        }

        if (holder.btnViewJobs != null) {
            holder.btnViewJobs.setOnClickListener(v -> {
                Intent intent = new Intent(context, JobsList.class);
                intent.putExtra("business", business);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public void updateData(List<Business> newBusinesses) {
        this.businesses = newBusinesses;
        notifyDataSetChanged();
    }

    class BusinessViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private Button deleteBusinessButton;

        // ✅ שדות אופציונליים לפי layout
        private TextView tvCategory, tvCity, tvPhone, tvEmail, tvStreet, tvWebsite, tvDetails;
        private ImageView ivBusinessImage;
        private Button btnViewJobs;

        BusinessViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvName);
            deleteBusinessButton = itemView.findViewById(R.id.btnDeleteBusiness);

            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvStreet = itemView.findViewById(R.id.tvStreet);
            tvWebsite = itemView.findViewById(R.id.tvWebsite);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            ivBusinessImage = itemView.findViewById(R.id.ivBusinessImage);
            btnViewJobs = itemView.findViewById(R.id.btnViewJobs);
        }
    }


}
