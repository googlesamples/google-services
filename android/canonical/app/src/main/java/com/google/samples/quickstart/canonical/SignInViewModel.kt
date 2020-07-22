package com.google.samples.quickstart.canonical

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

private const val tag = "signInVM"

class SignInViewModel : ViewModel() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context
    private lateinit var activity: MainActivity

    private fun googleSignOut(): Task<Void>? {
        return googleSignInClient.signOut()
    }

    private fun firebaseSignOut() {
        FirebaseAuth.getInstance().signOut()
    }

    private fun googleSignInInit() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity, gso)
        Log.d(tag, "googleSignInClientInit")
    }

    private fun firebaseAuthInit() {
        auth = FirebaseAuth.getInstance()
        Log.d(tag, "firebaseAuthInit")
    }

    fun getGoogleSignInClient(): GoogleSignInClient {
        return googleSignInClient
    }

    fun getFirebaseAuth(): FirebaseAuth {
        return auth
    }

    private fun setResources(activityContext: Context, activityMain: MainActivity) {
        context = activityContext
        activity = activityMain
    }

    fun signInInit(activityContext: Context, activityMain: MainActivity) {
        setResources(activityContext, activityMain)
        googleSignInInit()
        firebaseAuthInit()
    }

    fun signOut(): Task<Void>? {
        firebaseSignOut()
        return googleSignOut()
    }
}