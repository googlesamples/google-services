package com.google.samples.quickstart.canonical

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

  private lateinit var runFragment: RunFragment
  private lateinit var mapsFragment: MapsFragment
  private lateinit var meFragment: MeFragment


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    runFragment = RunFragment()
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.fragment_container, runFragment)
      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
      .commit()

    val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation_view)
    bottomNavigation.setOnNavigationItemSelectedListener { item ->
      when (item.itemId) {

        R.id.bottom_navigation_item_run -> {
          runFragment = RunFragment()
          supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, runFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
          true
        }

        R.id.bottom_navigation_item_map -> {
          mapsFragment = MapsFragment()
          supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, mapsFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
          true
        }

        R.id.bottom_navigation_item_profile -> {
          meFragment = MeFragment()
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



}