package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.ImageUtil;

public class UpdateBusiness extends BaseActivity {

    private EditText etName, etCategory, etCity, etPhone, etEmail, etStreet, etWebsite, etDetails;
    private ImageView ivBusinessImage;
    private Button btnSave;

    private Business business;

    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_business);

        databaseService = DatabaseService.getInstance();

        etName = findViewById(R.id.etName);
        etCategory = findViewById(R.id.etCategory);
        etCity = findViewById(R.id.etCity);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etStreet = findViewById(R.id.etStreet);
        etWebsite = findViewById(R.id.etWebsite);
        etDetails = findViewById(R.id.etDetails);
        ivBusinessImage = findViewById(R.id.ivBusinessImage);
        btnSave = findViewById(R.id.btnSave);

        business = getBusinessFromIntent();

        if (business != null) {
            populateViews(business);
        } else {
            Toast.makeText(this, "Business data not found", Toast.LENGTH_SHORT).show();
            Log.e("UpdateBusiness", "Business object is null.");
            finish();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBusinessDetails();
            }
        });

        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#1A237E"));
    }

    private Business getBusinessFromIntent() {
        Business b = (Business) getIntent().getSerializableExtra("business");
        Log.d("UpdateBusiness", "Business received: " + (b != null ? b.getName() : "null"));
        return b;
    }

    private void populateViews(Business business) {
        etName.setText(business.getName());
        etCategory.setText(business.getCategory());
        etCity.setText(business.getCity());
        etPhone.setText(business.getPhone());
        etEmail.setText(business.getEmail());
        etStreet.setText(business.getStreet());
        etWebsite.setText(business.getWebsite());
        etDetails.setText(business.getDetails());

        String imageBase64 = business.getImageUrl();
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            ivBusinessImage.setImageBitmap(ImageUtil.convertFrom64base(imageBase64));
            ivBusinessImage.setVisibility(View.VISIBLE);
        } else {
            ivBusinessImage.setVisibility(View.GONE);
        }
    }

    private void saveBusinessDetails() {
        if (business == null) {
            Toast.makeText(this, "Cannot save. Business data is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update business fields with current UI values
        business.setName(etName.getText().toString());
        business.setCategory(etCategory.getText().toString());
        business.setCity(etCity.getText().toString());
        business.setPhone(etPhone.getText().toString());
        business.setEmail(etEmail.getText().toString());
        business.setStreet(etStreet.getText().toString());
        business.setWebsite(etWebsite.getText().toString());
        business.setDetails(etDetails.getText().toString());

        databaseService.createNewBusiness(business, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                finish();  // close this activity
            }

            @Override
            public void onFailed(Exception e) {

            }
        });


    }
}
