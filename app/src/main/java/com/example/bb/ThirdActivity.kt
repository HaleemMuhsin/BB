// ThirdActivity.kt
package com.example.bb

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ThirdActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        // Initialize WebView
        webView = findViewById(R.id.webview)
        webView.webViewClient = WebViewClient() // Ensure links open within the WebView

        // Enable JavaScript if required
        webView.settings.javaScriptEnabled = true

        // Load a URL into the WebView
        val url = "https://billboardsjcetapp.netlify.app" // Replace with your desired URL
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
                    startActivity(Intent(this, SecondActivity::class.java))
                    true
                }
                else -> false
            }


        }


    }
}
