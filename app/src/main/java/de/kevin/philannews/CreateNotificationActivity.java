package de.kevin.philannews;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class CreateNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);
        ToggleButton topic = findViewById(R.id.notificationTopic);
        topic.setChecked(true);
        EditText message = findViewById(R.id.notificationMessage);
        message.setText("Es gibt Neuigkeiten!");
        Button sendNot = findViewById(R.id.sendNotification);

        sendNot.setOnClickListener(view -> {
            if (!NewsManager.isValidUser()) {
                finish();
                return;
            }
            NewsManager.sendNotification(topic.isChecked() ? "global" : "registered", message.getText().toString());
            finish();
        });
    }

}
