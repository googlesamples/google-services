package com.google.samples.quickstart.canonical

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jetbrains.annotations.Nullable

class MainActivity : AppCompatActivity() {

  lateinit var runFragment: RunFragment
  lateinit var mapsFragment: MapsFragment
  lateinit var meFragment: MeFragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    runFragment = RunFragment()
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.fragment_layout, runFragment)
      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
      .commit()

    val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation_view)
    bottomNavigation.setOnNavigationItemSelectedListener { item ->
      when (item.itemId) {

        R.id.bottom_navigation_item_run -> {
          runFragment = RunFragment()
          supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_layout, runFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        }

        R.id.bottom_navigation_item_map -> {
          mapsFragment = MapsFragment()
          supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_layout, mapsFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        }

        R.id.bottom_navigation_item_profile -> {
          meFragment = MeFragment()
          supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_layout, meFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        }


      }
      true

    }
  }



}