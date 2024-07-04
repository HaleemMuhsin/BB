package com.example.bb // Replace with your actual package name

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class FourthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth) // Replace with your layout file
        val saveButton: Button = findViewById(R.id.saveButton)
        val inputFirstName: EditText = findViewById(R.id.inputFirstName)
        val inputLastName: EditText = findViewById(R.id.inputLastName)

        saveButton.setOnClickListener {
            val firstName = inputFirstName.text.toString()
            val lastName = inputLastName.text.toString()
            startActivity(Intent(this, FourthActivity::class.java))
            true
            saveFireStore(firstName, lastName)
        }
        readFireStoreData()

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
    fun saveFireStore(firstname: String, lastname: String) {
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["name"] = firstname
        user["specializedIn"] = lastname

        db.collection("fields")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(this@FourthActivity, "record added successfully ", Toast.LENGTH_SHORT ).show()
            }
            .addOnFailureListener{
                Toast.makeText(this@FourthActivity, "record Failed to add ", Toast.LENGTH_SHORT ).show()
            }

    }
    fun readFireStoreData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("fields")
            .get()
            .addOnCompleteListener {

                val result: StringBuffer = StringBuffer()

                if(it.isSuccessful) {
                    for(document in it.result!!) {
                        result.append(document.data.getValue("name")).append("\n")
                            .append(document.data.getValue("specializedIn")).append("\n\n")
                    }
                    val textViewResult: EditText = findViewById(R.id.textViewResult)
                    textViewResult.setText(result)
                }
            }

    }
}