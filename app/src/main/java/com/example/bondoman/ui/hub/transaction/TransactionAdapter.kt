package com.example.bondoman.ui.hub.transaction

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.R
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.databinding.ItemTransactionBinding
import com.example.bondoman.ui.transaction.TransactionActivity
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import java.text.NumberFormat
import java.util.Locale


class TransactionAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val viewModel: TransactionViewModel,
    private val tsList: List<TransactionEntity>
): RecyclerView.Adapter<TransactionAdapter.ViewHolder>(), DeleteConfirmDialogFragment.DeleteConfirmDialogListener {
    inner class ViewHolder(val view: ItemTransactionBinding): RecyclerView.ViewHolder(view.root)
    private lateinit var transaction: TransactionEntity
    private lateinit var binding: ItemTransactionBinding
    private val deleteConfirmDialog: DeleteConfirmDialogFragment = DeleteConfirmDialogFragment()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTransactionBinding.inflate(LayoutInflater.from(context), parent, false)
        deleteConfirmDialog.listener = this
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.apply {
            tvDate.text = tsList[position].timestamp.split(" ")[0]
            tvCategory.text = tsList[position].category
            tvTitle.text = tsList[position].title

            val formatter: NumberFormat = NumberFormat.getNumberInstance(Locale("id", "ID"))
            val amount = "Rp${formatter.format(tsList[position].amount)}"
            tvAmount.text = amount

            // TODO: Reverse Geocoding
            tvLocation.text = tsList[position].location

            btnLocation.setOnClickListener {
                // TODO: location
                val gmapsIntentUri = Uri.parse("geo:46.414382,10.013988")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmapsIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            }

            btnEdit.setOnClickListener {
                val intent = Intent(context, TransactionActivity::class.java)
                intent.putExtra(TransactionActivity.actionKey, TransactionActivity.ACTION_EDIT)

                // TODO: Consider serializing or just pass the id and read it from the transaction page. Might offer better performance though
                intent.putExtra(TransactionActivity.transactionIdKey, tsList[position].id)
                intent.putExtra(TransactionActivity.titleKey, tsList[position].title)
                intent.putExtra(TransactionActivity.amountKey, tsList[position].amount)
                intent.putExtra(TransactionActivity.categoryKey, context.resources.getStringArray(R.array.category_choices).indexOf(tsList[position].category))
                intent.putExtra(TransactionActivity.locationKey, tsList[position].location)
                intent.putExtra(TransactionActivity.timestampKey, tsList[position].timestamp)

                context.startActivity(intent)
            }

            btnDelete.setOnClickListener {
                transaction = tsList[position]
                deleteConfirmDialog.show(fragmentManager, "delete")
            }
        }
    }

    override fun getItemCount(): Int {
        return tsList.size
    }

    override fun onDeleteDialogPositiveClick(dialog: DialogFragment) {
        viewModel.delete(transaction)
        deleteConfirmDialog.dismiss()
    }

    override fun onDeleteDialogNegativeClick(dialog: DialogFragment) {
        deleteConfirmDialog.dismiss()
    }
}