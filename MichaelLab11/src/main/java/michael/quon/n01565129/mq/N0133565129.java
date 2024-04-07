// Michael Quon N01565129
package michael.quon.n01565129.mq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class N0133565129 extends Fragment {

    private WebView webView;
    private int adClickCounter = 0;
    private SharedPreferences preferences;

    public N0133565129() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_n0133565129, container, false);

        webView = rootView.findViewById(R.id.Mic_webView);
        Spinner spinner = rootView.findViewById(R.id.Mic_cityspinner);

        preferences = requireContext().getSharedPreferences(getString(R.string.saved), Context.MODE_PRIVATE);

        AdView adView = rootView.findViewById(R.id.Mic_adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                adClickCounter++;
                preferences.edit().putInt(getString(R.string.adclickcounter), adClickCounter).apply();
                Toast.makeText(requireContext(), getString(R.string.name_id) + adClickCounter, Toast.LENGTH_LONG).show();
            }
        });

        // Initialize Mobile Ads SDK
        MobileAds.initialize(requireContext(), initializationStatus -> {
            // Mobile Ads SDK initialization completed
        });

        // Load ad into AdView
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Populate spinner with websites
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.website_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        webView.loadUrl("file:///android_res/raw/lab11.html");

        // Handle spinner item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedWebsite = parent.getItemAtPosition(position).toString();
                loadWebsite(selectedWebsite);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Handle WebView link clicks
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Open links in external browser
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
        });

        return rootView;
    }

    private void loadWebsite(String website) {
        switch (website) {
            case "Select URL":
                webView.loadUrl("file:///android_res/raw/lab11.html");
                break;
            case "CTV News":
                webView.loadUrl("https://toronto.ctvnews.ca/");
                break;
            case "NBA":
                webView.loadUrl("https://www.nba.com/news");
                break;
            case "Weather Network":
                webView.loadUrl("https://www.theweathernetwork.com/ca/weather/ontario/toronto");
                break;
        }
    }
}
