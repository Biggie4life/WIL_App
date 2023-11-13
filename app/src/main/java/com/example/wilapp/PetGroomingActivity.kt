package com.example.wilapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PetGroomingActivity : AppCompatActivity() {

    private lateinit var dropdownList: Spinner
    private lateinit var ddlLocation: Spinner
    private lateinit var addressEditText: EditText
    private lateinit var ddlsoweto: Spinner
    private lateinit var setAppointmentButton: Button

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_grooming)

        // Initialize UI elements
        dropdownList = findViewById(R.id.dropdownList)
        ddlLocation = findViewById(R.id.ddlLocation)
        addressEditText = findViewById(R.id.addressEditText)
        ddlsoweto = findViewById(R.id.ddlsoweto)
        setAppointmentButton = findViewById(R.id.setAppointmentButton)

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        // Set ArrayAdapter for Spinners
        val petGroomingAdapter = ArrayAdapter.createFromResource(
            this, R.array.Pet_grooming, android.R.layout.simple_spinner_item
        )
        petGroomingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropdownList.adapter = petGroomingAdapter

        val locationAdapter = ArrayAdapter.createFromResource(
            this, R.array.Groom_location, android.R.layout.simple_spinner_item
        )
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ddlLocation.adapter = locationAdapter

        val sowetoAdapter = ArrayAdapter.createFromResource(
            this, R.array.soweto, android.R.layout.simple_spinner_item
        )
        sowetoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ddlsoweto.adapter = sowetoAdapter

        // Set click listener for the "Set Appointment" button
        setAppointmentButton.setOnClickListener {
            saveAppointmentToDatabase()
        }
    }

    private fun saveAppointmentToDatabase() {
        // Retrieve selected values
        val selectedGroomingOption = dropdownList.selectedItem.toString()
        val selectedLocation = ddlLocation.selectedItem.toString()
        val enteredAddress = addressEditText.text.toString()
        val selectedSowetoOption = ddlsoweto.selectedItem.toString()

        // Create a data structure to store the appointment details
        val appointmentDetails = HashMap<String, Any>()
        appointmentDetails["groomingOption"] = selectedGroomingOption
        appointmentDetails["location"] = selectedLocation
        appointmentDetails["address"] = enteredAddress
        appointmentDetails["sowetoOption"] = selectedSowetoOption

        // Save the appointment details under the user's UID in the database
        val userAppointmentsRef = database.child("user-appointments").child(currentUser.uid)
        userAppointmentsRef.push().setValue(appointmentDetails)
            .addOnSuccessListener {
                Toast.makeText(
                    this@PetGroomingActivity,
                    "Appointment saved successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@PetGroomingActivity,
                    "Failed to save appointment.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
