package com.example.wilapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager

class ServicesFragment : Fragment() {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var btnRegisterPet: Button
    private lateinit var btnTracker: Button
    private lateinit var btnInsurance: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_services, container, false)

        btnRegisterPet = view.findViewById(R.id.btnRegisterPet)
        btnTracker = view.findViewById(R.id.btnTracker)
        btnInsurance = view.findViewById(R.id.btnInsurance)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegisterPet.setOnClickListener {
            goToFragment(PetRegisterFragment())
        }

        btnTracker.setOnClickListener {
            val intent = Intent(requireContext(), PetTrackerActivity::class.java)
            startActivity(intent)
        }

        btnInsurance.setOnClickListener {
            val intent = Intent(requireContext(), InsuranceActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToFragment(fragment: Fragment) {
        fragmentManager = requireActivity().supportFragmentManager // Use requireActivity() to get the Activity's FragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // Add this line if you want to enable back navigation
            .commit()
    }
}
