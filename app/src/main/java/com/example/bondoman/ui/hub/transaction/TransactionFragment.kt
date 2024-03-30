package com.example.bondoman.ui.hub.transaction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bondoman.R
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.databinding.FragmentTransactionBinding
import com.example.bondoman.ui.hub.HubActivity
import com.example.bondoman.ui.transaction.TransactionActivity
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

class TransactionFragment : Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var tsViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        binding.addTransaction.setOnClickListener(::onAddClick)
        binding.rvTransaction.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tsViewModel = (requireActivity() as HubActivity).transactionViewModel

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val header = requireActivity().findViewById<TextView>(R.id.nav_title)
            header.text = getString(R.string.hub_nav_transaction)
        }

        tsViewModel.list.observe(viewLifecycleOwner) { tsList ->
            val adapter = TransactionAdapter(requireActivity(), tsList)
            binding.rvTransaction.adapter = adapter
        }
    }

    private fun onAddClick(view: View){
        val intent = Intent(requireContext(), TransactionActivity::class.java)
        startActivity(intent)
    }
}