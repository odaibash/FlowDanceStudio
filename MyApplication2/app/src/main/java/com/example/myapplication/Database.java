package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private static Database instance = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Database() {
        // Private constructor to prevent instantiation
    }

    public static Database connection() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void createUser(String userID, String name, String email) {
        DocumentReference userRef = db.collection("users").document(userID);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDoc = task.getResult();
                    assert userDoc != null;
                    if (!userDoc.exists()) {
                        // User doesn't exist, create a new document with default values
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("name", name);
                        userData.put("email", email);
                        userData.put("isCoach", false); // Set isCoach to false by default
                        userData.put("coachedDances", new ArrayList<String>());
                        userData.put("upcomingSessions", new ArrayList<String>());
                        userRef.set(userData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("createUser", "User " + userID + " created successfully.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("createUser", "Failed to create user " + userID);
                                    }
                                });
                    } else {
                        // User already exists, handle existing user scenario
                        Log.d("createUser", "User " + userID + " already exists.");
                        // You might want to handle the case where the user already exists
                    }
                } else {
                    Log.e("createUser", "Failed to check user existence for " + userID);
                }
            }
        });
    }



    // Utility functions
    private Map<String, Object> DocToSessionMap(DocumentSnapshot sessionDoc) {
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put("participants", sessionDoc.get("participants"));
        sessionMap.put("site", sessionDoc.get("site"));
        sessionMap.put("start_date", sessionDoc.get("start_date"));
        sessionMap.put("end_date", sessionDoc.get("end_date"));
        sessionMap.put("title", sessionDoc.get("title"));
        return sessionMap;
    }

    private Map<String, Object> DocToUserMap(DocumentSnapshot userDoc) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", userDoc.get("name"));
        userMap.put("email", userDoc.get("email"));
        userMap.put("isCoach", userDoc.get("isCoach"));
        userMap.put("coachedDances", userDoc.get("coachedDances"));
        userMap.put("upcomingSessions", userDoc.get("upcomingSessions"));
        return userMap;
    }

    // Create methods
    public void CreateSession(Session newSession, String userID, DatabaseCallback callback) {
        db.collection("sessions")
                .document(newSession.getName())
                .set(newSession.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        addSessionToUser(newSession, userID, new DatabaseCallback() {
                            @Override
                            public void then(Object obj) {
                                Log.d("CreateSession", "Session " + newSession.getName() + " added successfully.");
                                callback.then(obj);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CreateSession", "Failed to add session " + newSession.getName());
                    }
                });
    }

    public void addSessionToUser(Session session, String userID, DatabaseCallback callback) {
        DocumentReference userRef = db.collection("users").document(userID);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDoc = task.getResult();
                    assert userDoc != null;
                    if (userDoc.exists()) {
                        Map<String, Object> userData = userDoc.getData();
                        ArrayList<String> userSessions = (ArrayList<String>) userData.get("upcomingSessions");
                        if (!userSessions.contains(session.getName())) {
                            userSessions.add(session.getName());
                            userData.put("upcomingSessions", userSessions);
                            db.collection("users")
                                    .document(userID)
                                    .set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("addSessionToUser", "Session " +
                                                    session.getName() +
                                                    " added to " + userID + " successfully.");
                                            callback.then(null);
                                        }
                                    });
                        }
                    } else {
                        Log.e("addSessionToUser", "Failed adding " + session.getName() + " to user " + userID);
                    }
                } else {
                    Log.e("addSessionToUser", "Failed adding " + session.getName() + " to user " + userID);
                }
            }
        });
    }

    // Read methods
    public void getSession(String sessionTitle, DatabaseCallback callback) {
        DocumentReference docRef = db.collection("sessions").document(sessionTitle);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot sessionDoc = task.getResult();
                    assert sessionDoc != null;
                    if (sessionDoc.exists()) {
                        Log.d("getSession", "Session fetched: " + sessionDoc.getData());
                        callback.then(Session.fromMap(DocToSessionMap(sessionDoc)));
                    } else {
                        Log.d("getSession", "No such document as " + sessionTitle);
                    }
                } else {
                    Log.e("getSession", "Failed to retrieve document " + sessionTitle + " " + task.getException());
                }
            }
        });
    }

    public void getAllSessions(DatabaseCallback callback) {
        db.collection("sessions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Session> sessions = new ArrayList<>();
                            for (QueryDocumentSnapshot session : task.getResult()) {
                                sessions.add(Session.fromMap(session.getData()));
                                Log.d("Collecting Sessions", session.getId() + ": " +
                                        session.getData());
                            }
                            callback.then(sessions);
                        } else {
                            Log.e("getAllSessions", "Error getting sessions: ", task.getException());
                        }
                    }
                });
    }

    public void getUser(String userID, DatabaseCallback callback) {
        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDoc = task.getResult();
                    assert userDoc != null;
                    if (userDoc.exists()) {
                        Log.d("getUser", "User fetched: " + userDoc.getData());
                        callback.then(userDoc.getData());
                    } else {
                        Log.d("getUser", "No such document as " + userID);
                    }
                } else {
                    Log.e("getUser", "Failed to retrieve document " + userID + " " + task.getException());
                }
            }
        });
    }


    public void getAllUsers(DatabaseCallback callback) {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> users = new HashMap<>();
                            for (QueryDocumentSnapshot user : task.getResult()) {
                                users.put(user.getId(), user.getData());
                                Log.d("Collecting Users", user.getId() + ": " +
                                        user.getData());
                            }
                            callback.then(users);
                        } else {
                            Log.e("getAllUsers", "Error getting users: ", task.getException());
                        }
                    }
                });
    }
}
