// ThirdActivity.kt
package com.example.bb

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

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
    }
}
