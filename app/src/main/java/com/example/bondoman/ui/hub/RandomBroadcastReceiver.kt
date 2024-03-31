package com.example.bondoman.ui.hub

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.bondoman.R
import com.example.bondoman.ui.hub.addtransaction.AddTransactionFragment
import com.example.bondoman.ui.transaction.TransactionActivity

class RandomBroadcastReceiver(val fragmentManager: FragmentManager) : BroadcastReceiver() {
    //TODO: Delete, this is alternative code for using activity
//    override fun onReceive(context: Context?, intent: Intent?) {
//        val randomIntent = Intent(context, TransactionActivity::class.java)
//        randomIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        randomIntent.putExtra(TransactionActivity.KEY_TITLE, "apa")
//        randomIntent.putExtra(TransactionActivity.KEY_AMOUNT, 0)
//        randomIntent.putExtra(TransactionActivity.KEY_CATEGORY, TransactionActivity.CATEGORY_INCOME)
//        context?.startActivity(randomIntent)
//    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = Bundle()
        bundle.putInt(TransactionActivity.KEY_ACTION, AddTransactionFragment.ACTION_ADD)

        // TODO: Consider serializing or just pass the id and read it from the transaction page. Might offer better performance though
        // TODO: Randomizer
        bundle.putString(TransactionActivity.KEY_TITLE, "apa")
        bundle.putInt(TransactionActivity.KEY_AMOUNT, 0)
        bundle.putInt(TransactionActivity.KEY_CATEGORY, AddTransactionFragment.CATEGORY_INCOME)

        val transaction = fragmentManager.beginTransaction()
        val fragment = AddTransactionFragment()
        fragment.arguments = bundle

        transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}