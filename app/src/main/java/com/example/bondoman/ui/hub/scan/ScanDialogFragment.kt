package com.example.bondoman.ui.hub.scan

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.databinding.FragmentDialogScanBinding
import com.example.bondoman.viewmodel.scan.ScanViewModel
import com.example.bondoman.viewmodel.scan.ScanViewModelFactory
import kotlinx.coroutines.launch

class ScanDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentDialogScanBinding
    private lateinit var scanViewModel: ScanViewModel

    val scannedTransactions = MutableLiveData(emptyList<TransactionEntity>())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogScanBinding.inflate(inflater, container, false)

        val database = AppDatabase.getInstance(requireActivity())
        val transactionRepo = TransactionRepository(database.transactionDao)
        val scanViewModelFactory = ScanViewModelFactory(transactionRepo)
        scanViewModel = ViewModelProvider(this, scanViewModelFactory)[ScanViewModel::class.java]

        binding.rvScanned.layoutManager = LinearLayoutManager(requireContext())
        val adapter = scannedTransactions.value?.let { ScanAdapter(requireContext(), it) }
        binding.rvScanned.adapter = adapter

        binding.btnRescan.setOnClickListener(::onRescanClick)
        binding.btnSave.setOnClickListener(::onSaveClick)

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        scannedTransactions.value = emptyList()
    }

    private fun onRescanClick(view: View) = dismiss()

    private fun onSaveClick(view: View) {
        scannedTransactions.value?.let { it1 ->
            viewLifecycleOwner.lifecycleScope.launch {
                scanViewModel.saveScanned(it1)
                dismiss()
            }
        }
    }
}