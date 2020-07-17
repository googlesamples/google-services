package com.google.samples.quickstart.canonical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
  lateinit var mGoogleSignInClient: GoogleSignInClient
  private lateinit var auth: FirebaseAuth
  private val tag = "MainActivity-Login"
  private val firebaseTag = "MainActivity-Login"

  private fun googleSignInInit() {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .build()

    mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
  }

  private fun signIn() {
    auth = FirebaseAuth.getInstance()
    val signInIntent = mGoogleSignInClient.signInIntent
    startActivityForResult(signInIntent, RC_SIGN_IN)
  }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)
      if (task.isSuccessful) {
        try {
          // Google Sign In was successful, authenticate with Firebase
          val account = task.getResult(ApiException::class.java)!!
          Log.d(tag, "firebaseAuthWithGoogle:" + account.id)
          firebaseAuthWithGoogle(account.idToken!!)
          setContentView(R.layout.activity_main)
          setupNavigationBar()
        } catch (e: ApiException) {
          // Google Sign In failed
          Log.w(tag, "Google sign in failed", e)
        }
      } else {
        Log.w(tag, "Google sign in unsuccessful")
      }

    }
    // No other requestCode, ignore it.
  }


  private fun firebaseAuthWithGoogle(idToken: String) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
      .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
          // Firebase Sign in success, update UI with the signed-in user's information
          Log.d(firebaseTag, "signInWithCredential:success")
          val user = auth.currentUser
          Log.d(firebaseTag, "signed-in user's Email:" + user!!.email)
        } else {
          // If sign in fails, log a message to the user.
          Log.w(firebaseTag, "signInWithCredential:failure", task.exception)
        }
      }
  }


  private fun setupNavigationBar() {
    val runFragment = RunFragment()
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.fragment_container, runFragment)
      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
      .commit()

    val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation_view)
    bottomNavigation.setOnNavigationItemSelectedListener { item ->
      when (item.itemId) {

        R.id.bottom_navigation_item_run -> {
          val runFragment = RunFragment()
          supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, runFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
          true
        }

        R.id.bottom_navigation_item_map -> {
          val mapsFragment = MapsFragment()
          supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, mapsFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
          true
        }

        R.id.bottom_navigation_item_profile -> {
          val meFragment = MeFragment()
          supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, meFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
          true
        }

        else -> false
      }
    }
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    googleSignInInit()
    setupNavigationBar()

  }


  override fun onStart() {
    super.onStart()
    val account = GoogleSignIn.getLastSignedInAccount(this)
    if (account == null) {
      setContentView(R.layout.login_in_page)
      findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
        signIn()
      }
    }
  }


  companion object {
    private const val RC_SIGN_IN = 0
  }


}