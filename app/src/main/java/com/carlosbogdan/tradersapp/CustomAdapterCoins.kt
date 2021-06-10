package com.carlosbogdan.tradersapp

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.card_coins_layout.view.*
import java.text.NumberFormat
import java.util.*


class CustomAdapterCoins(coin_pref: String, size: Int, activity: FragmentActivity?) : RecyclerView.Adapter<CustomAdapterCoins.ViewHolder>() {

    var coin_conversion = coin_pref
    var coin_size = size
    var context = activity

    val images = intArrayOf(
        R.drawable.ic_aave,
        R.drawable.ic_ada,
        R.drawable.ic_algo,
        R.drawable.ic_bch,
        R.drawable.ic_btc,
        R.drawable.ic_dot,
        R.drawable.ic_eos,
        R.drawable.ic_eth,
        R.drawable.ic_fil,
        R.drawable.ic_grt,
        R.drawable.ic_link,
        R.drawable.ic_ltc,
        R.drawable.ic_nu,
        R.drawable.ic_uni,
        R.drawable.ic_usdc,
        R.drawable.ic_usdt,
        R.drawable.ic_xlm,
        R.drawable.ic_xrp,
        R.drawable.ic_xtz,
        R.drawable.ic_yfi
    )

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage:ImageView
        var itemID: String
        var itemTitle : TextView
        var itemPrice: TextView
        var itemReturn: TextView
        var itemMarketCap:String
        var itemTotalExchange: String
        var itemTotalSupply:String
        var itemCategory:String
        var itemValueProposition:String

        //var itemDetail : TextView
        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemID = ""
            itemTitle = itemView.findViewById(R.id.item_title)
            itemPrice = itemView.findViewById(R.id.item_marketcap)
            itemReturn = itemView.findViewById(R.id.item_return)
            itemMarketCap = ""
            itemTotalExchange = ""
            itemTotalSupply = ""
            itemCategory = ""
            itemValueProposition = ""

            itemView.setOnClickListener{ v: View ->
                val coinIntent = Intent(context, CoinActivity::class.java).apply {
                    putExtra("id", itemID)
                    putExtra("title", v.item_title.text)
                    putExtra("price", v.item_marketcap.text)
                    putExtra("returns", v.item_return.text)
                    putExtra("returns_color", java.lang.String.format("#%06X", 0xFFFFFF and v.item_return.currentTextColor))
                    putExtra("market_cap", itemMarketCap)
                    putExtra("total_exchange", itemTotalExchange)
                    putExtra("total_supply", itemTotalSupply)
                    putExtra("category", itemCategory)
                    putExtra("value_proposition", itemValueProposition)
                }
                context?.startActivity(coinIntent)
                context?.overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left)
            }
        }
    }

    // Devuelve la vista actual con sus objetos
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_coins_layout,viewGroup,false)
        return ViewHolder(v)
    }

    // Asignar el item con su textview o imageview
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        Firebase.firestore.collection("current-cryptocoins").get()
            .addOnSuccessListener { result ->
                viewHolder.itemID = result.documents.get(i).id
                viewHolder.itemTitle.text = result.documents.get(i)["name"].toString()
                if (coin_conversion == "EURO") {
                    var value = result.documents.get(i)["price"].toString().replace("$", "").replace(",", "")
                    viewHolder.itemPrice.text = NumberFormat.getNumberInstance(Locale.GERMANY).format(value.toFloat() / 1.23) + "€"

                    value = result.documents.get(i)["total_exchange"].toString().replace("$", "").replace("M", "").replace("B", "")
                    var value_str = NumberFormat.getNumberInstance(Locale.GERMANY).format(value.toDouble() / 1.23)
                    if (result.documents.get(i)["total_exchange"]?.toString()?.contains('M') == true) {
                        value_str += "M €"
                    } else {
                        value_str += "B €"
                    }
                    viewHolder.itemTotalExchange = value_str

                    value = result.documents.get(i)["market_cap"].toString().replace("$", "").replace("M", "").replace("B", "")
                    value_str = NumberFormat.getNumberInstance(Locale.GERMANY).format(value.toDouble() / 1.23)
                    if (result.documents.get(i)["total_exchange"]?.toString()?.contains('M') == true) {
                        value_str += "M €"
                    } else {
                        value_str += "B €"
                    }
                    viewHolder.itemMarketCap = value_str
                } else {
                    viewHolder.itemPrice.text = result.documents.get(i)["price"].toString()
                    viewHolder.itemTotalExchange = result.documents.get(i)["total_exchange"].toString()
                    viewHolder.itemMarketCap = result.documents.get(i)["market_cap"].toString()
                }

                var return_24 = result.documents.get(i)["return"].toString()
                if (return_24.startsWith("-")) {
                    viewHolder.itemReturn.setTextColor(Color.parseColor("#F30E0E"))
                    viewHolder.itemReturn.text=return_24.replaceFirstChar { "" }
                } else {
                    viewHolder.itemReturn.setTextColor(Color.parseColor("#48EE4A"))
                    viewHolder.itemReturn.text=return_24.replaceFirstChar { "" }
                }

                viewHolder.itemTotalSupply = result.documents.get(i)["total_supply"].toString()
                viewHolder.itemCategory = result.documents.get(i)["category"].toString()
                viewHolder.itemValueProposition = result.documents.get(i)["value_proposition"].toString()
                }
        viewHolder.itemImage.setImageResource(images[i])
    }

    override fun getItemCount(): Int {
        return coin_size
    }
}