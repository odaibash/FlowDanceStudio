package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    EditText editName, editTextEmail, editTextPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        // Edit Texts
        editName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        findViewById(R.id.btn_register).setOnClickListener(v -> createNewUser());
        findViewById(R.id.btn_login).setOnClickListener(v -> moveToLoginScreen());
    }

    public void moveToLoginScreen() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void createNewUser() {
        String name, email, password;
        name = editName.getText().toString();
        email = editTextEmail.getText().toString().toLowerCase();
        password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(Register.this, "Enter your name", Toast.LENGTH_SHORT).show();
            editName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
            editTextPassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Account created.",
                                    Toast.LENGTH_SHORT).show();
                            // Call createUser in Database to add user data to Firestore
                            String userID = user.getUid();
                            Database.connection().createUser(userID, name, email);
                            moveToHomePageActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void moveToHomePageActivity() {
        Intent intent = new Intent(Register.this, HomePageActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}