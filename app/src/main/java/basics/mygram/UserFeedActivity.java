package basics.mygram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ParseObject> listParseObject;
    String username;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8013161153238571/2857222850");
        mInterstitialAd.loadAd(new AdRequest.Builder().build()); // Load the ad from website
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show(); // Show the ad after the ad has loaded
            }
        });
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Toast.makeText(this, username, Toast.LENGTH_LONG);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ParseQuery<ParseObject> query_data = new ParseQuery("Gambar");
        query_data.whereEqualTo("username", username);
        query_data.orderByDescending("createdAt");
        query_data.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    RecyclerViewAdapter adapter= new RecyclerViewAdapter(getApplicationContext(), objects);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
                else{
                    Toast.makeText(UserFeedActivity.this, "Fetch Error! " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
