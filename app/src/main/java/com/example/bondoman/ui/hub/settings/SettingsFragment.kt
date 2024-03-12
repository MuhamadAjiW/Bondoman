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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentSettingsBinding
import com.example.bondoman.ui.login.LoginActivity
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
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

    private fun saveTransaction(view: View){
        // Request permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            return
        }

        // Init with viewmodel
        val transactionViewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)
        transactionViewModel.list.observe(viewLifecycleOwner) { transactionList ->
            if (transactionList != null){

                // Initialize excel file
                val workbook = XSSFWorkbook()
                val workSheet = workbook.createSheet("Transactions")
                val headerCellStyle = workbook.createCellStyle()
                headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index)
                headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
                headerCellStyle.setBorderTop(BorderStyle.THIN)
                headerCellStyle.setBorderBottom(BorderStyle.THIN)
                headerCellStyle.setBorderLeft(BorderStyle.THIN)
                headerCellStyle.setBorderRight(BorderStyle.THIN)

                // Initialize headers
                val headers = arrayOf(
                    getString(R.string.transaction_label_number),
                    getString(R.string.transaction_label_title),
                    getString(R.string.transaction_label_category),
                    getString(R.string.transaction_label_amount),
                    getString(R.string.transaction_label_location),
                    getString(R.string.transaction_label_timestamp)
                )
                val firstRow = workSheet.createRow(0)
                for ((index, header) in headers.withIndex()) {
                    val cell = firstRow.createCell(index)
                    cell.setCellValue(header)
                    cell.cellStyle = headerCellStyle

                    workSheet.setColumnWidth(index, 6000)
                }
                workSheet.setColumnWidth(0, 2000)

                // Insert data
                val cellStyle = headerCellStyle.copy()
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.index)
                cellStyle.wrapText = true

                for ((index, transaction) in transactionList.withIndex()){
                    val row = workSheet.createRow(1 + index)

                    var cell = row.createCell(0)
                    cell.setCellValue((1 + index).toString())
                    cell.cellStyle = cellStyle

                    cell = row.createCell(1)
                    cell.setCellValue(transaction.title)
                    cell.cellStyle = cellStyle

                    cell = row.createCell(2)
                    cell.setCellValue(transaction.category)
                    cell.cellStyle = cellStyle

                    cell = row.createCell(3)
                    cell.setCellValue(transaction.amount.toDouble())
                    cell.cellStyle = cellStyle

                    cell = row.createCell(4)
                    cell.setCellValue(transaction.location)
                    cell.cellStyle = cellStyle

                    cell = row.createCell(5)
                    cell.setCellValue(transaction.timestamp)
                    cell.cellStyle = cellStyle
                }
                // Output file
                val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(
                    path,
                    "BondomanTransaction" + SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(Date()) + ".xlsx"
                )
                workbook.write(file.outputStream())
                workbook.close()

                Toast.makeText(requireContext(),  getString(R.string.save_toast_success) + "$path", Toast.LENGTH_SHORT).show()

                // Open file immediately
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider", file), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            else{
                Toast.makeText(requireContext(), getString(R.string.settings_toast_uninitialized_viewmodel), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendTransaction(view: View){
        // Init with viewmodel
        val transactionViewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)
        transactionViewModel.list.observe(viewLifecycleOwner) { transactionList ->
            if (transactionList != null){

                // Initialize excel file
                val workbook = XSSFWorkbook()
                val workSheet = workbook.createSheet("Transactions")
                val headerCellStyle = workbook.createCellStyle()
                headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index)
                headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
                headerCellStyle.setBorderTop(BorderStyle.THIN)
                headerCellStyle.setBorderBottom(BorderStyle.THIN)
                headerCellStyle.setBorderLeft(BorderStyle.THIN)
                headerCellStyle.setBorderRight(BorderStyle.THIN)

                // Initialize headers
                val headers = arrayOf(
                    getString(R.string.transaction_label_number),
                    getString(R.string.transaction_label_title),
                    getString(R.string.transaction_label_category),
                    getString(R.string.transaction_label_amount),
                    getString(R.string.transaction_label_location),
                    getString(R.string.transaction_label_timestamp)
                )
                val firstRow = workSheet.createRow(0)
                for ((index, header) in headers.withIndex()) {
                    val cell = firstRow.createCell(index)
                    cell.setCellValue(header)
                    cell.cellStyle = headerCellStyle

                    workSheet.setColumnWidth(index, 6000)
                }
                workSheet.setColumnWidth(0, 2000)

                // Insert data
                val cellStyle = headerCellStyle.copy()
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.index)
                cellStyle.wrapText = true

                for ((index, transaction) in transactionList.withIndex()){
                    val row = workSheet.createRow(1 + index)

                    var cell = row.createCell(0)
                    cell.setCellValue((1 + index).toString())
                    cell.cellStyle = cellStyle

                    cell = row.createCell(1)
                    cell.setCellValue(transaction.title)
                    cell.cellStyle = cellStyle

                    cell = row.createCell(2)
                    cell.setCellValue(transaction.category)
                    cell.cellStyle = cellStyle

                    cell = row.createCell(3)
                    cell.setCellValue(transaction.amount.toDouble())
                    cell.cellStyle = cellStyle

                    cell = row.createCell(4)
                    cell.setCellValue(transaction.location)
                    cell.cellStyle = cellStyle

                    cell = row.createCell(5)
                    cell.setCellValue(transaction.timestamp)
                    cell.cellStyle = cellStyle
                }

                // Output file
                val path = requireContext().cacheDir
                val file = File(
                    path,
                    "BondomanTransaction" + SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(Date()) + ".xlsx"
                )
                workbook.write(file.outputStream())
                workbook.close()

                // Prep email
                // TODO: set email recipient as the logged in account
                val emailRecipient = arrayOf("13521095@std.stei.itb.ac.id")
//                val emailIntent = Intent(Intent.ACTION_SENDTO,  Uri.fromParts("mailto",emailRecipient, null))
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.type = "text/plain"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailRecipient)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text) + SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
                emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider", file))
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                // Send email
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send Email"))
                    Toast.makeText(requireContext(), getString(R.string.email_toast_success), Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    println(e.message)
                    Toast.makeText(requireContext(), getString(R.string.email_toast_fail), Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(requireContext(), getString(R.string.settings_toast_uninitialized_viewmodel), Toast.LENGTH_SHORT).show()
            }
        }
    }

    //TODO: Implement
    private fun logout(view: View){


        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}