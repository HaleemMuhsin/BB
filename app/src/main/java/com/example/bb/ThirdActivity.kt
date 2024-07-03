package com.example.bb

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ThirdActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        auth = Firebase.auth

        webView = findViewById(R.id.webview)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        val url = "https://billboardsjcetapp.netlify.app"
        webView.loadUrl(url)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_button2
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_button2 -> {
                    startActivity(Intent(this, ThirdActivity::class.java))
                    true
                }
                R.id.nav_button1 -> {
                    startActivity(Intent(this, FourthActivity::class.java))
                    true
                }
                R.id.nav_button3 -> {
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
            .setPositiveButton("Yes") { _, _ ->// User confirmed logout, sign out and go to SecondActivity
                auth.signOut()
                startActivity(Intent(this, SecondActivity::class.java))
                finish() // Finish ThirdActivity
            }
            .setNegativeButton("No", null) // Do nothing if user cancels
            .show()
    }
}