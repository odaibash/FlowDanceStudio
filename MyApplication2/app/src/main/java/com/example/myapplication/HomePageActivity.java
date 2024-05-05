package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private ImageView logoImageView;
    private Button profileButton;
    private Button logoutButton;
    private Button upcomingSessionsButton;
    private Button registeredSessionsButton;
    private Button attendedSessionsButton;

    private TextView upcomingSessionsTextView;
    private TextView registeredSessionsTextView;

    private RecyclerView upcomingSessionsRecyclerView;
    private RecyclerView registeredSessionsRecyclerView;

    private SessionAdapter upcomingSessionAdapter;
    private SessionAdapter registeredSessionAdapter;

    private List<Session> upcomingSessionsList;
    private List<Session> registeredSessionsList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        profileButton = findViewById(R.id.btn_profile);
        logoutButton = findViewById(R.id.btn_logout);
        upcomingSessionsButton = findViewById(R.id.btn_upcoming_sessions);
        registeredSessionsButton = findViewById(R.id.btn_registered_sessions);
        attendedSessionsButton = findViewById(R.id.btn_attended_sessions);

//        upcomingSessionsTextView = findViewById(R.id.text_upcoming_sessions);
//        registeredSessionsTextView = findViewById(R.id.text_registered_sessions);
//
//        upcomingSessionsRecyclerView = findViewById(R.id.recycler_upcoming_sessions);
//        registeredSessionsRecyclerView = findViewById(R.id.recycler_registered_sessions);

        // Initialize session lists
        upcomingSessionsList = new ArrayList<>();
        registeredSessionsList = new ArrayList<>();

        // Initialize session adapters
        upcomingSessionAdapter = new SessionAdapter(this, upcomingSessionsList);
        registeredSessionAdapter = new SessionAdapter(this, registeredSessionsList);

        // Set layout managers for RecyclerViews
        upcomingSessionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        registeredSessionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set adapters for RecyclerViews
        upcomingSessionsRecyclerView.setAdapter(upcomingSessionAdapter);
        registeredSessionsRecyclerView.setAdapter(registeredSessionAdapter);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open profile activity
                Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user
                mAuth.signOut();
                Intent intent = new Intent(HomePageActivity.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        upcomingSessionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show upcoming sessions
                upcomingSessionsRecyclerView.setVisibility(View.VISIBLE);
                registeredSessionsRecyclerView.setVisibility(View.GONE);
                upcomingSessionsTextView.setVisibility(View.VISIBLE);
                registeredSessionsTextView.setVisibility(View.GONE);
            }
        });

        registeredSessionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show registered sessions
                upcomingSessionsRecyclerView.setVisibility(View.GONE);
                registeredSessionsRecyclerView.setVisibility(View.VISIBLE);
                upcomingSessionsTextView.setVisibility(View.GONE);
                registeredSessionsTextView.setVisibility(View.VISIBLE);
            }
        });

        attendedSessionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show attended sessions
                // Replace the following line with your code to show attended sessions
                Toast.makeText(HomePageActivity.this, "Attended sessions button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Load sessions from Firestore
        loadSessionsFromFirestore();
    }

    private void loadSessionsFromFirestore() {
        // Load upcoming sessions
        CollectionReference upcomingSessionsRef = db.collection("sessions");
        upcomingSessionsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                upcomingSessionsList.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    Session session = document.toObject(Session.class);
                    upcomingSessionsList.add(session);
                }
                upcomingSessionAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(HomePageActivity.this, "Error fetching upcoming sessions", Toast.LENGTH_SHORT).show();
            }
        });

        // Load registered sessions (sample implementation, replace with actual logic)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            CollectionReference registeredSessionsRef = db.collection("users").document(currentUser.getUid()).collection("registeredSessions");
            registeredSessionsRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    registeredSessionsList.clear();
                    for (DocumentSnapshot document : task.getResult()) {
                        Session session = document.toObject(Session.class);
                        registeredSessionsList.add(session);
                    }
                    registeredSessionAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HomePageActivity.this, "Error fetching registered sessions", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

//
//    private void addSessionToFirestore() {
//        String taskName = editTextTaskName.getText().toString().trim();
//        String taskDescription = editTextTaskDescription.getText().toString().trim();
//
//        if (taskName.isEmpty() || taskDescription.isEmpty()) {
//            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        TaskModel task = new TaskModel(taskName, taskDescription, false);
//        CollectionReference tasksCollection = db.collection("tasks");
//
//        tasksCollection.add(task)
//                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(HomePageActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
//                            editTextTaskName.setText("");
//                            editTextTaskDescription.setText("");
//                        } else {
//                            Toast.makeText(HomePageActivity.this, "Failed to add task", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//}
