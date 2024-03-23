package com.example.bondoman.ui.hub.transaction

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.databinding.ItemTransactionBinding

class TransactionAdapter(
    val ctx: Context,
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
            tvAmount.text = tsList[position].amount.toString()
            // TODO: location
            tvLocation.text = tsList[position].amount.toString()
            btnLocation.setOnClickListener {
                // TODO: location
                val gmapsIntentUri = Uri.parse("geo:46.414382,10.013988")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmapsIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                ctx.startActivity(mapIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return tsList.size
    }
}