package com.example.wilapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class VetAppointmentsActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var datePicker: DatePicker
    private lateinit var setAppointmentButton: Button

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vet_appointments)

        usernameEditText = findViewById(R.id.editTextText)
        phoneNumberEditText = findViewById(R.id.editTextPhone)
        datePicker = findViewById(R.id.appointdate)
        setAppointmentButton = findViewById(R.id.setAppointmentButton)

        // Initialize Firebase components
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        setAppointmentButton.setOnClickListener {
            saveAppointmentToDatabase()
        }
    }

    private fun saveAppointmentToDatabase() {
        // Retrieve selected values
        val username = usernameEditText.text.toString()
        val phoneNumber = phoneNumberEditText.text.toString()
        val appointmentDate =
            "${datePicker.year}-${datePicker.month + 1}-${datePicker.dayOfMonth}"

        // Create a data structure to store the appointment details
        val appointmentDetails = HashMap<String, Any>()
        appointmentDetails["username"] = username
        appointmentDetails["phoneNumber"] = phoneNumber
        appointmentDetails["appointmentDate"] = appointmentDate

        // Save the appointment details under the user's UID in the database
        val userAppointmentsRef =
            databaseReference.child("user-appointments").child(currentUser.uid)
        userAppointmentsRef.push().setValue(appointmentDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "save", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }

    }

    }

