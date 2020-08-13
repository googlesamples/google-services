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
    // Have a feeling that it's not a good practice to pass in context or activity to viewmodel.
    // Maybe should just just pass in GoogleSignInclient from activity/fragment instead of
    // creating it in this viewmodel.
    private lateinit var context: Context
    private lateinit var activity: MainActivity
    private var authStateListenerForSignOut: FirebaseAuth.AuthStateListener? = null

    val curFirebaseUser: MutableLiveData<FirebaseUser> by lazy {
        MutableLiveData<FirebaseUser>(
            Firebase.auth.currentUser
        )
    }

    private fun setResources(activityContext: Context, activityMain: MainActivity) {
        context = activityContext
        activity = activityMain
    }

    private fun signInFailureHandle() {
        Toast.makeText(context, context.getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
        signOut()
    }

    private fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        Log.d(SIGN_IN_VM_TAG, "googleSignInClientInit")
        return GoogleSignIn.getClient(activity, gso)
    }

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
            when (document!!.exists()) {
                true -> Tasks.forResult(null)
                false -> ref.set(dbFirebaseUser)
            } as Task<Void?>
        }
            .addOnSuccessListener {
                curFirebaseUser.value = user
            }.addOnFailureListener {
                Log.w(SIGN_IN_VM_TAG, "createUserTask failed", it)
            }
    }

    private fun googleSignOut() {
        getGoogleSignInClient().signOut()
            .addOnFailureListener {
                Log.w(SIGN_IN_VM_TAG, "googleSignOut Failed", it)
                Toast.makeText(
                    context,
                    context.getString(R.string.sign_out_failed),
                    Toast.LENGTH_SHORT
                ).show()
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
                Toast.makeText(
                    context,
                    context.getString(R.string.login_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .onSuccessTask { account ->
                Log.d(SIGN_IN_VM_TAG, "Google Sign In was successful ${account!!.id}")
                firebaseAuthWithGoogle(account.idToken!!)
            }
            .onSuccessTask { authResult ->
                createUserTask(authResult!!.user!!)
            }
            .addOnFailureListener {
                // Any of the previous tasks failed would propagate failure here, therefore we only
                // need to do signInFailureHandle() here.
                Log.w(SIGN_IN_VM_TAG, "Firebase Auth failed.", it)
                signInFailureHandle()
            }
    }

    fun getFirebaseAuthCurUser(): FirebaseUser? {
        return curFirebaseUser.value
    }

    fun getSignInIntent(): Intent {
        return getGoogleSignInClient().signInIntent
    }

    fun signInVMInit(activityContext: Context, activityMain: MainActivity) {
        Log.d(SIGN_IN_VM_TAG, "signInInit")
        setResources(activityContext, activityMain)
    }

    fun isLogIn(): Boolean {
        val loggedIn = curFirebaseUser.value == null
        Log.d(SIGN_IN_VM_TAG, "isLogIn(): $loggedIn")
        // If we don't need the logging, just
        // return curFirebaseUser.value == null
        return loggedIn
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
    }
}