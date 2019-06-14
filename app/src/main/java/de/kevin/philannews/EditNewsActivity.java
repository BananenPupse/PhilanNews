package de.kevin.philannews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class EditNewsActivity extends AppCompatActivity {

    public static String ID, TITLE, LINK, CONTENT;

    public static void setData(String id, String title, String link, String content) {
        EditNewsActivity.ID = id;
        EditNewsActivity.TITLE = title;
        EditNewsActivity.LINK  = link;
        EditNewsActivity.CONTENT = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);
        EditText title = findViewById(R.id.editTitle);
        EditText link = findViewById(R.id.editLink);
        EditText content = findViewById(R.id.editContent);
        title.setText(TITLE);
        link.setText(LINK);
        content.setText(CONTENT);
        Button editNews = findViewById(R.id.editNewsButton);
        Button deleteNews = findViewById(R.id.buttonDelete);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (title.getText().toString().isEmpty() || link.getText().toString().isEmpty() || content.getText().toString().isEmpty()) {
                    editNews.setEnabled(false);
                } else {
                    editNews.setEnabled(true);
                }
            }
        };
        title.addTextChangedListener(textWatcher);
        link.addTextChangedListener(textWatcher);
        content.addTextChangedListener(textWatcher);

        editNews.setOnClickListener(v -> {
            if (!NewsManager.isValidUser()) {
                finish();
                return;
            }
            NewsManager.editNews(ID, title.getText().toString(), link.getText().toString(), content.getText().toString().replace("\r\n", "%nl%").replace("\n", "%nl%").replace("\r", ""));
            finish();
        });

        deleteNews.setOnClickListener(v -> {
            if (!NewsManager.isValidUser()) {
                finish();
                return;
            }
            NewsManager.deleteNews(ID);
            finish();
        });

    }
}
