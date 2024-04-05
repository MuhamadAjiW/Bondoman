package com.example.bondoman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.bondoman.ui.transaction.TransactionActivity
import net.datafaker.Faker

class RandomBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val category = categoryFaker()
        val amount = amountFaker()
        val title = if (category == TransactionActivity.CATEGORY_INCOME) {
            "Sell ${titleFaker()}"
        } else {
            "Buy ${titleFaker()}"
        }

        //TODO: Randomizer
        val randomIntent = Intent(context, TransactionActivity::class.java)
        randomIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        randomIntent.putExtra(TransactionActivity.KEY_ACTION, TransactionActivity.ACTION_ADD)

        randomIntent.putExtra(TransactionActivity.KEY_TITLE, title)
        randomIntent.putExtra(TransactionActivity.KEY_AMOUNT, amount)
        randomIntent.putExtra(TransactionActivity.KEY_CATEGORY, TransactionActivity.CATEGORY_INCOME)
        context?.startActivity(randomIntent)
    }


    companion object {
        private val faker = Faker()

        private fun titleFaker(): String {
            return when ((1..4).random()) {
                1 -> faker.book().title()
                2 -> faker.music().instrument()
                3 -> faker.videoGame().title()
                else -> faker.food().dish()
            }
        }

        private fun categoryFaker(): Int {
            return when((0..1).random()) {
                0 -> TransactionActivity.CATEGORY_EXPENSES
                else -> TransactionActivity.CATEGORY_INCOME
            }
        }

        private fun amountFaker(): Int {
            return (1..20000000).random()
        }
    }
}