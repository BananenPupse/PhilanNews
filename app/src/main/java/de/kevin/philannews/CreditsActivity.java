package de.kevin.philannews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getAction() != null && getIntent().getAction().equals("loadApp")){
            setContentView(R.layout.activity_loading);
            Executors.newSingleThreadScheduledExecutor().schedule(this::finish, 5, TimeUnit.SECONDS);
        } else {
            setContentView(R.layout.activity_credits);
            TextView version = findViewById(R.id.creditsAppVersion);
            version.setText(String.format("Version %s", BuildConfig.VERSION_NAME));
            Button button = findViewById(R.id.creditsProblems);
            button.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://philan.de/du-hast-probleme-mit-der-app/"));
                startActivity(intent);
            });
        }
    }
}
