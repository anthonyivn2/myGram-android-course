package basics.mygram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Boolean show_login_page = true;
    Button button;
    TextView change_mode_text;
    Integer test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-8013161153238571~7977687595"); // Initialize adMob for our app, using sample ID for development
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        button = (Button) findViewById(R.id.btnMode);
        change_mode_text = (TextView) findViewById(R.id.tvChangeMode);
        parseInit();
        if(ParseUser.getCurrentUser() != null){
            goToHome();
        }
    }

    private void parseInit() {
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("9c344d161a974bb20f0cbb5e869a455e2bacb9ef")
                .server("http://ec2-52-14-11-143.us-east-2.compute.amazonaws.com:80/parse")
                .build()
        );
    }

    public void buttonClicked(View view){
//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("UserName");
//        ParseObject obj = new ParseObject("NameID");
//        obj.put("name", "Anthony");
//        obj.put("ID", "000001");
//        obj.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null){
//                    Toast.makeText(MainActivity.this, "DONE!", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(MainActivity.this, "FAILED!" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        if(show_login_page){
            login();
        }
        else if(!show_login_page){
            signUp();
        }


    }

    public void login(){
        ParseUser.logInInBackground(etUsername.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Toast.makeText(MainActivity.this, "DONE LOGIN AS - " + user.getUsername(), Toast.LENGTH_SHORT).show();
                    goToHome();
                }
                else{
                    Toast.makeText(MainActivity.this, "FAILED TO LOGIN AS -" + user.getUsername() + ", " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signUp(){
        ParseUser user = new ParseUser();
        user.setUsername(etUsername.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(MainActivity.this, "DONE!", Toast.LENGTH_SHORT).show();
                    goToHome();
                }
                else{
                    Toast.makeText(MainActivity.this, "FAILED!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void changeMode(View view){
        show_login_page = !show_login_page;
        if(show_login_page){
            button.setText("Login");
            change_mode_text.setText("Or signup");
        }
        else if(!show_login_page){
            button.setText("Signup");
            change_mode_text.setText("Or login");
        }
    }

    public void goToHome(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
}
