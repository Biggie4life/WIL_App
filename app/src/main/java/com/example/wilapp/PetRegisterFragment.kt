package com.example.wilapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PetRegisterFragment : Fragment() {

    private lateinit var petTypeEditText: EditText
    private lateinit var petNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var vaccinationRadioGroup: RadioGroup
    private lateinit var vaccinationDatePicker: DatePicker
    private lateinit var registerButton: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var petsRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pet_register, container, false)

        database = FirebaseDatabase.getInstance()
        petsRef = database.getReference("pets")

        petTypeEditText = view.findViewById(R.id.petTypeEditText)
        petNameEditText = view.findViewById(R.id.petNameEditText)
        ageEditText = view.findViewById(R.id.ageEditText)
        vaccinationRadioGroup = view.findViewById(R.id.vaccinationRadioGroup)
        vaccinationDatePicker = view.findViewById(R.id.vaccinationDatePicker)
        registerButton = view.findViewById(R.id.registerButton)

        vaccinationRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.yesRadioButton) {
                vaccinationDatePicker.visibility = View.VISIBLE
            } else {
                vaccinationDatePicker.visibility = View.GONE
            }
        }

        registerButton.setOnClickListener {
            // Retrieve user input
            val petType = petTypeEditText.text.toString()
            val petName = petNameEditText.text.toString()
            val age = ageEditText.text.toString()
            val isVaccinated = vaccinationRadioGroup.checkedRadioButtonId == R.id.yesRadioButton
            val vaccinationDate = if (isVaccinated) {
                "${vaccinationDatePicker.year}-${vaccinationDatePicker.month + 1}-${vaccinationDatePicker.dayOfMonth}"
            } else {
                ""
            }
            
            val pet = Pet(petType, petName, age, isVaccinated, vaccinationDate)

            // Push the pet data to the database
            val petId = petsRef.push().key
            if (petId != null) {
                petsRef.child(petId).setValue(pet)
            }
        }

        return view
    }
}
