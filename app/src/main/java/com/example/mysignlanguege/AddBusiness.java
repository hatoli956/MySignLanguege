package com.example.mysignlanguege;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mysignlanguege.model.Business;
import com.example.mysignlanguege.model.User;
import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.ImageUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBusiness extends AppCompatActivity implements View.OnClickListener {

    /// tag for logging
    private static final String TAG = "AddBusinessActivity";

    Spinner spBusinessType,spBusinessCity;
    String businessName,businessPhone,businessEmail,businessAddress,businessWebsite,businessDetails,businessType,businessCity;
    EditText etBusinessName,etBusinessPhone,etBusinessEmail,etBusinessAddress,etBusinessWebsite,etBusinessDetails;
    Button btnGallery,btnCamera, btnAddItem;

    ImageView imageBusiness;






    private DatabaseService databaseService;

    /// Activity result launcher for selecting image from gallery
    private ActivityResultLauncher<Intent> selectImageLauncher;
    /// Activity result launcher for capturing image from camera
    private ActivityResultLauncher<Intent> captureImageLauncher;
    private AuthenticationService authenticationService;
    private String uid;
    private User user=new User();



    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    // constant to compare
    // the activity result code

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

        initViews();
        /// request permission for the camera and storage
        ImageUtil.requestPermission(this);

        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();


        authenticationService = AuthenticationService.getInstance();
        uid=authenticationService.getCurrentUserId();



        /// register the activity result launcher for selecting image from gallery
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        imageBusiness.setImageURI(selectedImage);

                    }
                });

        /// register the activity result launcher for capturing image from camera
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        imageBusiness.setImageBitmap(bitmap);
                    }
                });



        databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User u) {
                user = u;




            }

            @Override
            public void onFailed(Exception e) {

            }
        });





    }

    private void initViews() {

        btnAddItem=findViewById(R.id.btnAddItem);
        btnGallery=findViewById(R.id.btnGallery);
        btnCamera=findViewById(R.id.btnCamera);

        spBusinessType=findViewById(R.id.spBusinessType);
        spBusinessCity=findViewById(R.id.spBusinessCity);

        etBusinessName=findViewById(R.id.etBusinessName);
        etBusinessPhone=findViewById(R.id.etBusinessPhone);
        etBusinessEmail=findViewById(R.id.etBusinessEmail);
        etBusinessAddress=findViewById(R.id.etBusinessAddress);
        etBusinessWebsite=findViewById(R.id.etBusinessWebsite);
        etBusinessDetails=findViewById(R.id.etBusinessDetails);
        imageBusiness=findViewById(R.id.imageRef);

        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnAddItem.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {


        if (v.getId() == btnAddItem.getId()) {
            Log.d(TAG, "Add business button clicked");
            addBusinessToDatabase();
            return;
        }
        if (v.getId() == btnGallery.getId()) {
            // select image from gallery
            Log.d(TAG, "Select image button clicked");
            selectImageFromGallery();
            return;
        }
        if (v.getId() == btnCamera.getId()) {
            // capture image from camera
            Log.d(TAG, "Capture image button clicked");
            captureImageFromCamera();
            return;
        }
    }

    /// add the business to the database
    /// @see Business
    private void addBusinessToDatabase() {
        /// get the values from the input fields
        businessType  = spBusinessType.getSelectedItem().toString();

        businessDetails = etBusinessDetails.getText().toString();
        businessCity = spBusinessCity.getSelectedItem().toString();


        businessName = etBusinessName.getText() + "";
        String imageBase64 = ImageUtil.convertTo64Base(imageBusiness);


        businessAddress=etBusinessAddress.getText() + "";
        businessEmail=etBusinessEmail.getText() +"";
        businessPhone=etBusinessPhone.getText() +"";
        businessWebsite=etBusinessWebsite.getText() +"";







        /// validate the input
        /// stop if the input is not valid
     //   if (!isValid(name, priceText, imageBase64)) return;

        /// convert the price to double


        /// generate a new id for the business
        String id = databaseService.generateBusinessId();









        /// create a new business object


            Business business = new Business(id, businessName,  businessType,  businessPhone,  businessEmail,  businessAddress,  businessWebsite,   businessCity,  businessDetails,  imageBase64,user);

        /// save the business to the database and get the result in the callback
        databaseService.createNewBusiness(business, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "Business added successfully");
                Toast.makeText(AddBusiness.this, "Business added successfully", Toast.LENGTH_SHORT).show();
                /// clear the input fields after adding the business for the next business

                startActivity(new Intent(AddBusiness.this, AfterLogin.class));
               
             
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to add business", e);
                Toast.makeText(AddBusiness.this, "Failed to add business", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /// select image from gallery
    private void selectImageFromGallery() {
        //   Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  selectImageLauncher.launch(intent);

        imageChooser();
    }





        /// capture image from camera
        private void captureImageFromCamera() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureImageLauncher.launch(takePictureIntent);
        }




        void imageChooser() {

            // create an instance of the
            // intent of the type image
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);

            // pass the constant to compare it
            // with the returned requestCode
            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
        }

        // this function is triggered when user
        // selects the image from the imageChooser
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {

                // compare the resultCode with the
                // SELECT_PICTURE constant
                if (requestCode == SELECT_PICTURE) {
                    // Get the url of the image from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        // update the preview image in the layout
                        imageBusiness.setImageURI(selectedImageUri);
                    }
                }
            }
        }
}
