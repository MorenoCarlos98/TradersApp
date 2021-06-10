package com.carlosbogdan.tradersapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme)

        setContentView(R.layout.activity_register)

        //Analytics Event
        val analytics = FirebaseAnalytics.getInstance(this);
        val bundle = Bundle();
        bundle.putString("message", "Integracion OK")
        analytics.logEvent("InitScreen", bundle);

        //Setup
        setup()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)
    }

    private fun showHome(email: String?, name:String?){
        val homeIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("name", name)
        }
        startActivity(homeIntent)
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ERROR")
        builder.setMessage("WE HAVE HAD AN AUTH USER ERROR")
        builder.setPositiveButton("OK", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun setup() {

        this.btnLogRegister.setOnClickListener {
            onBackPressed()
        }

        this.button_sign.setOnClickListener {
                if (SignEmailEditText.text!!.isNotEmpty() && SignPasswordEditText.text!!.isNotEmpty()) {
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(SignEmailEditText.text.toString(),
                            SignPasswordEditText.text.toString()).addOnCompleteListener {
                            if (it.isSuccessful) {
                                var request: UserProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(NameEditText.text.toString()).build()

                                FirebaseAuth.getInstance().currentUser.updateProfile(request)

                                val data = hashMapOf(
                                    "coin_convert" to "USD"
                                )

                                Firebase.firestore.collection("user-prefs").document(SignEmailEditText.text.toString()).set(data)

                                showHome(SignEmailEditText.text.toString(), NameEditText.text.toString())
                            } else {
                                showAlert()
                            }
                        }
                }
            }

        this.googleButton.setOnClickListener{
            //Configuracion
            val googleConf =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            val googleClient = GoogleSignIn.getClient(applicationContext, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val data = hashMapOf(
                                "coin_convert" to "USD"
                            )
                            Firebase.firestore.collection("user-prefs").document(FirebaseAuth.getInstance().currentUser.email).set(data)

                            showHome(FirebaseAuth.getInstance().currentUser.email, FirebaseAuth.getInstance().currentUser.displayName)
                        } else {
                            showAlert()
                        }
                    }
                }
            } catch(e: ApiException) {
                showAlert()
            }
        }
    }
}
