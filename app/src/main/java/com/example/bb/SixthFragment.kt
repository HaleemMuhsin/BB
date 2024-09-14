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

        val editButton = ImageButton(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 0, 50, 0) // Add left margin to separate button from text
            }
            setBackgroundColor(Color.TRANSPARENT)
            setColorFilter(Color.WHITE)
            setImageResource(R.drawable.pencil) // Set your edit icon here
            setOnClickListener {
                editFirestoreDocument(docId, count, position)
            }
        }

        val deleteButton = ImageButton(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 0, 0, 0) // Add left margin to separate button from text
            }
            setBackgroundColor(Color.TRANSPARENT)
            setColorFilter(Color.parseColor("#FF6347"))
            setImageResource(R.drawable.trash) // Set your delete icon here
            setOnClickListener {
                deleteFirestoreDocument(docId)
            }
        }

        linearLayout.addView(textView)
        linearLayout.addView(editButton)
        linearLayout.addView(deleteButton)

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

    // Edit Firestore document
    private fun editFirestoreDocument(docId: String, currentCount: String, currentPosition: String) {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 30)
        }

        val editCount = EditText(context).apply {
            setText(currentCount)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            hint = "Count"
            background = ContextCompat.getDrawable(context, R.drawable.gridbuttonstyle)
            setPadding(30, 30, 30, 30)
        }

        val editPosition = EditText(context).apply {
            setText(currentPosition)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            hint = "Position"
            background = ContextCompat.getDrawable(context, R.drawable.gridbuttonstyle)
            setPadding(30, 30, 30, 30)
        }

        layout.addView(editCount, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 0, 0, 20)
        })
        layout.addView(editPosition, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        builder.setView(layout)
            .setTitle("Edit Document")
            .setPositiveButton("Save") { _, _ ->
                val newCount = editCount.text.toString()
                val newPosition = editPosition.text.toString()
                updateFirestoreDocument(docId, newCount, newPosition)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.WHITE)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.WHITE)
    }

    private fun updateFirestoreDocument(docId: String, newCount: String, newPosition: String) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("position").document(docId)

        val updates = hashMapOf<String, Any>(
            "count" to newCount,
            "position" to newPosition
        )

        docRef.update(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "Document updated successfully", Toast.LENGTH_SHORT).show()
                readFirestoreData() // Refresh the data
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error updating document: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
