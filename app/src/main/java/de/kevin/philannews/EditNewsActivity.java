package de.kevin.philannews;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class EditNewsActivity extends AppCompatActivity {

    public static String ID, TITLE, SUMMARY, CONTENT;

    public static void setData(String id, String title, String summary, String content) {
        EditNewsActivity.ID = id;
        EditNewsActivity.TITLE = title;
        EditNewsActivity.SUMMARY = summary;
        EditNewsActivity.CONTENT = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);
        EditText title = findViewById(R.id.editTitle);
        EditText summary = findViewById(R.id.editSummary);
        EditText content = findViewById(R.id.editContent);
        title.setText(TITLE);
        summary.setText(SUMMARY);
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
                if (title.getText().toString().isEmpty() || content.getText().toString().isEmpty()) {
                    editNews.setEnabled(false);
                } else {
                    editNews.setEnabled(true);
                }
            }
        };
        title.addTextChangedListener(textWatcher);
        content.addTextChangedListener(textWatcher);

        editNews.setOnClickListener(v -> {
            if (!NewsManager.isValidUser()) {
                finish();
                return;
            }
            NewsManager.editNews(ID, title.getText().toString(), summary.getText().toString().isEmpty() ? "NULL" : summary.getText().toString(), content.getText().toString().replace("\r\n", "%nl%").replace("\n", "%nl%").replace("\r", ""));
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
