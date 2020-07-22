package com.google.samples.quickstart.canonical

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

  private lateinit var signInVM : SignInViewModel


  private fun setupNavigationBar() {
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.fragment_container, RunFragment())
      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
      .commit()

    val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation_view)
    bottomNavigation.setOnNavigationItemSelectedListener { item ->
      when (item.itemId) {

        R.id.bottom_navigation_item_run -> {
          supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, RunFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
          true
        }

        R.id.bottom_navigation_item_map -> {
          supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, MapsFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
          true
        }

        R.id.bottom_navigation_item_profile -> {
          supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, MeFragment())
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

    signInVM = ViewModelProviders.of(this)[SignInViewModel::class.java]
    signInVM.signInInit(this, this)

    setupNavigationBar()
  }

  override fun onStart() {
    super.onStart()

    val account = GoogleSignIn.getLastSignedInAccount(this)
    account?.let {
      Log.d(TAG, "Already login")
    } ?: run {
      Log.d(TAG, "No login. Update UI")
      findViewById<ConstraintLayout>(R.id.main_activity_view).removeAllViews()

      supportFragmentManager
        .beginTransaction()
        .replace(R.id.main_activity_view, LoginFragment())
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .commit()
    }
  }


  companion object {
    private const val TAG = "MainActivity"
  }

}