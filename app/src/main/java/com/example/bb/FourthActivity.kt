package com.example.bb

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class FourthActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)

        firestore = FirebaseFirestore.getInstance()
        container = findViewById(R.id. container)

        val saveButton: Button = findViewById(R.id.saveButton)
        val inputFirstName: EditText = findViewById(R.id.inputFirstName)
        val inputLastName: EditText = findViewById(R.id.inputLastName)

        saveButton.setOnClickListener {
            val firstName = inputFirstName.text.toString()
            val lastName = inputLastName.text.toString()
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

    private fun saveFireStore(firstname: String, lastname: String) {
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["name"] = firstname
        user["specializedIn"] = lastname

        db.collection("fields")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(this@FourthActivity, "Record added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@FourthActivity, "Record failed to add", Toast.LENGTH_SHORT).show()
            }
    }

    private fun readFireStoreData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("fields")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        addDocumentToLayout(document.id, document.getString("name") ?: "", document.getString("specializedIn") ?: "")
                    }
                } else {
                    Log.w("FourthActivity", "Error getting documents.", task.exception)
                }
            }
    }

    private fun addDocumentToLayout(docId: String, name: String, specializedIn: String) {
        val linearLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                if (this is ViewGroup.MarginLayoutParams) {
                    setMargins(0, 0, 0,
                        0) // Add top and bottom margins
                }
            }
        }

        val textView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            text = "$name \n$specializedIn"
            setTextColor(resources.getColor(R.color.white))
            textSize = 16f
        }

        val imageButton = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 0, 0, 0)// Add left margin to separate button from text
            setBackgroundColor(Color.TRANSPARENT)
            setColorFilter(Color.parseColor("#f44343"))
            }
            setImageResource(R.drawable.trash) // Set your desired image resource here
            id = docId.hashCode() // Convert docId to a unique int
            setOnClickListener {
                deleteFirestoreDocument(docId)
            }
        }

        linearLayout.addView(textView)
        linearLayout.addView(imageButton)

        container.addView(linearLayout)
    }

    private fun deleteFirestoreDocument(docId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("fields").document(docId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this@FourthActivity, "Document deleted successfully", Toast.LENGTH_SHORT).show()
                readFireStoreData()
                startActivity(Intent(this, FourthActivity::class.java))// Refresh the data
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@FourthActivity, "Error deleting document: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
