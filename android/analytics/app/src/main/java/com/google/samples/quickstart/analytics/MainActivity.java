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

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Activity which displays numerous background images that may be viewed. These background images
 * are shown via {@link ImageFragment}.
 */
public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";

  private static final ImageInfo[] IMAGE_INFOS = {
      new ImageInfo(R.drawable.favorite, R.string.pattern1_title),
      new ImageInfo(R.drawable.flash, R.string.pattern2_title),
      new ImageInfo(R.drawable.face, R.string.pattern3_title),
      new ImageInfo(R.drawable.whitebalance, R.string.pattern4_title),
  };

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each image.
   * This uses a {@link FragmentPagerAdapter}, which keeps every loaded fragment in memory.
   */
  private ImagePagerAdapter mImagePagerAdapter;

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
    AnalyticsApplication application = (AnalyticsApplication) getApplication();
    mTracker = application.getDefaultTracker();
    // [END shared_tracker]

    // Create the adapter that will return a fragment for each image.
    mImagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), IMAGE_INFOS);

    // Set up the ViewPager with the pattern adapter.
    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(mImagePagerAdapter);

    // When the visible image changes, send a screen view hit.
    mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        sendScreenImageName();
      }
    });

    // Send initial screen screen view hit.
    sendScreenImageName();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.menu_share:
        // [START custom_event]
        mTracker.send(new HitBuilders.EventBuilder()
            .setCategory("Action")
            .setAction("Share")
            .build());
        // [END custom_event]

        String name = getCurrentImageTitle();
        String text = "I'd love you to hear about " + name;

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        break;
    }
    return false;
  }

  /**
   * Return the title of the currently displayed image.
   * @return title of image
   */
  private String getCurrentImageTitle() {
    int position = mViewPager.getCurrentItem();
    ImageInfo info = IMAGE_INFOS[position];
    return getString(info.title);
  }

  /**
   * Record a screen view hit for the visible {@link ImageFragment} displayed
   * inside {@link FragmentPagerAdapter}.
   */
  private void sendScreenImageName() {
    String name = getCurrentImageTitle();

    // [START screen_view_hit]
    Log.i(TAG, "Setting screen name: " + name);
    mTracker.setScreenName("Image~" + name);
    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    // [END screen_view_hit]
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class ImagePagerAdapter extends FragmentPagerAdapter {

    private final ImageInfo[] infos;

    public ImagePagerAdapter(FragmentManager fm, ImageInfo[] infos) {
      super(fm);
      this.infos = infos;
    }

    @Override
    public Fragment getItem(int position) {
      ImageInfo info = infos[position];
      return ImageFragment.newInstance(info.image);
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
      ImageInfo info = infos[position];
      return getString(info.title).toUpperCase(l);
    }
  }
}
