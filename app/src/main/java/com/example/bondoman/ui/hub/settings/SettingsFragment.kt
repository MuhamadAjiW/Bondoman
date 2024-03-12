package com.example.bondoman.ui.hub.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentSettingsBinding
import com.example.bondoman.ui.hub.HubActivity
import com.example.bondoman.ui.login.LoginActivity

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.buttonSaveTransaction.setOnClickListener(::saveTransaction)
        binding.buttonSendTransaction.setOnClickListener(::sendTransaction)
        binding.buttonLogout.setOnClickListener(::logout)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val header = requireActivity().findViewById<TextView>(R.id.nav_title)
            header.text = getString(R.string.hub_nav_settings)
        }
    }

    //TODO: Implement
    private fun saveTransaction(view: View){
        println("Transaction saved")
    }

    //TODO: Implement
    private fun sendTransaction(view: View){
        println("Transaction sent")
    }

    //TODO: Implement
    private fun logout(view: View){


        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}