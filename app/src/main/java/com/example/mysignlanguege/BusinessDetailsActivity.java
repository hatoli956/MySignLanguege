package com.example.mysignlanguege;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.models.Business;
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
        }
    }

    private Business getBusinessFromIntent() {
        return (Business) getIntent().getSerializableExtra("business");
    }

    private void populateViews(Business business) {
        tvName.setText(business.getName());
        tvCategory.setText(business.getCategory());
        tvCity.setText(business.getCity());
        tvPhone.setText(business.getPhone());
        tvEmail.setText(business.getEmail());
        tvStreet.setText(business.getStreet());
        tvWebsite.setText(business.getWebsite());
        tvDetails.setText(business.getDetails());

        String imageBase64 = business.getImage();
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            ivBusinessImage.setImageBitmap(ImageUtil.convertFrom64base(imageBase64));
            ivBusinessImage.setVisibility(View.VISIBLE);
        } else {
            ivBusinessImage.setVisibility(View.GONE);
        }
    }
}
