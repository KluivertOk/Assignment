package com.example.assignment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    // Declare FirebaseAuth
    private FirebaseAuth mAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Set a click listener for the login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get email and password from EditText fields
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                // Call the signIn method with the provided email and password
                signIn(email, password);
            }
        });

    }
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Navigate to the main activity
                        navigateToMainActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(login.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void navigateToMainActivity() {
        // Create an Intent to start the main activity
        Intent intent = new Intent(login.this, MainActivity.class);
        startActivity(intent);

        // Finish the current activity (login activity) to prevent the user from coming back
        finish();
    }

    // Add the logout logic
    private void signOut() {
        mAuth.signOut();
        // Redirect to the login activity
        Intent intent = new Intent(login.this, login.class);
        startActivity(intent);

        // Finish the current activity to prevent the user from coming back
        finish();
    }
}
