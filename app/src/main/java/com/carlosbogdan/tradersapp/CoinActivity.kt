package com.carlosbogdan.tradersapp

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_coin.*
import kotlinx.android.synthetic.main.activity_coin.item_image
import kotlinx.android.synthetic.main.activity_coin.item_marketcap
import kotlinx.android.synthetic.main.activity_coin.item_return
import kotlinx.android.synthetic.main.activity_coin.item_title
import kotlinx.android.synthetic.main.card_coins_layout.*


class CoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)

        val bundle:Bundle? = intent.extras
        var img = "ic_" + bundle?.getString("id")?.lowercase()
        item_image.setImageResource(resources.getIdentifier(img, "drawable", packageName))
        item_title.text = bundle?.getString("title")
        item_symbol.text = bundle?.getString("id")
        item_price.text = bundle?.getString("price")
        item_return.text = bundle?.getString("returns")
        item_return.setTextColor(Color.parseColor(bundle?.getString("returns_color")))
        item_marketcap.text = bundle?.getString("market_cap")
        item_totalexchange.text = bundle?.getString("total_exchange")
        item_totalsupply.text = bundle?.getString("total_supply")
        item_category.text = bundle?.getString("category")
        item_valueproposition.text = bundle?.getString("value_proposition")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)
    }
}