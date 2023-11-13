package com.example.wilapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var btnloginactivity: Button

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        firstNameEditText = view.findViewById(R.id.firstNameEditText)
        lastNameEditText = view.findViewById(R.id.lastNameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText)
        addressEditText = view.findViewById(R.id.addressEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        registerButton = view.findViewById(R.id.registerButton)
        btnloginactivity = view.findViewById(R.id.btnloginactivity)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        btnloginactivity.setOnClickListener {
            goToFragment(LoginFragment())
        }
        registerButton.setOnClickListener {
            val FirstName = firstNameEditText.text.toString()
            val LastName = lastNameEditText.text.toString()
            val Email = emailEditText.text.toString()
            val PhoneNum = phoneNumberEditText.text.toString()
            val Address = addressEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check for empty fields
            if (FirstName.isNotEmpty() && LastName.isNotEmpty() && Email.isNotEmpty() &&
                PhoneNum.isNotEmpty() && Address.isNotEmpty() && password.isNotEmpty()) {

                auth.createUserWithEmailAndPassword(Email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Registration successful
                            val user = auth.currentUser

                            // Create a new user entry in the database
                            val userData = User(FirstName, LastName, Email, PhoneNum, Address, password)

                            user?.let {
                                val userId = it.uid  // Use the actual user ID from Firebase Authentication
                                databaseReference.child(userId).setValue(userData)
                                goToFragment(HomeFragment())
                            }
                        } else {
                            // Handle registration failures
                            val errorMessage = task.exception?.localizedMessage ?: "Registration failed."
                            Toast.makeText(
                                requireContext(),
                                "Error: $errorMessage",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }else {
                // Handle empty fields or other registration errors
                Toast.makeText(requireContext(), "Failed to register.", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun goToFragment(fragment: Fragment) {
        fragmentManager = requireActivity().supportFragmentManager // Use requireActivity() to get the Activity's FragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // Add this line if you want to enable back navigation
            .commit()
    }
}
