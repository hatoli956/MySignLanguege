package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.models.User;
import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.ImageUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddBusiness extends BaseActivity implements View.OnClickListener {

    Spinner spBusinessType, spBusinessCity;
    EditText etBusinessName, etBusinessPhone, etBusinessEmail, etBusinessAddress, etBusinessWebsite, etBusinessDetails;
    Button btnGallery, btnCamera, btnAddItem;
    ImageView imageBusiness;

    private ActivityResultLauncher<Intent> selectImageLauncher;
    private ActivityResultLauncher<Intent> captureImageLauncher;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    private User user = new User();
    private String uid;
    private Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);

        initViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageUtil.requestPermission(this);

        databaseService = DatabaseService.getInstance();
        authenticationService = AuthenticationService.getInstance();
        uid = authenticationService.getCurrentUserId();

        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        imageBusiness.setImageURI(selectedImageUri);
                    }
                });

        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        imageBusiness.setImageBitmap(bitmap);
                        selectedImageUri = null;
                    }
                });

        databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User u) {
                user = u;
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("AddBusiness", "Failed to get user", e);
            }
        });
    }

    private void initViews() {
        btnAddItem = findViewById(R.id.btnAddItem);
        btnGallery = findViewById(R.id.btnGallery);
        btnCamera = findViewById(R.id.btnCamera);

        spBusinessType = findViewById(R.id.spBusinessType);
        spBusinessCity = findViewById(R.id.spBusinessCity);

        etBusinessName = findViewById(R.id.etBusinessName);
        etBusinessPhone = findViewById(R.id.etBusinessPhone);
        etBusinessEmail = findViewById(R.id.etBusinessEmail);
        etBusinessAddress = findViewById(R.id.etBusinessAddress);
        etBusinessWebsite = findViewById(R.id.etBusinessWebsite);
        etBusinessDetails = findViewById(R.id.etBusinessDetails);
        imageBusiness = findViewById(R.id.imageRef);

        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnAddItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnAddItem.getId()) {
            addBusinessToDatabase();
        } else if (v.getId() == btnGallery.getId()) {
            selectImageFromGallery();
        } else if (v.getId() == btnCamera.getId()) {
            captureImageFromCamera();
        }
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        selectImageLauncher.launch(Intent.createChooser(intent, "בחר תמונה"));
    }

    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }

    private void addBusinessToDatabase() {
        String businessType = spBusinessType.getSelectedItem().toString();
        String businessCity = spBusinessCity.getSelectedItem().toString();

        String businessName = etBusinessName.getText().toString();
        String businessDetails = etBusinessDetails.getText().toString();
        String businessAddress = etBusinessAddress.getText().toString();
        String businessEmail = etBusinessEmail.getText().toString();
        String businessPhone = etBusinessPhone.getText().toString();
        String businessWebsite = etBusinessWebsite.getText().toString();

        if (businessName.isEmpty() || businessPhone.isEmpty()) {
            Toast.makeText(this, "יש למלא שם וטלפון של העסק", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databaseService.generateBusinessId();
        Business business = new Business();
        business.setId(id);
        business.setName(businessName);
        business.setCategory(businessType);
        business.setPhone(businessPhone);
        business.setEmail(businessEmail);
        business.setStreet(businessAddress);
        business.setWebsite(businessWebsite);
        business.setCity(businessCity);
        business.setDetails(businessDetails);

        // Set current user as owner
        business.setOwnerId(uid);

        String imageBase64 = null;

        if (selectedImageUri != null) {
            imageBase64 = encodeImageToBase64(selectedImageUri);
        } else if (imageBusiness.getDrawable() != null) {
            imageBusiness.setDrawingCacheEnabled(true);
            imageBusiness.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageBusiness.getDrawable()).getBitmap();
            imageBase64 = encodeBitmapToBase64(bitmap);
        }

        business.setImageUrl(imageBase64); // Save base64 in imageUrl

        saveBusinessToDatabase(business);
    }


    private void saveBusinessToDatabase(Business business) {
        databaseService.createNewBusiness(business, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(AddBusiness.this, "העסק נוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddBusiness.this, EmployerPage.class));
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AddBusiness.this, "נכשלה הוספת העסק", Toast.LENGTH_SHORT).show();
                Log.e("AddBusiness", "Failed to add business", e);
            }
        });
    }

    private String encodeImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String encodeBitmapToBase64(Bitmap bitmap) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
