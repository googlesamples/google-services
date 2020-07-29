package com.google.samples.quickstart.canonical

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

  private val signInVM : SignInViewModel by viewModels()

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
    Log.d(MAIN_ACTIVITY_TAG, "onCreate")
    signInVM.signInVMInit(this, this)
    setupNavigationBar()
  }

  override fun onStart() {
    super.onStart()

    when (signInVM.isLogIn()) {
      true -> {
        Log.d(MAIN_ACTIVITY_TAG, "Already login")
      }

      false -> {
        Log.d(MAIN_ACTIVITY_TAG, "No login. Update UI")
//        signInVM.signInAccountInit()
        findViewById<ConstraintLayout>(R.id.main_activity_view).removeAllViews()
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.main_activity_view, LoginFragment())
          .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
          .commit()
      }
    }
  }

  companion object {
    const val MAIN_ACTIVITY_TAG = "MainActivity"
  }

}