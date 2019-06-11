package de.kevin.philannews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getAction() != null && getIntent().getAction().equals("loadApp")){
            setContentView(R.layout.activity_loading);
            ImageView imageView = findViewById(R.id.logo);
            Executors.newSingleThreadScheduledExecutor().schedule(() -> finish(), 5, TimeUnit.SECONDS);
        } else {
            setContentView(R.layout.activity_credits);
        }
    }
}
