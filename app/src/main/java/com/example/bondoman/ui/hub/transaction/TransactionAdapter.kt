package com.example.bondoman.ui.hub.transaction

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.R
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.databinding.ItemTransactionBinding
import com.example.bondoman.ui.hub.addtransaction.AddTransactionFragment
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

    companion object {
        val locale: Locale = Locale("id", "ID")
    }
    private val geocoder = Geocoder(context, locale)

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

            val formatter: NumberFormat = NumberFormat.getNumberInstance(locale)
            val amount = "Rp${formatter.format(tsList[position].amount)}"
            tvAmount.text = amount

            var location = context.getString(R.string.no_location_data)
            if (tsList[position].latitude != null && tsList[position].longitude != null) {
                location = "(${tsList[position].latitude}, ${tsList[position].longitude})"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        tsList[position].latitude!!,
                        tsList[position].longitude!!,
                        1,
                        (GeocodeListener {
                            if (it.size > 0) {
                                tvLocation.text = it[0].locality.toString()
                            }
                        })
                    )
                } else {
                    tvLocation.text = location
                }

                btnLocation.visibility = View.VISIBLE
                tvLocation.visibility = View.VISIBLE
                locationIcon.visibility = View.VISIBLE
            } else {
                btnLocation.visibility = View.GONE
                tvLocation.visibility = View.GONE
                locationIcon.visibility = View.GONE
            }

            btnLocation.setOnClickListener {
                val gmapsIntentUri = Uri.parse("geo:${tsList[position].latitude}, ${tsList[position].longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmapsIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            }

            //TODO: Delete, this is alternative code for using activity
//            btnEdit.setOnClickListener {
//                val intent = Intent(context, TransactionActivity::class.java)
//                intent.putExtra(TransactionActivity.KEY_ACTION, TransactionActivity.ACTION_EDIT)
//
//                // TODO: Consider serializing or just pass the id and read it from the transaction page. Might offer better performance though
//                intent.putExtra(TransactionActivity.KEY_TRANSACTION_ID, tsList[position].id)
//                intent.putExtra(TransactionActivity.KEY_TITLE, tsList[position].title)
//                intent.putExtra(TransactionActivity.KEY_AMOUNT, tsList[position].amount)
//                intent.putExtra(TransactionActivity.KEY_CATEGORY, context.resources.getStringArray(R.array.category_choices).indexOf(tsList[position].category))
//                intent.putExtra(TransactionActivity.KEY_LOCATION, location)
//                intent.putExtra(TransactionActivity.KEY_TIMESTAMP, tsList[position].timestamp)
//
//                context.startActivity(intent)
//            }

            btnEdit.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(TransactionActivity.KEY_ACTION, AddTransactionFragment.ACTION_EDIT)

                // TODO: Consider serializing or just pass the id and read it from the transaction page. Might offer better performance though
                bundle.putInt(TransactionActivity.KEY_TRANSACTION_ID, tsList[position].id)
                bundle.putString(TransactionActivity.KEY_TITLE, tsList[position].title)
                bundle.putInt(TransactionActivity.KEY_AMOUNT, tsList[position].amount)
                bundle.putInt(TransactionActivity.KEY_CATEGORY, context.resources.getStringArray(R.array.category_choices).indexOf(tsList[position].category))
                bundle.putString(TransactionActivity.KEY_LOCATION, location)
                bundle.putString(TransactionActivity.KEY_TIMESTAMP, tsList[position].timestamp)

                val transaction = fragmentManager.beginTransaction()
                val fragment = AddTransactionFragment()
                fragment.arguments = bundle

                transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
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