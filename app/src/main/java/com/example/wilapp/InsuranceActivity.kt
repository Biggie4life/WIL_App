package com.example.wilapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.example.wilapp.databinding.ActivityDonateBinding
import com.example.wilapp.databinding.ActivityInsuranceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

        val submitButton: Button = findViewById(R.id.submitButton)
        val parentNameEditText: EditText = findViewById(R.id.parentNameEditText)
        val parentSurnameEditText: EditText = findViewById(R.id.parentSurnameEditText)
        val petNameEditText: EditText = findViewById(R.id.petNameEditText)
        val petAgeEditText: EditText = findViewById(R.id.petAgeEditText)
        val petTypeRadioGroup: RadioGroup = findViewById(R.id.petTypeRadioGroup)
        val otherPetTypeEditText: EditText = findViewById(R.id.otherPetTypeEditText)
        val cardDetailsEditText: EditText = findViewById(R.id.cardDetailsEditText)
        val auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

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

            if (currentUser != null) {
                val userId = currentUser.uid
                val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

                // Assuming you have a "users" node in your database
                val usersReference = databaseReference.child("users").child(userId)

                // Create a HashMap to store user details
                val userMap = HashMap<String, Any>()
                userMap["parentName"] = parentName
                userMap["parentSurname"] = parentSurname
                userMap["petName"] = petName
                userMap["petAge"] = petAge
                userMap["petType"] = when (petTypeRadioGroup.checkedRadioButtonId) {
                    R.id.dogRadioButton -> "Dog"
                    R.id.catRadioButton -> "Cat"
                    R.id.otherRadioButton -> otherPetTypeEditText.text.toString()
                    else -> ""
                }
                userMap["cardDetails"] = cardDetails

                // Push data to the database under the user's UID
                usersReference.setValue(userMap)
                    .addOnSuccessListener {
                        // Data successfully written to the database
                        showThankYouPage()
                    }
                    .addOnFailureListener { exception ->
                        // Handle failure
                        // You can add any error handling or logging here
                        Toast.makeText(this@InsuranceActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // The user is not logged in
            }
        }
    }

    private fun showThankYouPage() {
        val thankYouFragment = thank_youFragment()

        // Replace the current fragment with the thank you fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, thankYouFragment)
            .commit()

        Handler().postDelayed({
            // Replace the thank you fragment with the home fragment
            val homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit()
        }, 3000)
    }
}
