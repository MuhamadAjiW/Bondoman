package com.example.bondoman.ui.hub.settings

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

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
                .setTitle("Excel format")
                .setPositiveButton("Confirm", DialogInterface.OnClickListener { _, index -> listener.onExcelDialogPositiveClick(this)})
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { _, index -> listener.onExcelDialogNegativeClick(this)})
                .setSingleChoiceItems(
                    arrayOf(".xlsx", ".xls"), 0
                ) { _, index -> listener.onExcelDialogChoiceClick(this, index) }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}