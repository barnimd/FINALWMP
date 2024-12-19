package com.example.enroll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnEnrollment = findViewById(R.id.btnEnrollment);
        Button btnEnrollmentSummary = findViewById(R.id.btnEnrollmentSummary);

        btnEnrollment.setOnClickListener(v -> startActivity(new Intent(this, EnrollmentActivity.class)));
        btnEnrollmentSummary.setOnClickListener(v -> startActivity(new Intent(this, UserActivity.class)));
    }
}
