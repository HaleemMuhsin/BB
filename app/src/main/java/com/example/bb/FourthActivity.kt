package com.example.bb // Replace with your actual package name

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class FourthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth) // Replace with your layout file

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_button1
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