package com.example.wilapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import com.example.wilapp.databinding.ActivityDonateBinding
import com.example.wilapp.databinding.ActivityInsuranceBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsuranceActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    lateinit var binding: ActivityInsuranceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance)
        binding = ActivityInsuranceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val submitButton: Button =findViewById(R.id.submitButton)
        val parentNameEditText : EditText = findViewById(R.id.parentNameEditText)
        val parentSurnameEditText : EditText = findViewById(R.id.parentSurnameEditText)
        val petNameEditText : EditText = findViewById(R.id.petNameEditText)
        val petAgeEditText : EditText = findViewById(R.id.petAgeEditText)
        val petTypeRadioGroup : RadioGroup = findViewById(R.id.petTypeRadioGroup)
        val otherPetTypeEditText : EditText = findViewById(R.id.otherPetTypeEditText)
        val cardDetailsEditText : EditText = findViewById(R.id.cardDetailsEditText)

        databaseReference = FirebaseDatabase.getInstance().reference

        submitButton.setOnClickListener {
            val parentName = parentNameEditText.text.toString()
            val parentSurname = parentSurnameEditText.text.toString()
            val petName = petNameEditText.text.toString()
            val petAge = petAgeEditText.text.toString()
            val petType = when (petTypeRadioGroup.checkedRadioButtonId) {
                R.id.dogRadioButton -> "Dog"
                R.id.catRadioButton -> "Cat"
                R.id.otherRadioButton -> otherPetTypeEditText.text.toString()
                else -> ""
            }
            val cardDetails = cardDetailsEditText.text.toString()

            val userEntry = userpet(
                parentName, parentSurname, petName, petAge, petType, cardDetails
            )

            // Save the user's entry to Firebase Realtime Database.
            databaseReference.child("entries").push().setValue(userEntry)

            // Clear the input fields after saving.
            parentNameEditText.text.clear()
            parentSurnameEditText.text.clear()
            petNameEditText.text.clear()
            petAgeEditText.text.clear()
            petTypeRadioGroup.clearCheck()
            otherPetTypeEditText.text.clear()
            cardDetailsEditText.text.clear()


        }
    }

}