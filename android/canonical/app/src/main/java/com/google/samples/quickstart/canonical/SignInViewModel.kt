package com.google.samples.quickstart.canonical

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignInViewModel : ViewModel() {
    data class CurFirebaseUser(
        var firebaseUser: MutableLiveData<FirebaseUser?> = MutableLiveData(FirebaseAuth.getInstance().currentUser),
        var isLogin: MutableLiveData<Boolean> = MutableLiveData(firebaseUser.value?.let { true } ?: run{ false })
    )

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var context: Context
    private lateinit var activity: MainActivity
    private var authStateListenerForSignOut : FirebaseAuth.AuthStateListener? = null
    private lateinit var curFirebaseUser : MutableLiveData<CurFirebaseUser>


    private fun setResources(activityContext: Context, activityMain: MainActivity) {
        context = activityContext
        activity = activityMain
    }

    private fun signInFailureHandle() {
        Toast.makeText(context, context.getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
        signOut()
    }

    private fun setCurFirebaseUser(firebaseUser: FirebaseUser) {
        curFirebaseUser.value!!.firebaseUser.value = firebaseUser
        curFirebaseUser.value!!.isLogin.value = true
    }

    private fun googleSignInInit() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity, gso)
        Log.d(SIGN_IN_VM_TAG, "googleSignInClientInit")
    }

    private fun firebaseSignOutInit() {
        Log.d(SIGN_IN_VM_TAG, "firebaseSignOutInit")
        authStateListenerForSignOut = FirebaseAuth.AuthStateListener {
            it.currentUser ?. let {
                Log.w(SIGN_IN_VM_TAG, "firebaseSignOut Failed")
                Toast.makeText(context, context.getString(R.string.sign_out_failed), Toast.LENGTH_SHORT).show()
            } ?: run {
                Log.w(SIGN_IN_VM_TAG, "firebaseSignOut Succeed")
                curFirebaseUser.value!!.firebaseUser.value = null
                curFirebaseUser.value!!.isLogin.value = false
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener(authStateListenerForSignOut!!)
    }

    private fun createUser(firebaseUser : FirebaseUser) {
        val db = Firebase.firestore
        val dbFirebaseUser = hashMapOf(
            ProfileViewModel.KEY_USR_NAME to (firebaseUser.displayName ?: ""),
            ProfileViewModel.KEY_USR_EMAIL to (firebaseUser.email ?: ""),
            ProfileViewModel.KEY_TOTAL_DIS_M to 0L,
            ProfileViewModel.KEY_TOTAL_EN_CAL to 0L,
            ProfileViewModel.KEY_TOTAL_TIME_MS to 0L,
            ProfileViewModel.KEY_RUN_HISTORY to arrayListOf<HashMap<String, Any>>()
        )
        val ref = db.collection(ProfileViewModel.USER_COLLECTION_NAME).document(firebaseUser.uid)
        ref.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d(SIGN_IN_VM_TAG, "User already exist with ID: ${document.id}")
                    setCurFirebaseUser(firebaseUser)
                } else {
                    // New user
                    ref.set(dbFirebaseUser)
                        .addOnSuccessListener {
                            setCurFirebaseUser(firebaseUser)
                            Log.d(SIGN_IN_VM_TAG, "Create user with ID: ${ref.id}")
                        }
                        .addOnFailureListener {
                            signInFailureHandle()
                            Log.w(SIGN_IN_VM_TAG, "Error adding new user")
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w(SIGN_IN_VM_TAG, "Error getting user info", e)
                signInFailureHandle()
            }
    }

    private fun googleSignOut() {
        googleSignInInit()
        googleSignInClient.signOut()
            .addOnFailureListener {
                Log.w(SIGN_IN_VM_TAG, "googleSignOut Failed")
                Toast.makeText(context, context.getString(R.string.sign_out_failed), Toast.LENGTH_SHORT).show()
            }
    }

    private fun firebaseSignOut() {
        firebaseSignOutInit()
        FirebaseAuth.getInstance().signOut()
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(SIGN_IN_VM_TAG, "firebaseAuthWithGoogle start")
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                Log.d(SIGN_IN_VM_TAG, "firebase firebaseAuthWithGoogle:task check")
                if (task.isSuccessful) {
                    // Firebase Sign in success, update UI with the signed-in user's information
                    Log.d(SIGN_IN_VM_TAG, "firebase signInWithCredential:success")
                    Log.d(SIGN_IN_VM_TAG, "firebase signed-in user's Email:" + auth.currentUser!!.email)
                    createUser(auth.currentUser!!)
                } else {
                    // If sign in fails, log a message to the user.
                    signInFailureHandle()
                    Log.w(SIGN_IN_VM_TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun firebaseAuth(data: Intent?) : Int {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (task.isSuccessful) {
            return try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(SIGN_IN_VM_TAG, "Google Sign In was successful:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                FIREBASE_AUTH_WITH_GOOGLE_SUCCESSFUL
            } catch (e: ApiException) {
                // Google Sign In failed
                Log.w(SIGN_IN_VM_TAG, "Google sign in failed", e)
                FIREBASE_AUTH_WITH_GOOGLE_FAIL
            }
        } else {
            Toast.makeText(context, context.getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            Log.w(SIGN_IN_VM_TAG, "Google sign in unsuccessful")
            signInFailureHandle()
            return GOOGLE_SIGN_IN_UNSUCCESSFUL
        }
    }

    fun getFirebaseAuthLogStatusLiveData(): MutableLiveData<Boolean> {
        return curFirebaseUser.value!!.isLogin
    }

    fun getFirebaseAuthCurUser(): FirebaseUser? {
        return curFirebaseUser.value!!.firebaseUser.value
    }

    fun getSignInIntent(): Intent {
        googleSignInInit()
        return googleSignInClient.signInIntent
    }

    fun signInVMInit(activityContext: Context, activityMain: MainActivity) {
        Log.d(SIGN_IN_VM_TAG, "signInInit")
        setResources(activityContext, activityMain)
        curFirebaseUser = MutableLiveData(CurFirebaseUser())
    }

    fun isLogIn(): Boolean {
        Log.d(SIGN_IN_VM_TAG, "isLogIn():"+curFirebaseUser.value!!.isLogin.value.toString())
        return curFirebaseUser.value!!.isLogin.value!!
    }

    fun signOut() {
        firebaseSignOut()
        googleSignOut()
    }

    override fun onCleared() {
        super.onCleared()
        authStateListenerForSignOut?.let {
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListenerForSignOut!!)
        }
    }

    companion object {
        const val SIGN_IN_VM_TAG = "signInVM"
        const val GOOGLE_SIGN_IN_UNSUCCESSFUL = 1
        const val FIREBASE_AUTH_WITH_GOOGLE_SUCCESSFUL = 2
        const val FIREBASE_AUTH_WITH_GOOGLE_FAIL = 3
    }
}