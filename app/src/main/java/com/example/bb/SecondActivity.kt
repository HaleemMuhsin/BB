package com.example.bb

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SecondActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        auth = Firebase.auth

        val signInButton = findViewById<Button>(R.id.button2) // Assuming button2 is your Sign In button
        val emailEditText = findViewById<EditText>(R.id.editTextText) // Assuming editTextText is for email
        val passwordEditText = findViewById<EditText>(R.id.editTextText2) // Assuming editTextText2 is for password

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            signInWithEmailAndPassword(email, password)
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, open ThirdActivity
                    val intent = Intent(this, ThirdActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, show a dialog
                    AlertDialog.Builder(this)
                        .setTitle("Authentication Failed")
                        .setMessage("Incorrect email or password.")
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
    }
}