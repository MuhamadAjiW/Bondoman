package com.example.bondoman.ui.hub.settings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.bondoman.R

class ExcelDialogFragment : DialogFragment() {
    internal lateinit var listener: ExcelDialogListener

    interface ExcelDialogListener{
        fun onExcelDialogPositiveClick(dialog: DialogFragment)
        fun onExcelDialogNegativeClick(dialog: DialogFragment)
        fun onExcelDialogChoiceClick(dialog: DialogFragment, index: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.excel_dialog_label)
                .setPositiveButton(R.string.excel_dialog_positive) { _, _ -> listener.onExcelDialogPositiveClick(this) }
                .setNegativeButton(R.string.excel_dialog_negative) { _, _ -> listener.onExcelDialogNegativeClick(this) }
                .setSingleChoiceItems(arrayOf(".xlsx", ".xls"), 0)
                    { _, index -> listener.onExcelDialogChoiceClick(this, index) }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}