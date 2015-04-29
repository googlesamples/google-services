package com.google.samples.quickstart.bannerexample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
// [SNIPPET load_banner_ad]
// Load an ad into the AdView.
// [START load_banner_ad]
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // [END load_banner_ad]
    }
}
