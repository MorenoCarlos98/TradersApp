package com.carlosbogdan.tradersapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CustomAdapterNews(activity: FragmentActivity?, size: Int) : RecyclerView.Adapter<CustomAdapterNews.ViewHolder>() {

    val list: MutableList<String> = ArrayList()

    var context = activity

    var news_size = size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemTitle : TextView
        var itemImage:ImageView

          init {
            itemTitle = itemView.findViewById(R.id.item_title)
            itemImage = itemView.findViewById(R.id.item_image)


              itemView.setOnClickListener{
                  val position: Int = adapterPosition
                  val i = Intent(Intent.ACTION_VIEW)
                  i.data = Uri.parse(list.get(position))
                  context?.startActivity(i)
              }
          }
    }

  // Devuelve la vista actual con sus objetos
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
      val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_news_layout,viewGroup,false)
      return ViewHolder(v)
    }

    // Asignar el item con su textview o imageview
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        Firebase.firestore.collection("news-cryptocoins").get()
            .addOnSuccessListener { result ->
                viewHolder.itemTitle.text = result.documents[i]["title"].toString()

                Glide.with(viewHolder.itemView)
                    .load(result.documents[i]["link_img"].toString())
                    .into(viewHolder.itemImage)

                this.list.add(result.documents[i]["link_new"].toString())
            }
    }

    override fun getItemCount(): Int {
        return news_size
    }
}