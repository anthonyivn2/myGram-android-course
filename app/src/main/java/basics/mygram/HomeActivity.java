package basics.mygram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private AdView mAdView;
    Boolean clicked = false;
    Long time_clicked;
    int duration = 2500;

    ListView lvUser;
    List<String> userList;
    ArrayAdapter usernameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        lvUser = (ListView) findViewById(R.id.lv_usernames);
        userList = new ArrayList<>();
        usernameAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, userList);
        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), UserFeedActivity.class);
                intent.putExtra("username", userList.get(i));
                startActivity(intent);
            }
        });

        ParseQuery<ParseUser>  query_data = ParseUser.getQuery();
        query_data.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query_data.orderByAscending("username");
        query_data.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null){
                    for(ParseUser user: objects){
                        userList.add(user.getUsername());
                    }
                    lvUser.setAdapter(usernameAdapter);
                    Toast.makeText(HomeActivity.this, "Total User: " + objects.size(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(HomeActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Toast.makeText(HomeActivity.this, "Ad Closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Toast.makeText(HomeActivity.this, "Ad Failed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Toast.makeText(HomeActivity.this, "Leaving Ad!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Toast.makeText(HomeActivity.this, "Ad Opened!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(HomeActivity.this, "Ad Loaded!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed(); //Commenting this disabled the back button
        if(clicked){
            if((System.currentTimeMillis() - time_clicked) < duration){
                super.onBackPressed();
            }
            else{
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                time_clicked = System.currentTimeMillis();
            }
        }
        else{
            clicked = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            time_clicked = System.currentTimeMillis();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_upload){
            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menu_logout) {
            ParseUser.logOut();
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
