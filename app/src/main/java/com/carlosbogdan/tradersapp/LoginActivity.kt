package com.carlosbogdan.tradersapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.googleButton

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)
        setTheme(R.style.AppTheme)

        setContentView(R.layout.activity_login)

        //Analytics Event
        val analytics = FirebaseAnalytics.getInstance(this);
        val bundle = Bundle();
        bundle.putString("message", "Integracion OK")
        analytics.logEvent("InitScreen", bundle);

        //setup
        setup()
        session()
    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val name = prefs.getString("name", null)

        if (email != null) {
            showHome(email, name)
        }
    }

    private fun showHome(email: String?, name: String?){
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

        this.btnRegLogin.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left)
        }

        this.button_logOut.setOnClickListener{
            if (LogEmailEditText.text!!.isNotEmpty() && LogPasswordEditText.text!!.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(LogEmailEditText.text.toString(),
                        LogPasswordEditText.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful) {
                            showHome(LogEmailEditText.text.toString(), FirebaseAuth.getInstance().currentUser.displayName)
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
