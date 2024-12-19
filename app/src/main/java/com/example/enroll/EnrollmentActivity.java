package com.example.enroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrollmentActivity extends AppCompatActivity {
    private LinearLayout checkboxContainer;
    private Button btnEnroll;
    private TextView tvTotalCredit; // Tambahkan ini
    private List<Subject> selectedSubjects;
    private FirebaseFirestore db;
    private int totalCredits = 0; // Tambahkan ini untuk menghitung total kredit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        checkboxContainer = findViewById(R.id.checkboxContainer);
        btnEnroll = findViewById(R.id.btnEnroll);
        tvTotalCredit = findViewById(R.id.tvTotalCredit); // Inisialisasi TextView
        db = FirebaseFirestore.getInstance();
        selectedSubjects = new ArrayList<>();

        // Set listener untuk setiap checkbox
        setCheckBoxListeners();

        btnEnroll.setOnClickListener(view -> {
            collectCheckedSubjects();

            if (isCreditValid()) {
                saveDataToFirestore();
                navigateToSummary();
            } else {
                Toast.makeText(this, "Total credits cannot be more than 24. Please select fewer subjects.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setCheckBoxListeners() {
        // Loop untuk setiap checkbox dalam LinearLayout
        for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
            View view = checkboxContainer.getChildAt(i);

            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;

                // Tambahkan listener ke checkbox
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    int credit = extractCreditFromText(checkBox.getText().toString());

                    if (isChecked) {
                        totalCredits += credit; // Tambah kredit
                    } else {
                        totalCredits -= credit; // Kurangi kredit
                    }

                    updateTotalCreditDisplay(); // Update tampilan total kredit
                });
            }
        }
    }

    private void updateTotalCreditDisplay() {
        tvTotalCredit.setText("Total credit: " + totalCredits); // Update teks TextView
    }

    private void collectCheckedSubjects() {
        selectedSubjects.clear();

        for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
            View view = checkboxContainer.getChildAt(i);

            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;

                if (checkBox.isChecked()) {
                    String name = checkBox.getText().toString();
                    int credit = extractCreditFromText(name);
                    selectedSubjects.add(new Subject(name, credit));
                }
            }
        }
    }

    private boolean isCreditValid() {
        return totalCredits <= 24; // Validasi jika kredit tidak lebih dari 24
    }

    private int extractCreditFromText(String text) {
        String creditString = text.replaceAll("[^0-9]", ""); // Ambil angka dari teks
        return Integer.parseInt(creditString.isEmpty() ? "0" : creditString);
    }

    private void saveDataToFirestore() {
        List<String> subjectNames = new ArrayList<>();

        for (Subject subject : selectedSubjects) {
            subjectNames.add(subject.getName());
        }

        Map<String, Object> enrollmentData = new HashMap<>();
        enrollmentData.put("subjects", subjectNames);
        enrollmentData.put("totalCredits", totalCredits);

        db.collection("enrollments").document("student1").set(enrollmentData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Enrollment successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToSummary() {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putParcelableArrayListExtra("selectedSubjects", new ArrayList<>(selectedSubjects));
        startActivity(intent);
    }
}
