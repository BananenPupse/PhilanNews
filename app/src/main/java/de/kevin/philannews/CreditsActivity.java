package de.kevin.philannews;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getAction() != null && getIntent().getAction().equals("loadApp")){
            setContentView(R.layout.activity_loading);
            Executors.newSingleThreadScheduledExecutor().schedule(() -> finish(), 5, TimeUnit.SECONDS);
        } else {
            setContentView(R.layout.activity_credits);
        }
    }
}
