package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.utils.ImageUtil;

public class BusinessDetailsActivity extends BaseActivity {

    private TextView tvName, tvCategory, tvCity, tvPhone, tvEmail, tvStreet, tvWebsite, tvDetails;
    private ImageView ivBusinessImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

        tvName = findViewById(R.id.tvName);
        tvCategory = findViewById(R.id.tvCategory);
        tvCity = findViewById(R.id.tvCity);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvStreet = findViewById(R.id.tvStreet);
        tvWebsite = findViewById(R.id.tvWebsite);
        tvDetails = findViewById(R.id.tvDetails);
        ivBusinessImage = findViewById(R.id.ivBusinessImage);

        Business business = getBusinessFromIntent();

        if (business != null) {
            populateViews(business);
        } else {
            Log.e("BusinessDetails", "Business object is null!");
            Toast.makeText(this, "שגיאה: לא התקבל מידע על העסק", Toast.LENGTH_LONG).show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
    }

    private Business getBusinessFromIntent() {
        return (Business) getIntent().getSerializableExtra("business");
    }

    public void GoBack(View view) {
        Intent go = new Intent(getApplicationContext(), AllBusiness.class);
        startActivity(go);
    }

    private void populateViews(Business business) {
        tvName.setText(business.getName());
        tvCategory.setText("קטגוריה: " + business.getCategory());
        tvCity.setText("עיר: " + business.getCity());
        tvPhone.setText("טלפון: " + business.getPhone());
        tvEmail.setText("אימייל: " + business.getEmail());
        tvStreet.setText("רחוב: " + business.getStreet());
        tvWebsite.setText("אתר: " + business.getWebsite());
        tvDetails.setText("פרטים: " + business.getDetails());

        String imageBase64 = business.getImageUrl();
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            ivBusinessImage.setImageBitmap(ImageUtil.convertFrom64base(imageBase64));
            ivBusinessImage.setVisibility(View.VISIBLE);
        } else {
            ivBusinessImage.setVisibility(View.GONE);
        }
    }
}
