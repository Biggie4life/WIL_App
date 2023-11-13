package com.example.wilapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pet_register, container, false)

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance()
        petsRef = database.getReference("pets")
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        // Initialize UI elements
        petTypeEditText = view.findViewById(R.id.petTypeEditText)
        petNameEditText = view.findViewById(R.id.petNameEditText)
        ageEditText = view.findViewById(R.id.ageEditText)
        vaccinationRadioGroup = view.findViewById(R.id.vaccinationRadioGroup)
        vaccinationDatePicker = view.findViewById(R.id.vaccinationDatePicker)
        registerButton = view.findViewById(R.id.registerButton)

        // Set up listener for the RadioGroup
        vaccinationRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.yesRadioButton) {
                vaccinationDatePicker.visibility = View.VISIBLE
            } else {
                vaccinationDatePicker.visibility = View.GONE
            }
        }

        // Set up click listener for the registration button
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

            // Ensure the currentUser is not null before proceeding
            currentUser.let { user ->
                val pet = Pet(petType, petName, age, isVaccinated, vaccinationDate)

                // Push the pet data to the database
                val petId = petsRef.child(user.uid).push().key
                if (petId != null) {
                    savePetData(user.uid, petId, pet)
                }
            }
        }

        return view
    }

    private fun savePetData(userId: String, petId: String, pet: Pet) {
        // Save pet data to the database under the user's UID
        petsRef.child(userId).child(petId).setValue(pet)
            .addOnSuccessListener {
                showToast("Pet data saved successfully!")
                goToFragment(HomeFragment())
            }
            .addOnFailureListener { exception ->
                showToast("Error saving pet data: ${exception.message}")
                Log.e("FirebaseError", exception.toString())
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun validateInput(): Boolean {
        // Perform input validation and return true if input is valid, false otherwise
        return true
    }

    private fun goToFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
