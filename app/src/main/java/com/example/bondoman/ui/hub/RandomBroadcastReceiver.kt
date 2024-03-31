package com.example.bondoman.ui.hub

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.bondoman.R
import com.example.bondoman.ui.hub.addtransaction.AddTransactionFragment
import net.datafaker.Faker

class RandomBroadcastReceiver(private val fragmentManager: FragmentManager) : BroadcastReceiver() {

    companion object {
        private val faker = Faker()

        private fun titleFaker(): String {
            when ((1..4).random()) {
                1 -> return faker.book().title()
                2 -> return faker.music().instrument()
                3 -> return faker.videoGame().title()
                else -> return faker.food().dish()
            }
        }

        private fun categoryFaker(): Int {
            when((0..1).random()) {
                0 -> return AddTransactionFragment.CATEGORY_EXPENSES
                else -> return AddTransactionFragment.CATEGORY_INCOME
            }
        }

        private fun amountFaker(): Int {
            return (1..20000000).random()
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = Bundle()
        bundle.putInt(AddTransactionFragment.KEY_ACTION, AddTransactionFragment.ACTION_ADD)

        val category = categoryFaker()
        var title = titleFaker()
        if (category == AddTransactionFragment.CATEGORY_INCOME) {
            title = "Sell $title"
        } else {
            title = "Buy $title"
        }
        bundle.putString(AddTransactionFragment.KEY_TITLE, title)
        bundle.putInt(AddTransactionFragment.KEY_AMOUNT, amountFaker())
        bundle.putInt(AddTransactionFragment.KEY_CATEGORY, category)

        val transaction = fragmentManager.beginTransaction()
        val fragment = AddTransactionFragment()
        fragment.arguments = bundle

        val navHostFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        navHostFragment?.let {
            val navController = navHostFragment.findNavController()
            try {
                navController.popBackStack()
            }catch (error: Error){
                println("NavController don't have extra back stack")
            }
        }

        transaction.add(R.id.nav_host_fragment_activity_main, fragment)

        transaction.addToBackStack(null)
        transaction.commit()
    }
}