package com.example.wilapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginFragment : Fragment() {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var btnregisteractivity: Button
    private lateinit var btnforgot:Button

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginButton = view.findViewById(R.id.loginButton)
        btnregisteractivity = view.findViewById(R.id.btnregisteractiviy)
        btnforgot = view.findViewById(R.id.btnforgot)

        auth = FirebaseAuth.getInstance()
        // Initialize the Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        btnregisteractivity.setOnClickListener {
            goToFragment(RegisterFragment())
        }

        btnforgot.setOnClickListener {
            val  intent = Intent(requireContext(), ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Login successful
                            goToFragment(HomeFragment())
                        } else {
                            // Handle login failures
                            Toast.makeText(
                                requireContext(),
                                "Failed to login. Check your email and password.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                // Handle empty fields or other login errors
                Toast.makeText(requireContext(), "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun goToFragment(fragment: Fragment) {
        fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}