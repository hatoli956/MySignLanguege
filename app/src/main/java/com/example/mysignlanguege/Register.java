package com.example.mysignlanguege;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mysignlanguege.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    EditText etfName,etlName, etPhone, etEmail, etPassword;
    String fName,lName, phone, email, password,city;
    Button btnReg;
    Spinner SpCity;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        mAuth = FirebaseAuth.getInstance();



    }

    private void initViews() {
        etfName=findViewById(R.id.etfName);
        etlName=findViewById(R.id.etlName);
        etPhone=findViewById(R.id.etPhone);
        SpCity=findViewById(R.id.SpCity);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnReg=findViewById(R.id.btnReg);
        btnReg.setOnClickListener(this);
        SpCity.setOnItemSelectedListener(this);

    }


    @Override
    public void onClick(View v) {

        fName = etfName.getText().toString();
        lName = etlName.getText().toString();
        phone = etPhone.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

//check if registration is valid
        Boolean isValid = true;
        if (fName.length() < 2) {

            etfName.setError("שם פרטי קצר מדי");
            isValid = false;
        }
        if (lName.length() < 2) {
            Toast.makeText(Register.this, "שם משפחה קצר מדי", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if (phone.length() < 9 || phone.length() > 10) {
            Toast.makeText(Register.this, "מספר הטלפון לא תקין", Toast.LENGTH_LONG).show();
            isValid = false;
        }

        if (!email.contains("@")) {
            Toast.makeText(Register.this, "כתובת האימייל לא תקינה", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if (password.length() < 6) {
            Toast.makeText(Register.this, "הסיסמה קצרה מדי", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if (password.length() > 20) {
            Toast.makeText(Register.this, "הסיסמה ארוכה מדי", Toast.LENGTH_LONG).show();
            isValid = false;
        }

        if (isValid == true) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                FirebaseUser fireuser = mAuth.getCurrentUser();
                                User newUser = new User(fireuser.getUid(), fName, lName, phone, email, password);
                                myRef.child(fireuser.getUid()).setValue(newUser);
                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString("email", email);
                                editor.putString("password", password);

                                editor.commit();
                                Intent goLog = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(goLog);


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }
    }










    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        city= (String) adapterView.getItemAtPosition(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}