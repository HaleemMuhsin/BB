package com.example.bb

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class SixthFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sixth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()

        val saveButton: Button = view.findViewById(R.id.saveButton)
        val inputFirstName: EditText = view.findViewById(R.id.inputFirstName)
        val inputName1: EditText = view.findViewById(R.id.inputName1)

        saveButton.setOnClickListener {
            val firstName = inputFirstName.text.toString()
            val name1 = inputName1.text.toString()
            saveFirestoreData(firstName, name1)
            inputFirstName.text.clear()
            inputName1.text.clear()
        }

        readFirestoreData()
    }

    private fun saveFirestoreData(firstName: String, name1: String) {
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["count"] = firstName
        user["position"] = name1

        db.collection("position")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(context, "Record added successfully", Toast.LENGTH_SHORT).show()
                readFirestoreData()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Record failed to add", Toast.LENGTH_SHORT).show()
            }
    }

    private fun readFirestoreData() {
        val db = FirebaseFirestore.getInstance()
        val container2: ViewGroup = view?.findViewById(R.id.container2) ?: return
        container2.removeAllViews() // Clear previous views
        db.collection("position")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        addDocumentToLayout(
                            document.id,
                            document.getString("count") ?: "",
                            document.getString("position") ?: ""
                        )
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch documents", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addDocumentToLayout(
        docId: String,
        count: String,
        position: String
    ) {
        val container: ViewGroup = view?.findViewById(R.id.container2) ?: return
        val linearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                if (this is ViewGroup.MarginLayoutParams) {
                    setMargins(0, 8, 0, 8) // Add top and bottom margins
                }
            }
        }

        val textView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            text = "Count: $count\nPosition: $position"
            setTextColor(resources.getColor(R.color.white))
            textSize = 16f
        }

        val imageButton = ImageButton(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 0, 0, 0) // Add left margin to separate button from text
            }
            setBackgroundColor(Color.TRANSPARENT)
            setColorFilter(Color.parseColor("#FF6347")) // Example color
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
        db.collection("position").document(docId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Document deleted successfully", Toast.LENGTH_SHORT).show()
                readFirestoreData() // Refresh the data
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error deleting document: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
