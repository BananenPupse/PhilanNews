package de.kevin.philannews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Objects;

public class NewsViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels, height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*.9), (int) (height*.8));

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.x = 0;
        layoutParams.y = -20;

        getWindow().setAttributes(layoutParams);

        Objects.requireNonNull(getSupportActionBar()).hide();

        TextView title = findViewById(R.id.viewTitle);
        TextView content = findViewById(R.id.viewContent);
        TextView author = findViewById(R.id.viewAuthor);
        TextView date = findViewById(R.id.viewAt);

        title.setText(getIntent().getCharSequenceExtra("title"));
        content.setText(getIntent().getCharSequenceExtra("content"));
        author.setText(String.format("%s", getIntent().getCharSequenceExtra("author")));
        date.setText(String.format("%s", getIntent().getCharSequenceExtra("date")));
    }
}
