package com.example.bondoman.ui.hub.settings

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kotlin.ClassCastException

class ExcelDialogFragment : DialogFragment() {
    internal lateinit var listener: ExcelDialogListener

    interface ExcelDialogListener{
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
        fun onDialogChoiceClick(dialog: DialogFragment, index: Int)
    }

    override fun onAttach(context: Context){
        super.onAttach(context)
        try {
            listener = context as ExcelDialogListener
        } catch (e: ClassCastException){
            throw ClassCastException(("$context must implement ExcelDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle("Excel format")
                .setPositiveButton("Confirm", DialogInterface.OnClickListener { _, _ -> listener.onDialogPositiveClick(this)})
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ -> listener.onDialogNegativeClick(this)})
                .setSingleChoiceItems(
                    arrayOf(".Xlsx", "Xls"), 0
                ) { _, index -> listener.onDialogChoiceClick(this, index) }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}