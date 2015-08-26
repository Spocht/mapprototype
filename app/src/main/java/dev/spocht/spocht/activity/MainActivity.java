package dev.spocht.spocht.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import dev.spocht.spocht.R;

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

        startActivity(new Intent(this, LoginActivity.class));
    }
}
