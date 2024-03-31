package com.example.bondoman.ui.hub.transaction

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.bondoman.R

class DeleteConfirmDialogFragment : DialogFragment() {
    internal lateinit var listener: DeleteConfirmDialogListener

    interface DeleteConfirmDialogListener{
        fun onDeleteDialogPositiveClick(dialog: DialogFragment)
        fun onDeleteDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.delete_confirm_dialog_label)
                .setPositiveButton(R.string.delete_confirm_dialog_positive) { _, _ -> listener.onDeleteDialogPositiveClick(this) }
                .setNegativeButton(R.string.delete_confirm_dialog_negative) { _, _ -> listener.onDeleteDialogNegativeClick(this) }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}