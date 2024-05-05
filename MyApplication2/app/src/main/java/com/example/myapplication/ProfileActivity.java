package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText profileName;
    private EditText profileGender;
    private EditText profileAge;
    private EditText profileEmail;
    private Button btnEditProfile;
    private FirebaseFirestore db;
    private DocumentReference userRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(currentUser.getUid());

        profileName = findViewById(R.id.profile_name);
        profileGender = findViewById(R.id.profile_gender);
        profileAge = findViewById(R.id.profile_age);
        profileEmail = findViewById(R.id.profile_email);
        btnEditProfile = findViewById(R.id.btn_edit_profile);

        // Set initial visibility of edit fields
        setEditingEnabled(false);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnEditProfile.getText().toString().equals("Edit Profile")) {
                    setEditingEnabled(true);
                    btnEditProfile.setText("Save Changes");
                } else {
                    saveProfileChanges();
                    setEditingEnabled(false);
                    btnEditProfile.setText("Edit Profile");
                }
            }
        });

        loadUserProfile();
    }

    private void setEditingEnabled(boolean enabled) {
        profileName.setEnabled(enabled);
        profileGender.setEnabled(enabled);
        profileAge.setEnabled(enabled);
        profileEmail.setEnabled(enabled);
    }

    private void saveProfileChanges() {
        String name = profileName.getText().toString();
        String gender = profileGender.getText().toString();
        String age = profileAge.getText().toString();
        String email = profileEmail.getText().toString();

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("gender", gender);
        data.put("age", age);
        data.put("email", email);

        userRef.update(data);
    }

    private void loadUserProfile() {
        // Load user's profile data from Firestore and populate the EditText fields
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String gender = documentSnapshot.getString("gender");
                String age = documentSnapshot.getString("age");
                String email = documentSnapshot.getString("email");

                profileName.setText(name);
                profileGender.setText(gender);
                profileAge.setText(age);
                profileEmail.setText(email);
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to load user profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

