package com.google.samples.quickstart.canonical

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.SignInButton

class LoginFragment : Fragment() {

    private val signInVM: SignInViewModel by activityViewModels()

    private fun signIn() {
        val signInIntent = signInVM.getGoogleSignInClient().signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            when (signInVM.firebaseAuth(data)) {
                FIREBASE_AUTH_WITH_GOOGLE_SUCCESSFUL -> {
                    Log.i(LOGIN_FRAGMENT_TAG, "Google sign in successful")
                }

                FIREBASE_AUTH_WITH_GOOGLE_FAIL -> {
                    Log.w(LOGIN_FRAGMENT_TAG, "Google sign in failed")
                }

                GOOGLE_SIGN_IN_UNSUCCESSFUL -> {
                    Log.w(LOGIN_FRAGMENT_TAG, "Google sign in unsuccessful")
                }
            }
        }
        // No other requestCode, ignore it.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
        const val GOOGLE_SIGN_IN_UNSUCCESSFUL = 1
        const val FIREBASE_AUTH_WITH_GOOGLE_SUCCESSFUL = 2
        const val FIREBASE_AUTH_WITH_GOOGLE_FAIL = 3
        const val LOGIN_FRAGMENT_TAG = "Login fragment"
    }
}