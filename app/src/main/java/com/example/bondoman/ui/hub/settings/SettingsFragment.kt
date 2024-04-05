package com.example.bondoman.ui.hub.settings

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.BondomanApp
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentSettingsBinding
import com.example.bondoman.services.AuthService
import com.example.bondoman.services.SessionManager
import com.example.bondoman.types.enums.ExcelTypes
import com.example.bondoman.types.util.ExcelUtil
import com.example.bondoman.ui.login.LoginActivity
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SettingsFragment : Fragment(), ExcelDialogFragment.ExcelDialogListener {

    internal enum class ButtonCode{
        SAVE_BUTTON,
        SEND_BUTTON
    }

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var excelUtil: ExcelUtil
    private val excelDialog: ExcelDialogFragment = ExcelDialogFragment()
    private var excelFormat: ExcelTypes = ExcelTypes.XLSX
    private var buttonCode: ButtonCode = ButtonCode.SAVE_BUTTON
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.buttonSaveTransaction.setOnClickListener(::onSaveTransactionClick)
        binding.buttonSendTransaction.setOnClickListener(::onSendTransactionClick)
        binding.buttonRandomTransaction.setOnClickListener(::onRandomTransactionClick)
        binding.buttonLogout.setOnClickListener(::onLogoutClick)

        excelUtil = ExcelUtil(requireContext())
        sessionManager = SessionManager(requireContext())
        excelDialog.listener = this

        transactionViewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val header = requireActivity().findViewById<TextView>(R.id.nav_title)
            header.text = getString(R.string.hub_nav_settings)
        }
    }

    override fun onExcelDialogPositiveClick(dialog: DialogFragment) {
        when (buttonCode){
            ButtonCode.SAVE_BUTTON -> saveExcel()
            ButtonCode.SEND_BUTTON -> sendExcel()
        }
        excelFormat = ExcelTypes.XLSX
    }

    override fun onExcelDialogNegativeClick(dialog: DialogFragment) {
        excelDialog.dismiss()
    }

    override fun onExcelDialogChoiceClick(dialog: DialogFragment, index: Int) {
        when (index){
            0 -> excelFormat = ExcelTypes.XLSX
            1 -> excelFormat = ExcelTypes.XLS
        }
    }

    private fun saveExcel(){
        transactionViewModel.list.observe(viewLifecycleOwner) { transactionList ->
            try {
                val file = excelUtil.exportTransaction(
                    transactionList,
                    excelFormat,
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                )

                Toast.makeText(requireContext(),  getString(R.string.save_toast_success) + "$file", Toast.LENGTH_SHORT).show()

                // Open file immediately
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider", file), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            catch (e: Error){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendExcel(){
        transactionViewModel.list.observe(viewLifecycleOwner) { transactionList ->
            try {
                val file = excelUtil.exportTransaction(
                    transactionList,
                    ExcelTypes.XLS,
                    requireContext().cacheDir
                )

                // Prep email
                // TODO: set email recipient as the logged in account
                val emailRecipient = arrayOf("13521095@std.stei.itb.ac.id")
//                val emailIntent = Intent(Intent.ACTION_SENDTO,  Uri.fromParts("mailto",emailRecipient, null))
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.type = "text/plain"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailRecipient)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                emailIntent.putExtra(Intent.EXTRA_TEXT, "${getString(R.string.email_text)} ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}")
                emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider", file))
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send Email"))
                    Toast.makeText(requireContext(), getString(R.string.email_toast_success), Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    println(e.message)
                    throw Error(getString(R.string.email_toast_fail))
                }

            } catch (e: Error){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onSaveTransactionClick(view: View){
        // Request permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            return
        }

        buttonCode = ButtonCode.SAVE_BUTTON
        excelDialog.show(parentFragmentManager, "excel")
    }

    private fun onSendTransactionClick(view: View){
        buttonCode = ButtonCode.SEND_BUTTON
        excelDialog.show(parentFragmentManager, "excel")
    }


    private fun onRandomTransactionClick(view: View){
        val intent = Intent(BondomanApp.ACTION_RANDOM_TRANSACTION)
        requireContext().sendBroadcast(intent)
    }

    private fun onLogoutClick(view: View){
        sessionManager.clearToken()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()

        val authService = Intent(requireContext(), AuthService::class.java)
        requireContext().stopService(authService)
    }
}