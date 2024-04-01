package com.example.bondoman.ui.hub.scan

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.databinding.ItemTransactionBinding
import java.text.NumberFormat
import java.util.Locale

class ScanAdapter(private val context: Context, private val tsList: List<TransactionEntity>) :
    RecyclerView.Adapter<ScanAdapter.ViewHolder>() {
    inner class ViewHolder(val view: ItemTransactionBinding) : RecyclerView.ViewHolder(view.root)

    private lateinit var transaction: TransactionEntity
    private lateinit var binding: ItemTransactionBinding

    companion object {
        val locale: Locale = Locale("id", "ID")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("ScanAdapter", "masuk 2")
        binding = ItemTransactionBinding.inflate(LayoutInflater.from(context), parent, false)
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

            btnLocation.visibility = View.GONE
            tvLocation.visibility = View.GONE
            locationIcon.visibility = View.GONE

            btnDelete.visibility = View.GONE
            btnEdit.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        Log.d("ScanAdapter", tsList.size.toString())
        return tsList.size
    }
}