package com.google.samples.quickstart.canonical

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton

class LoginFragment : Fragment() {

    private val signInVM: SignInViewModel by activityViewModels()

    private fun signIn() {
        context?.let {
            val googleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context?.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .requestProfile()
                    .build()
            val signInIntent = GoogleSignIn.getClient(it, googleSignInOptions).signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            signInVM.firebaseAuth(data)
        }
        // No other requestCode, ignore it.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add login status change listener
        signInVM.curFirebaseUser.observe(this, Observer {
            if (it != null) {
                Log.d(LOGIN_FRAGMENT_TAG, "firebaseUser is not null")
                // Start main activity
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else {
                Log.d(LOGIN_FRAGMENT_TAG, "firebaseUser is null")
            }
        })
        signInVM.showSignInFailedMessage.observe(this, Observer { signInFailed ->
            if (signInFailed) {
                context?.let {
                    Toast.makeText(it, it.getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
            signIn()
        }
    }

    companion object {
        const val RC_SIGN_IN = 0
        const val LOGIN_FRAGMENT_TAG = "Login fragment"
    }
}