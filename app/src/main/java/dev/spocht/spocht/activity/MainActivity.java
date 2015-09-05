package dev.spocht.spocht.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import dev.spocht.spocht.R;
import dev.spocht.spocht.data.DataManager;

public class MainActivity extends FragmentActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            // no exception should be thrown
        }

        setContext();
        //"Let there be light", spoke god... but he meant
        //"Let there be a DataManager".
        if (DataManager.getInstance().isAnon()) {
            Log.d("spocht.mainActivity","Login anonymously ");
            // If user is anonymous, send the user to LoginSignupActivity.class
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com
            if (DataManager.getInstance().isLoggedIn()) {
                Log.d("spocht.mainActivity","Login again");
                // Send logged in users to Welcome.class
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.d("spocht.mainActivity","Login new user");
                // Send user to LoginSignupActivity.class
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void setContext () {
        DataManager.injectContext(this);

    }
}
