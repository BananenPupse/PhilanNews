package de.kevin.philannews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class CreateNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);
        EditText title = findViewById(R.id.newPassword);
        EditText link = findViewById(R.id.newLink);
        EditText content = findViewById(R.id.newContent);
        Button newNews = findViewById(R.id.newButton);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (title.getText().toString().isEmpty() || link.getText().toString().isEmpty() || content.getText().toString().isEmpty()) {
                    newNews.setEnabled(false);
                } else {
                    newNews.setEnabled(true);
                }
            }
        };
        title.addTextChangedListener(textWatcher);
        link.addTextChangedListener(textWatcher);
        content.addTextChangedListener(textWatcher);

        newNews.setOnClickListener(v -> {
            if (!NewsManager.isValidUser()) {
                finish();
                return;
            }
            NewsManager.createNews(title.getText().toString(), link.getText().toString(), content.getText().toString().replace("\r\n", "%nl%").replace("\n", "%nl%").replace("\r", ""));
            finish();
        });
    }

}
