package com.carlosbogdan.tradersapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosbogdan.tradersapp.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 */

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        var prefs = activity?.getSharedPreferences(getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )

        var coin_convert = ""

        Firebase.firestore.collection("user-prefs").document(prefs?.getString("email", null).toString()).get()
            .addOnSuccessListener { document ->
                coin_convert = document["coin_convert"].toString()
                prefs?.edit()?.putString("coin_convert", coin_convert)?.apply()
            }

        Firebase.firestore.collection("current-cryptocoins").get()
            .addOnSuccessListener { result ->
                val recyclerView = binding.recyclerView
                val adapter = CustomAdapterCoins(coin_convert, result.size(), activity)
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = adapter
            }

        return(binding.root)
    }
}
