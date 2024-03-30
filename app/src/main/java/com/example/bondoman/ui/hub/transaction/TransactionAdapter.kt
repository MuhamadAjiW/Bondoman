package com.example.bondoman.ui.hub.transaction

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.R
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.databinding.ItemTransactionBinding
import com.example.bondoman.ui.transaction.TransactionActivity
import java.text.NumberFormat
import java.util.Locale


class TransactionAdapter(
    private val ctx: Activity,
    private val tsList: List<TransactionEntity>
): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    inner class ViewHolder(val view: ItemTransactionBinding): RecyclerView.ViewHolder(view.root)
    private lateinit var binding: ItemTransactionBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTransactionBinding.inflate(LayoutInflater.from(ctx), parent, false)
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
                ctx.startActivity(mapIntent)
            }

            btnEdit.setOnClickListener {
                val intent = Intent(ctx, TransactionActivity::class.java)
                intent.putExtra(TransactionActivity.actionKey, TransactionActivity.ACTION_EDIT)

                // Pass all data
                // TODO: Consider serializing or just pass the id and read it from the transaction page. Might offer better performance though
                intent.putExtra(TransactionActivity.transactionIdKey, tsList[position].id)
                intent.putExtra(TransactionActivity.titleKey, tsList[position].title)
                intent.putExtra(TransactionActivity.amountKey, tsList[position].amount)
                intent.putExtra(TransactionActivity.categoryKey, ctx.resources.getStringArray(R.array.category_choices).indexOf(tsList[position].category))
                intent.putExtra(TransactionActivity.locationKey, tsList[position].location)
                intent.putExtra(TransactionActivity.timestampKey, tsList[position].timestamp)

                ctx.startActivity(intent)
            }
        }
    }



    override fun getItemCount(): Int {
        return tsList.size
    }
}