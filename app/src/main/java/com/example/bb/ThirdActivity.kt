package com.example.bb

import FifthFragment
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ThirdActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var floatingImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        loadFragment(ThirdFragment()) // Load the initial fragment

        setupBottomNavigationBar()

        floatingImageButton = findViewById(R.id.floatingImageButton)

        floatingImageButton.setOnClickListener {
            // Handle button click here
            Log.d("Debug", "Button clicked")
            updateThemeField()
        }
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.navigation_home // Set initial selected item
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(ThirdFragment())
                    true
                }
                R.id.navigation_dashboard -> {
                    loadFragment(FifthFragment())
                    true
                }
                R.id.navigation_logout -> {
                    showLogoutConfirmationDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                auth.signOut() // Sign out the user from Firebase if necessary
                startActivity(Intent(this, SecondActivity::class.java))
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog
                // Ensure the navigation selection stays on the current fragment
                val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView.selectedItemId = R.id.navigation_home // Set this to the ID of your current fragment
            }
            .show()
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

        // Example: Change root layout background color based on fragment type
        when (fragment) {
            is FifthFragment -> {
                val rootLayout = findViewById<ConstraintLayout>(R.id.third_activity) // Replace with your root layout type
                rootLayout.setBackgroundColor(Color.parseColor("#121316"))
            }
            is FourthFragment -> {
                val rootLayout = findViewById<ConstraintLayout>(R.id.third_activity) // Replace with your root layout type
                rootLayout.setBackgroundColor(Color.parseColor("#121316"))
            }
            // Add more cases for other fragments as needed
            else -> {
                val rootLayout = findViewById<ConstraintLayout>(R.id.third_activity) // Replace with your root layout type
                rootLayout.setBackgroundColor(Color.parseColor("#B8B8B8")) // Default color
            }
        }
    }

    private fun updateThemeField() {
        firestore.collection("theme")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val themeValue = document.getLong("theme") // Assuming "theme" is a field in your document
                    // Process the document as needed
                    // For example, you can change the value of the theme field and update the document
                    if (themeValue != null) {
                        val newThemeValue = if (themeValue == 1L) 2 else 1
                        document.reference.update("theme", newThemeValue)
                            .addOnSuccessListener {
                                // Successfully updated the document
                                Log.d("Debug", "Theme updated successfully")
                            }
                            .addOnFailureListener { e ->
                                // Handle the error
                                Log.e("Error", "Error updating theme", e)
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle the error
                Log.e("Error", "Error getting documents", e)
            }
    }
}
