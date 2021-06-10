package com.carlosbogdan.tradersapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosbogdan.tradersapp.databinding.FragmentNewsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 */
class NewsFragment : Fragment() {

    lateinit var binding : FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater)

        Firebase.firestore.collection("news-cryptocoins").get()
            .addOnSuccessListener { result ->
                val recyclerView = binding.recyclerView
                val adapter = CustomAdapterNews(activity, result.size())
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = adapter
            }
        return(binding.root)
    }

}
