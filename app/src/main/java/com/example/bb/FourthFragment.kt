package com.example.bb

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class FourthFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fourth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()

        val saveButton: Button = view.findViewById(R.id.saveButton)
        val inputFirstName: EditText = view.findViewById(R.id.inputFirstName)
        val inputLastName: EditText = view.findViewById(R.id.inputLastName)

        saveButton.setOnClickListener {
            val firstName = inputFirstName.text.toString()
            val lastName = inputLastName.text.toString()
            saveFireStore(firstName, lastName)
            inputFirstName.text.clear()
            inputLastName.text.clear()
        }

        readFireStoreData()
    }

    private fun saveFireStore(name: String, specializedIn: String) {
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["name"] = name
        user["specializedIn"] = specializedIn

        db.collection("fields")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(context, "Record added successfully", Toast.LENGTH_SHORT).show()
                readFireStoreData()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Record failed to add", Toast.LENGTH_SHORT).show()
            }
    }

    private fun readFireStoreData() {
        val db = FirebaseFirestore.getInstance()
        val container: LinearLayout = view?.findViewById(R.id.container) ?: return
        container.removeAllViews() // Clear previous views
        db.collection("fields")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        addDocumentToLayout(document.id, document.getString("name") ?: "", document.getString("specializedIn") ?: "")
                    }
                }
            }
    }

    private fun addDocumentToLayout(docId: String, name: String, specializedIn: String) {
        val container: LinearLayout = view?.findViewById(R.id.container) ?: return
        val itemLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16) // Add bottom margin
            }
        }

        val textView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            text = "$name \n$specializedIn"
            setTextColor(Color.WHITE)
            textSize = 16f
        }

        val editButton = ImageButton(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 50, 0) // Add right margin to separate from delete button
            }
            setBackgroundResource(android.R.color.transparent)
            setImageResource(R.drawable.pencil)
            setColorFilter(Color.WHITE)
            setOnClickListener {
                editFirestoreDocument(docId, name, specializedIn)
            }
        }

        val deleteButton = ImageButton(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setBackgroundResource(android.R.color.transparent)
            setImageResource(R.drawable.trash)
            setColorFilter(Color.parseColor("#FF6347"))
            setOnClickListener {
                deleteFirestoreDocument(docId)
            }
        }

        itemLayout.addView(textView)
        itemLayout.addView(editButton)
        itemLayout.addView(deleteButton)

        container.addView(itemLayout)
    }

    private fun deleteFirestoreDocument(docId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("fields").document(docId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Document deleted successfully", Toast.LENGTH_SHORT).show()
                readFireStoreData()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error deleting document: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    //Editing Firebase documents
    private fun editFirestoreDocument(docId: String, currentName: String, currentSpecializedIn: String) {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 30)
        }

        val editName = EditText(context).apply {
            setText(currentName)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            hint = "Name"
            background = ContextCompat.getDrawable(context, R.drawable.gridbuttonstyle)
            setPadding(30, 30, 30, 30)
        }

        val editSpecializedIn = EditText(context).apply {
            setText(currentSpecializedIn)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            hint = "Specialized In"
            background = ContextCompat.getDrawable(context, R.drawable.gridbuttonstyle)
            setPadding(30, 30, 30, 30)
        }

        layout.addView(editName, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 0, 0, 20)
        })
        layout.addView(editSpecializedIn, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        builder.setView(layout)
            .setTitle("Edit Document")
            .setPositiveButton("Save") { _, _ ->
                val newName = editName.text.toString()
                val newSpecializedIn = editSpecializedIn.text.toString()
                updateFirestoreDocument(docId, newName, newSpecializedIn)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val dialog = builder.create()
        dialog.show()

        // Customize button colors
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.WHITE)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.WHITE)
    }

    private fun updateFirestoreDocument(docId: String, newName: String, newSpecializedIn: String) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("fields").document(docId)

        val updates = hashMapOf<String, Any>(
            "name" to newName,
            "specializedIn" to newSpecializedIn
        )

        docRef.update(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "Document updated successfully", Toast.LENGTH_SHORT).show()
                readFireStoreData()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error updating document: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}