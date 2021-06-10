package com.carlosbogdan.tradersapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.carlosbogdan.tradersapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_profile.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        //obtener email y preferencias guardado en file
        val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )

        binding.emailTxt.text = prefs?.getString("email", null)
        binding.usernameTxt.text = prefs?.getString("name", null)
        binding.txtCoinConverter.text = prefs?.getString("coin_convert", null)

        binding.txtCoinConverter.setOnClickListener {
            prefs?.edit()?.remove("coin_convert")?.apply()

            if (binding.txtCoinConverter.text.equals("USD")) {
                prefs?.edit()?.putString("coin_convert", "EURO")?.apply()
                binding.txtCoinConverter.text = "EURO"
            } else{
                prefs?.edit()?.putString("coin_convert", "USD")?.apply()
                binding.txtCoinConverter.text = "USD"
            }

            Firebase.firestore.collection("user-prefs").document(binding.emailTxt.text.toString()).update("coin_convert", txt_coinConverter.text.toString())
        }

        binding.editUsername.setOnClickListener {
            val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }

            builder?.setTitle("Change username")

            val input = EditText(activity)

            input.setHint("New username")
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder?.setView(input)

            builder?.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                var newUsername = input.text.toString()

                binding.usernameTxt.text = newUsername

                prefs?.edit()?.putString("name", newUsername)?.apply()

                var request: UserProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(newUsername).build()

                FirebaseAuth.getInstance().currentUser.updateProfile(request)
            })
            builder?.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder?.show()
        }

        binding.editEmail.setOnClickListener{
            val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }

            builder?.setTitle("Change email")

            val input = EditText(activity)

            input.setHint("New email")
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder?.setView(input)

            builder?.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                var newEmail = input.text.toString()

                binding.emailTxt.text = newEmail

                prefs?.edit()?.putString("email", newEmail)?.apply()

                FirebaseAuth.getInstance().currentUser.updateEmail(newEmail)
            })
            builder?.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder?.show()
        }

        binding.editPassword.setOnClickListener{
            val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }

            builder?.setTitle("Change password")

            val input = EditText(activity)

            input.setHint("New password")
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder?.setView(input)

            builder?.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                Firebase.firestore.collection("user-prefs").document(binding.emailTxt.text.toString()).update("id", input.text.toString())
                FirebaseAuth.getInstance().currentUser.updatePassword(input.text.toString())
            })
            builder?.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder?.show()
        }

        binding.logOut.setOnClickListener {
            val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                ?.edit()
            prefs?.clear()
            prefs?.apply()

            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(activity, LoginActivity::class.java))
        }

        return(binding.root)
    }

}
