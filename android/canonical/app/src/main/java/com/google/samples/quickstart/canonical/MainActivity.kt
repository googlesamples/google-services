package com.google.samples.quickstart.canonical

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StopwatchView : ViewModel(){

  var pauseOffset = MutableLiveData<Long>(0L)
  var isWorking = MutableLiveData<Boolean>(false)
  var fragmentPauseStartTime = MutableLiveData<Long>(0L)

  fun setPauseOffset(pause_offset_value:Long){
    pauseOffset.value = pause_offset_value
  }

  fun setWorkingStatus(working_status:Boolean){
    isWorking.value = working_status
  }

  fun setFragmentPauseStartTime(fragment_pause_start_time:Long){
    fragmentPauseStartTime.value = fragment_pause_start_time
  }
}

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