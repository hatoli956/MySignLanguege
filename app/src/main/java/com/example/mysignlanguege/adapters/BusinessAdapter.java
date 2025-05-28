package com.example.mysignlanguege.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.screens.BusinessDetailsActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.screens.UpdateBusiness;

import java.util.List;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder> {

    private List<Business> businesses;
    private OnBusinessInteractionListener listener;
    private Context context;

    public interface OnBusinessInteractionListener {
        void onDeleteBusinessClicked(Business business);
        void onClickListener(Business business);
    }

    public BusinessAdapter(Context context, List<Business> businesses, OnBusinessInteractionListener listener) {
        this.context = context;
        this.businesses = businesses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_business, parent, false);
        return new BusinessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessViewHolder holder, int position) {
        Business business = businesses.get(position);
        holder.nameTextView.setText(business.getName());

        Log.d("DEBUG_ADAPTER", "ImageUrl length: " + (business.getImageUrl() != null ? business.getImageUrl().length() : "null"));

        holder.itemView.setOnClickListener(v -> this.listener.onClickListener(business));

        holder.deleteBusinessButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteBusinessClicked(business);
            }
        });
    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public void updateData(List<Business> newBusinesses) {
        this.businesses = newBusinesses;
        notifyDataSetChanged();
    }

    // ✅ מתודה חדשה לצפייה בפרטי העסק — לא נוגעת בלוגיקה הקיימת
    public void openBusinessDetails(Business business) {
        Intent intent = new Intent(context, BusinessDetailsActivity.class);
        intent.putExtra("business", business);
        context.startActivity(intent);
    }

    class BusinessViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private Button deleteBusinessButton;

        BusinessViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvName);
            deleteBusinessButton = itemView.findViewById(R.id.btnDeleteBusiness);
        }




    }

}
//        void bind(final Business business) {
//            nameTextView.setText(business.getName());
//
//            Log.d("DEBUG_ADAPTER", "ImageUrl length: " + (business.getImageUrl() != null ? business.getImageUrl().length() : "null"));
//
//            itemView.setOnClickListener(v -> );
//
//            deleteBusinessButton.setOnClickListener(v -> {
//                if (listener != null) {
//                    listener.onDeleteBusinessClicked(business);
//                }
//            });
//        }
//        void bind2(final Business business) {
//            nameTextView.setText(business.getName());
//
//            Log.d("DEBUG_ADAPTER", "ImageUrl length: " + (business.getImageUrl() != null ? business.getImageUrl().length() : "null"));
//
//            itemView.setOnClickListener(v -> {
//                Intent intent = new Intent(context, UpdateBusiness.class);
//                intent.putExtra("business", business);
//                context.startActivity(intent);
//            });
//
//            deleteBusinessButton.setOnClickListener(v -> {
//                if (listener != null) {
//                    listener.onDeleteBusinessClicked(business);
//                }
//            });
//        }
