package com.example.bb

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

        // Check if user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is alreadysigned in, proceed to ThirdActivity
            startActivity(Intent(this, ThirdActivity::class.java))
            finish() // Finish SecondActivity to prevent going back to login
            return
        }

        val signInButton = findViewById<Button>(R.id.button2)
        val emailEditText = findViewById<EditText>(R.id.editTextText)
        val passwordEditText = findViewById<EditText>(R.id.editTextText2)

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
                    // Sign in success, proceed to ThirdActivity
                    startActivity(Intent(this, ThirdActivity::class.java))
                    finish() // Finish SecondActivity
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