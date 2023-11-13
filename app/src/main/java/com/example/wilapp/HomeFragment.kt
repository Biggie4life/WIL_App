package com.example.wilapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.wilapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var Btndonte: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val loadWebPageButton = view.findViewById<Button>(R.id.heroButton)
        Btndonte = view.findViewById<Button>(R.id.donationButton) // Initialize Btndonte button

        loadWebPageButton.setOnClickListener {
            // Create an Intent to open the web page in a browser
            val webpage = Uri.parse("https://pdsa20231113114300.azurewebsites.net/")
            val intent = Intent(Intent.ACTION_VIEW, webpage)

            // Verify that the intent will resolve to an activity
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }

        Btndonte.setOnClickListener {
            val intent = Intent(requireContext(), DonateActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}
