package de.kevin.philannews;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);

        Switch notifySwitch = findViewById(R.id.notifySwitch);
        notifySwitch.setChecked(preferences.getBoolean("notify", false));
        notifySwitch.setOnClickListener(v -> {
            if (notifySwitch.isChecked()) {
                preferences.edit().putBoolean("notify", true).apply();
                Toast.makeText(this, "Du möchtest bei neuen News benachrichtigt werden :)", Toast.LENGTH_LONG).show();
            } else {
                preferences.edit().putBoolean("notify", false).apply();
                Toast.makeText(this, "Du möchtest bei neuen News nicht benachrichtigt werden :(", Toast.LENGTH_LONG).show();
            }
            NewsActivity.getNewsActivity().updateSubscription(preferences);
        });
    }
}
