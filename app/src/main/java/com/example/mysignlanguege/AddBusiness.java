package com.example.mysignlanguege;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBusiness extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database ;
    DatabaseReference myRef;
    private static final int GALLERY_INTENT = 2;
    Spinner spBusinessType;
    String BusinessName,BusinessPhone,BusinessEmail,BusinessAddress,BusinessWebsite,BusinessDetails,BusinessType;
    EditText etBusinessName,etBusinessPhone,etBusinessEmail,etBusinessAddress,etBusinessWebsite,etBusinessDetails;
    Button btnGallery,btnCamera, btnAddItem;
    Bitmap bitmap;
    String imageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_business);
        initViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {

        btnAddItem=findViewById(R.id.btnAddItem);
        btnGallery=findViewById(R.id.btnGallery);
        btnCamera=findViewById(R.id.btnCamera);

        spBusinessType=findViewById(R.id.spBusinessType);

        etBusinessName=findViewById(R.id.etBusinessName);
        etBusinessPhone=findViewById(R.id.etBusinessPhone);
        etBusinessEmail=findViewById(R.id.etBusinessEmail);
        etBusinessAddress=findViewById(R.id.etBusinessAddress);
        etBusinessWebsite=findViewById(R.id.etBusinessWebsite);
        etBusinessDetails=findViewById(R.id.etBusinessDetails);

        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnAddItem.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnCamera) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);

        }
        if (v == btnGallery) {

            Intent intent2 = new Intent(Intent.ACTION_PICK);
            intent2.setType("image/*");
            startActivityForResult(intent2, GALLERY_INTENT);

        }
        if (v == btnAddItem) {
            BusinessType  = spBusinessType.getSelectedItem().toString();

            BusinessDetails = etBusinessDetails.getText().toString();


            BusinessName = etBusinessName.getText() + "";




            if (bitmap != null) {

                //  uid ="thisisUid"; //FirebaseAuth.getInstance().getCurrentUser().getUid();

                String itemid = myRef.getKey().toString();
                imageRef = "gs://macroorder-508b4.appspot.com/" + itemid;

                Business newItem = new Business(BusinessName,BusinessPhone,BusinessType,BusinessEmail,BusinessAddress,BusinessWebsite,BusinessDetails, imageRef);


                //  item1.setImageRef("gs://who-needed.appspot.com\n"+item1.getItemKey()+"");
                myRef.setValue(newItem);

                //startActivity(intent2);

                Intent go = new Intent(this, AdminPage.class);
                startActivity(go);

            } else {
                Toast.makeText(AddBusiness.this, "Please take pic!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}