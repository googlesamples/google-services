/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.quickstart.analytics;

import java.util.Locale;

import android.support.v4.view.PagerTabStrip;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Activity which displays numerous background images that may be viewed. These background images
 * are shown via {@link PatternFragment}.
 */
public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";

  private static final PatternInfo[] PATTERN_INFOS = {
      new PatternInfo(R.drawable.angles, R.string.pattern1_title),
      new PatternInfo(R.drawable.blue, R.string.pattern2_title),
      new PatternInfo(R.drawable.dots, R.string.pattern3_title),
      new PatternInfo(R.drawable.lines, R.string.pattern4_title),
  };

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each pattern.
   * This uses a {@link FragmentPagerAdapter}, which keeps every loaded fragment in memory.
   */
  private PatternPagerAdapter mPatternPagerAdapter;

  /**
   * The {@link ViewPager} that will host the patterns.
   */
  private ViewPager mViewPager;

  /**
   * The {@link Tracker} used to record screen views.
   */
  private Tracker mTracker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // [START shared_tracker]
    // Obtain the shared Tracker instance.
    PatternApplication application = (PatternApplication) getApplication();
    mTracker = application.getDefaultTracker();
    // [END shared_tracker]

    // Create the adapter that will return a fragment for each pattern.
    mPatternPagerAdapter = new PatternPagerAdapter(getSupportFragmentManager(), PATTERN_INFOS);

    // Set up the ViewPager with the pattern adapter.
    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(mPatternPagerAdapter);

    // When the visible pattern changes, send a new visible event.
    mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        sendScreenPatternName();
      }
    });

    // Send initial screen event.
    sendScreenPatternName();
  }

  /**
   * Record a screen view event for the visible {@link PatternFragment} displayed
   * inside {@link FragmentPagerAdapter}.
   */
  private void sendScreenPatternName() {
    int position = mViewPager.getCurrentItem();

    PatternInfo info = PATTERN_INFOS[position];
    String name = getString(info.title);

    // [START screen_view_event]
    Log.i(TAG, "Setting screen name: " + name);
    mTracker.setScreenName("Pattern~" + name);
    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    // [END screen_view_event]
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class PatternPagerAdapter extends FragmentPagerAdapter {

    private final PatternInfo[] infos;

    public PatternPagerAdapter(FragmentManager fm, PatternInfo[] infos) {
      super(fm);
      this.infos = infos;
    }

    @Override
    public Fragment getItem(int position) {
      PatternInfo info = infos[position];
      return PatternFragment.newInstance(info.background);
    }

    @Override
    public int getCount() {
      return infos.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      if (position < 0 || position >= infos.length) {
        return null;
      }
      Locale l = Locale.getDefault();
      PatternInfo info = infos[position];
      return getString(info.title).toUpperCase(l);
    }
  }
}
