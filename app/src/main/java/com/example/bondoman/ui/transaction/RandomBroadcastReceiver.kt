package com.example.bondoman.ui.transaction

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RandomBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val randomIntent = Intent(context, TransactionActivity::class.java)
        randomIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        randomIntent.putExtra(TransactionActivity.KEY_TITLE, "apa")
        randomIntent.putExtra(TransactionActivity.KEY_AMOUNT, 0)
        randomIntent.putExtra(TransactionActivity.KEY_CATEGORY, TransactionActivity.CATEGORY_INCOME)
        context?.startActivity(randomIntent)
    }
}