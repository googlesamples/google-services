package com.google.samples.quickstart.canonical

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


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
        Log.d(SIGN_IN_FRAGMENT_TAG, "googleSignInClientInit")
    }

    private fun firebaseAuthInit() {
        auth = FirebaseAuth.getInstance()
        Log.d(SIGN_IN_FRAGMENT_TAG, "firebaseAuthInit")
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(SIGN_IN_FRAGMENT_TAG, "firebaseAuthWithGoogle start")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Log.d(SIGN_IN_FRAGMENT_TAG, "firebaseAuthWithGoogle credential:$credential")

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                Log.d(SIGN_IN_FRAGMENT_TAG, "firebase firebaseAuthWithGoogle:task check")
                if (task.isSuccessful) {
                    // Firebase Sign in success, update UI with the signed-in user's information
                    Log.d(SIGN_IN_FRAGMENT_TAG, "firebase signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d(SIGN_IN_FRAGMENT_TAG, "firebase signed-in user's Email:" + user!!.email)
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    context.startActivity(intent)
                } else {
                    // If sign in fails, log a message to the user.
                    Log.w(SIGN_IN_FRAGMENT_TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun firebaseAuth(data: Intent?) : Int {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (task.isSuccessful) {
            return try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(SIGN_IN_FRAGMENT_TAG, "Google Sign In was successful:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                LoginFragment.FIREBASE_AUTH_WITH_GOOGLE_SUCCESSFUL
            } catch (e: ApiException) {
                // Google Sign In failed
                Log.w(SIGN_IN_FRAGMENT_TAG, "Google sign in failed", e)
                LoginFragment.FIREBASE_AUTH_WITH_GOOGLE_FAIL
            }
        } else {
            Log.w(SIGN_IN_FRAGMENT_TAG, "Google sign in unsuccessful")
            return LoginFragment.GOOGLE_SIGN_IN_UNSUCCESSFUL
        }
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

    fun isLogIn(): Boolean {
        return auth.currentUser != null
    }

    fun signOut(): Task<Void>? {
        firebaseSignOut()
        return googleSignOut()
    }

    companion object {
        const val SIGN_IN_FRAGMENT_TAG = "signInVM"
    }
}