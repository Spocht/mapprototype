package dev.spocht.spocht.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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
        DataManager.getInstance();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void setContext () {
        DataManager.injectContext(this);

    }
}
