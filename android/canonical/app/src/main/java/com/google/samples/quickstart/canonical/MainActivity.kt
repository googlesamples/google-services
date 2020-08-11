package com.google.samples.quickstart.canonical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

class MainActivity : AppCompatActivity() {

  private val signInVM : SignInViewModel by viewModels()
  private val profileVM : ProfileViewModel by viewModels()

  private fun setupNavigationBar() {
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.fragment_container, RunFragment())
      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
      .commit()

    val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation_view)
    bottomNavigation.setOnNavigationItemSelectedListener { item ->
      try {
        when (item.itemId) {

          R.id.bottom_navigation_item_run -> {
            if (!bottomNavigation.menu.findItem(R.id.bottom_navigation_item_run).isChecked) {
              supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, RunFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
            }
            true
          }

          R.id.bottom_navigation_item_map -> {
            if (!bottomNavigation.menu.findItem(R.id.bottom_navigation_item_map).isChecked) {
              supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, MapsFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
            }
            true
          }

          R.id.bottom_navigation_item_profile -> {
            if (!bottomNavigation.menu.findItem(R.id.bottom_navigation_item_profile).isChecked) {
              supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
            }
            true
          }

          else -> false
        }
      } catch (e:Exception) {
        Log.e(MAIN_ACTIVITY_TAG, "setOnNavigationItemSelectedListener failed", e)
        false
      }
    }
  }

  private fun logoutUserObserver() {
    val observer = Observer<Boolean> {
      when (it) {
        true -> {
          Log.d("Profile", "firebaseUser is not null")
        }

        false -> {
          Log.d("Profile", "firebaseUser is null")
          val intent = Intent(this, MainActivity::class.java)
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
          startActivity(intent)
        }
      }
    }
    // set LifeCycle owner with MainActivity. Observe will be destroyed when MainActivity is destroyed
    signInVM.getFirebaseAuthLogStatusLiveData().observe(this, observer)
  }

  private fun checkLogin() {
    when (signInVM.isLogIn()) {
      true -> {
        Log.d(MAIN_ACTIVITY_TAG, "Already login")
        setupNavigationBar()
        // Init Profile
        profileVM.initAppUser(signInVM.getFirebaseAuthCurUser()!!)
        logoutUserObserver()
      }

      false -> {
        Log.d(MAIN_ACTIVITY_TAG, "No login. Update UI")
        findViewById<ConstraintLayout>(R.id.main_activity_view).removeAllViews()
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.main_activity_view, LoginFragment())
          .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
          .commit()
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    Log.d(MAIN_ACTIVITY_TAG, "onCreate")
    signInVM.signInVMInit(this, this)
    checkLogin()
  }

  companion object {
    const val MAIN_ACTIVITY_TAG = "MainActivity"
  }

}