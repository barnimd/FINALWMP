package com.example.enroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    // Deklarasi UI Components
    private EditText nameField, studentIdField, emailField, passwordField;
    private Button signupButton;
    private TextView loginText;

    // Firebase Instances
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Menggunakan XML yang kamu berikan

        // Inisialisasi UI Components
        nameField = findViewById(R.id.name);
        studentIdField = findViewById(R.id.username); // Student ID field
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        signupButton = findViewById(R.id.signup_button);
        loginText = findViewById(R.id.login_text);

        // Inisialisasi Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Event saat tombol Sign Up ditekan
        signupButton.setOnClickListener(v -> registerUser());

        // Navigasi ke halaman Login jika link ditekan
        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        // Ambil input dari pengguna
        String name = nameField.getText().toString().trim();
        String studentId = studentIdField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Validasi input kosong
        if (name.isEmpty() || studentId.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat akun dengan Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Simpan data user ke Firebase Realtime Database
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        User user = new User(name, studentId, email);
                        databaseReference.child(userId).setValue(user);

                        // Simpan data di SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", name);
                        editor.putString("studentId", studentId);
                        editor.apply(); // Jangan lupa apply untuk menyimpan perubahan

                        // Notifikasi berhasil dan pindah ke halaman Login
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Class User untuk model data
    static class User {
        public String name, studentId, email;

        public User() {
            // Default constructor untuk Firebase
        }

        public User(String name, String studentId, String email) {
            this.name = name;
            this.studentId = studentId;
            this.email = email;
        }
    }
}
