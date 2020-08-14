package com.google.samples.quickstart.canonical

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignInViewModel : ViewModel() {
    // Have a feeling that it's not a good practice to store context or activity in viewmodel.
    // Maybe should just just pass in GoogleSignInClient from activity/fragment instead of
    // creating it in this viewmodel.
//    private lateinit var context: Context
//    private lateinit var activity: MainActivity
    private var authStateListenerForSignOut: FirebaseAuth.AuthStateListener? = null

    val curFirebaseUser: MutableLiveData<FirebaseUser> by lazy {
        MutableLiveData<FirebaseUser>(
            Firebase.auth.currentUser
        )
    }
    var showSignInFailedMessage: MutableLiveData<Boolean> = MutableLiveData(false)
    var showSignOutFailedMessage: MutableLiveData<Boolean> = MutableLiveData(false)

    private fun firebaseSignOutInit() {
        Log.d(SIGN_IN_VM_TAG, "firebaseSignOutInit")
        authStateListenerForSignOut = FirebaseAuth.AuthStateListener {
            it.currentUser ?: let {
                Log.w(SIGN_IN_VM_TAG, "firebaseSignOut Succeed")
                curFirebaseUser.value = null
            }
        }
        Firebase.auth.addAuthStateListener(authStateListenerForSignOut!!)
    }

    private fun createUserTask(user: FirebaseUser): Task<Void?> {
        val db = Firebase.firestore
        val dbFirebaseUser = hashMapOf(
            ProfileViewModel.KEY_USR_NAME to (user.displayName ?: ""),
            ProfileViewModel.KEY_USR_EMAIL to (user.email ?: ""),
            ProfileViewModel.KEY_TOTAL_DIS_M to 0L,
            ProfileViewModel.KEY_TOTAL_EN_CAL to 0L,
            ProfileViewModel.KEY_TOTAL_TIME_MS to 0L,
            ProfileViewModel.KEY_RUN_HISTORY to arrayListOf<HashMap<String, Any>>()
        )
        val ref = db.collection(ProfileViewModel.USER_COLLECTION_NAME).document(user.uid)
        return ref.get().onSuccessTask { document ->
            if (document!!.exists()) {
                Tasks.forResult(null)
            } else {
                ref.set(dbFirebaseUser)
            } as Task<Void?>
        }
            .addOnSuccessListener {
                curFirebaseUser.value = user
                showSignInFailedMessage.value = false
            }.addOnFailureListener {
                Log.w(SIGN_IN_VM_TAG, "createUserTask failed", it)
            }
    }

    private fun firebaseSignOut() {
        firebaseSignOutInit()
        Firebase.auth.signOut()
    }

    private fun firebaseAuthWithGoogle(idToken: String): Task<AuthResult> {
        // If we don't need the logging, we can just do
        // Firebase.auth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null)) in
        // onSuccessTask and remove this firebaseAuthWithGoogle method entirely.
        Log.d(SIGN_IN_VM_TAG, "firebaseAuthWithGoogle start")
        return Firebase.auth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
            // Can remove this listener if don't want to specifically log error for this task.
            .addOnFailureListener {
                Log.w(SIGN_IN_VM_TAG, "FirebaseAuth signInWithCredential failed", it)
            }
    }

    fun firebaseAuth(data: Intent?) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
            // We can remove this listener if we don't want to specifically log error for this task.
            .addOnFailureListener {
                Log.w(SIGN_IN_VM_TAG, "Google sign in unsuccessful", it)
            }
            .onSuccessTask { account ->
                Log.d(SIGN_IN_VM_TAG, "Google Sign In was successful ${account!!.id}")
                firebaseAuthWithGoogle(account.idToken!!)
            }
            .onSuccessTask { authResult ->
                createUserTask(authResult!!.user!!)
            }
            .addOnFailureListener {
                // Any of the previous tasks failed would propagate failure here. Setting
                // signInFailed status here, and let the view handle displaying login failed text.
                Log.w(SIGN_IN_VM_TAG, "Firebase Auth failed.", it)
                showSignInFailedMessage.value = true
            }
    }

    fun getFirebaseAuthCurUser(): FirebaseUser? {
        return curFirebaseUser.value
    }

    fun isLogIn(): Boolean {
        val loggedIn = curFirebaseUser.value == null
        Log.d(SIGN_IN_VM_TAG, "isLogIn(): $loggedIn")
        // If we don't need the logging, just
        // return curFirebaseUser.value == null
        return loggedIn
    }

    fun signOut(googleSignInClient: GoogleSignInClient) {
        // Converting to a task so that when task completes, we can set the status and the view can
        // handle the message accordingly.
        Tasks.whenAll(
            Tasks.forResult(firebaseSignOut()),
            googleSignInClient.signOut()
        )
            .addOnSuccessListener {
                showSignOutFailedMessage.value = false
            }
            .addOnFailureListener {
                Log.w(SIGN_IN_VM_TAG, "SignOut failed", it)
                showSignOutFailedMessage.value = true
            }
    }

    override fun onCleared() {
        super.onCleared()
        authStateListenerForSignOut?.let {
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListenerForSignOut!!)
        }
    }

    companion object {
        const val SIGN_IN_VM_TAG = "signInVM"
    }
}