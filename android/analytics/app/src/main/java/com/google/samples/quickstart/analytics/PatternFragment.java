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


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A fragment that just displays a background image as a pattern.
 */
public class PatternFragment extends Fragment {
  private static final String ARG_PATTERN = "pattern";

  private int resId;

  /**
   * Create a {@link PatternFragment} displaying the given pattern.
   *
   * @param resId to display as the pattern background
   * @return a new instance of {@link PatternFragment}
   */
  public static PatternFragment newInstance(int resId) {
    PatternFragment fragment = new PatternFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_PATTERN, resId);
    fragment.setArguments(args);
    return fragment;
  }

  public PatternFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      resId = getArguments().getInt(ARG_PATTERN);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    // Generate a repeated tile from the source bitmap.
    Bitmap b = BitmapFactory.decodeResource(getResources(), resId);
    BitmapDrawable drawable = new BitmapDrawable(getResources(), b);
    drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

    ImageView view = new ImageView(getActivity());
    // Set background, as the image drawable doesn't respect tileMode. Use the deprecated call
    // as this app needs to support API 9+.
    view.setBackgroundDrawable(drawable);
    return view;
  }

}
