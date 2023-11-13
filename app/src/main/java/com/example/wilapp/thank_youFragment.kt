package com.example.wilapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*

class thank_youFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thank_you, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Use Coroutine to delay and then navigate to the home fragment
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000) // Delay for 3 seconds (adjust as needed)
            findNavController().navigate(R.id.action_thankYouFragment_to_homeFragment)
        }
    }
}