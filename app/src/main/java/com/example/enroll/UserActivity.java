package com.example.enroll;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserActivity extends AppCompatActivity {
    private TextView tvStudentInfo, tvSelectedSubjects, tvTotalCredits, tvStudentId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        tvStudentInfo = findViewById(R.id.userName);
        tvStudentId = findViewById(R.id.userId);
        tvSelectedSubjects = findViewById(R.id.tvSelectedSubjects);
        tvTotalCredits = findViewById(R.id.credits_summary);

        db = FirebaseFirestore.getInstance();

        //sharedpreferense dari register
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "No Name");
        String studentId = sharedPreferences.getString("studentId", "No ID");
        tvStudentInfo.setText(name);
        tvStudentId.setText(studentId);

        //kirim enroll data ke firebase
        db.collection("enrollments").document("student1")
                .get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> subjects = (List<String>) documentSnapshot.get("subjects");
                        Long totalCredits = documentSnapshot.getLong("totalCredits");

                        tvSelectedSubjects.setText(subjects != null ? String.join("\n", subjects) : "No subjects selected");
                        tvTotalCredits.setText("Total Credits: " + (totalCredits != null ? totalCredits : 0));
                    }
                });
    }
}
