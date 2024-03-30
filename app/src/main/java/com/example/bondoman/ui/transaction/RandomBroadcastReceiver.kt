package com.example.bondoman.ui.transaction

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RandomBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        //TODO: Randomizer
        val randomIntent = Intent(context, TransactionActivity::class.java)
        randomIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        randomIntent.putExtra(TransactionActivity.titleKey, "apa")
        randomIntent.putExtra(TransactionActivity.amountKey, 0)
        randomIntent.putExtra(TransactionActivity.categoryKey, TransactionActivity.CATEGORY_INCOME)
        randomIntent.putExtra(TransactionActivity.locationKey, "Ada di mana")
        context?.startActivity(randomIntent)
    }
}